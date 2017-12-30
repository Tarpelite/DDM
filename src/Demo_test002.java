import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Scanner;

interface gameconfig2{
    final int size = 21;
    ImageIcon hands_property[] = new ImageIcon[7];
    ImageIcon pic_property[] = new ImageIcon[7];
    boolean sp_flag = false;
    boolean mp_flag = false;
    boolean bp_flag = false;
    int curse_x = 1;
    int curse_y = 1;
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

}

public class Demo_test002 extends JFrame implements gameconfig2 {
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
    public static void main(String[]args)
    {
        Demo_test002 demo = new Demo_test002();
        demo.frame_init();
    }

    public void frame_init(){
        this.setTitle("DDM");
        this.setSize(1000,700);
        this.setVisible(true);
        this.setResizable(false);
        navigation_panel.setEditable(false);
        navigation_panel.setBackground(Color.CYAN);
        navigation_panel.setForeground(Color.red);
        p1_info.setText("");
        p2_info.setText("");
        ImageIcon i1 = new ImageIcon("./pic_img/pic_1.jpg");
        pic_field.setIcon(i1);
        info_board.setText("<html><h2>黑魔导师</h2><br>通常怪兽<br>ATK:5 DFS:3 Hp:3/3<br></html>");
        info_board.setHorizontalAlignment(JLabel.LEFT);



        for(int i=1;i<=6;i++)
        {
            hand_cards[i] = new JLabel();
            hand_cards[i].setSize(20,20);
            ImageIcon icon = new ImageIcon("./hand_img/0.jpg");
            hand_cards[i].setIcon(icon);
            hand_board.add(hand_cards[i]);
        }
        JPanel blank0 = new JPanel();
        JPanel blank1 = new JPanel();
        JPanel blank2 = new JPanel();


//        String []conts = new String[5];
        String conts[] = {"攻击(A)","防御(D)","魔法(M)","投掷(R)","结束(Q)"};
        for(int i=0;i<5;i++)
        {
            button_list[i] = new JButton(conts[i]);
            buttons_panel.add(button_list[i]);
        }



        GridLayout grid1=new GridLayout(size-1,size-1);
        battle_field.setLayout(grid1);
        //battle_field.setSize(600,400);
        for(int i=1;i<size-1;i++){
            for(int j=1;j<size-1;j++){
                label[i][j]=new Label();
                label[i][j].setSize(20,20);
                if((i+j)%2==0)
                    label[i][j].setBackground(Color.black);
                else
                    label[i][j].setBackground(Color.white);
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

        GridBagConstraints s= new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;
        s.gridwidth = 0;
        s.weightx = 1;
        s.weighty = 0;
        layout.setConstraints(navigation_panel,s);

        s.gridwidth=4;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(p1_info,s);

        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(p2_info,s);

        s.gridwidth = 4;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(pic_field,s);


        s.gridwidth = 4;
        s.weightx = 1;
        s.weighty = 0;
        layout.setConstraints(battle_field,s);

        s.gridwidth = 0;
        s.weightx  = 0;
        s.weighty = 0;
        layout.setConstraints(blank1,s);

        s.gridwidth = 4;
        s.weightx  = 1;
        s.weighty = 1;
        layout.setConstraints(info_board,s);

        s.gridwidth = 4;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(hand_board,s);

        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(blank2,s);

        s.gridwidth = 0;
        s.weightx = 1;
        s.weighty = 0;
        layout.setConstraints(buttons_panel,s);
    }


}
