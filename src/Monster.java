import javax.swing.*;

class square{
    int a,b;
    square(int x,int y)
    {
        a = x;
        b = y;
    }
    square(){};
}
public class Monster {
    boolean Atk_state = false;
    boolean Dfs_state = false;
    int x;
    int y;
    Monster(){

    };
    public int getId(){
        return id;
    }

    private int id;
    String name;
    int Hp;
    int Atk;
    int level;
    int rollpoint;
    int cubetype;
    String Description;
    boolean summon_pre = false;
    ImageIcon pic_hand;
    ImageIcon pic_big;
    public square method[][]=new square [11][5];
    public square layout[]=new square[5];
    public void setMethod() {
        for(int i=0;i<11;i++)
        {
            for(int j=0;j<5;j++)
            {
                method[i][j] = new square();
            }
        }
        for(int j=0;j<=5;j++) {
            if(j<=3) {
                this.method[j][0].a = 0;
                this.method[j][0].b = 1;
                this.method[j][1].a=j;
                this.method[j][1].b=-1;
            }
            else{
                this.method[j][0].a=1;
                this.method[j][0].b=1;
                this.method[j][1].a=j-3;
                this.method[j][1].b=-1;
            }
            for (int i = 2; i <= 4; i++) {
                this.method[j][i].a = i - 1;
                this.method[j][i].b = 0;
            }
        }

        for(int j=6;j<=9;j++){
            if(j<=8){
                this.method[j][0].a=j-6;
                this.method[j][0].b=-1;
            }
            else{
                this.method[j][0].a=4;
                this.method[j][0].b=1;
            }
            for(int i=1;i<=4;i++){
                if(i<3) {
                    this.method[j][i].a = i;
                    this.method[j][i].b = 0;
                }
                else{
                    this.method[j][i].a=i-1;
                    this.method[j][i].b=1;
                }
            }
        }
        for(int i=0;i<=4;i++){
            if(i<1){
                this.method[10][i].a=1;
                this.method[10][i].b=0;
            }
            else if(i<3){
                this.method[10][i].a=i;
                this.method[10][i].b=1;
            }
            else{
                this.method[10][i].a=i-1;
                this.method[10][i].b=2;
            }
        }
    }

    public void setLayout() {
        for(int i=0;i<5;i++)
        {
            layout[i] = new square(0,0);
            layout[i] = method[cubetype][i];
        }
    }

    public square[] getLayout() {
        return layout;
    }
    public square face[]=new square[6];
    public void setFace(int f[]){
        for(int i=0;i<6;i++)
        {
            face[i] = new square();
        }
        for(int i=0;i<6;i++){
            this.face[i].a=f[2*i];
            this.face[i].b=f[2*i+1];
        }
    }
    Monster(String name,int id,int level,int attack,int Hp, int cubetype,String description)
    {
        this.name = name;
        this.id = id;
        this.level = level;
        this.Atk=attack;
        this.Hp=Hp;
        this.cubetype=cubetype;
        this.Description=description;
        this.pic_hand = new ImageIcon("./hand_img/"+this.id+".jpg");
        this.pic_big = new ImageIcon("./pic_img/pic_"+this.id+".jpg");
    }

}
