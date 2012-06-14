package view;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;

import menu.ExistProjectMenu;
import menu.HexagonMenu;
import menu.NewProjectMenu;
import menu.RectangleMenu;
import model.ApplicationModel;
import model.Project;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTSceneTexture;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.widgets.menus.MenuItem;

import processing.core.PImage;

public class StartMenuScene extends AbstractScene{

	private static int LIB_WIDTH = 125;
	
	private AbstractMTApplication app;
	private HexagonMenu hMenu = null;
	//private HexagonMenu hMenu2 = null;
	private ApplicationModel model;
	private WidgetLibrary widgets = null;
	private PageLibrary pages = null;
	private RectangleMenu rMenu = null;
	private NewProjectMenu nameArea = null;
	private ExistProjectMenu projectList = null;
	private DrawSurfaceScene drawingScene;
	private MTEllipse pencilBrush,eraserBrush,noBrush;	
	private List<MenuItem> menus = new ArrayList<MenuItem>();
	private String imagesPath = "drawingIcon/";
	//private List<MenuItem> menus2 = new ArrayList<MenuItem>();
	private int state = 0;
	
	public StartMenuScene(AbstractMTApplication mtApplication, String name, ApplicationModel model)
	{
		super(mtApplication, name);

		
		//this.setClearColor(new MTColor(187, 200, 188));
		this.setClearColor(new MTColor(200, 210, 215));
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		this.model = model;
		this.app = mtApplication;
		this.getCanvas().setFrustumCulling(true); //?

		initializeDrawing();
		
		// on le cree par defaut pour que la PageLibrary capte les evenements des view.Page
		this.pages = new PageLibrary(app, app.getWidth()-LIB_WIDTH, 0, LIB_WIDTH, app.getHeight(), model);
		this.pages.setVisible(false);
		this.getCanvas().addChild(pages);
		
		//default: create widget library 
		this.widgets = new WidgetLibrary(app, 0, 0, LIB_WIDTH, app.getHeight(), model);
		this.widgets.setVisible(false);
		this.getCanvas().addChild(widgets);
		
		
		/** Hexagon menu **/
		
		PImage newPageIcon = app.loadImage("data/Plus.jpg");
		
		//Create Default Menu Items
		menus.add(new MenuItem("Widgets", new gestureListener("Widgets",this)));
		menus.add(new MenuItem("Scenes", new gestureListener("Scenes",this)));
		menus.add(new MenuItem("Project", new gestureListener("Project",this)));
		menus.add(new MenuItem(newPageIcon, new gestureListener("New page",this)));
		menus.add(new MenuItem("Exit", new gestureListener("Exit",this)));
		menus.add(new MenuItem("DrawWidget", new gestureListener("Draw Widget",this)));
		menus.add(new MenuItem("Export", new gestureListener("Export",this)));
		
		//New Menu Items After clicking on Project
		/*menus2.add(new MenuItem("Widgets", new gestureListener("Widgets",this)));
		menus2.add(new MenuItem("Scenes", new gestureListener("Scenes",this)));
		menus2.add(new MenuItem("Cancel", new gestureListener("Cancel",this)));
		menus2.add(new MenuItem(newPageIcon, new gestureListener("New page",this)));
		menus2.add(new MenuItem("Exit", new gestureListener("Exit",this)));
		menus2.add(new MenuItem("Save", new gestureListener("Save",this)));
		menus2.add(new MenuItem("Export", new gestureListener("Export",this)));*/
		
		//Create Hexagon Menu
		hMenu = new HexagonMenu(app, new Vector3D(app.getWidth()-LIB_WIDTH*2,550), menus, 70);
		this.getCanvas().addChild(hMenu);
		
		
//		DuoMenu dm = new DuoMenu(mtApplication, 150, app.getHeight(), model);
//		this.getCanvas().addChild(dm);
		
	}
	
	public WidgetLibrary getWidgetLibrary()
	{
		return widgets;
	}
	
	public void onEnter() {}
	
	public void onLeave() {}
	

	public class gestureListener implements IGestureEventListener {
		String string;
		AbstractScene scene;
		
		public gestureListener(String string, AbstractScene scene) {
			super();
			this.string = string;
			this.scene = scene;
		}
			
