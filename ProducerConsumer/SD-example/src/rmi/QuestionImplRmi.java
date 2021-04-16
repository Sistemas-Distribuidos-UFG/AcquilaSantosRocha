package rmi;

import common.Question;
import java.rmi.RemoteException;

public class QuestionImplRmi implements QuestionRmiInterface {
    Question instance;

    public QuestionImplRmi(Question instance) {
        this.instance = instance;
    }

    @Override
    public void  ProduceA() throws RemoteException {
        instance.ProduceA();
    }

    @Override
    public void  ProduceB() throws RemoteException {
        instance.ProduceB();
    }

    @Override
    public void  ConsumeA() throws RemoteException {
        instance.ConsumeA();
    }

    @Override
    public void  ConsumeB() throws RemoteException {
        instance.ConsumeB();
    }

    @Override
    public int  GetQtdAConsumers() throws RemoteException {
        return instance.GetQtdAConsumers();
    }

    @Override
    public int  GetQtdBConsumers() throws RemoteException {
        return instance.GetQtdBConsumers();
    }

    @Override
    public int  GetQtdAProducers() throws RemoteException {
        return instance.GetQtdAProducers();
    }

    @Override
    public int  GetQtdBProducers() throws RemoteException {
        return instance.GetQtdBProducers();
    }
}
