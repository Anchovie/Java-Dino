import java.awt.Graphics;
import java.awt.Image;


public class Gunner extends Dinosaur implements Info {
    
    final double MISSILESPEED=2.3;  //not really final! change 2.3
    final int COOLDOWN=7;
    final int DAMAGE=1;
    
    public Gunner(int x, Image pic, Dino obs){
        super(x, pic, obs);
        this.currentCool=10;
    }

    public void shoot(double direction) {
        if (this.currentCool>this.COOLDOWN){
            this.projectiles.add(new Missile(direction,this.x+DINOSAUR_WIDTH/2/*+3*/, this.MISSILESPEED, this));  ///// THIS CAUSES THE AIMING ERROR IF CHANGED
            this.currentCool=0;
        }
    }
    
    public void testShoot(double direction, int mouseX, int mouseY) {
        this.projectiles.add(new Missile(direction, mouseX, mouseY, this.MISSILESPEED));
    }
    
    public void draw(Graphics g) {
//        g.setColor(Color.green.darker());
//        g.fillRect(this.x, this.y, DINOSAUR_WIDTH, DINOSAUR_HEIGHT);
//        g.setColor(Color.black);
//        g.drawRect(this.x-1, this.y-1, DINOSAUR_WIDTH+1, DINOSAUR_HEIGHT+2);
        g.drawImage(this.pic,this.x,this.y,this.obs);
        if (this.obs.hitBoxes()){
            g.drawRect(this.getHitBox().x,this.getHitBox().y,this.getHitBox().width,this.getHitBox().height);
        }
    }
    
    public void drawPlacement(Graphics g, int x){
        g.drawImage(this.pic,x,this.y,this.obs);
    }
    

    public int getDamage(){
        return this.DAMAGE;
    }

}
