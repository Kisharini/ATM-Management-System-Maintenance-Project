package atm;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestTransactionHistory {
    public static void main(String[] args) {
        String pin = "1234";
        String filename = "transactions_" + pin + ".txt";
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 1; i <= 12; i++) {
                String ts = sdf.format(new Date());
                bw.write("Deposit | " + (100 + i) + " | " + (1000 + i) + " | " + ts);
                bw.newLine();
                try { Thread.sleep(30); } catch (InterruptedException ex) {}
            }
            bw.close();
            System.out.println("Wrote 12 sample transactions to " + filename);
        } catch (IOException e) {
            System.out.println("Failed to write transactions: " + e);
            e.printStackTrace();
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
            br.close();

            int start = Math.max(0, lines.size() - 10);
            System.out.println("\nLast " + Math.min(10, lines.size()) + " transactions:");
            for (int i = start; i < lines.size(); i++) {
                System.out.println(lines.get(i));
            }
        } catch (IOException e) {
            System.out.println("Failed to read transactions: " + e);
            e.printStackTrace();
        }
    }
}
