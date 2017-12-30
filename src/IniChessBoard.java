
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IniChessBoard {

    //set int cubewidth and boardwidth  
    int cubewidth = 45;
    int boardwidth = 8 * cubewidth ;

    public static void main (String[] args)
    {
        IniChessBoard gui = new IniChessBoard();
        gui.go();
    }

    public void go()
    {
        //new a frame  
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //new a panel  
        MyDrawPanel drawPanel = new MyDrawPanel();

        frame.getContentPane().add(drawPanel);
        frame.setSize(380,400);
        frame.setVisible(true);
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getButton() == MouseEvent.BUTTON1)
                {
                    int x = e.getX();
                    int y = e.getY();
                    String banner = "鼠标当前点击位置的坐标是"+x+","+y;
                    System.out.println(banner);
                }
            }
        });

    }

    class MyDrawPanel extends JPanel
    {
        public void paintComponent(Graphics G)
        {
            //draw the chess board  
            G.setColor(Color.black);
            G.fillRect(0, 0, boardwidth, boardwidth);
            G.setColor(Color.white);
            for (int Line = 0; Line < 8; Line ++)
            {
                for (int Row = 0; Row < 8; Row ++)
                {
                    if((Line - Row) % 2 == 0 )
                    {
                        G.fillRect(Line * cubewidth , Row * cubewidth , cubewidth, cubewidth);
                    }
                }
            }
        }
    }
}  