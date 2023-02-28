package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.estore.api.estoreapi.persistence.InventoryDAO;

/**
 * Unit test suite for the Inventory Controller.
 * 
 * @author Team Red Wings
 */
@Tag("Controller-tier")
public class InventoryControllerTest {
    private InventoryController inventoryController;
    private InventoryDAO mockInventoryDAO;

    /**
     * Before each test, create a new InvenroyController object and inject
     * a mock Inventory DAO
     */
    @BeforeEach
    public void setupJerseyController() {
        mockInventoryDAO = mock(InventoryDAO.class);
        inventoryController = new InventoryController(mockInventoryDAO);
    }

    @Test
    public void testUpdateJersey() throws IOException { 
        Jersey jersey = new Jersey(4, "Husso", 35, 36.79, Size.X_LARGE, Color.WHITE, 9, "jerseyImages/Munson.png");
        when(mockInventoryDAO.updateJersey(jersey)).thenReturn(jersey);
        ResponseEntity<Jersey> response = inventoryController.updateJersey(jersey);
        jersey.setColor(Color.RED);
        jersey.setSize(Size.SMALL);

        response = inventoryController.updateJersey(jersey);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(jersey,response.getBody());
    }

    @Test
    public void testUpdateJerseyFailed() throws IOException { 
        Jersey jersey = new Jersey(4, "Husso", 35, 36.79, Size.X_LARGE, Color.WHITE, 5, "jerseyImages/Munson.jpg");
        when(mockInventoryDAO.updateJersey(jersey)).thenReturn(null);

        ResponseEntity<Jersey> response = inventoryController.updateJersey(jersey);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteJersey() throws IOException { // deleteJersey may throw IOException
        // Setup
        int id = 99;
        // when deleteJersey is called return true, simulating successful deletion
        when(mockInventoryDAO.deleteJersey(id)).thenReturn(true);

        // Invoke
        ResponseEntity<Jersey> response = inventoryController.deleteJersey(id);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteJerseyNotFound() throws IOException { // deleteJersey may throw IOException
        // Setup
        int id = 99;
        // when deleteJersey is called return false, simulating failed deletion
        when(mockInventoryDAO.deleteJersey(id)).thenReturn(false);

        // Invoke
        ResponseEntity<Jersey> response = inventoryController.deleteJersey(id);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetInventory() throws IOException { // getInventory may throw IOException
        // Setup
        Jersey[] inventory = new Jersey[2];
        inventory[0] = new Jersey(1, "Jackson", 32, 39.99, Size.MEDIUM, null, 4,"jerseyImages/Munson.jpg");
        inventory[1] = new Jersey(2, "Miller", 45, 39.99, Size.SMALL, null, 18, "jerseyImages/Munson.jpg");

        // When getInventory is called return the Inventory created above
        when(mockInventoryDAO.getInventory()).thenReturn(inventory);

        // Invoke
        ResponseEntity<Jersey[]> response = inventoryController.getInventory();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(inventory,response.getBody());
    }

    @Test
    public void testGetInventoryNotFound() throws Exception { // createJersey may throw IOException
        // Setup
        // When the same id is passed in, our mock Inventory DAO will return null, simulating
        // no inventory found
        when(mockInventoryDAO.getInventory()).thenReturn(null);

        // Invoke
        ResponseEntity<Jersey[]> response = inventoryController.getInventory();

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetInventoryHandleException() throws IOException { // getInventory may throw IOException
        // Setup
        // When getInventory is called on the Mock Inventory DAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).getInventory();

        // Invoke
        ResponseEntity<Jersey[]> response = inventoryController.getInventory();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetJersey() throws IOException { 
        Jersey jersey = new Jersey(4, "Husso", 35, 36.79, Size.X_LARGE, Color.WHITE, 7, "jerseyImages/Munson.jpg");
        when(mockInventoryDAO.getJersey(jersey.getId())).thenReturn(jersey);

        ResponseEntity<Jersey> response = inventoryController.getJersey(jersey.getId());

        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testCreateJersey() throws IOException {
        Jersey jersey = new Jersey(99,"Kampfer",44,49.99,Size.XX_LARGE,Color.WHITE,16, "jerseyImages/Munson.jpg");

        when(mockInventoryDAO.createJersey(jersey)).thenReturn(jersey);

        ResponseEntity<Jersey> response = inventoryController.createJersey(jersey);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertEquals(jersey, response.getBody());
    }

    @Test
    public void testGetJerseyNotFound() throws Exception { 
        int id = 4;
        when(mockInventoryDAO.getJersey(id)).thenReturn(null);

        ResponseEntity<Jersey> response = inventoryController.getJersey(id);

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateJerseyHandleException() throws IOException { 
        Jersey jersey = new Jersey(4, "Husso", 35, 36.79, Size.X_LARGE, Color.WHITE,7, "jerseyImages/Munson.jpg");
        doThrow(new IOException()).when(mockInventoryDAO).updateJersey(jersey);

        ResponseEntity<Jersey> response = inventoryController.updateJersey(jersey);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteJerseyHandleException() throws IOException { // deleteJersey may throw IOException
        // Setup
        int id = 99;
        // When deleteJersey is called on the Mock Inventory DAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).deleteJersey(id);

        // Invoke
        ResponseEntity<Jersey> response = inventoryController.deleteJersey(id);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetJerseyHandleException() throws Exception {
        int id = 4;
        doThrow(new IOException()).when(mockInventoryDAO).getJersey(id);

        ResponseEntity<Jersey> response = inventoryController.getJersey(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testCreateJerseyAddedToQuantity() throws IOException {
        Jersey jersey = new Jersey(99,"Collins",59,49.99,Size.XX_LARGE,Color.WHITE,8, "jerseyImages/Munson.jpg");
        Jersey incrementedReturn = new Jersey(57,"Collins",59,49.99,Size.XX_LARGE,Color.WHITE,18,"jerseyImages/Munson.jpg");

        when(mockInventoryDAO.createJersey(jersey)).thenReturn(incrementedReturn);

        ResponseEntity<Jersey> response = inventoryController.createJersey(jersey);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(incrementedReturn, response.getBody());
        assertEquals(incrementedReturn.getQuantity(), response.getBody().getQuantity());
    }

    @Test
    public void testCreateJerseyFailed() throws IOException {
        Jersey jersey = new Jersey(99,"Collins",59,49.99,Size.XX_LARGE,Color.WHITE,8,"jerseyImages/Munson.jpg");

        when(mockInventoryDAO.createJersey(jersey)).thenReturn(null);

        ResponseEntity<Jersey> response = inventoryController.createJersey(jersey);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateJerseyException() throws IOException {
        Jersey jersey = new Jersey(99,"Krupp",44,49.32,Size.SMALL,Color.WHITE,10,"jerseyImages/Munson.jpg");

        doThrow(new IOException()).when(mockInventoryDAO).createJersey(jersey);

        ResponseEntity<Jersey> response = inventoryController.createJersey(jersey);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchJerseys() throws IOException { // may throw IOException
        // Setup
        String searchString = "n";
        Jersey[] jerseys = new Jersey[2];
        jerseys[0] = new Jersey(1, "Rasmussen", 27, 24.99, Size.SMALL, Color.BLACK,7,"jerseyImages/Munson.jpg");
        jerseys[1] = new Jersey(2, "Larkin", 71, 39.99, Size.MEDIUM, Color.RED,2,"jerseyImages/Munson.jpg");
        // When findJerseys is called with the search string, return the two
        /// jerseys above
        when(mockInventoryDAO.findJerseys(searchString)).thenReturn(jerseys);

        // Invoke
        ResponseEntity<Jersey[]> response = inventoryController.searchJerseys(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(jerseys, response.getBody());
    }

    @Test
    public void testSearchJerseysNone() throws IOException { // may throw IOException
        // Setup
        String searchString = "z";
        Jersey[] jerseys = new Jersey[2];
        when(mockInventoryDAO.findJerseys(searchString)).thenReturn(jerseys);

        // Invoke
        ResponseEntity<Jersey[]> response = inventoryController.searchJerseys(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(jerseys, response.getBody());
    }

    @Test
    public void testSearchJerseysHandleException() throws IOException { // may throw IOException
        // Setup
        String searchString = "n";
        // When createJersey is called on the Mock Inventory DAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).findJerseys(searchString);

        // Invoke
        ResponseEntity<Jersey[]> response = inventoryController.searchJerseys(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}

