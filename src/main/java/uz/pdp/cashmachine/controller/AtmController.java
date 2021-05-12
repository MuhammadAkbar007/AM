package uz.pdp.cashmachine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cashmachine.entity.AtmBox;
import uz.pdp.cashmachine.payload.ApiResponse;
import uz.pdp.cashmachine.payload.AtmDto;
import uz.pdp.cashmachine.payload.CardDto;
import uz.pdp.cashmachine.service.AtmService;
import uz.pdp.cashmachine.service.TransferService;

@RestController
@RequestMapping("/api/atm")
public class AtmController {
    @Autowired
    AtmService atmService;
    @Autowired
    TransferService transferService;

    //bankomatni ro`yxatdan o`tkazish
    //faqat direktor uchun
    @PreAuthorize("hasRole('DIRECTOR')")
    @PostMapping
    public HttpEntity<?> addATM(@RequestBody AtmDto atmDto) {
        ApiResponse apiResponse = atmService.addATM(atmDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse.getMessage());
    }

    //pin kodni tekshirish
    @PostMapping("/card/verify")
    public HttpEntity<?>verifyCard(@RequestBody CardDto cardDto){
        ApiResponse apiResponse = atmService.verifyCard(cardDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    //bankamat hisobini xodim tomonidan to`ldirish
    @PreAuthorize("hasRole('WORKER')")
    @PutMapping("/{atmSerialNumber}")
    public HttpEntity<?> create(@PathVariable String atmSerialNumber, @RequestBody AtmBox dto) {
        ApiResponse apiResponse = transferService.update(atmSerialNumber, dto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse.getMessage());
    }
}
