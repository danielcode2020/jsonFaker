package com.example.jsonfaker.service;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.example.jsonfaker.model.Users;

import com.example.jsonfaker.repository.UsersRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.oasis.StyleBuilder;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final UsersRepository usersRepository;

    private final JdbcTemplate jdbcTemplate;

    public ReportService(UsersRepository usersRepository, JdbcTemplate jdbcTemplate) {
        this.usersRepository = usersRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void exportReport(String reportFormat) throws FileNotFoundException, JRException {
        List<Users> usersList = (List<Users>) usersRepository.findAll();

        File file = ResourceUtils.getFile("classpath:jasper/users.jrxml");
        JasperDesign jasperDesign;
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(usersList);
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("daniel","piva");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);

//        JasperViewer.viewReport(jasperPrint);
        JasperExportManager.exportReportToHtmlFile(jasperPrint,"jasperreport.html");
    }

    public void exportCrossTab() throws FileNotFoundException, JRException {
//        List<Users> usersList = usersRepository.findForCrosstab();
        List<Users> usersList = (List<Users>) usersRepository.findAll();

        File file = ResourceUtils.getFile("classpath:jasper/users_crosstab.jrxml");
        JasperDesign jasperDesign;
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(usersList);
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("daniel","piva");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);

//        JasperViewer.viewReport(jasperPrint);
        JasperExportManager.exportReportToHtmlFile(jasperPrint,"crosstab.html");


    }

    public void exportReportJDBC() throws FileNotFoundException, JRException {

        File file = ResourceUtils.getFile("classpath:jasper/users_jdbc.jrxml");
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            Map<String,Object> parameters = new HashMap<>();
            parameters.put("daniel","piva");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,conn);

            JasperExportManager.exportReportToPdfFile(jasperPrint,"jasperreport_jdbc.pdf");
        } catch (SQLException e) {
            throw new RuntimeException("unable to establish jdbc connection");
        }
    }




}
