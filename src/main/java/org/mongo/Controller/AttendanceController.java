package org.mongo.Controller;

import org.mongo.Entity.Attendance;
import org.mongo.Entity.Response;
import org.mongo.Entity.User;
import org.mongo.Repository.AttendanceRepository;
import org.mongo.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/attendance")
public class AttendanceController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public AttendanceRepository attendanceRepository;

    @Autowired
    public UserRepository userRepository;

    @GetMapping(value = "/all")
    public Response getAllAttendance(){
        Response response = new Response();
        response.setData(attendanceRepository.findAll());
        response.setMessage("Request Completed Successfully");
        response.setError("0");
        return response;
    }

    @PostMapping(value = "/add/{key}")
    public Response addAttendance(@RequestBody final Attendance attendance, @PathVariable final String key){
        Response response = new Response();

        User user = userRepository.getUserByUsername(attendance.getUsername());

        if(user != null) {
            Attendance attendance1 = attendanceRepository.getAttendanceByDateAndUsername(attendance.getDate(), attendance.getUsername());
            if (attendance1 == null) {
                Attendance newAttendance = new Attendance();
                newAttendance.setDate(attendance.getDate());
                newAttendance.setEntryTime(key);
                newAttendance.setMonth(attendance.getMonth());
                newAttendance.setUsername(attendance.getUsername());
                    String[] tokens = key.split(":");
                    int secondsToMs = Integer.parseInt(tokens[2]) * 1000;
                    int minutesToMs = Integer.parseInt(tokens[1]) * 60000;
                    int hoursToMs = Integer.parseInt(tokens[0]) * 3600000;
                    long total = secondsToMs + minutesToMs + hoursToMs;
                    if(total > 37800000){
                        long letetime = total - 37800000;
                        String latet = String.format("%02d:%02d:%02d",
                                TimeUnit.MILLISECONDS.toHours(letetime),
                                TimeUnit.MILLISECONDS.toMinutes(letetime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(letetime)),
                                TimeUnit.MILLISECONDS.toSeconds(letetime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(letetime)));
                        newAttendance.setLateswipetime(latet);
                    }
                response.setData(attendanceRepository.save(newAttendance));
                response.setMessage("Request Completed Successfully");
                response.setError("0");
                return response;
            } else if (attendance1.getUsername().equals(attendance.getUsername())) {

                attendance1.setExitTime(key);

                DateFormat entry = new SimpleDateFormat("hh:mm:ss");
                DateFormat exit = new SimpleDateFormat("hh:mm:ss");

                try {
                    Date exitt = exit.parse(key);
                    Date entryt = entry.parse(attendance1.getEntryTime());
                    long diff = (exitt.getTime() - entryt.getTime());
                    if(diff > 32400000){
                        long over = diff - 32400000;
                        String overt = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(over),
                                TimeUnit.MILLISECONDS.toMinutes(over) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(over)),
                                TimeUnit.MILLISECONDS.toSeconds(over) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(over)));
                        attendance1.setOvertime(overt);
                    }
                    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(diff),
                            TimeUnit.MILLISECONDS.toMinutes(diff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff)),
                            TimeUnit.MILLISECONDS.toSeconds(diff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff)));
                    attendance1.setTotaltime(hms);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                response.setData(attendanceRepository.save(attendance1));
                response.setError("0");
                response.setMessage("Request Completed Successfully");
                return response;
            }
            response.setError("1");
            response.setMessage("Error Adding Attendance");
            return response;
        }
        response.setError("1");
        response.setMessage("User with username "+attendance.getUsername()+" doesn't exist");
        return  response;
    }

    @GetMapping(value = "/get/date/{date}")
    public Response getAttendanceByDate(@PathVariable final String date){
        Response response = new Response();
        Object attendance = attendanceRepository.getAttendanceByDate(date);

        if(attendance == null){
            response.setError("1");
            response.setMessage("No data found for date "+date);
            return response;
        }
        response.setData(attendance);
        response.setError("0");
        response.setMessage("Request Completed Successfully");
        return response;
    }

    @GetMapping(value = "/get/username/{username}")
    public Response getAttendanceByUsername(@PathVariable final String username){
        Response response = new Response();
        Object attendance = attendanceRepository.getByUsername(username);

        if(attendance == null){
            response.setError("1");
            response.setMessage("No data found for "+username);
            return response;
        }
        response.setData(attendance);
        response.setError("0");
        response.setMessage("Request Completed Successfully");
        return response;
    }

    @GetMapping(value = "/get")
    public Response getAttendanceByUsernameDate(@RequestParam final String username, @RequestParam() final String date){
        Response response = new Response();
        Attendance attendance = attendanceRepository.getAttendanceByDateAndUsername(date, username);
        if (attendance == null){
            response.setError("1");
            response.setMessage("No data found for "+username+" on "+date);
            return response;
        }
        response.setData(attendance);
        response.setError("0");
        response.setMessage("Request Successfully Completed");
        return response;
    }

    @GetMapping(value = "/get/monthly")
    public Response getMonthlyAttendance(@RequestParam final String username, @RequestParam final String month){
        Response response = new Response();
        User user = userRepository.getUserByUsername(username);
        if (user != null){
        Object attendance = attendanceRepository.getAttendanceByMonthAndUsername(month, username);
        if (((List) attendance).isEmpty()){
            response.setError("1");
            response.setMessage("No data found for "+username+" for "+month);
            return response;
        }
        response.setData(attendance);
        response.setError("0");
        response.setMessage("Request Successfully Completed");
        return response;
        }
        response.setError("1");
        response.setMessage("User with username "+username+" doesn't exist");
        return response;

    }

    @GetMapping(value = "/get/all/monthly")
    public Response getAllMonthlyAttendance(@RequestParam final String month){
        Response response = new Response();
        Object attendance = attendanceRepository.getAllAttendanceByMonth(month);
        if (attendance == null){
            response.setError("1");
            response.setMessage("No data found for "+month);
            return response;
        }
        response.setData(attendance);
        response.setError("0");
        response.setMessage("Request Successfully Completed");
        return response;
    }
}
