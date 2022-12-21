package spaceinvaders;

import java.io.*; 
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;

public class SpaceInvaders extends JFrame implements Runnable {
    static final int WINDOW_WIDTH = 500;
    static final int WINDOW_HEIGHT = 800;
    final int XBORDER = 80;
    final int YBORDER = 60;
    final int YTITLE = 25;
    boolean animateFirstTime = true;
    int xsize = -1;
    int ysize = -1;
    Image image;
    Graphics2D g;
    boolean gameOver;
    boolean youWin;
    int cannonXPos;
    int cannonYPos;    
    int currentScore;
    int maxScore; 
    int invaderXDir;
    int invaderScore[];
      
    // int invaderXPos;
    // int invaderYPos;
    int numInvaders = 5;
    int invaderXPos[] = new int[numInvaders];
    int invaderYPos[] = new int[numInvaders];
    boolean InvaderVisible[] = new boolean[numInvaders];
    
    int numCannonBalls = 10;
    int currentCannonBallIndex;
    int cannonballXPos[] = new int[numCannonBalls];
    int cannonballYPos[] = new int[numCannonBalls];
    boolean cannonBallVisible[] = new boolean[numCannonBalls];
    
    static SpaceInvaders frame;
    public static void main(String[] args) {
        frame = new SpaceInvaders();
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public SpaceInvaders() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    //left button
                    cannonBallVisible[currentCannonBallIndex] = true;                   
                    cannonballXPos[currentCannonBallIndex] = cannonXPos;
                    cannonballYPos[currentCannonBallIndex] = cannonYPos;
                    
                    if(currentCannonBallIndex > 9)
                    {
                    currentCannonBallIndex = 0;
                    }
                    else
                    currentCannonBallIndex++;
                    
                    if (gameOver || youWin)
                    return;
                }
                if (e.BUTTON3 == e.getButton())
                    //right button
                    
                    
                    
                    reset();
                  
                
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        
         if(gameOver || youWin)
             return;
        cannonXPos = e.getX()- getX(0);
        
          
          
        repaint();
      }
    });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.VK_UP == e.getKeyCode()) {
                } else if (e.VK_DOWN == e.getKeyCode()) {
                } else if (e.VK_LEFT == e.getKeyCode()) {
                } else if (e.VK_RIGHT == e.getKeyCode()) {
                }
                repaint();
            }
        });
        init();
        start();
    }
    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }

 

////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || xsize != getSize().width || ysize != getSize().height) {
            xsize = getSize().width;
            ysize = getSize().height;
            image = createImage(xsize, ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
//fill background
        g.setColor(Color.cyan);
        g.fillRect(0, 0, xsize, ysize);

        int x[] = {getX(0), getX(getWidth2()), getX(getWidth2()), getX(0), getX(0)};
        int y[] = {getY(0), getY(0), getY(getHeight2()), getY(getHeight2()), getY(0)};
//fill border
        g.setColor(Color.white);
        g.fillPolygon(x, y, 4);
// draw border
        g.setColor(Color.red);
        g.drawPolyline(x, y, 5);

        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }

      
        DrawCannon(cannonXPos,cannonYPos,0,1,1,Color.red);
        
       g.setColor(Color.BLACK);
       StringTransform("Score= "+currentScore,"Arial",200, 600,0,1.3,1.3);
       
       g.setColor(Color.BLACK);
       StringTransform("High Score= "+maxScore,"Arial",200, 550,0,1.3,1.3);
       
       
       if(gameOver ==true)
       {
       g.setColor(Color.BLACK);
       StringCentered(200,250,"Game Over!","Arial",50);
       }
       
       if(youWin ==true)
       {
       g.setColor(Color.BLACK);
       StringCentered(200,250,"You Win!","Arial",50);
       }
       
       
       if(gameOver == false)  
       {
        for(int i=0;i<numCannonBalls;i++)
        {
            if(cannonBallVisible[i])
            DrawCannonBall(cannonballXPos[i],cannonballYPos[i],0,2,2,Color.black);            
            
        }
       }
        for(int i=0;i<numInvaders;i++)
            {
            if(InvaderVisible[i] == true)
            DrawInvader(invaderXPos[i],invaderYPos[i],-45,1.0,1.0,Color.green);
            }
        
        gOld.drawImage(image, 0, 0, null);
    }


