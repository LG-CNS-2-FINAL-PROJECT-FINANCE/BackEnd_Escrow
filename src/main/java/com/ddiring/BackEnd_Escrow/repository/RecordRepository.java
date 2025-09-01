package com.ddiring.BackEnd_Escrow.repository;

import com.ddiring.BackEnd_Escrow.entity.Escrow;
import com.ddiring.BackEnd_Escrow.entity.Record;
import com.ddiring.BackEnd_Escrow.enums.TransType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {
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
    Integer findBalanceByEscrowSeq(@Param("escrowSeq") Integer escrowSeq);

    @Query("SELECT COALESCE(r.balance, 0) FROM Record r WHERE r.escrow.escrowSeq = :escrowSeq ORDER BY r.completedAt DESC")
    Integer findLatestBalanceByEscrowSeq(@Param("escrowSeq") Integer escrowSeq);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Record r WHERE r.escrow.account = :account " +
            "AND r.userSeq = :userSeq " +
            "AND r.amount = :distributionAmount " +
            "AND r.transType = com.ddiring.BackEnd_Escrow.enums.TransType.DISTRIBUTEIN")
    Boolean existsDistributedIncome(@Param("account") String account,
                                    @Param("userSeq") String userSeq,
                                    @Param("distributionAmount") Integer distributionAmount);

}