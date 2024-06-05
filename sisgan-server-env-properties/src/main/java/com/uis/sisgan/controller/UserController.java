package com.uis.sisgan.controller;

import com.uis.sisgan.service.UserService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/user")
public class UserController {
    
    private UserService userService;
}
