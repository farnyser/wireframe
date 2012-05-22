import model.ApplicationModel;
import model.Project;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ApplicationModel app = new ApplicationModel();
		
		app.createProject("test projet");
		Project cp = app.getCurrentProject();
		/*cp.addScene("Scene 1");
		cp.addScene("Scene 2");
		app.saveCurrentProject("/Users/joseph/Desktop/");
		System.out.println(app.getCurrentProject().getLabel());
		cp.pagesToString();

		
		app.createProject("project wireframe");
		cp = app.getCurrentProject();
		System.out.println(cp.getLabel());
		cp.addScene("Scene 19");
		cp.pagesToString();

		
		app.loadProject("/Users/joseph/Desktop/test-projet");
		cp = app.getCurrentProject();
		System.out.println(cp.getLabel());
		cp.pagesToString();
		*/
	}

}
