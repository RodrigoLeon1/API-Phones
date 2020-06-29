package com.phones.phones.controller;

import com.phones.phones.exception.invoice.InvoiceDoesNotExistException;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.Invoice;
import com.phones.phones.model.User;
import com.phones.phones.service.InvoiceService;
import com.phones.phones.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final SessionManager sessionManager;

    @Autowired
    public InvoiceController(final InvoiceService invoiceService,
                             final SessionManager sessionManager) {
        this.invoiceService = invoiceService;
        this.sessionManager = sessionManager;
    }


    public List<Invoice> findAllInvoices(@RequestHeader("Authorization") final String sessionToken) throws UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return invoiceService.findAll();
    }

    public Invoice findInvoiceById(@RequestHeader("Authorization") final String sessionToken,
                                                   @PathVariable final Long id) throws InvoiceDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return invoiceService.findById(id);
    }

}
