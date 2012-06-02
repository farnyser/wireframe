package menu;

import java.util.ArrayList;

import model.ApplicationModel;
import model.Project;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.util.MTColor;

import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimation;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.ani.AniAnimation;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;
import processing.core.PApplet;

public class ExistProjectMenu extends MTList{
	
	private AbstractMTApplication app;
	
	/** The font. */
	private IFont font;
	
	/** The preferred icon height. */
	private int preferredIconHeight;
	
	/** The preferred icon width. */
	private int preferredIconWidth;
	
	/** The list width. */
	private float listWidth;
	
	/** The list height. */
	private int listHeight;
	

	
	private ApplicationModel model;
	
	public ExistProjectMenu(PApplet applet, ApplicationModel m, float x, float y, float width, int height, float padY) {
		super(applet, x, y, width, height, padY);
		
		this.app = (AbstractMTApplication) applet;
		this.model = m;
		
		preferredIconWidth = 192;
		preferredIconHeight = 256;
		listWidth = width;
		listHeight = height;
		font = FontManager.getInstance().createFont(app, "SansSerif", 18, MTColor.WHITE);
		
		this.setFillColor(new MTColor(150,150,150,200));
		this.setNoStroke(true);

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
		
		float border = 1;
		float bothBorders = 2*border;
		float listCellWidth = listWidth;		
		float realListCellWidth = listCellWidth - bothBorders;
		float listCellHeight = preferredIconWidth ;
		
		SingleProjectMenu cell = new SingleProjectMenu(app,model,realListCellWidth, listCellHeight, p);
		cell.setNoFill(true);
		cell.setNoStroke(true);
		this.addListElement(cell);
	}
	
	public void close(){
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
	
}
