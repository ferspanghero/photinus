package sample.JustOneSample;

public class SimpleSampleCode {

	Integer SeedLimit = null;

	public SimpleSampleCode(Integer seedValue) {
		if (SeedLimit == null)
			this.SeedLimit = new Integer(seedValue);
		Integer testes = 3;
		if (testes == 5)
			this.SeedLimit = new Integer(testes);
		if (testes == 8) {this.SeedLimit = new Integer(testes);	} // If with curly braces at the same line
		if (testes == 10) 
		{

			this.SeedLimit = new Integer(testes); // If with curly braces
		}
	}

	public Integer factorial(Integer Seed, Integer Iterations) {
		if (Seed != null) 
		{	int aux = 1;
			for (int i = 0; i < Iterations.intValue(); i++) {
				aux = aux * Seed;
			}
			return new Integer(aux);
		}
		else
			return null;
	}

	public void newCases() {
		Integer cases = 2;
		cases -= 1;
		switch (cases) {
		case 1:
			System.out.println("1");
			break;
		case 2:
			System.out.println("2");
			break;
		case 3:
			System.out.println("3");
			break;
		default:
			System.out.println("Default");
			break;
		}
		Integer myArray[] = { 0, 1, 2 };
		for (Integer numero : myArray)
			System.out.println(numero.toString());
		Integer i = 0;
		for (; i < 3; i++) { // For with curly braces at the same starting line
			System.out.println(myArray[i]);
			System.out.println(myArray[i]);
			System.out.println(myArray[i]);
			if (1 == cases) {
				if (2 == myArray[i]) { // If with curly braces at the same
					for (Integer j = 1;;) {
						j++;
						break;
					}
				}
				else if (3 == myArray[i])
					break;
				else
					cases = 1;
			}
			else if (2 == cases) {
				break;
			}
		}
		Integer j = 0;
		do {
			System.out.println("Do While here");
			j = this.factorial(j, j);
			j++;
		}
		while (j < 3);
		do {
			System.out.println("Do While here");
			j++;
		}
		while (j < 5);
		do {
			System.out.println("Do While here");
			j++;
		}
		while (j < 7);
		if (j == 11) {
			j--;
			System.out.println("Another if teste");
		}
		if (j == 10) {
			j--;
			System.out.println("Another if teste");
		}
		else {
			j++;
			System.out.println("Another if teste");
		}
	}
}
