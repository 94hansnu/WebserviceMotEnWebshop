package WebserviceMotEnWebshop.demo.controller.History;

import WebserviceMotEnWebshop.demo.database.entity.Article;
import WebserviceMotEnWebshop.demo.database.entity.History;
import WebserviceMotEnWebshop.demo.database.entity.User;
import WebserviceMotEnWebshop.demo.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
            History newHistory = historyService.addHistory(history);
            return ResponseEntity.ok(newHistory);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    // Hämta historik för en specifik användare
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<History>> getHistoryByUser(@PathVariable Long userId, Authentication authentication) {
        if (isAdmin(authentication) || isUserAuthorized(authentication, userId)) {
            List<History> histories = historyService.getHistoryByUser(userId);
            return ResponseEntity.ok(histories);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // Hämta historik för en specifik artikel
    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<History>> getHistoryByArticle(@PathVariable Long articleId, Authentication authentication) {
        if (isAdmin(authentication)) {
            List<History> histories = historyService.getHistoryByArticle(articleId);
            return ResponseEntity.ok(histories);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    //hämta all historik, endast tillgängligt för användaren
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

    // hämta historik för en specifik användare

  /*  @GetMapping("/user/{userId}")
    public ResponseEntity<List<History>> getHistoryByUser(@PathVariable Long userId, Authentication authentication) {
        if (isAdmin(authentication) || isUserAuthorized(authentication, userId)) {
            User user = new User(); // Skapa ett User-objekt med det specifika användar-ID:et
            user.setId(userId);
            List<History> histories = (List<History>) historyService.getHistoryByUser(user);
            return ResponseEntity.ok(histories);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // hämta historik för en specifik artikel
    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<History>> getHistoryByArticle(@PathVariable Long articleId, Authentication authentication) {
        if (isAdmin(authentication)) {
            Article article = new Article(); // Skapa ett Article-objekt med det specifika artikel-ID:et
            article.setId(articleId);
            List<History> histories = (List<History>) historyService.getHistoryByArticle(article);
            return ResponseEntity.ok(histories);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }*/

    public boolean isAdmin(Authentication authentication) {
        // Implementation för att kontrollera om användaren är admin
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isUserAuthorized(Authentication authentication, Long userId) {
        // Kontrollera om användaren är admin, i så fall har den alltid behörighet
        if (isAdmin(authentication)) {
            return true;
        }

        // Jämför användar-ID från autentiseringen med det som tillhandahålls som (userId)
        String authenticatedUserId = authentication.getName(); // Antag att användar-ID:et är lagrat i användarens namn
        return authenticatedUserId.equals(String.valueOf(userId));
    }


//osäker om den ska vara kvar
  /*
    //hämta specifik id, man kan bara hämta sin egen som kund, osäker om den ska vara kvar
    @GetMapping("/{id}")
    public ResponseEntity<History> getOneHistory(Authentication authentication, @PathVariable Long id) {
        Optional<History> history = historyService.getHistoryById(id);

        // Kontrollera om användaren har behörighet att hämta denna historik
        if (history.isPresent() && (isAdmin(authentication) || isUserAuthorized(authentication, history.get()))) {
            return ResponseEntity.ok(history.get());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }*/
}
