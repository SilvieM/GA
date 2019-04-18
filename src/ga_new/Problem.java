package ga_new;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
//Optimaler Wert für 123UnifS.txt: 71342

public class Problem{
	
	// problembezogene Daten
	static int n;							//Anzahl Standorte zugleich Anzahl Gene
	static int m;							//Anzahl Kunden
	static int[]   f;   					//Fixkosten
	static int[][] t;						//Transportkosten

	// verfahrensbezogene Daten
	static double pm; 						//Mutationswahrscheinlichkeit

	
	public static void einlesenInstanz(String dateiName){
		// von Florian Haefner

		// Aufbau der Datei:
		// Dateiname
		// Zeilen   --- Spalten --- 0 (= Es gibt keine Kapazität)
		// Standort --- Fixkosten --- TransportkostenZuKunde1 --- ...
		// Standort --- ...
		// ...

		// Die Eingabedatei muss im Projektordner liegen!
		//File inputfile = new File("123UnifS.txt");
		File inputfile = new File(dateiName);
		
			
		FileReader filereader = null;
		BufferedReader reader = null;
		Scanner lineScanner   = null;

		try
		{
			filereader = new FileReader(inputfile);
			reader     = new BufferedReader(filereader);

			// Erste Zeile wird gelesen und direkt verworfen, da dort nur der Dateiname
			// steht.
			reader.readLine();

			String secondline = reader.readLine();
			lineScanner = new Scanner(secondline);
			lineScanner.useDelimiter(" ");
			int zeilen  = lineScanner.nextInt();//Anzahl Standorte
			int spalten = lineScanner.nextInt();//Anzahl Kunden
			n    = zeilen;
			m    = spalten;
			f    = new int[zeilen];
			t    = new int[zeilen][spalten];
			
			//System.out.println(zeilen + " " + spalten);
			
			for (int y = 0; y < zeilen; y++){
				lineScanner = new Scanner(reader.readLine());
				lineScanner.useDelimiter(" ");
				lineScanner.nextInt(); // Standortnummer
				f[y] = lineScanner.nextInt(); // Fixkosten
				
				for (int x = 0; x < spalten; x++)
				{
					t[y][x] = lineScanner.nextInt();
				}
			}
		}

		catch (IOException e)
		{
			e.printStackTrace();
		}

		finally
		{
			try
			{
				filereader.close();
				reader.close();
				lineScanner.close();
			}

			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	public static void ausgabeKosten(int[][] t, int[] f) {
		System.out.println("Transportkosten:");		
		for(int i=0;i<t.length;i++) {
			for(int j=0;j<t[i].length;j++) {
				System.out.print(t[i][j] + " "); 
			}
			System.out.println();
		}
		
		System.out.println("Fixkosten:");
		for(int i=0;i<t.length;i++) {
			System.out.println(f[i]); 
		}
		System.out.println("-------------------------------");
		
	}		
	
	
	public static void initialisierung() {
		//Anzahl der Gene werden problemspezifisch festgelegt
		for(int i=0;i<Ablauf.POP.length;i++){
			Ablauf.POP[i] = new int[n];
			Ablauf.POPCHILD[i] = new int[n];
		}
		pm = 1.0/n;           //Mutationswahrscheinlichkeit
		
		randomPop();
	}

	public static void randomPop() {

		for (int i = 0; i < Ablauf.POP.length; i++) {
			for (int s = 0; s < Ablauf.POP[i].length; s++) {
				int anz = 0;
				if (Math.random() < 0.5) {
					Ablauf.POP[i][s] = 0;
				} else {
					Ablauf.POP[i][s] = 1;
					anz++;
				}

				if (anz == 0) {
					int spalte = (int) (n * Math.random());// spalte ist kleiner als n
					Ablauf.POP[i][spalte] = 1;
				}
			}
		}
	}
	
	
	
	
	public static int fitness(int[] indi) {
		// 010101010
		// indi = 111
		System.out.println("indi is" + indi[0]);
		int fit = 0;
		int warehouseFixFitness =0;
		for (int k = 0; k < indi.length;k++)
		{
			//calculate the warehouse cost based on the individual(indi) selected
			if(indi[k] == 1) {
				fit = fit + f[k];
			}
		}
		for (int i=0; i < t[0].length ; i++){ //for each customer
			int min = Integer.MAX_VALUE;
			for (int j =0;j<indi.length;j++) // for each customer, check the minimum of the cost for each location.
			{
				if(indi[j] == 1) {
					if (t[j][i] < min) {
						min = t[j][i];
						fit = min;
					}
				}
			}
			fit = min+fit;
		}
		return fit;
	}

	
}
