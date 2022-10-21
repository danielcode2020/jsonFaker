package com.example.jsonfaker.controller;

import com.example.jsonfaker.model.Address;
import com.example.jsonfaker.service.AddressService;
import com.example.jsonfaker.service.AddressServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {
    private AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/add")
    ResponseEntity addAddress(@RequestBody Address address){
        Long address1 = addressService.saveAddress(address);
        return ResponseEntity.ok(address1);
    }

}
