#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    map<string, double> gradetable;
    gradetable["A+"] = 4.5;
    gradetable["A0"] = 4.0;
    gradetable["B+"] = 3.5;
    gradetable["B0"] = 3.0;
    gradetable["C+"] = 2.5;
    gradetable["C0"] = 2.0;
    gradetable["D+"] = 1.5;
    gradetable["D0"] = 1.0;
    gradetable["F"] = 0.0;
    
    string subject;
    double sum = 0;
    double credit;
    double creditsum = 0;
    string grade;

    for (int i = 0; i < 20; i++) {
        cin >> subject >> credit >> grade;
        
        if (grade != "P") {
            creditsum += credit;
            sum += credit * gradetable[grade];
        }
    }
    cout << sum / creditsum;
}