package ember.engine.graphics.test;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class G2DTest {

	static Frame f;
	static Canvas c;
	static BufferStrategy bs;
	
	static int countx, county;
	
	public static void main(String[] args){
		
		countx = 0;
		county = 0;
		setup();
		
		while (true){
			draw();
			update();
			
			countx++;
			county++;
			
			if (countx > 800){
				countx = 0;
			}
			if (county > 600){
				county = 0;
			}
		}
		
	}
	
	public static void setup(){
		f = new Frame();
		f.setSize(800,600);
		c = new Canvas();
		c.setSize(800,600);
		f.add(c);
		f.setVisible(true);
		
		c.createBufferStrategy(2);
		bs = c.getBufferStrategy();
		
		
	}
	
	public static void draw(){
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.green);
		g.fillRect(0,0,800,600);
		g.setColor(Color.gray);
		g.fillRect(countx,county, 50,50);
		
	}
	
	public static void update(){
	
		bs.show();
	}
}
