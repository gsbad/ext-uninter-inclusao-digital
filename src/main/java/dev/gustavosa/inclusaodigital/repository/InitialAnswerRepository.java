package dev.gustavosa.inclusaodigital.repository;

import dev.gustavosa.inclusaodigital.entity.InitialAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InitialAnswerRepository extends JpaRepository<InitialAnswer, Long> {

    long countByParticipantId(Long participantId);

    Optional<InitialAnswer> findByParticipantIdAndQuestionId(Long participantId, Long questionId);
}
