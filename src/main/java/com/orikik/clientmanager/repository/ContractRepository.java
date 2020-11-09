package com.orikik.clientmanager.repository;

import com.orikik.clientmanager.entity.ClientEntity;
import com.orikik.clientmanager.entity.ContractEntity;
import com.orikik.clientmanager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long> {
    Optional<ContractEntity> findByAddendumNumber(Long addendumNumber);

    void deleteAllByUserEntityAndClientEntity(UserEntity userId, ClientEntity client);
}
