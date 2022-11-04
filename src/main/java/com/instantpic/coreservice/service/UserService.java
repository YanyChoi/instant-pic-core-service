package com.instantpic.coreservice.service;

import com.instantpic.coreservice.dto.user.UserDto;
import com.instantpic.coreservice.repository.UserRepository;
import org.springframework.stereotype.Service;
@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto loginService(String userId, String pw) {
        UserDto result;
        result = userRepository.readUserByIdAndPw(userId, pw).get();
        return result;

    }

    public UserDto logoutService(String userId) {
        UserDto result;
        result = userRepository.readUserById(userId).get();
        return result;
    }

    public UserDto signupService(UserDto user) {
        UserDto result;
        if (userRepository.createUser(user)) {
            result = userRepository.readUserByIdAndPw(user.getUserId(), user.getPw()).get();
        }
        else {
            result = new UserDto();
            result.setIntroduction("Signup Fail");
        }
        return result;
    }

}