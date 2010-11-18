/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucungsr;

import java.io.File;
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

        procesarDirectorio();

    }

    private void procesarDirectorio() {
        String[] args = new String[1];
        SR2 sr2;
        File[] testWaveFiles = null;

        File fileOrDir = new File("waves");
        if (fileOrDir.isDirectory()) {
            testWaveFiles = fileOrDir.listFiles();
        } else {
            testWaveFiles = new File[1];
            testWaveFiles[0] = fileOrDir;
        }

        for (int i = 0; i < testWaveFiles.length; i++) {
            if (testWaveFiles[i].canRead()
                    && testWaveFiles[i].getName().toLowerCase().endsWith(".wav")) {
                args[0] = testWaveFiles[i].getPath();
                sr2 = new SR2(args);
            }
        }


    }
}
