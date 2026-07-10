package dev.gustavosa.inclusaodigital.controller;

import dev.gustavosa.inclusaodigital.dto.ParticipantForm;
import dev.gustavosa.inclusaodigital.entity.AgeRange;
import dev.gustavosa.inclusaodigital.entity.Participant;
import dev.gustavosa.inclusaodigital.service.ParticipantService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oficina")
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping("/cadastro")
    public String showForm(Model model) {
        model.addAttribute("participantForm", new ParticipantForm());
        model.addAttribute("ageRanges", AgeRange.values());
        return "oficina/cadastro";
    }

    @PostMapping("/cadastro")
    public String submitForm(@Valid @ModelAttribute("participantForm") ParticipantForm form,
                              BindingResult bindingResult,
                              Model model,
                              HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("ageRanges", AgeRange.values());
            return "oficina/cadastro";
        }
        Participant participant = participantService.register(form);
        session.setAttribute("participantId", participant.getId());
        return "redirect:/oficina/questionario";
    }
}
