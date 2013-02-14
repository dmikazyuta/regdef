package uralchem.kckk.uit;

import java.util.concurrent.CountDownLatch;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class LoopingThread extends Thread {
    private CountDownLatch syncLatch = new CountDownLatch(1);
    private Handler handler;

    public LoopingThread() {
        super();
        start();
    } 

    @Override
    public void run() {
        try {
            Looper.prepare();
            handler = new Handler();
            syncLatch.countDown();
            Looper.loop();
        } catch(Exception e) {
            Log.d("LoopingThread", e.getMessage());
        }
    }

    public Handler getHandler() throws InterruptedException {
        syncLatch.await();
        return handler;
    }
    
    
}