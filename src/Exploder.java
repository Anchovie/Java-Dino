import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;


public class Exploder extends Dinosaur implements Info{
    
    final double MISSILESPEED=25;  //not really final! change
    final int COOLDOWN=80;
    final int DAMAGE=999;
    
    Meteor target;
    double firstX, firstY;
    
    public Exploder(int x, Image pic, Dino obs){
        super(x, pic, obs);
        this.currentCool=10;
    }

    public void shoot(double direction) {
        int target;
        //double direction;
        if (this.currentCool>this.COOLDOWN){
            
            if (this.obs.getMeteors()!=null && this.obs.getMeteors().size()>0){
                target =(int) Math.round(Math.random()*(this.obs.getMeteors().size()-1));
                if (this.obs.getMeteors().get(target)!=null){
                    this.target=this.obs.getMeteors().get(target);          ///WHILE - LOOPPI??
                    this.firstX=this.target.getX();
                    this.firstY=this.target.getY();
                    //this.obs.getMeteors().remove(target);
                    direction=Math.atan2( (this.target.getY()+METEOR_DEFAULT_SIZE/2) - this.y, (this.target.getX()-40+(METEOR_DEFAULT_SIZE/2))-(this.x-DINOSAUR_WIDTH/2));
                    //direction=Math.atan2(this.mouseY-this.dinosaurs.get(i).getY()/*+10/*!!*/, this.mouseX-this.dinosaurs.get(i).getX()-DINOSAUR_WIDTH/2/*+10/*!!*/)
                    this.projectiles.add(new Missile(direction,this.x+DINOSAUR_WIDTH/2, this.MISSILESPEED, this));
                    this.currentCool=0;
                }
            }
            
        }
    }
    
    public void testShoot(double direction, int mouseX, int mouseY) {
        //this.projectiles.add(new Missile(direction, mouseX, mouseY, this.MISSILESPEED));
    }
    
    public void draw(Graphics g) {
//        g.setColor(Color.magenta.darker());
//        g.fillRect(this.x, this.y, DINOSAUR_WIDTH, DINOSAUR_HEIGHT);
//        g.setColor(Color.black);
//        g.drawRect(this.x-1, this.y-1, DINOSAUR_WIDTH+1, DINOSAUR_HEIGHT+2);
        g.drawImage(this.pic,this.x,this.y,this.obs);
        
        if (this.currentCool<=30 && this.target!=null){
            g.setColor(Color.red);
            g.drawString("BOOM!!",this.target.getX()+10,this.target.getY());
            g.fillOval(this.target.getX()+5, this.target.getY()+15, 10, 10);
            
            g.setColor(Color.white);
            g.fillOval((int)this.firstX+5, (int)this.firstY+15, 10, 10);
        }
        if (this.obs.hitBoxes()){
            g.drawRect(this.getHitBox().x,this.getHitBox().y,this.getHitBox().width,this.getHitBox().height);
        }
    }
    

    public int getDamage(){
        return this.DAMAGE;
    }

}
