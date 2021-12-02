package com.blockb.beez.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.blockb.beez.dto.ChargeDto;
import com.blockb.beez.dto.HistoryDto;
import com.blockb.beez.dto.UserDto;
import com.blockb.beez.service.ChargeService;
import com.blockb.beez.dto.response.BaseResponse;
import com.blockb.beez.dto.response.SingleDataResponse;
import com.blockb.beez.exception.DuplicatedUsernameException;
import com.blockb.beez.exception.UserNotFoundException;
import com.blockb.beez.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.exceptions.TransactionException;



@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class ChargeController {
    @Autowired
    ChargeService chargeService;
    @Autowired
    ResponseService responseService;
    //유저 토큰 충전
    @PostMapping("/charge/amount")
    public ResponseEntity charge(final Authentication authentication, @RequestBody ChargeDto chargeDto) throws IOException, ExecutionException, InterruptedException,TransactionException {
        ResponseEntity responseEntity = null;
        try {
            //String address = addressService.userLogin(chargeDto.getEmail());
            Long userId = ((UserDto) authentication.getPrincipal()).getUserId();
            System.out.println("address : "+chargeDto.getAddress()+"\n"+"id : " + chargeDto.getEmail()+"\n"+"chargeAmount :" +chargeDto.getCharge());
            
            List<String> chargeInfo = chargeService.chargeCheck(chargeDto.getAddress(), userId, Integer.parseInt(chargeDto.getCharge()));
            SingleDataResponse<List<String>> response = responseService.getSingleDataResponse(true, "충전 성공", chargeInfo);

            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (DuplicatedUsernameException exception) {
            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());

            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return responseEntity;
    }

    //유저 계좌 번호 출력
    @PostMapping("/charge/account")
    public ResponseEntity findByUserAccount(@RequestBody ChargeDto chargeDto) {
        ResponseEntity responseEntity = null;
        try {
            UserDto findUser = chargeService.findByUserAccount(chargeDto.getEmail());
    
            SingleDataResponse<UserDto> response = responseService.getSingleDataResponse(true, "조회 성공", findUser);
    
            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserNotFoundException exception) {
            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());
    
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    
         return responseEntity;
    }

    //유저 History 출력
    @PostMapping("/charge/historylist")
    public ResponseEntity historyList(final Authentication authentication, @RequestBody HistoryDto date) throws IOException, ExecutionException, InterruptedException {
        ResponseEntity responseEntity = null;
        try {
                
            Long userId = ((UserDto) authentication.getPrincipal()).getUserId();
    
            List<HistoryDto> historyList = chargeService.historyList(userId, date.getStartDate(), date.getEndDate());
    
            SingleDataResponse<List<HistoryDto>> response = responseService.getSingleDataResponse(true, "충전List 출력 성공", historyList);
    
            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);
    
        } catch (DuplicatedUsernameException exception) {
            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());
    
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return responseEntity;
    }
    //회원가입시 이더 전송
    // @PostMapping("/ethSend")
    // public String ethSend(@RequestParam String toAddress) throws IOException{
    //     chargeService.ethSend(toAddress);
    //     return "이더 전송 성공";
    // }
}