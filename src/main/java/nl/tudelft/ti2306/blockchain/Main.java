package nl.tudelft.ti2306.blockchain;

import nl.tudelft.ti2306.blockchain.datastructure.InteractionGraph;

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
            peerCnt = 5;
            interactionCnt = 20;
        }

        InteractionGraph graph = InteractionGraphGenerator.generate(peerCnt, interactionCnt);
        new InteractionGraphToViz(graph, "output.gv").run();
        System.out.println("Done");
    }

}
