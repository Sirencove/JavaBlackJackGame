/*
Madison Stevens
CompSci 182/182L
10-12-17 
*/
package project3;

import java.awt.Image;

/*****************************************************************
   Class Card, the derived class each card is one object of type Card
   May be placed in a file named Card.java
******************************************************************/

class Card extends Link {
  private Image cardimage;
  private int cardNum;
  public Card (int cardnum) {
    cardNum = cardnum%13+1;
    if (cardNum >= 10){
    cardNum = 10;
    }
    cardimage = Project3.load_picture("images/gbCard" + cardnum + ".gif");
    // code ASSUMES there is an images sub-dir in your project folder
    if (cardimage == null) {
      System.out.println("Error - image failed to load: images/gbCard" + cardnum + ".gif");
      System.exit(-1);
    }
  }
  public Card getNextCard() {
    return (Card)next;
  }
  public Image getCardImage() {
    return cardimage;
  }  public int getcardnum (){
      return cardNum;
  }
}
  //end class Card 
   