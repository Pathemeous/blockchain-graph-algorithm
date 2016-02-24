package nl.tudelft.ti2306.blockchain;

import java.util.ArrayList;
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

    private static List<Peer> concat(Peer p, List<Peer> path) {
        ArrayList<Peer> res = new ArrayList<>();
        res.add(p);
        res.addAll(path);
        return res;
    }

    public static Map<Peer, List<List<Peer>>> getAllPaths(PeerGraph pgraph, Peer source) {
        return getAllPaths(pgraph, source, 0);
    }

    public static Map<Peer, List<List<Peer>>> getAllPaths(PeerGraph pgraph, Peer source, long minTime) {
        int n = pgraph.getNodes().size();
        Map<Peer, List<List<Peer>>> map = new HashMap<>();
        for (Peer p : pgraph.getNodes()) {
            map.put(p, new ArrayList<>());
            if (pgraph.getEdges(p.getId()).contains(source.getId())) {
                map.get(p).add(new ArrayList<Peer>());
                map.get(p).get(0).add(p);
                map.get(p).get(0).add(source);
            }
        }
        for (int i = 0; i < n - 1; i++) {
            for (Peer p : pgraph.getNodes()) {
                for (Integer z : pgraph.getEdges(p.getId())) {
                    Peer q = pgraph.getNodes().get(z);
                    if (p.getPrevious(q) == null)
                        continue;
                    if (p.getPrevious(q).getTimestamp() < minTime)
                        continue;
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
    
    /**
     * Only for use if you need ONE SINGLE source-dest, calls getAllPaths!
     */
    public static double calculateTrust(PeerGraph pgraph, Peer source, Peer dest, long currTime) {
        return calculateTrust(pgraph, source, getAllPaths(pgraph, source).get(dest), currTime);
    }
    
    public static double calculateTrust(PeerGraph pgraph, Peer source, List<List<Peer>> allPathsToDest, long currTime) {
        double failChance = 1.0;
        double pathSucceed, edgeSucceed;
        Peer p, q;
        Interaction last;
        for (List<Peer> path : allPathsToDest) {
            pathSucceed = 1.0;
            for (int i = 0; i < path.size() - 1; i++) {
                p = path.get(i);
                q = path.get(i + 1);
                last = p.getPrevious(q);
                if (last == null) {
                    pathSucceed = 0;
                    break;
                }
                edgeSucceed = 1.0 - (currTime - last.getTimestamp()) / 100.0;
                pathSucceed *= Math.max(0, edgeSucceed);
            }
            failChance *= 1.0 - pathSucceed;
        }
        return 1.0 - failChance;
    }

    /**
     * Naive approach getting the hop count.
     * When return value is 0, <tt>from</tt> has seen <tt>to</tt> himself, or they are equal.
     */
    public static int getHopCount(final Peer from, final Peer to) {
        return getHopCount(to, from, new HashSet<>());
    }

    private static int getHopCount(final Peer from, final Peer to, final Set<Peer> visited) {

        if (from.seenPeers.keySet().contains(to)) {
            return 0;
        }

        int out = -1;

        for (Peer peer : from.seenPeers.keySet()) {
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

