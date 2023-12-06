package WebserviceMotEnWebshop.demo.utils;

import WebserviceMotEnWebshop.demo.model.KundTabell;

public class LoginResponse {
    private KundTabell user;

    private String jwt;

    public LoginResponse(){
        super();
    }

    public LoginResponse(KundTabell user, String jwt){
        this.user = user;
        this.jwt = jwt;
    }
    public KundTabell getUser(){
        return this.user;
    }

    public void setUser(KundTabell user){
        this.user = user;
    }

    public String getJwt(){
        return this.jwt;
    }

    public void setJwt(String jwt){
        this.jwt = jwt;
    }
}
