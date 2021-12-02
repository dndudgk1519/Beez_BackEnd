package com.blockb.beez.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.blockb.beez.service.AddressService;
import com.blockb.beez.dto.AddressDto;
import com.blockb.beez.dto.AddressListDto;
import com.blockb.beez.dto.response.BaseResponse;
import com.blockb.beez.dto.response.SingleDataResponse;
import com.blockb.beez.exception.DuplicatedUsernameException;
import com.blockb.beez.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class AddressController {
    
    @Autowired
    AddressService addressService;
    @Autowired
    ResponseService responseService;

    //유저 History
    @PostMapping("/find/address")
    public ResponseEntity findAddress(@RequestBody AddressDto address) throws IOException, ExecutionException, InterruptedException {
        ResponseEntity responseEntity = null;
        try {
            //List<HistoryDto> historyList = historyService.historyList(userId);
            List<String> findAddress = addressService.findAddress(address.getWalletAddress());

            SingleDataResponse<List<String>> response = responseService.getSingleDataResponse(true, "충전List 출력 성공", findAddress);

            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (DuplicatedUsernameException exception) {
            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());

            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return responseEntity;
    }
}