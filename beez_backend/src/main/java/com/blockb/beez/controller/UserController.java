package com.blockb.beez.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.blockb.beez.dto.KakaoLoginDto;
import com.blockb.beez.dto.LoginDto;
import com.blockb.beez.dto.UserDto;
import com.blockb.beez.dto.response.BaseResponse;
import com.blockb.beez.dto.response.SingleDataResponse;
import com.blockb.beez.exception.DuplicatedUsernameException;
import com.blockb.beez.exception.LoginFailedException;
import com.blockb.beez.exception.UserNotFoundException;
import com.blockb.beez.service.ChargeService;
import com.blockb.beez.service.Coolsms;
import com.blockb.beez.service.ResponseService;
import com.blockb.beez.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    ChargeService chargeService;
    @Autowired
    ResponseService responseService;
    @Autowired
    Coolsms coolsms;
    public String phoneCheck;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto) {
        //username, password 잘 가져옴.
        ResponseEntity responseEntity = null;
        try {
            //토큰생성
            List<String> token = new ArrayList<>();
            token = userService.login(loginDto);
            
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + token.get(0));

            SingleDataResponse<List<String>> response = responseService.getSingleDataResponse(true, "로그인 성공", token);

            responseEntity = ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(response);
        } catch (LoginFailedException exception) {

            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());

            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return responseEntity;
    }
    //카카오 로그인
    @PostMapping("/login/kakao")
    public ResponseEntity loginKakao(@RequestBody KakaoLoginDto kakaoLoginDto) {
        //username, password 잘 가져옴.
        ResponseEntity responseEntity = null;
        try {
            //토큰생성
            List<String> token = new ArrayList<>();
            token = userService.loginKakao(kakaoLoginDto);
            
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + token.get(0));

            SingleDataResponse<List<String>> response = responseService.getSingleDataResponse(true, "로그인 성공", token);

            responseEntity = ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(response);
        } catch (LoginFailedException exception) {

            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());

            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return responseEntity;
    }
    //회원 가입
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody UserDto userDto) throws IOException {
        ResponseEntity responseEntity = null;
        try {
            UserDto savedUser = userService.join(userDto);
            chargeService.ethSend(userDto.getWalletAddress());
            SingleDataResponse<UserDto> response = responseService.getSingleDataResponse(true, "회원가입 성공", savedUser);

            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DuplicatedUsernameException exception) {
            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());

            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return responseEntity;
    }

    //아이디 중복체크
    @PostMapping("/join/check")
    public int checkEmail(@RequestBody UserDto userDto) throws Exception {
        String email = userDto.getEmail();
        // System.out.println(email);

        int emailCheck = userService.findUserByEmail(email);
        // System.out.println(emailCheck);

        return emailCheck;
    }

    //전화번호 인증번호 보내기
    @PostMapping("/join/phoneCheck")
    public int checkPhone(@RequestBody UserDto userDto) throws Exception {
        String phone = userDto.getPhone();
        System.out.println(phone);

        int phoneCheck = coolsms.sendCoolsms(phone);
        System.out.println(phoneCheck);

        return phoneCheck;
    }

    //전화번호 인증번호 확인
    // @PostMapping("/join/phoneCheck2")
    // public int checkPhone2(@RequestBody UserDto userDto) throws Exception {
    //     String phone2 = userDto.getPhone();
    //     // System.out.println(phone);

    //     int phoneCheck2 = userService.phoneCheck2(phone2);
    //     // System.out.println(phoneCheck2);

    //     return phoneCheck2;
    // }


    //회원 조회(privateKey 받을때 빼고 사용 금지)
    @PostMapping("/users/priv")
    public ResponseEntity findUserByUsername(final Authentication authentication) {
        ResponseEntity responseEntity = null;
        try {
            Long userId = ((UserDto) authentication.getPrincipal()).getUserId();

            UserDto findUser = userService.findByUserId(userId);

            SingleDataResponse<UserDto> response = responseService.getSingleDataResponse(true, "조회 성공", findUser);

            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserNotFoundException exception) {
            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());

            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return responseEntity;
    }

    

}
