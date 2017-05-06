package struct_ojas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame{
	private JButton startGameButton;
	private JButton exitButton;
	private JButton firstMapButton;
	private JButton secondMapButton;
	private JButton thirdMapButton;
	private JPanel startingPanel;
	private JPanel mapChoosingPanel;
	private GamePanel gamePanel;
	private Controller controller;
	private int size;
	
	public MainFrame(Controller c){
		super("StarPortal");
		this.controller = c;
		initialize();
		
	}
	
	/*
	 * TODO!!!
	 */
	private void initialize(){
		Properties prop=new Properties();
		try {
			prop.load(new FileInputStream("src/main/resources/conf.prop"));
			size=Integer.parseInt(prop.getProperty("size"));
		} catch (FileNotFoundException e) {
			size=10;
			e.printStackTrace();
		} catch (IOException e) {
			size=10;
			e.printStackTrace();
		}
		
		
		this.setMinimumSize(new Dimension(300,300));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		GridLayout gl=new GridLayout(2,1);
		//A kezdõképernyp beállítása, 300*300-as méretben.
		
		this.startingPanel=new JPanel();
		startingPanel.setMinimumSize(new Dimension(300,300));
		startingPanel.setLayout(gl);
		
		this.startGameButton=new JButton("Start Game");
		startGameButton.setActionCommand("start");
		startGameButton.addActionListener(controller);
		startingPanel.add(startGameButton);
		
		
		this.exitButton=new JButton("Exit");
		exitButton.setActionCommand("exit");
		exitButton.addActionListener(controller);
		startingPanel.add(exitButton);
		
		
		//A pályaválasztó panel beállítása
		this.mapChoosingPanel=new JPanel();
		GridLayout g2=new GridLayout(3,1);
		mapChoosingPanel.setLayout(g2);
		
		this.firstMapButton=new JButton("1. map");
		firstMapButton.setActionCommand("first");
		firstMapButton.addActionListener(controller);
		mapChoosingPanel.add(firstMapButton);
		
		this.secondMapButton=new JButton("2. map");
		secondMapButton.setActionCommand("second");
		secondMapButton.addActionListener(controller);
		mapChoosingPanel.add(secondMapButton);
		
		this.thirdMapButton=new JButton("3. map");
		thirdMapButton.setActionCommand("third");
		thirdMapButton.addActionListener(controller);
		mapChoosingPanel.add(thirdMapButton);
		
		//gamePanel inicializálása
		gamePanel=new GamePanel(controller,size);
		gamePanel.setMinimumSize(new Dimension(size*100, size*100));
		this.setVisible(true);
		this.pack();
		
		
		this.invokeStartingPanel();
		
		
		
		
	}
	
	/*
	 * Láthatóvá teszi és elõtérbe hozza a kezdõképernyõt
	 */
	public void invokeStartingPanel(){
		mapChoosingPanel.setVisible(false);
		gamePanel.setVisible(false);
		//gamePanel.removeKeyListener(controller);
		this.getContentPane().removeAll();
		startingPanel.setVisible(true);
		this.setMinimumSize(new Dimension(300,300));
		this.setSize(300, 300);
		this.getContentPane().add(startingPanel, BorderLayout.CENTER);
		this.pack();
		
	}
	
	/*
	 * Láthatóvá teszi és elõtérbe hozza a pályaválasztó képernyõt.
	 */
	public void invokeMapChoosingPanel(){
		startingPanel.setVisible(false);
		gamePanel.setVisible(false);
		gamePanel.removeKeyListener(controller);
		this.getContentPane().removeAll();
		mapChoosingPanel.setVisible(true);
		this.getContentPane().add(mapChoosingPanel, BorderLayout.CENTER);
		this.pack();
		
		
	}
	
	/*
	 * Láthatóvá teszi és elõtérbe hozza a játék képrenyõjét, és meghívja rajta a repaint() fv-t.
	 */
	public void invokeGamePanel(){
		mapChoosingPanel.setVisible(false);
		startingPanel.setVisible(false);
		this.getContentPane().removeAll();
		gamePanel.addKeyListener(controller);
		this.getContentPane().add(gamePanel, BorderLayout.CENTER);
		gamePanel.setVisible(true);
		this.setMinimumSize(new Dimension(size*100+20,size*100+20));
		
		gamePanel.repaint();
		gamePanel.requestFocusInWindow();
	}
	
	/*
	 * A játékpanel kívülrõl hívható újrarajzoló függvénye.
	 */
	public void repaintGamePanel(){
		gamePanel.repaint();
	}
}
