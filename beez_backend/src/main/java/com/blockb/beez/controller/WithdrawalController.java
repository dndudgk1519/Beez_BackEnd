package com.blockb.beez.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.blockb.beez.dto.ExchangeDto;
import com.blockb.beez.dto.UserDto;
import com.blockb.beez.dto.WithdrawalDto;
import com.blockb.beez.dto.WithdrawalHistoryDto;
import com.blockb.beez.dto.response.BaseResponse;
import com.blockb.beez.dto.response.SingleDataResponse;
import com.blockb.beez.exception.DuplicatedUsernameException;
import com.blockb.beez.exception.UserNotFoundException;
import com.blockb.beez.service.ResponseService;
import com.blockb.beez.service.WithdrawalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class WithdrawalController {
    @Autowired
    ResponseService responseService;
    @Autowired
    WithdrawalService withdrawalService;

    //소상공인 출금
    @PostMapping("/withdrawal/amount")
    public ResponseEntity withdrawal(final Authentication authentication, @RequestBody WithdrawalDto withdrawalDto) throws IOException, ExecutionException, InterruptedException {
        ResponseEntity responseEntity = null;
        try {
            Long userId = ((UserDto) authentication.getPrincipal()).getUserId();

            System.out.println("address : "+withdrawalDto.getAddress()+"\n"+"id : " + withdrawalDto.getEmail()+"\n"+"withdrawalAmount :" + withdrawalDto.getWithdrawal());
            
            List<String> withdrawalInfo = withdrawalService.withdrawal(withdrawalDto.getAddress(), userId, Integer.parseInt(withdrawalDto.getWithdrawal()));
            SingleDataResponse<List<String>> response = responseService.getSingleDataResponse(true, "환전 성공", withdrawalInfo);

            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (DuplicatedUsernameException exception) {
            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());

            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return responseEntity;
    }

    //소상공인 계좌
    @PostMapping("/withdrawal/account")
    public ResponseEntity findByUserAccount(@RequestBody WithdrawalDto withdrawalDto){
        ResponseEntity responseEntity = null;

        try {

            UserDto findUser = withdrawalService.findByUserAccount(withdrawalDto.getEmail());

            SingleDataResponse<UserDto> response = responseService.getSingleDataResponse(true, "조회 성공", findUser);
            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch(UserNotFoundException exception) {

            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        }
    
         return responseEntity;
    }

    //소상공인 출금 History 출력
    @PostMapping("/withdrawal/historylist")
    public ResponseEntity historyList(final Authentication authentication, @RequestBody WithdrawalHistoryDto date) throws IOException, ExecutionException, InterruptedException {
        
        ResponseEntity responseEntity = null;
        try {

            Long userId = ((UserDto) authentication.getPrincipal()).getUserId();
    
            List<WithdrawalHistoryDto> historyList = withdrawalService.withdrawHistoryList(userId, date.getStartDate(), date.getEndDate());
    
            SingleDataResponse<List<WithdrawalHistoryDto>> response = responseService.getSingleDataResponse(true, "충전List 출력 성공", historyList);
    
            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);
    
        } catch (DuplicatedUsernameException exception) {
            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());
    
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return responseEntity;
    }
}
