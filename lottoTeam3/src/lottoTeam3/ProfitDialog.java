package lottoTeam3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.ScrollPane;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ProfitDialog extends JDialog {
	List<Integer> proceedsList = new ArrayList<>();
	List<Integer> investmentList = new ArrayList<>();

	private ProfitDialog(List<LottoRecord> lottoRecordList, JFrame frame) {
		setTitle("로또 손익결산");

		JPanel pnlNorth = new JPanel(null);
		pnlNorth.setBackground(Color.WHITE);
		pnlNorth.setPreferredSize(new Dimension(390, 70));
		JLabel lblTitle = createMyLabel("로또 손익 결산(총 " + lottoRecordList.size() + "회)", new Rectangle(0, 0, 450, 40), 30,
				pnlNorth);
		lblTitle.setHorizontalAlignment(JLabel.CENTER);
		JLabel lblNumAttribute = createMyLabel("", new Rectangle(0, 40, 60, 30), 30, pnlNorth);
		JLabel lblInvestmentAttribute = createMyLabel("투자금", new Rectangle(60, 40, 130, 30), 30, pnlNorth);
		lblInvestmentAttribute.setHorizontalAlignment(JLabel.CENTER);
		JLabel lblProceedsAttribute = createMyLabel("수익금", new Rectangle(190, 40, 130, 30), 30, pnlNorth);
		lblProceedsAttribute.setHorizontalAlignment(JLabel.CENTER);
		JLabel lblProfitAttribute = createMyLabel("순이익", new Rectangle(320, 40, 130, 30), 30, pnlNorth);
		lblProfitAttribute.setHorizontalAlignment(JLabel.CENTER);

		JPanel pnlCenter = new JPanel(null);
		pnlCenter.setBackground(Color.WHITE);
		pnlCenter.setPreferredSize(new Dimension(390, 20 * lottoRecordList.size()));
		JScrollPane sp = new JScrollPane(pnlCenter);
		sp.getVerticalScrollBar().setUnitIncrement(10);

		long totalInvestment = 0;
		long totalProceeds = 0;
		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		String formattedInvestment;
		String formattedProceeds;
		String formattedProfit;
		for (int i = 0; i < lottoRecordList.size(); i++) {
			LottoRecord lottoRecord = lottoRecordList.get(i);
			long investment, proceeds;
			totalInvestment += investment = lottoRecord.getInvestment();
			totalProceeds += proceeds = lottoRecord.getProceeds();
			formattedInvestment = decimalFormat.format(investment);
			formattedProceeds = decimalFormat.format(proceeds);
			formattedProfit = decimalFormat.format(proceeds - investment);
			JLabel lblNum = createMyLabel(i + 1 + "회", new Rectangle(0, i * 20, 60, 20), 15, pnlCenter);
			JLabel lblInvestment = createMyLabel(formattedInvestment + "원", new Rectangle(60, i * 20, 130, 20), 15, pnlCenter);
			lblInvestment.setHorizontalAlignment(JLabel.RIGHT);
			JLabel lblProceeds = createMyLabel(formattedProceeds + "원", new Rectangle(190, i * 20, 130, 20), 15, pnlCenter);
			lblProceeds.setHorizontalAlignment(JLabel.RIGHT);
			JLabel lblProfit = createMyLabel(formattedProfit + "원", new Rectangle(320, i * 20, 130, 20), 15, pnlCenter);
			lblProfit.setHorizontalAlignment(JLabel.RIGHT);

		}

		JPanel pnlSouth = new JPanel(null);
		pnlSouth.setBackground(Color.WHITE);
		pnlSouth.setPreferredSize(new Dimension(390, 20));

		formattedInvestment = decimalFormat.format(totalInvestment);
		formattedProceeds = decimalFormat.format(totalProceeds);
		formattedProfit = decimalFormat.format(totalProceeds - totalInvestment);
		JLabel lblTotal = createMyLabel("총", new Rectangle(0, 0, 60, 20), 15, pnlSouth);
		JLabel lblTotalInvestment = createMyLabel(formattedInvestment + "원", new Rectangle(60, 0, 130, 20), 15, pnlSouth);
		lblTotalInvestment.setHorizontalAlignment(JLabel.RIGHT);
		JLabel lblTotalProceeds = createMyLabel(formattedProceeds + "원", new Rectangle(190, 0, 130, 20), 15, pnlSouth);
		lblTotalProceeds.setHorizontalAlignment(JLabel.RIGHT);
		JLabel lblTotalProfit = createMyLabel(formattedProfit + "원", new Rectangle(320, 0, 130, 20), 15, pnlSouth);
		lblTotalProfit.setHorizontalAlignment(JLabel.RIGHT);

		add(pnlNorth, "North");
		add(sp);
		add(pnlSouth, "South");

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(485, 483);
		setLocation(frame.getX() + frame.getWidth(), frame.getY());
//		setResizable(false);
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
		new ProfitDialog(lottoRecordList, frame).setVisible(true);
	}
}
