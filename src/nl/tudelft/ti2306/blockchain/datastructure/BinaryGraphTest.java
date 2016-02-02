package nl.tudelft.ti2306.blockchain.datastructure;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.ti2306.blockchain.datastructure.BinaryGraph.Node;

public class BinaryGraphTest {

    @Before
    public void setUp() throws Exception {}

    @Test
    public final void test() {
        BinaryGraph<String> g = new BinaryGraph<>("RootNode");
        Node<String> root = g.getRoot();
        assertEquals("RootNode", root.getData());

        assertTrue(root.addChild("Child1"));
        assertTrue(root.addChild("Child2"));
        assertFalse(root.addChild("Child3"));
        
        Node<String> n1 = root.getChildren().get(0);
        assertEquals(root, n1.getParents().get(0));

        assertTrue(n1.addParent("Parent1"));
        assertFalse(n1.addParent("Parent2"));
        
        Node<String> p1 = n1.getParents().get(1);
        assertEquals(n1, p1.getChildren().get(0));
    }
    
    @Test
    public final void testEquals() {
        Node<String> root = new Node<String>("Root");
        assertTrue(root.equals(new Node<String>("Root")));
        assertFalse(root.equals(new BinaryGraph<String>("Root")));
        assertFalse(root.equals(new Node<Integer>(42)));
        assertFalse(root.equals(new Node<String>("Carrot")));
    }

}
