package com.habitapp.profile_service.controller;

import com.habitapp.common.http.request.person.PersonRequestHttp;
import com.habitapp.common.http.request_response.person.PersonRequestResponseHttp;
import com.habitapp.common.http.request_response.person.PersonRequestResponseHttpList;
import com.habitapp.profile_service.domain.enumeration.Gender;
import com.habitapp.profile_service.domain.exception.person.PersonImageUrlPatternException;
import com.habitapp.profile_service.domain.exception.user.IdAccountNotFoundException;
import com.habitapp.profile_service.domain.facade.PersonFacade;
import com.habitapp.profile_service.dto.person.PersonDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user/person")
@RequiredArgsConstructor
public class PersonController {
    @NonNull
    public PersonFacade personFacade;

    @PostMapping("/create")
    @PreAuthorize("hasRole(T(com.habitapp.common.common.account.RoleNameCommonConstants).AUTHENTICATION_SERVER)")
    public ResponseEntity<?> createCandidate(@RequestBody PersonRequestHttp personRequestHttp){
        HttpHeaders headers = new HttpHeaders();
        System.out.println(personRequestHttp.getIdAccount());
        PersonDTO personDTO = new PersonDTO(personRequestHttp.getIdAccount());

//        personDTO.setIdAccount(personRequestHttp.getIdAccount());
        personDTO.setFirstName(personRequestHttp.getFirstName());
        personDTO.setLastName(personRequestHttp.getLastName());
        personDTO.setEmail(personRequestHttp.getEmail());
        personDTO.setImageUrl(personRequestHttp.getImageUrl());
        try {
            personDTO.setGender(Gender.valueOf(personRequestHttp.getGender()));
        } catch (IllegalArgumentException | NullPointerException e){
            personDTO.setGender(null);
        }

        try {
            if (personFacade.createPerson(personDTO)){
                return new ResponseEntity<>(headers, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (IdAccountNotFoundException e) {
            return new ResponseEntity<>("Id not found exception", headers, HttpStatus.BAD_REQUEST);
        } catch (PersonImageUrlPatternException e) {
            //return new ResponseEntity<>("Image url not valid", headers, HttpStatus.BAD_REQUEST);
            personDTO.setImageUrl(null);
            try {
                if (personFacade.createPerson(personDTO)){
                    return new ResponseEntity<>(headers, HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (IdAccountNotFoundException ex) {
                return new ResponseEntity<>("Id not found exception", headers, HttpStatus.BAD_REQUEST);
            } catch (PersonImageUrlPatternException ex) {
                return new ResponseEntity<>("Image url not valid", headers, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @GetMapping("/read/all")
    @PreAuthorize("hasRole(T(com.habitapp.common.common.account.RoleNameCommonConstants).AUTHENTICATION_SERVER)")
    public ResponseEntity<PersonRequestResponseHttpList> readAllPerson() {
        List<PersonDTO> personList =  personFacade.readAllPerson();
        PersonRequestResponseHttpList personRequestResponseHttpList = new PersonRequestResponseHttpList(new ArrayList<>());

        for (PersonDTO person : personList){
            personRequestResponseHttpList.getPersonRequestResponseHttpList().add(new PersonRequestResponseHttp(person.getIdAccount(), person.getFirstName(), person.getLastName(), person.getEmail(), person.getImageUrl(), person.getGender() != null? person.getGender().toString() : null));
        }

        return new ResponseEntity<PersonRequestResponseHttpList>(personRequestResponseHttpList, new HttpHeaders(), HttpStatus.OK);
    }
}
