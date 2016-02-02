package nl.tudelft.ti2306.blockchain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import nl.tudelft.ti2306.blockchain.datastructure.InteractionGraph;
import nl.tudelft.ti2306.blockchain.datastructure.Interaction;

public class InteractionGraphToViz implements Runnable {

    private InteractionGraph graph;
    private String output;
    
    public InteractionGraphToViz(InteractionGraph graph, String output) {
        this.graph = graph;
        this.output = output;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(new File(output))) {
            out.println("digraph bartercast {");
            out.println("size=\"10,9\";");
            out.println("ratio=expand;");
            out.println("node[width=.20,height=.20, label=\"\"]");
            out.println("edge[arrowsize=\"0.3\"]");
            out.println(0 + " [style=filled fillcolor=yellow]");
            for (Interaction n2 : graph.getRoot().getChildren()) {
                out.println(0 + "->" + graph.getNodes().indexOf(n2));
            }
            for (int i = 1; i < graph.getNodes().size(); i++) {
                Interaction n = graph.getNodes().get(i);
                out.println(i + " [style=filled fillcolor=red]");
                for (Interaction n2 : n.getChildren()) {
                    out.println(i + "->" + graph.getNodes().indexOf(n2));
                }
            }
            out.println("}");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
}
