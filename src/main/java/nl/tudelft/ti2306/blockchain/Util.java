package nl.tudelft.ti2306.blockchain;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import nl.tudelft.ti2306.blockchain.datastructure.Interaction;
import nl.tudelft.ti2306.blockchain.datastructure.Peer;
import nl.tudelft.ti2306.blockchain.datastructure.PeerGraph;

public class Util {

    /**
     * Naive approach finding common ancestor. Will only return first one found.
     * @return null when none found, otherwise the first found (should be lowest) common ancestor
     */
    public static Interaction findCommonAncestor(final Interaction left, final Interaction right) {

        // queue for bfs
        Queue<Interaction> queue = new LinkedList<>();

        // start somewhere
        Interaction current = left;
        queue.add(current);

        while (!queue.isEmpty()) {

            current = queue.poll();

            for (Interaction parent : current.getParents()) {
                if (parent != null) {
                    // Interaction.getParents() kan list met null items geven
                    queue.add(parent);
                }
            }

            if (current.getPeer1().equals(left.getPeer1()) || current.getPeer1().equals(left.getPeer2())) {
                // left is peer1
                if (current.getPeer2().equals(right.getPeer1()) || current.getPeer2().equals(right.getPeer2())) {
                    return current;
                }
            } else {
                // left is peer2
                if (current.getPeer2().equals(right.getPeer1()) || current.getPeer2().equals(right.getPeer2())) {
                    return current;
                }
            }

        }

        // nothing found so far
        return null;
    }

    public static String listToString(Collection<? extends Object> collection) {
        StringBuilder out = new StringBuilder();
        out.append("[");
        for (Object o : collection) {
            out.append(String.format("%s ", o));
        }
        out.append("]");
        return out.toString();
    }

    public static Map<Peer, List<List<Peer>>> getAllPaths(PeerGraph pgraph, Peer source) {
        int n = pgraph.getNodes().size();
        Map<Peer, List<List<Peer>>> map = new HashMap<>();
        for (Peer p : pgraph.getNodes()) {
            map.put(p, new LinkedList<>());
            if (pgraph.getEdges(p.getId()).contains(source.getId())) {
                map.get(p).add(new LinkedList<Peer>());
                map.get(p).get(0).add(p);
                map.get(p).get(0).add(source);
            }
        }
        for (int i = 0; i < n - 1; i++) {
            for (Peer p : pgraph.getNodes()) {
                for (Integer z : pgraph.getEdges(p.getId())) {
                    Peer q = pgraph.getNodes().get(z);
//                    System.out.printf("%d,%d\n", p.getId(), q.getId());
                    for (List<Peer> path : map.get(q)) {
                        if (!path.contains(p)) {
                            List<Peer> newPath = concat(p, path);
                            if (!map.get(p).contains(newPath))
                                map.get(p).add(newPath);
                        }
                    }
                }
            }
        }
        map.remove(source);
        return map;
    }

    private static List<Peer> concat(Peer p, List<Peer> path) {
        LinkedList<Peer> res = new LinkedList<>();
        res.add(p);
        res.addAll(path);
        return res;
    }

    /**
     * Naive approach getting the hop count.
     * When return value is 0, <tt>from</tt> has seen <tt>to</tt> himself, or they are equal.
     */
    public static int getHopCount(final Peer from, final Peer to) {
        return getHopCount(to, from, new HashSet<>());
    }

    private static int getHopCount(final Peer from, final Peer to, final Set<Peer> visited) {

        if (from.seenPeers.contains(to)) {
            return 0;
        }

        int out = -1;

        for (Peer peer : from.seenPeers) {
            if (visited.contains(peer)) {
                continue;
            }
            visited.add(peer);
            int res = getHopCount(peer, to, visited) + 1;
            if (out == -1 || res == -1) {
                out = res;
            } else {
                out = Math.min(res, out);
            }
        }

        return out;
    }

}

