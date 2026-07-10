package dev.gustavosa.inclusaodigital.dto;

public record ParticipantSummary(Long id, String fullName, String ageRangeLabel, Integer quizScore,
                                  Integer quizTotal) {
}
