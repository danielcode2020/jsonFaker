package employeeDynamic;

import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.lang.reflect.Field;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Collection<Employee> list = new ArrayList<>();

        list.add(new Employee(101, "Ravinder Shah",  67000, (float) 2.5));
        list.add(new Employee(102, "John Smith",  921436, (float) 9.5));
        list.add(new Employee(103, "Kenneth Johnson",  73545, (float) 1.5));
        list.add(new Employee(104, "John Travolta",  43988, (float) 0.5));
        list.add(new Employee(105, "Peter Parker",  93877, (float) 3.5));
        list.add(new Employee(106, "Leonhard Euler",  72000, (float) 2.3));
        list.add(new Employee(107, "William Shakespeare",  33000, (float) 1.4));
        list.add(new Employee(108, "Arup Bindal",  92000, (float) 6.2));
        list.add(new Employee(109, "Arin Kohfman",  55000, (float) 8.5));
        list.add(new Employee(110, "Albert Einstein",  89000, (float) 8.2));


//        EmployeeReport report = new EmployeeReport(list);
//
//        try {
//            JasperPrint jp = report.getReport();
//            JasperViewer jasperViewer = new JasperViewer(jp);
//            jasperViewer.setVisible(true);
//
//        } catch (JRException | ColumnBuilderException | ClassNotFoundException ex) {
//
//        }
    }
}