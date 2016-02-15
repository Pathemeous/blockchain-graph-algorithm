package nl.tudelft.ti2306.blockchain;

import java.util.Random;

import nl.tudelft.ti2306.blockchain.datastructure.Peer;
import nl.tudelft.ti2306.blockchain.datastructure.PeerGraph;
import nl.tudelft.ti2306.blockchain.util.WeightedRandom;

/**
 * Generates a PeerGraph.
 * @author Maarten
 */
public class PeerGraphGenerator {

    public static final int SMALL_WORLD = 0;
    public static final int SCALE_FREE = 1;
    public static final int UNIFORM = 2;
    
    // https://en.wikipedia.org/wiki/List_of_countries_by_Internet_connection_speeds
    private static final double[] SPEEDS = new double[]{20.5,17.4,16.4,16.2,15.8,15.6,15,14.8,14.5,14,13.1,13,12.8,12.6,12.5,12.4,11.9,11.5,11.4,11.2,11.2,10.7,10.6,10.6,10.4,10.2,10.1,8.7,8.2,8.2,7.8,6.8,6.5,6.2,5.9,5.7,5.5,5.1,4.9,4.4,4.2,4.2,4.1,3.7,3.7,3.6,3.5,3.4,3.2,3,2.8,2.5,1.8,1.5,1.5};

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
        for (Peer p : res.getNodes()) {
            p.setUploadSpeed(SPEEDS[random.nextInt(SPEEDS.length)] * (0.5 + random.nextDouble()));
        }
        switch (method) {
        case SMALL_WORLD: 
            for (int peer1 = 0; peer1 < peerCount; peer1++) {
                for (int i = 1; i <= degree / 2; i++) {
                    if (random.nextDouble() < params[0]) {
                        int peer2;
                        while ((peer2 = random.nextInt(peerCount)) == peer1
                                || res.getEdges(peer1).contains(peer2)
                                || inRange(peer1 - degree/2, peer2, peer1 + degree/2, peerCount))
                            continue;
                        res.addEdge(peer1, peer2);
                    } else {
                        res.addEdge(peer1, (peer1 + i) % peerCount);
                    }
                }
            }
            break;
        case SCALE_FREE:
            WeightedRandom<Peer> weightedRandom = new WeightedRandom<>(res.getNodes(), i->res.getEdges(i).size());
            res.addEdge(0, 1);
            for (int edges = 1; edges < peerCount * degree / 2; edges++) {
                int peer1 = random.nextInt(peerCount);
                int peer2 = weightedRandom.next().getId();
                if (peer1 == peer2 || res.getEdges(peer1).contains(peer2)) {
                    edges--;
                    continue;
                }
                res.addEdge(peer1, peer2);
            }
            break;
        case UNIFORM:
            for (int edges = 0; edges < peerCount * degree / 2; edges++) {
                int peer1 = random.nextInt(peerCount);
                int peer2;
                while ((peer2 = random.nextInt(peerCount)) == peer1
                        || res.getEdges(peer1).contains(peer2))
                    continue;
                res.addEdge(peer1, peer2);
            }
            break;
        }
        return res;
    }

    private static boolean inRange(double bound1, int value, double bound2,
            int range) {
        // TODO something with modulo probably looks better
        return (bound1 <= value && value <= bound2
                || bound1 - range <= value && value <= bound2 - range
                || bound1 + range <= value && value <= bound2 + range);
    }

}
