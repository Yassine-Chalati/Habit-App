package com.habitapp.profile_service.domain.service;

import com.habitapp.profile_service.domain.exception.person.PersonImageUrlPatternException;
import com.habitapp.profile_service.domain.exception.person.PersonNotFoundException;
import com.habitapp.profile_service.domain.exception.user.FieldNullException;
import com.habitapp.profile_service.domain.exception.user.IdAccountNotFoundException;
import com.habitapp.profile_service.dto.person.PersonDTO;

import java.util.List;

public interface PersonService {

    public boolean createPerson(PersonDTO PersonDTO) throws IdAccountNotFoundException, PersonImageUrlPatternException;
    public List<PersonDTO> readAllPerson();
//    public PersonDTO enterPersonInformation(PersonDTO PersonDTO) throws IdAccountNotFoundException, FieldNullException, PersonNotFoundException, PersonImageUrlPatternException;
}
