package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ApplicationModel {

	/**
	 * The current project
	 */
	private Project _currentProject;
	private ArrayList<Project> _projectList = new ArrayList<Project>();
	
	protected PropertyChangeSupport _pcs = new PropertyChangeSupport(this);

	public ApplicationModel() {
		String filePath = Project._path;
		File dir = new File(filePath);
		
		if(dir.list() != null) {
			
			for ( String s : dir.list() )
				this.loadProject(filePath + "/" + s);
		}
		_currentProject = null;
	}
	
	public void createProject(String projectLabel) {
		if ( _currentProject != null )
			_currentProject.close();
		
		_currentProject = new Project(projectLabel);
		_projectList.add(_currentProject);
		_pcs.firePropertyChange("newProject", null, _currentProject);
		saveCurrentProject(Project._path);
	}
	
	/**
	 * Saves the current project at the specified path
	 * @param path
	 */
	public void saveCurrentProject(String path) {
		
		if(_currentProject == null) return;
		if(_currentProject.getUpdateStatus() == false) return;
		
		String filePath = path+_currentProject.getSluggedLabel()+".wire";
		try {
			File f = new File(path);
			f.mkdirs();
			ObjectOutput out = new ObjectOutputStream(new FileOutputStream(filePath));
			out.writeObject(_currentProject);
			out.close();
			System.out.println("DEBUG: ApplicationModel#project saved to "+filePath);
		}
		catch(IOException e) {
			System.out.println("DEBUG: ApplicationModel#can't save the project to "+filePath);
			e.printStackTrace();
		}
	}
	
	
	public void saveProject(Project p)
	{	
		if(p.getUpdateStatus() == false) return;
		String newfilePath = Project._path + p.getSluggedLabel()+".wire";
		
		try {
			File f = new File(Project._path);
			f.mkdirs();
			ObjectOutput out = new ObjectOutputStream(new FileOutputStream(newfilePath));
			out.writeObject(p);
			out.close();
			System.out.println("DEBUG: ApplicationModel#project saved to "+newfilePath);
		}
		catch(IOException e) {
			System.out.println("DEBUG: ApplicationModel#can't save the project to "+newfilePath);
			e.printStackTrace();
		}
	}
	
	/**
	 * Rename a project
	 * 
	 * @param p		Project to rename
	 * @param name	New name
	 */
	public void renameProject(Project p, String name)
	{
		String oldname = p.getSluggedLabel();
		p.setLabel(name);
		this.saveProject(p);
		this.deleteProjectFile(oldname);
	}
	
	/**
	 * Loads the project at the specified path
	 * @param filePath
	 */
	public void loadProject(String filePath) {
		if ( _currentProject != null )
			_currentProject.close();		

		try {
			File file = new File(filePath);
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			_currentProject = (Project) in.readObject();
			_pcs.firePropertyChange("newProject", null, _currentProject);
			
			boolean insert = true;
			for ( Project p : _projectList )
			{
				if ( p.getLabel().equals(_currentProject.getLabel()) )
					insert = false;
			}

			if ( insert )
				_projectList.add(_currentProject);
			
			in.close();
		}
		catch(ClassNotFoundException e) {
			System.out.println("DEBUG: ApplicationModel#Class not found when loading the project at "+filePath);
		}
		catch(IOException e) {
			System.out.println("DEBUG: ApplicationModel#can't load the project at "+filePath);
			e.printStackTrace();
		}
	}
	
	public Project getCurrentProject() 
	{
		if ( _currentProject == null )
		{
			int i = 0;
			for ( Project p : _projectList )
			{
				if ( i == 0 && p.getLabel().equals("untitled project") )
					i++;
				else if ( p.getLabel().equals("untitled project ("+i+")") )
					i++;
			}
			
			if ( i == 0 )
				createProject("untitled project");
			else
				createProject("untitled project ("+i+")");
		}
		
		return _currentProject; 
	}
	
	public void deleteProjectFile(String filename)
	{
		String filePath = Project._path + filename+".wire";
		
		File f = new File(filePath);
			
		// Make sure the file or directory exists and isn't write protected
		if (!f.exists()) return;

		if (!f.canWrite())throw new IllegalArgumentException("Delete: write protected: " + filePath);
	
		// Attempt to delete it
		boolean success = f.delete();

		if (!success)throw new IllegalArgumentException("Delete: deletion failed");
		else System.out.println("DEBUG: ApplicationModel#project:"+filePath + "was deleted");
	}
	
	public void deleteProject(Project p)
	{
		if ( p == _currentProject )
			_pcs.firePropertyChange("closeProject", null, null);
			
		deleteProjectFile(p.getSluggedLabel());
		_projectList.remove(p);	
	}
	
	public ArrayList<Project> getProjectList()
	{
		return _projectList;
	}
	
	public void addListener(PropertyChangeListener listener)
	{
		_pcs.addPropertyChangeListener(listener);
	}
	
	public void removeListener(PropertyChangeListener listener)
	{
		_pcs.removePropertyChangeListener(listener);
	}
	
	public void resetListener()
	{
		_pcs = new PropertyChangeSupport(this);
	}
}
