package nl.tudelft.ti2306.blockchain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.tudelft.ti2306.blockchain.datastructure.InteractionGraph;
import nl.tudelft.ti2306.blockchain.datastructure.Peer;
import nl.tudelft.ti2306.blockchain.datastructure.PeerGraph;

/**
 * @author Maarten
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String ... args) {

        int peerCnt = 10;
        int interactionCnt = 30;
        int graphDegree = 2;

        switch (args.length) {
        case 3:
            graphDegree    = Integer.valueOf(args[2]);
        case 2:
            interactionCnt = Integer.valueOf(args[1]);
        case 1:
            peerCnt        = Integer.valueOf(args[0]);
            break;
        }

        PeerGraph pgraph = PeerGraphGenerator.generate(
                peerCnt, PeerGraphGenerator.SCALE_FREE, graphDegree, 0.1);
        new PeerGraphToViz(pgraph, "outputPeer.gv").run();

        InteractionGraph graph = InteractionGraphGenerator.generate(
                pgraph, interactionCnt);
        new InteractionGraphToViz(graph, "output.gv").run();

        System.out.println("Generated graph");
        
        printEdgeCount(pgraph);
    }

    /**
     * @param pgraph A PeerGraph.
     */
    private static void printEdgeCount(PeerGraph pgraph) {
        List<Peer> list = new ArrayList<>(pgraph.getNodes());
        Collections.sort(list, new PeerGraph.EdgeAmountSorter(pgraph));
        for (Peer p : list) {
            System.out.println(p.getId() + "\t" + pgraph.getEdges(p.getId()).size() + " edges");
        }
    }

}
