package dev.gustavosa.inclusaodigital.service;

import dev.gustavosa.inclusaodigital.entity.InitialAnswer;
import dev.gustavosa.inclusaodigital.entity.InitialQuestion;
import dev.gustavosa.inclusaodigital.entity.InitialQuestionOption;
import dev.gustavosa.inclusaodigital.entity.Participant;
import dev.gustavosa.inclusaodigital.repository.InitialAnswerRepository;
import dev.gustavosa.inclusaodigital.repository.InitialQuestionOptionRepository;
import dev.gustavosa.inclusaodigital.repository.InitialQuestionRepository;
import dev.gustavosa.inclusaodigital.repository.ParticipantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InitialQuestionnaireServiceTest {

    @Mock
    private InitialQuestionRepository questionRepository;

    @Mock
    private InitialAnswerRepository answerRepository;

    @Mock
    private InitialQuestionOptionRepository optionRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private InitialQuestionnaireService service;

    @Test
    void deveRetornarPrimeiraPerguntaQuandoNenhumaRespondida() {
        InitialQuestion primeira = mock(InitialQuestion.class);
        InitialQuestion segunda = mock(InitialQuestion.class);
        when(questionRepository.findAllWithOptionsOrderByOrderIndex()).thenReturn(List.of(primeira, segunda));
        when(answerRepository.countByParticipantId(1L)).thenReturn(0L);

        Optional<InitialQuestionnaireService.QuestionnaireStep> step = service.getCurrentStep(1L);

        assertThat(step).isPresent();
        assertThat(step.get().question()).isEqualTo(primeira);
        assertThat(step.get().currentPosition()).isEqualTo(1);
        assertThat(step.get().totalQuestions()).isEqualTo(2);
    }

    @Test
    void deveRetornarVazioQuandoTodasAsPerguntasForamRespondidas() {
        InitialQuestion unica = mock(InitialQuestion.class);
        when(questionRepository.findAllWithOptionsOrderByOrderIndex()).thenReturn(List.of(unica));
        when(answerRepository.countByParticipantId(1L)).thenReturn(1L);

        Optional<InitialQuestionnaireService.QuestionnaireStep> step = service.getCurrentStep(1L);

        assertThat(step).isEmpty();
    }

    @Test
    void deveCriarNovaRespostaQuandoParticipanteAindaNaoRespondeuEssaPergunta() {
        Participant participant = mock(Participant.class);
        InitialQuestion question = mock(InitialQuestion.class);
        InitialQuestionOption option = mock(InitialQuestionOption.class);

        when(answerRepository.findByParticipantIdAndQuestionId(1L, 10L)).thenReturn(Optional.empty());
        when(participantRepository.getReferenceById(1L)).thenReturn(participant);
        when(questionRepository.getReferenceById(10L)).thenReturn(question);
        when(optionRepository.getReferenceById(100L)).thenReturn(option);

        service.saveAnswer(1L, 10L, 100L);

        ArgumentCaptor<InitialAnswer> captor = ArgumentCaptor.forClass(InitialAnswer.class);
        verify(answerRepository).save(captor.capture());
        InitialAnswer saved = captor.getValue();
        assertThat(saved.getParticipant()).isEqualTo(participant);
        assertThat(saved.getQuestion()).isEqualTo(question);
        assertThat(saved.getOption()).isEqualTo(option);
    }

    @Test
    void deveAtualizarRespostaExistenteEmVezDeCriarUmaNova() {
        InitialAnswer existente = mock(InitialAnswer.class);
        Participant participant = mock(Participant.class);
        InitialQuestion question = mock(InitialQuestion.class);
        InitialQuestionOption option = mock(InitialQuestionOption.class);

        when(answerRepository.findByParticipantIdAndQuestionId(1L, 10L)).thenReturn(Optional.of(existente));
        when(participantRepository.getReferenceById(1L)).thenReturn(participant);
        when(questionRepository.getReferenceById(10L)).thenReturn(question);
        when(optionRepository.getReferenceById(100L)).thenReturn(option);

        service.saveAnswer(1L, 10L, 100L);

        verify(existente).setParticipant(participant);
        verify(existente).setQuestion(question);
        verify(existente).setOption(option);
        verify(answerRepository).save(existente);
    }
}
