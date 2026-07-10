package dev.gustavosa.inclusaodigital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Resposta de um participante a uma pergunta do quiz. Mutável de propósito,
 * assim como {@link InitialAnswer}: o service faz upsert para evitar
 * respostas duplicadas em caso de duplo clique.
 */
@Entity
@Table(name = "quiz_answer",
        uniqueConstraints = @UniqueConstraint(columnNames = {"participant_id", "question_id"}))
public class QuizAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private QuizOption option;

    @CreationTimestamp
    @Column(name = "answered_at", nullable = false, updatable = false)
    private Instant answeredAt;

    public QuizAnswer() {
    }

    public Long getId() {
        return id;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public QuizQuestion getQuestion() {
        return question;
    }

    public void setQuestion(QuizQuestion question) {
        this.question = question;
    }

    public QuizOption getOption() {
        return option;
    }

    public void setOption(QuizOption option) {
        this.option = option;
    }

    public Instant getAnsweredAt() {
        return answeredAt;
    }
}
