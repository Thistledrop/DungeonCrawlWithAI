import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Maze extends JPanel implements KeyListener
{
	private static final long serialVersionUID = 1L;
	public int tileSize = 100;
	public static final int SLEEP_TIME = 100;
	
	int playerX;
	int playerY;
	
	private Brain brain;
	
	private Tile[][] MazeTiles;
	public int size;
	
	private int numActions;
	private int numGold;
	private int numMonsters;
	private boolean declaredVictory;
	private boolean quit;
	private boolean metDeath;
	private int goldGotten;
	private int monstersSlain;
	
	public Maze (int size)
	{
		brain = new Brain();
		
		this.size = size;
		tileSize = 800 / size;
		this.setFocusable(true);
		this.addKeyListener(this);
		setDoubleBuffered(true);
		this.setSize(size*tileSize,size*tileSize);
		this.setPreferredSize(getSize());
		this.setMinimumSize(getSize());	
		
		MazeTiles = new Tile[size][size];
		
		System.out.println("Building Maze...");
		System.out.println("\tArrows to move");
		System.out.println("\tWASD to attack");
		System.out.println("\tspacebar to pickup gold");
		System.out.println("\tv to declare victory");
		System.out.println("\tEscape to quit");
		
		buildMaze();
		addObjects();
		
		System.out.println();
		
	}
	
	private void buildMaze()
	{	
		numActions = 0;
		declaredVictory = false;
		quit = false;
		metDeath = false;
		goldGotten = 0;
		numMonsters = 0;
		numGold = 0;
		
		for (int i = 0; i < MazeTiles.length; i++)
		{
			for (int j = 0; j < MazeTiles[i].length; j++)
			{
				MazeTiles[i][j] = new Tile();
			}
		}
		
		boolean full = false;
		int currentX = 0;
		int currentY = 0;
		//String[] path = new String[(size*size)+1];	//TODO: change to 2D array of ints!!!
		int[][] path = new int[2][size*size];
		int pathnum = 0;
		int direction;

		while(!full)
		{
			MazeTiles[currentX][currentY].Marked = true;
			direction = (int)(Math.random() * 4);

			if((!checkReal(currentX, currentY-1) || MazeTiles[currentX][currentY-1].Marked)
					&& (!checkReal(currentX, currentY+1) || MazeTiles[currentX][currentY+1].Marked)
					&& (!checkReal(currentX+1, currentY) || MazeTiles[currentX+1][currentY].Marked)
					&& (!checkReal(currentX-1, currentY) || MazeTiles[currentX-1][currentY].Marked))
			{
				if(!CheckForBlanks())
				{
					full = true;
				}

				else
				{
					pathnum--;
					currentX = path[0][pathnum];
					currentY = path[1][pathnum];
				}
			}

			else
			{
				if (direction == 0 && checkReal(currentX, currentY-1) && !MazeTiles[currentX][currentY-1].Marked)//0 = west
				{
					MazeTiles[currentX][currentY].west = false;
					MazeTiles[currentX][currentY-1].east = false;
					path[0][pathnum] = currentX;
					path[1][pathnum] = currentY;
					pathnum++;
					currentY--;
				}

				else if (direction == 1 && checkReal(currentX-1, currentY) && !MazeTiles[currentX-1][currentY].Marked)	//1 = north
				{
					MazeTiles[currentX][currentY].north = false;
					MazeTiles[currentX-1][currentY].south = false;
					path[0][pathnum] = currentX;
					path[1][pathnum] = currentY;
					pathnum++;
					currentX--;
				}

				else if (direction == 2 && checkReal(currentX, currentY+1) && !MazeTiles[currentX][currentY+1].Marked)	//2 = east
				{
					MazeTiles[currentX][currentY].east = false;
					MazeTiles[currentX][currentY+1].west = false;
					path[0][pathnum] = currentX;
					path[1][pathnum] = currentY;
					pathnum++;
					currentY++;
				}

				else if (direction == 3 && checkReal(currentX+1, currentY) && !MazeTiles[currentX+1][currentY].Marked)	//3 = south
				{
					MazeTiles[currentX][currentY].south = false;
					MazeTiles[currentX+1][currentY].north = false;
					path[0][pathnum] = currentX;
					path[1][pathnum] = currentY;
					pathnum++;
					currentX++;
				}
			}
		}
	}
	
	private void addObjects()
	{
		double rand;
		int gold = 0;
		int monsters = 0;
		
		for (int i = 0; i < MazeTiles.length; i++)
		{
			for (int j = 0; j < MazeTiles[i].length; j++)
			{
				rand = Math.random();
				if(rand <= .10)
				{MazeTiles[i][j].hasGold = true; gold++;}
				rand = Math.random();
				if(rand <= .10)
				{MazeTiles[i][j].hasMonster = true; monsters++;}
			}
		}
		
		int center = size/2;
		
		MazeTiles[center][center].hasGold = false;
		MazeTiles[center][center].hasMonster = false;
		MazeTiles[center][center].hasPlayer = true;
		playerX = center;
		playerY = center;
		
		numGold = gold;
		numMonsters = monsters;
		
		System.out.println("\tThere are " + monsters + " monsters");
		System.out.println("\tThere is " + gold + " gold");
		System.out.println("BEGIN...");
	}
	
	public boolean CheckForBlanks()
	{
		for (int i = 0; i < MazeTiles.length; i++)
		{
			for (int j = 0; j < MazeTiles[i].length; j++)
			{
				if (!MazeTiles[i][j].Marked)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean checkReal(int x, int y)
	{
		if (x < 0 || y < 0)
			return false;
		
		if(x >= size || y >= size)
			return false;
		
		return true;
	}
	
	public boolean isValidMove(int dir)
	{
		switch(dir)
		{
		case 0:
			if(!MazeTiles[playerX][playerY].west && MazeTiles[playerX][playerY-1] != null)
			{return true;}
			else
				break;
		case 1:
			if(!MazeTiles[playerX][playerY].north && MazeTiles[playerX-1][playerY] != null)
			{return true;}
			else
				break;
		case 2:
			if(!MazeTiles[playerX][playerY].east && MazeTiles[playerX][playerY+1] != null)
			{return true;}
			else
				break;
		case 3:
			if(!MazeTiles[playerX][playerY].south && MazeTiles[playerX+1][playerY] != null)
			{return true;}
			else
				break;
		default:
			System.err.println("isValidMove() ERROR");
		}
		
		return false;
	}
	
	private void move()
	{
		Action action = brain.getNextMove(MazeTiles);
		if(action == null)
		{return;}
		
		int row = playerX;
		int col = playerY;
		
		if(action == Action.victory)
		{declaredVictory = true;}
		
		else if(action == Action.quit)
		{declaredVictory = true; quit = true;}
		
		else if(action == Action.pickup)
		{
			if(MazeTiles[row][col].hasGold)
			{
				goldGotten++;
				numGold--;
				System.out.println("\tYou got gold!");
				System.out.println("\t" + numGold + " remaining");
				MazeTiles[row][col].hasGold = false;
			}
		}
		
		else if(action == Action.moveUp)
		{
			if(isValidMove(1))
			{
				MazeTiles[row][col].hasPlayer = false;
				MazeTiles[row-1][col].hasPlayer = true;
				playerX--;
			}
		}
		
		else if(action == Action.moveDown)
		{
			if(isValidMove(3))
			{
				MazeTiles[row][col].hasPlayer = false;
				MazeTiles[row+1][col].hasPlayer = true;
				playerX++;
			}
		}
		
		else if(action == Action.moveLeft)
		{
			if(isValidMove(0))
			{
				MazeTiles[row][col].hasPlayer = false;
				MazeTiles[row][col-1].hasPlayer = true;
				playerY--;
			}
		}
		
		else if(action == Action.moveRight)
		{
			if(isValidMove(2))
			{
				MazeTiles[row][col].hasPlayer = false;
				MazeTiles[row][col+1].hasPlayer = true;
				playerY++;
			}
		}
		
		else if(action == Action.attackUp)
		{
			if(isValidMove(1))
			{
				if(MazeTiles[row-1][col].hasMonster)
				{
					MazeTiles[row-1][col].hasMonster = false;
					MazeTiles[row-1][col].hadMonster = true;
					monstersSlain++;
				}
			}
		}
		
		else if(action == Action.attackDown)
		{
			if(isValidMove(3))
			{
				if(MazeTiles[row+1][col].hasMonster)
				{
					MazeTiles[row+1][col].hasMonster = false;
					MazeTiles[row+1][col].hadMonster = true;
					monstersSlain++;
				}
			}
		}
		
		else if(action == Action.attackLeft)
		{
			if(isValidMove(0))
			{
				if(MazeTiles[row][col-1].hasMonster)
				{
					MazeTiles[row][col-1].hasMonster = false;
					MazeTiles[row][col-1].hadMonster = true;
					monstersSlain++;
				}
			}
		}
		
		else if(action == Action.attackRight)
		{
			if(isValidMove(2))
			{
				if(MazeTiles[row][col+1].hasMonster)
				{
					MazeTiles[row][col+1].hasMonster = false;
					MazeTiles[row][col+1].hadMonster = true;
					monstersSlain++;
				}
			}
		}
		
		else if (action == Action.doNothing) {}
		
		else
		{
			System.err.println("UNHANDLED ACTION" + action);
		}
		
		if(action.isAnAction())
		{
			numActions++;
			if(MazeTiles[playerX][playerY].hasMonster)
			{
				metDeath = true;
				declaredVictory = true;
			}
		}
		
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if(declaredVictory)
		{
			if(quit)
			{System.exit(0);}
			System.out.println("This round: ");
			
			if(metDeath)
			{System.out.println("You died...");}
			
			
			buildMaze();
			addObjects();
		}
		else
			{move();}
		
		if(MazeTiles != null)
		{
			for (int i = 0; i < MazeTiles.length; i++)
			{
				for (int j = 0; j < MazeTiles[i].length; j++)
				{
					if(MazeTiles[i][j] != null)
						MazeTiles[i][j].drawTheImage(g, j*tileSize, i*tileSize, tileSize, tileSize);
				}
			}
		}
		
		try {Thread.sleep(SLEEP_TIME);} 
		catch (InterruptedException e) {e.printStackTrace();}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		//System.out.println("KEY pressed");
		setNextMove(e,this);
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	
	private void setNextMove(KeyEvent k, Maze s)
	{
		int keyEventCode = k.getKeyCode();
		
		if(keyEventCode == KeyEvent.VK_RIGHT) {
			brain.setNextMove(Action.moveRight);
		}
		else if(keyEventCode == KeyEvent.VK_LEFT) {
			brain.setNextMove(Action.moveLeft);
		}
		else if(keyEventCode == KeyEvent.VK_UP) {
			brain.setNextMove(Action.moveUp);
		}
		else if(keyEventCode == KeyEvent.VK_DOWN) {
			brain.setNextMove(Action.moveDown);
		}
		else if (keyEventCode == KeyEvent.VK_V) {//Player Declares Victory
			brain.setNextMove(Action.victory);
		}
		else if (keyEventCode == KeyEvent.VK_ESCAPE) {//Player quits
			brain.setNextMove(Action.quit);
		}
		else if(keyEventCode == KeyEvent.VK_SPACE) {
			brain.setNextMove(Action.pickup);
		}
		else if(keyEventCode == KeyEvent.VK_W) {
			brain.setNextMove(Action.attackUp);
		}
		else if(keyEventCode == KeyEvent.VK_A) {
			brain.setNextMove(Action.attackLeft);
		}
		else if(keyEventCode == KeyEvent.VK_S) {
			brain.setNextMove(Action.attackDown);
		}
		else if(keyEventCode == KeyEvent.VK_D) {
			brain.setNextMove(Action.attackRight);
		}
		else {
			System.out.println("Unknown key event " + keyEventCode);
		}
	}
	
	public static BufferedImage findRandomImage(String foldername) 
	{
		BufferedImage image = null;
		try {
			File folder = new File(foldername);
			String [] files = folder.list();
			File f = new File(folder.getCanonicalPath() + "\\" + files[(int)((Math.random()*files.length))]);
			while(f.isDirectory()) { //2 folders in that array
				f = new File(folder.getCanonicalPath()  + "\\" + files[(int)((Math.random()*files.length))]);
			}
			image = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}
	
	private static BufferedImage copyImage(BufferedImage source)
	{
		BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
		Graphics g = b.getGraphics();
		g.drawImage(source, 0, 0, null);
		g.dispose();
		return b;
	}
	
	private static BufferedImage findRandomImage(String foldername, BufferedImage bottomStartingImage) 
	{
		BufferedImage image = copyImage(bottomStartingImage);
		BufferedImage tmp = findRandomImage(foldername);
		image.getGraphics().drawImage(tmp,0,0,null);
		return image;
	}
}
