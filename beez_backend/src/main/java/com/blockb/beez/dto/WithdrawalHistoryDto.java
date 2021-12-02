package com.blockb.beez.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalHistoryDto {
    private String userId;
    private String amount;
    private String txHash;
    private String withdrawDate;
    private String startDate;
    private String endDate;
}