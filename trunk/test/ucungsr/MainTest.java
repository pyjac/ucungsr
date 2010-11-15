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
import static org.junit.Assert.*;

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
        String[] traingArgs = new String[3];

        traingArgs[0] = "-train";
        traingArgs[1] = "waves/gustavo_1.wav";
        traingArgs[2] = "1";

        Main.main(traingArgs);

        traingArgs[1] = "waves/jesica_1.wav";
        traingArgs[2] = "2";

        Main.main(traingArgs);

        String[] testingArgs = new String[2];

        testingArgs[0] = "-ident";
        testingArgs[1] = "waves/gustavo_3.wav";

        Main.main(testingArgs);



        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

}