package WebserviceMotEnWebshop.demo.controller.History;

import WebserviceMotEnWebshop.demo.service.HistoryService;
import WebserviceMotEnWebshop.demo.table.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
            History newHistory = historyService.addHistory(history, authentication.getName());
            return ResponseEntity.ok(newHistory);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //hämtar all historik (endast för admin)
    @GetMapping("")
    public ResponseEntity<List<History>> getAllHistory(Authentication authentication) {
        // Kontrollera om användaren är admin innan du tillåter att hämta all historik
        if (isAdmin(authentication)) {
            List<History> histories = historyService.getAll();
            return ResponseEntity.ok(histories);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //hämta specifik id, man kan bara hämta sin egen som kund
    @GetMapping("/{id}")
    public ResponseEntity<History> getOneHistory(Authentication authentication, @PathVariable Long id) {
        Optional<History> history = historyService.getOneById(id);

        // Kontrollera om användaren har behörighet att hämta denna historik
        if (history.isPresent() && (isAdmin(authentication) || isUserAuthorized(authentication, history.get()))) {
            return ResponseEntity.ok(history.get());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    public boolean isAdmin(Authentication authentication) {
        // Implementation för att kontrollera om användaren är admin
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isUserAuthorized(Authentication authentication, History history) {
        // Implementation för att kontrollera om användaren har behörighet att hämta denna historik
        return authentication.getName().equals(history.getCustomerId());
    }

}
