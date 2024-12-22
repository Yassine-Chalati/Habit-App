package com.habitapp.authentication_service.proxy.client.profile;

import com.habitapp.authentication_service.annotation.Proxy;
import com.habitapp.authentication_service.client.profile.IndividualClient;
import com.habitapp.authentication_service.proxy.exception.common.UnauthorizedException;
import com.habitapp.authentication_service.proxy.exception.common.UnexpectedException;
import com.habitapp.common.http.request_response.individual.IndividualRequestResponseHttp;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

@Proxy
@AllArgsConstructor
public class IndividualServiceProxy {
    private final IndividualClient individualClient;
    

    public void createIndividual(IndividualRequestResponseHttp individual) throws UnauthorizedException, UnexpectedException {
        try {
            individualClient.createIndividual(individual);
        } catch (FeignException e) {
            handleFeignException(e, "create individual");
        }
    }

    public IndividualRequestResponseHttp readOneIndividual(Long id) throws UnauthorizedException, UnexpectedException {
        try {
            ResponseEntity<IndividualRequestResponseHttp> response = individualClient.readOneIndividual(id);
            return response.getBody();
        } catch (FeignException e) {
            handleFeignException(e, "read individual with id: " + id);
            return null; // Unreachable but required for compilation
        }
    }

    public IndividualRequestResponseHttp updateIndividual(Long id, IndividualRequestResponseHttp updatedIndividual) throws UnauthorizedException, UnexpectedException {
        try {
            ResponseEntity<IndividualRequestResponseHttp> response = individualClient.updateProfile(id, updatedIndividual);
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
