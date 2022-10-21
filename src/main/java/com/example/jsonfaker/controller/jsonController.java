package com.example.jsonfaker.controller;

import com.example.jsonfaker.configuration.CustomProps;
import com.example.jsonfaker.model.Address;
import com.example.jsonfaker.model.Company;
import com.example.jsonfaker.model.Geo;
import com.example.jsonfaker.model.Users;
import com.example.jsonfaker.service.UsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/api")
public class jsonController {

    private CustomProps customProps;

    private UsersService usersService;

    public jsonController(CustomProps customProps, UsersService usersService) {
        this.customProps = customProps;
        this.usersService = usersService;
    }

    @GetMapping("/populate")
    public ResponseEntity getData() throws JsonProcessingException {
        final String uri = customProps.getUri();
        RestTemplate restTemplate = new RestTemplate();

        ObjectMapper map = new ObjectMapper();
        JsonNode node = map.readTree(restTemplate.getForObject(uri, String.class));

        if(node.isArray()){
            System.out.println("true");
            for(JsonNode jsonNode : node){
                String fieldId = jsonNode.get("id").asText();
                Users user = new Users();
                user.setName(jsonNode.get("name").asText());
                user.setUsername(jsonNode.get("username").asText());
                user.setEmail(jsonNode.get("email").asText());

                Address address = new Address();
                address.setStreet(jsonNode.get("address").get("street").asText());
                address.setSuite(jsonNode.get("address").get("suite").asText());
                address.setCity(jsonNode.get("address").get("city").asText());
                address.setZipcode(jsonNode.get("address").get("zipcode").asText());

                Geo geo = new Geo();
                geo.setLat(jsonNode.get("address").get("geo").get("lat").asDouble());
                geo.setLng(jsonNode.get("address").get("geo").get("lng").asDouble());

                address.setgeo(geo);
                user.setaddress(address);

                user.setPhone(jsonNode.get("phone").asText());
                user.setWebsite(jsonNode.get("website").asText());

                Company company = new Company();
                company.setName(jsonNode.get("company").get("name").asText());
                company.setCatchPhrase(jsonNode.get("company").get("catchPhrase").asText());
                company.setBs(jsonNode.get("company").get("bs").asText());

                user.setcompany(company);

                usersService.saveUser(user);

            }
        }


        return new ResponseEntity(HttpStatus.CREATED);
    }
}
