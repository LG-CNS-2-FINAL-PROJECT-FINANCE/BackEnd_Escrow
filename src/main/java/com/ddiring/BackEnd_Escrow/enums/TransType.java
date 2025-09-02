package com.ddiring.BackEnd_Escrow.enums;

public enum TransType {
    REFUND(-1),         //환불
    INVESTMENT(0),      //투자(1차 거래 입금)
    TRADEIN(1),         //2차 거래 입금
    TRADEOUT(2),        //2차 거래 출금
    DISTRIBUTEDOUT(3),  //분배금 출금
    DISTRIBUTEIN(4),    //분배금 입금
    CREATORIN(5);       //창작자에게 투자금 입금

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
