package model;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

import org.mt4j.util.MTColor;

public class Page extends Element implements Serializable 
{
	private static final long serialVersionUID = 1L;
	protected Vector<model.widget.Widget> linkeds = new Vector<model.widget.Widget>();
	
	final static public String EVENT_PAGE_DELETED = "Page.event_page_deleted";
	final static public String EVENT_PAGE_CLOSED = "Page.event_page_closed";
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

	public void fireClose() {
		this.pcs.firePropertyChange(Page.EVENT_PAGE_CLOSED, null, null);
	}
	
	public void setLabel(String newLabel) {
		_label = newLabel;
		this.pcs.firePropertyChange(Page.EVENT_PAGE_RENAMED, null, newLabel);
	}

	public String getLabel() { return _label; }
	
	public void addLinked(model.widget.Widget widget) 
	{
		if ( linkeds.contains(widget) == false )
			linkeds.add(widget);
	}
	
	/**
	 * Get Random color associated to model
	 * @return a MTColor object
	 */
	public MTColor getColorFromId()
	{
		Random rand = new Random(this.hashCode());
		int r = rand.nextInt(200) + 55;
		int g = rand.nextInt(200) + 55;
		int b = rand.nextInt(200) + 55;
		return new MTColor(r,g,b);
	}
}
