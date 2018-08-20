import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Stream;

import javax.print.event.PrintJobAttributeListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;

import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;


public class FileExplorer extends JFrame {

	private JPanel contentPane;
	private JLabel lblCurrentDirectory;
	private JTable filesAndDirectoriesTable;	
	private MyFileTableModel tableModel;
	private TableRowSorter<TableModel> sorter; 	
	private JScrollPane scrollPane;	
	private JButton btnGoToDirectory;
	private JButton btnBack;
	private JPopupMenu popupMenu;	
	private JMenuItem showOption;
	private JMenuItem makeCopyOption;
	private JMenuItem deleteOption;
	private JMenuItem renameOption;	
	private JTextField textFieldEnterDirectoryPath;	
	private Stack<String> directoryHistory;
	private String startDirectory;
	
	
	public FileExplorer(String start) 
	{
		directoryHistory = new Stack<String>();
		startDirectory = start;
		
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try
				{
					createComponents();
					setComponentLocations();
					setComponentSizes();
					setComponentSettings();	
					setActionListeners();
					addComponentsToPanel();
					
					pack();
					setVisible(true);
					btnGoToDirectory.doClick();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	private void createComponents()
	{
		contentPane = SwingComponentFactory.createJPanel();
		filesAndDirectoriesTable = SwingComponentFactory.createJTable();
		scrollPane = SwingComponentFactory.createJScrollPane(filesAndDirectoriesTable);
		textFieldEnterDirectoryPath = SwingComponentFactory.createJTextField(startDirectory); 
		lblCurrentDirectory = SwingComponentFactory.createJLabel("Current Directory:");
		btnGoToDirectory = SwingComponentFactory.createJButton("Go To Directory");
		btnBack = SwingComponentFactory.createJButton("Back");
		popupMenu = SwingComponentFactory.createJPopupMenu();
		showOption = SwingComponentFactory.createJMenuItem("View");
		makeCopyOption = SwingComponentFactory.createJMenuItem("Make A Copy");
		deleteOption = SwingComponentFactory.createJMenuItem("Delete");
		renameOption = SwingComponentFactory.createJMenuItem("Rename");
	}
	
	private void setComponentLocations()
	{
		setBounds(100, 100, 450, 300);
		filesAndDirectoriesTable.setBounds(451, 166, 304, 200);
		scrollPane.setBounds(10, 165, 764, 396);
		textFieldEnterDirectoryPath.setBounds(10, 32, 365, 23);
		lblCurrentDirectory.setBounds(10, 19, 125, 14);
		btnBack.setLocation(10, 66);
		btnGoToDirectory.setLocation(250, 66);
	}
	
	private void setComponentSizes()
	{
		setMinimumSize(new Dimension(800,600));
		btnGoToDirectory.setSize(125, 23);
		btnBack.setSize(71, 23);
	}

	private void setComponentSettings()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setContentPane(contentPane);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		textFieldEnterDirectoryPath.setColumns(10);	
		popupMenu.add(renameOption);
		popupMenu.add(deleteOption);
		popupMenu.add(showOption);
		popupMenu.add(makeCopyOption);
		filesAndDirectoriesTable.setComponentPopupMenu(popupMenu);
	}
	
	private void setActionListeners()
	{
		filesAndDirectoriesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if(!e.getValueIsAdjusting())
				{
				}
			}
		});											
		btnGoToDirectory.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				displayDirectoryPathInTable(textFieldEnterDirectoryPath.getText());
			}
		});							
		btnBack.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				viewPreviousDirectory();
			}
		});
		popupMenu.addPopupMenuListener(new PopupMenuListener()
		{
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run()
					{
						setRowWhereClicked();
					}
				});
			}
			
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
			{	
			}
			
			@Override
			public void popupMenuCanceled(PopupMenuEvent e)
			{
			}
		});
		showOption.addActionListener(new ActionListener()
		{			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				openMyFile(getSelectedFile());
			}
		});
		makeCopyOption.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				makeCopyOfFile(getSelectedFile());
			}
		});		
		deleteOption.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				deleteMyFileIfExists(getSelectedFile());
			}
		});		
		renameOption.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String newName = JOptionPane.showInputDialog("Enter a new name: ");
				MyFile selected = getSelectedFile();
				while(!checkMatchingExtensions(selected.getMyFileName(), newName))
				{
					JOptionPane.showMessageDialog(contentPane, "invalid file extension");
					newName = JOptionPane.showInputDialog("Enter a new name: ");
				}
				renameFile(selected, newName);
			}
		});
	}

	private void addComponentsToPanel()
	{
		contentPane.add(textFieldEnterDirectoryPath);
		contentPane.add(btnGoToDirectory);
		contentPane.add(lblCurrentDirectory);
		contentPane.add(scrollPane);
		contentPane.add(btnBack);
	}
	
	private boolean checkMatchingExtensions(String file1, String file2)
	{
		String ext = MyFile.getFileExtension(file1);
		if(ext.equals(""))
		{
			return false;
		}
		if(ext.length()+2 > file2.length())
		{
			return false;
		}
		if(!(ext.equals(file2.substring(file2.length()-ext.length()))))
		{
			return false;
		}
		return true;
	}
	
	public MyFile getSelectedFile()
	{
		return tableModel.getMyFile(filesAndDirectoriesTable.convertRowIndexToModel(filesAndDirectoriesTable.getSelectedRow()));
	}
	
	public void openMyFile(MyFile file)
	{
		System.out.println("Location: " + file.getDirectory());
		if(file.isDirectory())
		{
			displayDirectoryPathInTable(filesAndDirectoriesTable.getValueAt(filesAndDirectoriesTable.getSelectedRow(),1).toString());
		}
		else 
		{
			String extension = file.getFileExtension();
			switch(extension)
			{
			case "txt":
				TextFileViewer v = new TextFileViewer(file.getMyFilePath()); 
				break;
			case "png": case "jpg": case "gif": 
			case "bmp": case "jpeg": case "jpe": 
			case "jfif":
				ImageFileViewer ifv = new ImageFileViewer(file.getMyFilePath()); 
				break;
			default:
				if(!openWithDefaultProgram(file))
				{
					JOptionPane.showMessageDialog(contentPane, "Cannot open file type in this app");
				}
				break;
			}
		}
	}
	
	private void renameFile(MyFile file, String newName)
	{
		MyFile renamedFile = new MyFile(new File(file.getDirectory() + "\\" + newName));
		if(!file.myRenameTo(renamedFile))
		{
			System.out.println("Unsuccessfull rename");
		}
		else
		{
			tableModel.setValueAt(newName, filesAndDirectoriesTable.convertRowIndexToModel(filesAndDirectoriesTable.getSelectedRow()), 0);
			tableModel.setValueAt(file.getMyFilePath(), filesAndDirectoriesTable.convertRowIndexToModel(filesAndDirectoriesTable.getSelectedRow()), 1);
		}
	}
	
	private boolean openWithDefaultProgram(MyFile file)
	{
		try
		{
			Desktop.getDesktop().open(file);
		} 
		catch (IOException e)
		{
			System.out.println("Error opening Desktop program");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void makeCopyOfFile(MyFile file)
	{
		if(file.isDirectory())
		{
			JOptionPane.showMessageDialog(contentPane, "Cannot copy directories (yet)");
		}
		else
		{
			try
			{
				String newFilePath = file.getDirectory() + "\\" + file.getMyFileNameWithoutExtension() +
						"_copy." + file.getFileExtension();
				MyFile temp = new MyFile(new File(newFilePath));
				Files.copy(
						file.toPath(),
						temp.toPath(),
						StandardCopyOption.REPLACE_EXISTING,
						StandardCopyOption.COPY_ATTRIBUTES);
				tableModel.addMyFile(temp);
			} 
			catch (IOException e1)
			{
				System.out.println("Error in making copy of file");
				e1.printStackTrace();
			}
		}
	}
	
	public void setRowWhereClicked()
	{
		int rowAtPoint = filesAndDirectoriesTable.rowAtPoint(
				SwingUtilities.convertPoint(
						popupMenu, 
						new Point(0,0), 
						filesAndDirectoriesTable));
		if(rowAtPoint > -1)
		{
			filesAndDirectoriesTable.setRowSelectionInterval(rowAtPoint, rowAtPoint);
		}
	}
	
	public void viewPreviousDirectory()
	{
		if(directoryHistory.size() < 2)
		{
			System.out.println("No more history");
		}
		else
		{
			directoryHistory.pop();
			String prev = directoryHistory.pop();
			displayDirectoryPathInTable(prev);
		}
	}
	
	public void deleteMyFileIfExists(MyFile file)
	{
		try
		{
			Files.deleteIfExists(file.toPath());
			tableModel.removeMyFile(filesAndDirectoriesTable.convertRowIndexToModel(filesAndDirectoriesTable.getSelectedRow()));
		}
		catch(NoSuchFileException e)
		{
			System.out.println("Error in deleting file, does not exist");
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			System.out.println("Error in deleting file");
			e.printStackTrace();
		}
	}
	
	public void displayDirectoryPathInTable(String directory)
	{
		File folder = new File(directory);
		File[] files = folder.listFiles();
		tableModel = new MyFileTableModel();
		
		for(File filepath : files)
		{
			tableModel.addMyFile(new MyFile(filepath));
		}
		textFieldEnterDirectoryPath.setText(directory);
		directoryHistory.add(directory);
		filesAndDirectoriesTable.setModel(tableModel);
		sorter = new TableRowSorter<TableModel>(filesAndDirectoriesTable.getModel());
		filesAndDirectoriesTable.setRowSorter(sorter);
		
		// Sort rows by type, then name
		List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
		sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
	}
}


 class MyFile extends File {
	private String myFileName;
	private String myFilePath;
	private String sizeInBytes;
	private String directory;
	
	public MyFile(String fileName, String filePath)
	{
		super(filePath);
		this.myFileName = fileName;
		this.myFilePath = filePath;
		this.sizeInBytes = Long.toString(this.length());
		this.directory = this.myFilePath.substring(0, myFilePath.lastIndexOf(File.separator));
	}
	
	public MyFile(File file)
	{
		super(file.getPath());
		this.myFileName = file.getName();
		this.myFilePath = file.getPath();
		if(file.isDirectory())
		{
			System.out.println(file.getPath() + " is not a file");
			this.sizeInBytes = "";
		}
		else
		{
			this.sizeInBytes = Long.toString(file.length());
		}
		this.directory = this.myFilePath.substring(0, myFilePath.lastIndexOf(File.separator));
	}
	
	public String getDirectory()
	{
		return directory;
	}
	
	public void setDirectory(String directory)
	{
		this.directory = directory;
	}
	
	public String getFileExtension()
	{
		String extension = "";
		int i = myFileName.lastIndexOf('.');
		if(i>0)
		{
			extension = myFileName.substring(i+1);
		}
		return extension;
	}
	
	static public String getFileExtension(String name)
	{
		String extension = "";
		int i = name.lastIndexOf('.');
		if(i>0)
		{
			extension = name.substring(i+1);
		}
		return extension;
	}
	
	public String getMyFileNameWithoutExtension()
	{
		String strippedFiledName = "";
		int i = myFileName.lastIndexOf('.');
		if(i>0)
		{
			strippedFiledName = myFileName.substring(0,i);
		}
		return strippedFiledName;
	}
	
	public String getMyFileName()
	{
		return myFileName;
	}
	
	public boolean myRenameTo(MyFile file)
	{
		boolean result = this.renameTo(file);
		if(result)
		{
			myFileName = file.getName();
			myFilePath = file.getPath();
			directory = file.getMyFilePath().substring(0, file.getMyFilePath().lastIndexOf(File.separator));
		}
		return result;
	}
	
	public void setMyFileName(String fileName)
	{
		this.myFileName = fileName;
	}

	public String getMyFilePath()
	{
		return myFilePath;
	}

	public void setMyFilePath(String filePath)
	{
		this.myFilePath = filePath;
	}
	
	public String getSizeInBytes()
	{
		return sizeInBytes;
	}
	
	public void setSizeInBytes(long n)
	{
		sizeInBytes = Long.toString(n);
	}
 }

class MyFileTableModel extends AbstractTableModel
{
	private String[] columnNames = 
	{
				"File Name",
				"File Path",
				"File Size"
	};
	private List<MyFile> files;
	
	public MyFileTableModel()
	{
		files = new ArrayList<MyFile>();
	}
	
	public MyFileTableModel(List<MyFile> f)
	{
		files = f;
	}
	
	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int col)
	{
		return columnNames[col];
	}
	
	@Override
	public int getRowCount()
	{
		return files.size();
	}
	
	@Override
	public Class getColumnClass(int column)
	{
	    switch (column)
	    {
	        default: return String.class;
	    }
	}
	 
	@Override
	public boolean isCellEditable(int row, int column)
	{
	    switch (column)
	    {
	        default: return false;
	    }
	}
	 
	@Override
	public Object getValueAt(int row, int column)
	{
	    MyFile file = getMyFile(row);
	 
	    switch (column)
	    {
	        case 0: return file.getMyFileName();
	        case 1: return file.getMyFilePath();
	        case 2: return file.getSizeInBytes();
	        default: return null;
	    }
	}
	 
	@Override
	public void setValueAt(Object value, int row, int column)
	{
		MyFile file = getMyFile(row);
	 
	    switch (column)
	    {
	        case 0: file.setMyFileName((String)value); break;
	        case 1: file.setMyFilePath((String)value); break;
	        case 2: file.setSizeInBytes((long)value); break;
	    }
	    fireTableCellUpdated(row, column);
	}
	
	public MyFile getMyFile(int row)
	{
	    return files.get( row );
	}
	
	public void addMyFile(MyFile file)
	{
	    insertMyFile(getRowCount(), file);
	}
	 
	public void insertMyFile(int row, MyFile file)
	{
	    files.add(row, file);
	    fireTableRowsInserted(row, row);
	}

	public void removeMyFile(int row)
	{
		files.remove(row);
	    fireTableRowsDeleted(row, row);
	}
}