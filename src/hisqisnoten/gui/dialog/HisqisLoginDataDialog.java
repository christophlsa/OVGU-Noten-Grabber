/* Copyright (C) 2010-2011 Christoph Giesel
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package hisqisnoten.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class HisqisLoginDataDialog extends JDialog implements ActionListener, KeyListener {

	/**
	 * I don't know why but Eclipse expects a version id.
	 */
	private static final long serialVersionUID = -8581833520024767430L;

	JLabel labelUsername;
	JTextField textfieldUsername;
	JLabel labelPassword;
	JPasswordField passwordfieldPassword;
	JButton buttonLogin;

	boolean success = false;

	public HisqisLoginDataDialog(JFrame parent, String username, String password) {
		super(parent, "Login Data", true);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();

		cs.fill = GridBagConstraints.HORIZONTAL;

		labelUsername = new JLabel("Username: ");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		panel.add(labelUsername, cs);

		textfieldUsername = new JTextField(20);
		textfieldUsername.addKeyListener(this);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 2;
		panel.add(textfieldUsername, cs);

		if (username != null)
			textfieldUsername.setText(username);

		labelPassword = new JLabel("Password: ");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		panel.add(labelPassword, cs);

		passwordfieldPassword = new JPasswordField(20);
		passwordfieldPassword.addKeyListener(this);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 2;
		panel.add(passwordfieldPassword, cs);
		panel.setBorder(new LineBorder(Color.GRAY));

		if (password != null)
			passwordfieldPassword.setText(password);

		buttonLogin = new JButton("Login");

		buttonLogin.addActionListener(this);

		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(buttonLogin, BorderLayout.PAGE_END);

		pack();
		setResizable(false);
		setLocationRelativeTo(parent);

		setVisible(true);
	}

	public void done() {
		success = true;
		dispose();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(buttonLogin)) {
			done();
		}

	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_ENTER) {
			done();
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
	}

	@Override
	public void keyTyped(KeyEvent event) {
	}

	public static Object[] getLoginDataViaDialog(JFrame parent, String username, String password) {
		HisqisLoginDataDialog dialog = new HisqisLoginDataDialog(parent, username, password);

		if(!dialog.success)
			return new Boolean[] { false };

		return new Object[] {
				true,
				dialog.textfieldUsername.getText().trim(),
				String.valueOf(dialog.passwordfieldPassword.getPassword()).trim()
		};
	}
}
