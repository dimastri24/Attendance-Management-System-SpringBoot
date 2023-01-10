package com.dimas.jumpstart.attendance.util;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.dimas.jumpstart.attendance.dao.Attendance;
import com.dimas.jumpstart.attendance.dao.Users;
import com.dimas.jumpstart.attendance.service.AttendanceService;

public class UserExcelExporter {
	
	@Autowired
	private AttendanceService attendanceService;
	
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Users> listUsers;
//    private Date startDate;
//    private Date endDate;
    private String startDate;
    private String endDate;
    
    public UserExcelExporter(List<Users> listUsers) {
        this.listUsers = listUsers;
        workbook = new XSSFWorkbook();
    }
    
//    public UserExcelExporter(List<Users> listUsers, Date startDate, Date endDate) {
//		this.listUsers = listUsers;
//		this.startDate = startDate;
//		this.endDate = endDate;
//		workbook = new XSSFWorkbook();
//	}
    
    public UserExcelExporter(List<Users> listUsers, String startDate, String endDate) {
		super();
		this.listUsers = listUsers;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
    

	private void writeHeaderLine(Users user) { 	
    	String s1 = user.getFullName();
    	s1 = s1.replaceAll("\\s+","");
    	
    	if(s1.length() <= 10) {
    		sheet = workbook.createSheet(s1);
    	} else {
    		sheet = workbook.createSheet(s1.substring(0, 10));
    	}
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "Attendance ID", style);      
        createCell(row, 1, "Check-In time", style);       
        createCell(row, 2, "Check-Out time", style);    
        createCell(row, 3, "Is Late", style);
        createCell(row, 4, "Is Full Day", style);
        createCell(row, 5, "Time Work(Mins)", style);
         
    }
    
	private void writeDataLines(Users user) {
		int rowCount = 1;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);

		List<Attendance> ls = attendanceService.listAllAttendanceFromUserBetweenDate(user, startDate, endDate);

		for (Attendance at : ls) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			createCell(row, columnCount++, at.getAttendanceId(), style);
			createCell(row, columnCount++, at.getCheckIn(), style);
			createCell(row, columnCount++, at.getCheckOut(), style);
			createCell(row, columnCount++, at.isLate(), style);
			createCell(row, columnCount++, at.isFullDay(), style);
			createCell(row, columnCount++, at.getTimeWorkInMins(), style);

		}

	}
    
    public void export(HttpServletResponse response) throws IOException {
    	for (Users user : listUsers) {
    		writeHeaderLine(user);
    		writeDataLines(user);    		
    	}
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }

}
