package fxsrc.propyecto.drivers;

import fxsrc.propyecto.domain.ContentBasedFiltering;
import fxsrc.propyecto.domain.DataManager;
import fxsrc.propyecto.domain.Item;
import fxsrc.propyecto.domain.ItemDoesNotExistException;
import javafx.util.Pair;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContentBasedFilteringTest {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("fxsrc.propyecto.drivers.ContentBasedFilteringTest");
    }
    @Test
    public void computeKNearest() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./src/main/java/fxsrc/propyecto/drivers/text/ContentBasedFilteringTest.txt"));

        int itemID1 = sc.nextInt();
        int itemID2 = sc.nextInt();
        float simil = sc.nextFloat();
        Item item1 = DataManager.GetInstance().GetItemByID(itemID1);
        Item item2 = DataManager.GetInstance().GetItemByID(itemID2);

        ContentBasedFiltering c = mock(ContentBasedFiltering.class);
        when(c.CompareItems(item1, item2)).thenReturn(simil);
        ArrayList<Pair<Integer,Float>> result = c.ComputeKNearest(item1.GetItemId());
        ArrayList<Pair<Integer,Float>> expected = new ArrayList<>();
        assertEquals(result,expected);
    }
}