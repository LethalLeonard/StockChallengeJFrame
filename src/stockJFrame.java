import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class stockJFrame extends JFrame
{
    private JTextField sharesBuyTF, sharesBuyPriceTF, sharesSellTF, sharesSellPriceTF;
    private JLabel sharesBuyLabel, sharesBuyPriceLabel, sharesSellLabel, sharesSellPriceLabel;
    private JPanel inputBoxes, buttonPanel;
    private JButton continueButton;
    private stockHandler handler;
    private double sharesBuy, sharesSell, sharesBuyPrice, sharesSellPrice, profit, totalSell, totalBuy, totalComm;
    private double totalBuyAtSell, totalCommAtSell, totalCommAtBuy, commBuyAtSell, effectiveComm, stillOwned, valueStillOwned;
    private String output;
    private final double COMMISSION = 0.02;

    public static void main(String[] args)
    {
        //creates the object for the JFrame in stockJFrame
        stockJFrame f1 = new stockJFrame();

        //Sets the Default Close Op
        f1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //sets the window size
        f1.setSize(350,170);

        //sets whether the window is visible or not
        f1.setVisible(true);
    }

    private stockJFrame()
    {
        //sets the window title and layout for the popup box
        super("Stocks Calculator");
        setLayout(new BorderLayout());

        //Initializes the action handler
        handler = new stockHandler();

        //sets the layout for the information boxes
        inputBoxes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(inputBoxes);

        //sets up the panel for the button
        buttonPanel = new JPanel(new FlowLayout());
        add(buttonPanel, BorderLayout.SOUTH);
        
        //sets up the label and text field for the number of shares purchased
        sharesBuyLabel = new JLabel("Number of Shares Bought:");
        sharesBuyLabel.setLabelFor(sharesBuyTF);
        inputBoxes.add(sharesBuyLabel);
        sharesBuyTF = new JTextField(15);
        sharesBuyTF.addActionListener(handler);
        inputBoxes.add(sharesBuyTF);

        //sets up the label and text field for the price of shares bought
        sharesBuyPriceLabel = new JLabel("Price per share bought: $");
        sharesBuyPriceLabel.setLabelFor(sharesBuyPriceTF);
        inputBoxes.add(sharesBuyPriceLabel);
        sharesBuyPriceTF = new JTextField(15);
        sharesBuyPriceTF.addActionListener(handler);
        inputBoxes.add(sharesBuyPriceTF);

        //sets up the label and text field for the number of shares sold
        sharesSellLabel = new JLabel("Number of Shares Sold:");
        sharesSellLabel.setLabelFor(sharesSellTF);
        inputBoxes.add(sharesSellLabel);
        sharesSellTF = new JTextField(15);
        sharesSellTF.addActionListener(handler);
        inputBoxes.add(sharesSellTF);

        //sets up the label and text field for the sell price
        sharesSellPriceLabel = new JLabel("Price per share sold: $");
        sharesSellPriceLabel.setLabelFor(sharesSellPriceTF);
        inputBoxes.add(sharesSellPriceLabel);
        sharesSellPriceTF = new JTextField(15);
        sharesSellPriceTF.addActionListener(handler);
        inputBoxes.add(sharesSellPriceTF);

        //sets up the continue button
        continueButton = new JButton("Continue");
        buttonPanel.add(continueButton);
        continueButton.addActionListener(handler);

        //makes the window not resizable
        setResizable(false);
    }

    private void calculateProfit()
    {
        //Calculates total money for the buy and sell transactions
        totalSell = sharesSell * sharesSellPrice;
        totalBuy = sharesBuy * sharesBuyPrice;
        totalBuyAtSell = sharesSell * sharesBuyPrice;

        //Calculates the stock still owned and it's price at the sell price point
        stillOwned = sharesBuy - sharesSell;
        valueStillOwned = stillOwned * sharesSellPrice;

        //Calculates all commission-related things
        totalCommAtBuy = totalBuy * COMMISSION;
        totalCommAtSell = totalSell * COMMISSION;
        commBuyAtSell = totalBuyAtSell * COMMISSION;
        totalComm = totalCommAtBuy + totalCommAtSell;
        effectiveComm = totalComm - commBuyAtSell;
        //calculates profit
        profit = totalSell - totalBuyAtSell - effectiveComm;
    }

    private void displayOutput()
    {   //formats the output for the joptionpane
        output =  String.format("Total Buy Price: $%.2f \n" +
                  "Total Buy Commission: $%.2f\n" +
                  "Total Sell Price: $%.2f\n" +
                  "Total Sell Commission: $%.2f\n" +
                  "Effective Commission: $%.2f\n",
                  totalBuy, totalCommAtBuy, totalSell, totalCommAtSell, effectiveComm);

        //decision structure to decide whether it was a good investment to sell
        if(profit > 0)
        {
            output += String.format("Total Profit/Loss: $%.2f\n" +
                    "This was a wise investment.\n", profit);
        }
        else
        {
            output += String.format("Total Profit/Loss: $%.2f\n" +
                    "This was an unwise investment.\n", profit);
        }
        //add the final string to the output
        output += String.format("Stocks still owned: %.0f\n" +
                                "Current value of owned Stocks: $%.2f",
                                stillOwned,valueStillOwned);
        //displays the output
        JOptionPane.showMessageDialog(null, output);
    }

    //class for the action handler for the text box
    private class stockHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            //when the continue button gets pressed it recieves the text from the text fields and assigns them to proper variables
            if(event.getSource() == continueButton)
            {
                //trys to parse as doubles, if fails, it notifies the user
                try
                {
                    sharesBuy = Double.parseDouble(sharesBuyTF.getText());
                    sharesBuyPrice = Double.parseDouble(sharesBuyPriceTF.getText());
                    sharesSell = Double.parseDouble(sharesSellTF.getText());
                    sharesSellPrice = Double.parseDouble(sharesSellPriceTF.getText());
                }
                catch(NumberFormatException e)
                {
                    JOptionPane.showMessageDialog(null, "Please ensure all characters are only numbers.");
                    throw e;
                }

                //checks to ensure that there are no errors in the input. If error free,
                if(sharesSell > sharesBuy)
                {
                    JOptionPane.showMessageDialog(null, "Error: You cannot sell more stocks than owned.");
                }
                else if(sharesBuy < 0 || sharesBuyPrice < 0 || sharesSell < 0 || sharesSellPrice < 0)
                {
                    JOptionPane.showMessageDialog(null, "Error: You can not Buy or sell less than 0 stocks \n" +
                                                                                 "nor sell for less than $0.");
                }
                else
                {   //if passes all the checks, gets rid of the initital window, does calculations, then displays results
                    dispose();
                    calculateProfit();
                    displayOutput();
                }
            }
            else if(event.getSource() == sharesBuyTF)
                sharesBuyTF.transferFocus();
            else if(event.getSource() == sharesBuyPriceTF)
                sharesBuyPriceTF.transferFocus();
            else if(event.getSource() == sharesSellTF)
                sharesSellTF.transferFocus();
        }
    }
}