package nl.tudelft.ti2306.blockchain.datastructure;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Data class for storing a Peer.
 * @author Maarten
 */
public class Peer {
    
    private String name;
    private byte[] hash;
    
    public Peer(String name) {
        this.name = name;
        try {
            this.hash = MessageDigest.getInstance("MD5").digest(name.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the hash
     */
    public byte[] getHash() {
        return hash;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hash == null) ? 0 : hash.hashCode());
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
        Peer other = (Peer) obj;
        if (hash == null) {
            if (other.hash != null)
                return false;
        } else if (!hash.equals(other.hash))
            return false;
        return true;
    }

}
