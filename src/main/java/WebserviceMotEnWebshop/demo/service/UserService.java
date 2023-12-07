package WebserviceMotEnWebshop.demo.service;

import WebserviceMotEnWebshop.demo.table.Customer;
import WebserviceMotEnWebshop.demo.table.dao.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer getUserByUsername(String username){
        return customerRepository.findByUsername(username);
    }
}
