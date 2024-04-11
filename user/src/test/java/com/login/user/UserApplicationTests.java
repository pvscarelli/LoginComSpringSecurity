package com.login.user;

import com.login.user.dtos.UserDto;
import com.login.user.models.User;
import com.login.user.models.UserRole;
import com.login.user.repositories.UsersRepository;
import com.login.user.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserByLogin() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        user.setMail("john@example.com");
        user.setLogin("login");
        user.setPassword("password");
        user.setRole(UserRole.ADMIN);
    
        when(usersRepository.findByLogin(user.getUsername())).thenReturn(user);
    
        User foundUser = userService.getUserByLogin(user.getUsername());   
        assertNotNull(foundUser);
    
        assertEquals(user, foundUser);
    }

    @Test
    void getUserByLoginFailure(){
        String login = "teste";

        when(usersRepository.findByLogin(login)).thenReturn(null);

        assertNull(userService.getUserByLogin(login));
    }

    @Test
    void getAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(createUser("John Doe", "john@example.com","login", "password"));
        users.add(createUser("Jane Smith","jane@example.com","login2", "password"));

        when(usersRepository.findAll()).thenReturn(users);

        Iterable<User> allUsers = userService.getAllUsers();

        assertNotNull(allUsers);
        assertTrue(allUsers.iterator().hasNext());
        assertEquals(users.size(), ((List<User>) allUsers).size());
    }

    @Test
    void registerUser() {
        UserDto userDto = new UserDto("John Doe", "john@example.com","login","password");
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(userDto.name());
        user.setMail(userDto.mail());
		user.setLogin(userDto.login());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.password()));
        user.setRole(UserRole.ADMIN);

        when(usersRepository.findByMail(userDto.mail())).thenReturn(null);
        when(usersRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.registerUser(userDto);

        assertNotNull(savedUser);
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getMail(), savedUser.getMail());
        assertEquals(user.getUsername(), savedUser.getUsername());
    }

    @Test
    void registerUserFalse() {
        UserDto userDto = new UserDto("John Doe", "john@example.com","login", "password");
        User existingUser = new User();

        when(usersRepository.findByMail(userDto.mail())).thenReturn(existingUser);

        User savedUser = userService.registerUser(userDto);

        assertNull(savedUser);
    }

    @Test
    void updateUser() {
        UUID userId = UUID.randomUUID();
        UserDto updatedUserDto = new UserDto("Updated Name", "updated@example.com", "logi", "updatedpassword");
        User existingUser = createUser("John Doe", "john@example.com","login", "password");

        when(usersRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(usersRepository.save(any(User.class))).thenReturn(existingUser);

        User updatedUser = userService.updateUser(userId, updatedUserDto);

        assertEquals(updatedUserDto.name(), updatedUser.getName());
        assertEquals(updatedUserDto.mail(), updatedUser.getMail());
    }

    @Test
    void updateUserFailure() {
        UUID userId = UUID.randomUUID();
    
        when(usersRepository.findById(userId)).thenReturn(Optional.empty());
    
        Optional<User> userOptional = usersRepository.findById(userId);
        
        assertTrue(userOptional.isEmpty()); 
    }

    @Test
    void deleteUser() {
        UUID userId = UUID.randomUUID();
        User existingUser = createUser("John Doe", "john@example.com", "login", "password");

        when(usersRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        boolean deleted = userService.deleteUser(userId);

        assertTrue(deleted);
    }

    @Test
    void deleteUserFailure() {
        UUID userId = UUID.randomUUID();

        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        boolean deleted = userService.deleteUser(userId);

        assertFalse(deleted);
    }

    private User createUser(String name, String mail, String login, String password) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(name);
        user.setMail(mail);
        user.setLogin(login);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRole(UserRole.ADMIN);
        return user;
    }
}
