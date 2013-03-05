package perlin;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

import javax.imageio.ImageIO;

public class Perlinesque {

	public static void genPerlin(int size) throws IOException{
		//Generate a perlin noise texture
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		img.createGraphics();
		//Graphics2D gr = (Graphics2D)img.getGraphics();
		
		int[][]highr = genNoise(size,size/4);//"blurred" static
		int[][]medr = genNoise(size,size/2);
		int[][]lowr = genNoise(size,size);//pure static
		int[][]highg = genNoise(size,size/4);//"blurred" static
		int[][]medg = genNoise(size,size/2);
		int[][]lowg = genNoise(size,size);//pure static
		int[][]highb = genNoise(size,size/4);//"blurred" static
		int[][]medb = genNoise(size,size/2);
		int[][]lowb = genNoise(size,size);//pure static
		int[][]higha = genNoise(size,size/4);//"blurred" static
		int[][]meda = genNoise(size,size/2);
		int[][]lowa = genNoise(size,size);//pure static
		
		int r=128;
		int g =128;
		int b = 128;
		int a = 128;
		for(int x=0; x<size;x++){
			for(int y=0;y<size;y++){
				r=Math.max(0, Math.min(128+(highr[x][y]-128)/4+(medr[x][y]-128)/2+(lowr[x][y]-128),255));
				g=Math.max(0, Math.min(128+(highg[x][y]-128)/4+(medg[x][y]-128)/2+(lowg[x][y]-128),255));
				b=Math.max(0, Math.min(128+(highb[x][y]-128)/4+(medb[x][y]-128)/2+(lowb[x][y]-128),255));
				a=Math.max(0, Math.min(128+(higha[x][y]-128)/4+(meda[x][y]-128)/2+(lowa[x][y]-128),255));
				//gr.setColor(new Color(r,g,b));
				int clr = r+(g<<8)+(b<<16)+(a<<24);
				img.setRGB(x, y, clr);
				//gr.drawRect(x,y,1,1);
			}
		}

		ImageIO.write(img, "png", new File("data/textures/perlin1krgba.png"));
		
	}
	
	private static int[][] genNoise(int size, int resolution) {
		int[][] proc = new int[size][size];
		int[][] control = new int[resolution][resolution];
		int[][] out = new int[size][size];
		
		int sf = size/resolution;
		
		for(int x=0; x<resolution;x++){
			for(int y=0;y<resolution;y++){
				control[x][y]=r.nextInt(255);
			}
		}
		
		for(int x=0; x<size;x++){
			for(int y=0;y<size;y++){
				proc[x][y]=control[x/sf][y/sf];
			}
		}
		
		//blur
		int rtotal;
		int ady,adx;
		for(int x=0; x<size;x++){
			for(int y=0;y<size;y++){
				rtotal=0;
				for(int dx=-2;dx<2;dx++){
					for(int dy=-2;dy<2;dy++){
						adx=dx;ady=dy;
						if(x+dx<0||x+dx>=size){adx=0;}
						if(y+dy<0||y+dy>=size){ady=0;}
						rtotal=rtotal+(proc[x+adx][y+ady]/16);
					}	
				}
				out[x][y]=rtotal;
			}
		}
		
		
		return out;
	}

	static SecureRandom r;
	
	public static void main(String[] args){
		r=new SecureRandom();
		r.setSeed(System.nanoTime());
		
		try {
			genPerlin(1024);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done");
	}
}
