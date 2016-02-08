package nl.tudelft.ti2306.blockchain;

import java.util.Random;

import nl.tudelft.ti2306.blockchain.datastructure.InteractionGraph;
import nl.tudelft.ti2306.blockchain.datastructure.PeerGraph;

/**
 * Created by justin on 02/02/16.
 */
public class InteractionGraphGenerator {
    
    public static final int LINEAR = 0;
    public static final int QUAD = 1;
    
    private static Random random = new Random();
    
    public static InteractionGraph generate(int peerCnt, int interactionCnt, int method) {

        InteractionGraph g = new InteractionGraph(peerCnt);

        int interactionsCreated = 0;

        while (interactionsCreated < interactionCnt) {

            int n = g.getPeers().size();
            int p1 = selectPeer(n, method);
            int p2 = selectPeer(n, method);

            // prevent interactions with same peers
            if (p1 == p2) {
                continue;
            }

            g.addInteraction(p1, p2);

            interactionsCreated++;

        }
        
        return g;

    }

    private static int selectPeer(int n, int method) {
        int res;
        switch (method) {
        default:
        case LINEAR: res = random.nextInt(n); break;
        case QUAD:   res = n - 1 - (int) Math.sqrt(random.nextInt(n * n)); break;
        }
        return res;
    }
    
    public static InteractionGraph generate(PeerGraph pgraph, int interactionCnt) {
        int n = pgraph.getNodes().size();
        InteractionGraph res = new InteractionGraph(n);
        int interactionsCreated = 0;
        while (interactionsCreated < interactionCnt) {
            int peer1 = random.nextInt(n);
            int peer2 = pgraph.getEdges(peer1).get(random.nextInt(pgraph.getEdges(peer1).size()));
            res.addInteraction(peer1, peer2, interactionsCreated);
            interactionsCreated++;
        }
        return res;
    }

}
