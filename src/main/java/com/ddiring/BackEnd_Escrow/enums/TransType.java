package com.ddiring.BackEnd_Escrow.enums;

public enum TransType {
    REFUND(-1),         //환불
    INVESTMENT(0),      //투자
    TRADE(1),           //거래
    DISTRIBUTED(2);     //분배

    private final int code;

    TransType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static TransType fromCode(int code) {
        for (TransType type : TransType.values()) {
            if (type.code == code) return type;
        }
        throw new IllegalArgumentException("Invalid TransType code: " + code);
    }
}
