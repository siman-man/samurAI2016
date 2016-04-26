package test.java.com.siman;

import main.java.com.siman.GameInfo;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by siman on 2/1/16.
 */
public class GameInfoTest {
    GameInfo info;

    @Before
    public void setup() {
        info = new GameInfo();
        info.width = 15;
        info.height = 15;
        info.createField();
    }

    @Test
    public void calcDistTest() {
        assertThat(info.calcDist(0, 0, 1, 1), is(2));
        assertThat(info.calcDist(0, 2, 10, 1), is(11));
    }

    @Test
    public void fieldValueTest() {
        info.side = 0;
        info.weapon = 0;
        info.initFieldValue();

        assertThat(info.field[5][0].value, is(0));
        assertThat(info.field[0][0].value, is(9));

        info.side = 1;
        info.initFieldValue();
        assertThat(info.field[9][14].value, is(0));
    }
}