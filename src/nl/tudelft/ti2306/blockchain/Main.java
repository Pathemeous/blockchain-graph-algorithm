package nl.tudelft.ti2306.blockchain;

import nl.tudelft.ti2306.blockchain.datastructure.BinaryGraph;
import nl.tudelft.ti2306.blockchain.datastructure.Interaction;
import nl.tudelft.ti2306.blockchain.datastructure.Peer;

/**
 * @author Maarten
 */
public class Main {
    
    /**
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        Peer[] peer = new Peer[10];
        for (int i = 0; i < peer.length; i++) {
            peer[i] = new Peer("Peer" + i);
        }
        BinaryGraph<Interaction> graph =
                new BinaryGraph<>(new Interaction(peer[0], peer[1]));
        graph.getNodes().get(0).addChild(new Interaction(peer[0], peer[2]));
        graph.getNodes().get(0).addChild(new Interaction(peer[1], peer[3]));
        Thread.sleep(50);
        graph.getNodes().get(1).addChild(new Interaction(peer[0], peer[4]));
        graph.getNodes().get(1).addChild(new Interaction(peer[2], peer[1]));
        graph.getNodes().get(2).addChild(new Interaction(peer[1], peer[3]));
        graph.getNodes().get(2).addChild(new Interaction(peer[3], peer[5]));
        Thread.sleep(50);
        graph.getNodes().get(3).addChild(new Interaction(peer[0], peer[6]));
        graph.getNodes().get(3).addChild(new Interaction(peer[4], peer[3]));
        graph.getNodes().get(4).addChild(new Interaction(peer[2], peer[5]));
        graph.getNodes().get(4).addChild(new Interaction(peer[1], peer[7]));
        graph.getNodes().get(5).addChild(new Interaction(peer[1], peer[8]));
        graph.getNodes().get(5).addChild(new Interaction(peer[3], peer[7]));
        graph.getNodes().get(6).addChild(new Interaction(peer[3], peer[8]));
        graph.getNodes().get(6).addChild(new Interaction(peer[5], peer[9]));
        new BinaryGraphToViz(graph, "output.gv").run();
    }

}
