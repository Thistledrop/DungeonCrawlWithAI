import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Brain 
{
	private Action nextMove;
	private ArrayList<Action> nextMoves = new ArrayList<Action>();
	HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
	Maze puzzle;
	
	private static int numGamesPlayed = 0;
	private static boolean keyboardPlay = false;
	
	public Brain(Maze given)
	{
		puzzle = given;
		nextMove = null;
		numGamesPlayed++;
	}
	
	public void setNextMove(Action m)
	{
		if(nextMove != null) 
		{System.out.println("Trouble adding move, only allowed to add 1 at a time");}
		else
		{nextMove = m;}
	}
	
	public Action getNextMove(Tile[][] map)
	{
		if(keyboardPlay) 
		{
			if(nextMove == null) 
			{return Action.doNothing;}
			else 
			{
				System.out.println(puzzle.readyToLeave());
				Action tmp = nextMove;
				nextMove = null;
				return tmp;
			}
		}
		
		else
		{
			Action temp;
			//Maze current = new Maze(puzzle);
			
			if(nextMoves.isEmpty())
			{
				if(!puzzle.readyToLeave())
				{
					nextMoves = GetOut(puzzle);
				}
				//else
				//	return Action.victory;
			}
			

			Action tmp = nextMoves.remove(0);
			return tmp;
		}
	}
	
	public ArrayList<Action> GetOut(Maze start)
	{
		ArrayList<Action> actions = new ArrayList<Action>();
		Maze[] children;
		Queue<Maze> q = new LinkedList<Maze>();
		q.add(start);
		
		while(!q.isEmpty())
		{	
			Maze s = q.remove();
			
			if(visited.containsKey(s.toString()))
				{continue;}
			
			else if(s.readyToLeave())
			{
				actions = s.getActions();
				break;
			}
			else
			{
				visited.put(s.toString(), true);
				children = s.getChildren();
				for (Maze i : children)
				{
					if(i != null)
					{q.add(i);}
				}
			}
		}
		
		actions.add(Action.victory);
		return actions;
	}
}
