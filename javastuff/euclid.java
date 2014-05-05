public class Euclid {
	private final int x;
	private final int y;

	public Euclid (final int x, final int y) {
		this.x = x;
		this.y = y;	}
}

public static void main (String [] args) {
	Euclid myobj = new Euclid (args[0], args[1]);
	System.out.println("The LCD is :" + runLCD(args[0], args[1]));
}

// if divides x test y
//   if divides both return lcd
final public int runLCD (final int x, 
						 final int y, 
						 final int n) {
	if ( ((x % n) == 0) && ((y % n) == 0)) return n;
	runLCD(x, y, ++n);
}