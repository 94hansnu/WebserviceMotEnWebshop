package WebserviceMotEnWebshop.demo.database.repository;

import WebserviceMotEnWebshop.demo.database.entity.Article;
import WebserviceMotEnWebshop.demo.database.entity.History;
import WebserviceMotEnWebshop.demo.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findByUser(User user);
    List<History> findByArticle(Article article);
    @Query("SELECT h FROM History h " +
            "JOIN h.article a " +
            "JOIN h.user u " +
            "WHERE lower(a.name) LIKE lower(concat('%', :searchTerm, '%')) OR lower(u.username) LIKE lower(concat('%', :searchTerm, '%'))")
    List<History> searchByArticleOrUser(@Param("searchTerm") String searchTerm);
}
