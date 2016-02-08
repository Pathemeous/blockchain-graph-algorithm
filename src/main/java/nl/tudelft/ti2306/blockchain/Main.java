package nl.tudelft.ti2306.blockchain;

import nl.tudelft.ti2306.blockchain.datastructure.InteractionGraph;
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
        int interactionCnt = 50;
        int graphDegree = 4;

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
                peerCnt, PeerGraphGenerator.SMALL_WORLD, graphDegree, 0.1);
        new PeerGraphToViz(pgraph, "outputPeer.gv").run();

        InteractionGraph graph = InteractionGraphGenerator.generate(
                pgraph, interactionCnt);
        new InteractionGraphToViz(graph, "output.gv").run();

        System.out.println("Generated graph");
    }

}
