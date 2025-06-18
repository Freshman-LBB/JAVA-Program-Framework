import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 
import javax.imageio.ImageIO;

import java.awt.Color;
public class EnemyBullet extends weapon {
    private Clip soundClip;
    // 默认构造函数
    public EnemyBullet() {
        super();
    }
    private void loadSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("resources\\SOUND\\big_spaceship_flying.wav"));
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
    // 拷贝构造函数
    protected EnemyBullet(EnemyBullet T) {
        super(T);
        this.soundClip=T.soundClip;
    }
    @Override
    protected void activate(int x,int y)
    {
        state = true;
        img=0;
        setPositionX(x);
        setPositionY(y);
        playSound(); // 播放音效
    }
    // 重写父类的init方法
    @Override
    protected void init() {
        // 设置普通敌人的默认属性
        setWide(9);
        setHeight(21);
        setPositionX(0);
        setPositionY(0);
        setStrength(1);
        img = 0;
        imagePath = new String[1];
        imagePath[0]="resources\\WEAPON\\bullet2.png";
        loadImages();
        loadSound();
    }
    private void loadImages() {
        this.originalImage = new BufferedImage[1];
        try {
            for (int i = 0; i < 1; i++) {
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
        setPositionY(currentY + 1);
        if (getPositionY()>960) 
        {
            deactivate();
        }
    }
    @Override
    protected Node copy() {
        return new EnemyBullet(this);
    }
    @Override
    protected void print(Graphics2D g2d) {
        // 只在激活状态下绘制
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
          default:
            break;
        }
        }
    }
}
