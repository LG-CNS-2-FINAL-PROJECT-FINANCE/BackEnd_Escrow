package com.ddiring.BackEnd_Escrow.entity;

import com.ddiring.BackEnd_Escrow.converter.EscrowStatusConverter;
import com.ddiring.BackEnd_Escrow.converter.TransTypeConverter;
import com.ddiring.BackEnd_Escrow.enums.EscrowStatus;
import com.ddiring.BackEnd_Escrow.enums.TransType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Record {
    //기록번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_seq", nullable = false)
    private Integer recordSeq;

    //에스크로 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escrow_seq", nullable = false)
    private Escrow escrow;

    //사용자 번호
    @Column(name = "user_seq", nullable = false)
    private String userSeq;

    //주문 번호
    @Column(name = "trans_seq")
    private Integer transSeq;

    //주문 종류
    @Convert(converter = TransTypeConverter.class)
    @Column(name = "trans_type")
    private TransType transType;

    //금액
    @Column(name = "amount", nullable = false)
    private Integer amount;

    //입출금 out = 0, in = 1
    @Column(name = "flow", nullable = false)
    private Integer flow;

    //에스크로 상태
    @Convert(converter = EscrowStatusConverter.class)
    @Column(name = "escrow_status", nullable = false)
    private EscrowStatus escrowStatus;

    //주문발생시간
    @Column(name = "initiated_at", nullable = false)
    private LocalDateTime initiatedAt;

    //주문완료시간
    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;

    //생성자
    @Column(name = "created_id", nullable = false)
    private String createdId;

    //생성일자
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    //수정자
    @Column(name = "updated_id", nullable = false)
    private String updatedId;

    //수정일자
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
