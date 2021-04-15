package common;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.rmi.RemoteException;

import common.IQuestion;

import util.CustomQueue;
import rmi.QuestionRmiInterface;

public class Question implements IQuestion{
    private static CustomQueue<Double> q1Queue;
	private static CustomQueue<Double> q4Queue;

    ThreadPoolExecutor pool;

    public Question () {
        q1Queue = new CustomQueue<Double>();
        q4Queue = new CustomQueue<Double>();

        pool = new ThreadPoolExecutor(400, 400, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public Double solve1(String cargo, double salario) {
        if (cargo.equals("operador")) {
            return (double) salario*1.2;
        } else if (cargo.equals("programador")) {
            return (double) salario*1.18;
        }
        return (double) salario;
    }
    
    @Override
    public void ProduceQuestion1(String cargo, double salario) throws RemoteException {
        pool.execute(new ThreadExec(q1Queue, solve1(cargo, salario)));
    }


    public Double solve4(String sexo, double altura) {
        if (sexo.equals("masculino")) {
            return (72.7 * altura - 58);
        }
        return (62.1 * altura - 44.7);
    }


    @Override
    public void ProduceQuestion4(String sexo, double altura) throws RemoteException {
        pool.execute(new ThreadExec(q4Queue, solve4(sexo, altura)));
    }


    @Override
    public void ConsumeQuestion1() throws RemoteException {
        pool.execute(new ThreadExec(q1Queue, null));
    }

    @Override
    public void ConsumeQuestion4() throws RemoteException {
        pool.execute(new ThreadExec(q4Queue, null));
    }

    private class ThreadExec implements Runnable{
		CustomQueue<Double> queue;
		Double d;
		
		public ThreadExec(CustomQueue<Double> queue, Double d) {
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
    public int  GetQtdQuestion1Consumers() throws RemoteException {
        return q1Queue.getCountConsumer();
    }

    @Override
    public int  GetQtdQuestion4Consumers() throws RemoteException {
        return q4Queue.getCountConsumer();
    }

    @Override
    public int  GetQtdQuestion1Producers() throws RemoteException {
        return q1Queue.getCountProducer();
    }

    @Override
    public int  GetQtdQuestion4Producers() throws RemoteException {
        return q4Queue.getCountProducer();
    }

}
