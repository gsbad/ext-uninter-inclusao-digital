package dev.gustavosa.inclusaodigital.controller;

import dev.gustavosa.inclusaodigital.service.InitialQuestionnaireService;
import dev.gustavosa.inclusaodigital.service.InitialQuestionnaireService.QuestionnaireStep;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/oficina/questionario")
public class InitialQuestionnaireController {

    private final InitialQuestionnaireService questionnaireService;

    public InitialQuestionnaireController(InitialQuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @GetMapping
    public String showQuestion(HttpSession session, Model model) {
        Long participantId = (Long) session.getAttribute("participantId");
        Optional<QuestionnaireStep> step = questionnaireService.getCurrentStep(participantId);
        if (step.isEmpty()) {
            return "redirect:/oficina/materiais";
        }
        populateModel(model, step.get());
        return "oficina/questionario";
    }

    @PostMapping
    public String saveAnswer(@RequestParam Long questionId,
                              @RequestParam(required = false) Long optionId,
                              HttpSession session,
                              Model model) {
        Long participantId = (Long) session.getAttribute("participantId");

        if (optionId == null) {
            Optional<QuestionnaireStep> step = questionnaireService.getCurrentStep(participantId);
            if (step.isEmpty()) {
                return "redirect:/oficina/materiais";
            }
            model.addAttribute("erro", "Por favor, selecione uma opção.");
            populateModel(model, step.get());
            return "oficina/questionario";
        }

        questionnaireService.saveAnswer(participantId, questionId, optionId);
        return "redirect:/oficina/questionario";
    }

    private void populateModel(Model model, QuestionnaireStep step) {
        model.addAttribute("question", step.question());
        model.addAttribute("currentPosition", step.currentPosition());
        model.addAttribute("totalQuestions", step.totalQuestions());
    }
}
