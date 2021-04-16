package communication;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.logging.Level;
import java.util.logging.Logger;

import common.IQuestion;
import rmi.QuestionRmiInterface;

public class CommunicationService implements IQuestion {
    
    private static final Logger LOGGER = Logger.getLogger(CommunicationService.class.getName());

    QuestionRmiInterface rmi;
    

    public CommunicationService() throws Exception {
        String name = QuestionRmiInterface.QUESTON;
        try {

            LOGGER.info("Setting up RMI interface");
            Registry registry = LocateRegistry.getRegistry();
            rmi = (QuestionRmiInterface) registry.lookup(name);
        } catch (Exception e) {
            rmi = null;
        }
    }

    @Override
    public void ProduceA() throws RemoteException {
        try {
            rmi.ProduceA();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "ProduceA()", e);
            System.exit(0);
        }
        
    }

    @Override
    public void ProduceB() throws RemoteException {
        try {
            rmi.ProduceB();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "ProduceB()", e);
            System.exit(0);
        }
    }


    @Override
    public void ConsumeA() throws RemoteException {
        try {
            rmi.ConsumeA();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "ConsumeA()", e);
            System.exit(0);
        }
    }

    @Override
    public void ConsumeB() throws RemoteException {
        try {
            rmi.ConsumeB();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "ConsumeB()", e);
            System.exit(0);
        }
    }
    
    @Override
    public int  GetQtdAConsumers() throws RemoteException {
        try {
            return rmi.GetQtdAConsumers();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "GetQtdAConsumers()", e);
            System.exit(0);
        }
        return 0;
    }

    @Override
    public int  GetQtdBConsumers() throws RemoteException {
        try {
            return rmi.GetQtdBConsumers();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "GetQtdBConsumers()", e);
            System.exit(0);
        }
        return 0;
    }

    @Override
    public int  GetQtdAProducers() throws RemoteException {
        try {
            return rmi.GetQtdAProducers();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "GetQtdAProducers()", e);
            System.exit(0);
        }
        return 0;
    }

    @Override
    public int  GetQtdBProducers() throws RemoteException {
        try {
            return rmi.GetQtdBProducers();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "GetQtdBProducers()", e);
            System.exit(0);
        }
        return 0;
    }

}
