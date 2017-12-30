import java.util.ArrayList;
import java.util.Scanner;

class WinnerException extends Exception{

}

class DeckRunOutException extends Exception{

}

class conflictException extends Exception{
    String message;
    conflictException(String s)
    {
        this.message = s;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

public class Game {
    private player p1 = new player("Tom",1,"蓝");
    private player p2 = new player("Jim",2,"红");
    private final int size = 21;
    private int board[][] = new int[size][size];//棋盘陆地和海洋分布数组
    private int board_M[][] = new int[size][size];//棋盘怪兽分布数组
    private ArrayList<Monster> property = new ArrayList<Monster>();
    private ArrayList<Monster> tmp = new ArrayList<Monster>();//

    //初始化怪兽库
    public void Monster_init(){
//        Monster m1 = new Monster(2,"Demon",1,7,3,5,10);
//        m1.setFace(1,0,1,3,1,0,1,2,5,4,3,2);
//        Monster m2 = new Monster(3,"Angel",1,5,4,6,7);
//        m2.setFace(1,0,1,2,1,1,5,1,1,0,2,2);
//        Monster m3 = new Monster(4,"Knight",2,5,5,6,4);
//        m3.setFace(2,3,3,1,1,0,4,2,1,0,1,0);
//        property.add(m1);
//        property.add(m2);
//        property.add(m3);
//        property.add(new Monster(5,"Magician",2,2,3,5,6));
//        property.get(3).setFace(5,2,1,0,4,3,3,1,1,0,1,0);
//        property.add(new Monster(6,"Fairy",1,1,2,3,1));
//        property.get(4).setFace(1,0,2,4,1,0,3,2,1,0,1,0);
//        property.add(new Monster(7,"Monkey_King",1,4,2,4,2));
//        property.get(5).setFace(2,3,1,0,1,0,5,1,1,0,1,0);

    }

    //抽卡
    public void chou_ka(player p){
        if(p.deck.size()>=3)
        {
            for(int i=0;i<3;i++)
            {
                int seed = (int)(Math.random()*p.deck.size());
                p.hand.add(p.deck.get(seed));
                p.deck.remove(p.deck.get(seed));
            }
        }
        else{
            p.hand.addAll(p.deck);
            p.deck.clear();
        }
    }

    //全局初始化
    public void init() {
        System.out.println("Welcom to DDM,Now Loading...");
        //初始化怪兽库
        Monster_init();
        p1.init();
        p2.init();
        //初始化棋盘
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                board[i][j] = 0;    //0表示海洋
                board_M[i][j] = 0;//0表示无怪兽
            }
        }
        for(int i=0;i<size;i++)
        {
            board[0][i] = -3;       //-3是不可以进入
            board[i][0] = -3;
            board[i][size-1] = -3;
            board[size-1][i] = -3;

            board_M[0][i] = -3;
            board_M[i][0] = -3;
            board_M[i][size-1] = -3;
            board_M[size-1][i] = -3;

        }
        for(int i=1;i<size-1;i++)
        {
            board[1][i] = -2;       //-2是红
            board[size-2][i] = -1;  //-1是蓝
        }
        //加载卡组
        for(Monster m:property)
        {
            p1.deck.add(m);
            p2.deck.add(m);
        }
        p1.target_x = size/2;
        p1.target_y = size - 2;
        p2.target_x = size/2;
        p2.target_y = 1;
        //board[p1.target_x][p1.target_y] = -2;
//        board[p2.target_x][p2.target_y] = -1;
        //抽卡
        chou_ka(p1);
        chou_ka(p2);
        update();
    }

    //投掷色子
    public String[] roll(ArrayList<Monster> tmp,player p)throws Exception{
        try{
        System.out.println("开始投掷骰子：");
        String []result = new String[3];
        for(int i=0;i<3;i++){
            int d = (int)(Math.random()*6);
            switch (tmp.get(i).face[d].a){
                case 1:{
                    tmp.get(i).summon_pre=true;
                    result[i]="召唤";
                    break;
                }
                case 2:{
                    tmp.get(i).summon_pre=false;
                    p.Mp+=tmp.get(i).face[d].b;
                    result[i]="行动";
                    break;
                }
                case 3:{
                    tmp.get(i).summon_pre=false;
                    p.Mp+=tmp.get(i).face[d].b;
                    result[i]="攻击";
                    break;
                }
                case 4:{
                    tmp.get(i).summon_pre=false;
                    p.Mp=tmp.get(i).face[d].b;
                    result[i]="防御";
                    break;
                }
                case 5:{
                    tmp.get(i).summon_pre=false;
                    p.Mp=tmp.get(i).face[d].b;
                    result[i]="魔法";
                    break;
                }
                case 6:{
                    tmp.get(i).summon_pre=false;
                    p.Mp=tmp.get(i).face[d].b;
                    result[i]="魔法";
                    break;
                }
            }
        }
        return result;}catch (Exception e){
            throw e;
        }
    }



