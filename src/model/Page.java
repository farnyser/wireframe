package model;

import java.io.Serializable;

public class Page extends Element implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final static public String EVENT_PAGE_DELETED= "Page.event_page_deleted";

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

	public String getLabel() { return _label; }
}
