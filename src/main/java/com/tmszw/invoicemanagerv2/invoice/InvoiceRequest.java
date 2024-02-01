package com.tmszw.invoicemanagerv2.invoice;

import org.springframework.web.multipart.MultipartFile;

public record InvoiceRequest(
        MultipartFile file,
        String description
) {
}
