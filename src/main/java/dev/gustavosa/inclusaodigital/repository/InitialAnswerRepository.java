package dev.gustavosa.inclusaodigital.repository;

import dev.gustavosa.inclusaodigital.entity.InitialAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InitialAnswerRepository extends JpaRepository<InitialAnswer, Long> {

    long countByParticipantId(Long participantId);

    Optional<InitialAnswer> findByParticipantIdAndQuestionId(Long participantId, Long questionId);

    @Query("SELECT a FROM InitialAnswer a JOIN FETCH a.question JOIN FETCH a.option " +
            "WHERE a.participant.id = :participantId ORDER BY a.question.orderIndex")
    List<InitialAnswer> findAllByParticipantIdWithQuestionAndOption(Long participantId);
}
