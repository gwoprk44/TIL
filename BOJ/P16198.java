import java.io.*;
import java.util.*;

public class P16198 {
    static int N;
    static int MAX = Integer.MIN_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        List<Integer> list = new ArrayList<>();


        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            list.add(Integer.parseInt(st.nextToken()));
        }

        dfs(list, 0);
        System.out.println(MAX);
    }

    public static void dfs(List<Integer> list, int sum) {
        if (list.size() == 2) {
            MAX = Math.max(MAX, sum);
            return;
        }

        for (int i = 1; i < list.size() - 1; i++) {
            int tmp = list.get(i);
            int energy = list.get(i - 1) * list.get(i + 1);
            list.remove(i);
            dfs(list, sum + energy);
            list.add(i, tmp);
        }
    }
}
