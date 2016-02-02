package nl.tudelft.ti2306.blockchain;

import java.util.Random;

import nl.tudelft.ti2306.blockchain.datastructure.InteractionGraph;

/**
 * Created by justin on 02/02/16.
 */
public class InteractionGraphGenerator {
    
    public static InteractionGraph generate(int peerCnt, int interactionCnt) {

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
        
        return g;

    }

}
