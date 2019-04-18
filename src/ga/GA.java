package ga;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import static java.lang.Integer.parseInt;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
//import jxl.write.*;


class GA {

    public static ArrayList<Individuum> Results = new ArrayList<Individuum>();

    String filepath;
    String solutionpath;
    public GASettings settings;

    static int number = 50;
    static int num_genes = 22;//22
    static int pm = 8;

    LinkedList<Individuum> parents;
    LinkedList<Individuum> children;
    Individuum bestSolution;
    float bestFitness = Integer.MAX_VALUE;
    public static double bestFromFile;

    int[] fixCosts;
    int[][] transportCosts;
    Random random = new Random(206);


    public void DoOneStep(){
        reversedFitness();
        recombine_TurnwheelMethod();//selektion

        if (settings.mutation == GASettings.Mutation.bitflip) {
            flip_mutation();
        } else {
            swap_mutation();
        }
        if (settings.selections == GASettings.Selections.halfdie) {
            Rating_ReplaceHalfParents();
        } else {
            Rating_ReplaceAllParents();
        }
    }



    float calculateFitness(Individuum parent) {
        int costs = 0;
        for (int j = 0; j < num_genes; j++) {
            if (parent.gene[j] == 1) {
                costs = costs + fixCosts[j];
            }
        }
        int variableCosts = 0;
        for (int customerCounter = 0; customerCounter < number; customerCounter++) {
            int smallest = Integer.MAX_VALUE;
            for (int factoryNumber = 0; factoryNumber < num_genes; factoryNumber++) {
                if (parent.gene[factoryNumber] == 1) {
                    if (transportCosts[customerCounter][factoryNumber] < smallest) {
                        smallest = transportCosts[customerCounter][factoryNumber];
                    }
                }
            }
            variableCosts += smallest;
        }
        return costs + variableCosts;
    }

