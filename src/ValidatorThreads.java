
public class ValidatorThreads implements Runnable
{	
	//------------ (RUNNABLE) INSTANCES OF THIS CLASS ----------
	
	ValidatorThreads allRows;
	ValidatorThreads allCols;
	
	ValidatorThreads subgrid0_0;
	ValidatorThreads subgrid0_1;
	ValidatorThreads subgrid0_2;
	
	ValidatorThreads subgrid1_0;
	ValidatorThreads subgrid1_1;
	ValidatorThreads subgrid1_2;
	
	ValidatorThreads subgrid2_0;
	ValidatorThreads subgrid2_1;
	ValidatorThreads subgrid2_2;
	
	//-------------------- THREADS -----------------------------

	Thread allRowsValidator; 	  //checks all rows for validity
	Thread allColsValidator; 	  //checks all cols for validity
	
	Thread subgrid0_0Validator; //checks top    left   subgrid
	Thread subgrid0_1Validator; //checks top    middle subgrid
	Thread subgrid0_2Validator; //checks top    right  subgrid

	Thread subgrid1_0Validator; //checks middle left   subgrid
	Thread subgrid1_1Validator; //checks middle        subgrid
	Thread subgrid1_2Validator; //checks middle left   subgrid
	
	Thread subgrid2_0Validator; //checks bottom left   subgrid
	Thread subgrid2_1Validator; //checks bottom middle subgrid
	Thread subgrid2_2Validator; //checks bottom right  subgrid
	
	//----------------------------------------------------------
	
	//initialized for this class
	int[][] solution;
	
	//initialized for each thread
	String purpose;
	
	//initialized in the run method after threads run 
	boolean areGood; //for tracking all rows or all cols
	boolean isgood;  //for tracking a subgrid
	
	//----------------------------------------------------------
	
	//constructor for this class to validate a particular solution in the main method
	public ValidatorThreads(int[][]solution)
	{
		this.solution = solution;
	}
	
	//constructor for the 11 threads within this class
	public ValidatorThreads(String purpose, int[][]solution)
	{
		this.solution = solution;
		this.purpose  = purpose;
	}
	
	public void initializeThreads()
	{
		//instantiate runnable objects of this class with their thread's purposes and the solution that was given in our main method
		allRows    = new ValidatorThreads("validate all rows"      , solution);
		allCols    = new ValidatorThreads("validate all cols"      , solution);
		
		subgrid0_0 = new ValidatorThreads("validate subgrid (0, 0)", solution);
		subgrid0_1 = new ValidatorThreads("validate subgrid (0, 1)", solution);
		subgrid0_2 = new ValidatorThreads("validate subgrid (0, 2)", solution);
		
		subgrid1_0 = new ValidatorThreads("validate subgrid (1, 0)", solution);
		subgrid1_1 = new ValidatorThreads("validate subgrid (1, 1)", solution);
		subgrid1_2 = new ValidatorThreads("validate subgrid (1, 2)", solution);
		
		subgrid2_0 = new ValidatorThreads("validate subgrid (2, 0)", solution);
		subgrid2_1 = new ValidatorThreads("validate subgrid (2, 1)", solution);
		subgrid2_2 = new ValidatorThreads("validate subgrid (2, 2)", solution);
		//-----------------------------------------------------------
		
		//initialize threads with the above runnable objects
		allRowsValidator    = new Thread(allRows);	
		allColsValidator    = new Thread(allCols);
		
		subgrid0_0Validator = new Thread(subgrid0_0);
		subgrid0_1Validator = new Thread(subgrid0_1);
		subgrid0_2Validator = new Thread(subgrid0_2);
		
		subgrid1_0Validator = new Thread(subgrid1_0);
		subgrid1_1Validator = new Thread(subgrid1_1);
		subgrid1_2Validator = new Thread(subgrid1_2);
		
		subgrid2_0Validator = new Thread(subgrid2_0);
		subgrid2_1Validator = new Thread(subgrid2_1);
		subgrid2_2Validator = new Thread(subgrid2_2);
	}
	
	public void startAllThreads()
	{
		try
		{
			allRowsValidator.start();
			allColsValidator.start();
			
			subgrid0_0Validator.start();
			subgrid0_1Validator.start();
			subgrid0_2Validator.start();
			
			subgrid1_0Validator.start();
			subgrid1_1Validator.start();
			subgrid1_2Validator.start();
			
			subgrid2_0Validator.start();
			subgrid2_1Validator.start();
			subgrid2_2Validator.start();
		}
		catch (Exception e) {e.printStackTrace();}
	}
	
	public void joinAllThreads()
	{
		try
		{
			//wait for all threads to terminate
			allRowsValidator.join();
			allColsValidator.join();
			
			subgrid0_0Validator.join();
			subgrid0_1Validator.join();
			subgrid0_2Validator.join();
			
			subgrid1_0Validator.join();
			subgrid1_1Validator.join();
			subgrid1_2Validator.join();
			
			subgrid2_0Validator.join();
			subgrid2_1Validator.join();
			subgrid2_2Validator.join();
		} 
		catch (Exception e) {e.printStackTrace();}
	}
	
	public boolean allThreadsAreValid()
	{
		return this.allRows.areGood   && this.allCols.areGood   &&
			   this.subgrid0_0.isgood && this.subgrid0_1.isgood && this.subgrid0_2.isgood && 
			   this.subgrid1_0.isgood && this.subgrid1_1.isgood && this.subgrid1_2.isgood && 
			   this.subgrid2_0.isgood && this.subgrid2_1.isgood && this.subgrid2_2.isgood;
	}
	
