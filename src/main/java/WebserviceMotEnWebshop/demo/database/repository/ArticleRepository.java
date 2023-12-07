package WebserviceMotEnWebshop.demo.database.repository;

import WebserviceMotEnWebshop.demo.database.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByName(String name);

    // Sök på artikel
    @Query("select a from Article a " +
            "where lower(a.name) like lower(concat('%', :searchTerm, '%'))")
    List<Article> search(@Param("searchTerm") String searchTerm);

}
