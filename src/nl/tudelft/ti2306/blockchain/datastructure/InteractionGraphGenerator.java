package nl.tudelft.ti2306.blockchain.datastructure;

import java.util.Collections;
import java.util.Random;

import nl.tudelft.ti2306.blockchain.InteractionGraphToViz;

/**
 * Created by justin on 02/02/16.
 */
public class InteractionGraphGenerator {

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

        // betere random
        Random r = new Random();

        InteractionGraph g = new InteractionGraph(peerCnt);

        int interactionsCreated = 0;

        while (interactionsCreated < interactionCnt) {

            int p1 = r.nextInt(g.getPeers().size());
            int p2 = r.nextInt(g.getPeers().size());

            // prevent interactions with same peers
            if (p1 == p2) {
                continue;
            }

            g.addInteraction(p1, p2);

            interactionsCreated++;

        }

        new InteractionGraphToViz(g, "output.gv").run();

    }

}
