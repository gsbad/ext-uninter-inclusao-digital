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
 * Resposta de um participante a uma pergunta do questionário inicial.
 * Diferente de {@link Participant}, é mutável de propósito: o service faz
 * upsert (busca por participante+pergunta e atualiza a opção) para evitar
 * respostas duplicadas caso o participante toque duas vezes no botão.
 */
@Entity
@Table(name = "initial_answer",
        uniqueConstraints = @UniqueConstraint(columnNames = {"participant_id", "question_id"}))
public class InitialAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private InitialQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private InitialQuestionOption option;

    @CreationTimestamp
    @Column(name = "answered_at", nullable = false, updatable = false)
    private Instant answeredAt;

    public InitialAnswer() {
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

    public InitialQuestion getQuestion() {
        return question;
    }

    public void setQuestion(InitialQuestion question) {
        this.question = question;
    }

    public InitialQuestionOption getOption() {
        return option;
    }

    public void setOption(InitialQuestionOption option) {
        this.option = option;
    }

    public Instant getAnsweredAt() {
        return answeredAt;
    }
}
