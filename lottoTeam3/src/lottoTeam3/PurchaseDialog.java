package lottoTeam3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PurchaseDialog extends JDialog implements ActionListener {
	private List<LottoRecord> lottoRecordList;
	private LottoRecord curLottoRecord;
	private JLabel[][] lblNums = new JLabel[5][6]; // 로또 숫자 라벨
	private JLabel[][] lblCircles = new JLabel[5][6]; // 로또 원 아이콘 라벨
	private JButton[] btnAmend = new JButton[5]; // 수정 버튼
	private JButton[] btnDelete = new JButton[5]; // 삭제 버튼
	private JButton[] btnCopy = new JButton[5];
	private JButton[] btnPaste = new JButton[5];
	private JLabel lblPrice; // 가격 라벨
	private int buyCount; // 구매 갯수
	private JButton btnConfirm; // 결과 버튼
	private JButton btnReset; // 초기화 버튼
	private JButton btnCancel; // 종료 버튼
	private LottoData[] lottoDatas = new LottoData[5]; // 로또 정보
	private JButton btnAuto;
	private JLabel[] lblModes;
	private JButton btnPrev;
	private JButton btnCur;
	private boolean buy = false;

	private PurchaseDialog(List<LottoRecord> lottoRecordList, LottoRecord curLottoRecord, JFrame frame) {
		this.lottoRecordList = lottoRecordList;
		this.curLottoRecord = curLottoRecord;
		JPanel pnlNorth = new JPanel(); // 플로우 레이아웃으로
		initNorth(pnlNorth); // 상단 패널 전체 생성
		JPanel pnlCenter = new JPanel(null); // 앱솔루트 레이아웃으로
		initCenter(pnlCenter); // 중앙 패널 전체 생성
		JPanel pnlSouth = new JPanel(null); // 앱솔루트 레이아웃으로
		initSouth(pnlSouth); // 하단 패널 전체 생성

		add(pnlCenter); // 중앙 패널 추가
		add(pnlNorth, "North"); // 상단 패널 추가
		add(pnlSouth, "South"); // 하단 패널 추가

		dialogSetting(frame);
	}

	private void dialogSetting(JFrame frame) {
		setTitle("로또 구매");
		pack(); // 화면 크기를 패널 크기에 맞춤
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
		setLocationRelativeTo(frame);

		// x를 눌렀을 때 종료 확인 다이알로그가 생성되게 윈도우리스너 추가
		addWindowListener(new WindowAdapter() { // 윈도우 어댑터를 사용하여 필요한 메서드만 구현
			@Override
			public void windowClosing(WindowEvent e) { // x를 누를 때
				dialogClose(); // 종료 확인 다이알로그를 처리하는 메서드
			}
		});
	}

	private void initSouth(JPanel pnl) {
		pnl.setPreferredSize(new Dimension(0, 70)); // 하단 패널 세로크기 설정
		pnl.setBackground(Color.WHITE); // 배경 흰색으로 설정

		lblPrice = new JLabel("총 가격: " + (buyCount * 1000) + "원"); // 가격 패널 생성
		lblPrice.setBounds(35, 25, 150, 30); // 가격 패널 크기 설정
//		lblPrice.setBorder(BorderFactory.createLineBorder(Color.black)); // 라벨 크기 확인을 위한 테두리 테스트
		lblPrice.setFont(FontHolder.getInstance().getDeriveFont(Font.PLAIN, 20)); // 가격 라벨 폰트설정
		pnl.add(lblPrice); // 가격 라벨 추가

		btnPrev = createMyButton("이전 회차 확인", new Rectangle(190, 25, 125, 30), pnl);
		btnCur = createMyButton("현재 구매 확인", new Rectangle(325, 25, 125, 30), pnl);
		if (!curLottoRecord.hasBought())
			btnCur.setEnabled(false);
		btnAuto = createMyButton("자동", new Rectangle(470, 25, 50, 30), pnl); // 메서드를 사용하여 자동 버튼 생성
		btnReset = createMyButton("초기화", new Rectangle(530, 25, 70, 30), pnl); // 메서드를 사용하여 초기화 버튼 생성
		btnConfirm = createMyButton("구매", new Rectangle(620, 25, 50, 30), pnl); // 메서드를 사용하여 결과 버튼 생성
		btnCancel = createMyButton("취소", new Rectangle(680, 25, 50, 30), pnl); // 메서드를 사용하여 종료 버튼 생성
		btnResDisable(); // 초기화 결과 버튼 비활성화
	}

	private void initCenter(JPanel pnl) {
		pnl.setPreferredSize(new Dimension(740, 300)); // 중앙 패널 크기 설정
		pnl.setBackground(Color.WHITE); // 배경 흰색으로 설정

		lblModes = new JLabel[5];
		for (int i = 0; i < lblModes.length; i++) { // 5번 반복
			char c = (char) ('A' + i); // A문자를 i씩 증가시켜서 A~E까지 char에 대입
			lblModes[i] = new JLabel(String.valueOf(c)); // 문자 c로 라벨 생성
			lblModes[i].setBounds(10, i * 60 - 3, 90, 60); // 라벨 바운드 설정
			lblModes[i].setFont(FontHolder.getInstance().getDeriveFont(Font.PLAIN, 20)); // 폰트 설정
			lblModes[i].setHorizontalAlignment(JLabel.CENTER);
			pnl.add(lblModes[i]); // 라벨 추가
		}

		// 로또 숫자 라벨 배열 전체 초기화 및 설정
		for (int i = 0; i < lblNums.length; i++) {
			for (int j = 0; j < lblNums[i].length; j++) {
				lblNums[i][j] = new JLabel("");
				lblNums[i][j].setBounds(j * 60 + 99, i * 60 - 3, 60, 60); // 라벨 바운드 설정
//				lblNums[i][j].setBorder(BorderFactory.createLineBorder(Color.RED)); // 테두리 테스트
				lblNums[i][j].setForeground(Color.WHITE); // 폰트 색상 흰색으로 설정
				lblNums[i][j].setHorizontalAlignment(JLabel.CENTER); // 라벨 안의 글자 가운데 정렬
				lblNums[i][j].setFont(FontHolder.getInstance().getDeriveFont(Font.PLAIN, 20)); // 폰트 설정
				pnl.add(lblNums[i][j]); // 라벨 추가
			}
		}

		// 로또 원 아이콘 라벨 배열 전체 초기화 및 설정
		for (int i = 0; i < lblCircles.length; i++) {
			for (int j = 0; j < lblCircles[i].length; j++) {
				lblCircles[i][j] = new JLabel(LottoCircle.BLACK.getImageIcon()); // 라벨 검은 원으로 생성
				lblCircles[i][j].setBounds(j * 60 + 100, i * 60, 60, 60); // 라벨 바운드 설정
				pnl.add(lblCircles[i][j]); // 라벨 추가
			}
		}

		for (int i = 0; i < btnAmend.length; i++) {
			btnAmend[i] = createMyButton("추가", new Rectangle(470, i * 60 + 13, 50, 30), pnl); // 메서드를 사용하여 수정 버튼 생성 및 설정
			if (i != 0)
				btnAmend[i].setEnabled(false); // 첫번째 추가 버튼을 제외하고 모두 비활성화
		}

		for (int i = 0; i < btnDelete.length; i++) {
			btnDelete[i] = createMyButton("삭제", new Rectangle(530, i * 60 + 13, 50, 30), pnl); // 메서드를 사용하여 삭제 버튼 생성 및
																								// 설정
			btnDelete[i].setEnabled(false); // 모든 삭제 버튼 비활성화
		}

		for (int i = 0; i < btnCopy.length; i++) {
			btnCopy[i] = createMyButton("복사", new Rectangle(590, i * 60 + 13, 50, 30), pnl); // 메서드를 사용하여 삭제 버튼 생성 및
																								// 설정
			btnCopy[i].setEnabled(false); // 모든 삭제 버튼 비활성화
		}

		for (int i = 0; i < btnPaste.length; i++) {
			btnPaste[i] = createMyButton("붙여넣기", new Rectangle(650, i * 60 + 13, 80, 30), pnl); // 메서드를 사용하여 삭제 버튼 생성 및
			if (i != 0 || LottoData.getCopy() == null) // 설정
				btnPaste[i].setEnabled(false); // 모든 삭제 버튼 비활성화
		}
	}

	// 버튼을 생성하고 설정하여 반환하는 메서드
	private JButton createMyButton(String text, Rectangle r, JPanel pnl) {
		JButton btn = new JButton(text); // 텍스트로 버튼 생성
		btn.setBackground(Color.WHITE); // 버튼 배경 흰색으로 설정
		btn.setBounds(r); // 버튼 바운드 설정
		btn.setMargin(new Insets(0, 0, 0, 0)); // 버튼 여백
		btn.setFocusable(false); // 버튼 포커스 안되게 설정 (클릭할 때 네모 뜸)
		btn.setFont(FontHolder.getInstance().getDeriveFont(Font.PLAIN, 20)); // 버튼 폰트 설정
		btn.addActionListener(this); // 버튼 액션 리스너 추가
		pnl.add(btn); // 패널에 버튼 추가
		return btn; // 버튼 반환
	}

	private void initNorth(JPanel pnl) {
		pnl.setPreferredSize(new Dimension(0, 90)); // 상단 패널 세로 크기 90으로 설정
		pnl.setBackground(Color.WHITE); // 패널 배경 흰색으로 설정

		JLabel lblLotto = new JLabel(new ImageIcon(PurchaseDialog.class.getResource("/resource/lotto.png"))); // 로또 아이콘 라벨 생성
		pnl.add(lblLotto); // 라벨 추가
	}

	@Override
	public void actionPerformed(ActionEvent e) { // 액션 리스너 구현 메서드
		Object o = e.getSource(); // 액션 이벤트에서 클릭된 객체 가져오기

		for (int i = 0; i < btnAmend.length; i++) { // 수정 버튼
			if (o.equals(btnAmend[i])) {
				LottoData input = NumberChooseDialog.showDialog(lottoDatas[i], this);
				if (input == null)
					return;
				if (lottoDatas[i] == null) { // 로또 추가라면
					buyLottoSetting(i);
				}
				lottoDatas[i] = input; // 받은 로또 정보 배열에 넣어주기
				changeLabels(i);
				return;
			}
		}

		for (int i = 0; i < btnDelete.length; i++) { // 삭제 버튼
			if (o.equals(btnDelete[i])) { // 클릭된 버튼이 i번째 삭제 버튼일 때
				buyCount--; // 구매 갯수 감소
				setPriceLabel(); // 가격 라벨 변경
				for (int j = i; j < buyCount; j++) {
					lottoDatas[j] = lottoDatas[j + 1]; // i+1번째 로또 정보를 i번째 로또 정보로 이동
					changeLabels(j);
				}
				deleteLottoLine(buyCount); // 로또 한줄 지우기

				if (buyCount == 0) {
					btnResDisable(); // 결과 초기화 버튼 비활성화
				}
				return;
			}
		}

		for (int i = 0; i < btnCopy.length; i++) { // 복사 버튼
			if (o.equals(btnCopy[i])) {
				if (LottoData.getCopy() == null) {
					for (int j = 0; j <= buyCount && j < btnPaste.length; j++) {
						btnPaste[j].setEnabled(true);
					}
				}
				LottoData.setCopy(lottoDatas[i]);
				return;
			}
		}

		for (int i = 0; i < btnPaste.length; i++) { // 붙여넣기 버튼
			if (o.equals(btnPaste[i])) {
				if (lottoDatas[i] == null) {
					buyLottoSetting(i);
				}
				lottoDatas[i] = LottoData.getCopy();
				changeLabels(i);
				return;
			}
		}
		if (o.equals(btnPrev)) {
			boolean prev = LottoData.getCopy() == null;
			PrevLottoDialog.showDialog(lottoRecordList, this);
			if (prev && LottoData.getCopy() != null) {
				for (int j = 0; j <= buyCount && j < btnPaste.length; j++) {
					btnPaste[j].setEnabled(true);
				}
			}
		} else if (o.equals(btnCur)) {
			boolean prev = LottoData.getCopy() == null;
			PurchasedLottoDialog.showDialog(curLottoRecord, this);
			if (prev && LottoData.getCopy() != null) {
				for (int j = 0; j <= buyCount && j < btnPaste.length; j++) {
					btnPaste[j].setEnabled(true);
				}
			}
		} else if (o.equals(btnAuto)) { // 자동 버튼
			btnResEnable(); // 결과 리셋 버튼 활성화
			while (buyCount < 5) {
				lottoDatas[buyCount] = createRandomLottoData();
				changeLabels(buyCount);
				btnAmend[buyCount].setText("수정"); // 수정 버튼 텍스트 변경
				btnDelete[buyCount].setEnabled(true); // 삭제 버튼 활성화
				btnCopy[buyCount].setEnabled(true);
				if (++buyCount < btnAmend.length) {
					btnAmend[buyCount].setEnabled(true); // 다음 수정 버튼 활성화
					if (LottoData.getCopy() != null)
						btnPaste[buyCount].setEnabled(true);
				}
			}
			setPriceLabel(); // 가격 라벨 변경
		} else if (o.equals(btnConfirm)) { // 결과 버튼
//			ResultDialog.showDialog(lottoDatas, PurchaseDialog.this); // 결과 다이얼로그 출력
			buy = true;
			dispose();
		} else if (o.equals(btnReset)) { // 초기화 버튼
			reset();
		} else if (o.equals(btnCancel)) { // 종료 버튼
			dialogClose();
		}
	}

	private void buyLottoSetting(int i) {
		if (buyCount == 0) { // 로또 처음 살 때
			btnResEnable(); // 결과 리셋 버튼 활성화
		}
		buyCount++; // 구매 갯수 증가
		setPriceLabel(); // 가격 라벨 변경
		btnAmend[i].setText("수정"); // 수정 버튼 텍스트 변경
		btnDelete[i].setEnabled(true); // 삭제 버튼 활성화
		btnCopy[i].setEnabled(true);
		if (i + 1 < 5) {
			btnAmend[i + 1].setEnabled(true); // 다음 수정 버튼 활성화
			if (LottoData.getCopy() != null)
				btnPaste[i + 1].setEnabled(true);
		}
	}

	private void changeLabels(int line) {
		char c = (char) ('A' + line);
		if (lottoDatas[line] == null) {
			for (int j = 0; j < 6; j++) {
				lblNums[line][j].setText(""); // 숫자 라벨 비우기
				lblCircles[line][j].setIcon(LottoCircle.BLACK.getImageIcon()); // 원 라벨 모두 검정으로 설정
			}
			lblModes[line].setText(String.valueOf(c));
		} else {
			int[] nums = lottoDatas[line].getNums(); // 로또 정보에서 숫자 배열 받아오기
			for (int j = 0; j < nums.length; j++) {
				lblNums[line][j].setText(String.valueOf(nums[j])); // 숫자 라벨 텍스트 입력한 숫자로 설정
				if (nums[j] <= 10)
					lblCircles[line][j].setIcon(LottoCircle.YELLOW.getImageIcon()); // 10보다 작거나 같을 때 노랑
				else if (nums[j] <= 20)
					lblCircles[line][j].setIcon(LottoCircle.BLUE.getImageIcon()); // 20보다 작거나 같을 때 파랑
				else if (nums[j] <= 30)
					lblCircles[line][j].setIcon(LottoCircle.RED.getImageIcon()); // 30보다 작거나 같을 때 빨강
				else if (nums[j] <= 40)
					lblCircles[line][j].setIcon(LottoCircle.GRAY.getImageIcon()); // 40보다 작거나 같을 때 회색
				else
					lblCircles[line][j].setIcon(LottoCircle.GREEN.getImageIcon()); // 나머지 초록
			}
			lblModes[line].setText(c + " (" + lottoDatas[line].getMode().getKorean() + ")");
		}
	}

	private void btnResEnable() {
		if (buyCount == 0) {
			btnReset.setEnabled(true); // 리셋 버튼 활성화
			btnConfirm.setEnabled(true); // 결과 버튼 활성화
		}
	}

	private static LottoData createRandomLottoData() {
		Random random = new Random();
		Set<Integer> set = new TreeSet<>();
		while (set.size() < 6) {
			set.add(random.nextInt(45) + 1);
		}
		return new LottoData(set.stream().mapToInt(Integer::intValue).toArray(), Mode.AUTO);
	}

	public static LottoData[] createAuto() {
		LottoData[] lottoDatas = new LottoData[5];
		for (int j = 0; j < lottoDatas.length; j++) {
			lottoDatas[j] = createRandomLottoData();
		}
		return lottoDatas;
	}

	private void reset() {
		for (int i = 0; i < buyCount; i++) { // 구매한 수만큼
			deleteLottoLine(i); // 로또 한줄 지우기
		}
		buyCount = 0; // 구매 갯수 초기화
		setPriceLabel(); // 가격 라벨 변경
		btnResDisable(); // 초기화 결과 버튼 비활성화
	}

	private void deleteLottoLine(int line) {
		lottoDatas[line] = null; // 로또 정보 없애기
		btnAmend[line].setText("추가"); // 로또 버튼 수정으로 변경
		btnDelete[line].setEnabled(false); // 삭제 버튼 비활성화
		btnCopy[line].setEnabled(false);
		changeLabels(line);
		if (line + 1 < btnAmend.length) {
			btnAmend[line + 1].setEnabled(false); // 다음 수정 버튼 비활성화
			if (LottoData.getCopy() != null)
				btnPaste[line + 1].setEnabled(false);
		}
	}

	private void setPriceLabel() {
		lblPrice.setText("총 가격: " + (buyCount * 1000) + "원"); // 가격 라벨 텍스트 설정
	}

	private void btnResDisable() {
		btnReset.setEnabled(false); // 초기화 버튼 비활성화
		btnConfirm.setEnabled(false); // 결과 버튼 비활성화
	}

	private void dialogClose() {
		int input = JOptionPane.showOptionDialog(PurchaseDialog.this, "구매를 취소하시겠습니까?", "취소", JOptionPane.YES_NO_OPTION,
				JOptionPane.ERROR_MESSAGE, null, null, null); // 종료 확인 다이알로그 출력 및 값 대입
		if (input == JOptionPane.YES_OPTION) { // 종료 확인을 눌렀을 때
			dispose(); // 창 사라지게
		}
	}

	public static LottoData[] showDialog(List<LottoRecord> lottoRecordList, LottoRecord curLottoRecord, JFrame frame) {
		PurchaseDialog pd = new PurchaseDialog(lottoRecordList, curLottoRecord, frame);
		pd.setVisible(true);
		if (pd.buy)
			return pd.lottoDatas;
		else
			return null;
	}
}
