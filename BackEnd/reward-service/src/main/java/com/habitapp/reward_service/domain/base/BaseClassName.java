package com.habitapp.reward_service.domain.base;

import java.time.LocalDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class BaseClassName {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountSeq")
    @SequenceGenerator(name = "accountSeq", initialValue = 1)
    private long id;
    private String email;
    private LocalDateTime creationDate;
//    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
//    @JoinColumn(name = "idAccount", referencedColumnName = "id")
//    private List<AccountAuthentication> accountAuthentications;
    // @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    // private List<AccountJsonWebToken> accountJsonWebTokens;
    // @OneToOne //(orphanRemoval = true, cascade = CascadeType.ALL)
//    @JoinColumn(name = "id", referencedColumnName = "idAccount")
    // private AccountAuthentication accountAuthentication;
    // @ManyToMany
    // private List<Role> roles;
    // @ManyToMany
    // private List<Permission> permissions;

    // public Account(String email, LocalDateTime creationDate, List<Role> roles, List<Permission> permissions){
    //     this.email = email;
    //     this.creationDate = creationDate;
    //     this.roles = roles;
    //     this.permissions = permissions;
    // }

    // public Account(long id, String email, LocalDateTime creationDate){
    //     this.email = email;
    //     this.creationDate = creationDate;
    //     this.roles = null;
    //     this.permissions = null;
    //     this.accountAuthentication = null;
    // }
}
