package view;

import java.util.ArrayList;
import java.util.List;

import menu.MessageBox;
import menu.NewProjectMenu;
import menu.HexagonMenu;
import menu.ExistProjectMenu;
import menu.RectangleMenu;
import model.ApplicationModel;
import model.Project;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.widgets.menus.MenuItem;

import processing.core.PImage;

public class StartMenuScene extends AbstractScene{

	private AbstractMTApplication app;
	private HexagonMenu hMenu;
	private ApplicationModel model;
	WidgetLibrary widgets = null;
	PageLibrary pages = null;
	
	public StartMenuScene(AbstractMTApplication mtApplication, String name, ApplicationModel model)
	{
		super(mtApplication, name);
		
		this.setClearColor(MTColor.BLACK);
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		this.model = model;
		this.app = mtApplication;
		this.getCanvas().setFrustumCulling(true); //?
		
		PImage newPageIcon = app.loadImage("data/Plus.jpg");
		
		//Create Menu Items
		List<MenuItem> menus = new ArrayList<MenuItem>();
		menus.add(new MenuItem("Widgets", new gestureListener("Widgets",this)));
		menus.add(new MenuItem("Scenes", new gestureListener("Scenes",this)));
		menus.add(new MenuItem("Load", new gestureListener("Load",this)));
		menus.add(new MenuItem(newPageIcon, new gestureListener("New page",this)));
		menus.add(new MenuItem("Exit", new gestureListener("Exit",this)));
		menus.add(new MenuItem("Undo", new gestureListener("Undo",this)));
		menus.add(new MenuItem("Redo", new gestureListener("Redo",this)));

		//Create Hexagon Menu
		hMenu = new HexagonMenu(app, new Vector3D(800,550), menus, 70);
		this.getCanvas().addChild(hMenu);
	
	}
	
	public void onEnter() {}
	
	public void onLeave() {}
	

	public class gestureListener implements IGestureEventListener {
		String string;
		AbstractScene scene;
		RectangleMenu rMenu;
		NewProjectMenu nameArea;
		ExistProjectMenu projectList;
		
		public gestureListener(String string, AbstractScene scene) {
			super();
			this.string = string;
			this.scene = scene;
		}
			
		public boolean processGestureEvent(MTGestureEvent ge) {

			if (ge instanceof TapEvent) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					
					if(string.equals("Load"))
					{
						createLoadMenu();
					}
					if(string.equals("New project"))
					{
						createNewProject();
					}
					if(string.equals("Existing project"))
					{
						loadFromExistingProject();
					}
					if(string.equals("Exit"))
					{
						exitApplication();
					}
					if(string.equals("New page"))
					{
						createNewPage();
					}
					if(string.equals("Scenes"))
					{
						openScenesLibrary();
					}
					if(string.equals("Widgets"))
					{
						openWidgetLibrary();
					}
					if(string.equals("Redo"))
					{
						redoOperation();
					}
					if(string.equals("Undo"))
					{
						undoOperation();
					}
				}
			}
			return false;
		}
		
		
		private void createLoadMenu()
		{
			
			List<MenuItem> menuLoad = new ArrayList<MenuItem>();
			menuLoad.add(new MenuItem("New projet", new gestureListener("New project",this.scene)));
			menuLoad.add(new MenuItem("From an exsiting projet", new gestureListener("Existing project", this.scene)));
			
			rMenu = new RectangleMenu(app, new Vector3D(320,180) , menuLoad, 170);
			scene.getCanvas().addChild(rMenu);
			
		}
	
		private void createNewProject()
		{
			
			System.out.println("new project");
			
			if(nameArea != null) nameArea.destroy();
			
			nameArea = new NewProjectMenu(app, model, 0, 0, 380, 100);
			nameArea.setPositionGlobal(new Vector3D(app.width/2, app.height/2));
			scene.getCanvas().addChild(nameArea);
	
		}
		
		private void loadFromExistingProject()
		{
			System.out.println("Existing project");
			
			int preferredIconHeight = 192;
			int gapBetweenIconAndReflection = 9;
			int displayHeightOfReflection = (int) (preferredIconHeight * 0.6f);
			int listWidth = preferredIconHeight + displayHeightOfReflection + gapBetweenIconAndReflection;
			int listHeight = app.width;

			if(projectList != null) projectList.destroy();
			
			projectList = new ExistProjectMenu(app, model, 0, 0, (float)listWidth, listHeight, 40);
			scene.getCanvas().addChild(projectList);
			scene.getCanvas().setFrustumCulling(true); 
		}
	
	    private void createNewPage()
	    {
	    	if(model.getCurrentProject() == null)
			{	
	    		MessageBox m = new MessageBox(app, 0, 0, 200, 60);
				m.setMessage("Please first load a project");
				scene.getCanvas().addChild(m);
				m.setPositionRelativeToParent(new Vector3D(app.width/2, app.height/2));	
			}
	    	else
	    	{
	    		model.Page newAbPage = model.getCurrentProject().createPage("untitled");
		    	view.page.Page newPage = new view.page.Page(app, 0, 0, newAbPage);
		    	scene.getCanvas().addChild(newPage);
		    	newPage.setPositionGlobal(new Vector3D(200 + newAbPage.getWidth()/2,150 + newAbPage.getHeight()/2));
	    	}
	    	
	    }
		
		private void openWidgetLibrary()
		{
			if(model.getCurrentProject() == null)
			{	
				MessageBox m = new MessageBox(app, 0, 0, 200, 60);
				m.setMessage("Please first load a project");
				scene.getCanvas().addChild(m);
				m.setPositionRelativeToParent(new Vector3D(app.width/2, app.height/2));	
			}
			else
			{
				if ( widgets == null )
				{
					widgets = new WidgetLibrary(app, 0, 0, 200, 200);
					scene.getCanvas().addChild(widgets);
				}
				else
				{
					widgets.destroy();
					widgets = null;	
				}
			}
			
		}
		
		private void openScenesLibrary()
		{
			if(model.getCurrentProject() == null)
			{
				MessageBox m = new MessageBox(app, 0, 0, 200, 60);
				m.setMessage("Please first load a project");
				scene.getCanvas().addChild(m);
				m.setPositionRelativeToParent(new Vector3D(app.width/2, app.height/2));
			}
			else
			{
				if ( pages == null )
				{
					pages = new PageLibrary(app, 800, 0, 200, 200, model);
					scene.getCanvas().addChild(pages);
				}
				else
				{
					pages.destroy();
					pages = null;
				}
			}
		}
		
		private void exitApplication()
	    {
	    	if(model.getCurrentProject() != null)
	    	{
				model.saveCurrentProject(Project._path);
				model.getCurrentProject().close();
	    	}
	    	app.exit();
	    }
		
		private void redoOperation()
	    {
	    	
	    }
		
		private void undoOperation()
	    {
	    	
	    }
	}

}
