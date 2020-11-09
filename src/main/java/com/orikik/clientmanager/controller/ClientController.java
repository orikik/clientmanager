package com.orikik.clientmanager.controller;

import com.orikik.clientmanager.dto.ClientDto;
import com.orikik.clientmanager.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    ClientService clientService;

    @PostMapping("/create")
    public ClientDto createClientAndHisContract(Principal principal, @RequestBody ClientDto clientDto) {
        return clientService.createClientAndHisContract(clientDto, principal.getName());
    }

    @PutMapping("/update")
    public ClientDto updateClient(@RequestBody ClientDto clientDto) {
        return clientService.updateClient(clientDto);
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete")
    public void deleteClient(Principal principal, @RequestParam(required = true) Long partnerCode) {
        clientService.deleteClient(partnerCode, principal.getName());
    }
}
