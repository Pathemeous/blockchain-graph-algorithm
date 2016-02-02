package nl.tudelft.ti2306.blockchain.datastructure;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class InteractionGraphTest {

    @Before
    public void setUp() throws Exception {}

    @Test
    public final void test() {
        InteractionGraph g = new InteractionGraph(3);
        assertEquals(3, g.getPeers().size());
        g.addInteraction(0, 1);
        Interaction root = g.getRoot();
        assertEquals(root.getPeer1(), g.getPeers().get(0));
        
        g.addInteraction(0, 2);
        assertEquals(g.getPeers().get(0), g.getPeers().get(2).getPrevious().getPeer1());
    }
    
    @Test
    public final void testEquals() {/*
        Node<String> root = new Node<String>("Root");
        assertTrue(root.equals(new Node<String>("Root")));
        assertFalse(root.equals(new InteractionGraph<String>("Root")));
        assertFalse(root.equals(new Node<Integer>(42)));
        assertFalse(root.equals(new Node<String>("Carrot")));*/
    }

}
