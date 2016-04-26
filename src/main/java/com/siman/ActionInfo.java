package main.java.com.siman;

import java.util.List;

/**
 * Created by siman on 1/3/16.
 *
 * action information
 */
public class ActionInfo {
    /**
     * occupy value
     */
    public int occupyValue;

    /**
     * occupy enemy value
     */
    public int occupyEnemyValue;

    /**
     * killing value
     */
    public int killValue;

    /**
     * danger value
     */
    private int dangerValue;

    /**
     * distance of team homes.
     */
    private int teamHomeDist;

    /**
     * action is valid or not
     */
    private boolean valid;

    /**
     * whether attack
     */
    public boolean isAttack;

    /**
     * distance of target point
     */
    public int targetDist;

    /**
     * distance of each player
     */
    public int[] playerDist;

    /**
     * cost
     */
    public int cost;

    /**
     * Action list
     */
    List<Integer> actions;

    public ActionInfo(){
        this.occupyValue = 0;
        this.occupyEnemyValue = 0;
        this.killValue = 0;
        this.dangerValue = 0;
        this.isAttack = false;
        this.teamHomeDist = 0;
        this.cost = 7;
        this.valid = true;
    }

    public Boolean isValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public int getDangerValue() {
        return dangerValue;
    }

    public void setDangerValue(int dangerValue) {
        this.dangerValue = dangerValue;
    }

    public int getTeamHomeDist() {
        return teamHomeDist;
    }

    public void setTeamHomeDist(int teamHomeDist) {
        this.teamHomeDist = teamHomeDist;
    }
}
