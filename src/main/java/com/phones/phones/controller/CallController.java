package com.phones.phones.controller;

import com.phones.phones.exception.call.CallNotExistException;
import com.phones.phones.exception.line.LineNotExistException;
import com.phones.phones.exception.line.LineNumberNotExistException;
import com.phones.phones.exception.user.UserSessionNotExistException;
import com.phones.phones.model.Call;
import com.phones.phones.model.User;
import com.phones.phones.projection.CallDuration;
import com.phones.phones.service.CallService;
import com.phones.phones.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calls")
public class CallController {

    private final CallService callService;
    private final SessionManager sessionManager;

    @Autowired
    public CallController(final CallService callService,
                          final SessionManager sessionManager) {
        this.callService = callService;
        this.sessionManager = sessionManager;
    }


    @GetMapping("/")
    public ResponseEntity<List<Call>> findAllCalls(@RequestHeader("Authorization") String sessionToken) throws UserSessionNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser.hasRoleEmployee()) {
            List<Call> calls = callService.findAll();
            return (calls.size() > 0) ? ResponseEntity.ok(calls) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // No encuentra el id...
    @GetMapping("/{id}")
    public ResponseEntity<Call> findCallById(@RequestHeader("Authorization") String sessionToken,
                                             @PathVariable final Long id) throws CallNotExistException, UserSessionNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser.hasRoleEmployee()) {
            return ResponseEntity.ok(callService.findById(id));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    /* Ejercicio parcial */
    @GetMapping("/lines/duration")
    public ResponseEntity<List<CallDuration>> findCallsByOriginAndDestinyLines(@RequestHeader("Authorization") String sessionToken,
                                                                               @RequestParam(name = "origin") final String lineOrigin,
                                                                               @RequestParam(name = "destiny") final String lineDestiny) throws LineNumberNotExistException, UserSessionNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser.hasRoleEmployee()) {
            List<CallDuration> calls = callService.findCallsDurationByOriginAndDestiny(lineOrigin, lineDestiny);
            return (calls.size() > 0) ? ResponseEntity.ok(calls) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
