package com.habitapp.profile_service.domain.service.imp;

import com.habitapp.profile_service.common.constant.RegexPatternConstants;
import com.habitapp.profile_service.common.utlil.regex.RegexPatternValidatorUtil;
import com.habitapp.profile_service.domain.entity.Person;
import com.habitapp.profile_service.domain.exception.person.PersonImageUrlPatternException;
import com.habitapp.profile_service.domain.exception.user.IdAccountNotFoundException;
import com.habitapp.profile_service.domain.repository.PersonRepository;
import com.habitapp.profile_service.domain.service.PersonService;
import com.habitapp.profile_service.dto.person.PersonDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImp implements PersonService {
    @NonNull
    private PersonRepository PersonRepository;
    @NonNull
    private RegexPatternValidatorUtil regexPatternValidatorUtil;

    @Override
    public boolean createPerson(PersonDTO PersonDTO) throws IdAccountNotFoundException, PersonImageUrlPatternException {
        Person Person;

        if (PersonDTO.getIdAccount() == null
            || PersonDTO.getIdAccount() == 0){
            throw new IdAccountNotFoundException("Id Person not found exception");
        }

        if (PersonDTO.getImageUrl() != null
                && !PersonDTO.getImageUrl().isBlank()
                && !PersonDTO.getImageUrl().isEmpty()
                && !regexPatternValidatorUtil.validateStringPattern(PersonDTO.getImageUrl(), RegexPatternConstants.URL_REGEX_PATTERN)){
            throw new PersonImageUrlPatternException("the url of image is not valid exception");
        }

        Person = new Person(PersonDTO.getIdAccount(),
                PersonDTO.getFirstName(),
                PersonDTO.getLastName(),
                PersonDTO.getEmail(),
                PersonDTO.getGender(),
                PersonDTO.getImageUrl());

        PersonRepository.save(Person);

        return Person.getIdAccount() != 0;
    }

    @Override
    public List<PersonDTO> readAllPerson() {
        List<PersonDTO> PersonList = new ArrayList<>();
        for (Person Person : PersonRepository.findAll()){
            PersonList.add(new PersonDTO(Person.getIdAccount(),
                    Person.getFirstName(),
                    Person.getLastName(),
                    Person.getEmail(),
                    Person.getImageUrl(),
                    Person.getGender()));
        }

        return PersonList;
    }

    // the id Person must be the same id from accessToken
    /*@Override
    public PersonDTO enterPersonInformation(PersonDTO PersonDTO) throws IdAccountNotFoundException, FieldNullException, PersonNotFoundException, PersonImageUrlPatternException {
        Person Person = null;
        if (PersonDTO.getIdAccount() == null
                || PersonDTO.getIdAccount() == 0){
            throw new IdAccountNotFoundException("Id Person not found exception");
        }

        Person = PersonRepository.findByIdAccount(PersonDTO.getIdAccount());

        if (Person == null || Person.getIdAccount() == 0){
            throw new PersonNotFoundException("Person not found exception");
        }

        if (PersonDTO.getFirstName() == null
                || PersonDTO.getFirstName().isEmpty()
                || PersonDTO.getFirstName().isBlank()){
            throw new FieldNullException("the firstname is null");
        }

        if (PersonDTO.getLastName() == null
                || PersonDTO.getLastName().isEmpty()
                || PersonDTO.getLastName().isBlank()){
            throw new FieldNullException("the firstname is null");
        }

        if (PersonDTO.getGender() == null){
            throw new FieldNullException("the firstname is null");
        }

        if (PersonDTO.getImageUrl() == null
                || PersonDTO.getImageUrl().isEmpty()
                || PersonDTO.getImageUrl().isBlank()){
            throw new FieldNullException("the firstname is null");
        }

        if (!regexPatternValidatorUtil.validateStringPattern(PersonDTO.getImageUrl(), RegexPatternConstants.URL_REGEX_PATTERN)){
            throw new PersonImageUrlPatternException("the url of image is not valid exception");
        }

        Person.setFirstName(PersonDTO.getFirstName());
        Person.setLastName(PersonDTO.getLastName());
        Person.setGender(PersonDTO.getGender());
        Person.setImageUrl(PersonDTO.getImageUrl());

        Person = PersonRepository.save(Person);

        return new PersonDTO(Person.getIdAccount(),
                Person.getFirstName(),
                Person.getLastName(),
                Person.getEmail(),
                Person.getImageUrl(),
                Person.getGender());
    }*/
}
