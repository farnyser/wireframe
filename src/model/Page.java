package model;

import java.io.Serializable;

public class Page extends Element implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final static public String EVENT_PAGE_DELETED = "Page.event_page_deleted";
	final static public String EVENT_PAGE_RENAMED = "Page.event_page_renamed"; 

	/**
	 * The scene label
	 */
	private String _label;
	
	public Page(String sceneLabel) {
		
		super();
		
		_label = sceneLabel;
	}
	
	public void fireDeletion() {
		this.pcs.firePropertyChange(Page.EVENT_PAGE_DELETED, null, null);
	}
	
	public void setLabel(String newLabel) {
		_label = newLabel;
		this.pcs.firePropertyChange(Page.EVENT_PAGE_RENAMED, null, newLabel);
	}

	public String getLabel() { return _label; }
}
