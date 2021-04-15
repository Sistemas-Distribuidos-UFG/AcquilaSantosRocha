package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.util.Random;


public interface IQuestion extends Remote{

    static final String QUESTON = "Question";
    Random randomObj = new SecureRandom();
    enum Option {
        QUESTION1, QUESTION4;

        public static Option Radomize() {
            return values()[(int) (randomObj.nextInt(values().length))];
        }
    }

    public void ProduceQuestion1(String cargo, double salario) throws RemoteException;
    public void ProduceQuestion4(String sexo, double altura) throws RemoteException;

    public void  ConsumeQuestion1() throws RemoteException;
    public void  ConsumeQuestion4() throws RemoteException;

    public int  GetQtdQuestion1Consumers() throws RemoteException;
    public int  GetQtdQuestion4Consumers() throws RemoteException;

    public int  GetQtdQuestion1Producers() throws RemoteException;
    public int  GetQtdQuestion4Producers() throws RemoteException;
}
