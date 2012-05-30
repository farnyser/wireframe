package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import utils.Slugger;

public class Project implements Serializable
{
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

	protected PropertyChangeSupport _pcs = new PropertyChangeSupport(this);

	public static String _path = "repository/";

	
	public Project(String projectLabel) {
		
		_label = projectLabel;
		_sluggedLabel = Slugger.toSlug(projectLabel);

		_pageList = new ArrayList<Page>();
		
		// for initialization # debug
//		model.Page tmpPage;
//		model.widget.Widget tmpWidget;
//		for ( int i = 0 ; i < 3 ; i++ )	{
//			tmpPage = createPage("untitled document "+i);
//			tmpWidget = new model.widget.Widget(100,100,0);
//			tmpWidget.setSize(100, 100);
//			tmpPage.addElement(tmpWidget);
//		}		
	}
	
	/**
	 * Close project
	 */
	public void close() {
		System.out.println("close project " + this);
		_pcs.firePropertyChange("closeProject",null,this);
		
		for(model.Page p : _pageList)
			p.fireClose();
	}
	
	/**
	 * Create a page
	 * @param pageLabel
	 * @return the created page
	 */
	public Page createPage(String pageLabel) {
		
		Page sc = new Page(pageLabel);
		this.addPage(sc);
		
		return sc;
	}
	
	/**
	 * Adds a page
	 * @param page
	 */
	public void addPage(Page page) {
		_pageList.add(page);
		_pcs.firePropertyChange("addPage", null, page);
	}
	
	/**
	 * Removes a page
	 * @param page
	 */
	public void removePage(Page page) {
		page.fireDeletion();
		_pageList.remove(page);
	}
	
	/**
	 * Put the specified page at the specified index
	 * @param newIndex
	 * @param page
	 */
	public void reorderPage(int newIndex, Page page) {
		_pageList.remove(page);
		_pageList.add(newIndex, page);
	}
	
	public ArrayList<Page> getPageList() { return _pageList; }
	
	public String getLabel() {	return _label;	}

	public String getSluggedLabel() { return this._sluggedLabel; }
	
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
