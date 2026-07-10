package dev.gustavosa.inclusaodigital.dto;

import java.time.Instant;
import java.util.List;

public record ParticipantDetail(Long id,
                                 String fullName,
                                 String ageRangeLabel,
                                 String phone,
                                 Instant createdAt,
                                 List<AnswerView> initialAnswers,
                                 List<AnswerView> quizAnswers,
                                 Integer quizScore,
                                 Integer quizTotal) {
}
