package com.blockb.beez.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalDto {
    private String email;
    private String withdrawal;
    private String address;
}
