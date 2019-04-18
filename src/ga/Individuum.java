/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga;

/**
 *
 * @author 82musi1mst
 */
public class Individuum implements Comparable {
    int nummer;
    int[]gene;
    float fitness;
    float reversedFitness;
    Double DiffFromLit;
    Double Optimum;
    
    Individuum(int gene){
        this.gene = new int[gene];
    }

    @Override
    public int compareTo(Object o) {

        Individuum other = (Individuum)o;
        if (other.fitness>this.fitness) return 1;
        else if (other.fitness == this.fitness) return 0;
        else return -1;

    }
    
}
