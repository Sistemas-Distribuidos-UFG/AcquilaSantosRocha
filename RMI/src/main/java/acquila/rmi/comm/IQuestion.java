package acquila.rmi.comm;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface que deve ser compartilhada por servidor e clientes
 *
 * http://docente.ifsc.edu.br/mello
 */
public interface IQuestion extends Remote{

    public double solve1(String cargo, double salario) throws RemoteException;
    public Boolean solve2(String sexo, int idade) throws RemoteException;
    public Boolean solve3(double n1, double n2, double n3) throws RemoteException;
    public double solve4(String sexo, double altura) throws RemoteException;
    public String solve5(int idade) throws RemoteException;
    public double solve6(char nivel, double salario, int dependentes) throws RemoteException;
}
