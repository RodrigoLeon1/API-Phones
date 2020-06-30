package com.phones.phones.controller.web;

public class ClientControllerTest {
/*
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
        ResponseEntity<List<Call>> calls = ResponseEntity.ok(TestFixture.testListOfCalls());
        when(userController.findCallsByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020")).thenReturn(calls);
        ResponseEntity<List<Call>> returnedCalls = clientController.findCallsByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020");

        assertEquals(calls.getBody().get(0).getId(), returnedCalls.getBody().get(0).getId());
        assertEquals(calls.getBody().get(0).getDestinationNumber(), returnedCalls.getBody().get(0).getDestinationNumber());
        assertEquals(calls.getBody().get(0).getCreationDate(), returnedCalls.getBody().get(0).getCreationDate());
        assertEquals(1L, returnedCalls.getBody().get(0).getId());
    }

    @Test
    public void findInvoicesByUserSessionBetweenDatesOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        ResponseEntity<List<Invoice>> invoices = ResponseEntity.ok(TestFixture.testListOfInvoices());
        when(userController.findInvoicesByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020")).thenReturn(invoices);
        ResponseEntity<List<Invoice>> returnedInvoices = clientController.findInvoicesByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020");

        assertEquals(invoices.getBody().get(0).getId(), returnedInvoices.getBody().get(0).getId());
        assertEquals(invoices.getBody().get(0).getNumberCalls(), returnedInvoices.getBody().get(0).getNumberCalls());
        assertEquals(1L, returnedInvoices.getBody().get(0).getId());
    }


    @Test
    public void findTopCitiesCallsByUserSession() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        ResponseEntity<List<CityTopDto>> topCities = ResponseEntity.ok(TestFixture.testListOfCityTop());
        when(userController.findTopCitiesCallsByUserSession("123")).thenReturn(topCities);
        ResponseEntity<List<CityTopDto>> returnedInvoices = clientController.findTopCitiesCallsByUserSession("123");

        assertEquals(topCities.getBody().get(0).getName(), returnedInvoices.getBody().get(0).getName());
        assertEquals(topCities.getBody().get(0).getQuantity(), returnedInvoices.getBody().get(0).getQuantity());
        assertEquals("Capital Federal", returnedInvoices.getBody().get(0).getName());
    }*/
}
