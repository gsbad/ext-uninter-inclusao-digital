package dev.gustavosa.inclusaodigital.service;

import dev.gustavosa.inclusaodigital.dto.DashboardStats;
import dev.gustavosa.inclusaodigital.dto.ParticipantDetail;
import dev.gustavosa.inclusaodigital.dto.ParticipantSummary;
import dev.gustavosa.inclusaodigital.entity.AgeRange;
import dev.gustavosa.inclusaodigital.entity.InitialAnswer;
import dev.gustavosa.inclusaodigital.entity.InitialQuestion;
import dev.gustavosa.inclusaodigital.entity.InitialQuestionOption;
import dev.gustavosa.inclusaodigital.entity.Participant;
import dev.gustavosa.inclusaodigital.entity.QuizAnswer;
import dev.gustavosa.inclusaodigital.entity.QuizOption;
import dev.gustavosa.inclusaodigital.entity.QuizQuestion;
import dev.gustavosa.inclusaodigital.entity.QuizResult;
import dev.gustavosa.inclusaodigital.repository.InitialAnswerRepository;
import dev.gustavosa.inclusaodigital.repository.ParticipantRepository;
import dev.gustavosa.inclusaodigital.repository.QuizAnswerRepository;
import dev.gustavosa.inclusaodigital.repository.QuizResultRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private InitialAnswerRepository initialAnswerRepository;

    @Mock
    private QuizAnswerRepository quizAnswerRepository;

    @Mock
    private QuizResultRepository quizResultRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void deveListarResumoComEsemQuizConcluido() {
        Participant comQuiz = mock(Participant.class);
        when(comQuiz.getId()).thenReturn(1L);
        when(comQuiz.getFullName()).thenReturn("Maria Silva");
        when(comQuiz.getAgeRange()).thenReturn(AgeRange.SEVENTY_TO_SEVENTY_NINE);

        Participant semQuiz = mock(Participant.class);
        when(semQuiz.getId()).thenReturn(2L);
        when(semQuiz.getFullName()).thenReturn("José Santos");
        when(semQuiz.getAgeRange()).thenReturn(AgeRange.SIXTY_TO_SIXTY_NINE);

        QuizResult resultado = mock(QuizResult.class);
        when(resultado.getParticipant()).thenReturn(comQuiz);
        when(resultado.getScore()).thenReturn(4);
        when(resultado.getTotalQuestions()).thenReturn(5);

        when(participantRepository.findAllByOrderByCreatedAtAsc()).thenReturn(List.of(comQuiz, semQuiz));
        when(quizResultRepository.findAll()).thenReturn(List.of(resultado));

        List<ParticipantSummary> summaries = dashboardService.listParticipantSummaries();

        assertThat(summaries).hasSize(2);
        ParticipantSummary primeiro = summaries.get(0);
        assertThat(primeiro.fullName()).isEqualTo("Maria Silva");
        assertThat(primeiro.quizScore()).isEqualTo(4);
        assertThat(primeiro.quizTotal()).isEqualTo(5);

        ParticipantSummary segundo = summaries.get(1);
        assertThat(segundo.fullName()).isEqualTo("José Santos");
        assertThat(segundo.quizScore()).isNull();
    }

    @Test
    void deveCalcularEstatisticasComMediaDePercentualDeAcerto() {
        QuizResult resultado = mock(QuizResult.class);
        when(resultado.getScore()).thenReturn(4);
        when(resultado.getTotalQuestions()).thenReturn(5);

        when(participantRepository.count()).thenReturn(2L);
        when(quizResultRepository.findAll()).thenReturn(List.of(resultado));

        DashboardStats stats = dashboardService.getStats();

        assertThat(stats.totalParticipants()).isEqualTo(2);
        assertThat(stats.completedQuizCount()).isEqualTo(1);
        assertThat(stats.averageScorePercentage()).isEqualTo(80.0);
    }

    @Test
    void deveRetornarDetalheComRespostasDoQuestionarioEDoQuiz() {
        Participant participant = mock(Participant.class);
        when(participant.getId()).thenReturn(1L);
        when(participant.getFullName()).thenReturn("Maria Silva");
        when(participant.getAgeRange()).thenReturn(AgeRange.SEVENTY_TO_SEVENTY_NINE);
        when(participant.getPhone()).thenReturn(null);

        InitialQuestion pergunta1 = mock(InitialQuestion.class);
        when(pergunta1.getText()).thenReturn("Você usa WhatsApp?");
        InitialQuestionOption opcao1 = mock(InitialQuestionOption.class);
        when(opcao1.getText()).thenReturn("Sim");
        InitialAnswer respostaInicial = mock(InitialAnswer.class);
        when(respostaInicial.getQuestion()).thenReturn(pergunta1);
        when(respostaInicial.getOption()).thenReturn(opcao1);

        QuizQuestion perguntaQuiz = mock(QuizQuestion.class);
        when(perguntaQuiz.getText()).thenReturn("Como reconhecer um link falso?");
        QuizOption opcaoQuiz = mock(QuizOption.class);
        when(opcaoQuiz.getText()).thenReturn("Erros de português e prêmios exagerados");
        QuizAnswer respostaQuiz = mock(QuizAnswer.class);
        when(respostaQuiz.getQuestion()).thenReturn(perguntaQuiz);
        when(respostaQuiz.getOption()).thenReturn(opcaoQuiz);

        QuizResult resultado = mock(QuizResult.class);
        when(resultado.getScore()).thenReturn(4);
        when(resultado.getTotalQuestions()).thenReturn(5);

        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));
        when(initialAnswerRepository.findAllByParticipantIdWithQuestionAndOption(1L))
                .thenReturn(List.of(respostaInicial));
        when(quizAnswerRepository.findAllByParticipantIdWithQuestionAndOption(1L))
                .thenReturn(List.of(respostaQuiz));
        when(quizResultRepository.findByParticipantId(1L)).thenReturn(Optional.of(resultado));

        Optional<ParticipantDetail> detail = dashboardService.getParticipantDetail(1L);

        assertThat(detail).isPresent();
        assertThat(detail.get().fullName()).isEqualTo("Maria Silva");
        assertThat(detail.get().initialAnswers()).hasSize(1);
        assertThat(detail.get().initialAnswers().get(0).questionText()).isEqualTo("Você usa WhatsApp?");
        assertThat(detail.get().quizAnswers()).hasSize(1);
        assertThat(detail.get().quizScore()).isEqualTo(4);
    }

    @Test
    void deveRetornarVazioQuandoParticipanteNaoExiste() {
        when(participantRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<ParticipantDetail> detail = dashboardService.getParticipantDetail(99L);

        assertThat(detail).isEmpty();
    }

    @Test
    void deveGerarCsvComBomEEscapandoAspasNoNome() {
        Participant participant = mock(Participant.class);
        when(participant.getId()).thenReturn(1L);
        when(participant.getFullName()).thenReturn("Maria \"Silva\"");
        when(participant.getAgeRange()).thenReturn(AgeRange.EIGHTY_OR_MORE);

        when(participantRepository.findAllByOrderByCreatedAtAsc()).thenReturn(List.of(participant));
        when(quizResultRepository.findAll()).thenReturn(List.of());

        byte[] csv = dashboardService.exportCsv();

        assertThat(csv[0]).isEqualTo((byte) 0xEF);
        assertThat(csv[1]).isEqualTo((byte) 0xBB);
        assertThat(csv[2]).isEqualTo((byte) 0xBF);

        String content = new String(csv, 3, csv.length - 3, StandardCharsets.UTF_8);
        assertThat(content).startsWith("Nome;Faixa de idade;Pontuação no quiz\n");
        assertThat(content).contains("\"Maria \"\"Silva\"\"\"");
        assertThat(content).contains("não concluído");
    }
}
