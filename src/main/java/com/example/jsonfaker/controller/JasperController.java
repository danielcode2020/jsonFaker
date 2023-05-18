package com.example.jsonfaker.controller;

import com.example.jsonfaker.service.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/jasper")
public class JasperController {
    private final ReportService reportService;

    public JasperController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public void export(){
        try {
            reportService.exportReport("pdf");
        } catch (FileNotFoundException | JRException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/crosstab")
    public void exportCrosstab(){
        try {
            reportService.exportCrossTab();
        } catch (FileNotFoundException | JRException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/jdbc")
    public void exportJdbc(){
        try {
            reportService.exportReportJDBC();
        } catch (FileNotFoundException | JRException e) {
            throw new RuntimeException(e);
        }
    }

}
