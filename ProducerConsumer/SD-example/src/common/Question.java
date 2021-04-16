package common;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.rmi.RemoteException;

import java.util.logging.Logger;

import util.CustomQueue;

public class Question implements IQuestion{
    private static final Logger LOGGER = Logger.getLogger(Question.class.getName());

    private static CustomQueue<Character> AQueue;
	private static CustomQueue<Character> BQueue;

    ThreadPoolExecutor pool;

    public Question () {
        AQueue = new CustomQueue<Character>();
        BQueue = new CustomQueue<Character>();

        pool = new ThreadPoolExecutor(400, 400, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }
    
    private class ThreadExec implements Runnable{
		CustomQueue<Character> queue;
		Character d;
		
		public ThreadExec(CustomQueue<Character> queue, Character d) {
			super();
			this.queue = queue;
			this.d = d;
		}


		public void run() {
			if(d!= null)
				queue.insert(d);
			else
				queue.remove();
		}
		
	}

    @Override
    public void ProduceA() throws RemoteException {
        LOGGER.info("Producing A");
        pool.execute(new ThreadExec(AQueue, new Character('A')));
    }

    @Override
    public void ProduceB() throws RemoteException {
        LOGGER.info("Producing B");
        pool.execute(new ThreadExec(BQueue, new Character('B')));
    }


    @Override
    public void ConsumeA() throws RemoteException {
        LOGGER.info("Consuming A");
        pool.execute(new ThreadExec(AQueue, null));
    }

    @Override
    public void ConsumeB() throws RemoteException {
        LOGGER.info("Consuming B");
        pool.execute(new ThreadExec(BQueue, null));
    }

    
    @Override
    public int  GetQtdAConsumers() throws RemoteException {
        return AQueue.getCountConsumer();
    }

    @Override
    public int  GetQtdBConsumers() throws RemoteException {
        return BQueue.getCountConsumer();
    }

    @Override
    public int  GetQtdAProducers() throws RemoteException {
        return AQueue.getCountProducer();
    }

    @Override
    public int  GetQtdBProducers() throws RemoteException {
        return BQueue.getCountProducer();
    }

}
