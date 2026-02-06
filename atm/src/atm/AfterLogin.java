package atm;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
 
public class AfterLogin extends JFrame implements ActionListener
{
    
    JButton equiryBtn,withdrawBtn,logoutBtn,transferBtn,transactionHistoryButton;  
    JLabel atmLab;
    Container con;
    ArrayList customerlist;
    String s1;
    String pincode;
    
    AfterLogin()
    {
        
        super("Transaction");
        customerlist=new ArrayList();
        
        con = this.getContentPane();
        con.setLayout(null);
        con.setBackground(Color.BLACK);
       
        
        atmLab = new JLabel(new ImageIcon("atm.png"));
        atmLab.setBounds(60,10,300,100);
        
        equiryBtn = new JButton("Balance Enquiry");
        equiryBtn.setBounds(10,130,150,40);
        
        transferBtn = new JButton("Transfer Money");
        transferBtn.setBounds(260,130,150,40);
        
        withdrawBtn = new JButton("WithDraw Money");
        withdrawBtn.setBounds(260,230,150,40);
        
        transactionHistoryButton = new JButton("Transaction History");
        transactionHistoryButton.setBounds(200,300,180,30);
        transactionHistoryButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(10,230,150,40);
               
       con.add(atmLab);
       con.add(equiryBtn);
       con.add(withdrawBtn);
       con.add(transferBtn);
       con.add(transactionHistoryButton);
       con.add(logoutBtn);
    /********************************************************************/
    
    equiryBtn.addActionListener(this);
    transferBtn.addActionListener(this);
    withdrawBtn.addActionListener(this);
    transactionHistoryButton.addActionListener(this);
    logoutBtn.addActionListener(this);
   
    loadPersons();
    }
    /*****************************Load Data from File****************************************/ 
   
