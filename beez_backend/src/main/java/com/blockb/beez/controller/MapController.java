package com.blockb.beez.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import com.blockb.beez.dto.MapDto;
import com.blockb.beez.service.MapService;



@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class MapController {

    @Autowired
    MapService mapservice;     
        
    @PostMapping("/StoreList")
    public List<MapDto> getStoreList(@RequestBody MapDto inputMapDto ) {

        List<MapDto> mapdto = null;
 
        try{ 
            // System.out.println(mapservice.getStoreList( inputMapDto.getLat(),inputMapDto.getLon()).size());

            return mapservice.getStoreList( inputMapDto.getLat(),inputMapDto.getLon());

    }catch(Exception error){
        error.printStackTrace();
        return mapdto ;
    } 
}
}
