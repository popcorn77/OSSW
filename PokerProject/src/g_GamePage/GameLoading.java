package g_GamePage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import normalclass.Main;
import normalclass.SetupInfo;
import java.awt.Color;

public class GameLoading extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private JLabel loadingLabel = new JLabel("", SwingConstants.CENTER);
	private String loadingMsg = "Now Loading";
	private boolean loading;
	
	public GameLoading() {
		loading = true;
		
		setSize(200, 60);
		setUndecorated(true);
		setLocationRelativeTo(null);
		loadingLabel.setForeground(Color.WHITE);
		loadingLabel.setBackground(Color.BLACK);
		loadingLabel.setOpaque(true);
		
		
		new SetupInfo().start();
		
		new Thread (new Runnable(){
			
			public void run(){
				while(loading){
					loadingMsg += ".";
					if(loadingMsg.equals("Now Loading.....")){
						loadingMsg = "Now Loading";
					}
					loadingLabel.setText(loadingMsg);
					Main.waitTime(200);
				}
				Main.loading.dispose();
				Main.game = new PlayGame();
			}
		}).start();
				
		add(loadingLabel);
		setVisible(true);
	}
	
	public void setLoading(boolean loading){
		this.loading = loading;
	}
}