    public boolean check(){
            if(p1.Hp == 0 || p2.Hp == 0)return true;
            return false;
        //游戏胜利条件设置
    }

    //召唤准备
    public boolean summon_check(ArrayList<Monster> tmp){
        if(tmp.get(0).summon_pre)
        {
            if(tmp.get(1).summon_pre && tmp.get(1).level == tmp.get(0).level )
            {
                if (tmp.get(2).summon_pre && tmp.get(2).level == tmp.get(0).level)
                {
                    System.out.println("可召唤："+ tmp.get(0).name+" "+tmp.get(1).name+" "+tmp.get(2).name);
                    return true;
                }
                else
                {
                    System.out.println("可召唤："+tmp.get(0).name+" "+tmp.get(1).name);
                    tmp.get(2).summon_pre = false;
                    return true;
                }
            }
            else if(tmp.get(2).summon_pre&&tmp.get(2).level == tmp.get(0).level)
            {
                System.out.println("可召唤："+tmp.get(0).name +" "+tmp.get(2).name);
                tmp.get(1).summon_pre = false;
                return true;
            }
            else {
                if(tmp.get(1).summon_pre) {
                    tmp.get(0).summon_pre = false;
                    if (tmp.get(2).summon_pre && tmp.get(2).level == tmp.get(1).level) {
                        System.out.println("可召唤：" + tmp.get(1).name + " " + tmp.get(2).name);
                        return true;
                    }
                }
            }
        }
        else if(tmp.get(1).summon_pre)
        {
            tmp.get(0).summon_pre = false;
            if(tmp.get(2).summon_pre && tmp.get(2).level == tmp.get(1).level)
            {
                System.out.println("可召唤："+tmp.get(1).name + " "+tmp.get(2).name);
                return true;
            }
        }
        System.out.println("抱歉，无法召唤");
        return false;
    }

    //召唤怪兽
    public void summon(player owner,Monster m) {
        int temp, x, y, count = 0, p;
        System.out.println("请输入召唤怪兽的位置：");
        Scanner reader = new Scanner(System.in);
        x = reader.nextInt();
        y = reader.nextInt();
        m.setMethod();
        m.setLayout();
        if (owner.id == 2) {
            p = 2;
            for (int i = 0; i < 5; i++) {
                if (board[x + m.layout[i].a][y + m.layout[i].b] != 0&&board[x][y]!=0) {
                    count = -1;
                    break;
                }
                if (count == 0)
                    if (board[x + m.layout[i].a + 1][y + m.layout[i].b] == -1 * p || board[x + m.layout[i].a - 1][y + m.layout[i].b] == -1 * p || board[x + m.layout[i].a][y + m.layout[i].b + 1] == -1 * p || board[x + m.layout[i].a][y + m.layout[i].b - 1] == -1 * p)
                        count++;
            }
            if(board[x+1][y]==-1*p||board[x-1][y]==-1*p||board[x][y+1]==-1*p||board[x][y-1]==-1*p)
                count++;
            if (count == 1) {
                System.out.println("召唤成功");
                board_M[x][y] = m.getId();
                board[x][y] = -1 * p;
                owner.alive.add(m);
                for (int i = 0; i < 5; i++) {
                    board[x + m.layout[i].a][y + m.layout[i].b] = -1 * p;
                }
                update();
                //召唤完成后刷新信息
            } else {
                System.out.println("召唤失败");
                summon(owner, m);
            }
        } else {
            p = 1;
            for (int i = 0; i < 5; i++) {
                if (board[x - m.layout[i].a][y + m.layout[i].b] != 0&&board[x][y]!=0) {
                    count = -1;
                    break;
                }
                if (count == 0)
                    if (board[x - m.layout[i].a + 1][y + m.layout[i].b] == -1 * p || board[x - m.layout[i].a - 1][y + m.layout[i].b] == -1 * p || board[x - m.layout[i].a][y + m.layout[i].b + 1] == -1 * p || board[x - m.layout[i].a][y + m.layout[i].b - 1] == -1 * p)
                        count=1;
            }
            if(count!=1) {
                if (board[x + 1][y] == -1 * p || board[x - 1][y] == -1 * p || board[x][y + 1] == -1 * p || board[x][y - 1] == -1 * p)
                    count++;
            }
            if (count == 1) {
                System.out.println("召唤成功");
                board_M[x][y] = m.getId();
                board[x][y] = -1 * p;
                m.x = x;
                m.y = y;
                owner.alive.add(m);
                for (int i = 0; i < 5; i++) {
                    board[x - m.layout[i].a][y + m.layout[i].b] = -1 * p;
                }
                update();
                //召唤完成后刷新信息
            } else {
                System.out.println("召唤失败");
                count = 0;
                summon(owner, m);
            }

        }
    }

