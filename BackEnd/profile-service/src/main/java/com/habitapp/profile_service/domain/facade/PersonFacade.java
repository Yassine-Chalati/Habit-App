package com.habitapp.profile_service.domain.facade;

import com.habitapp.profile_service.domain.exception.user.IdAccountNotFoundException;
import com.habitapp.profile_service.domain.exception.person.PersonImageUrlPatternException;


import com.habitapp.profile_service.dto.person.PersonDTO;



import java.util.List;

public interface PersonFacade {
    public boolean createPerson(PersonDTO person) throws IdAccountNotFoundException, PersonImageUrlPatternException;
    public List<PersonDTO> readAllPerson();
//    public PersonDTO enterPersonInformation(PersonDTO PersonDTO) throws IdAccountNotFoundException, FieldNullException, PersonNotFoundException, PersonImageUrlPatternException;

}
