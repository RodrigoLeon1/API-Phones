package com.phones.phones.controller.web;

import com.phones.phones.TestFixture;
import com.phones.phones.dto.InfrastructureCallDto;
import com.phones.phones.exception.line.LineCannotMakeCallsException;
import com.phones.phones.exception.line.LineNumberDoesNotExistException;
import com.phones.phones.exception.user.UserInvalidLoginException;
import com.phones.phones.model.Call;
import com.phones.phones.service.CallService;
import com.phones.phones.utils.RestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@PrepareForTest(RestUtils.class)
@RunWith(PowerMockRunner.class)
public class InfrastructureControllerTest {

        InfrastructureController infrastructureController;

        @Mock
        CallService callService;


        @Before
        public void setUp() {
            initMocks(this);
            PowerMockito.mockStatic(RestUtils.class);
            infrastructureController = new InfrastructureController(callService );
        }


        @Test
        public void createCallOk() throws LineCannotMakeCallsException, LineNumberDoesNotExistException, UserInvalidLoginException {
            InfrastructureCallDto infrastructureCallDto = TestFixture.testInfrastructureCallDto();
            Call call = TestFixture.testCall();

            when(callService.create(infrastructureCallDto)).thenReturn(call);
            when(RestUtils.getLocation(call.getId())).thenReturn(URI.create("miUri.com"));

            ResponseEntity response = infrastructureController.createCall("infrastructure1", infrastructureCallDto);

            assertEquals(URI.create("miUri.com"), response.getHeaders().getLocation());
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }

    @Test
    public void createCallInvalidToken() throws LineCannotMakeCallsException, LineNumberDoesNotExistException, UserInvalidLoginException {

        InfrastructureCallDto infrastructureCallDto = TestFixture.testInfrastructureCallDto();
        ResponseEntity response = infrastructureController.createCall("infrastructure2", infrastructureCallDto);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
