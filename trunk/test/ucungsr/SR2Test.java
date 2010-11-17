/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ucungsr;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author mauricio
 */
public class SR2Test {

    public SR2Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() {
        String[] args = new String[2];
        args[1] = "-insert";

        //Entreno con Gustavo:
        args[0] = "waves/gustavo_1.wav";
//        SR2 sr2 = new SR2(args);

        //Entreno con Jesica:
        args[0] = "waves/jesica_1.wav";
//        sr2 = new SR2(args);

        //Identifico a Jesica:
        args = new String[1];
        args[0] = "waves/gustavo_1.wav";
        SR2 sr2 = new SR2(args);

    }

}