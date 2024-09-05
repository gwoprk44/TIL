import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.util.StringTokenizer;
 
public class P14890 {
    static int n,m,r,num;
    static int[][] map;
    static int[][] tmpMap;
 
 
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
 
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        r = Integer.parseInt(st.nextToken());
 
        map = new int[n][m];
 
        for(int i=0; i<n; i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<m; j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
 
 
        st = new StringTokenizer(br.readLine());
        // 케이스 별로 연산의 수만큼 반복 계산
        for(int i=0; i<r; i++){
            //케이스 번호
            num = Integer.parseInt(st.nextToken());
 
            switch(num){
//                연산 : 배열 상하 반전
                case 1:
                    one();
                    break;
//                2번 연산 : 좌우 반전
                case 2:
                    two();
                    break;
//                3번 연산 : 오른쪽 90도 회전
                case 3:
                    three();
                    break;
//                4번 연산 : 왼쪽 90도 회전
                case 4:
                    four();
                    break;
//                5번 연산 : 4그룹 1->2, 2->3, 3->4, 4->1
                case 5:
                    five();
                    break;
//                6번 연산 : 4그룹 1->4, 4->3, 3->2, 2->1
                case 6:
                    six();
                    break;
 
            }
 
        }
        //결과 배열 출력
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                System.out.print(map[i][j]+ " " );
            }
            System.out.println();
        }
    }
 
    /*----------------------------------------------------------*/
    public static void one(){
        //1번 연산 : 배열 상하 반전
        tmpMap = new int[n][m];
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                tmpMap[n-i-1][j] = map[i][j];  //행만 서로 반대
            }
        }
        map = tmpMap;
    }
    public static void two(){
        //2번 연산 : 좌우 반전
        tmpMap = new int[n][m];
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
               tmpMap[i][m-j-1] = map[i][j]; //열만 반대
            }
        }
        map = tmpMap;
    }
    public static void three(){
        //3번 연산 : 오른쪽 90도 회전
        tmpMap = new int[m][n];
 
        for(int i=0; i<n; i++){
 
            for(int j=0; j<m; j++){
                //System.out.println("i, j : " + i + ", " + j);
                //System.out.println(j + ", " + (n-i-1));
                tmpMap[j][n-i-1] =  map[i][j];
            }
        }
        //배열 바꾸기 => 결과 배열 크기 다르므로
        int tmp = n;
        n = m;
        m= tmp;
 
        map = tmpMap;
 
    }
    public static void four(){
        //4번 연산 : 왼쪽 90도 회전
        tmpMap = new int[m][n];
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                tmpMap[m-j-1][i] =  map[i][j];
            }
        }
        //배열 바꾸기
        int tmp = n;
        n = m;
        m= tmp;
 
        map = tmpMap;
 
 
    }
    public static void five(){
        //5번 연산 : 4그룹 1->2, 2->3, 3->4, 4->1
        tmpMap = new int[n][m];
        for(int i=0; i<n/2; i++){
            for(int j=0; j<m/2; j++){
                tmpMap[i][m/2+j] = map[i][j];
 
            }
        }
 
        // 0,4 -> 3,4
        for(int i=0; i<n/2; i++){
            for(int j=m/2; j<m; j++){
                tmpMap[n/2+i][j] = map[i][j];
            }
        }
        // 3,4 -> 3,0
        for(int i=n/2; i<n; i++){
            for(int j=m/2; j<m; j++){
                tmpMap[i][j-m/2] = map[i][j];
            }
        }
        // 3,0 -> 0,0
        for(int i=n/2; i<n; i++){
            for(int j=0; j<m/2; j++){
                tmpMap[i-n/2][j] = map[i][j];
            }
        }
        map = tmpMap;
 
    }
    public static void six(){
        //          4그룹 1->2, 2->3, 3->4, 4->1
        //6번 연산 : 4그룹 1->4, 4->3, 3->2, 2->1
        tmpMap = new int[n][m];
        // 0,0 -> 3,0
        for(int i=0; i<n/2; i++){
            for(int j=0; j<m/2; j++){
                tmpMap[n/2+i][j] = map[i][j];
 
            }
        }
        
 
        // 3,0 -> 3,4
        for(int i=n/2; i<n; i++){
            for(int j=0; j<m/2; j++){
                tmpMap[i][j+m/2] = map[i][j];
            }
        }
        // 3,4 -> 0,4
        for(int i=n/2; i<n; i++){
            for(int j=m/2; j<m; j++){
                tmpMap[i-n/2][j] = map[i][j];
            }
        }
       
 
        // 0,4 -> 0,0
        for(int i=0; i<n/2; i++){
            for(int j=m/2; j<m; j++){
                tmpMap[i][j-m/2] = map[i][j];
            }
        }
        
        map = tmpMap;
 
    }
}