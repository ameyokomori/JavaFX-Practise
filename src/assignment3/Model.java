package assignment3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Model class for the Character Editor
 * @author leggy (Lachlan Healey)
 * @author Weiye Zhao
 */
public class Model {
	
	private CharacterDatabase database;
	private String path;
	
	public Model() {
		path = null;
		database = null;
	}
	
	/**
	 * Create an empty database file.
	 * 
	 * @param path The name or path of database
	 * @throws IOException
	 */
	public void CreateDB(String path) throws IOException {
		CharacterDatabase db = new CharacterDatabase(path);
		db.save();	
	}

	/**
	 * Load all characters in database.
	 * 
	 * @param path The name or path of database
	 * @return list of characters' name
	 * @throws FileNotFoundException if file not found
	 * @throws Exception
	 */
	public List<String> Load(String path) throws FileNotFoundException, Exception {
		this.path = path;
		database = new CharacterDatabase(this.path);
		database.load();
		List<String> name = database.getCharacterNames();
		/* Sort the name */
		Collections.sort(name, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.hashCode() - o2.hashCode();
			}			
		});		
		return name;
	}
	
	/**
	 * Save characters to database.
	 * 
	 * @throws IOException
	 */
	public void Save() throws IOException {
		database.save();
	}
	
	/**
	 * Updates a character in the database if it present, adding the character if it
	 * is not present.
	 * 
	 * @param character The Character to update.
	 */
	public void Update(Character character) {
		database.update(character);
	}
	
	/**
	 * Remove a character to the database.
	 * 
	 * @param character The character to remove.
	 */
	public void Remove(Character character) {
		database.remove(character);
	}
	
	/**
	 * Search the character database for a character with the given name.
	 * 
	 * @param name The character name to search for
	 * @return character object found or null
	 */
	public Character Search(String name) {
		return database.search(name);
	}
}
