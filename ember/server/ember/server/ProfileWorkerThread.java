package ember.server;
import ember.engine.Constants;
import ember.server.exceptions.BannedException;
import ember.server.game.*;
import ember.server.io.*;
import ember.server.lobby.Lobby;
import ember.util.*;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This runs continually and load/save events are fed to it. 
 * By moving loading and saving to this thread everything is disconnected
 * Hence high reliability can be ensured.
 */
public class ProfileWorkerThread implements Runnable {

	/**
	 * Logger instance.
	 */
	private Logger logger = Logger.getInstance();

	/**
	 * Constructor.
	 *
	 * @param loader
	 */
	public ProfileWorkerThread(ProfileLoader loader, ProfileSaver saver) {
		this.loader = loader;
		this.saver = saver;
		this.profilesToLoad = new LinkedList<ProfileDetails>();
		this.profilesToSave = new LinkedList<Profile>();
	}

	/**
	 * Players to load.
	 */
	private Queue<ProfileDetails> profilesToLoad;

	/**
	 * Players to save.
	 */
	private Queue<Profile> profilesToSave;

	/**
	 * The player loader and saver.
	 */
	private ProfileLoader loader;
	private ProfileSaver saver;
	
	private boolean isRunning;
	
	@Override
	public void run() {
		isRunning = true;
		while (isRunning) {
			try {//TODO better timing accounting for processing time. 
				Thread.sleep(ServerConstants.PROFILE_WORKER_TICK);
			} catch (InterruptedException e) {
				cleanup();
				break;
			}
			doLoop();
		}
	}
	private void doLoop(){
		synchronized (profilesToLoad) {
			if (!profilesToLoad.isEmpty()) {
				ProfileDetails d;
				while ((d = profilesToLoad.poll()) != null) {
					
					Profile r = null;
					int returnCode = Constants.LoginCodes.LOGIN_OK;
					
					/*
					 * Set the ProfileLoader loading it.
					 */
					try {
						r = loader.load(d);
					}
					catch(InvalidDetailsException e) {
						//If the credentials are shit.
						returnCode = Constants.LoginCodes.INVALID_CREDENTIALS;
					}
					catch(BannedException e){
						//Or the person is... undesirable.
						returnCode = Constants.LoginCodes.BANNED;
					}


					//TODO check if already logged on
					
					/*
					 * If the credentials are OK, see if there's a slot.
					 */
					int slot = -1;
					
					if (returnCode == Constants.LoginCodes.LOGIN_OK){
						
						r.setConnection(d.getConnection());
						if (!Lobby.register(r)) {
							//We have no more rooms, Mary. 
							returnCode = Constants.LoginCodes.NO_MORE_SLOTS;
						}
					}

					/*
					 * 
					 * TODO return shit. 
					 * 
					 * LoginHandshakeBuilder loginReturn = new LoginHandshakeBuilder();
					loginReturn.addByte((byte) returnCode);
					if (returnCode == Constants.LoginCodes.LOGIN_OK) 
					{
						//TODO config bits.
						loginReturn.addByte((byte) 0);//r.getRights());
						loginReturn.addByte((byte) 0);
						loginReturn.addByte((byte) 0);
						loginReturn.addByte((byte) 0);
						loginReturn.addByte((byte) 1);
						loginReturn.addByte((byte) 0);
						loginReturn.addByte((byte) 0);
						loginReturn.addShort(slot);
						loginReturn.addByte((byte) 1);
						loginReturn.addByte((byte) 1); //members
						
					}*/
				/*	WriteFuture f = d.getSession().write(spb.toPacket());
					if (r.returnCode != 2) {
						f.addListener(new IoFutureListener() {
							@Override
							public void operationComplete(IoFuture arg0) {
								arg0.getSession().close();
							}
						});
					} else {
						r.player.setOnLogin(true);
						r.player.getActionSender().sendLogin();
					}*/
					
					//d.getConnection().writePacket(loginReturn.toPacket());
					logger.info("Loaded " + d.getUsername() + "'s profile.");
					//profilesToLoad.remove(d);
				}
				profilesToLoad.remove(d);
			}
		}
		synchronized (profilesToSave) {
			if (!profilesToSave.isEmpty()) {
				Profile p;
				while ((p = profilesToSave.poll()) != null) {
					if (saver.save(p)) {
						logger.debug("Saved " + p.getUsername() + "'s profile.");
					} else {
						logger.warning("Could not save " + p.getUsername() + "'s profile.");
					}
				}
				profilesToSave.remove(p);
			}
		}
	}

	private void cleanup() {
		// saving all games
		logger.info("Saving all profiles...TODO");
		/*int saved = 0;
		int total = 0;
		for (Player p : World.getInstance().getPlayerList()) {
			total++;
			if (loader.save(p)) {
				logger.info("Saved " + p.getPlayerDetails().getUsername() + "'s game.");
				saved++;
			} else {
				logger.info("Could not save " + p.getPlayerDetails().getUsername() + "'s game.");
			}
		}
		if (total == 0) {
			logger.info("No games to save.");
		} else {
			logger.info("Saved " + (saved / total * 100) + "% of games (" + saved + "/" + total + ").");
		}*/
	}

	public void loadProfile(ProfileDetails d) {
		synchronized(profilesToLoad) {
			profilesToLoad.add(d);
		}
	}

	public void saveProfile(Profile p) {
		synchronized(profilesToSave) {
			profilesToSave.add(p);
		}
	}

	public void quit() {
		isRunning = false;
		doLoop();
		cleanup();
		
	}

}
