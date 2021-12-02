package com.blockb.beez.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto {
    private String sender;
    private String recipient;
    private String amount;
}