package utils;

import model.ApplicationModel;

import org.mt4j.MTApplication;
import org.mt4j.input.inputSources.MacTrackpadSource;


import view.WorkspaceScene;

public class StartWireframe extends MTApplication 
{
	private static final long serialVersionUID = -4290913978074932279L;
	private	ApplicationModel app = new ApplicationModel();
	
	public static void main(String[] args) 
	{
		initialize();
	}
	
	@Override
	public void startUp() 
	{
		// Auto-detect Mac magic touchpad
		if(System.getProperty("os.name").equals("Mac OS X"))
			getInputManager().registerInputSource(new MacTrackpadSource(this));
	
		app.createProject("untitled");
		addScene(new WorkspaceScene(this,"Wireframe",app));
	}
}
