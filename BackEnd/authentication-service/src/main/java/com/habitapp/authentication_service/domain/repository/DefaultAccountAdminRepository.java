package com.menara.authentication.domain.repository;

import com.menara.authentication.domain.entity.DefaultAccountAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DefaultAccountAdminRepository extends JpaRepository<DefaultAccountAdmin, Long> {
    public DefaultAccountAdmin findDefaultAccountAdminByEmail(String email);
    public List<DefaultAccountAdmin> findAllByIdIn(List<Long> id);
    public List<DefaultAccountAdmin> findAllBySuspended(boolean suspended);
    public void deleteAllByIdIn(List<Long> ids);
}
