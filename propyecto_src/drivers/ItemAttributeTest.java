package fxsrc.propyecto.drivers;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import fxsrc.propyecto.domain.ItemAttribute;

import static org.junit.Assert.*;


public class ItemAttributeTest {

    private static String attribute;
    private static ArrayList<String> lst;

    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        lst = new ArrayList<>();
        Scanner sc = new Scanner(new File("./src/main/java/fxsrc/propyecto/drivers/text/ItemAttributeTest.txt"));
        attribute = sc.nextLine();
        while(sc.hasNextLine()) lst.add(sc.nextLine());
    }

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("fxsrc.propyecto.drivers.ItemAttributeTest");
    }


    @Test
    public void testGetAttributeName() {
        ItemAttribute T = new ItemAttribute(attribute);
        assertEquals(attribute, T.GetAttributeName());
    }

    @Test
    public void testSetAttributeName(){
        ItemAttribute T = new ItemAttribute(attribute);
        attribute = "Qualificacions";
        T.SetAttributeName(attribute);
        assertEquals(attribute, T.GetAttributeName());
    }


    @Test
    public void testGetAllAttributeValues() {
        ItemAttribute T = new ItemAttribute(attribute, lst);
        assertEquals(T.GetAllAttributeValues(), lst);
    }

    @Test
    public void testGetSize() {
        ItemAttribute T = new ItemAttribute(attribute, lst);
        assertEquals(lst.size(), T.GetSize());
    }

    @Test
    public void testContains() {
        ItemAttribute T = new ItemAttribute(attribute, lst);
        for(int i=0; i<lst.size(); i++){
            assertTrue(T.Contains(lst.get(i)));
        }
    }

    @Test
    public void testGetClass() {
        ItemAttribute T = new ItemAttribute(attribute, lst);
        assertEquals(lst.getClass(), T.GetClass());
    }

    @Test
    public void testSetAttributeValue() {
        ItemAttribute T = new ItemAttribute(attribute);
        T.SetAttributeValue(lst);
        assertEquals(lst, T.GetAllAttributeValues());
    }

    @Test
    public void testAddAttributeValue() {

        ItemAttribute T = new ItemAttribute(attribute);
        for(int i=0; i < lst.size(); i++){
            T.AddAttributeValue(lst.get(i));
        }
        assertEquals(lst, T.GetAllAttributeValues());
    }

    @Test
    public void testToString() {

        ItemAttribute T = new ItemAttribute(attribute, lst);
        String test = "Accion;Aventura;Misterio";
        assertEquals(test, T.ToString());

    }
}