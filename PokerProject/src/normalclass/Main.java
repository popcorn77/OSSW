package normalclass;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import g_FirstPage.Join;
import g_FirstPage.FirstPage;
import g_GamePage.GameLoading;
import g_GamePage.PlayGame;
import g_TitlePage.TitlePage;

public class Main {
	/**
	 * 게임을 플레이하는 유저
	 */
	public static User user;
	/**
	 * PlayGame 제어 변수
	 */
	public static PlayGame game;
	/**
	 * TitlePage 제어 변수
	 */
	public static TitlePage title;
	/**
	 * FirstPage 제어 변수
	 */
	public static FirstPage first;
	/**
	 * 로딩창 제어 변수
	 */
	public static GameLoading loading;
	/**
	 * 게임에서 발생하는 모든 메시지를 저장하는 변수
	 */
	public static String gameMsg = "";
	/**
	 * 카드를 나눠줄 때 카드의 이동속도 변수
	 */
	public static int splitSpeed;
	/**
	 * 현재 접속한 계정의 인덱스 값을 가지는 변수
	 */
	public static int accountIndex;
	
	/** 
	 * 회원가입 클래스를 제어하기 위한 상수
	 */
	public final static Join join = new Join();
	/** 
	 * 딜러 클래스를 제어하기 위한 상수
	 */
	public final static Dealer dealer = new Dealer();
	/** 
	 * 게임시스템 클래스를 제어하기 위한 상수
	 */
	public final static GameSystem gameSystem = new GameSystem();
	/** 
	 * 게임시작후 로그인을 하면 유저는 player의 첫번째 인덱스에 배치되며,
	 * 게임을 새로 시작 할 때마다 player들이 저장되는 상수
	 */
	public final static List<User> player = new LinkedList<User>();
	/**
	 * 기본 분석기를 제어하기위한 상수
	 */
	public final static AnalyzerBasic analyzerB = new AnalyzerBasic();
	/**
	 * 자신의 패를 예측해주는 분석기 제어를 위한  상수
	 */
	public final static AnalyzerPredictMe analyzerPM = new AnalyzerPredictMe();
	/**
	 * 상대방의 패를 예측해주는 분석기 제어를 위한 상수
	 */
	public final static AnalyzerPredictEnemy analyzerPE = new AnalyzerPredictEnemy();
	
	/**
	 * 게임 화면 크기
	 */
	public static final int SCREEN_WIDTH = 1280;
	public static final int SCREEN_HEIGHT = 720;
	/**
	 * 유저 최대 충전한도
	 */
	public static final int MAX_CHARGE = 1000000;
	/**
	 * 현재 게임의 배팅 및 진행 상태
	 */
	public static final int FIRST = 0;
	public static final int DIE = 0;
	public static final int HALF = 1;
	public static final int BBING = 2;
	public static final int CHECK = 3;
	public static final int CALL = 4;
	
	public static void main(String[] args) {
		new SetupInfo().setup();
		first = new FirstPage();

	}
	
	public static void waitTime(int waitNum) {
		try {
			Thread.sleep(waitNum);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void msgBox(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
	}
}