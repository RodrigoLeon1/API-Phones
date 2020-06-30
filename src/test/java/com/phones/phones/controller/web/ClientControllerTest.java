package com.phones.phones.controller.web;

import com.phones.phones.TestFixture;
import com.phones.phones.controller.UserController;
import com.phones.phones.dto.CityTopDto;
import com.phones.phones.exception.user.UserDoesNotExistException;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.Call;
import com.phones.phones.model.Invoice;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

public class ClientControllerTest {
    ClientController clientController;

    @Mock
    UserController userController;

    @Before
    public void setUp() {
        initMocks(this);
        clientController = new ClientController(userController);
    }


    @Test
    public void findCallsByUserSessionBetweenDatesOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        List<Call> testCalls = TestFixture.testListOfCalls();
        when(userController.findCallsByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020")).thenReturn(testCalls);
        ResponseEntity<List<Call>> returnedCalls = clientController.findCallsByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020");

        assertEquals(testCalls.size(), returnedCalls.getBody().size());
        assertEquals(testCalls.get(0).getOriginNumber(), returnedCalls.getBody().get(0).getOriginNumber());
    }


    @Test
    public void findCallsByUserSessionBetweenDatesNoContent() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        List<Call> emptyCalls = new ArrayList<>();

        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        Mockito.when(userController.findCallsByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020")).thenReturn(emptyCalls);

        ResponseEntity<List<Call>> returnedCalls = clientController.findCallsByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020");

        assertEquals(response.getStatusCode(), returnedCalls.getStatusCode());
    }


    @Test
    public void findInvoicesByUserSessionBetweenDatesOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        List<Invoice> testInvoices = TestFixture.testListOfInvoices();
        when(userController.findInvoicesByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020")).thenReturn(testInvoices);
        ResponseEntity<List<Invoice>> returnedInvoices = clientController.findInvoicesByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020");

        assertEquals(testInvoices.size(), returnedInvoices.getBody().size());
        assertEquals(testInvoices.get(0).getTotalPrice(), returnedInvoices.getBody().get(0).getTotalPrice());
    }

    @Test
    public void findInvoicesByUserSessionBetweenDatesNoContent() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        List<Invoice> emptyCalls = new ArrayList<>();

        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        when(userController.findInvoicesByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020")).thenReturn(emptyCalls);

        ResponseEntity<List<Invoice>> returnedCalls = clientController.findInvoicesByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020");
        assertEquals(response.getStatusCode(), returnedCalls.getStatusCode());
    }


    @Test
    public void findTopCitiesCallsByUserSession() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        List<CityTopDto> testCities = TestFixture.testListOfCityTop();
        when(userController.findTopCitiesCallsByUserSession("123")).thenReturn(testCities);

        ResponseEntity<List<CityTopDto>> returnedCities = clientController.findTopCitiesCallsByUserSession("123");

        assertEquals(testCities.size(), returnedCities.getBody().size());
        assertEquals(testCities.get(0).getName(), returnedCities.getBody().get(0).getName());
        assertEquals(testCities.get(0).getQuantity(), returnedCities.getBody().get(0).getQuantity());
        assertEquals("Capital Federal", returnedCities.getBody().get(0).getName());
        assertEquals(15, returnedCities.getBody().get(1).getQuantity());
    }


    @Test
    public void findTopCitiesCallsByUserSessionNoContent() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        List<CityTopDto> emptyCitiesList = new ArrayList<>();

        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        when(userController.findTopCitiesCallsByUserSession("123")).thenReturn(emptyCitiesList);

        ResponseEntity<List<CityTopDto>> returnedCalls = clientController.findTopCitiesCallsByUserSession("123");
        assertEquals(response.getStatusCode(), returnedCalls.getStatusCode());
    }
}
