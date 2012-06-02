package menu;

import java.util.ArrayList;

import model.ApplicationModel;
import model.Project;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimation;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.ani.AniAnimation;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import utils.Slugger;
import view.page.Page;

public class ExistProjectMenu extends MTList{
	
	private AbstractMTApplication app;
	
	/** The font. */
	private IFont font;
	
	/** The preferred icon height. */
	private int preferredIconHeight;
	
	/** The gap between icon and reflection. */
	private int gapBetweenIconAndReflection;
	
	/** The display height of reflection. */
	private float displayHeightOfReflection;
	
	/** The list width. */
	private float listWidth;
	
	/** The list height. */
	private int listHeight;
	
	/** The preferred icon width. */
	private int preferredIconWidth;
	
	private ApplicationModel model;
	
	public ExistProjectMenu(PApplet applet, ApplicationModel m, float x, float y, float width, int height, float padY) {
		super(applet, x, y, width, height, padY);
		
		this.app = (AbstractMTApplication) applet;
		this.model = m;
		
		preferredIconWidth = 192;
		preferredIconHeight = 256;
		
		listWidth = width;
		listHeight = height;
		
		this.setFillColor(new MTColor(150,150,150,200));
		this.setNoStroke(true);
		
		font = FontManager.getInstance().createFont(app, "SansSerif", 18, MTColor.WHITE);
		
		this.loadProjectList();
		
		this.rotateZ(this.getCenterPointLocal(), -90, TransformSpace.LOCAL);
		this.setPositionGlobal(new Vector3D(app.width/2f, app.height/2f));
	
	}
	
	private void loadProjectList()
	{
		ArrayList<Project> list = model.getProjectList();
		
		if(list.size() == 0)
		{
			MessageBox m = new MessageBox(app, 0, 0, 260, 60);
			m.setMessage("No existing projects available");
			this.addChild(m);
			m.rotateZ(m.getCenterPointLocal(), 90, TransformSpace.LOCAL);
			m.setPositionGlobal(getCenterPointGlobal());
		}
		else
		{
			for(Project p : list)
			{
				this.addProjectScene(p);
			}
		}
		
	}
	
