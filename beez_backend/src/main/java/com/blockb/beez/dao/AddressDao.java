package com.blockb.beez.dao;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AddressDao {
    public String findAddress(String address);
}
