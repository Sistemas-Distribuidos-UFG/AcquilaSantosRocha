package com.calculadora.Calculadora;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.logging.Logger;

import com.calculadora.Calculadora.util.Resultado;
import com.calculadora.Calculadora.util.ArithmeticEvaluation.Expression;
import com.calculadora.Calculadora.util.ArithmeticEvaluation;
import com.calculadora.Calculadora.util.BigRational;



@Path("/calc")
public class CalculadoraResource {
    private final static Logger logger = Logger.getLogger(String.valueOf(CalculadoraResource.class));
    private Resultado parseEntrada(String a, String b) {
        int aI = 0, bI = 0;

        try {
            aI = Integer.parseInt(a);
        } catch (NumberFormatException e) {
            logger.warning("NumberFormatException, convertendo valor a 0");
        }
        try {
            bI = Integer.parseInt(b);
        } catch (NumberFormatException e) {
            logger.warning("NumberFormatException, convertendo valor a 0");
        }
        return new Resultado(aI, bI);
    }

    @GET
    @Path("soma/{a}/{b}")
    @Produces("text/plain")
    public String Soma(@PathParam("a") String a, @PathParam("b") String b) {
        logger.info("Requisitou Soma: " + a + " " + b);

        Resultado entrada = parseEntrada(a,b);
        int resultado = entrada.getFirst() + entrada.getSecond();

        try {
            return String.valueOf(resultado);
        } catch (Exception e) {
            return "";
        }
    }

    @GET
    @Path("sub/{a}/{b}")
    @Produces("text/plain")
    public String Subtracao(@PathParam("a") String a, @PathParam("b") String b) {
        logger.info("Requisitou Subtração: " + a + " " + b);
        Resultado entrada = parseEntrada(a,b);
        int resultado = entrada.getFirst() - entrada.getSecond();
        try {
            return String.valueOf(resultado);
        } catch (Exception e) {
            return "";
        }
    }

    @GET
    @Path("mult/{a}/{b}")
    @Produces("text/plain")
    public String Multiplicacao(@PathParam("a") String a, @PathParam("b") String b) {
        logger.info("Requisitou Multiplicação: " + a + " " + b);
        Resultado entrada = parseEntrada(a,b);
        int resultado = entrada.getFirst() * entrada.getSecond();
        try {
            return String.valueOf(resultado);
        } catch (Exception e) {
            return "";
        }
    }

    @GET
    @Path("div/{a}/{b}")
    @Produces("text/plain")
    public String Divisao(@PathParam("a") String a, @PathParam("b") String b) {
        logger.info("Requisitou Divisão: " + a + " " + b);
        Resultado entrada = parseEntrada(a,b);
        float resultado = entrada.getFirst();

        if (entrada.getSecond() > 0) {
            resultado = (float) entrada.getFirst() / entrada.getSecond();
        }
        try {
            return String.valueOf(resultado);
        } catch (Exception e) {
            return "";
        }
    }

    @GET
    @Path("expression/{exp}")
    @Produces("text/plain")
    public String ParseExpression(@PathParam("exp") String exp) {
        logger.info("Requisitou a expressão: " + exp);
        ArithmeticEvaluation ae = new ArithmeticEvaluation(exp);
        Expression expressao =  ae.GetRes();
        float resultado = 0;
        try {
            resultado = expressao.eval().floatValue();
        } catch (Exception e) {
            logger.warning("Erro ao transformar " + exp);
        }

        try {
            return String.valueOf(resultado);
        } catch (Exception e) {
            return "";
        }
    }
}
