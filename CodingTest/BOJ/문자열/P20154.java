package BOJ.문자열;
import java.util.*;
import java.io.*;

public class P20154 {
	static int n, result;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	String str = br.readLine();

    	int[] alpha = {3, 2, 1,	2, 3, 3, 3, 3, 1, 1, 3, 1, 3, 3, 1, 2, 2, 2, 1, 2, 1, 1, 2, 2, 2, 1};

    	Deque<Integer> deque = new ArrayDeque<Integer>();
    	for(int i = 0; i < str.length(); i++) {
    		deque.add(alpha[str.charAt(i) - 'A']);
    	}

    	int size = deque.size();
    	int a, b;
    	while(true) {
    		if(size == 1)
    			break;

    		for(int i = 0; i < size; i += 2) {
    			if(i == size - 1) {		// 짝이 지어지지 않은 수
    				deque.addLast(deque.pollFirst());
    				break;
    			}
    			a = deque.pollFirst();
    			b = deque.pollFirst();
    			deque.addLast((a + b) % 10);
    		}
    		size = deque.size();
    	}
    	result = deque.poll();

    	if(result % 2 == 1)
    		System.out.println("I'm a winner!");
    	else
    		System.out.println("You're the winner?");
    	br.close();
	}


	static int stoi(String str) {
    	return Integer.parseInt(str);
    }
}
