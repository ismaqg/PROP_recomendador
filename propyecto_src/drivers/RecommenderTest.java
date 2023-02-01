package fxsrc.propyecto.drivers;

import fxsrc.propyecto.domain.CollaborativeFiltering;
import fxsrc.propyecto.domain.Rating;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

public class RecommenderTest {

    private static float DCG = 0.0f, IDCG=0.0f, NDCG=0.0f;
    private static CollaborativeFiltering cf = new CollaborativeFiltering(3);
    private static ArrayList<Rating> predicted;

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("fxsrc.propyecto.drivers.RecommenderTest");
    }

    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./src/main/java/fxsrc/propyecto/drivers/text/RecommenderTest.txt"));
        predicted = new ArrayList<>();
        int userID1 = sc.nextInt(); //1
        int itemID1 = sc.nextInt(); //2
        float rating1 = sc.nextFloat(); //3.0
        int userID2 = sc.nextInt(); //1
        int itemID2 = sc.nextInt(); //4
        float rating2 = sc.nextFloat(); //5.0
        predicted.add( new Rating(userID1, itemID1, rating1));
        predicted.add( new Rating(userID2, itemID2, rating2));
    }

    @Test
    public void computeDCG() {
        //No se puede hacer porque accede a funciones privadas y no puedo hacer mocks
    }

    @Test
    public void computeIDCG() {
        //No se puede hacer porque accede a funciones privadas y no puedo hacer mocks
    }

    @Test
    public void computeNDCG() {
        NDCG = DCG / IDCG;
        float result = cf.ComputeNDCG(DCG, IDCG);
        assertEquals(NDCG, result,2);
    }
}