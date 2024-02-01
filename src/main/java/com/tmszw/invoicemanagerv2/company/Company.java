package com.tmszw.invoicemanagerv2.company;

import com.tmszw.invoicemanagerv2.appuser.AppUser;
import com.tmszw.invoicemanagerv2.invoice.Invoice;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "company")
public class Company {

    @Id
    @SequenceGenerator(
            name = "company_id_seq",
            sequenceName = "company_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "company_id_seq"
    )
    @Column(name = "company_id")
    private Integer companyId;

    @Column(
            name = "company_name",
            nullable = false
    )
    private String companyName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @OneToMany(mappedBy = "company", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private Set<Invoice> invoices = new HashSet<>();

    @Column(name = "accountant_email")
    @Email
    private String accountantEmail;
}
