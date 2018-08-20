import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class SwingComponentFactory
{
	public static JButton createJButton(String text)
	{
		JButton button = new JButton(text);
		return button;
	}
	
	public static JPanel createJPanel()
	{
		JPanel panel = new JPanel();
		return panel;
	}
	
	public static JTextField createJTextField()
	{
		JTextField textField = new JTextField();
		return textField;
	}
	public static JTextField createJTextField(String text)
	{
		JTextField textField = new JTextField(text);
		return textField;
	}
	
	public static JTable createJTable()
	{
		JTable table = new JTable();
		return table;
	}
	
	public static JScrollPane createJScrollPane()
	{
		JScrollPane scrollPane = new JScrollPane();
		return scrollPane;
	}
	
	public static JScrollPane createJScrollPane(JComponent component)
	{
		JScrollPane scrollPane = new JScrollPane(component);
		return scrollPane;
	}
	
	public static JLabel createJLabel(String text)
	{
		JLabel label = new JLabel(text);
		return label;
	}
	
	public static JLabel createJLabel()
	{
		JLabel label = new JLabel();
		return label;
	}
	
	public static JLabel createJLabel(ImageIcon image) 
	{
		JLabel label = new JLabel(image);
		return label;
	}
	
	public static JPopupMenu createJPopupMenu()
	{
		JPopupMenu popupMenu = new JPopupMenu();
		return popupMenu;
	}
	
	public static JMenuItem createJMenuItem(String text)
	{
		JMenuItem menuItem = new JMenuItem(text);
		return menuItem;
	}
	
	public static JEditorPane createJEditorPane()
	{
		JEditorPane editorPane = new JEditorPane();
		return editorPane;
	}
	
	public static JToolBar createJToolBar()
	{
		JToolBar toolBar = new JToolBar();
		return toolBar;
	}
	
	public static JMenuBar createJMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		return menuBar;
	}
	
	public static JMenu createJMenu(String text)
	{
		JMenu menu = new JMenu(text);
		return menu;
	}
}