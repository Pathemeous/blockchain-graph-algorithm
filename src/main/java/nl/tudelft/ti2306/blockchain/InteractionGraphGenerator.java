package nl.tudelft.ti2306.blockchain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import nl.tudelft.ti2306.blockchain.datastructure.InteractionGraph;
import nl.tudelft.ti2306.blockchain.datastructure.PeerGraph;

/**
 * Created by justin on 02/02/16.
 */
public class InteractionGraphGenerator {
    
    private static Random random = new Random();
    private static Map<Integer, Set<Integer>> maxSpeeds = new HashMap<>();
    
    public static InteractionGraph generate(PeerGraph pgraph, int interactionCnt) {
        initMaxSpeeds(pgraph);
        int n = pgraph.getNodes().size();
        InteractionGraph res = new InteractionGraph(pgraph.getNodes());
        int interactionsCreated = 0;
        while (interactionsCreated < interactionCnt) {
            int peer1 = random.nextInt(n);
            if (pgraph.getEdges(peer1).size() < 1) continue;
            // int peer2 = pgraph.getEdges(peer1).get(random.nextInt(pgraph.getEdges(peer1).size()));
            int peer2 = getPeerBittorrent(pgraph, peer1);
            res.addInteraction(peer1, peer2, interactionsCreated);
            interactionsCreated++;
        }
        return res;
    }

    private static int getPeerBittorrent(PeerGraph pgraph, int peer1) {
        // Choose one of the four peers with maximum speeds
        if (random.nextDouble() < 0.8) {
            return new ArrayList<Integer>(maxSpeeds.get(peer1)).get(random.nextInt(maxSpeeds.get(peer1).size()));
        } else {
            return pgraph.getEdges(peer1).get(random.nextInt(pgraph.getEdges(peer1).size()));
        }
    }
    
    private static void initMaxSpeeds(PeerGraph pgraph) {
        for (int p1 = 0; p1 < pgraph.getNodes().size(); p1++) {
            List<Integer> edges = pgraph.getEdges(p1);
            Set<Integer> set = new HashSet<>();
            set.addAll(edges);
            while (set.size() > 4) {
                Integer min = -1;
                for (Integer i : set) {
                    if (min == -1) min = i;
                    if (pgraph.getNodes().get(i).getUploadSpeed() < pgraph.getNodes().get(min).getUploadSpeed())
                        min = i;
                }
                set.remove(min);
            }
            maxSpeeds.put(p1, set);
        }
    }

}
