package com.example.jsonfaker.pdfUtils;

import com.example.jsonfaker.model.Users;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.security.core.parameters.P;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PdfUserExporter {

    public PdfUserExporter() {
    }

    public byte[] exportUserById(Users users) throws MalformedURLException {
        ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(out));

        Document doc = new com.itextpdf.layout.Document(pdfDoc);


        pdfDoc.addNewPage();

        String userString = "Report of user with id " +
                users.getId();

        Paragraph paragraph = new Paragraph(userString);
        ImageData data = ImageDataFactory.create(new URL("https://memorynotfound.com/wp-content/uploads/java-duke.png"));
        paragraph.add(new Image(data).setWidth(75f).setHeight(75f));

        doc.add(paragraph);

        float [] pointColumnWidths = {150f, 250f};
        Table table = new Table(pointColumnWidths);
        table.setFixedLayout();

        List<Cell> cellList  = new ArrayList<>();
        cellList.add(new Cell().add(new Paragraph("Name").setBold()));

        cellList.add(new Cell().add(new Paragraph(users.getName())));

        cellList.add(new Cell().add(new Paragraph("Email").setBold()));

        cellList.add(new Cell().add(new Paragraph(users.getEmail())));

        cellList.add(new Cell().add(new Paragraph("Phone").setBold()));

        cellList.add(new Cell().add(new Paragraph(users.getPhone())));

        cellList.add(new Cell().add(new Paragraph("Website").setBold()));

        cellList.add(new Cell().add(new Paragraph(users.getWebsite())));

        cellList.add(new Cell().add(new Paragraph("Address").setBold()));

        String addressString = "Street : " + users.getAddress().getStreet() +
                "\n City : " + users.getAddress().getCity()+
                "\n Suite : " + users.getAddress().getSuite()+
                "\n Zipcode : " + users.getAddress().getZipcode();

        cellList.add(new Cell().add(new Paragraph(addressString)));

        cellList.add(new Cell().add(new Paragraph("Company").setBold()));

        String companyString = "Name : " + users.getCompany().getName() +
                "\n Bs : " + users.getCompany().getBs() +
                "\n CatchPhrase : " + users.getCompany().getCatchPhrase();

        cellList.add(new Cell().add(new Paragraph(companyString)));

        cellList.forEach(table::addCell);

        doc.add(table);

        doc.close();
        pdfDoc.close();

        return out.toByteArray();
    }
}
