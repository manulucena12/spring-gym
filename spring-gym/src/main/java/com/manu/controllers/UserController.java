package com.manu.controllers;

import com.manu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Object> getAllUsers(){
        var response = userService.getAllUsers();
        return ResponseEntity.status(response.getStatusCode()).body(
                response.getStatusCode() == 200 ? response.getContent() : response.getMessage()
        );
    }

}
