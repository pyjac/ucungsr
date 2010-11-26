/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remarf;

import remarf.Main;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author mauricio
 */
public class MainTest {

    public MainTest() {
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

    /**
     * Test of main method, of class Main.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = new String[3];
        args[0] = "-train";
        args[1] = "waves/gustavo_1.wav";
        args[2] = "1";
        Main.main(args);

        args[1] = "waves/jesica_1.wav";
        args[2] = "2";
        Main.main(args);

        args[0] = "-ident";
        args[1] = "waves/jesica_2.wav";
        Main.main(args);

        
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
}
