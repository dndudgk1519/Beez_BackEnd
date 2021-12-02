package com.blockb.beez.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.blockb.beez.dao.MapDao;
import com.blockb.beez.dto.MapDto;


@Service
public class MapService {
    @Autowired
    MapDao mapdao;
    
    public List<MapDto> getStoreList(double mylat, double  mylon){
        return mapdao.getStoreList( mylat,  mylon);   
       
}
}
