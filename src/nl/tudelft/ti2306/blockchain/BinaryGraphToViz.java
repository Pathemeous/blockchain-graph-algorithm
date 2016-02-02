package nl.tudelft.ti2306.blockchain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import nl.tudelft.ti2306.blockchain.datastructure.BinaryGraph;
import nl.tudelft.ti2306.blockchain.datastructure.BinaryGraph.Node;
import nl.tudelft.ti2306.blockchain.datastructure.Interaction;

public class BinaryGraphToViz implements Runnable {

    private BinaryGraph<Interaction> graph;
    private String output;
    
    public BinaryGraphToViz(BinaryGraph<Interaction> graph, String output) {
        this.graph = graph;
        this.output = output;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(new File(output))) {
            out.println("digraph bartercast {");
            out.println("size=\"20,18\";");
            out.println("ratio=expand;");
            out.println("node[width=.20,height=.20, label=\"\"]");
            out.println("edge[arrowsize=\"0.3\"]");
            for (int i = 0; i < graph.getNodes().size(); i++) {
                Node<Interaction> n = graph.getNodes().get(i);
                out.println(i + " [style=filled fillcolor=red]");
                for (Node<Interaction> n2 : n.getChildren()) {
                    out.println(i + "->" + graph.getNodes().indexOf(n2));
                }
            }
            out.println("}");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
}
