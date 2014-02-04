import java.applet.*;
import java.awt.*;
import java.util.ArrayList;
//import java.awt.image.ColorModel;
//import java.net.*;
import java.awt.event.*;
//import java.io.*;


/*      TODO
 * - FPS control DONE? not properly
 * - Meteor blastradius destroying dinos Done?? not done, implement the radius to kill dinos, not the impact of meteor. Check hitboxes.
 * - Meteor rotation/graphic enchancements (better bombs!)
 * - Levels   infra done
 * - Different meteor sizes and breakup   halfway, implement meteors breaking into several smaller pieces and cracking animation to work properly
 * - Dino placement (with levels)
 * - More dinos (Missile, Laser, Autoaim, timestop)
 * - Dino accessories/upgrades
 * - (changing scenery/BG, upgrades between levels, )
 */

public class Dino extends Applet implements Runnable, Info,  MouseListener, MouseMotionListener,KeyListener {
    private Image dbImage;
    private Graphics dbg;
    private int mouseX,mouseY;
    private int mouseMovedX, mouseMovedY;
    
    protected int kills=0;
    protected int money=0;
    
    private ArrayList<Dinosaur> dinosaurs;
    private ArrayList<Meteor> meteors;
    private ArrayList<Loot> loots;
    
    private LevelManager levelManager;
    
    Image background;
    Image meteor;
    Image loot;
    Image cross;
    Image explosion;
    Image dino1;
    Image dino2;
    Image dino3;
    
    private Cursor crosshair;
    
    private boolean hitBoxes=false;
    private boolean gameOver;
    private boolean inGame;
    private boolean placingDinos;
    
    private Thread t = null;
    
    private final int TARGETFPS = 50;
    private int FPS;
    private int counter;
    private long startingTime;
    private long startingTime2;
    private long currentDifference;
    
    
    public void start() {
        this.t=new Thread(this);
        this.t.start();
    }
    public void stop() {
        this.t.interrupt();
    }
    public void destroy() {
    }
    
    public void init() {
        setSize(APPLET_WIDTH, APPLET_HEIGHT);
        setName("Dino");
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        
        this.background=getImage(getCodeBase(),"img/BG.png");
        this.meteor=getImage(getCodeBase(),"img/meteorite.png");
        this.loot=getImage(getCodeBase(),"img/diamond.png");
        this.cross=getImage(getCodeBase(),"img/cursor6.png");
        this.explosion=getImage(getCodeBase(),"img/explosion.png");
        this.dino1=getImage(getCodeBase(),"img/dino1.png");
        this.dino2=getImage(getCodeBase(),"img/dino2.png");
        this.dino3=getImage(getCodeBase(),"img/dino3.png");
        this.crosshair = Toolkit.getDefaultToolkit().createCustomCursor(this.cross, new Point(15,15), "crosshair");
        setCursor(this.crosshair);
        
        newGame();
        this.setVisible(true);
    }

    /////////////////////////////////////////\-> M A I N   B O D Y <-/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void run() {
        int sleepTime=0;
        int periodGoal=1000/this.TARGETFPS;
        this.startingTime2 = this.startingTime = System.currentTimeMillis();
        
