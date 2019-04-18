package ga;

public class GASettings {
    enum Selections  {alldie, halfdie};
    enum SelectionPressure {easy, hard};
    enum Crossover {uniform, onepoint};
    enum Mutation {bitflip, bitswap};

    public Selections selections;
    public SelectionPressure selectionPressure;
    public Crossover crossover;
    public Mutation mutation;

    public GASettings(Selections selections, SelectionPressure selectionPressure, Crossover crossover, Mutation mutation) {
        this.selections = selections;
        this.selectionPressure = selectionPressure;
        this.crossover = crossover;
        this.mutation = mutation;
    }
}
