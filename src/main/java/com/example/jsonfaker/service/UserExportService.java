package com.example.jsonfaker.service;

import com.example.jsonfaker.model.dto.UserExportDTO;
import com.example.jsonfaker.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserExportService {
    private final UsersRepository usersRepository;

    public UserExportService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<UserExportDTO> getUsers(){
        List<UserExportDTO> userExportDTOList = new ArrayList<>();

        usersRepository.findAll().forEach(user -> userExportDTOList.add(
                new UserExportDTO(user.getName(),
                        user.getUsername(),
                        user.getEmail() ,
                        user.getAddress().getStreet(),
                        user.getAddress().getSuite(),
                        user.getAddress().getCity(),
                        user.getAddress().getZipcode(),
                        user.getAddress().getGeo().getLat(),
                        user.getAddress().getGeo().getLng(),
                        user.getPhone(),
                        user.getWebsite()
                ))

        );
        return userExportDTOList;
    }
}
