package com.example.jsonfaker.service;

import com.example.jsonfaker.model.Address;
import org.springframework.stereotype.Service;


public interface AddressService {
    Long saveAddress(Address address);
}
