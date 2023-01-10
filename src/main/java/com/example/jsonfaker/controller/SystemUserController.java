package com.example.jsonfaker.controller;

import com.example.jsonfaker.model.SystemUser;
import com.example.jsonfaker.repository.SystemUserRepository;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system-user")
@CrossOrigin
public class SystemUserController {
    private final SystemUserRepository systemUserRepository;

    public SystemUserController(SystemUserRepository systemUserRepository) {
        this.systemUserRepository = systemUserRepository;
    }

    @GetMapping("")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public List<SystemUser> getAllSystemUsers(){
        return systemUserRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSystemUserById(@PathVariable Long id){
        systemUserRepository.deleteById(id);
        return ResponseEntity.ok().body("deleted");
    }
}
