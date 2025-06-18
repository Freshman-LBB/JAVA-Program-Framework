import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;  
import java.awt.event.MouseMotionListener;  
import java.awt.event.MouseListener;
import javax.sound.sampled.*;

public class Program extends JPanel implements ActionListener, KeyListener, MouseMotionListener, MouseListener {
    private BufferedImage[] backgroundImages; // 存储4张图片
    private int panelWidth;   // 窗口宽度
    private int panelHeight;  // 窗口高度
    private int imageWidth;   // 单张图片宽度
    private int imageHeight;  // 单张图片高度
    private int[] yPositions; // 记录每组图片的垂直位置
    private int fps;
    private int bkspeed;
    private Timer timer;
    private CircularDoubleLinkedListWithFreeNode heroweapon;

    private Clip backgroundMusicClip;
    private FloatControl volumeControl; // 控制音量
    
    // 双缓冲相关
    private BufferedImage offscreenImage;
    private Graphics2D offscreenGraphics;
    
    private CircularDoubleLinkedListWithFreeNode commonEnemyList;
    private CircularDoubleLinkedListWithFreeNode specialEnemyList;
    private CircularDoubleLinkedListWithFreeNode weaponEnemyList;
    private CircularDoubleLinkedListWithFreeNode toolChain;
    private hero mainHero;

    // 游戏状态管理
    private enum GameState { START_MENU, PLAYING, SETTINGS, GAME_OVER }
    private GameState gameState = GameState.START_MENU;

    // UI元素
    private Rectangle startButton;
    private Rectangle settingsButton;
    private Rectangle backButton;
    private Rectangle restartButton;
    private Rectangle exitButton;
    private Rectangle volUpButton;
    private Rectangle volDownButton;
    private int currentVolume = 50; // 默认音量50%
    private int score = 0; // 玩家得分
    private Font titleFont;
    private Font buttonFont;
    private Font scoreFont;

    public Program() {
        try {
            fps=0;
            bkspeed = 5;
            // 读取背景图片
            BufferedImage originalImage = ImageIO.read(new File("resources\\BACKGROUND\\background.png"));
            
            // 计算尺寸
            imageWidth = originalImage.getWidth();
            imageHeight = originalImage.getHeight();
            
            // 设置窗口大小（两张图片宽，1.5倍图片高）
            panelWidth = imageWidth * 2;
            panelHeight = (int)(imageHeight * 1.1); // 能看见2张完整 + 2张部分图片

            // 准备4张图片数组
            backgroundImages = new BufferedImage[4];
            for (int i = 0; i < 4; i++) {
                backgroundImages[i] = originalImage;
            }

            // 初始化y坐标数组
            yPositions = new int[2];
            for (int i = 0; i < 2; i++) {
                yPositions[i] = -panelHeight * i;
            }

            // 创建离屏图像
            offscreenImage = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_ARGB);
            offscreenGraphics = offscreenImage.createGraphics();
            
            // 设置渲染提示
            offscreenGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            offscreenGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            offscreenGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 设置面板属性
            setPreferredSize(new Dimension(panelWidth, panelHeight));
            setLayout(null);
            
            mainHero = new hero();
            heroweapon=mainHero.getweapon();
            CommonEnemy a = new CommonEnemy();
            commonEnemyList = new CircularDoubleLinkedListWithFreeNode(3,a);
            SpecialEnemy b = new SpecialEnemy();
            specialEnemyList =new CircularDoubleLinkedListWithFreeNode(13,b);
            EnemyWeapon c = new EnemyWeapon();
            weaponEnemyList = new CircularDoubleLinkedListWithFreeNode(7, c);
            GoodTool d = new GoodTool();
            toolChain = new CircularDoubleLinkedListWithFreeNode(5, d);
            
            setFocusable(true);
            addKeyListener(this);

            addMouseMotionListener(this);
            addMouseListener(this);
            
            // 初始化UI元素
            int centerX = panelWidth / 2;
            int buttonWidth = 200;
            int buttonHeight = 50;

            startButton = new Rectangle(centerX - buttonWidth/2, panelHeight/2 - 40, buttonWidth, buttonHeight);
            settingsButton = new Rectangle(centerX - buttonWidth/2, panelHeight/2 + 40, buttonWidth, buttonHeight);
            backButton = new Rectangle(20, 20, 100, 40);
            restartButton = new Rectangle(centerX - buttonWidth/2, panelHeight/2 - 40, buttonWidth, buttonHeight);
            exitButton = new Rectangle(centerX - buttonWidth/2, panelHeight/2 + 40, buttonWidth, buttonHeight);
            volUpButton = new Rectangle(centerX + 60, panelHeight/2, 40, 40);
            volDownButton = new Rectangle(centerX - 100, panelHeight/2, 40, 40);
            
            // 加载字体
            titleFont = new Font("Arial", Font.BOLD, 48);
            buttonFont = new Font("Arial", Font.BOLD, 24);
            scoreFont = new Font("Arial", Font.BOLD, 36);
            
            // 创建定时器控制移动
            timer = new Timer(10, this);
            timer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    try {
    // 加载背景音乐
    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("resources\\SOUND\\game_music.wav"));
    backgroundMusicClip = AudioSystem.getClip();
    backgroundMusicClip.open(audioInputStream);
    volumeControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);

