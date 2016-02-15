package nl.tudelft.ti2306.blockchain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import nl.tudelft.ti2306.blockchain.datastructure.PeerGraph;

public class PeerGraphToViz implements Runnable {

    private PeerGraph graph;
    private String output;

    public PeerGraphToViz(PeerGraph graph, String output) {
        this.graph = graph;
        this.output = output;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(new File(output))) {
            out.println("graph peergraph {");
            out.println("ratio=expand;");
            out.println("node[width=.40,height=.40, label=\"\"]");
            out.println("edge[arrowsize=\"0.3\"]");
            for (int i = 0; i < graph.getNodes().size(); i++) {
                double hue = i / (double) graph.getNodes().size();
                double spd = Math.round(graph.getNodes().get(i).getUploadSpeed() * 100) / 100.0;
                out.println(i + " [label=<" + i + "<BR />"
                        + "<FONT POINT-SIZE=\"8\">" + spd + "</FONT>> "
                        + "style=filled fillcolor=\"" + hue + " 1.0 1.0\"]");
                for (int j = 0; j < graph.getEdges(i).size(); j++) {
                    int k = graph.getEdges(i).get(j);
                    if (i > k) continue;
                    out.println(i + "--" + k + " [color=black]");
                }
            }
            out.println("}");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
