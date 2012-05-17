package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable
{
	private static final long serialVersionUID = 1422300687543381832L;
	
	protected String name;
	protected ArrayList<model.Page> pages;
	
	public Project(String _name)
	{
		this.name = _name;
		this.pages = new ArrayList<model.Page>();
		
		for ( int i = 0 ; i < 3 ; i++ )
		{
			model.Page page = new model.Page("untitled " + i);
			model.widget.Widget widget = new model.widget.Widget(100,100,0);
			widget.setSize(100, 100);
			page.addElement(widget);
			this.addPage(page);
		}

	}
	
	public void addPage(model.Page p)
	{
		this.pages.add(p);
	}
	
	public ArrayList<model.Page> getPages()
	{
		return pages;
	}
}
