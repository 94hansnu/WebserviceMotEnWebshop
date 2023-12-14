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

   public List <History> getHistoryByUser(String username){
        Optional <User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()){
            throw new UsernameNotFoundException("Användaren hittades inte.");
        }
        User existingUser = optionalUser.get();
        return historyRepository.findByUser(existingUser);
   }

   public List <History> getHistoryByArticle(String articleName){
        //hämta all historik för en specifik artikel.
       Optional <Article> optionalArticle = articleRepository.findByName(articleName);
       if (optionalArticle.isEmpty()){
           throw new RuntimeException("Artikel kunde inte hittas");
       }
       Article article = optionalArticle.get();
       return historyRepository.findByArticle(article);
   }

    public List<History> getAllHistory() {
        // Implementation för att hämta alla historiker
        return historyRepository.findAll();
    }

    public List <History> getHistoryByUsername(String username){
        Optional <User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()){
            throw new UsernameNotFoundException("Användaren hittades inte");
        }
        User existingUser = optionalUser.get();
        return historyRepository.findByUser(existingUser);
    }

}
  /*  public List<History> getHistoryByUser(Long userId) {
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
    }*/






