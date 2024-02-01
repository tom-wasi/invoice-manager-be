package com.tmszw.invoicemanagerv2.mail.confirmation;

import com.tmszw.invoicemanagerv2.appuser.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="token_id")
    private Long tokenId;

    @Column(name="confirmation_token")
    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(targetEntity = AppUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "app_user_id")
    private AppUser appUser;

    public ConfirmationToken(AppUser user) {
        this.appUser = user;
        createdDate = new Date();
        confirmationToken = UUID.randomUUID().toString();
    }
}
