package com.ddiring.BackEnd_Escrow.repository;

import com.ddiring.BackEnd_Escrow.entity.Escrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EscrowRepository extends JpaRepository<Escrow, Integer> {
    Optional<Escrow> findByEscrowSeq(Integer escrowSeq);
    boolean existsByEscrowSeq(Integer escrowSeq);
    boolean existsByAccount(String account);
}
