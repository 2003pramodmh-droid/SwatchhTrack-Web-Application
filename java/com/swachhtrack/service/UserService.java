package com.swachhtrack.service;

import com.swachhtrack.entity.User;
import com.swachhtrack.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getOrCreateUser(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber)
                .orElseGet(() -> userRepository.save(User.builder()
                        .mobileNumber(mobileNumber)
                        .verified(true)
                        .build()));
    }
}
