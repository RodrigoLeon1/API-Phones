package com.phones.phones.service;

import com.phones.phones.TestFixture;
import com.phones.phones.dto.UserDto;
import com.phones.phones.exception.user.*;
import com.phones.phones.model.User;
import com.phones.phones.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceTest {

    UserService userService;

    @Mock
    UserService userService2;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        initMocks(this);
        this.userService = new UserService(userRepository, passwordEncoder);
    }


    @Test
    public void testCreateOk() throws UsernameAlreadyExistException, UserAlreadyExistException {
        User newUser = TestFixture.testUser();

        when(userRepository.save(newUser)).thenReturn(newUser);

        User returnedUser = this.userService.create(newUser);

        assertEquals(newUser.getId(), returnedUser.getId());
        assertEquals(newUser.getName(), returnedUser.getName());
    }

    @Test(expected = UserAlreadyExistException.class)
    public void testCreateExistingUser() throws UsernameAlreadyExistException, UserAlreadyExistException {

        User newUser = TestFixture.testUser();
        when(this.userRepository.findByDni(newUser.getDni())).thenReturn(Optional.ofNullable(newUser));
        userService.create(newUser);
    }

    @Test
    public void testFindAllOk() {
        List<User> allUsers = TestFixture.testListOfUsers();
        when(userRepository.findAll()).thenReturn(allUsers);

        List<User> returnedUsers = userService.findAll();

        assertEquals(returnedUsers.size(), allUsers.size());
        assertEquals(returnedUsers.get(0).getId(), allUsers.get(0).getId());
    }

    @Test
    public void testFindAllEmpty() {
       List<User> emptyList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(emptyList);

        List<User> returnedUsers = userService.findAll();
        assertEquals(returnedUsers.size(), 0);
    }

    @Test
    public void testFindByIdOk() throws UserDoesNotExistException {
        User userGetById = TestFixture.testUser();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(userGetById));

        User returnedUser = this.userService.findById(1L);

        assertEquals(userGetById.getId(), returnedUser.getId());
    }


    @Test(expected = UserDoesNotExistException.class)
    public void testFindByIdUserNotExist() throws UserDoesNotExistException {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        this.userService.findById(2L);
    }


    /***
     * Returns True if disabled
     * @throws UserDoesNotExistException
     * @throws UserAlreadyDisableException
     */
    @Test
    public void testDisableByIdOk() throws UserDoesNotExistException, UserAlreadyDisableException {
        User disabledUser = TestFixture.testUser();
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(disabledUser));
        when(userRepository.disableById(1L)).thenReturn(1);
        boolean disabled = userService.disableById(disabledUser.getId());
        assertEquals(true, disabled);
    }

    @Test
    public void testDisableByIdError() throws UserDoesNotExistException, UserAlreadyDisableException {
        User disabledUser = TestFixture.testUser();
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(disabledUser));
        when(userRepository.disableById(1L)).thenReturn(0);
        boolean disabled = userService.disableById(disabledUser.getId());
        assertEquals(false, disabled);
    }



    @Test(expected = UserDoesNotExistException.class)
    public void testDisableByIdUserNotExist() throws UserDoesNotExistException, UserAlreadyDisableException {

        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        this.userService.disableById(2L);
    }

    @Test(expected = UserAlreadyDisableException.class)
    public void testDisableByIdUserIsNotActive() throws UserDoesNotExistException, UserAlreadyDisableException {
        User disabledUser = TestFixture.testDisabledUser();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(disabledUser));
        this.userService.disableById(1L);
    }




    @Test
    public void testUpdateByIdOk() throws UserDoesNotExistException, UsernameAlreadyExistException {
        User loggedUser = TestFixture.testUser();
        UserDto update = TestFixture.testUserDto();
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(loggedUser));
        when(userService2.existsUsername("rl")).thenReturn(false);
        when(passwordEncoder.encode(update.getPassword())).thenReturn("asd32a1s3d21as");
        when(userRepository.save(loggedUser)).thenReturn(loggedUser);

         User updatedUser  = userService.updateById(1L, update);

        assertEquals(loggedUser.getId(), updatedUser.getId());
        assertEquals(loggedUser.getUsername(), updatedUser.getUsername());

    }

    @Test(expected = UserDoesNotExistException.class)
    public void testUpdateByIdOkExistingUser() throws UsernameAlreadyExistException, UserDoesNotExistException {

        UserDto newUser = TestFixture.testUserDto();
        when(this.userRepository.findById(1L)).thenReturn(Optional.empty());
        userService.updateById(1L, newUser);
    }

    @Test
    public void testExistsUsernameOk() {
        when(userRepository.findByUsernameBoolean("username")).thenReturn(1);
        boolean exists = userService.existsUsername("username");
        assertEquals(true, exists);
    }

    @Test
    public void testDoesNotExistsUsernameOk() {
        when(userRepository.findByUsernameBoolean("username")).thenReturn(0);
        boolean exists = userService.existsUsername("username");
        assertEquals(false, exists);
    }



    public boolean existsUsernameToUpdate(String username, Long id) {
        return userRepository.findByUsernameToUpdateBoolean(username, id) > 0;
    }
    @Test
    public void existsUsernameToUpdateOk() {
        when(userRepository.findByUsernameToUpdateBoolean("username", 1L)).thenReturn(1);
        boolean exists = userService.existsUsernameToUpdate("username", 1L);
        assertEquals(true, exists);
    }

    @Test
    public void testDoesNotExistsUsernameToUpdate() {
        when(userRepository.findByUsernameBoolean("username")).thenReturn(0);
        boolean exists = userService.existsUsernameToUpdate("username", 1L);
        assertEquals(false, exists);
    }


    @Test
    public void testLoginOk() throws UserInvalidLoginException {
        User loggedUser = TestFixture.testUser();
        when(userRepository.findByUsername("rl")).thenReturn(Optional.ofNullable(loggedUser));
        when(passwordEncoder.matches("123", loggedUser.getPassword())).thenReturn(true);
        Optional<User> returnedUser = userService.login("rl","123");

        assertEquals(loggedUser.getId(), returnedUser.get().getId());
        assertEquals(loggedUser.getUsername(), returnedUser.get().getUsername());
        verify(userRepository, times(1)).findByUsername("rl");
    }


    @Test (expected = UserInvalidLoginException.class)
    public void testLoginUserInvalidLogin() throws UserInvalidLoginException {
        User loggedUser = TestFixture.testUser();
        when(userRepository.findByUsername("rl")).thenReturn(Optional.empty());
        when(passwordEncoder.matches("123", loggedUser.getPassword())).thenReturn(true);
        userService.login("rl","123");
    }

    @Test (expected = UserInvalidLoginException.class)
    public void testLoginWrongUserAndPass() throws UserInvalidLoginException {
        User loggedUser = TestFixture.testUser();
        when(userRepository.findByUsername("rl")).thenReturn(Optional.empty());
        when(passwordEncoder.matches("123", loggedUser.getPassword())).thenReturn(false);
        userService.login("rl","123");
    }

    @Test (expected = UserInvalidLoginException.class)
    public void testLoginInvalidPass() throws UserInvalidLoginException {
        User loggedUser = TestFixture.testUser();
        when(userRepository.findByUsername("rl")).thenReturn(Optional.ofNullable(loggedUser));
        when(passwordEncoder.matches("123", loggedUser.getPassword())).thenReturn(false);
        userService.login("rl","123");
    }


}
