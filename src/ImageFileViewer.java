import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalComboBoxUI.MetalComboBoxLayoutManager;

public class ImageFileViewer extends JFrame
{

	private JPanel contentPane;
	private ImageIcon img;
	private JLabel label;
	private String imagePath;
	
	public ImageFileViewer(String imagePath)
	{
		this.imagePath = imagePath;
		
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
		label = SwingComponentFactory.createJLabel();
		img = GetImage.openImage(imagePath);
	}
	
	private void setComponentLocations()
	{
		setBounds(100, 100, 399, 355);
	}
	
	private void setComponentSizes()
	{
		
	}
	
	private void setComponentSettings()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(contentPane);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		label.setIcon(img);
		label.setVisible(true);
	}
	
	private void setActionListeners()
	{
		
	}
	
	private void addComponentsToPanel()
	{
		contentPane.add(label, BorderLayout.CENTER);
	}
}

class GetImage {
	
	public GetImage()
	{
		
	}
	
	public static ImageIcon openImage(String imagePath)
	{
		try
		{
			File imageFile = new File(imagePath);
			BufferedImage image = ImageIO.read(imageFile);
			ImageIcon icon = new ImageIcon(image);
			return icon;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new ImageIcon();
		}
	}
}