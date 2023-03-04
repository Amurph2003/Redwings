package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.estore.api.estoreapi.model.Color;
import com.estore.api.estoreapi.model.Jersey;
import com.estore.api.estoreapi.model.Size;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test suite for the Inventory File Data Access Object.
 * 
 * @author Team Red Wings
 */
@Tag("Persistence-tier")
public class InventoryFileDAOTest {
    InventoryFileDAO inventoryFileDAO;
    Jersey[] testInventory;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupInventoryFileDAO() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);
        testInventory = new Jersey[3];
        testInventory[0] = new Jersey(1, "Rasmussen", 27, 24.99, Size.SMALL, Color.BLACK, 11, "jerseyImages/Rasmussen.jpg");
        testInventory[1] = new Jersey(2, "Larkin", 71, 39.99, Size.MEDIUM, Color.RED, 5, "jerseyImages/Larkin.jpg");
        testInventory[2] = new Jersey(3, "Copp", 18, 49.99, Size.X_LARGE, Color.WHITE, 7, "jerseyImages/Copp.jpg");

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the jersey array above

        when(mockObjectMapper
            .readValue(new File("doesntmatter.txt"), Jersey[].class))
                .thenReturn(testInventory);
        inventoryFileDAO = new InventoryFileDAO("doesntmatter.txt", mockObjectMapper);
    }
    
    @Test
    public void testUpdateJersey() {
        Jersey jersey = new Jersey(1, "Smith", 48, 32.99, Size.LARGE, Color.RED, 7, "jerseyImages/Smith.jpg");

        Jersey result = assertDoesNotThrow(() -> inventoryFileDAO.updateJersey(jersey), "Unexpected exception thrown");

        assertNotNull(result);
        Jersey actual;
        try{actual = inventoryFileDAO.getJersey(jersey.getId());}catch(IOException e){actual = null;};
        assertEquals(actual, jersey);
    }

    @Test
    public void testUpdateJerseyNotFound() {
        Jersey jersey = new Jersey(5, "Hagg", 38, 52.99, Size.LARGE, Color.RED, 7, "jerseyImages/Hagg.jpg");

        Jersey result = assertDoesNotThrow(() -> inventoryFileDAO.updateJersey(jersey), "Unexpected exception thrown");
        assertNull(result);
    }

    @Test
    public void testUpdateJerseyConflicts() {
        Jersey jersey = new Jersey(2, "Glitch", 27, 24.99, Size.SMALL, Color.BLACK, 11, "jerseyImages/Hagg.jpg");

        Jersey result = assertDoesNotThrow(() -> inventoryFileDAO.updateJersey(jersey), "Unexpected exception thrown");
        assertNotEquals(result, jersey);
        assertEquals(result, testInventory[0]);
    }

    @Test
    public void testDeleteJersey() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> inventoryFileDAO.deleteJersey(1),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test jerseys array - 1 (because of the delete)
        // Because jerseys attribute of inventoryFileDAO is package private
        // we can access it directly
        assertEquals(inventoryFileDAO.jerseys.size(),testInventory.length-1);
    }

    @Test
    public void testDeleteJerseyNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> inventoryFileDAO.deleteJersey(98),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(inventoryFileDAO.jerseys.size(),testInventory.length);
    }


    @Test
    public void testGetInventory() {
        // Invoke
        Jersey[] inventory = inventoryFileDAO.getInventory();

        // Analyze
        assertEquals(inventory.length,testInventory.length);
        for (int i = 0; i < testInventory.length;++i)
            assertEquals(inventory[i],testInventory[i]);
    }

    

    @Test
    public void testGetJersey() throws IOException {
        // Invoke
        Jersey jersey = inventoryFileDAO.getJersey(1);

        // Analzye
        assertEquals(jersey, testInventory[0]);
    }

    @Test
    public void testGetJerseyNotFound() throws IOException {
        // Invoke
        Jersey jersey = inventoryFileDAO.getJersey(98);

        // Analyze
        assertEquals(jersey, null);
    }

    @Test
    public void testCreateJersey() throws IOException {
        Jersey jersey = new Jersey(62,"Gilgamesh",44,49.99,Size.XX_LARGE,Color.RED,7,"jerseyImages/Gilgamesh.jpg");

        Jersey result = assertDoesNotThrow(() -> inventoryFileDAO.createJersey(jersey),
                                "Unexpected exception thrown");

        assertNotNull(result);
        Jersey actual = inventoryFileDAO.getJersey(result.getId());
        assertEquals(actual.getName(),jersey.getName());
        assertEquals(actual.getNumber(),jersey.getNumber());
        assertEquals(actual.getPrice(),jersey.getPrice());
        assertEquals(actual.getSize(),jersey.getSize());
        assertEquals(actual.getColor(),jersey.getColor());
        assertEquals(actual.getQuantity(),jersey.getQuantity());
    }

    @Test
    public void testCreateJerseyEquals() throws IOException {
        Jersey jersey = new Jersey(1, "Rasmussen", 27, 24.99, Size.SMALL, Color.BLACK, 11, "jerseyImages/Rasmussen.jpg");

        Jersey result = assertDoesNotThrow(() -> inventoryFileDAO.createJersey(jersey), "Unexpected exception thrown");
        assertNotNull(result);
        Jersey actual = inventoryFileDAO.getJersey(result.getId());
        assertEquals(jersey, actual);
        assertEquals(actual.getQuantity(), 22);
    }

    @Test
    public void testCreateJerseyConflicts() throws IOException {
        Jersey jersey = new Jersey(1, "Garrits", 27, 24.99, Size.SMALL, Color.BLACK, 11, "jerseyImages/Garrits.jpg");

        Jersey result = assertDoesNotThrow(() -> inventoryFileDAO.createJersey(jersey), "Unexpected exception thrown");
        assertNull(result);
    }

    @Test
    public void testFindJerseys() {
        // Invoke
        Jersey[] jerseys = inventoryFileDAO.findJerseys("n");

        // Analyze
        assertEquals(jerseys.length,2);
        assertEquals(jerseys[0],testInventory[0]);
        assertEquals(jerseys[1],testInventory[1]);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the InventoryFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Jersey[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new InventoryFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}
