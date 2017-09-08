import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Color;

import java.awt.*;
import java.awt.event.*;

class StageMap {
	public StageMap() {
		try {
			bg = ImageIO.read(getClass().getResource("fcbchrt.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
			bg = null;
		}
	}
	public void drawMap(Graphics g, JPanel jp, double panelWidth, double panelHeight) {
		Graphics2D g2D = (Graphics2D)g;
		double imageWidth = 400;
		double imageHeight = 320;
		g.setColor(Color.black);
		g.fillRect( (int)0,(int)0,(int)panelWidth,(int)panelHeight);
	}
	private BufferedImage bg;
	public int map[];
	public int chip[];
}
class FireBall {
	public FireBall() {
		fx = 1000;
		fy = 0;
	}
	public boolean checkCross(int x, int y) {
		if( fx - 16 <= x && x <= fx +16 ) {
			if( fy - 16 <= y && y <= fy +16 ) {
				return true;
			}
		}
		return false;
	}
	
	public void moveFireBall(StageMap sm, int mx, int my, int md, int key_fire, int key_left, int key_right) {
		fx = fx + 8;
	}

	public int fx;
	public int fy;
}
class Option {
	final int OPT_DLY = 64;
	int ox[];
	int oy[];
	Option (int x, int y) {
		ox = new int[OPT_DLY];
		oy = new int[OPT_DLY];
		for(int i=0; i< OPT_DLY; i++) {
			ox[i] = x;
			oy[i] = y;
		}
	}
}
class MainPanel extends JPanel implements Runnable {
    // 描画する画像
    private Thread game_loop;
    private BufferedImage image;
    private int key_left;
    private int key_right;
    private int key_up;
    private int key_down;
    private int key_jump;
    private int key_fire;
    private StageMap sm;
    private int mx; // 自機の位置
    private int my; // 自機の位置
    private FireBall fb[];
    private int ff;
    private boolean fmove;
    private Option opt[];
    private int gt; // ゲーム進行フレーム
    
    final int FIRE_MAX = 256;
    final int OPT_MAX = 2;

    public void gameStart() {
	fmove = false;
	fb = new FireBall[FIRE_MAX];
	for( int i = 0; i < FIRE_MAX; i++){
		fb[i] = new FireBall();
	}
	opt = new Option[OPT_MAX];
	for( int i = 0; i < OPT_MAX; i++){
		opt[i] = new Option(mx,my);
	}
	sm = new StageMap();
	game_loop = new Thread(this);
	game_loop.start();
    }

    public void setLeft(int a) {
        key_left = a;
    }
    public void setRight(int a) {
        key_right = a;
    }
    public void setUp(int a) {
        key_up = a;
    }
    public void setDown(int a) {
        key_down = a;
    }
    public void setJump(int a) {
	key_jump = a;
    }
    public void setFire(int a) {
    	if( key_fire == 0 )
		key_fire = a;
	if( key_fire == 2 && a == 0 )
		key_fire = 0;
    }

    public MainPanel(String path) {
        try {
            this.image = ImageIO.read(getClass().getResource(path));
        } catch (IOException ex) {
            ex.printStackTrace();
            this.image = null;
        }
    }

    public void buffCopy(Graphics2D g2D, int dstX1,int dstY1,int dstX2,int dstY2,int x,int y, boolean lr){
		double imageWidth = image.getWidth();
		double imageHeight = image.getHeight();
		int srcX1 = 12 + x * (32 + 16);
		int srcY1 = 12 + y * (32 + 11);
		int srcX2 = srcX1+32;
		int srcY2 = srcY1+32;
		// スケーリング
		if( lr == false ) {
		    g2D.drawImage(image, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,srcX1,srcY1,srcX2,srcY2, this);
		} else {
		    g2D.drawImage(image, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,srcX2,srcY1,srcX1,srcY2, this);
		}
    }

    public void fireBall(Graphics2D g2D,int fbx,int fby,int t){
	double panelWidth = this.getWidth();
	double panelHeight = this.getHeight();
	double dstX1=(fbx) * panelWidth / 400;
	double dstX2=dstX1 + (panelWidth / 25);
	double dstY1=fby;
	double dstY2=dstY1 + (panelHeight / 20);
	int x=0;
	int y=0;
	boolean lr = false;
	switch((t % 16) / 4) {
		case 0: x = 316; y = 270; break;
		case 1: x = 316; y = 286; break;
		case 2: x = 364; y = 270; break;
		case 3: x = 316; y = 286; lr = true; break;
	}
	int srcX1 = x;
	int srcY1 = y;
	int srcX2 = x+16;
	int srcY2 = y+16;
	// スケーリング
	if( lr == false ) {
		g2D.drawImage(image, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,srcX1,srcY1,srcX2,srcY2, this);
	} else {
		g2D.drawImage(image, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,srcX2,srcY1,srcX1,srcY2, this);
	}
    }

    public void run() {
	while( true ) {
	    moveCommand();
	    repaint();
	    try {
	    	Thread.sleep(18);
	    } catch (InterruptedException e) {
	    	e.printStackTrace();
	    }
	}
    }

    public void moveCommand() {
	double g = 0.0625;
	if( key_fire == 1 ) {
		fb[ff].fx = mx + 16;
		fb[ff].fy = my + 12;
		ff++;
		if(ff == FIRE_MAX) ff = 0;
		for(int i=0; i<OPT_MAX; i++){
			int t = gt % (16 * (i+1));
			fb[ff].fx = opt[i].ox[t] + 16;
			fb[ff].fy = opt[i].oy[t] + 12;
			ff++;
			if(ff == FIRE_MAX) ff = 0;
		}
		key_fire = 2;
	}
	for( int i=0; i<FIRE_MAX; i++ ) {
		fb[i].moveFireBall(sm,mx,my,0,key_fire,key_left,key_right);
	}
	fmove = false;
	if(key_left == 1) {
		mx = mx -4;
		fmove = true;
	} else if(key_right == 1) {
		mx = mx + 4;
		fmove = true;
	}
	if(key_up == 1) {
		my = my + 4;
		fmove = true;
	} else if(key_down == 1) {
		my = my - 4;
		fmove = true;
	}
    }

    @Override
    public void paintComponent(Graphics g) {
	Graphics2D g2D = (Graphics2D) g;

	double panelWidth = this.getWidth();
	double panelHeight = this.getHeight();

	double dstX1 = 0;
	double dstY1 = 0;
	double dstX2 = 0;
	double dstY2 = 0;
	// マップの描画	
	sm.drawMap(g2D,this,panelWidth,panelHeight);
	// オプションの描画
	for(int i=0; i<OPT_MAX; i++){
		int t = gt % (16 * (i+1));
		dstX1=(opt[i].ox[t]) * panelWidth / 400;
		dstX2=dstX1 + 32 * panelWidth / 400;
		dstY1 = (16 * 6 -opt[i].oy[t]) * panelHeight / 320;
		dstY2 = dstY1 + 32 * panelHeight / 320;
		buffCopy(g2D, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,6,3, true);
	}
	// 自機の描画
	dstX1=(mx) * panelWidth / 400;
	dstX2=dstX1 + 32 * panelWidth / 400;
	dstY1 = (16 * 6 -my) * panelHeight / 320;
	dstY2 = dstY1 + 32 * panelHeight / 320;
	buffCopy(g2D, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,3,5, true);
	// フレーム進行
	if(fmove){
		for(int i=0; i<OPT_MAX; i++){
			int t = gt % (16 * (i+1));
			opt[i].ox[t] = mx;
			opt[i].oy[t] = my;
		}
		gt++;
	}
	// ファイアボールの描画
	for( int i=0; i<FIRE_MAX; i++ ) {
		fireBall(g2D, fb[i].fx, (int)( ( 16 * 7 -fb[i].fy ) * panelHeight / 320), 0);
	}
    }
};

class Gradius extends JFrame{
    private MainPanel FBI;
    public static void main(String args[]){
	Gradius frame = new Gradius();
	frame.WindowEventSample();
	frame.Key1();
	frame.setVisible(true);
    }

    Gradius(){
	setTitle("Gradius");
	setBounds(100, 100, 400, 320);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	Container contentPane = getContentPane();
	FBI = new MainPanel("Fcbobj.png");
	contentPane.add(FBI);
	
	FBI.gameStart();
    }

    public void WindowEventSample() {
	//移動やサイズ変更関係
	addComponentListener(new ComponentAdapter(){
	    @Override
	    public void componentResized(ComponentEvent e) {
		double H = getHeight();
		if(H <160) {H =160;}
	        setSize((int)(H / 320 * 400),(int)H);
	    }
 	});
    }
    public void Key1(){
        requestFocus();   //フォーカスを取得
        addKeyListener(   //キーリスナーを追加
            new KeyAdapter(){
                public void keyPressed(KeyEvent e){
                    switch (e.getKeyCode()){  //押されたキーコードを得る
                        case KeyEvent.VK_LEFT  :  FBI.setLeft(1); break;
                        case KeyEvent.VK_RIGHT :  FBI.setRight(1); break;
                        case KeyEvent.VK_UP  :  FBI.setUp(1); break;
                        case KeyEvent.VK_DOWN :  FBI.setDown(1); break;
                        case KeyEvent.VK_Z :  FBI.setJump(1); break;
			case KeyEvent.VK_X :  FBI.setFire(1); break;
                        default:
                            break;
                    }
		}
    		public void keyReleased(KeyEvent e) {
                    switch (e.getKeyCode()){  //押されたキーコードを得る
                        case KeyEvent.VK_LEFT  :  FBI.setLeft(0); break;
                        case KeyEvent.VK_RIGHT :  FBI.setRight(0); break;
                        case KeyEvent.VK_UP  :  FBI.setUp(0); break;
                        case KeyEvent.VK_DOWN :  FBI.setDown(0); break;
                        case KeyEvent.VK_Z :  FBI.setJump(0); break;
                        case KeyEvent.VK_X :  FBI.setFire(0); break;
                        default:
                            break;
                    }
    		}
            }
        );
        //addWindowListener(new WinAdapter());            
    }  
};
