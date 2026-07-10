package dev.gustavosa.inclusaodigital.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(MaterialController.class)
class MaterialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveExibirListaDeTemas() throws Exception {
        mockMvc.perform(get("/oficina/materiais").sessionAttr("participantId", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("oficina/materiais/index"));
    }

    @Test
    void deveExibirArtigoParaTemaValido() throws Exception {
        mockMvc.perform(get("/oficina/materiais/pix").sessionAttr("participantId", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("oficina/materiais/artigo"));
    }

    @Test
    void deveRedirecionarParaListaQuandoTemaNaoExiste() throws Exception {
        mockMvc.perform(get("/oficina/materiais/tema-inexistente").sessionAttr("participantId", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/oficina/materiais"));
    }
}
