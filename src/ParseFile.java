
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class TickerData{
    String ticker;
    int shares;
    double mv;
    double price;

    TickerData(String ticker, int shares, double mv, double price)
    {
        this.ticker = ticker;
        this.shares = shares;
        this.mv = mv;
        this.price = price;
    }

    public String toString()
    {
        return this.ticker + " " + this.shares + " " + this.mv + " " + this.price;
    }
}

class MyComparator implements Comparator<Object> {

    Map<String, TickerData> map;

    public MyComparator(Map<String, TickerData> map) {
        this.map = map;
    }

    public int compare(Object o1, Object o2) {

        if (map.get(o2) == map.get(o1))
            return 1;
        else

//            return ((TickerData) map.get(o2)).compareTo((TickerData)
//                    map.get(o1));
            if (map.get(o2).mv >= map.get(o1).mv)
                return 1;
            else
                return -1;
    }
}

public class ParseFile {

    public static void main(String[] args) {

        // Creating a HashMap
        Map<String, TickerData >  tickerMapping = new HashMap<>();

        System.out.println("curr_dir" + System.getProperty("user.dir"));

        String csvFile =  System.getProperty("user.dir") + "/src/data1.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            int line_count = -1;
            while ((line = br.readLine()) != null) {

                line_count++;
                // use comma as separator
                String[] ticker_data = line.split(cvsSplitBy);
                System.out.println("ticker = " + ticker_data[0] + " , shares=" + ticker_data[1] + " MV" + ticker_data[2]);

                if (line_count == 0)
                    continue;

                String ticker = ticker_data[0];
                int shares0  = Integer.parseInt(ticker_data[1].trim());
                double mv0   = Double.parseDouble(ticker_data[2].trim());

                TickerData my_ticker_data;
                if(tickerMapping.containsKey(ticker)) {
                    my_ticker_data = tickerMapping.get(ticker);
                    my_ticker_data.shares += shares0;
                    my_ticker_data.mv += mv0;
                }
                else {

                    my_ticker_data = new TickerData(ticker, shares0, mv0, 0.0);

                }
                tickerMapping.put(ticker, my_ticker_data);

            }

            tickerMapping.forEach((ticker, my_data) -> {
                my_data.price = my_data.mv / my_data.shares;
                System.out.println(ticker + " => " + my_data);
            });


            System.out.println("======= Another way to get all data");

            for (String key: tickerMapping.keySet()) {
                System.out.println(key + " => " + tickerMapping.get(key));
            }

            /*** Sorting logic ***/

            MyComparator comparator = new MyComparator(tickerMapping);

            Map<String, TickerData> newMap = new TreeMap<String, TickerData>(comparator);
            newMap.putAll(tickerMapping);


            System.out.println("======= Al results are sorted by mv (desc)");
            System.out.println(newMap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
