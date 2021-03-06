/*
Madison Stevens
Compsci 182/182L
10-9-17
 */
package project3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Project3 extends JFrame implements ActionListener {

    private static int winxpos = 0, winypos = 0;      // place window here

    private JButton exitButton, newButton, hitButton, standButton;
    private CardList theDeck = null;
    private CardList playerDeck = null;
    private CardList dealerDeck = null;
    private JPanel northPanel;

    private MyPanel centerPanel;
    private static JFrame myFrame = null;

    ////////////              MAIN      ////////////////////////
    public static void main(String[] args) {
        Project3 tpo = new Project3();
    }

    ////////////            CONSTRUCTOR   /////////////////////
    public Project3() {
        myFrame = this;                 // need a static variable reference to a JFrame object
        northPanel = new JPanel();
        northPanel.setBackground(Color.white);
        newButton = new JButton("New Game");
        northPanel.add(newButton);
        newButton.addActionListener(this);
        hitButton = new JButton("Hit");
        northPanel.add(hitButton);
        hitButton.addActionListener(this);
        standButton = new JButton("Stand");
        northPanel.add(standButton);
        standButton.addActionListener(this);
        exitButton = new JButton("Exit");
        northPanel.add(exitButton);
        exitButton.addActionListener(this);
        getContentPane().add("North", northPanel);
        exitButton.setEnabled(false);
        centerPanel = new MyPanel();
        getContentPane().add("Center", centerPanel);

        theDeck = new CardList(52);
        theDeck.shuffle();
        playerDeck = new CardList(0);
        playerDeck.insertCard(theDeck.deleteCard(1));
        playerDeck.insertCard(theDeck.deleteCard(1));
        dealerDeck = new CardList(0);
        dealerDeck.insertCard(theDeck.deleteCard(1));
        dealerDeck.insertCard(theDeck.deleteCard(1));
        setSize(800, 700);
        setLocation(winxpos, winypos);

        setVisible(true);

        boolean play = blackjack(cardsum(playerDeck));
        boolean deal = blackjack(cardsum(dealerDeck));
        if (play == true && deal == false) {
            System.out.println("You win!!! You got a blackjack");
            dispose();
            System.exit(0);
        } else if (play == false && deal == true) {
            System.out.println("You lose!!! Dealer had black jack");
            dispose();
            System.exit(0);
        } else if (play == true && deal == true){
            System.out.println("Draw!!! Both had blackjack");
            dispose();
            System.exit(0);
        }
    }

    ////////////   BUTTON CLICKS ///////////////////////////
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == exitButton) {
            dispose();
            System.exit(0);
        }
        if (e.getSource() == newButton) {
            Project3 tpo = new Project3();
            boolean play = blackjack(cardsum(playerDeck));
            boolean deal = blackjack(cardsum(dealerDeck));
            if (play == true && deal == false) {
                System.out.println("You win!!!");
                dispose();
                System.exit(0);
            } else if (play == false && deal == true) {
                System.out.println("You lose!!! Dealer had black jack");
                dispose();
                System.exit(0);
            } else if (play == true && play == true){
                System.out.println("Draw!!! Both had blackjack");
                dispose();
                System.exit(0);
            }
            repaint();
        }
        if (e.getSource() == hitButton) {
            playerDeck.insertCard(theDeck.deleteCard(0));
            playeraction(cardsum(playerDeck));
            repaint();
        }
        if (e.getSource() == standButton) {
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            dealeraction(cardsum(dealerDeck));
        }
    }

    public int cardsum(CardList hand) {
        Card current = hand.getFirstCard();
        boolean ace = (false);
        int sum = 0;
        while (current != null) {
            int cardnum = current.getcardnum();
            sum = sum + cardnum;
            if (cardnum == 1) {
                ace = (true);
            }
            current = current.getNextCard();
            if (ace && sum <= 11) {
                sum = sum + 10;
            } else if (ace && sum >= 11) {
                sum = sum++;
            }
        }
        return sum;
    }

    public boolean blackjack(int sum) {
        if (sum == 21) {
            return true;
        } else {
            return false;
        }
    }

    public void playeraction(int sum) {
        if (sum > 21) {
            System.out.println("You lose!!! Went over 21");
            dispose();
            System.exit(0);
        } else if (sum < 21) {
            cardsum(playerDeck);
        }
    }

    public void comparehands() {
        if (cardsum(playerDeck) > cardsum(dealerDeck)) {
            System.out.println("You win!!! Hand higher than dealers");
            dispose();
            System.exit(0);
        } else if (cardsum(playerDeck) < cardsum(dealerDeck)) {
            System.out.println("You lose!!! Dealer hand was higher");
            dispose();
            System.exit(0);
        } else {
            System.out.println("Draw!!! Your hands matched");
            dispose();
            System.exit(0);
        }
    }

    public void dealeraction(int sum) {
        if (sum < 16) {
            dealerDeck.insertCard(theDeck.deleteCard(0));
            repaint();
            dealeraction(cardsum(dealerDeck));
        } else if (sum > 21) {
            System.out.println("You win!!! Dealer went bust");
            dispose();
            System.exit(0);
        } else if (sum >= 16) {
            comparehands();
        }
    }
// This routine will load an image into memory
//

    public static Image load_picture(String fname) {
        // Create a MediaTracker to inform us when the image has
        // been completely loaded.
        Image image;
        MediaTracker tracker = new MediaTracker(myFrame);

        // getImage() returns immediately.  The image is not
        // actually loaded until it is first used.  We use a
        // MediaTracker to make sure the image is loaded
        // before we try to display it.
        image = myFrame.getToolkit().getImage(fname);

        // Add the image to the MediaTracker so that we can wait
        // for it.
        tracker.addImage(image, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
            System.err.println(e);
        }

        if (tracker.isErrorID(0)) {
            image = null;
        }
        return image;
    }
// --------------   end of load_picture ---------------------------

    class MyPanel extends JPanel {

        ////////////    PAINT   ////////////////////////////////
        public void paintComponent(Graphics g) {
            //
            int xpos = 330, ypos = 500;
            if (playerDeck == null) {
                return;
            }
            Card current = playerDeck.getFirstCard();
            while (current != null) {
                Image tempimage = current.getCardImage();
                g.drawImage(tempimage, xpos, ypos, this);
                // note: tempimage member variable must be set BEFORE paint is called
                xpos += 80;
                if (xpos > 700) {
                    xpos = 25;
                    ypos += 105;
                }
                current = current.getNextCard();
            } //while
            int dealerxpos = 330, dealerypos = 50;
            if (dealerDeck == null) {
                return;
            }
            Card dealercurrent = dealerDeck.getFirstCard();
            while (dealercurrent != null) {
                Image tempimage = dealercurrent.getCardImage();
                g.drawImage(tempimage, dealerxpos, dealerypos, this);
                // note: tempimage member variable must be set BEFORE paint is called
                dealerxpos += 80;
                if (dealerxpos > 700) {
                    dealerxpos = 25;
                    dealerypos += 105;
                }
                dealercurrent = dealercurrent.getNextCard();
            } //while
            int deckxpos = 650, deckypos = 30;
            Image tempimage = load_picture("images/gbCard52.gif");
            g.drawImage(tempimage, deckxpos, deckypos, this);
        }
    }
}    // End Of class Project3
