package dev.gustavosa.inclusaodigital.repository;

import dev.gustavosa.inclusaodigital.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

    @Query("SELECT DISTINCT q FROM QuizQuestion q LEFT JOIN FETCH q.options ORDER BY q.orderIndex")
    List<QuizQuestion> findAllWithOptionsOrderByOrderIndex();
}
