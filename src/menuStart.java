import java.util.ArrayList;
import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.css.style.CSSSelector;
import org.mt4j.components.css.util.CSSTemplates;
import org.mt4j.components.css.util.CSSKeywords.CSSSelectorType;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.widgets.menus.MTHexagonMenu;
import org.mt4jx.components.visibleComponents.widgets.menus.MTSquareMenu;
import org.mt4jx.components.visibleComponents.widgets.menus.MenuItem;

import processing.core.PImage;


public class menuStart  extends AbstractScene{
	
private AbstractMTApplication app;
	
	public menuStart(AbstractMTApplication mtApplication, String name) {
		
		super(mtApplication, name);
		
		this.app = mtApplication;
		
		//Set CSS Enabled for all components
		if(app != null){
			app.getCssStyleManager().setGloballyEnabled(true);
		}

		//Load a different CSS Style for each component
		app.getCssStyleManager().loadStylesAndOverrideSelector(CSSTpl.GREENSTYLE, new CSSSelector("MTHexagonMenu", CSSSelectorType.CLASS));

		
		PImage newPageIcon = app.loadImage("data/img/Plus.jpg");
		
		//Create Menu Items
		List<MenuItem> menus = new ArrayList<MenuItem>();
		menus.add(new MenuItem("Project", new gestureListener("Project",this)));
		menus.add(new MenuItem("Widget", new gestureListener("Widget",this)));
		menus.add(new MenuItem("Load", new gestureListener("Load",this)));
		menus.add(new MenuItem(newPageIcon, new gestureListener("New page",this)));
		menus.add(new MenuItem("Save", new gestureListener("Save",this)));
		menus.add(new MenuItem("Undo", new gestureListener("Undo",this)));
		menus.add(new MenuItem("Exit", new gestureListener("Exit",this)));

		//Create Hexagon Menu
		MTHexagonMenu hm = new MTHexagonMenu(app, new Vector3D(600,400),  menus, 100);
		this.getCanvas().addChild(hm);
	
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
					
					if(string.equals("Load")){
						
						List<MenuItem> menuLoad = new ArrayList<MenuItem>();
						menuLoad.add(new MenuItem("New projet", new gestureListener("New projet",this.scene)));
						menuLoad.add(new MenuItem("From an exsiting projet", new gestureListener("Exsiting projet", this.scene)));
						
						MTSquareMenu sm1 = new MTSquareMenu(app, new Vector3D(100,500), menuLoad, 200);
						scene.getCanvas().addChild(sm1);
					}
					
					if(string.equals("Save")){
						
						List<MenuItem> menuSave = new ArrayList<MenuItem>();
						menuSave.add(new MenuItem("Enter the projet's name", new gestureListener("Project name", this.scene)));
						
						MTSquareMenu sm2 = new MTSquareMenu(app, new Vector3D(300,300), menuSave, 200);
						scene.getCanvas().addChild(sm2);
					}
					
				}
			}
			return true;
		}
		
	}
}
