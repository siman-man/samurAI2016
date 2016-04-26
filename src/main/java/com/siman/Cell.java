package main.java.com.siman;

/**
 * Created by siman on 1/16/16.
 */
public class Cell {
    public int owner;
    public int lastOwner;
    public int update_at;
    public boolean existHome;
    public boolean warning;
    public boolean killZone;
    public boolean changed;
    public boolean enemyView;
    public boolean enemyWeakPoint;
    public int value;
    public int dangerValue;
    public int attackId;

    public Cell(){
        this.owner = Field.NEUTRAL;
        this.update_at = 0;
        this.existHome = false;
        this.warning = false;
        this.killZone = false;
        this.dangerValue = 0;
        this.changed = false;
        this.enemyView = false;
        this.value = 0;
        this.attackId = -1;
    }

    public void update(int owner, int turn){
        this.owner = owner;
        this.lastOwner = owner;
        this.update_at = turn;
    }

    public boolean isHome(){
        return this.existHome;
    }

    public void clear(){
        this.killZone = false;
        this.enemyView = false;
        this.changed = false;
        this.warning = false;
        this.enemyWeakPoint = false;
        this.dangerValue = 0;
    }
}
