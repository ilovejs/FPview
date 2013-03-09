/** This class represents a door */
import java.awt.*;
import java.util.*;
import javax.media.opengl.*;
import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;

public class Door extends Wall {

    public float lineWidth() {
	return 2.0f;
    }


    /** paint this door into g.*/
    public void paint(GL gl, GLDrawable glc){

	if (fill != null){
	    setColor(gl,fill);
	    gl.glDisable( GL.GL_TEXTURE_2D );
	}
	gl.glLineWidth(lineWidth());
	gl.glBegin( GL.GL_LINE_STRIP );  //draw the wall

	Point2D centre = (Point2D)(pts2d.get(0));
	Point2D rad = (Point2D)(pts2d.get(1));
	double dx = rad.x - centre.x;
	double dy = rad.y - centre.y;
	double radius = Math.sqrt(dx*dx + dy*dy);
	double theta = Math.atan2(dy,dx);

	gl.glVertex2d(centre.x, centre.y);
	for (int i = 0; i< 10; i++){
	    double angle = theta + (i/10.0)*Math.PI/2.0;
	    gl.glVertex2d(centre.x+radius*Math.cos(angle),
			  centre.y+radius*Math.sin(angle));
	}
	gl.glEnd(); 
    }

    public void render3D(GL gl, GLDrawable glc){
    }

}
