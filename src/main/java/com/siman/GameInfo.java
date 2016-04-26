package main.java.com.siman;

import org.omg.CORBA.UNKNOWN;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by siman on 1/2/16.
 */
public class GameInfo {
    static public int ENEMY_LANCER = 3;
    static public int ENEMY_SABER = 4;
    static public int ENEMY_BERSERKER = 5;

    static public int[] ENEMY_ID_LIST = {ENEMY_LANCER, ENEMY_SABER, ENEMY_BERSERKER};

    private final int[] DY = {1, 0, -1, 0, -1, -1, 1, 1};
    private final int[] DX = {0, 1, 0, -1, -1, 1, -1, 1};

    // enemy weak point
    private final int[] WY = {-2, -2, 2, 2};
    private final int[] WX = {-2, 2, -2, 2};

    private int nextY;
    private int nextX;

    /**
     * プレイヤーの人数
     */
    public static final int PLAYER_NUM = 6;

    public static BufferedReader stdReader;

    /**
     * total turn
     */
    public int turns;

    /**
     * どちら側の陣営か
     */
    public int side;

    /**
     * 武器のタイプ
     */
    public int weapon;

    /**
     * field width
     */
    public int width;

    /**
     * field height
     */
    public int height;

    /**
     * フィールドチェック用
     */
    private boolean[][] checkField;

    /**
     * 倒された際に必要な治療期間
     */
    public int maxCure;
    public SamuraiInfo[] samuraiInfo;
    public int turn, curePeriod;
    public Cell[][] field;
    public int[] playerDist;
    public int attackId;
    public Boolean allView;

    public GameInfo(GameInfo info) {
        this.turns = info.turns;
        this.side = info.side;
        this.weapon = info.weapon;
        this.width = info.width;
        this.height = info.height;
        this.maxCure = info.maxCure;
        this.samuraiInfo = info.samuraiInfo;
        this.turn = info.turn;
        this.curePeriod = info.curePeriod;
        this.field = info.field;
        this.attackId = 0;
    }

    /**
     * 一番最初にゲーム情報が渡されるのでそれを元に初期化を行う
     */
    public GameInfo() {
    }

    public void init() {
        GameInfo.stdReader = new BufferedReader(new InputStreamReader(System.in));

        String[] res = this.read();

        this.turns = Integer.parseInt(res[0]);
        this.side = Integer.parseInt(res[1]);
        this.weapon = Integer.parseInt(res[2]);
        this.width = Integer.parseInt(res[3]);
        this.height = Integer.parseInt(res[4]);
        this.maxCure = Integer.parseInt(res[5]);
        this.samuraiInfo = new SamuraiInfo[GameInfo.PLAYER_NUM];

        // initialize game parameter
        this.turn = 0;
        this.curePeriod = 0;
        this.checkField = new boolean[this.height][this.width];
        this.playerDist = new int[GameInfo.PLAYER_NUM];

        createField();

        // initialize samurai information
        for (int i = 0; i < GameInfo.PLAYER_NUM; ++i) {
            this.samuraiInfo[i] = new SamuraiInfo();
            this.samuraiInfo[i].weapon = i % 3;
        }

        // initialize home information
        for (int i = 0; i < GameInfo.PLAYER_NUM; ++i) {
            res = this.read();
            int homeX = Integer.parseInt(res[0]);
            int homeY = Integer.parseInt(res[1]);
            this.samuraiInfo[i].homeY = homeY;
            this.samuraiInfo[i].homeX = homeX;
            this.samuraiInfo[i].lastY = homeY;
            this.samuraiInfo[i].lastX = homeX;
            this.field[homeY][homeX].owner = i;
            this.field[homeY][homeX].existHome = true;
        }

        // initialize player ranking information
        for (int i = 0; i < GameInfo.PLAYER_NUM; ++i) {
            res = this.read();
            this.samuraiInfo[i].rank = Integer.parseInt(res[0]);
            this.samuraiInfo[i].score = Integer.parseInt(res[1]);
        }

        // initialize field value
        initFieldValue();

        System.out.println("0");
    }

