package menu;


import model.ApplicationModel;
import model.Project;

import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import utils.Slugger;
import view.page.Page;

public class SingleProjectMenu extends MTListCell{

	private PApplet applet;
	private ApplicationModel model;
	private MTPolygon image;
	private MTTextArea deleteButton;
	private MTPolygon okArrow;
	private MTTextArea titleArea;
	private Project project;
	
	public SingleProjectMenu(PApplet applet, ApplicationModel m, float width, float height, Project p) {
		super(applet, width, height);
		this.applet = applet;
		this.model = m;
		this.project = p;
		this.setComposite(false);
		
		IFont font = FontManager.getInstance().createFont(applet, "SansSerif", 18, MTColor.WHITE);
		deleteButton = new MTTextArea(applet, font);
		titleArea = new MTTextArea(applet, font);
		Vertex[] v = new Vertex[]{
	       		new Vertex(0,0,0),
	       		new Vertex(0,30,0),
	       		new Vertex(15,15,0),
		};
		okArrow = new MTPolygon(applet,v);
		
		setTitle();
		setOKArrow();
		setImage();
		setDeleteButton();

	}
	
	private void setImage()
	{
		Page firstPage = null;
		PImage defaultImage = null;
		
		int preferredIconWidth = 192;
		int preferredIconHeight = 256;
		float border = 1;
		float bothBorders = 2*border;
		float topShift = 30;
		int gapBetweenImageAndButton = 9;
		int displayButtonHeight = (int) (preferredIconHeight * 0.3f);
		int listWidth = preferredIconHeight + displayButtonHeight + gapBetweenImageAndButton;
		float listCellWidth = listWidth;		
		float realListCellWidth = listCellWidth - bothBorders;
		float listCellHeight = preferredIconWidth ;
		
		if(project.getPageList().size() != 0)
		{
			firstPage = new Page(this.getRenderer(), project.getPageList().get(0));
			firstPage.setMinSize(preferredIconWidth, preferredIconWidth);
			firstPage.setComposite(true);
			firstPage.setPickable(false);
			firstPage.rotateZ(firstPage.getCenterPointLocal(), 90, TransformSpace.LOCAL);
			
			Vertex[] vertices = new Vertex[]{
					new Vertex(realListCellWidth-topShift, 				border,		  		0, 0,0),
					new Vertex(realListCellWidth-topShift, 				 listCellHeight -border,	0, 1,0),
					new Vertex(realListCellWidth-topShift - preferredIconWidth/2 , listCellHeight -border,	0, 1,1),
					new Vertex(realListCellWidth-topShift - preferredIconWidth/2,	border,		  		0, 0,1),
					new Vertex(realListCellWidth-topShift, 				border,		  		0, 0,0),
			};
			image = new MTPolygon(applet, vertices);
			image.addChild(firstPage);
			firstPage.setPositionRelativeToParent(image.getCenterPointLocal());
		}
		else
		{
			System.out.println("Project without pages");
			
			defaultImage = cropImage(applet.loadImage("data/noImageAvailable.jpg"),preferredIconHeight,preferredIconWidth);
		
			Vertex[] vertices = new Vertex[]{
					new Vertex(realListCellWidth-topShift, 				border,		  		0, 0,0),
					new Vertex(realListCellWidth-topShift, 				 listCellHeight -border,	0, 1,0),
					new Vertex(realListCellWidth-topShift - defaultImage.height, listCellHeight -border,	0, 1,1),
					new Vertex(realListCellWidth-topShift - defaultImage.height,	border,		  		0, 0,1),
					new Vertex(realListCellWidth-topShift, 				border,		  		0, 0,0),
			};
			image = new MTPolygon(applet, vertices);
			image.setTexture(defaultImage);
		}				
			
		image.setComposite(true);
		image.setStrokeColor(new MTColor(80,80,80, 255));
		image.setNoStroke(true);
		image.setName(titleArea.getText());
		this.addChild(image);
		
		image.setPositionRelativeToParent(this.getCenterPointLocal());
		
		image.removeAllGestureEventListeners();
		//image.removeAllGestureEventListeners(TapProcessor.class);
		image.setGestureAllowance(TapProcessor.class, true);
		TapProcessor tp = new TapProcessor(this.applet);
		image.registerInputProcessor(tp);
		image.addGestureListener(TapProcessor.class, new IGestureEventListener(){
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				if (ge instanceof TapEvent) 
				{
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) 
					{
						System.out.println("Selected :" + te.getTarget().getName());
						openSelectedProject(te.getTarget().getName());
						ExistProjectMenu parent = (ExistProjectMenu)SingleProjectMenu.this.getParent().getParent();
						parent.close();
					}
		        }	
		        return false;
			}
		});
		image.addGestureListener(DragProcessor.class, new IGestureEventListener(){
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				if ( image.getParent() != null )
					return image.getParent().processGestureEvent(ge);
				else
					return false;
			}
		});
	}
	
	private void setTitle()
	{
		titleArea.setFillColor(new MTColor(150,150,250,200));
		titleArea.setNoFill(true);
		titleArea.setNoStroke(true);
		titleArea.setText(project.getLabel());
		titleArea.rotateZ(titleArea.getCenterPointLocal(), 90, TransformSpace.LOCAL);
		this.addChild(titleArea);
		this.setName(titleArea.getText());
			
		titleArea.setPositionRelativeToParent(this.getCenterPointLocal());
		titleArea.translate(new Vector3D(280*0.5f - titleArea.getHeightXY(TransformSpace.LOCAL)*0.5f, 0));
	
		titleArea.removeAllGestureEventListeners();
		titleArea.setGestureAllowance(TapProcessor.class, true);
		TapProcessor tp = new TapProcessor(this.applet);
		titleArea.registerInputProcessor(tp);
		titleArea.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{	
					MTKeyboard keyboard = new MTKeyboard(applet);
					SingleProjectMenu.this.getRoot().addChild(keyboard);
					keyboard.setPositionGlobal(new Vector3D(applet.width/2, applet.height/4*3));
					keyboard.addTextInputListener(titleArea);
					keyboard.addStateChangeListener(
						  StateChange.COMPONENT_DESTROYED,
						  new StateChangeListener() 
						  {
							@Override
							public void stateChanged(StateChangeEvent arg0) 
							{
									titleArea.setEnableCaret(false);
									okArrow.setPositionRelativeToParent(new Vector3D(titleArea.getWidthXY(TransformSpace.LOCAL),
									titleArea.getHeightXY(TransformSpace.LOCAL)/2));
							}
						  }
					);	
					titleArea.setEnableCaret(true);
			     }
				return false;
			}
		});
		
	}
	
	private void setOKArrow()
	{
		okArrow.setFillColor(new MTColor(154,192,205));
		okArrow.setNoStroke(true);
		titleArea.addChild(okArrow);
		okArrow.setPositionRelativeToParent(new Vector3D(titleArea.getWidthXY(TransformSpace.LOCAL),titleArea.getHeightXY(TransformSpace.LOCAL)/2));
		
		okArrow.removeAllGestureEventListeners(TapProcessor.class);
		okArrow.setGestureAllowance(TapProcessor.class, true);
		TapProcessor tp2 = new TapProcessor(this.applet);
		okArrow.registerInputProcessor(tp2);
		okArrow.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{	
					System.out.println("Project renamed :" + titleArea.getText());
					
					image.setName(titleArea.getText());
					model.renameProject(project,titleArea.getText());
			    }
				return false;
			}
		});
	}
	
	private void setDeleteButton()
	{
		deleteButton.setText("Delete Project");
		deleteButton.setName(titleArea.getText());
		
		deleteButton.setFillColor(new MTColor(154,192,205));
		deleteButton.setStrokeColor(MTColor.WHITE);
		deleteButton.setStrokeWeight(1);
		
		this.addChild(deleteButton);
		deleteButton.rotateZ(titleArea.getCenterPointLocal(), 90, TransformSpace.LOCAL);
		deleteButton.setPositionRelativeToParent(this.getCenterPointLocal());
		deleteButton.translate(new Vector3D(-280*0.5f + titleArea.getHeightXY(TransformSpace.LOCAL)*0.5f, 0));
	
		deleteButton.removeAllGestureEventListeners(TapProcessor.class);
		deleteButton.setGestureAllowance(TapProcessor.class, true);
		TapProcessor tp = new TapProcessor(this.applet);
		deleteButton.registerInputProcessor(tp);
		deleteButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{	
					deleteProject();
			    }
				return false;
			}
		});
	
	}
	
	private PImage cropImage(PImage image, int w, int h) {
		PImage workingCopy;
		try {
			workingCopy= (PImage) image.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Cloning not supported!");
			workingCopy = image;
		}

		//Crops (and resizes) an Image to fit into the suqare
		PImage returnImage = applet.createImage(w, h, PConstants.RGB);
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
	
	private void openSelectedProject(String name)
	{
		model.saveCurrentProject(Project._path);
		model.loadProject(Project._path+Slugger.toSlug(name)+".wire");
	}
	
	private void deleteProject()
	{
		if(project.equals(model.getCurrentProject()))
		{
			MessageBox m = new MessageBox(applet, 0, 0, 260, 60);
			m.setMessage("Cannot delete the current project");
			this.addChild(m);
			m.rotateZ(m.getCenterPointLocal(), 90, TransformSpace.LOCAL);
			m.setPositionGlobal(new Vector3D(applet.width/2, applet.height/2));
		}
		else
		{
			model.deleteProject(project);
			this.destroy();
		}
	}

}
