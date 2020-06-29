package com.phones.phones.controller.web;

import com.phones.phones.TestFixture;
import com.phones.phones.controller.UserController;
import com.phones.phones.dto.UserLoginDto;
import com.phones.phones.exception.user.UserInvalidLoginException;
import com.phones.phones.model.User;
import com.phones.phones.session.SessionManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LoginControllerTest {

    LoginController loginController;

    @Mock
    UserController userController;
    @Mock
    SessionManager sessionManager;

    @Before
    public void setUp() {
        initMocks(this);
        loginController = new LoginController(userController, sessionManager);
    }


    @Test
    public void loginOk() throws UserInvalidLoginException{
        UserLoginDto userLoginDto = TestFixture.testUserLoginDto();
        User user = TestFixture.testUser();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token");
        ResponseEntity response = ResponseEntity.ok().headers(headers).build();

        when(userController.login(userLoginDto.getUsername(), userLoginDto.getPassword())).thenReturn(Optional.ofNullable(user));
        when(sessionManager.createSession(user)).thenReturn("token");
        ResponseEntity returnedResponse = loginController.login(TestFixture.testUserLoginDto());
        assertEquals(response.getHeaders().toString(), returnedResponse.getHeaders().toString());
    }

    @Test(expected = UserInvalidLoginException.class)
    public void loginUserInvalidLoginException() throws UserInvalidLoginException{
        UserLoginDto userLoginDto = TestFixture.testUserLoginDto();

        when(userController.login(userLoginDto.getUsername(), userLoginDto.getPassword())).thenReturn(Optional.empty());
        loginController.login(TestFixture.testUserLoginDto());
    }

    @Test
    public void logoutOk(){
        ResponseEntity response = ResponseEntity.ok().build();

        doNothing().when(sessionManager).removeSession("123");
        ResponseEntity returnedResponse = loginController.logout("123");
        assertEquals(response.getBody(), returnedResponse.getBody());
    }


}