    public void Attack(player p,player opponent) throws WinnerException{
        Scanner in = new Scanner(System.in);
        String cmd;
        System.out.println(p.name+"可以使用的怪兽有：");
        for(Monster m:p.alive)
        {
            if(m.Atk_state) {
                System.out.print(m.name + "(" + m.x + "," + m.y + ")\\");
            }
        }
        System.out.println();
        System.out.println("请选择你要使用的怪兽：");
        ArrayList<Monster>targets = new ArrayList<Monster>();
        Monster attacker = new Monster();
        cmd = in.next();
        for(Monster m:p.alive)
        {
            if(m.name.equals(cmd))
            {
                attacker = m;
                System.out.println("你可进行攻击的目标有：");
                int x = m.x;
                int y = m.y;
                if(x == p.target_x && y== p.target_y)System.out.print(opponent.name+"\\");
                if(board_M[x-1][y]>0){
                    for(Monster e:opponent.alive)
                    {
                        if(board_M[x-1][y] == e.getId())
                        {
                            targets.add(e);
                            System.out.print(e.name+"\\");
                        }
                    }
                }

                if(board_M[x][y-1]>0){
                    for(Monster e:opponent.alive)
                    {
                        if(board_M[x][y-1] == e.getId())
                        {
                            targets.add(e);
                            System.out.print(e.name+"\\");
                        }
                    }
                }

                if(board_M[x+1][y]>0){
                    for(Monster e:opponent.alive)
                    {
                        if(board_M[x+1][y] == e.getId())
                        {
                            targets.add(e);
                            System.out.print(e.name+"\\");
                        }
                    }
                }

                if(board_M[x][y+1]>0){
                    for(Monster e:opponent.alive)
                    {
                        if(board_M[x][y+1] == e.getId())
                        {
                            targets.add(e);
                            System.out.print(e.name+"\\");
                        }
                    }
                }
                break;
            }
        }

        System.out.println("请选择攻击的目标（输入名称）按Q退出：");
        cmd = in.next();
        if(cmd.charAt(0) == 'Q')return;
        else if(cmd.equals(opponent.name)){
            opponent.Hp--;
            if(check())throw new WinnerException();
        }
        else{
            for(Monster e:opponent.alive){
                if(e.name.equals(cmd)){
                    if(e.Dfs_state)
                    {
                        e.Hp -= attacker.Atk ;
                    }
                    else
                    {
                        e.Hp -= attacker.Atk;
                    }
                    if(e.Hp<=0)
                    {
                        if(opponent.id == 1)board[e.x][e.y] = -1;
                        else board_M[e.x][e.y] =0;
                        opponent.alive.remove(e);
                    }
                    break;
                }
            }
        }
        attacker.Atk_state = false;
        attacker.Dfs_state = false;
        update();

    }

    public void Defence(player p){
        Scanner in = new Scanner(System.in);
        String cmd;
        System.out.println("现在可供操作的怪兽有：");
        for(Monster m:p.alive){
            if(!m.Dfs_state){
                System.out.print(m.name + "(" + m.x + "," + m.y + ")\\");
            }
        }
        System.out.println("请选择设置状态的怪兽：");
        cmd = in.next();
        for(Monster m:p.alive){
            if(!m.Dfs_state && cmd.equals(m.name))
            {
                m.Dfs_state = true;
                System.out.println(m.name+"已设置成防守状态");
                break;
            }
        }

    }

    public void Magic(player p){

    }

