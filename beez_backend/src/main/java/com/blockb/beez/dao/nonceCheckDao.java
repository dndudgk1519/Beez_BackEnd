package com.blockb.beez.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface nonceCheckDao {
    public void nonceCheck(String nonceReqId, String usedNonce, String prefix);
    public void nonceStausUpdate(Map<String, String> map);
    public int lastNonce();

}
