package com.login.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.login.user.domain.dtos.AuthenticationDto;
import com.login.user.domain.dtos.UserDto;
import com.login.user.domain.exceptions.IncorrectCredentialsException;
import com.login.user.domain.exceptions.UserNotFoundException;

@Service
public class AuthUserService {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    public UserDto authenticateLogin(AuthenticationDto data){
        try{
        UserDto userDto = userService.getUserByLogin(data.login());
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        if (auth.getPrincipal() instanceof UserDetails) {
            return userDto;
        }
        } catch (UserNotFoundException exception){
            throw new IncorrectCredentialsException("Login incorreto");
        } catch (AuthenticationException exception){
            throw new IncorrectCredentialsException("Senha incorreta");
        }
        throw new UserNotFoundException("Não foi possível autenticar esse usuário");
    }
}
