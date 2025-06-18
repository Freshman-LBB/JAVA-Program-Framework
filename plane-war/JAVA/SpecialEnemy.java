import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
public class SpecialEnemy extends Enemy {
    private int speed=1;
    private int acc=1;
    // 默认构造函数
    public SpecialEnemy() {
        super();
    }

    // 拷贝构造函数
    protected SpecialEnemy(SpecialEnemy T) {
        super(T);
    }
    @Override
    protected void activate(int x,int y)
    {
        state = true;
        img=0;
        setPositionX(0);
        setPositionY(0);
    }
    @Override
    protected int takedamage(int strength)
    {
        int a=getBlood();
        setBlood(a-=strength);
        if(img==0&&getBlood()<=0)
        {
            img=1;
            return -getBlood();
        }
        return strength;
    }
    // 重写父类的init方法
    @Override
    protected void init() {
        // 设置普通敌人的默认属性
        setWide(57);
        setHeight(43);
        setPositionX((int)(Math.random() * (960 - 57)));
        setPositionY((int)(Math.random() * (0 - (-852))));
        setKindCode(1);
        setStrength(1);
        setBlood(2);
        img = 0;
        imagePath = new String[5];
        imagePath[0]="resources\\ENEMY\\enemy1.png";
        imagePath[1]="resources\\ENEMY\\enemy1_down1.png";
        imagePath[2]="resources\\ENEMY\\enemy1_down2.png";
        imagePath[3]="resources\\ENEMY\\enemy1_down3.png";
        imagePath[4]="resources\\ENEMY\\enemy1_down4.png";
        loadImages();
    }
    private void loadImages() {
        this.originalImage = new BufferedImage[5];
        try {
            for (int i = 0; i < 5; i++) {
                File imageFile = new File(imagePath[i]);
                if (imageFile.exists()) {
                    originalImage[i] = ImageIO.read(imageFile);
                } else {
                    System.err.println("Image file not found: " + imagePath[i]);
                    // 可以设置一个默认图像或者处理错误
                    originalImage[i] = null;
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading images: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @Override
    protected void forward(int x,int y) 
    {
        int currentY = getPositionY();
        int currentX = getPositionX();
        setPositionX(currentX+speed);
        speed+=acc;
        if (getPositionX()<-getWide()/2) {
          setPositionX(-getWide()/2);
          speed=0;
          acc=1;
        }
        if (getPositionX()>960-getWide()/2) {
          setPositionX(960-getWide()/2);
          speed=0;
          acc=-1;
        }
        setPositionY(currentY + 3);

        if (getPositionY()>960) 
        {
            deactivate();
            setBlood(10);
        }
    }
    @Override
    protected Node copy() {
        return new SpecialEnemy(this);
    }
    @Override
    protected void print(Graphics2D g2d) {
        // 只在激活状态下绘制
        if (checkAlive()) {
            g2d.setColor(Color.red); // 设置文本颜色
            g2d.drawString("Blood: " + getBlood(), getPositionX(), getPositionY()-2); 
            switch (img) {
          case 0:
            try {
                // 尝试使用图片
                g2d.drawImage(
                originalImage[0], 
                getPositionX(), 
                getPositionY(), 
                getWide(), 
                getHeight(), 
                null
            );
            } catch (Exception e) {
                // 图片加载失败时，绘制一个红色矩形
                g2d.setColor(Color.RED);
                g2d.fillRect(
                getPositionX(), 
                getPositionY(), 
                getWide(), 
                getHeight()
                );
                g2d.dispose();
            }
            break;
            case 1:
            try {
                // 尝试使用图片
                g2d.drawImage(
                originalImage[1], 
                getPositionX(), 
                getPositionY(), 
                getWide(), 
                getHeight(), 
                null
            );
                img=2;
            } catch (Exception e) {
                // 图片加载失败时，绘制一个红色矩形
                g2d.setColor(Color.RED);
                g2d.fillRect(
                getPositionX(), 
                getPositionY(), 
                getWide(), 
                getHeight()
                );
                g2d.dispose();
            }
            break;
            case 2:
            try {
                // 尝试使用图片
                g2d.drawImage(
                originalImage[2], 
                getPositionX(), 
                getPositionY(), 
                getWide(), 
                getHeight(),  
                null
            );
                img=3;
            } catch (Exception e) {
                // 图片加载失败时，绘制一个红色矩形
                g2d.setColor(Color.RED);
                g2d.fillRect(
                getPositionX(), 
                getPositionY(), 
                getWide(), 
                getHeight()
                );
                g2d.dispose();
            }
            break;
            case 3:
            try {
                // 尝试使用图片
                g2d.drawImage(
                originalImage[3], 
                getPositionX(), 
                getPositionY(), 
                getWide(), 
                getHeight(),  
                null
            );
                img=4;
            } catch (Exception e) {
                // 图片加载失败时，绘制一个红色矩形
                g2d.setColor(Color.RED);
                g2d.fillRect(
                getPositionX(), 
                getPositionY(), 
                getWide(), 
                getHeight()
                );
                g2d.dispose();
            }
            break;
            case 4:
            try {
                // 尝试使用图片
                g2d.drawImage(
                originalImage[4], 
                getPositionX(), 
                getPositionY(), 
                getWide(), 
                getHeight() ,
                null
            );
            deactivate();
            } catch (Exception e) {
                // 图片加载失败时，绘制一个红色矩形
                g2d.setColor(Color.RED);
                g2d.fillRect(
                getPositionX(), 
                getPositionY(), 
                getWide(), 
                getHeight()
                );
                g2d.dispose();
            }
            break;
          default:
            break;
        }
        }
    }
}