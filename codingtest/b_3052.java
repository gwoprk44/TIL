package codingtest;

import java.io.*;
import java.util.*;

public class b_3052 {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        HashSet<Integer> hs = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            hs.add(Integer.parseInt(br.readLine()) % 42);
        }
        System.out.println(hs.size());
    } 
}