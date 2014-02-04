import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;


public class Loot implements Info{
    
    private int x;
    private int y;
    private int value;
    private int lifespan;
    private int life;
    
    private boolean dead;
    private int rare;
    
    private Image pic;
    private Dino obs;
    private Rectangle hitBox;
    
    public Loot(int x, int y, Dino obs){
        this.x=x;
        this.y=y;
        this.obs=obs;
        this.lifespan=LOOT_LIFESPAWN;
        this.life=0;
        this.pic=obs.loot;
        this.dead=false;
        this.rare=0;
        this.hitBox=new Rectangle(x,y,LOOT_DEFAULT_SIZE,LOOT_DEFAULT_SIZE);
        
        this.value=1; ///????
        if ((Math.random()*19)+1<=2){
            this.value=5;
            this.rare=1;
            System.out.println("RARE CREATED");
        }
        
    }
    
    public void update(){
        this.life++;
        if (this.life>=this.lifespan){
            this.dead=true;
            //System.out.println("LOOT DIED!!!!!!");
        }
    }
    
    public boolean checkCursor(Point mouse){
        if (this.hitBox.contains(mouse)){
            this.dead=true;
            this.obs.money+=this.value;
            return true;
        }
        return false;
    }
    
    public void draw(Graphics g){
        if (this.dead) return;
        if (this.life%100<=49){
            //g.drawImage(this.pic, this.x, this.y+((this.life%50)/10),this.obs);
            g.drawImage(this.pic, this.x, this.y+((this.life%50)/10),this.x+LOOT_DEFAULT_SIZE,this.y+LOOT_DEFAULT_SIZE,
                    this.rare*LOOT_DEFAULT_SIZE,0,(this.rare+1)*LOOT_DEFAULT_SIZE,LOOT_DEFAULT_SIZE,this.obs);
        }else{
            //g.drawImage(this.pic, this.x, this.y+Math.round((50-(this.life%50))/10),this.obs);
            g.drawImage(this.pic, this.x, this.y+Math.round((50-(this.life%50))/10),this.x+LOOT_DEFAULT_SIZE,this.y+LOOT_DEFAULT_SIZE,
                    this.rare*LOOT_DEFAULT_SIZE,0,(this.rare+1)*LOOT_DEFAULT_SIZE,LOOT_DEFAULT_SIZE,this.obs);
        }
        
        
        if (DEBUG){
          //DecimalFormat df = new DecimalFormat("##.##");
            g.setColor(Color.white);
            g.drawRect(getX(),getY(),LOOT_DEFAULT_SIZE,LOOT_DEFAULT_SIZE);
            //g.setColor(Color.black);
            //if (this.age>=70) g.setColor(Color.red.darker());
            //g.drawString("d:"+(df.format(this.direction)),(int) this.x-15,(int) this.y-5);
        }
    }
    
    public boolean isDead(){
        return this.dead;
    }
    
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }

}
