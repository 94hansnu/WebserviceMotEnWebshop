package WebserviceMotEnWebshop.demo.controller;

import WebserviceMotEnWebshop.demo.database.entity.Article;
import WebserviceMotEnWebshop.demo.service.ShopService;
import WebserviceMotEnWebshop.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ShopService shopService;

    //GET-förfrågan- Hämta alla artiklar (För alla användare)
    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles(@RequestParam String searchTerm) {
        List<Article> articles = shopService.getArticles(searchTerm);
        return ResponseEntity.ok(articles);
    }
    @PostMapping("/create")
    public ResponseEntity<Article> create(@RequestBody Article article) {
        Optional<Article> optionalArticle = articleService.findByName(article.getName());
        if(optionalArticle.isPresent()) {
            Article present = optionalArticle.get();
            present.setPrice(article.getPrice());
            present.setDescription(article.getDescription());
            return ResponseEntity.ok(articleService.createArticle(present));
        }
        return ResponseEntity.ok(articleService.createArticle(article));
    }
    @PutMapping("/update")
    public ResponseEntity<Article> update(@RequestBody Article article) {
        Optional<Article> isExsisting = articleService.findByName(article.getName());
        if(isExsisting.isPresent())  {
            return ResponseEntity.ok(articleService.createArticle(isExsisting.get()));
        }
        return ResponseEntity.notFound().build();
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
        Article createdArticle  = articleService.createArticle(article);
        return ResponseEntity.ok(createdArticle);
    }

    //PUT-förfrågan- Uppdatera en artikel (ADMIN)
    @PutMapping("/{articleId}")
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
    @DeleteMapping("/delete")
    public ResponseEntity deleteArticle(@RequestBody Article article, Authentication authentication) {
        articleService.delete(article);
        return ResponseEntity.ok(article);
    }
}
