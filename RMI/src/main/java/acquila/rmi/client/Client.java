package acquila.rmi.client;

import acquila.rmi.comm.IQuestion;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Client {
    private static final String IPSERVER = "127.0.0.1";
    private static final int PORT = 54321;
    private static final String REMOTEOBJ = "QUESTIONSOLVER";

    
    public static void main(String args[]) {
        try {
            Registry questionSolver = LocateRegistry.getRegistry(IPSERVER, PORT);

            IQuestion stub = (IQuestion) questionSolver.lookup(REMOTEOBJ);

            System.out.println("[*] Question1('operador', 2341.0) -> " + stub.solve1("operador", 2341.0));
            System.out.println("[*] Question2('masculino', 19) -> " + stub.solve2("masculino", 19));
            System.out.println("[*] Question3(9.0, 2.2, 4.0) -> " + stub.solve3(9.0, 2.2, 4.0));
            System.out.println("[*] Question4('masculino', 1.76) -> " + stub.solve4("masculino", 1.76));
            System.out.println("[*] Question5(15) -> " + stub.solve5(15));
            System.out.println("[*] Question6('A', 2341.0, 3) -> " + stub.solve6('A', 2341.0, 3));

            System.out.println("Fim da execução do cliente!");
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}