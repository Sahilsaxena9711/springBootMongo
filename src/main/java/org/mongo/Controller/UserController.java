package org.mongo.Controller;

import org.mongo.Entity.Response;
import org.mongo.Entity.User;
import org.mongo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

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
    public Response addUser(@RequestBody final User user){
            Response response = new Response();
                if(userRepository.getUserByUsername(user.getUsername()) != null){
                    response.setMessage("User with username "+user.getUsername()+" already exist");
                    response.setError("1");
                    return response;
                }
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

}
