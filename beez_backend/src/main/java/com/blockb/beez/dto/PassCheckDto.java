package com.blockb.beez.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassCheckDto {
    private Long userId;
    private String passwordCheck;
    private String failCount;
}
