package com.blockb.beez.dao;

import java.util.List;
import java.util.Map;

import com.blockb.beez.dto.WithdrawalHistoryDto;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WithdrawalDao {
    // public void exchangeHistory(Map<String, String> map);
    public void withdrawalHistory(Map<String, String> map);
    public List<WithdrawalHistoryDto> withdrawHistoryList(Long userId, String startDate, String endDate);
}
