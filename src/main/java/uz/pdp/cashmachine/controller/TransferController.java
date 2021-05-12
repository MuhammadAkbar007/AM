package uz.pdp.cashmachine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.cashmachine.entity.AtmBox;
import uz.pdp.cashmachine.payload.ApiResponse;
import uz.pdp.cashmachine.payload.TransferDto;
import uz.pdp.cashmachine.service.TransferService;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {
    @Autowired
    TransferService service;

    //bankamatdan pul yechish
    @PutMapping("/outcome")
    public HttpEntity<?> outcome(@RequestBody TransferDto transferDto) {
        ApiResponse apiResponse = service.getMoney(transferDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse.getMessage());
    }

    //    bankamatga pul solish
    @PutMapping("/income")
    public HttpEntity<?> income(@RequestBody String atmSerialNumber, AtmBox atmBox) {
        ApiResponse apiResponse = service.update(atmSerialNumber, atmBox);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse.getMessage());
    }
}
