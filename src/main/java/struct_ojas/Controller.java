	package struct_ojas;

	import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
	import java.util.ArrayList;
	import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
	import javax.xml.parsers.DocumentBuilderFactory;


	import org.w3c.dom.Document;
	import org.w3c.dom.Element;
	import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

	public class Controller extends KeyAdapter implements Drawable, ActionListener{
		private MainFrame mainframe;
		private Player player1;
		private Player player2;
		private Player replicator;
		private List<String> mazeURLlist=new ArrayList<String>();
		private Maze maze;
		private int ZPMCount;
		private ReplicatorThread replicatorThread;
		private int player1ZPM;
		
		
		public Controller(){
			super();
			mainframe=new MainFrame(this);
		}
		/*
		 * Az elérhetõ labirintusok listájához ad új URL-t.
		 * @param - String, az új URL.
		 */
		public void addToURL(String s){
			mazeURLlist.add(s);
		}
		
		/*
		 * Új pályát tölt be a kapott szám alapján, ha létezik az XML fájl.
		 */
		public void loadMap(int mapNumber){
			File xmlfile=new File(mazeURLlist.get(mapNumber));
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setValidating(true);
		    dbFactory.setNamespaceAware(true);
			
			DocumentBuilder dBuilder;
			Document doc;
			
			
			
			
			try {
				dBuilder = dbFactory.newDocumentBuilder();
				
				//XML validation part//
				dBuilder.setErrorHandler(
				          new ErrorHandler() {
				            public void warning(SAXParseException e) throws SAXException {
				              System.out.println("WARNING : " + e.getMessage()); // do nothing
				            }

				            public void error(SAXParseException e) throws SAXException {
				              System.out.println("ERROR : " + e.getMessage());
				              throw e;
				            }

				            public void fatalError(SAXParseException e) throws SAXException {
				              System.out.println("FATAL : " + e.getMessage());
				              throw e;
				            }
				          }
				          );
				//xml validation part end//
				
				doc = dBuilder.parse(xmlfile);
				
				
				doc.getDocumentElement().normalize();
				
				
				NodeList nlist;
				//Initializing maze
				nlist=doc.getElementsByTagName("Maze");
				Element mazeelement=(Element)nlist.item(0);
				int sizeX=Integer.parseInt(mazeelement.getAttribute("width"));
				int sizeY=Integer.parseInt(mazeelement.getAttribute("height"));
				
				maze=new Maze(sizeX,sizeY);
				
				
				//System.out.println(maze.getField(1, 1).getPosX()+" "+maze.getField(1, 1).getPosY());
				
				//adding Holes
				nlist=doc.getElementsByTagName("Hole");
				for(int i=0;i<nlist.getLength();i++){
					Element hole=(Element)nlist.item(i);
					int x=Integer.parseInt(hole.getAttribute("posX"));
					int y=Integer.parseInt(hole.getAttribute("posY"));
					maze.addHole(x, y);
					
				}
				
				//adding ZPM-s
				
				ZPMCount=0;
				nlist=doc.getElementsByTagName("ZPM");
				for(int i=0;i<nlist.getLength();i++){
					Element zpm=(Element)nlist.item(i);
					int x=Integer.parseInt(zpm.getAttribute("posX"));
					int y=Integer.parseInt(zpm.getAttribute("posY"));
					maze.addZPM(x, y);
					ZPMCount++;
					
				}
				
				
				//add Boxes
				nlist=doc.getElementsByTagName("Box");
				for(int i=0;i<nlist.getLength();i++){
					Element box=(Element)nlist.item(i);
					int x=Integer.parseInt(box.getAttribute("posX"));
					int y=Integer.parseInt(box.getAttribute("posY"));
					int weight=Integer.parseInt(box.getAttribute("weight"));
					maze.addBox(x, y, weight);
					
				}
				
				//add Walls
				
				nlist=doc.getElementsByTagName("Wall");
				for(int i=0;i<nlist.getLength();i++){
					Element wall=(Element)nlist.item(i);
					//if it has a door, we have to find out the coordinates of the proper Scale
					if(wall.getAttribute("hasDoor").equals("true")){
						NodeList scalelist=doc.getElementsByTagName("Scale");
						String scaleid=wall.getAttribute("scaleID");
						Element scale=null;
						
						for(int j=0;j<scalelist.getLength();j++){
							Element actualScale=(Element)scalelist.item(j);
							if(actualScale.getAttribute("id").equals(scaleid)){
								scale=actualScale;
								break;
							}
						}
						
						int x=Integer.parseInt(wall.getAttribute("posX"));
						int y=Integer.parseInt(wall.getAttribute("posY"));
						int dir=Integer.parseInt(wall.getAttribute("orientation"));
						
						if(scale != null){
							int scaleX=Integer.parseInt(scale.getAttribute("posX"));
							int scaleY=Integer.parseInt(scale.getAttribute("posY"));
							maze.addWallWithDoor(x, y, dir, scaleX, scaleY);
						}
						
						
					}
					
					//else, it hasnt got a door, so we use the other function
					else{
						int x=Integer.parseInt(wall.getAttribute("posX"));
						int y=Integer.parseInt(wall.getAttribute("posY"));
						int dir=Integer.parseInt(wall.getAttribute("orientation"));
						boolean isSpecial=Boolean.parseBoolean(wall.getAttribute("isSpecial")); 
						maze.addWall(x, y, dir, isSpecial);
					
					}
				}
				
				//Add players
				player1=maze.addPlayerToCorner(1, 0);
				player2=maze.addPlayerToCorner(2, 1);
				this.replicatorThread=new ReplicatorThread(maze.getField(0, 0),this);
				replicatorThread.start();
				
				player1ZPM=0;
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			
		
			
					
			
			
		}
		
		public void setReplicator(Player rep){
			replicator=rep;
		}
		
		/*Statikus pályabetöltõ függvény a teszteléshez
		 * @param - A pálya neve
		 * @return - a létrehozott pálya
		 */
		public static Maze staticLoadMap(String s){
			Controller c=new Controller();
			c.addToURL(s);
			c.loadMap(0);
			return c.maze;
		}

		public void repaintGamePanel(){
			mainframe.repaintGamePanel();
		}
		/*
		 * (non-Javadoc)
		 * @see struct_ojas.Drawable#draw(java.awt.Graphics)
		 */

		public void draw(Graphics g, int size) {
			maze.draw(g, size);
			
			if(player1!=null && player1.isAlive){
				player1.draw(g, size);
			}
			
			if(player2!=null && player2.isAlive){
				player2.draw(g, size);
			}
			
			if(replicator!=null){
				replicator.draw(g, size);
			}
			
		}
		
		/*
		 * A gombnyomásokat kezelõ függvény.
		 * (non-Javadoc)
		 * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
		 */
		@Override
		public void keyPressed(KeyEvent e){
			
			//Nyomógomb események lekezelése
			if(player1!=null){
				
				switch(e.getKeyCode()){
				case KeyEvent.VK_W: player1.step(1);
				break;
				case KeyEvent.VK_D: player1.step(2); 
				break;
				case KeyEvent.VK_S: player1.step(3); 
				break;
				case KeyEvent.VK_A: player1.step(4); 
				break;
				case KeyEvent.VK_R: player1.pickUp(); 
				break;
				case KeyEvent.VK_T: player1.putDown(); 
				break;
				case KeyEvent.VK_SPACE: player1.fire(); 
				break;
				case KeyEvent.VK_Q: player1.changeBulletType(); 
				break;
				case KeyEvent.VK_F: player1.turn(); 
				break;
				default: break;
				}
				
			}
			//Ugyanez Player 2-nél.
			if(player2!=null){
				switch(e.getKeyCode()){
			
				case KeyEvent.VK_UP: player2.step(1); 
				break;
				case KeyEvent.VK_RIGHT: player2.step(2); 
				break;
				case KeyEvent.VK_DOWN: player2.step(3); 
				break;
				case KeyEvent.VK_LEFT: player2.step(4); 
				break;
				case KeyEvent.VK_NUMPAD1: player2.pickUp(); 
				break;
				case KeyEvent.VK_NUMPAD2: player2.putDown(); 
				break;
				case KeyEvent.VK_ENTER: player2.fire(); 
				break;
				case KeyEvent.VK_SHIFT: player2.changeBulletType(); 
				break;
				case KeyEvent.VK_NUMPAD3: player2.turn(); 
				break;
				default: break;
				}
			}
			mainframe.repaintGamePanel();
			
			//Megvizsgáljuk, nem halt -e meg mindenki.
			if(!(player1.isAlive||player2.isAlive)){
				mainframe.invokeStartingPanel();
				JOptionPane.showMessageDialog(mainframe,
					    "Everyone's dead.");
			}
			
			//Megvizsgáljuk, hogy elfogyott -e az összes ZPM, ha igen, vége a játéknak, visszatérünk a fõmenübe, és értesítjük a felhasználókat.
			int collectedZPM=0;
			if(player1!=null){
				collectedZPM+=player1.getZPMCount();
			}
			
			if(player2!=null){
				collectedZPM+=player2.getZPMCount();
			}
			
			if(collectedZPM>=this.ZPMCount){
				mainframe.invokeStartingPanel();
				StringBuilder s=new StringBuilder();
				s.append("End of game.\n");
				if(player1.getZPMCount()>player2.getZPMCount()){
					s.append("Player 1 won. Collected ZPM: ");
					s.append(player1.getZPMCount());
					
				}
				else if(player1.getZPMCount()<player2.getZPMCount()){
					s.append("Player 2 won. Collected ZPM: ");
					s.append(player2.getZPMCount());
				}
				else{
					s.append("Drawn. Collected ZPM:");
					s.append(player2.getZPMCount());
				}
				
				JOptionPane.showMessageDialog(mainframe,
					    s);
				replicatorThread.stopThread();
			}
			
			//"Ha az ezredes begyûjt 2 zpm-et, keletkezik egy új random helyen."
			if(player1.getZPMCount()%2==0&&player1.getZPMCount()!=0&&player1ZPM!=player1.getZPMCount()){
				maze.addRandomZPM();
				ZPMCount++;
				player1ZPM=player1.getZPMCount();
			}
			
			
			

	}
		@Override
		public void finalize(){
			this.replicatorThread.stopThread();
		}


		public void actionPerformed(ActionEvent bPressed) {
			if("start".equals(bPressed.getActionCommand())){
				mainframe.invokeMapChoosingPanel();
			}
			
			else if("exit".equals(bPressed.getActionCommand())){
				mainframe.removeAll();
				mainframe.dispose();
				mainframe=null;
				
				
			}
			
			else if("first".equals(bPressed.getActionCommand())){
				this.loadMap(0);
				mainframe.invokeGamePanel();
			}
			
			else if("second".equals(bPressed.getActionCommand())){
				this.loadMap(1);
				mainframe.invokeGamePanel();
			}
			
			else if("third".equals(bPressed.getActionCommand())){
				this.loadMap(2);
				mainframe.invokeGamePanel();
			}
			
		}
}


