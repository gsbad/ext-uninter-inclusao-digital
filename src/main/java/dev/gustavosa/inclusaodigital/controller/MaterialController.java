package dev.gustavosa.inclusaodigital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/oficina/materiais")
public class MaterialController {

    @GetMapping
    public String index(Model model) {
        model.addAttribute("topics", MaterialTopic.values());
        return "oficina/materiais/index";
    }

    @GetMapping("/{slug}")
    public String show(@PathVariable String slug, Model model) {
        Optional<MaterialTopic> topic = MaterialTopic.fromSlug(slug);
        if (topic.isEmpty()) {
            return "redirect:/oficina/materiais";
        }
        model.addAttribute("topic", topic.get());
        return "oficina/materiais/artigo";
    }
}
