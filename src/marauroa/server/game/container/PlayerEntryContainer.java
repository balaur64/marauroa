package marauroa.server.game.container;

import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import marauroa.common.Log4J;
import marauroa.common.crypto.RSAKey;
import marauroa.common.game.RPObject;
import marauroa.common.game.RPObject.ID;
import marauroa.server.RWLock;
import marauroa.server.game.db.IPlayerDatabase;
import marauroa.server.game.db.PlayerDatabaseFactory;
import marauroa.server.game.db.Transaction;

/**
 * This is a helper class to sort and access PlayerEntry in a controlled way.
 * This class implements the singleton pattern.
 * @author miguel
 *
 */
public class PlayerEntryContainer implements Iterable<PlayerEntry> {
	/** the logger instance. */
	private static final Logger logger = Log4J.getLogger(PlayerEntryContainer.class);

	/** A reader/writers lock for controlling the access */
	private RWLock lock;

	/** A random number generator instance. */
	private Random rand;
	
	/** This map store player entry for fast access using clientid */
	Map<Integer, PlayerEntry> clientidMap;
	
	private static PlayerEntryContainer playerEntryContainer;

	/** Constructor */
	private PlayerEntryContainer() throws Exception {
		/* Initialize the random number generator */
		rand = new Random();
		rand.setSeed(new Date().getTime());
		
		lock = new RWLock();
		
		/* We initialize the list that will help us sort the player entries. */
		clientidMap = new HashMap<Integer, PlayerEntry>();
		
		/* Choose the database type using configuration file */
		try {
			PlayerEntry.initDatabase();
		} catch (Exception e) {
			logger.error("ABORT: marauroad can't allocate database");
			throw e;
		}
	}

	/**
	 * This method returns an instance of PlayerEntryContainer
	 * 
	 * @return A shared instance of PlayerEntryContainer
	 */
	public static PlayerEntryContainer getContainer() throws Exception {
		if (playerEntryContainer == null) {
			playerEntryContainer = new PlayerEntryContainer();
		}
		return playerEntryContainer;
	}
	
	/**
	 * This method returns an iterator over tha available player entry objects.
	 * @return the iterator
	 */	
	public Iterator<PlayerEntry> iterator() {
		return clientidMap.values().iterator();
	}

	/**
	 * This method returns the lock so that you can control how the resource is
	 * used
	 * 
	 * @return the RWLock of the object
	 */
	public RWLock getLock() {
		return lock;
	}
	
	/** 
	 * This method returns the size of the container.
	 * @return Container's size.
	 */
	public int size() {
		return clientidMap.size();
	}
	
	/** 
	 * This method returns true if there is any PlayerEntry which has client id as clientid.
	 * @param clientid the id of the PlayerEntry we are looking for
	 * @return true if it is found or false otherwise.
	 */
	public boolean has(int clientid) {
		return clientidMap.containsKey(clientid);
	}
	
	/**
	 * This method returns the PlayerEntry whose client id is clientid or null otherwise.
	 * @param clientid the id of the PlayerEntry we are looking for
	 * @return the PlayerEntry if is it found or null otherwise
	 */
	public PlayerEntry get(int clientid) {
		return clientidMap.get(clientid);
	}

	/**
	 * This method returns the entry that has been associated with this SocketChannel,
	 * or null if it does not exists.
	 * @param channel the socket channel to check
	 * @return the PlayerEntry or null if it is not found.
	 */
	public PlayerEntry get(SocketChannel channel) {
		for(PlayerEntry entry: clientidMap.values()) {
			if(entry.channel==channel) {
				return entry;
			}
		}
		
		return null;
	}

	/**
	 * This method returns the entry that has been associated to this player or null
	 * if it does not exists. 
	 * @param username the username to look for
	 * @return the PlayerEntry or null if it is not found
	 */
	public PlayerEntry get(String username) {
		for(PlayerEntry entry: clientidMap.values()) {
			if(entry.username.equals(username)) {
				return entry;
			}
		}
		
		return null;
	}
	
	/**
	 * This method returns the entry that has been associated to this player or null
	 * if it does not exists. 
	 * @param id the RPObject.ID we have to look for
	 * @return the PlayerEntry or null if it is not found
	 */
	public PlayerEntry get(RPObject.ID id) {
		for(PlayerEntry entry: clientidMap.values()) {
			if(entry.object.getID().equals(id)) {
				return entry;
			}
		}
		
		return null;
	}
		/** 
	 * This method removed a player entry from the container and return it or null if the entry
	 * does not exist.
	 * @param clientid the clientid we want its Player entry to remove.
	 * @return the player entry or null if it has not been found.
	 */
	public PlayerEntry remove(int clientid) {
		return clientidMap.remove(clientid);
		
	}

	/** Add a new Player entry to the container.
	 *  This method assigns automatically a random clientid to this player entry.
	 *  
	 * @param key the RSA Key used at server
	 * @param hash the hash send from client
	 * @param socketChannel the socket channel associated with the client
	 * @return client id resulting
	 */
	public PlayerEntry add(RSAKey key, byte[] hash, SocketChannel channel) {
		/* We create an entry */
		PlayerEntry entry=new PlayerEntry(channel);
		entry.clientid=generateClientID();
		
		/* Finally adds it to map */
		clientidMap.put(entry.clientid, entry);
		
		return entry;
	}

	private int generateClientID() {
		int clientid = rand.nextInt();

		while (has(clientid)) {
			clientid = rand.nextInt();
		}

		return clientid;
	}
	
}