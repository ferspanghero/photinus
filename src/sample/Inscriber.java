package sample;
public class Inscriber {

	public static void main(String[] args){
		Inscriber.inscribe(new Circle(1,1,6),new Circle(2,2,4));
	}
	
	public static void inscribe(Circle fig1, Circle fig2){
		String title;
		fig1.y=fig2.y;
		if(fig1.diameter<fig2.diameter){
			fig1.x = fig2.x;
			title = "Concentric";
		}
		else{
			fig1.x = shift(fig1.diameter,fig2.diameter,fig2.x);
			title = "Side-by-Side";
		}
		fig1.draw();
		fig2.draw();
		System.out.println(title);
	}
	
	public static int shift(int center, int size1, int size2){
		return (center + (size2+size1)/2);
	}
}
