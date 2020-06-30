package com.phones.phones.controller;

import com.phones.phones.TestFixture;
import com.phones.phones.exception.city.CityAlreadyExistException;
import com.phones.phones.exception.city.CityDoesNotExistException;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.City;
import com.phones.phones.model.User;
import com.phones.phones.service.CityService;
import com.phones.phones.session.SessionManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CityControllerTest {

    CityController cityController;

    @Mock
    CityService cityService;
    @Mock
    SessionManager sessionManager;


    @Before
    public void setUp() {
        initMocks(this);
        cityController = new CityController(cityService, sessionManager);
    }

    @Test
    public void createCityOk() throws UserSessionDoesNotExistException, CityAlreadyExistException {
        User loggedUser = TestFixture.testUser();
        City newCity = TestFixture.testCity();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(cityService.create(newCity)).thenReturn(newCity);


        City createdCity = cityController.createCity("123", newCity);

        assertEquals(newCity.getProvince(), createdCity.getProvince());
        assertEquals(newCity.getName(), createdCity.getName());
    }



    @Test
    public void findAllCitiesOk() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<City> listOfCities = TestFixture.testListOfCities();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(cityService.findAll()).thenReturn(listOfCities);

        List<City> returnedCities = cityController.findAllCities("123");

        assertEquals(listOfCities.size(), returnedCities.size());
        assertEquals(listOfCities.get(0).getName(), returnedCities.get(0).getName());
        assertEquals(listOfCities.get(0).getPrefix(), returnedCities.get(0).getPrefix());
    }


    @Test
    public void findCityByIdOk() throws UserSessionDoesNotExistException, CityDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        City city = TestFixture.testCity();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(cityService.findById(1L)).thenReturn(city);

        City returnedCity = cityController.findCityById("123", 1L);

        assertEquals(city.getId(), returnedCity.getId());
        assertEquals(city.getPrefix(), returnedCity.getPrefix());
        assertEquals(city.getProvince(), returnedCity.getProvince());
        assertEquals(1L, returnedCity.getId());
    }


}
