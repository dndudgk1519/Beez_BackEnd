package com.blockb.beez.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDto {
    private String userId;
    private String chargeAmount;
    private String chargeInc;
    private String txHash;
    private String chargeDate;
    private String startDate;
    private String endDate;
}