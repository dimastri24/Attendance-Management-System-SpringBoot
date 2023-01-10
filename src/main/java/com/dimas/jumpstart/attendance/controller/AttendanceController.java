package com.dimas.jumpstart.attendance.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dimas.jumpstart.attendance.dao.Attendance;
import com.dimas.jumpstart.attendance.dao.Stores;
import com.dimas.jumpstart.attendance.dao.Users;
import com.dimas.jumpstart.attendance.exception.ResourceNotFoundException;
import com.dimas.jumpstart.attendance.payload.TwoDatesInString;
import com.dimas.jumpstart.attendance.repo.AttendanceRepo;
import com.dimas.jumpstart.attendance.repo.UsersRepo;
import com.dimas.jumpstart.attendance.service.AttendanceService;
import com.dimas.jumpstart.attendance.service.StoresService;
import com.dimas.jumpstart.attendance.service.UsersPrincipal;
import com.dimas.jumpstart.attendance.service.UsersService;
import com.dimas.jumpstart.attendance.util.UserExcelExporter;

@RestController
@RequestMapping(value="/jmpstart/attend")
public class AttendanceController {
	
	@Autowired
	private UsersRepo usersRepo;
	
	@Autowired AttendanceRepo attendanceRepo;
	
	@Autowired
	private AttendanceService attendanceService;
	
	@Autowired
	private StoresService storesService;
	
	@Autowired
	private UsersService usersService;
	
	DateFormat tsf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	DateFormat tf = new SimpleDateFormat("HH:mm");
	
	@GetMapping(value = "/check/in")
	@Secured({"ROLE_EMPLOYEE","ROLE_MANAGER"})
	public ResponseEntity<?> CheckInAttend(@CurrentUser UsersPrincipal usersPrincipal) throws ParseException {
		
		Users user =  usersRepo.findById((usersPrincipal.getUserId())) 
    	        .orElseThrow(() -> new ResourceNotFoundException("Users", "userId", usersPrincipal.getUserId()));
		
		List<Attendance> ls = attendanceService.listAllAttendanceFromUser(user);
		
		for(Attendance at : ls) {
			System.out.println("Inside for loop" + ls);
			
			if (at.getCheckIn().getTime() == at.getCheckOut().getTime() 
					&& at.isHasCheckIn() == true && at.isHasCheckOut() == false) {
				System.out.println(at.getAttendanceId());
				
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
						"This user already chek in! Please check out first to do another check in.");
				
			}
		}
		
		Date timestamp = new Date();
		
//		Date clockEntry = user.getStore().getClockEntry();
//		String StringCE = tf.format(clockEntry);
//		System.out.println(StringCE);
//		Date DateCE = tf.parse(StringCE);
		
		Calendar checkin = dateToCalendar(timestamp);
		Calendar storeentry = dateToCalendar(user.getStore().getClockEntry());
		
		Attendance attendance = new Attendance();
		attendance.setCheckIn(timestamp);
		attendance.setCheckOut(timestamp);
		attendance.setHasCheckIn(true);
		attendance.setUser(user);
		
//		System.out.println("checkin: " + tsf.format(checkin.getTime()));
//		System.out.println("storeentry: " + tsf.format(storeentry.getTime()));
//		
//		boolean isLate = checkin.after(storeentry);
//		if (isLate) {
//			attendance.setLate(true);
//			System.out.println("You are late");
//		} else {
//			attendance.setLate(false);
//			System.out.println("You are Not Late");
//		}
		
		long checkHoursMillies = TimeUnit.MILLISECONDS.convert(checkin.get(Calendar.HOUR_OF_DAY), TimeUnit.HOURS);
		long checkMinsMillies = TimeUnit.MILLISECONDS.convert(checkin.get(Calendar.MINUTE), TimeUnit.MINUTES);
		long checkMillies = checkHoursMillies + checkMinsMillies;
		
		long entryHourMillies = TimeUnit.MILLISECONDS.convert(storeentry.get(Calendar.HOUR_OF_DAY), TimeUnit.HOURS);
		long entryMinsMillies = TimeUnit.MILLISECONDS.convert(storeentry.get(Calendar.MINUTE), TimeUnit.MINUTES);
		long entryMillies = entryHourMillies + entryMinsMillies;
		
		boolean isLate = checkMillies >= entryMillies;
		if (isLate) {
			attendance.setLate(true);
			System.out.println("You are late");
		} else {
			attendance.setLate(false);
			System.out.println("You are Not Late");
		}
		
		attendanceRepo.save(attendance);
		
