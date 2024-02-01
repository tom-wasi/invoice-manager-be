package com.tmszw.invoicemanagerv2.invoice;

import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.tmszw.invoicemanagerv2.company.CompanyDTO;
import com.tmszw.invoicemanagerv2.company.CompanyService;
import com.tmszw.invoicemanagerv2.s3.S3Buckets;
import com.tmszw.invoicemanagerv2.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final S3Service s3Service;
    private final S3Buckets s3Buckets;
    private final InvoiceRepository invoiceRepository;
    private final CompanyService companyService;
    private final InvoiceDTOMapper invoiceDTOMapper;

    public void uploadInvoiceFile(Integer companyId,
                                  MultipartFile file,
                                  String description) {

        checkIfCompanyExistsOrThrow(companyId);
        String fileId = UUID.randomUUID().toString();
        Invoice invoice = new Invoice();

        try {
            System.out.println("Before uploading file to S3");
            s3Service.putObject(
                    s3Buckets.getInvoice(),
                    "invoice-files/%s/%s".formatted(companyId, fileId),
                    file.getBytes()
            );
            System.out.println("File uploaded successfully to S3");
        } catch (IOException e) {
            System.err.println("Failed to upload invoice: " + e.getMessage());
            throw new RuntimeException("Failed to upload invoice");
        }

        invoice.setCompany(companyService.getCompanyById(companyId));
        invoice.setDescription(description);
        invoice.setInvoice_file_id(fileId);
        invoice.setPending(true);
        invoice.setLocalDate(LocalDate.now());
        invoiceRepository.save(invoice);
    }

    public byte[] getInvoiceFile(Integer invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Invoice with id [%s] not found".formatted(invoiceId)
                ));

        return s3Service.getObject(
                s3Buckets.getInvoice(),
                "invoice-files/%s/%s".formatted(invoice.getCompany().getCompanyId(), invoice.getInvoice_file_id())
        );
    }

    public void updateInvoiceStatus(Integer invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Invoice with id [%s] not found".formatted(invoiceId)
                ));

        invoice.setPending(false);
        invoiceRepository.save(invoice);
    }

    public void deleteInvoice(Integer invoiceId, Integer companyId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow();

        invoiceRepository.deleteById(invoiceId);
        try {
            s3Service.deleteObject(
                    s3Buckets.getInvoice(),
                    "invoice-files/%s/%s".formatted(companyId, invoice.getInvoice_file_id())
            );

        } catch (Exception e) {
            throw new ResourceNotFoundException("Invoice not present");
        }
    }

    private void checkIfCompanyExistsOrThrow(Integer companyId) {
        if (!companyService.existsById(companyId)) {
            throw new ResourceNotFoundException(
                    "Company with id [%s] not found".formatted(companyId)
            );
        }
    }

    public List<InvoiceDTO> getCompanyInvoices(Integer companyId) {

        List<Invoice> invoices = invoiceRepository.findAllByCompanyId(companyId);
        List<InvoiceDTO> invoiceDTOS = new ArrayList<>();
        for(Invoice c : invoices) {
            invoiceDTOS.add(invoiceDTOMapper.apply(c));
        }
        return invoiceDTOS;
    }
}
