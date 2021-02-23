package com.skankhunt220.api.controller;

import com.skankhunt220.api.dto.UserDto;
import com.skankhunt220.api.transformer.UserTransformer;
import com.skankhunt220.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/{spreadsheetId}", params = {"page", "size"})
    public Page<UserDto> readAll(@RequestParam("page") int page, @RequestParam("size") int size, @PathVariable("spreadsheetId") String spreadsheetId) throws IOException {
        return userService.readAll(PageRequest.of(page, size), spreadsheetId).map(user -> UserTransformer.transform(user));
    }

    @GetMapping("/{spreadsheetId}/{id}")
    public UserDto read(@PathVariable("id") String id, @PathVariable("spreadsheetId") String spreadsheetId) throws IOException {
        return UserTransformer.transform(userService.readByID(id, spreadsheetId));
    }

    @PostMapping("/{spreadsheetId}")
    public UserDto create(@RequestBody UserDto dto, @PathVariable("spreadsheetId") String spreadsheetId ) throws IOException {
        return UserTransformer.transform(userService.create(UserTransformer.transform(dto), spreadsheetId));
    }

    @PutMapping("/{spreadsheetId}/{id}")
    public UserDto update(@PathVariable("id") String id, @RequestBody UserDto dto, @PathVariable("spreadsheetId") String spreadsheetId) throws IOException {
        return UserTransformer.transform(userService.update(id, UserTransformer.transform(dto), spreadsheetId));
    }

    @DeleteMapping("/{spreadsheetId}/{id}")
    public void delete(@PathVariable("id") String id, @PathVariable("spreadsheetId") String spreadsheetId) throws IOException {
        userService.delete(id, spreadsheetId);
    }
}