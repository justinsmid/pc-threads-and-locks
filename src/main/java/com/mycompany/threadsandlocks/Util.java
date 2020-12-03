package com.mycompany.threadsandlocks;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {
    public static void joinAll(List<Thread> threads) {
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public interface Function {
        public void execute();
    }
}
