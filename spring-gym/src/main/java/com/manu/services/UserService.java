package com.manu.services;

import com.manu.repositories.UserRepository;
import com.manu.responses.HttpServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public HttpServiceResponse<Object> getAllUsers(){
        try{
            return new HttpServiceResponse<>(200, userRepository.findAll(), null);
        } catch (Exception e){
            return new HttpServiceResponse<>(500, null, "Internal server error");
        }
    }

}
