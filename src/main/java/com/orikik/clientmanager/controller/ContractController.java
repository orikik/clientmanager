package com.orikik.clientmanager.controller;

import com.orikik.clientmanager.dto.ContractDto;
import com.orikik.clientmanager.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/contract")
public class ContractController {
    @Autowired
    ContractService contractService;

    @PostMapping("/create/{partnerCode}")
    public ContractDto createContract(Principal principal, @PathVariable Long partnerCode,
                                      @RequestBody ContractDto contractDto) {
        return contractService.createContract(contractDto, principal.getName(), partnerCode);
    }

    @PutMapping("/update")
    public ContractDto updateContract(Principal principal, @RequestBody ContractDto contractDto) {
        return contractService.updateContract(contractDto, principal.getName());
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete")
    public void deleteContract(Principal principal, @RequestParam(required = true) Long addendumNumber) {
        contractService.deleteContract(addendumNumber, principal.getName());
    }
}
