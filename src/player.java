import javafx.util.Pair;

import java.util.ArrayList;

public class player {
    String name;
    String color;
    int id;
    int Hp = 3;
    int Mp = 0;
    int target_x = 0;
    int target_y = 0;
    ArrayList<Monster> deck = new ArrayList<Monster>();
    ArrayList<Monster> hand = new ArrayList<Monster>();
    ArrayList<Monster> alive = new ArrayList<Monster>();//场上控制的怪兽
    player(String name,int id,String color)
    {
        this.name = name;
        this.id = id;
        this.color = color;
    }
    public void init()
    {
        this.deck.clear();
        this.hand.clear();
        this.alive.clear();
        System.out.println(name + "已准备就绪");
    }

}
