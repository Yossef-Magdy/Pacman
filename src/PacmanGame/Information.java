/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PacmanGame;

/**
 *
 * @author Yossef
 */
public class Information implements Comparable<Information>{
    private String name;
    private int score;
    public Information(String name, int score) {
        this.name = name;
        this.score = score;
    }
    public String getName() {
        return this.name;
    }
    public int getScore() {
        return this.score;
    }
    @Override
    public int compareTo(Information o) {
        return o.score - score;
    }
    
}
