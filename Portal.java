/** This class represents a wall */
import java.awt.*;
import java.util.*;
import javax.media.opengl.*;
import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;

public class Portal extends FPPolygon {

    public final double portalHeight = 100.0;

    public String extraName(int i){
	return i==0 ? "Start Height" : "End Height";
    }

    public float lineWidth() {
	return 4.0f;
    }

    /** paint the portal using gl.*/
    public void paint(GL gl, GLDrawable glc){

	if (fill != null){
	    setColor(gl,fill);
	    gl.glDisable( GL.GL_TEXTURE_2D );
	}
	gl.glLineWidth(lineWidth());
	gl.glBegin( GL.GL_LINES );  //draw each wall
	for(int i = 0; i < pts2d.size(); i++) {
	    Point2D p = (Point2D)(pts2d.get(i));
	    gl.glVertex2d(p.x, p.y);
	}
	gl.glEnd(); 

        if (pts2d.size() == 4) {
	    /* Connect mid point of the two line segments with dotted line*/
	    gl.glLineStipple(1, (short) 0x0F0F);
	    gl.glEnable(GL.GL_LINE_STIPPLE);
	    gl.glLineWidth(1);
	    gl.glBegin( GL.GL_LINES ); 
	    Point2D mid =  pts2d.get(0).interp(pts2d.get(1),0.5);
	    gl.glVertex2d(mid.x, mid.y);
	    mid =  pts2d.get(2).interp(pts2d.get(3),0.5);
	    gl.glVertex2d(mid.x, mid.y);
	    gl.glEnd(); 
	    gl.glDisable(GL.GL_LINE_STIPPLE);
	}
    }

       public void render3D(GL gl, GLDrawable glc){
    }
   

    /** add a control point */
    public void addPoint(Point p) {
	if (pts2d.size() < 4) {
	    super.addPoint(p);
	}
    }
  

    /** remove specified control point - not allowed for a portal*/
    public void removePoint() {
	return;
    }
    
    //utility for contains takes an offset so we can check the two parts of the portal
    public boolean contains(int offset, int x,int y){
	Edge2D e = new Edge2D(pts2d.get(offset), pts2d.get(offset+1));
	Point2D pe = e.toLineSpace(new Point(x,y));
	return (pe.getX() > 0 && pe.getX() < 1 && Math.abs(pe.getY()) < EPSILON);
    }

    public boolean contains(int x,int y){
	return contains(0,x,y) || contains(2,x,y) ;
    }


}
