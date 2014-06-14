package sample;

public class SimpleSampleCode {

	Integer SeedLimit = null;

	public SimpleSampleCode(Integer seedValue) {
		if(SeedLimit == null)
			this.SeedLimit = new Integer(seedValue);
	}

	public Integer factorial(Integer Seed, Integer Iterations){

		if(Seed!=null){
			int aux=1;
			for (int i=0;i<Iterations.intValue();i++)
            {
				aux =  aux * Seed;
			}
			return new Integer(aux);
		}
		else return null;
	}

	public void newCases()
	{
		 Integer cases = 2;
	        cases -= 1;
	        switch (cases)
	        {
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


        Integer myArray[] = {0, 1, 2};
        for (Integer numero : myArray)
        {
            System.out.println(numero.toString());
        }

        Integer i = 0;
        for (; i < 3; i++)
            {
            System.out.println(numero[i]);
            System.out.println(numero[i]);
            System.out.println(numero[i]);
            if (1 == cases)
            {
                if (2 == MyArray[i])
                {
                    for (Integer j = 1;;)
                    {
                        j++;
                        break;
                    }
                }
                else if (3 == MyArray[i])
                    break;
                else
                    cases = 1;
            }
            else if (2 == cases)
                break;
        }

	    do
        {
            System.out.println("Do While here");
        } while (true);
	}

}
