package project;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.NoSuchElementException;

public interface IFileHandler {
	
	final static String DEFAULT_SAVE = null;
	
	void ensureFolderAndFileExists();

	void save(String fileName, Game game) throws FileNotFoundException;
	
	Game load(String fileName) throws FileNotFoundException;
	
	Path getFolderPath();
	
	Path getFilePath(String fileName);

}
