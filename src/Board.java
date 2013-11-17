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


	private static Vector<Vector<String>> vectors;
	private static Set<String> constraintVals;

	private static JTable table;
	private DefaultTableModel model;

	public Board(int x, int y) {
		System.out.println("What's going on?");
		constraintVals = getConstraintValues(N);
		System.out.println("What's going on?");
		JTable boardGrid = new JTable(p,q);		

		//create data
		System.out.println("vectors made?");


		model = new DefaultTableModel(); 
		table = new JTable(model); 

		model.setRowCount(N);
		model.setColumnCount(N);
		// Create a couple of columns 
		// Append a row 
		System.out.println("p: " + p);
		System.out.println("q: " + q);

		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				model.setValueAt(vectors.get(i).get(j), i, j);
				System.out.println("model.getRowCount(): " + model.getRowCount());
				System.out.println("model.getColumnCount(): " + model.getColumnCount());

				System.out.println("element getting added: " + vectors.get(i).elementAt(i) + " at (" + i + ", " + j + ")");
				printDebugData(table);
			}
		}

		System.out.println("vectors created: "+ vectors);

		//create table interface
		boardGrid = table;
		table.setSize(new Dimension(2000,2000));
		table.setRowHeight(75);
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

		//Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		//Add the scroll pane to this panel.
		add(scrollPane);
	}

	public Set<String> getConstraintValues(int n) {
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
		return vectors.get(i).get(j);
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

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 * @throws IOException 
	 * @throws InvalidStartInputException 
	 */
	@SuppressWarnings("resource")
	private static void createAndShowGUI() throws IOException, InvalidStartInputException {
		JFrame frame = new JFrame("Board");
		BorderLayout frameGrid = new BorderLayout();
		frame.setLayout(frameGrid);
		frame.setSize(600,600);
		vectors = new Vector<Vector<String>>(N);

		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\owner\\workspace\\MonsterSudoku\\src\\input.txt"));
		String line = br.readLine();
		System.out.println("line: "+line);

		String [] strings = line.split(" ");
		System.out.println("strings[0]: "+strings[0]);
		System.out.println("strings[1]: "+strings[1]);
		System.out.println("strings[2]: "+strings[2]);
		System.out.println("strings[3]: "+strings[3]);
		strings[3] = strings[3].replace(" ","");
		N=Integer.parseInt(strings[0]);
		p=Integer.parseInt(strings[1]);
		q=Integer.parseInt(strings[2]);
		M=Integer.parseInt(strings[3]);


		if(N != p*q)
			throw new InvalidStartInputException("N doesn't equal p*q!");
		if(M > Math.pow(N,2))
			throw new InvalidStartInputException("M is greater than all the cells on the board!");

		do{
			line = br.readLine();
			System.out.println("second line: " + line);
			if(line != null) {
				String [] curline = null;
				curline = line.split(" ");
				System.out.println("curline: " + Arrays.toString(curline));
				Vector<String> strVector = new Vector<String>(Arrays.asList(curline));
				vectors.add(strVector);
			}
		} while (line != null);


		br.close();

		final JPanel buttonInterface = new JPanel();
		System.out.println("board size about to be created");
		JPanel panel = new JPanel();
		FlowLayout boardSizeGrid = new FlowLayout(2,2,1);
		panel.setLayout(boardSizeGrid);
		
		//Create and set up the content pane.
		System.out.println("newContentPane about to be created");
		Board newContentPane = new Board(p,q);
		newContentPane.setOpaque(true); //content panes must be opaque
		System.out.println("Does this get here?");

		frame.setContentPane(newContentPane);
		JButton NewGame = new JButton("Start New Game");
		buttonInterface.add(NewGame);
		NewGame.addActionListener (new Start_Action());
		
		JButton LoadGame = new JButton("Load Game");
		buttonInterface.add(LoadGame);
		NewGame.addActionListener (new Load_Action());

		JButton SolveGame = new JButton("Solve Game");
		buttonInterface.add(SolveGame);
		SolveGame.addActionListener (new Solve_Action()); 
		frame.setVisible(true);
		JButton ClearGame = new JButton("Clear Game");
		buttonInterface.add(ClearGame);
		ClearGame.addActionListener (new Clear_Action()); 
		frame.add(buttonInterface);
		//Display the window.


		BoxLayout buttonFlow = new BoxLayout(buttonInterface,3);
		FlowLayout gridStack = new FlowLayout();
		buttonInterface.setLayout(buttonFlow);
		frame.setLayout(gridStack);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//			}
		//		});


		//Create and set up the main interface, but don't show it yet

	}


	static class Start_Action implements ActionListener {        
		public void actionPerformed (ActionEvent e) {     
			System.out.println("Start Action entered");
			for(int i=0;i<p*q;i++){
				double rndRow = Math.random();
				double rndCol = Math.random();
				double rndDifficulty = Math.random();
				if(rndDifficulty<0.6) {
					vectors.get(i).set((int)(p*q*rndCol), "1A");
					Board.table.getModel().setValueAt(vectors.get(i).get((int)(p*q*rndCol)), (int)(p*q*rndRow),(int)(p*q*rndCol));
					System.out.println("random value " + 3 + " set at (" + (int)(p*q*rndRow) + ", " + (int)(p*q*rndCol) + ")");
				}
			}
			printDebugData(Board.table);
		}
	}

	static class Load_Action implements ActionListener {        
		public void actionPerformed (ActionEvent e) {     
			System.out.println("Load Action entered");
		}
	} 
	
	static class Solve_Action implements ActionListener {        
		public void actionPerformed (ActionEvent e) {     
			System.out.println("Solve Action entered");
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
					table.getModel().setValueAt(0, i, j);
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

