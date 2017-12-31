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
    /**
     * 游戏属性配置
     */
    final int size = 21;//游戏棋盘大小

    //窗口部分
    JTextField navigation_panel = new JTextField();//导航栏，位于窗口最上方
    JLabel p1_info = new JLabel();//p1玩家状态栏，位于导航栏下方
    JLabel p2_info = new JLabel();//p2玩家状态栏，位于导航栏下方
    JLabel pic_field = new JLabel();//卡图显示栏
    JLabel info_board = new JLabel();//卡牌信息显示栏
    JPanel battle_field = new JPanel();//棋盘显示区
    JPanel hand_board = new JPanel();//手牌显示区
    JPanel buttons_panel = new JPanel();//按钮显示区
    JLabel hand_cards[] = new JLabel[7];//手牌显示对象
    JButton []button_list = new JButton[5];//按钮显示对象
    Label label[][]=new Label[size][size];//棋盘格子显示对象

    //游戏部分
    player p1 = new player("Tom",1,"蓝");
    player p2 = new player("Jim",2,"红");//初始化玩家
    int board[][] = new int[size][size];//棋盘陆地和海洋分布数组
    int board_M[][] = new int[size][size];//棋盘怪兽分布数组
    ArrayList<Monster> property = new ArrayList<Monster>();//怪兽库，存放所有的怪兽
    ArrayList<Monster> tmp = new ArrayList<Monster>();//待投掷区，所有选中的待投掷怪兽将存放在这里


}

public class GridBagDemo extends JFrame implements gameconfig {
    int curse_x = 1;//手牌区光标
    int cnt;
    player currentplayer;//当前玩家
    player opponent;//对手玩家
    JFrame frame = this;

    public static void main(String[] args) {

        GridBagDemo demo = new GridBagDemo();//实例化对象
    }

    public GridBagDemo() {
        frame_init();//显示界面初始化
        init();//游戏数据初始化
        run();//运行游戏
    }


