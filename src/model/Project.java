package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable
{
	private static final long serialVersionUID = 1422300687543381832L;
	
	protected String name;
	protected ArrayList<Page> pages;
	
	public Project(String _name)
	{
		this.name = _name;
	}
	
	public void addPage(Page p)
	{
		this.pages.add(p);
	}
}
