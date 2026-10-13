package com.dati.auth.server.controller;

import com.dati.auth.domain.service.GuestService;
import com.dati.auth.server.pojo.LoginResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "auth-controller")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @PostMapping("/guest")
    public LoginResponse guestLogin() {
        String token = guestService.issueGuestToken();
        return new LoginResponse(token);
    }

}
