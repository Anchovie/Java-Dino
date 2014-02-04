
public class LevelManager {
    
    private int level;
    private int[] meteors;
    private int[] bigMeteors;
    private double[] meteorSpeed;
    private int[] meteorSpawnRate;
    private boolean levelFinished;
    private boolean betweenLevels;
    
    private int meteorsLeft;
    private int bigMeteorsLeft;
    
    private Dino obs;
    
    public LevelManager(Dino obs){
        this.level=-1;
        this.meteors = new int[]{18,22,27,39,51,80,111,145,185,230};
        this.bigMeteors = new int[]{0,2,3,3,4,7,8,11,13,15};
        this.meteorSpeed = new double[]{0.6, 0.6, 0.7, 0.7, 0.8, 0.9, 1, 1.1, 1.1, 1.2};
        this.meteorSpawnRate = new int[]{3,4,4,5,6,7,8,9,9,10};
        this.levelFinished=true;
        this.betweenLevels=false;
        this.nextLevel();
        this.obs=obs; //observer
        
        
    }
    
    public void finishLevel(){
        this.levelFinished=true;
        this.betweenLevels=true;
        System.out.println("LEVEL FINISHED, next lv="+(this.level+1));
    }
    public int nextLevel(){
        if (this.levelFinished) {
            this.level++;
            this.levelFinished=false;
            this.betweenLevels=false;
            if (this.level>=9){
                this.meteorsLeft=this.meteors[9]+this.level*2;
                this.bigMeteorsLeft=this.bigMeteors[9]+this.level/2;
            } else {
                this.meteorsLeft=this.meteors[this.level];
                this.bigMeteorsLeft=this.bigMeteors[this.level];
            }
        }
        return this.level;
    }
    
    public void meteorAdded(){
        this.meteorsLeft--;
    }
    public void bigMeteorAdded(){
        this.bigMeteorsLeft--;
    }
    
    public boolean checkFinish(){
        if (this.meteorsLeft<=0 && this.bigMeteorsLeft<=0){
            if (this.obs.getMeteors().size()==0){
                finishLevel();
            }
        }
        return this.levelFinished;
    }
    
    public void setBetweenLevels(){
        this.betweenLevels=false;
    }
    public void setLevelFinished(){
        this.levelFinished=false;
    }
    
    public boolean isLevelFinished(){
        return this.levelFinished;
    }
    public boolean isBetweenLevels(){
        return this.betweenLevels;
    }
    
    public int getLevel(){
        return this.level;
    }
    public int getTotalMeteorsLeft(int level){
        return this.meteorsLeft + this.bigMeteorsLeft;
    }
    public int getMeteorsLeft(int level){
        return this.meteorsLeft;
    }
    public int getBigMeteorsLeft(int level){
        return this.bigMeteorsLeft;
    }
    public int getMeteorNo(int level){
        return this.meteors[level];
    }
    public int getBigMeteorNo(int level){
        return this.bigMeteors[level];
    }
    public double getMeteorSpeed(int level){
        return this.meteorSpeed[level];
    }
    public int getMeteorSpawnRate(int level){
        return this.meteorSpawnRate[level];
    }
    

}
