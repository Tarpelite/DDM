//import javax.swing.*;
//import java.awt.*;
//
//interface gameConfig{
//    String title = "DDM";
//    int frameX = 700;
//    int frameY = 700;
//    int panelX = 650;
//    int panelY = 650;
//
//    int elsesize = 50;
//    int playersize = 50;
//
//    //游戏素材
//    ImageIcon icon0 = new ImageIcon("1.jpg");
//
//
//    //镜头
//    ImageIcon shadow1 = new ImageIcon("pic_1.jpg");
//}
//public class mainFrame extends JFrame implements gameConfig{
//     //游戏面板
//    JPanel panel;
//
//    public mainFrame(){
//        init();
//    }
//    /**
//     * 设置窗体
//     */
//
//    public void init(){
//        this.setTitle(title);
//        this.setSize(frameX,frameY);
//        this.setLayout(new FlowLayout());
//        this.setDefaultCloseOperation(3);
//        //创建游戏面板
//        panel = setpanel();
//
//        this.add(panel);
//        this.setVisible(true);
//
//        //安装键盘监听器
//        PanelListenner plis = new PanelListenner();
//        this.addKeyListener(plis);
//
//        //启动人物移动线程
//        Player player = new Player();
//        player.start();
//
//        //启动刷新面板线程
//        UpdateThread ut = new UpdateThread(panel);
//        ut.start();
//
//    }
//
//    /**
//     * 设置游戏面板
//     */
//    public JPanel setpanel(){
//        JPanel panel = new MyPanel();
//        panel.setPreferredSize(new Dimension(panelX, panelY));
//        panel.setLayout(null);
//        panel.setBackground(Color.black);
//
//        return panel;
//    }
//
//    /**
//     * 内部游戏按键监听类
//     * @author yy
//     *
//     */
//    class PanelListenner extends KeyAdapter{
//        //当按键按下
//        public void keyPressed(KeyEvent e){
//            int code = e.getKeyCode();
//            switch (code) {
//                case KeyEvent.VK_UP:
//                    Player.up = true;
//                    break;
//                case KeyEvent.VK_DOWN:
//                    Player.down = true;
//                    break;
//                case KeyEvent.VK_LEFT:
//                    Player.left = true;
//                    break;
//                case KeyEvent.VK_RIGHT:
//                    Player.right = true;
//                    break;
//
//                default:
//                    break;
//            }
//        }
//        //当按键释放
//        public void keyReleased(KeyEvent e){
//            int code = e.getKeyCode();
//            switch (code) {
//                case KeyEvent.VK_UP:
//                    Player.up = false;
//                    break;
//                case KeyEvent.VK_DOWN:
//                    Player.down = false;
//                    break;
//                case KeyEvent.VK_LEFT:
//                    Player.left = false;
//                    break;
//                case KeyEvent.VK_RIGHT:
//                    Player.right = false;
//                    break;
//
//                default:
//                    break;
//            }
//        }
//    }
//    /**
//     * 自定义内部游戏面板类
//     * @author yy
//     *
//     */
//    class MyPanel extends JPanel{
//        @Override
//        public void paint(Graphics g) {
//            super.paint(g);
//            //找到角色旁边的素材，上下左右各5格
//            for(int i=Player.getI()-6;i<=Player.getI()+6;i++){
//                for(int j=Player.getJ()-6;j<=Player.getJ()+6;j++){
//                    //如果这一格没有超界（由于还没处理碰撞，这一条暂时没用  = =！）
//                    if(i>=0&&j>=0&&i<ReadMapFile.map1.length&&j<ReadMapFile.map1[0].length){
//                        //画第一层元素
//                        ImageIcon icon = GetMap.int2icon(ReadMapFile.map1[i][j]);
//                        g.drawImage(icon.getImage(), (Player.px-elesize/2)+((j-Player.getJ())*elesize)-(Player.mx%elesize), (Player.py-elesize/2)+((i-Player.getI())*elesize)-(Player.my%elesize), elesize, elesize, null);
//                        //第二层
//                        ImageIcon icon2 = GetMap.int2icon(ReadMapFile.map2[i][j]);
//                        g.drawImage(icon2.getImage(), (Player.px-elesize/2)+((j-Player.getJ())*elesize)-(Player.mx%elesize), (Player.py-elesize/2)+((i-Player.getI())*elesize)-(Player.my%elesize), elesize, elesize, null);
//                        //第三层
//                        ImageIcon icon3 = GetMap.int2icon(ReadMapFile.map3[i][j]);
//                        g.drawImage(icon3.getImage(), (Player.px-elesize/2)+((j-Player.getJ())*elesize)-(Player.mx%elesize), (Player.py-elesize/2)+((i-Player.getI())*elesize)-(Player.my%elesize), elesize, elesize, null);
//                    }
//                }
//            }
////          g.setColor(Color.black);
////          g.fillRect(0, 0, 50, 650);
////          g.fillRect(0, 0, 650, 50);
////          g.fillRect(600, 0, 50, 650);
////          g.fillRect(0, 600, 650, 50);
//
//            //由于暂时还没弄好游戏角色的移动图，所以角色先用一个黑色的小球代替一下....  = =！
//            g.fillOval(Player.px-elesize/2, Player.py-elesize/2, elesize, elesize);
//            //个人的一个小想法，做一个黑色的图片，然后中间挖空一个圆，加上模糊效果，来模拟人的视野
//            g.drawImage(shadow2.getImage(), 0, 0, 650, 650, null);
//        }
//    }
//    public class UpdateThread extends Thread{
//        JPanel panel;
//        public UpdateThread(JPanel panel) {
//            this.panel = panel;
//        }
//
//        @Override
//        public void run() {
//            while(true){
//                panel.repaint();
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//    }
//}