    	public void loadPersons()
	{
		String ss[]=null;
		String pincode,customername,accounttype,accountnumber,startbalance;
		
		try
		{
			FileReader fr=new FileReader("Customer Record.txt");
			BufferedReader br=new BufferedReader(fr);
			

			String line=br.readLine();	
			
			while(line != null)
			{
				ss=line.split(",");
				pincode=ss[0];
				customername=ss[1];
				accounttype=ss[2];
				accountnumber=ss[3];
				startbalance=ss[4];

				AccountData atm=new AccountData(pincode,customername,accounttype,accountnumber,startbalance);
				customerlist.add(atm);
				line=br.readLine();
			}
				br.close();
				fr.close();
		}
		catch(IOException ioEX)
		{
			System.out.println(ioEX);
		}
	}

/***************************************************************************************************************************/

/********************************************************* Transaction Logging Method *************************************/
private void logTransaction(String type, double amount, double newBalance) {
    String filename = "transactions_" + this.pincode + ".txt";
    try {
        FileWriter fw = new FileWriter(filename, true);
        BufferedWriter bw = new BufferedWriter(fw);
        
        // Get current timestamp
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = sdf.format(new java.util.Date());
        
        // Write transaction details
        bw.write(type + " | " + amount + " | " + newBalance + " | " + timestamp);
        bw.newLine();
        bw.close();
        fw.close();
    } catch(IOException ioEX) {
        System.out.println(ioEX);
    }
}

/***************************************************************************************************************************/

/********************************************************* View Transaction History *************************************/
public void viewTransactionHistory(String pincode) {
    String filename = "transactions_" + pincode + ".txt";
    try {
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        
        ArrayList<String> transactions = new ArrayList<>();
        String line = br.readLine();
        
        while(line != null) {
            transactions.add(line);
            line = br.readLine();
        }
        br.close();
        fr.close();
        
        // Display last 10 transactions
        StringBuilder history = new StringBuilder();
        int start = Math.max(0, transactions.size() - 10);
        
        if(transactions.isEmpty()) {
            history.append("No transactions found.");
        } else {
            history.append("Last ").append(Math.min(10, transactions.size())).append(" Transactions:\n\n");
            for(int i = start; i < transactions.size(); i++) {
                history.append(transactions.get(i)).append("\n");
            }
        }
        
        JOptionPane.showMessageDialog(null, history.toString(), "Transaction History", JOptionPane.INFORMATION_MESSAGE);
    } catch(IOException ioEX) {
        JOptionPane.showMessageDialog(null, "No transaction history found for this account.", "Transaction History", JOptionPane.INFORMATION_MESSAGE);
    }
}

/***************************************************************************************************************************/


/********************************************************* Balance Enquiry of Customer ************************************/
    private void inquiry(String k) {
        for(int i=0;i<customerlist.size();i++) {
            AccountData atm=(AccountData)customerlist.get(i);
            if(k.equals(atm.pincode)) {
                this.pincode = k;
                JOptionPane.showMessageDialog(null,"Welcome to your atm data Mr  ."+atm.customername+"\nYour Total Cash Is : "+atm.startbalance,"WELCOME WELCOME MR  "+atm.customername,JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }        

/***************************************************************************************************************************/


/****************************************************** Balance Transfer ***************************************************/
	public void transfer(String k)
	{
		String a,b,c;
		int d,e,f;


		for(int i=0;i<customerlist.size();i++)
		{
			AccountData atm=(AccountData)customerlist.get(i);
			if(k.equals(atm.pincode));
			{
				a=atm.startbalance;
				d=Integer.parseInt(a);

				c=JOptionPane.showInputDialog(null,"Enter The Account Number To whom you Transfer Amount","MONEY TRANSACTION MENU",JOptionPane.QUESTION_MESSAGE);
				//CR02- validation & error handling for balance transfer 
        		try {
            		b = JOptionPane.showInputDialog(null,"Enter The Amount To Transfer", "MONEY TRANSACTION MENU",JOptionPane.QUESTION_MESSAGE);
          			e = Integer.parseInt(b);
          			if (e <= 0) {
            			throw new NumberFormatException();
          			}
        				} catch (NumberFormatException ex) {JOptionPane.showMessageDialog(null,"Invalid input. Please enter a valid numeric amount.","Input Error",JOptionPane.ERROR_MESSAGE);
         				return;
        			}
        		f = d - e;
				while(f < 0)
				{
					a=atm.startbalance;
					d=Integer.parseInt(a);

					b=JOptionPane.showInputDialog(null,"Invalid Amount\nPlease Enter The Suffecient Amount To Transfer","MONEYTRANSACTION MENU",JOptionPane.WARNING_MESSAGE);
					e=Integer.parseInt(b);
					f=d-e;
				}
				String u=String.valueOf(f);
				atm.startbalance=u;
				
			logTransaction("Transfer", (double)e, (double)f);
			
				JOptionPane.showMessageDialog(null,"Transaction is done succesfully\n\nAmount of "+b+"is transferd To "+c+"\n\nYour Total Cash Is : "+atm.startbalance,"MONEY TRANSACTION PROCESSED",JOptionPane.INFORMATION_MESSAGE);
				
                                Admin as = new Admin();
				as.savePerson();
			}	
		}
	}

/**********************************************************************************************************************************/


/********************************************************* WithDraw Balance ******************************************************/        
	public void withdraw(String o)
	{
		String a,b,c;
		int d,e,f;

		for(int i=0;i<customerlist.size();i++)
		{
			AccountData atm=(AccountData)customerlist.get(i);
			if(o.equals(atm.pincode))
			{
				a=atm.startbalance;
				d=Integer.parseInt(a);

				//CR02-validation & error handling for withdraw  balance
    			try {
          		b = JOptionPane.showInputDialog(null,"Enter The Amount To Withdraw","WITHDRAW MENU",JOptionPane.QUESTION_MESSAGE);
          		e = Integer.parseInt(b);
          		if (e <= 0) {
            		throw new NumberFormatException();
          		}
        			} catch (NumberFormatException ex) {JOptionPane.showMessageDialog(null,"Invalid input. Please enter a valid numeric amount.","Input Error",JOptionPane.ERROR_MESSAGE);
        			return;
     			}
      			f = d - e;

				while(f <0)
				{
					a=atm.startbalance;
					d=Integer.parseInt(a);

					b=JOptionPane.showInputDialog(null,"Invalid Amount\nPlease Enter The Suffecient Amount To WithDraw","WITHDRAW MENU",JOptionPane.WARNING_MESSAGE);
					e=Integer.parseInt(b);

					f=d-e;
				}
				c=String.valueOf(f);
				atm.startbalance=c;			
			logTransaction("Withdraw", (double)e, (double)f);
							JOptionPane.showMessageDialog(null,"Withdarw proccesed\nYou have Withdarwed Amount of"+b+"\nYour Total Cash Is now: "+atm.startbalance,"Information",JOptionPane.INFORMATION_MESSAGE);
				Admin ad = new Admin();
                                ad.savePerson();
			}
		}
	}
/********************************************************************************************************************************/



/*************************************** ActionListener Code for Buttons ***********************************************************/
    
    public void actionPerformed(ActionEvent e)
    {
        JButton b = (JButton)e.getSource();
        
	if(b == equiryBtn)
        {		
            s1= JOptionPane.showInputDialog(null,"Enter PinCode To Check Account Balance ","Check Balance",JOptionPane.QUESTION_MESSAGE);
            this.pincode = s1;
            
            
            		for(int i=0;i<customerlist.size();i++)
		{
			AccountData atm=(AccountData)customerlist.get(i);

			if(!s1.equals(atm.pincode))
			{
				JOptionPane.showMessageDialog(null,"You have entered Wrong Pincode \nPlease Enter Valid Pincode!!!!","Warning",JOptionPane.WARNING_MESSAGE);
					
			}
                        else if(s1.equals(atm.pincode))
			{
			
                            inquiry(s1);
                        }            
                }
        }
/******************************************************************************************************************************/
        
        if(b == withdrawBtn)
        {
          s1=JOptionPane.showInputDialog(null,"Enter PinCode To withDraw Balance ","Withdraw Balance",JOptionPane.QUESTION_MESSAGE);
          this.pincode = s1;
           for(int i=0;i<customerlist.size();i++)
		{
			AccountData atm=(AccountData)customerlist.get(i);

                         if(s1.equals(atm.pincode))
			{
			
                            withdraw(s1);
                        }
                         
                         else if(!s1.equals(atm.pincode))
			{
				JOptionPane.showMessageDialog(null,"You have entered Wrong Pincode \nPlease Enter Valid Pincode!!!!","Warning",JOptionPane.WARNING_MESSAGE);
				
			}
                }
        }
/******************************************************************************************************************************/

        if(b == transferBtn)
        {
             s1=JOptionPane.showInputDialog(null,"Enter PinCode To Transfer Balance ","Share balance",JOptionPane.QUESTION_MESSAGE);
             this.pincode = s1;
           	
             for(int i=0;i<customerlist.size();i++)
		{
			AccountData atm=(AccountData)customerlist.get(i);

			if(!s1.equals(atm.pincode))
			{
				JOptionPane.showMessageDialog(null,"You have entered Wrong Pincode \nPlease Enter Valid Pincode!!!!","Warning",JOptionPane.WARNING_MESSAGE);
					
			}
                        else if(s1.equals(atm.pincode))
			{
			
                            transfer(s1);
                        }            
                }        
        }
        
/******************************************************************************************************************************/

        if(b == transactionHistoryButton)
        {
            s1= JOptionPane.showInputDialog(null,"Enter PinCode To View Transaction History ","Transaction History",JOptionPane.QUESTION_MESSAGE);
            
            for(int i=0;i<customerlist.size();i++)
            {
                AccountData atm=(AccountData)customerlist.get(i);
                
                if(s1.equals(atm.pincode))
                {
                    viewTransactionHistory(s1);
                }
                else if(!s1.equals(atm.pincode))
                {
                    JOptionPane.showMessageDialog(null,"You have entered Wrong Pincode \nPlease Enter Valid Pincode!!!!","Warning",JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        
/******************************************************************************************************************************/
        
            if(b == logoutBtn)
        {					
	int n=JOptionPane.showConfirmDialog(null,"Are you sure you want to exit?","Exit",JOptionPane.YES_NO_OPTION);
		if(n==JOptionPane.YES_OPTION)
                {
		JOptionPane.showMessageDialog(null,"Good Bye","ATM",JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
                }

        }
            
   /******************************************************************************************************************************/                             
    
    
    
    
    }  
   
}
