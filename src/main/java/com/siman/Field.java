package main.java.com.siman;

/**
 * Created by siman on 1/12/16.
 */
public class Field {
    static public int NEUTRAL = 8;
    static public int UNKNOWN = 9;

    static public char[][] CHAR_FIELD = {
            {'0','0','0','0','0','A','0','0','0','0','0'},
            {'0','0','0','0','B','C','D','0','0','0','0'},
            {'0','0','0','E','F','G','H','I','0','0','0'},
            {'0','0','J','K','L','M','N','O','P','0','0'},
            {'0','Q','R','S','T','U','V','W','X','Y','0'},
            {'!','@','#','$','%','^','&','*','(',')','-'},
            {'0','q','r','s','t','u','v','w','x','y','0'},
            {'0','0','j','k','l','m','n','o','p','0','0'},
            {'0','0','0','e','f','g','h','i','0','0','0'},
            {'0','0','0','0','b','c','d','0','0','0','0'},
            {'0','0','0','0','0','a','0','0','0','0','0'},
    };

    public static int[][][] VALUE = {
            // 0チーム 槍
            {
                    {9, 4, 3, 3, 3, 0, 3, 3, 3, 3, 2, 2, 2, 2, 0},
                    {8, 4, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2},
                    {7, 4, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2},
                    {6, 4, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2},
                    {5, 4, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2},
                    {0, 5, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2},
                    {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1},
                    {2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1},
                    {2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1},
                    {2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                    {1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1},
            },

            // 0チーム 剣
            {
                    {1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 2, 2, 2, 2, 0},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2},
                    {1, 1, 1, 1, 1, 1, 1, 1, 3, 2, 2, 2, 2, 2, 2},
                    {1, 1, 1, 1, 1, 1, 1, 3, 2, 2, 2, 2, 2, 2, 2},
                    {0, 1, 1, 1, 1, 1, 3, 2, 2, 2, 2, 2, 2, 2, 2},
                    {1, 1, 1, 1, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2, 1},
                    {1, 1, 1, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1},
                    {4, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1},
                    {5, 4, 3, 3, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 0},
                    {6, 5, 4, 3, 3, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1},
                    {7, 6, 5, 4, 3, 3, 2, 2, 2, 1, 1, 1, 1, 1, 1},
                    {8, 7, 6, 5, 4, 3, 2, 1, 1, 1, 1, 1, 1, 1, 1},
                    {9, 8, 7, 6, 5, 4, 3, 1, 1, 1, 1, 1, 1, 1, 1},
                    {0, 9, 8, 7, 6, 5, 4, 1, 1, 0, 1, 1, 1, 1, 1},
            },

            // 0チーム マサカリ
            {
                    {1, 1, 1, 1, 1, 0, 1, 1, 2, 2, 2, 2, 2, 2, 0},
                    {1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2},
                    {1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
                    {1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
                    {0, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
                    {1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3},
                    {1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 2, 2},
                    {1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 2, 2},
                    {1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 2, 0},
                    {1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 1, 1, 1, 1, 1},
                    {0, 1, 1, 1, 1, 1, 1, 3, 3, 0, 1, 1, 1, 1, 1},
            },
    };
}
