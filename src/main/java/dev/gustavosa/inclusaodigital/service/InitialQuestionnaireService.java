package dev.gustavosa.inclusaodigital.service;

import dev.gustavosa.inclusaodigital.entity.InitialAnswer;
import dev.gustavosa.inclusaodigital.entity.InitialQuestion;
import dev.gustavosa.inclusaodigital.repository.InitialAnswerRepository;
import dev.gustavosa.inclusaodigital.repository.InitialQuestionOptionRepository;
import dev.gustavosa.inclusaodigital.repository.InitialQuestionRepository;
import dev.gustavosa.inclusaodigital.repository.ParticipantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InitialQuestionnaireService {

    private final InitialQuestionRepository questionRepository;
    private final InitialAnswerRepository answerRepository;
    private final InitialQuestionOptionRepository optionRepository;
    private final ParticipantRepository participantRepository;

    public InitialQuestionnaireService(InitialQuestionRepository questionRepository,
                                        InitialAnswerRepository answerRepository,
                                        InitialQuestionOptionRepository optionRepository,
                                        ParticipantRepository participantRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.optionRepository = optionRepository;
        this.participantRepository = participantRepository;
    }

    /**
     * A pergunta atual é derivada da quantidade de respostas já registradas
     * pelo participante, em vez de um índice guardado na sessão. Isso torna
     * o fluxo tolerante a atualização de página (F5) e retomada da oficina.
     */
    @Transactional(readOnly = true)
    public Optional<QuestionnaireStep> getCurrentStep(Long participantId) {
        List<InitialQuestion> questions = questionRepository.findAllWithOptionsOrderByOrderIndex();
        long answered = answerRepository.countByParticipantId(participantId);
        if (answered >= questions.size()) {
            return Optional.empty();
        }
        InitialQuestion current = questions.get((int) answered);
        return Optional.of(new QuestionnaireStep(current, (int) answered + 1, questions.size()));
    }

    /**
     * Faz upsert da resposta: se o participante já respondeu essa pergunta
     * (ex.: duplo clique no botão "Próximo"), atualiza a opção em vez de
     * criar uma segunda linha, o que quebraria o cálculo de progresso.
     */
    @Transactional
    public void saveAnswer(Long participantId, Long questionId, Long optionId) {
        InitialAnswer answer = answerRepository.findByParticipantIdAndQuestionId(participantId, questionId)
                .orElseGet(InitialAnswer::new);
        answer.setParticipant(participantRepository.getReferenceById(participantId));
        answer.setQuestion(questionRepository.getReferenceById(questionId));
        answer.setOption(optionRepository.getReferenceById(optionId));
        answerRepository.save(answer);
    }

    public record QuestionnaireStep(InitialQuestion question, int currentPosition, int totalQuestions) {
    }
}
