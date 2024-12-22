package com.habitapp.profile_service.domain.entity;

import java.time.LocalDate;

import com.habitapp.profile_service.domain.base.User;
import com.habitapp.profile_service.domain.enumeration.Gender;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Individual extends User {

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate birthdate;

    public Individual(long idAccount, String firstName, String lastName, String email, Gender gender, LocalDate birthdate) {
        super(idAccount, firstName, lastName, email);
        this.gender = gender;
        this.birthdate = birthdate;
    }
}
