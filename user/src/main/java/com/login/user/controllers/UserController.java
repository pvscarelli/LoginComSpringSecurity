package com.login.user.controllers;

import com.login.user.dtos.AuthenticationDto;
import com.login.user.dtos.LoginResponseDto;
import com.login.user.dtos.UserDto;
import com.login.user.models.User;
import com.login.user.services.AuthUserService;
import com.login.user.services.TokenService;
import com.login.user.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthUserService authUser;

    @Autowired
    private TokenService tokenService;

    @Operation(description = "Busca todos os usuários no repositório")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna todos os usuários"),
        @ApiResponse(responseCode = "400", description = "Não existe nenhum usuário salvo")
    })
    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers() {
        Iterable<User> users = userService.getAllUsers();
        if(users == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(description = "Busca um usuário pelo login")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna o usuário procurado"),
        @ApiResponse(responseCode = "404", description = "Não existe nenhum usuário salvo com esse login")
    })
    @GetMapping("/users/{login}")
    public ResponseEntity<User> getUser(@PathVariable String login) {
        User user = userService.getUserByLogin(login);
        if(user != null){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(description = "Faz o registro de um usuário no banco de dados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Retorna o usuário criado"),
        @ApiResponse(responseCode = "400", description = "Retorna os erros do formulário caso tenha algum campo inválido, ou retorna junto a mensagem \"E-mail ou login duplicado\"")
    })
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
            responseBody.put("error", "E-mail ou login duplicado.");
            return ResponseEntity.badRequest().body(responseBody);
        }
    }

    @Operation(description = "Faz o login do usuário com login e autenticação da senha")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna o token que vai ser utilizado pelo usuário automaticamente"),
        @ApiResponse(responseCode = "404", description = "Não existe nenhum usuário salvo com esse login")
    })
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthenticationDto data) {
        User user = authUser.authenticateLogin(data);
            if (user != null) {
                var token = tokenService.generateToken(user);
                return ResponseEntity.ok(new LoginResponseDto(token));
            }
       
        return ResponseEntity.badRequest().body("Login ou senha incorretos");
    }
    
    @Operation(description = "Atualiza um usuário do repositório pelo id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna o usuário procurado"),
        @ApiResponse(responseCode = "404", description = "Não existe nenhum usuário salvo com esse login")
    })
    @PutMapping("/editUser/{id}")
    public ResponseEntity<Object> editUser(@PathVariable("id") UUID id, @Valid @RequestBody UserDto userDto, BindingResult result) {
        User updatedUser = userService.updateUser(id, userDto);
        if(updatedUser != null){
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(description = "Deleta um usuário pelo id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Não existe nenhum usuário salvo com esse login")
    })
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") UUID id) {
        boolean deleted = userService.deleteUser(id);
        if(deleted){
            return ResponseEntity.ok().body("Usuário deletado com sucesso!");
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(description = "Deleta todos os usuários cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Todos os usuários foram deletados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Não foi possível excluir todos os usuários")
    })
    @DeleteMapping("/deleteAllUsers")
    public ResponseEntity<Object> deleteAllUsers() {
        boolean deleted = userService.deleteAllUsers();
        if(deleted){
            return ResponseEntity.ok().body("Todos os usuários foram deletados com sucesso!");
        }
        return ResponseEntity.notFound().build();
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