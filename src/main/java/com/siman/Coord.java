package main.java.com.siman;

/**
 * Created by siman on 1/10/16.
 */
public class Coord {
    public int weapon;
    public int count;
    public int[] coords;

    public Coord(int weapon, int count, int... coords){
        this.weapon = weapon;
        this.count = count;
        this.coords = coords.clone();
    }
}
