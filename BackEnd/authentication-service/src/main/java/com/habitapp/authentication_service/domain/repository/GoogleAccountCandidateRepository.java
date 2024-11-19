package com.menara.authentication.domain.repository;

import com.menara.authentication.domain.entity.GoogleAccountCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoogleAccountCandidateRepository extends JpaRepository<GoogleAccountCandidate, Long> {
    public GoogleAccountCandidate findGoogleAccountCandidateByEmail(String email);
}
