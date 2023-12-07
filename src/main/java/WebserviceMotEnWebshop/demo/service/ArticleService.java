package WebserviceMotEnWebshop.demo.service;

import WebserviceMotEnWebshop.demo.table.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article getArticleById(Long articleId) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        return optionalArticle.orElse(null);
    }

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    public Article updateArticle(Long articleId, Article updatedArticle) {
        Optional<Article>  optionalArticle = articleRepository.findById(articleId);

        if (optionalArticle.isPresent()) {
            Article existingArticle = optionalArticle.get();
            existingArticle.setTitle(updatedArticle.getTitle());
            existingArticle.setDescription(updatedArticle.getDescription());
            existingArticle.setPrice(updatedArticle.getPrice());

            return articleRepository.save(existingArticle);
        } else {
            return null;
        }
    }

    public boolean deleteArticle(Long articleId)  {
        if (articleRepository.existsById(articleId)) {
            articleRepository.deleteById(articleId);
            return true;
        } else {
            return false;
        }
    }
}
