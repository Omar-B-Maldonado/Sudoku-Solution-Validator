
public class ValidatorThreads implements Runnable
{	
	//------------ (RUNNABLE) INSTANCES OF THIS CLASS ----------
	ValidatorThreads[] 	runnableObjects;
	
	ValidatorThreads 	allRows, allCols,
						subgrid0_0, subgrid0_1, subgrid0_2,
						subgrid1_0 ,subgrid1_1, subgrid1_2,
						subgrid2_0, subgrid2_1, subgrid2_2;
	
	//----------------------------------------------------------
	Thread[] threads;
	int[][] solution;
	
	//initialized for each thread
	String purpose;
	
	//initialized in the run method after threads run 
	boolean isValid; 		//for tracking the validity of each thread individually
	boolean[] containsNum; 	//for checking if each number is contained in each row/col/subgrid
	//----------------------------------------------------------
	
	//constructor for this class to validate a particular solution in the main method
	public ValidatorThreads(int[][]solution)
	{
		this.solution    = solution;
	}
	
	//constructor for the 11 threads within this class
	public ValidatorThreads(String purpose, int[][]solution)
	{
		this.solution = solution;
		this.purpose  = purpose;
		this.containsNum = new boolean[9];
	}
	
	public void initializeThreads()
	{
		runnableObjects = new ValidatorThreads[] 
		{
			//instantiate runnable objects of this class with their thread's purposes and the solution that was given in our main method
			allRows    = new ValidatorThreads("validate all rows"      , solution),
			allCols    = new ValidatorThreads("validate all cols"      , solution),
			
			subgrid0_0 = new ValidatorThreads("validate subgrid (0, 0)", solution),
			subgrid0_1 = new ValidatorThreads("validate subgrid (0, 1)", solution),
			subgrid0_2 = new ValidatorThreads("validate subgrid (0, 2)", solution),
			
			subgrid1_0 = new ValidatorThreads("validate subgrid (1, 0)", solution),
			subgrid1_1 = new ValidatorThreads("validate subgrid (1, 1)", solution),
			subgrid1_2 = new ValidatorThreads("validate subgrid (1, 2)", solution),
			
			subgrid2_0 = new ValidatorThreads("validate subgrid (2, 0)", solution),
			subgrid2_1 = new ValidatorThreads("validate subgrid (2, 1)", solution),
			subgrid2_2 = new ValidatorThreads("validate subgrid (2, 2)", solution)
		};
		//-----------------------------------------------------------
		
		threads = new Thread[] {
			//initialize threads with the above runnable objects
			
			new Thread(allRows), new Thread(allCols),
			
			new Thread(subgrid0_0), new Thread(subgrid0_1), new Thread(subgrid0_2),
			
			new Thread(subgrid1_0), new Thread(subgrid1_1), new Thread(subgrid1_2),
			
			new Thread(subgrid2_0), new Thread(subgrid2_1), new Thread(subgrid2_2),
		};
	}
	
	public void startAllThreads()
	{
		try
		{
			for (Thread t : threads) t.start();
		}
		catch (Exception e) {e.printStackTrace();}
	}
	
	public void joinAllThreads()
	{
		try
		{
			for (Thread t : threads) t.join(); //wait for all threads to terminate
		} 
		catch (Exception e) {e.printStackTrace();}
	}
	
	public boolean allThreadsAreValid()
	{
		for (ValidatorThreads obj : runnableObjects) if (!obj.isValid) return false;
		
		return true;
	}
	
	public boolean containsAllNums()
	{
		for (Boolean bool : this.containsNum) if (bool == false) return false;
		
		return true;
	}
	
	public boolean isValidSubgrid(int subgridRow, int subgridCol, int[][]solution)
	{	
		//offset the row and col based on which subgrid row and col
		int rowOffset = subgridRow * 3;
		int colOffset = subgridCol * 3;
		
		//iterate through 3 all rows of the subgrid
		for (int row = 0; row < 3; row++)
		{
			//iterate through 3 columns for each row
			for(int col = 0; col < 3; col++)
			{
				//checks which number (1 through 9) is contained in each column and row spot
				for (int i = 1; i <= 9; i++)
				{
					//sets contains boolean at [i] to true based on which numbers in the subgrid are present
					//accounting for offset
					if (solution[row + rowOffset][col + colOffset] == i) this.containsNum[i-1] = true;
				}
			}
		}
		//returns true if the subgrid contains all numbers (1 through 9)
		return containsAllNums();
	}
	
	public boolean isValidRow(int row, int[][] solution)
	{	
		//freeze the row and check all columns in that row
		for (int col = 0; col < solution[0].length; col++)
		{
			for (int i = 1; i <= 9; i++)
			{
				if (solution[row][col] == i) this.containsNum[i-1] = true;
			}
		}
		return containsAllNums();
	}
	
	public boolean isValidCol(int col, int[][] solution)
	{
		//freeze the column and check all rows in that column
		for (int row = 0; row < solution.length; row++)
		{
			for (int i = 1; i <= 9; i++)
			{
				if (solution[row][col] == i) this.containsNum[i-1] = true;
			}
		}
		return containsAllNums();
	}
	
	public boolean allRowsValid(int[][] solution)
	{
		for (int i = 0; i < 9; i++ ) if (!isValidRow(i, solution)) return false;
		
		return true;
	}
	public boolean allColsValid(int[][] solution)
	{
		for (int i = 0; i < 9; i++ ) if (!isValidCol(i, solution)) return false;
		
		return true;
	}
	
	@Override
	public void run() 
	{
		/*				EACH THREAD HAS ITS OWN RUN CONDITION			   */
		
		//------------------------ ROWS AND COLS ----------------------------
		if (purpose.equals("validate all rows"))
		{
			if (allRowsValid(solution)) this.isValid = true;
		}
		if(purpose.equals("validate all cols"))
		{
			if (allColsValid(solution)) this.isValid = true; 
		}
		//-------------------------- TOP 3 SUBGRIDS -------------------------
		
		if(purpose.equals("validate subgrid (0, 0)"))
		{
			if (isValidSubgrid(0, 0, solution)) this.isValid = true;
		}
		if(purpose.equals("validate subgrid (0, 1)"))
		{
			if (isValidSubgrid(0, 1, solution)) this.isValid = true;
		}
		if(purpose.equals("validate subgrid (0, 2)"))
		{
			if (isValidSubgrid(0, 2, solution)) this.isValid = true;
		}
		//------------------------ MIDDLE 3 SUBGRIDS -------------------------
		
		if(purpose.equals("validate subgrid (1, 0)"))
		{
			if (isValidSubgrid(1, 0, solution)) this.isValid = true;
		}
		if(purpose.equals("validate subgrid (1, 1)"))
		{
			if (isValidSubgrid(1, 1, solution)) this.isValid = true;
		}
		if(purpose.equals("validate subgrid (1, 2)"))
		{
			if (isValidSubgrid(1, 2, solution)) this.isValid = true;
		}
		//------------------------ BOTTOM 3 SUBGRIDS ------------------------
		
		if(purpose.equals("validate subgrid (2, 0)"))
		{
			if (isValidSubgrid(2, 0, solution)) this.isValid = true;
		}
		if(purpose.equals("validate subgrid (2, 1)"))
		{
			if (isValidSubgrid(2, 1, solution)) this.isValid = true;
		}
		if(this.purpose.equals("validate subgrid (2, 2)"))
		{
			if (isValidSubgrid(2, 2, solution)) this.isValid = true;
		}
		//-------------------------------------------------------------------	
	}
}
