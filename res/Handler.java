import java.awt.Graphics;
import java.util.LinkedList;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * @author EdmundYuYi Tan
 *
 */
public class Handler {
  
  LinkedList<GameObject> object = new LinkedList<GameObject>();
  
  public void tick(int frameWidth, int frameHeight) {
    for (int i = 0; i < object.size(); i++) {
      GameObject temp = object.get(i);
      temp.tick();
      checkCollision(frameWidth, frameHeight, temp);
      
    //we only care about things colliding with the environment,
      //not the environment colliding with things
      if(temp.id != ID.Environment) {
        for(GameObject g : object) {
          checkObjectCollision(temp, g);
        }
      }
    }
  }
  
  public void drawMenu(Graphics2D g, double width, double height, Point locOnScreen) {
    //check to see if the cursor is hovering quit or start buttons
    int[] bound = {(int)width/2 - 150, (int)width/2 + 150, (int)height/2, (int)height/2 + 50}; //x1, x2, y1, y2
    double x = MouseInfo.getPointerInfo().getLocation().getX() - locOnScreen.getX();
    double y = MouseInfo.getPointerInfo().getLocation().getY() - locOnScreen.getY();
    BufferedImage punk = null;
    BufferedImage biker;
    try {
      punk = ImageIO.read(getClass().getResourceAsStream("punk_title.png"));
    } catch(Exception e) {
      e.printStackTrace();
    }

    if(x > bound[0] && x < bound[1] && y > bound[2] && y < bound[3]) {
      g.setColor(Color.GREEN);
      g.fillRect((int)width/2 - 155, (int)height/2 - 5, 310, 60); //border for start button
    } else if(x > bound[0] && x < bound[1] && y > bound[2] + 150 && y < bound[3] + 150) {
      g.setColor(Color.RED);
      g.fillRect((int)width/2 - 155, (int)height/2 - 5 + 150, 310, 60); //border for quit button
    }

    g.setColor(Color.WHITE);
    g.fillRect((int)width/2 - 150, (int)height/2, 300, 50); //start button
    g.fillRect((int)width/2 - 150, (int)height/2 + 150, 300, 50); //quit button
    g.setColor(Color.BLACK);
    g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 32));
    g.drawString("START", (int)width/2 - g.getFontMetrics().stringWidth("START")/2, (int)height/2 + 25); //start button
    g.drawString("QUIT", (int)width/2 - g.getFontMetrics().stringWidth("QUIT")/2, (int)height/2 + 175); //start button
    g.drawImage(punk, 100, 100, null);//punk title screen image
  }
  
  public void render(Graphics g) {
    GameObject temp;
    for (int i = 0; i < object.size(); i++) {
      temp = object.get(i);
      temp.render(g);
    }
  }

  public void handleKeyPress(KeyEvent k) {
    GameObject temp;
    for (int i = 0; i < object.size(); i++) {
      temp = object.get(i);
      if(temp.id == ID.Player) {
        Player p = (Player)temp;
        p.keyPressed(k);
      }
    }
  }

  public void handleKeyRelease(KeyEvent k) {
    GameObject temp;
    for (int i = 0; i < object.size(); i++) {
      temp = object.get(i);
      if(temp.id == ID.Player) {
        Player p = (Player)temp;
        p.keyReleased(k);
      }
    }
  }
  
  public void addObject(GameObject object) {
    this.object.add(object);
  }
  
  public void removeObject(GameObject object) {
    this.object.remove(object);
  }
  
  public void checkCollision(int frameWidth, int frameHeight, GameObject temp) {      
    if (temp.getBounds().getX() <= 0) {
      if (temp.getId() == ID.Attack) {
        this.removeObject(temp);
      }
      temp.setX(0);
    }
    
    if (temp.getBounds().getY() <= 0) {
      if (temp.getId() == ID.Attack) {
        this.removeObject(temp);
      }
      temp.setY(0);
    }    
    
    if (temp.getBounds().getX() >= frameWidth - 13 - temp.getWidth()) {
      if (temp.getId() == ID.Attack) {
        this.removeObject(temp);
      }
      temp.setX(frameWidth - 13 - temp.getWidth());
    }
    
    if (temp.getBounds().getY() >= frameHeight - 37 - temp.getHeight()) {
      if (temp.getId() == ID.Attack) {
        this.removeObject(temp);
      }
      temp.setY(frameHeight - 37 - temp.getHeight());
    }
    
    
    
  }
  
  /**
   * method to check collision with other game objects
   */
  public void checkObjectCollision(GameObject g1, GameObject g2) {
    //just for rectangles at the moment
    if((g1.getBounds().getY() + g1.getHeight()) > g2.getBounds().getY() && g1.getBounds().getY() < g2.getBounds().getY() + g2.getHeight()) { //check y
      if(g1.getBounds().getX() + g1.getBounds().getWidth() > g2.getBounds().getX() && g1.getBounds().getX() < g2.getBounds().getX() + g2.getWidth() && g2.getId() == ID.Environment) {
        g1.setY((int)g2.getBounds().getY() - g1.getHeight());
      }  
    }
  }

}