import javax.swing.*;
import java.awt.*;

public class test4 {

    private JPanel cards_pic;


    private JPanel note;
    private JPanel information;
    private JPanel hands;
    private JPanel chessboard;
    private JPanel pmain;
    private JPanel right_upper;

    private void createUIComponents() {
        // TODO: place custom component creation code here
        pmain.setBounds(0,0,1080,960);
        GridLayout grid1=new GridLayout(12,12);
        chessboard.setLayout(grid1);
        Label label[][]=new Label[12][12];
        for(int i=0;i<12;i++){
            for(int j=0;j<12;j++){
                label[i][j]=new Label();
                if((i+j)%2==0)
                    label[i][j].setBackground(Color.black);
                else
                    label[i][j].setBackground(Color.white);
                chessboard.add(label[i][j]);
            }
        }
        chessboard.setBounds(400,200,500,500);
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("test4");
        frame.setContentPane(new test4().pmain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1080,960);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);


    }
}
