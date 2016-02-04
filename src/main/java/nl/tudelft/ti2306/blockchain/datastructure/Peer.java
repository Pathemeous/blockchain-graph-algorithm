package nl.tudelft.ti2306.blockchain.datastructure;

/**
 * Data class for storing a Peer.
 * @author Maarten
 */
public class Peer {
    
    private int id;
    private String name;
    private double uploadSpeed;
    private Interaction previous;
    
    Peer(int id, String name, double uploadSpeed) {
        this.id = id;
        this.name = name;
        this.uploadSpeed = uploadSpeed;
    }
    
    Peer(int id, String name) {
        this(id, name, 42);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the uploadSpeed
     */
    public double getUploadSpeed() {
        return uploadSpeed;
    }

    /**
     * @param uploadSpeed the uploadSpeed to set
     */
    public void setUploadSpeed(double uploadSpeed) {
        this.uploadSpeed = uploadSpeed;
    }

    /**
     * @param previous the previous to set
     */
    public void setPrevious(Interaction previous) {
        this.previous = previous;
    }

    /**
     * @return the previous
     */
    public Interaction getPrevious() {
        return previous;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
