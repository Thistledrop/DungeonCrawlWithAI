import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Stats extends JPanel
{
	static Maze maze;
	
	static JLabel mremaining;
	static JLabel mslain;
	static JLabel gremaining;
	static JLabel ggotten;
	static JLabel points;
	static JLabel lastAction;
	
	public Stats(Maze mz)
	{
		maze = mz;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setFocusable(false);
		setDoubleBuffered(true);
		this.setSize(200,800);
		this.setPreferredSize(getSize());
		this.setMinimumSize(getSize());	
		
		mremaining = new JLabel(maze.numMonsters + " monsters remain");
		mslain = new JLabel(maze.monstersSlain + " monsters slain");
		gremaining = new JLabel(maze.numGold + " gold remaining");
		ggotten = new JLabel(maze.goldGotten + " gold retrieved");
		points = new JLabel("Points: " + maze.potentialPoints);
		lastAction = new JLabel(maze.lastAction);
		
		//this.add(mremaining);
		this.add(mslain);
		//this.add(gremaining);
		this.add(ggotten);
		this.add(points);
		this.add(lastAction);
	}
	
	public static void updateStats()
	{
		mremaining.setText(maze.numMonsters + " monsters remain");
		mslain.setText(maze.monstersSlain + " monsters slain");
		gremaining.setText(maze.numGold + " gold remaining");
		ggotten.setText(maze.goldGotten + " gold retrieved");
		points.setText("Points: " + maze.potentialPoints);
		lastAction.setText(maze.lastAction);
	}
}
