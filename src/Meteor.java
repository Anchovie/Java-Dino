import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;


public class Meteor implements Info {
    private double x, y;
    private double speed;
    private double direction;   //implement so they don't go straight down
    private int size;
    private int width, height;
    private int blastRadius;    //make sure that blastradius destroys the dinos, not impact
    private int hp, hpMax;
    private Image pic;
    private Image explosion;
    private Dino obs;
    private Rectangle hitbox;
    private boolean hitGround=false;
    private int groundTimer;
    private boolean hitAmmo=false;
    private int ammoTimer;
    
    public Meteor(int x, double speed, int size, double direction, Dino obs){
        this.x=x;
        this.y=0-size;
        this.size=size;
        this.hpMax=this.hp=size*3; /// NOT RIGHT
        this.speed=speed;
        this.direction=(3*Math.PI/2);/*direction;*/
        this.height=this.width=(METEOR_DEFAULT_SIZE*size);  //NOT RIGHT EITHER, depends on size and "meteorlevel"
        this.blastRadius = size*2;
        this.pic=obs.meteor;
        this.explosion=obs.explosion;
        this.obs=obs;
        this.hitbox=new Rectangle((int)Math.round(this.x),(int)Math.round(this.y),this.width,this.height);
        this.groundTimer=0;
        this.ammoTimer=0;
    }
    
    public boolean move(){
        if (!this.hitGround){
            //this.y+=this.speed;
            this.x+=this.speed*Math.cos(this.direction);
            this.y-=this.speed*Math.sin(this.direction);
            this.hitbox=new Rectangle((int)Math.round(this.x),(int)Math.round(this.y),this.width,this.height);
        }
        if (this.y>=BORDER_DOWN && !this.hitAmmo) {
            this.hitGround=true;
            this.y=BORDER_DOWN;
        }
        if (this.hitAmmo && this.ammoTimer>=15){
            return false;
        }
        if (this.hitGround&&this.groundTimer>=14) { //THIS MODIFIES THE GROUND EXPLOSION SIZE
            return false;
        }
        return true;
    }
    
    public void draw(Graphics g){
        int z=this.hpMax-this.hp;
        
        if (!this.hitGround && !this.hitAmmo){  //normal drawing with cracking //IMPLEMENT ROTATION
            g.drawImage(this.pic,getX(),getY(),getX()+this.width,getY()+this.height,METEOR_DEFAULT_SIZE*z,0,METEOR_DEFAULT_SIZE*(z+1),METEOR_DEFAULT_SIZE,this.obs);
            //g.drawImage(this.pic,(int)Math.round(this.x),(int)Math.round(this.y), this.obs);
        } else {
            if (!this.hitAmmo){     //hitting ground
                this.groundTimer+=2;
                g.drawImage(this.explosion,getX()-this.groundTimer*2+5,getY()-this.groundTimer,getX()+15+this.groundTimer*2,getY()+20,0,0,40,20,this.obs);
            } else {            //destroyed by ammo
                this.ammoTimer++;
                g.drawImage(this.pic,getX(),getY(),getX()+20,getY()+20,60,0,80,20,this.obs);
            }
        }
        
        if (this.obs.hitBoxes()){
            g.drawRect((int)this.x, (int)this.y, this.width, this.height);
        }
        
        if(DEBUG){
            g.setColor(Color.white);
            g.drawRect(getX(),getY(),this.height,this.height);
        }
    }
    
    public boolean checkDinoCollision(){
        if (this.y>=400) return false; //no reason to test if not low enough
        ArrayList<Dinosaur> dinos = this.obs.getDinosaurs();
        for (int i=0; i<dinos.size(); i++){
            if (checkCollision(dinos.get(i).getHitBox())){
                
                dinos.get(i).kill();
                System.out.println("Dino #" + i + " KILLED! Should soon disappear");
                this.hp=0;
                return true;
            }
        }
        return false;
    }
    

    public boolean checkCollision(Rectangle target){
        if (this.hitbox.intersects(target) && !this.hitAmmo){
            return true;
        }
       return false;
    }
    
    public boolean takeDamage(int amount){
        this.hp-=amount;
        if (this.hp<=0){
            this.hp=this.hpMax;
            return this.substractSize(1);
        }
        return this.substractSize(0);
    }
    
    public boolean substractSize(int amount){  // IMPLEMENT BREAKING INTO SMALLER PIECES OR SHRINKING
        
        this.size-=amount;
        this.height=this.width=(METEOR_DEFAULT_SIZE*this.size);
        if (this.size<=0) {
            this.hitAmmo=true;
            this.obs.kills++; //shitty way
            this.obs.createLoot(this);
            if (this.hitAmmo && this.ammoTimer>=15){
                return true;
            }
        } else{
            if (amount>0){
                spawnMeteors();
            }
        }
        return false;
    }
    
    public void spawnMeteors(){
        this.x-=this.size*METEOR_DEFAULT_SIZE/2;
        setRandomDirection();
        this.obs.getMeteors().add(new Meteor((int)(this.x+this.size*METEOR_DEFAULT_SIZE)+10,this.speed,this.size,getRandomDirection(),this.obs));
        this.obs.getMeteors().get(this.obs.getMeteors().size()-1).setY(this.y);
        this.obs.getMeteors().get(this.obs.getMeteors().size()-1).setRandomDirection();
        //this.obs.getMeteors().add(this);
    }
    public double getRandomDirection(){
        return Math.atan2(this.y-(APPLET_HEIGHT-GROUND_HEIGHT),this.x-Math.random()*(APPLET_WIDTH-50)+25);
    }
    public void setRandomDirection(){
        this.direction=getRandomDirection();
    }
    
    public void setX(double x){
        this.x=x;
    }
    public void setY(double y){
        this.y=y;
    }
    public int getX() {
        return (int)Math.round(this.x);
    }
    public int getY() {
        return (int)Math.round(this.y);
    }

    

}
