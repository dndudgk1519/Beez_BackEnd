package com.blockb.beez.dao;

import java.util.List;

import com.blockb.beez.dto.MapDto;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface MapDao {

 public List<MapDto> getStoreList(double mylat, double mylon);


}
