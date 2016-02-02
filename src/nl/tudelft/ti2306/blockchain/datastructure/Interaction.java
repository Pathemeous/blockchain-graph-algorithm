package nl.tudelft.ti2306.blockchain.datastructure;

/**
 * Data class for storing 
 * @author Maarten
 */
public class Interaction {

    private Peer peer1;
    private Peer peer2;
    private long timestamp;
    
    public Interaction(Peer peer1, Peer peer2, long timestamp) {
        this.peer1 = peer1;
        this.peer2 = peer2;
        this.timestamp = timestamp;
    }
    
    public Interaction(Peer peer1, Peer peer2) {
        this(peer1, peer2, System.currentTimeMillis());
    }

    /**
     * @return the peer1
     */
    public Peer getPeer1() {
        return peer1;
    }

    /**
     * @return the peer2
     */
    public Peer getPeer2() {
        return peer2;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((peer1 == null) ? 0 : peer1.hashCode());
        result = prime * result + ((peer2 == null) ? 0 : peer2.hashCode());
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
        if (peer1 == null) {
            if (other.peer1 != null)
                return false;
        } else if (!peer1.equals(other.peer1))
            return false;
        if (peer2 == null) {
            if (other.peer2 != null)
                return false;
        } else if (!peer2.equals(other.peer2))
            return false;
        if (timestamp != other.timestamp)
            return false;
        return true;
    }
    
}
