package dev.gustavosa.inclusaodigital.repository;

import dev.gustavosa.inclusaodigital.entity.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {

    Optional<QuizResult> findByParticipantId(Long participantId);
}
