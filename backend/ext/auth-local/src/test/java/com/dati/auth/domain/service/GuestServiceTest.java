package com.dati.auth.domain.service;

import com.dati.auth.authentication.User;
import com.dati.base.exception.DatiException;
import com.dati.base.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("GuestService Tests")
class GuestServiceTest {

    private final JwtTokenHelper jwtTokenHelper = mock(JwtTokenHelper.class);

    @Test
    @DisplayName("Should generate token for ephemeral guest user")
    void issueGuestToken_shouldGenerateTokenWithGuestPrefix() {
        GuestService guestService = new GuestService(jwtTokenHelper, true);
        when(jwtTokenHelper.generateToken(any(User.class))).thenReturn("mock-guest-token");

        String token = guestService.issueGuestToken();

        assertThat(token).isEqualTo("mock-guest-token");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(jwtTokenHelper).generateToken(captor.capture());

        User user = captor.getValue();
        assertThat(user.getId()).startsWith("guest-");
        assertThat(user.getName()).startsWith("guest-");
        assertThat(user.getDisplayName()).isEqualTo("Guest");
    }

    @Test
    @DisplayName("Should throw AUTH_GUEST_DISABLED when guest access is disabled")
    void issueGuestToken_whenDisabled_shouldThrow() {
        GuestService guestService = new GuestService(jwtTokenHelper, false);

        assertThatThrownBy(guestService::issueGuestToken)
                .isInstanceOf(DatiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.AUTH_GUEST_DISABLED);
    }
}
