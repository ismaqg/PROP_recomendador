package fxsrc.propyecto.drivers;

import fxsrc.propyecto.domain.Rating;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.Assert.*;

public class RatingTest {

    private static String[] s;
    private static int userID;
    private static int itemID;
    private static float rating;
    private static Rating r;

    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./src/main/java/fxsrc/propyecto/drivers/text/DataRatingTest.txt"));
        String input = sc.nextLine();
        s = input.split(",");
        userID = Integer.parseInt(s[0]);
        itemID = Integer.parseInt(s[1]);
        rating = Float.parseFloat(s[2]);

        r = new Rating(userID, itemID, rating);
    }

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("fxsrc.propyecto.drivers.RatingTest");
    }

    @Test
    public void testGetUserID() {
        assertEquals(userID, r.GetUserID());
    }

    @Test
    public void testSetUserID() {
        Rating r = new Rating(userID, itemID, rating);
        r.SetUserID(44);
        assertEquals(44, r.GetUserID());
    }

    @Test
    public void testGetItemID() {
        Rating r = new Rating(userID, itemID, rating);
        assertEquals(itemID, r.GetItemID());
    }

    @Test
    public void testSetItemID() {
        Rating r = new Rating(userID, itemID, rating);
        r.SetItemID(6);
        assertEquals(6, r.GetItemID());
    }

    @Test
    public void testGetRating() {
        Rating r = new Rating(userID, itemID, rating);
        assertEquals(rating, r.GetRating(), 2);
    }

    @Test
    public void testSetRating() {
        Rating r = new Rating(userID, itemID, rating);
        r.SetRating(4.6f);
        assertEquals(4.6f, r.GetRating(), 2);
    }

    @Test
    public void testEquals(){
        Rating r2 = new Rating(userID, itemID, rating);
        assertTrue(r.Equals(r2));
    }

}