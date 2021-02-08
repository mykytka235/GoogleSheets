package com.skankhunt220.api.controller;

import com.skankhunt220.api.dto.UserDto;
import com.skankhunt220.api.transformer.UserTransformer;
import com.skankhunt220.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/allUsers")
    public List<UserDto> getAllUsers() throws IOException {
        List<UserDto> listDto = new ArrayList<>();
        userService.readAll().forEach(user -> listDto.add(UserTransformer.transformIntoUserDto(user)));

        return listDto;
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") String id) throws IOException {
        return UserTransformer.transformIntoUserDto(userService.readByID(id));
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto dto) throws IOException {
        return UserTransformer.transformIntoUserDto(userService.create(UserTransformer.transformIntoUser(dto)));
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable("id") String id, @RequestBody UserDto dto) throws IOException {
        return UserTransformer.transformIntoUserDto(userService.update(id, UserTransformer.transformIntoUser(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) throws IOException {
        userService.delete(id);
    }
}