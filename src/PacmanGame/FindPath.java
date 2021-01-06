package PacmanGame;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
/**
 *
 * @author Yossef
 */
public class FindPath {
    private final int grid[][];
    public FindPath(int grid[][]) {
        this.grid = grid;
    }
    public ArrayList<Pair> find(int sourceX, int sourceY, int targetX, int targetY) {
        Queue<Pair> children = new LinkedList<>();
        children.add(new Pair(sourceX, sourceY));
        int n = grid.length;
        int m = grid[0].length;
        int visited[][] = new int[n][m];
        Pair path[][] = new Pair[n][m];
        boolean found = false;
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                path[i][j] = new Pair(-1, -1);
            }
        }
        visited[sourceX][sourceY] = 1;
        while(!children.isEmpty() && !found) {
            Pair cur = children.poll();
            found = getChildren(cur.x, cur.y, children, visited, path, targetX, targetY);
        }
        return getPath(path, targetX, targetY);
    }
    public boolean getChildren(int x, int y, Queue<Pair> children, int visited[][], Pair path[][], int targetX, int targetY) {
        int dx[] = {0, 1, 0, -1};
        int dy[] = {1, 0, -1, 0};
        int n = grid.length;
        int m = grid[0].length;
        for(int i = 0; i < 4; i++) {
            int X = x + dx[i];
            int Y = y + dy[i];
            if(X >= n || Y >= m || X < 0 || Y < 0) {
                continue;
            }
            if(grid[X][Y] == 1) {
                continue;
            }
            if(visited[X][Y] == 1) {
                continue;
            }
            children.add(new Pair(X, Y));
            visited[X][Y] = 1;
            path[X][Y] = new Pair(x, y);
            if(X == targetX && Y == targetY) {
                return true;
            }
        }
        return false;
    }
    public ArrayList<Pair> getPath(Pair path[][], int x, int y) {
        ArrayList<Pair> sequence = new ArrayList<>();
        sequence.add(new Pair(x, y));
        while(path[x][y].x != -1 && path[x][y].y != -1) {
            int newX = path[x][y].x;
            int newY = path[x][y].y;
            x = newX;
            y = newY;
            sequence.add(new Pair(x, y));
        }
        ArrayList<Pair> actualPath = new ArrayList<>();
        for(int i = sequence.size() - 1; i >= 0; i--) {
            actualPath.add(sequence.get(i));
        }
        return actualPath;
    }
}
