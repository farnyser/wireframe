import org.mt4j.MTApplication;


public class menuLauncher extends MTApplication {
	
	public static void main(String[] args) {
		initialize();
	}

	@Override
	public void startUp() {
		addScene(new menuStart(this, "Integration Test Menu"));
	}

}
