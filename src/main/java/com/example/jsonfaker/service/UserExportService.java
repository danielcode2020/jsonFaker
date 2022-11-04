package com.example.jsonfaker.service;

import com.example.jsonfaker.model.Address;
import com.example.jsonfaker.model.Company;
import com.example.jsonfaker.model.Geo;
import com.example.jsonfaker.model.Users;
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

    public List<UserExportDTO> getUsers() {
        List<UserExportDTO> userExportDTOList = new ArrayList<>();

        usersRepository.findAll().forEach(user -> userExportDTOList.add(
                new UserExportDTO(user.getName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getAddress().getStreet(),
                        user.getAddress().getSuite(),
                        user.getAddress().getCity(),
                        user.getAddress().getZipcode(),
                        user.getAddress().getGeo().getLat().toString(),
                        user.getAddress().getGeo().getLng().toString(),
                        user.getPhone(),
                        user.getWebsite()
                ))

        );
        return userExportDTOList;
    }

    public List<Users> getUsersFromUsersDto(List<UserExportDTO> userExportDTOS) {
        List<Users> usersList = new ArrayList<>();
        // skip 1 because first column are the names
        userExportDTOS.stream().skip(1).forEach(userExportDTO -> usersList.add(
                new Users(
                        userExportDTO.getName(),
                        userExportDTO.getUsername(),
                        userExportDTO.getEmail(),
                        userExportDTO.getPhone(),
                        userExportDTO.getWebsite(),
                        new Address(
                                userExportDTO.getStreet(),
                                userExportDTO.getCity(),
                                userExportDTO.getSuite(),
                                userExportDTO.getZipcode(),
                                new Geo(
                                        Double.parseDouble(userExportDTO.getLat()),
                                        Double.parseDouble(userExportDTO.getLng())
                                )
                        ),
                        new Company(
                                "company_name",
                                "catch_phrase",
                                "bs"

                        )

                )
        ));

        return usersList;

    }
}