    // 启动并循环播放音乐
    backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);

    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        e.printStackTrace();
    }
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        // 仅在游戏进行中响应鼠标移动
    if (gameState == GameState.PLAYING) {
        int mouseX = e.getX() - mainHero.getWidth() / 2;
        int mouseY = e.getY() - mainHero.getHeight() / 2;
        mainHero.setPosition(mouseX, mouseY);
        repaint();
    }
    }
    // 鼠标拖拽监听方法
    @Override
    public void mouseDragged(MouseEvent e) {
        // 仅在游戏进行中响应鼠标拖拽
    if (gameState == GameState.PLAYING) {
        int mouseX = e.getX() - mainHero.getWidth() / 2;
        int mouseY = e.getY() - mainHero.getHeight() / 2;
        mainHero.setPosition(mouseX, mouseY);
        repaint();
    }
    }
    // 鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (gameState) {
        case START_MENU:
            // 处理开始菜单按钮点击
            if (startButton.contains(e.getPoint())) {
                gameState = GameState.PLAYING;
                // 重置游戏状态
                resetGame();
            } else if (settingsButton.contains(e.getPoint())) {
                gameState = GameState.SETTINGS;
            }
            break;
        
        case PLAYING:
            // 游戏中只处理子弹发射
            if (e.getButton() == MouseEvent.BUTTON1) {
                mainHero.BulletStart();
            }
            break;
        
        case SETTINGS:
        if (backButton.contains(e.getPoint())) {
            gameState = GameState.START_MENU;
        } else if (volUpButton.contains(e.getPoint())) {
            // 增加音量
            currentVolume = Math.min(100, currentVolume + 10);
            setVolume(currentVolume);  // 调用设置音量的方法
        } else if (volDownButton.contains(e.getPoint())) {
            // 降低音量
            currentVolume = Math.max(0, currentVolume - 10);
            setVolume(currentVolume);  // 调用设置音量的方法
        }
        break;
        
        case GAME_OVER:
            if (restartButton.contains(e.getPoint())) {
                gameState = GameState.PLAYING;
                resetGame();
            } else if (exitButton.contains(e.getPoint())) {
                gameState = GameState.START_MENU;
            }
            break;
    }
    repaint();
    }
    private void resetGame() {
    // 重置主角
    mainHero = new hero();
    heroweapon = mainHero.getweapon();
    // 重置敌人列表
    CommonEnemy a = new CommonEnemy();
    commonEnemyList = new CircularDoubleLinkedListWithFreeNode(3, a);
    SpecialEnemy b = new SpecialEnemy();
    specialEnemyList = new CircularDoubleLinkedListWithFreeNode(13, b);
    EnemyWeapon c = new EnemyWeapon();
    weaponEnemyList = new CircularDoubleLinkedListWithFreeNode(7, c);
    GoodTool d = new GoodTool();
    toolChain = new CircularDoubleLinkedListWithFreeNode(5, d);
    
    // 重置分数和帧数
    score = 0;
    fps = 0;
}
    private void setVolume(int volume) {
    if (volumeControl != null) {
        float gainValue = (volume - 100) * 0.8f; // 调整音量范围
        volumeControl.setValue(gainValue); // 设置音量
    }
}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
public void keyPressed(KeyEvent e) {
    switch (gameState) {
        case PLAYING:
            int keyCode = e.getKeyCode();
            switch (keyCode) {
                case KeyEvent.VK_W: // 上
                    mainHero.changeY(-1);
                    break;
                case KeyEvent.VK_S: // 下
                    mainHero.changeY(1);
                    break;
                case KeyEvent.VK_A: // 左
                    mainHero.changeX(-1);
                    break;
                case KeyEvent.VK_D: // 右
                    mainHero.changeX(1);
                    break;
                case KeyEvent.VK_ESCAPE:
                    gameState = GameState.GAME_OVER;
                    break;
            }
            repaint();
            break;
        
        case START_MENU:
        case SETTINGS:
        case GAME_OVER:
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                gameState = GameState.START_MENU;
            }
            break;
    }
}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 清除离屏图像
        offscreenGraphics.setComposite(AlphaComposite.Clear);
        offscreenGraphics.fillRect(0, 0, panelWidth, panelHeight);
        offscreenGraphics.setComposite(AlphaComposite.SrcOver);

        // 绘制背景图到离屏图像
        for (int i = 0; i < 2; i++) {
            offscreenGraphics.drawImage(backgroundImages[i * 2], 0, yPositions[i], imageWidth, panelHeight + bkspeed, null);
            offscreenGraphics.drawImage(backgroundImages[i * 2 + 1], panelWidth / 2, yPositions[i], imageWidth, panelHeight + bkspeed, null);
        }

        // 根据游戏状态绘制不同内容
        switch (gameState) {
            case START_MENU:
                drawStartMenu(offscreenGraphics);
                break;
            case PLAYING:
                // 绘制敌人
                commonEnemyList.print(offscreenGraphics);
                specialEnemyList.print(offscreenGraphics);
                weaponEnemyList.print(offscreenGraphics);
                toolChain.print(offscreenGraphics);
                mainHero.print(offscreenGraphics);
                drawScore(offscreenGraphics);
                break;
            case SETTINGS:
                drawSettingsMenu(offscreenGraphics);
                break;
            case GAME_OVER:
                drawGameOverMenu(offscreenGraphics);
                break;
        }
        
        // 将离屏图像绘制到屏幕
        g.drawImage(offscreenImage, 0, 0, null);
    }
     // 绘制开始菜单
    private void drawStartMenu(Graphics2D g2d) {
        // 半透明背景
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, panelWidth, panelHeight);
        // 游戏标题
        g2d.setFont(titleFont);
        g2d.setColor(Color.WHITE);
        String title = "PLANE WAR";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, panelWidth/2 - titleWidth/2, panelHeight/4);
        
        // 开始按钮
        drawButton(g2d, startButton, "START", startButton.contains(getMousePosition()));
        
        // 设置按钮
        drawButton(g2d, settingsButton, "SETTINGS", settingsButton.contains(getMousePosition()));
    }
    // 绘制设置菜单
    private void drawSettingsMenu(Graphics2D g2d) {
        // 半透明背景
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, panelWidth, panelHeight);
        
        // 标题
        g2d.setFont(titleFont);
        g2d.setColor(Color.WHITE);
        String title = "SETTINGS";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, panelWidth/2 - titleWidth/2, panelHeight/4);
        
        // 返回按钮
        drawButton(g2d, backButton, "RETURN", backButton.contains(getMousePosition()));
        
        // 音量设置
        g2d.setFont(buttonFont);
        g2d.setColor(Color.WHITE);
        String volText = "VOLUME: " + currentVolume + "%";
        int textWidth = g2d.getFontMetrics().stringWidth(volText);
        g2d.drawString(volText, panelWidth/2 - textWidth/2, panelHeight/2 - 30);
        
        // 音量调节按钮
        drawButton(g2d, volDownButton, "-", volDownButton.contains(getMousePosition()));
        drawButton(g2d, volUpButton, "+", volUpButton.contains(getMousePosition()));
    }
    // 绘制游戏结束菜单
    private void drawGameOverMenu(Graphics2D g2d) {
        // 半透明黑色背景
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, panelWidth, panelHeight);
        // 游戏结束标题
        g2d.setFont(titleFont);
        g2d.setColor(Color.RED);
        String title = "GAME OVER";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, panelWidth/2 - titleWidth/2, panelHeight/3);
        
        // 显示得分
        g2d.setFont(scoreFont);
        g2d.setColor(Color.YELLOW);
        String scoreText = "FINAL SCORE: " + score;
        int scoreWidth = g2d.getFontMetrics().stringWidth(scoreText);
        g2d.drawString(scoreText, panelWidth/2 - scoreWidth/2, panelHeight/2 - 40);
        
        // 重新开始按钮
        drawButton(g2d, restartButton, "RESTART", restartButton.contains(getMousePosition()));
        
        // 退出按钮
        drawButton(g2d, exitButton, "EXIT", exitButton.contains(getMousePosition()));
    }
    // 绘制游戏得分
    private void drawScore(Graphics2D g2d) {
        g2d.setFont(scoreFont);
        
        // 添加描边效果使文字更清晰
        g2d.setStroke(new BasicStroke(3)); // 加粗描边
        g2d.setColor(Color.BLACK);
        g2d.drawString("SCORE: " + score, panelWidth/2 - 80 + 1, 55 + 1); // 向下移动位置
        
        g2d.setColor(Color.YELLOW);
        g2d.drawString("SCORE: " + score, panelWidth/2 - 80, 55); // 向下移动位置
        
        // 添加结束信息
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(Color.WHITE);
        g2d.drawString("PRESS ESC TO END", 20, 30);
    }
    
    // 绘制按钮
    private void drawButton(Graphics2D g2d, Rectangle rect, String text, boolean hover) {
        // 按钮背景
        if (hover) {
            g2d.setColor(new Color(100, 180, 255)); // 悬停状态
        } else {
            g2d.setColor(new Color(70, 130, 200)); // 正常状态
        }
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);
        
        // 按钮边框
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);
        
        // 按钮文字
        g2d.setFont(buttonFont);
        g2d.setColor(Color.WHITE);
        
        // 计算文本宽度
        int textWidth = g2d.getFontMetrics().stringWidth(text);
        
        // 计算文本高度
        int textHeight = g2d.getFontMetrics().getHeight();
        int ascent = g2d.getFontMetrics().getAscent();
        
        // 计算文本位置
        int textX = rect.x + (rect.width - textWidth) / 2;
        int textY = rect.y + (rect.height - textHeight) / 2 + ascent;
        
        // 绘制文字
        g2d.drawString(text, textX, textY);
    }
    
    @Override
