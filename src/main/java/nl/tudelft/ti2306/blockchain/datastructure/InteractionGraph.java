package nl.tudelft.ti2306.blockchain.datastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Interaction binary graph datastructure. 
 * Each node can have at most two parents and two children.
 * @author Maarten
 */
public class InteractionGraph {

    protected Interaction root;
    protected List<Interaction> nodes;
    protected List<Peer> peers;

    /**
     * Constructs a new InteractionGraph with peers.
     * @param initialPeers the initial amount of Peers
     */
    public InteractionGraph(int initialPeers) {
        nodes = new ArrayList<>();
        peers = new ArrayList<>(initialPeers);
        for (int i = 0; i < initialPeers; i++) {
            addPeer("Peer" + i);
        }
    }
    
    public InteractionGraph(List<Peer> peers) {
        this.nodes = new ArrayList<>();
        this.peers = peers;
    }

    public void addInteraction(int peer1, int peer2, long timestamp) {
        Interaction ia = new Interaction(peers.get(peer1), peers.get(peer2), timestamp);
        if (root == null) {
            root = ia;
        }
        ia.graph = this;
        nodes.add(ia);
        peers.get(peer1).setPrevious(ia);
        peers.get(peer2).setPrevious(ia);
    }
    
    public void addInteraction(int peer1, int peer2) {
        addInteraction(peer1, peer2, System.currentTimeMillis());
    }
    
    public void addPeer() {
        peers.add(new Peer(peers.size(), "Peer" + peers.size()));
    }
    
    public void addPeer(String name) {
        peers.add(new Peer(peers.size(), name));
    }

    /**
     * @return the root
     */
    public Interaction getRoot() {
        return root;
    }
    
    /**
     * @return the nodes
     */
    public List<Interaction> getNodes() {
        return Collections.unmodifiableList(nodes);
    }
    
    /**
     * @return the peers
     */
    public List<Peer> getPeers() {
        return Collections.unmodifiableList(peers);
    }

}
