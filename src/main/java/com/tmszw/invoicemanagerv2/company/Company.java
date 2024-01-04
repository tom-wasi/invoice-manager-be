package com.tmszw.invoicemanagerv2.company;

import com.tmszw.invoicemanagerv2.appuser.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
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
    private Integer id;

    @Column(
            nullable = false
    )
    private String companyName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser userId;
}
