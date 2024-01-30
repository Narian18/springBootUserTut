package com.example.demo.registration.token;

import com.example.demo.appUser.AppUser;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class ConfirmationToken {

    @Id
    @SequenceGenerator(
            name = "token_sequence",
            sequenceName = "token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "token_sequence"
    )
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @Column(nullable = false)
    private LocalDateTime expiryDate;
    private LocalDateTime confirmDate;
    @ManyToOne
    // equivalent to a SQLAlchemy relationship, I believe - defining fk name as `name`
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;

    public ConfirmationToken(String token,
                             LocalDateTime createdDate,
                             LocalDateTime expiryDate,
                             AppUser appUser) {
        this.token = token;
        this.createdDate = createdDate;
        this.expiryDate = expiryDate;
        this.appUser = appUser;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setConfirmDate(LocalDateTime confirmDate) {
        this.confirmDate = confirmDate;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public LocalDateTime getConfirmDate() {
        return confirmDate;
    }

    public AppUser getAppUser() {
        return appUser;
    }
}
