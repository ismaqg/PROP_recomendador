package fxsrc.propyecto.drivers;

import fxsrc.propyecto.domain.Item;
import fxsrc.propyecto.domain.ParserCSV;
import fxsrc.propyecto.domain.Rating;
import fxsrc.propyecto.domain.UserActual;
import fxsrc.propyecto.enums.ItemTypes;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.Assert.*;

public class ParserCSVTest {

    private static String[] atributsItem;
    private static String rawInputItem = "";
    private static String rawInputItem2 = "";
    private static String[] atributsUser;
    private static String rawInputUser = "";
    private static String[] atributsExtremsUsr;
    private static String rawUserExtreme = "";
    private static String[] atributRating;
    private static String ratingInput = "";
    private static String[] atributRatingExtreme;
    private static String ratingInputExtreme = "";
    private static String rawException = "";

    @BeforeClass
    public static void setUp() throws FileNotFoundException {

        Scanner sc = new Scanner(new File("./src/main/java/fxsrc/propyecto/drivers/text/ParserCSVtest.txt"));

        atributsItem = sc.nextLine().split(",");;
        rawInputItem  = sc.nextLine();
        rawInputItem2  = sc.nextLine();
        atributsUser = sc.nextLine().split(",");;
        rawInputUser = sc.nextLine();
        atributsExtremsUsr = sc.nextLine().split(",");;
        rawUserExtreme  = sc.nextLine();
        atributRating = sc.nextLine().split(",");;
        ratingInput = sc.nextLine();
        atributRatingExtreme = sc.nextLine().split(",");;
        ratingInputExtreme = sc.nextLine();
        rawException = sc.nextLine();
    }

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("fxsrc.propyecto.drivers.ParserCSVTest");
    }

    @Test
    public void testGetItemType() {
        String[] num = {"1", "2", "3", "4", "5"};
        String[] str = {"jinx", "powder", "vi", "violet"};
        String[] fl = {"3.5", "4.5", "1.2", "3.2", "3.6"};
        assertEquals(ItemTypes.INT, ParserCSV.GetInstance().GetItemType(num));
        assertEquals(ItemTypes.STRING, ParserCSV.GetInstance().GetItemType(str));
        assertEquals(ItemTypes.FLOAT, ParserCSV.GetInstance().GetItemType(fl));
    }

    @Test
    //Solo comprueba el primer valor, por lo tanto los demás tipos estarán mal
    public void testGetItemTypeExtreme() {
        String[] num = {"1.45f", "powder", "jinx"};
        assertEquals(ItemTypes.FLOAT, ParserCSV.GetInstance().GetItemType(num));
    }

    @Test
    //Item del CSV
    public void testParseItem(){
        Item real = ParserCSV.GetInstance().ParseItem(atributsItem, rawInputItem);

        assertEquals("Haikyuu!! Second Season", real.GetName());
        assertEquals("https://cdn.myanimelist.net/images/anime/9/76662.jpg" ,real.GetImagePath());
    }

    @Test
    //Si no hay imagen y el titulo tiene http y png o jpg o jpeg, detectara el titulo como imagePath
    public void testParseItemExtreme(){
        Item real = ParserCSV.GetInstance().ParseItem(atributsItem, rawInputItem2);
        assertEquals("Made in http Abyss png", real.GetName());
        assertNotEquals("https://cdn.myanimelist.net/images/anime/6/86733.jpg", real.GetImagePath());
    }

    @Test
    public void testParseUser(){
        //User del CSV
        UserActual user = ParserCSV.GetInstance().ParseUser(atributsUser, rawInputUser);

        assertEquals("iqg", user.GetUsername());
        assertEquals(1, user.GetUserID());
        assertEquals("ismaqg@gmail.com", user.GetEmail());
        assertEquals("1234I", user.GetPassword());
        assertEquals("FIB", user.GetSecurity());
        assertFalse(user.GetStarted());
    }

    @Test
    //Orden cambiado, diferentes nombres de atributos, mayusculas y minusculas mezcladas y password y favorite mal escrito
    public void testParseUserExtreme(){
        UserActual user = ParserCSV.GetInstance().ParseUser(atributsExtremsUsr, rawUserExtreme);

        assertEquals("iqg", user.GetUsername());
        assertEquals(1, user.GetUserID());
        assertEquals("ismaqg@gmail.com", user.GetEmail());
        assertEquals("1234I", user.GetPassword());
        assertEquals("FIB", user.GetSecurity());
        assertFalse(user.GetStarted());
    }

    @Test
    //Rating que hay en el CSV
    public void testParseRating() {

        Rating r = ParserCSV.GetInstance().ParseRating(atributRating, ratingInput);

        assertEquals(894353, r.GetUserID());
        assertEquals(19614643, r.GetItemID());
        assertEquals(4.232424525f, r.GetRating(), 2);

    }

    @Test
    //Parse rating desordenado y mal escrito
    public void testParseRatingExtreme() {

        Rating r = ParserCSV.GetInstance().ParseRating(atributRatingExtreme, ratingInputExtreme);

        assertEquals(894353, r.GetUserID());
        assertEquals(19614643, r.GetItemID());
        assertEquals(4.232424525f, r.GetRating(), 2);

    }

    @Test(expected = NumberFormatException.class)
    //ni itemID ni userID pueden recibir floats, ya que no deberian poder por diseño
    public void testParseRatingException() {
        ParserCSV.GetInstance().ParseRating(atributRatingExtreme, rawException);
    }
}