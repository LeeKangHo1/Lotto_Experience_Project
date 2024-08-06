package lottoTeam3;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PrevLottoDialog extends JDialog implements ActionListener {

	private List<LottoRecord> lottoRecordList;
	private JComboBox<String> comboBoxHwe;
	private JComboBox<String> comboBoxBeon;
	private JLabel[] lblCodes;
	private JLabel[][] lblNums;
	private JLabel[][] lblCircles;
	private JLabel[] lblResults;
	private LottoData[] lottoDatas;
	private List<Integer> lotteryNums;
	private int lotteryBonus;
	private JLabel[] lblResultNums;
	private JLabel[] lblResultNumsCircles;
	private JLabel lblResultBonus;
	private JLabel lblResultBonusCircle;
	private JLabel winMoneyLabel;
	private int indexHwe;
	private JButton[] btnCopy;
	private JLabel lblBeon;
	private JButton btnPrevHwe;
	private JButton btnNextHwe;
	private JButton btnNextBeon;
	private JButton btnPrevBeon;
	private int indexBeon;
	private boolean onlyWin;
	private JButton btnWin;

	private PrevLottoDialog(Window window, List<LottoRecord> lottoRecordList) {
		this.lottoRecordList = lottoRecordList;
		indexHwe = lottoRecordList.size() - 1;
		setLottoDatas();

		JPanel pnlNorth = new JPanel();
		initNorth(pnlNorth);

		JPanel pnlCenter = new JPanel(null);
		initCenter(pnlCenter);

		add(pnlNorth, "North");
		add(pnlCenter);

		dialogSetting(window);
	}

	private void dialogSetting(Window window) {
		setTitle("이전 회차 확인");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocation(window.getX() + window.getWidth(), window.getY());
		setResizable(false);
		setModal(true);
		resize();
	}

	private void initCenter(JPanel pnl) {
		pnl.setPreferredSize(new Dimension(500, 500));
		pnl.setBackground(Color.WHITE);

		initResults(pnl);

		settingResults();

		initButtons(pnl);
	}

	private void initButtons(JPanel pnl) {
		btnCopy = new JButton[5];
		for (int i = 0; i < btnCopy.length; i++) {
			btnCopy[i] = new JButton("복사");
			btnCopy[i].addActionListener(this);
			btnCopy[i].setBounds(520, i * 60 + 13, 50, 30);
			btnCopy[i].setBackground(Color.WHITE);
			btnCopy[i].setFocusable(false);
			btnCopy[i].setMargin(new Insets(0, 0, 0, 0));
			btnCopy[i].setFont(FontHolder.getInstance().getDeriveFont(Font.PLAIN, 20));
			pnl.add(btnCopy[i]);
		}
	}

	private void initNorth(JPanel pnl) {
//		pnl.setBorder(new EmptyBorder(10, 0, 0, 0));
		pnl.setBackground(Color.white);
		pnl.setPreferredSize(new Dimension(0, 195));

		initCombos(pnl);

		initShowWinPnl(pnl);

		initWinMoneyLabel(pnl);

		settingCombos();

		settingWinNumPnl();

		settingWinMoneyLabel();
	}

	private void settingCombos() {
		btnNextHwe.setEnabled(indexHwe < lottoRecordList.size() - 1);
		btnPrevHwe.setEnabled(indexHwe > 0);
		btnNextBeon.setEnabled(lottoRecordList.get(indexHwe).getPuchaseNum() > 1);
		btnPrevBeon.setEnabled(false);
		btnWin.setEnabled(lottoRecordList.get(indexHwe).hasWin());

		beonEnable(comboBoxBeon.getItemCount() != 0);
	}

	private void initCombos(JPanel pnl) {
		JPanel pnlCombo = new JPanel(null);
		pnlCombo.setBackground(Color.WHITE);
//		pnlCombo.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pnlCombo.setPreferredSize(new Dimension(350, 70));
		JLabel lblHwe = new JLabel("이전 회차 선택");
//		lblHwe.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblHwe.setFont(FontHolder.getInstance().getDeriveFont(Font.PLAIN, 20));
		lblHwe.setBounds(25, 0, 120, 35);
		lblHwe.setHorizontalAlignment(JLabel.CENTER);

		pnlCombo.add(lblHwe);

		btnPrevHwe = createMoveButton("◀", pnlCombo);
		btnPrevHwe.setBounds(145, 0, 30, 35);

		comboBoxHwe = new JComboBox<>();
		for (int i = 0; i < lottoRecordList.size(); i++) {
			comboBoxHwe.addItem((i + 1) + "회");
		}
		comboBoxHwe.setSelectedIndex(indexHwe);
		comboBoxHwe.setBackground(Color.WHITE);
		comboBoxHwe.setFont(FontHolder.getInstance().getDeriveFont(Font.PLAIN, 20));
		comboBoxHwe.addActionListener(this);
		comboBoxHwe.setFocusable(false);
		comboBoxHwe.setBounds(175, 0, 90, 35);
		pnlCombo.add(comboBoxHwe);
		btnNextHwe = createMoveButton("▶", pnlCombo);
		btnNextHwe.setBounds(265, 0, 30, 35);

		lblBeon = new JLabel("구매 번호 선택");
		lblBeon.setFont(FontHolder.getInstance().getDeriveFont(Font.PLAIN, 20));
		lblBeon.setBounds(0, 35, 120, 35);
		lblBeon.setHorizontalAlignment(JLabel.CENTER);
		pnlCombo.add(lblBeon);

		btnPrevBeon = createMoveButton("◀", pnlCombo);
		btnPrevBeon.setBounds(120, 35, 30, 35);
		comboBoxBeon = new JComboBox<>();
		for (int i = 0; i < lottoRecordList.get(indexHwe).getPuchaseNum(); i++) {
			comboBoxBeon.addItem((i + 1) + "번");
		}
		comboBoxBeon.setBackground(Color.WHITE);
		comboBoxBeon.setFont(FontHolder.getInstance().getDeriveFont(Font.PLAIN, 20));
		comboBoxBeon.addActionListener(this);
		comboBoxBeon.setFocusable(false);
		comboBoxBeon.setBounds(150, 35, 90, 35);
		pnlCombo.add(comboBoxBeon);
		btnNextBeon = createMoveButton("▶", pnlCombo);
		btnNextBeon.setBounds(240, 35, 30, 35);
		btnWin = new JButton("당첨만");
		btnWin.setMargin(new Insets(0, 0, 0, 0));
		btnWin.setBounds(270, 35, 50, 35);
		btnWin.setBackground(Color.WHITE);
		btnWin.setFocusable(false);
		btnWin.setFont(FontHolder.getInstance().getDeriveFont(Font.PLAIN, 16));
		btnWin.addActionListener(this);
		pnlCombo.add(btnWin);
		pnl.add(pnlCombo);

	}

	private void beonEnable(boolean b) {
		btnPrevBeon.setVisible(b);
		comboBoxBeon.setVisible(b);
		lblBeon.setVisible(b);
		btnNextBeon.setVisible(b);
		btnWin.setVisible(b);
	}

	private JButton createMoveButton(String text, JPanel pnl) {
		JButton btn = new JButton(text);
		btn.setBackground(Color.WHITE);
		btn.setMargin(new Insets(0, 3, 0, 3));
		btn.setFocusable(false);
		btn.addActionListener(this);
		pnl.add(btn);
		return btn;
	}

	private void resize() {
		int count = 0;
		if (lottoDatas != null)
			for (LottoData lottoData : lottoDatas) {
				if (lottoData == null) {
					break;
				}
				count++;
			}
		setSize(600, 225 + count * 60);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		for (int i = 0; i < btnCopy.length; i++) {
			if (o.equals(btnCopy[i])) {
				LottoData.setCopy(lottoDatas[i]);
				return;
			}
		}
		if (o.equals(comboBoxHwe)) {
			if (onlyWin) {
				onlyWin = false;
				btnWin.setText("당첨만");
			}
			indexHwe = comboBoxHwe.getSelectedIndex();
			setLottoDatas();

			comboBoxBeon.removeAllItems();

			for (int i = 0; i < lottoRecordList.get(indexHwe).getPuchaseNum(); i++) {
				comboBoxBeon.addItem((i + 1) + "번");
			}
			settingCombos();
			settingWinNumPnl();
			settingWinMoneyLabel();
			settingResults();
			resize();
		} else if (o.equals(comboBoxBeon)) {
			if (comboBoxBeon.getSelectedItem() == null)
				return;
			indexBeon = Integer.parseInt(((String) comboBoxBeon.getSelectedItem()).split("번")[0]) - 1;
			if (indexBeon < 0)
				return;
			lottoDatas = lottoRecordList.get(indexHwe).getLottoDatas(indexBeon);
			btnPrevBeon.setEnabled(comboBoxBeon.getSelectedIndex() > 0);
			btnNextBeon.setEnabled(comboBoxBeon.getSelectedIndex() < comboBoxBeon.getItemCount() - 1);

			settingWinMoneyLabel();
			settingResults();
			resize();
		} else if (o.equals(btnPrevHwe)) {
			comboBoxHwe.setSelectedIndex(comboBoxHwe.getSelectedIndex() - 1);
		} else if (o.equals(btnNextHwe)) {
			comboBoxHwe.setSelectedIndex(comboBoxHwe.getSelectedIndex() + 1);
		} else if (o.equals(btnPrevBeon)) {
			comboBoxBeon.setSelectedIndex(comboBoxBeon.getSelectedIndex() - 1);
		} else if (o.equals(btnNextBeon)) {
			comboBoxBeon.setSelectedIndex(comboBoxBeon.getSelectedIndex() + 1);
		} else if (o.equals(btnWin)) {
			onlyWin = !onlyWin;
			if (onlyWin) {
				btnWin.setText("모두다");
				comboBoxBeon.removeAllItems();
				for (int i = 0; i < lottoRecordList.get(indexHwe).getPuchaseNum(); i++) {
					if (lottoRecordList.get(indexHwe).isWin(i)) {
						comboBoxBeon.addItem(String.valueOf(i + 1) + "번");
					}
				}
				comboBoxBeon.setSelectedIndex(0);

			} else {
				btnWin.setText("당첨만");
				comboBoxBeon.removeAllItems();
				for (int i = 0; i < lottoRecordList.get(indexHwe).getPuchaseNum(); i++) {
					comboBoxBeon.addItem(String.valueOf(i + 1) + "번");
				}
				comboBoxBeon.setSelectedIndex(0);
			}
		}
	}

	private void setLottoDatas() {
		lotteryNums = lottoRecordList.get(indexHwe).getLotteryNums();
		lotteryBonus = lottoRecordList.get(indexHwe).getLotteryBonus();
		lottoDatas = lottoRecordList.get(indexHwe).getLottoDatas(0);
	}

	private void settingWinMoneyLabel() {
		// 당첨금 계산
		int resultMoney = 0;
		if (comboBoxBeon.getItemCount() != 0)
			resultMoney += lottoRecordList.get(indexHwe).getPrize(indexBeon);

		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		String formattedNumber = decimalFormat.format(resultMoney);
		winMoneyLabel.setText("당첨금액: " + formattedNumber + "원");
	}

	private void initWinMoneyLabel(JPanel pnlCenter) {
		winMoneyLabel = new JLabel();
		setColorCenterFont(winMoneyLabel, Color.BLACK, JLabel.CENTER, 17);
		winMoneyLabel.setBounds(50, 80, 300, 30);
		pnlCenter.add(winMoneyLabel);
	}

	private void settingResults() {
		if (lottoDatas == null)
			return;
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

				if (lotteryNums.contains(lottoArr[j])) {
					setToColor(lblCircles[i][j], lottoArr[j]);
				} else {
					setToBlack(lblCircles[i][j]);
				}
				if (lottoRecordList.get(indexHwe).getLottoRank(indexBeon, i) == 2) {
					if (lottoArr[j] == lotteryBonus) {
						setToColor(lblCircles[i][j], lottoArr[j]);
					}
				}
			}
		}
		for (int i = 0; i < lblResults.length; i++) {
			if (lottoDatas[i] != null) {
				int rank = lottoRecordList.get(indexHwe).getLottoRank(indexBeon, i);
				lblResults[i].setText(rank == 6 ? "꽝" : rank + "등");
			}
		}
	}

	private void initResults(JPanel pnlCenter) {
		lblCodes = new JLabel[5];
		for (int i = 0; i < lblCodes.length; i++) {
			lblCodes[i] = new JLabel();
			lblCodes[i].setBounds(5, i * 60 + -3, 120, 60);
			setColorCenterFont(lblCodes[i], Color.BLACK, JLabel.CENTER, 20);
			pnlCenter.add(lblCodes[i]);
		}
		// 번호 라벨,아이콘 라벨
		lblNums = new JLabel[5][6];
		lblCircles = new JLabel[5][6];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 6; j++) {
				lblNums[i][j] = new JLabel();
				lblNums[i][j].setBounds(j * 60 + 110, i * 60 + -3, 60, 60);
				setColorCenterFont(lblNums[i][j], Color.WHITE, JLabel.CENTER, 17);
				pnlCenter.add(lblNums[i][j]);

				lblCircles[i][j] = new JLabel();
				lblCircles[i][j].setBounds(j * 60 + 110, i * 60, 60, 60);
				pnlCenter.add(lblCircles[i][j]);
			}
		}

		// 1등, 2등, 3등, 4등, 5등, 꽝(디폴트)
		lblResults = new JLabel[5];
		for (int i = 0; i < lblResults.length; i++) {
			lblResults[i] = new JLabel();
			lblResults[i].setBounds(460, i * 60 + -3, 60, 60);
			setColorCenterFont(lblResults[i], Color.BLACK, JLabel.CENTER, 20);
			pnlCenter.add(lblResults[i]);
		}
	}

	private void initShowWinPnl(JPanel pnl) {
		JPanel showWinNumPnl = new JPanel(null);
		showWinNumPnl.setPreferredSize(new Dimension(490, 70));
		showWinNumPnl.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		showWinNumPnl.setBackground(Color.WHITE);
		pnl.add(showWinNumPnl);

		lblResultNums = new JLabel[6];
		lblResultNumsCircles = new JLabel[6];
		for (int i = 0; i < 6; i++) {
			lblResultNums[i] = new JLabel();
			setColorCenterFont(lblResultNums[i], Color.WHITE, JLabel.CENTER, 17);
			lblResultNums[i].setBounds(i * 60 + 10, 6 - 3, 60, 60);
			showWinNumPnl.add(lblResultNums[i]);

			lblResultNumsCircles[i] = new JLabel();
			lblResultNumsCircles[i].setBounds(i * 60 + 10, 6, 60, 60);
			showWinNumPnl.add(lblResultNumsCircles[i]);
		}
		JLabel lblPlus = new JLabel(" + ");
		setColorCenterFont(lblPlus, Color.BLACK, JLabel.CENTER, 25);
		lblPlus.setBounds(6 * 60 + 20, 20, 30, 30);
		showWinNumPnl.add(lblPlus);

		lblResultBonus = new JLabel();
		setColorCenterFont(lblResultBonus, Color.WHITE, JLabel.CENTER, 17);
		lblResultBonus.setBounds(7 * 60, 6 - 3, 60, 60);
		showWinNumPnl.add(lblResultBonus);

		lblResultBonusCircle = new JLabel();
		lblResultBonusCircle.setBounds(7 * 60, 6, 60, 60);
		showWinNumPnl.add(lblResultBonusCircle);
	}

	private void settingWinNumPnl() {
		Integer[] resultArray = lotteryNums.toArray(new Integer[6]);

		for (int i = 0; i < 6; i++) {
			lblResultNums[i].setText("" + resultArray[i]);
			setToColor(lblResultNumsCircles[i], resultArray[i]);
		}
		lblResultBonus.setText("" + lotteryBonus);
		setToColor(lblResultBonusCircle, lotteryBonus);
	}

	private void setColorCenterFont(JLabel lbl, Color color, int alignment, int fontSize) {
		lbl.setForeground(color);
		lbl.setHorizontalAlignment(alignment);
		lbl.setVerticalAlignment(alignment);
		lbl.setFont(FontHolder.getInstance().getDeriveFont(Font.PLAIN, fontSize));
	}

	private int equalsNum(int[] lottoArr) {
		int count = 0;
		for (int i = 0; i < lottoArr.length; i++) {
			if (lotteryNums.contains(lottoArr[i])) {
				count++;
			}
		}
		return count;
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

	private void setToBlack(JLabel lbl) {
		lbl.setIcon(LottoCircle.BLACK.getImageIcon());
	}

	public static void showDialog(List<LottoRecord> lottoRecordList, Window window) {
		PrevLottoDialog prevLottoDialog = new PrevLottoDialog(window, lottoRecordList);
		prevLottoDialog.setVisible(true);
	}
}
