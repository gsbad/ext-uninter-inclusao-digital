package dev.gustavosa.inclusaodigital.repository;

import dev.gustavosa.inclusaodigital.entity.InitialQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InitialQuestionRepository extends JpaRepository<InitialQuestion, Long> {

    @Query("SELECT DISTINCT q FROM InitialQuestion q LEFT JOIN FETCH q.options ORDER BY q.orderIndex")
    List<InitialQuestion> findAllWithOptionsOrderByOrderIndex();
}
