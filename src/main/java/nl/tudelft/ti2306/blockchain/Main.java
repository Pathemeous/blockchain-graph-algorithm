package nl.tudelft.ti2306.blockchain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import nl.tudelft.ti2306.blockchain.datastructure.InteractionGraph;
import nl.tudelft.ti2306.blockchain.datastructure.Peer;
import nl.tudelft.ti2306.blockchain.datastructure.PeerGraph;
import nl.tudelft.ti2306.blockchain.util.Statistics;

/**
 * @author Maarten
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String ... args) {

        int peerCnt = 200; // Amount of peers
        int graphDegree = 10; // Average graph degree of peer-graph
        int method = PeerGraphGenerator.SCALE_FREE; // Method of generating peer-graph
        double param = 0.1; // For SMALL_WORLD: chance of an edge "crossing the circle"
        int interactionCnt = 1000; // Amount of interactions
        int experimentCnt = 100; // Amount of experiments
        int fileSize = 100; // Payload of the file

        try {
            switch (args.length) {
            case 7:
                fileSize       = Integer.valueOf(args[6]);
            case 6:
                experimentCnt  = Integer.valueOf(args[5]);
            case 5:
                interactionCnt = Integer.valueOf(args[4]);
            case 4:
                param          = Double.valueOf(args[3]);
            case 3:
                method         = Integer.valueOf(args[2]);
            case 2:
                graphDegree    = Integer.valueOf(args[1]);
            case 1:
                peerCnt        = Integer.valueOf(args[0]);
                break;
            }
        } catch (Exception e) { }
        System.out.printf("peerCnt=%d graphDegree=%d method=%d param=%f interactionCnt=%d experimentCnt=%d fileSize=%d \n",
                           peerCnt,   graphDegree,   method,   param,   interactionCnt,   experimentCnt,   fileSize);
        System.out.println("Methods: 0=SMALL_WORLD 1=SCALE_FREE 2=UNIFORM");

        generate(peerCnt, method, interactionCnt, graphDegree, param, false);

        experiment(experimentCnt, fileSize, 
                peerCnt, method, interactionCnt, graphDegree, param);
        System.out.println("Experiment Done");
    }

    /** For science! */
    private static void experiment(int experimentCnt, double fileSize,
            int peerCnt, int method, int interactionCnt, int graphDegree, double param) {
        final int trials = 100;
        double[][] calculateTime = new double[experimentCnt][trials];
        double[][] downloadTime = new double[experimentCnt][trials];
        for (int i = -2; i < experimentCnt; i++) {
            if (i == -2)
                System.out.print("Warming up ");
            else if (i >= 0)
                System.out.print("Experiment " + (i+1) + " out of " + experimentCnt + " ");
            // Each experiment (different history size) is conducted 100 times, averages are calculated
            for (int j = 0; j < trials; j++) {
                // First generate PeerGraph and InteractionGraph
                PeerGraph pgraph = PeerGraphGenerator.generate(
                        peerCnt, method, graphDegree, param);
                InteractionGraph igraph = InteractionGraphGenerator.generate(
                        pgraph, interactionCnt);
                // start timing
                long start = System.currentTimeMillis();
                // determine download speed based on trust
                double speed = calculate(pgraph, igraph, interactionCnt, interactionCnt * i / experimentCnt);
                // end timing and save it.
                if (i >= 0) calculateTime[i][j] = (System.currentTimeMillis() - start) / 1000.0;
                // calculate download time based on fileSize and download speed
                if (i >= 0) downloadTime[i][j] = fileSize / speed;
                // Dots in the terminal indicate the amount of trials done
                System.out.print(".");
            }
            System.out.println();
        }
        try (PrintWriter out = new PrintWriter(new File("output.data"))) {
            out.println("type\tsize\ttime\tstd");
            Statistics stat;
            // Print average and stddev for calculation, download and total times for all experiments
            for (int i = 0; i < experimentCnt; i++) {
                stat = new Statistics(calculateTime[i]);
                out.print("calc\t");
                out.print(i + "\t");
                out.print(stat.getMean() + "\t");
                out.println(stat.getStdDev() + "\t");
                stat = new Statistics(downloadTime[i]);
                out.print("down\t");
                out.print(i + "\t");
                out.print(stat.getMean() + "\t");
                out.println(stat.getStdDev() + "\t");
                stat.add(calculateTime[i]);
                out.print("total\t");
                out.print(i + "\t");
                out.print(stat.getMean() + "\t");
                out.println(stat.getStdDev() + "\t");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** Calculates the maximum possible download speed from Peer 0 to any other Peer */
    private static double calculate(PeerGraph pgraph, InteractionGraph igraph, long currTime, long minTime) {
        double maxSpeed = 0, spd;

        double[][] trusts = Util.getNewAllTrusts(pgraph, igraph.getNodes().size(), minTime);
        for (int i = 1; i < pgraph.getNodes().size(); i++) {
            if (Util.getNewTrust(trusts, 0, i) > 0.9) {
                spd = pgraph.getNodes().get(i).getUploadSpeed();
                maxSpeed = spd > maxSpeed ? spd : maxSpeed;
            }
        }
//        Map<Peer, List<List<Peer>>> allPaths = Util.getAllPaths(pgraph, pgraph.getNodes().get(0), minTime);
//        
//        // From all peers for which the trust > 0.9, get max download speed
//        for (int i = 1; i < pgraph.getNodes().size(); i++) {
//            if (0.9 < Util.calculateTrust(pgraph, pgraph.getNodes().get(0), allPaths.get(pgraph.getNodes().get(i)), currTime)) {
//                spd = pgraph.getNodes().get(i).getUploadSpeed();
//                maxSpeed = spd > maxSpeed ? spd : maxSpeed;
//            }
//        }
        // If no one is found, get best download speed from seenPeers
        if (maxSpeed == 0) {
            for (Peer other : pgraph.getNodes().get(0).seenPeers.keySet()) {
                spd = other.getUploadSpeed();
                maxSpeed = spd > maxSpeed ? spd : maxSpeed;
            }
        }
        // If no one is found, get a random peer from somewhere
        if (maxSpeed == 0) {
            maxSpeed = pgraph.getNodes().get(new Random().nextInt(pgraph.getNodes().size())).getUploadSpeed();
        }
        
        return maxSpeed;
    }


    /** Generates only one time a peer and a interaction graph and outputs .gv files and stuff */
    private static void generate(int peerCnt, int method, int interactionCnt, int graphDegree, double param, boolean print) {
        PeerGraph pgraph = PeerGraphGenerator.generate(
                peerCnt, method, graphDegree, param);

        InteractionGraph igraph = InteractionGraphGenerator.generate(
                pgraph, interactionCnt);
        
        new PeerGraphToViz(pgraph, "outputPeer.gv").run();
        new InteractionGraphToViz(igraph, "output.gv").run();

        System.out.println("Generated graph");
        if (!print) return;

//        Map<Peer, List<List<Peer>>> map = Util.getAllPaths(pgraph, pgraph.getNodes().get(0), 0);//interactionCnt * 4 / 5);
//        for (Entry<Peer, List<List<Peer>>> e : map.entrySet()) {
//            System.out.printf("======= %d =======\n", e.getKey().getId());
//            for (List<Peer> path : e.getValue()) {
//                System.out.println(path);
//            }
//        }
//        System.out.println(Util.calculateTrust(pgraph, pgraph.getNodes().get(0), map.get(pgraph.getNodes().get(peerCnt - 1)), interactionCnt));

        double[][] trusts = Util.getNewAllTrusts(pgraph, interactionCnt, 0);
        for (int i = 0; i < peerCnt; i++) {
            for (int j = 0; j < peerCnt; j++)
                System.out.printf("%.6f  ", trusts[i][j]);
            System.out.println();
        }

        List<Peer> list = new ArrayList<>(pgraph.getNodes());
        Collections.sort(list, pgraph.new EdgeAmountSorter());
        for (Peer p : list) {
            System.out.println(p.getId() + "\t" + pgraph.getEdges(p.getId()).size() + " edges");
        }
    }

}
