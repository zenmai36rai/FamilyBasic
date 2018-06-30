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

import java.lang.Math;

class DrawPanel extends JPanel implements Runnable {

    public void gameStart() {
	game_loop = new Thread(this);
	game_loop.start();
    }

    private Thread game_loop;
    private int t;
    public void run() {
        t = 0;
	while( true ) {
            try {
                Thread.sleep(10);
            } catch(InterruptedException e){
                e.printStackTrace();
            }
	    t++;
	    this.repaint();
	}
    }

    @Override
    public void paintComponent(Graphics g) {
	Graphics2D g2D = (Graphics2D) g;
        g.setColor(Color.black);
	double panelWidth = this.getWidth();
	double panelHeight = this.getHeight();
        int ymin[];
	int ymax[];
	int px;
        int py;
        int y;
	ymin = new int[640];
	ymax = new int[640];
        double Rd = 3.14159 / 180;
        for(int i = 0; i<= 639; i++){
            ymin[i] = 399; ymax[i] = 0;
        }
        for( int z = 200; z > -200; z -= 10){
            for( int x = -200; x < 200; x++ ){
	        int buff = x * x + z * z;
		int h = 5 - (t % 30) / 10;
                y = (int)(30 * (Math.cos(Math.sqrt(buff) * Rd) + Math.cos(h * Math.sqrt(buff) * Rd)));
                px = (int)(320 + x * Math.cos(-30 * Rd) + z * Math.sin(-30 * Rd));
                py = (int)(200 - (y * Math.cos(30 * Rd) - (-x * Math.sin(-30 * Rd) + z * Math.cos(-30 * Rd)) * Math.sin(30 * Rd)));
                if( py < ymin[px] ){
                    ymin[px] = py;
                    g.drawLine(px, py, px + 1, py + 1);
		    //g.fillRect( (int)0,(int)0,(int)panelWidth,(int)panelHeight);
                }
                if( py > ymax[px] ) {
                    ymax[px] = py;
                    g.drawLine(px, py, px + 1, py + 1);
		}
            }
        }
    }
};

class Dimension extends JFrame{
    private DrawPanel FBI;
    public static void main(String args[]){
	Dimension frame = new Dimension();
	frame.setVisible(true);
    }

    Dimension(){
	setTitle("Dimension");
	setBounds(100, 100, 640, 480);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	Container contentPane = getContentPane();
	FBI = new DrawPanel();
	contentPane.add(FBI);
	
	FBI.gameStart();
    }

};
