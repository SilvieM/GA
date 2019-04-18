package ga_new;//04_12_18
import java.util.*;

public class Ablauf {
	// Parameter
	static int anz   = 10*2;  				//Anzahl Individuen
	static int maxit = 200;				    //Maximale Anzahl an Itertionen
	static boolean minimierung = true;		//Minimierungsproblem
	
	// Datenstrukturen für GA
	static int[][] POP = new int[anz][];	  //Population aus anz Individuen
	static int[]   FIT = new int[anz];		  //Fitness für anz Individuen
	static int[][] POPCHILD = new int[anz][]; //Kinder aus anz Individuen
	static int[]   FITCHILD = new int[anz];	  //Fitness für anz Kinder
	
	// Variablen für GA
	static int iteration;					//Iterationszaehler
	static int bestFit;						//Beste Fitness 
	
	public static void main(String[] args) {
		if(minimierung)	bestFit = Integer.MAX_VALUE; 
		else 			bestFit = Integer.MIN_VALUE; 

		Problem.einlesenInstanz("123UnifS.txt");
		
		Problem.initialisierung();
		bewertung(POP,FIT);

	}
	
	public static void bewertung(int[][] ind, int[] fit) {
		int aktFit;
		if(minimierung)aktFit = Integer.MAX_VALUE;
		else           aktFit = Integer.MIN_VALUE;
		
		for (int i = 0; i < fit.length; i++) {
			fit[i] = Problem.fitness(ind[i]);
			System.out.println(i + " " + fit[i]);
			
			if(minimierung){
				if (fit[i] < bestFit) {
					bestFit = fit[i];
				}
				if (fit[i] < aktFit) {
					aktFit = fit[i];
				}
			}
			else{
				if (fit[i] > bestFit) {
					bestFit = fit[i];
				}
				if (fit[i] > aktFit) {
					aktFit = fit[i];
				}
			}
		}
		System.out.println(iteration + " " + aktFit + " " + bestFit);

	}

	public static void ausgabePop() {
		System.out.println("Eltern:");
		for (int indi = 0; indi < POP.length; indi++) {
			System.out.print(indi + " ");
			for (int st = 0; st < POP[indi].length; st++) {
				System.out.print(POP[indi][st]);
			}
			System.out.print(" " + FIT[indi]);
			System.out.println();
		}
	}
	public static void ausgabeChild() {

		System.out.println("Childs:");
		for (int indi = 0; indi < POPCHILD.length; indi++) {
			System.out.print(indi + " ");
			for (int st = 0; st < POPCHILD[indi].length; st++) {
				System.out.print(POPCHILD[indi][st]);
			}
			System.out.print(" " + FITCHILD[indi]);
			System.out.println();
		}
	}

}
