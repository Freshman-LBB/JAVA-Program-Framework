import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 
public class EnemyWeapon extends Enemy {
    private Clip soundClip;
    private int moveTimes=0;
    private CircularDoubleLinkedListWithFreeNode weaponList; 
    private void loadSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("resources\\SOUND\\enemy2_down.wav"));
            soundClip = AudioSystem.getClip();
            soundClip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading sound: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // 播放音频
    private void playSound() {
        if (soundClip != null) {
            soundClip.setFramePosition(0); // Rewind to the beginning
            soundClip.start();
        }
    }
    // 默认构造函数
    public EnemyWeapon() {
        super();
    }

    // 拷贝构造函数
    protected EnemyWeapon(EnemyWeapon T) {
        super(T);
        this.soundClip=T.soundClip;
        this.moveTimes=T.moveTimes;
        this.weaponList=T.weaponList;
    }
    @Override
    protected void activate(int x,int y)
    {
        state = true;
        img=0;
        setPositionX((int)(Math.random() * (960 - 57)));
        setPositionY((int)(Math.random() * (-852)));
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
        setWide(69);
        setHeight(99);
        setPositionX((int)(Math.random() * (960 - 69)));
        setPositionY((int)(Math.random() * (0 - (-852))));
        setKindCode(2);
        setBlood(15);
        img = 0;
        imagePath = new String[6];
        imagePath[0]="resources\\ENEMY\\enemy2.png";
        imagePath[1]="resources\\ENEMY\\enemy2_hit.png";
        imagePath[2]="resources\\ENEMY\\enemy2_down1.png";
        imagePath[3]="resources\\ENEMY\\enemy2_down2.png";
        imagePath[4]="resources\\ENEMY\\enemy2_down3.png";
        imagePath[5]="resources\\ENEMY\\enemy2_down4.png";
        loadImages();
        loadSound(); // 加载音频
        EnemyBullet a = new EnemyBullet();
        setStrength(a.getStrength());
        weaponList = new CircularDoubleLinkedListWithFreeNode(10, a);
    }
    private void loadImages() {
        this.originalImage = new BufferedImage[6];
        try {
            for (int i = 0; i < 6; i++) {
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
    protected CircularDoubleLinkedListWithFreeNode getNowWeapon()
    {
      return weaponList;
    }

    @Override
    protected void forward(int x,int y) 
    {
        int currentY = getPositionY();
        setPositionY(currentY + 1);
        if (getPositionY()>960) 
        {
            deactivate();
            setBlood(15);
        }
        moveTimes++;
        if (moveTimes%100==20&&state==true&&currentY>0) {
          weaponList.activated(getPositionX()+getWide()/2-2, currentY+getHeight());
        }
        weaponList.forward();
        moveTimes%=400;
    }
    @Override
    protected Node copy() {
        return new EnemyWeapon(this);
    }
    @Override
    protected void print(Graphics2D g2d) {
        g2d.setColor(Color.red); // 设置文本颜色
        g2d.drawString("Blood: " + getBlood(), getPositionX()-8, getPositionY()-2); 
        // 只在激活状态下绘制
        weaponList.print(g2d);
        if (checkAlive()) {
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
                playSound(); // 播放音效
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
            img=5;
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
            case 5:
            try {
                // 尝试使用图片
                g2d.drawImage(
                originalImage[5], 
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
