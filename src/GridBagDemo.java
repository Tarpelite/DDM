import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

interface gameconfig{
    final int size = 21;
    ImageIcon hands_property[] = new ImageIcon[7];
    ImageIcon pic_property[] = new ImageIcon[7];

    //窗口部分
    JTextField navigation_panel = new JTextField();
    JLabel p1_info = new JLabel();
    JLabel p2_info = new JLabel();
    JLabel pic_field = new JLabel();
    JLabel info_board = new JLabel();
    JPanel battle_field = new JPanel();
    JPanel hand_board = new JPanel();
    JPanel buttons_panel = new JPanel();
    JLabel hand_cards[] = new JLabel[7];
    JButton []button_list = new JButton[5];
    Label label[][]=new Label[size][size];

    //游戏部分
    player p1 = new player("Tom",1,"蓝");
    player p2 = new player("Jim",2,"红");
    int board[][] = new int[size][size];//棋盘陆地和海洋分布数组
    int board_M[][] = new int[size][size];//棋盘怪兽分布数组
    ArrayList<Monster> property = new ArrayList<Monster>();
    ArrayList<Monster> tmp = new ArrayList<Monster>();//

    //图片加载
    ImageIcon big[] = new ImageIcon[500];
    ImageIcon small[] = new ImageIcon[500];
    ImageIcon bg_pic = new ImageIcon("bg.jpg");
    JLabel bg = new JLabel(bg_pic);

}

public class GridBagDemo extends JFrame implements gameconfig {
    int curse_x = 1;
    int cnt;
    player currentplayer;
    player opponent;
    Monster Chest1=new Monster("宝箱",((int) (Math.random() * 1)+2)*100,0,0,40,1,"装有特殊物品的宝箱");
    Monster Chest2=new Monster("宝箱",((int) (Math.random() * 1)+2)*100,0,0,40,1,"装有特殊物品的宝箱");
    //定义一个独特的怪兽“宝箱”，他的id对应不同的技能，模拟宝箱里的宝物随机生成
    JFrame frame = this;

    public static void main(String[] args) {

        GridBagDemo demo = new GridBagDemo();
    }

    public GridBagDemo() {
        frame_init();
        init();
        run();
    }


