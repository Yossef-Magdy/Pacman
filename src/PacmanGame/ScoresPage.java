/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PacmanGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javax.swing.*;

/**
 *
 * @author Yossef
 */
public class ScoresPage extends JDialog {
    File score;
    String cols[];
    Object data[][];
    ArrayList<Information> scores;
    public ScoresPage() {
        score = new File("score.txt");
        cols = new String[2];
        cols[0] = "Name";
        cols[1] = "Score";
        scores = new ArrayList<>();
        this.setSize(300, 500);
    }
    public void getScores() throws FileNotFoundException{
        data = null;
        scores.clear();
        int indicator = 0;
        String name = "";
        int scoreValue;
        try (Scanner read = new Scanner(score)) {
            while (read.hasNext()) {
                String value = read.next();
                if(!isNumber(value)) {
                    name += " " + value;
                }
                else {
                    scoreValue = Integer.parseInt(value);
                    scores.add(new Information(name, scoreValue));
                    name = "";
                }
            }
            read.close();
        }
        Collections.sort(scores);
        data = new Object[scores.size()][2];
        for(int i = 0; i < scores.size(); i++) {
            data[i][0] = scores.get(i).getName();
            data[i][1] = scores.get(i).getScore();
        }
        JTable scoreTable = new JTable(data, cols);
        this.add(new JScrollPane(scoreTable));
    }
    private boolean isNumber(String s) {
        int numCount = 0;
        for(int i = 0; i < s.length(); i++) {
            if('0' <= s.charAt(i) && s.charAt(i) <= '9') {
                numCount++;
            }
        }
        return numCount == s.length();
    }
}
