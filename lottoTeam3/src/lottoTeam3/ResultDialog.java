package lottoTeam3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ResultDialog extends JDialog {
	private Set<Integer> resultTreeSet;
	private int bonus;
	private String roundText;
	private int listIndex;

	private LottoRecord lottoRecord;
	private JFrame mainFrame;
	private JComboBox<String> comboBox;
	private JPanel showWinNumPnl;
	private JLabel winMoneyLabel;
	private JPanel resultPanel;
	private JLabel roundNow;
	private LottoData[] lottoDatas;
	private JLabel[] lblCodes;
	private JLabel[][] lblNums;
	private JLabel[][] lblCircles;
	private JLabel[] lblResults;

	private boolean onlyWin;
	private Integer[] resultArrays;

	public ResultDialog(LottoRecord lottoRecord, JFrame mainFrame) {
		this.lottoRecord = lottoRecord;
		this.mainFrame = mainFrame;

		// 로또 구매 기록이 없을 경우 당첨 번호만 랜덤으로 생성해서 저장
		if (lottoRecord.getLottoDatas(0) == null) {
			iniResultDialog();
//			iniRoundLblAndDropdown();
			roundNow = new JLabel();
			roundNow.setPreferredSize(new Dimension(150, 30));
			setColorCenterFont(roundNow, Color.BLACK, JLabel.CENTER, 20);
			add(roundNow);

			iniLottoResultNum();
			setSize(550, 150);
			setRoundLblAndDropdown();
		} else {
			// 필요한 component 초기화
			initComponents();

			// component의 내용을 설정
			setAndUpdate();
		}
	}

	private void initComponents() {

		// resultDiaglog 세팅
		iniResultDialog();

		// 회차 라벨, 회차 선택 가능한 드랍다운 버튼 세팅
		iniRoundLblAndDropdown();

		// 첫번째 회차 당첨 번호를 랜덤으로 돌려서 생성해서 보여준다.
		// 동시에 당첨 번호를 기록한다.
		iniLottoResultNum();

		// 당첨금액을 표시해주는 라벨 생성. 계산은 여기서 하지 않음
		iniWinMoneyLbl();

		// 당첨 결과 패널 생성
		iniResultPanel();
	}

	private void iniResultDialog() {
		// 창 제일 위에 이름 어떻게 하지
		setTitle("로또 결과창");

		RandomResult();
		resultArrays = resultTreeSet.toArray(new Integer[6]);
		lottoRecord.SetLottery(Arrays.asList(resultArrays), bonus);

		// 요소들 끼리 간격 설정 (중앙정렬, hgap, vgap)
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		getContentPane().setBackground(Color.WHITE);

		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setLocation(mainFrame.getX() + mainFrame.getWidth(), mainFrame.getY());
	}

	private void iniRoundLblAndDropdown() {
		// 랜덤으로 결정된 당첨 결과를 resultTreeSet에 적용

		roundNow = new JLabel();
		roundNow.setPreferredSize(new Dimension(150, 30));
		setColorCenterFont(roundNow, Color.BLACK, JLabel.CENTER, 20);
		add(roundNow);

		JButton btnPrev = new JButton("◀");
		add(btnPrev);
		comboBox = new JComboBox<>();

		for (int i = 0; i < lottoRecord.getPuchaseNum(); i++) {
			comboBox.addItem(String.valueOf(i + 1) + "번 로또 결과");
		}

		comboBox.setPreferredSize(new Dimension(110, 30));
		add(comboBox);
		JButton btnNext = new JButton("▶");
		add(btnNext);
		btnPrev.setEnabled(false);
		JButton btnWin = new JButton("당첨만");
		if (!lottoRecord.hasWin())
			btnWin.setEnabled(false);
		add(btnWin);
		if (comboBox.getItemCount() <= 1) {
			btnPrev.setVisible(false);
			btnNext.setVisible(false);
		}

		btnWin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onlyWin = !onlyWin;
				if (onlyWin) {
					btnWin.setText("모두다");
					comboBox.removeAllItems();
					for (int i = 0; i < lottoRecord.getPuchaseNum(); i++) {
						if (lottoRecord.isWin(i)) {
							comboBox.addItem(String.valueOf(i + 1) + "번 로또 결과");
						}
					}
					comboBox.setSelectedIndex(0);

				} else {
					btnWin.setText("당첨만");
					comboBox.removeAllItems();
					for (int i = 0; i < lottoRecord.getPuchaseNum(); i++) {
						comboBox.addItem(String.valueOf(i + 1) + "번 로또 결과");
					}
					comboBox.setSelectedIndex(0);
				}
			}
		});

		btnPrev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comboBox.setSelectedIndex(comboBox.getSelectedIndex() - 1);
			}
		});
		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comboBox.setSelectedIndex(comboBox.getSelectedIndex() + 1);
			}
		});
		// 드랍 다운 버튼 리스너 설정
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (comboBox.getSelectedItem() == null)
					return;
				listIndex = Integer.parseInt(((String) comboBox.getSelectedItem()).split("번")[0]) - 1;
