package com.example.jsonfaker.controller;

import com.example.jsonfaker.configuration.AppProperties;
import com.example.jsonfaker.configuration.CSVProperties;
import com.example.jsonfaker.csvUtils.CustomMappingStrategy;
import com.example.jsonfaker.excelUtils.UserExcelExporter;
import com.example.jsonfaker.model.Users;
import com.example.jsonfaker.model.dto.UserExportDTO;
import com.example.jsonfaker.repository.UsersRepository;
import com.example.jsonfaker.security.AuthoritiesConstants;
import com.example.jsonfaker.service.UserExportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import io.swagger.annotations.ApiImplicitParam;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FackerController {

    private final Logger logger;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final AppProperties customProps;
    private final UsersRepository usersRepository;

    private final UserExportService userExportService;

    private final CSVProperties csvProperties;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public FackerController(Logger logger, ObjectMapper objectMapper, RestTemplate restTemplate,
                            AppProperties customProps, UsersRepository usersRepository, UserExportService userExportService, CSVProperties csvProperties, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.logger = logger;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.customProps = customProps;
        this.usersRepository = usersRepository;
        this.userExportService = userExportService;
        this.csvProperties = csvProperties;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/populate")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity getData(){
        ResponseEntity<Object[]> response = restTemplate.getForEntity(customProps.getUri(), Object[].class);
        List<Users> users = Arrays.stream(response.getBody())
                .map(obj -> objectMapper.convertValue(obj, Users.class))
                .collect(Collectors.toList());
        usersRepository.saveAll(users);
        logger.info("succesfully saved");
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/export-csv")
    public void exportCSV(HttpServletResponse response) throws Exception{

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" +csvProperties.getFilename()+ "\"");

        CustomMappingStrategy<UserExportDTO> mappingStrategy = new CustomMappingStrategy<>();
        mappingStrategy.setType(UserExportDTO.class);

        StatefulBeanToCsv<UserExportDTO> writer = new StatefulBeanToCsvBuilder<UserExportDTO>(response.getWriter())
                .withMappingStrategy(mappingStrategy)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();


        writer.write(userExportService.getUsers());
    }

    @GetMapping("/export-excel")
    public void exportToExcel(HttpServletResponse response) throws IOException{
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        UserExcelExporter excelExporter = new UserExcelExporter(userExportService.getUsers());
        excelExporter.export(response);
    }

    @GetMapping("/read-csv")
    public void readCSV() throws FileNotFoundException {
        List<UserExportDTO> userExportDTOList = new CsvToBeanBuilder( new FileReader(System.getProperty("user.home") + "/Downloads/" + csvProperties.getFilename()))
                .withType(UserExportDTO.class)
                .build()
                .parse();
        usersRepository.saveAll(userExportService.getUsersFromUsersDto(userExportDTOList));
    }

    @GetMapping("/delete-users-from-db")
    public void deleteUsersFromDb(){
        usersRepository.deleteAll();
    }

    @PostMapping("/add")// for testing validation
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity addUser(@Valid @RequestBody Users user) {
        usersRepository.save(user);
        return ResponseEntity.ok("saved");
    }
}
