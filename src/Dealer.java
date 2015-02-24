
public class Dealer {
	private static  int numOfPlayer;
	private static Card [][] cardArr;
	private static Card [] communityCards;
	private static int [][] playerComp;
	
	public Dealer(int numOfPlayer){
		this.numOfPlayer = numOfPlayer;
		playerComp = new int [numOfPlayer+1][5];
		cardArr = new Card[numOfPlayer+1][2];
		dealCard();
		for(int i=0; i<numOfPlayer+1; i++)
			checkCardType(cardArr[i]);
	}
	
	public Card [][] dealCard(){
		communityCards = new Card [5];
		cardArr[0][0] = new Card("A","diamond");
		cardArr[0][1] = new Card("A","club");
		for(int i=1 ; i<numOfPlayer+1 ; i++){
			cardArr[i][0] = new Card();
			cardArr[i][1] = new Card();
		}
		for(int i=0 ; i<5 ; i++){
			communityCards[i] = new Card();
		}
		cardTranslate();

		return cardArr;
	}

	public void cardTranslate(){
		for(int i=0 ; i<5 ; i++){
			System.out.println(Card.cardNumVar[communityCards[i].getCardNum()]+" - "+Card.cardSuitVar[communityCards[i].getCardSuit()]);
		}
			System.out.println("============");
		for(int i=0 ; i<numOfPlayer+1 ; i++){
			System.out.print(Card.cardNumVar[cardArr[i][0].getCardNum()]+" - "+Card.cardSuitVar[cardArr[i][0].getCardSuit()]+" / ") ;
			System.out.println(Card.cardNumVar[cardArr[i][1].getCardNum()]+" - "+Card.cardSuitVar[cardArr[i][1].getCardSuit()]) ;
		}
		System.out.println("===== After sorting ======");
	}

	public int [] checkCardType(Card [] playerCards){
		Card [] cardComb = new Card[7];
		cardComb[0] = playerCards[0];
		cardComb[1] = playerCards[1];
		for(int i=2 ; i<cardComb.length ; i++){
			cardComb[i] = communityCards[i-2];
		}
		playerCards = Sort.sort(cardComb);
			for(int j=0 ; j<playerCards.length ; j++)
				System.out.print(Card.cardNumVar[playerCards[j].getCardNum()]+Card.cardSuitVar[playerCards[j].getCardSuit()]+" / ") ;
			System.out.print("\n") ;

		return checkStraightFlush(cardComb);
	}

	public int [] checkStraightFlush(Card [] playerCard){ //[type][biggest number][flush]
		int [] tempFlush = checkFlush(playerCard);
		int [] tempStraight = checkStraight(playerCard);
		if( tempFlush != null && tempStraight != null ){
			int [] temp = new int [2];
			temp[0] = 8;
			temp[1] = tempStraight[1];
			return temp;
		}else if(tempStraight != null){
			return tempStraight;
		}else
			return null;
	}
	
	public int [] checkFourKind(Card [] playerCards){
		int i;
		int [] temp = new int[3];
		boolean isFourKind=false;
		for(i=0 ; i<4 ; i++){
			if( playerCards[i].getCardNum() == playerCards[i+1].getCardNum() 
			 && playerCards[i+1].getCardNum() == playerCards[i+2].getCardNum()
			 && playerCards[i+2].getCardNum() == playerCards[i+3].getCardNum()){
				isFourKind=true;
				break;
			}
		}
		if(isFourKind){
			temp[0] = 7;
			temp[1] = playerCards[i].getCardNum();
			if(i==3)
				temp[2] = playerCards[2].getCardNum();
			else
				temp[2] = playerCards[6].getCardNum();
			return temp;
		}else
			return null;
			
	}
	
	public int [] checkFullHouse(Card [] playerCards){ //[type][three kind card][pair]
		int [] temp = new int[3];
		temp[1] = -1;
		for(int i=6 ; i>=2 ; i--){
			if(playerCards[i].getCardNum() == playerCards[i-1].getCardNum() && playerCards[i-1].getCardNum() == playerCards[i-2].getCardNum()){
				temp[1] = playerCards[i].getCardNum();
				break;
			}
		}
		if(temp[1] == -1)
			return checkFlush(playerCards);
		else{
			temp[2] = -1;
			for(int i=6 ; i>=1 ; i--){
				if(playerCards[i].getCardNum() != temp[1] && playerCards[i].getCardNum() == playerCards[i-1].getCardNum()){
					temp[2] = playerCards[i].getCardNum();
					break;
				}
			}
		}
		
		if(temp[2] == -1)
			return null;
		else{
			temp[0] = 6;
			return temp;
		}
	}
	
