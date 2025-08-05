package com.ddiring.BackEnd_Escrow.dto.response;

import com.ddiring.BackEnd_Escrow.entity.Escrow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountResponse {
    private String account;

    public static CreateAccountResponse fromEntity(Escrow escrow) {
        return new CreateAccountResponse(escrow.getAccount());
    }
}