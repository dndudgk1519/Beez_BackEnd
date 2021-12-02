package com.blockb.beez.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;

import com.blockb.beez.dto.PassCheckDto;
import com.blockb.beez.dto.UserDto;
import com.blockb.beez.dto.response.BaseResponse;
import com.blockb.beez.dto.response.SingleDataResponse;
import com.blockb.beez.exception.UserNotFoundException;
import com.blockb.beez.service.PassCheckService;
import com.blockb.beez.service.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class  PassCheckController {

    @Autowired
    PassCheckService passCheckService;
    @Autowired
    ResponseService responseService;

    //회원 보안 Password 여부 확인
    @GetMapping("/pass/confirm")
    public ResponseEntity findUserByPassConfirm(final Authentication authentication) {
        ResponseEntity responseEntity = null;
        try {
            
            Long userId = ((UserDto) authentication.getPrincipal()).getUserId();

            PassCheckDto findUser = passCheckService.findByUserPassConfirm(userId);

            SingleDataResponse<PassCheckDto> response = responseService.getSingleDataResponse(true, "조회 성공", findUser);

            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserNotFoundException exception) {

            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());

            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return responseEntity;
    }
    
    //회원 PasswordCheck
    @PostMapping("/pass/check")
    public ResponseEntity findUserByPassCheck(final Authentication authentication,@RequestBody UserDto userDto) {
        ResponseEntity responseEntity = null;
        Long userId = ((UserDto) authentication.getPrincipal()).getUserId();
        try {

            PassCheckDto findUser = passCheckService.findByUserPassCheck(userId, userDto.getPassword());

            SingleDataResponse<PassCheckDto> response = responseService.getSingleDataResponse(true, "조회 성공", findUser);

            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (UserNotFoundException exception) {
            
            if(exception.getMessage().equals("비밀번호가 틀립니다.")){
                passCheckService.saveFailCount(userId);
            }
            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());

            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return responseEntity;
    }

    //회원 PasswordStorage
    @PostMapping("/pass/storage")
    public ResponseEntity findUserByPassStorage(final Authentication authentication,@RequestBody PassCheckDto passCheckDto) {
        ResponseEntity responseEntity = null;
        try {
            System.out.println(passCheckDto.getPasswordCheck()+"");
            Long userId = ((UserDto) authentication.getPrincipal()).getUserId();
            System.out.println(userId+"");
            passCheckDto.setUserId(userId);
            passCheckService.findByUserPassStorage(passCheckDto);

            SingleDataResponse<String> response = responseService.getSingleDataResponse(true, "저장 성공", "");

            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserNotFoundException exception) {
            
            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());

            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return responseEntity;
    }
    

}