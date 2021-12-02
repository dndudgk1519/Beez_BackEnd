package com.blockb.beez.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

import com.blockb.beez.dto.UserDto;

@Mapper
public interface UserDao {
    //Optional는 널포인트오류를 방지
    Optional<UserDto> findUserByUsername(String email);
    Optional<UserDto> findByUserId(Long userId);
    Optional<UserDto> findByUserAccount(String email);
    int findUserByEmail(String email);
    void save(UserDto userDto);
}