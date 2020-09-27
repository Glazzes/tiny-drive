package com.tiny.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "verification_token")
@Data
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "token", nullable = false, length = 100)
    private String token;

    @Column(name = "expire_date", nullable = false, length = 100)
    private Timestamp expireDate;

    @OneToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public VerificationToken(String token, User user){
        this.token = token;
        this.user = user;
    }

}
