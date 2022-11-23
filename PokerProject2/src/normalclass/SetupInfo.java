package normalclass;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import g_TitlePage.DeleteAccount;

/**
 * SetupInfo - save,setup : 게임 시작 및 종료시에 계정 정보를 셋업 및 저장
 * run : 매 게임 시작시 컴퓨터 성능정보 분석 후 카드 이동 스피드 설정
 * @author ASUS
 */
public class SetupInfo extends Thread {
	
	private ArrayList<Account> tmpList = new ArrayList<Account>();
	private boolean isLoop = true;
	private int[] checkArr = new int[1000];
	private int checkNum;
	
	//<Function>----------------------------------------------
	public void save() {
		try {
			System.out.println("<save> 저장된 계정 수 : "+Main.join.getMemberList().size());
			if(Main.player.size() > 0 && DeleteAccount.getChk() == 0)
				Main.join.getMemberList().get(Main.accountIndex).setMoney(Main.user.getMoney());
			
			FileOutputStream fos = new FileOutputStream("Account.txt");
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ObjectOutputStream out = new ObjectOutputStream(bos);

			tmpList.addAll(Main.join.getMemberList());
			out.writeObject(tmpList);
			out.close();
		} catch(Exception e) {
			e.getMessage();
		}
	}
	
	public void setup() {
		try {
			FileInputStream fis = new FileInputStream("Account.txt");
			BufferedInputStream bis = new BufferedInputStream(fis);
			ObjectInputStream in = new ObjectInputStream(bis);

			@SuppressWarnings("unchecked")
			ArrayList<Account> read = (ArrayList<Account>)in.readObject();
			Main.join.getMemberList().clear();
			Main.join.getMemberList().addAll(read);
			
			System.out.println("<setup> 저장된 계정 수 : "+Main.join.getMemberList().size());
			read.clear();
			in.close();
		} catch(Exception e) {
			e.getMessage();
		}
	}
	
	@Override
	public void run() {
		int speed = 0;
		
		new Thread (new Runnable() {
			@Override
			public void run() {
				while(checkNum != 999) {
					Main.waitTime(1);
					checkNum ++;
				}
				isLoop = false;
			}
		}).start();
		
		while(isLoop) {
			System.out.println();
			checkArr[checkNum] ++;
		}
		
		for(int tmp : checkArr)
			speed += tmp;
		
		speed = speed / 1000;
		Main.splitSpeed = speed / 36;
		Main.loading.setLoading(false);
	}
}