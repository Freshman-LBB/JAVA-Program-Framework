import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
public abstract class Tool extends Node{
    private int wide;
    private int height;
    private int positionX;
    private int positionY;
    private int strength;
    protected String[] imagePath;
    protected int img;
    protected BufferedImage[] originalImage ;
    public Tool() {
        super();
        init();
    }

    // 拷贝构造函数
    protected Tool(Tool T) {
        super(T);  // 调用父类的拷贝构造函数
        this.wide = T.wide;
        this.height = T.height;
        this.positionX = T.positionX;
        this.positionY = T.positionY;
        this.strength = T.strength;
        this.imagePath = T.imagePath;
        this.img=T.img;
        this.originalImage=T.originalImage;
    }
    // 抽象初始化方法
    protected abstract void init();

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

    public void setStrength(int strength) {
        this.strength = strength;
    }
    @Override
    protected int getStrength()
    {
      return strength;
    }
    @Override
    protected int [] getNowPos()
    {
      int a[]=new int[4];
      a[0]=positionX;
      a[1]=positionY;
      a[2]=wide;
      a[3]=height;
      return a;
    }
    protected void setImagePath(String path) {}
}
