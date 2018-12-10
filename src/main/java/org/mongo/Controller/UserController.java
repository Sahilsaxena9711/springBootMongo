package org.mongo.Controller;

import org.mongo.Entity.Response;
import org.mongo.Entity.User;
import org.mongo.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedList;

@RestController
@RequestMapping(value = "/user")

public class UserController {
//    Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    public UserRepository userRepository;

    @GetMapping(value = "/all")
    public Response allUser(){
        Response response = new Response();
            response.setData(userRepository.findAll());
            response.setMessage("Request Completed Successfully");
            response.setError("0");
        return response;
    }

    @PostMapping(value = "/add")
    public Response addUser(@RequestBody final User user) throws Exception {
            Response response = new Response();
                if(userRepository.getUserByUsername(user.getUsername()) != null){
                    response.setMessage("User with username "+user.getUsername()+" already exist");
                    response.setError("1");
                    return response;
                }
//        this.sendEmail(user.getEmail());
        response.setMessage("Request Completed Successfully");
        response.setError("0");
        response.setData(userRepository.save(user));
        return response;
    }

    @GetMapping(value = "/username/{username}")
    public Response getUserByUserName(@PathVariable String username){
        Response response = new Response();
            User user = userRepository.getUserByUsername(username);
            if(user == null){
                response.setMessage("User with username : "+username+" doesn't exist");
                response.setError("1");
                return response;
            }
        response.setMessage("Request Completed Successfully");
        response.setError("0");
        response.setData(user);
            return response;
    }

    @DeleteMapping(value = "/delete/{username}")
    public Response deleteUserByUsername(@PathVariable String username){
        Response response = new Response();
        if(userRepository.getUserByUsername(username) != null){
            userRepository.deleteUserByUsername(username);
            response.setMessage("Request Completed successfully");
            response.setError("0");
            return response;
        }
        response.setMessage("Error User with username "+username+" not found");
        response.setError("1");
        return response;
    }

    @PostMapping(value = "/login")
    public Response loginRequest(@RequestBody User user){
        Response response = new Response();
        User users = userRepository.getUserByUsername(user.getUsername());
//    logger.info(users.toString());
        if(users == null){
            response.setError("1");
            response.setMessage("Invalid Username or Password");
            return response;
        }else if(users != null){
//            logger.info(user.getPassword());
//            logger.info(users.getPassword());
            if(users.getPassword().equals(user.getPassword())){
                HashMap<String, String> hm = new HashMap<>();
                hm.put("token","");
                hm.put("username", user.getUsername());
                response.setError("0");
                response.setData(hm);
                response.setMessage("Login Successful");
                return response;
            }else{
                response.setError("1");
                response.setMessage("Invalid Username or Password");
                return response;
            }
        }
        return response;
    }

//    private void sendEmail( String email) throws Exception{
//
//        MimeMessage message = sender.createMimeMessage();
//
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//
//        helper.setTo(email);
//
//        helper.setText("How are you?");
//
//        helper.setSubject("Hi");
//        sender.send(message);
//    }


}
