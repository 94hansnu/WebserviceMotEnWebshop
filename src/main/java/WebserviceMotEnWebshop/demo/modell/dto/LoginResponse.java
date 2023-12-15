package WebserviceMotEnWebshop.demo.modell.dto;

import WebserviceMotEnWebshop.demo.database.entity.User;

public class LoginResponse {
    private User user;

    private String jwt;

    public LoginResponse(){
        super();
    }

    public LoginResponse(User user, String jwt){
        this.user = user;
        this.jwt = jwt;
    }
    public User getUser(){
        return this.user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public String getJwt(){
        return this.jwt;
    }

    public void setJwt(String jwt){
        this.jwt = jwt;
    }
}
