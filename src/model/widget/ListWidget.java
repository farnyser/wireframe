package model.widget;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Vector;

import model.Element;

public class ListWidget extends Widget
{
	protected Vector<String> content = new Vector<String>();

	public ListWidget()
	{
		content.add("item 1");
		content.add("item 2");
		content.add("item 3");
	}
	
	public Vector<String> getContent() 
	{
		return content;
	}

	public void setContent(Vector<String> content) 
	{
		this.content = content;
		pcs.firePropertyChange("setContent", null, content);
	}
}
