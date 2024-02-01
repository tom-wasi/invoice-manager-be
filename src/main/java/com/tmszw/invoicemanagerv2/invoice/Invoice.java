package com.tmszw.invoicemanagerv2.invoice;

import com.tmszw.invoicemanagerv2.company.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table( name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(
            name = "invoice_file_id",
            nullable = false
    )
    private String invoice_file_id;

    @Column(
            name = "invoice_description",
            nullable = true
    )
    private String description;

    @ManyToOne
    @JoinColumn(
            name = "company_id",
            nullable = false
    )
    private Company company;

    @Column(
            name = "uploaded",
            nullable = false
    )
    private LocalDate localDate;

    @Column(
            name = "is_pending"
    )
    private boolean isPending;
}