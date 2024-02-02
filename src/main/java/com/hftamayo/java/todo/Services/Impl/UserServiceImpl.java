package com.hftamayo.java.todo.Services.Impl;

import com.hftamayo.java.todo.Model.User;
import com.hftamayo.java.todo.Repository.UserRepository;
import com.hftamayo.java.todo.Services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public List<User> getAllUsersByStatus(boolean isActive){
        return userRepository.findAllByStatus(isActive);
    }

    public User getUserById(long userId){
        return userRepository.findById(userId).get();
    }

    public Optional<User> getUserByUsername(String username){
        return userRepository.findByName(username);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User getUserByUsernameAndPassword(String username, String password){
        return userRepository.findByNameAndPassword(username, password);
    }

    public Optional<User> getUserByNameOrEmail(String username, String email){
        return userRepository.findByNameOrEmail(username, email);
    }

    public User getUserByEmailAndPassword(String email, String password){
        return userRepository.findByEmailAndPassword(email, password);
    }

    public long countAllUserByUsername(String username){
        return userRepository.countAllByName(username);
    }

    public long countAllUserByEmail(String email){
        return userRepository.countAllByEmail(email);
    }

    @Override
    public User saveUser(User newUser) {
        User requestedUser = userRepository.findByEmail(newUser.getEmail());
        if(requestedUser == null){
            return userRepository.save(newUser);
        } else {
            throw new RuntimeException("User already exists");
        }
    }

    @Override
    public User updateUser(long userId, User updatedUser) {
        User requestedUser = userRepository.findById(userId).get();
        if(requestedUser != null){
            requestedUser.setName(updatedUser.getName());
            requestedUser.setEmail(updatedUser.getEmail());
            requestedUser.setPassword(updatedUser.getPassword());
            requestedUser.setAge(updatedUser.getAge());
            requestedUser.setAdmin(updatedUser.isAdmin());
            requestedUser.setStatus(updatedUser.isStatus());
            return userRepository.save(requestedUser);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public void deleteUser(long userId) {
        boolean userExists = userRepository.existsById(userId);
        if(userExists){
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
