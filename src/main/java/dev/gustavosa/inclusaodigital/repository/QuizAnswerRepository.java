package dev.gustavosa.inclusaodigital.repository;

import dev.gustavosa.inclusaodigital.entity.QuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {

    long countByParticipantId(Long participantId);

    Optional<QuizAnswer> findByParticipantIdAndQuestionId(Long participantId, Long questionId);

    @Query("SELECT a FROM QuizAnswer a JOIN FETCH a.option WHERE a.participant.id = :participantId")
    List<QuizAnswer> findAllByParticipantIdWithOption(Long participantId);

    @Query("SELECT a FROM QuizAnswer a JOIN FETCH a.question JOIN FETCH a.option " +
            "WHERE a.participant.id = :participantId ORDER BY a.question.orderIndex")
    List<QuizAnswer> findAllByParticipantIdWithQuestionAndOption(Long participantId);
}
