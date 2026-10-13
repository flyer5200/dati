package com.dati.auth.domain.service;

import com.dati.auth.authentication.User;
import com.dati.base.exception.DatiException;
import com.dati.base.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GuestService {

    private final JwtTokenHelper jwtTokenHelper;
    private final boolean guestEnabled;

    public GuestService(JwtTokenHelper jwtTokenHelper,
                        @Value("${auth.guest.enabled:true}") boolean guestEnabled) {
        this.jwtTokenHelper = jwtTokenHelper;
        this.guestEnabled = guestEnabled;
    }

    public String issueGuestToken() {
        if (!guestEnabled) {
            throw new DatiException(ErrorCode.AUTH_GUEST_DISABLED);
        }
        User guest = new User();
        String guestId = "guest-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        guest.setId(guestId);
        guest.setName(guestId);
        guest.setDisplayName("Guest");
        return jwtTokenHelper.generateToken(guest);
    }
}
