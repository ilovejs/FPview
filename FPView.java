/** 
 *  Program to display floorplans designed using FPED
 *
 *      The code is copyright (C) Waleed Kadous 2001 
 *      and copyright (C) Tim Lambert 2010
 *   
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
  */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;

public class FPView extends Frame 
    implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {

    FPShapeList shapeList = null; // List of shapes that we have to render

    int appHeight = 400; // Height of the browsing window
    int appWidth = 400; // Width of the browsing window

    int prevx = 0; // Previously observed x value of mouse click.  
    int prevy = 0; // Previously observed y value of mouse click.  

    GLCanvas glc = null;

    Vector3D viewpos = new Vector3D(10, 60, 30); // Default viewing position
    Vector3D viewdir = new Vector3D(1,0,1);  // Default viewing direction (diagonal across world)

    Avatar avatar; // 

    static final int FIRST_PERSON_VIEW = 1; 
    static final int THIRD_PERSON_VIEW = 2;
    int viewType = THIRD_PERSON_VIEW; // Is view from avatar, or is it 3rd person view?

    public FPView(String filename){

        super("Floor Plan Viewer: " + filename); // Create window with title 
        shapeList = new FPShapeList();
        try {
            shapeList.read(filename);
        }
        catch(Exception e){
            System.err.println("Could not load the floor plan " + filename);
            e.printStackTrace();
            System.exit(1);
        }


        setLayout(new BorderLayout());

	glc = new GLCanvas();
        
	// quit if the window closes
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    System.exit(0);
		}
	    });

        // More initialisation.
        glc.addGLEventListener(this);
        glc.addKeyListener(this);
        glc.addMouseListener(this);
        glc.addMouseMotionListener(this);
        
        add("Center", glc);
        pack();
        setSize(appHeight,appWidth);

        //  Run animation in a tight loop. 
        Animator animator = new FPSAnimator(glc, 30); //animate at 30 fps

        animator.start();

        setVisible(true);

    }
    
    /* Main simply created a new window. */
    public static void main(String[] args){
        FPView dsview = new FPView(args[0]);
    }
    

    /** Methods that must be implemented for Interface fulfillment   */


    /** For GLEventListener */
    public void init(GLAutoDrawable drawable){
	GL gl = drawable.getGL();
      
	avatar = new Avatar(new Vector3D(0, Avatar.height, 75), new Vector3D(1,0,0));

    }
    

    public void reshape(GLAutoDrawable drawable, int x, int y,
			int width, int height){
	GL gl = drawable.getGL();
        GLU glu = new GLU();
        gl.glViewport(0,0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(60, 1, 1, 10000);
        
        gl.glEnable(GL.GL_DEPTH_TEST);

        appWidth = width;
        appHeight = height;

    }

    public void display(GLAutoDrawable drawable){
        
	GL gl = drawable.getGL();
        GLU glu = new GLU();
	// automatically make normals length 1
        gl.glEnable(GL.GL_NORMALIZE);

        // Use two-sided lighting .
        gl.glLightModeli(GL.GL_LIGHT_MODEL_TWO_SIDE, 1);

        gl.glShadeModel(GL.GL_SMOOTH); // Use Gouraud shading. 
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL.GL_COLOR_MATERIAL);

        // Clear the matrix stack. 
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

        // If it's a user view, set it from viewpos and viewdir.
        if(viewType == THIRD_PERSON_VIEW){
            setView(glu, viewpos, viewdir);
        }
       
        // ... otherwise use information from avatar to do it. 
        else {
	    setView(glu, avatar.currentPos(), avatar.currentDir());
        }
        

        gl.glPushMatrix();
        // Render the shapes. 
        shapeList.render3D(gl, glc);

        // Render the avatar. 
	avatar.advancePos(shapeList);
        if(viewType == THIRD_PERSON_VIEW){
            avatar.render3D(gl, glc);
        }
        
        gl.glPopMatrix();
        
    }

    /** This method handles things if display depth changes */
    public void displayChanged(GLAutoDrawable drawable,
			       boolean modeChanged,
			       boolean deviceChanged){
    }

    

    /** For KeyListener */
    public void keyTyped(KeyEvent evt){
        
    }
    
    public void keyReleased(KeyEvent evt){
        
    }

    public void keyPressed(KeyEvent evt){

        if(evt.getKeyChar() == 'f'){
            if(viewType == FIRST_PERSON_VIEW){
                viewType = THIRD_PERSON_VIEW;
            }
            else {
                viewType = FIRST_PERSON_VIEW;
            }
        }

        if(evt.getKeyChar() == 'p'){
            System.out.println(new ViewPoint(viewpos,viewdir));
        }
        if(evt.getKeyChar() == 'w'){
            avatar.move(4);
        }
        if(evt.getKeyChar() == 's'){
            avatar.move(-4);
        }
        if(evt.getKeyChar() == 'q') System.exit(0);
        
    }

    //Set view, given eye and view direction
    public void setView(GLU glu, Vector3D pos, Vector3D dir){
        Vector3D look = pos.add(dir);
	glu.gluLookAt(pos.x, pos.y, pos.z, look.x, look.y, look.z, 0, 1, 0); 
    }


    /** For MouseListener and MouseMotionListener */
    
    public void mouseMoved(MouseEvent evt){

    }
    
    public void mouseEntered(MouseEvent evt){
        
    }
    public void mouseExited(MouseEvent evt){
        
    }
    public void mouseClicked(MouseEvent evt){

    }
    public void mouseReleased(MouseEvent evt){

    }
    public void mousePressed(MouseEvent evt){
        prevx = evt.getX();
        prevy = evt.getY();
    }

    // Do view changing.
    public void mouseDragged(MouseEvent evt){
    }

}
