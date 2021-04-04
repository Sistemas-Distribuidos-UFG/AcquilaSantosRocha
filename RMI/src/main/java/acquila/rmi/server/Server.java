package acquila.rmi.server;

import acquila.rmi.comm.IQuestion;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


public class Server {

    private static final String IPSERVER = "127.0.0.1";
    private static final int PORT = 54321;
    private static final String REMOTEOBJ = "QUESTIONSOLVER";

    public static void main (String args[]) {
        try {
            
            Question q = new Question();

            // Definindo o hostname do servidor
            System.setProperty("java.rmi.server.hostname", IPSERVER);

            IQuestion stub = (IQuestion) UnicastRemoteObject.exportObject(q, 0);

            // Criando serviço de registro
            Registry registro = LocateRegistry.createRegistry(PORT);
            registro.bind(REMOTEOBJ, stub);
            // Registrando objeto distribuído
            
            // System.out.println("Remote Objects " + objs + " ready!");
            System.out.println("Servidor pronto!\n");
            System.out.println("Pressione CTRL + C para encerrar...");


        } catch (RemoteException | AlreadyBoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}