package nl.tudelft.ti2306.blockchain;

import java.util.ArrayList;
import java.util.Arrays;
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

    /** Returns for each Peer a List of Paths (Lists of Peers) */
    public static Map<Peer, List<List<Peer>>> getAllPaths(PeerGraph pgraph, Peer source, long minTime) {
        int n = pgraph.getNodes().size();
        Map<Peer, List<List<Peer>>> map = new HashMap<>();
        // Initialization of return Map
        for (Peer p : pgraph.getNodes()) {
            map.put(p, new ArrayList<>());
            // If Peer p is connected to `source`
            if (pgraph.getEdges(p.getId()).contains(source.getId())) {
                if (p.getPrevious(source) == null)
                    continue;
                if (p.getPrevious(source).getTimestamp() < minTime)
                    continue;
                // If p and q ever had an interaction and the time is within bounds,
                // Add an initial path from `p` to `source` to the list of paths
                map.get(p).add(new ArrayList<Peer>());
                map.get(p).get(0).add(p);
                map.get(p).get(0).add(source);
            }
        }
        // Dynamic programming commences! Path can be max. `n` long because we do not allow cycles
        for (int i = 0; i < n - 1; i++) {
            // For each Peer `p` in graph
            for (Peer p : pgraph.getNodes()) {
                // For all edges (p, q)
                for (Integer z : pgraph.getEdges(p.getId())) {
                    Peer q = pgraph.getNodes().get(z);
                    if (p.getPrevious(q) == null)
                        continue;
                    if (p.getPrevious(q).getTimestamp() < minTime)
                        continue;
                    // If p and source ever had an interaction and the time is within bounds,
                    // For all currently known paths from q to source
                    for (List<Peer> path : map.get(q)) {
                        // If p is not already in the path from q to source
                        if (!path.contains(p)) {
                            // Add p to the path add the new path to the list
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
     * Only for use if you need ONE SINGLE source-dest, because it calls getAllPaths!
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
    
    public static double[][] getNewAllTrusts(PeerGraph pgraph, long currTime, long minTime) {
        int n = pgraph.getNodes().size();
        double[][] res = new double[n][n];
        Interaction last;
        for (int i = 0; i < n; i++) {
            res[i][i] = 1;
            for (int j : pgraph.getEdges(i)) {
                last = pgraph.getNodes().get(i).getPrevious(pgraph.getNodes().get(j));
                if (last != null) {
                    if (last.getTimestamp() < minTime && last.getTimestamp() != -1)
                        continue;
                    res[i][j] = Math.max(0, 1.0 - (currTime - last.getTimestamp()) / 100.0);  // the weight of the edge (i,j)
                }
            }
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (res[i][j] < res[i][k] * res[k][j]) {
                        res[i][j] = res[i][k] * res[k][j];
                    }
                }
            }
        }
        return res;
    }
    public static double getNewTrust(double[][] trusts, int peer1, int peer2) {
        return trusts[peer1][peer2];
    }
    public static double getNewTrust(PeerGraph pgraph, int peer1, int peer2, long currTime, long minTime) {
        return getNewTrust(getNewAllTrusts(pgraph, currTime, minTime), peer1, peer2);
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