    public void Move(player p)throws Exception{
        Scanner in = new Scanner(System.in);
        int move = p.Mp;
        System.out.println("可选择移动的怪兽(怪兽名 目标坐标x 目标坐标y）");
        for(Monster m:p.alive)
        {
            System.out.print(m.name + "(" + m.x + "," + m.y + ")\\");
        }
        System.out.println();
        String cmd = in.next();//读入怪兽名
        Monster monster = new Monster();
        int x = in.nextInt();// 读入x
        int y = in.nextInt();// 读入y
        int flag = 0;
        for(Monster m:p.alive)
        {
            if(cmd.equals(m.name))
            {
                monster = m;
                flag = 1;
                break;
            }
        }
        if(flag == 0)throw new Exception("没有找到"+cmd+"  请重新输入");

//        if(monster<move)move = monster.move;
        if(board[x][y]>=0 || board_M[x][y]>0)throw new conflictException("该目标地点无法进入，请重新输入");
        else if((Math.abs(x-monster.x)+Math.abs(y-monster.y))>move)throw new conflictException("目标地点超越你的可行范围，请重新输入");
        else
        {
            board_M[monster.x][monster.y] = 0;
            monster.x = x;
            monster.y = y;
            board_M[x][y] = monster.getId();
            System.out.println(monster.name+"移动成功");
            p.Mp -= Math.abs(x-monster.x)+Math.abs(y-monster.y);
        }

    }


    public void update(){
        //展示棋盘信息
        for(int i=1;i<size-1;i++)
        {
            System.out.print("\n");
            for(int j=1;j<size-1;j++)
            {
                if(board[i][j] == 0) {
                    System.out.print("海 ");
                }
                else if(board[i][j] == -1){
                    if(board_M[i][j]>0){
                        for(Monster m:property)
                        {
                            if(m.getId()==board_M[i][j])
                            {
                                System.out.print(m.name);
                            }
                        }
                    }
                    else {
                        System.out.print("蓝 ");
                    }
                }
                else if(board[i][j] == -2){
                    if(board_M[i][j]>0){
                        for(Monster m:property)
                        {
                            if(m.getId()==board_M[i][j])
                            {
                                System.out.print(m.name+" ");
                            }
                        }
                    }
                    else {
                        System.out.print("红 ");
                    }
                }
                else
                {
                    for(Monster m:property)
                    {
                        if(m.getId() == board[i][j])
                        {
                            System.out.print(m.name+"  ");
                        }
                    }
                }
            }
        }
        System.out.println();
        //展示p1信息
        System.out.println(p1.name+"("+p1.color+")"+"目前的状态是：");
        System.out.println("Hp:"+p1.Hp+"行动点"+p1.Mp );
        System.out.println();
        //展示p2信息
        System.out.println(p2.name+"("+p2.color+")"+"目前的状态是：");
        System.out.println("Hp:"+p2.Hp+"行动点"+p2.Mp );
    }

    //准备阶段具体实现
    private void SP(player p)throws DeckRunOutException{
        System.out.println("进入准备阶段，抽卡！");
        Scanner in = new Scanner(System.in);
        String cmd;
            chou_ka(p);
            System.out.println(p.name+"目前的手牌有：");
            for(Monster m:p.hand) {
                System.out.print(m.name+" ");
            }

            for(int i=1;i<=3;i++) {
                int flag = 0;
                System.out.println("请选择三只怪兽("+i+")：");
                cmd = in.next();
                for (int j=0;j<p.hand.size();j++) {
                    if (p.hand.get(j).name.equals(cmd)) {
                        tmp.add(p.hand.get(j));
                        p.hand.remove(p.hand.get(j));
                        flag = 1;
                        System.out.println(cmd+"选择成功");
                    }
                }
                if(flag == 0){
                    System.out.println("没有找到"+cmd+",请重新输入");
                    break;
                }
            }
    }