    public void frame_init() {
        navigation_panel.setEditable(false);
        navigation_panel.setBackground(Color.CYAN);
        navigation_panel.setForeground(Color.red);
        p1_info.setText("");
        p2_info.setText("");
        ImageIcon i1 = new ImageIcon("./pic_img/pic_1.jpg");
        pic_field.setIcon(i1);
        info_board.setHorizontalAlignment(JLabel.LEFT);
        for (int i = 1; i <= 6; i++) {
            hand_cards[i] = new JLabel();
            hand_cards[i].setSize(20, 20);
            ImageIcon icon = new ImageIcon("./hand_img/0.jpg");
            hand_cards[i].setIcon(icon);
            hand_board.add(hand_cards[i]);
        }
        JPanel blank1 = new JPanel();
        JPanel blank2 = new JPanel();
        show_rule();
        pick(p1);
        pick(p2);
        this.getLayeredPane().add(bg,new Integer(Integer.MIN_VALUE));
        bg.setBounds(0,0,bg_pic.getIconWidth(),bg_pic.getIconHeight());

//        String []conts = new String[5];
        String conts[] = {"攻击", "魔法", "投掷","移动", "结束"};
        for (int i = 0; i < 5; i++) {
            button_list[i] = new JButton(conts[i]);
            buttons_panel.add(button_list[i]);
        }


        GridLayout grid1 = new GridLayout(size - 1, size - 1);
        battle_field.setLayout(grid1);
        //battle_field.setSize(600,400);
        for (int i = 1; i < size - 1; i++) {
            for (int j = 1; j < size - 1; j++) {
                label[i][j] = new Label();
                label[i][j].setSize(20, 20);
                if ((i + j) % 2 == 0) {
                    label[i][j].setBackground(Color.black);
                    label[i][j].setForeground(Color.yellow);
                    label[i][j].setAlignment(Label.CENTER);
                }
                else{
                    label[i][j].setBackground(Color.white);
                    label[i][j].setForeground(Color.yellow);
                    label[i][j].setAlignment(Label.CENTER);
                }
                battle_field.add(label[i][j]);
            }
        }

        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        this.add(navigation_panel);
        this.add(p1_info);
        this.add(p2_info);
//        this.add(blank0);
        this.add(pic_field);
        //this.add(blank1);
        this.add(battle_field);
        this.add(blank1);
        this.add(info_board);
        this.add(hand_board);
        this.add(blank2);
        this.add(buttons_panel);

        GridBagConstraints s = new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;
        s.gridwidth = 0;
        s.weightx = 1;
        s.weighty = 0;
        layout.setConstraints(navigation_panel, s);

        s.gridwidth = 4;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(p1_info, s);

        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(p2_info, s);

        s.gridwidth = 4;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(pic_field, s);


        s.gridwidth = 4;
        s.weightx = 1;
        s.weighty = 0;
        layout.setConstraints(battle_field, s);

        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(blank1, s);

        s.gridwidth = 4;
        s.weightx = 1;
        s.weighty = 1;
        layout.setConstraints(info_board, s);

        s.gridwidth = 4;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(hand_board, s);

        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(blank2, s);

        s.gridwidth = 0;
        s.weightx = 1;
        s.weighty = 0;
        layout.setConstraints(buttons_panel, s);


        //加载事件
        //手牌区光标系统
        hand_board.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyTyped(e);
                if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    if(curse_x>1){
                        curse_x--;
                        hand_field_update(curse_x);
                    }
                }
                else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    if(curse_x<6){
                        curse_x++;
                        hand_field_update(curse_x);
                    }
                }
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    Monster m = currentplayer.hand.get(curse_x -1);
                    tmp.add(m);
                    currentplayer.hand.remove(m);
                    update(currentplayer);
                    cnt++;

                    if(cnt == 3)
                    {
                        navigation_panel.setText("开始投掷骰子");
                        //JOptionPane.showMessageDialog(null,"按确认键继续","warining",JOptionPane.ERROR_MESSAGE);
                        try {
                            Thread.sleep(500);
                        }catch (Exception e1){}
                        String[] result = new String[3];
                        for (int i = 0; i < 3; i++) {
                            int d = (int) (Math.random() * 6);
                            switch (tmp.get(i).face[d].a) {
                                case 1: {
                                    tmp.get(i).summon_pre = true;
                                    result[i] = "召唤 ";
                                    break;
                                }
                                case 2: {
                                    tmp.get(i).summon_pre = false;
                                    currentplayer.Mp += tmp.get(i).face[d].b;
                                    result[i] = "行动 ";
                                    info_update();
                                    break;
                                }
                                case 3: {
                                    tmp.get(i).summon_pre = false;
                                    currentplayer.Mp += tmp.get(i).face[d].b;
                                    result[i] = "攻击 ";
                                    info_update();
                                    break;
                                }
                                case 4: {
                                    tmp.get(i).summon_pre = false;
                                    currentplayer.Mp += tmp.get(i).face[d].b;
                                    result[i] = "防御 ";
                                    info_update();
                                    break;
                                }
                                case 5: {
                                    tmp.get(i).summon_pre = false;
                                    currentplayer.Mp += tmp.get(i).face[d].b;
                                    result[i] = "魔法 ";
                                    info_update();
                                    break;
                                }
                                case 6: {
                                    tmp.get(i).summon_pre = false;
                                    currentplayer.Mp += tmp.get(i).face[d].b;
                                    result[i] = "魔法 ";
                                    info_update();
                                    break;
                                }
                            }
                        }
                        JOptionPane.showMessageDialog(frame,result[0]+result[1]+result[2],"确认投掷结果",JOptionPane.INFORMATION_MESSAGE);
                        ArrayList<Object> options = new ArrayList<>();
                        //Object[] options = {"空","空","空","取消"};
                        for (Monster mst :tmp  ) {
                            if(mst.summon_pre) {
                                options.add(mst.name);
                            }
                        }
                        options.add("取消");
                        int response = JOptionPane.showOptionDialog(frame,"请选择你想召唤的怪兽","怪兽选择",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options.toArray(),options.toArray()[0]);
                        if(response<tmp.size()) {
                            String cmd = (String) options.get(response);
                            for (Monster mst : tmp) {
                                if (mst.name.equals(cmd)) {
                                    summon(currentplayer, mst);
                                    break;
                                }
                            }
                        }
                    }
                }

            }
        });
        this.setTitle("DDM");
        this.setSize(1000, 700);
        this.setVisible(true);
        this.setResizable(false);
    }



    void buttonlist_event(){
        button_list[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Attack(currentplayer,opponent);
                }catch (WinnerException e1){
                    end(currentplayer);
                }
            }
        });
        button_list[1].addActionListener(new ActionListener() {//监视魔法按键
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentplayer.hidden==0) {               //判断能否使用宝箱魔法，如果不能直接使用进入职业技能使用界面
                    if (currentplayer.characterid == 0)  //根据不同职业进入不同的技能使用界面
                        Priest(currentplayer);
                    if (currentplayer.characterid == 1)
                        singer(currentplayer);
                    if (currentplayer.characterid == 2)
                        shaman(currentplayer, opponent);
                    if (currentplayer.characterid == 3)
                        constructer(currentplayer);
                }
                else if(currentplayer.hidden==1){  //判断能否使用传送魔法
                    if (currentplayer.characterid == 0){
                        //如果可以，让玩家选择传送或者使用职业技能
                        Object[] options={"传送","职业技能"};//判断玩家选择
                        int response = JOptionPane.showOptionDialog(null,"请选择你要使用的技能","技能选择",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                        if(response==1) {
                            Priest(currentplayer);
                        }
                        else if(response==0){
                            portal(currentplayer);
                        }
                    }
                    if (currentplayer.characterid == 1) {
                        Object[] options={"传送","职业技能"};
                        int response = JOptionPane.showOptionDialog(null,"请选择你要使用的技能","技能选择",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                        if(response==1) {
                            singer(currentplayer);
                        }
                        else if(response==0){
                            portal(currentplayer);
                        }
                    }
                    if (currentplayer.characterid == 2) {
                        Object[] options={"传送","职业技能"};
                        int response = JOptionPane.showOptionDialog(null,"请选择你要使用的技能","技能选择",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                        if(response==1) {
                            shaman(currentplayer, opponent);
                        }
                        else if(response==0){
                            portal(currentplayer);
                        }
                    }
                    if (currentplayer.characterid == 3) {
                        Object[] options={"传送","职业技能"};
                        int response = JOptionPane.showOptionDialog(null,"请选择你要使用的技能","技能选择",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                        if(response==1) {
                            constructer(currentplayer);
                        }
                        else if(response==0){
                            portal(currentplayer);
                        }
                    }
                }
                else{
                    if (currentplayer.characterid == 0){
                        //判断能否使用爆破鬼才魔法，如果可以操作与传送魔法类似
                        Object[] options={"爆破鬼才","职业技能"};
                        int response = JOptionPane.showOptionDialog(null,"请选择你要使用的技能","技能选择",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                        if(response==1) {
                            Priest(currentplayer);
                        }
                        else if(response==0){
                            explosion(currentplayer,opponent);
                        }
                    }
                    if (currentplayer.characterid == 1) {
                        Object[] options={"爆破鬼才","职业技能"};
                        int response = JOptionPane.showOptionDialog(null,"请选择你要使用的技能","技能选择",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                        if(response==1) {
                            singer(currentplayer);
                        }
                        else if(response==0){
                            explosion(currentplayer,opponent);
                        }
                    }
                    if (currentplayer.characterid == 2) {
                        Object[] options={"爆破鬼才","职业技能"};
                        int response = JOptionPane.showOptionDialog(null,"请选择你要使用的技能","技能选择",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                        if(response==1) {
                            shaman(currentplayer, opponent);
                        }
                        else if(response==0){
                            explosion(currentplayer,opponent);
                        }
                    }
                    if (currentplayer.characterid == 3) {
                        Object[] options={"爆破鬼才","职业技能"};
                        int response = JOptionPane.showOptionDialog(null,"请选择你要使用的技能","技能选择",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                        if(response==1) {
                            constructer(currentplayer);
                        }
                        else if(response==0){
                            explosion(currentplayer,opponent);
                        }
                    }
                }
            }
        });

        button_list[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    roll(tmp,currentplayer);
                }catch (Exception e2){
                    e2.printStackTrace();
                    System.out.println(e2.getMessage());
                }
            }
        });

        button_list[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Move(currentplayer);
                }catch (Exception e1){
                    System.out.println(e1.getMessage());
                }
            }
        });

        button_list[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    next_player();
                }catch (Exception e1){
                    System.out.println(e1.getMessage());
                }
            }
        });
    }

    public void next_player(){
        player tmp_player = currentplayer;
        currentplayer = opponent;
        opponent = tmp_player;
        curse_x = 1;
        navigation_panel.setText("现在是"+currentplayer.name+"的回合");
        try{
            chou_ka(currentplayer);
            update(currentplayer);
        }catch (Exception e){
            end(opponent);
        }
    }

    void hand_board_event()
    {
        cnt = 0;
        hand_field_update(curse_x);
        hand_board.setFocusable(true);
        hand_board.requestFocus();

    }
    public void read(){
        String s1,s2[];//定义一个字符串用来读一行，一个字符串数组来存怪兽的各种数据
        int ints[]={0,0,0,0,0,0},dice[]={0,0,0,0,0,0,0,0,0,0,0,0};   //两个int数组分别存怪兽的数值与其对应骰子的数值属性
        File f=new File("src/Monster1.txt");                             //从预先设置好的文件中读取怪兽属性
        try {
            InputStream inputStream = new FileInputStream(f);
            StringBuilder stringBuilder=new StringBuilder();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            s1=null;
            try{
                int j=0;
                while((s1=bufferedReader.readLine())!=null){
                    s2=s1.split(" ");                     //按空格进行分离，将Monster的各个属性分别存入字符串数组
                    for(int i=1;i<=5;i++){
                        ints[i]=Integer.valueOf(s2[i]);//将数值属性传入int数组
                    }
                    property.add(new Monster(s2[0],ints[1],ints[2],ints[3],ints[4],ints[5],s2[6]));
                    for(int i=0;i<6;i++){                                                   //对骰子的六个面进行定义
                        if(i<5-ints[2]){
                            dice[2*i]=1;
                            dice[2*i+1]=0;
                        }
                        else{
                            dice[2*i]=2;
                            int d = (int) (Math.random() * 10);
                            dice[2*i+1]=d;
                        }
                    }
                    property.get(j++).setFace(dice);
                }
            }
            catch (IOException e){
            }
        }
        catch (FileNotFoundException a){
        }
    }
    public class DeckRunOutException extends Exception{
        public String toString() {
            return "没有多余的卡牌了";
        }
    }
    public void chou_ka(player p) throws DeckRunOutException{
        if( p.deck.size() == 0){
            throw new DeckRunOutException();
        }
        if (p.deck.size() >= 3) {
            for (int i = 0; i < 3; i++) {
                int seed = (int) (Math.random() * p.deck.size());
                p.hand.add(p.deck.get(seed));
                p.deck.remove(p.deck.get(seed));
            }
        } else {
            for(int i=0;i<p.deck.size();i++)
            {
                int seed = (int) (Math.random() * p.deck.size());
                p.hand.add(p.deck.get(seed));
                p.deck.remove(p.deck.get(seed));
            }
        }
    }

    class WinnerException extends Exception{}
    class conflictException1 extends Exception{
        public String toString() {
            return "该目标地点无法进入，请重新输入";
        }
    }
    class conflictException2 extends Exception {
        public String toString() {
            return super.toString();
        }
    }
    class OutofBoard extends Exception{
        public String toString(){
            return "展开区域超出棋盘范围";
        }
    }
    public boolean check(player p) {                                       //对游戏是否结束进行判定
        if (p.Hp==0)                                                        //如果一名玩家死亡，则结束
            return true;
        else{
            if(p.alive.isEmpty()){      //如果一名玩家的怪兽全部死亡，对场上能否继续展开怪兽进行判定，如果不能游戏结束
                int pid=p.id;
                int ch=0;
                labelA:
                for(int m=2;m<=18;m++){
                    int x=m;
                    for(int n=1;n<=18;n++) {
                        int y = n;
                        if(pid==2) {
                            if (board[x][y] == 0) {
                                for (int t = 0; t < p.hand.size(); t++) {    //对展开后的点的坐标进行判定是否越界
                                    int count = 0;
                                    for (int i = 0; i < 5; i++) {
                                        try {
                                            if (x - p.hand.get(t).layout[i].a > 18 || y + p.hand.get(t).layout[i].b > 18 || y + p.hand.get(t).layout[i].b < 2 || x - p.hand.get(t).layout[i].a < 2
                                                    || x - p.hand.get(t).layout[i].a + 1 > 18 || x - p.hand.get(t).layout[i].a + 1 < 2 || x - p.hand.get(t).layout[i].a - 1 > 18 || x - p.hand.get(t).layout[i].a - 1 < 2
                                                    || y + p.hand.get(t).layout[i].b + 1 > 18 || y + p.hand.get(t).layout[i].b - 1 < 2 || y + p.hand.get(t).layout[i].b + 1 < 2 || y + p.hand.get(t).layout[i].b - 1 > 18)
                                                throw new OutofBoard();
                                            else {
                                                if (board[x - p.hand.get(t).layout[i].a][y + p.hand.get(t).layout[i].b] != 0) {
                                                    count = -1;
                                                    break;
                                                }
                                                if (count == 0)
                                                    if (board[x - p.hand.get(t).layout[i].a + 1][y + p.hand.get(t).layout[i].b] == -1 * pid || board[x - p.hand.get(t).layout[i].a - 1][y + p.hand.get(t).layout[i].b] == -1 * pid || board[x - p.hand.get(t).layout[i].a][y + p.hand.get(t).layout[i].b] + 1 == -1 * pid || board[x - p.hand.get(t).layout[i].a][y + p.hand.get(t).layout[i].b] - 1 == -1 * pid)
                                                        count = 1;
                                            }
                                        }
                                        catch(OutofBoard a){
                                            break;
                                        }
                                    }
                                    if (count != 1) {
                                        if (board[x + 1][y] == -1 * pid || board[x - 1][y] == -1 * pid || board[x][y + 1] == -1 * pid || board[x][y - 1] == -1 * pid)
                                            count++;
                                    }
                                    if (count == 1) {
                                        ch = 1;
                                        break labelA;             //如果存在一种情况可以展开，则break，游戏继续
                                    }
                                }
                            }
                        }
                        else{
                            if (board[x][y] == 0) {
                                for (int t = 0; t < p.hand.size();t++) {
                                    int count = 0;
                                    for (int i = 0; i < 5; i++) {
                                        try {
                                            if (x + p.hand.get(t).layout[i].a > 18 || y + p.hand.get(t).layout[i].b > 18 || y + p.hand.get(t).layout[i].b < 2 || x + p.hand.get(t).layout[i].a < 2
                                                    || x + p.hand.get(t).layout[i].a + 1 > 18 || x + p.hand.get(t).layout[i].a + 1 < 2 || x + p.hand.get(t).layout[i].a - 1 > 18 || x + p.hand.get(t).layout[i].a - 1 < 2
                                                    || y + p.hand.get(t).layout[i].b + 1 > 18 || y + p.hand.get(t).layout[i].b - 1 < 2 || y + p.hand.get(t).layout[i].b + 1 < 2 || y + p.hand.get(t).layout[i].b - 1 > 18)
                                                throw new OutofBoard();
                                            else {
                                                if (board[x - p.hand.get(t).layout[i].a][y + p.hand.get(t).layout[i].b] != 0) {
                                                    count = -1;
                                                    break;
                                                }
                                                if (count == 0)
                                                    if (board[x + p.hand.get(t).layout[i].a + 1][y + p.hand.get(t).layout[i].b] == -1 * pid || board[x + p.hand.get(t).layout[i].a - 1][y + p.hand.get(t).layout[i].b] == -1 * pid || board[x + p.hand.get(t).layout[i].a][y + p.hand.get(t).layout[i].b] == -1 * pid || board[x + p.hand.get(t).layout[i].a][y + p.hand.get(t).layout[i].b] == -1 * pid)
                                                        count = 1;
                                            }
                                        }
                                        catch (OutofBoard a) {
                                            break;
                                        }
                                    }
                                    if (count != 1) {
                                        if (board[x + 1][y] == -1 * pid || board[x - 1][y] == -1 * pid || board[x][y + 1] == -1 * pid || board[x][y - 1] == -1 * pid)
                                            count++;
                                    }
                                    if (count == 1) {
                                        ch = 1;
                                        break labelA;                      //如果存在一种情况可以展开，则break，游戏继续
                                    }
                                }
                            }
                        }
                    }
                }
                if(ch==1)
                    return false;
                else return true;
            }
            else return false;
        }
        //游戏胜利条件设置
    }

    //全局初始化
    public void init() {
        navigation_panel.setText("Welcom to DDM,Now Loading...");
        navigation_panel.setCaretColor(Color.red);
        //初始化怪兽库
        read();
        p1.init();
        p2.init();
        //初始化棋盘
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = 0;    //0表示海洋
                board_M[i][j] = 0;//0表示无怪兽
            }
        }
        for (int i = 0; i < size; i++) {
            board[0][i] = -3;       //-3是不可以进入
            board[i][0] = -3;
            board[i][size - 1] = -3;
            board[size - 1][i] = -3;

            board_M[0][i] = -3;
            board_M[i][0] = -3;
            board_M[i][size - 1] = -3;
            board_M[size - 1][i] = -3;

        }
