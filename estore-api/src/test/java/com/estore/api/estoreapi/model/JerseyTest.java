package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Jersey class.
 * 
 * @author Team Red Wings
 */
@Tag("Model-tier")
public class JerseyTest {
    @Test
    public void testCtor() {
        int expectedId = 99;
        String expectedName = "Zadina";
        int expectedNumber = 11;
        double expectedPrice = 49.99;
        Size expectedSize = Size.LARGE;
        Color expectedColor = Color.RED;
        int expectedQuantity = 7;
        String expectedImageURL = "jerseyImages/Zadina.jpg";

        Jersey jersey = new Jersey(expectedId, expectedName, expectedNumber, expectedPrice, expectedSize, expectedColor, expectedQuantity, expectedImageURL);

        assertEquals(expectedId, jersey.getId());
        assertEquals(expectedName, jersey.getName());
        assertEquals(expectedNumber, jersey.getNumber());
        assertEquals(expectedPrice, jersey.getPrice());
        assertEquals(expectedSize, jersey.getSize());
        assertEquals(expectedColor, jersey.getColor());
        assertEquals(expectedQuantity, jersey.getQuantity());
    }

    @Test
    public void testToString() {
        int expectedId = 99;
        String expectedName = "Zadina";
        int expectedNumber = 11;
        double expectedPrice = 49.99;
        Size expectedSize = Size.LARGE;
        Color expectedColor = Color.RED;
        int expectedQuantity = 7;
        String expectedImageURL = "jerseyImages/Zadina.jpg";
        String expectedString = String.format(Jersey.STRING_FORMAT, expectedId, expectedName, expectedNumber, expectedPrice, expectedSize, expectedColor, expectedQuantity, expectedImageURL);

        Jersey jersey = new Jersey(expectedId, expectedName, expectedNumber, expectedPrice, expectedSize, expectedColor, expectedQuantity, expectedImageURL);

        assertEquals(expectedString, jersey.toString());
    }

    @Test
    public void testEquals() {
        int expectedId = 99;
        String expectedName = "Zadina";
        int expectedNumber = 11;
        double expectedPrice = 49.99;
        Size expectedSize = Size.LARGE;
        Color expectedColor = Color.RED;
        int expectedQuantity = 7;
        String expectedImageURL = "jerseyImages/Zadina.jpg";

        Jersey jersey = new Jersey(expectedId, expectedName, expectedNumber, expectedPrice, expectedSize, expectedColor, expectedQuantity, expectedImageURL);
        Jersey jersey2 = new Jersey(expectedId+1, expectedName, expectedNumber, expectedPrice, expectedSize, expectedColor, expectedQuantity-3, expectedImageURL);

        assertEquals(jersey, jersey2);
    }
    
    @Test
    public void testEqualsFalse() {
        int expectedId = 99;
        String expectedName = "Zadina";
        int expectedNumber = 11;
        double expectedPrice = 49.99;
        Size expectedSize = Size.LARGE;
        Color expectedColor = Color.RED;
        int expectedQuantity = 7;
        String expectedImageURL = "jerseyImages/Zadina.jpg";

        Jersey jersey = new Jersey(expectedId, expectedName, expectedNumber, expectedPrice, expectedSize, expectedColor, expectedQuantity, expectedImageURL);
        Jersey jersey2 = new Jersey(expectedId+1, expectedName, expectedNumber+3, expectedPrice, expectedSize, expectedColor, expectedQuantity-3, expectedImageURL);
        Jersey jersey3 = new Jersey(expectedId+2, expectedName, expectedNumber, expectedPrice+5.00, expectedSize, expectedColor, expectedQuantity-3, expectedImageURL);
        Jersey jersey4 = new Jersey(expectedId+3, expectedName, expectedNumber, expectedPrice, Size.XX_LARGE, expectedColor, expectedQuantity-3, expectedImageURL);
        Jersey jersey5 = new Jersey(expectedId+4, expectedName, expectedNumber, expectedPrice, expectedSize, Color.BLACK, expectedQuantity-3, expectedImageURL);

        assertNotEquals(jersey, jersey2);
        assertNotEquals(jersey, jersey3);
        assertNotEquals(jersey, jersey4);
        assertNotEquals(jersey, jersey5);
    }

    @Test
    public void testEqualsNotJersey() {
        int expectedId = 99;
        String expectedName = "Zadina";
        int expectedNumber = 11;
        double expectedPrice = 49.99;
        Size expectedSize = Size.LARGE;
        Color expectedColor = Color.RED;
        int expectedQuantity = 7;
        String expectedImageURL = "jerseyImages/Zadina.jpg";

        Jersey jersey = new Jersey(expectedId, expectedName, expectedNumber, expectedPrice, expectedSize, expectedColor, expectedQuantity, expectedImageURL);
        ShoppingCart cart = new ShoppingCart(expectedName);

        assertNotEquals(jersey, cart);
    }

    @Test
    public void testConflictsTrue() {
        int expectedId = 99;
        String expectedName = "Zadina";
        int expectedNumber = 11;
        double expectedPrice = 49.99;
        Size expectedSize = Size.LARGE;
        Color expectedColor = Color.RED;
        int expectedQuantity = 7;
        String expectedImageURL = "jerseyImages/Zadina.jpg";

        Jersey jersey = new Jersey(expectedId, expectedName, expectedNumber, expectedPrice, expectedSize, expectedColor, expectedQuantity, expectedImageURL);
        Jersey jersey2 = new Jersey(expectedId, "Oprah", expectedNumber, 34.99, expectedSize, expectedColor, 4, expectedImageURL);
        assertEquals(jersey.conflicts(jersey2), true);
    }

    @Test
    public void testConflictsFalse() {
        int expectedId = 99;
        String expectedName = "Zadina";
        int expectedNumber = 11;
        double expectedPrice = 49.99;
        Size expectedSize = Size.LARGE;
        Color expectedColor = Color.RED;
        int expectedQuantity = 7;
        String expectedImageURL = "jerseyImages/Zadina.jpg";

        Jersey jersey = new Jersey(expectedId, expectedName, expectedNumber, expectedPrice, expectedSize, expectedColor, expectedQuantity, expectedImageURL);
        Jersey jersey2 = new Jersey(expectedId, expectedName, expectedNumber, 34.99, expectedSize, expectedColor, 4, expectedImageURL);
        Jersey jersey3 = new Jersey(expectedId, "Oprah", 33, 34.99, expectedSize, expectedColor, 4, expectedImageURL);
        assertEquals(jersey.conflicts(jersey2), false);
        assertEquals(jersey2.conflicts(jersey3), false);
    }

    @Test
    public void testConflictsNotJersey() {
        int expectedId = 99;
        String expectedName = "Zadina";
        int expectedNumber = 11;
        double expectedPrice = 49.99;
        Size expectedSize = Size.LARGE;
        Color expectedColor = Color.RED;
        int expectedQuantity = 7;
        String expectedImageURL = "jerseyImages/Zadina.jpg";

        Jersey jersey = new Jersey(expectedId, expectedName, expectedNumber, expectedPrice, expectedSize, expectedColor, expectedQuantity, expectedImageURL);
        ShoppingCart cart = new ShoppingCart(expectedName);

        assertFalse(jersey.conflicts(cart));
    }
}