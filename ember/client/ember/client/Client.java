package ember.client;


import java.applet.Applet;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;

import ember.client.game.Game;
import ember.client.game.Location;
import ember.client.game.ScreenSpaceBox;
import ember.client.game.entity.Entity;
import ember.client.net.GameConnection;
import ember.client.protocol.LoginBlock;
import ember.engine.GameEngine;
import ember.engine.graphics.EmberGraphics;
import ember.engine.input.InputHandler;

/**
 * The ember prototype game client. 
 * @author charlie
 *
 */
public class Client extends GameEngine {

	/**
	 * Client version
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Client type - used to disable features and settings.
	 */
	public static int type = 3;//1 = live, 2 = rc, 3 = wip
	
	/**
	 * The instance of the client in action. 
	 */
	public static Client instance;

	/**
	 * The current condition of the game - where we're at...
	 */
	public int state;
	
	/**
	 * The game (if any) in which the player is participating.
	 * TODO differently!
	 */
	public Game game;
	
	/**
	 * Set-up and initialisation.
	 */
	
	/**
	 * When run as an applet (browser) this code handles start-up and parameters.
	 */
	public void init(){
		
		instance = this;
		instance.applet = (Applet) this;
		
		//in engine
		instance.runGame();
	}
	
	/**
	 * When run as an application (local) this code handles start-up. Arguments passed by command-line.
	 * @param args
	 */
	public static void main(String[] args){
		
		//TODO read settings into static variables...
		
		instance = new Client(); //Client extends engine
		instance.createFrame(800, 600);
		
		//Hand over to the engine.
		instance.runGame();
	}
	
	
	
	/**
	 * The main logic and render code is below.
	 */
	
	
	
	/**
	 * After engine load-up has completed, this loop begins, and then renders.
	 * Here, the overall state is updated.
	 */
	public final void mainLogicLoop() {
		
	switch (state) {
		case 0: {
			//Load-up just finished. This would normally go to login. For now to lobbby. 
			state = ClientConstants.States.LOBBY;
			
			//DO NOT break;? For convenience - so that no rendering of state 0 takes place.
			
		}
		
		case ClientConstants.States.LOBBY: {
			//Process lobby

			break;
		}
		
		case ClientConstants.States.LOADINGGAME: {
			//Check I have the required resources
			//Load them
			//Keep the server in the loop.
			break;
		}
		
		
		case ClientConstants.States.INGAME: {
			gameLogicLoop();
			break;
		}
		
	}
	
	
	if (state > 10) {
		if (conn.status != 1){
			dieWithError("Oh shit. Connection fucked up.");
			
		}
		//If a connection is open then check it. 
	}
	
	
		//This block of code is cycled continuously with the engine handling timing. 
		//Asynchronous tasks are not included but can be referenced. 
		//TODO big loop to handle shit...
		
		//TODO handle input, net events, movement etc
		
	}

	
	/**
	 * The game's actual logic is handled by this code.
	 */
	public final void gameLogicLoop(){
		
		//Sync
		//Movement
		//Win/lose/statechanges
		//Players
		//Chat
		//Warnings/sounds
		//Interfaces
		//Unit selections?/for render group?
		
		//Note that lowlevel input isnt necessarily done here...
		
	}
	
	private long gameFrames = 0;
	
	/**
	 * Once the world update is complete, render it. 
	 * TODO render on different cycle than update?
	 */

	public final void render(){
		gameFrames++;
		long starttime=0;
		
		if (gameFrames < 2){
			starttime = System.currentTimeMillis();
		}
		
		//Placeholder
		Graphics2D g = (Graphics2D) EmberGraphics.getTarget();
		g.setColor(Color.GREEN);
		
		g.fillRect(0, 0, EmberGraphics.gameCanvas.getWidth(), EmberGraphics.gameCanvas.getHeight());
		g.setColor(Color.gray);
		//InputHandler ih = instance.currentEngine.ih;
		g.fillOval(ih.mouseLocation.x, ih.mouseLocation.y, 200,200);
		if (!(Client.getInstance().conn == null)){
			long timeelapsed = System.currentTimeMillis()-starttime;
			g.drawString("Ember Client Test: Frame "+gameFrames + " - State: "+ state + " - GameConnection is " + Client.getInstance().conn.status + " timeing: "+(gameFrames/((timeelapsed/1000)+0.001))  +" elapsed is "+ timeelapsed, 10, 10);
		}
		
		g.fillRect(400,400,10,10);
		
		//End place-holder.
		
		switch (state) {
		
		case ClientConstants.States.LOBBY: {
			//Render game list, chat, etc
			g.drawString("Game lobby", 10, 50);
			g.setColor(Color.red);
			
			Entity e = new Entity();
			e.setSprite(EmberGraphics.currentToolkit.createSprite(10,30));
			e.position = new Location(15,15);
			ScreenSpaceBox test = Misc.getForEntity(e);
			g.fillRect(test.x, test.y, test.w, test.h);
			
			break;
		}
		case ClientConstants.States.LOADINGGAME:{
			g.drawString("Game loading", 10, 50);
			//Display progress bars.
			//Display background image.
			break;
		}
		
		case ClientConstants.States.INGAME:{
			renderGame();
			break;
		}
		
		case ClientConstants.States.PROPOSEDGAME:{
			g.drawString("In proposed game selection", 10, 50);
			//Player list, chat, settings etc
			break;
		}
		
		
		}
		

	}
	
	
	public final void renderGame(){
		//TODO this
		
		//Compose viewable entities
		//Compose list of effects visible
		//Calculate viewable terrain
		//Iterate through terrain, units, effects
		//Lighting pass (HDR)?
		
		//HUD
		//Overlay
		
		
	}
	

	
	/**
	 * Render a simple graphic to show loading progress. Currently just centered text.
	 * @param g2
	 */
	public void renderProgressBar(Graphics g2){

		Graphics2D g = (Graphics2D) g2;
		
		//Blackout the screen. This could be an image too, if so desired.
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, EmberGraphics.gameCanvas.getWidth(), EmberGraphics.gameCanvas.getHeight());
		
		//Overlay the text.
		g.setColor(Color.RED);
		FontMetrics fm = g.getFontMetrics();
		int w = fm.stringWidth(loadText);
		g.drawString(loadText, (EmberGraphics.gameCanvas.getSize().width/2)-(w/2), 50);
		
	}
	
	
	public void doLogon(){
		

		try {
			
			conn = GameConnection.connect("localhost",11235);
		} catch (Exception e) {
			// TODO Don't just die, retry!
			e.printStackTrace();
			dieWithError("Could not connect to server. Returned "+e.toString());
		}
		int returnCode = conn.returnCode;
		if (returnCode != 1){
			dieWithError("Test connection returned bad result.");
		}
		
		System.out.println("Game connection established.");
		loadText = "Game connection established. Logging on...";
		
		
		/*
		 * Send a packet, ID 1, containing the encrypted login block.
		 */
		try {
			
			//Send the packet. 
			//TODO this via stream and buffer not direct writes.
			LoginBlock lb = new LoginBlock("charlie","testpass");
			conn.out.write(1);
			conn.out.write(lb.data.length);
			conn.out.write(lb.data);
			lb = null;
			
		} catch (IOException e) {
			// General failures in the proceedings.
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * South of here be dragons...
	 * @return
	 */
	
	public static long getVersion(){
		return serialVersionUID;
	}
	
	public Client() {
		instance = this;
	}

	public static Client getInstance(){
		return instance;
	}
}
