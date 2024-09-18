package com.example.myrest1.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myrest1.dto.UserDTO;
import com.example.myrest1.repo.UserRepo;
import com.example.myrest1.entity.*;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;


    public UserDTO saveUser(UserDTO userDTO){
        userRepo.save(modelMapper.map(userDTO,User.class));
        return userDTO;
    }

    //MySQL
    //public List<UserDTO> getAllUsers(){
        
        // List<User> userList= userRepo.findAll();
        //return modelMapper.map(userList, new TypeToken<List<UserDTO>>(){}.getType());

   
    //}


    //Postgres
    public Iterable<UserDTO> getAllUsers(){

       Iterable<User> userList= userRepo.findAll();

    //    List<User> userList = new ArrayList<User>();
    //     while (iterator.hasNext()) {
    //         actualList.add(iterator.next());
    //     }

        return modelMapper.map(userList, new TypeToken<List<UserDTO>>(){}.getType());
    }

    

    public UserDTO updateUser(UserDTO userDTO){
        userRepo.save(modelMapper.map(userDTO, User.class));
        return userDTO;
    }


    public boolean deleteUser(UserDTO userDTO){
        userRepo.delete(modelMapper.map(userDTO,User.class));
        return true;
    }


    // public UserDTO getUserById(String userId){

    //     User user= userRepo.getUserByUserID(userId);
    //     return modelMapper.map(user, UserDTO.class);

    // }

    // public UserDTO getUserByIdandAddress(String userId, String address){

    //     User user= userRepo.getUserByUserIdandAddress(userId, address);
    //     return modelMapper.map(user, UserDTO.class);
    // }

}
