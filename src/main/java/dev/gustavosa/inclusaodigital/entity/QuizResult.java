package dev.gustavosa.inclusaodigital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Resumo agregado do quiz de um participante, calculado e persistido uma
 * única vez pelo QuizService (ver getOrComputeResult). Diferente de
 * {@link QuizAnswer}, nunca é atualizado após criado.
 */
@Entity
@Table(name = "quiz_result")
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false, unique = true)
    private Participant participant;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "total_questions", nullable = false)
    private int totalQuestions;

    @CreationTimestamp
    @Column(name = "completed_at", nullable = false, updatable = false)
    private Instant completedAt;

    protected QuizResult() {
        // exigido pelo JPA
    }

    public QuizResult(Participant participant, int score, int totalQuestions) {
        this.participant = participant;
        this.score = score;
        this.totalQuestions = totalQuestions;
    }

    public Long getId() {
        return id;
    }

    public Participant getParticipant() {
        return participant;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }
}
