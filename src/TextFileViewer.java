import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class TextFileViewer extends JFrame {

	private JPanel contentPane;
	private JEditorPane editorPane;
	private JScrollPane scrollPane;
	private JToolBar toolBar;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem saveFileMenuItem;
	private String fileName;
	private File file;
	
	public TextFileViewer(String name) 
	{
		fileName = name;
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
					addComponentsToPanel();
					addActionListeners();
							
					setVisible(true);
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
		editorPane = SwingComponentFactory.createJEditorPane();
		scrollPane = SwingComponentFactory.createJScrollPane(editorPane);
		toolBar = SwingComponentFactory.createJToolBar();
		menuBar = SwingComponentFactory.createJMenuBar();
		fileMenu = SwingComponentFactory.createJMenu("File");
		saveFileMenuItem = SwingComponentFactory.createJMenuItem("Save");
	}
	
	private void setComponentLocations()
	{
		setBounds(100, 100, 450, 309);
	}
	
	private void setComponentSizes()
	{
		scrollPane.setPreferredSize(new Dimension(250,145));
		scrollPane.setMinimumSize(new Dimension(10,10));
	}
	
	private void setComponentSettings()
	{
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		editorPane.setEditable(true);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		fileMenu.add(saveFileMenuItem);
		menuBar.add(fileMenu);
		toolBar.add(menuBar);
		
		// setup file
		if(fileName != null)
		{
			try 
			{
				file = new File(fileName);
				editorPane.setPage(file.toURI().toURL());
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void addComponentsToPanel()
	{
		contentPane.add(scrollPane);
		contentPane.add(toolBar, BorderLayout.NORTH);
	}
	
	private void addActionListeners()
	{
		saveFileMenuItem.addActionListener(new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Saving file...");
				saveFile();
			}
		});
	}
	
	public void saveFile()
	{
		File file = null;
		FileWriter out = null;
		
		try 
		{
			file = new File(fileName);
			out = new FileWriter(file);
			out.write(editorPane.getText());
			out.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e) 
		{
			e.printStackTrace();
		}
	}
}