	private void addProjectScene(Project p)
	{
		Page firstPage = null;
		PImage defaultImage = null;
		MTPolygon po = null;
		
		float border = 1;
		float bothBorders = 2*border;
		float topShift = 30;
		
		float listCellWidth = listWidth;		
		float realListCellWidth = listCellWidth - bothBorders;

		float listCellHeight = preferredIconWidth ;
		
		//MTListCell cell = new MTListCell(app ,realListCellWidth, listCellHeight);
		SingleProjectMenu cell = new SingleProjectMenu(app,model,realListCellWidth, listCellHeight,p);
		cell.setNoFill(true);
		cell.setNoStroke(true);
		this.addListElement(cell);
		
		/*if(p.getPageList().size() != 0)
		{
			firstPage = new Page(this.getRenderer(), 0, 0, p.getPageList().get(0));
			firstPage.setMinSize(preferredIconWidth, preferredIconWidth);
			firstPage.rotateZ(firstPage.getCenterPointLocal(), 90, TransformSpace.LOCAL);
			
			Vertex[] vertices = new Vertex[]{
					new Vertex(realListCellWidth-topShift, 				border,		  		0, 0,0),
					new Vertex(realListCellWidth-topShift, 				 listCellHeight -border,	0, 1,0),
					new Vertex(realListCellWidth-topShift - preferredIconWidth/2 , listCellHeight -border,	0, 1,1),
					new Vertex(realListCellWidth-topShift - preferredIconWidth/2,	border,		  		0, 0,1),
					new Vertex(realListCellWidth-topShift, 				border,		  		0, 0,0),
			};
			po = new MTPolygon(app, vertices);
			po.addChild(firstPage);
			firstPage.setPositionRelativeToParent(po.getCenterPointLocal());
		}
		else
		{
			System.out.println("Project without pages");
			
			defaultImage = cropImage(app.loadImage("data/noImageAvailable.jpg"),preferredIconHeight,preferredIconWidth);
		
			Vertex[] vertices = new Vertex[]{
					new Vertex(realListCellWidth-topShift, 				border,		  		0, 0,0),
					new Vertex(realListCellWidth-topShift, 				 listCellHeight -border,	0, 1,0),
					new Vertex(realListCellWidth-topShift - defaultImage.height, listCellHeight -border,	0, 1,1),
					new Vertex(realListCellWidth-topShift - defaultImage.height,	border,		  		0, 0,1),
					new Vertex(realListCellWidth-topShift, 				border,		  		0, 0,0),
			};
			po = new MTPolygon(app, vertices);
			po.setTexture(defaultImage);
		}				
			
		po.setStrokeColor(new MTColor(80,80,80, 255));
		po.setNoStroke(true);
		po.setName(title);
		cell.addChild(po);
		po.setPositionRelativeToParent(cell.getCenterPointLocal());
		po.registerInputProcessor(new TapProcessor(app, 15));
		po.addGestureListener(TapProcessor.class, new IGestureEventListener(){
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				if (ge instanceof TapEvent) 
				{
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) 
					{
						System.out.println("Selected :" + te.getTarget().getName());
						setData(te.getTarget().getName());//Model
						close();
					}
		        }
				
		        return false;
			}
		});
		
		
		this.addListElement(cell);
		//this.addTapProcessor(cell);
			
		MTTextArea text = new MTTextArea(app, font);
		text.setFillColor(new MTColor(150,150,250,200));
		text.setNoFill(true);
		text.setNoStroke(true);
		text.setText(title);
		text.rotateZ(text.getCenterPointLocal(), 90, TransformSpace.LOCAL);
		cell.addChild(text);
			
		text.setPositionRelativeToParent(cell.getCenterPointLocal());
		text.translate(new Vector3D(realListCellWidth*0.5f - text.getHeightXY(TransformSpace.LOCAL)*0.5f, 0));

		//this.addImageTapProcessor(po);
		//this.addTextTapProcessor(text);

		 */
	}
	
/*	private PImage cropImage(PImage image, int w, int h) {
		PImage workingCopy;
		try {
			workingCopy= (PImage) image.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Cloning not supported!");
			workingCopy = image;
		}

		//Crops (and resizes) an Image to fit into the suqare
		PImage returnImage = app.createImage(w, h, PConstants.RGB);
		workingCopy.resize(w,h);
		
		// Crop Starting Points
		int x = (workingCopy.width / 2) - (w / 2);
		int y = (workingCopy.height / 2) - (h / 2);
		
		// Bugfixing: Don't Allow Out-of-Bounds coordinates
		if (x + w > workingCopy.width)
			x = workingCopy.width - w;
		if (x < 0)
			x = 0;
		if (x + w > workingCopy.width)
			w = workingCopy.width - x;
		if (y + h > workingCopy.height)
			x = workingCopy.height - h;
		if (y < 0)
			y = 0;
		if (y + h > workingCopy.height)
			h = workingCopy.height - y;
		
		//Crop Image
		returnImage.copy(workingCopy, x, y, w, h, 0, 0, w, h);

		return returnImage;
	}
	
	
	private void addTapProcessor(MTListCell cell){
		cell.registerInputProcessor(new TapProcessor(app, 15));
		cell.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					System.out.println("Selected :" + te.getTarget().getName());
					setData(te.getTarget().getName());//Model
					close();
				}
				return false;
			}
		});
	}
	
	private void addImageTapProcessor(MTPolygon p){
		p.removeAllGestureEventListeners(TapProcessor.class);
		p.setGestureAllowance(TapProcessor.class, true);
		TapProcessor tp = new TapProcessor(this.app);
		p.registerInputProcessor(tp);
		
		p.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					System.out.println("Selected :" + te.getTarget().getClass());
					setData(te.getTarget().getName());//Model
					close();
				}
				return false;
			}
		});
	}
	
	private void addTextTapProcessor(final MTTextArea t){
		t.registerInputProcessor(new TapProcessor(app, 15));
		t.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{	
					MTKeyboard keyboard = new MTKeyboard(app);
					ExistProjectMenu.this.addChild(keyboard);
					keyboard.setPositionGlobal(new Vector3D(app.width/2, app.height/4*3));
					keyboard.addTextInputListener(t);
					keyboard.addStateChangeListener(
						  StateChange.COMPONENT_DESTROYED,
						  new StateChangeListener() {
								@Override
							public void stateChanged(StateChangeEvent arg0) {
								t.setEnableCaret(false);	
							}
						  }
					);		
							t.setEnableCaret(true);
			            }
				return false;
			}
		});
	}
	
	private void setData(String name)
	{
		model.saveCurrentProject(Project._path);
		model.loadProject(Project._path+Slugger.toSlug(name)+".wire");
	}
	
	private void close(){
		float width = this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		IAnimation closeAnim = new AniAnimation(width, 1, 350, AniAnimation.SINE_IN, this);
		closeAnim.addAnimationListener(new IAnimationListener(){
			public void processAnimationEvent(AnimationEvent ae) {
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					float currentVal = ae.getAnimation().getValue();
					setWidthXYRelativeToParent(currentVal);
					rotateZ(getCenterPointRelativeToParent(), -ae.getDelta()*0.3f);
					break;
				case AnimationEvent.ANIMATION_ENDED:
					setVisible(false);
					destroy();
					break;	
				default:
					break;
				}//switch
			}//processanimation
		});
		closeAnim.start();
	}
	*/
	
}
