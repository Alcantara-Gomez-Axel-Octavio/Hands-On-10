package com.company;

import java.util.ArrayList;
import java.util.Random;


public class AlgoritmoGenetico {
    private static final int CROMOSOMAS_POR_INDIVIDUO = 10;
    private static final int POBLACION = 6;
    public static int totalFitness=0;
    public static double padre=0;
    private static final int MAX_GENERACIONES = 50; // Ajusta el valor según sea necesario
    private static final double MUTATION_RATE = 0.05;

    public static void main(String[] args) {

        Ruleta<Individuo> ruleta = new Ruleta<>();

        // Crear una población inicial con 6 individuos
        ArrayList<Individuo> poblacionInicial = generarPoblacionInicial();
        ArrayList<Individuo> NuevaPoblacion = generarPoblacionInicial();

        // Definir el fitness deseado o la condición de parada
        double fitnessDeseado = 0.9; // ajusta el valor según sea necesario
        int generacionActual = 0;


        // Bucle principal del algoritmo genético
        while (Padre(poblacionInicial)/10 < fitnessDeseado && generacionActual < MAX_GENERACIONES) {

            // Calcular el total de fitness de la población
            totalFitness = totalFitness(poblacionInicial);

            // Calcular el porcentaje real de cada individuo
            porcentaje(poblacionInicial, totalFitness);

            // Seleccionar al más fuerte de la generación y colocarlo en la primera posición
            int posicionPadre = encontrarPadre(poblacionInicial);

            // Agregar elementos a la ruleta (excepto el padre)
            for (int i = 0; i < poblacionInicial.size(); i++) {
                Individuo individuo = poblacionInicial.get(i);
                double probabilidad = individuo.getFitness(); // Usar el porcentaje de fitness como probabilidad
                ruleta.agregarElemento(individuo, probabilidad);
            }

            // Generar nueva población con crossover
            double crossoverRate = 0.9; // ajusta el valor del crossover al gusto
            NuevaPoblacion = generarNuevaPoblacion(poblacionInicial, crossoverRate, ruleta);
            poblacionInicial.clear();
            poblacionInicial.addAll(NuevaPoblacion);


            // Aplicar mutación a la nueva población
            aplicarMutacion(NuevaPoblacion, MUTATION_RATE);

            // Incrementar el contador de generaciones
            generacionActual++;



            // Imprimir información sobre la generación actual
            System.out.println("Generación: " + generacionActual);
            System.out.println("Fitness promedio: " + calcularFitnessPromedio(NuevaPoblacion));
            System.out.println("Cromosoma con mayor fitness: " + Padre(poblacionInicial));
        }

        // Imprimir la población final
        System.out.println("Población Final:");
        for (Individuo individuo : NuevaPoblacion) {
            System.out.println(individuo.getCromosomas());
        }



    }



    private static void aplicarMutacion(ArrayList<Individuo> poblacion, double mutationRate) {
        for (Individuo individuo : poblacion) {
            for (int i = 0; i < individuo.getCromosomas().size(); i++) {
                if (mutationRate > Math.random()) {
                    // Aplicar mutación
                    int currentGene = individuo.getCromosomas().get(i);
                    int newGene = (currentGene == 1) ? 0 : 1;
                    individuo.setGene(i, newGene);
                }
            }
        }

    }

    private static double calcularFitnessPromedio(ArrayList<Individuo> poblacion) {
        int totalFitness = totalFitness(poblacion);
        return (double) totalFitness / poblacion.size();
    }

