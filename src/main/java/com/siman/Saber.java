package main.java.com.siman;

/**
 * Created by siman on 1/3/16.
 */
public class Saber extends Servant {
    public Saber(GameInfo info) {
        super(info);
    }

    /**
     * 得られた行動情報から評価値を割り出す
     *
     * @param actionInfo 行動後の情報
     * @return
     */
    public int calcEval(ActionInfo actionInfo, GameInfo info) {
        int eval = calcBasicEval(actionInfo, info);

        int sdist = actionInfo.playerDist[GameInfo.ENEMY_SABER];

        if (sdist != -1) {
            if (this.myself.doubleMove) {
                if (info.field[myself.curY][myself.curX].killZone) {
                    eval += 1000;
                }
            } else {
                if (info.field[myself.curY][myself.curX].enemyView) {
                    eval -= 1000;

                    if (sdist >= 7 && (myself.isHide() || actionInfo.cost > 0 && info.isAllyArea(myself.curY, myself.curX))) {
                        eval += 200;
                    }
                    if (sdist >= 5 && (myself.isHide() || actionInfo.cost > 0 && info.isAllyArea(myself.curY, myself.curX))) {
                        eval += 1000;
                    }
                }
            }
        }

        return eval;
    }
}
