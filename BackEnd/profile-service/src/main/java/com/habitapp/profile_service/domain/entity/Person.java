package com.habitapp.profile_service.domain.entity;

import com.habitapp.profile_service.domain.base.User;
import com.habitapp.profile_service.domain.enumeration.Gender;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Person extends User {
    private Gender gender;
    private String imageUrl;

    public Person(long idAccount, String firstName, String lastName, String email, Gender gender, String imageUrl) {
        super(idAccount, firstName, lastName, email);
        this.gender = gender;
        this.imageUrl = imageUrl;
    }
}
