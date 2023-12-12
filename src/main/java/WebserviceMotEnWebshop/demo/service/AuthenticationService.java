package WebserviceMotEnWebshop.demo.service;

import WebserviceMotEnWebshop.demo.database.entity.Role;
import WebserviceMotEnWebshop.demo.database.entity.User;
import WebserviceMotEnWebshop.demo.database.repository.RoleRepository;
import WebserviceMotEnWebshop.demo.database.repository.UserRepository;
import WebserviceMotEnWebshop.demo.modell.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    public User registerUser(String username, String password){

        String encodedPassword = passwordEncoder.encode(password);

        Role userRole = roleRepository.findByAuthority("USER").orElseThrow();

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        return userRepository.save(new User(username, encodedPassword, authorities));
    }

    public LoginResponse loginUser(String username, String password){

        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            String token = tokenService.generateJwt(auth);

            return new LoginResponse(userRepository.findByUsername(username).get(), token);

        } catch(AuthenticationException e){
            return new LoginResponse(null, "");
        }
    }

    public LoginResponse login(String username, String password) {
        try {
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (passwordEncoder.matches(password, user.getPassword())) {
                    Authentication auth = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(username, password)
                    );
                    String token = tokenService.generateJwt(auth);
                    return new LoginResponse(user, token);
                } else throw new RuntimeException("Felaktigt lösenord");
            } else {
                registerUser(username, password);
                Optional<User> newUser = userRepository.findByUsername(username);
                Authentication auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                );
                String token = tokenService.generateJwt(auth);
                return new LoginResponse(newUser.get(), token);
            }
        } catch (AuthenticationException e) {
            throw new RuntimeException("Misslyckades med authentisering");
        }
    }

}


