package nl.tudelft.ti2306.blockchain.datastructure;

import java.util.ArrayList;
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
        ArrayList<Interaction> res = new ArrayList<>(2);
        if (parent[0] != null)
            res.add(parent[0]);
        if (parent[1] != null)
            res.add(parent[1]);
        return res;
    }

    /**
     * @return the children
     */
    public List<Interaction> getChildren() {
        ArrayList<Interaction> res = new ArrayList<>(2);
        if (child[0] != null)
            res.add(child[0]);
        if (child[1] != null)
            res.add(child[1]);
        return res;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(child);
        result = prime * result + Arrays.hashCode(parent);
        result = prime * result + Arrays.hashCode(peer);
        result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Interaction other = (Interaction) obj;
        for (int i = 0; i < 2; i++) {
            if (child[i] == null && other.child[i] != null)
                return false;
            if (child[i] != null) {
                if (other.child[i] == null)
                    return false;
                if (child[i].getTimestamp() != other.child[i].getTimestamp())
                    return false;
                if (!Arrays.equals(child[i].peer, other.child[i].peer))
                    return false;
            }
            if (parent[i] == null && other.parent[i] != null)
                return false;
            if (parent[i] != null) {
                if (other.parent[i] == null)
                    return false;
                if (parent[i].getTimestamp() != other.parent[i].getTimestamp())
                    return false;
                if (!Arrays.equals(parent[i].peer, other.parent[i].peer))
                    return false;
            }
        }
        if (!Arrays.equals(peer, other.peer))
            return false;
        if (timestamp != other.timestamp)
            return false;
        return true;
    }

}