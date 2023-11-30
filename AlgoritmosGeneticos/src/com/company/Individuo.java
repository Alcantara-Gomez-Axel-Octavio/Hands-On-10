package com.company;

import java.util.ArrayList;

class Individuo {
    private ArrayList<Integer> cromosomas;
    private double fitness;

    public Individuo(ArrayList<Integer> cromosomas) {
        this.cromosomas = cromosomas;
        // Calcular y asignar el fitness aquí según tus criterios
        this.fitness = calcularFitness();
    }

    public ArrayList<Integer> getCromosomas() {
        return new ArrayList<>(cromosomas);
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double porcentaje) {
        this.fitness = porcentaje;
    }

    public double calcularFitness() {
        double porcentaje = 0;
        for (int cromosoma : cromosomas) {
            if (cromosoma == 1) {
                porcentaje++;
            }
        }
        return porcentaje;
    }

    public void setGene(int index, int value) {
        if (index >= 0 && index < cromosomas.size()) {
            cromosomas.set(index, value);
        } else {
            // Puedes manejar este caso según tus necesidades (lanzar una excepción, por ejemplo)
            System.out.println("Índice fuera de rango.");
        }
    }
}