		return ResponseEntity.status(HttpStatus.OK).body("ChekIn successfully execute.");
	}
	
	@GetMapping(value = "/check/out")
	@Secured({"ROLE_EMPLOYEE","ROLE_MANAGER"})
	public ResponseEntity<?> CheckOutAttend(@CurrentUser UsersPrincipal usersPrincipal) {
		
		Users user =  usersRepo.findById((usersPrincipal.getUserId())) 
    	        .orElseThrow(() -> new ResourceNotFoundException("Users", "userId", usersPrincipal.getUserId()));

		Stores store = storesService.getById(user.getStore().getStoreId());
		
		long intervalInMillies = Math.abs(store.getClockOut().getTime() - store.getClockEntry().getTime());
		long intervalInMins = TimeUnit.MINUTES.convert(intervalInMillies, TimeUnit.MILLISECONDS);
		
		List<Attendance> ls = attendanceService.listAllAttendanceFromUser(user);
		
		for (Attendance at : ls) {
			System.out.println("Inside for loop" + ls);
			
			if (at.getCheckIn().getTime() == at.getCheckOut().getTime() 
					&& at.isHasCheckIn() == true && at.isHasCheckOut() == false) {
				System.out.println(at.getAttendanceId());
				
				Date timeStamp = new Date();
				
				Attendance attendance = attendanceService.getById(at.getAttendanceId());
				
				attendance.setCheckOut(timeStamp);
				attendance.setHasCheckOut(true);
				
				long diffInMillies = Math.abs(timeStamp.getTime() - attendance.getCheckIn().getTime());
				long diffInMins = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
				
				attendance.setTimeWorkInMins(diffInMins);
				
				if (diffInMins >= intervalInMins) {
					attendance.setFullDay(true);
				}
				
				attendanceRepo.save(attendance);
				
				break;
				
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"This user has not check in!. Please do check in first.");
			}
			
		}
		System.out.println("outside for loop");
		
		return ResponseEntity.status(HttpStatus.OK).body("ChekOut successfully execute.");
	}
	
//	@GetMapping(value = "/list/user")
//	@Secured({"ROLE_MANAGER", "ROLE_EMPLOYEE"})
//	public List<Attendance> listAttendanceFromUser(@CurrentUser UsersPrincipal usersPrincipal) {
//		Users user =  usersService.findById(usersPrincipal.getUserId());
//		
//		List<Attendance> list = attendanceService.listAllAttendanceFromUser(user);
//		
//		return list;
//	}
	
	@GetMapping(value = "/list")
	@Secured({"ROLE_MANAGER"})
	public List<Attendance> listAttendanceFromStore(@CurrentUser UsersPrincipal usersPrincipal) {
		Users user =  usersService.findById(usersPrincipal.getUserId());
		
		List<Attendance> list = attendanceService.listAllAttendanceFromStore(user.getStore());
		
		return list;
	}
	
	@GetMapping(value = "/{aid}")
	@Secured("ROLE_MANAGER")
	public Attendance getAttendanceById(@PathVariable Long aid) {
		return attendanceService.getById(aid);
	}
	
	@DeleteMapping(value = "/{aid}")
	@Secured({"ROLE_MANAGER"})
	public ResponseEntity<?> deleteAttendance(@PathVariable Long aid){
		attendanceService.deleteAttendance(aid);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body("Attendance successfully deleted!");
	}
	
	@PutMapping(value = "/{aid}")
	@Secured("ROLE_MANAGER")
	public ResponseEntity<?> editAttendance(@PathVariable Long aid, @RequestBody ParamEditAttendance editAttendance) throws ParseException {
		Attendance attendance = editAttendance.getAttendance();
		TwoDatesInString datesInString = editAttendance.getTwoDatesInString();
		
		Date checkIn = tsf.parse(datesInString.getStart());
		Date checkkOut = tsf.parse(datesInString.getEnd());
		
		attendance.setCheckIn(checkIn);
		attendance.setCheckOut(checkkOut);
		
		attendanceService.updateAttendance(aid, attendance);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body("Attendance successfully updated!");
	}
	
	@GetMapping(value = "/export/excel")
	@Secured("ROLE_MANAGER")
	public ResponseEntity<?> generateReportExcel(HttpServletResponse response, 
			@CurrentUser UsersPrincipal usersPrincipal, @RequestBody TwoDatesInString twoDates) throws IOException, ParseException {
		
		response.setContentType("application/octet-stream");
        //DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = tsf.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=attendance_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        
        Users user =  usersService.findById(usersPrincipal.getUserId());
        
        List<Users> listUsers = usersService.listAllUsersFromStore(user.getStore());
        
//        Date start = df.parse(twoDates.getStart());
//        Date end = df.parse(twoDates.getEnd());
//        
//        UserExcelExporter excelExporter = new UserExcelExporter(listUsers, start, end);
        
        UserExcelExporter excelExporter = new UserExcelExporter(listUsers, twoDates.getStart(), twoDates.getEnd());
        
        excelExporter.export(response);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body("Report Excek successfuly generated");
	}
	
	
	public static Calendar dateToCalendar(Date date){ 
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(date);
		  return cal;
		}
	
}

class ParamEditAttendance {
	private Attendance attendance;
	private TwoDatesInString twoDatesInString;
	public Attendance getAttendance() {
		return attendance;
	}
	public void setAttendance(Attendance attendance) {
		this.attendance = attendance;
	}
	public TwoDatesInString getTwoDatesInString() {
		return twoDatesInString;
	}
	public void setTwoDatesInString(TwoDatesInString twoDatesInString) {
		this.twoDatesInString = twoDatesInString;
	}
}

