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
import java.util.Calendar;

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
		map =  new String(
		"6666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666" +
		"0000000006000000000000000006500000000000000000005000000000060000000000000000065000000000000000000000" +
		"0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
		"0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
		"0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
		"0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000056000000" +
		"0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000056000000" +
		"0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000556600000" +
		"0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000005556660000" +
		"0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000055556666000" +
		"0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000006665550000" +
		"0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000665600000" +
		"0000000000000000000000000000000000000000000000000000000000000000000056000000000000000000000065000000" +
		"0000000000000000000560000000000000000000006600000000000000000000000556600000000000000000000065000000" +
		"0000000000000000005666000000000000000000056660000000000000000000000000000000000000000000000000000000" +
		"0000000000000000055666600000000000000005556666000000000000000000000000000000000000000000000000000000" +
		"0000000000000000555666660000000000000055556666600000000000000000000000000000000000000000000000000000" +
		"0007770000000055555666666007770000005555556666666000007770000000555556666000007700000000000007777000" +
		"6666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666" +
		"2132132132132132132132132132131232132132132132132132132132132132132132132132131232132132132132132132" +
		"0");
		chip = new int[] {
		0,0,	// 0:ダミー
		0,5,	// 1:プロック
		1,0,	// 2:左ブロック
		0,0,	// 3:右ブロック
		2,7,	// 4:ハシゴ
		2,6,	// 5:岩１
		3,6,	// 6:岩２
		3,8,	// 7:森
		5,5,	// 8:パワー中
		6,5,	// 9:パワー右
		0 };
	}
	public int getChipX(int a) {
		return chip[(a-'0')*2];
	}
	public int getChipY(int a) {
		return chip[(a-'0')*2+1];
	}
	public void drawMap(Graphics g, int sx, JPanel jp, double panelWidth, double panelHeight) {
		Graphics2D g2D = (Graphics2D)g;
		double imageWidth = 400;
		double imageHeight = 320;
		g.setColor(Color.black);
		g.fillRect( (int)0,(int)0,(int)panelWidth,(int)panelHeight);
		for(int y=0;y<20;y++){
			for(int x=0;x<26;x++){
				int cx = (x + (sx / 16)) % MAP_WIDTH;
				if(map.charAt(y*MAP_WIDTH+cx) != '0'){
					int a = map.charAt(y*MAP_WIDTH+x);
					int ax = x * 16;
					if(y < 19) {
						a = map.charAt(y*MAP_WIDTH+cx);
						ax = x * 16 - (sx % 16);
					}
					int dstX1 = (int)( ax * panelWidth / imageWidth);
					int dstY1 = (int)( y * 16 * panelHeight / imageHeight);
					int dstX2 = (int)( (ax + 16) * panelWidth / imageWidth);
					int dstY2 = (int)( (y+1) * 16 * panelHeight / imageHeight);
					int srcX1 = 35 + getChipX(a) * (16 + 16);
					int srcY1 = 28 + getChipY(a) * (16 + 4);
					int srcX2 = srcX1 + 16;
					int srcY2 = srcY1 + 16;
					g2D.drawImage(bg, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,srcX1,srcY1,srcX2,srcY2, jp);
				}
			}
		}
	}
	private BufferedImage bg;
	public String map;
	public int chip[];
	public final int MAP_WIDTH = 100;
}
class FireBall {
	public FireBall() {
		fx = 1000;
		fy = 0;
	}
	public boolean checkCross(int x, int y, int w, int h) {
		if(Math.abs((fx + 8) - (x + 16)) < w/2 + 4 //横の判定
		   &&
		   Math.abs((fy + 8) - (y + 16)) < h/2 + 4 //縦の判定
		) {
			return true;
		}
		return false;
	}
	
	public void moveFireBall(StageMap sm, int mx, int my, int md, int key_fire, int key_left, int key_right) {
		fx = fx + 8;
	}

