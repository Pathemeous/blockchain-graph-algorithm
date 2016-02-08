package nl.tudelft.ti2306.blockchain.util;

import java.util.List;
import java.util.Random;

public class WeightedRandom<T> {

    private List<T> list;
    private WeightSelector selector;
    private Random random = new Random();
    
    /**
     * @param list
     * @param selector
     */
    public WeightedRandom(List<T> list, WeightSelector selector) {
        super();
        this.list = list;
        this.selector = selector;
    }
    
    public T next() {
        int total = totalWeight();
        int pos = random.nextInt(total);
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            count += selector.select(i);
            if (count > pos) {
                return list.get(i);
            }
        }
        return null; // should not happen (note: NOT thead-safe)
    }
    
    private int totalWeight() {
        int res = 0;
        for (int i = 0; i < list.size(); i++) {
            res += selector.select(i);
        }
        return res;
    }

    public static interface WeightSelector {
        public int select(int i);
    }
    
}
