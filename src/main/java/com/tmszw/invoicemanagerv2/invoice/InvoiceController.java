package com.tmszw.invoicemanagerv2.invoice;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void uploadInvoiceFile(
            @RequestParam("companyId") Integer companyId,
            @RequestParam("file") MultipartFile file,
            @RequestParam String description) {
        invoiceService.uploadInvoiceFile(companyId, file, description);
    }

    @GetMapping(
            value = "/get-invoice",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] getInvoiceFile(
            @RequestParam("invoiceId") Integer invoiceId) {
        return invoiceService.getInvoiceFile(invoiceId);
    }

    @GetMapping("/get-invoices")
    public ResponseEntity<?> getInvoices(@RequestParam("companyId") Integer companyId) {
        List<InvoiceDTO> invoices = invoiceService.getCompanyInvoices(companyId);
        return ResponseEntity.ok(invoices);
    }

    @PutMapping("/update-pending")
    public void setInvoiceStatus(@RequestParam Integer invoiceId) {
        invoiceService.updateInvoiceStatus(invoiceId);
    }

    @DeleteMapping("/delete-invoice/{companyId}")
    public void deleteInvoice(@RequestParam Integer invoiceId, @PathVariable("companyId") Integer companyId) {
        invoiceService.deleteInvoice(invoiceId, companyId);
    }
}