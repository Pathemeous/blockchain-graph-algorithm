package nl.tudelft.ti2306.blockchain;

import nl.tudelft.ti2306.blockchain.datastructure.InteractionGraph;

/**
 * @author Maarten
 */
public class Main {
    
    /**
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        InteractionGraph graph = new InteractionGraph(10);
        graph.addInteraction(0, 1);
        graph.addInteraction(0, 2);
        graph.addInteraction(1, 3);
        graph.addInteraction(2, 3);
        graph.addInteraction(0, 4);
        graph.addInteraction(1, 5);
        graph.addInteraction(2, 6);
        graph.addInteraction(4, 2);
        graph.addInteraction(3, 7);
        graph.addInteraction(7, 0);
        graph.addInteraction(7, 8);
        graph.addInteraction(7, 3);
        graph.addInteraction(7, 2);
        graph.addInteraction(3, 2);
        graph.addInteraction(9, 2);
        graph.addInteraction(3, 4);
        graph.addInteraction(1, 3);
        new InteractionGraphToViz(graph, "output.gv").run();
    }

}
