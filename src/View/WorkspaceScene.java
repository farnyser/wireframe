package View;

import org.mt4j.AbstractMTApplication;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;

public class WorkspaceScene extends AbstractScene {

	public WorkspaceScene(AbstractMTApplication mtApplication, String name)
	{
		super(mtApplication, name);
		
		this.setClearColor(MTColor.SILVER);
		
		//Show touches
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		Library widgets = new Library(mtApplication, 0, 0, 200, 200);
		Library pages = new Library(mtApplication, 800, 0, 200, 200);
		Page blank = new Page(mtApplication, 250, 250, 400, 400);
		Widget w = new Widget(mtApplication, 300, 300, 50, 50);
		
		this.getCanvas().addChild(blank);
		this.getCanvas().addChild(widgets);
		this.getCanvas().addChild(pages);
		this.getCanvas().addChild(w);
	}

}
