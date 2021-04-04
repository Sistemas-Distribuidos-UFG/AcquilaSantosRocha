package acquila.rmi.server;

import acquila.rmi.comm.IQuestion;
import java.rmi.RemoteException;


public class Question implements IQuestion{

    private String result;

    @Override
    public double solve1(String cargo, double salario) throws RemoteException {

        if (cargo.equals("operador")) {
            return (double) salario*1.2;
        } else if (cargo.equals("programador")) {
            return (double) salario*1.18;
        }
        return (double) salario;
    }

    @Override
    public Boolean solve2(String sexo, int idade) throws RemoteException {

        if ((sexo.equals("masculino") && idade >= 18) || (sexo.equals("feminino") && idade >= 21)) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean solve3(double n1, double n2, double n3) throws RemoteException {

        double media = (n1 + n2)/2.0;
        if ((media >= 7.0) || ((media > 3.0 && media < 7.0) && (media + n3)/2.0 >= 5.0)) {
            return true;
        }
        return false;
    }

    @Override
    public double solve4(String sexo, double altura) throws RemoteException {

        if (sexo.equals("masculino")) {
            return (72.7 * altura - 58);
        }
        return (62.1 * altura - 44.7);
    }

    @Override
    public String solve5(int idade) throws RemoteException {

        if (idade >= 5 && idade <= 7)
          return "INFANTIL A";
        else if (idade >= 8 && idade <= 10)
          return "INFANTIL B";
        else if (idade >= 11 && idade <= 13)
          return "JUVENIL A";
        else if (idade >= 14 && idade <= 17)
          return "JUVENIL B";
        else if (idade >= 18)
          return "ADULTO";

        return "INAPTO";
    }

    @Override
    public double solve6(char nivel, double salario, int dependentes) throws RemoteException {

        if (nivel == 'A')
            return (dependentes == 0 ? salario*0.97 : salario*0.92);
        else if (nivel == 'B')
            return (dependentes == 0 ? salario*0.95 : salario*0.90);
        else if (nivel == 'C')
            return (dependentes == 0 ? salario*0.92 : salario*0.85);
        else if (nivel == 'D')
            return (dependentes == 0 ? salario*0.90 : salario*0.83);

        return salario;

    }
}
