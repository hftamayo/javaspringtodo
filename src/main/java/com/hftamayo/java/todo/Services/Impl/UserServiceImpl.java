package com.hftamayo.java.todo.Services.Impl;

import com.hftamayo.java.todo.Model.User;
import com.hftamayo.java.todo.Repository.UserRepository;
import com.hftamayo.java.todo.Services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return userRepository.findAllByActive(isActive);
    }

    public User getUserById(long userId){
        return userRepository.findById(userId).get();
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User getUserByUsernameAndPassword(String username, String password){
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public User getUserByEmailAndPassword(String email, String password){
        return userRepository.findByEmailAndPassword(email, password);
    }

    public long countAllUserByUsername(String username){
        return userRepository.countAllByUsername(username);
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
            requestedUser.setUserStatus(updatedUser.isUserStatus());
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
