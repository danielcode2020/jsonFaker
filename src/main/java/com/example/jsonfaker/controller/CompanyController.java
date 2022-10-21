package com.example.jsonfaker.controller;
import com.example.jsonfaker.model.Company;
import com.example.jsonfaker.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @PostMapping("/add")
    public Long addCompany(@RequestBody Company company){
        return companyService.saveCompany(company);
    }
}
