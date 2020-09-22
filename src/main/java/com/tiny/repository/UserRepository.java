package com.tiny.repository;

import java.util.Optional;

import com.tiny.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
    Optional<User> findByUsername( String username );
}