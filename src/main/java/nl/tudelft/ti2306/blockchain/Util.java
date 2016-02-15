package nl.tudelft.ti2306.blockchain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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

    private static String ListToString(Collection collection) {
        StringBuilder out = new StringBuilder();
        out.append("[");
        for (Object o : collection) {
            out.append(String.format("%s ", o));
        }
        out.append("]");
        return out.toString();
    }

    public static List<List<Peer>> getAllPaths(Peer source, Peer destination, PeerGraph pgraph) {
        List<List<Peer>> paths = new ArrayList<>();
        recursive(source, destination, paths, new LinkedHashSet<>(), pgraph);
        return paths;
    }

    private static void recursive (Peer current, Peer destination, List<List<Peer>> paths, LinkedHashSet<Peer> path, PeerGraph pgraph) {

        System.out.println("Current path: " + ListToString(path));

        path.add(current);

        if (current == destination) {
            System.out.println("Found destination!");
            paths.add(new ArrayList<>(path));
            path.remove(current);
            return;
        }

        final Collection<Integer> edges = pgraph.getEdges(current);

        for (Integer i : edges) {
            if (edges == current) continue;

            Peer p = pgraph.getNodes().get(i);

            if (!path.contains(p)) {
                recursive (p, destination, paths, path, pgraph);
            }
        }

        path.remove(current);
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

