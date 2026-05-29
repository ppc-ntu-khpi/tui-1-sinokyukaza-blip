package com.mybank.tui;

import com.mybank.domain.Account;
import com.mybank.domain.Bank;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.Customer;
import com.mybank.domain.SavingsAccount;
import jexer.TAction;
import jexer.TApplication;
import jexer.TField;
import jexer.TText;
import jexer.TWindow;
import jexer.event.TMenuEvent;
import jexer.menu.TMenu;

/**
 *
 * @author Alexander 'Taurus' Babich
 */
public class TUIdemo extends TApplication {

    private static final int ABOUT_APP = 2000;
    private static final int CUST_INFO = 2010;

    public static void main(String[] args) throws Exception {
        TUIdemo tdemo = new TUIdemo();
        (new Thread(tdemo)).start();
    }

    public TUIdemo() throws Exception {
        super(BackendType.SWING);

        addToolMenu();
        //custom 'File' menu
        TMenu fileMenu = addMenu("&File");
        fileMenu.addItem(CUST_INFO, "&Customer Info");
        fileMenu.addDefaultItem(TMenu.MID_SHELL);
        fileMenu.addSeparator();
        fileMenu.addDefaultItem(TMenu.MID_EXIT);
        //end of 'File' menu  

        addWindowMenu();

        //custom 'Help' menu
        TMenu helpMenu = addMenu("&Help");
        helpMenu.addItem(ABOUT_APP, "&About...");
        //end of 'Help' menu 

        setFocusFollowsMouse(true);
        //Customer window
        ShowCustomerDetails();
    }

    @Override
    protected boolean onMenu(TMenuEvent menu) {
        if (menu.getId() == ABOUT_APP) {
            messageBox("About", "\t\t\t\t\t   Just a simple Jexer demo.\n\nCopyright \u00A9 2019 Alexander \'Taurus\' Babich").show();
            return true;
        }
        if (menu.getId() == CUST_INFO) {
            ShowCustomerDetails();
            return true;
        }
        return super.onMenu(menu);
    }

    private void ShowCustomerDetails() {

        TWindow custWin = addWindow(
                "Customer Window",
                2,
                1,
                50,
                14,
                TWindow.NOZOOMBOX
        );

        custWin.newStatusBar(
                "Enter customer number and press Show..."
        );

        custWin.addLabel("Enter customer number:", 2, 2);

        TField custNo = custWin.addField(
                26,
                2,
                5,
                false
        );

        TText details = custWin.addText(
                "Customer information will appear here...",
                2,
                5,
                44,
                6
        );

        custWin.addButton("&Show", 35, 2, new TAction() {

            @Override
            public void DO() {

                try {

                    int custNum = Integer.parseInt(
                            custNo.getText()
                    );

                    // Create bank
                    Bank bank = new Bank();

                    // Create customers
                    Customer customer1 =
                            new Customer("John", "Doe");

                    Customer customer2 =
                            new Customer("Jane", "Smith");

                    // Add accounts
                    customer1.addAccount(
                            new CheckingAccount(500.0)
                    );

                    customer2.addAccount(
                            new SavingsAccount(1200.0, 0.05)
                    );

                    // Add customers to bank
                    bank.addCustomer(customer1);
                    bank.addCustomer(customer2);

                    // Get customer
                    Customer customer =
                            bank.getCustomer(custNum);

                    Account account =
                            customer.getAccount(0);

                    details.setText(
                            "Customer ID: " + custNum +
                            "\nOwner Name: "
                            + customer.getFirstName()
                            + " "
                            + customer.getLastName()
                            + "\nAccount Type: "
                            + account.getClass()
                                    .getSimpleName()
                            + "\nBalance: $"
                            + account.getBalance()
                    );

                } catch (Exception e) {

                    messageBox(
                            "Error",
                            "Invalid customer number!"
                    ).show();
                }
            }
        });
    }
}
