package com.phones.phones.controller;

import com.phones.phones.dto.RateDto;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.User;
import com.phones.phones.service.RateService;
import com.phones.phones.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RateController {

    private final RateService rateService;
    private final SessionManager sessionManager;

    @Autowired
    public RateController(final RateService rateService,
                          final SessionManager sessionManager) {
        this.rateService = rateService;
        this.sessionManager = sessionManager;
    }


    public List<RateDto> findAllRates(@RequestHeader("Authorization") final String sessionToken) throws UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return rateService.findAll();
    }

}
