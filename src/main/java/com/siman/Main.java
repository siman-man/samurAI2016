package main.java.com.siman;

public class Main {
    public static void main(String[] args) {
        GameInfo info = new GameInfo();
        info.init();
        Servant player;

        if(info.weapon == Weapon.LANCE){
            player = new Lancer(info);
        }else if(info.weapon == Weapon.SWORD){
            player = new Saber(info);
        }else{
            player = new Berserker(info);
        }

        while (true) {
            info.readTurnInfo();
            System.out.println("# Trun " + info.turn);

            if (info.curePeriod != 0) {
                System.out.println("0");
            } else {
                player.play(info);
                System.out.println("0");
            }
        }
    }
}
