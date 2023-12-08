package WebserviceMotEnWebshop.demo.service;


import WebserviceMotEnWebshop.demo.database.entity.Article;
import WebserviceMotEnWebshop.demo.database.entity.History;
import WebserviceMotEnWebshop.demo.database.entity.User;
import WebserviceMotEnWebshop.demo.database.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    public History addHistory(History history) {
        // Implementation för att lägga till historik
        return historyRepository.save(history);
    }
    public List <History> getHistoryByUser(User user){
        // Implementation för att hämta historik för en specifik användare
        return historyRepository.findByUser(user);
    }

    public List <History> getHistoryByArticle(Article article){
        // Implementation för att hämta historik för en specifik artikel
        return historyRepository.findByArticle(article);
    }
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
