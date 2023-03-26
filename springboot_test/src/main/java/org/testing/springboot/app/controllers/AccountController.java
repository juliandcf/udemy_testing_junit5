package org.testing.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.testing.springboot.app.dtos.TransferDTO;
import org.testing.springboot.app.models.Account;
import org.testing.springboot.app.services.AccountService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account detail(@PathVariable Long id) {
        return accountService.findById(id);
    }

    @PostMapping("transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferDTO transferDTO) {
        accountService.transfer(transferDTO.getSourceAccountId(), transferDTO.getDestinationAccountId(), transferDTO.getAmount(), transferDTO.getBankId());

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Succesful transfer");
        response.put("transfer", transferDTO);

        return ResponseEntity.ok(response);
    }
}
