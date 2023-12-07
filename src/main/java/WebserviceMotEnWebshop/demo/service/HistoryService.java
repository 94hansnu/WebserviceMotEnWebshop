package WebserviceMotEnWebshop.demo.service;

import WebserviceMotEnWebshop.demo.entity.dao.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    public History addHistory(History history, String customerId) {
        // Implementation för att lägga till historik
        return historyRepository.save(history);
    }

    public List<History> getAll() {
        // Implementation för att hämta alla historiker
        return historyRepository.findAll();
    }

    public Optional<History> getOneById(Long id) {
        // Implementation för att hämta en historik med ett specifikt ID
        return historyRepository.findById(id);
    }

}
