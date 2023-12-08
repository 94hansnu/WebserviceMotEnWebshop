package WebserviceMotEnWebshop.demo.database.service;

import WebserviceMotEnWebshop.demo.database.repository.ArticleRepository;
import WebserviceMotEnWebshop.demo.database.repository.HistoryRepository;
import WebserviceMotEnWebshop.demo.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EconomyService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

    /* Det skall finnas följande funktioner för economy:

     * USERS:
        * SE SIN EGEN KÖPHISTORIK, PRIS OCH PRODUKT

     * ADMIN:
        * SE ALL / SPECIFIK KÖPHISTORIK
        * UPPDATERA PRIS PÅ ARTIKEL
        * SE ALL HISTORIK FÖR SPECIFIKA PRODUKTER
     */
}
