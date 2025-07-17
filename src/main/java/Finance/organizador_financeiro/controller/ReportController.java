package Finance.organizador_financeiro.controller;

import Finance.organizador_financeiro.dto.CategorySpendingDTO;
import Finance.organizador_financeiro.dto.SummaryReportDTO;
import Finance.organizador_financeiro.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/summary")
    public ResponseEntity<SummaryReportDTO> getSummaryReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        SummaryReportDTO summary = reportService.getSummaryReport(startDate, endDate);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/spending-by-category")
    public ResponseEntity<List<CategorySpendingDTO>> getSpendingByCategory(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<CategorySpendingDTO> spending = reportService.getSpendingByCategory(startDate, endDate);
        return ResponseEntity.ok(spending);
    }
}
