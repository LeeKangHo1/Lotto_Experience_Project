package lottoTeam3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ResultDialog extends JDialog {
	private Set<Integer> resultTreeSet;
	private String[] resultString = new String[5];
	private int resultMoney;
	private int bonus;

	private FontHolder fontHolder = new FontHolder();
	private LottoData[] lottoDatas;
	private JFrame mainFrame;

	public ResultDialog(LottoData[] lottoData, JFrame mainFrame) {
		this.lottoDatas = lottoData;
		this.mainFrame = mainFrame;

		// 결과 dialog 설정
		resultDialogSetting();

		// 당첨 회차 라벨
		showRound();

		// 이번 회차 랜덤으로 선정된 당첨 번호 출력
		showLottoResultNum();

		// 당첨 금액
		showWinMoney();

		// 당첨 결과 패널
		showResultPaenl();
	}

	private void resultDialogSetting() {
		// 창 제일 위에 이름 어떻게 하지
		setTitle("로또 결과창");

		// 당첨되지 않을 경우 "꽝"이 출력되도록
		for (int i = 0; i < resultString.length; i++) {
			resultString[i] = "꽝";
		}

		// 요소들 끼리 간격 설정 (중앙정렬, hgap, vgap)
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
		getContentPane().setBackground(Color.WHITE);

		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(mainFrame);

		// 구매한 로또 개수만큼 창 크기 조절
		int count = 0;
		for (LottoData lottoData : lottoDatas) {
			if (lottoData == null) {
				break;
			}
			count++;
		}
		setSize(550, 190 + count * 60);
	}

	private void showRound() {
		JLabel roundNow = new JLabel("777회");
		roundNow.setPreferredSize(new Dimension(100, 30));
		setColorCenterFont(roundNow, Color.BLACK, JLabel.CENTER, 20);
		add(roundNow);
	}

	private void showLottoResultNum() {
		// 당첨 번호 아이콘 6개 + 1 패널 생성
		JPanel showWinNumPnl = new JPanel(null);
		showWinNumPnl.setPreferredSize(new Dimension(490, 70));
		showWinNumPnl.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		showWinNumPnl.setBackground(Color.WHITE);
		add(showWinNumPnl);

		// 랜덤으로 결정된 당첨 결과를 resultTreeSet에 적용
		RandomResult();
		// 테스트용
//		ManualResult();
		Integer[] resultArray = resultTreeSet.toArray(new Integer[6]);

		for (int i = 0; i < resultArray.length; i++) {
			JLabel lblResultNum = new JLabel("" + resultArray[i]);
			setColorCenterFont(lblResultNum, Color.WHITE, JLabel.CENTER, 17);
			lblResultNum.setBounds(i * 60 + 10, 6 - 3, 60, 60);
			showWinNumPnl.add(lblResultNum);

			JLabel lblResultCircle = numToColor(resultArray[i]);
			lblResultCircle.setBounds(i * 60 + 10, 6, 60, 60);
			showWinNumPnl.add(lblResultCircle);
		}
		JLabel lblPlus = new JLabel(" + ");
		setColorCenterFont(lblPlus, Color.BLACK, JLabel.CENTER, 25);
		lblPlus.setBounds(6 * 60 + 20, 20, 30, 30);
		showWinNumPnl.add(lblPlus);

		JLabel lblResultNum = new JLabel("" + bonus);
		setColorCenterFont(lblResultNum, Color.WHITE, JLabel.CENTER, 17);
		lblResultNum.setBounds(7 * 60, 6 - 3, 60, 60);
		showWinNumPnl.add(lblResultNum);

		JLabel lblResultCircle = numToColor(bonus);
		lblResultCircle.setBounds(7 * 60, 6, 60, 60);
		showWinNumPnl.add(lblResultCircle);
	}

	private void showWinMoney() {
		JLabel winMoneyLabel = new JLabel();

		// 당첨금
		calculateMoney();

		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		String formattedNumber = decimalFormat.format(resultMoney);

		winMoneyLabel.setText("당첨금액: " + formattedNumber + "원");

		setColorCenterFont(winMoneyLabel, Color.BLACK, JLabel.CENTER, 17);
		winMoneyLabel.setPreferredSize(new Dimension(300, 30));
		add(winMoneyLabel);
	}

	private void showResultPaenl() {
		// 결과창을 보여줄 패널 생성
		JPanel resultPanel = new JPanel(null);
		resultPanel.setPreferredSize(new Dimension(540, 300));
		resultPanel.setBackground(Color.WHITE);

		// A (반자동) 출력
		JLabel[] lblCode = new JLabel[5];
		for (int i = 0; i < lblCode.length; i++) {
			if (lottoDatas[i] != null) {
				char c = (char) ('A' + i);
				lblCode[i] = new JLabel(String.valueOf(c) + " (" + lottoDatas[i].getMode().getKorean() + ")");
				lblCode[i].setBounds(5, i * 60 - 3, 120, 60);
				setColorCenterFont(lblCode[i], Color.BLACK, JLabel.CENTER, 20);
				resultPanel.add(lblCode[i]);
			}
		}
		// 번호 라벨,아이콘 라벨
		for (int i = 0; i < 5; i++) {
			if (lottoDatas[i] == null)
				break;
			for (int j = 0; j < 6; j++) {
				// 로또 데이터에서 배열 추출
				int[] lottoArr = lottoDatas[i].getNums();

				JLabel[][] lblNums = new JLabel[5][6];
				lblNums[i][j] = new JLabel("" + lottoArr[j]);
				lblNums[i][j].setBounds(j * 60 + 110, i * 60 - 3, 60, 60);
				setColorCenterFont(lblNums[i][j], Color.WHITE, JLabel.CENTER, 17);
				resultPanel.add(lblNums[i][j]);

				// 일치한 번호만 색깔 부여
				JLabel[][] lblCircles = new JLabel[5][6];
				if (resultTreeSet.contains(lottoArr[j])) {
					lblCircles[i][j] = numToColor(lottoArr[j]);
				} else {
					lblCircles[i][j] = numToBlack(lottoArr[j]);
				}
				lblCircles[i][j].setBounds(j * 60 + 110, i * 60, 60, 60);
				resultPanel.add(lblCircles[i][j]);
			}
		}

		// 1등, 2등, 3등, 4등, 5등, 꽝(디폴트) cnffur
		JLabel[] lblResult = new JLabel[5];
		for (int i = 0; i < lblCode.length; i++) {
			if (lottoDatas[i] != null) {
				lblResult[i] = new JLabel(resultString[i]);
				lblResult[i].setBounds(460, i * 60 - 3, 60, 60);
				setColorCenterFont(lblResult[i], Color.BLACK, JLabel.CENTER, 20);
				resultPanel.add(lblResult[i]);
			}
		}
		// 결과 패널을 메인프레임에 추가
		add(resultPanel);
	}

	private void setColorCenterFont(JLabel lbl, Color color, int alignment, int fontSize) {
		lbl.setForeground(color);
		lbl.setHorizontalAlignment(alignment);
		lbl.setVerticalAlignment(alignment);
		lbl.setFont(fontHolder.getDeriveFont(Font.PLAIN, fontSize));
	}

	private int equalsNum(int[] lottoArr) {
		int count = 0;
		for (int i = 0; i < lottoArr.length; i++) {
			if (resultTreeSet.contains(lottoArr[i])) {
				count++;
			}
		}
		return count;
	}

	private void calculateMoney() {
		for (int i = 0; i < 5; i++) {
			if (lottoDatas[i] != null) {
				int[] lottoArr = lottoDatas[i].getNums();
				int count = equalsNum(lottoArr);

				// 1등, 6개 번호 일치
				if (count == 6) {
					resultMoney += 100_000_000;
					resultString[i] = "1등";
				} else if (count == 5) {
					// 3등, 5개 번호 일치
					resultMoney += 3_000_000;
					resultString[i] = "3등";

					// 2등, 5개 번호 일치 + 보너스 볼과 번호 일치
					for (int j = 0; j < lottoArr.length; j++) {
						if (lottoArr[j] == bonus) {
							resultMoney += 17_000_000;
							resultString[i] = "2등";
						}
					}
					// 4등, 4개 번호 일치
				} else if (count == 4) {
					resultMoney += 50_000;
					resultString[i] = "4등";
					// 5등, 3개 번호 일치
				} else if (count == 3) {
					resultMoney += 5_000;
					resultString[i] = "5등";
				}
			}
		}
	}

	// yellow blue red gray green 순서
	public JLabel numToColor(int n) {
		JLabel lbl = new JLabel();
		if (n <= 10) {
			lbl.setIcon(LottoCircle.YELLOW.getImageIcon());
		} else if (10 < n && n <= 20) {
			lbl.setIcon(LottoCircle.BLUE.getImageIcon());
		} else if (20 < n && n <= 30) {
			lbl.setIcon(LottoCircle.RED.getImageIcon());
		} else if (30 < n && n <= 40) {
			lbl.setIcon(LottoCircle.GRAY.getImageIcon());
		} else if (40 < n && n <= 50) {
			lbl.setIcon(LottoCircle.GREEN.getImageIcon());
		}
		return lbl;
	}

	public JLabel numToBlack(int n) {
		JLabel lbl = new JLabel();
		lbl.setIcon(LottoCircle.BLACK.getImageIcon());
		return lbl;
	}

	// lottoDatas를 받아서 결과 다이얼로그를 보여주는 메소드
	public static void showDialog(LottoData[] lottoData, JFrame mainFrame) {
		ResultDialog resultDialog = new ResultDialog(lottoData, mainFrame);
		resultDialog.setVisible(true);
	}

	public void RandomResult() {
		Random random = new Random();
		resultTreeSet = new TreeSet<>();
		while (resultTreeSet.size() < 6) {
			resultTreeSet.add(random.nextInt(45) + 1);
		}
		do {
			bonus = random.nextInt(45) + 1;
		} while (resultTreeSet.contains(bonus));
	}

	// 테스트용 당첨 결과를 정해주는 메서드
	private void ManualResult() {
		resultTreeSet = new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6));
		bonus = 7;
	}
}
