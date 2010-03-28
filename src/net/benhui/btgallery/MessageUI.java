package net.benhui.btgallery;

import javax.microedition.lcdui.*;
import java.util.*;
/**
 *
 *
 * A screen to display text string on screen.
 * <p>Description: This is a canvas screen to display the current messages in
 * virtual chat room. Only the latest messages are displayed. If there are  more
 * messages than those can fit into one screen, old messages are roll off from
 * the upper edge. User is not able to scroll back to see old messages, however,
 * the old messages is still available in msgs Vector until a clear command
 * is invoked. When a clear command is invoked, all message will be removed
 * from msgs vector. </p>
 * <p>Copyright: Copyright (c) 2003</p>
 *
 * @author Ben Hui (www.benhui.net)
 * @version 1.0
 */
public class MessageUI extends Canvas
{

  // list of available message to display
  public Vector msgs = new Vector();
  // current message idx
  int midx = 0;
  // graphic width and height
  int w, h;
  // font height
  int fh;
  // font to write message on screen
  Font f;

  int x0=0, y0=0;

  public int bookmarkId = 1;

  // 0 = BTBroserMain
  // 1 = ListServicesUI
  // 2 = ServiceDetailsUI
  public int backTo = 0;

  public MessageUI()
  {
    addCommand(new Command("Back", Command.BACK, 1));
  }


  protected void paint(Graphics g)
  {

    if ( f == null )
    {
      // cache the font and width,height value
      // when it is used the first time
      f = Font.getFont(  Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL );
      w = this.getWidth();
      h = this.getHeight();
      fh = f.getHeight();
    }
    //
    // detemine midx based on the screen height and number of message
    /*
    midx = msgs.size() - (h/fh) ;
    if ( midx < 0 )
      midx = 0;
        */
    int y = fh; // 1st line y value

    // message will be rendered in black color, on top of white backgound
    g.setColor( 255, 255, 255 );
    g.fillRect( 0, 0, w, h );
    g.setColor( 0, 0, 0 );
    g.setFont( f );

    g.translate(-x0, -y0);

    // render the messages on screen
    for ( int i= midx; i< msgs.size(); i++ )
    {
      String s = (String)msgs.elementAt(i);
      g.drawString( s, 0, y, Graphics.BASELINE | Graphics.LEFT );
      y += fh;
    }

  }

  public void keyPressed( int key )
  {
    if ( getGameAction( key ) == Canvas.RIGHT )
    {
      x0+=50;
    } else if ( getGameAction( key ) == Canvas.LEFT )
    {
      x0-=50;
    } else if ( getGameAction( key ) == Canvas.UP )
    {
      // note: change this from 50 to 100 if you want to scroll faster
      y0-=50;
    } else if ( getGameAction( key ) == Canvas.DOWN )
    {
      // note: change this from 50 to 100 if you want to scroll faster
      y0+=50;
    }
    repaint();
  }

  public void clear()
  {
    msgs.removeAllElements();
    midx = 0;
    x0 = 0;
    y0 = 0;
    bookmarkId = 1;
    repaint();
  }

  public void add( String s )
  {
    msgs.addElement( s );
    repaint();
  }

  public void addNoRepaint( String s )
  {
    msgs.addElement( s );
  }
}
