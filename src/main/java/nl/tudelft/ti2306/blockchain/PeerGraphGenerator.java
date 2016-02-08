package nl.tudelft.ti2306.blockchain;

import java.util.Random;

import nl.tudelft.ti2306.blockchain.datastructure.PeerGraph;

/**
 * Generates a PeerGraph.
 * @author Maarten
 */
public class PeerGraphGenerator {

    public static final int SMALL_WORLD = 0;
    public static final int SCALE_FREE = 1;
    public static final int UNIFORM = 2;
    
    private static Random random = new Random();

    /**
     * Generates a PeerGraph.
     * @param peerCount amount of Peers in the graph.
     * @param method method to generate graph.
     * @param degree average graph degree.
     * @param params parameters for methods:
     *      SMALL_WORLD: chance for crossing edge.
     *      SCALE_FREE: none.
     *      UNIFORM: none.
     * @return
     */
    public static PeerGraph generate(int peerCount, int method, double degree, double... params) {
        if (peerCount == 1)
            return new PeerGraph(1);
        
        PeerGraph res = new PeerGraph(peerCount);
        switch (method) {
        case SMALL_WORLD: 
            for (int peer1 = 0; peer1 < peerCount; peer1++) {
                for (int i = 1; i < degree / 2 + 1; i++) {
                    if (random.nextDouble() < params[0]) {
                        int peer2;
                        while ((peer2 = random.nextInt(peerCount)) == peer1)
                            continue;
                        res.addEdge(peer1, peer2);
                    } else {
                        res.addEdge(peer1, (peer1 + i) % peerCount);
                    }
                }
            }
            break;
        case SCALE_FREE:
            res.addEdge(0, 1);
            for (int edges = 1; edges < peerCount * degree / 2; edges++) {
                int count = 0;
                int i = random.nextInt(edges * 2); // edge connects two nodes
                int peer1 = random.nextInt(peerCount);
                for (int peer2 = 0; peer2 < peerCount; peer2++) {
                    if (peer1 == peer2) continue;
                    count += res.getEdges(peer2).size();
                    if (count > i) {
                        res.addEdge(peer1, peer2);
                        break;
                    }
                }
            }
            break;
        case UNIFORM:
            for (int edges = 0; edges < peerCount * degree / 2; edges++) {
                int peer1 = random.nextInt(peerCount);
                int peer2;
                while ((peer2 = random.nextInt(peerCount)) == peer1)
                    continue;
                res.addEdge(peer1, peer2);
            }
            break;
        }
        return res;
    }

}