////////////////////////////////////////////////////////////////////////////  
    public void RectTransform(int xpos,int ypos,double rot,double xscale,double yscale,boolean fill)
    {      
        int xposMod = getX(xpos);
        int yposMod = getYNormal(ypos);  
        g.translate(xposMod,yposMod);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );
        
        if (fill)
        {
            g.fillRect(-10,-10,20,20);   
        }
        else
        {
            g.drawRect(-10,-10,20,20);               
        }
        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xposMod,-yposMod);        
    }    
    
    public void OvalTransform(int xpos,int ypos,double rot,double xscale,double yscale,boolean fill)
    {      
        int xposMod = getX(xpos);
        int yposMod = getYNormal(ypos);  
        g.translate(xposMod,yposMod);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );
        
        if (fill)
        {
            g.fillOval(-10,-10,20,20);   
        }
        else
        {
            g.drawOval(-10,-10,20,20);               
        }
        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xposMod,-yposMod);        
    }        
    
    
    public void ArcTransform(int start,int sweep,int xpos,int ypos,double rot,double xscale,double yscale,boolean fill)
    {      
        int xposMod = getX(xpos);
        int yposMod = getYNormal(ypos);  
        g.translate(xposMod,yposMod);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );
        
        if (fill)
        {
            g.fillArc(-10,-10,20,20,start,sweep);   
        }
        else
        {
            g.drawArc(-10,-10,20,20,start,sweep);               
        }
        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xposMod,-yposMod);        
    }        
    
    public void StringTransform(String text,String font,int xpos,int ypos,double rot,double xscale,double yscale)
    {      
        int xposMod = getX(xpos);
        int yposMod = getYNormal(ypos);  
        g.translate(xposMod,yposMod);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );
        
        g.setFont (new Font (font,Font.PLAIN, 10)); 
        int width = g.getFontMetrics().stringWidth(text);
        int height = g.getFontMetrics().getHeight();               
        g.drawString(text, -width/2, height/4 );                

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xposMod,-yposMod);        
    }       
    
////////////////////////////////////////////////////////////////////////////////

    public void RectCentered(int xpos,int ypos,int width,int height,boolean fill)
    {
        xpos = xpos - width/2;
        ypos = ypos + height/2;
        xpos = getX(xpos);
        ypos = getYNormal(ypos);
        if (fill)
        {
            g.fillRect(xpos,ypos,width,height);              
        }
        else
        {
            g.drawRect(xpos,ypos,width,height);              
        }        
    }    
    
    public void OvalCentered(int xpos,int ypos,int width,int height,boolean fill)
    {
        xpos = xpos - width/2;
        ypos = ypos + height/2;
        xpos = getX(xpos);
        ypos = getYNormal(ypos);
        if (fill)
        {
            g.fillOval(xpos,ypos,width,height);              
        }
        else
        {
            g.drawOval(xpos,ypos,width,height);              
        }        
    }        
    
    public void ArcCentered(int xpos,int ypos,int width,int height,int start,int sweep,boolean fill)
    {
        xpos = xpos - width/2;
        ypos = ypos + height/2;
        xpos = getX(xpos);
        ypos = getYNormal(ypos);
        if (fill)
        {
            g.fillArc(xpos,ypos,width,height,start,sweep);              
        }
        else
        {
            g.drawArc(xpos,ypos,width,height,start,sweep);              
        }        
    }        
    
    public void StringCentered(int xpos,int ypos,String text,String font,int size)
    {
        g.setFont (new Font (font,Font.PLAIN, size)); 
        int width = g.getFontMetrics().stringWidth(text);
        int height = g.getFontMetrics().getHeight();
        xpos = xpos - width/2;
        ypos = ypos - height/4;
        xpos = getX(xpos);
        ypos = getYNormal(ypos);
        g.drawString(text, xpos, ypos);           
    }    
    
    
