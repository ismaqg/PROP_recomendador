package fxsrc.propyecto.drivers;

import fxsrc.propyecto.data.DataRating;
import fxsrc.propyecto.domain.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataRatingTest {

    private static ArrayList<Rating> ratings;
    private static ArrayList<Rating> ratings2;
    private static String rawInput;
    private static String[] s;
    private static DataRating d;

    @BeforeClass
    public static void setUp() throws FileNotFoundException {

        ratings = new ArrayList<>();
        Scanner sc = new Scanner(new File("./src/main/java/fxsrc/propyecto/drivers/text/DataRatingTest.txt"));
        String input = sc.nextLine();
        s = input.split(",");
        ratings.add(new Rating(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Float.parseFloat(s[2])));
        rawInput = input;

    }

    @Before
    public void Before() {
        /*
        String[] atributs = {"userID", "itemID", "rating"};
        d = new DataRating();
        ParserCSV p = mock(ParserCSV.class);
        when(p.ParseRating(rawInput)).thenReturn(ratings.get(0));
        d.ParseAndAddData(atributs, rawInput);

         */
    }

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("fxsrc.propyecto.drivers.DataRatingTest");
    }

    @Test
    public void testClearData() {
        d.ClearData();
        assertEquals(new ArrayList<>(), d.GetAllRatings());
    }

    @Test
    public void testParseAndAddData() {
        ArrayList<Rating> rating2 = d.GetAllRatings();
        assertTrue(ratings.get(0).Equals(rating2.get(0)));
    }

}