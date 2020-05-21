package com.phones.phones.service;

import com.phones.phones.exception.user.UserAlreadyExistException;
import com.phones.phones.exception.user.UserNotExistException;
import com.phones.phones.exception.user.UsernameAlreadyExistException;
import com.phones.phones.model.City;
import com.phones.phones.model.Province;
import com.phones.phones.model.User;
import com.phones.phones.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static java.util.Optional.ofNullable;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceTest {

    UserService userService;

    @Mock
    UserRepository userRepository;

    @Before
    public void setUp() {
        initMocks(this);
    }


    @Test
    public void testAddOk() {
    }

    @Test(expected = UserAlreadyExistException.class)
    public void testAddUserAlreadyExist() {
    }

    @Test(expected = UsernameAlreadyExistException.class)
    public void testAddUsernameAlreadyExist(){
    }

    @Test
    public void testGetAllOk() {
    }

    /*
    @Test
    public void testGetByIdOk() throws UserNotExistException {
        User userGetById = User
                            .builder()
                            .id(1L)
                            .name("Rodrigo")
                            .surname("Leon")
                            .dni("404040")
                            .username("rl")
                            .password("123")
                            //.city(City.builder().id(1L).name("Mdp").prefix("223").build())
                            .build();

        when(userRepository.findById(1L)).thenReturn(ofNullable(userGetById));

        User returnedUser = userService.getById(1L);

        assertEquals(userGetById.getId(), returnedUser.getId());
    }

    @Test(expected = UserNotExistException.class)
    public void testGetByIdUserNotExist() {
    }

    @Test
    public void testDisableByIdOk() {
    }

    @Test(expected = UserNotExistException.class)
    public void testDisableByIdUserNotExist() {
    }

    @Test
    public void testUpdateByIdOk() {
    }

    @Test(expected = UserNotExistException.class)
    public void testUpdateByIdUserNotExist() {
    }

    @Test
    public void testConverterToDtoOk() {
    }*/

}