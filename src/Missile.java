import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Missile implements Projectile, Info {
    
    double x,y;
    double speed;
    double direction;
    Dinosaur owner;
    int age=0;
    int finalAge=98;
    
    public Missile(double direction, double x, double speed, Dinosaur owner){
        this.direction=direction;
        this.speed=speed;
        this.x=x;
        this.y=BORDER_DOWN-DINOSAUR_HEIGHT;
        this.owner=owner;
    }
    
    //for testShot
    public Missile(double direction, double x, double y, double speed){
        System.out.println("MISSILE CREATED");
        this.direction=direction;
        this.speed=speed;
        this.x=x;
        this.y=y;
    }
    
    public boolean move(){
        //System.out.println("---------------------");
        //System.out.println("MISSILE MOVED!");
        //System.out.println("X="+this.x+" Y="+this.y+" SPEED:"+this.speed+ "AGE:"+this.age);
        this.x+=this.speed*Math.cos(this.direction);
        this.y+=this.speed*Math.sin(this.direction);
        this.age++;
        //System.out.println("X="+this.x+" Y="+this.y+" SPEED:"+this.speed+ "AGE:"+this.age);
        if (this.age>=this.finalAge) return false;
        if (this.y>=APPLET_HEIGHT-GROUND_HEIGHT) return false;
        
        return true;
    }
    
    public void draw(Graphics g){
        g.setColor(Color.red);
        if (this.age>=50) g.setColor(Color.red.darker());
        g.fillOval(getX(), getY(), BULLET1_SIZE, BULLET1_SIZE);   //this causes small error in aiming (not anymore??)
        
        if (DEBUG){
          //DecimalFormat df = new DecimalFormat("##.##");
            g.setColor(Color.white);
            g.drawRect(getX(),getY(),BULLET1_SIZE,BULLET1_SIZE);
            //g.setColor(Color.black);
            //if (this.age>=70) g.setColor(Color.red.darker());
            //g.drawString("d:"+(df.format(this.direction)),(int) this.x-15,(int) this.y-5);
        }
        
        
    }

    public int getX() {
        return (int)Math.round(this.x)-BULLET1_SIZE/2;
    }

    public int getY() {
        return (int)Math.round(this.y)-BULLET1_SIZE/2;
    }
    public Rectangle getHitbox(){
        return new Rectangle(getX(),getY(),BULLET1_SIZE,BULLET1_SIZE);
    }

}
