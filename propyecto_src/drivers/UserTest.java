package fxsrc.propyecto.drivers;

import fxsrc.propyecto.domain.Rating;
import fxsrc.propyecto.domain.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

public class UserTest {

    private static int userID;
    private static ArrayList<Rating> r;
    private User user;

    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        r = new ArrayList<>();
        Scanner sc = new Scanner(new File("./src/main/java/fxsrc/propyecto/drivers/text/UserTest.txt"));
        userID = Integer.parseInt(sc.nextLine());
        while(sc.hasNextLine()){
            Integer r_usr = Integer.parseInt(sc.nextLine());
            Integer r_item = Integer.parseInt(sc.nextLine());
            Float r_rating = Float.parseFloat(sc.nextLine());
            r.add(new Rating(r_usr, r_item, r_rating));
        }
    }

    @Before
    public void before() {
        user = new User(userID);
        user.AddToRatingsList(r.get(0));
    }

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("fxsrc.propyecto.drivers.UserTest");
    }

    @Test
    public void testAddRating() {
        assertEquals(r.get(0), user.GetRatingsList().get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveRating() {
        user.RemoveFromRatingsList(r.get(0));
        user.GetRatingsList().get(0);
    }

    @Test
    public void testModifyRating() {
        user.ModifyItemRating(1, 4.96f);
        assertEquals(4.96f, user.GetRatingsList().get(0).GetRating(), 2);
    }
}