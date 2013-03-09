/** This class represents a wall */
import java.awt.*;
import java.util.*;
import javax.media.opengl.*;
import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;

public class Wall extends FPPolygon {


    public String extraName(int i){
	return i==0 ? "Base" : "Top";
    }

    public float lineWidth() {
	return 4.0f;
    }

    /** paint the wall using gl.*/
    public void paint(GL gl, GLDrawable glc){

	if (fill != null){
	    setColor(gl,fill);
	    gl.glDisable( GL.GL_TEXTURE_2D );
	}
	gl.glLineWidth(lineWidth());
	gl.glBegin( GL.GL_LINES );  //draw the wall
	for(int i = 0; i < 2; i++) {
	    Point2D p = (Point2D)(pts2d.get(i));
	    gl.glVertex2d(p.x, p.y);
	}
	gl.glEnd(); 
    }

  
      public void render3D(GL gl, GLDrawable glc){
    }

    /** add a control point */
    public void addPoint(Point p) {
	if (pts2d.size() < 2) {
	    super.addPoint(p);
	}
    }
  

    /** remove specified control point */
    public void removePoint() {
	return;
    }

    public boolean contains(int x,int y){
	Edge2D e = new Edge2D((Point2D)pts2d.get(0),
			      (Point2D)pts2d.get(1));
	Point2D pe = e.toLineSpace(new Point(x,y));
	return (pe.getX() > 0 && pe.getX() < 1 && Math.abs(pe.getY()) < EPSILON);
    }


}
