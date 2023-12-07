package WebserviceMotEnWebshop.demo.modell;

import WebserviceMotEnWebshop.demo.table.Customer;

public class LoginResponse {
    private Customer user;

    private String jwt;

    public LoginResponse(){
        super();
    }

    public LoginResponse(Customer user, String jwt){
        this.user = user;
        this.jwt = jwt;
    }
    public Customer getUser(){
        return this.user;
    }

    public void setUser(Customer user){
        this.user = user;
    }

    public String getJwt(){
        return this.jwt;
    }

    public void setJwt(String jwt){
        this.jwt = jwt;
    }
}
