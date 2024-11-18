package com.habitapp.profile_service.domain.facade.imp;

import com.habitapp.profile_service.annotation.Facade;
import com.habitapp.profile_service.domain.exception.person.PersonImageUrlPatternException;
import com.habitapp.profile_service.domain.exception.user.FieldNullException;
import com.habitapp.profile_service.domain.exception.user.IdAccountNotFoundException;
import com.habitapp.profile_service.domain.facade.PersonFacade;
import com.habitapp.profile_service.domain.service.PersonService;
import com.habitapp.profile_service.dto.person.PersonDTO;

import lombok.AllArgsConstructor;

import java.util.List;

@Facade
@AllArgsConstructor
public class PersonFacadeImp implements PersonFacade {
    private PersonService PersonService;

    @Override
    public List<PersonDTO> readAllPerson() {
        return PersonService.readAllPerson();
    }

    @Override
    public boolean createPerson(PersonDTO person) throws IdAccountNotFoundException, PersonImageUrlPatternException {
        return PersonService.createPerson(person);
    }

//    @Override
//    public PersonDTO enterPersonInformation(PersonDTO PersonDTO) throws IdAccountNotFoundException, FieldNullException, PersonNotFoundException, PersonImageUrlPatternException {
//        return PersonService.enterPersonInformation(PersonDTO);
//    }
}
