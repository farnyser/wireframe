package utils;

import org.mt4j.MTApplication;

import view.WorkspaceScene;


public class Main extends MTApplication 
{
	private static final long serialVersionUID = -4290913978074932279L;

	public static void main(String[] args) 
	{
		initialize();
	}
	
	@Override
	public void startUp() 
	{
		addScene(new WorkspaceScene(this,"Wireframe"));
	}
}
