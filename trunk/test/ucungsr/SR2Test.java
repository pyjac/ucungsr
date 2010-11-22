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
//        entrenarDirectorio();
        procesarDirectorio();

    }

    private void procesarDirectorio() {
        String[] args = new String[1];
        File[] testWaveFiles = null;

        File fileOrDir = new File("testing-samples");
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
                SR2 sR2 = new SR2(args);
                break;
            }
        }


    }

    private void entrenarDirectorio() {

        String[] args = new String[3];
        args[1] = "-insert";

        File[] testWaveFiles = null;

        File fileOrDir = new File("training-samples");
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
                args[2] = testWaveFiles[i].getName().split("_")[0];
                SR2 sR2 = new SR2(args);
            }
        }
    }
}
