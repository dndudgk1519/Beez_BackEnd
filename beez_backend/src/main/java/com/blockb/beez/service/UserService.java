package com.blockb.beez.service;

import java.util.Collections;
import java.util.List;
import java.math.BigInteger;
import java.util.ArrayList;

import com.blockb.beez.controller.UserController;
import com.blockb.beez.dao.UserDao;
import com.blockb.beez.dto.KakaoLoginDto;
import com.blockb.beez.dto.LoginDto;
import com.blockb.beez.dto.UserDto;
import com.blockb.beez.exception.DuplicatedUsernameException;
import com.blockb.beez.exception.LoginFailedException;
import com.blockb.beez.exception.UserNotFoundException;
import com.blockb.beez.utils.JwtTokenProvider;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

import lombok.RequiredArgsConstructor;

//비즈니스 로직을 가진 Service생성.
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserDao userDao;
    private final UserController userController;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private String privKey;
    private String address;
    //로그인
    public List<String> login(LoginDto loginDto) {
        //db에 아이디랑 비교하고 존재하지 않을 경우, 노티 출력
        UserDto userDto = userDao.findUserByUsername(loginDto.getEmail())
                .orElseThrow(() -> new LoginFailedException("잘못된 아이디입니다"));
        //db에 아이디가 존재하고 비밀번호가 잘못 됐을 경우, 노티 출력
        if (!passwordEncoder.matches(loginDto.getPassword(), userDto.getPassword())) {
            throw new LoginFailedException("잘못된 비밀번호입니다");
        }
        if (!("ROLE_"+loginDto.getRole()).equals(userDto.getRole())) {
            throw new LoginFailedException("잘못된 권한입니다");
        }
        //jwtTokenProvider - 토큰을 생성하기 위해
        List<String> token = new ArrayList<>();
        token.add(jwtTokenProvider.createToken(userDto.getUserId(), Collections.singletonList(userDto.getRole())));
        token.add(userDto.getNickName());
        token.add(userDto.getWalletAddress());
        
        return token;
    }
    //카카오 로그인
    public List<String> loginKakao(KakaoLoginDto kakaoLoginDto) {
        //db에 아이디랑 비교하고 존재하지 않을 경우, 노티 출력
        UserDto userDto = userDao.findUserByUsername(kakaoLoginDto.getEmail())
                .orElseThrow(() -> new LoginFailedException("잘못된 아이디입니다"));
        if (!("ROLE_"+kakaoLoginDto.getRole()).equals(userDto.getRole())) {
            throw new LoginFailedException("잘못된 권한입니다");
        }
        //jwtTokenProvider - 토큰을 생성하기 위해
        List<String> token = new ArrayList<>();
        token.add(jwtTokenProvider.createToken(userDto.getUserId(), Collections.singletonList(userDto.getRole())));
        token.add(userDto.getNickName());
        token.add(userDto.getWalletAddress());
        
        return token;
    }

    //회원 가입
    @Transactional
    public UserDto join(UserDto userDto) {
        if (userDao.findUserByUsername(userDto.getEmail()).isPresent()) {
            throw new DuplicatedUsernameException("이미 가입된 유저입니다");
            
        }
        //여기서 랜덤하게 지갑주소 생성! 64자 16진수 string을 랜덤하게 출력!
        try {
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            BigInteger privateKeyInDec = ecKeyPair.getPrivateKey();

            privKey = privateKeyInDec.toString(16);

            WalletFile aWallet = Wallet.createLight(userDto.getPassword(), ecKeyPair);
            address = "0x" + aWallet.getAddress();
            
        } catch (Exception e){
            e.printStackTrace();
        }
        // System.out.println("address: " + address);
        // System.out.println("private key: " + privKey);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setPrivateKey(privKey);
        userDto.setWalletAddress(address);
        userDao.save(userDto);

        return userDao.findUserByUsername(userDto.getEmail()).get();
    }
    //회원 조회
    public UserDto findByUserId(Long userId) {
        return userDao.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("없는 유저입니다."));
    }
    //이메일 중복 조회
    public int findUserByEmail(String email) throws Exception {
        return userDao.findUserByEmail(email);
    }
    //중복 조회 확인
    // public int phoneCheck2(String phone2) throws Exception {
    //     if ( userController.phoneCheck == phone2 ) {
    //         System.out.println(phone2);
    //         System.out.println(userController.phoneCheck);
    //         return 1;
    //     } else {
    //         return 0;
    //     }
    // }
}
