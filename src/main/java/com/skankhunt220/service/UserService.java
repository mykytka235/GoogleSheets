package com.skankhunt220.service;

import com.skankhunt220.entity.User;
import com.skankhunt220.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User create(User user) throws IOException {
        return userRepository.save(user);
    }

    public List<User> readAll() throws IOException {
        return userRepository.findAll();
    }

    public User readByID(String id) throws IOException {
        return userRepository.findById(id);
    }

    public User update(String id, User user) throws IOException {
        return userRepository.update(id, user);
    }

    public void delete(String id) throws IOException {
        userRepository.deleteById(id);
    }
}
