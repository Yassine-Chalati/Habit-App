package com.habitapp.profile_service.domain.service.imp;

import org.springframework.stereotype.Service;

import com.habitapp.profile_service.domain.entity.Individual;
import com.habitapp.profile_service.domain.repository.IndividualRepository;
import com.habitapp.profile_service.domain.service.IndividualService;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndividualServiceImp implements IndividualService {

    @NonNull
    private IndividualRepository individualRepository;

    @Override
    public boolean createIndividual(Individual individual) {
        try {
            individualRepository.save(individual);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Individual readOneIndividual(long id) {
        return individualRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Individual with ID " + id + " not found."));
    }

    @Override
    public Individual updateProfile(Individual updatedProfile) {
        Individual existingProfile = individualRepository.findById(updatedProfile.getIdAccount())
                .orElseThrow(() -> new EntityNotFoundException("Individual with ID " + updatedProfile.getIdAccount() + " not found."));

        existingProfile.setFirstName(updatedProfile.getFirstName());
        existingProfile.setLastName(updatedProfile.getLastName());
        existingProfile.setEmail(updatedProfile.getEmail());
        existingProfile.setBirthdate(updatedProfile.getBirthdate());
        existingProfile.setGender(updatedProfile.getGender());

        return individualRepository.save(existingProfile);
    }

    @Override
    public void deleteProfile(Long id) {
        Individual existingProfile = individualRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Individual with ID " + id + " not found."));

        individualRepository.delete(existingProfile);
    }

}
