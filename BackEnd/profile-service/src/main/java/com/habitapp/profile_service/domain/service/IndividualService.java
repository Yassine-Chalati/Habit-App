package com.habitapp.profile_service.domain.service;

import com.habitapp.profile_service.domain.entity.Individual;

public interface IndividualService {

    public boolean createIndividual(Individual individual);

    public Individual readOneIndividual(long id);

    public Individual updateProfile(Individual updatedProfile);

    public void deleteProfile(Long id);

}
