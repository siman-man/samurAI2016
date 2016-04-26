package main.java.com.siman;

/**
 * Created by siman on 1/2/16.
 */
public class SamuraiInfo {
    public static int VIEW_RANGE = 5;

    public int homeX, homeY, hidden;
    public int beforeX, beforeY, beforeHidden;
    public int curX, curY;
    public int lastY, lastX;
    public int rank, score;
    public int update_at;
    public int weapon;
    public int canMoveTurn;
    public boolean doubleMove;
    public boolean expect;
    public int unviewCount;

    public SamuraiInfo() {
        this.homeX = 0;
        this.homeY = 0;
        this.curX = 0;
        this.curY = 0;
        this.lastY = 0;
        this.lastX = 0;
        this.weapon = 0;
        this.beforeX = 0;
        this.beforeY = 0;
        this.beforeHidden = 0;
        this.rank = 0;
        this.score = 0;
        this.unviewCount = 0;
        this.canMoveTurn = 0;
        this.hidden = 0;
        this.update_at = 0;
        this.doubleMove = false;
        this.expect = false;
    }

    /**
     * プレイヤーの状態を保存する
     */
    public void saveStatus(){
        this.beforeX = this.curX;
        this.beforeY = this.curY;
        this.beforeHidden = this.hidden;
    }

    /**
     * プレイヤーの状態を元に戻す
     */
    public void rollbackStatus(){
        this.curX = this.beforeX;
        this.curY = this.beforeY;
        this.hidden = this.beforeHidden;
    }

    public void move(int action){
        if (action == PlayerAction.MOVE_SOUTH) {
            this.curY++;
        }
        if (action == PlayerAction.MOVE_EAST) {
            this.curX++;
        }
        if (action == PlayerAction.MOVE_NORTH) {
            this.curY--;
        }
        if (action == PlayerAction.MOVE_WEST) {
            this.curX--;
        }
    }

    /**
     * check hide or not
     * @return true(hide)
     */
    public Boolean isHide(){
        return this.hidden == 1;
    }

    public void hide(){
        this.hidden = 1;
    }

    public void showUp(){
        this.hidden = 0;
    }

    public void update(int turn, int side){
        this.doubleMove = false;
        this.update_at = turn;

        if(side == 0){
            if(this.weapon == 0 && turn % 12 == 7){
                this.doubleMove = true;
            }else if(this.weapon == 1 && turn % 12 == 3){
                this.doubleMove = true;
            }else if(this.weapon == 2 && turn % 12 == 11){
                this.doubleMove = true;
            }
        }else{
            if(this.weapon == 0 && turn % 12 == 1){
                this.doubleMove = true;
            }else if(this.weapon == 1 && turn % 12 == 9){
                this.doubleMove = true;
            }else if(this.weapon == 2 && turn % 12 == 5){
                this.doubleMove = true;
            }
        }
    }

    public void backToHome(){
        this.curY = this.homeY;
        this.curX = this.homeX;
        this.lastY = this.homeY;
        this.lastX = this.homeX;
    }

    public void expectUpdate(int y, int x, int turn){
        this.update_at = turn;
        this.curY = y;
        this.curX = x;
        this.beforeY = y;
        this.beforeX = x;
    }
}
