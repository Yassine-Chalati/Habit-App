package com.habitapp.authentication_service.client.profile;

import com.habitapp.authentication_service.configuration.client.IndividualConfiguration;
import com.habitapp.common.http.request_response.individual.IndividualRequestResponseHttp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "profile-service", configuration = IndividualConfiguration.class)
public interface IndividualClient {
    @PostMapping("/api/individuals")
    ResponseEntity<Void> createIndividual(@RequestBody IndividualRequestResponseHttp individual);

    @GetMapping("/api/individuals/{id}")
    ResponseEntity<IndividualRequestResponseHttp> readOneIndividual(@PathVariable("id") long id);

    @PutMapping("/api/individuals/{id}")
    ResponseEntity<IndividualRequestResponseHttp> updateProfile(
            @PathVariable("id") Long id,
            @RequestBody IndividualRequestResponseHttp updatedProfile);

    @DeleteMapping("/api/individuals/{id}")
    ResponseEntity<Void> deleteProfile(@PathVariable("id") Long id);
}
