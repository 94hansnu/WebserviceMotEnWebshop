package WebserviceMotEnWebshop.demo.service;


import WebserviceMotEnWebshop.demo.database.entity.Article;
import WebserviceMotEnWebshop.demo.database.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public Optional<Article> findByName(String articleName) {
        return articleRepository.findByName(articleName);
    }
    public void delete(Article article) {
        Optional<Article> deleteArticle = articleRepository.findByName(article.getName());
        if (deleteArticle.isPresent()) {
            articleRepository.deleteById(deleteArticle.get().getId());
        }

    }
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
            existingArticle.setName(updatedArticle.getName());
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
