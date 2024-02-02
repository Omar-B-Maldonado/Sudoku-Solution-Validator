To validate a solution, simply change the potentialSolution variable 
inside the Main class (while matching the formatting)
and run the code in your IDE.

 *Explanation of how ValidatorThreads works via the main method*
 
 The main method:
 
  1) Creates an instance of ValidatorThreads with its first constructor.
  
      * The instance will then create 11 threads, each of which is passed a unique 
        runnable ValidatorThreads object that has a specific purpose in
        the potential Sudoku solution's validation process:

        * allRows purpose -> checking the validity of all rows collectively
        * allCols purpose -> checking the validity of all cols collectively
        * purpose of each subgrid object -> checking the validity of the specified subgrid
  
  2) Calls startAllThreads() on the instance
   
        Each thread will use the same run method, which:
    
        a) calls a specific validating method based on the purpose of its runnable

        b) stores the method's returned boolean value in its runnable's validity variable.
  
  4) Calls joinAllThreads() on the instance
  	  
       * This waits until each thread terminates to ensure the next step evaluates without flaw
  
  5) Calls allThreadsReturnedValid() on the instance
  
       * Each runnable object's validity value will be checked.
       * Returns true if all are valid, false otherwise
  		
The output value of this call is then printed used to
print information regarding the solution's validity to the console.
