package com.ddiring.BackEnd_Escrow.enums;

public enum EscrowStatus {
    FAILED(-1),     //거래 실패
    PENDING(0),     //거래 대기
    COMPLETED(1);   //거래 완료

    private final int code;

    EscrowStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static EscrowStatus fromCode(int code) {
        for (EscrowStatus status : EscrowStatus.values()) {
            if (status.code == code) return status;
        }
        throw new IllegalArgumentException("Invalid EscrowStatus code: " + code);
    }
}
