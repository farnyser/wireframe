package view;

import model.ApplicationModel;

import org.mt4j.AbstractMTApplication;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;


public class WorkspaceScene extends AbstractScene 
{

	public WorkspaceScene(AbstractMTApplication mtApplication, String name, ApplicationModel model)
	{
		super(mtApplication, name);
		
		this.setClearColor(MTColor.SILVER);
		
		//Show touches
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		WidgetLibrary widgets = new WidgetLibrary(mtApplication, 0, 0, 200, 200);
		PageLibrary pages = new PageLibrary(mtApplication, 800, 0, 200, 200, model);
		
		this.getCanvas().addChild(widgets);
		this.getCanvas().addChild(pages);
	}

}
