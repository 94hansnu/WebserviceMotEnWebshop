package WebserviceMotEnWebshop.demo.service;


import WebserviceMotEnWebshop.demo.database.entity.Article;
import WebserviceMotEnWebshop.demo.database.entity.History;
import WebserviceMotEnWebshop.demo.database.entity.User;
import WebserviceMotEnWebshop.demo.database.repository.ArticleRepository;
import WebserviceMotEnWebshop.demo.database.repository.HistoryRepository;
import WebserviceMotEnWebshop.demo.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public History addHistory(History history) {
        // Implementation för att lägga till historik
        return historyRepository.save(history);
    }
    public List<History> getHistoryByUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("Användaren hittades inte.");
        }
        User existingUser = optionalUser.get();
        return historyRepository.findByUser(existingUser);
    }

    public List<History> getHistoryByArticle(Long articleId) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isEmpty()) {
            throw new RuntimeException("Artikeln hittades inte.");
        }
        Article existingArticle = optionalArticle.get();
        return historyRepository.findByArticle(existingArticle);
    }

  /*  public User getHistoryByUser(User user){
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isEmpty()) throw new UsernameNotFoundException("Historik hittas inte.");

        User existingUser = optionalUser.get();
        return existingUser;
    }


    public Article getHistoryByArticle(Article article){
        Optional<Article> existingArticle = articleRepository.findById(article.getId());

        if (existingArticle.isEmpty()) {
            throw new RuntimeException("Historik finns inte.");
        }
        Article fetchedArticle = existingArticle.get();
        return fetchedArticle;
    }*/
   /*
    public List<History> getAllHistory() { // osäker om den ska vara kvar
        // Implementation för att hämta alla historiker
        return historyRepository.findAll();
    }

    public Optional<History> getHistoryById(Long id) { // osäker om den ska vara kvar
        // Implementation för att hämta en historik med ett specifikt ID
        return historyRepository.findById(id);
    }*/



}