    public void frame_init() {
        /**
         * 显示界面
         */
        navigation_panel.setEditable(false);//导航栏值用于显示信息，所以设置为不可编辑
        navigation_panel.setBackground(Color.CYAN);
        navigation_panel.setForeground(Color.red);//更改一下颜色
        p1_info.setText("");
        p2_info.setText("");//p1,p2信息栏初始为空
        ImageIcon i1 = new ImageIcon("./pic_img/pic_1.jpg");
        pic_field.setIcon(i1);//图片显示区加载一张图片起到占位的作用
        info_board.setHorizontalAlignment(JLabel.LEFT);//信息显示栏设置左对齐
        for (int i = 1; i <= 6; i++) {
            //初始化手牌区显示对象
            hand_cards[i] = new JLabel();
            hand_cards[i].setSize(20, 20);
            ImageIcon icon = new ImageIcon("./hand_img/0.jpg");//0.jpg是卡背的图片，这样做是为了让初始手牌都为空
            hand_cards[i].setIcon(icon);
            hand_board.add(hand_cards[i]);
        }
        JPanel blank1 = new JPanel();
        JPanel blank2 = new JPanel();//设置两个空的panel用于占位
        show_rule();//在游戏开始前先显示一下规则
        pick(p1);//p1选择职业
        pick(p2);//p2选择职业

        //初始化按钮区
        String conts[] = {"攻击", "魔法", "投掷", "移动", "结束"};
        for (int i = 0; i < 5; i++) {
            button_list[i] = new JButton(conts[i]);
            buttons_panel.add(button_list[i]);
        }

        //初始化棋盘区
        GridLayout grid1 = new GridLayout(size - 1, size - 1);
        battle_field.setLayout(grid1);//棋盘区采用Gridlayout实现密布局
        for (int i = 1; i < size - 1; i++) {
            for (int j = 1; j < size - 1; j++) {
                label[i][j] = new Label();  //初始化各个格子对象
                label[i][j].setSize(20, 20);
                if ((i + j) % 2 == 0) {
                    label[i][j].setBackground(Color.black);
                    label[i][j].setForeground(Color.yellow);
                    label[i][j].setAlignment(Label.CENTER);
                } else {
                    label[i][j].setBackground(Color.white);
                    label[i][j].setForeground(Color.yellow);
                    label[i][j].setAlignment(Label.CENTER);
                }
                battle_field.add(label[i][j]);
            }
        }

        //界面布局管理，这次主要采用gridbaglayout,因为每个空件占用的格子数可能不同
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        this.add(navigation_panel);
        this.add(p1_info);
        this.add(p2_info);
        this.add(pic_field);
        this.add(battle_field);
        this.add(blank1);
        this.add(info_board);
        this.add(hand_board);
        this.add(blank2);
        this.add(buttons_panel);

        //采用逐个填充，逐个限制的方式
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
        layout.setConstraints(blank2, s);//小trick，空panel占位，抵消

        s.gridwidth = 0;
        s.weightx = 1;
        s.weighty = 0;
        layout.setConstraints(buttons_panel, s);


        //加载事件
        //手牌区光标系统
        hand_board.addKeyListener(new KeyAdapter() {
            //启动键盘监听
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (curse_x > 1) {//判断操作是否合法
                        curse_x--;
                        hand_field_update(curse_x);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (curse_x < 6) {
                        curse_x++;
                        hand_field_update(curse_x);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    //回车键表示确认，连续执行三次
                    Monster m = currentplayer.hand.get(curse_x - 1);
                    tmp.add(m);//将手牌中的怪兽放入待投掷区
                    currentplayer.hand.remove(m);
                    update(currentplayer);//更新界面
                    cnt++;

                    if (cnt == 3) {
                        navigation_panel.setText("开始投掷骰子");//如果已经选好3个骰子，那么开始投掷
                        try {
                            Thread.sleep(500);//线程暂停0.5秒，模拟投色子的延迟
                        } catch (Exception e1) {
                        }
                        String[] result = new String[3];//用一个字符串数组保存投掷结果
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
                        JOptionPane.showMessageDialog(frame, result[0] + result[1] + result[2], "确认投掷结果", JOptionPane.INFORMATION_MESSAGE);
                        ArrayList<Object> options = new ArrayList<>();
                        for (Monster mst : tmp) {
                            if (mst.summon_pre) {
                                options.add(mst.name);
                            }
                        }
                        options.add("取消");
                        int response = JOptionPane.showOptionDialog(frame, "请选择你想召唤的怪兽", "怪兽选择", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options.toArray(), options.toArray()[0]);
                        if (response < tmp.size()) {
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


    void buttonlist_event() {
        /**
         * 按钮区监听
         */
        button_list[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Attack(currentplayer, opponent);
                } catch (WinnerException e1) {
                    end(currentplayer);
                }
            }
        });
        button_list[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //对于魔法，根据不同角色选取不同函数
                if (currentplayer.characterid == 0)
                    Priest(currentplayer);
                if (currentplayer.characterid == 1)
                    singer(currentplayer);
                if (currentplayer.characterid == 2)
                    shaman(currentplayer, opponent);
                if (currentplayer.characterid == 3)
                    constructer(currentplayer);
            }
        });

        button_list[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    roll(tmp, currentplayer);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    System.out.println(e2.getMessage());
                }
            }
        });