	public int fx;
	public int fy;
}
class EnemyFire {
	public EnemyFire() {
		eraseFire();
	}
	public void eraseFire() {
		bx = -100;
		by =  0;
		vx = 0;
		vy = 0;
	}
	public void setFire(int tx, int ty, int x, int y, double speed) {
		double mx = tx + 16;
		double my = ty + 16;
		bx = x + 16;
		by = y + 16;
		double d = Math.sqrt((mx - bx) * (mx - bx) + (my - by) * (my - by));
		vx = (mx - bx) / d * speed;
		vy = (my - by) / d * speed;
	}
	public void moveEnemyFire(StageMap sm, int mx, int my) {
		bx = bx +vx;
		by = by +vy;
	}
	public double bx;
	public double by;
	public double vx;
	public double vy;
}
class Option {
	final int OPT_DLY = 256;
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
class Enemy {
	Enemy (int x, int y) {
		ex = x;
		y_center = y;
	}
	public boolean isDisp() {
		if(-32 < ex && ex < 400 && -32 < ey && ey < 320 ) {
			return true;
		}
		return false;
	}
	public void moveEnemy(){
		ex = ex - 3;
		ey = y_center + Math.cos(ex / 32) * 32;
	}
	double ex;
	double ey;
	double y_center;
}
class Tank {
	Tank (int x, int y) {
		tx = x;
		ty = y;
	}
	public void moveTank(StageMap sm, int sx){
		if(stop > 0) {
			tx = tx - 1;
			stop--;
		} else {
			tx = tx + 2;
		}
		ty = 320 - 48;
		// 床の上にいるか
		for(int y=18;y>0;y--){
			for(int x=0;x<25;x++){
				int cx = (x + (sx / 16)) % sm.MAP_WIDTH;
				int a = sm.map.charAt(y*sm.MAP_WIDTH+cx);
				// マップチップがブロックなら交差判定
				if(a == '5' || a == '6'){
					int fx = x * 16 - (sx % 16);;
					int fy = y * 16;
					int w = 32;
					int h = 32;
					if(Math.abs((fx + 8) - (tx + 16)) < w/2 + 8 //横の判定
					   &&
					   Math.abs((fy + 8) - (ty + 16)) < h/2 + 4 //縦の判定
					) {
						ty = fy - 32;
					}
				}
			}
		}
	}
	public double tx;
	public double ty;
	public int stop;
}
class OriginalRandom {
	private static final double M = 65536;
	private static final double A = 997;
	private static final double B = 1;
	private static final double X = 573;
	private double x;
	OriginalRandom() {
		x = X;
	}
	private void next() {
		x = (A * x + B) % M;
	}
	public int nextInt(int a) {
		next();
		return (int)x % a;
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
    private int sx;
    private int mx; // 自機の位置
    private int my; // 自機の位置
    private FireBall fb[];
    private int ff;
    private boolean fmove;
    private Option opt[];
    private Enemy em[];
    private EnemyFire ef[];
    private Tank ta;
    private int eff;
    private OriginalRandom rnd;
    private int gt; // ゲーム進行フレーム
    private long gtimer; //フレーム進行タイマー	
    
    final int FRAME_INTERVAL = 16;
    final int SCROLL_SPEED = 1;
    final int FIRE_MAX = 1024;
    final int OPT_MAX = 2;
    final int ENEMY_MAX = 8;
    final int BULLET_RATE = 300;
    final int TANK_BULLET = 100;

    public void gameStart() {
	fmove = false;
	my = 16 * 4;
	fb = new FireBall[FIRE_MAX];
	ef = new EnemyFire[FIRE_MAX];
	for( int i = 0; i < FIRE_MAX; i++){
		fb[i] = new FireBall();
		ef[i] = new EnemyFire();
	}
	opt = new Option[OPT_MAX];
	for( int i = 0; i < OPT_MAX; i++){
		opt[i] = new Option(mx,my);
	}
	rnd = new OriginalRandom();
	em = new Enemy[ENEMY_MAX];
	for( int i = 0; i < ENEMY_MAX; i++){
		em[i] = new Enemy(-100,0);
	}
	ta = new Tank(1000, 0);
	sx = 0;
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

    public void buffCopy(Graphics2D g2D, int dstX1,int dstY1,int dstX2,int dstY2,int x,int y, boolean lr, boolean ud){
		double imageWidth = image.getWidth();
		double imageHeight = image.getHeight();
		int srcX1 = 12 + x * (32 + 16);
		int srcY1 = 12 + y * (32 + 11);
		int srcX2 = srcX1+32;
		int srcY2 = srcY1+32;
		// スケーリング
		if( lr == true ) {
			int buff = srcX1;
			srcX1 = srcX2;
			srcX2 = buff;
		}
		if( ud == true ) {
			int buff = srcY1;
			srcY1 = srcY2;
			srcY2 = buff;
		}
		g2D.drawImage(image, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,srcX1,srcY1,srcX2,srcY2, this);
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
	if( lr == true ) {
		int buff = srcX1;
		srcX1 = srcX2;
		srcX2 = buff;
	}
	g2D.drawImage(image, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,srcX1,srcY1,srcX2,srcY2, this);
    }

    public void run() {
	while( true ) {
	    long buff = gtimer;
	    moveCommand();
	    repaint();
	    sx = sx + SCROLL_SPEED; // スクロール
	    if(sx % (300 * SCROLL_SPEED ) == 0){ // 敵発生
		for( int i = 0; i < ENEMY_MAX; i++){
			em[i].ex = rnd.nextInt(200) + 400;
			em[i].y_center = rnd.nextInt(320 - 128) + 64;
		}
	    }
	    if(sx % ( 260 * SCROLL_SPEED ) == 0){ // 戦車（カメ）発生
		if(ta.tx > 400) {
			ta.tx = -96;
		}
	    }
	    try {
		for(int i=0; i<FRAME_INTERVAL; i++){
			Thread.sleep(1);
			gtimer = System.currentTimeMillis();
			if (FRAME_INTERVAL <= gtimer - buff) {
				break;
			}
		}
	    } catch (InterruptedException e) {
	    	e.printStackTrace();
	    }
	}
    }

    public void moveCommand() {
	double g = 0.0625;
	if( key_fire == 1 ) {
		fb[ff].fx = mx + 16;
		fb[ff].fy = my + 8;
		ff++;
		if(ff == FIRE_MAX) ff = 0;
		for(int i=0; i<OPT_MAX; i++){
			int t = gt % (16 * (i+1));
			fb[ff].fx = opt[i].ox[t] + 16;
			fb[ff].fy = opt[i].oy[t] + 8;
			ff++;
			if(ff == FIRE_MAX) ff = 0;
		}
		key_fire = 2;
	}
	for( int i=0; i<FIRE_MAX; i++ ) {
		fb[i].moveFireBall(sm,mx,my,0,key_fire,key_left,key_right);
		ef[i].moveEnemyFire(sm,mx,my);
	}
	fmove = false;
	if(key_left == 1) {
		mx = mx -4;
		if(mx < 0){ mx = 0; }
		fmove = true;
	} else if(key_right == 1) {
		mx = mx + 4;
		if(mx > 400-32){ mx = 400-32; }
		fmove = true;
	}
	if(key_up == 1) {
		my = my - 4;
		if(my < 16) { my = 16; }
		fmove = true;
	} else if(key_down == 1) {
		my = my + 4;
		if(my > 320-64){ my = 320-64; }
		fmove = true;
	}
	// 敵の移動
	for( int i = 0; i < ENEMY_MAX; i++){
		em[i].moveEnemy();
		if(em[i].isDisp() == false) {
			continue;
		}
		if(rnd.nextInt(BULLET_RATE) == 0) {
			ef[eff].setFire(mx,my,(int)em[i].ex,(int)em[i].ey,3);
			eff++;
			if(eff == FIRE_MAX) eff = 0;
		}
		for(int n=0; n<FIRE_MAX; n++){
			if(fb[n].checkCross((int)em[i].ex,(int)em[i].ey,32,32)){
				em[i].ex = -100;
				fb[n].fx = 1000;
			}
		}
	}
	// 戦車（カメ）の移動
	ta.moveTank(sm,sx);
	if(ta.stop ==0 && 32 < ta.tx && ta.tx < 400 && rnd.nextInt(TANK_BULLET) == 0) {
		ta.stop = 60;
	}
	if(ta.stop == 30) {
		ef[eff].setFire(mx,my,(int)ta.tx,(int)ta.ty,3);
		eff++;
		if(eff == FIRE_MAX) eff = 0;
	}
	for(int n=0; n<FIRE_MAX; n++){
		if(fb[n].checkCross((int)ta.tx,(int)ta.ty,32,32)){
			ta.tx = 1000;
			fb[n].fx = 1000;
		}
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
	// アニメーション係数
	int a = (sx % 36) / 18;
	// マップの描画	
	sm.drawMap(g2D,sx,this,panelWidth,panelHeight);
	// オプションの描画
	for(int i=0; i<OPT_MAX; i++){
		int t = gt % (16 * (i+1));
		dstX1=(opt[i].ox[t]) * panelWidth / 400;
		dstX2=dstX1 + 32 * panelWidth / 400;
		dstY1 = opt[i].oy[t] * panelHeight / 320;
		dstY2 = dstY1 + 32 * panelHeight / 320;
		buffCopy(g2D, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,6+a,3, true, false);
	}
	// 自機の描画
	dstX1=(mx) * panelWidth / 400;
	dstX2=dstX1 + 32 * panelWidth / 400;
	dstY1 = my * panelHeight / 320;
	dstY2 = dstY1 + 32 * panelHeight / 320;
	buffCopy(g2D, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,3,5, true, false);
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
		fireBall(g2D, fb[i].fx, (int)( fb[i].fy * panelHeight / 320), 0);
		fireBall(g2D, (int)ef[i].bx, (int)( ef[i].by * panelHeight / 320), sx / 3);
	}
	// 敵の描画
	for( int i = 0; i < ENEMY_MAX; i++){
		dstX1=(em[i].ex) * panelWidth / 400;
		dstX2=dstX1 + 32 * panelWidth / 400;
		dstY1 = em[i].ey * panelHeight / 320;
		dstY2 = dstY1 + 32 * panelHeight / 320;
		buffCopy(g2D, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,4+a,6,false, false);
	}
	// 戦車（カメ）の描画
	dstX1=(ta.tx) * panelWidth / 400;
	dstX2=dstX1 + 32 * panelWidth / 400;
	dstY1 = ta.ty * panelHeight / 320;
	dstY2 = dstY1 + 32 * panelHeight / 320;
	boolean td = true;
	int tank_anime = a;
	if(ta.stop > 0) {
		tank_anime = 0;
		if( mx < ta.tx) {
			td = false;
		}
	}
	buffCopy(g2D, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,0+tank_anime,6,td,false);
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
