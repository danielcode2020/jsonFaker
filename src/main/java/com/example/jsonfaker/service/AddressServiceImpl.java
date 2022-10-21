package com.example.jsonfaker.service;

import com.example.jsonfaker.model.Address;
import com.example.jsonfaker.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService{
    @Autowired
    private AddressRepository addressRepository;
    @Override
    public Long saveAddress(Address address) {
        return addressRepository.save(address).getId();
    }

}