////////////////////////////////////////////////////////////////////////////////

    public void RectOriginCentered(int xpos,int ypos,int width,int height,boolean fill)
    {
        xpos = xpos - width/2;
        ypos = -(ypos + height/2);
        if (fill)
        {
            g.fillRect(xpos,ypos,width,height);              
        }
        else
        {
            g.drawRect(xpos,ypos,width,height);              
        }        
    }    

    public void OvalOriginCentered(int xpos,int ypos,int width,int height,boolean fill)
    {
        xpos = xpos - width/2;
        ypos = -(ypos + height/2);
        if (fill)
        {
            g.fillOval(xpos,ypos,width,height);              
        }
        else
        {
            g.drawOval(xpos,ypos,width,height);              
        }        
    }    

    public void ArcOriginCentered(int xpos,int ypos,int width,int height,int start,int sweep,boolean fill)
    {
        xpos = xpos - width/2;
        ypos = -(ypos + height/2);
        if (fill)
        {
            g.fillArc(xpos,ypos,width,height,start,sweep);              
        }
        else
        {
            g.drawArc(xpos,ypos,width,height,start,sweep);              
        }        
    }    
            
////////////////////////////////////////////////////////////////////////////  
    public void RectOriginTransform(int xpos,int ypos,double rot,double xscale,double yscale,boolean fill)
    {
        int xposMod = xpos;
        int yposMod = -ypos;
        g.translate(xposMod,yposMod);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );
        if (fill)
            g.fillRect(-10,-10,20,20);   
        else
            g.drawRect(-10,-10,20,20);               
        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xposMod,-yposMod);        
    }         
        
    public void OvalOriginTransform(int xpos,int ypos,double rot,double xscale,double yscale,boolean fill)
    {
        int xposMod = xpos;
        int yposMod = -ypos;
        g.translate(xposMod,yposMod);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );
        
        if (fill)
        {
            g.fillOval(-10,-10,20,20);   
        }
        else
        {
            g.drawOval(-10,-10,20,20);               
        }
        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xposMod,-yposMod);        
    }         
    public void ArcOriginTransform(int start,int sweep,int xpos,int ypos,double rot,double xscale,double yscale,boolean fill)
    {
        int xposMod = xpos;
        int yposMod = -ypos;
        g.translate(xposMod,yposMod);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );
        
        if (fill)
        {
            g.fillArc(-10,-10,20,20,start,sweep);   
        }
        else
        {
            g.drawArc(-10,-10,20,20,start,sweep);               
        }
        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xposMod,-yposMod);        
    }         
                    
    public void StringOriginTransform(String text,String font,int xpos,int ypos,double rot,double xscale,double yscale)
    {
        int xposMod = xpos;
        int yposMod = -ypos;
        g.translate(xposMod,yposMod);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );
         
        g.setFont (new Font (font,Font.PLAIN, 10)); 
        int width = g.getFontMetrics().stringWidth(text);
        int height = g.getFontMetrics().getHeight();               
        g.drawString(text, -width/2, height/4 );                

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xposMod,-yposMod);        
    }         
    
    public void PolygonOrigin(int xvals[],int yvals[],boolean fill)
    {
        for (int i=0;i<xvals.length;i++)
        {
            yvals[i] = -yvals[i];
        }
        if (fill)
        {
            g.fillPolygon(xvals, yvals, xvals.length);
        }
        else
        {
            g.drawPolygon(xvals, yvals, xvals.length);            
        }
    }
        
