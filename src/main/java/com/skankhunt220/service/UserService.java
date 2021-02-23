package com.skankhunt220.service;

import com.skankhunt220.entity.User;
import com.skankhunt220.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User create(User user, String spreadsheetId) throws IOException {
        return userRepository.save(user, spreadsheetId);
    }

    public Page<User> readAll(Pageable pageable, String spreadsheetId) throws IOException {
        return userRepository.findAll(pageable, spreadsheetId);
    }

    public User readByID(String id, String spreadsheetId) throws IOException {
        return userRepository.findById(id, spreadsheetId);
    }

    public User update(String id, User user, String spreadsheetId) throws IOException {
        return userRepository.update(id, user, spreadsheetId);
    }

    public void delete(String id, String spreadsheetId) throws IOException {
        userRepository.deleteById(id, spreadsheetId);
    }
}
