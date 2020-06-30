package com.phones.phones.service;

import com.phones.phones.dto.UserDto;
import com.phones.phones.exception.user.*;
import com.phones.phones.model.User;
import com.phones.phones.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(final UserRepository userRepository,
                       final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User create(User newUser) throws UserAlreadyExistException, UsernameAlreadyExistException {
        Optional<User> user = userRepository.findByDni(newUser.getDni());
        if (user.isPresent()) {
            throw new UserAlreadyExistException();
        }
        if (existsUsername(newUser.getUsername())) {
            throw new UsernameAlreadyExistException();
        }
        String passwordHashed = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(passwordHashed);
        return userRepository.save(newUser);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) throws UserDoesNotExistException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException();
        }
        return user.get();
    }

    public boolean disableById(Long id) throws UserDoesNotExistException, UserAlreadyDisableException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException();
        }
        if (!user.get().isActive()) {
            throw new UserAlreadyDisableException();
        }
        return (userRepository.disableById(id) > 0);
    }

    public User updateById(Long id,
                           UserDto updatedUser) throws UserDoesNotExistException, UsernameAlreadyExistException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException();
        }
        if (existsUsernameToUpdate(updatedUser.getUsername(), user.get().getId())) {
            throw new UsernameAlreadyExistException();
        }
        user.get().setName(updatedUser.getName());
        user.get().setSurname(updatedUser.getSurname());
        user.get().setDni(updatedUser.getDni());
        user.get().setUsername(updatedUser.getUsername());

        String passwordHashed = passwordEncoder.encode(updatedUser.getPassword());
        user.get().setPassword(passwordHashed);

        user.get().setCity(updatedUser.getCity());
        user.get().setUserRoles(updatedUser.getUserRoles());
        user.get().setActive(updatedUser.isActive());

        return userRepository.save(user.get());
    }

    public boolean existsUsername(String username) {
        return userRepository.findByUsernameBoolean(username) > 0;
    }

    public boolean existsUsernameToUpdate(String username, Long id) {
        return userRepository.findByUsernameToUpdateBoolean(username, id) > 0;
    }

    public Optional<User> login(String username,
                                String password) throws UserInvalidLoginException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            throw new UserInvalidLoginException();
        }
        return user;
    }

}