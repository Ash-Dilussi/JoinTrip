package com.example.myrest1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myrest1.dto.UserDTO;
import com.example.myrest1.service.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping(value = "api/v1/user")
@CrossOrigin

public class UserController {

    @Autowired
    private UserService userService;

    // @GetMapping("/getUsers")
    // public List<UserDTO> getUser() {

    //     return userService.getAllUsers();
    // }
    


    @PostMapping("/saveUser")
    public String saveUser(@RequestBody UserDTO userDTO) {

        userService.saveUser(userDTO);

        return "Saved user";
    }
    


    @PutMapping("/updateUser")
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {

        return userService.updateUser(userDTO);
    }
    



    @DeleteMapping("/deleteUser")
    public boolean removeUser(@RequestBody UserDTO userDTO) {

        return userService.deleteUser(userDTO);
    }
    

    // @GetMapping("/getUserByUserId/{userId}")
    // public UserDTO getUserByUserId(@PathVariable String userId) {
    //     return userService.getUserById(userId);
    // }

    // @GetMapping("/getUserByUserIdandAddress/{userId}/{address}")
    // public UserDTO getUserByUserIdandAddress(@PathVariable String userId,@PathVariable String address) {
    //     return userService.getUserByIdandAddress(userId,address);
    // }

    

}
