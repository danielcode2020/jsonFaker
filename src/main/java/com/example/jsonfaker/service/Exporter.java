package com.example.jsonfaker.service;

import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class Exporter {

    public String exportXMLFileName(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        return "users-faker-xml" + currentDateTime;
    }

    public String exportFileNameQR(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        return "QR-" + currentDateTime;
    }

    public String exportPdfUserFileName(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        return "User-" + currentDateTime;
    }


}
