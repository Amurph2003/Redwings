package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.model.Color;
import com.estore.api.estoreapi.model.Jersey;
import com.estore.api.estoreapi.model.Size;
import com.estore.api.estoreapi.persistence.ShoppingCartDAO;

/**
 * Unit test suite for the Shopping Cart Controller.
 * 
 * @author Team Red Wings
 */
@Tag("Controller-tier")
public class ShoppingCartControllerTest {
    private ShoppingCartController shoppingCartController;
    private ShoppingCartDAO mockShoppingCartDAO;

    /**
     * Before each test, create a new ShoppingCartController object and inject
     * a mock Shopping Cart DAO
     */
    @BeforeEach
    public void setupJerseyController() {
        mockShoppingCartDAO = mock(ShoppingCartDAO.class);
        shoppingCartController = new ShoppingCartController(mockShoppingCartDAO);
    }

    @Test
    public void testAddJersey() throws IOException { 
        Jersey jersey = new Jersey(4, "Husso", 35, 36.79, Size.X_LARGE, Color.WHITE,6, "jerseyImages/Munson.jpg");
        when(mockShoppingCartDAO.addJersey("Gustavo", jersey)).thenReturn(jersey);
        ResponseEntity<Jersey> response = shoppingCartController.addJersey("Gustavo", jersey);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(jersey,response.getBody());
    }

    @Test
    public void testAddJerseyFailed() throws IOException { 
        Jersey jersey = new Jersey(4, "Husso", 35, 36.79, Size.X_LARGE, Color.WHITE,1, "jerseyImages/Munson.jpg");
        when(mockShoppingCartDAO.addJersey("Gustavus", jersey)).thenReturn(null);

        ResponseEntity<Jersey> response = shoppingCartController.addJersey("Gustavo", jersey);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testAddJerseyHandleException() throws IOException { 
        Jersey jersey = new Jersey(4, "Husso", 35, 36.79, Size.X_LARGE, Color.WHITE, 1, "jerseyImages/Munson.jpg");
        doThrow(new IOException()).when(mockShoppingCartDAO).addJersey("Gustavo", jersey);

        ResponseEntity<Jersey> response = shoppingCartController.addJersey("Gustavo", jersey);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testRemoveJersey() throws IOException { // removeJersey may throw IOException
        // Setup
        Jersey jersey = new Jersey(4, "Husso", 35, 36.79, Size.X_LARGE, Color.WHITE, 1, "jerseyImages/Munson.jpg");
        
        when(mockShoppingCartDAO.removeJersey("Gustavo", jersey)).thenReturn(true);

        // Invoke
        ResponseEntity<Jersey> response = shoppingCartController.deleteJersey("Gustavo", jersey);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testRemoveJerseyNotFound() throws IOException { // removeJersey may throw IOException
        // Setup
        Jersey jersey = new Jersey(4, "Husso", 35, 36.79, Size.X_LARGE, Color.WHITE, 1, "jerseyImages/Munson.jpg");
        
        when(mockShoppingCartDAO.removeJersey("Gustavo", jersey)).thenReturn(false);

        // Invoke
        ResponseEntity<Jersey> response = shoppingCartController.deleteJersey("Gustavo", jersey);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testRemoveJerseyHandleException() throws IOException { // removeJersey may throw IOException
        Jersey jersey = new Jersey(4, "Husso", 35, 36.79, Size.X_LARGE, Color.WHITE, 1, "jerseyImages/Munson.jpg");
        
        doThrow(new IOException()).when(mockShoppingCartDAO).removeJersey("Gustavo", jersey);

        // Invoke
        ResponseEntity<Jersey> response = shoppingCartController.deleteJersey("Gustavo", jersey);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetCart() throws IOException { 
        Jersey[] jerseys = new Jersey[2];
        jerseys[0] = new Jersey(4, "Husso", 35, 36.79, Size.X_LARGE, Color.WHITE, 1, "jerseyImages/Munson.jpg");
        jerseys[1] = new Jersey(8, "Munson", 21, 36.79, Size.XX_LARGE, Color.WHITE, 3, "jerseyImages/Munson.jpg");
        when(mockShoppingCartDAO.getCart("Gustavo")).thenReturn(jerseys);

        ResponseEntity<Jersey[]> response = shoppingCartController.getCart("Gustavo");

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertArrayEquals(jerseys, response.getBody());
    }

    @Test
    public void testGetCartNotFound() throws IOException { 
        when(mockShoppingCartDAO.getCart("Gustavo")).thenReturn(null);

        ResponseEntity<Jersey[]> response = shoppingCartController.getCart("Gustavo");

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetCartHandleException() throws IOException {
        doThrow(new IOException()).when(mockShoppingCartDAO).getCart("Gustavo");

        ResponseEntity<Jersey[]> response = shoppingCartController.getCart("Gustavo");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateShoppingCart() throws IOException {

        when(mockShoppingCartDAO.createShoppingCart("Gustavo")).thenReturn(true);

        ResponseEntity<Jersey[]> response = shoppingCartController.createShoppingCart("Gustavo");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testCreateJerseyFailed() throws IOException {
        when(mockShoppingCartDAO.createShoppingCart("Gustavo")).thenReturn(false);

        ResponseEntity<Jersey[]> response = shoppingCartController.createShoppingCart("Gustavo");

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateJerseyException() throws IOException {
        doThrow(new IOException()).when(mockShoppingCartDAO).createShoppingCart("Gustavo");

        ResponseEntity<Jersey[]> response = shoppingCartController.createShoppingCart("Gustavo");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}