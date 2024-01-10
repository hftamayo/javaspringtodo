package com.hftamayo.java.todo.Controller;

import com.hftamayo.java.todo.Model.User;
import com.hftamayo.java.todo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping(value = "/users/allusers")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping(value = "/users/getuserbyid/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable long userId){
        return userService.getUserById(userId);
    }

    @GetMapping(value = "/users/getuserbyusername/{username}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByUsername(@PathVariable String username){
        return userService.getUserByUsername(username);
    }

    @GetMapping(value = "/users/getuserbyemail/{email}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @GetMapping(value = "/users/getuserbyusernameandpassword/{username}/{password}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByUsernameAndPassword(@PathVariable String username, @PathVariable String password){
        return userService.getUserByUsernameAndPassword(username, password);
    }

    @GetMapping(value = "/users/getuserbyemailandpassword/{email}/{password}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByEmailAndPassword(@PathVariable String email, @PathVariable String password){
        return userService.getUserByEmailAndPassword(email, password);
    }

    @GetMapping(value = "/users/countuserbyusername/{username}")
    @ResponseStatus(HttpStatus.OK)
    public long countUserByUsername(@PathVariable String username){
        return userService.countAllUserByUsername(username);
    }

    @GetMapping(value = "/users/countuserbyemail/{email}")
    @ResponseStatus(HttpStatus.OK)
    public long countUserByEmail(@PathVariable String email){
        return userService.countAllUserByEmail(email);
    }


}
