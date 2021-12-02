package com.blockb.beez.service;

import com.blockb.beez.dao.PassCheckDao;
import com.blockb.beez.dto.PassCheckDto;
import com.blockb.beez.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PassCheckService {
    private final PassCheckDao passCheckDao;
    private final PasswordEncoder passwordEncoder;

    //회원 보안 Password 여부 확인
    public PassCheckDto findByUserPassConfirm(Long userId) {
        return passCheckDao.findByUserPassConfirm(userId)
                .orElseThrow(() -> new UserNotFoundException("없는 유저입니다."));
    }
    //회원 PasswordCheck
    public PassCheckDto findByUserPassCheck(Long userId, String password) {
        PassCheckDto passCheckDto = passCheckDao.findByUserPassCheck(userId)
                .orElseThrow(() -> new UserNotFoundException("없는 유저입니다."));

        if (!passwordEncoder.matches(password, passCheckDto.getPasswordCheck())) {
            throw new UserNotFoundException("비밀번호가 틀립니다."); 
        }
        if(!passCheckDto.getFailCount().equals("0")){
            passCheckDao.initPassCount(userId);
        }
        return passCheckDto;
    }
    
    //회원 PasswordStorage
    public void findByUserPassStorage(PassCheckDto passCheckDto) {
        passCheckDto.setPasswordCheck(passwordEncoder.encode(passCheckDto.getPasswordCheck()));
        passCheckDao.passSave(passCheckDto);
    }

     //회원 비밀번호 틀렸을 경우,
     public void saveFailCount(Long userId) {
        passCheckDao.passCount(userId);
    }
}
