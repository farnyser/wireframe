package model.widget;

public class ButtonWidget extends Widget 
{
	private static final long serialVersionUID = 4714947455135815561L;
	
	protected String content = new String("Button");

	public String getContent() 
	{
		return content;
	}

	public void setContent(String content) 
	{
		this.content = content;
		pcs.firePropertyChange("setContent", null, content);
	}
}
