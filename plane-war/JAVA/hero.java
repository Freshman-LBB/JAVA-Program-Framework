import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 

public class hero {
    // 私有成员变量
    private int blood;
    private int speed;
    private int x;
    private int y;
    private int width;
    private int height;
    private int state;
    private int strength;
    private String[] imagePath;
    private BufferedImage[] originalImage ;
    private CircularDoubleLinkedListWithFreeNode heroWeapon;
    private String soundPath = "resources\\SOUND\\use_bomb.wav"; // 音频文件路径
    private Clip soundClip; // Clip 对象
    private void loadSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundPath));
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
    // 无参构造方法
    public hero() {
        this.blood = 20;
        this.speed = 10;
        this.x = 480-102/2;
        this.y = 700;
        this.width = 102;
        this.height = 126;
        this.state = 0;
        
        HeroBullet a = new HeroBullet();
        strength = a.getStrength();
        heroWeapon = new CircularDoubleLinkedListWithFreeNode(10, a);

        // 初始化imagePath数组，包含6个元素
        this.imagePath = new String[6];
        imagePath[0]="resources\\HERO\\hero1.png";
        imagePath[1]="resources\\HERO\\hero2.png";
        imagePath[2]="resources\\HERO\\hero_blowup_n1.png";
        imagePath[3]="resources\\HERO\\hero_blowup_n2.png";
        imagePath[4]="resources\\HERO\\hero_blowup_n3.png";
        imagePath[5]="resources\\HERO\\hero_blowup_n4.png";
        loadImages();
        loadSound(); 
    }
    public CircularDoubleLinkedListWithFreeNode getweapon(){return heroWeapon;}
    public void takedamage(int damage)
    {
      blood-=damage;
      if (blood<=0&&(state==0||state==1)) {
        state=2;
      }
    }
    public void checkCollision(CircularDoubleLinkedListWithFreeNode enemyBullet)
    {
        for(int i=0;i<enemyBullet.getRunnum();i++)
        {
            int[] pos = enemyBullet.getNowPos();
            if(pos[0]<x+width&&pos[0]+pos[2]>x&&pos[1]<y+height&&pos[3]+pos[1]>y)
            {
                blood-=enemyBullet.runList.getStrength();
                if (blood<=0) 
                {
                    state=2;    
                }
                enemyBullet.deactivate();
                return;
            }else if (enemyBullet.runList!=null) {
                enemyBullet.runList.moveToNext();
            }
        }
    }
    public int getStrength()
    {
      return strength;
    }
    public void forward()
    {
      heroWeapon.forward();
    }
    public void BulletStart()
    {
      heroWeapon.activated(x+width/2-3,y);
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
    // Getter 方法
    public int getBlood() {
        return blood;
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getState() {
        return state;
    }

    public String[] getImagePath() {
        return imagePath;
    }

    // Setter 方法
    public void setBlood(int blood) {
        this.blood = blood;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(int x,int y)
    {
      this.x = x;
      this.y = y;
      if (x<width/2) {
        x=width/2;
      }else if(x>960-width/2)
      {
        x=960-width/2;
      }
      if (y<0) {
        y=0;
      }else if(y>934-height)
      {
        y=934-height;
      }
    }

    public void changeX(int offset)
    {
      x+=offset*speed;
      if (x<-width/2) {
        x=-width/2;
      }else if(x>960-width/2)
      {
        x=960-width/2;
      }
    }

    public void changeY(int offset)
    {
      y+=offset*speed;
      if (y<0) {
        y=0;
      }else if(y>934-height)
      {
        y=934-height;
      }
    }
    
    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setImagePath(String[] imagePath) {
        this.imagePath = imagePath;
    }

    protected void print(Graphics2D g2d) {
        // 只在激活状态下绘制
        switch (state) {
          case 0:
            try {
                // 尝试使用图片
                g2d.drawImage(
                originalImage[0], 
                x, 
                y, 
                width, 
                height, 
                null
            );
                state=1;
            } catch (Exception e) {
                // 图片加载失败时，绘制一个红色矩形
                g2d.setColor(Color.RED);
                g2d.fillRect(
                x, 
                y,
                width, 
                height
                );
                g2d.dispose();
            }
            break;
            case 1:
            try {
                // 尝试使用图片
                g2d.drawImage(
                originalImage[1], 
                x, 
                y, 
                width, 
                height, 
                null
            );
                state=0;
            } catch (Exception e) {
                // 图片加载失败时，绘制一个红色矩形
                g2d.setColor(Color.RED);
                g2d.fillRect(
                x, 
                y,
                width, 
                height
                );
                g2d.dispose();
            }
            break;
            case 2:
            try {
                // 尝试使用图片
                g2d.drawImage(
                originalImage[2], 
                x, 
                y, 
                width, 
                height, 
                null
            );
                state=3;
                playSound();
            } catch (Exception e) {
                // 图片加载失败时，绘制一个红色矩形
                g2d.setColor(Color.RED);
                g2d.fillRect(
                x, 
                y,
                width, 
                height
                );
                g2d.dispose();
            }
            break;
            case 3:
            try {
                // 尝试使用图片
                g2d.drawImage(
                originalImage[3], 
                x, 
                y, 
                width, 
                height, 
                null
            );
                state=4;
            } catch (Exception e) {
                // 图片加载失败时，绘制一个红色矩形
                g2d.setColor(Color.RED);
                g2d.fillRect(
                x, 
                y,
                width, 
                height
                );
                g2d.dispose();
            }
            break;
            case 4:
            try {
                // 尝试使用图片
                g2d.drawImage(
                originalImage[4], 
                x, 
                y, 
                width, 
                height, 
                null
            );
                state=5;
            } catch (Exception e) {
                // 图片加载失败时，绘制一个红色矩形
                g2d.setColor(Color.RED);
                g2d.fillRect(
                x, 
                y,
                width, 
                height
                );
                g2d.dispose();
            }
            break;
            case 5:
            try {
                // 尝试使用图片
                g2d.drawImage(
                originalImage[5], 
                x, 
                y, 
                width, 
                height, 
                null
            );
            state = 6;
            } catch (Exception e) {
                // 图片加载失败时，绘制一个红色矩形
                g2d.setColor(Color.RED);
                g2d.fillRect(
                x, 
                y,
                width, 
                height
                );
                g2d.dispose();
            }
            break;
          default:
            break;
        }
        g2d.setColor(Color.red); // 设置文本颜色
        g2d.drawString("Blood: " + blood, x+8, y+height); // 输出血量，坐标可以根据需要调整
        heroWeapon.print(g2d);
    }
}