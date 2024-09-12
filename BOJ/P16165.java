import java.io.*;
import java.util.*;

public class P16165 {
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        HashMap<String, List<String>> map = new HashMap<>();

        for (int i = 0; i < N; i++) {
            String group = br.readLine(); // 그룹 이름
            int num = Integer.parseInt(br.readLine()); // 그룹 인원 수
            List<String> mem = new ArrayList<>(); // 그룹 멤버 이름
            for (int j = 0; j < num; j++) {
                mem.add(br.readLine());
            }
            map.put(group, mem); // 해시에 추가
        }

        for (int i = 0; i < M; i++) {
            String name = br.readLine();
            int quiz = Integer.parseInt(br.readLine());

            if (quiz == 0) { // 해당팀에 속한 벰버의 이름 사전순으로 출력
                List<String> mem = map.get(name);
                Collections.sort(mem);
                for (String list : mem) {
                    sb.append(list).append("\n");
                }
            } else {    // 해당 멤버가 속한 팀의 이름 출력
                for (String group : map.keySet()) {
                    if (map.get(group).contains(name)) {
                        sb.append(group).append("\n");
                        break;
                    }
                }
            }
        }
        System.out.println(sb);
    }
}
