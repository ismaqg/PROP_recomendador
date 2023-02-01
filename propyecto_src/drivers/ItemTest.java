package fxsrc.propyecto.drivers;

import fxsrc.propyecto.domain.Item;
import fxsrc.propyecto.domain.ItemAttribute;

import org.junit.BeforeClass;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;


public class ItemTest<T> {

    private static int itemID;
    private static ArrayList<ItemAttribute> attribute;

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("fxsrc.propyecto.drivers.ItemTest");
    }

    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        attribute = new ArrayList<ItemAttribute>();
        Scanner sc = new Scanner(new File("./src/main/java/fxsrc/propyecto/drivers/text/ItemTest.txt"));
        itemID = Integer.parseInt(sc.nextLine());
        String nom;
        while(sc.hasNextLine()) {
            ArrayList<String> valors = new ArrayList<>();
            nom = sc.nextLine();
            String[] values = sc.nextLine().split(";");
            for(int i=0; i<values.length; i++) {
                valors.add(values[i]);
            }
            attribute.add(new ItemAttribute(nom, valors));
        }
    }

    @Test
    public void testAddAttribute() {
        Item item = new Item();
        ItemAttribute X = new ItemAttribute("sinopsis");
        item.AddAttribute(X);

        assertEquals(item.GetAttribute("sinopsis"), X);
    }

    @Test
    public void testGetAttributeName() {

        String match = "Adulto";
        Item item = new Item(attribute, itemID);
        String test = item.GetAttribute(match).GetAttributeName();
        assertEquals(match, test);
    }

    @Test
    public void testGetAttributePos() {
        Item item = new Item(attribute, itemID);
        for(int i = 0; i < attribute.size(); i++) {
            assertEquals(attribute.get(i).GetAttributeName(), item.GetAttribute(i).GetAttributeName());
        }
    }

    @Test
    public void testGetNumberOfAttributes() {
        Item item = new Item(attribute, itemID);
        assertEquals(attribute.size(), item.GetNumberOfAttributes());
    }

    @Test
    public void testSetItemID() {
        Item item = new Item(attribute, itemID);
        item.SetItemID(itemID);
        assertEquals(itemID, item.GetItemId());
    }

    @Test
    public void testGetItemId() {
        Item item = new Item(attribute, itemID);
        assertEquals(itemID, item.GetItemId());
    }

    @Test
    public void testToString() {
        Item item = new Item(attribute, itemID);
        String s = "Accio;Aventura;Misteri,2019,es;eng,n";
        assertEquals(s, item.ToString());
    }
}