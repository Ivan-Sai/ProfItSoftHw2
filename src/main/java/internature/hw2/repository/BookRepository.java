package internature.hw2.repository;

import internature.hw2.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    Page<Book> findAll(Specification<Book> specification, Pageable pageable);

    List<Book> findAll(Specification<Book> specification);

    Optional<Book> findByTitle(String title);

    List<Book> findAllByAuthorId(Long id);
}
