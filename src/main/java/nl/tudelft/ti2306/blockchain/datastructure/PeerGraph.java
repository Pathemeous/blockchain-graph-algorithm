package nl.tudelft.ti2306.blockchain.datastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Interaction binary graph datastructure. 
 * Each node can have at most two parents and two children.
 * @author Maarten
 */
public class PeerGraph {

    protected List<Peer> nodes;
    protected List<List<Integer>> edges;

    /**
     * Constructs a new PeerGraph.
     * @param initialPeers the initial amount of Peers
     */
    public PeerGraph(int initialPeers) {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        for (int i = 0; i < initialPeers; i++) {
            addPeer("Peer" + i);
        }
    }
    
    public void addEdge(int peer1, int peer2) {
        edges.get(peer1).add(peer2);
        edges.get(peer2).add(peer1);
    }
    
    public void addPeer(String name) {
        nodes.add(new Peer(nodes.size(), name));
        edges.add(new ArrayList<>());
    }
    
    public void addPeer() {
        addPeer("Peer" + nodes.size());
    }

    /**
     * @return the nodes
     */
    public List<Peer> getNodes() {
        return Collections.unmodifiableList(nodes);
    }
    
    /**
     * @param peer The Peer to return the edges for.
     * @return The opposite sides of the edges for the peer.
     */
    public List<Integer> getEdges(int peer) {
        return Collections.unmodifiableList(edges.get(peer));
    }
    
    /**
     * @param peer The Peer to return the edges for.
     * @return The opposite sides of the edges for the peer.
     */
    public List<Integer> getEdges(Peer peer) {
        return getEdges(nodes.indexOf(peer));
    }

    public static class EdgeAmountSorter implements Comparator<Peer> {
    
        PeerGraph graph;
        
        /**
         * @param graph PeerGraph to be used for sorting
         */
        public EdgeAmountSorter(PeerGraph graph) {
            super();
            this.graph = graph;
        }
    
        @Override
        public int compare(Peer a, Peer b) {
            return graph.getEdges(((Peer)b).getId()).size() -
                    graph.getEdges(((Peer)a).getId()).size();
        }
    
    }

}
