import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;


public class Dinosaur implements Info{

    protected int x;
    protected int y;
    //private Image pic;
    protected int currentCool;
    protected boolean dead;
    protected boolean firstUpgrade;
    protected boolean secondUpgrade;
    Image pic;
    Dino obs;
    
    protected ArrayList<Projectile> projectiles;
    
    public Dinosaur(int x, Image pic, Dino obs){
        this.x=x;
        this.y=BORDER_DOWN-DINOSAUR_HEIGHT;
        this.projectiles=new ArrayList<Projectile>();
        this.currentCool=0;
        this.pic=pic;
        this.obs=obs;
        this.dead=false;
        
    }
    
    public void shoot(double atan2) {
        System.out.println("SHOULD BE OVERWRITTEN!");
        
    }
    
    public void kill(){
        this.dead=true;
    }
    
    public void updateAmmo(){
        for (int i=0; i<this.projectiles.size();i++){
            if (this.projectiles.get(i)!=null){
                if (this.projectiles.get(i).move()==false){
                    this.projectiles.remove(i);
                }
            }
        }
        this.currentCool++;
    }
    public void drawAmmo(Graphics g){
        for (int i=0; i<this.projectiles.size(); i++){
            if (this.projectiles.get(i)!=null){
                this.projectiles.get(i).draw(g);
            }
        }
    }
    
    public void clearAmmo(){
        this.projectiles=new ArrayList<Projectile>();
    }

    public void draw(Graphics g) {
        System.out.println("ALWAYS OVERWRITTEN!");
    }
    
    public void drawPlacement(Graphics g, int x){
        System.out.println("Always overwritten");//maybe could be implemented here as well as draw??
    }
    
    

    public Projectile getProjectile(int i){
        if (this.projectiles.size()>i && this.projectiles.get(i)!=null){
            return this.projectiles.get(i);
        }
        return null;
    }
    public int getMissileAmount(){
        return this.projectiles.size();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
    

    public ArrayList<Projectile> getProjectiles() {
        return this.projectiles;
    }

    public int getDamage() {
        System.out.println("NOT SUPPOSED TO BE HERE!");
        return 9999;
    }
    
    public Rectangle getHitBox(){
        return new Rectangle(this.x, this.y, Info.DINOSAUR_WIDTH, Info.DINOSAUR_HEIGHT);
    }
    
    public boolean isDead(){
        return this.dead;
    }

    
}
