package com.example.jsonfaker.controller;

import com.example.jsonfaker.configuration.AppProperties;
import com.example.jsonfaker.configuration.CSVProperties;
import com.example.jsonfaker.csvUtils.CustomMappingStrategy;
import com.example.jsonfaker.excelUtils.UserExcelExporter;
import com.example.jsonfaker.model.Users;
import com.example.jsonfaker.model.dto.AllUsersDTO;
import com.example.jsonfaker.model.dto.UserExportDTO;
import com.example.jsonfaker.pdfUtils.PdfUserExporter;
import com.example.jsonfaker.repository.UsersRepository;
import com.example.jsonfaker.service.Exporter;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/faker")
@CrossOrigin
public class FackerController {

    private final Logger logger;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final AppProperties customProps;
    private final UsersRepository usersRepository;

    private final UserExportService userExportService;

    private final CSVProperties csvProperties;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Exporter exporter;

    public FackerController(Logger logger, ObjectMapper objectMapper, RestTemplate restTemplate, AppProperties customProps, UsersRepository usersRepository, UserExportService userExportService, CSVProperties csvProperties, BCryptPasswordEncoder bCryptPasswordEncoder, Exporter exporter) {
        this.logger = logger;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.customProps = customProps;
        this.usersRepository = usersRepository;
        this.userExportService = userExportService;
        this.csvProperties = csvProperties;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.exporter = exporter;
    }

    @GetMapping("/populate")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity populateDbFromURI() {
        ResponseEntity<Object[]> response = restTemplate.getForEntity(customProps.getUri(), Object[].class);
        System.out.println(response);

        List<Users> users = Arrays.stream(response.getBody())
                .map(obj -> objectMapper.convertValue(obj, Users.class))
                .collect(Collectors.toList());

        System.out.println(users.toString());
        usersRepository.saveAll(users);
        logger.info("succesfully saved");
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/export-csv")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public void exportCSV(HttpServletResponse response) throws Exception {

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + csvProperties.getFilename() + "\"");

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
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        UserExcelExporter excelExporter = new UserExcelExporter(userExportService.getUsers());
        excelExporter.export(response);
    }

    @PostMapping("/upload-from-csv")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public void uploadCSVFile(@RequestParam("file") MultipartFile file ){
        if(file.isEmpty()){
            logger.error("file is empty");
        } else{
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
                List<UserExportDTO> userExportDTOList = new CsvToBeanBuilder(reader)
                        .withType(UserExportDTO.class)
                        .build()
                        .parse();
                logger.info("received list of users from file");
                usersRepository.saveAll(userExportService.getUsersFromUsersDto(userExportDTOList));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @GetMapping("/export-xml")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<byte[]> exportXml() throws JAXBException, IOException {

        JAXBContext jaxbContext = JAXBContext.newInstance(AllUsersDTO.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        List<Users> users = new ArrayList<>();
        usersRepository.findAll().forEach(users::add);
        AllUsersDTO allUsersDTO = new AllUsersDTO();
        allUsersDTO.setUsersList(users);

        ByteArrayOutputStream resultXML = new ByteArrayOutputStream();
        jaxbMarshaller.marshal(allUsersDTO, resultXML);
        byte[] export = resultXML.toByteArray();

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\""+exporter.exportXMLFileName() + ".xml\"")
                .body(export);
    }

    @PostMapping("/populate-db-from-xml")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<String> populateDbFromXML(@RequestParam("file") MultipartFile file ){
        if(file.isEmpty()){
            logger.error("file is empty");
            return ResponseEntity.noContent().build();
        } else{
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
                JAXBContext jaxbContext = JAXBContext.newInstance(AllUsersDTO.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                AllUsersDTO users = (AllUsersDTO) jaxbUnmarshaller.unmarshal(reader);
                logger.info("received list of users from xml file");


                List<Users> toSave = new ArrayList<>();
                for (Users user:users.getUsersList()) {
                    toSave.add(user);
                }
                usersRepository.saveAll(toSave);
                return ResponseEntity.ok().body("populated successfully ");
            } catch (IOException | JAXBException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @GetMapping("/export-pdf/{id}")
    public ResponseEntity<byte[]> exportPdfUserById(@Valid @PathVariable("id") Long id) throws MalformedURLException {
        PdfUserExporter pdfUserExporter = new PdfUserExporter();
        byte[] export = pdfUserExporter.exportUserById(usersRepository.findById(id).get());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\""+exporter.exportPdfUserFileName() + ".pdf\"")
                .body(export);
    }

    @DeleteMapping
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public void deleteUsersFromDb() {
        usersRepository.deleteAll();
    }

    @GetMapping("/test")
    public void forTesting(){
        for(int i = 0; i<500; i++){
        }
        logger.info("received req");
    }

}
