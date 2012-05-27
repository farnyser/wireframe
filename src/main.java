import org.mt4j.MTApplication;
import view.StartMenuScene;
import model.ApplicationModel;


public class main extends MTApplication{

	public static void main(String[] args) {	
		initialize();	
	}

	@Override
	public void startUp() {
		
		ApplicationModel app = new ApplicationModel();
		addScene(new StartMenuScene(this, "Start Menu", app));
	}

}
