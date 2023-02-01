package fxsrc.propyecto.drivers;

import fxsrc.propyecto.data.DataItem;
import fxsrc.propyecto.domain.Item;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

public class DataTest {

    private static String rawInput;
    private static String path;
    private static DataItem d;

    @BeforeClass
    public static void SetUp() throws FileNotFoundException {
        path = "./src/main/java/fxsrc/propyecto/drivers/text/DataTest.txt";
        Scanner sc = new Scanner(new File(path));
        sc.nextLine();
        rawInput = sc.nextLine();
        rawInput += sc.nextLine();
        rawInput += sc.nextLine();
        d = new DataItem();
    }

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("fxsrc.propyecto.drivers.DataTest");
    }

    @Test
    public void testLoadCSV() {
        d.LoadCSV(path);
        ArrayList<Item> items;
        items = d.GetAllItems();
        //Comprovació que hi ha 1 item
        assertEquals(1, items.size());
        //Comprovació que hi ha 25 atributs
        assertEquals(25, items.get(0).GetNumberOfAttributes());
    }

    @Test
    //Caso extremo en el que lee una descripción con saltos de línea
    public void testReadNextLineCSV() throws FileNotFoundException {
        Scanner ss = new Scanner(new File(path));
        ss.nextLine();
        String actual = d.ReadNextLineCSV(ss);
        assertEquals(rawInput, actual);

    }
}