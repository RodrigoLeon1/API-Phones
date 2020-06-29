package com.phones.phones.controller;

import com.phones.phones.exception.province.ProviceAlreadyExistException;
import com.phones.phones.exception.province.ProvinceDoesNotExistException;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.Province;
import com.phones.phones.model.User;
import com.phones.phones.service.ProvinceService;
import com.phones.phones.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ProvinceController {

    private final ProvinceService provinceService;
    private final SessionManager sessionManager;

    @Autowired
    public ProvinceController(final ProvinceService provinceService,
                              final SessionManager sessionManager) {
        this.provinceService = provinceService;
        this.sessionManager = sessionManager;
    }


    public Province createProvince(@RequestHeader("Authorization") final String sessionToken,
                                   @RequestBody @Valid final Province province) throws ProviceAlreadyExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return provinceService.create(province);
    }

    public List<Province> findAllProvinces(@RequestHeader("Authorization") final String sessionToken) throws UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return provinceService.findAll();
    }

    public Province findProvinceById(@RequestHeader("Authorization") final String sessionToken,
                                     @PathVariable final Long id) throws ProvinceDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return provinceService.findById(id);
    }

}
