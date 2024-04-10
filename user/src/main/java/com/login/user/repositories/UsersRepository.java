package com.login.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.login.user.models.User;

import java.util.UUID;


@Repository
public interface UsersRepository extends JpaRepository<User, UUID>{
    
    public User findByMail(String mail);
    UserDetails findByLogin(String login);
}