    //主要阶段1具体实现
    private void M1(player p){
        Scanner in = new Scanner(System.in);
        String cmd;
        while(true) {
            System.out.println("目前你可以执行的操作有：(输入Q进入下一阶段,输入V查看战场信息）");
            if(p.Mp>0&&!p.alive.isEmpty())System.out.print("魔法（M） ");
            if(p.Mp>0&&!p.alive.isEmpty())System.out.print("移动（T） ");
            System.out.print("投掷（R）\n");
            cmd = in.next();
            if(cmd.charAt(0) == 'Q')break;
            else if(cmd.charAt(0) == 'V')update();
            else if(cmd.charAt(0) == 'T')try{Move(p);}catch (Exception e){System.out.println(e.getMessage());}
            else if(cmd.charAt(0)=='R') {
                try {
                    String[] result = roll(tmp, p);
                    for (int i = 0; i < result.length; i++) {
                        System.out.print(result[i] + ' ');
//                    if (result[i].equals("攻击")) p.Ap++;
//                    if (result[i].equals("魔法")) p.Mp++;
//                    if (result[i].equals("移动")) p.Mop++;
//                    if (result[i].equals("防御")) p.Dp++;
                    }

                    if (summon_check(tmp)) {
                        System.out.println("请选择你要召唤的怪兽:");
                        cmd = in.next();
                        for (int i = 0; i < tmp.size(); i++) {
                            if (tmp.get(i).summon_pre && tmp.get(i).name.equals(cmd)) {
                                summon(p, tmp.get(i));
                                tmp.get(i).summon_pre = false;
                                tmp.remove(tmp.get(i));
                                ;
                            } else {
                                tmp.get(i).summon_pre = false;
                            }
                        }
                        for (Monster m : tmp) {
                            m.summon_pre = false;
                        }

                    }
                    p.deck.addAll(tmp);
                    tmp.clear();
                    break;

                }catch (Exception e){
                    e.printStackTrace();
                System.out.println("指令输入失败，请重新输入指令");
            }
            }

        }


    }

    //战斗阶段具体实现
    private void BP(player p,player opponent)throws WinnerException{
        System.out.println("进入战斗阶段");
        Scanner in = new Scanner(System.in);
        String cmd;
        while(true) {
            System.out.println("目前你可以执行的操作有：(输入Q进入下一阶段,输入V查看战场信息）");
            if(p.Mp>0&&!p.alive.isEmpty())System.out.print("攻击（A） ");
            if(p.Mp>0&&!p.alive.isEmpty())System.out.print("移动（T） ");
            cmd = in.next();
            if(cmd.charAt(0) == 'Q')break;
            else if(cmd.charAt(0) == 'V')update();
            else if(cmd.charAt(0)=='M')Magic(p);
            else if(cmd.charAt(0)=='D')Defence(p);
            else if(cmd.charAt(0) =='T')
                try{Move(p);}catch (Exception e){System.out.println(e.getMessage());}
            else if(cmd.charAt(0)=='A')Attack(p,opponent);
        }


    }

    //主要阶段2具体实现
    private void M2(player p)throws WinnerException{
        System.out.println("进入主要阶段2：");
        Scanner in = new Scanner(System.in);
        String cmd;
        while(true) {
            System.out.println("目前你可以执行的操作有：(输入Q结束回合,输入V查看战场信息）");
            if(p.Mp>0&&!p.alive.isEmpty())System.out.print("魔法（M） ");
            if(p.Mp>0&&!p.alive.isEmpty())System.out.print("移动（T） ");
            if (p.Mp>0&&!p.alive.isEmpty())System.out.print("防御（D） ");
            cmd = in.next();
            if(cmd.charAt(0) == 'Q')break;
            else if(cmd.charAt(0) == 'V')update();
            else if(cmd.charAt(0)=='M')Magic(p);
            else if(cmd.charAt(0)=='D')Defence(p);
            else if(cmd.charAt(0) =='T')try{Move(p);}catch (Exception e){System.out.println(e.getMessage());}
        }

    }

    //游戏结束画面
    private void end(player p){
        System.out.println("Game Over!The Winner is"+p.name);
    }

    //游戏流程
    public void run() {
        int cnt = 0;
        while (true) {
            //判断现在是谁的回合
            player current_player;
            player opponent;
            if (cnt == 0) {
                current_player = p1;
                opponent = p2;
            }
            else {
                current_player = p2;
                opponent = p1;
            }
            System.out.println("现在是"+current_player.name+"的回合");
            //准备阶段
            try {
                SP(current_player);
            }catch (DeckRunOutException e){
                end(opponent);
                break;
            }
            //主要阶段1
            M1(current_player);
            //战斗阶段
            try{
                BP(current_player,opponent);
            }catch (WinnerException e){
                end(current_player);
                break;
            }
            //主要阶段2
            try {
                M2(current_player);
            }catch (WinnerException e){
                end(current_player);
                break;
            }
            //对方回合
            cnt = ~cnt;
        }
    }

    public static  void main(String[]args){
        Game game = new Game();
        game.init();
        game.run();
    }
}
