package ga;

//import jxl.*;
//import jxl.write.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import static ga.GASettings.*;


class BeispielGA {

    public static GASettings settings = new GASettings(Selections.halfdie, SelectionPressure.easy, Crossover.onepoint, Mutation.bitflip);
    //public static WritableSheet sheet;
    
    
    static String[] filenames = {"250/a/ga250a-1", "250/a/ga250a-2", "250/a/ga250a-3", "250/a/ga250a-4","250/a/ga250a-5",
        "250/b/ga250b-1", "250/b/ga250b-2", "250/b/ga250b-3", "250/b/ga250b-4","250/b/ga250b-5",
        "250/c/ga250c-1", "250/c/ga250c-2", "250/c/ga250c-3", "250/c/ga250c-4","250/c/ga250c-5",
        "500/a/ga500a-1", "500/a/ga500a-2", "500/a/ga500a-3", "500/a/ga500a-4","500/a/ga500a-5",
        "500/b/ga500b-1", "500/b/ga500b-2", "500/b/ga500b-3", "500/b/ga500b-4","500/b/ga500b-5",
        "500/c/ga500c-1", "500/c/ga500c-2", "500/c/ga500c-3", "500/c/ga500c-4","500/c/ga500c-5",
        "750/a/ga750a-1", "750/a/ga750a-2", "750/a/ga750a-3", "750/a/ga750a-4","750/a/ga750a-5",
        "750/b/ga750b-1", "750/b/ga750b-2", "750/b/ga750b-3", "750/b/ga750b-4","750/b/ga750b-5",
        "750/c/ga750c-1", "750/c/ga750c-2", "750/c/ga750c-3", "750/c/ga750c-4","750/c/ga750c-5",
        };

    public static void main(String[] args) {

        try {
            
           /* WritableWorkbook wb = Workbook.createWorkbook(new File("lsg_4.xls"));
            WritableSheet ws = wb.createSheet("Sheet1", 0);
            for (int i=0;i<filenames.length;i++) {
                ws.addCell(new Label(1, i+1 , filenames[i]));
                ws.addCell(new Label(0, i+1 , String.valueOf(i)));
            }*/
            /*
            
            for (String filename : filenames) {
                calculateOneFile(filename);
            }
            for(int i=0;i<GA.Results.size();i++){
                Individuum indi = GA.Results.get(i);
                String Fitness = String.format("%.12f", indi.fitness);
                String DiffFromLit = String.format("%.12f", indi.DiffFromLit);
                ws.addCell(new Label(3, i+1, Fitness));
                ws.addCell(new Label(4, i+1, DiffFromLit));
                ws.addCell(new Label(2, i+1, String.valueOf(indi.Optimum)));

            }
            System.out.println("Runde 1 durch");
            GA.Results.clear();
            */
            //useSelektionUeberlebende_haelfte = false;
            /*
            for(String filename : filenames){
                calculateOneFile(filename);
            }
            for(int i=0;i<GA.Results.size();i++){
                Individuum indi = GA.Results.get(i);
                String Fitness = String.format("%.12f", indi.fitness);
                String DiffFromLit = String.format("%.12f", indi.DiffFromLit);
                ws.addCell(new Label(5, i+1, Fitness));
                ws.addCell(new Label(6, i+1, DiffFromLit));

            }
            System.out.println("Runde 2 durch");
            GA.Results.clear();
            */
            

            /*useUniformCrossover = false;
            
            for(String filename : filenames){
                calculateOneFile(filename);
            }
             for(int i=0;i<GA.Results.size();i++){
                Individuum indi = GA.Results.get(i);
               String Fitness = String.format("%.12f", indi.fitness);
                String DiffFromLit = String.format("%.12f", indi.DiffFromLit);
                ws.addCell(new Label(7, i+1, Fitness));
                ws.addCell(new Label(8, i+1, DiffFromLit));
            }
            System.out.println("Runde 3 durch");

            GA.Results.clear();
            */
            
            //useBitFlipMutation = false;

            for(String filename : filenames){
                System.out.print("File "+filename+": ");
                calculateOneFile(filename);
            }
             /*for(int i = 0; i<GA.Results.size(); i++){
                Individuum indi = GA.Results.get(i);
               String Fitness = String.format("%.12f", indi.fitness);
                String DifferenceToLiterature = String.format("%.12f", indi.DiffFromLit);
                ws.addCell(new Label(9, i+1, Fitness));
                ws.addCell(new Label(10, i+1, DifferenceToLiterature));

            }
             System.out.println("Runde 4 durch");
            GA.Results.clear();
            
            wb.write();
            wb.close();*/
            
        } catch (Exception ex) {
            Logger.getLogger(BeispielGA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void calculateOneFile(String filename){
        GA ga = new GA(filename, settings);

        for (int g = 0; g < 400; g++) {
            ga.DoOneStep();
        }
        ga.CalcDiffToLiterature();

        
    }
}
