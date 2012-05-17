package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import utils.Slugger;

public class Project implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The project label
	 */
	private String _label;
	
	
	/**
	 * The list of scenes
	 */
	private ArrayList<Scene> _sceneList;
	
	/**
	 * The project label slugged (for save)
	 */
	private String _sluggedLabel;
	
	public Project(String projectLabel) {
		
		_label = projectLabel;
		_sluggedLabel = Slugger.toSlug(projectLabel);
		
		_sceneList = new ArrayList<Scene>();
	}
	
	/**
	 * Adds a scene
	 * @param sceneLabel
	 * @return the created scene
	 */
	public Scene addScene(String sceneLabel) {
		
		Scene sc = new Scene(sceneLabel);
		_sceneList.add(sc);
		
		return sc;
	}
	
	/**
	 * Removes a scene
	 * @param scene
	 */
	public void removeScene(Scene scene) {
		_sceneList.remove(scene);
	}
	
	/**
	 * Put the specified scene at the specified index
	 * @param newIndex
	 * @param scene
	 */
	public void reorderScene(int newIndex, Scene scene) {
		_sceneList.remove(scene);
		_sceneList.add(newIndex, scene);
	}
	
	public ArrayList<Scene> getSceneList() { return _sceneList; }
	
	public String getLabel() {	return _label;	}
	
	public String getSluggedLabel() { return _sluggedLabel; }
	
	/**
	 * Debug method
	 */
	public void scenesToString() {
		
		if(_sceneList.isEmpty()) {
			System.out.println("DEBUG: Project#This project has no scene.");
		}
		
		Iterator<Scene> it = _sceneList.iterator();
		
		while(it.hasNext()) {
			Scene s = (Scene) it.next();
			System.out.println(s.getLabel());
		}
	}
}
