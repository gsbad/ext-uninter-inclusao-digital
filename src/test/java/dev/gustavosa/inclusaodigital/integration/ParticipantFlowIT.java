package dev.gustavosa.inclusaodigital.integration;

import dev.gustavosa.inclusaodigital.entity.InitialQuestion;
import dev.gustavosa.inclusaodigital.entity.QuizQuestion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Exercita o fluxo completo do participante (cadastro -> questionário
 * inicial -> materiais -> quiz -> resultado) contra um PostgreSQL real via
 * Testcontainers, rodando as migrations do Flyway de verdade. Requer Docker
 * instalado para executar (não roda no sandbox usado durante o
 * desenvolvimento assistido por IA deste projeto — validar localmente com
 * `mvn verify`).
 *
 * Sufixo "IT" (não "Test") de propósito: o Failsafe roda esta classe na
 * fase integration-test/verify, separada dos testes unitários do Surefire
 * (fase test), que não dependem de Docker.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Testcontainers
class ParticipantFlowIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveCompletarFluxoDoParticipanteDoCadastroAoResultadoDoQuiz() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/oficina/cadastro")
                        .session(session)
                        .param("fullName", "Maria da Integração")
                        .param("ageRange", "SEVENTY_TO_SEVENTY_NINE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/oficina/questionario"));

        for (int i = 0; i < 5; i++) {
            MvcResult getResult = mockMvc.perform(get("/oficina/questionario").session(session))
                    .andExpect(status().isOk())
                    .andReturn();
            InitialQuestion question = (InitialQuestion) getResult.getModelAndView().getModel().get("question");

            mockMvc.perform(post("/oficina/questionario")
                            .session(session)
                            .param("questionId", String.valueOf(question.getId()))
                            .param("optionId", String.valueOf(question.getOptions().get(0).getId())))
                    .andExpect(status().is3xxRedirection());
        }

        mockMvc.perform(get("/oficina/questionario").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/oficina/materiais"));

        mockMvc.perform(get("/oficina/materiais").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("oficina/materiais/index"));

        for (int i = 0; i < 5; i++) {
            MvcResult getResult = mockMvc.perform(get("/oficina/quiz").session(session))
                    .andExpect(status().isOk())
                    .andReturn();
            QuizQuestion question = (QuizQuestion) getResult.getModelAndView().getModel().get("question");

            mockMvc.perform(post("/oficina/quiz")
                            .session(session)
                            .param("questionId", String.valueOf(question.getId()))
                            .param("optionId", String.valueOf(question.getOptions().get(0).getId())))
                    .andExpect(status().is3xxRedirection());
        }

        MvcResult resultView = mockMvc.perform(get("/oficina/quiz/resultado").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("oficina/quiz-resultado"))
                .andReturn();
        assertThat(resultView.getModelAndView().getModel().get("total")).isEqualTo(5);

        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"));
    }
}
