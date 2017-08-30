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

import java.awt.*;
import java.awt.event.*;

class FireBall {
	public FireBall() {
		fx = 400;
		fy = 0;
		ft = 0;
		ff = 3;
	}
	public void moveFireBall(int mx, int my, int md, int key_fire, int key_left, int key_right) {
		double g = 0.0625;
		ft++;
		fg++;
		fx = fx + fxv;
		fy = fy + ff - (int)(fg * fg * g);
		if( fy <= 8 ) {
			fy = 8;
			fg = 0;
		}
		if(key_fire == 1) {
			fy = my + 24;
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
    private int key_jump;
    private int key_fire;
    private int mx; // マリオの位置
    private int my; // マリオの位置
    private int md; // マリオの向き
    private int mw; // マリオの歩き
    private int ma; // マリオのアニメ
    private boolean fj; // ジャンプフラグ
    private int jxv;
    private FireBall fb;
    private boolean mario_move;
    private int peach_position[];
    private int peach_timer;
    public void setLeft(int a) {
        key_left = a;
    }
    public void setRight(int a) {
        key_right = a;
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
		double imageWidth = image.getWidth() - 16;
		double imageHeight = image.getHeight() - 16;
		int srcX1 = x * (int)(imageWidth / 8) + 8;
		int srcY1 = y * (int)(imageHeight / 7) + 8;
		int srcX2 = (x+1) * (int)(imageWidth / 8) + 3;
		int srcY2 = (y+1) * (int)(imageHeight / 7) + 3;
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
	double imageWidth = image.getWidth() - 16;
	double imageHeight = image.getHeight() - 16;
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


    public void gameStart() {
    	mario_move = false;
    	peach_timer = 0;
	peach_position = new int[240];
	fb = new FireBall();
	game_loop = new Thread(this);
	game_loop.start();
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
	fb.moveFireBall(mx,my,md,key_fire,key_left,key_right);
	mario_move = false;
	if(key_jump == 1 && fj == false) {
		jxv = 0;
		if(key_left == 1) {
			jxv=-4;
		} else if(key_right == 1) {
			jxv=4;
		}
		fj = true;
		ma = 3;
		mw = 0;
		my = 0;
	}
	if(fj) {
		mx = mx + jxv;
		my = my + 8 - (int)(mw * mw * g);
		if( my <= 0 ) {
			mw = 0;
			my = 0;
			ma = 2;
			fj = false;
		}
		mw++;
		mario_move = true;
		return;
	}
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

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        double panelWidth = this.getWidth();
        double panelHeight = this.getHeight();

	double dstX1 = 0;
	double dstY1 = 0;
	double dstX2 = 0;
	double dstY2 = 0;
	g.fillRect( (int)dstX1,(int)dstY1,(int)panelWidth,(int)panelHeight);
	// ピーチの描画
	int t = peach_timer % 16;
	if(peach_timer>=16) {
		boolean b = false;
		if(peach_position[5+t*6] == 1 ) { b = true; }
        	buffCopy(g2D, peach_position[0+t*6],peach_position[1+t*6],peach_position[2+t*6],peach_position[3+t*6],peach_position[4+t*6],1, b);
	}
	// マリオの描画
	dstX1=(mx) * panelWidth / 400;
	dstX2=dstX1 + (panelWidth / 8);
	dstY1 = (panelHeight / 7) * 2 - my * panelHeight / 320;
	dstY2 = dstY1 + (panelHeight / 7);
	boolean b = false;
	if(md == 1 ) { b = true; }
        buffCopy(g2D, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,ma,0, b);
	if(mario_move == true || peach_position[4+t*6] == 3) {
		peach_position[0 + t * 6] = (int)dstX1;
		peach_position[1 + t * 6] = (int)dstY1;
		peach_position[2 + t * 6] = (int)dstX2;
		peach_position[3 + t * 6] = (int)dstY2;
		peach_position[4 + t * 6] = (int)ma;
		peach_position[5 + t * 6] = (int)md;
		peach_timer++;
	}
	dstX1=dstX2=0;
	dstY1 = (panelHeight / 7) * 3;
	for( int y=3; y<7; y++){
	    for( int x=0; x<8; x++){
		dstX2 += (panelWidth / 8);
		dstY2 = dstY1 + (panelHeight / 7);
		// スケーリング
		buffCopy(g2D, (int)dstX1,(int)dstY1,(int)dstX2,(int)dstY2,x,y,false);
		dstX1 = dstX2;
	    }
	    dstX1 = dstX2 = 0;
	    dstY1 = dstY2;
	}
	// ファイアボールの描画
	fireBall(g2D, fb.fx, (int)( -fb.fy * panelHeight / 320 + (panelHeight / 7) * 3), fb.ft);
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