		public boolean processGestureEvent(MTGestureEvent ge) {

			if (ge instanceof TapEvent) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					
					if(string.equals("Project"))
					{
						openProject();
					}
					if(string.equals("New project"))
					{
						createNewProject();
					}
					if(string.equals("Existing projects"))
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
					if(string.equals("Draw Widget"))
					{
						onDrawingMode();
					}
					if(string.equals("Export"))
					{
						exportToPngOperation();
					}
					if(string.equals("Cancel"))
					{
						cancelLoadFromExistingProject();
					}
				}
			}
			return false;
		}
		
		private void openProject()
		{
			List<MenuItem> menuLoad = new ArrayList<MenuItem>();
			menuLoad.add(new MenuItem("New project", new gestureListener("New project",this.scene)));
			menuLoad.add(new MenuItem("Existing projects", new gestureListener("Existing projects", this.scene)));	
			
			if(state == 2 || state == 3)
			{
				rMenu.destroy();
				rMenu = null;
			}
			
			if(nameArea != null) nameArea.destroy();
			if(projectList != null)projectList.destroy();
			
			if(rMenu != null)
			{
				rMenu.destroy();
				rMenu = null;
			}
			else
			{
				rMenu = new RectangleMenu(app, new Vector3D(0,0) , menuLoad, 170);
				rMenu.setPositionGlobal(new Vector3D(app.width/2, app.height/2));
				scene.getCanvas().addChild(rMenu);
			}
			
			state = 1;
		}
	
		private void createNewProject()
		{
			System.out.println("new project");
			
			if(nameArea != null) nameArea.destroy();
			nameArea = new NewProjectMenu(app, model, 0, 0, 440, 120);
			nameArea.setPositionGlobal(new Vector3D(app.width/2, app.height/2));
			scene.getCanvas().addChild(nameArea);
			
			state = 2;
		}
		
		private void loadFromExistingProject()
		{
			System.out.println("Existing projects");
			
			int preferredIconHeight = 192;
			int gapBetweenIconAndReflection = 9;
			int displayHeightOfReflection = (int) (preferredIconHeight * 0.5f);
			int listWidth = preferredIconHeight + displayHeightOfReflection + gapBetweenIconAndReflection;
			int listHeight = app.width;
			
			if(projectList != null) projectList.destroy();

			projectList = new ExistProjectMenu(app, model, 0, 0, (float)listWidth, listHeight, 40);
			scene.getCanvas().addChild(projectList);
			scene.getCanvas().setFrustumCulling(true); 
			
		/*	hMenu.destroy();
			hMenu = null;
			hMenu2 = new HexagonMenu(app, new Vector3D(app.getWidth()-LIB_WIDTH*2,550), menus2, 70);
			scene.getCanvas().addChild(hMenu2);*/
			
			state = 3;
		}
		
		private void cancelLoadFromExistingProject()
		{
			if(projectList != null) projectList.destroy();
			if(nameArea != null) nameArea.destroy();
		
			//hMenu2.destroy();
			
			hMenu = new HexagonMenu(app, new Vector3D(app.getWidth()-LIB_WIDTH*2,550), menus, 70);
			scene.getCanvas().addChild(hMenu);	
			
			/*if(hMenu2 == null)
			{
				hMenu = new HexagonMenu(app, new Vector3D(app.getWidth()-LIB_WIDTH*2,550), menus, 70);
				scene.getCanvas().addChild(hMenu);	
			}
			else
			{
				hMenu2.destroy();
				hMenu2 = null;	
			}*/
		}
	
	    private void createNewPage()
	    {
	    	model.Page newAbPage = model.getCurrentProject().createPage("untitled");
	    	view.page.Page newPage = new view.page.Page(app, newAbPage);
	    	newPage.addListener(pages);
	    	scene.getCanvas().addChild(newPage);
	    	newPage.setPositionGlobal(new Vector3D(app.width/2, app.height/2));
	    }
	    		
		private void openWidgetLibrary()
		{	
			widgets.setVisible(!widgets.isVisible());
		}
		
		private void openScenesLibrary()
		{
			pages.setVisible(!pages.isVisible());
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
		
		
		private void exportToPngOperation()
	    {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
			String date = sdf.format(Calendar.getInstance().getTime());
			String fileName = "export/"+date+"-"+model.getCurrentProject().getSluggedLabel()+".png";

			try {
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				Rectangle screenRectangle = new Rectangle(screenSize);
				Robot robot = new Robot();
				BufferedImage image = robot.createScreenCapture(screenRectangle);
				File f = new File(fileName);
				f.mkdirs();
				ImageIO.write(image, "png", f);
			}
			catch(Exception e) {
				System.out.println(e.getStackTrace());
			}
	    }
	}
	
	protected void initializeDrawing() {
		
		//Create window frame
        MTRectangle frame = new MTRectangle(app, app.width, app.height);
        this.getCanvas().addChild(frame);
        
        /** Drawing Menu **/
		// Create menu frame
        MTRoundRectangle frameMenu = new MTRoundRectangle(app,90, app.getHeight()-150, 0, 220, 68, 25, 25);
        frameMenu.setSizeXYGlobal(220, 68);
        this.getCanvas().addChild(frameMenu);
        // Menu buttons
        // . Pen brush selector button
        PImage freemodeIcon = app.loadImage(imagesPath + "freemode.png");
        final MTImageButton freemodeButton = new MTImageButton(app, freemodeIcon);
        freemodeButton.translate(new Vector3D(110,app.getHeight()-140,0));
        freemodeButton.setNoStroke(false);
        freemodeButton.setStrokeColor(new MTColor(0,0,0));
        frameMenu.addChild(freemodeButton);
        // . Pen brush selector button
        PImage penIcon = app.loadImage(imagesPath + "pen.png");
        final MTImageButton penButton = new MTImageButton(app, penIcon);
        penButton.translate(new Vector3D(175,app.getHeight()-140,0));
        penButton.setNoStroke(true);
        penButton.setStrokeColor(new MTColor(0,0,0));
        frameMenu.addChild(penButton);
        // . Eraser button
        PImage eraser = app.loadImage(imagesPath + "Kde_crystalsvg_eraser.png");
        final MTImageButton eraserButton = new MTImageButton(app, eraser);
        eraserButton.setNoStroke(true);
        eraserButton.translate(new Vector3D(240,app.getHeight()-140,0));
        eraserButton.setStrokeColor(new MTColor(0,0,0));
        frameMenu.addChild(eraserButton);
		
        
        // Canvas frame
        frame.setFillColor(new MTColor(200, 210, 215));
        
        //Create the scene in which we actually draw
        drawingScene = new DrawSurfaceScene(app, "DrawSurface Scene");
        drawingScene.setClear(false);
		
		// Create pencil brush
		pencilBrush = new MTEllipse(this.getMTApplication(), new Vector3D(7.5f, 7.5f,0), 7.5f, 7.5f, 60);
		pencilBrush.setPickable(false);
		pencilBrush.setNoFill(false); 
		pencilBrush.setNoStroke(false); 
		pencilBrush.setDrawSmooth(true);
		pencilBrush.setStrokeColor(new MTColor(0, 0, 0, 255));
		pencilBrush.setFillColor(new MTColor(0, 0, 0, 255));
		
		// Create eraser brush
		eraserBrush = new MTEllipse(this.getMTApplication(), new Vector3D(7.5f, 7.5f,0), 20.0f, 20.0f, 60);
		eraserBrush.setPickable(false);
		eraserBrush.setNoFill(false); 
		eraserBrush.setNoStroke(false); 
		eraserBrush.setDrawSmooth(true);
		eraserBrush.setStrokeColor(new MTColor(200, 210, 215, 255));
		eraserBrush.setFillColor(new MTColor(200, 210, 215, 255));
		
		// No Brush (while freemode activated)
		noBrush =  new MTEllipse(this.getMTApplication(), new Vector3D(7.5f, 7.5f,0), 7.5f, 7.5f, 60);
		noBrush.setPickable(false);
		noBrush.setNoFill(true); // disable drawing
		noBrush.setNoStroke(true); // disabled drawing
		noBrush.setDrawSmooth(true);
		
		drawingScene.setBrush(noBrush); // disabled at start-up
		
		final MTSceneTexture sceneTexture = new MTSceneTexture(app, 0, 0, app.width, app.height, drawingScene);
        sceneTexture.getFbo().clear(true, 255, 255, 255, 0, true);
        sceneTexture.setStrokeColor(new MTColor(155,155,155));
        frame.addChild(sceneTexture);
        
        
        /** Drawing menu behaviour **/
        // Freemode button behaviour
        freemodeButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					//As we are messing with opengl here, we make sure it happens in the rendering thread
					app.invokeLater(new Runnable() {
						public void run() {
							// Set color to white and a pencil brush
							drawingScene.setBrush(noBrush);
							
							// Highlight current button
							eraserButton.setNoStroke(true);
							penButton.setNoStroke(true);
							freemodeButton.setNoStroke(false);
						}
					});
				}
				return true;
			}
        });
        // Pen button behaviour
        penButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					//As we are messing with opengl here, we make sure it happens in the rendering thread
					app.invokeLater(new Runnable() {
						public void run() {
							// Set color to white and a pencil brush
							drawingScene.setBrushColor(pencilBrush.getStrokeColor());
							drawingScene.setBrush(pencilBrush);
							
							// Highlight current button
							eraserButton.setNoStroke(true);
							penButton.setNoStroke(false);
							freemodeButton.setNoStroke(true);
						}
					});
				}
				return true;
			}
        });
        // Eraser button behaviour
        eraserButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					//As we are messing with opengl here, we make sure it happens in the rendering thread
					app.invokeLater(new Runnable() {
						public void run() {
							// Set color to white and a pencil brush
							drawingScene.setBrushColor(eraserBrush.getStrokeColor());
							drawingScene.setBrush(eraserBrush);
							
							// Highlight current button
							eraserButton.setNoStroke(false);
							penButton.setNoStroke(true);
							freemodeButton.setNoStroke(true);
						}
					});
				}
				return true;
			}
        });
    
        
        
	}

	private void onDrawingMode()
	{
		MainDrawingScene scene = new MainDrawingScene(app,"Main drawing scene", widgets);
		app.addScene(scene);
		app.pushScene();
		app.changeScene(scene);
	}
	
}
