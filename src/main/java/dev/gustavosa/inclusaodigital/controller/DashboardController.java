package dev.gustavosa.inclusaodigital.controller;

import dev.gustavosa.inclusaodigital.dto.ParticipantDetail;
import dev.gustavosa.inclusaodigital.service.DashboardService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("stats", dashboardService.getStats());
        model.addAttribute("participants", dashboardService.listParticipantSummaries());
        return "admin/dashboard";
    }

    @GetMapping("/participantes/{id}")
    public String participantDetail(@PathVariable Long id, Model model) {
        Optional<ParticipantDetail> detail = dashboardService.getParticipantDetail(id);
        if (detail.isEmpty()) {
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("detail", detail.get());
        return "admin/participante-detalhe";
    }

    @GetMapping("/participantes/exportar")
    public ResponseEntity<byte[]> exportCsv() {
        byte[] csv = dashboardService.exportCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=participantes.csv")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csv);
    }
}