public void actionPerformed(ActionEvent e) {
    // 只在游戏进行状态下执行游戏逻辑
    if (gameState == GameState.PLAYING) {
        // 移动背景
        for (int i = 0; i < 2; i++) {
            yPositions[i] += bkspeed; // 向下移动速度

            // 如果图片完全移出屏幕，重置位置
            if (yPositions[i] >= panelHeight) {
                yPositions[i] = -panelHeight;
            }
        }
        
        if(fps%200==0)commonEnemyList.activated(0,0);
        if (fps % 10 == 1 && fps>890) {
            specialEnemyList.activated(0, 0);
        }
        if(fps==250||fps==500)weaponEnemyList.activated(0, 0);
        if (fps==200|fps==600) 
        {
            toolChain.activated(0, 0);
        }
        commonEnemyList.forward();
        specialEnemyList.forward();
        weaponEnemyList.forward();
        toolChain.forward();
        int a = commonEnemyList.checkCollision(mainHero.getX(), mainHero.getY(), mainHero.getWidth(), mainHero.getHeight(), mainHero.getStrength());
        if(a != 0) mainHero.takedamage(a);
        score += a;
        
        a = specialEnemyList.checkCollision(mainHero.getX(), mainHero.getY(), mainHero.getWidth(), mainHero.getHeight(), mainHero.getStrength());
        if(a != 0) mainHero.takedamage(a);
        score += a;
        
        a = weaponEnemyList.checkCollision(mainHero.getX(), mainHero.getY(), mainHero.getWidth(), mainHero.getHeight(), mainHero.getStrength());
        if(a != 0) mainHero.takedamage(a);
        score += a;

        // 检查主角状态，切换到游戏结束
        if (mainHero.getState() == 6) {
            gameState = GameState.GAME_OVER;
        }
        
        for(int i = 0; i < heroweapon.runNum; i++) {
            int[] pos = heroweapon.getNowPos();
            int effect1 = commonEnemyList.checkCollision(pos[0], pos[1], pos[2], pos[3], mainHero.getStrength());
            int effect2 = specialEnemyList.checkCollision(pos[0], pos[1], pos[2], pos[3], mainHero.getStrength());
            int effect3 = weaponEnemyList.checkCollision(pos[0], pos[1], pos[2], pos[3], mainHero.getStrength());
            if(effect1 != 0) {
                heroweapon.deactivate();
                score += effect1;
            } else if(effect2 != 0) {
                heroweapon.deactivate();
                score += effect2;
            } else if(effect3 != 0)
            {
                heroweapon.deactivate();
                score += effect3;
            }else
            {
                heroweapon.runList.moveToNext();
            }
        }
        if(weaponEnemyList.runList!=null)
        {
            CircularDoubleLinkedListWithFreeNode d=weaponEnemyList.getNowWeapon();
            mainHero.checkCollision(d);
        }
        if (toolChain.runList!=null) 
        {
            mainHero.checkCollision(toolChain);
        }
        mainHero.forward();
        fps++;
        fps %= 1000;
    }
    
    repaint();
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("飞机大战");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Program panel = new Program();
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
