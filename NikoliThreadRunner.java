import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.awt.Toolkit;



public class NikoliThreadRunner{

    /**
    NikoliGen gen = new NikoliGen();      < creates new generator object
    gen.start();                        < creates a new thread to run generator
    gen.run();                          < runs generator WITHOUT threading
    */


    public static void main(String[] args) {

        /// THREAD RUNNER FOR nIKOLIgEN.JAVA:
        /* 
        ArrayList<Thread> threads = new ArrayList<>();
        for (int n_threads = 0; n_threads < 30; n_threads++){
            NikoliGen gen = new NikoliGen();
            Thread genThread = new Thread(gen);
            threads.add(genThread);
            genThread.start();
        }
        */

        // beep to indicate program running:
        //Toolkit.getDefaultToolkit().beep();
        System.out.println("Running generator... this may take a few minutes...");

        //NikoliGenRedo genR = new NikoliGenRedo();
        //Thread genThread = new Thread(genR);
        //genThread.run();
         

        ArrayList<Thread> threads = new ArrayList<>();
        for (int n_threads = 0; n_threads < 10; n_threads++){
            NikoliGenRedo genR = new NikoliGenRedo();
            Thread genThread = new Thread(genR);
            threads.add(genThread);
        }
        for (Thread thread : threads){
            thread.start();
        }
        

        // System.out.println("started threads");
        // try { Thread.sleep(10000);} 
        // catch (InterruptedException e) { System.out.println("crash"); }
        // System.out.println("sleep complete");

        //try {
            // when one thread finds a potential solution, end all other threads:
        //    System.in.read();
        //    for (Thread thread : threads){
        //        thread.stop();
        //    }
        //} catch (IOException e) { e.printStackTrace();}
    }
}