        button_list[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Move(currentplayer);
                } catch (Exception e1) {
                    System.out.println(e1.getMessage());
                }
            }
        });

        button_list[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    next_player();
                } catch (Exception e1) {
                    System.out.println(e1.getMessage());
                }
            }
        });
    }

    public void next_player() {
        //交换角色
        player tmp_player = currentplayer;
        currentplayer = opponent;
        opponent = tmp_player;
        curse_x = 1;//将光标重新放在最左边，防止数组越界
        navigation_panel.setText("现在是" + currentplayer.name + "的回合");
        try {
            chou_ka(currentplayer);//回合开始时抽卡
            update(currentplayer);//更新界面
        } catch (Exception e) {
            end(opponent);
        }
    }

    void hand_board_event() {
        //进入选择界面
        cnt = 0;
        //先更新
        hand_field_update(curse_x);
        hand_board.setFocusable(true);//一个小技巧，改变程序的焦点，而不再写一个程序
        hand_board.requestFocus();

    }

    public void read() {
        //读取文件库
        String s1, s2[];
        int ints[] = {0, 0, 0, 0, 0, 0}, dice[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        File f = new File("src/Monster1.txt");
        try {
            InputStream inputStream = new FileInputStream(f);
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            s1 = null;
            try {
                int j = 0;
                while ((s1 = bufferedReader.readLine()) != null) {
                    s2 = s1.split(" ");
                    for (int i = 1; i <= 5; i++) {
                        ints[i] = Integer.valueOf(s2[i]);
                    }
                    property.add(new Monster(s2[0], ints[1], ints[2], ints[3], ints[4], ints[5], s2[6]));
                    for (int i = 0; i < 6; i++) {
                        if (i < 5 - ints[2]) {
                            dice[2 * i] = 1;
                            dice[2 * i + 1] = 0;
                        } else {
                            dice[2 * i] = 2;
                            int d = (int) (Math.random() * 10);
                            dice[2 * i + 1] = d;
                        }
                    }
                    property.get(j++).setFace(dice);
                }
            } catch (IOException e) {
            }
        } catch (FileNotFoundException a) {
        }
    }

    public class DeckRunOutException extends Exception {
        public String toString() {
            return "没有多余的卡牌了";
        }
    }

    public void chou_ka(player p) throws DeckRunOutException {
        if (p.deck.size() == 0) {//如果卡组没卡的话直接结束游戏
            throw new DeckRunOutException();
        }
        if (p.deck.size() >= 3) {
            for (int i = 0; i < 3; i++) {//如果卡组的卡大于三张的话随机抽三张
                int seed = (int) (Math.random() * p.deck.size());
                p.hand.add(p.deck.get(seed));
                p.deck.remove(p.deck.get(seed));
            }
        } else {
            for (int i = 0; i < p.deck.size(); i++) {//在0到3张就全部加入卡组
                int seed = (int) (Math.random() * p.deck.size());
                p.hand.add(p.deck.get(seed));
                p.deck.remove(p.deck.get(seed));
            }
        }
    }

    class WinnerException extends Exception {
    }

    class conflictException1 extends Exception {
        public String toString() {
            return "该目标地点无法进入，请重新输入";
        }
    }

    class conflictException2 extends Exception {
        public String toString() {
            return super.toString();
        }
    }

    class OutofBoard extends Exception {
        public String toString() {
            return "展开区域超出棋盘范围";
        }
    }

    public boolean check(player p) {
        //胜利条件判断
        if (p.Hp == 0)
            return true;
        else {
            if (p.alive.isEmpty()) {
                int pid = p.id;
                int ch = 0;
                labelA:
                for (int m = 2; m <= 18; m++) {
                    int x = m;
                    for (int n = 1; n <= 18; n++) {
                        int y = n;
                        if (pid == 2) {
                            if (board[x][y] == 0) {
                                for (int t = 0; t < p.hand.size(); t++) {
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
                                        } catch (OutofBoard a) {
                                            break;
                                        }
                                    }
                                    if (count != 1) {
                                        if (board[x + 1][y] == -1 * pid || board[x - 1][y] == -1 * pid || board[x][y + 1] == -1 * pid || board[x][y - 1] == -1 * pid)
                                            count++;
                                    }
                                    if (count == 1) {
                                        ch = 1;
                                        break labelA;
                                    }
                                }
                            }
                        } else {
                            if (board[x][y] == 0) {
                                for (int t = 0; t < p.hand.size(); t++) {
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
                                        } catch (OutofBoard a) {
                                            break;
                                        }
                                    }
                                    if (count != 1) {
                                        if (board[x + 1][y] == -1 * pid || board[x - 1][y] == -1 * pid || board[x][y + 1] == -1 * pid || board[x][y - 1] == -1 * pid)
                                            count++;
                                    }
                                    if (count == 1) {
                                        ch = 1;
                                        break labelA;
                                    }
                                }
                            }
                        }
                    }
                }
                if (ch == 1)
                    return false;
                else return true;
            } else return false;
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

        //加载卡组
        ArrayList<Monster> vis = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            int seed = (int) (Math.random() * property.size());
            p1.deck.add(property.get(seed));
            vis.add(property.get(seed));
        }
        int j = 0;
        while (j < 30) {
            int seed = (int) (Math.random() * property.size());
            Monster m1 = property.get(seed);
            int flag = 0;
            for (Monster m2 : vis) {
                if (m2.equals(m1)) {
                    flag = 1;
                }
            }
            if (flag == 0) {
                p2.deck.add(m1);
                vis.add(m1);
                j++;
            }
        }

        /**
         * 对于玩家所在的点进行特别标注
         */
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
        } catch (DeckRunOutException e) {
        }
        try {
            chou_ka(p2);
        } catch (DeckRunOutException e) {
        }
        update(p1);
    }

    void roll(ArrayList<Monster> tmp, player p) throws Exception {
        navigation_panel.setBackground(Color.green);
        navigation_panel.setText("请选择三只怪兽");
        JOptionPane.showMessageDialog(this, "请使用左右键移动光标选择怪兽，回车键确认", "Hint", JOptionPane.INFORMATION_MESSAGE);
        try {
            hand_board_event();
        } catch (Exception e) {
            throw e;
        }

    }


    //召唤怪兽
    public void summon(player owner, Monster m) {
        int x, y, count = 0, p, ch = 0;
        String inputValue = JOptionPane.showInputDialog(this, "请输入召唤的坐标");
        if (inputValue == null) {
            JOptionPane.showMessageDialog(this, "请重新输入坐标", "警告", JOptionPane.WARNING_MESSAGE);
            inputValue = JOptionPane.showInputDialog(this, "请输入召唤的坐标");
        }
        String[] index = inputValue.split(" ");
        x = Integer.valueOf(index[0]);
        y = Integer.valueOf(index[1]);
//        Scanner reader = new Scanner(System.in);
//        x = reader.nextInt();
//        y = reader.nextInt();
        m.setMethod();
        m.setLayout();
        if (owner.id == 2) {
            p = 2;
            for (int i = 0; i < 5; i++) {
                try {
                    if (x - m.layout[i].a > 19 || x - m.layout[i].a < 1 || x - m.layout[i].a + 1 > 19 || x - m.layout[i].a + 1 < 1 || x - m.layout[i].a - 1 > 19 || x - m.layout[i].a - 1 < 1
                            || y + m.layout[i].b > 19 || y + m.layout[i].b < 1 || y + m.layout[i].b + 1 > 19 || y + m.layout[i].b - 1 > 19 || y + m.layout[i].b + 1 < 1 || y + m.layout[i].b - 1 < 1)
                        throw new OutofBoard();
                    else {
                        if (board[x - m.layout[i].a][y + m.layout[i].b] != 0 || board[x][y] != 0) {
                            count = -1;
                            break;
                        }
                        if (count == 0)
                            if (board[x - m.layout[i].a + 1][y + m.layout[i].b] == -1 * p || board[x - m.layout[i].a - 1][y + m.layout[i].b] == -1 * p || board[x - m.layout[i].a][y + m.layout[i].b + 1] == -1 * p || board[x - m.layout[i].a][y + m.layout[i].b - 1] == -1 * p)
                                count = 1;
                    }
                } catch (OutofBoard a) {
                    JOptionPane.showMessageDialog(this, "召唤失败" + a.toString(),
                            "召唤失败", JOptionPane.WARNING_MESSAGE);
                    ch = 1;
                    break;
                }
            }
            if (ch == 0) {
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
                        board[x - m.layout[i].a][y + m.layout[i].b] = -1 * p;
                    }
                    update(owner);
                    //召唤完成后刷新信息
                } else {
                    if (count == -1)
                        JOptionPane.showMessageDialog(this, "召唤失败,展开位置与现有区域重合",
                                "召唤失败", JOptionPane.WARNING_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(this, "召唤失败,展开位置不与己方区域相邻",
                                "召唤失败", JOptionPane.WARNING_MESSAGE);
                    count = 0;
                    summon(owner, m);
                }
            } else {
                count = 0;
                summon(owner, m);
            }
        } else {
            p = 1;
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
                } catch (OutofBoard a) {
                    JOptionPane.showMessageDialog(this, "召唤失败" + a.toString(),
                            "召唤失败", JOptionPane.WARNING_MESSAGE);
                    ch = 1;
                    break;
                }
            }
            if (ch == 0) {
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
                    if (count == -1)
                        JOptionPane.showMessageDialog(this, "召唤失败,展开位置与现有区域重合",
                                "召唤失败", JOptionPane.WARNING_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(this, "召唤失败,展开位置不与己方区域相邻",
                                "召唤失败", JOptionPane.WARNING_MESSAGE);
                    count = 0;
                    summon(owner, m);
                }
            } else {
                count = 0;
                summon(owner, m);
            }
        }
        for (int i = 0; i < tmp.size(); i++) {
            currentplayer.deck.add(tmp.get(i));
        }
        tmp.clear();
    }

    public void Attack(player p, player opponent) throws WinnerException {
        //攻击函数
        String cmd;
        if (p.Mp < 2) {//如果玩家的行动力不足，那么不能攻击
            JOptionPane.showMessageDialog(this, "行动力不足，攻击需要2点行动力", "警告", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ArrayList<Object> options = new ArrayList<>();
        for (Monster m : p.alive) {
            options.add(m.name);
        }
        options.add("取消");

        int response = JOptionPane.showOptionDialog(null, "请选择你想使用的怪兽", "怪兽选择", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options.toArray(), options.toArray()[0]);
        ArrayList<Monster> targets = new ArrayList<Monster>();//首先找出可以攻击的目标
        ArrayList<Object> option2 = new ArrayList<Object>();//加到菜单里
        Monster attacker = new Monster();
        if (!options.toArray()[response].equals("取消")) {
            p.Mp -= 2;//每次执行攻击行动力减2
            update(p);
            cmd = (String) options.toArray()[response];
            for (Monster m : p.alive) {
                if (m.name.equals(cmd)) {
                    attacker = m;
                    JOptionPane.showConfirmDialog(this, m.name + " 攻击力:" + m.Atk + "\r\nID:" + m.getId() + "\r\n生命值:" + m.Hp + "\r\n坐标:(" + m.x + "," + m.y + ")", "怪兽信息确认", JOptionPane.YES_NO_OPTION);
                    int x = m.x;
                    int y = m.y;
                    //特判一下怪兽现在的位置是不是能够攻击到对手
                    if (p.id == 1) {
                        {
                            if (x == 18 && y == 10 || x == 19 && y == 9 || x == 20 && y == 11) {
                                option2.add(opponent.name);
                            }
                        }
                    } else {
                        if (x == 2 && y == 10 || x == 1 && y == 9 || x == 3 && y == 11) {
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
                    }

                    if (board_M[x][y - 1] > 0) {
                        for (Monster e : opponent.alive) {
                            if (board_M[x][y - 1] == e.getId()) {
                                targets.add(e);
                                option2.add(e.name);
//                                System.out.print(e.name + "\\");
                            }
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
                    }

                    if (board_M[x][y + 1] > 0) {
                        for (Monster e : opponent.alive) {
                            if (board_M[x][y + 1] == e.getId()) {
                                targets.add(e);
                                option2.add(e.name);
//                                System.out.print(e.name + "\\");
                            }
                        }
                    }
                    break;
                }
            }
            option2.add("取消");
            int response2 = JOptionPane.showOptionDialog(null, "选择攻击目标", "攻击目标选择", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, option2.toArray(), option2.toArray()[0]);
//            System.out.println("请选择攻击的目标（输入名称）按Q退出：");
            if (!option2.toArray()[response2].equals("取消")) {
                cmd = (String) option2.toArray()[response2];
                if (cmd.equals(opponent.name)) {
                    opponent.Hp--;//攻击对手
                    JOptionPane.showConfirmDialog(this, opponent.name + "被" + attacker.name + "狠狠揍了一顿，生命值-1", "痛击", JOptionPane.INFORMATION_MESSAGE);
                    if (check(opponent)) throw new WinnerException();
                } else {
                    for (Monster e : opponent.alive) {
                        if (e.name.equals(cmd)) {
                            //攻击生物
                            JOptionPane.showConfirmDialog(this, e.name + " 攻击力:" + e.Atk + "\r\nID:" + e.getId() + "\r\n生命值:" + e.Hp + "\r\n坐标:(" + e.x + "," + e.y + ")", "怪兽信息确认", JOptionPane.YES_NO_OPTION);
                            e.Hp -= attacker.Atk;//遭到反伤
                            attacker.Hp -= e.Atk;
                            if (e.Hp <= 0) {//判定生物是否死亡
                                board_M[e.x][e.y] = 0;
                                opponent.alive.remove(e);
                                update(p);
                                if (check(opponent)) throw new WinnerException();
                            }
                            if (attacker.Hp <= 0) {
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


    boolean Isaccesible(Pair<Integer, Integer> p_tmp) {
        //判断坐标是否合法
        int x = p_tmp.getKey();
        int y = p_tmp.getValue();
        if (x >= 1 && x <= 19 && y >= 1 && y <= 19) {
            if (board[x][y] > -3 && board[x][y] < 0) {
                return true;
            }
        }
        return false;
    }

    boolean isConencted(int x1, int y1, int x2, int y2) {
        //bfs搜索从起始点到终点可能的路径
        Queue<Pair> Q = new LinkedList<Pair>();
        Pair ps = new Pair<Integer, Integer>(x1, y1);
        Pair pt = new Pair<Integer, Integer>(x2, y2);
        boolean[][] vis = new boolean[size][size];

        int[][] dir = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};//定义4个方向，用偏移量来定义
        Q.offer(new Pair<Integer, Integer>(x1, y1));
        vis[(int) ps.getKey()][(int) ps.getValue()] = true;
        while (!Q.isEmpty()) {
            //只要队列不空，就继续搜索
            Pair p_tmp = Q.poll();
            for (int i = 0; i < 4; i++) {
                //对于每一个方向进行判断
                Pair p_tmp2 = new Pair<Integer, Integer>((int) p_tmp.getKey() + dir[i][0], (int) p_tmp.getValue() + dir[i][1]);
                if (p_tmp2.equals(pt)) {
                    //如果到达终点
                    cnt++;
                    return true;
                } else if (Isaccesible(p_tmp2) && !vis[(int) p_tmp2.getKey()][(int) p_tmp2.getValue()]) {
                    //如果无法到达终点，但是该点还未被访问过，那么加到队列里继续搜索
                    cnt++;
                    Q.offer(p_tmp2);
                    vis[(int) p_tmp2.getKey()][(int) p_tmp2.getValue()] = true;
                }
            }
        }
        //如果无法达到，返回false
        return false;
    }


    public void Move(player p) throws Exception {
//       Move函数


        ArrayList<Object> options = new ArrayList<Object>();
        for (Monster m : p.alive) {
            options.add(m.name);
        }
        //将怪兽加入到选项中
        options.add("取消");
        int response = JOptionPane.showOptionDialog(this, "选择怪兽", "移动目标选择", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options.toArray(), options.toArray()[0]);

        if (!options.toArray()[response].equals("取消")) {
            String cmd = (String) options.toArray()[response];
            Monster monster = new Monster();
            for (Monster m : p.alive) {
                if (cmd.equals(m.name)) {
                    monster = m;
                    break;
                }
            }
            //选中怪兽后展示信息确认
            int s = JOptionPane.showConfirmDialog(this, monster.name + " 攻击力:" + monster.Atk + "\r\nID:" + monster.getId() + "\r\n生命值:" + monster.Hp + "\r\n坐标:(" + monster.x + "," + monster.y + ")", "确认怪兽信息", JOptionPane.YES_NO_OPTION);
            if (s == 0) {
                cnt = 0;
                String inputValue = JOptionPane.showInputDialog(this, "请输入目标位置的坐标");
                String[] index = inputValue.split(" ");
                int x = Integer.valueOf(index[0]);
                int y = Integer.valueOf(index[1]);
                try {
                    if (board[x][y] >= 0 || board_M[x][y] > 0) {
                        //如果棋盘上这个格子有怪兽，那么不能进入
                        JOptionPane.showMessageDialog(this, "该地点无法进入", "警告", JOptionPane.WARNING_MESSAGE);
                        throw new conflictException1();
                    } else if (!isConencted(monster.x, monster.y, x, y)) {
                        //如果没有连通路径，那么不能进入
                        JOptionPane.showMessageDialog(this, "无法到达该地点", "警告", JOptionPane.WARNING_MESSAGE);
                        cnt = 0;
                    } else {
                        //如果移动花费超过行动力，那么不能进入
                        if (cnt > p.Mp) JOptionPane.showMessageDialog(this, "行动力不足", "警告", JOptionPane.WARNING_MESSAGE);
                        else {
                            //移动消耗行动力
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
                        label[i][j].setSize(20, 20);
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
                                label[i][j].setSize(20, 20);
                            }
                        }
                    } else {
                        label[i][j].setBackground(Color.red);
                        label[i][j].setText("");
                    }
                } else {
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
        p1_info.setText(p1.name + "(" + p1.color + ")" + "目前的状态是：\r\n" + "Hp:" + p1.Hp + " 行动点" + p1.Mp + " 手牌数" + p1.hand.size() + " 卡组剩余卡牌:" + p1.deck.size());
        p2_info.setText(p2.name + "(" + p2.color + ")" + "目前的状态是：\r\n" + "Hp:" + p2.Hp + " 行动点" + p2.Mp + " 手牌数" + p2.hand.size() + " 卡组剩余卡牌:" + p2.deck.size());

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
        //更新信息面板
        info_board.setText(s);
    }

    void info_update() {
        //更新p1,p2状态栏
        p1_info.setText(p1.name + "(" + p1.color + ")" + "目前的状态是：\n" + "Hp:" + p1.Hp + " 行动点" + p1.Mp + " 手牌数" + p1.hand.size());
        p2_info.setText(p2.name + "(" + p2.color + ")" + "目前的状态是：\n" + "Hp:" + p2.Hp + " 行动点" + p2.Mp + " 手牌数" + p2.hand.size());
    }

    public void pic_field_update(ImageIcon i) {
        //更新图片
        pic_field.setIcon(i);
    }

    public void hand_field_update(int curse_x) {
        //更新手牌区，光标所在的手牌加上边框表示选中
        Monster m = currentplayer.hand.get(curse_x - 1);
        hand_cards[curse_x].setBorder(BorderFactory.createLineBorder(Color.green));
        //图片栏和信息栏与光标同步更新
        pic_field_update(m.pic_big);
        info_board_update("<html><h3>" + m.name + "</h3><br>" + "<h3>怪兽id：" + m.getId() + "</h3><br>" + m.Description + "</html>");
        for (int i = 1; i <= 6; i++) {
            if (i != curse_x) hand_cards[i].setBorder(BorderFactory.createLineBorder(Color.black));
        }
    }


    //游戏结束画面
    private void end(player p) {
        JOptionPane.showMessageDialog(this, "Game Over!The Winner is" + p.name, "游戏结束", JOptionPane.INFORMATION_MESSAGE);
        navigation_panel.setBackground(Color.yellow);
        navigation_panel.setText("Game Over!The Winner is" + p.name);
    }

    //游戏流程
    public void run() {
        show_rule();//在游戏开始前先显示一下规则
        pick(p1);//p1选择职业
        pick(p2);//
        currentplayer = p1;
        opponent = p2;
        buttonlist_event();
    }

    public void Priest(player p) {
        if (p.Mp < 15)
            JOptionPane.showMessageDialog(this, "移动点数不够", "技能使用失败", JOptionPane.WARNING_MESSAGE);
        else {
            Object[] options = {"确定", "取消"};
            int response = JOptionPane.showOptionDialog(null, "你将使用牧师技能", "使用技能", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            for (int i = 0; i < p.hand.size(); i++) {
                p.hand.get(i).Hp += 200;
            }
            p.Mp -= 15;
        }
    }

    public void constructer(player p) {
        if (p.Mp < 7)
            JOptionPane.showMessageDialog(this, "移动点数不够", "技能使用失败", JOptionPane.WARNING_MESSAGE);
        else {
            int x, y;
            String inputValue = JOptionPane.showInputDialog("请输入要填充的土地坐标");
            String[] index = inputValue.split(" ");
            x = Integer.valueOf(index[0]);
            y = Integer.valueOf(index[1]);
            board[x][y] = -1 * p.id;
            p.Mp -= 7;
        }
    }

    public void singer(player p) {
        if (p.Mp < 5)
            JOptionPane.showMessageDialog(this, "移动点数不够", "技能使用失败", JOptionPane.WARNING_MESSAGE);
        else {
            ArrayList<Object> options = new ArrayList<Object>();
            for (Monster m : p1.alive) {
                options.add(m.name);
            }
            int response = JOptionPane.showOptionDialog(null, "你将使用歌颂者技能，请选择你要强化的怪兽", "使用技能", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options.toArray(), options.toArray()[0]);
            p1.alive.get(response).Atk += 200;
            p.Mp -= 5;
        }
    }

    public void shaman(player p1, player p2) {
        if (p1.Mp < 10)
            JOptionPane.showMessageDialog(this, "移动点数不够", "技能使用失败", JOptionPane.WARNING_MESSAGE);
        else {
            ArrayList<Object> options = new ArrayList<Object>();
            for (Monster m : p1.alive) {
                options.add(m.name);
            }
            int response = JOptionPane.showOptionDialog(null, "你将使用萨满技能，请选择你想献祭的怪兽", "使用技能", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options.toArray(), options.toArray()[0]);
            int t = response;
            p1.alive.get(t).Hp = 0;
            for (int i = 0; i < p2.alive.size(); i++) {
                if (p2.alive.get(i).y == p1.alive.get(t).y) {
                    p2.alive.get(i).Hp -= p1.alive.get(t).Atk;
                    if (p2.alive.get(i).Hp <= 0)
                        p2.alive.remove(i--);
                }
            }
            p1.alive.remove(t);
            p1.Mp -= 10;
        }
    }

    public void pick(player p) {
        Object[] options = {"医疗兵", "拉拉队员", "萨满祭司", "建筑工人"};
        int response = JOptionPane.showOptionDialog(null, "请" + p.id + "号玩家选择你的角色", "角色选择", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        p.characterid = response;
        if (p.characterid == 0) {
            Object[] options0 = {"确定", "取消"};
            int response0 = JOptionPane.showOptionDialog(null, "医疗兵的技能为：为你的所有怪兽恢复两点生命，消耗移动点数5", "医疗兵", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options0, options0[0]);
            if (response0 == 1)
                pick(p);
        } else if (p.characterid == 1) {
            Object[] options0 = {"确定", "取消"};
            int response0 = JOptionPane.showOptionDialog(null, "拉拉队员的技能为：为你的一只怪兽增加两点攻击，消耗移动点数5", "拉拉队员", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options0, options0[0]);
            if (response0 == 1)
                pick(p);
        } else if (p.characterid == 2) {
            Object[] options0 = {"确定", "取消"};
            int response0 = JOptionPane.showOptionDialog(null, "萨满祭司的技能为：消灭自己的一只怪兽，对一整列上对方怪兽造成等同于其攻击力的伤害，消耗移动点数10", "萨满祭司", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options0, options0[0]);
            if (response0 == 1)
                pick(p);
        } else if (p.characterid == 3) {
            Object[] options0 = {"确定", "取消"};
            int response0 = JOptionPane.showOptionDialog(null, "建筑工人的技能为：填充某一块陆地，使其可以行走，消耗移动点数7", "建筑工人", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options0, options0[0]);
            if (response0 == 1)
                pick(p);
        }
    }

    public void show_rule() {
        File f = new File("src/GameRule.txt");
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(f);

            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String msg = "";
            String s1;
            while (((s1 = bufferedReader.readLine()) != null)) {
                msg = msg + "\n" + s1;
            }
            JOptionPane.showMessageDialog(null, msg, "欢迎来到DDM的世界", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
        }

    }
}