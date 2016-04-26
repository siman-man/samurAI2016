package main.java.com.siman;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siman on 1/2/16.
 */
abstract public class Servant {
    public int weapon;
    public SamuraiInfo myself;

    public Servant(GameInfo info) {
        this.weapon = info.weapon;
    }

    abstract int calcEval(ActionInfo actionInfo, GameInfo info);

    public int calcBasicEval(ActionInfo actionInfo, GameInfo info) {
        int basicValue = 0;

        // 敵を倒すことが出来る
        basicValue += actionInfo.killValue;

        // 占領する領地の数
        basicValue += 15 * actionInfo.occupyValue;
        // 占領できる敵の領地の数
        basicValue += 15 * actionInfo.occupyEnemyValue;
        // 味方の居館からの距離
        basicValue += actionInfo.getTeamHomeDist();

        // 敵に倒される可能性のある場所への移動は避ける
        basicValue -= actionInfo.getDangerValue();
        // 目的地との距離
        basicValue -= 2 * actionInfo.targetDist;

        Cell cell = info.field[myself.curY][myself.curX];

        if (cell.owner == Field.NEUTRAL) {
            basicValue -= 3;
        }

        if (cell.warning) {
            basicValue -= 250;
        }

        if (cell.enemyWeakPoint) {
            if (actionInfo.cost > 0 && (cell.owner <= 2)) {
                basicValue += 200;
            }
        }

        return basicValue;
    }

    public GameInfo play(GameInfo info) {
        List<Integer> bestAction = new ArrayList<Integer>();
        int maxEval = Integer.MIN_VALUE;

        this.myself = info.getSamuraiInfo(this.weapon);

        for (int[] actions : PlayerAction.PATTERN_LIST) {
            List<Integer> actionList = toList(actions);
            ActionInfo ai = info.evalAction(actionList);

            // evaluate actions if it is valid.
            if (ai.isValid()) {
                int eval = calcEval(ai, info);

                if (maxEval < eval) {
                    maxEval = eval;
                    bestAction = new ArrayList(ai.actions);
                }
            }

            myself.rollbackStatus();
        }

        int cost = 7;

        // 実際の盤面に反映させる
        for (int action : bestAction) {
            info.doAction(action);
            cost -= PlayerAction.COST[action];
        }

        if (cost > 0 && info.isValidAction(PlayerAction.HIDE)) {
            info.doAction(PlayerAction.HIDE);
        }

        return new GameInfo(info);
    }

    private List<Integer> toList(int[] actions){
        List<Integer> intList = new ArrayList<Integer>();

        for(int action : actions){
            intList.add(action);
        }

        return intList;
    }
}
