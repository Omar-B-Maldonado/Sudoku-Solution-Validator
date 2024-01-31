
public class Main 
{
	public static void main(String[] args) 
	{
		int[][] potentialSolution = 
			{
					{6, 2, 4, 5, 3, 9, 1, 8, 7},
					{5, 1, 9, 7, 2, 8, 6, 3, 4},
					{8, 3, 7, 6, 1, 4, 2, 9, 5},
					{1, 4, 3, 8, 6, 5, 7, 2, 9},
					{9, 5, 8, 2, 4, 7, 3, 6, 1},
					{7, 6, 2, 3, 9, 1, 4, 5, 8},
					{3, 7, 1, 9, 5, 6, 8, 4, 2},
					{4, 9, 6, 1, 8, 2, 5, 7, 3},
					{2, 8, 5, 4, 7, 3, 9, 1, 6}
			};
		ValidatorThreads Validator = new ValidatorThreads(potentialSolution);
		
		Validator.initializeThreads();
		Validator.startAllThreads  ();
		Validator.joinAllThreads   ();
		
		if (Validator.allThreadsAreValid())
		{
			System.out.println("Yes, your solution to the Sudoku puzzle is valid! Good job!");
		}	
		else //if at least 1 thread is not valid
		{
			System.out.println("Your solution to the Sudoku puzzle is not valid! Please try again with a different solution this time.");
		}
	}
}
