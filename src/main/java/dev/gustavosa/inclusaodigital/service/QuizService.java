package dev.gustavosa.inclusaodigital.service;

import dev.gustavosa.inclusaodigital.entity.Participant;
import dev.gustavosa.inclusaodigital.entity.QuizAnswer;
import dev.gustavosa.inclusaodigital.entity.QuizQuestion;
import dev.gustavosa.inclusaodigital.entity.QuizResult;
import dev.gustavosa.inclusaodigital.repository.ParticipantRepository;
import dev.gustavosa.inclusaodigital.repository.QuizAnswerRepository;
import dev.gustavosa.inclusaodigital.repository.QuizOptionRepository;
import dev.gustavosa.inclusaodigital.repository.QuizQuestionRepository;
import dev.gustavosa.inclusaodigital.repository.QuizResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizQuestionRepository questionRepository;
    private final QuizOptionRepository optionRepository;
    private final QuizAnswerRepository answerRepository;
    private final QuizResultRepository resultRepository;
    private final ParticipantRepository participantRepository;

    public QuizService(QuizQuestionRepository questionRepository,
                        QuizOptionRepository optionRepository,
                        QuizAnswerRepository answerRepository,
                        QuizResultRepository resultRepository,
                        ParticipantRepository participantRepository) {
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.answerRepository = answerRepository;
        this.resultRepository = resultRepository;
        this.participantRepository = participantRepository;
    }

    @Transactional(readOnly = true)
    public Optional<QuizStep> getCurrentStep(Long participantId) {
        List<QuizQuestion> questions = questionRepository.findAllWithOptionsOrderByOrderIndex();
        long answered = answerRepository.countByParticipantId(participantId);
        if (answered >= questions.size()) {
            return Optional.empty();
        }
        QuizQuestion current = questions.get((int) answered);
        return Optional.of(new QuizStep(current, (int) answered + 1, questions.size()));
    }

    @Transactional
    public void saveAnswer(Long participantId, Long questionId, Long optionId) {
        QuizAnswer answer = answerRepository.findByParticipantIdAndQuestionId(participantId, questionId)
                .orElseGet(QuizAnswer::new);
        answer.setParticipant(participantRepository.getReferenceById(participantId));
        answer.setQuestion(questionRepository.getReferenceById(questionId));
        answer.setOption(optionRepository.getReferenceById(optionId));
        answerRepository.save(answer);
    }

    /**
     * A pontuação é calculada e persistida uma única vez, na primeira vez
     * que a tela de resultado é aberta. Chamadas seguintes (ex.: F5) apenas
     * leem o QuizResult já salvo, em vez de recalcular.
     */
    @Transactional
    public QuizResult getOrComputeResult(Long participantId) {
        return resultRepository.findByParticipantId(participantId)
                .orElseGet(() -> computeAndSaveResult(participantId));
    }

    private QuizResult computeAndSaveResult(Long participantId) {
        List<QuizAnswer> answers = answerRepository.findAllByParticipantIdWithOption(participantId);
        long correctAnswers = answers.stream().filter(answer -> answer.getOption().isCorrect()).count();
        int totalQuestions = (int) questionRepository.count();
        Participant participant = participantRepository.getReferenceById(participantId);
        QuizResult result = new QuizResult(participant, (int) correctAnswers, totalQuestions);
        return resultRepository.save(result);
    }

    public record QuizStep(QuizQuestion question, int currentPosition, int totalQuestions) {
    }
}
