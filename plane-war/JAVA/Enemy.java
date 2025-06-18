import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
public abstract class Enemy extends Node {
    private int wide;
    private int height;
    private int positionX;
    private int positionY;
    private int kindCode;
    private int strength;
    private int blood;
    protected String[] imagePath;
    protected int img;
    protected BufferedImage[] originalImage ;
    // 默认构造函数
    public Enemy() {
        super();
        init();
    }
    // 拷贝构造函数
    protected Enemy(Enemy T) {
        super(T);  // 调用父类的拷贝构造函数
        this.wide = T.wide;
        this.height = T.height;
        this.positionX = T.positionX;
        this.positionY = T.positionY;
        this.kindCode = T.kindCode;
        this.strength = T.strength;
        this.blood = T.blood;
        this.imagePath = T.imagePath;
        this.img=T.img;
        this.originalImage=T.originalImage;
    }
    // 抽象初始化方法
    protected int takedamage(int strength){return 0;};
    protected abstract void init();
    @Override
    protected int checkCollision(int x,int y,int width,int height,int strength)
    {
        int a=0;
        if (x<(positionX+wide)&&(x+width)>positionX&&y<(positionY+this.height)&&(y+height)>positionY) {
            a=takedamage(strength);
        }
        return a;
    };
    @Override
    protected void forward(int x,int y) {}
    @Override
    protected void print(Graphics2D g2d) {}
     // Getters and Setters
    public int getWide() {
        return wide;
    }
    public void setWide(int wide) {
        this.wide = wide;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getPositionX() {
        return positionX;
    }
    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }
    public int getPositionY() {
        return positionY;
    }
    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
    public int getKindCode() {
        return kindCode;
    }
    public void setKindCode(int kindCode) {
        this.kindCode = kindCode;
    }
    public int getStrength() {
        return strength;
    }
    public void setStrength(int strength) {
        this.strength = strength;
    }
    public int getBlood() {
        return blood;
    }
    public void setBlood(int blood) {
        this.blood = blood;
    }
    protected void setImagePath(String path) {}
}