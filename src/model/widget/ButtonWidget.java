package model.widget;

public class ButtonWidget extends Widget 
{
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
