package com.menara.authentication.domain.repository;

import com.menara.authentication.domain.entity.DefaultAccountCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultAccountCandidateRepository extends JpaRepository<DefaultAccountCandidate, Long> {
    public DefaultAccountCandidate findDefaultAccountCandidateByEmail(String email);
}
