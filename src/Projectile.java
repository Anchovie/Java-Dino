import java.awt.Graphics;
import java.awt.Rectangle;



public interface Projectile {

    
    boolean move();
    
    void draw(Graphics g);
    
    int getX();
    int getY();
    Rectangle getHitbox();
}