    /**
     * read input (eliminate comment)
     *
     * @return コメントを除いた1行
     */
    public String[] read() {
        String line = "";
        try {
            for (line = GameInfo.stdReader.readLine(); line.startsWith("#"); line = GameInfo.stdReader.readLine()) ;
        } catch (Exception e) {
            e.getStackTrace();
            System.exit(-1);
        }
        return line.split("\\s");
    }

    /**
     * ターン毎に渡される情報を読み込む
     * 1. read current turn
     * 2. get cure period
     * 3. get each samurai information
     */
    public void readTurnInfo() {
        String[] res = this.read();

        if (res.length == 0) {
            System.exit(-1);
        }

        // get current turn
        this.turn = Integer.parseInt(res[0]);

        if (this.turn < 0) {
            System.exit(-1);
        }

        res = this.read();
        // get healing time
        this.curePeriod = Integer.parseInt(res[0]);

        // clear all cell
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.field[y][x].clear();
            }
        }

        // get samurai information
        for (int playerId = 0; playerId < GameInfo.PLAYER_NUM; ++playerId) {
            res = this.read();

            int curX = Integer.parseInt(res[0]);
            int curY = Integer.parseInt(res[1]);

            SamuraiInfo samurai = this.samuraiInfo[playerId];

            samurai.hidden = Integer.parseInt(res[2]);
            samurai.expect = false;

            // if get samurai information, update samurai data.
            if (curY != -1) {
                samurai.curX = curX;
                samurai.curY = curY;
                samurai.lastY = samurai.curY;
                samurai.lastX = samurai.curX;
                samurai.unviewCount = 0;
                samurai.update(this.turn, this.side);
            } else {
                samurai.unviewCount += 1;
            }

            // memorize samurai position (for rollback)
            samurai.saveStatus();
        }

        // get field information
        for (int y = 0; y < this.height; ++y) {
            res = this.read();

            for (int x = 0; x < this.width; ++x) {
                int owner = Integer.parseInt(res[x + 1]);

                // 前の自分の記録と異なるフィールド変化が起きた場合はマークを付ける
                if (this.field[y][x].owner != owner && this.field[y][x].owner != Field.UNKNOWN && (3 <= owner && owner <= 5)) {
                    this.field[y][x].changed = true;

                    checkWarning(owner, y, x);
                } else {
                    this.field[y][x].changed = false;
                }

                if (owner != Field.UNKNOWN) {
                    this.field[y][x].update(owner, this.turn);
                } else {
                    this.field[y][x].owner = owner;
                }
            }
        }

        // update field information
        updateFieldInfo();
    }

    /**
     * update field information
     *
     * 1. フィールドの危険地帯の更新(敵に倒される可能性のある場所)
     */
    private void updateFieldInfo() {
        SamuraiInfo myself = this.samuraiInfo[this.weapon];

        // expect enemy point
        expectEnemyPoint();

        // update field danger value
        updateDangerField();

        // update next target position
        directNextPoint();

        if (myself.doubleMove) {
            updateKillZone();
        }

        this.allView = true;

        for (int enemyId : ENEMY_ID_LIST) {
            SamuraiInfo enemy = this.samuraiInfo[enemyId];

            if (enemy.update_at != this.turn) {
                this.allView = false;
            }
            if (enemy.update_at == this.turn) {
                checkEnemySight(enemy.curY, enemy.curX);
            }
            if (enemy.update_at == this.turn && enemy.canMoveTurn < this.turn && !this.field[enemy.curY][enemy.curX].isHome()) {
                checkWeakPoint(enemy.curY, enemy.curX);
            }
        }
    }

    /**
     * 敵の現在位置を予測する
     */
    private void expectEnemyPoint() {
        SamuraiInfo myself = this.samuraiInfo[this.weapon];

        for (int y = 0; y < this.height; y++) {
            Arrays.fill(this.checkField[y], false);
        }

        // 自分自身の視界から得られるフィールドの変化を確認する
        for (int dy = 0; dy < 11; dy++) {
            for (int dx = 0; dx < 11; dx++) {
                int ny = myself.curY + (dy - 5);
                int nx = myself.curX + (dx - 5);
                int dist = calcDist(dy, dx, 5, 5);

                if (isOutside(ny, nx)) continue;
                if (this.checkField[ny][nx]) continue;
                if (dist > 5) continue;

                if (this.field[ny][nx].changed) {
                    String expectHash = getExpectHash(this.field[ny][nx].owner);

                    int myId = (3 * (this.side) + this.weapon) % 6;
                    int enemyId = (3 * (this.side) + this.field[ny][nx].owner) % 6;
                    System.out.println("#expectHash2:" + expectHash + " myId:" + myId + " enemyId:" + enemyId);

                    boolean lhit = LancerPattern.EXPECT_MAP.containsKey(expectHash);
                    boolean shit = SaberPattern.EXPECT_MAP.containsKey(expectHash);
                    boolean bhit = BerserkerPattern.EXPECT_MAP.containsKey(expectHash);

                    if (lhit || shit || bhit) {
                        Coord coord;

                        if (lhit) {
                            coord = LancerPattern.EXPECT_MAP.get(expectHash);
                        } else if (shit) {
                            coord = SaberPattern.EXPECT_MAP.get(expectHash);
                        } else {
                            coord = BerserkerPattern.EXPECT_MAP.get(expectHash);
                        }
                        SamuraiInfo enemy = this.samuraiInfo[coord.weapon];

                        if (enemy.update_at == this.turn) continue;

                        int[] point = getMostNearCoords(coord, enemy, myself.curY, myself.curX);
                        int nearY = point[0];
                        int nearX = point[1];

                        if (nearY != -1) {
                            if (coord.count > 4) {
                                enemy.expect = true;
                            }

                            enemy.expectUpdate(nearY, nearX, this.turn);
                        }
                    }
                }
            }
        }
    }

    /**
     * hashed change field
     *
     * @return ハッシュ値
     */
    private String getExpectHash(int enemyId) {
        SamuraiInfo myself = this.samuraiInfo[this.weapon];
        String str = Integer.toString(enemyId);

        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                int curY = myself.curY + (y - 5);
                int curX = myself.curX + (x - 5);
                int dist = calcDist(y, x, 5, 5);

                if (isOutside(curY, curX)) continue;
                if (dist > 5) continue;

                if (this.field[curY][curX].changed && this.field[curY][curX].owner == enemyId) {
                    str += Field.CHAR_FIELD[y][x];
                    this.checkField[curY][curX] = true;
                }
            }
        }

        return str;
    }

    private void checkWeakPoint(int y, int x) {
        for (int i = 0; i < 4; i++) {
            int wy = y + WY[i];
            int wx = x + WX[i];

            if (isOutside(wy, wx)) continue;

            this.field[wy][wx].enemyWeakPoint = true;
        }
    }

    /**
     * update warning field
     *
     * @param enemyId enemy id
     * @param y       y coord
     * @param x       x coord
     */
    private void checkWarning(int enemyId, int y, int x) {
        SamuraiInfo enemy = getSamuraiInfo(enemyId);

        if (!enemy.isHide()) return;

        for (int i = 0; i < 8; i++) {
            int ny = y + DY[i];
            int nx = x + DX[i];

            if (isOutside(ny, nx)) continue;
            this.field[ny][nx].warning = true;
        }
    }

    /**
     * initialize field information
     */
    public void createField() {
        this.field = new Cell[this.height][this.width];

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.field[y][x] = new Cell();
            }
        }
    }

    /**
     * initialize field value
     */
    public void initFieldValue() {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (this.side == 0) {
                    this.field[y][x].value = Field.VALUE[this.weapon][y][x];
                } else {
                    this.field[y][x].value = Field.VALUE[this.weapon][14 - y][14 - x];
                }
            }
        }
    }

    private int calcGetFieldCount(int y, int x) {
        int cnt = 0;

        for (int dy = 0; dy < 11; dy++) {
            for (int dx = 0; dx < 11; dx++) {
                int ny = y + (dy - 5);
                int nx = x + (dx - 5);

                if(isOutside(ny, nx)) continue;
                Cell cell = this.field[ny][nx];

                if(cell.lastOwner == Field.UNKNOWN) continue;

                if (cell.lastOwner >= 3 && Weapon.KILL_RANGE[this.weapon][dy][dx] > 0) {
                    cnt++;
                }
            }
        }

        return Math.max(0, cnt-3);
    }

    /**
     * update field danger value
     */
    private void updateDangerField() {
        SamuraiInfo myself = this.samuraiInfo[this.weapon];

        for (int enemyId : ENEMY_ID_LIST) {
            // サムライ情報の取得
            SamuraiInfo enemy = this.samuraiInfo[enemyId];

            // skip if player infomation is old
            if (enemy.update_at < this.turn - 12) {
                continue;
            }
            // skip if player can not action
            if (enemy.canMoveTurn > this.turn) {
                continue;
            }
            // 2回行動を取れるときは同じ武器の相手キャラの動きは考慮しない
            if (myself.doubleMove && this.weapon == enemyId % 3) {
                continue;
            }

            for (int dy = 0; dy < 11; dy++) {
                for (int dx = 0; dx < 11; dx++) {
                    int ny = enemy.curY + (dy - 5);
                    int nx = enemy.curX + (dx - 5);
                    int weapon = enemyId % 3;

                    if (isInside(ny, nx) && Weapon.KILL_RANGE[weapon][dy][dx] > 0) {
                        this.field[ny][nx].dangerValue += 6000;
                    }
                }
            }
        }
    }

    /**
     * 同キャラ戦用
     * 2回行動が取れるときに、相手を倒せる距離まで詰める
     */
    private void updateKillZone() {
        int enemyId = this.weapon + 3;

        SamuraiInfo enemy = this.samuraiInfo[enemyId];

        // 相手の情報が最新の場合に発動(あと隠れていない)
        if (enemy.update_at == this.turn && !enemy.isHide()) {
            for (int dy = 0; dy < 11; dy++) {
                for (int dx = 0; dx < 11; dx++) {
                    int ny = enemy.curY + (dy - 5);
                    int nx = enemy.curX + (dx - 5);

                    if (isInside(ny, nx) && Weapon.KILL_RANGE[this.weapon][dy][dx] > 0) {
                        this.field[ny][nx].killZone = true;
                    }
                }
            }
        }
    }

    /**
     * check enemy sight
     */
    private void checkEnemySight(int enemyY, int enemyX) {
        Queue<Integer> queueX = new LinkedList<Integer>();
        Queue<Integer> queueY = new LinkedList<Integer>();
        Queue<Integer> queueD = new LinkedList<Integer>();

        queueY.add(enemyY);
        queueX.add(enemyX);
        queueD.add(0);

        boolean[][] checkList = new boolean[this.height][this.width];
        for (int y = 0; y < this.height; y++) {
            Arrays.fill(checkList[y], false);
        }

        while (!queueX.isEmpty()) {
            int y = queueY.poll();
            int x = queueX.poll();
            int dist = queueD.poll();

            if (dist > SamuraiInfo.VIEW_RANGE) continue;
            if (checkList[y][x]) continue;
            checkList[y][x] = true;

            this.field[y][x].enemyView = true;

            for (int i = 0; i < 4; i++) {
                int ny = y + DY[i];
                int nx = x + DX[i];

                if (isOutside(ny, nx)) continue;

                queueY.add(ny);
                queueX.add(nx);
                queueD.add(dist + 1);
            }
        }
    }

    /**
     * 進む方向を決める
     */
    private void directNextPoint() {
        SamuraiInfo myself = this.samuraiInfo[this.weapon];
        this.nextX = myself.curX;
        this.nextY = myself.curY;

        int maxLength = 5;
        Queue<Integer> queueX = new LinkedList<Integer>();
        Queue<Integer> queueY = new LinkedList<Integer>();
        Queue<Integer> queueD = new LinkedList<Integer>();
        boolean[][] checkList = new boolean[this.height][this.width];

        for (int y = 0; y < this.height; y++) {
            Arrays.fill(checkList[y], false);
        }

        queueY.add(myself.curY);
        queueX.add(myself.curX);
        queueD.add(0);

        int maxCount = 0;
        int nY = myself.curY;
        int nX = myself.curX;

        while (!queueX.isEmpty()) {
            int x = queueX.poll();
            int y = queueY.poll();
            int dist = queueD.poll();

            if (dist > maxLength) continue;
            if (checkList[y][x]) continue;
            checkList[y][x] = true;

            Cell cell = this.field[y][x];

            int cnt = calcGetFieldCount(y, x);
            if(maxCount < cnt){
                maxCount = cnt;
                nY = y;
                nX = x;
            }

            for (int i = 0; i < 4; i++) {
                int ny = y + DY[i];
                int nx = x + DX[i];

                if (isOutside(ny, nx)) continue;

                queueY.add(ny);
                queueX.add(nx);
                queueD.add(dist + 1);
            }
        }

        nextY = nY;
        nextX = nX;

        if (myself.curY == nextY && myself.curX == nextX) {
            nextY = this.height / 2;
            nextX = this.width / 2;

            if (this.side == 1) {
                nextY--;
                nextX--;
            }
        }
    }

    /**
     * update each player distance
     */
    private void updatePlayerDist() {
        SamuraiInfo myself = this.samuraiInfo[this.weapon];

        for (int playerId = 0; playerId < GameInfo.PLAYER_NUM; playerId++) {
            SamuraiInfo si = this.samuraiInfo[playerId];

            if (si.update_at == this.turn) {
                this.playerDist[playerId] = calcDist(myself.curY, myself.curX, si.curY, si.curX);
            } else {
                this.playerDist[playerId] = -1;
            }
        }
    }

    public int[] getMostNearCoords(Coord coord, SamuraiInfo enemy, int ty, int tx) {
        int nearY = -1;
        int nearX = -1;
        int minDist = Integer.MAX_VALUE;
        int size = coord.count;
        int cnt = 0;

        for (int i = 0; i < size; i++) {
            int cy = ty + coord.coords[i * 2];
            int cx = tx + coord.coords[i * 2 + 1];
            int dist = calcDist(enemy.lastY, enemy.lastX, cy, cx);

            if (isOutside(cy, cx)) continue;
            Cell cell = this.field[cy][cx];

            if (isAllyArea(cy, cx) || cell.owner == Field.NEUTRAL) {
                continue;
            }
            if (enemy.unviewCount == 1 && dist > 1) {
                continue;
            }

            cnt++;

            if (minDist - 1 > dist) {
                minDist = dist;
                nearY = cy;
                nearX = cx;
            }
        }

        coord.count = cnt;

        return new int[]{nearY, nearX};
    }

    /**
     * check is outside field
     *
     * @param y y position
     * @param x x position
     * @return true(outside)
     */
    private boolean isOutside(int y, int x) {
        return (y < 0 || this.height <= y || x < 0 || this.width <= x);
    }

    /**
     * check is inside field
     *
     * @param y y position
     * @param x x position
     * @return true(inside)
     */
    public boolean isInside(int y, int x) {
        return (0 <= y && y < this.height && 0 <= x && x < this.width);
    }

    /**
     * check ally area or not
     */
    public boolean isAllyArea(int y, int x) {
        return (0 <= this.field[y][x].owner && this.field[y][x].owner <= 2);
    }

    /**
     * check enemy area or not
     *
     * @param y y position
     * @param x x position
     * @return true(enemy area)
     */
    public Boolean isEnemyArea(int y, int x) {
        return (3 <= this.field[y][x].owner && this.field[y][x].owner <= 5);
    }

    /**
     * check valid ation or not
     *
     * @param action action list
     * @return aciton is valid or invalid
     */
    public boolean isValidAction(int action) {
        SamuraiInfo myself = this.samuraiInfo[this.weapon];
        int curX = myself.curX;
        int curY = myself.curY;

        // NO_ACTION is success
        if (action == PlayerAction.NO_ACTION) {
            return true;
        }

        // success if not hide mode
        if (isAttackAction(action)) {
            return myself.hidden == 0;
        }

        if (isMoveAction(action)) {
            // South(down)
            if (action == PlayerAction.MOVE_SOUTH) {
                curY++;
            }
            // East(right)
            if (action == PlayerAction.MOVE_EAST) {
                curX++;
            }
            // North(up)
            if (action == PlayerAction.MOVE_NORTH) {
                curY--;
            }
            // West(left)
            if (action == PlayerAction.MOVE_WEST) {
                curX--;
            }
            // failed if current position is field outside
            if (isOutside(curY, curX)) {
                return false;
            }
            // failed if go to not ally area in hide mode.
            if (myself.isHide() && this.field[curY][curX].owner >= 3) {
                return false;
            }
            for (int playerId = 0; playerId < GameInfo.PLAYER_NUM; ++playerId) {
                SamuraiInfo samurai = getSamuraiInfo(playerId);

                if (playerId == this.weapon) continue;

                // failed if other samurai already exists.
                if (curX == samurai.curX && curY == samurai.curY) {
                    return false;
                }
                // failed if other home field.
                if (curX == samurai.homeX && curY == samurai.homeY) {
                    return false;
                }
            }

            return true;
        }

        if (action == PlayerAction.HIDE) {
            // failed if already hide mode
            if (myself.isHide()) {
                return false;
            }
            // failed if is not ally area
            if (!isAllyArea(curY, curX)) {
                return false;
            }
            return true;
        }

        if (action == PlayerAction.SHOW_UP) {
            if (myself.hidden != 1) {
                return false;
            }
            for (int i = 0; i < GameInfo.PLAYER_NUM; ++i) {
                SamuraiInfo other = this.samuraiInfo[i];
                if (other.hidden != 1 && (other.curX == curX && other.curY == curY)) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    /**
     * attack
     *
     * @param direction 攻撃コマンドの方向
     */
    public void attack(ActionInfo actionInfo, int direction) {
        this.attackId++;

        SamuraiInfo myself = this.samuraiInfo[this.weapon];
        int curX = myself.curX;
        int curY = myself.curY;
        int occupyValue = 0;
        int realCount = 0;
        int occupyEnemyValue = 0;
        int killValue = 0;

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                int ny = (y - 4) + curY;
                int nx = (x - 4) + curX;
                int mask = Weapon.ATTACK_RANGE[myself.weapon][y][x];

                if (isOutside(ny, nx)) continue;

                Cell cell = this.field[ny][nx];

                if (!cell.isHome() && ((mask >> direction) & 1) == 1) {
                    this.field[ny][nx].attackId = this.attackId;

                    // フィールドが自分自身のではない場合は占領カウントが増える
                    if (this.field[ny][nx].owner >= 3) {
                        occupyValue += this.field[ny][nx].value;
                        realCount++;
                    }

                    if (isEnemyArea(ny, nx)) {
                        occupyEnemyValue++;
                    }

                    for (int enemyId : ENEMY_ID_LIST) {
                        SamuraiInfo enemy = this.samuraiInfo[enemyId];

                        // もし敵のサムライが存在していた場合はkillValueを増やす
                        if (enemy.curX == nx && enemy.curY == ny) {
                            enemy.curY = enemy.homeY;
                            enemy.curX = enemy.homeX;

                            if (enemy.expect) {
                                killValue += 999;
                            } else {
                                killValue += 9999;
                            }
                        }
                    }
                }
            }
        }

        if (realCount < 2) {
            occupyValue = 0;
            occupyEnemyValue = 0;
        }

        actionInfo.killValue = killValue;
        actionInfo.occupyValue = occupyValue;
        actionInfo.occupyEnemyValue = occupyEnemyValue;
    }

    /**
     * occupy command
     *
     * @param direction direction of attack
     */
    public void occupy(int direction) {
        SamuraiInfo myself = this.samuraiInfo[this.weapon];
        int curX = myself.curX;
        int curY = myself.curY;

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                int ny = (y - 4) + curY;
                int nx = (x - 4) + curX;
                int mask = Weapon.ATTACK_RANGE[myself.weapon][y][x];

                // skip if outside field
                if (isOutside(ny, nx)) continue;

                Cell cell = this.field[ny][nx];

                // skip if home
                if (cell.isHome()) continue;

                if (((mask >> direction) & 1) == 1) {
                    // そのフィールドを占領する
                    cell.owner = this.weapon;

                    for (int enemyId : ENEMY_ID_LIST) {
                        SamuraiInfo enemy = this.samuraiInfo[enemyId];
                        // もし敵のサムライが存在していた場合は、倒した時の処理を行う
                        if (enemy.curX == nx && enemy.curY == ny && !enemy.isHide()) {
                            enemy.backToHome();
                            enemy.canMoveTurn = this.turn + (this.maxCure - 6);
                        }
                    }
                }
            }
        }
    }

    /**
     * 攻撃系のアクションかどうかを判定する
     *
     * @param action
     * @return
     */
    public boolean isAttackAction(int action) {
        return (1 <= action && action <= 4);
    }

    /**
     * 行動系のアクションなのかどうかを判定する
     *
     * @param action
     * @return
     */
    public boolean isMoveAction(int action) {
        return (5 <= action && action <= 8);
    }

    /**
     * evalute actions
     *
     * @param actions action list
     */
    public ActionInfo evalAction(List<Integer> actions) {
        ActionInfo actionInfo = new ActionInfo();
        int size = actions.size();
        SamuraiInfo myself = this.samuraiInfo[this.weapon];

        for (int i = 0; i < size; i++) {
            int action = actions.get(i);

            if (isValidAction(action)) {
                actionInfo.cost -= PlayerAction.COST[action];

                if (isAttackAction(action)) {
                    attack(actionInfo, action - 1);
                    actionInfo.isAttack = true;
                } else if (isMoveAction(action)) {
                    myself.move(action);
                } else if (action == PlayerAction.SHOW_UP) {
                    myself.showUp();
                } else if (action == PlayerAction.HIDE) {
                    myself.hide();
                }
            } else if (action != PlayerAction.NO_ACTION) {
                if (action != PlayerAction.SHOW_UP && myself.isHide()) {
                    actions.add(i, PlayerAction.SHOW_UP);
                    size++;
                    i--;
                } else {
                    // stop evalution if there are any invalid action
                    actionInfo.setValid(false);

                    return actionInfo;
                }
            }
        }

        // 今自分がいる場所の危険度をセット
        actionInfo.setDangerValue(this.field[myself.curY][myself.curX].dangerValue);

        int teamHomeDist = 0;
        int minTeamDist = Integer.MAX_VALUE;

        // チームメンバーとの距離を測る
        for (int id = 0; id < 3; id++) {
            SamuraiInfo si = this.samuraiInfo[id];

            // 自分自身は外す
            if (id != this.weapon) {
                int dist = calcDist(myself.curY, myself.curX, si.homeY, si.homeX);
                int mdist = calcDist(myself.curY, myself.curX, si.curY, si.curX);
                teamHomeDist += Math.min(12, dist);

                if (si.curY != si.homeY && si.curX != si.homeX) {
                    minTeamDist = Math.min(minTeamDist, mdist);
                }
            }
        }

        actionInfo.setTeamHomeDist(teamHomeDist);

        // update each player distance
        updatePlayerDist();
        actionInfo.playerDist = this.playerDist.clone();
        actionInfo.targetDist = calcDist(myself.curY, myself.curX, nextY, nextX);

        actionInfo.actions = actions;

        return actionInfo;
    }

    /**
     * calculation Manhattan distance between two point
     */
    public int calcDist(int y1, int x1, int y2, int x2) {
        return Math.abs(y1 - y2) + Math.abs(x1 - x2);
    }

    /**
     * get samurai information
     *
     * @param id samurai id
     * @return samurai information
     */
    public SamuraiInfo getSamuraiInfo(int id) {
        return this.samuraiInfo[id];
    }

    /**
     * 行動をフィールドに反映させる
     *
     * @param action
     */
    public void doAction(int action) {
        SamuraiInfo myself = this.samuraiInfo[this.weapon];

        if (action == PlayerAction.NO_ACTION) {
            return;
        }

        if (isAttackAction(action)) {
            this.occupy(action - 1);
        } else if (isMoveAction(action)) {
            myself.move(action);
        } else if (action == PlayerAction.HIDE) {
            myself.hide();
        } else if (action == PlayerAction.SHOW_UP) {
            myself.showUp();
        }

        System.out.print(action + " ");
    }
}
