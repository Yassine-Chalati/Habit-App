package com.habitapp.authentication_service.client.profile;

import com.habitapp.authentication_service.configuration.client.IndividualConfiguration;
import com.habitapp.profile_service.domain.entity.Individual;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "profile-service", contextId = "individual", configuration = IndividualConfiguration.class)
public interface IndividualClient {
    @PostMapping("/api/individuals")
    ResponseEntity<Void> createIndividual(@RequestBody Individual individual);

    @GetMapping("/api/individuals/{id}")
    ResponseEntity<Individual> readOneIndividual(@PathVariable("id") long id);

    @PutMapping("/api/individuals/{id}")
    ResponseEntity<Individual> updateProfile(
            @PathVariable("id") Long id,
            @RequestBody Individual updatedProfile);

    @DeleteMapping("/api/individuals/{id}")
    ResponseEntity<Void> deleteProfile(@PathVariable("id") Long id);
}
