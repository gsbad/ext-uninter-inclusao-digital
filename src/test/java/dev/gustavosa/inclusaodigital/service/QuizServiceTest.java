package dev.gustavosa.inclusaodigital.service;

import dev.gustavosa.inclusaodigital.entity.Participant;
import dev.gustavosa.inclusaodigital.entity.QuizAnswer;
import dev.gustavosa.inclusaodigital.entity.QuizOption;
import dev.gustavosa.inclusaodigital.entity.QuizQuestion;
import dev.gustavosa.inclusaodigital.entity.QuizResult;
import dev.gustavosa.inclusaodigital.repository.ParticipantRepository;
import dev.gustavosa.inclusaodigital.repository.QuizAnswerRepository;
import dev.gustavosa.inclusaodigital.repository.QuizOptionRepository;
import dev.gustavosa.inclusaodigital.repository.QuizQuestionRepository;
import dev.gustavosa.inclusaodigital.repository.QuizResultRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock
    private QuizQuestionRepository questionRepository;

    @Mock
    private QuizOptionRepository optionRepository;

    @Mock
    private QuizAnswerRepository answerRepository;

    @Mock
    private QuizResultRepository resultRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private QuizService quizService;

    @Test
    void deveRetornarPrimeiraPerguntaQuandoNenhumaRespondida() {
        QuizQuestion primeira = mock(QuizQuestion.class);
        QuizQuestion segunda = mock(QuizQuestion.class);
        when(questionRepository.findAllWithOptionsOrderByOrderIndex()).thenReturn(List.of(primeira, segunda));
        when(answerRepository.countByParticipantId(1L)).thenReturn(0L);

        Optional<QuizService.QuizStep> step = quizService.getCurrentStep(1L);

        assertThat(step).isPresent();
        assertThat(step.get().question()).isEqualTo(primeira);
        assertThat(step.get().currentPosition()).isEqualTo(1);
        assertThat(step.get().totalQuestions()).isEqualTo(2);
    }

    @Test
    void deveRetornarVazioQuandoTodasAsPerguntasForamRespondidas() {
        QuizQuestion unica = mock(QuizQuestion.class);
        when(questionRepository.findAllWithOptionsOrderByOrderIndex()).thenReturn(List.of(unica));
        when(answerRepository.countByParticipantId(1L)).thenReturn(1L);

        Optional<QuizService.QuizStep> step = quizService.getCurrentStep(1L);

        assertThat(step).isEmpty();
    }

    @Test
    void deveCriarNovaRespostaQuandoParticipanteAindaNaoRespondeuEssaPergunta() {
        Participant participant = mock(Participant.class);
        QuizQuestion question = mock(QuizQuestion.class);
        QuizOption option = mock(QuizOption.class);

        when(answerRepository.findByParticipantIdAndQuestionId(1L, 10L)).thenReturn(Optional.empty());
        when(participantRepository.getReferenceById(1L)).thenReturn(participant);
        when(questionRepository.getReferenceById(10L)).thenReturn(question);
        when(optionRepository.getReferenceById(100L)).thenReturn(option);

        quizService.saveAnswer(1L, 10L, 100L);

        ArgumentCaptor<QuizAnswer> captor = ArgumentCaptor.forClass(QuizAnswer.class);
        verify(answerRepository).save(captor.capture());
        assertThat(captor.getValue().getParticipant()).isEqualTo(participant);
        assertThat(captor.getValue().getQuestion()).isEqualTo(question);
        assertThat(captor.getValue().getOption()).isEqualTo(option);
    }

    @Test
    void deveCalcularPontuacaoComBaseNasRespostasCorretas() {
        QuizAnswer respostaCorreta = mock(QuizAnswer.class);
        QuizOption opcaoCorreta = mock(QuizOption.class);
        when(opcaoCorreta.isCorrect()).thenReturn(true);
        when(respostaCorreta.getOption()).thenReturn(opcaoCorreta);

        QuizAnswer respostaErrada = mock(QuizAnswer.class);
        QuizOption opcaoErrada = mock(QuizOption.class);
        when(opcaoErrada.isCorrect()).thenReturn(false);
        when(respostaErrada.getOption()).thenReturn(opcaoErrada);

        Participant participant = mock(Participant.class);

        when(resultRepository.findByParticipantId(1L)).thenReturn(Optional.empty());
        when(answerRepository.findAllByParticipantIdWithOption(1L))
                .thenReturn(List.of(respostaCorreta, respostaErrada));
        when(questionRepository.count()).thenReturn(5L);
        when(participantRepository.getReferenceById(1L)).thenReturn(participant);
        when(resultRepository.save(any(QuizResult.class))).thenAnswer(invocation -> invocation.getArgument(0));

        QuizResult result = quizService.getOrComputeResult(1L);

        assertThat(result.getScore()).isEqualTo(1);
        assertThat(result.getTotalQuestions()).isEqualTo(5);
    }

    @Test
    void deveRetornarResultadoJaExistenteSemRecalcular() {
        QuizResult existente = mock(QuizResult.class);
        when(resultRepository.findByParticipantId(1L)).thenReturn(Optional.of(existente));

        QuizResult result = quizService.getOrComputeResult(1L);

        assertThat(result).isEqualTo(existente);
        verify(answerRepository, never()).findAllByParticipantIdWithOption(any());
    }
}
