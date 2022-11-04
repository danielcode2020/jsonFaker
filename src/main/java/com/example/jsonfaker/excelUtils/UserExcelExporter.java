package com.example.jsonfaker.excelUtils;

import com.example.jsonfaker.model.dto.UserExportDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<UserExportDTO> userExportDTOList;

    public UserExcelExporter(List<UserExportDTO> userExportDTOList) {
        this.userExportDTOList = userExportDTOList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Users");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "name", style);
        createCell(row, 1, "username", style);
        createCell(row, 2, "email", style);
        createCell(row, 3, "street", style);
        createCell(row, 4, "suite", style);
        createCell(row, 5, "city", style);
        createCell(row, 6, "zipcode", style);
        createCell(row, 7, "lat", style);
        createCell(row, 8, "lng", style);
        createCell(row, 9, "phone", style);
        createCell(row, 10, "website", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (UserExportDTO user : userExportDTOList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, user.getName(), style);
            createCell(row, columnCount++, user.getUsername(), style);
            createCell(row, columnCount++, user.getEmail(), style);
            createCell(row, columnCount++, user.getStreet(), style);
            createCell(row, columnCount++, user.getSuite(), style);
            createCell(row, columnCount++, user.getCity(), style);
            createCell(row, columnCount++, user.getZipcode(), style);
            createCell(row, columnCount++, user.getLat().toString(), style);
            createCell(row, columnCount++, user.getLng().toString(), style);
            createCell(row, columnCount++, user.getWebsite(), style);

        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }

}
