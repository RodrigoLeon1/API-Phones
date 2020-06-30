package com.phones.phones.controller;

import com.phones.phones.TestFixture;
import com.phones.phones.exception.call.CallDoesNotExistException;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.Call;
import com.phones.phones.model.User;
import com.phones.phones.service.CallService;
import com.phones.phones.session.SessionManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class CallControllerTest {

    CallController callController;

    @Mock
    CallService callService;
    @Mock
    SessionManager sessionManager;


    @Before
    public void setUp() {
        initMocks(this);
        callController = new CallController(callService, sessionManager);
    }

    @Test
    public void findAllCallsOk() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<Call> listOfCalls = TestFixture.testListOfCalls();
        List<Call> testCalls = TestFixture.testListOfCalls();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(callService.findAll()).thenReturn(listOfCalls);

       List<Call> returnedCalls = callController.findAllCalls("123");

        assertEquals(testCalls.size(), returnedCalls.size());
        assertEquals(testCalls.get(0).getOriginNumber(), returnedCalls.get(0).getOriginNumber());
    }


    @Test
    public void findAllCallByIdOk() throws UserSessionDoesNotExistException, CallDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        Call call = TestFixture.testCall();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(callService.findById(1L)).thenReturn(call);

        Call returnedCall = callController.findCallById("123", 1L);

        assertEquals(call.getId(), returnedCall.getId());
        assertEquals(call.getDuration(), returnedCall.getDuration());
        assertEquals(call.getOriginLine(), returnedCall.getOriginLine());
        assertEquals(1L, returnedCall.getId());
    }

}
