import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tile 
{
	public static BufferedImage floor;
	public static BufferedImage gold;
	public static BufferedImage monster;
	public static BufferedImage player;
	public static BufferedImage NWall;
	public static BufferedImage SWall;
	public static BufferedImage WWall;
	public static BufferedImage EWall;
	public static BufferedImage spatter;
	
	public boolean north;
	public boolean south;
	public boolean west;
	public boolean east;
	
	public boolean hasPlayer;
	public boolean hasGold;
	public boolean hasMonster;
	public boolean hadMonster;
	
	public boolean Marked;
	
	static {setupImages();}
	
	public Tile()
	{
		north = true; 
		south = true; 
		west = true; 
		east = true;
		Marked = false;
	}
	
	public static void setupImages()
	{
		//floor = Maze.findRandomImage("images\\floors");
		try {
			floor = ImageIO.read(new File("images\\floor.png"));
			gold = ImageIO.read(new File("images\\Gold.png"));
			monster = ImageIO.read(new File("images\\Monster.png"));
			player = ImageIO.read(new File("images\\wizard.png"));
			NWall = ImageIO.read(new File("images\\NorthWall.png"));
			SWall = ImageIO.read(new File("images\\SouthWall.png"));
			WWall = ImageIO.read(new File("images\\WestWall.png"));
			EWall = ImageIO.read(new File("images\\EastWall.png"));
			spatter = ImageIO.read(new File("images\\blood.png"));
		} catch (IOException e) {
			System.err.println("Couldn't find images...");
			e.printStackTrace();
		}
	}
	
	public void drawTheImage(Graphics g, int colOnGraphics, int rowOnGraphics, int imageWidth, int imageHeight)
	{
		g.drawImage(floor, (int)colOnGraphics,(int)rowOnGraphics, imageWidth, imageHeight, null);
		
		if(north)
		{g.drawImage(NWall, (int)colOnGraphics,(int)rowOnGraphics, imageWidth, imageHeight, null);}
		if(south)
		{g.drawImage(SWall, (int)colOnGraphics,(int)rowOnGraphics, imageWidth, imageHeight, null);}
		if(west)
		{g.drawImage(WWall, (int)colOnGraphics,(int)rowOnGraphics, imageWidth, imageHeight, null);}
		if(east)
		{g.drawImage(EWall, (int)colOnGraphics,(int)rowOnGraphics, imageWidth, imageHeight, null);}
		if(hadMonster)
		{g.drawImage(spatter, (int)colOnGraphics,(int)rowOnGraphics, imageWidth, imageHeight, null);}
		if(hasGold)
		{g.drawImage(gold, (int)colOnGraphics,(int)rowOnGraphics, imageWidth, imageHeight, null);}
		if(hasMonster)
		{g.drawImage(monster, (int)colOnGraphics,(int)rowOnGraphics, imageWidth, imageHeight, null);}
		if(hasPlayer)
		{g.drawImage(player, (int)colOnGraphics,(int)rowOnGraphics, imageWidth, imageHeight, null);}
	}
}
