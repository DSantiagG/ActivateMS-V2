package com.activate.ActivateMSV1.user_management_ms.infra.repository;

import com.activate.ActivateMSV1.user_management_ms.infra.repository.model.Credentials;
import com.activate.ActivateMSV1.user_management_ms.infra.repository.model.ModUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialsRepository extends JpaRepository<Credentials,Long> {

    
    Optional<Credentials> findByUsername(String username);
}
