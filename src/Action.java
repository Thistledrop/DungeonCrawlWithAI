public class Action 
{
	public static final Action moveUp = new Action(true);
	public static final Action moveLeft = new Action(true);
	public static final Action moveRight = new Action(true);
	public static final Action moveDown = new Action(true);
	
	public static final Action attackUp = new Action(true);
	public static final Action attackDown = new Action(true);
	public static final Action attackLeft = new Action(true);
	public static final Action attackRight = new Action(true);
	
	public static final Action pickup = new Action(true);
	public static final Action victory = new Action(false);
	public static final Action quit = new Action(false);
	public static final Action doNothing = new Action(false);
	
	private boolean isAnAction;
	
	public Action(boolean isAnAction)
	{this.isAnAction = isAnAction;}
	
	public boolean isAnAction() {return isAnAction;}
	
	@Override
	public String toString()
	{
		if(this == moveLeft) {
			return "Move Left";
		}
		else if(this == moveRight) {
			return "Move Right";
		}
		else if(this == moveUp) {
			return "Move Up";
		}
		else if(this == moveDown) {
			return "Move Down";
		}
		else if(this == attackUp) {
			return "Shoot Arrow North";
		}
		else if(this == attackDown) {
			return "Shoot Arrow South";
		}
		else if(this == attackLeft) {
			return "Shoot Arrow East";
		}
		else if(this == attackRight) {
			return "Shoot Arrow West";
		}
		else if(this == pickup) {
			return "Pickup Something";
		}
		else if(this == victory) {
			return "Declare Victory";
		}
		else if(this == doNothing) {
			return "Do Nothing";
		}
		return null;
	}
}
