package nl.tudelft.ti2306.blockchain.util;

import java.util.Arrays;

public class Statistics {
    double[] data;
    int size;   

    public Statistics(double[] data) {
        this.data = data;
        size = data.length;
    }   
    
    public void add(double[] addData) {
        for (int i = 0; i < Math.min(data.length, addData.length); i++) {
            data[i] += addData[i];
        }
    }

    public double getMean() {
        double sum = 0.0;
        for(double a : data)
            sum += a;
        return sum/size;
    }

    public double getVariance() {
        double mean = getMean();
        double temp = 0;
        for (double a :data)
            temp += (mean - a) * (mean - a);
        return temp/size;
    }

    public double getStdDev() {
        return Math.sqrt(getVariance());
    }

    public double getMedian() {
       Arrays.sort(data);

       if (data.length % 2 == 0) {
          return (data[(data.length / 2) - 1] + data[data.length / 2]) / 2.0;
       } else {
          return data[data.length / 2];
       }
    }
}