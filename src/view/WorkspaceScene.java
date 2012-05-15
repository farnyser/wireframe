package view;

import org.mt4j.AbstractMTApplication;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;

import widget.Widget;


public class WorkspaceScene extends AbstractScene {

	public WorkspaceScene(AbstractMTApplication mtApplication, String name)
	{
		super(mtApplication, name);
		
		this.setClearColor(MTColor.SILVER);
		
		//Show touches
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		WidgetLibrary widgets = new WidgetLibrary(mtApplication, 0, 0, 200, 200);
		PageLibrary pages = new PageLibrary(mtApplication, 800, 0, 200, 200);
		
		this.getCanvas().addChild(widgets);
		this.getCanvas().addChild(pages);
	}

}