//            for(int i=1;i<size-1;i++)
//            {
//                board[1][i] = -2;       //-2是红
//                board[size-2][i] = -1;  //-1是蓝
//            }
        //加载卡组
        ArrayList<Monster>vis = new ArrayList<>();
        for(int i=0;i<30;i++)
        {
            int seed = (int) (Math.random() * property.size());
            p1.deck.add(property.get(seed));
            vis.add(property.get(seed));
        }
        int j=0;
        while(j<30)
        {
            int seed = (int)(Math.random()*property.size());
            Monster m1 = property.get(seed);
            int flag =0;
            for(Monster m2:vis){
                if(m2.equals(m1)){
                    flag = 1;
                }
            }
            if(flag == 0){
                p2.deck.add(m1);
                vis.add(m1);
                j++;
            }
        }

        Chest1.x=(int) (Math.random() * 10)+5;                                     //对宝箱的坐标进行定义，宝箱位置随机
        Chest1.y=(int) (Math.random() * 10)+5;
        Chest2.x=(int) (Math.random() * 10)+5;                                     //对宝箱的坐标进行定义，宝箱位置随机
        Chest2.y=(int) (Math.random() * 10)+5;
        board_M[Chest1.x][Chest1.y]=Chest1.getId();
        board_M[Chest2.x][Chest2.y]=Chest2.getId();
        board[Chest1.x][Chest1.y]=-4;
        board[Chest2.x][Chest2.y]=-5;
        p1.target_x = 19;
        p1.target_y = 10;
        p2.target_x = 1;
        p2.target_y = 10;
        board[p1.target_x][p1.target_y] = -2;
        label[p1.target_x][p1.target_y].setBackground(Color.PINK);
        board[p2.target_x][p2.target_y] = -1;
        label[p2.target_x][p2.target_y].setBackground(Color.CYAN);
        //抽卡
        try {
            chou_ka(p1);
        }
        catch (DeckRunOutException e){}
        try {
            chou_ka(p2);
        }
        catch (DeckRunOutException e){}
        update(p1);
    }

    void roll(ArrayList<Monster> tmp, player p) throws Exception {
        navigation_panel.setBackground(Color.green);
        navigation_panel.setText("请选择三只怪兽");
        JOptionPane.showMessageDialog(this,"请使用左右键移动光标选择怪兽，回车键确认","Hint",JOptionPane.INFORMATION_MESSAGE);
        try {
            hand_board_event();
        }catch (Exception e){
            throw  e;
        }

    }


    //召唤怪兽
    public void summon(player owner, Monster m) {
        int x, y, count = 0, p,ch=0;
        String inputValue = JOptionPane.showInputDialog(this,"请输入召唤的坐标");
        if(inputValue == null){
            JOptionPane.showMessageDialog(this,"请重新输入坐标","警告",JOptionPane.WARNING_MESSAGE);
            inputValue = JOptionPane.showInputDialog(this,"请输入召唤的坐标");
        }
        String[]index = inputValue.split(" ");
        x = Integer.valueOf(index[0]);
        y = Integer.valueOf(index[1]);
//        Scanner reader = new Scanner(System.in);
//        x = reader.nextInt();
//        y = reader.nextInt();
        m.setMethod();
        m.setLayout();               //将怪兽的展开方式确定，将得到一个二维数组，分别对应每一个正方形相对展开点的位移偏移量
        if (owner.id == 2) {
            p = 2;
            for (int i = 0; i < 5; i++) {
                try {                          //对展开点进行是否越界判断
                    if (x - m.layout[i].a > 19 || x - m.layout[i].a < 1 || x - m.layout[i].a + 1 > 19 || x - m.layout[i].a + 1 < 1 || x - m.layout[i].a - 1 > 19 || x - m.layout[i].a - 1 < 1
                            || y + m.layout[i].b > 19 || y + m.layout[i].b < 1 || y + m.layout[i].b + 1 > 19 || y + m.layout[i].b - 1 > 19 || y + m.layout[i].b + 1 < 1 || y + m.layout[i].b - 1 < 1)
                        throw new OutofBoard();
                    else {
                        if (board[x - m.layout[i].a][y + m.layout[i].b] != 0 || board[x][y] != 0) {
                            count = -1;                               //如果某个正方形与一个已经展开的区域重合，则展开失败
                            break;
                        }
                        if (count == 0)
                            if (board[x - m.layout[i].a + 1][y + m.layout[i].b] == -1 * p || board[x - m.layout[i].a - 1][y + m.layout[i].b] == -1 * p || board[x - m.layout[i].a][y + m.layout[i].b + 1] == -1 * p || board[x - m.layout[i].a][y + m.layout[i].b - 1] == -1 * p)
                                count = 1;                         //如果展开后区域与己方区域相邻，则满足展开条件，可以展开
                    }
                }
                catch (OutofBoard a){
                    JOptionPane.showMessageDialog(this,"召唤失败"+a.toString(),
                            "召唤失败",JOptionPane.WARNING_MESSAGE);
                    ch=1;
                    break;
                }
            }
            if(ch==0) {
                if (count == 0) {
                    if (board[x + 1][y] == -1 * p || board[x - 1][y] == -1 * p || board[x][y + 1] == -1 * p || board[x][y - 1] == -1 * p)
                        count++;
                }
                if (count == 1) {
                    board_M[x][y] = m.getId();
                    board[x][y] = -1 * p;
                    m.x = x;
                    m.y = y;
                    owner.alive.add(m);
                    for (int i = 0; i < 5; i++) {
                        board[x - m.layout[i].a][y + m.layout[i].b] = -1 * p; //展开后更新棋盘上的信息，显示展开区域与怪兽位置
                    }
                    update(owner);
                    //召唤完成后刷新信息
                } else {
                    if(count==-1)
                        JOptionPane.showMessageDialog(this,"召唤失败,展开位置与现有区域重合",
                                "召唤失败",JOptionPane.WARNING_MESSAGE);    //针对不同的展开失败原因进行提示
                    else
                        JOptionPane.showMessageDialog(this,"召唤失败,展开位置不与己方区域相邻",
                                "召唤失败",JOptionPane.WARNING_MESSAGE);
                    count=0;
                    summon(owner, m);                                             //如果失败，让玩家再次展开
                }
            }
            else{
                count=0;
                summon(owner, m);
            }
        } else {
            p = 1;                                              //由于一号玩家向下展开，二号玩家向上展开，因此函数不同
            for (int i = 0; i < 5; i++) {
                try {
                    if (x + m.layout[i].a > 19 || x + m.layout[i].a < 1 || x + m.layout[i].a + 1 > 19 || x + m.layout[i].a + 1 < 1 || x + m.layout[i].a - 1 > 19 || x + m.layout[i].a - 1 < 1
                            || y + m.layout[i].b > 19 || y + m.layout[i].b < 1 || y + m.layout[i].b + 1 > 19 || y + m.layout[i].b - 1 > 19 || y + m.layout[i].b + 1 < 1 || y + m.layout[i].b - 1 < 1)
                        throw new OutofBoard();
                    else {
                        if (board[x + m.layout[i].a][y + m.layout[i].b] != 0 || board[x][y] != 0) {
                            count = -1;
                            break;
                        }
                        if (count == 0)
                            if (board[x + m.layout[i].a + 1][y + m.layout[i].b] == -1 * p || board[x + m.layout[i].a - 1][y + m.layout[i].b] == -1 * p || board[x + m.layout[i].a][y + m.layout[i].b + 1] == -1 * p || board[x + m.layout[i].a][y + m.layout[i].b - 1] == -1 * p)
                                count = 1;
                    }
                }
                catch (OutofBoard a){
                    JOptionPane.showMessageDialog(this,"召唤失败"+a.toString(),
                            "召唤失败",JOptionPane.WARNING_MESSAGE);
                    ch=1;
                    break;
                }
            }
            if(ch==0) {
                if (count == 0) {
                    if (board[x + 1][y] == -1 * p || board[x - 1][y] == -1 * p || board[x][y + 1] == -1 * p || board[x][y - 1] == -1 * p)
                        count++;
                }
                if (count == 1) {
                    board_M[x][y] = m.getId();
                    board[x][y] = -1 * p;
                    m.x = x;
                    m.y = y;
                    owner.alive.add(m);
                    for (int i = 0; i < 5; i++) {
                        board[x + m.layout[i].a][y + m.layout[i].b] = -1 * p;
                    }
                    update(owner);
                    //召唤完成后刷新信息
                } else {
                    if(count==-1)
                        JOptionPane.showMessageDialog(this,"召唤失败,展开位置与现有区域重合",
                                "召唤失败",JOptionPane.WARNING_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(this,"召唤失败,展开位置不与己方区域相邻",
                                "召唤失败",JOptionPane.WARNING_MESSAGE);
                    count = 0;
                    summon(owner, m);
                }
            }
            else{
                count=0;
                summon(owner, m);
            }
        }
        for(int i=0;i<tmp.size();i++){
            currentplayer.deck.add(tmp.get(i));
        }
        tmp.clear();
    }

    public void Attack(player p, player opponent) throws WinnerException {
        String cmd;
//        System.out.println(p.name + "可以使用的怪兽有：");
        if(p.Mp<2){
            JOptionPane.showMessageDialog(this,"行动力不足，攻击需要2点行动力","警告",JOptionPane.WARNING_MESSAGE);
            return;
        }
        ArrayList<Object> options = new ArrayList<>();
        for (Monster m : p.alive) {
            options.add(m.name);
        }
        options.add("取消");
//        System.out.println();
//        System.out.println("请选择你要使用的怪兽：");
        int response = JOptionPane.showOptionDialog(null,"请选择你想使用的怪兽","怪兽选择",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options.toArray(),options.toArray()[0]);
        ArrayList<Monster> targets = new ArrayList<Monster>();
        ArrayList<Object>  option2 = new ArrayList<Object>();
        Monster attacker = new Monster();
        if(!options.toArray()[response].equals("取消")) {
            p.Mp -= 2;
            update(p);
            cmd = (String) options.toArray()[response];
            for (Monster m : p.alive) {
                if (m.name.equals(cmd)) {
                    attacker = m;
//                    System.out.println("你可进行攻击的目标有：");
                    JOptionPane.showConfirmDialog(this,m.name+" 攻击力:"+m.Atk+"\r\nID:"+m.getId()+"\r\n生命值:"+m.Hp+"\r\n坐标:("+m.x+","+m.y+")","怪兽信息确认",JOptionPane.YES_NO_OPTION);
                    int x = m.x;
                    int y = m.y;
                    if (p.id == 1) {
                        {
                            if (x == 18 && y == 10 || x == 19 && y == 9 || x == 20 && y == 11) {
                                option2.add(opponent.name);
                            }
                        }
                    }
                    else{
                        if( x== 2&& y == 10 || x==1 && y==9 || x==3 && y==11){
                            option2.add(opponent.name);
                        }
                    }
                    if (board_M[x - 1][y] > 0) {
                        for (Monster e : opponent.alive) {
                            if (board_M[x - 1][y] == e.getId()) {
                                targets.add(e);
                                option2.add(e.name);
                            }
                        }
                        if(board[x-1][y]==-4){                    //由于宝箱不属于任何一名玩家，需要借助一个特殊值单独判断
                            targets.add(Chest1);
                            option2.add("宝箱一号");
                        }
                        if(board[x-1][y]==-5){                    //由于宝箱不属于任何一名玩家，需要借助一个特殊值单独判断
                            targets.add(Chest2);
                            option2.add("宝箱二号");
                        }
                    }

                    if (board_M[x][y - 1] > 0) {
                        for (Monster e : opponent.alive) {
                            if (board_M[x][y - 1] == e.getId()) {
                                targets.add(e);
                                option2.add(e.name);
//                                System.out.print(e.name + "\\");
                            }
                        }
                        if(board[x][y-1]==-4){
                            targets.add(Chest1);
                            option2.add("宝箱一号");
                        }
                        if(board[x][y-1]==-5){                    //由于宝箱不属于任何一名玩家，需要借助一个特殊值单独判断
                            targets.add(Chest2);
                            option2.add("宝箱二号");
                        }
                    }

                    if (board_M[x + 1][y] > 0) {
                        for (Monster e : opponent.alive) {
                            if (board_M[x + 1][y] == e.getId()) {
                                targets.add(e);
                                option2.add(e.name);
//                                System.out.print(e.name + "\\");
                            }
                        }
                        if(board[x+1][y]==-4){
                            targets.add(Chest1);
                            option2.add("宝箱一号");
                        }
                        if(board[x+1][y]==-5){                    //由于宝箱不属于任何一名玩家，需要借助一个特殊值单独判断
                            targets.add(Chest2);
                            option2.add("宝箱二号");
                        }
                    }

                    if (board_M[x][y + 1] > 0) {
                        for (Monster e : opponent.alive) {
                            if (board_M[x][y + 1] == e.getId()) {
                                targets.add(e);
                                option2.add(e.name);
//                                System.out.print(e.name + "\\");
                            }
                        }
                        if(board[x][y+1]==-4){
                            targets.add(Chest1);
                            option2.add("宝箱一号");
                        }
                        if(board[x][y+1]==-5){                    //由于宝箱不属于任何一名玩家，需要借助一个特殊值单独判断
                            targets.add(Chest2);
                            option2.add("宝箱二号");
                        }
                    }
                    break;
                }
            }
            option2.add("取消");
            int response2= JOptionPane.showOptionDialog(null,"选择攻击目标","攻击目标选择",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,option2.toArray(),option2.toArray()[0]);
//            System.out.println("请选择攻击的目标（输入名称）按Q退出：");
            if(!option2.toArray()[response2].equals("取消")) {
                cmd = (String) option2.toArray()[response2];
                if (cmd.equals(opponent.name)) {
                    opponent.Hp--;
                    JOptionPane.showConfirmDialog(this,opponent.name+"被"+attacker.name+"狠狠揍了一顿，生命值-1","痛击",JOptionPane.INFORMATION_MESSAGE);
                    if (check(opponent)) throw new WinnerException();
                }else if(cmd.equals("宝箱一号")){
                    JOptionPane.showConfirmDialog(this,Chest1.name+" 攻击力:"+Chest1.Atk+"\r\n生命值:"+Chest1.Hp+"\r\n坐标:("+Chest1.x+","+Chest1.y+")","怪兽信息确认",JOptionPane.YES_NO_OPTION);
                    Chest1.Hp-=attacker.Atk;
                    if(Chest1.Hp<=0){
                        board_M[Chest1.x][Chest1.y]=0;
                        board[Chest1.x][Chest1.y]=0;
                        update(p);
                        if(Chest1.getId()==200){                              //如果玩家通过攻击打开宝箱，提示其所获得技能
                            p.hidden=1;
                            JOptionPane.showMessageDialog(null, "成功打开宝箱，获得技能：传送，可将一个怪兽传送至任意一块土地，使用次数一次，消耗行动力30", "Surprise！", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else if(Chest1.getId()==300){
                            p.hidden=2;
                            JOptionPane.showMessageDialog(null, "成功打开宝箱，获得技能:爆破鬼才,可自爆一只怪兽，杀死其周围所有怪兽（3*3区域），并破坏土地，使用次数一次，消耗行动力20", "Surprise！", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
                else if(cmd.equals("宝箱二号")){
                    JOptionPane.showConfirmDialog(this,Chest2.name+" 攻击力:"+Chest2.Atk+"\r\n生命值:"+Chest2.Hp+"\r\n坐标:("+Chest2.x+","+Chest2.y+")","怪兽信息确认",JOptionPane.YES_NO_OPTION);
                    Chest2.Hp-=attacker.Atk;
                    if(Chest2.Hp<=0){
                        board_M[Chest2.x][Chest2.y]=0;
                        board[Chest2.x][Chest2.y]=0;
                        update(p);
                        if(Chest2.getId()==200){                              //如果玩家通过攻击打开宝箱，提示其所获得技能
                            p.hidden=1;
                            JOptionPane.showMessageDialog(null, "成功打开宝箱，获得技能：传送，可将一个怪兽传送至任意一块土地，使用次数一次，消耗行动力30", "Surprise！", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else if(Chest2.getId()==300){
                            p.hidden=2;
                            JOptionPane.showMessageDialog(null, "成功打开宝箱，获得技能:爆破鬼才,可自爆一只怪兽，杀死其周围所有怪兽（3*3区域），并破坏土地，使用次数一次，消耗行动力20", "Surprise！", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
                else {
                    for (Monster e : opponent.alive) {
                        if (e.name.equals(cmd)) {
                            JOptionPane.showConfirmDialog(this,e.name+" 攻击力:"+e.Atk+"\r\nID:"+e.getId()+"\r\n生命值:"+e.Hp+"\r\n坐标:("+e.x+","+e.y+")","怪兽信息确认",JOptionPane.YES_NO_OPTION);
                            e.Hp -= attacker.Atk;
                            attacker.Hp -= e.Atk;
                            if (e.Hp <= 0) {
//                                if (opponent.id == 1) board[e.x][e.y] = -1;
//                                else board[e.x][e.y] = -2;
                                board_M[e.x][e.y] = 0;
                                opponent.alive.remove(e);
                                update(p);
                                if (check(opponent)) throw new WinnerException();
                            }
                            if(attacker.Hp<=0){
                                board_M[attacker.x][attacker.y] = 0;
                                currentplayer.alive.remove(attacker);
                                update(p);
                            }
                            break;
                        }
                    }
                }
                attacker.Atk_state = false;
                update(p);
            }
        }

    }


    boolean Isaccesible(Pair<Integer,Integer>p_tmp){
        int x = p_tmp.getKey();
        int y = p_tmp.getValue();
        if(x>=1 && x<=19 && y>=1 && y<=19){
            if(board[x][y]>-3 && board[x][y]<0){
                return true;
            }
        }
        return false;
    }
    boolean isConencted(int x1,int y1,int x2,int y2){
        Queue<Pair>Q = new LinkedList<Pair>();
        Pair ps = new Pair<Integer,Integer>(x1,y1);
        Pair pt = new Pair<Integer,Integer>(x2,y2);
        boolean [][]vis = new boolean[size][size];

        int [][]dir = {{0,1},{1,0},{0,-1},{-1,0}};
        Q.offer(new Pair<Integer,Integer>(x1,y1));
        vis[(int)ps.getKey()][(int)ps.getValue()] = true;
        while(!Q.isEmpty())
        {
            Pair p_tmp = Q.poll();
            for(int i=0;i<4;i++)
            {
                Pair p_tmp2 = new Pair<Integer,Integer>((int)p_tmp.getKey()+dir[i][0],(int)p_tmp.getValue()+dir[i][1]);
                if(p_tmp2.equals(pt)){
                    cnt++;
                    return true;
                }
                else if(Isaccesible(p_tmp2)&&!vis[(int)p_tmp2.getKey()][(int)p_tmp2.getValue()]){
                    cnt++;
                    Q.offer(p_tmp2);
                    vis[(int)p_tmp2.getKey()][(int)p_tmp2.getValue()] = true;
                }
            }
        }
        return false;
    }




    public void Move(player p) throws Exception {
//        Scanner in = new Scanner(System.in);
        int move = p.Mp;

//        System.out.println("可选择移动的怪兽(怪兽名 目标坐标x 目标坐标y）");
        ArrayList<Object> options = new ArrayList<Object>();
        for (Monster m : p.alive) {
            options.add(m.name);
        }
        options.add("取消");
        int response = JOptionPane.showOptionDialog(this,"选择怪兽","移动目标选择",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options.toArray(),options.toArray()[0]);
//        System.out.println();
        if(!options.toArray()[response].equals("取消")) {
            String cmd = (String)options.toArray()[response];
            Monster monster = new Monster();
            for (Monster m : p.alive) {
                if (cmd.equals(m.name)) {
                    monster = m;
                    break;
                }
            }
            int s=JOptionPane.showConfirmDialog(this,monster.name+" 攻击力:"+monster.Atk+"\r\nID:"+monster.getId()+"\r\n生命值:"+monster.Hp+"\r\n坐标:("+monster.x+","+monster.y+")","确认怪兽信息",JOptionPane.YES_NO_OPTION);
            if(s== 0) {
                cnt = 0;
                String inputValue = JOptionPane.showInputDialog(this, "请输入目标位置的坐标");
                String[] index = inputValue.split(" ");
                int x = Integer.valueOf(index[0]);
                int y = Integer.valueOf(index[1]);
                try {
                    if (board[x][y] >= 0 || board_M[x][y] > 0) {
                        JOptionPane.showMessageDialog(this, "该地点无法进入", "警告", JOptionPane.WARNING_MESSAGE);
                        throw new conflictException1();
                    } else if (!isConencted(monster.x,monster.y,x,y)) {
                        JOptionPane.showMessageDialog(this, "无法到达该地点", "警告", JOptionPane.WARNING_MESSAGE);
                        cnt =0;
                    }
                    else {
                        if(cnt > p.Mp)JOptionPane.showMessageDialog(this,"行动力不足","警告",JOptionPane.WARNING_MESSAGE);
                        else {
                            board_M[monster.x][monster.y] = 0;
                            p.Mp -= cnt;
                            monster.x = x;
                            monster.y = y;
                            board_M[x][y] = monster.getId();
                            JOptionPane.showMessageDialog(this, monster.name + "移动成功", "info", JOptionPane.INFORMATION_MESSAGE);
                            update(p);
                        }
                    }
                } catch (conflictException1 a) {
                    System.out.println(a.toString());
                }
            }
        }
    }



    public void update(player current_player) {
        //展示棋盘信息
        //初始化棋盘

        for (int i = 1; i < size - 1; i++) {
            for (int j = 1; j < size - 1; j++) {
                if ((i + j) % 2 == 0) {
                    label[i][j].setBackground(Color.black);
                    if (board_M[i][j] < 0) {
                        label[i][j].setText("");
                        label[i][j].setSize(20,20);
                    }
                } else {
                    label[i][j].setBackground(Color.white);
                    if (board_M[i][j] < 0) {
                        label[i][j].setText("");
                    }
                }
            }
        }

        for (int i = 1; i < size - 1; i++) {
            for (int j = 1; j < size - 1; j++) {
                if (board[i][j] == 0) {
                } else if (board[i][j] == -p1.id) {
                    if (board_M[i][j] > 0) {
                        for (Monster m : property) {
                            if (m.getId() == board_M[i][j]) {
                                label[i][j].setText(Integer.toString(m.getId()));
                                label[i][j].setBackground(Color.blue);
                            }
                        }
                    } else {
                        label[i][j].setBackground(Color.blue);
                        label[i][j].setText("");
                    }
                } else if (board[i][j] == -p2.id) {
                    if (board_M[i][j] > 0) {
                        for (Monster m : property) {
                            if (m.getId() == board_M[i][j]) {
                                label[i][j].setText(Integer.toString(m.getId()));
                                label[i][j].setBackground(Color.red);
                                label[i][j].setSize(20,20);
                            }
                        }
                    } else {
                        label[i][j].setBackground(Color.red);
                        label[i][j].setText("");
                    }
                }else if(board[i][j]==-4||board[i][j]==-5){
                    label[i][j].setText("C");                                        //宝箱状态单独判断与刷新
                    label[i][j].setBackground(Color.yellow);
                    label[i][j].setSize(20,20);
                }
                else {
                    for (Monster m : property) {
                        if (m.getId() == board[i][j]) {
                            label[i][j].setText(Integer.toString(m.getId()));
                        }
                    }
                }
            }
        }

        label[p1.target_x][p1.target_y].setBackground(Color.PINK);
        label[p2.target_x][p2.target_y].setBackground(Color.CYAN);
        //展示p1信息
        p1_info.setText(p1.name + "(" + p1.color + ")" + "目前的状态是：\r\n" + "Hp:" + p1.Hp + " 行动点" + p1.Mp + " 手牌数"+p1.hand.size()+" 卡组剩余卡牌:"+p1.deck.size());
        p2_info.setText(p2.name + "(" + p2.color + ")" + "目前的状态是：\r\n" + "Hp:" + p2.Hp + " 行动点" + p2.Mp + " 手牌数" + p2.hand.size()+" 卡组剩余卡牌:"+p2.deck.size());

        //设置图区
        //pic_field.setIcon(new ImageIcon("./pic_img/pic_1.jpg"));
        //设置手牌
        int i = 1;
        for (Monster m : current_player.hand) {
            try {
                hand_cards[i].setIcon(m.pic_hand);
                // hand_cards[i].setBorder(BorderFactory.createLineBorder(Color.green));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            i++;
        }
        for (int j = i; j <= 6; j++) {
            try {
                hand_cards[i].setIcon(new ImageIcon("./hand_img/0.jpg"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void info_board_update(String s) {
        info_board.setText(s);
    }

    void info_update(){
        p1_info.setText(p1.name + "(" + p1.color + ")" + "目前的状态是：\n" + "Hp:" + p1.Hp + " 行动点" + p1.Mp  + " 手牌数" + p1.hand.size());
        p2_info.setText(p2.name + "(" + p2.color + ")" + "目前的状态是：\n" + "Hp:" + p2.Hp + " 行动点" + p2.Mp  + " 手牌数" + p2.hand.size());
    }

    public void pic_field_update(ImageIcon i) {
        pic_field.setIcon(i);
    }

    public void hand_field_update(int curse_x) {
        Monster m = currentplayer.hand.get(curse_x-1);
        hand_cards[curse_x].setBorder(BorderFactory.createLineBorder(Color.green));
        pic_field_update(m.pic_big);
        info_board_update("<html><h3>"+m.name+"</h3><br>"+"<h3>怪兽id："+m.getId()+"</h3><br>"+m.Description+"</html>");
        for(int i=1;i<=6;i++)
        {
            if(i!=curse_x)hand_cards[i].setBorder(BorderFactory.createLineBorder(Color.black));
        }
    }


    //游戏结束画面
    private void end(player p) {
        JOptionPane.showMessageDialog(this,"Game Over!The Winner is" + p.name,"游戏结束",JOptionPane.INFORMATION_MESSAGE);
        navigation_panel.setBackground(Color.yellow);
        navigation_panel.setText("Game Over!The Winner is" + p.name);
    }

    //游戏流程
    public void run() {
        currentplayer = p1;
        opponent = p2;
        buttonlist_event();
    }
    //职业技能与宝箱魔法
    public void Priest(player p){
        if(p.Mp<5)                                                         //如果行动点数不足以让玩家使用技能，则报错
            JOptionPane.showMessageDialog(this,"行动点数不够","技能使用失败",JOptionPane.WARNING_MESSAGE);
        else {
            ArrayList<Object> options = new ArrayList<Object>();
            for (Monster m : p.alive) {
                options.add(m.name);
            }                                                              //恢复生命的技能，让玩家选择一只怪兽，恢复其生命
            int response = JOptionPane.showOptionDialog(null, "你将使用牧师技能，请选择你要治疗的怪兽", "使用技能", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options.toArray(), options.toArray()[0]);
            p.alive.get(response).Hp+=20;
            p.Mp -= 5;
            update(p);
            JOptionPane.showMessageDialog(null,p.alive.get(response).name+"的生命值增加为"+p.alive.get(response).Hp,"技能使用结果",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public void constructer(player p){
        if(p.Mp<7)
            JOptionPane.showMessageDialog(this,"行动点数不够","技能使用失败",JOptionPane.WARNING_MESSAGE);
        else {
            int x, y;
            String inputValue = JOptionPane.showInputDialog("请输入要填充的土地坐标");
            String[] index = inputValue.split(" ");
            if(index!=null) {
                x = Integer.valueOf(index[0]);
                y = Integer.valueOf(index[1]);
                if(x<1||x>19||y<1||y>19||board[x][y]!=0) {                                  //判断要填充的土地是否有效,若无效则要求玩家更改坐标
                    JOptionPane.showMessageDialog(null, "无效地址，请重新输入", "技能使用失败", JOptionPane.WARNING_MESSAGE);
                    constructer(p);
                }
                else {
                    board[x][y] = -1 * p.id;                                             //更新土地状态与玩家行动点数
                    p.Mp -= 7;
                    update(p);
                    JOptionPane.showMessageDialog(null, "技能使用成功", "技能使用结果", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
    public void singer(player p){
        if(p.Mp<5)
            JOptionPane.showMessageDialog(this,"行动点数不够","技能使用失败",JOptionPane.WARNING_MESSAGE);
        else {
            ArrayList<Object> options = new ArrayList<Object>();
            for (Monster m : p.alive) {
                options.add(m.name);
            }                                                                       //增加攻击力的技能，原理与加血类似
            int response = JOptionPane.showOptionDialog(null, "你将使用歌颂者技能，请选择你要强化的怪兽", "使用技能", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options.toArray(), options.toArray()[0]);
            p.alive.get(response).Atk += 20;
            p.Mp-=5;
            update(p);
            JOptionPane.showMessageDialog(null,p.alive.get(response).name+"的攻击力增加为"+p.alive.get(response).Atk,"技能使用结果",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public void shaman(player p1,player p2){
        if(p1.Mp<10)
            JOptionPane.showMessageDialog(this,"移动点数不够","技能使用失败",JOptionPane.WARNING_MESSAGE);
        else {
            ArrayList<Object> options = new ArrayList<Object>();
            for (Monster m : p1.alive) {
                options.add(m.name);
            }
            int response = JOptionPane.showOptionDialog(null, "你将使用萨满技能，请选择你想献祭的怪兽", "使用技能", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options.toArray(), options.toArray()[0]);
            int t =0;
            p1.alive.get(response).Hp = 0;                                      //献祭的技能使己方怪兽死亡
            for (int i = 0; i < p2.alive.size(); i++) {
                if (p2.alive.get(i).y == p1.alive.get(response).y) {
                    p2.alive.get(i).Hp -= p1.alive.get(response).Atk;          //对同一列的对方怪兽造成伤害
                    if (p2.alive.get(i).Hp <= 0) {
                        board_M[p2.alive.get(i).x][p2.alive.get(i).y]=0;
                        p2.alive.remove(i--);                                   //如果死亡，则需要将其从棋盘上移除
                        t++;
                    }
                }
            }
            board_M[p1.alive.get(response).x][p1.alive.get(response).y]=0;
            p1.alive.remove(response);
            p1.Mp-=10;
            update(p1);
            if(t>0)
                JOptionPane.showMessageDialog(null, "技能使用成功,消灭了对方"+t+"只怪兽,希望你的怪兽死的有价值", "技能使用结果", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public void portal(player p){
        if(p.Mp<30)
            JOptionPane.showMessageDialog(this,"行动点数不够","技能使用失败",JOptionPane.WARNING_MESSAGE);
        else{
            ArrayList<Object> options = new ArrayList<Object>();
            for (Monster m : p.alive) {
                options.add(m.name);
            }
            int response = JOptionPane.showOptionDialog(null, "你将使用传送技能，请选择你想传送的怪兽", "使用技能", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options.toArray(), options.toArray()[0]);
            int x, y;
            String inputValue = JOptionPane.showInputDialog("请输入要传送到的土地坐标");
            String[] index = inputValue.split(" ");
            if(index!=null) {
                x = Integer.valueOf(index[0]);
                y = Integer.valueOf(index[1]);
                if(x<1||x>19||y<1||y>19||board[x][y]==0) {
                    JOptionPane.showMessageDialog(null, "无效地址，请重新输入", "技能使用失败", JOptionPane.WARNING_MESSAGE);
                    constructer(p);
                }
                else {
                    board_M[p.alive.get(response).x][p.alive.get(response).y]=0;        //怪兽原来的位置不在有怪兽
                    p.alive.get(response).x=x;                                            //更新怪兽坐标
                    p.alive.get(response).y=y;
                    board_M[x][y]=p.alive.get(response).getId();                          //新的位置上拥有怪兽
                    p.Mp-=30;
                    p.hidden=0;                                                             //此技能只能使用一次
                    update(p);
                    JOptionPane.showMessageDialog(null, "传送成功！", "技能使用结果", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
    public void explosion(player p1,player p2){
        if(p1.Mp<20)
            JOptionPane.showMessageDialog(this,"行动点数不够","技能使用失败",JOptionPane.WARNING_MESSAGE);
        else {
            ArrayList<Object> options = new ArrayList<Object>();
            for (Monster m : p1.alive) {
                options.add(m.name);
            }
            int response = JOptionPane.showOptionDialog(null, "你将使用爆破鬼才技能，请选择你想自爆的怪兽", "使用技能", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options.toArray(), options.toArray()[0]);
            for(int i=p1.alive.get(response).x-1;i<=p1.alive.get(response).x+1;i++){
                for(int j=p1.alive.get(response).y-1;j<=p1.alive.get(response).y+1;j++){
                    if(board_M[i][j]>0){
                        for (Monster e : p1.alive) {                                 //将选定怪兽的周围区域（3*3）里的所有怪兽定义为死亡，从棋盘上移去
                            if (board_M[i][j] == e.getId()) {
                                p1.alive.remove(e);
                                board_M[i][j]=board[i][j]=0;
                            }
                        }
                        for (Monster e : p2.alive) {
                            if (board_M[i][j] == e.getId()) {
                                p2.alive.remove(e);
                                board_M[i][j]=board[i][j]=0;
                            }
                        }
                    }
                }
            }
            p1.Mp-=20;
            p1.hidden=0;
            update(p1);
            update(p2);
            JOptionPane.showMessageDialog(null, "技能使用成功,希望你的怪兽死的有价值", "技能使用结果", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public void pick(player p){                                                  //职业选择页面，同时每个选项会介绍技能
        Object[] options={"医疗兵","拉拉队员","萨满祭司","建筑工人"};
        int response = JOptionPane.showOptionDialog(null,"请"+p.id+"号玩家选择你的角色","角色选择",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
        p.characterid=response;
        if(p.characterid==0){
            Object[] options0={"确定","取消"};
            int response0 = JOptionPane.showOptionDialog(null,"医疗兵的技能为：为你的一只怪兽恢复二十点生命，消耗移动点数5","医疗兵",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options0,options0[0]);
            if(response0==1)                                                        //如果点取消，让玩家再次选择
                pick(p);
        }
        else if(p.characterid==1){
            Object[] options0={"确定","取消"};
            int response0 = JOptionPane.showOptionDialog(null,"拉拉队员的技能为：为你的一只怪兽增加二十点攻击，消耗移动点数5","拉拉队员",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options0,options0[0]);
            if(response0==1)
                pick(p);
        }
        else if(p.characterid==2){
            Object[] options0={"确定","取消"};
            int response0 = JOptionPane.showOptionDialog(null,"萨满祭司的技能为：消灭自己的一只怪兽，对一整列上对方怪兽造成等同于其攻击力的伤害，消耗移动点数10","萨满祭司",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options0,options0[0]);
            if(response0==1)
                pick(p);
        }
        else if(p.characterid==3){
            Object[] options0={"确定","取消"};
            int response0 = JOptionPane.showOptionDialog(null,"建筑工人的技能为：填充某一块陆地，使其可以行走，消耗移动点数7","建筑工人",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options0,options0[0]);
            if(response0==1)
                pick(p);
        }
    }

    public void show_rule(){
        File f=new File("src/GameRule.txt");
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(f);

            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String msg="";
            String s1;
            while ( ((s1 = bufferedReader.readLine()) != null))
            {
                msg = msg+"\n"+s1;
            }
            JOptionPane.showMessageDialog(null,msg,"欢迎来到DDM的世界",JOptionPane.INFORMATION_MESSAGE);
        }catch (Exception e) {
        }

    }
}