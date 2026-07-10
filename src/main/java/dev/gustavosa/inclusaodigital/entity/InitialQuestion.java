package dev.gustavosa.inclusaodigital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.util.List;

/**
 * Pergunta fixa do questionário diagnóstico inicial. É conteúdo semeado via
 * Flyway (V3__seed_initial_questions.sql), nunca criado pela aplicação.
 */
@Entity
@Table(name = "initial_question")
public class InitialQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false, length = 255)
    private String text;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @OneToMany(mappedBy = "question")
    @OrderBy("orderIndex ASC")
    private List<InitialQuestionOption> options;

    protected InitialQuestion() {
        // exigido pelo JPA
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public List<InitialQuestionOption> getOptions() {
        return options;
    }
}
