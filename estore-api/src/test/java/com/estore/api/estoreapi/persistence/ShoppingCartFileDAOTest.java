package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.estore.api.estoreapi.model.ShoppingCart;
import com.estore.api.estoreapi.model.Size;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test suite for the Shopping Cart File Data Access Object.
 * 
 * @author Team Red Wings
 */
@Tag("Persistence-tier")
public class ShoppingCartFileDAOTest {
    ShoppingCartFileDAO shoppingCartFileDAO;
    ShoppingCart[] testShoppingCarts;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupShoppingCartFileDAO() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);
        testShoppingCarts = new ShoppingCart[1];
        testShoppingCarts[0] = new ShoppingCart("Gustavo");
        testShoppingCarts[0].addJersey(new Jersey(1, "Rasmussen", 27, 24.99, Size.SMALL, Color.BLACK, 1, "jerseyImages/Rasmussen.jpg"));
        testShoppingCarts[0].addJersey(new Jersey(2, "Larkin", 71, 39.99, Size.MEDIUM, Color.RED, 1, "jerseyImages/Larkin.jpg"));
        testShoppingCarts[0].addJersey(new Jersey(3, "Copp", 18, 49.99, Size.X_LARGE, Color.WHITE, 12, "jerseyImages/Copp.jpg"));

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the shopping cart above

        when(mockObjectMapper
            .readValue(new File("doesntmatter.txt"), ShoppingCart[].class))
                .thenReturn(testShoppingCarts);
        shoppingCartFileDAO = new ShoppingCartFileDAO("doesntmatter.txt", mockObjectMapper);
    }
    
    @Test
    public void testAddJersey() {
        Jersey[] jerseys = new Jersey[4];
        jerseys[0] = new Jersey(1, "Rasmussen", 27, 24.99, Size.SMALL, Color.BLACK, 1, "jerseyImages/Rasmussen.jpg");
        jerseys[1] = new Jersey(2, "Larkin", 71, 39.99, Size.MEDIUM, Color.RED, 1, "jerseyImages/Larkin.jpg");
        jerseys[2] = new Jersey(3, "Copp", 18, 49.99, Size.X_LARGE, Color.WHITE, 2, "jerseyImages/Copp.jpg");
        Jersey jersey = new Jersey(1, "Smith", 48, 32.99, Size.LARGE, Color.RED, 1, "jerseyImages/Smith.jpg");
        jerseys[3] = jersey;
        String username = "Gustavo";

        Jersey result = assertDoesNotThrow(() -> shoppingCartFileDAO.addJersey(username, jersey), "Unexpected exception thrown");

        assertNotNull(result);
        Jersey[] actual = assertDoesNotThrow(() -> shoppingCartFileDAO.getCart(username), "Unexpected exception thrown");
        assertArrayEquals(actual, jerseys);
    }

    @Test
    public void testAddJerseyToQuantity() {
        Jersey jersey = new Jersey(3, "Copp", 18, 49.99, Size.X_LARGE, Color.WHITE, 2, "jerseyImages/Copp.jpg");
        String username = "Gustavo";

        Jersey result = assertDoesNotThrow(() -> shoppingCartFileDAO.addJersey(username, jersey), "Unexpected exception thrown");

        assertNotNull(result);
        assertEquals(result, jersey);
        assertEquals(13, result.getQuantity());
    }

    @Test
    public void testAddJerseyNotFound() {
        Jersey jersey = new Jersey(5, "Hagg", 38, 52.99, Size.LARGE, Color.RED, 3, "jerseyImages/Hagg.jpg");
        String username = "Gustavus";

        Jersey result = assertDoesNotThrow(() -> shoppingCartFileDAO.addJersey(username, jersey), "Unexpected exception thrown");
        assertNull(result);
    }

    @Test
    public void testDeleteJerseyOneRemaining() {
        // Invoke
        String username = "Gustavo";
        Jersey testJersey = new Jersey(1, "Rasmussen", 27, 24.99, Size.SMALL, Color.BLACK, 8, "jerseyImages/Rasmussen.jpg");

        boolean result = assertDoesNotThrow(() -> shoppingCartFileDAO.removeJersey(username, testJersey),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);

        assertEquals(shoppingCartFileDAO.shoppingCarts.get("Gustavo").getCart().size(), 2);
    }

    @Test
    public void testDeleteJerseyMultipleRemaining() {
        // Invoke
        String username = "Gustavo";
        Jersey testJersey = new Jersey(3, "Copp", 18, 49.99, Size.X_LARGE, Color.WHITE, 2, "jerseyImages/Copp.jpg");

        boolean result = assertDoesNotThrow(() -> shoppingCartFileDAO.removeJersey(username, testJersey),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);

        assertEquals(shoppingCartFileDAO.shoppingCarts.get("Gustavo").getCart().size(), 3);
        int index = shoppingCartFileDAO.shoppingCarts.get("Gustavo").getCart().indexOf(testJersey);
        assertEquals(shoppingCartFileDAO.shoppingCarts.get("Gustavo").getCart().get(index).getQuantity(), 11);
    }

    @Test
    public void testDeleteJerseyNotFound() {
        // Invoke
        Jersey jersey = new Jersey(5, "Hagg", 38, 52.99, Size.LARGE, Color.RED, 9, "jerseyImages/Hagg.jpg");
        boolean result = assertDoesNotThrow(() -> shoppingCartFileDAO.removeJersey("Gustavo", jersey),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(shoppingCartFileDAO.shoppingCarts.get("Gustavo").getCart().size(), 3);
    }

    @Test
    public void testGetCart() throws IOException {
        // Invoke
        Jersey[] jerseys = shoppingCartFileDAO.getCart("Gustavo");

        // Analzye
        assertArrayEquals(jerseys, testShoppingCarts[0].getCart().toArray());
    }

    @Test
    public void testGetCartNotFound() throws IOException {
        // Invoke
        Jersey[] jerseys = shoppingCartFileDAO.getCart("Gustavus");

        // Analyze
        assertEquals(jerseys, null);
    }

    @Test
    public void testCreateShoppingCart() throws IOException {

        boolean result = assertDoesNotThrow(() -> shoppingCartFileDAO.createShoppingCart("Gustavus"),
                                "Unexpected exception thrown");

        assertNotNull(result);
        Jersey[] actual = shoppingCartFileDAO.getCart("Gustavus");
        assertArrayEquals(actual, new Jersey[0]);
        assertEquals(result, true);
    }

    @Test
    public void testCreateShoppingCartExists() throws IOException {

        boolean result = assertDoesNotThrow(() -> shoppingCartFileDAO.createShoppingCart("Gustavo"),
                                "Unexpected exception thrown");

        assertNotNull(result);
        assertEquals(result, false);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the ShoppingCartFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),ShoppingCart[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new ShoppingCartFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}

