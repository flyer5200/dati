package com.dati.base;

import com.dati.auth.domain.service.UserService;
import com.dati.base.pojo.BaseResourceVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("BaseAssembler Tests")
class BaseAssemblerTest {

    @Mock
    private UserService userService;

    static class DummyResourceVO extends BaseResourceVO {}

    @Test
    @DisplayName("Should fallback createdUserName to Guest when createdBy starts with guest")
    void fillUserInfo_guestCreatedBy_shouldFallbackToGuest() {
        BaseAssembler assembler = new BaseAssembler();
        ReflectionTestUtils.setField(assembler, "userService", userService);
        when(userService.getUserMap(any())).thenReturn(Map.of());

        DummyResourceVO vo = new DummyResourceVO();
        vo.setCreatedBy("guest-123456");

        assembler.fillUserInfo(List.of(vo));

        assertThat(vo.getCreatedUserName()).isEqualTo("Guest");
    }
}