////////////////////////////////////////////////////////////////
    public void DrawCannon(int xpos,int ypos,double rot,double xscale,double yscale,Color color)
    {
        int xposMod = getX(xpos);
        int yposMod = getYNormal(ypos);
        g.translate(xposMod,yposMod);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.setColor(color);
        int xvals[] = {0,20,-20};
        int yvals[] = {30,20,20};
        PolygonOrigin(xvals,yvals,true);
        RectOriginCentered(0,0,40,40,true);
       
        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xposMod,-yposMod);
    }   
    
    public void DrawCannonBall(int xpos,int ypos,double rot,double xscale,double yscale,Color color)
    {
        int xposMod = getX(xpos);
        int yposMod = getYNormal(ypos);
        g.translate(xposMod,yposMod);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.setColor(Color.black);
        OvalOriginCentered(0,0,10,10,true);
       
        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xposMod,-yposMod);
    }   
    public void DrawInvader(int xpos,int ypos,double rot,double xscale,double yscale,Color color)
    {
        int xposMod = getX(xpos);
        int yposMod = getYNormal(ypos);
        g.translate(xposMod,yposMod);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.setColor(Color.green);
        RectOriginCentered(0,0,20,20,true);
       
        g.setColor(Color.green);
        RectOriginTransform(10,10,45,1.0,1.0,true);
        
        g.setColor(Color.green);
        RectOriginTransform(10,10,-90,1.0,1.0,true);
        
        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xposMod,-yposMod);
    }   
    
 

////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = .04;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {

       cannonXPos = getWidth()/2;
       cannonYPos = 0;       
       currentCannonBallIndex = 0;
       currentScore = 0;
       gameOver = false;
       youWin = false;
       invaderXDir = 2;
       
       
       for(int i=0;i<numCannonBalls;i++)
       {
       cannonballXPos[i] = -300;
       cannonballYPos[i] = 0;
       cannonBallVisible[i] = false;
       }    
       
       
       
       for(int i=0;i<numInvaders;i++)
       {
       invaderXPos[i] = (int)(Math.random()*getWidth2())+0;
       invaderYPos[i] = (int)(Math.random()* getHeight2()/2)+getHeight2()/2;
       InvaderVisible[i] = true;
       }       
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {
        if (animateFirstTime) {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height) {
                xsize = getSize().width;
                ysize = getSize().height;
            }
         
          
            
            reset();
        }
        
       
        for(int i=0;i<numInvaders;i++)
        {
        for(int j=0;j<numCannonBalls;j++)
        {              
          if(cannonBallVisible[j] & InvaderVisible[i])
        {
            if(cannonballXPos[j] < invaderXPos[i]+10 && 
               cannonballXPos[j] > invaderXPos[i]-10 &&
               cannonballYPos[j] < invaderYPos[i]+10 &&
               cannonballYPos[j] > invaderYPos[i]-10)                    
            {  
         //   InvaderVisible[i] = false;
            invaderXPos[i] =(int)(Math.random()*getWidth2())+0;
            invaderYPos[i] = getHeight2();
            cannonBallVisible[j] = false;  
            currentScore++; 
            
            if(currentScore >= numInvaders)
             youWin=true;  
            
            if(currentScore > maxScore)
            maxScore = currentScore;
            if (gameOver || youWin)
                return;
            }
           
        }
        
        }
        }
   /*     
         for(int i=0;i<numInvaders;i++)
         {
         if(invaderXPos[i] < getWidth2() || invaderXPos[i] > getWidth2() )
         {
         invaderXPos[i] = +invaderXDir;
         
         }
         
         
         
         }
   */      
        
        
        
        
        for(int i=0;i<numInvaders;i++)
        {        
        if(InvaderVisible[i])
        invaderYPos[i]-=2;             
        if(invaderYPos[i]< 0)
        {
        gameOver = true;
            if (gameOver || youWin)
                return;
        }
        }
        
            
            
        
        for(int i=0;i<numCannonBalls;i++)
        {
          if(cannonBallVisible[i])
           cannonballYPos[i]+=5; 
        if(cannonballYPos[i] > getHeight2())
        {
        cannonBallVisible[i] = false;    
        }       
        }
        
    }
   

////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }
/////////////////////////////////////////////////////////////////////////
    public int getX(int x) {
        return (x + XBORDER);
    }

    public int getY(int y) {
        return (y + YBORDER + YTITLE);
    }

    public int getYNormal(int y) {
        return (-y + YBORDER + YTITLE + getHeight2());
    }
    
    
    public int getWidth2() {
        return (xsize - getX(0) - XBORDER);
    }

    public int getHeight2() {
        return (ysize - getY(0) - YBORDER);
    }
}
