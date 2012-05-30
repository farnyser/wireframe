package utils;

import model.ApplicationModel;

import org.mt4j.MTApplication;
import org.mt4j.input.inputSources.MacTrackpadSource;

import view.StartMenuScene;

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
	
		//addScene(new WorkspaceScene(this,"Wireframe",app));
		addScene(new StartMenuScene(this,"Wireframe",app));
	}

}
