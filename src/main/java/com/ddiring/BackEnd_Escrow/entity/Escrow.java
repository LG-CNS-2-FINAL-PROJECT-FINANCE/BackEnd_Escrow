package com.ddiring.BackEnd_Escrow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "escrow")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Escrow {
    //에스크로 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "escrow_seq", nullable = false)
    private Integer escrowSeq;

    //프로젝트 번호
    @Column(name = "project_id", nullable = false)
    private String projectId;

    //에스크로 계좌
    @Column(name = "account", nullable = false, length = 50)
    private String account;

    //생성자
    @Column(name = "created_id", nullable = false, length = 20)
    private String createdId;

    //생성일자
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    //수정자
    @Column(name = "updated_id", nullable = false, length = 20)
    private String updatedId;

    //수정일자
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}