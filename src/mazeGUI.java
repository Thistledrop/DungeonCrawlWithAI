import javax.swing.JFrame;

public class mazeGUI extends JFrame 
{
	private Maze maze;
	
	public mazeGUI()
	{
		setTitle("Empty");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		maze = new Maze(7);
		this.add(maze);
		
		pack();
		setVisible(true);
	}
	
	public static void main(String [] args)
	{new mazeGUI();}
}
