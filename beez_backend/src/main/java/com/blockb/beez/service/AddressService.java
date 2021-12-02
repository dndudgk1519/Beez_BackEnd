package com.blockb.beez.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.blockb.beez.dao.AddressDao;
import com.blockb.beez.dto.AddressDto;
import com.blockb.beez.dto.AddressListDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    @Autowired
    AddressDao addressDao;
    
    public List<String> findAddress(List<AddressListDto> address){
        
        List<String> list = new ArrayList<String>();
        for(int i = 0; i<address.size(); i++){
            String tmp = addressDao.findAddress(address.get(i).getAddress());
            list.add(tmp);
            
        }
        return list;
    }
}
