package com.ddiring.BackEnd_Escrow.repository;

import com.ddiring.BackEnd_Escrow.entity.Escrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EscrowRepository extends JpaRepository<Escrow, Integer> {

    Optional<Escrow> findByAccount(String account);

    Optional<Escrow> findByProjectId(String projectId);

    boolean existsByAccount(String account);

    boolean existsByProjectId(String projectId);

    // escrowSeq만 조회할 때
    @Query("SELECT e.escrowSeq FROM Escrow e WHERE e.projectId = :projectId")
    Optional<Integer> findEscrowSeqByProjectId(@Param("projectId") String projectId);

    @Query("SELECT e.escrowSeq FROM Escrow e WHERE e.account = :account")
    Optional<Integer> findEscrowSeqByAccount(@Param("account") String account);
}