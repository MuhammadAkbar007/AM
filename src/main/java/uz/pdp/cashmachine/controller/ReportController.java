package uz.pdp.cashmachine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.cashmachine.payload.ApiResponse;
import uz.pdp.cashmachine.service.AtmService;
import uz.pdp.cashmachine.service.TransferService;

import java.util.UUID;

@RestController
@RequestMapping("/api/report")
@PreAuthorize("hasRole('DIRECTOR')")
public class ReportController {
    @Autowired
    AtmService atmService;
    @Autowired
    TransferService transferService;

    //bankomatdagi mablag`
    @GetMapping("/{id}")
    public HttpEntity<?> getBalance(@RequestBody UUID id) {
        ApiResponse apiResponse = atmService.balance(id);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getObject());
        return ResponseEntity.status(409).body(apiResponse.getMessage());
    }

    @GetMapping
    public HttpEntity<?> getAll() {
        ApiResponse apiResponse = transferService.getAll();
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getObject());
        return ResponseEntity.status(409).body(apiResponse.getMessage());
    }

    @GetMapping("/income")
    public HttpEntity<?> getIncome(@RequestBody String atmSerialNumber) {
        ApiResponse apiResponse = transferService.getIncome(atmSerialNumber);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getObject());
        return ResponseEntity.status(409).body(apiResponse.getMessage());
    }

    @GetMapping("/outcome")
    public HttpEntity<?> getOutcome(@RequestBody String atmSerialNumber) {
        ApiResponse apiResponse = transferService.getOutcome(atmSerialNumber);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getObject());
        return ResponseEntity.status(409).body(apiResponse.getMessage());
    }

    @GetMapping("/byWorker")
    public HttpEntity<?> byWorker(@RequestBody String atmSerialNumber) {
        ApiResponse apiResponse = transferService.getByWorker(atmSerialNumber);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getObject());
        return ResponseEntity.status(409).body(apiResponse.getMessage());
    }
}
