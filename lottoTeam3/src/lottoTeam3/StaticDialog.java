package lottoTeam3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class StaticDialog extends JDialog {
	private int[] numCounts = new int[45];
	private int[] bonusCounts = new int[45];

	private StaticDialog(List<LottoRecord> lottoRecordList, JFrame frame) {
		for (int i = 0; i < lottoRecordList.size(); i++) {
			LottoRecord lr = lottoRecordList.get(i);
			for (Integer integer : lr.getLotteryNums()) {
				numCounts[integer - 1]++;
			}
			bonusCounts[lr.getLotteryBonus() - 1]++;
		}
		int size = lottoRecordList.size();
		JPanel pnlNorth = new JPanel(null);
		pnlNorth.setBackground(Color.WHITE);
		pnlNorth.setPreferredSize(new Dimension(0, 80));

		initNorth(size, pnlNorth);

		JPanel pnlCenter = new JPanel(null);
		pnlCenter.setPreferredSize(new Dimension(620, 900));
		pnlCenter.setBackground(Color.WHITE);

		initCenter(size, pnlCenter);

		JScrollPane sp = new JScrollPane(pnlCenter);
		sp.getVerticalScrollBar().setUnitIncrement(10);
		add(pnlNorth, "North");
		add(sp);

		setTitle("로또 통계");
		setSize(650, 483);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocation(frame.getX() + frame.getWidth(), frame.getY());
		setResizable(false);

	}

	private void initCenter(int size, JPanel pnlC) {
		JLabel[][] lblNums = new JLabel[3][45];
		JLabel[][] lblCounts = new JLabel[3][45];
		JLabel[][] lblPercents = new JLabel[3][45];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 45; j++) {
				lblNums[i][j] = createMyLabel((j + 1) + "번", new Rectangle(i * 210, j * 20, 70, 20), 20, pnlC);
				int count = i == 0 ? numCounts[j] : i == 1 ? bonusCounts[j] : numCounts[j] + bonusCounts[j];
				lblCounts[i][j] = createMyLabel(count + "회", new Rectangle(i * 210 + 70, j * 20, 70, 20), 20, pnlC);
				int total = size == 0 ? 1 : i == 0 ? size * 6 : i == 1 ? size : size * 7;
				lblPercents[i][j] = createMyLabel(String.format("%.1f%%", count * 100.0 / total),
						new Rectangle(i * 210 + 140, j * 20, 70, 20), 20, pnlC);
			}
		}
	}

	private void initNorth(int size, JPanel pnl) {
		JLabel lblTitle = createMyLabel("로또 통계 (총 " + size + "회)", new Rectangle(0, 0, 630, 35), 30, pnl);
		lblTitle.setHorizontalAlignment(JLabel.CENTER);
		JLabel lblNumStatc = createMyLabel("당첨 번호 통계", new Rectangle(0, 35, 210, 25), 22, pnl);
		lblNumStatc.setHorizontalAlignment(JLabel.CENTER);
		JLabel lblBonusStatc = createMyLabel("보너스 번호 통계", new Rectangle(210, 35, 210, 25), 22, pnl);
		lblBonusStatc.setHorizontalAlignment(JLabel.CENTER);
		JLabel lblTotalStatc = createMyLabel("전체 번호 통계", new Rectangle(420, 35, 210, 25), 22, pnl);
		lblTotalStatc.setHorizontalAlignment(JLabel.CENTER);

		JLabel[] lblNumTitle = new JLabel[3];
		JLabel[] lblCountTitle = new JLabel[3];
		JLabel[] lblPercentTitle = new JLabel[3];
		for (int i = 0; i < lblNumTitle.length; i++) {
			lblNumTitle[i] = createMyLabel("번호", new Rectangle(i * 210, 60, 70, 20), 18, pnl);
			lblCountTitle[i] = createMyLabel("횟수", new Rectangle(i * 210 + 70, 60, 70, 20), 18, pnl);
			lblPercentTitle[i] = createMyLabel("통계", new Rectangle(i * 210 + 140, 60, 70, 20), 18, pnl);
		}
	}

	private JLabel createMyLabel(String text, Rectangle r, int fontSize, JPanel pnl) {
		JLabel lbl = new JLabel(text);
		lbl.setBounds(r);
		lbl.setFont(FontHolder.getInstance().getDeriveFont(Font.PLAIN, fontSize));
		lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pnl.add(lbl);
		return lbl;
	}

	public static void showDialog(List<LottoRecord> lottoRecordList, JFrame frame) {
		new StaticDialog(lottoRecordList, frame).setVisible(true);
	}

}
