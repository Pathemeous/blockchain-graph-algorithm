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

        final int peerCnt;
        final int interactionCnt;

        switch (args.length) {
        case 2:
            peerCnt        = Integer.valueOf(args[0]);
            interactionCnt = Integer.valueOf(args[1]);
            break;
        default:
            peerCnt = 10;
            interactionCnt = 50;
        }

        PeerGraph pgraph = PeerGraphGenerator.generate(
                peerCnt, PeerGraphGenerator.SCALE_FREE, 5, 0.1);
        new PeerGraphToViz(pgraph, "outputPeer.gv").run();

        InteractionGraph graph = InteractionGraphGenerator.generate(
                pgraph, interactionCnt);
        new InteractionGraphToViz(graph, "output.gv").run();

        System.out.println("Generated graph");
    }

}
