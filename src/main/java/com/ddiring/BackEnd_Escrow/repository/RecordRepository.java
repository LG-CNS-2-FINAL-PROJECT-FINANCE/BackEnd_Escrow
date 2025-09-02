package com.ddiring.BackEnd_Escrow.repository;

import com.ddiring.BackEnd_Escrow.entity.Record;
import com.ddiring.BackEnd_Escrow.entity.Escrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {

    // 전체 거래 내역 조회
    List<Record> findAllByEscrow(Escrow escrow);

    // 전체 누적 잔액 계산
    @Query("""
        SELECT COALESCE(SUM(
            CASE WHEN r.flow = 1 THEN r.amount ELSE -r.amount END
        ), 0)
        FROM Record r
        WHERE r.escrow.escrowSeq = :escrowSeq
          AND r.escrowStatus = com.ddiring.BackEnd_Escrow.enums.EscrowStatus.COMPLETED
    """)
    Integer findBalanceByEscrowSeq(@Param("escrowSeq") Integer escrowSeq);

    // 최신 거래 기준 잔액 조회 (Top 1)
    Record findTopByEscrow_EscrowSeqOrderByCompletedAtDesc(Integer escrowSeq);

    // 분배금 입금 여부 확인
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Record r " +
            "WHERE r.escrow.account = :account " +
            "AND r.userSeq = :userSeq " +
            "AND r.amount = :distributionAmount " +
            "AND r.transType = com.ddiring.BackEnd_Escrow.enums.TransType.DISTRIBUTEIN")
    Boolean existsDistributedIncome(@Param("account") String account,
                                    @Param("userSeq") String userSeq,
                                    @Param("distributionAmount") Integer distributionAmount);
}