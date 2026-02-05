package atm;

import javax.swing.*;

public class AccountData {
	String pincode;
	String customername;
	String accounttype;
	String accountnumber;
	String startbalance;

	// CR-01: Daily withdrawal limit validation added for maintenance updategit
	public AccountData(String p, String c, String a, String an, String s) {
		pincode = p;
		customername = c;
		accounttype = a;
		accountnumber = an;
		startbalance = s;
	}

	public void print() {
		JOptionPane.showMessageDialog(null,
				"PINCODE : " + pincode + "\n\tCustomer Name : " + customername + "\n\tAccount Type : " + accounttype +
						"Account Number : " + accountnumber + "\nStarting Balance : " + startbalance,
				"Account Information ", JOptionPane.INFORMATION_MESSAGE);
	}

}
