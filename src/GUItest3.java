import sun.awt.image.FileImageSource;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GUItest3 {
    public static void main(String[]args)
    {
        JFrame frame = new JFrame();
        frame.setLayout(null);
        frame.setSize(1080,960);
        frame.setVisible(true);

        JPanel pane1=new JPanel();
        GridLayout grid1=new GridLayout(12,12);
        pane1.setLayout(grid1);
        Label label[][]=new Label[12][12];
        for(int i=0;i<12;i++){
            for(int j=0;j<12;j++){
                label[i][j]=new Label();
                if((i+j)%2==0)
                    label[i][j].setBackground(Color.black);
                else
                    label[i][j].setBackground(Color.white);
                pane1.add(label[i][j]);
            }
        }
        File SourceImage = new File("./1.jpg");
        Image image = null;
        try{ image = ImageIO.read(SourceImage);}catch (Exception e){e.printStackTrace();};
        JLabel l1 = new JLabel(new ImageIcon(image));
        l1.setBounds(0,0,350,350);
        frame.getContentPane().add(l1);



//       pane1.setBounds(400,400,500,500);
//       frame.add(pane1);
//        frame.add(pane1,BorderLayout.CENTER);
        frame.add(new JLabel("north"),BorderLayout.NORTH);
        frame.add(new JButton("south"),BorderLayout.SOUTH);
        frame.add(new JButton("west"),BorderLayout.WEST);
       frame.add(new JButton("east"),BorderLayout.EAST);
    }
}
