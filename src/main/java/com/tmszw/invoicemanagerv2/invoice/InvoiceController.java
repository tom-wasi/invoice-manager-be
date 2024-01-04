package com.tmszw.invoicemanagerv2.invoice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping(
            value = "{companyId}/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void uploadInvoiceFile(
            @PathVariable("companyId") Integer companyId,
            @RequestParam("file") MultipartFile file) {
        invoiceService.uploadInvoiceFile(companyId, file);
    }

    @GetMapping(
            value = "{invoiceId}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] getInvoiceFile(
            @PathVariable("invoiceId") Integer invoiceId) {
        return invoiceService.getInvoiceFile(invoiceId);
    }

    @DeleteMapping("{invoiceId}")
    public void deleteInvoice(@PathVariable("invoiceId") Integer invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
    }

}