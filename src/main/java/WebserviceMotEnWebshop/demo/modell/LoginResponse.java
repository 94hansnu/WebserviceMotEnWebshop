package WebserviceMotEnWebshop.demo.modell;

import WebserviceMotEnWebshop.demo.table.Kund;

public class LoginResponse {
    private Kund user;

    private String jwt;

    public LoginResponse(){
        super();
    }

    public LoginResponse(Kund user, String jwt){
        this.user = user;
        this.jwt = jwt;
    }
    public Kund getUser(){
        return this.user;
    }

    public void setUser(Kund user){
        this.user = user;
    }

    public String getJwt(){
        return this.jwt;
    }

    public void setJwt(String jwt){
        this.jwt = jwt;
    }
}
