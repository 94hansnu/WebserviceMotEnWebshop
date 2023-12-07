package WebserviceMotEnWebshop.demo.controller.Article;

import WebserviceMotEnWebshop.demo.table.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    //private ArticleService articleService;

    //GET-förfrågan- Hämta alla artiklar (För alla användare)
    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    //GET-förfrågan- Hämta en specifik artikel (för alla användare)
    @GetMapping("/{articleId}")
    public ResponseEntity<Article> getArticles(@PathVariable Long articleId) {
        Article article = articleService.getArticleById(articleId);
        if (article != null) {
            return ResponseEntity.ok(article);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //POST-förfrågan- Skapa en ny artikel (ADMIN)
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article createdArticle  = articleService.createdArticle(article);
        return ResponseEntity.ok(createdArticle);
    }

    //PUT-förfrågan- Uppdatera en artikel (ADMIN)
    @PutMapping("/{articleId")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Article> updateArticle(@PathVariable Long articleId, @RequestBody Article updatedArticle) {
        Article article  = articleService.updateArticle(articleId, updatedArticle);
        if (article != null) {
            return ResponseEntity.ok(article);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //DELETE-förfrågan- Radera en artikel (ADMIN)
    @DeleteMapping("/{articleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long article) {
        boolean deleted = articleService.deleteArticle(articleId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
