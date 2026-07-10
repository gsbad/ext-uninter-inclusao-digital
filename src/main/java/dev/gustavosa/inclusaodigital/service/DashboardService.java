package dev.gustavosa.inclusaodigital.service;

import dev.gustavosa.inclusaodigital.dto.AnswerView;
import dev.gustavosa.inclusaodigital.dto.DashboardStats;
import dev.gustavosa.inclusaodigital.dto.ParticipantDetail;
import dev.gustavosa.inclusaodigital.dto.ParticipantSummary;
import dev.gustavosa.inclusaodigital.entity.Participant;
import dev.gustavosa.inclusaodigital.entity.QuizResult;
import dev.gustavosa.inclusaodigital.repository.InitialAnswerRepository;
import dev.gustavosa.inclusaodigital.repository.ParticipantRepository;
import dev.gustavosa.inclusaodigital.repository.QuizAnswerRepository;
import dev.gustavosa.inclusaodigital.repository.QuizResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Agregações para o painel do facilitador. Feitas em memória, sobre listas
 * pequenas (uma oficina tem, no máximo, algumas dezenas de participantes) —
 * não há necessidade de consultas de agregação no banco nesta escala.
 */
@Service
public class DashboardService {

    private final ParticipantRepository participantRepository;
    private final InitialAnswerRepository initialAnswerRepository;
    private final QuizAnswerRepository quizAnswerRepository;
    private final QuizResultRepository quizResultRepository;

    public DashboardService(ParticipantRepository participantRepository,
                             InitialAnswerRepository initialAnswerRepository,
                             QuizAnswerRepository quizAnswerRepository,
                             QuizResultRepository quizResultRepository) {
        this.participantRepository = participantRepository;
        this.initialAnswerRepository = initialAnswerRepository;
        this.quizAnswerRepository = quizAnswerRepository;
        this.quizResultRepository = quizResultRepository;
    }

    @Transactional(readOnly = true)
    public List<ParticipantSummary> listParticipantSummaries() {
        List<Participant> participants = participantRepository.findAllByOrderByCreatedAtAsc();
        Map<Long, QuizResult> resultsByParticipantId = quizResultsByParticipantId();

        return participants.stream()
                .map(participant -> toSummary(participant, resultsByParticipantId.get(participant.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public DashboardStats getStats() {
        long totalParticipants = participantRepository.count();
        List<QuizResult> results = quizResultRepository.findAll();
        double averagePercentage = results.isEmpty() ? 0.0 : results.stream()
                .mapToDouble(result -> (result.getScore() * 100.0) / result.getTotalQuestions())
                .average()
                .orElse(0.0);
        return new DashboardStats(totalParticipants, results.size(), averagePercentage);
    }

    @Transactional(readOnly = true)
    public Optional<ParticipantDetail> getParticipantDetail(Long participantId) {
        return participantRepository.findById(participantId).map(participant -> {
            List<AnswerView> initialAnswers = initialAnswerRepository
                    .findAllByParticipantIdWithQuestionAndOption(participantId).stream()
                    .map(answer -> new AnswerView(answer.getQuestion().getText(), answer.getOption().getText()))
                    .toList();

            List<AnswerView> quizAnswers = quizAnswerRepository
                    .findAllByParticipantIdWithQuestionAndOption(participantId).stream()
                    .map(answer -> new AnswerView(answer.getQuestion().getText(), answer.getOption().getText()))
                    .toList();

            QuizResult result = quizResultRepository.findByParticipantId(participantId).orElse(null);

            return new ParticipantDetail(
                    participant.getId(),
                    participant.getFullName(),
                    participant.getAgeRange().getLabel(),
                    participant.getPhone(),
                    participant.getCreatedAt(),
                    initialAnswers,
                    quizAnswers,
                    result != null ? result.getScore() : null,
                    result != null ? result.getTotalQuestions() : null
            );
        });
    }

    /**
     * CSV com ';' como delimitador e BOM UTF-8: o Excel em português do
     * Brasil trata ',' como separador decimal e só reconhece corretamente
     * acentos/':'-delimitação quando o arquivo tem BOM — sem isso, o
     * facilitador precisaria passar pelo assistente de importação de texto.
     */
    @Transactional(readOnly = true)
    public byte[] exportCsv() {
        List<ParticipantSummary> summaries = listParticipantSummaries();

        StringBuilder csv = new StringBuilder();
        csv.append("Nome;Faixa de idade;Pontuação no quiz\n");
        for (ParticipantSummary summary : summaries) {
            String quiz = summary.quizScore() != null
                    ? summary.quizScore() + "/" + summary.quizTotal()
                    : "não concluído";
            csv.append(escapeCsv(summary.fullName())).append(';')
                    .append(escapeCsv(summary.ageRangeLabel())).append(';')
                    .append(escapeCsv(quiz))
                    .append('\n');
        }

        byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byte[] content = csv.toString().getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[bom.length + content.length];
        System.arraycopy(bom, 0, result, 0, bom.length);
        System.arraycopy(content, 0, result, bom.length, content.length);
        return result;
    }

    private Map<Long, QuizResult> quizResultsByParticipantId() {
        return quizResultRepository.findAll().stream()
                .collect(Collectors.toMap(result -> result.getParticipant().getId(), result -> result));
    }

    private ParticipantSummary toSummary(Participant participant, QuizResult result) {
        return new ParticipantSummary(
                participant.getId(),
                participant.getFullName(),
                participant.getAgeRange().getLabel(),
                result != null ? result.getScore() : null,
                result != null ? result.getTotalQuestions() : null
        );
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "\"\"";
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
