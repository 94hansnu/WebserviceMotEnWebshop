package WebserviceMotEnWebshop.demo.controller.History;

import WebserviceMotEnWebshop.demo.database.entity.History;

import WebserviceMotEnWebshop.demo.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/history")
@CrossOrigin("*")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    // skapar ny historik
    @PostMapping("")
    public ResponseEntity <History> addHistory(Authentication authentication, @RequestBody History history){
        if (isAdmin(authentication)) {
            History newHistory = historyService.addHistory(history);
            return ResponseEntity.ok(newHistory);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    // Hämta historik för en specifik användare (admin kan hämta allas och user kan bara hämta sin egen)
    @GetMapping("/user")
    public ResponseEntity<List<History>> getHistoryByUser(Authentication authentication) {
        String authenticatedUserId = authentication.getName();
        if (isAdmin(authentication) || isUserAuthorized(authentication, authenticatedUserId)) {
            List<History> histories = historyService.getHistoryByUser(authenticatedUserId);
            return ResponseEntity.ok(histories);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // Hämta historik för en specifik artikel
    @GetMapping("/article")
    public ResponseEntity<List<History>> getHistoryByArticle(@RequestParam String searchterm, Authentication authentication) {
        if (isAdmin(authentication)) {
            List<History> histories = historyService.getHistoryByArticleOrUser(searchterm);
            return ResponseEntity.ok(histories);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    //hämta all historik, endast tillgängligt för sin egen historik om man inte är admin
    @GetMapping("")
    public ResponseEntity<List<History>> getAllHistory(Authentication authentication) {
        // Kontrollera om användaren är admin innan du tillåter att hämta all historik
        if (isAdmin(authentication)) {
            List<History> histories = historyService.getAllHistory();
            return ResponseEntity.ok(histories);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //hämta historik för en specifik användare baserat på username (endast för admin)
    @GetMapping("/user/{username}")
    public ResponseEntity <List<History>> getHistoryByUsername(@PathVariable String username, Authentication authentication){
        if (isAdmin(authentication)){
            List <History> histories = historyService.getHistoryByUsername(username);
            return ResponseEntity.ok(histories);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    public boolean isAdmin(Authentication authentication) {
        // Implementation för att kontrollera om användaren är admin
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isUserAuthorized(Authentication authentication, String authenticatedUserId) {
        // Kontrollera om användaren är admin, i så fall har den alltid behörighet
        if (isAdmin(authentication)) {
            return true;
        }

        // Jämför användar-ID från autentiseringen med det som tillhandahålls som (authenticatedUserId)
        return authenticatedUserId.equals(authentication.getName());
    }

}


