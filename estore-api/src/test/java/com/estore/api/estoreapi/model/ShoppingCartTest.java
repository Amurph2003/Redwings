package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Shopping Cart class.
 * 
 * @author Team Red Wings
 */
@Tag("Model-tier")
public class ShoppingCartTest {
    @Test
    public void testCtor() {
        String expectedName = "Juan";
        ArrayList<Jersey> expectedCart = new ArrayList<>();

        ShoppingCart testCart = new ShoppingCart(expectedName);

        assertEquals(expectedName, testCart.getName());
        assertEquals(expectedCart, testCart.getCart());
    }

    @Test
    public void testToString() {
        Jersey testJersey = new Jersey(99, "Zadina", 11, 49.99, Size.LARGE, Color.RED, 7, "jerseyImages/Zadina.jpg");

        String expectedName = "Juan";
        ArrayList<Jersey> expectedContents = new ArrayList<>();
        expectedContents.add(testJersey);

        ShoppingCart testCart = new ShoppingCart(expectedName);
        testCart.addJersey(testJersey);

        String expectedString = String.format(ShoppingCart.STRING_FORMAT, expectedName, expectedContents);

        assertEquals(expectedString, testCart.toString());
    }

    @Test
    public void testAddJersey() {
        Jersey testJersey = new Jersey(99, "Zadina", 11, 49.99, Size.LARGE, Color.RED, 7, "jerseyImages/Zadina.jpg");

        String name = "Juan";
        ArrayList<Jersey> expectedContents = new ArrayList<>();
        expectedContents.add(testJersey);

        ShoppingCart testCart = new ShoppingCart(name);
        boolean success = testCart.addJersey(testJersey);

        assertEquals(expectedContents, testCart.getCart());
        assertTrue(success);
    }

    @Test
    public void testAddJerseyFailed() {
        Jersey testJersey = new Jersey(99, "Zadina", 11, 49.99, Size.LARGE, Color.RED, 7, "jerseyImages/Zadina.jpg");

        String name = "Juan";
        ArrayList<Jersey> expectedContents = new ArrayList<>();
        expectedContents.add(testJersey);

        ShoppingCart testCart = new ShoppingCart(name);
        testCart.addJersey(testJersey);
        boolean success = testCart.addJersey(testJersey);

        assertEquals(expectedContents, testCart.getCart());
        assertFalse(success);
    }

    @Test
    public void testRemoveJersey() {
        Jersey testJersey = new Jersey(99, "Zadina", 11, 49.99, Size.LARGE, Color.RED, 7, "jerseyImages/Zadina.jpg");

        String name = "Juan";
        ArrayList<Jersey> expectedContents = new ArrayList<>();

        ShoppingCart testCart = new ShoppingCart(name);
        testCart.addJersey(testJersey);
        Jersey resultJersey = testCart.removeJersey(testJersey);

        assertEquals(expectedContents, testCart.getCart());
        assertEquals(resultJersey, testJersey);
    }

    @Test
    public void testRemoveNotContained() {
        Jersey testJersey = new Jersey(99, "Zadina", 11, 49.99, Size.LARGE, Color.RED, 7, "jerseyImages/Zadina.jpg");

        String name = "Juan";
        ArrayList<Jersey> expectedContents = new ArrayList<>();

        ShoppingCart testCart = new ShoppingCart(name);
        Jersey resultJersey = testCart.removeJersey(testJersey);

        assertEquals(expectedContents, testCart.getCart());
        assertNull(resultJersey);
    }
}