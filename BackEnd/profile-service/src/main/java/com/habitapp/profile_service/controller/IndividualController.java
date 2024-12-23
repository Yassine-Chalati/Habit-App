package com.habitapp.profile_service.controller;

import com.habitapp.common.http.request_response.individual.IndividualRequestResponseHttp;
import com.habitapp.profile_service.domain.entity.Individual;
import com.habitapp.profile_service.domain.enumeration.Gender;
import com.habitapp.profile_service.domain.service.IndividualService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/individuals")
public class IndividualController {

    private final IndividualService individualService;

    public IndividualController(IndividualService individualService) {
        this.individualService = individualService;
    }

    @PostMapping
    public ResponseEntity<Void> createIndividual(@RequestBody IndividualRequestResponseHttp individual) {
        boolean isCreated = individualService.createIndividual(mapIndividualRequestResponseHttpToIndividual(individual));
        if (isCreated) {
            return ResponseEntity.created(URI.create("/api/individuals/" + individual.getIdAccount())).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<IndividualRequestResponseHttp> readOneIndividual(@PathVariable long id) {
        IndividualRequestResponseHttp individual = mapIndividualToIndividualRequestResponseHttp(individualService.readOneIndividual(id));
        return ResponseEntity.ok(individual);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IndividualRequestResponseHttp> updateProfile(
            @PathVariable Long id,
            @RequestBody IndividualRequestResponseHttp updatedProfile) {
        updatedProfile.setIdAccount(id);

        IndividualRequestResponseHttp updated = mapIndividualToIndividualRequestResponseHttp(individualService.updateProfile(mapIndividualRequestResponseHttpToIndividual(updatedProfile)));
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        individualService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }

    private Individual mapIndividualRequestResponseHttpToIndividual(IndividualRequestResponseHttp individualRequestResponseHttp) {
        Individual individual = new Individual();
        individual.setIdAccount(individualRequestResponseHttp.getIdAccount());
        individual.setFirstName(individualRequestResponseHttp.getFirstName());
        individual.setLastName(individualRequestResponseHttp.getLastName());
        individual.setEmail(individualRequestResponseHttp.getEmail());
        individual.setBirthdate(individualRequestResponseHttp.getBirthdate());
        individual.setGender((individualRequestResponseHttp.getGender().equals(Gender.Female.name()) ? Gender.Female : Gender.Mal));

        return individual;
    }

    private IndividualRequestResponseHttp mapIndividualToIndividualRequestResponseHttp(Individual individual) {
        IndividualRequestResponseHttp individualRequestResponseHttp = new IndividualRequestResponseHttp();
        individualRequestResponseHttp.setIdAccount(individual.getIdAccount());
        individualRequestResponseHttp.setFirstName(individual.getFirstName());
        individualRequestResponseHttp.setLastName(individual.getLastName());
        individualRequestResponseHttp.setEmail(individual.getEmail());
        individualRequestResponseHttp.setBirthdate(individual.getBirthdate());
        individualRequestResponseHttp.setGender(individual.getGender().name());

        return individualRequestResponseHttp;
    }

}