//				listIndex = comboBox.getSelectedIndex();
				setAndUpdate();
				btnPrev.setEnabled(comboBox.getSelectedIndex() > 0);
				btnNext.setEnabled(comboBox.getSelectedIndex() < comboBox.getItemCount() - 1);
			}
		});
	}

	private void iniLottoResultNum() {
		showWinNumPnl = new JPanel(null);
		showWinNumPnl.setPreferredSize(new Dimension(490, 70));
		showWinNumPnl.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		showWinNumPnl.setBackground(Color.WHITE);
		add(showWinNumPnl);

		// 당첨 결과를 lotto.txt에 저장
		writeCurLottoRecord();

		for (int i = 0; i < resultArrays.length; i++) {
			JLabel lblResultNum = new JLabel("" + resultArrays[i]);
			setColorCenterFont(lblResultNum, Color.WHITE, JLabel.CENTER, 17);
			lblResultNum.setBounds(i * 60 + 10, 6 - 3, 60, 60);
			showWinNumPnl.add(lblResultNum);

			JLabel lblResultCircle = numToColor(resultArrays[i]);
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

	private void iniWinMoneyLbl() {
		winMoneyLabel = new JLabel();
		setColorCenterFont(winMoneyLabel, Color.BLACK, JLabel.CENTER, 17);
		winMoneyLabel.setPreferredSize(new Dimension(300, 30));
		add(winMoneyLabel);
	}

	private void iniResultPanel() {
		resultPanel = new JPanel(null);
		resultPanel.setPreferredSize(new Dimension(540, 300));
		resultPanel.setBackground(Color.WHITE);

		// A (반자동) 출력
		lblCodes = new JLabel[5];
		for (int i = 0; i < lblCodes.length; i++) {
			lblCodes[i] = new JLabel();
			lblCodes[i].setBounds(5, i * 60 - 3, 120, 60);
			setColorCenterFont(lblCodes[i], Color.BLACK, JLabel.CENTER, 20);
			resultPanel.add(lblCodes[i]);
		}
		// 번호 라벨,아이콘 라벨
		lblNums = new JLabel[5][6];
		lblCircles = new JLabel[5][6];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 6; j++) {
				lblNums[i][j] = new JLabel();
				lblNums[i][j].setBounds(j * 60 + 110, i * 60 - 3, 60, 60);
				setColorCenterFont(lblNums[i][j], Color.WHITE, JLabel.CENTER, 17);
				resultPanel.add(lblNums[i][j]);

				lblCircles[i][j] = new JLabel();
				lblCircles[i][j].setBounds(j * 60 + 110, i * 60, 60, 60);
				resultPanel.add(lblCircles[i][j]);
			}
		}
		// 1등, 2등, 3등, 4등, 5등, 꽝(디폴트)
		lblResults = new JLabel[5];
		for (int i = 0; i < lblResults.length; i++) {
			lblResults[i] = new JLabel();
			lblResults[i].setBounds(460, i * 60 - 3, 60, 60);
			setColorCenterFont(lblResults[i], Color.BLACK, JLabel.CENTER, 20);
			resultPanel.add(lblResults[i]);
		}
		// 결과 패널을 메인프레임에 추가
		add(resultPanel);
	}

	private void setAndUpdate() {
		setResultDialog();

		setRoundLblAndDropdown();

		// iniLottoResultNum는 필요없다. 당첨 결과는 그대로

		setWinMoneyLbl();

		setResultPanel();
	}

	private void setResultDialog() {
		lottoDatas = lottoRecord.getLottoDatas(listIndex);
		int count = 0;
		for (LottoData lottoData : lottoDatas) {
			if (lottoData == null) {
				break;
			}
			count++;
		}
		setSize(550, 180 + count * 60);
	}

	private void setRoundLblAndDropdown() {
		roundText = String.valueOf(lottoRecord.getLottoRound()) + "회 당첨 결과";
		roundNow.setText(roundText);
	}

	private void setWinMoneyLbl() {
		// 회차 별로 당첨 금액을 계산하기 위해서
		// 당첨금 계산
		int resultMoney = 0;
		resultMoney += lottoRecord.getPrize(listIndex);

		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		String formattedNumber = decimalFormat.format(resultMoney);
		winMoneyLabel.setText("당첨금액: " + formattedNumber + "원");
	}

	private void setResultPanel() {
		// A (반자동) 출력
		for (int i = 0; i < lblCodes.length; i++) {
			if (lottoDatas[i] != null) {
				char c = (char) ('A' + i);
				lblCodes[i].setText(String.valueOf(c) + " (" + lottoDatas[i].getMode().getKorean() + ")");
			}
		}
		// 번호 라벨,아이콘 라벨
		for (int i = 0; i < 5; i++) {
			if (lottoDatas[i] == null)
				break;
			for (int j = 0; j < 6; j++) {
				// 로또 데이터에서 배열 추출
				int[] lottoArr = lottoDatas[i].getNums();
				lblNums[i][j].setText("" + lottoArr[j]);

				if (resultTreeSet.contains(lottoArr[j])) {
					setToColor(lblCircles[i][j], lottoArr[j]);
				} else {
					setToBlack(lblCircles[i][j]);
				}
				if (lottoRecord.getLottoRank(listIndex, i) == 2) {
					if (lottoArr[j] == bonus) {
						setToColor(lblCircles[i][j], lottoArr[j]);
					}
				}
			}
		}
		for (int i = 0; i < lblResults.length; i++) {
			if (lottoDatas[i] != null) {
				int rank = lottoRecord.getLottoRank(listIndex, i);
				lblResults[i].setText(rank == 6 ? "꽝" : rank + "등");
			}
		}
	}

	private void setColorCenterFont(JLabel lbl, Color color, int alignment, int fontSize) {
		lbl.setForeground(color);
		lbl.setHorizontalAlignment(alignment);
		lbl.setVerticalAlignment(alignment);
		lbl.setFont(FontHolder.getInstance().getDeriveFont(Font.PLAIN, fontSize));
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

	private void setToColor(JLabel lbl, int n) {
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
	}

	private JLabel numToBlack() {
		JLabel lbl = new JLabel();
		lbl.setIcon(LottoCircle.BLACK.getImageIcon());
		return lbl;
	}

	private void setToBlack(JLabel lbl) {
		lbl.setIcon(LottoCircle.BLACK.getImageIcon());
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

	public void writeCurLottoRecord() {
		File file = new File(".//lotto.txt");
		boolean success = LottoRecordIO.writeLottoRecords(file, lottoRecord);
		if (!success)
			System.out.println("기록 실패");
	}

	// lottoDatas를 받아서 결과 다이얼로그를 보여주는 메소드
	public static void showDialog(LottoRecord lottoRecord, JFrame mainFrame) {
		ResultDialog resultDialog = new ResultDialog(lottoRecord, mainFrame);
		resultDialog.setVisible(true);
	}

	// 테스트용 당첨 결과를 임의로 정해주는 메서드
	private void ManualResult() {
		resultTreeSet = new TreeSet<>(Arrays.asList(1, 11, 21, 31, 41, 45));
		bonus = 7;
	}
}
