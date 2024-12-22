package com.habitapp.authentication_service.proxy.client.profile;

import com.habitapp.authentication_service.annotation.Proxy;

import com.habitapp.authentication_service.client.profile.IndividualClient;
import com.habitapp.authentication_service.proxy.exception.common.UnauthorizedException;
import com.habitapp.authentication_service.proxy.exception.common.UnexpectedException;
import com.habitapp.profile_service.domain.entity.Individual;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

@Proxy
@AllArgsConstructor
public class IndividualServiceProxy {
    private final IndividualClient individualClient;
    

    public void createIndividual(Individual individual) throws UnauthorizedException, UnexpectedException {
        try {
            individualClient.createIndividual(individual);
        } catch (FeignException e) {
            handleFeignException(e, "create individual");
        }
    }

    public Individual readOneIndividual(Long id) throws UnauthorizedException, UnexpectedException {
        try {
            ResponseEntity<Individual> response = individualClient.readOneIndividual(id);
            return response.getBody();
        } catch (FeignException e) {
            handleFeignException(e, "read individual with id: " + id);
            return null; // Unreachable but required for compilation
        }
    }

    public Individual updateIndividual(Long id, Individual updatedIndividual) throws UnauthorizedException, UnexpectedException {
        try {
            ResponseEntity<Individual> response = individualClient.updateProfile(id, updatedIndividual);
            return response.getBody();
        } catch (FeignException e) {
            handleFeignException(e, "update individual with id: " + id);
            return null; // Unreachable but required for compilation
        }
    }

    public void deleteIndividual(Long id) throws UnauthorizedException, UnexpectedException {
        try {
            individualClient.deleteProfile(id);
        } catch (FeignException e) {
            handleFeignException(e, "delete individual with id: " + id);
        }
    }

    private void handleFeignException(FeignException e, String action) throws UnauthorizedException, UnexpectedException {
        if (e.status() == 401) {
            throw new UnauthorizedException("Unauthorized to " + action);
        }
        throw new UnexpectedException("Unexpected error during " + action + " - HTTP status: " + e.status());
    }
}
