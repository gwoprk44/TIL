package CodingTest.BOJ.기하;

import java.io.*;
import java.util.*;


public class P3009 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        int[] coord1 = {Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())};
        st = new StringTokenizer(br.readLine());
        int[] coord2 = {Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())};
        st = new StringTokenizer(br.readLine());
        int[] coord3 = {Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())};
        
        int x, y;

        if (coord1[0] == coord2[0]) {
            x = coord3[0];
        } else if (coord1[0] == coord3[0]) {
            x = coord2[0];
        } else {
            x = coord1[0];
        }


        if (coord1[1] == coord2[1]) {
            y = coord3[1];
        } else if (coord1[1] == coord3[1]) {
            y = coord2[1];
        } else {
            y = coord1[1];
        }

        System.out.println(x + " " + y);
    }
}
