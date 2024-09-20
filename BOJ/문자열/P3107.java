package 문자열;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class P3107 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        String ipv6 = br.readLine();
        if (ipv6.contains("::")) {
            ipv6 = ipv6.replace("::", ":group:");
        }

        List<String> ipv6s = Stream.of(ipv6.split(":")).collect(Collectors.toList());
        List<String> fullIPs = new ArrayList<>();

        for (int i = 0; i < ipv6s.size(); i++) {
            String s = ipv6s.get(i);
            if (s.isEmpty()) continue;
            while (s.length() < 4) {
                s = "0" + s;
            }
            fullIPs.add(s);
        }

        int groupLen = 8 - fullIPs.size() + 1;
        String[] ipFullAddress = new String[8];
        int idx = 0;
        for (String ip : fullIPs) {
            if(ip.equals("group")) {
                while (groupLen --> 0) {
                    ipFullAddress[idx++] = "0000";
                }
            } else {
                ipFullAddress[idx++] = ip;
            }
        }
        System.out.println(String.join(":", ipFullAddress));
    }
}
