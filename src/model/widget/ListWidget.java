package model.widget;

import java.util.Vector;

public class ListWidget extends Widget
{
	private static final long serialVersionUID = -6480233766945866098L;
	
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
