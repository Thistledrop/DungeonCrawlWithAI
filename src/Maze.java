import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Maze extends JPanel implements KeyListener
{
	private static final long serialVersionUID = 1L;
	public int tileSize = 100;
	public static final int SLEEP_TIME = 100;
	public static boolean ADD_GEMS = true;
	public static boolean ADD_SLIMES = true;
	public static boolean ADD_EXITS = true;
	
	public int playerX;
	public int playerY;
	
	private Brain brain;
	Maze parent;
	Action action;
	
	public Tile[][] MazeTiles;
	public int size;
	
	public static int points = 0;
	public int potentialPoints;
	public int numActions;
	public int numGold;
	public int numMonsters;
	public boolean declaredVictory;
	public boolean quit;
	public boolean metDeath;
	public int goldGotten;
	public int monstersSlain;
	public String lastAction;
	
	public Maze (int size)
	{
		brain = new Brain(this);
		
		this.size = size;
		tileSize = 800 / size;
		this.setFocusable(true);
		this.addKeyListener(this);
		setDoubleBuffered(true);
		this.setSize(size*tileSize,size*tileSize);
		this.setPreferredSize(getSize());
		this.setMinimumSize(getSize());	
		
		MazeTiles = new Tile[size][size];
		
		System.out.println("Arrows to move");
		System.out.println("WASD to attack");
		System.out.println("spacebar to pickup gold");
		System.out.println("v to declare victory");
		System.out.println("Escape to quit");
		
		buildMaze();
	}
	
	public Maze(Maze maze) 
	{
		size = maze.size;
		parent = maze;
		action = null;
		MazeTiles = new Tile[size][size];
		playerX = maze.playerX;
		playerY = maze.playerY;
		for(int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				MazeTiles[i][j] = new Tile(maze.MazeTiles[i][j]);
			}
		}
	}

	private void buildMaze()
	{	
		brain = new Brain(this);
		numActions = 0;
		declaredVictory = false;
		quit = false;
		metDeath = false;
		goldGotten = 0;
		numMonsters = 0;
		numGold = 0;
		monstersSlain = 0;
		potentialPoints = 0;
		lastAction = "Let's get started";
		
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
		
		double rand;
		int gold = 0;
		int monsters = 0;
		int center = size/2;
		
			for (int i = 0; i < MazeTiles.length; i++)
			{
				for (int j = 0; j < MazeTiles[i].length; j++)
				{
					if(ADD_GEMS)
					{
						rand = Math.random();
						if(rand <= .10)
						{MazeTiles[i][j].hasGold = true; gold++;}
					}
					
					if(ADD_SLIMES)
					{
						rand = Math.random();
						if(rand <= .10)
						{MazeTiles[i][j].hasMonster = true; monsters++;}
					}
					
					if(ADD_EXITS)
					{
						rand = Math.random();
						double dist = Math.sqrt(Math.pow((center - i), 2) + Math.pow((center - j), 2));
						double prg = dist/(center*140);
						if(rand < prg)
						{MazeTiles[i][j].hasExit = true;}
					}
				}
			}
			
		MazeTiles[0][0].hasExit = true;
		MazeTiles[center][center].hasGold = false;
		MazeTiles[center][center].hasMonster = false;
		MazeTiles[center][center].hasPlayer = true;
		playerX = center;
		playerY = center;
		
		numGold = gold;
		numMonsters = monsters;
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
	
	public boolean isValidMove(int dir)	//0 west, 1 north, 2 east, 3 south
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
			System.err.println("Invalid Input");
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
		{
//			System.out.println("tried to victory");
			if(MazeTiles[row][col].hasExit)
			{
				lastAction = "You Win!";
				declaredVictory = true;
				potentialPoints += 100;
			}
			else
				lastAction = "Can't exit from here";
		}
		
		else if(action == Action.secretVictory)
		{
			lastAction = "You Win!";
			declaredVictory = true;
			potentialPoints += 100;
		}
		
		else if(action == Action.quit)
		{
			declaredVictory = true; 
			quit = true;
		}
		
		else if(action == Action.pickup)
		{
//			System.out.println("tried to pickup");
			if(MazeTiles[row][col].hasGold)
			{
				lastAction = "You got a gem!";
				goldGotten++;
				numGold--;
				MazeTiles[row][col].hasGold = false;
				potentialPoints += 10;
			}
			else
				lastAction = "There's nothing there...";
		}
		
		else if(action == Action.moveUp)
		{
//			System.out.println("tried to move up");
			if(isValidMove(1))
			{
				lastAction = "Moved North";
				MazeTiles[row][col].hasPlayer = false;
				MazeTiles[row-1][col].hasPlayer = true;
				playerX--;
			}
			else
				lastAction = "Can't move North";
		}
		
		else if(action == Action.moveDown)
		{
//			System.out.println("tried to move down");
			if(isValidMove(3))
			{
				lastAction = "Moved South";
				MazeTiles[row][col].hasPlayer = false;
				MazeTiles[row+1][col].hasPlayer = true;
				playerX++;
			}
			else
				lastAction = "Can't move South";
		}
		
		else if(action == Action.moveLeft)
		{
//			System.out.println("tried to move left");
			if(isValidMove(0))
			{
				lastAction = "Moved West";
				MazeTiles[row][col].hasPlayer = false;
				MazeTiles[row][col-1].hasPlayer = true;
				playerY--;
			}
			else
				lastAction = "Can't move West";
		}
		
		else if(action == Action.moveRight)
		{
//			System.out.println("tried to move right");
			if(isValidMove(2))
			{
				lastAction = "Moved East";
				MazeTiles[row][col].hasPlayer = false;
				MazeTiles[row][col+1].hasPlayer = true;
				playerY++;
			}
			else
				lastAction = "Can't move East";
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
					numMonsters--;
					lastAction = "Killed a monster";
					potentialPoints += 5;
				}
				else
					lastAction = "Swing and a miss";
			}
			else
				lastAction = "Swung at a wall";
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
					numMonsters--;
					lastAction = "Killed a monster";
					potentialPoints += 5;
				}
				else
					lastAction = "Swing and a miss";
			}
			else
				lastAction = "Swung at a wall";
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
					numMonsters--;
					lastAction = "Killed a monster";
					potentialPoints += 5;
				}
				else
					lastAction = "Swing and a miss";
			}
			else
				lastAction = "Swung at a wall";
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
					numMonsters--;
					lastAction = "Killed a monster";
					potentialPoints += 5;
				}
				else
					lastAction = "Swing and a miss";
			}
			else
				lastAction = "Swung at a wall";
		}
		
		else if(action == Action.seduce)
		{
			for (int i = 0; i < MazeTiles.length; i++)
			{
				for (int j = 0; j < MazeTiles[i].length; j++)
				{
					if (MazeTiles[i][j].hasMonster)
					{
						MazeTiles[i][j].hasMonster = false;
						MazeTiles[i][j].isHeart = true;
					}
				}
			}
			
			potentialPoints += 1000;
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
				potentialPoints = 0;
				declaredVictory = true;
				lastAction = "You died...";
			}
		}
		
		Stats.updateStats();
		//System.out.println(potentialPoints);
	}
	
	public String toString()
	{
		String s = "";
		
		for (int i = 0; i < MazeTiles.length; i++)
		{
			for (int j = 0; j < MazeTiles[i].length; j++)
			{
				if(MazeTiles[i][j].hasPlayer)
				{s += "P";}
				if(MazeTiles[i][j].hasGold)
				{s += "G";}
				if(MazeTiles[i][j].hasMonster)
				{s += "M";}
			
				s += ", ";
			}
		}
		
		return s;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if(declaredVictory)
		{
			if(quit)
			{
				System.out.println("Game Over");
				System.out.println(points + " points!");
				System.exit(0);
			}
			
			if(metDeath)
			{System.out.println("You died... Lost " + potentialPoints + " points");}
			
			if(declaredVictory && !metDeath)
			{
				System.out.println("You made it out with " + potentialPoints + " points!");
				points += potentialPoints;
				System.out.println("\t" + points + " total points!");
			}
			
			buildMaze();
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
		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		repaint();
	}
	
	public boolean readyToLeave()
	{
		return MazeTiles[playerX][playerY].hasExit;
	}
	
	public boolean gotAllGold()
	{
		for (int i = 0; i < MazeTiles.length; i++)
		{
			for (int j = 0; j < MazeTiles[i].length; j++)
			{
				if (MazeTiles[i][j].hasGold)
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	public ArrayList<Action> getActions()
	{
		ArrayList<Action> acts = new ArrayList<Action>();
		if(parent != null)
		{
			acts = parent.getActions();
		}
		acts.add(this.action);
		return acts;
	}
	
	public Maze[] getChildren()
	{
		Maze[] children = new Maze[9];
		
		if(MazeTiles[playerX][playerY].hasGold)
		{children[0] = this.pickup();return children;}
		else
		{
			children[1] = this.moveUp();
			children[2] = this.moveLeft();
			children[3] = this.moveDown();
			children[4] = this.moveRight();
			
			children[5] = this.attackUp();
			children[6] = this.attackLeft();
			children[7] = this.attackDown();
			children[8] = this.attackRight();
		}
		return children;
	}
	
	public Maze pickup()
	{
		Maze newMaze = new Maze(this);
		newMaze.action = Action.pickup;

		if(MazeTiles[playerX][playerY].hasGold)
		{newMaze.MazeTiles[playerX][playerY].hasGold = false;return newMaze;}
		else
			return null;
	}
	
	public Maze moveUp()
	{
		Maze newMaze = new Maze(this);
		newMaze.action = Action.moveUp;
		
		if(this.isValidMove(1))
		{
			Tile here = newMaze.MazeTiles[playerX][playerY];
			Tile there = newMaze.MazeTiles[playerX-1][playerY];
			if(!there.hasMonster)
			{
				here.hasPlayer = false;
				there.hasPlayer = true;
				newMaze.playerX = playerX-1;
				return newMaze;
			}
		}
			return null;
	}
	
	public Maze moveDown()
	{
		Maze newMaze = new Maze(this);
		newMaze.action = Action.moveDown;
				
		if(this.isValidMove(3))
		{
			Tile here = newMaze.MazeTiles[playerX][playerY];
			Tile there = newMaze.MazeTiles[playerX+1][playerY];
			if(!there.hasMonster)
			{
				here.hasPlayer = false;
				there.hasPlayer = true;
				newMaze.playerX = playerX+1;
				return newMaze;
			}
			
		}
			return null;
	}
	
	public Maze moveLeft()
	{
		Maze newMaze = new Maze(this);
		newMaze.action = Action.moveLeft;
		
		if(this.isValidMove(0))
		{
			Tile here = newMaze.MazeTiles[playerX][playerY];
			Tile there = newMaze.MazeTiles[playerX][playerY-1];
			if(!there.hasMonster)
			{
				here.hasPlayer = false;
				there.hasPlayer = true;
				newMaze.playerY = playerY-1;
				return newMaze;
			}
			
		}
			return null;
	}
	
	public Maze moveRight()
	{
		Maze newMaze = new Maze(this);
		newMaze.action = Action.moveRight;
		
		if(this.isValidMove(2))
		{
			Tile here = newMaze.MazeTiles[playerX][playerY];
			Tile there = newMaze.MazeTiles[playerX][playerY+1];
			if(!there.hasMonster)
			{
				here.hasPlayer = false;
				there.hasPlayer = true;
				newMaze.playerY = playerY+1;
				return newMaze;
			}
		}
			return null;
	}
	
	public Maze attackUp()
	{
		if(this.isValidMove(1))
		{
			Maze newMaze = new Maze(this);
			newMaze.action = Action.attackUp;
			Tile there = newMaze.MazeTiles[playerX-1][playerY];
			if(there.hasMonster)
			{
				there.hasMonster = false;
				return newMaze;
			}
		}
		return null;
	}
	
	public Maze attackDown()
	{
		if(this.isValidMove(3))
		{
			Maze newMaze = new Maze(this);
			newMaze.action = Action.attackDown;	
			Tile there = newMaze.MazeTiles[playerX+1][playerY];
			if(there.hasMonster)
			{
				there.hasMonster = false;
				return newMaze;
			}
		}
		return null;
	}
	
	public Maze attackLeft()
	{
		if(this.isValidMove(0))
		{
			Maze newMaze = new Maze(this);
			newMaze.action = Action.attackLeft;
			Tile there = newMaze.MazeTiles[playerX][playerY-1];
			if(there.hasMonster)
			{
				there.hasMonster = false;
				return newMaze;
			}
		}
		return null;
	}
	
	public Maze attackRight()
	{
		if(this.isValidMove(2))
		{
			Maze newMaze = new Maze(this);
			newMaze.action = Action.attackRight;
			Tile there = newMaze.MazeTiles[playerX][playerY+1];
			if(there.hasMonster)
			{
				there.hasMonster = false;
				return newMaze;
			}
		}
		return null;
	}

	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) 
	{
		//System.out.println("KEY pressed");
		setNextMove(e,this);
	}

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
		else if(keyEventCode == KeyEvent.VK_SHIFT) {
			brain.setNextMove(Action.secretVictory);
		}
		else if(keyEventCode == KeyEvent.VK_F) {
			brain.setNextMove(Action.seduce);
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