	public int [] checkFlush(Card [] playerCards){ //[type][biggest card][2nd card][3rd card][4th card][5th card]
		int [] temp = new int[6];
		
		for(int i=6 ; i>=4 ; i--){
			if(playerCards[i].getCardSuit() == playerCards[i-1].getCardSuit()
			&& playerCards[i-1].getCardSuit() == playerCards[i-2].getCardSuit()
			&& playerCards[i-2].getCardSuit() == playerCards[i-3].getCardSuit()
			&& playerCards[i-3].getCardSuit() == playerCards[i-4].getCardSuit()
			){
				temp[0] = 5;
				temp[1] = playerCards[i].getCardNum();
				temp[2] = playerCards[i-1].getCardNum();
				temp[3] = playerCards[i-2].getCardNum();
				temp[4] = playerCards[i-3].getCardNum();
				temp[5] = playerCards[i-4].getCardNum();
				break;
			}
		}
		if(temp[0] != 5)
			return null;
		else
			return temp;
	}

	public int [] checkStraight(Card [] playerCards){ //[type][largest card]
		int [] temp = new int[2];
		for(int i=6 ; i>=4 ; i--){
			if(playerCards[i].getCardNum() == playerCards[i-1].getCardNum()-1
			&& playerCards[i-1].getCardNum() == playerCards[i-2].getCardNum()-1
			&& playerCards[i-2].getCardNum() == playerCards[i-3].getCardNum()-1
			&& playerCards[i-3].getCardNum() == playerCards[i-4].getCardNum()-1
			){
				temp[0] = 4;
				temp[1] = playerCards[i].getCardNum();
				break;
			}
		}
		if(temp[0] != 4)
			return null;
		else
			return temp;
	}
	
	public int [] checkThreeKind(Card [] playerCards){ //type,three kind largest card, 2nd largest card
		int [] temp = new int[4];
		for(int i=6 ; i>=2 ; i--){
			if(playerCards[i].getCardNum() == playerCards[i-1].getCardNum()
			&& playerCards[i-1].getCardNum() == playerCards[i-2].getCardNum()
			&& playerCards[i-2].getCardNum() == playerCards[i-3].getCardNum()
			){
				temp[0] = 3;
				temp[1] = playerCards[i].getCardNum();
				break;
			}
		}
		if(temp[0] == 3){
			if(playerCards[6].getCardNum() == temp[1]){
				temp[2] = playerCards[3].getCardNum();
				temp[3] = playerCards[2].getCardNum();
			}else if(playerCards[5].getCardNum() == temp[1] && playerCards[6].getCardNum() != temp[1]){
				temp[2] = playerCards[6].getCardNum();
				temp[3] = playerCards[2].getCardNum();
			}else{
				temp[2] = playerCards[6].getCardNum();
				temp[3] = playerCards[5].getCardNum();
			}
			return temp;
		}else
			return null;
	}
	
	public int [] checkTwoPair(Card [] playerCards){ //type,1st pair,2nd pair
		int [] temp = new int [3];
		int [] pair1;
		int [] pair2;

		pair1 = checkPair(6,playerCards);
		pair2 = checkPair(pair1[1]-1,playerCards);
		
		if(pair2 == null)
			return null;
		else{
			temp[0] = 2;
			temp[1] = pair1[2];
			temp[2] = pair2[2];
			return temp;
		}
	}
	
	public int [] checkPair(int index , Card [] playerCards){ // type,index,cardnum
		int [] temp = new int[3];
		
		for(int i=index ; i>=1 ; i--){
			if( playerCards[i].getCardNum() == playerCards[i-1].getCardNum() ){
				temp[0] = 1;
				temp[1] = i-1;
				temp[2] = playerCards[i].getCardNum();
				break;
			}
		}
		
		if(temp[0] != 1)
			return null;
		else{
			return temp;
		}
	}
	
	public int [] checkHighCard(Card [] playerCards){ // type,1st,2nd,3rd,4th,5th
		int [] temp = {0,playerCards[6].getCardNum(),
							playerCards[5].getCardNum(),
							playerCards[4].getCardNum(),
							playerCards[3].getCardNum(),
							playerCards[2].getCardNum()};
		return temp;
	}
}
