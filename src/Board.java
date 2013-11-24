import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import javax.swing.JFileChooser;
import java.io.File;   
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Board extends JPanel {
	private boolean DEBUG = false;
	private static int p;
	private static int q;
	private static int M;
	private static int N;

	//Create a file chooser
	private static JFileChooser fc;
	private static JFrame frame;
	private static JButton NewGame;
	private static JButton LoadGame;
	private static JButton SolveGame;
	private static JButton ClearGame;

	private static JFrame setParams;
	private static JTextArea theSizeN;
	private static JTextArea theSizeP;
	private static JTextArea theSizeQ;
	private static JTextArea theSizeM;

	private static Vector<Vector<String>> vectors;
	private static String[][] grid;
	private static String[][] freeCells;
	//	private static ArrayList<String> constraintVals;
	private static Cell[][] valid;
	private static HashMap<String,Integer> odometerStyleMap;

	private static JTable table;
	private static DefaultTableModel model;
	private static BufferedReader br;
	private static File selectedFile;

	private static boolean FC;
	private static boolean MRV;
	private static boolean ACP;


	public Board(int x, int y) {
		p=x;
		q=y;
		N=p*q;
		grid = new String[N][N];
		//		constraintVals = getConstraintValues(N);
		valid = new Cell[N][N];
		//		populateMap(N);
		model = new DefaultTableModel(); 
		table = new JTable(model); 
		model.setRowCount(N);
		model.setColumnCount(N);

		for(int i=0; i<N;i++)
			for(int j=0; j<N;j++) {
				grid[i][j] = "0";
				valid[i][j] = new Cell(0);
				valid[i][j].setConstraintVals(N);
				model.setValueAt(valid[i][j], i, j);
			}

		//create table interface
		table.setPreferredSize(new Dimension(450,450));
		table.setRowHeight((int)(450/N));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setFont(new Font("Serif", Font.BOLD, 16));
		table.setTableHeader(null);
		DefaultTableCellRenderer defaultRenderer = (DefaultTableCellRenderer) table.getDefaultRenderer(Object.class);
		defaultRenderer.setHorizontalAlignment( JLabel.CENTER );
		table.setBorder(new MatteBorder(3, 3, 3, 3, Color.BLACK) );
		table.setDefaultRenderer(String.class, defaultRenderer);
		table.setFillsViewportHeight(true);

		if (DEBUG) {
			table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					//					printDebugData(table);
				}
			});
		}
		add(table);
	}

	public static boolean UsedInRow(int row, int num) {
		for (int i = 0; i < table.getModel().getColumnCount(); i++) {
			System.out.println("UsedInRow(), check ("+row+", "+i+") = NO_TABLE_VAL against "+num);
			if ((Integer)table.getModel().getValueAt(row, i) == num) {
				return true;
			}
		}
		return false;	
	}

	public static boolean UsedInCol(int col, int num) {
		printDebugData(table);
		for (int i = 0; i < table.getModel().getRowCount(); i++)
			if ((Integer)table.getModel().getValueAt(i, col) == num)		
				return true;
		return false;
	}

	public static boolean UsedInBox(int boxStartRow, int boxStartCol, int num) {
		printDebugData(table);
		for (int row = 0; row < p; row++)
			for (int col = 0; col < q; col++)
				if ((Integer)table.getModel().getValueAt(row+boxStartRow, col+boxStartCol) == num)
					return true;
		return false;
	}

	//cell with constraint list
	public static class Cell {
		private ArrayList<Integer> constraintVals;
		private int value;

		Cell(int x) {
			value = x;
			constraintVals = new ArrayList<Integer>();
			if(x>0)
				for(int i=1; i<=N; i++)
					constraintVals.add(i);
		}

		public ArrayList<Integer> getConstraintVals(){
			return constraintVals;
		}

		public void setConstraintVals(int N) {
			for(int i=1; i<=N;i++)
				constraintVals.add(i);
			System.out.println("Cell ConstraintVals: " + constraintVals);
		}

		public int getValue() {
			return value;
		}

		public void setValue(int x) {
			value = x;
		}	
	}


	public static ArrayList<String> getConstraintValues(int n) {
		ArrayList<String> s = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		String letter = "";
		String num;
		for(int i=1;i<=n;i++){
			letter=Character.toString((char)(i+55));
			num=String.valueOf(i);
			if(i<10) {
				s.add(num);
				System.out.println("s: " + s);
			}
			else if (i<36)
				s.add(letter);
			else if (i%36<10 && i%36 != 0) {
				int countdown = i;
				int rpt = (int) Math.ceil(i/35);
				for(int j=0;j<rpt;j++) {
					sb.append(Integer.toString(countdown%36));
					countdown-=10;
					//					s.add(Integer.toString(i%36)+Integer.toString(i%36));
				}
			}
			else if(i%36 >= 10 && i%36 != 0) {
				int rpt = (int)Math.ceil(i/35);
				for(int j=0; j<rpt;j++) {

				}
			}
			else{};
		}
		Collections.sort(s);
		System.out.println("s: " + s);

		return s;
	}

	private String getCellValue(int i, int j) {
		return grid[i][j];
	}

	private void setCellValue(String s, int i, int j) {
		grid[i][j]=s;
	}

	private static void printDebugData(JTable table) {
		int numRows = table.getRowCount();
		int numCols = table.getColumnCount();
		javax.swing.table.TableModel model = table.getModel();

		System.out.println("Value of data: ");
		for (int i=0; i < numRows; i++) {
			System.out.print("    row " + i + ":");
			for (int j=0; j < numCols; j++) {
				System.out.print("  " + model.getValueAt(i, j));
			}
			System.out.println();
		}
		System.out.println("--------------------------");
	}


	@SuppressWarnings("resource")
	private static void createAndShowGUI() throws IOException, InvalidStartInputException {
		fc = new JFileChooser();	
		frame = new JFrame("Board");
		BorderLayout frameGrid = new BorderLayout();
		frame.setLayout(frameGrid);
		vectors = new Vector<Vector<String>>(N);
		p=2;
		q=2;
		N=p*q;

		final JPanel buttonInterface = new JPanel();
		JPanel panel = new JPanel();
		FlowLayout boardSizeGrid = new FlowLayout(2,2,1);
		panel.setLayout(boardSizeGrid);

		//Create and set up the content pane.
		Board newContentPane = new Board(p,q);
		((DefaultTableModel) newContentPane.table.getModel()).fireTableDataChanged();


		frame.setContentPane(newContentPane);

		NewGame = new JButton("Start New Game");
		NewGame.setToolTipText("Click here to start a new random game");
		buttonInterface.add(NewGame);
		NewGame.addActionListener (new Start_Action());

		LoadGame = new JButton("Load Game");
		LoadGame.setToolTipText("Click here to start a new game from file");
		buttonInterface.add(LoadGame);
		LoadGame.addActionListener (new Load_Action());

		SolveGame = new JButton("Solve Game");
		SolveGame.setToolTipText("Click here to solve a game");
		SolveGame.setEnabled(false);
		buttonInterface.add(SolveGame);
		SolveGame.addActionListener (new Solve_Action()); 


		ClearGame = new JButton("Clear Game");
		ClearGame.setToolTipText("Click here to clear the current game");
		buttonInterface.add(ClearGame);
		ClearGame.addActionListener (new Clear_Action()); 
		frame.add(buttonInterface);
		JCheckBox FC = new JCheckBox();
		JCheckBox MRV = new JCheckBox();
		JCheckBox ARC = new JCheckBox();
		JLabel FCLabel = new JLabel("FC");
		JLabel MRVLabel = new JLabel("MRV");
		JLabel ARCLabel = new JLabel("ARC");
		frame.add(FCLabel);
		frame.add(FC);
		frame.add(MRVLabel);
		frame.add(MRV);
		frame.add(ARCLabel);
		frame.add(ARC);
		//Display the window.
		frame.setVisible(true);

		BoxLayout buttonFlow = new BoxLayout(buttonInterface,3);
		buttonInterface.setLayout(buttonFlow);

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public boolean IsValid (String s, int i, int j) {

		return false;
	}

	static class Start_Action implements ActionListener {        
		public void actionPerformed (ActionEvent e) { 
			setParams = new JFrame();
			JPanel paramsContent = new JPanel(new GridBagLayout());
			JLabel sizeN = new JLabel("Size of N: ");
			theSizeN = new JTextArea(1,5);
			JLabel sizeP = new JLabel("Size of P: ");
			theSizeP = new JTextArea(1,5);
			JLabel sizeQ = new JLabel("Size of Q: ");
			theSizeQ = new JTextArea(1,5);
			JLabel sizeM = new JLabel("Size of M: ");
			theSizeM = new JTextArea(1,5);
			JButton paramsOK = new JButton("OK");
			paramsContent.add(sizeN);
			paramsContent.add(theSizeN);
			paramsContent.add(sizeP);
			paramsContent.add(theSizeP);
			paramsContent.add(sizeQ);
			paramsContent.add(theSizeQ);
			paramsContent.add(sizeM);
			paramsContent.add(theSizeM);
			paramsContent.add(paramsOK);
			paramsOK.addActionListener (new SetParams_Action()); 

			setParams.add(paramsContent);

			setParams.pack();
			setParams.setVisible(true);

			//			printDebugData(Board.table);

		}
	}



	static class Load_Action implements ActionListener {        
		public void actionPerformed (ActionEvent e) { 
			System.out.println("Load Action entered");
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = fileChooser.showOpenDialog(frame);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				System.out.println("Selected file: " + selectedFile.getAbsolutePath());
				try{
					br = new BufferedReader(new FileReader(selectedFile));



					String line = br.readLine();

					System.out.println("line: "+line);

					String [] strings = line.split(" ");
					System.out.println("strings[0]: "+strings[0]);
					System.out.println("strings[1]: "+strings[1]);
					System.out.println("strings[2]: "+strings[2]);
					System.out.println("strings[3]: "+strings[3]);
					strings[3] = strings[3].trim();
					N=Integer.parseInt(strings[0]);
					p=Integer.parseInt(strings[1]);
					q=Integer.parseInt(strings[2]);
					M=Integer.parseInt(strings[3]);
					System.out.println("N: " + N);


					if(N != p*q)
						throw new InvalidStartInputException("N doesn't equal p*q!");
					if(M > Math.pow(N,2))
						throw new InvalidStartInputException("M is greater than all the cells on the board!");

					grid = new String[N][N];

					((DefaultTableModel) table.getModel()).setRowCount(N);
					((DefaultTableModel) table.getModel()).setColumnCount(N);
					((DefaultTableModel) table.getModel()).fireTableDataChanged();
					table.setRowHeight((int)(450/N));
					System.out.println("newRowCount: " + model.getRowCount());


					int i=0;
					do {
						line = br.readLine();
						System.out.println("second line: " + line);
						if(line != null) {
							String [] curline = null;
							curline = line.split(" ");
							System.out.println("curline: " + Arrays.toString(curline)); 
							for(int j=0; j<N;j++) {
								grid[i][j] = curline[j];
								System.out.println("curline[j]: " + curline[j]);
								table.getModel().setValueAt(curline[j], i, j);
								//								valid[i][j] = new Cell(Integer.parseInt(curline[j]));
							}
							i++;
						}
					} while (line != null);
					br.close();
				}catch(InvalidStartInputException f) {
					System.err.println("Bad Input Error: " + f.getMessage());
				}
				catch(FileNotFoundException g) {
					System.err.println("Bad Input Error: " + g.getMessage());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			SolveGame.setEnabled(true);
		}
	} 


	static class Solve_Action implements ActionListener {        
		public void actionPerformed (ActionEvent e) {
			System.out.println("Solve Action entered");
			SolveSudoku(0,0);
			//			Thread thread = new Thread(new Runnable() {
			//				@Override
			//				public void run() {
			//					System.out.println("Sudoku Solve Started");
			//					SolveSudoku();
			//				}
			//			});

			//			thread.start();
			//			long endTimeMillis = System.currentTimeMillis() + 60000;
			//			while (thread.isAlive()) {
			//				if (System.currentTimeMillis() > endTimeMillis) {
			//					System.out.println("you're done");
			//					break;
			//				}
			//				try {
			//					System.out.println("still going");
			//					Thread.sleep(500);
			//				}
			//				catch (InterruptedException t) {}
			//			}
			//		}

		}
	}

	public static boolean SolveSudoku() {
		//		long endTimeMillis = System.currentTimeMillis() + 60000;
		System.out.println("SolveSudoku loop entered");
		System.out.println("How're we doin?");
		printDebugData(table);

		if (!FindUnassignedLocation().getFoundUnassigned()) {
			System.out.println("didn't find an unassigned");
			return true;
		}
		else {
			System.out.println("dang, still not solved");
			System.out.println("row: "+i);
			for (int num = 1; num <= N; num++) { // consider digits 1 to 9
				//					System.out.println("Now we're in the NoConflicts loop");
				if (NoConflicts(i, j, num)) { // if looks promising,
					System.out.println("found a value that might work");
					table.getModel().setValueAt(num, i, j); // make tentative assignment
					((DefaultTableModel) table.getModel()).fireTableDataChanged();
					if(i+1 == N) {
						if(j+1 == N)
							if (SolveSudoku(i,j++))
								return true;
					}
					else {
						if (SolveSudoku())
							return true;
						else
							table.getModel().setValueAt(0, i, j); // failure, unmake & try again
					}
				}
			}
			return false;


			//			if (System.currentTimeMillis() > endTimeMillis) {
			//				System.out.println("died");
			//				return false;
			//			}
			//			else
			//				return false;
			//		}
		}
	}

	public static Unassigned FindUnassignedLocation() {
		System.out.println("FindUnassignedLocation");
		printDebugData(table);
		System.out.println("getRowCount(): "+table.getModel().getRowCount());
		System.out.println("getColCount(): "+table.getModel().getColumnCount());
		for (int row = 0; row < table.getModel().getRowCount(); row++) {
			for (int col = 0; col < table.getModel().getColumnCount(); col++) {
				System.out.println("Am I getting out of this loop? row: ("+row+", "+col+")");
				System.out.println("table value at: " + table.getModel().getValueAt(row, col));
				if ((Integer)table.getModel().getValueAt(row, col) == 0) {
					System.out.println("Did we get a match?");
					return new Unassigned(true,row,col);
				}
			}
		}
		return new Unassigned(false,0,0);
	}

	public static class Unassigned {
		private boolean foundUnassigned;
		private int row;
		private int col;

		Unassigned(boolean found, int r, int c) {
			foundUnassigned = found;
			row = r;
			col = c;
		}

		private boolean getFoundUnassigned() {
			return foundUnassigned;
		}

		private int getRow() {
			return row;
		}

		private int getColumn() {
			return col;
		}
	}

	public static boolean NoConflicts(int row, int col, int num) {
		System.out.println("NoConflicts() row: "+row+", col: "+col+"num: "+num);
		return !UsedInRow(row, num) && !UsedInCol(col, num)
				&& !UsedInBox((row - row%p) , (col - col%q), num);
	}

	static class Clear_Action implements ActionListener {        
		public void actionPerformed (ActionEvent e) {     
			System.out.println("Clear Action entered");
			((DefaultTableModel) table.getModel()).getDataVector().removeAllElements();
			((DefaultTableModel) table.getModel()).setRowCount(N);
			((DefaultTableModel) table.getModel()).setColumnCount(N);
			((DefaultTableModel) table.getModel()).fireTableDataChanged();
			for(int i=0; i<N;i++)
				for(int j=0; j<N;j++)
					table.getModel().setValueAt(0, i, j);
			SolveGame.setEnabled(false);
		}
	} 

	static class SetParams_Action implements ActionListener {        
		public void actionPerformed (ActionEvent e) {     
			System.out.println("SetParams Action entered");
			p = Integer.parseInt(theSizeP.getText().replace(" ", ""));
			System.out.println("p: " + p);
			q = Integer.parseInt(theSizeQ.getText().replace(" ", ""));
			System.out.println("q: " + q);
			M = Integer.parseInt(theSizeM.getText().replace(" ", ""));
			System.out.println("M: " + M);
			N = Integer.parseInt(theSizeN.getText().replace(" ", ""));
			System.out.println("n: " + N);
			//			N = p*q;
			//			constraintVals = getConstraintValues(N);
			((DefaultTableModel) table.getModel()).setRowCount(N);
			((DefaultTableModel) table.getModel()).setColumnCount(N);
			valid = new Cell[N][N];

			table.setRowHeight((int)(450/N));
			System.out.println("newRowCount: " + model.getRowCount());
			for(int i=0; i<N;i++)
				for(int j=0; j<N;j++) {
					((DefaultTableModel) table.getModel()).setValueAt(0, i, j);
				}

			//	Min + (int)(Math.random() * ((Max - Min) + 1))
			int randomValue=0;
			int randomRow=0;
			int randomColumn=0;


			//still have to update valid to tableModel
			int count = 0;
			while(count < M) {
				randomValue = 1+(int)(Math.random()*(N)); 
				randomRow = (int)(Math.random()*(N-1)); 
				randomColumn = (int)(Math.random()*(N-1));
				System.out.println("randomValue: " + randomValue);
				System.out.println("randomRow: " + randomRow);
				System.out.println("randomColumn: " + randomColumn);

				if(NoConflicts(randomRow, randomColumn, randomValue)) {
					((DefaultTableModel) table.getModel()).setValueAt(randomValue, randomRow, randomColumn);
					count++;
				}
			}

			((DefaultTableModel) table.getModel()).fireTableDataChanged();
			SolveGame.setEnabled(true);
			setParams.dispose();
		}
	}



	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					createAndShowGUI();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidStartInputException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}

