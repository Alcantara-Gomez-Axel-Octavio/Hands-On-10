package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ruleta<T> {
    private List<T> elementos;
    private List<Double> probabilidades;
    private Random aleatorio;

    public Ruleta() {
        this.elementos = new ArrayList<>();
        this.probabilidades = new ArrayList<>();
        this.aleatorio = new Random();
    }

    public void agregarElemento(T elemento, double probabilidad) {
        elementos.add(elemento);
        probabilidades.add(probabilidad);
    }

    public T girar() {
        double valorGiro;
        double probabilidadAcumulada;

        do {
            valorGiro = aleatorio.nextDouble();
            probabilidadAcumulada = 0.0;

            for (int i = 0; i < probabilidades.size(); i++) {
                probabilidadAcumulada += probabilidades.get(i);
                if (valorGiro <= probabilidadAcumulada) {
                    return elementos.get(i);
                }
            }

            // En caso de errores de redondeo, devolver el último elemento
            return elementos.get(elementos.size() - 1);
        } while (valorGiro <= probabilidades.get(1));

    }


    public static void main(String[] args) {
        Ruleta<String> ruleta = new Ruleta<>();

        // Agregando elementos con sus respectivas probabilidades
        ruleta.agregarElemento("Opción 1", 0.1);
        ruleta.agregarElemento("Opción 2", 0.1);
        ruleta.agregarElemento("Opción 3", 0.8);

        // Girando la ruleta
        for (int i = 0; i < 10; i++) {
            String resultado = ruleta.girar();
            System.out.println("Giro " + (i + 1) + ": " + resultado);
        }
    }
}
