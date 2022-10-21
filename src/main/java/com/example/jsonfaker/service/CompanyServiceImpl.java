package com.example.jsonfaker.service;

import com.example.jsonfaker.model.Company;
import com.example.jsonfaker.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService{

    private CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Long saveCompany(Company company) {
        return companyRepository.save(company).getId();
    }
}
