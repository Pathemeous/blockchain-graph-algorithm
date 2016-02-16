package nl.tudelft.ti2306.blockchain.datastructure;

import java.util.Arrays;
import java.util.List;

/**
 * Data class for storing an interaction.
 * @author Maarten
 */
public class Interaction {

    InteractionGraph graph;
    private Interaction[] parent;
    private Interaction[] child;
    private Peer[] peer;
    private long timestamp;

    Interaction(Peer peer1, Peer peer2, long timestamp) {
        parent = new Interaction[2];
        child = new Interaction[2];
        peer = new Peer[2];
        peer[0] = peer1;
        peer[1] = peer2;
        for (int i = 0; i < 2; i++) {
            if (peer[i].getPrevious() != null) {
                parent[i] = peer[i].getPrevious();
                peer[i].getPrevious().setChildOf(peer[i], this);
            }
        }
        this.timestamp = timestamp;
    }

    public Interaction(Peer peer1, Peer peer2) {
        this(peer1, peer2, System.currentTimeMillis());
    }

    /**
     * @return the peer1
     */
    public Peer getPeer1() {
        return peer[0];
    }

    /**
     * @return the peer2
     */
    public Peer getPeer2() {
        return peer[1];
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    public Interaction getParentOf(Peer p) {
        return parent[p.equals(peer[0]) ? 0 : 1];
    }

    public Interaction getChildOf(Peer p) {
        return child[p.equals(peer[0]) ? 0 : 1];
    }

    public Interaction setChildOf(Peer p, Interaction i) {
        return child[p.equals(peer[0]) ? 0 : 1] = i;
    }

    /**
     * @return the parents
     */
    public List<Interaction> getParents() {
        return Arrays.asList(parent);
    }

    /**
     * @return the children
     */
    public List<Interaction> getChildren() {
        return Arrays.asList(child);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Interaction [" + Arrays.toString(peer) + ", "
                + timestamp + "]";
    }

}