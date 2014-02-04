import java.awt.Graphics;
import java.awt.Image;


public class Bomber extends Dinosaur implements Info {
    
    final double MISSILESPEED=1.6;  //not really final! change
    final int COOLDOWN=50;
    final int DAMAGE=3;
    
    public Bomber(int x, Image pic, Dino obs){
        super(x, pic, obs);
        this.currentCool=10;
    }

    public void shoot(double direction) {
        if (this.currentCool>this.COOLDOWN){
            this.projectiles.add(new Bomb(direction,this.x+DINOSAUR_WIDTH/2, this.MISSILESPEED, this));
            this.currentCool=0;
        }
    }
    
    public void testShoot(double direction, int mouseX, int mouseY) {
        this.projectiles.add(new Missile(direction, mouseX, mouseY, this.MISSILESPEED));
    }
    
    public void draw(Graphics g) {
//        g.setColor(Color.magenta.darker());
//        g.fillRect(this.x, this.y, DINOSAUR_WIDTH, DINOSAUR_HEIGHT);
//        g.setColor(Color.black);
//        g.drawRect(this.x-1, this.y-1, DINOSAUR_WIDTH+1, DINOSAUR_HEIGHT+2);
        g.drawImage(this.pic,this.x,this.y,this.obs);
        if (this.obs.hitBoxes()){
            g.drawRect(this.getHitBox().x,this.getHitBox().y,this.getHitBox().width,this.getHitBox().height);
        }
    }
    

    public int getDamage(){
        return this.DAMAGE;
    }

}