    public static ArrayList<Individuo> generarNuevaPoblacion(ArrayList<Individuo> poblacion, double crossoverRate, Ruleta<Individuo> ruleta) {
        // Lista para almacenar la nueva población
        ArrayList<Individuo> nuevaPoblacion = new ArrayList<>();

        // Iterar sobre cada individuo en la población actual
        for (Individuo individual : poblacion) {
            // Verificar si se aplicará crossover con la tasa especificada
            if (crossoverRate > Math.random()) {
                // Aplicar crossover
                Individuo segundoPadre = ruleta.girar(); // Obtener el segundo padre de la ruleta
                ArrayList<Individuo> offspring = crossover(individual, segundoPadre); // Realizar crossover
                nuevaPoblacion.addAll(offspring); // Agregar los hijos a la nueva población

                // Verificar y controlar el tamaño de la población
                if (nuevaPoblacion.size() > POBLACION) {
                    nuevaPoblacion.subList(POBLACION, nuevaPoblacion.size()).clear();
                }
            } else {
                // No aplicar crossover, agregar el individuo original a la nueva población
                nuevaPoblacion.add(individual);
            }
        }

        // Devolver la nueva población generada
        return nuevaPoblacion;
    }


    public static ArrayList<Individuo> crossover(Individuo padre1, Individuo padre2) {
        // Seleccionar un punto de crossover aleatorio
        int puntoCrossover = (int) (Math.random() * (CROMOSOMAS_POR_INDIVIDUO - 1)) + 1;

        // Crear cromosomas para los dos hijos basados en el punto de crossover
        ArrayList<Integer> cromosomasHijo1 = new ArrayList<>(padre1.getCromosomas().subList(0, puntoCrossover));
        cromosomasHijo1.addAll(padre2.getCromosomas().subList(puntoCrossover, CROMOSOMAS_POR_INDIVIDUO));

        ArrayList<Integer> cromosomasHijo2 = new ArrayList<>(padre2.getCromosomas().subList(0, puntoCrossover));
        cromosomasHijo2.addAll(padre1.getCromosomas().subList(puntoCrossover, CROMOSOMAS_POR_INDIVIDUO));

        // Crear objetos Individuo para representar a los dos hijos
        Individuo hijo1 = new Individuo(cromosomasHijo1);
        Individuo hijo2 = new Individuo(cromosomasHijo2);

        // Crear una lista para almacenar los hijos
        ArrayList<Individuo> offspring = new ArrayList<>();
        offspring.add(hijo1);
        offspring.add(hijo2);

        // Devolver la lista de hijos generados por crossover
        return offspring;
    }


    public static int encontrarPadre(ArrayList<Individuo> poblacion) {
        double maxFitness = Double.MIN_VALUE;
        int posicionPadre = -1;

        for (int i = 0; i < poblacion.size(); i++) {
            Individuo individuo = poblacion.get(i);
            if (individuo.calcularFitness() > maxFitness) {
                maxFitness = individuo.calcularFitness();
                posicionPadre = i;
            }
        }

        // Mover al padre a la primera posición
        if (posicionPadre != -1) {
            Individuo padre = poblacion.remove(posicionPadre);
            poblacion.add(0, padre);
        }

        return posicionPadre;
    }

    public static double Padre(ArrayList<Individuo> poblacion) {
        double padre = 0;
        for (Individuo individuo : poblacion) {

            if(individuo.calcularFitness()>padre){
                padre=individuo.calcularFitness();
            }
        }
        return padre;
    }

    public static void porcentaje(ArrayList<Individuo> poblacion, int totalFitness) {
        for (Individuo individuo : poblacion) {
            double porcentaje = individuo.calcularFitness() / totalFitness;
            individuo.setFitness(porcentaje);
        }
    }

    public static int totalFitness(ArrayList<Individuo> poblacion) {
        int total = 0;
        for (Individuo individuo : poblacion) {
            total += individuo.calcularFitness();
        }
        return total;
    }

    // Generar una población inicial con valores aleatorios para los cromosomas
    private static ArrayList<Individuo> generarPoblacionInicial() {
        ArrayList<Individuo> poblacionInicial = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < POBLACION; i++) {
            ArrayList<Integer> cromosomas = new ArrayList<>();
            for (int j = 0; j < CROMOSOMAS_POR_INDIVIDUO; j++) {
                cromosomas.add(random.nextInt(2)); // Se asume que los cromosomas son binarios (0 o 1)
            }
            poblacionInicial.add(new Individuo(cromosomas));
        }

        return poblacionInicial;
    }
}



