import com.iflytek.cloud.speech.*;
import com.iflytek.util.DebugLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import com.iflytek.util.DebugLog;

import com.iflytek.util.JsonParser;

import com.iflytek.util.Version;

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
    SpeechRecognizer speechRecognizor = SpeechRecognizer.createRecognizer();

}

public class GridBagDemo extends JFrame implements gameconfig {
    int curse_x = 1;
    int cnt;
    player currentplayer;
    player opponent;
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
        info_board.setText("开启了声控新功能哦");
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
                        JOptionPane.showMessageDialog(null,result[0]+result[1]+result[2],"确认投掷结果",JOptionPane.INFORMATION_MESSAGE);
                     Object[] options = {"空","空","空","取消"};
                        int i1 = 0;
                        for(int i=0;i<tmp.size();i++)
                        {
                            if(tmp.get(i).summon_pre)
                            {
                                options[i1] = tmp.get(i).name;

                            }
                            else{
                                options[i1] = "空";
                            }
                            i1++;
                        }
                        navigation_panel.setText(result[0] + result[1] + result[2]);
                        int response = JOptionPane.showOptionDialog(null,"请选择你想召唤的怪兽，如果没有，请直接按取消","怪兽选择",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                        if(response<3)summon(currentplayer,tmp.get(response));
                    }
                }

            }
        });
        this.setTitle("DDM");
        this.setSize(1000, 700);
        this.setVisible(true);
        this.setResizable(false);
        speaker_listen();
    }
    void speaker_listen(){
        if(!speechRecognizor.isListening())
            try {
                Thread.sleep(500);
                speechRecognizor.startListening(recognizerListener);
            }catch (Exception e){}
        else
            speechRecognizor.stopListening();

    }

    boolean match(String child ,String Parent){
        int len1 = child.length();
        int len2 = Parent.length();
        for(int i=0;i<len2-len1;i++){
            if(Parent.substring(i,i+len1).equals(child))return true;

        }
        return false;
    }

    /**
     * 听写监听器
     */
    private RecognizerListener recognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int volume) {
            DebugLog.Log("onVolumeChanged enter");

            if (volume == 0)

                volume = 1;

            else if (volume >= 6)

                volume = 6;

        }

        @Override
        public void onBeginOfSpeech() {
            DebugLog.Log("开始监听");

        }

        @Override
        public void onEndOfSpeech() {
            DebugLog.Log("onEndOfSpeech enter");

        }

        @Override
        public void onResult(RecognizerResult results, boolean islast) {
            StringBuffer param = new StringBuffer();

            param.append( "appid=" + Version.getAppid() );

//  param.append( ","+SpeechConstant.LIB_NAME_32+"=myMscName" );

            SpeechUtility.createUtility( param.toString() );

            com.iflytek.view.VoiceSpeech t = new com.iflytek.view.VoiceSpeech();
            DebugLog.Log("onResult enter");
            String ch = JsonParser.parseIatResult(results.getResultString());
            String text="";
            while(null != ch){
                text = text + ch;
                ch = JsonParser.parseIatResult(results.getResultString());
            }

            System.out.println(text);

            if(match("投掷",text))try
            {
                roll(tmp,currentplayer);
            }catch (Exception e){

            }
            else if(match("攻击",text))try{
                Attack(currentplayer,opponent);
            }catch (Exception e){
                end(currentplayer);
            }
            else if(match("移动",text))try{
                Move(currentplayer);
            }catch (Exception e){

            };

        }

        @Override
        public void onError(SpeechError speechError) {
            DebugLog.Log("onEvent enter");

        }

        @Override
        public void onEvent(int i, int i1, int i2, String s) {

        }
    };
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
            speaker_listen();
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
        String s1,s2[];
        int ints[]={0,0,0,0,0,0},dice[]={0,0,0,0,0,0,0,0,0,0,0,0};
        File f=new File("src/Monster1.txt");
        try {
            InputStream inputStream = new FileInputStream(f);
            StringBuilder stringBuilder=new StringBuilder();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            s1=null;
            try{
                int j=0;
                while((s1=bufferedReader.readLine())!=null){
                    s2=s1.split(" ");
                    for(int i=1;i<=5;i++){
                        ints[i]=Integer.valueOf(s2[i]);
                    }
                    property.add(new Monster(s2[0],ints[1],ints[2],ints[3],ints[4],ints[5],s2[6]));
                    for(int i=0;i<6;i++){
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
    public boolean check(player p) {
        if (p.Hp==0)
            return true;
        else{
            if(p.alive.isEmpty()){
                int pid=p.id;
                int ch=0;
                labelA:
                for(int m=2;m<=18;m++){
                    int x=m;
                    for(int n=1;n<=18;n++) {
                        int y = n;
                        if(pid==2) {
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
                                        break labelA;
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
                                        break labelA;
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
        for (Monster m : property) {
            p1.deck.add(m);
            p2.deck.add(m);
        }
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
        String inputValue = JOptionPane.showInputDialog("请输入召唤的坐标");
        String[]index = inputValue.split(" ");
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
                        if (board[x - m.layout[i].a][y + m.layout[i].b] != 0 && board[x][y] != 0) {
                            count = -1;
                            break;
                        }
                        if (count == 0)
                            if (board[x - m.layout[i].a + 1][y + m.layout[i].b] == -1 * p || board[x - m.layout[i].a - 1][y + m.layout[i].b] == -1 * p || board[x - m.layout[i].a][y + m.layout[i].b + 1] == -1 * p || board[x - m.layout[i].a][y + m.layout[i].b - 1] == -1 * p)
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
                        board[x - m.layout[i].a][y + m.layout[i].b] = -1 * p;
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
                    count=0;
                    summon(owner, m);
                }
            }
            else{
                count=0;
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
                        if (board[x + m.layout[i].a][y + m.layout[i].b] != 0 && board[x][y] != 0) {
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
        Scanner in = new Scanner(System.in);
        String cmd;
//        System.out.println(p.name + "可以使用的怪兽有：");
        ArrayList<Object> options = new ArrayList<>();
        for (Monster m : p.alive) {
                System.out.print(m.name + "(" + m.x + "," + m.y + ")\\");
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
            cmd = (String) options.toArray()[response];
            for (Monster m : p.alive) {
                if (m.name.equals(cmd)) {
                    attacker = m;
//                    System.out.println("你可进行攻击的目标有：");
                    int x = m.x;
                    int y = m.y;
                    if (x == p.target_x && y == p.target_y) {
                        System.out.print(opponent.name + "\\");
                        option2.add(opponent.name);
                    }
                    if (board_M[x - 1][y] > 0) {
                        for (Monster e : opponent.alive) {
                            if (board_M[x - 1][y] == e.getId()) {
                                targets.add(e);
                                option2.add(e.name);
//                                System.out.print(e.name + "\\");
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
            int response2= JOptionPane.showOptionDialog(null,"选择攻击目标","攻击目标选择",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,option2.toArray(),option2.toArray()[0]);
//            System.out.println("请选择攻击的目标（输入名称）按Q退出：");
            if(!option2.toArray()[response2].equals("取消")) {
                cmd = (String) option2.toArray()[response2];
                 if (cmd.equals(opponent.name)) {
                    opponent.Hp--;
                    if (check(opponent)) throw new WinnerException();
                } else {
                    for (Monster e : opponent.alive) {
                        if (e.name.equals(cmd)) {
                            if (e.Dfs_state) {
                                e.Hp -= attacker.Atk;
                            } else {
                                e.Hp -= attacker.Atk;
                            }
                            if (e.Hp <= 0) {
                                if (opponent.id == 1) board[e.x][e.y] = -1;
                                else board_M[e.x][e.y] = 0;
                                opponent.alive.remove(e);
                                if (check(opponent)) throw new WinnerException();
                            }
                            break;
                        }
                    }
                }
                attacker.Atk_state = false;
                attacker.Dfs_state = false;
                update(p);
            }
        }

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
        int response = JOptionPane.showOptionDialog(null,"选择怪兽","移动目标选择",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options.toArray(),options.toArray()[0]);
//        System.out.println();
        if(!options.toArray()[response].equals("取消")) {
            String cmd = (String)options.toArray()[response];
            Monster monster = new Monster();
            String inputValue = JOptionPane.showInputDialog("请输入目标位置的坐标");
            String[]index = inputValue.split(" ");
            int x = Integer.valueOf(index[0]);
            int y = Integer.valueOf(index[1]);
            int flag = 0;
            for (Monster m : p.alive) {
                if (cmd.equals(m.name)) {
                    monster = m;
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) throw new Exception("没有找到" + cmd + "  请重新输入");

            try {
                if (board[x][y] >= 0 || board_M[x][y] > 0)
                {
                    JOptionPane.showMessageDialog(this,"该地点无法进入","警告",JOptionPane.WARNING_MESSAGE);
                    throw new conflictException1();
                }
                else if ((Math.abs(x - monster.x) + Math.abs(y - monster.y)) > move)
                    throw new conflictException2();
                else {
                    board_M[monster.x][monster.y] = 0;
                    monster.x = x;
                    monster.y = y;
                    board_M[x][y] = monster.getId();
                    JOptionPane.showMessageDialog(this,monster.name+"移动成功","info",JOptionPane.INFORMATION_MESSAGE);
                    p.Mp -= Math.abs(x - monster.x) + Math.abs(y - monster.y);
                    update(p);
                }
            } catch (conflictException1 a) {
                System.out.println(a.toString());
            } catch (conflictException2 a) {
                System.out.println(a.toString());
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
                } else if (board[i][j] == -1) {
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
                } else if (board[i][j] == -2) {
                    if (board_M[i][j] > 0) {
                        for (Monster m : property) {
                            if (m.getId() == board_M[i][j]) {
                                label[i][j].setText(Integer.toString(m.getId()));
                                label[i][j].setBackground(Color.red);
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
        p1_info.setText(p1.name + "(" + p1.color + ")" + "目前的状态是：\n" + "Hp:" + p1.Hp + "行动点" + p1.Mp + "手牌数"+p1.hand.size());
        p2_info.setText(p2.name + "(" + p2.color + ")" + "目前的状态是：\n" + "Hp:" + p2.Hp + "行动点" + p2.Mp + "手牌数" + p2.hand.size());

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
        info_board_update("<html><h2>"+m.name+"</h2><br>"+"<h2>怪兽id："+m.getId()+"</h2><br>"+m.Description+"</html>");
        for(int i=1;i<=6;i++)
        {
            if(i!=curse_x)hand_cards[i].setBorder(BorderFactory.createLineBorder(Color.black));
        }
    }


    //游戏结束画面
    private void end(player p) {
        System.out.println("Game Over!The Winner is" + p.name);
        navigation_panel.setBackground(Color.yellow);
        navigation_panel.setText("Game Over!The Winner is" + p.name);
    }

    //游戏流程
    public void run() {
        int curse_x = 1;
        int curse_y = 1;
        int cnt = 0;
        currentplayer = p1;
        opponent = p2;
        buttonlist_event();

    }
}