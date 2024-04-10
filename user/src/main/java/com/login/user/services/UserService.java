package com.login.user.services;

import com.login.user.dtos.UserDto;
import com.login.user.models.User;
import com.login.user.repositories.UsersRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    public Iterable<User> getAllUsers() {
        return usersRepository.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        return usersRepository.findById(id);
    }

    public User registerUser(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        if(usersRepository.findByMail(user.getMail()) != null){
            return null;
        }

        String hashedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(hashedPassword);
        return usersRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByLogin(username);
    }

    public Optional<User> updateUser(UUID id, UserDto userDto) {
        Optional<User> optionalUser = usersRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            BeanUtils.copyProperties(userDto, user);
            String hashedPassword = encodePassword(user.getPassword());
            user.setPassword(hashedPassword);
            return Optional.of(usersRepository.save(user));
        }
        return Optional.empty();
    }

    public boolean deleteUser(UUID id) {
        Optional<User> optionalUser = usersRepository.findById(id);
        if (optionalUser.isPresent()) {
            usersRepository.delete(optionalUser.get());
            return true;
        }
        return false;
    }

    private String encodePassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}
