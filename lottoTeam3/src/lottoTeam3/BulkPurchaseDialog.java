package lottoTeam3;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BulkPurchaseDialog extends JDialog {
	boolean buy;
	JTextField tf;

	private BulkPurchaseDialog(JFrame frame) {
		JPanel pnl = new JPanel();
		pnl.setBorder(BorderFactory.createEmptyBorder(13, 0, 0, 0));
		pnl.setPreferredSize(new Dimension(300, 80));
		add(pnl);
		JLabel lblInfor = new JLabel("구매할 로또의 장수를 입력하세요 (최대 9999)");
		pnl.add(lblInfor);
		tf = new JTextField(10);
		pnl.add(tf);

		JButton btnConfirm = new JButton("확인");
		btnConfirm.setFocusable(false);
		pnl.add(btnConfirm);

		JButton btnCancel = new JButton("취소");
		btnCancel.setFocusable(false);
		pnl.add(btnCancel);

		tf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (c == '\n') {
					btnConfirm.getActionListeners()[0].actionPerformed(new ActionEvent(e, 0, null));
				} else if (c < '0' || '9' < c) {
					e.consume();
				} else {
					int input = Integer.parseInt(tf.getText() + c);
					if (input == 0 || input > 9999)
						e.consume();
				}
			}
		});
		btnConfirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tf.getText().isEmpty())
					JOptionPane.showMessageDialog(BulkPurchaseDialog.this, "구매할 로또 장수를 입력 해주세요", "입력 안됨",
							JOptionPane.ERROR_MESSAGE);
				else {
					int input = JOptionPane.showConfirmDialog(BulkPurchaseDialog.this,
							Integer.parseInt(tf.getText()) + "장 구매하시겠습니까?", "구매 확인", JOptionPane.OK_CANCEL_OPTION);
					if (input == JOptionPane.OK_OPTION) {
						buy = true;
						dispose();
					} else {
						tf.setText("");
					}
				}
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		setModal(true);
		setTitle("대량 구매");
		pack();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(frame);
	}

	public static int showDialog(JFrame frame) {
		BulkPurchaseDialog bulkPurchaseDialog = new BulkPurchaseDialog(frame);
		bulkPurchaseDialog.setVisible(true);
		if (bulkPurchaseDialog.buy)
			return Integer.parseInt(bulkPurchaseDialog.tf.getText());
		else
			return 0;
	}
}
