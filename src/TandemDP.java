import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TandemDP {

	public static final int MATCH = 1;

	public void start(HashMap<String, String> database) {
		System.out.println("From Dynamic programming:");
		for(String key: database.keySet()){
			System.out.print("ID: "+ key+" ");
			String sequence = database.get(key);
			buildScoringMatrix(sequence);
		}
	}

	public  void buildScoringMatrix(String sequence) {

		int resultMatrix[][] = new int[sequence.length() + 1][sequence.length() + 1];
		int maxValue=0,maxRow,maxCol;
		ArrayList<Position> maxPositions = new ArrayList<>();

		for (int i = 0; i <= sequence.length(); i++) {
			resultMatrix[0][i] = 0;
			resultMatrix[i][i] = 0;
		}

		for (int i = 1; i <= sequence.length(); i++) {
			for (int j = i + 1; j <= sequence.length(); j++) {
				if (sequence.charAt(j - 1) == sequence.charAt(i - 1))
					resultMatrix[i][j] = resultMatrix[i - 1][j - 1] + MATCH;
				else
					resultMatrix[i][j] = 0;
				if(maxValue < resultMatrix[i][j]){
					maxValue = resultMatrix[i][j];
				}
			}
		}
		maxPositions = findMaxPositions(maxValue ,resultMatrix);
		HashSet<String> result = new HashSet<>();
		StringBuilder tempResult = new StringBuilder();
		for(Position position: maxPositions){
			maxRow = position.getRow(); maxCol = position.getCol();
			while(resultMatrix[maxRow][maxCol]>0){
				tempResult.append(sequence.charAt(maxRow-1));
				maxRow-=1;maxCol-=1;
			} 
			result.add(tempResult.reverse().toString());
			tempResult = new StringBuilder();
		}


		System.out.println(result);
	}

	private  ArrayList<Position> findMaxPositions(int max, int[][] resultMatrix) {
		ArrayList<Position> maxPositions = new ArrayList<>();
		for(int i=0;i<resultMatrix.length;i++){
			for(int j=i+1;j<resultMatrix.length;j++){
				if(max == resultMatrix[i][j]){
					maxPositions.add(new Position(i,j));
				}
			}
		}
		return maxPositions;
	}


}
class Position {
	private int row, col;

	Position(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

}
