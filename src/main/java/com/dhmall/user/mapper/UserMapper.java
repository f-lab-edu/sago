package com.dhmall.user.mapper;

import com.dhmall.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    void insertUser(UserDto user);

    void updateAuthStatus(String email);

    UserDto findById(String nickname);
}