
public class Brain 
{
	private Action nextMove;
	
	private static int numGamesPlayed = 0;
	private static boolean keyboardPlay = true;
	
	public Brain()
	{
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
				Action tmp = nextMove;
				nextMove = null;
				return tmp;
			}
		}
		
		else
		{
			System.out.println("NOT YET IMPLEMENTED");
			return Action.doNothing;
		}
	}
}
