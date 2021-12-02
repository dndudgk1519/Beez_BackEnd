package com.blockb.beez.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MapDataResponse<T> extends BaseResponse{
    private Map<T,T> data; // 리스트 형태 데이터
}