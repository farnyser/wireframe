package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;

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

	/**
	 * Project has been modified and need to b e saved ?
	 */
	protected boolean _updated = false;
	
	protected PropertyChangeSupport _pcs = new PropertyChangeSupport(this);

	public static String _path = "repository/";

	
	public Project(String projectLabel) {
		
		_label = projectLabel;
		_sluggedLabel = Slugger.toSlug(_label);

		_pageList = new ArrayList<Page>();
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
		_updated = true;
		
		return sc;
	}
	
	/**
	 * Adds a page
	 * @param page
	 */
	public void addPage(Page page) {
		_pageList.add(page);
		_pcs.firePropertyChange("addPage", null, page);
		_updated = true;
	}
	
	/**
	 * Removes a page
	 * @param page
	 */
	public void removePage(Page page) {
		page.fireDeletion();
		_pageList.remove(page);
		_updated = true;
	}
	
	/**
	 * Put the specified page at the specified index
	 * @param newIndex
	 * @param page
	 */
	public void reorderPage(int newIndex, Page page) {
		_pageList.remove(page);
		_pageList.add(newIndex, page);
		_updated = true;
	}
	
	public ArrayList<Page> getPageList() { return _pageList; }
	
	public String getLabel() {	return _label;	}
	
	public void setLabel(String label) { _label = label; _sluggedLabel = Slugger.toSlug(_label); }

	public String getSluggedLabel() { return this._sluggedLabel; }
	
	public boolean getUpdateStatus()
	{
		return _updated;
	}
	
	public void setSaved()
	{
		_updated = false;
	}
	
	//---
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
