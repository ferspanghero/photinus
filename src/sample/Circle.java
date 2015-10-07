package sample;

public class Circle {

	public int x;
	public int y;
	public int diameter;
	
	public Circle(int x, int y, int diameter) {
		super();
		this.x = x;
		this.y = y;
		this.diameter = diameter;
	}
	
	public Integer getDiameter(){
		return new Integer(diameter);
	}
	
	public void draw(){};
	
	
}
