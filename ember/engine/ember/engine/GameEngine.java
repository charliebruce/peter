package ember.engine;

import java.applet.Applet;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.IOException;
import ember.util.Logger;
import ember.client.Misc;
import ember.client.game.Location;
import ember.client.net.GameConnection;
import ember.engine.graphics.EmberGraphics;
import ember.engine.input.*;
import ember.engine.maths.Vector2f;

public abstract class GameEngine extends Applet implements Runnable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The output for the game - both are Components
	 */
	
	public Frame windowedFrame;
	public Applet applet;
	public GameEngine currentEngine;
	
	public boolean runningInFrame;
	public boolean runningAsApplet;
	
	public String loadText;
	
	public InputHandler ih;
	
	
	public GameEngine instance;
	private Thread theThread = null;
	
	public GameConnection conn;
	
	private WindowHandler wh;
	
	public void createFrame(int width, int height){

		wh = new WindowHandler();
		windowedFrame = new Frame();
		windowedFrame.setTitle("Ember");
		windowedFrame.setResizable(true);
		windowedFrame.addWindowListener(wh);
		windowedFrame.setVisible(true);
		windowedFrame.setLocation(100, 100);
		windowedFrame.toFront();
		Insets insets = windowedFrame.getInsets();
		windowedFrame.setSize((width + insets.right +  insets.left),
					    (height + insets.top + insets.bottom));
		
		//TODO check fullscreen, settings etc
	}

	public void runGame(){
		
		//Ignite!
		
		Logger l = Logger.getInstance();
		instance = this;
		l.info("Client starting");
		
		if(windowedFrame==null && applet != null){
			runningInFrame = false;
			runningAsApplet = true;
		}
		else {
			runningInFrame = true;
			runningAsApplet = false;
		}
		
		//Graphic acceleration settings.
		 try
	        {
	            System.setProperty("sun.java2d.noddraw", "true");
	            System.setProperty("sun.java2d.opengl", "true");
	        }
		 catch(Exception ex) {l.warning(ex.toString());}
		
		
		//Trigger execution of the core engine. 
		theThread = new Thread((Runnable) this);
		theThread.setDaemon(true);
		theThread.start();
		//thread.setPriority(10);
	
	}
	
	@Override
	public void run() {
		
		//Load up basic support code and subsystems
		//Timing infrastructure, native libs (FS?)
		//Check HW
		
		currentEngine = this;
		
		EmberGraphics.initialise();
		
		loadText = "Loading subsystems.";

		
		
		ProgressBarThread pbt = new ProgressBarThread();
		new Thread(pbt, "ProgressRender").start();
		
		
		//Now load

		
		loadText = "Loading input handler.";
		
		ih = new InputHandler(EmberGraphics.gameCanvas);
		ih.bind();
		
		
		//initFSs
		//FileSystem.Initialise();

		
		
		String test = "";
		
		Vector2f experiment = Misc.locationToScreen(new Location(8,8));
		test = "Test"+ experiment.vec[0] + "," + experiment.vec[1] + " Ok?" + experiment.getMagnitude();
		
		System.out.println(test);
		
		/*
		 * The following creates a Client GameConnection - sends ID to server. 
		 */
		loadText = "Establishing game connection - Simulating Login.";
		
		doLogon();

		loadText = "Logged in.";
		
	
		//Check hardware capabilities
		
		//Create a timer with best accuracy
		//(native nanotimer, milli or whatever)
		
		//Startup misc libs
		
		
		//Loaded - stop showing progress. 
		loadText = "Loaded.";
		pbt.abort = true;
		
		//postloadtasks in clients
		while(true){
			//The game's main loop
			updateLoop();
		}
	}
	
	long timeElapsed = 0;
	long lastDone = 0;
	
	@Override
	public void update(Graphics g){
		super.update(g);
		System.out.println("update");
	}
	public void updateLoop(){
		
		//Timer update and profiling perhaps?
			
		mainLogicLoop();//Client's code
		render(); //Ditto
		EmberGraphics.doFlip(); //Make visible.
		
		if (timeElapsed - lastDone > 100 ){//Crap but works.
			lastDone = timeElapsed;
		//TODO this into buffer not direct!!!
		try {//Heart-beat 
			conn.out.write(255);
			conn.out.write(0);
			
		} catch (IOException e) {
			// TODO handle heart attacks!
			e.printStackTrace();
			conn.status = Constants.ConnectionStatus.DISCONNECTED;
		}
		}
		
		
		//TODO this obvs. limits FPS. Do this better.

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timeElapsed += 10;
	}
	
	
	public void dieWithError(String message){
		Logger.getInstance().error("Unable to continue: " + message);
		//System.err.println("Unable to continue: " + message);
		System.exit(ERROR);
	}

	public final synchronized void paint(Graphics g){
		//TODO get borders only. 
		System.out.println("Engine painted.");
		EmberGraphics.doFlip(); //Make visible.
		
	}
	
	@Override
	public void setSize(int width, int height){
		Logger.getInstance().info("Engine setSize.");
		//super.setSize(width,height); 
		//validate();
	}

	/**
	 * Abstracts.
	 */
	public abstract void doLogon(); //Filled by client
	public abstract void render(); //Filled by client
	public abstract void mainLogicLoop(); //Filled by client
	public GameEngine(){}
}
