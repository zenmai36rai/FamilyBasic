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
		map = new int[] {
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,2,1,1,1,1,1,1,1,3,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		1,1,1,1,1,1,1,3,0,0,0,0,0,0,0,0,0,2,1,1,1,1,1,1,1,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,
		0,0,0,0,0,0,0,0,2,1,1,1,1,1,1,1,3,0,0,0,0,0,4,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,
		1,1,1,1,1,3,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1,1,1,1,1,
		1,1,1,1,1,1,1,3,0,0,0,0,0,0,0,0,0,2,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,3,0,0,0,0,0,2,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		0 };
		chip = new int[] {
		0,0,	// 0:ダミー
		0,5,	// 1:プロック
		1,0,	// 2:左ブロック
		0,0,	// 3:右ブロック
		2,7,	// 4:ハシゴ
		0 };
	}
	public int getChipX(int a) {
		return chip[a*2];
	}
	public int getChipY(int a) {
		return chip[a*2+1];
	}
	public void drawMap(Graphics g, JPanel jp, double panelWidth, double panelHeight) {
		Graphics2D g2D = (Graphics2D)g;
		double imageWidth = 400;
		double imageHeight = 320;
		g.setColor(Color.black);
		g.fillRect( (int)0,(int)0,(int)panelWidth,(int)panelHeight);
		for(int y=0;y<20;y++){
			for(int x=0;x<25;x++){
				if(map[y*25+x] != 0){
					int a = map[y*25+x];
					int dstX1 = (int)( x * 16 * panelWidth / imageWidth);
					int dstY1 = (int)( y * 16 * panelHeight / imageHeight);
					int dstX2 = (int)( (x+1) * 16 * panelWidth / imageWidth);
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
	// 床の上にいるか
	public int onFloor(int mx, int my) {
		for(int y=0;y<20;y++){
			for(int x=0;x<25;x++){
				int a = map[y*25+x];
				// マップチップがブロックなら交差判定
				if(a == 1 || a == 2 || a == 3){
					int fx = x * 16;
					int fy = 16 * 8 - y * 16;
					if( fx - 16 <= mx && mx <= fx + 16 ) {
						if ( fy - 32 <= my && my <= fy ) {
							return fy;
						}
					}
				}
			}
		}
		return -1;
	}
	// ハシゴに昇れるか
	public boolean onLadder(int mx, int my) {
		for(int y=0;y<20;y++){
			for(int x=0;x<25;x++){
				int a = map[y*25+x];
				// マップチップがハシゴなら交差判定
				if(a == 4){
					int fx = x * 16;
					int fy = 16 * 8 - y * 16;
					if( fx - 16 <= mx && mx <= fx + 8 ) {
						if ( fy -16 <= my && my <= fy + 16 ) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	private BufferedImage bg;
	public int map[];
	public int chip[];
}
class FireBall {
	public FireBall() {
		fx = 1000;
		fy = 0;
		ft = 0;
		ff = 3;
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
		double g = 0.0625;
		ft++;
		fg++;
		fx = fx + fxv;
		fy = fy + ff - (int)(fg * fg * g);
		int fv = ff - (int)(fg * fg * g);
		int h = sm.onFloor(fx,fy);
		if( (fv <= 0) && (h != -1) ) {
			fy = h;
			fg = 0;
		}
		if(key_fire == 1) {
			fy = my + 16;
			fg = 0;
			ft = 0;
			if ( md == 0 ) {
				fx = mx;
				fxv = -4;
			}
			if ( md == 1 ) {
				fx = mx + 24;
				fxv = 4;
			}
			if(key_left == 1) {
				fxv=-6;
			} else if(key_right == 1) {
				fxv=6;
			}
		}
	}

	public int fx;
	public int fy;
	public int ft;
	public int ff;
	public int fg;
	public int fxv;
}
class FBImage extends JPanel implements Runnable {
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
    private int mx; // マリオの位置
    private int my; // マリオの位置
    private int md; // マリオの向き
    private int mw; // マリオの歩き
    private int ma; // マリオのアニメ
    private boolean fj; // ジャンプフラグ
    private int jxv;
    private int jyv;
    private FireBall fb;
    private boolean mario_move;
    private int peach_position[];
    private int peach_timer;
    int kx; // 亀
    int ky;
    int kd;
    int kw;
    int kf;
    int cx; // カニ
    int cy;
    int cd;
    int cw;
    int cf;

    public void gameStart() {
	mario_move = false;
	peach_timer = 0;
	peach_position = new int[240];
	kx = 400 - 32;
	cy = -8 *16;
	fb = new FireBall();
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
    	if(key_jump == 0 && a == 1)
        	key_jump = 1;
	if(key_jump == 1 && a == 0)
		key_jump = 0;
    }
    public void setFire(int a) {
    	if(key_fire == 0 && a == 1)
        	key_fire = 1;
	if(key_fire == 1 && a == 0)
		key_fire = 0;
    }

    public FBImage(String path) {
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
	fb.moveFireBall(sm,mx,my,md,key_fire,key_left,key_right);
	mario_move = false;
	if(key_jump == 1 && fj == false) {
		jxv = 0;
		if(key_left == 1) {
			jxv=-4;
		} else if(key_right == 1) {
			jxv=4;
		}
		jyv = 9;
		fj = true;
		ma = 3;
		mw = 0;
	}
	// 床から落ちる処理
	if(sm.onFloor(mx,my) == -1 && fj == false && ma != 5) {
		jxv = 4;
		if(key_left == 1) {
			jxv=-4;
		}
		jyv = 0;
		fj = true;
		ma = 3;
		mw = 0;
	}
	// 空中にいる時
	if(fj) {
		mx = mx + jxv;
		if(mx < 0) mx = 400;
		if(mx > 400) mx = 0;
		my = my + jyv - (int)(mw * mw * g);
		int jv = jyv - (int)(mw * mw * g);
		int h = sm.onFloor(mx,my);
		if( jv <= 0 && h != -1 ) {
			mw = 0;
			my = h;
			ma = 2;
			fj = false;
		}
		mw++;
		mario_move = true;
	} else {
		if(key_left == 1) {
			mx = mx - 4;
			md = 0;
			mw++;
			if(mw == 12 ) { mw = 0; }
			ma = mw / 4;
			if(ma == 3){ ma = 1; }
			mario_move = true;
		} else if(key_right == 1) {
			mx =mx + 4;
			md = 1;
			mw++;
			if(mw == 12 ) { mw = 0; }
			ma = mw / 4;
			if(ma == 3){ ma = 1; }
			mario_move = true;
		}
	}
	if(mx < 0) mx = 400;
	if(mx > 400) mx = 0;
	// はしごを昇る処理
	if(key_up == 1) {
		if(sm.onLadder(mx,my + 1) == true) {
			my = my + 2;
			mw++;
			if(mw == 12) { mw = 0; }
			md = mw / 6;
			ma = 5;
			mario_move = true;
		}
	}
	if(key_down == 1) {
		if(sm.onLadder(mx,my - 1) == true) {
			my = my - 2;
			mw++;
			if(mw == 12) { mw = 0; }
			md = mw / 6;
			ma = 5;
			mario_move = true;
		}
	}
	// 亀の移動
	if(kf == 0 && fb.checkCross(kx, ky)) {
		kf = 1;
		kw = 0;
	} 
	if(kf == 1){
		kw++;
		if(kw > 120) {
			kf = 0;
			kw = 0;
		}
	} else {
		if(kd == 0) {
			if(sm.onFloor(kx-1,ky) == -1 ) {
				kd = 1;
			} else {
				kx = kx - 1;
			}
		} else {
			if(sm.onFloor(kx+1,ky) == -1 ) {
				kd = 0;
			} else {
				kx = kx + 1;
			}
		}
		kw++;
		if(kw == 16 ) { kw = 0; }
	}
	if(cf == 0 && fb.checkCross(cx, cy)) {
		cf = 1;
		cw = 0;
	} 
	// カニの移動
	if(cf == 1){
		cw++;
		if(cw > 120) {
			cf = 0;
			cw = 0;
		}
	} else {
		if(cd == 0) {
			if(sm.onFloor(cx-1,cy) == -1 ) {
				cd = 1;
			} else {
				cx = cx - 2;
			}
		} else {
			if(sm.onFloor(cx+1,cy) == -1 ) {
				cd = 0;
			} else {
				cx = cx + 2;
			}
		}
		cw++;
		if(cw == 8 ) { cw = 0; }
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
	// ピーチの描画
	int t = peach_timer % 16;
	if(peach_timer>=16) {
		int px = peach_position[0+t*4];
		int py = peach_position[1+t*4];
		dstX1=(px) * panelWidth / 400;
		dstX2=dstX1 + 32 * panelWidth / 400;
		dstY1 = (16 * 6 -py) * panelHeight / 320;
		dstY2 = dstY1 + 32 * panelHeight / 320;
		boolean b = false;
		if(peach_position[3+t*4] == 1 ) { b = true; }
		buffCopy(g2D, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,peach_position[2+t*4],1, b);
	}
	// マリオの描画
	dstX1=(mx) * panelWidth / 400;
	dstX2=dstX1 + 32 * panelWidth / 400;
	dstY1 = (16 * 6 -my) * panelHeight / 320;
	dstY2 = dstY1 + 32 * panelHeight / 320;
	boolean b = false;
	if(md == 1 ) { b = true; }
	buffCopy(g2D, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,ma,0, b);
	if(mario_move == true || peach_position[2+t*4] == 3) {
		peach_position[0 + t * 4] = (int)mx;
		peach_position[1 + t * 4] = (int)my;
		peach_position[2 + t * 4] = (int)ma;
		peach_position[3 + t * 4] = (int)md;
		peach_timer++;
	}
	// ファイアボールの描画
	fireBall(g2D, fb.fx, (int)( ( 16 * 7 -fb.fy ) * panelHeight / 320), fb.ft);
	// 亀の描画
	dstX1=(kx) * panelWidth / 400;
	dstX2=dstX1 + 32 * panelWidth / 400;
	dstY1 = (16 * 6 -ky) * panelHeight / 320;
	dstY2 = dstY1 + 32 * panelHeight / 320;
	b = false;
	if(kd == 1 ) { b = true; }
	if ( kf == 0 ) {
		buffCopy(g2D, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,kw/8,6, b);
	} else {
		int fa = 6 + (kw % 16) / 8;
		buffCopy(g2D, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,fa,3, b);
	}
	// カニの描画
	dstX1=(cx) * panelWidth / 400;
	dstX2=dstX1 + 32 * panelWidth / 400;
	dstY1 = (16 * 6 -cy) * panelHeight / 320;
	dstY2 = dstY1 + 32 * panelHeight / 320;
	b = false;
	if(cd == 1 ) { b = true; }
	if ( cf == 0 ) {
		buffCopy(g2D, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,2+(cw/4),6, b);
	} else {
		int fa = 6 + (cw % 16) / 8;
		buffCopy(g2D, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,fa,3, b);
	}
    }
};

class FamilyBasic extends JFrame{
    private FBImage FBI;
    public static void main(String args[]){
	FamilyBasic frame = new FamilyBasic();
	frame.WindowEventSample();
	frame.Key1();
	frame.setVisible(true);
    }

    FamilyBasic(){
	setTitle("FamilyBasic");
	setBounds(100, 100, 400, 320);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	Container contentPane = getContentPane();
	FBI = new FBImage("Fcbobj.png");
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
