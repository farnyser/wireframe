package model;

public class Page extends Element
{
	private static final long serialVersionUID = -2928023297600664920L;
	
	protected String name;
	
	public Page(String _name)
	{
		this.name = _name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String _name)
	{
		this.name = _name;
	}
}
