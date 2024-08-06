package lottoTeam3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainFrame extends JFrame implements ActionListener {
	private List<LottoRecord> lottoRecordList;
	private LottoRecord curLottoRecord;
	private JButton buyBtn;
	private JButton resultBtn;
	private JButton btnPrev;
	private JButton btnCur;
	private JButton endBtn;
	private JButton btnMoneyCheck;
	private JButton btnStats;
	private JButton btnManyBuy;

	public MainFrame() {
		super("가상 로또 시뮬레이션");

		readLottoRecords();
		curLottoRecord = new LottoRecord(lottoRecordList.size() + 1);

		JPanel pnl = new JPanel(new BorderLayout());
		JPanel pnlPic = new JPanel();
		JPanel pnlBtn = new JPanel(null);

		pnlPic.setBackground(Color.WHITE);
		pnlBtn.setBackground(Color.WHITE);
		pnlBtn.setPreferredSize(new Dimension(440, 350));
		JLabel lblLotto = new JLabel(new ImageIcon(MainFrame.class.getResource("/resource/lotto.png")));
		pnlPic.add(lblLotto);

		buyBtn = createMyButton("구매(" + curLottoRecord.getPuchaseNum() + "장)", new Insets(0, 0, 0, 0), pnlBtn, 30,
				new Rectangle(20, 10, 200, 80));
		resultBtn = createMyButton("추첨(" + curLottoRecord.getLottoRound() + "회차)", new Insets(0, 0, 0, 0), pnlBtn, 30,
				new Rectangle(20, 90, 200, 80));
		btnCur = createMyButton("현재 구매 확인", new Insets(0, 0, 0, 0), pnlBtn, 34, new Rectangle(220, 10, 200, 80));
		btnPrev = createMyButton("이전 회차 확인", new Insets(0, 0, 0, 0), pnlBtn, 30, new Rectangle(220, 90, 200, 80));
		btnMoneyCheck = createMyButton("손익결산", new Insets(0, 0, 0, 0), pnlBtn, 34, new Rectangle(20, 170, 200, 80));
		btnStats = createMyButton("통계", new Insets(0, 0, 0, 0), pnlBtn, 30, new Rectangle(220, 170, 200, 80));
		btnManyBuy = createMyButton("대량구매", new Insets(0, 0, 0, 0), pnlBtn, 30, new Rectangle(20, 250, 200, 80));
		endBtn = createMyButton("종료", new Insets(0, 0, 0, 0), pnlBtn, 34, new Rectangle(220, 250, 200, 80));
		btnCur.setEnabled(false);
		if (lottoRecordList.size() == 0) {
			btnPrev.setEnabled(false);
			btnMoneyCheck.setEnabled(false);
		}

		pnl.add(pnlPic, "North");
		pnl.add(pnlBtn);
		add(pnl);
		pack();
		addWindowListener(new WindowAdapter() { // 윈도우 어댑터를 사용하여 필요한 메서드만 구현
			@Override
			public void windowClosing(WindowEvent e) { // x를 누를 때
				frameClose(); // 종료 확인 다이알로그를 처리하는 메서드
			}
		});
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		pack();
		// 임시로 가운데에서 창 나오도록, setSize와 Pack(); 이후에 작성해야 한다.
		setLocation((1920 - getWidth()) / 2, (1080 - getHeight()) / 2);
	}

	// 버튼 셋팅 메서드
	private JButton createMyButton(String text, Insets insets, JPanel pnl, int fontSize, Rectangle r) {
		JButton btn = new JButton(text);
		btn.setFocusable(false);
		btn.setMargin(insets);
		btn.setFont(FontHolder.getInstance().getDeriveFont(Font.PLAIN, 30));
		btn.setBackground(Color.WHITE);
		btn.setBounds(r);
		btn.addActionListener(this);
		pnl.add(btn);
		return btn;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(buyBtn)) { // 구매
			LottoData[] ld = PurchaseDialog.showDialog(lottoRecordList, curLottoRecord, this);
			if (ld != null) {
				curLottoRecord.addBuyLotto(ld);
				buyBtn.setText("구매(" + curLottoRecord.getPuchaseNum() + "장)");
				btnCur.setEnabled(true);
				resultBtn.setEnabled(true);
			}
		} else if (o.equals(resultBtn)) { // 추첨
			ResultDialog.showDialog(curLottoRecord, this);
			lottoRecordList.add(curLottoRecord);
			curLottoRecord = new LottoRecord(lottoRecordList.size() + 1);
			buyBtn.setText("구매(" + curLottoRecord.getPuchaseNum() + "장)");
			resultBtn.setText("추첨(" + curLottoRecord.getLottoRound() + "회차)");
			btnPrev.setEnabled(true);
			btnMoneyCheck.setEnabled(true);
			btnCur.setEnabled(false);
		} else if (o.equals(btnPrev)) {
			PrevLottoDialog.showDialog(lottoRecordList, this);
		}
		// 현재 구매 확인 : 선택한 번호가 적용된 창 출력
		else if (o.equals(btnCur)) {
			PurchasedLottoDialog.showDialog(curLottoRecord, this);
		} else if (o.equals(btnMoneyCheck)) {
			ProfitDialog.showDialog(lottoRecordList, this);
			// 수익금 조회
		} else if (o.equals(btnStats)) {
			StaticDialog.showDialog(lottoRecordList, this);
		} else if (o.equals(endBtn)) { // 종료
			frameClose();
		} else if (o.equals(btnManyBuy)) { // 대량구매
			// 구매하고자 하는 금액만큼 모두 자동으로 구입
			int lottoCount = BulkPurchaseDialog.showDialog(this);
			if (lottoCount > 0) {
				for (int i = 0; i < lottoCount; i++) {
					curLottoRecord.addBuyLotto(PurchaseDialog.createAuto());
				}

				buyBtn.setText("구매(" + curLottoRecord.getPuchaseNum() + "장)");
				btnCur.setEnabled(true);
				resultBtn.setEnabled(true);
			}
		}

	}

	// 종료확인 다이얼로그 표시
	private void frameClose() {
		int input = JOptionPane.showOptionDialog(this, "종료하시겠습니까?", "종료", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE,
				null, null, null); // 종료 확인 다이알로그 출력 및 값 대입
		if (input == JOptionPane.YES_OPTION) { // 종료 확인을 눌렀을 때
			dispose(); // 창 사라지게
		}
	}

	public void readLottoRecords() {
		File file = new File(".//lotto.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		lottoRecordList = LottoRecordIO.readLottoRecord(file);
	}

	public static void main(String[] args) {
		new MainFrame().setVisible(true);
	}

	public LottoRecord getCurLottoRecord() {
		return curLottoRecord;
	}
}