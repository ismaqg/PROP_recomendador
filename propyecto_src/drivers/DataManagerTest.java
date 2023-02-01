package fxsrc.propyecto.drivers;

import fxsrc.propyecto.domain.DataManager;
import fxsrc.propyecto.domain.UserActual;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.Assert.*;

public class DataManagerTest {


    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("fxsrc.propyecto.drivers.DataManagerTest");
    }


    private static String username, password, mail, security;

    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./src/main/java/fxsrc/propyecto/drivers/text/DataManagerTest.txt"));
        username = sc.nextLine();
        password = sc.nextLine();
        mail = sc.nextLine();
        security = sc.nextLine();
    }


    @Test
    public void testCheckCredentials() {
        /*
        assertTrue(DataManager.GetInstance().CheckCredentials(username, password));

         */
    }

    @Test
    public void testSignIn() {
        /*
        UserActual usr = DataManager.GetInstance().SignIn(username, true);
        assertEquals(username, usr.GetUsername());
        assertEquals("contraseña", usr.GetPassword());

         */

    }

    @Test
    public void testSignUp() {

        UserActual usr = DataManager.GetInstance().SignUp(username, password, mail, security);
        assertEquals(username, usr.GetUsername());
        assertEquals(password, usr.GetPassword());
        assertEquals(mail, usr.GetEmail());
        assertEquals(security, usr.GetSecurity());
    }
}