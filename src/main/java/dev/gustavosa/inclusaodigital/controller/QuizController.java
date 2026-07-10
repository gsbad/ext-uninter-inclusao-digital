package dev.gustavosa.inclusaodigital.controller;

import dev.gustavosa.inclusaodigital.entity.QuizResult;
import dev.gustavosa.inclusaodigital.service.QuizService;
import dev.gustavosa.inclusaodigital.service.QuizService.QuizStep;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/oficina/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public String showQuestion(HttpSession session, Model model) {
        Long participantId = (Long) session.getAttribute("participantId");
        Optional<QuizStep> step = quizService.getCurrentStep(participantId);
        if (step.isEmpty()) {
            return "redirect:/oficina/quiz/resultado";
        }
        populateModel(model, step.get());
        return "oficina/quiz";
    }

    @PostMapping
    public String saveAnswer(@RequestParam Long questionId,
                              @RequestParam(required = false) Long optionId,
                              HttpSession session,
                              Model model) {
        Long participantId = (Long) session.getAttribute("participantId");

        if (optionId == null) {
            Optional<QuizStep> step = quizService.getCurrentStep(participantId);
            if (step.isEmpty()) {
                return "redirect:/oficina/quiz/resultado";
            }
            model.addAttribute("erro", "Por favor, selecione uma opção.");
            populateModel(model, step.get());
            return "oficina/quiz";
        }

        quizService.saveAnswer(participantId, questionId, optionId);
        return "redirect:/oficina/quiz";
    }

    @GetMapping("/resultado")
    public String result(HttpSession session, Model model) {
        Long participantId = (Long) session.getAttribute("participantId");
        if (quizService.getCurrentStep(participantId).isPresent()) {
            // Acesso direto à URL antes de responder todas as perguntas:
            // volta para a pergunta pendente em vez de calcular e travar
            // uma pontuação incompleta (QuizResult nunca é recalculado).
            return "redirect:/oficina/quiz";
        }
        QuizResult result = quizService.getOrComputeResult(participantId);
        model.addAttribute("score", result.getScore());
        model.addAttribute("total", result.getTotalQuestions());
        return "oficina/quiz-resultado";
    }

    /**
     * Encerra a sessão do participante ao final do fluxo, para que o
     * próximo idoso no mesmo tablet comece do zero em vez de herdar estado.
     */
    @GetMapping("/concluir")
    public String finish(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    private void populateModel(Model model, QuizStep step) {
        model.addAttribute("question", step.question());
        model.addAttribute("currentPosition", step.currentPosition());
        model.addAttribute("totalQuestions", step.totalQuestions());
    }
}
