package com.login.user.controllers;

import com.login.user.dtos.AuthenticationDto;
import com.login.user.dtos.LoginResponseDto;
import com.login.user.dtos.UserDto;
import com.login.user.models.User;
import com.login.user.services.AuthUserService;
import com.login.user.services.TokenService;
import com.login.user.services.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthUserService authUser;

    @Autowired
    private TokenService tokenService;


    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable String login) {
        User user = userService.getUserByLogin(login);
        if(user != null){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(validationErrors(result));
        }

        User savedUser = userService.registerUser(userDto);
        if(savedUser != null){
            return ResponseEntity.created(URI.create("/users/" + savedUser.getId())).build();
        } else {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("error", "O email já está em uso.");
            return ResponseEntity.badRequest().body(responseBody);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthenticationDto data) {
        User user = authUser.authenticateLogin(data);
            if (user != null) {
                var token = tokenService.generateToken(user);
                return ResponseEntity.ok(new LoginResponseDto(token));
            }
       
        return ResponseEntity.notFound().build();
    }
    

    @PutMapping("/editUser/{id}")
    public ResponseEntity<Object> editUser(@PathVariable("id") UUID id, @Valid @RequestBody UserDto userDto, BindingResult result) {
        Optional<User> updatedUser = userService.updateUser(id, userDto);
        return updatedUser.map(user -> ResponseEntity.ok().build()).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") UUID id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    private String validationErrors(BindingResult result) {
        StringBuilder errors = new StringBuilder();
        result.getAllErrors().forEach(error -> {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                errors.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage()).append("\n");
            } else {
                errors.append(error.getDefaultMessage()).append("\n");
            }
        });
        return errors.toString();
    }
}