import javafx.scene.layout.Border;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import java.awt.*;
public class GUItest {
    public static void main(String[]args)
    {
        final int size = 21;
        JFrame frame = new JFrame("test1");//主窗口及相关设置
        frame.setSize(800,800);
        frame.setResizable(false);
        frame.setVisible(true);

        //战斗棋盘界面
        JPanel pane1=new JPanel();
        pane1.setVisible(true);
        GridLayout grid1=new GridLayout(size-1,size-1);
        pane1.setLayout(grid1);
        JLabel label[][]=new JLabel[size][size];
        for(int i=1;i<size-1;i++){
            for(int j=1;j<size-1;j++){
                label[i][j]=new JLabel();
                label[i][j].setBounds(20*(i-1),20*(i-1),20,20);
                if((i+j)%2==0)
                    label[i][j].setBackground(Color.black);
                else
                    label[i][j].setBackground(Color.white);
                pane1.add(label[i][j]);
            }
        }

        //窗口上方显示信息
        JPanel up = new JPanel();
        up.setLayout(null);
        up.setBounds(0,0,800,200);
        JLabel up_text = new JLabel();
        up_text.setText("<html>昵称：Jim<br>Hp:1 Mp:1 攻击点数：1 防御点数：1 魔法点数：1<br>昵称：Tom<br>Hp:1 Mp:1 攻击点数：1 防御点数：1 魔法点数：1</html>");
        up.add(up_text);

        //窗口左侧信息界面
        JPanel left = new JPanel();
        left.setLayout(null);
        left.setBounds(0,250,300,500);
        JLabel label_Image = new JLabel();
        label_Image.setBounds(0,0,300,300);
        JLabel label_Info = new JLabel();
        label_Info.setBounds(0,320,300,150);
        left.add(label_Image);
        left.add(label_Info);

        //窗口右下方手牌界面
        JPanel hand = new JPanel();
        hand.setLayout(null);
        hand.setBounds(300,700,300,100);
        JLabel hand_cards[] = new JLabel[7];
        for(int i=1;i<=6;i++)
        {
            hand_cards[i] = new JLabel();
            hand_cards[i].setSize(20,20);
            ImageIcon icon = new ImageIcon("1.jpg");
            hand_cards[i].setIcon(icon);
            hand.add(hand_cards[i]);
        }

//        JPanel panel_right = new JPanel();
//        panel_right.setSize(200,900);
//
//        JLabel p1 = new JLabel();
//        p1.setText("<html>昵称：Jim<br>Hp:1 Mp:1 攻击点数：1 防御点数：1 魔法点数：1<br>昵称：Tom<br>Hp:1 Mp:1 攻击点数：1 防御点数：1 魔法点数：1</html>");
//        p1.setBounds(0,0,600,200);
//        JLabel Info_Image = new JLabel();
//        Info_Image.setText("这里将显示怪兽图片");
//        Info_Image.setBounds(600,0,200,400);
//        JLabel Info = new JLabel();
//        Info.setSize(200,400);
//        Info.setBounds(600,500,200,400);
//        Info.setText("这里将显示怪兽卡牌信息");
//        panel_right.add(Info_Image);
//        panel_right.add(Info);
//        JPanel hand = new JPanel();
//        JLabel hand_cards[] = new JLabel[7];
//        for(int i=1;i<=6;i++)
//        {
//            hand_cards[i] = new JLabel();
//            hand_cards[i].setSize(20,20);
//            ImageIcon icon = new ImageIcon("1.jpg");
//            hand_cards[i].setIcon(icon);
//            hand.add(hand_cards[i]);
//        }
//        p1.setText("<html>昵称：Jim<br>Hp:1 Mp:1 攻击点数：1 防御点数：1 魔法点数：1<br>昵称：Tom<br>Hp:1 Mp:1 攻击点数：1 防御点数：1 魔法点数：1</html>");
//
//
//
//
//        GridLayout grid1=new GridLayout(size-1,size-1);
//        pane1.setLayout(grid1);
//        pane1.setSize(600,400);
//        frame.setSize(800,900);
//        frame.setResizable(false);
//        frame.setVisible(true);
//        Label label[][]=new Label[size][size];
//        for(int i=1;i<size-1;i++){
//            for(int j=1;j<size-1;j++){
//                label[i][j]=new Label();
//                label[i][j].setSize(20,20);
//                if((i+j)%2==0)
//                    label[i][j].setBackground(Color.black);
//                else
//                    label[i][j].setBackground(Color.white);
//                pane1.add(label[i][j]);
//            }
//        }
        frame.add(pane1,BorderLayout.CENTER);
        //frame.add(up);
        //frame.add(left);
        //frame.add(new JButton("west"),BorderLayout.WEST);
        //frame.add(hand);
    }
}
