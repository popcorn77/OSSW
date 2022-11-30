package g_TitlePage;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import g_GamePage.GameLoading;
import normalclass.Main;

public class LevelSelect extends JFrame {
	private static final long serialVersionUID = 1L;
	int computerNum = 1;
	
	public LevelSelect (int playerNum){
		
		setTitle("LEVEL SELECT");
		setSize(210, 118);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(3, 1));
		
		JLabel label = new JLabel("'Computer"+computerNum+"'의 AI수준 선택", SwingConstants.CENTER);
		JButton button = new JButton("Select");
		String[] level = {"Level One", "Level Two", "Level Three"} ;
		JComboBox<String> combo = new JComboBox<String>(level);
		label.setOpaque(true);
		label.setBackground(Color.BLACK);
		label.setForeground(Color.GREEN);
		
		label.setBounds(0,50,50,40);
		combo.setBounds(0,75,100,40);
		button.setBounds(100,150,50,40);

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e){
				button.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e){
				button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
			public void mouseClicked(MouseEvent e){
				Main.title.setAILevel(combo.getSelectedIndex() + 1);

				if(computerNum == playerNum){
					Main.title.setAIPlayer(playerNum);
					Main.title.dispose();
					dispose();
					computerNum = 1;

					Main.loading = new GameLoading();
				}
				label.setText("'Computer"+ (computerNum + 1) +"'의 AI수준 선택");
				computerNum ++;
			}
		});

		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
					dispose();
					Main.title.getAILevel().clear();
					Main.title = new TitlePage();
			}
		});

		add(label);
		add(combo);
		add(button);
		
		setVisible(true);
	}
}