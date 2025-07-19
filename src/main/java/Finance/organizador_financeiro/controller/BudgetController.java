package Finance.organizador_financeiro.controller;

import Finance.organizador_financeiro.dto.BudgetDTO;
import Finance.organizador_financeiro.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<BudgetDTO> createBudget(@RequestBody BudgetDTO budgetDTO) {
        try {
            BudgetDTO createdBudget = budgetService.createBudget(budgetDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBudget);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null); // Or a more specific error response
        }
    }

    @GetMapping
    public ResponseEntity<List<BudgetDTO>> getAllBudgets() {
        List<BudgetDTO> budgets = budgetService.getAllBudgets();
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetDTO> getBudgetById(@PathVariable Long id) {
        return budgetService.getBudgetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetDTO> updateBudget(@PathVariable Long id, @RequestBody BudgetDTO budgetDTO) {
        try {
            BudgetDTO updatedBudget = budgetService.updateBudget(id, budgetDTO);
            return ResponseEntity.ok(updatedBudget);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null); // Or a more specific error response
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        if (budgetService.deleteBudget(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/period")
    public ResponseEntity<List<BudgetDTO>> getBudgetsForPeriod(
            @RequestParam int year,
            @RequestParam int month) {
        List<BudgetDTO> budgets = budgetService.getBudgetsForPeriod(year, month);
        return ResponseEntity.ok(budgets);
    }
}
