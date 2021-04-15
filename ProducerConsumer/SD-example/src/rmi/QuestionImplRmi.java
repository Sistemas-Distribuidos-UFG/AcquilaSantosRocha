package rmi;

import common.Question;
import java.rmi.RemoteException;

public class QuestionImplRmi implements QuestionRmiInterface {
    Question instance;

    public QuestionImplRmi(Question instance) {
        this.instance = instance;
    }

    @Override
    public void  ProduceQuestion1(String cargo, double salario) throws RemoteException {
        instance.ProduceQuestion1(cargo, salario);
    }

    @Override
    public void  ProduceQuestion4(String cargo, double salario) throws RemoteException {
        instance.ProduceQuestion4(cargo, salario);
    }

    @Override
    public void  ConsumeQuestion1() throws RemoteException {
        instance.ConsumeQuestion1();
    }

    @Override
    public void  ConsumeQuestion4() throws RemoteException {
        instance.ConsumeQuestion4();
    }

    @Override
    public int  GetQtdQuestion1Consumers() throws RemoteException {
        return instance.GetQtdQuestion1Consumers();
    }

    @Override
    public int  GetQtdQuestion4Consumers() throws RemoteException {
        return instance.GetQtdQuestion4Consumers();
    }

    @Override
    public int  GetQtdQuestion1Producers() throws RemoteException {
        return instance.GetQtdQuestion1Producers();
    }

    @Override
    public int  GetQtdQuestion4Producers() throws RemoteException {
        return instance.GetQtdQuestion4Producers();
    }
}
