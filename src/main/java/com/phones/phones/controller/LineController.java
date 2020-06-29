package com.phones.phones.controller;

import com.phones.phones.dto.LineDto;
import com.phones.phones.exception.line.LineAlreadyDisabledException;
import com.phones.phones.exception.line.LineDoesNotExistException;
import com.phones.phones.exception.line.LineNumberAlreadyExistException;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.Call;
import com.phones.phones.model.Line;
import com.phones.phones.model.User;
import com.phones.phones.service.CallService;
import com.phones.phones.service.LineService;
import com.phones.phones.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;
import java.util.List;

@Controller
public class LineController {

    private final LineService lineService;
    private final CallService callService;
    private final SessionManager sessionManager;

    @Autowired
    public LineController(final LineService lineService,
                          final CallService callService,
                          final SessionManager sessionManager) {
        this.lineService = lineService;
        this.callService = callService;
        this.sessionManager = sessionManager;
    }


    public Line createLine(@RequestHeader("Authorization") String sessionToken,
                           @RequestBody @Valid final Line line) throws LineNumberAlreadyExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return lineService.create(line);
    }

    public List<Line> findAllLines(@RequestHeader("Authorization") String sessionToken) throws UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return lineService.findAll();
    }

    public Line findLineById(@RequestHeader("Authorization") String sessionToken,
                             @PathVariable final Long id) throws LineDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return lineService.findById(id);
    }

    public List<Call> findCallsByLineId(@RequestHeader("Authorization") String sessionToken,
                                        @PathVariable final Long id) throws LineDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return callService.findByLineId(id);
    }

    public int deleteLineById(@RequestHeader("Authorization") final String sessionToken,
                              @PathVariable final Long id) throws LineDoesNotExistException, UserSessionDoesNotExistException, LineAlreadyDisabledException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return lineService.disableById(id);
    }

    public boolean updateLineByIdLine(@RequestHeader("Authorization") final String sessionToken,
                                      @RequestBody @Valid final LineDto updatedLine,
                                      @PathVariable final Long id) throws LineDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return lineService.updateLineByIdLine(id, updatedLine);
    }

}
