package com.habitapp.profile_service.controller;

import com.habitapp.profile_service.domain.entity.Individual;
import com.habitapp.profile_service.domain.service.IndividualService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/individuals")
public class IndividualController {

    private IndividualService individualService;

    public IndividualController(IndividualService individualService) {
        this.individualService = individualService;
    }

    @PostMapping
    public ResponseEntity<Void> createIndividual(@RequestBody Individual individual) {
        boolean isCreated = individualService.createIndividual(individual);
        if (isCreated) {
            return ResponseEntity.created(URI.create("/api/individuals/" + individual.getIdAccount())).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Individual> readOneIndividual(@PathVariable long id) {
        Individual individual = individualService.readOneIndividual(id);
        return ResponseEntity.ok(individual);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Individual> updateProfile(
            @PathVariable Long id,
            @RequestBody Individual updatedProfile) {
        updatedProfile.setIdAccount(id);

        Individual updated = individualService.updateProfile(updatedProfile);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        individualService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}
