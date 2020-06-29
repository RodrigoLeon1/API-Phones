package com.phones.phones.controller;

import com.phones.phones.exception.city.CityAlreadyExistException;
import com.phones.phones.exception.city.CityDoesNotExistException;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.City;
import com.phones.phones.model.User;
import com.phones.phones.service.CityService;
import com.phones.phones.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;
import java.util.List;

@Controller
public class CityController {

    private final CityService cityService;
    private final SessionManager sessionManager;

    @Autowired
    public CityController(final CityService cityService,
                          final SessionManager sessionManager) {
        this.cityService = cityService;
        this.sessionManager = sessionManager;
    }


    public City createCity(@RequestHeader("Authorization") String sessionToken,
                           @RequestBody @Valid final City city) throws CityAlreadyExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return cityService.create(city);
    }

    public List<City> findAllCities(@RequestHeader("Authorization") final String sessionToken) throws UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return cityService.findAll();
    }

    public City findCityById(@RequestHeader("Authorization") String sessionToken,
                                             @PathVariable final Long id) throws CityDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return cityService.findById(id);
    }

}
