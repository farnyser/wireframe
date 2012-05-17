package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class ApplicationModel {

	/**
	 * The current project
	 */
	private Project _currentProject;
	
	public void createProject(String projectLabel) {
		
		_currentProject = new Project(projectLabel);
	}
	
	/**
	 * Saves the current project at the specified path
	 * @param path
	 */
	public void saveCurrentProject(String path) {
		
		if(_currentProject == null) return;

		String filePath = path+_currentProject.getSluggedLabel();
		try {
			ObjectOutput out = new ObjectOutputStream(new FileOutputStream(filePath));
			out.writeObject(_currentProject);
			out.close();
		}
		catch(IOException e) {
			System.out.println("DEBUG: ApplicationModel#can't save the project to "+filePath);
		}
	}
	
	/**
	 * Loads the project at the specified path
	 * @param filePath
	 */
	public void loadProject(String filePath) {
		try {
			File file = new File(filePath);
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			_currentProject = (Project) in.readObject();
			in.close();
		}
		catch(ClassNotFoundException e) {
			System.out.println("DEBUG: ApplicationModel#Class not found when loading the project at "+filePath);
		}
		catch(IOException e) {
			System.out.println("DEBUG: ApplicationModel#can't load the project at "+filePath);
		}
	}
	
	public Project getCurrentProject() { return _currentProject; }
}