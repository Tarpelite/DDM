import javax.swing.*;
import java.awt.*;

public class GUItest2 {
    public static void main(String[]args)
    {
        //主框架
        JFrame frame = new JFrame("test2");
        final int width = 1080;
        final int height = 960;
        final int size = 21;
        frame.setSize(width,height);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLayout(null);
/*
        //高清卡图
        JPanel card_pricture= new JPanel();
//        card_pricture.setLayout(null);
        JLabel pic = new JLabel();
        pic.setSize(350,350);
        ImageIcon icon1 = new ImageIcon("pic_1.jpg");
        pic.setIcon(icon1);
        card_pricture.add(pic);
        card_pricture.setBounds(0,0,350,350);
       card_pricture.setSize(350,350);
        frame.add(card_pricture);
*/


//        ImageIcon icon1 = new ImageIcon("pic1.jpg");
//        JLabel label_pictrue = new JLabel();
//        label_pictrue.setSize(350,350);
//        label_pictrue.setIcon(icon1);
//        card_pricture.validate();
//        card_pricture.add(label_pictrue,BorderLayout.CENTER);
//        card_pricture.setBounds(0,0,350,350);
//        card_pricture.setSize(350,350);
//        frame.add(card_pricture);



        //卡片信息
        JPanel card_Information = new JPanel();
        JLabel p1 = new JLabel();
        p1.setText("<html>昵称：Jim<br>Hp:1 Mp:1 攻击点数：1 防御点数：1 魔法点数：1<br>昵称：Tom<br>Hp:1 Mp:1 攻击点数：1 防御点数：1 魔法点数：1</html>");
        p1.setBounds(20,400,350,350);
        p1.setSize(350,350);
        frame.add(p1);




 /*

        //card_Information.setLayout(null);
        card_Information.setBounds(0,460,350,350);
        card_Information.setSize(350,350);
      JLabel label_text = new JLabel("<html><h1>黑魔法师</h1></html>");
      label_text.setBounds(0,460,350,350);
       card_Information.add(label_text);
        frame.add(card_Information);
        */
/*
        //玩家状态栏
        JPanel player_state = new JPanel();
        player_state.setBounds(410,0,650,200);
        player_state.setSize(680,200);
//        JLabel up_text = new JLabel();
//        up_text.setText("<html>昵称：Jim<br>Hp:1 Mp:1 攻击点数：1 防御点数：1 魔法点数：1<br>昵称：Tom<br>Hp:1 Mp:1 攻击点数：1 防御点数：1 魔法点数：1</html>");
//        player_state.add(up_text,BorderLayout.CENTER);
        frame.add(player_state);

        //战斗棋盘
        JPanel chess_board = new JPanel();
        chess_board.setBounds(400,200,500,500);
        chess_board.setSize(500,500);
        GridLayout grid1=new GridLayout(size-1,size-1);
        chess_board.setLayout(grid1);
        Label label[][]=new Label[size][size];
        for(int i=1;i<=size-1;i++){
            for(int j=1;j<=size-1;j++){
                label[i][j]=new Label();
                label[i][j].setSize(25,25);
                if((i+j)%2==0)
                    label[i][j].setBackground(Color.black);
                else
                    label[i][j].setBackground(Color.white);
                chess_board.add(label[i][j]);
            }
        }
        frame.add(chess_board);

        //手牌界面
        JPanel hand = new JPanel();
        hand.setBounds(400,700,680,260);
        hand.setSize(680,260);
        JLabel hand_cards[] = new JLabel[7];
        for(int i=1;i<=6;i++)
        {
            hand_cards[i] = new JLabel();
            hand_cards[i].setSize(20,20);
            ImageIcon icon = new ImageIcon("1.jpg");
            hand_cards[i].setIcon(icon);
            hand.add(hand_cards[i]);
        }
        frame.add(hand);

        //按钮界面
        JPanel op_board = new JPanel();
        op_board.setBounds(900,200,180,500);
        op_board.setSize(180,500);
        frame.add(op_board);



*/
    }

}
