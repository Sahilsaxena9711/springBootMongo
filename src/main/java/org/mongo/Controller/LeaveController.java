package org.mongo.Controller;


import org.mongo.Entity.Leave;
import org.mongo.Entity.Response;
import org.mongo.Entity.User;
import org.mongo.Repository.AttendanceRepository;
import org.mongo.Repository.LeaveRepository;
import org.mongo.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "leave")
public class LeaveController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public LeaveRepository leaveRepository;

    @Autowired
    public AttendanceRepository attendanceRepository;

    @Autowired
    public UserRepository userRepository;

    @PostMapping(value = "/apply")
    public Response applyLeave(@RequestBody final Leave leave){
        Response response = new Response();
        User user = userRepository.getUserByUsername(leave.getUsername());
        if(user != null ){
            Leave leave1 = leaveRepository.getLeaveByUsernameAndDate(leave.getUsername(), leave.getDate());
            if(leave1 != null){
                response.setError("1");
                response.setMessage("Leave was already applied for "+leave.getDate());
                return  response;
            }
            response.setData(leaveRepository.save(leave));
            response.setError("0");
            response.setMessage("Leave has been applied successfully, you will be informed once it is approved or unapproved!");
            return  response;
        }
        response.setMessage("User with username "+leave.getUsername()+" not found!");
        response.setError("1");
        return response;
    }

    @GetMapping(value = "/all/unapproved")
    public Response allUnapproveLeave(){
        Response response = new Response();
        boolean approved = false;
        Object allLeaves = leaveRepository.getByApproved(approved);
        response.setMessage("Request Completed Successfully!");
        response.setError("0");
        response.setData(allLeaves);
        return response;
    }

    @GetMapping(value = "/approved")
    public Response approveLeave(@RequestParam final String username, @RequestParam final String date, @RequestParam final boolean approved){
        Response response = new Response();

        User user = userRepository.getUserByUsername(username);
        logger.info(String.valueOf(user));
        if(user != null){
            Leave leave = leaveRepository.getLeaveByUsernameAndDate(username, date);
            if(leave != null){
                leave.setApproved(approved);
                response.setMessage("Leave Approved Successfully!");
                response.setError("0");
                response.setData(leaveRepository.save(leave));
                return response;
            }
            response.setError("1");
            response.setMessage("No leaves found for "+username+" on "+date+"!");
            return response;
        }
        response.setError("1");
        response.setMessage("User with username "+username+" doesn't exist!");
        return response;
    }

    @GetMapping(value = "/myleaves")
    public Response myLeaves(@RequestParam final String username){
        Response response = new Response();
        User user = userRepository.getUserByUsername(username);
        logger.info(String.valueOf(user));
        if(user != null) {
            Object myleaves = leaveRepository.getLeaveByUsername(username);
            response.setData(myleaves);
            response.setError("0");
            response.setMessage("Request Completed Successfully!");
            return response;
        }
        response.setError("1");
        response.setMessage("User with username "+username+" doesn't exist!");
        return response;
    }
    @GetMapping(value = "/monthly")
    public Response monthlyLeave(@RequestParam final String username, @RequestParam final String month){
        Response response = new Response();
        User user = userRepository.getUserByUsername(username);
        logger.info(String.valueOf(user));
        if(user != null) {
            Object myleaves = leaveRepository.getLeaveByUsernameAndMonth(username, month);
            response.setData(myleaves);
            response.setError("0");
            response.setMessage("Request Completed Successfully!");
            return response;
        }
        response.setError("1");
        response.setMessage("User with username "+username+" doesn't exist!");
        return response;
    }

    @GetMapping(value = "/delete")
    public Response deleteLeave(@RequestParam final String username, @RequestParam final String date){
        Response response = new Response();
        User user = userRepository.getUserByUsername(username);
        logger.info(String.valueOf(user));
        if(user != null) {
            Leave leave = leaveRepository.getLeaveByUsernameAndDate(username, date);
            leaveRepository.delete(leave);
            response.setError("0");
            response.setMessage("Leave Deleted Successfully!");
            return  response;
        }
        response.setError("1");
        response.setMessage("User with username "+username+" doesn't exist!");
        return response;
    }

    @GetMapping(value = "/today")
    public Response getLeaveForToday(@RequestParam final String username, @RequestParam final String date){
        Response response = new Response();
        User user = userRepository.getUserByUsername(username);
        logger.info(String.valueOf(user));
        if(user != null) {
            Leave leave = leaveRepository.getLeaveByUsernameAndDate(username, date);
            response.setData(leave);
            response.setError("0");
            response.setMessage("Request Completed Successfully!");
            return  response;
        }
        response.setError("1");
        response.setMessage("User with username "+username+" doesn't exist!");
        return response;
    }



}
