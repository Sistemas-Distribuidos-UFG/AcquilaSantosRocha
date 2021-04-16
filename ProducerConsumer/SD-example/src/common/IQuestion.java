package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.util.Random;


public interface IQuestion extends Remote{

    static final String QUESTON = "Question";
    Random randomObj = new SecureRandom();
    enum Option {
        A, B;

        public static Option Radomize() {
            return values()[(int) (randomObj.nextInt(values().length))];
        }
    }

    public void ProduceA() throws RemoteException;
    public void ProduceB() throws RemoteException;

    public void  ConsumeA() throws RemoteException;
    public void  ConsumeB() throws RemoteException;

    public int  GetQtdAConsumers() throws RemoteException;
    public int  GetQtdBConsumers() throws RemoteException;

    public int  GetQtdAProducers() throws RemoteException;
    public int  GetQtdBProducers() throws RemoteException;
}
