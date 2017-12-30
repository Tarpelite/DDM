import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

//interface gameconfig{
//    String title = "DDM";
//    int navigate_panelX = 800;
//    int navigate_panelY = 50;
//   int frameX = 800;
//   int frameY = 800;
//   int p1_panelX = 400;
//   int p1_panelY = 200;
//   int p2_panelX = 400;
//   int p2_panelY = 200;
//   int pic_panelX = 300;
//   int pic_panelY = 300;
//   int info_panelX = 300;
//   int info_panelY = 200;
//   int battle_panelX = 500;
//   int battle_panelY = 500;
//   int hand_panelX  = 500;
//   int hand_panelY = 200;
//   int button_panelX = 800;
//   int button_panelY = 50;
//   final int size = 21;
//   final int pic_list_size = 1;
//   final int hand_pic_list_size = 1;
//   final int battle_pic_list_size = 1;
//
//   ImageIcon pic_list[] = new ImageIcon[pic_list_size];
//   ImageIcon hand_pic_list[] = new ImageIcon[hand_pic_list_size];
//   ImageIcon battle_pic_list[] = new ImageIcon[battle_pic_list_size];
//
//}
//
//class mainframe extends JFrame implements gameconfig {
//
//
//    //游戏面板
////    JPanel main_panel = new JPanel();
//    JPanel navigate_panel = new JPanel();
//    JTextField p1_info = new JTextField();
//    JTextField p2_info = new JTextField();
//    JLabel pic_field = new JLabel();
//    JPanel battle_field = new JPanel();
//    JLabel chess_board[][] = new JLabel[size][size];
//    JTextField info_board = new JTextField();
//    JPanel hand_panel = new JPanel();
//    JLabel hand_cards[] = new JLabel[7];
//    JPanel buttons_panel = new JPanel();
//    JButton buttons[] = new JButton[5];
//
//
//
//
//    public mainframe(){
//        init();
//    }
//
//    /**
//     * 设置窗体
//     */
//
//    public void init() {
//        //初始化窗口
//        this.setTitle(title);
//        this.setSize(frameX, frameY);
//        this.setDefaultCloseOperation(3);
//        this.setVisible(true);
//        this.setResizable(true);
//
//        //初始化布局管理
//        GridBagLayout layout = new GridBagLayout();
//        this.setLayout(layout);
//
//        //初始化各模块大小
//        navigate_panel.setSize(navigate_panelX,navigate_panelY);
//        p1_info.setSize(p1_panelX,p1_panelY);
//        p2_info.setSize(p2_panelX,p2_panelY);
//        pic_field.setSize(pic_panelX,pic_panelY);
//        battle_field.setSize(battle_panelX,battle_panelY);
//        info_board.setSize(info_panelX,info_panelY);
//        hand_panel.setSize(hand_panelX,hand_panelY);
//        buttons_panel.setSize(button_panelX,button_panelY);
//
//        //初始化导航栏
//
//        //初始化p1,p2
//        p1_info.setText("昵称：Jim Hp:1 Mp:1 攻击点数：1 防御点数：1 魔法点数：1 手牌数：6");
//        p1_info.setEditable(false);
//        p2_info.setText("昵称：Jim Hp:1 Mp:1 攻击点数：1 防御点数：1 魔法点数：1 手牌数：6");
//        p2_info.setHorizontalAlignment(JTextField.RIGHT);
//        p2_info.setEditable(false);
//
//        //初始化图片区
//        pic_field.setIcon(new ImageIcon("pic_1.jpg"));
//
//        //初始化战斗区
//        GridLayout grid1=new GridLayout(size-1,size-1);
//        battle_field.setLayout(grid1);
//        for(int i=1;i<size-1;i++){
//            for(int j=1;j<size-1;j++){
//                chess_board[i][j]=new JLabel();
//                chess_board[i][j].setSize(20,20);
//                if((i+j)%2==0)
//                    chess_board[i][j].setBackground(Color.black);
//                else
//                    chess_board[i][j].setBackground(Color.blue);
//                battle_field.add(chess_board[i][j]);
//            }
//        }
//        battle_field.setVisible(true);
//
//        //初始化提示区
//        info_board.setEditable(false);
//        info_board.setText("这里是提示区");
//
//        //初始化手牌区
//        for(int i=1;i<=6;i++)
//        {
//            hand_cards[i] = new JLabel();
//            hand_cards[i].setSize(20,20);
//            ImageIcon icon = new ImageIcon("1.jpg");
//            hand_cards[i].setIcon(icon);
//            hand_panel.add(hand_cards[i]);
//        }
//
//        //初始化按钮区
//        String conts[] = {"攻击","防御","魔法","投掷","结束"};
//        JButton []button_list = new JButton[5];
//        for(int i=0;i<5;i++)
//        {
//            button_list[i] = new JButton(conts[i]);
//            buttons_panel.add(button_list[i]);
//        }
//
//        //布局整理
//        GridBagConstraints s = new GridBagConstraints();
//        JPanel blank1 = new JPanel();
//        this.add(navigate_panel);
//        this.add(p1_info);
//        this.add(p2_info);
//        this.add(pic_field);
//        this.add(battle_field);
//        this.add(blank1);
//        this.add(info_board);
//        this.add(hand_panel);
//        this.add(buttons_panel);
//
//        s.fill = GridBagConstraints.BOTH;
//
//        s.gridwidth = 0;
//        s.weightx = 0;
//        s.weighty = 0;
//        layout.setConstraints(navigate_panel,s);
//
//        s.gridwidth = 4;
//        s.weightx = 0;
//        s.weighty = 0;
//        layout.setConstraints(p1_info,s);
//
//        s.gridwidth = 0;
//        s.weightx = 0;
//        s.weighty = 0;
//        layout.setConstraints(p2_info,s);
//
//        s.gridwidth = 4;
//        s.weightx = 0;
//        s.weighty = 0;
//        layout.setConstraints(pic_field,s);
//
//        s.gridwidth = 4;
//        s.weightx = 0;
//        s.weighty = 0;
//        layout.setConstraints(battle_field,s);
//
//        s.gridwidth = 0;
//        s.weightx = 0;
//        s.weighty = 0;
//        layout.setConstraints(blank1,s);
//
//        s.gridwidth = 4;
//        s.weightx = 0;
//        s.weighty = 1;
//        layout.setConstraints(info_board,s);
//
//        s.gridwidth = 0;
//        s.weightx = 1;
//        s.weighty = 0;
//        layout.setConstraints(hand_panel,s);
//
//        s.gridwidth = 0;
//        s.weightx = 1;
//        s.weighty = 0;
//        layout.setConstraints(buttons_panel,s);
//
//    }
//
//}
//
//public class DDM_test {
//    public static void main(String[]args)
//    {
//        mainframe mf = new mainframe();
//        //mf.init();
//    }
//}
