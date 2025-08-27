package com.ddiring.BackEnd_Escrow.repository;

import com.ddiring.BackEnd_Escrow.entity.Escrow;
import com.ddiring.BackEnd_Escrow.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {

    //Escrow 엔티티 내부의 escrowSeq로 조회
    List<Record> findAllByEscrow_EscrowSeq(Integer escrowSeq);

    //또는 Escrow 객체 자체로도 조회 가능
    List<Record> findAllByEscrow(Escrow escrow);

    @Query("""
        SELECT COALESCE(SUM(
            CASE WHEN r.flow = 1 THEN r.amount ELSE -r.amount END
        ), 0)
        FROM Record r
        WHERE r.escrow.escrowSeq = :escrowSeq
          AND r.escrowStatus = com.ddiring.BackEnd_Escrow.enums.EscrowStatus.COMPLETED
    """)
    BigDecimal findBalanceByEscrowSeq(@Param("escrowSeq") Integer escrowSeq);
}