        while (true) {
            updateTimer();
         
            gameUpdate();
            
            sleepTime = (int) (periodGoal - (System.currentTimeMillis() - this.startingTime));
            if (sleepTime<=0){
                sleepTime=10;
            }
            try{ 
                repaint();
                Thread.sleep(sleepTime);
//                System.out.println("JUST SLEPT FOR " + sleepTime);
//                System.out.println("periodGoal=" + periodGoal + " difference in time="+(System.currentTimeMillis() - this.startingTime));
            }catch (Exception e) {
                System.out.println("COULD NOT SLEEP WTF " + e);
            }
//            System.out.println("TIME IN WHOLE RUN-LOOP = " + (System.currentTimeMillis()-this.startingTime));
//            System.out.println("----------------");
        }
    }
    
    public void updateTimer(){
        long time=System.currentTimeMillis();
        this.currentDifference=System.currentTimeMillis()-this.startingTime2;
        this.counter++;
        //System.out.println("counter = " + this.counter + " currentDifference is now " + this.currentDifference);
        if (this.currentDifference>=500){
            this.FPS=this.counter*2;
            this.counter=0;
            this.currentDifference=0;
            this.startingTime2 = System.currentTimeMillis();
        }
        this.startingTime=System.currentTimeMillis();
        //System.out.println("TIME IN TIMERUPDATE = " + (System.currentTimeMillis()-time));
    }

    public void gameUpdate(){
        long time = System.currentTimeMillis();
        if (!this.levelManager.isLevelFinished() && this.inGame){
            updateDinos();
            updateMissiles();
            updateMeteors();
            updateLoot();
            this.levelManager.checkFinish();
            
        } else {
            //Graphic shit
            
            if (!this.levelManager.isBetweenLevels()){
                this.loots=new ArrayList<Loot>();
                for (int i=0; i<this.dinosaurs.size();i++){
                    if (this.dinosaurs.get(i)!=null){
                        this.dinosaurs.get(i).clearAmmo();
                    }
                }
                this.levelManager.nextLevel();
                
            }
        }
        
        //System.out.println("TIME UPDATING = " + (System.currentTimeMillis()-time));
    }
    
    public void updateDinos(){      //makes dinos shoot(gives direction) and removes them after death
        if (this.mouseX!=0 && this.mouseY!=0){
            for (int i=0; i<this.dinosaurs.size();i++){
                if (this.dinosaurs.get(i)!=null){   //somethings wrong, notice the magic numbers (10) used to fix the alignment of ammo
                    this.dinosaurs.get(i).shoot(Math.atan2(this.mouseY-this.dinosaurs.get(i).getY()/*+10/*!!*/, this.mouseX-this.dinosaurs.get(i).getX()-DINOSAUR_WIDTH/2/*+10/*!!*/));
                }
                if (this.dinosaurs.get(i).isDead()) {
                    this.dinosaurs.remove(i);
                }
            }
        }
    }
        //VOIS PARANTAA? hyiv
    public void updateMissiles(){       //updates and moves dino ammunition, checks if they hit any meteors and removes them or deals damage accordingly
        //System.out.println("missileUpdate");
        for (int i=0; i<this.dinosaurs.size();i++){
            if (this.dinosaurs.get(i)!=null){
                this.dinosaurs.get(i).updateAmmo();
                for(int j=0; j<this.dinosaurs.get(i).getMissileAmount();j++){
                    if (this.dinosaurs.get(i).getProjectile(j)!=null){
                        for (int k=0; k<this.meteors.size(); k++){
                            if (this.meteors.get(k)!=null){
                                if (this.dinosaurs.get(i).getProjectile(j)!=null){
                                    if (this.meteors.get(k).checkCollision(this.dinosaurs.get(i).getProjectile(j).getHitbox())){
                                        if (this.dinosaurs.get(i).getProjectiles()!=null){
                                            this.dinosaurs.get(i).getProjectiles().remove(j);
                                            if (this.meteors.get(k).takeDamage(this.dinosaurs.get(i).getDamage())){
                                                this.meteors.remove(k);
                                                this.kills++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void updateMeteors(){        //moves, creates and removes meteors if necessary, also checks collision with dinos
        for (int i=0; i<this.meteors.size();i++){
            if (this.meteors.get(i)!=null){
                if (this.meteors.get(i).checkDinoCollision()){
                    this.meteors.remove(i); //HITS DINOSAUR, IMPLEMENT ANIMATION!
                    checkGameOver();
                    break;
                }
                if (this.meteors.get(i)!=null){         //recheck necessary
                    if (!this.meteors.get(i).move()){
                        this.meteors.remove(i);
                    }
                }
            }
        }
        createMeteors();
    }
    
    public void updateLoot(){
        for (int i=0;i<this.loots.size();i++){
            if (this.loots.get(i)!=null){
                this.loots.get(i).update();
                this.loots.get(i).checkCursor(new Point(this.mouseMovedX, this.mouseMovedY));
                if (this.loots.get(i).isDead()){
                    this.loots.remove(i);
                }
            }
        }
    }
    
    public void checkGameOver(){
        boolean dinosDead=true;
        if (this.dinosaurs.size()==0){
            //System.out.println("Last dino was hit! arraylength is 0, initiating game over");
            gameOver();
        }else{
            for (int j=0; j<this.dinosaurs.size(); j++){
                if (this.dinosaurs.get(j)!=null){
                    if (!this.dinosaurs.get(j).isDead()){
                        dinosDead=false;
                        //System.out.println("DINOSDEAD FALSE INDEX#"+j+" IS ALIVE");
                    }
                }
            }
            if (dinosDead){
                //System.out.println("Dinosaurs checked, all dead. initiated game over");
                gameOver();
            }
        }
    }
    
    public void createMeteors(){
        LevelManager lm = this.levelManager;  // just to clarify and shorten the long lines
        if (lm.getMeteorsLeft(lm.getLevel())>0){    //if there's still more meteors to a level
            if (((int)Math.round(Math.random()*99)+1)<=lm.getMeteorSpawnRate(lm.getLevel())){  //create meteors according to spawn rate
                this.meteors.add(new Meteor(    (int)(Math.random()*(APPLET_WIDTH-30))+15,      //new meteor with random x-coordinate
                                                lm.getMeteorSpeed(lm.getLevel())+Math.random()*0.7, //the speed is gotten from levelManager, with random 0..1
                                                1,1,this));          //size should also vary according to levels, direction?
                this.levelManager.meteorAdded();
            }
        }
        
        if (lm.getBigMeteorsLeft(lm.getLevel())>0){        //the spawning of bigger meteorites
            if ((int)(Math.round(Math.random()*999)+1)<=lm.getMeteorSpawnRate(lm.getLevel())*1){    //create meteors according to spawn rate
                this.meteors.add(new Meteor(    (int)(Math.random()*(APPLET_WIDTH-30))+15,          //new meteor with random x-coordinate
                                                lm.getMeteorSpeed(lm.getLevel())+Math.random()*0.6, //the speed is gotten from levelManager, with random 0..1
                                                (int) (2+Math.random()*(lm.getLevel()/2)),1,        //size should also vary according to levels, direction?
                                                this));                  //images could prolly be gotten through observer
            
            this.levelManager.bigMeteorAdded();
            System.out.println("Big meteor created. left:"+ lm.getBigMeteorsLeft(lm.getLevel()));
            }
            
        }
    }
    
    public void createLoot(Meteor meteor){
        this.loots.add(new Loot(meteor.getX(),meteor.getY(),this));
    }
    
    public void gameOver(){
        System.out.println("GAME OVER");
        //this.levelManager.setLevelFinished();
        this.gameOver=true;
        this.inGame=false;
        
    }
    
    public void newGame(){
        this.meteors=new ArrayList<Meteor>();
        this.dinosaurs=new ArrayList<Dinosaur>();
        this.loots = new ArrayList<Loot>();
        this.levelManager = new LevelManager(this);
        this.kills=0;
        this.money=0;
        this.gameOver=false;
        
        
        //starting shit
        
        this.inGame=true;
        System.out.println("Stats inited, new Game starting!");
    }
    
    
    public void placeDinosaur(int type, int x){
        switch (type){
        case 1: //Gunner
            this.dinosaurs.add(new Gunner(x,this.dino1,this));
            break;
        case 2: //Bomber
            this.dinosaurs.add(new Bomber(x,this.dino2,this));
            break;
        case 3: //Exploder
            this.dinosaurs.add(new Exploder(x,this.dino3,this));
            break;    
        default:
            break;
        }
    }
    
    public void dinoPlacement(){
        
    }
    
    public ArrayList<Dinosaur> getDinosaurs(){
        return this.dinosaurs;
    }
    public ArrayList<Meteor> getMeteors(){
        return this.meteors;
    }
    
    public boolean hitBoxes(){
        return this.hitBoxes;
    }
    
    
    ///////////////////////////////////////\-> P A I N T <-/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void paint( Graphics g ) {
        long time = System.currentTimeMillis();
        g.drawImage(this.background, 0, 0, this);
        //paintCursorTest(g);
        
        
        if (this.placingDinos){
            paintDinos(g,true);
        }else{
            paintDinos(g,false);
        }
        if (this.inGame){
            paintMissiles(g);
            paintMeteors(g);
            paintLoot(g);
        }
        if (this.levelManager.isBetweenLevels()){
            paintBetweenLevels(g);
        }
        if (this.gameOver){
            paintGameOver(g);
        }
        
        g.setColor(Color.red);
        g.setFont(new Font("Forte",Font.PLAIN,30));
        g.drawString(""+this.kills, APPLET_WIDTH/2-10, APPLET_HEIGHT-10);
        g.setColor(Color.green.darker());
        g.drawString(""+this.money, APPLET_WIDTH/2-10, APPLET_HEIGHT-35);
        g.setFont(new Font("Arial",Font.PLAIN,14));
        g.drawString("FPS="+this.FPS,20,20);
        g.drawString("Meteors="+this.levelManager.getTotalMeteorsLeft(this.levelManager.getLevel()),20,30);
        g.drawString("Level="+ (this.levelManager.getLevel()+1), 20, 40);
        
        //System.out.println("TIME PAINTING = " + (System.currentTimeMillis()-time));
    }
    
    public void paintDinos(Graphics g, boolean placingDinos){
        for (int i=0;i<this.dinosaurs.size();i++){
            if (this.dinosaurs.get(i)!=null){
                this.dinosaurs.get(i).draw(g);
            }
        }
        if (placingDinos){
            this.dinosaurs.get(this.dinosaurs.size()-1).drawPlacement(g, this.mouseMovedX);
        }
    }
    public void paintMissiles(Graphics g){
        for (int i=0;i<this.dinosaurs.size();i++){
            if (this.dinosaurs.get(i)!=null){
                this.dinosaurs.get(i).drawAmmo(g);
            }
        }
    }
    public void paintMeteors(Graphics g){
        for (int i=0;i<this.meteors.size();i++){
            if (this.meteors.get(i)!=null){
                this.meteors.get(i).draw(g);
            }
        }
    }
    public void paintLoot(Graphics g){
        for (int i=0;i<this.loots.size();i++){
            if (this.loots.get(i)!=null){
                this.loots.get(i).draw(g);
            }
        }
    }
    
    public void paintUpgrades(Graphics g){
        /*
        System.out.println("DRAWING UPGRADES");
        g.setColor(Color.white);
        g.fillRoundRect(100,100,300,200,25,25);
        g.setColor(Color.blue);
        g.setFont(new Font("Forte",Font.PLAIN,30));
        g.drawString("WAVE CLEARED!", 200, 120);
        for (int i=0; i<10000000;i++){
            int x=(98252+i)^5*2221-5;
        }
        */
        
    }
    
    public void paintBetweenLevels(Graphics g){
        g.setColor(Color.blue.darker());
        //g.fillRoundRect(200, 200, 400, 300, 25, 25);
        g.setColor(Color.white);
        g.setFont(new Font("Forte",Font.PLAIN,30));
        g.drawString("WAVE CLEARED",300,130);
        g.setFont(new Font("Arial",Font.PLAIN,14));
        g.drawString("Click to continue",355,150);
    }
    
    public void paintGameOver(Graphics g){
        g.setColor(Color.blue.darker());
        g.fillRoundRect(200, 100, 400, 200, 25, 25);
        g.setColor(Color.white);
        g.setFont(new Font("Forte",Font.PLAIN,30));
        g.drawString("GAME OVER",320,130);
        
    }
    
    public void paintCursorTest(Graphics g){
        g.setColor(Color.black);
        g.fillRect(100, 100, 100, 100);
        g.setColor(Color.yellow);
        g.fillRect(125, 125, 50, 50);
        g.setColor(Color.red);
        g.fillRect(145,145,10,10);
        g.setColor(Color.white);
        g.drawRect(149,149,2,2);
        
    }

    public void update(Graphics g){     //handles double buffering
        if (this.dbImage == null){
            this.dbImage=createImage(this.getSize().width,this.getSize().height);
            this.dbg=this.dbImage.getGraphics();
        }
        this.dbg.setColor(getBackground());
        this.dbg.fillRect(0,0,this.getSize().width,this.getSize().height);

        this.dbg.setColor(getForeground());
        paint(this.dbg);

        g.drawImage(this.dbImage,0,0,this);
    }
    
    
    ////////////////////////////////////////\-> E V E N T S <-/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void mouseClicked(MouseEvent e) {
        
    }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
        if (this.gameOver){
            //if (new Rectangle(100,100,300,200).contains(e.getPoint())){
                newGame();
            //}
        }
        if (this.levelManager.isBetweenLevels()){
            this.levelManager.setBetweenLevels();
        }
        if (this.inGame){
            this.mouseX=e.getX();
            this.mouseY=e.getY();
            
        }
        //System.out.println("X="+this.mouseX + ", Y="+this.mouseY);
    }
    public void mouseReleased(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {
        this.mouseX=e.getX();
        this.mouseY=e.getY();
    }
    public void mouseMoved(MouseEvent e) {
        this.mouseMovedX=e.getX();
        this.mouseMovedY=e.getY();
        
    }
    
    public void keyPressed(KeyEvent e){
        if (e.getKeyCode()==37){ } //left
        if (e.getKeyCode()==39){ } //right
        if (e.getKeyCode()==40){ }//down
        if (e.getKeyCode()==38){ }//up
        if (e.getKeyCode()==10){ }//enter
        if (e.getKeyChar()=='x'){ }
        
        if (e.getKeyChar()==27){//escape
            stop();
            System.exit(0);
        }
        if (e.getKeyChar()=='1'){
            if(this.inGame){
                placeDinosaur(1,this.mouseX-DINOSAUR_WIDTH/2);
            }
        }
        if (e.getKeyChar()=='2'){
            if(this.inGame){
                placeDinosaur(2,this.mouseX-DINOSAUR_WIDTH/2);
            }
        }
        if (e.getKeyChar()=='3'){
            if(this.inGame){
                placeDinosaur(3,this.mouseX-DINOSAUR_WIDTH/2);
            }
        }
        if (e.getKeyChar()=='h'){
            this.hitBoxes = this.hitBoxes ? false : true;
        }
    }
    public void keyReleased(KeyEvent e){ }
    public void keyTyped(KeyEvent e){}
}

