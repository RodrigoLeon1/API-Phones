package com.phones.phones.controller;

import com.phones.phones.TestFixture;
import com.phones.phones.exception.invoice.InvoiceDoesNotExistException;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.Invoice;
import com.phones.phones.model.User;
import com.phones.phones.service.InvoiceService;
import com.phones.phones.session.SessionManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class InvoiceControllerTest {
    InvoiceController invoiceController;

    @Mock
    InvoiceService invoiceService;
    @Mock
    SessionManager sessionManager;


    @Before
    public void setUp() {
        initMocks(this);
        invoiceController = new InvoiceController(invoiceService, sessionManager);
    }

    @Test
    public void findAllInvoicesOk() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<Invoice> listOfInvoices = TestFixture.testListOfInvoices();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(invoiceService.findAll()).thenReturn(listOfInvoices);

        List<Invoice> returnedInvoices = invoiceController.findAllInvoices("123");

        assertEquals(listOfInvoices.size(), returnedInvoices.size());
        assertEquals(listOfInvoices.get(0).getDueDate(), returnedInvoices.get(0).getDueDate());
        assertEquals(listOfInvoices.get(1).getNumberCalls(), returnedInvoices.get(1).getNumberCalls());
    }


    @Test
    public void findInvoiceByIdOk() throws UserSessionDoesNotExistException, InvoiceDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        Invoice invoice = TestFixture.testInvoice();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(invoiceService.findById(1L)).thenReturn(invoice);

        Invoice returnedInvoices = invoiceController.findInvoiceById("123", 1L);

        assertEquals(invoice.getId(), returnedInvoices.getId());
        assertEquals(invoice.getDueDate(), returnedInvoices.getDueDate());
        assertEquals(invoice.getNumberCalls(), returnedInvoices.getNumberCalls());
        assertEquals(1L, returnedInvoices.getId());
    }

}
