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

@Entity
@Table(name = "initial_question_option")
public class InitialQuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private InitialQuestion question;

    @Column(name = "text", nullable = false, length = 255)
    private String text;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    protected InitialQuestionOption() {
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
}