    void readFromFile() {
        try {
            FileReader fr = new FileReader(new File(filepath));
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            String line1 = br.readLine();
            String[] line1parts = line1.split(" ");
            num_genes = parseInt(line1parts[0]);
            number = parseInt(line1parts[1]);
        //System.out.print("Gene: " + num_genes+" Anzahl: "+number+" \n");

            children = new LinkedList<Individuum>();
            bestSolution = new Individuum(num_genes);
            fixCosts = new int[number];
            transportCosts = new int[number][num_genes];
            for (int i = 0; i < number; i++) {
                String line = br.readLine();
                String[] lineparts = line.split(" ");
                fixCosts[i] = parseInt(lineparts[1]);
                for (int j = 0; j < num_genes; j++) {
                    transportCosts[i][j] = parseInt(lineparts[j + 2]);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(GA.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    GA(String filepath, GASettings incsettings) {
        settings = incsettings;

        this.filepath = filepath;
        this.solutionpath = filepath + ".bub";
        this.parents = new LinkedList<Individuum>();
        readFromFile();
        for (int i = 0; i < number; i++) {
            Individuum ind = new Individuum(num_genes);
            for (int j = 0; j < num_genes; j++) {
                ind.gene[j] = Math.abs(random.nextInt()) % 2;
//				System.out.print(" "+parents[i][j]);
            }
            ind.fitness = calculateFitness(ind);

            if (ind.fitness < bestFitness) { //hier richtung ändern
                for (int j = 0; j < num_genes; j++) {
                    bestSolution = ind;
                }
                bestFitness = ind.fitness;
                //System.out.println("Beste Fitness= " + bestFitness);
            }
            parents.add(ind);

//			System.out.print(" "+fitness[i]);			
//			System.out.println();
        }

    }

    void rekombinieren_Homberger() {
        int indi1, indi2, divisionPlace;
        Individuum parent1, parent2;

        for (int i = 0; i < number; i++) {

            //Wähle zwei zufällige Elternteile, das Bessere davon wird elter1
            indi1 = Math.abs(random.nextInt()) % number;
            indi2 = Math.abs(random.nextInt()) % number;
            Individuum ind1 = (Individuum) parents.get(indi1);
            Individuum ind2 = (Individuum) parents.get(indi2);
            if (ind1.fitness < ind2.fitness) {
                parent1 = ind1;
            } else {
                parent1 = ind2;
            }
            //Wähle zwei zufällige Elternteile, das Bessere davon wird elter2
            indi1 = Math.abs(random.nextInt()) % number;
            indi2 = Math.abs(random.nextInt()) % number;
            ind1 = (Individuum) parents.get(indi1);
            ind2 = (Individuum) parents.get(indi2);
            if (ind1.fitness < ind2.fitness) {
                parent2 = ind1;
            } else {
                parent2 = ind2;
            }
            //Zufällige Trennstellte
            divisionPlace = Math.abs(random.nextInt()) % num_genes;

            Individuum stueckel = new Individuum(num_genes);

            for (int j = 0; j < divisionPlace; j++) {
                stueckel.gene[j] = parent1.gene[j];
            }

            for (int j = divisionPlace; j < num_genes; j++) {
                stueckel.gene[j] = parent2.gene[j];
            }
            children.add(stueckel);
        }

    }

    void reversedFitness(){
        if (!parents.isEmpty()) {
            Collections.sort(parents);
            float maxFitness = parents.peek().fitness;
            float minFitness = parents.peekLast().fitness;
            //System.out.println("Min: "+minFitness+" Max: "+maxFitness);
            for (Individuum indi : parents) {
                if(settings.selectionPressure== GASettings.SelectionPressure.hard)
                indi.reversedFitness = (maxFitness * minFitness / indi.fitness) - bestFitness + 1;
                else indi.reversedFitness = maxFitness * minFitness / indi.fitness;
            }
        }
        else
        {
            System.out.println("Error - Parents is empty");
        }
    }

    void recombine_TurnwheelMethod() {
        int overallFitness = 0;
        for (Individuum indi : parents) {
            overallFitness += indi.reversedFitness;
        }
        for (int i = 0; i < number; i++) {
            //System.out.println("recom");
            //Collections.sort(parents);
            Individuum best = null;
            Individuum secondbest = null;
            //System.out.println("Sorted");
            double chance = Math.random() * overallFitness;
            //System.out.println(chance);
            double fitnesscounter = 0;

            for (Individuum indi : parents) {
                if (chance < indi.reversedFitness + fitnesscounter) {
                    best = indi;
                    break;
                }
                fitnesscounter += indi.reversedFitness;
            }
            chance = Math.random() * overallFitness;
            fitnesscounter = 0;

            for (Individuum indi : parents) {
                if (chance < indi.reversedFitness + fitnesscounter) {
                    secondbest = indi;
                    break;
                }
                fitnesscounter += indi.reversedFitness;
            }
            //nur zur fehlervermeidung... eigentlich sollte das nicht nötig sein
            if(best == null){
                best = parents.getFirst();
            }
            if (secondbest == null){
                secondbest = parents.getFirst();
            }
            if (settings.crossover == GASettings.Crossover.uniform) {
                uniform_crossover(best, secondbest);
            } else {
                onepoint_crossover(best, secondbest);
            }
            //System.out.println("Best: " + best);

        }
    }

    void onepoint_crossover(Individuum indi1, Individuum indi2) {
        int trennstelle = Math.abs(random.nextInt()) % num_genes;
        Individuum stueckel = new Individuum(num_genes);
        for (int j = 0; j < trennstelle; j++) {
            stueckel.gene[j] = indi1.gene[j];
        }
        for (int j = trennstelle; j < num_genes; j++) {
            stueckel.gene[j] = indi2.gene[j];
        }
        children.add(stueckel);
    }

    void uniform_crossover(Individuum indi1, Individuum indi2) {
        Individuum stueckel = new Individuum(num_genes);
        boolean[] maske = new boolean[num_genes];
        for (int j = 0; j < num_genes; j++) {
            maske[j] = random.nextBoolean();
        }
        for (int j = 0; j < num_genes; j++) {
            if (maske[j] == true) {
                stueckel.gene[j] = indi1.gene[j];
            } else {
                stueckel.gene[j] = indi2.gene[j];
            }
        }
        children.add(stueckel);
    }

    void flip_mutation() {
        int randomnumber;
        for (Individuum ind1 : children) {
            for (int j = 0; j < num_genes; j++) {
                randomnumber = Math.abs(random.nextInt()) % 1000;
                if (randomnumber < pm) {
                    if (ind1.gene[j] == 0) {
                        ind1.gene[j] = 1;
                    } else {
                        ind1.gene[j] = 0;
                    }
                }
            }
        }
    }

    void swap_mutation() {
        int zz;
        for (Individuum ind1 : children) {
            for (int j = 0; j < num_genes - 1; j++) {
                zz = Math.abs(random.nextInt()) % 1000;
                if (zz < pm) {
                    int swap = ind1.gene[j];
                    ind1.gene[j] = ind1.gene[j + 1];
                    ind1.gene[j + 1] = swap;
                }
            }
        }
    }

    void Rating_ReplaceAllParents() {
        parents = (LinkedList) children.clone();
        children.clear();
        for (Individuum parent : parents) {
            parent.fitness = calculateFitness(parent);

            if (parent.fitness < bestFitness) { //hier richtung ändern
                bestFitness = parent.fitness;
                bestSolution = parent;
            }
        }
        //System.out.println("Beste Loesung: " + bestFitness);
    }

    void Rating_ReplaceHalfParents() {
        for (Individuum child : children) {
            child.fitness = calculateFitness(child);
        }
        Collections.sort(children); //hier error
        for (int i = 0; i < number / 2; i++) {
            parents.set(i, children.pollLast());
        }
        children.clear();

        for (Individuum parent : parents) {

            parent.fitness = calculateFitness(parent);

            if (parent.fitness < bestFitness) { //hier richtung ändern
                bestFitness = parent.fitness;
                bestSolution = parent;
            }
        }
        //System.out.println("Beste Loesung: " + bestFitness);
    }

    void CalcDiffToLiterature() {
        try {
            FileReader fr = new FileReader(new File(solutionpath));
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            String[] parts = line.split(" ");
            bestFromFile = Double.parseDouble(parts[parts.length - 1]);
            Double Difference = (bestFitness - bestFromFile) / bestFromFile;
            System.out.println("Best Fitness: " + bestFitness + "  Difference: " + Difference);
            bestSolution.DiffFromLit = Difference;
            bestSolution.Optimum = bestFromFile;
            Results.add(bestSolution);
        //BeispielGA.sheet

        } catch (Exception ex) {
            Logger.getLogger(GA.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
