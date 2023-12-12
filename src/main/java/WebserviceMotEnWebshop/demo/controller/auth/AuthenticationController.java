package WebserviceMotEnWebshop.demo.controller.auth;

import WebserviceMotEnWebshop.demo.database.entity.User;
import WebserviceMotEnWebshop.demo.service.AuthenticationService;
import WebserviceMotEnWebshop.demo.modell.LoginResponse;
import WebserviceMotEnWebshop.demo.modell.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public User registerUser(@RequestBody Registration body){
        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }
    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody Registration body){
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }
    @PostMapping("/login-user")
    public LoginResponse login(@RequestBody Registration body) {
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }
}
