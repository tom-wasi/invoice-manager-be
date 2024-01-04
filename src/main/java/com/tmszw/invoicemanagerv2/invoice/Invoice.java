package com.tmszw.invoicemanagerv2.invoice;

import com.tmszw.invoicemanagerv2.company.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String invoice_file_id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
