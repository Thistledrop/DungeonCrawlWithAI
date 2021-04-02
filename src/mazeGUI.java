import java.awt.FlowLayout;

import javax.swing.JFrame;

public class mazeGUI extends JFrame 
{
	private Maze maze;
	private Stats stats;
	
	public mazeGUI()
	{
		setTitle("Maze Crawler");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		
		maze = new Maze(30);
		this.add(maze);
		stats = new Stats(maze);
		this.add(stats);
		
		pack();
		setVisible(true);
	}
	
	public static void main(String [] args)
	{new mazeGUI();}
}
