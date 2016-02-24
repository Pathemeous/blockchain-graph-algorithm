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
            out.println("digraph interactiongraph {");
            out.println("ratio=expand;");
            out.println("node[width=.20,height=.20, label=\"\"]");
            out.println("edge[arrowsize=\"0.3\"]");
            for (int i = 0; i < graph.getNodes().size(); i++) {
                Interaction n = graph.getNodes().get(i);
                double hue[] = new double[]{
                        n.getPeer1().getId() / (double) graph.getPeers().size(), 
                        n.getPeer2().getId() / (double) graph.getPeers().size()
                };
                out.println(i + " [label=" + i + " style=filled fillcolor="
                        + "\"" + hue[0] + " 1.0 1.0;0.5:" + hue[1] + " 1.0 1.0\"]");
                for (int j = 0; j < 2; j++) {
                    if (n.getChildren().get(j) == null) continue;
                    int n1 = graph.getNodes().indexOf(n.getChildren().get(j));
                    int n2 = new int[]{n.getPeer1().getId(), n.getPeer2().getId()}[j];
                    out.println(i + "->" + n1 +
                            " [label=" + n2 + ", color=\"" + hue[j] + " 1.0 1.0\"]");
                }
            }
            out.println("}");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
