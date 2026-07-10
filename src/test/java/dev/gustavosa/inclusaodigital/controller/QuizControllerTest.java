package dev.gustavosa.inclusaodigital.controller;

import dev.gustavosa.inclusaodigital.entity.QuizQuestion;
import dev.gustavosa.inclusaodigital.entity.QuizResult;
import dev.gustavosa.inclusaodigital.service.QuizService;
import dev.gustavosa.inclusaodigital.service.QuizService.QuizStep;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuizController.class)
class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuizService quizService;

    @Test
    void deveRedirecionarParaPerguntaPendenteAoAcessarResultadoAntesDeTerminar() throws Exception {
        QuizQuestion perguntaPendente = mock(QuizQuestion.class);
        when(quizService.getCurrentStep(1L)).thenReturn(Optional.of(new QuizStep(perguntaPendente, 3, 5)));

        mockMvc.perform(get("/oficina/quiz/resultado").sessionAttr("participantId", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/oficina/quiz"));

        verify(quizService, never()).getOrComputeResult(1L);
    }

    @Test
    void deveExibirResultadoQuandoQuizEstiverCompleto() throws Exception {
        QuizResult resultado = mock(QuizResult.class);
        when(resultado.getScore()).thenReturn(4);
        when(resultado.getTotalQuestions()).thenReturn(5);
        when(quizService.getCurrentStep(1L)).thenReturn(Optional.empty());
        when(quizService.getOrComputeResult(1L)).thenReturn(resultado);

        mockMvc.perform(get("/oficina/quiz/resultado").sessionAttr("participantId", 1L))
                .andExpect(status().isOk());
    }
}
