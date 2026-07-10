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
 * Pergunta fixa do quiz de fixação. Conteúdo semeado via Flyway
 * (V5__seed_quiz_questions.sql), nunca criado pela aplicação.
 */
@Entity
@Table(name = "quiz_question")
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false, length = 255)
    private String text;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @OneToMany(mappedBy = "question")
    @OrderBy("orderIndex ASC")
    private List<QuizOption> options;

    protected QuizQuestion() {
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

    public List<QuizOption> getOptions() {
        return options;
    }
}
