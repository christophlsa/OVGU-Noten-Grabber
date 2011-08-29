package hisqisnoten.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class HisqisLoginDataDialog extends JDialog implements ActionListener {

	/**
	 * I don't know why but Eclipse expects a version id.
	 */
	private static final long serialVersionUID = -8581833520024767430L;
	
	JLabel labelUsername;
	JTextField textfieldUsername;
	JLabel labelPassword;
	JPasswordField passwordfieldPassword;
	JButton buttonLogin;

	public HisqisLoginDataDialog(JFrame owner) {
		super(owner, "Login Data", true);
		
		JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        labelUsername = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(labelUsername, cs);

        textfieldUsername = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(textfieldUsername, cs);

        labelPassword = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(labelPassword, cs);

        passwordfieldPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(passwordfieldPassword, cs);
        panel.setBorder(new LineBorder(Color.GRAY));

        buttonLogin = new JButton("Login");
        
        buttonLogin.addActionListener(this);
        
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonLogin, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
        
        setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(buttonLogin)) {
			dispose();
		}
		
	}
	
	public static String[] getLoginDataViaDialog(JFrame parent) {
		HisqisLoginDataDialog dialog = new HisqisLoginDataDialog(parent);
		
		return new String[] {
					dialog.textfieldUsername.getText().trim(),
					String.valueOf(dialog.passwordfieldPassword.getPassword()).trim()
				};
	}
}
