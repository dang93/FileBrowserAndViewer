import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.border.MatteBorder;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import java.awt.Button;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.border.BevelBorder;
import javax.swing.UIManager;
import java.awt.SystemColor;

public class App extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldEnterDirectoryPrompt;
	private JLabel labelEnterDirectoryPromt;
	private Image logo;
	private ImageIcon logoIcon;
	private JLabel labelPic;
	private JButton btnStart;
	
	public App() 
	{
		setResizable(false);
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
					setComponentListeners();
					addComponentsToPanel();
					
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
		try
		{
			logo = ImageIO.read(new File("bgrndapp.png"));
		} 
		catch (IOException e)
		{
			System.out.println("Error opening background image");
			e.printStackTrace();
		}
		logoIcon = new ImageIcon(logo);
		labelPic = SwingComponentFactory.createJLabel(new ImageIcon());
		textFieldEnterDirectoryPrompt = SwingComponentFactory.createJTextField();
		labelEnterDirectoryPromt = SwingComponentFactory.createJLabel("Enter a directory path:");
		btnStart = SwingComponentFactory.createJButton("Start");
	}
	
	private void setComponentLocations()
	{
		setBounds(100, 100, 595, 435);
	}
	
	private void setComponentSizes()
	{
		labelPic.setBounds(5, 11, 280, 387);
		textFieldEnterDirectoryPrompt.setBounds(292, 80, 283, 20);
		labelEnterDirectoryPromt.setBounds(295, 61, 186, 14);
		btnStart.setBounds(449, 111, 126, 30);
	}
	
	private void setComponentSettings()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(contentPane);
		contentPane.setBackground(SystemColor.menu);
		contentPane.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.LIGHT_GRAY));
		contentPane.setLayout(null);
		labelPic.setIcon(logoIcon);
		textFieldEnterDirectoryPrompt.setText("C:\\Users\\dguer_000\\Desktop\\TestFolder");
		textFieldEnterDirectoryPrompt.setColumns(10);
		labelEnterDirectoryPromt.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnStart.setBorderPainted(false);
	}
	
	private void setComponentListeners()
	{
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				FileExplorer explorer = new FileExplorer(textFieldEnterDirectoryPrompt.getText());
			}
		});
	}
	
	private void addComponentsToPanel()
	{
		contentPane.add(textFieldEnterDirectoryPrompt);
		contentPane.add(labelEnterDirectoryPromt);
		contentPane.add(labelPic);
		contentPane.add(btnStart);
	}
}
