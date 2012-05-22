
import org.mt4j.MTApplication;
import org.mt4j.input.inputSources.MacTrackpadSource;

public class StartApplication extends MTApplication {
	
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
		
		addScene(new StartMenuScene(this,"StartMenu"));
		
	}

}
