package WebserviceMotEnWebshop.demo.controller.auth;

import WebserviceMotEnWebshop.demo.model.KundTabell;
import WebserviceMotEnWebshop.demo.service.AuthenticationService;
import WebserviceMotEnWebshop.demo.utils.LoginResponse;
import WebserviceMotEnWebshop.demo.utils.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public KundTabell registerUser(@RequestBody Registration body){

        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }
    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody Registration body){
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }
}
