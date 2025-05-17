package com.spring.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.security.model.AuthenticationResponse;
import com.spring.security.model.User;
import com.spring.security.service.AuthentationService;

@RestController
public class AuthenticationController {

    private final AuthentationService authentationService;

    public AuthenticationController(AuthentationService authentationService) {
        this.authentationService = authentationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
        @RequestBody User request){
        return ResponseEntity.ok(authentationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
        @RequestBody User request){
        return ResponseEntity.ok(authentationService.authenticate(request));
    }

    @GetMapping("/success-common")
    public ResponseEntity<String> successResponse(){
        return ResponseEntity.ok("Login done successfully : common");
    }    

    @GetMapping("/success-admin")
    public ResponseEntity<String> successAdminResponse(){
        return ResponseEntity.ok("Login done successfully : admin");
    }   
}
