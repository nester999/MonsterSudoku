import javax.swing.JTable;


public class SubBoard extends JTable {
	private int[][] data;
	
	SubBoard(){
		
	}
	
	SubBoard(int p, int q) {
		for(int i=0;i<p;i++)
			for(int j=0;j<q;j++)
				data[i][j] = 0;
	}
	
	public int[][] getData() {
		return data;
	}
}
