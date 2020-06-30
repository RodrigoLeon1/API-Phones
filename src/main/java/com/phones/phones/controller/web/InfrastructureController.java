package com.phones.phones.controller.web;

import com.phones.phones.dto.InfrastructureCallDto;
import com.phones.phones.exception.line.LineCannotMakeCallsException;
import com.phones.phones.exception.line.LineNumberDoesNotExistException;
import com.phones.phones.exception.user.UserInvalidLoginException;
import com.phones.phones.model.Call;
import com.phones.phones.service.CallService;
import com.phones.phones.utils.RestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/infrastructure")
public class InfrastructureController {

    private final CallService callService;
    private final String token = "infrastructure1";

    public InfrastructureController(final CallService callService) {
        this.callService = callService;
    }


    @PostMapping("/call")
    public ResponseEntity createCall(@RequestHeader("Authorization") final String sessionToken,
                                     @RequestBody @Valid final InfrastructureCallDto infrastructureCallDto) throws LineNumberDoesNotExistException, LineCannotMakeCallsException, UserInvalidLoginException {
        if (token.equals(sessionToken)) {
            Call newCall = callService.create(infrastructureCallDto);
            return ResponseEntity.created(RestUtils.getLocation(newCall.getId())).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
