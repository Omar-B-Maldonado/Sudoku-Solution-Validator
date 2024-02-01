
public class ValidatorThreads implements Runnable
{	
	ValidatorThreads[] 	runnableObjects;
	
	ValidatorThreads 	allRows, allCols,
	
						subgrid0_0, subgrid0_1, subgrid0_2,
						subgrid1_0 ,subgrid1_1, subgrid1_2,
						subgrid2_0, subgrid2_1, subgrid2_2;
	
	Thread[] threads; //to hold our 11 threads
	
	int[][] solution;
	
	/* each runnable object is initialized with a purpose for validity-checking a specific area */
	String purpose;
	
	int subgridRow, subgridCol; //for specifying the subgrid in question when this class' purpose is to validate a subgrid
	
	/* for individually tracking the validity of each thread's run condition */
	boolean isValid;
	
	/* each thread must check if every number from 1-9 *
	 * is contained in its row or col or subgrid 	   */
	boolean[] containsNum;
	//
	
	//constructor for this class to validate a particular solution in the main method
	public ValidatorThreads(int[][]solution)
	{
		this.solution    = solution;
	}
	
	//constructor for the row and col threads
		public ValidatorThreads(String purpose, int[][]solution)
		{
			this.solution = solution;
			this.purpose  = purpose;
			this.containsNum = new boolean[9];
		}
	
	//constructor for the 9 subgrid threads within the class
	public ValidatorThreads(String purpose, int subgridRow, int subgridCol, int[][]solution)
	{
		this.solution = solution;
		this.purpose  = purpose;
		this.subgridRow = subgridRow;
		this.subgridCol = subgridCol;
		this.containsNum = new boolean[9];
	}
	
	public void initializeThreads()
	{
		runnableObjects = new ValidatorThreads[] 
		{
			//instantiate runnable objects of this class with their thread's purposes and the solution that was given in our main method
			allRows    = new ValidatorThreads("check all rows", solution),
			allCols    = new ValidatorThreads("check all cols", solution),
			
			subgrid0_0 = new ValidatorThreads("check subgrid", 0, 0, solution),
			subgrid0_1 = new ValidatorThreads("check subgrid", 0, 1, solution),
			subgrid0_2 = new ValidatorThreads("check subgrid", 0, 2, solution),
			
			subgrid1_0 = new ValidatorThreads("check subgrid", 1, 0, solution),
			subgrid1_1 = new ValidatorThreads("check subgrid", 1, 1, solution),
			subgrid1_2 = new ValidatorThreads("check subgrid", 1, 2, solution),
			
			subgrid2_0 = new ValidatorThreads("check subgrid", 2, 0, solution),
			subgrid2_1 = new ValidatorThreads("check subgrid", 2, 1, solution),
			subgrid2_2 = new ValidatorThreads("check subgrid", 2, 2, solution)
		};
		//create threads with the above runnable objects
		threads = new Thread[] 
		{	
			new Thread(allRows),    new Thread(allCols),
			new Thread(subgrid0_0), new Thread(subgrid0_1), new Thread(subgrid0_2),	
			new Thread(subgrid1_0), new Thread(subgrid1_1), new Thread(subgrid1_2),	
			new Thread(subgrid2_0), new Thread(subgrid2_1), new Thread(subgrid2_2),
		};
	}
	
	public void startAllThreads()
	{
		for (Thread t : threads)
		{
			try 				{t.start();          }
			catch (Exception e) {e.printStackTrace();}
		}
	}
	public void joinAllThreads()
	{
		for (Thread t : threads)
		{
			try 				{t.join();           } //wait for all threads to terminate
			catch (Exception e) {e.printStackTrace();}
		}
	}
	
	public boolean allThreadsReturnedValid()
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
		for (int col = 0; col < solution.length; col++)
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
		for (int row = 0; row < solution[0].length; row++)
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
		
		     if (purpose.equals("check all rows")) this.isValid = allRowsValid(solution);
		
		else if (purpose.equals("check all cols")) this.isValid = allColsValid(solution); 
		
		else if (purpose.equals("check subgrid"))
		{
			this.isValid = isValidSubgrid(this.subgridRow, this.subgridCol, solution);
		}
	}
}
