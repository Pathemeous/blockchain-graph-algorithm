package nl.tudelft.ti2306.blockchain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

        int peerCnt = 10;
        int graphDegree = 2;
        int method = PeerGraphGenerator.SCALE_FREE;
        double param = 0.1;
        int interactionCnt = 30;
        int experimentCnt = 50;
        int fileSize = 100;

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

//        experiment(experimentCnt, fileSize, 
//                peerCnt, method, interactionCnt, graphDegree, param);
        System.out.println("Experiment Done");
    }

    private static void experiment(int expermentCnt, double fileSize,
            int peerCnt, int method, int interactionCnt, int graphDegree, double param) {
        double[][] calculateTime = new double[100][expermentCnt];
        double[][] downloadTime = new double[100][expermentCnt];
        for (int i = 0; i < expermentCnt; i++) {
            PeerGraph pgraph = PeerGraphGenerator.generate(
                    peerCnt, method, graphDegree, param);
            InteractionGraph igraph = InteractionGraphGenerator.generate(
                    pgraph, interactionCnt);
            for (int j = 0; j < 100; j++) {
                long start = System.currentTimeMillis();
                downloadTime[j][i] = fileSize / calculate(pgraph, igraph);
                calculateTime[j][i] = (System.currentTimeMillis() - start) / 1000.0;
            }
        }
        try (PrintWriter out = new PrintWriter(new File("output.data"))) {
            out.println("type\tsize\ttime\tstd");
            Statistics stat;
            for (int j = 0; j < 5; j++) {
                stat = new Statistics(calculateTime[j]);
                out.print("calc\t");
                out.print(j + "\t");
                out.print(stat.getMean() + "\t");
                out.println(stat.getStdDev() + "\t");
                stat = new Statistics(downloadTime[j]);
                out.print("down\t");
                out.print(j + "\t");
                out.print(stat.getMean() + "\t");
                out.println(stat.getStdDev() + "\t");
                stat.add(calculateTime[j]);
                out.print("total\t");
                out.print(j + "\t");
                out.print(stat.getMean() + "\t");
                out.println(stat.getStdDev() + "\t");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static double calculate(PeerGraph pgraph, InteractionGraph igraph) {
        double maxSpeed = 0, spd;
        for (int i = 0; i < pgraph.getNodes().size(); i++) {
            spd = pgraph.getNodes().get(i).getUploadSpeed();
            spd /= (1 + Util.getHopCount(pgraph.getNodes().get(0), pgraph.getNodes().get(i)));
            maxSpeed = spd > maxSpeed ? spd : maxSpeed;
        }
        return maxSpeed;
    }



    private static void generate(int peerCnt, int method, int interactionCnt, int graphDegree, double param, boolean print) {
        PeerGraph pgraph = PeerGraphGenerator.generate(
                peerCnt, method, graphDegree, param);
        new PeerGraphToViz(pgraph, "outputPeer.gv").run();

        InteractionGraph igraph = InteractionGraphGenerator.generate(
                pgraph, interactionCnt);
        new InteractionGraphToViz(igraph, "output.gv").run();

        System.out.println("Generated graph");
        Map<Peer, List<List<Peer>>> map = Util.getAllPaths(pgraph, pgraph.getNodes().get(0));
//        for (Entry<Peer, List<List<Peer>>> e : map.entrySet()) {
//            System.out.printf("======= %d =======\n", e.getKey().getId());
//            for (List<Peer> path : e.getValue()) {
//                System.out.println(path);
//            }
//        }
        System.out.println(Util.calculateTrust(pgraph, pgraph.getNodes().get(0), map.get(pgraph.getNodes().get(peerCnt - 1)), interactionCnt));
    
        if (!print) return;

        List<Peer> list = new ArrayList<>(pgraph.getNodes());
        Collections.sort(list, pgraph.new EdgeAmountSorter());
        for (Peer p : list) {
            System.out.println(p.getId() + "\t" + pgraph.getEdges(p.getId()).size() + " edges");
        }
    }

}
