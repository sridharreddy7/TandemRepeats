import java.lang.StringBuffer;
import java.util.Arrays;

public class tandem {

	public static final int MATCH = 1; 
	public static final int MISMATCH = -1; 
	public static final int GAP_PENALTY = -1; 
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String sequenceA=args[0];
		
		int score_threshold=Integer.parseInt(args[1]);
		
		
		int score_matrix[][]=buildScoringMatrix(sequenceA);
	//	System.out.println("Matrix Built");
	//	System.out.println(Arrays.deepToString(score_matrix));
		
		findRepeats(score_matrix,sequenceA,score_threshold);

		
		
	}
	
	public static int[][] buildScoringMatrix(String sequenceA)
	{
		
		int res[][]=new int[sequenceA.length()+1][sequenceA.length()+1];
	
		for(int i = 0; i <= sequenceA.length(); i++){
			res[0][i] = 0; 
			res[i][i] = 0; 
		}
		
		int score;
		
		for(int i = 1; i <= sequenceA.length(); i++) {
			for(int j = i + 1; j <= sequenceA.length(); j++) {
			if(sequenceA.charAt(j-1) == sequenceA.charAt(i-1)) 
			score = MATCH;
			else
			score = MISMATCH;
			res[i][j] =Math.max( Math.max(res[i-1][j-1] + score, res[i-1][j] + GAP_PENALTY),Math.max( res[i][j-1] + GAP_PENALTY, 0));
			}
			}

		
		
		return res;
	}
	
	public static Position findMax(int[][] matrix) {
		
		int max = Integer.MIN_VALUE;
		Position maxPos = new Position();
		
		for(int i = 1; i < matrix.length;i++) {
		for(int j = i; j < matrix.length; j++) {
			
		if(matrix[i][j] >= max) {
		max = matrix[i][j];
		maxPos = new Position(i,j);
		}
		}
		}
		return maxPos;
		}
	
	public static void findRepeats(int score_matrix[][],String sequenceA, int score_threshold)
	{
		
		Position[] path;
		Position end_path = findMax(score_matrix);

		while(score_matrix[end_path.getRow()][end_path.getCol()] >= score_threshold) {
			
			path = traceBack(score_matrix, sequenceA, end_path);
			score_matrix = adjustMatrix(score_matrix, path, sequenceA);
		
			end_path = findMax(score_matrix);
			}


	}
	public static int[][] adjustMatrix(int[][] matrix, Position[] path, String str) {
		Position pathfirstpos = path[0];
		int pathfirstrow = pathfirstpos.getRow();
		int[][] shaded = new int[matrix.length][2];
		shaded = shade(matrix, path, str);
		//printMatrix(matrix, str, path, shaded);
		int top, diag, left, score;
		for(int i = pathfirstrow+1; i < matrix.length && shaded[i][0] != -1; i++) {
		for(int j = shaded[i][0]; j <= shaded[i][1]; j++) {
		if(str.charAt(i-1) == str.charAt(j-1)) 
		score = MATCH;
		else
		score = MISMATCH;

		if(isPath(path, i-1, j-1, i, j)) 
		diag = 0; 
		else diag = matrix[i-1][j-1] + score; 
		if(isPath(path, i-1, j, i, j)) 
		top = 0; 
		else top = matrix[i-1][j] + GAP_PENALTY; 
		if(isPath(path, i, j-1, i, j)) 
		left = 0; 
		else left= matrix[i][j-1] + GAP_PENALTY; 
		matrix[i][j] = Math.max(Math.max(diag, top),Math.max( left, 0)); 
		}
		}
		return matrix;
		}
	public static boolean isShaded(int[][] shaded, int row, int col) {
		if(shaded[row][0] != -1 && col >= shaded[row][0] && col <= shaded[row][1]) { //if the column is in the range between the start and end
		return true; 
		}
		return false; //otherwise, it is not shaded
		}
	
		public static boolean isPath(Position[] path, int prevrow, int prevcol, int row, int col) {
		Position pathpos = path[0]; 
		
		for(int i = 0; i < path.length; i++) {
		pathpos = path[i]; //update the value the the given path position
		if(pathpos.getRow() == row && pathpos.getCol() == col) { //if the position matches the given row and col
		if(path[i-1].getRow() == prevrow && path[i-1].getCol() == prevcol) 
			return true; //position matches row and col and prevrow, prevcol

		else
		return false; //position matches row and col but not prevrow and prevcol
		}
		}
		return false; //no positions on the path match the passed row and col
		}
		/* isPath takes an array of Position that are on the path, and two ints which represent a row and column
		isPath returns true if the path contains the Position that is represented by the row and col
		*/
		public static boolean isPath(Position[] path, int row, int col) {
		Position pathpos = path[0]; //assign the value of the first position on the path
		//iterate through the path array and look for a position that matches the given row and col
		for(int i = 0; i < path.length; i++) {
		pathpos = path[i]; //assign a new value to pathpos
		if(pathpos.getRow() == row && pathpos.getCol() == col)
		return true; //if the row and col of that pathpos is equal to the passed row and col, return true
		}
		return false; //the path does not have a Position with a row and col equal to the passed row and col
		}

	public static int[][] shade(int[][] matrix, Position[] path, String str) {
		int score, max;
		int[][] shaded = new int[matrix.length][2];

		for(int i = 0; i < shaded.length; i++) {
		shaded[i][0] = -1;
		}
		for(int i = 0; i < path.length; i++) {
		int row = path[i].getRow();
		if(shaded[row][0] == -1)
		shaded[row][0] = shaded[row][1] = path[i].getCol();
		else
		shaded[row][1] = path[i].getCol();
		}
		for(int i = (path[0].getRow())+1; i < matrix.length && shaded[i-1][0] != -1; i++) {
		boolean foundEnd = false;
		for(int j = (shaded[i-1][0] > i ? shaded[i-1][0] : i+1); j < matrix[0].length && !foundEnd; j++) {
		if(str.charAt(i-1) == str.charAt(j-1)) //test for a match
		score = MATCH;
		else
		score = MISMATCH;
		max = Math.max(Math.max(matrix[i-1][j-1] + score, matrix[i-1][j] + GAP_PENALTY), Math.max(matrix[i][j-1] + GAP_PENALTY, 0));
		//check if this cell comes from shaded cell
		boolean comesFromShaded =
		! ((max == matrix[i-1][j-1] + score) && !isShaded(shaded, i-1, j-1) || //diag score is max, and diag is not shaded
		(max == matrix[i-1][j] + GAP_PENALTY) && !isShaded(shaded, i-1, j) || //top score is max, and top is not shaded
		(max == matrix[i][j-1] + GAP_PENALTY) && !isShaded(shaded, i, j-1)); //left score is max, and max is not shaded

		
		if (max != 0 && comesFromShaded) //if the nonzero max comes from shaded
		{
		if(shaded[i][0]== -1) //if the leftmost shaded column hasn't been found yet
		shaded[i][0] = shaded[i][1] = j; //set leftmost & rightmost
		else if (shaded[i][0] != -1 && j < shaded[i][0] ) //if current column is less than leftmost shaded column
		shaded[i][0] = j;
		//set leftmost shaded
		else if (j > shaded[i][1]) //if the current column is greater than the rightmost
		shaded[i][1] = j; //update rightmost shaded column
		}
		
		if (shaded[i][0] != -1 && !(isShaded(shaded, i-1,j) || isShaded(shaded, i-1, j+1) || isShaded(shaded, i, j))
		)
		foundEnd = true;
		
		if (shaded[i][0] == -1 && j > shaded[i-1][1]+1)
		foundEnd = true;
		}
		}
		return shaded;
		}

	public static Position[] traceBack(int[][] score_matrix, String sequenceA, Position end_path) {
		
		int i = end_path.getRow();
		int j = end_path.getCol();
		Position[] tmppath = new Position[2*i];
		int z = 0; 
		StringBuffer topString = new StringBuffer();
		StringBuffer leftString = new StringBuffer();
		while(score_matrix[i][j] != 0 ) {

		tmppath[z] = new Position(i,j); 
		z++;
		if(score_matrix[i][j] == score_matrix[i][j-1] + GAP_PENALTY) { 
		topString.insert(0, sequenceA.charAt(j-1));
		leftString.insert(0, "-"); 
		j--;
		}
		else if(score_matrix[i][j] == score_matrix[i-1][j] + GAP_PENALTY) { 
		topString.insert(0,"-"); 
		leftString.insert(0,sequenceA.charAt(i-1));
		i--;
		}
		else { 
		topString.insert(0, sequenceA.charAt(j-1));
		leftString.insert(0,sequenceA.charAt(i-1));
		i--; j--;
		}
		}
		tmppath[z] = new Position(i,j); 
	
		Position[] path = new Position[z + 1];
		for(i = 0; i < path.length; i++) {
		path[i] = tmppath[z - i];
		}
		String topStr = topString.toString(); 
		String leftStr = leftString.toString();
		System.out.println();
		System.out.print("Top string: ");
		System.out.printf("%3d",(path[0].getCol()+1)); 
		System.out.print(" " + topStr + " "); 
		System.out.printf("%3d",(path[path.length-1].getCol())); 
		System.out.println();
		System.out.print("Left string: ");
		System.out.printf("%3d",(path[0].getRow()+1)); 
		System.out.print(" " + leftStr + " "); 

		System.out.printf("%3d", (path[path.length-1].getRow()));
		System.out.println("\n");
		//printMatrix(matrix, str, path);
		return path;
		}

}