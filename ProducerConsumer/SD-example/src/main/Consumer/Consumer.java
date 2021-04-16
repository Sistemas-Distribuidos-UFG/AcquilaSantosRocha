package main.Consumer;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import java.util.logging.Level;
import java.util.logging.Logger;

import communication.CommunicationController;
import common.IQuestion;

public class Consumer extends Thread{
    private static final Logger LOGGER = Logger.getLogger(Consumer.class.getName());
    private static LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();

    private static int coreCount = 7;
    private static int maxCore = 7;

    public static void main(String[] args) {
        try {
            LOGGER.info("Setting up 7 consumers producing between 1 a 100ms ");
            
            IQuestion qStub = CommunicationController.getReference();
            ThreadPoolExecutor pool = new ThreadPoolExecutor(coreCount, maxCore, 10, TimeUnit.SECONDS, workQueue);
            
            while (true) {
                IQuestion.Option op = IQuestion.Option.Radomize();
                pool.execute(new ConsumerThread(qStub, op));
                Thread.sleep((long) (Math.random() * 100));
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Consumer", e);
        }

    }

    private static class ConsumerThread implements Runnable {
        IQuestion qStub;
        IQuestion.Option op;

        private static final Logger LOGGER = Logger.getLogger(ConsumerThread.class.getName());

        public ConsumerThread(IQuestion qStub, IQuestion.Option op) {
            this.qStub = qStub;
            this.op = op;
        }

        public void run() {
            try {
                switch (op) {
                    case A:
                        qStub.ProduceA();
                        break;
                    case B:
                        qStub.ProduceB();
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
				LOGGER.log(Level.SEVERE, "ConsumerThread", e);
			}
        }
    }

}