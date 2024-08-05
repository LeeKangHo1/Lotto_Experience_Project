package lottoTeam3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NumberChooseDialog extends JDialog implements ActionListener {
	// 지역변수>필드. 어디서든 사용가능(Ctrl + 1)
	// 필드값
	private int count = 0;
	private JLabel countText;
	private JButton[] btns = new JButton[45];
	private JButton btnCheck;
	private JButton btnReset;
	private JButton btnAuto;
	private boolean buy = false;
	private Mode mode = Mode.AUTO; // 기본은 자동모드

	public NumberChooseDialog(LottoData prevLottoData, JDialog dialog) {
		setTitle("번호 선택");

		JPanel pnl = new JPanel(new BorderLayout());

		// 패널A : 1~45 숫자배열 / 왼쪽정렬
		JPanel pnlA = new JPanel(new FlowLayout(FlowLayout.LEFT));
		settingPanelA(pnlA);
		pnl.add(pnlA);

		inputLottoData(prevLottoData);

		// 패널B : 기능 버튼 모음
		JPanel pnlB = new JPanel();
		settingPanelB(pnlB);
		pnl.add(pnlB, "South");
		add(pnl);

		settingDailog(dialog);
	}

	// JFrame 위치 - frame은 메인창 위치 의미
	private void settingDailog(JDialog dialog) {
		int x = dialog.getX();
		int y = dialog.getY();
		int width = dialog.getWidth();
		setLocation(x + width, y);
		setSize(421, 280);
		setResizable(false);

		setModal(true); // 대화상자 닫기 전까지 상호작용 불가
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); // 다이얼로그 창 닫기
	}

	// 로또 번호 수정
	private void inputLottoData(LottoData inputlotto) {
		if (inputlotto != null) {
			// 선택한 번호가 nums에 들어감
			int nums[] = inputlotto.getNums();
			for (int i = 0; i < nums.length; i++) {
				// 인덱스는 0부터 시작
				btns[nums[i] - 1].setBackground(Color.GRAY);
			}
			// 번호 수정시 모드 변경. 아니면 그대로 유지
			mode = inputlotto.getMode();
			count = 6;
		}
	}

	// 패널A 셋팅
	private void settingPanelA(JPanel pnlA) {
		pnlA.setBackground(Color.WHITE);
		for (int i = 0; i < btns.length; i++) { // 숫자 배열
			btns[i] = createMyButton(String.valueOf(i + 1), new Insets(0, 0, 0, 0), pnlA, 20);
			btns[i].setPreferredSize(new Dimension(35, 35));
			pnlA.add(btns[i]);
		}
	}

	// 패널B 셋팅
	private void settingPanelB(JPanel pnlB) {
		countText = new JLabel("개수: " + count);
//		countText.setBorder(new LineBorder(new Color(122, 138, 153), 1));
		countText.setFont(FontHolder.getInstance().getDeriveFont(Font.PLAIN, 17));
		countText.setPreferredSize(new Dimension(54, 26));
//		countText.setHorizontalAlignment(JLabel.CENTER);

		pnlB.add(countText);
		pnlB.setBackground(Color.WHITE);
		btnCheck = createMyButton("확인", new Insets(0, 2, 0, 2), pnlB, 17);
		btnReset = createMyButton("초기화", new Insets(0, 2, 0, 2), pnlB, 17);
		btnAuto = createMyButton("자동", new Insets(0, 2, 0, 2), pnlB, 17);
		if (count == 6) {
			btnAuto.setEnabled(false);
			numberEnabled(false);
		} else {
			btnCheck.setEnabled(false);
			btnReset.setEnabled(false);
		}
	}

	// 버튼 설정 셋팅
	private JButton createMyButton(String text, Insets insets, JPanel pnlB, int fontSize) {
		JButton btnMenu = new JButton(text);
		btnMenu.setFocusable(false); // 가운데 선택시 상자 제거
		btnMenu.setMargin(insets); // 여백
		btnMenu.setFont(FontHolder.getInstance().getDeriveFont(Font.PLAIN, fontSize)); // 폰트, 크기
		btnMenu.addActionListener(this); // 버튼 액션
		btnMenu.setBackground(Color.WHITE); // 색상
		pnlB.add(btnMenu); // 패널B에 추가
		return btnMenu; // 버튼 반환
	}

	// 선택한 번호 개수 출력
	private void setCount() {
		countText.setText("개수: " + count);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 클릭된 버튼을 e이벤트 정보로 가져와라
		// getSource 버튼 객체
		Object o = e.getSource();

		// 번호 선택O(화이트)&count 증가. 선택X(그레이)
		// 오브젝트는 equals를 포함
		for (int i = 0; i < btns.length; i++) {
			if (o.equals(btns[i])) { // 클릭한 버튼일때
				if (btns[i].getBackground().equals(Color.WHITE)) {
					// 자동
					btns[i].setBackground(Color.GRAY);
					count++;
					// 반자동 : 하나라도 선택된 상태
					if (count == 1) {
						mode = Mode.SEMI;
						btnReset.setEnabled(true);
						// 수동 : 모두 선택된 상태
					} else if (count == 6) {
						mode = Mode.MANUAL;
						settingBtnWhenSellectAll(true);
					}

					// 번호 선택 취소
				} else {
					btns[i].setBackground(Color.WHITE);
					count--;
					// 선택을 직접 안했으니 자동
					if (count == 0) {
						mode = Mode.AUTO;
						btnReset.setEnabled(false);
						// 5개가 선택되었고 1개가 남아있어 반자동
					} else if (count == 5) {
						mode = Mode.SEMI;
						settingBtnWhenSellectAll(false);
					}
				}
				setCount();
				return;
				// void return : 해당 함수 빠져 나오기(break와 비슷함)
			}
		}

		// 리셋
		if (o.equals(btnReset)) {
			for (int i = 0; i < btns.length; i++) {
				if (btns[i].getBackground().equals(Color.GRAY)) {
					btns[i].setBackground(Color.WHITE);
				}
			}
			if (count == 6) {
				settingBtnWhenSellectAll(false);
			}
			btnReset.setEnabled(false);
			count = 0;
			mode = Mode.AUTO;
			setCount();
			return;
		}

		// 자동 모드 - 랜덤 번호 6개 선택됨
		if (o.equals(btnAuto)) {
			for (Random r = new Random(); count < 6;) {
				int n = r.nextInt(45);
				if (btns[n].getBackground().equals(Color.WHITE)) {
					btns[n].setBackground(Color.GRAY);
					count++;
				}
			}
			// buy는 구매여부를 boolean반환
			buy = true; // 구매
			dispose();
			return;
		}

		// 확인 - 선택된 숫자가 6개 이면 빠져나옴
		if (o.equals(btnCheck)) {
			// 선택창 닫기 위해 만든 객체
			buy = true;
			dispose();
			return;
		}
	}

	private void settingBtnWhenSellectAll(boolean b) {
		numberEnabled(!b);
		btnCheck.setEnabled(b);
		btnAuto.setEnabled(!b);
	}

	// 번호 6개 선택시 다른 번호 비활성화
	private void numberEnabled(boolean b) {
		for (int i = 0; i < btns.length; i++) {
			if (btns[i].getBackground().equals(Color.WHITE)) {
				btns[i].setEnabled(b);
			}
		}
	}

	// '확인'클릭시 번호 6개를 LottoData로 전달
	public static LottoData showDialog(LottoData inputlotto, JDialog dialog) {
		NumberChooseDialog nc = new NumberChooseDialog(inputlotto, dialog);
		nc.setVisible(true);
		return nc.getLottoData();
	}

	// 창을 끄면 선택된 숫자가 전달됨
	private LottoData getLottoData() {
		int[] nums = new int[6];
		if (buy) {
			for (int i = 0, j = 0; i < btns.length && j < nums.length; i++) {
				if (btns[i].getBackground().equals(Color.GRAY)) {
					nums[j] = i + 1;
					j++;
				}
			}
			return new LottoData(nums, mode);
		} else {
			return null;
		}
	}
}
