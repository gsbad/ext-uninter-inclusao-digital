package dev.gustavosa.inclusaodigital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Placeholder temporário para /oficina/quiz, até o Epic 6 implementar o
 * quiz de fato. Remover esta classe e o template correspondente quando o
 * QuizController real for criado.
 */
@Controller
public class QuizComingSoonController {

    @GetMapping("/oficina/quiz")
    public String comingSoon() {
        return "oficina/quiz-em-breve";
    }
}
