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
	private static JTextArea theSizeP;
	private static JTextArea theSizeQ;
	private static JTextArea theSizeM;

	private static Vector<Vector<String>> vectors;
	private static String[][] grid;
	private static String[][] freeCells;
	private static Set<String> constraintVals;
	private static HashMap<String,Integer> odometerStyleMap;

	private static JTable table;
	private static DefaultTableModel model;


	public Board(int x, int y) {
		p=x;
		q=y;
		N=p*q;
		grid = new String[p*q][p*q];
		constraintVals = getConstraintValues(N);
		for(int i=0; i<N;i++)
			for(int j=0; j<N;j++)
				grid[i][j] = "0";

		model = new DefaultTableModel(); 
		table = new JTable(model); 

		model.setRowCount(N);
		model.setColumnCount(N);

		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				model.setValueAt(grid[i][j], i, j);
				printDebugData(table);
			}
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
					printDebugData(table);
				}
			});
		}

		
		add(table);
	}

	public static Set<String> getConstraintValues(int n) {
		Set<String> s = new HashSet<String>();
		String letter = "";
		String num;
		for(int i=1;i<=n;i++){
			letter=Character.toString((char)(i+54));
			num=String.valueOf(i);
			if(i<10) {
				s.add(num);
				System.out.println("s: " + s);
			}
			else if (i<36)
				s.add(letter);
			else if (i<62) 
				s.add(num+num);
			else{};
		}
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



		//		System.out.println("Load Action entered");
		//		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\owner\\workspace\\MonsterSudoku\\src\\input.txt"));
		//		String line = br.readLine();
		//		System.out.println("line: "+line);
		//
		//		String [] strings = line.split(" ");
		//		System.out.println("strings[0]: "+strings[0]);
		//		System.out.println("strings[1]: "+strings[1]);
		//		System.out.println("strings[2]: "+strings[2]);
		//		System.out.println("strings[3]: "+strings[3]);
		//		strings[3] = strings[3].replace(" ","");
		//		N=Integer.parseInt(strings[0]);
		//		p=Integer.parseInt(strings[1]);
		//		q=Integer.parseInt(strings[2]);
		//		M=Integer.parseInt(strings[3]);
		//
		//
		//		if(N != p*q)
		//			throw new InvalidStartInputException("N doesn't equal p*q!");
		//		if(M > Math.pow(N,2))
		//			throw new InvalidStartInputException("M is greater than all the cells on the board!");
		//
		//		do {
		//			line = br.readLine();
		//			System.out.println("second line: " + line);
		//			if(line != null) {
		//				String [] curline = null;
		//				curline = line.split(" ");
		//				System.out.println("curline: " + Arrays.toString(curline));
		//				Vector<String> strVector = new Vector<String>(Arrays.asList(curline));
		//				vectors.add(strVector);
		//			}
		//		} while (line != null);
		//
		//
		//		br.close();



		final JPanel buttonInterface = new JPanel();
		JPanel panel = new JPanel();
		FlowLayout boardSizeGrid = new FlowLayout(2,2,1);
		panel.setLayout(boardSizeGrid);

		//Create and set up the content pane.
		Board newContentPane = new Board(p,q);
		newContentPane.setPreferredSize(new Dimension(500, 600));

		frame.setContentPane(newContentPane);

		NewGame = new JButton("Start New Game");
		NewGame.setToolTipText("Click here to start a new random game");
		buttonInterface.add(NewGame);
		NewGame.addActionListener (new Start_Action());

		LoadGame = new JButton("Load Game");
		LoadGame.setToolTipText("Click here to start a new game from file");
		buttonInterface.add(LoadGame);
		NewGame.addActionListener (new Load_Action());

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
			JLabel sizeP = new JLabel("Size of P: ");
			theSizeP = new JTextArea(1,5);
			JLabel sizeQ = new JLabel("Size of Q: ");
			theSizeQ = new JTextArea(1,5);
			JLabel sizeM = new JLabel("Size of M: ");
			theSizeM = new JTextArea(1,5);
			JButton paramsOK = new JButton("OK");
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
			
			printDebugData(Board.table);

		}
	}

	static class Load_Action implements ActionListener {        
		public void actionPerformed (ActionEvent e) {     
			SolveGame.setEnabled(true);
		}
	} 
	

	static class Solve_Action implements ActionListener {        
		public void actionPerformed (ActionEvent e) {   
			System.out.println("Solve Action entered");
			Thread thread = new Thread(new Runnable() {
	            @Override
	            public void run() {
	                System.out.println("0");
	                method();
	            }
	        });
			
	        thread.start();
	        long endTimeMillis = System.currentTimeMillis() + 1;
	        while (thread.isAlive()) {
	            if (System.currentTimeMillis() > endTimeMillis) {
	                System.out.println("1");
	                break;
	            }
	            try {
	                System.out.println("2");
	                Thread.sleep(500);
	            }
	            catch (InterruptedException t) {}
	        }
		}
	}
	
    public static void method() {
        long endTimeMillis = System.currentTimeMillis() + 1;
        while (true) {
            // method logic
            System.out.println("going strong");
            if (System.currentTimeMillis() > endTimeMillis) {
                // do some clean-up
                System.out.println("died");
                return;
            }
        }
    }

	static class Clear_Action implements ActionListener {        
		public void actionPerformed (ActionEvent e) {     
			System.out.println("Clear Action entered");
			((DefaultTableModel) table.getModel()).getDataVector().removeAllElements();
			((DefaultTableModel) table.getModel()).setRowCount(p*q);
			((DefaultTableModel) table.getModel()).setColumnCount(p*q);
			((DefaultTableModel) table.getModel()).fireTableDataChanged();
			for(int i=0; i<p*q;i++)
				for(int j=0; j<p*q;j++)
					table.getModel().setValueAt("0", i, j);
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
			N = p*q;
			constraintVals = getConstraintValues(N);
			((DefaultTableModel) table.getModel()).setRowCount(N);
			((DefaultTableModel) table.getModel()).setColumnCount(N);
			((DefaultTableModel) table.getModel()).fireTableDataChanged();
			table.setRowHeight((int)(450/N));
			System.out.println("newRowCount: " + model.getRowCount());
			for(int i=0; i<p*q;i++)
				for(int j=0; j<p*q;j++)
					table.getModel().setValueAt("0", i, j);
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

