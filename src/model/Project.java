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
	private ArrayList<Page> _pageList;
	
	/**
	 * The project label slugged (for save)
	 */
	private String _sluggedLabel;
	
	public Project(String projectLabel) {
		
		_label = projectLabel;
		_sluggedLabel = Slugger.toSlug(projectLabel);
		
		_pageList = new ArrayList<Page>();
	}
	
	/**
	 * Adds a scene
	 * @param sceneLabel
	 * @return the created scene
	 */
	public Page addScene(String sceneLabel) {
		
		Page sc = new Page(sceneLabel);
		_pageList.add(sc);
		
		return sc;
	}
	
	/**
	 * Removes a scene
	 * @param scene
	 */
	public void removeScene(Page scene) {
		_pageList.remove(scene);
	}
	
	/**
	 * Put the specified scene at the specified index
	 * @param newIndex
	 * @param scene
	 */
	public void reorderScene(int newIndex, Page scene) {
		_pageList.remove(scene);
		_pageList.add(newIndex, scene);
	}
	
	public ArrayList<Page> getSceneList() { return _pageList; }
	
	public String getLabel() {	return _label;	}
	
	public String getSluggedLabel() { return _sluggedLabel; }
	
	/**
	 * Debug method
	 */
	public void pagesToString() {
		
		if(_pageList.isEmpty()) {
			System.out.println("DEBUG: Project#This project has no scene.");
		}
		
		Iterator<Page> it = _pageList.iterator();
		
		while(it.hasNext()) {
			Page s = (Page) it.next();
			System.out.println(s.getLabel());
		}
	}
}
