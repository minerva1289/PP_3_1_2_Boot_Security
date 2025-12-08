package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository <User, Long> {
    @Query ("SELECT u FROM User u JOIN FETCH u.setOfRoles WHERE u.email = :email")
    Optional <User> findByEmail (@Param("email") String email);
    // 1. JOIN FETCH для загрузки ОДНОГО пользователя с ролями
    @Query ("SELECT u FROM User u JOIN FETCH u.setOfRoles WHERE u.id = :id")
    Optional<User> findByIdWithRoles(@Param ("id") Long id);
    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.setOfRoles")
    List <User> findAllWithRoles();
}