	public boolean isValidCondition(boolean[] contains)
	{
		return contains[1] && contains[2] && contains[3] && 
			   contains[4] && contains[5] && contains[6] && 
			   contains[7] && contains[8] && contains[9];
	}
	
	public boolean isValidSubgrid(int subgridRow, int subgridCol, int[][]solution)
	{
		boolean[] containsAll = new boolean[10];
		
		int rowOffset = -1;
		int colOffset = -1;
		
		//offset the row and col based on which subgrid you're in
		if (subgridRow == 0) rowOffset = 0;
		if (subgridRow == 1) rowOffset = 3;
		if (subgridRow == 2) rowOffset = 6;
		
		if (subgridCol == 0) colOffset = 0;
		if (subgridCol == 1) colOffset = 3;
		if (subgridCol == 2) colOffset = 6;
		
		//iterate through a 3 rows
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
					if (solution[row + rowOffset][col + colOffset] == i) containsAll[i] = true;
				}
			}
		}
		//returns true if the subgrid contains all numbers (1 through 9)
		return isValidCondition(containsAll);
		
	}
	
	public boolean isValidRow(int row, int[][] solution)
	{
		/*for readability, we make the boolean array
		 * hold 10 values so we can
		 * access indices 1 through 9
		 */
		boolean[] containsAll = new boolean[10];
		
		//freeze the row and check all columns in that row
		for (int col = 0; col < solution[0].length; col++)
		{
			/* checks which number (1 through 9) is contained in
			 * each col of this row and stores that number as true
			 * in the boolean array's matching index
			 * (meaning this row contains that number)
			 */
			for (int i = 1; i <= 9; i++)
			{
				if (solution[row][col] == i) containsAll[i] = true;
			}
		}	
		return isValidCondition(containsAll);
	}
	
	public boolean isValidCol(int col, int[][] solution)
	{
		boolean[] containsAll = new boolean[10];
	
		//freeze the column and check all rows in that column
		for (int row = 0; row < solution.length; row++)
		{
			/* checks which number (1 through 9) is contained in
			 * each row of this col and stores that number as true
			 * in the boolean array's matching index
			 * (meaning this column contains that number)
			 */
			for (int i = 1; i <= 9; i++)
			{
				if (solution[row][col] == i) containsAll[i] = true;
			}
		}
		
		//returns true if the solution's column contains all numbers (1 through 9)
		return isValidCondition(containsAll);
	}
	
	public boolean allRowsValid(int[][] solution)
	{
		/*  our row count starts at 0 since we're checking
		 *  if the row is valid via its row index in the 2D array
		 */
		return isValidRow(0, solution) &&
			   isValidRow(1, solution) &&
			   isValidRow(2, solution) &&
			   isValidRow(3, solution) &&
			   isValidRow(4, solution) &&
			   isValidRow(5, solution) &&
			   isValidRow(6, solution) &&
			   isValidRow(7, solution) &&
			   isValidRow(8, solution);
	}
	
	public boolean allColsValid(int[][] solution)
	{
		return isValidCol(0, solution) &&
			   isValidCol(1, solution) &&
			   isValidCol(2, solution) &&
			   isValidCol(3, solution) &&
			   isValidCol(4, solution) &&
			   isValidCol(5, solution) &&
			   isValidCol(6, solution) &&
			   isValidCol(7, solution) &&
			   isValidCol(8, solution);
	}
	
	@Override
	public void run() 
	{
		/*				EACH THREAD HAS ITS OWN RUN CONDITION			   */
		
		//------------------------ ROWS AND COLS ----------------------------
		if (purpose.equals("validate all rows"))
		{
			if (allRowsValid(this.solution)) this.areGood = true;
		}
		if(purpose.equals("validate all cols"))
		{
			if (allColsValid(this.solution)) this.areGood = true; 
		}
		//-------------------------- TOP 3 SUBGRIDS -------------------------
		
		if(purpose.equals("validate subgrid (0, 0)"))
		{
			if (isValidSubgrid(0, 0, this.solution)) this.isgood = true;
		}
		if(purpose.equals("validate subgrid (0, 1)"))
		{
			if (isValidSubgrid(0, 1, this.solution)) this.isgood = true;
		}
		if(purpose.equals("validate subgrid (0, 2)"))
		{
			if (isValidSubgrid(0, 2, this.solution)) this.isgood = true;
		}
		//------------------------ MIDDLE 3 SUBGRIDS -------------------------
		
		if(purpose.equals("validate subgrid (1, 0)"))
		{
			if (isValidSubgrid(1, 0, this.solution)) this.isgood = true;
		}
		if(purpose.equals("validate subgrid (1, 1)"))
		{
			if (isValidSubgrid(1, 1, this.solution)) this.isgood = true;
		}
		if(purpose.equals("validate subgrid (1, 2)"))
		{
			if (isValidSubgrid(1, 2, this.solution)) this.isgood = true;
		}
		//------------------------ BOTTOM 3 SUBGRIDS ------------------------
		
		if(purpose.equals("validate subgrid (2, 0)"))
		{
			if (isValidSubgrid(2, 0, this.solution)) this.isgood = true;
		}
		if(purpose.equals("validate subgrid (2, 1)"))
		{
			if (isValidSubgrid(2, 1, this.solution)) this.isgood = true;
		}
		if(this.purpose.equals("validate subgrid (2, 2)"))
		{
			if (isValidSubgrid(2, 2, this.solution)) this.isgood = true;
		}
		//-------------------------------------------------------------------	
	}
}
