package mjproject;

import java.sql.Date;

public class CADTO {
	
	   	//Member테이블
		private String id;
		private String pw;
		private String name;
		private String inputDate;
		
		//Account테이블
		private String accDiv;		//구분 : 입출금/예적금
		private String bank; 		//은행명
		private String accName;		//예금주
		private	String accNo;		//계좌번호
		private String bankNick;	//계좌 간편 이름
		private int accAmount;		//계좌금액
		
		//Card테이블
		private String card;		//카드사명
		private String cardName;	//명의자
		private String cardNo;		//카드번호
		private int cardAmount;  	//카드금액
		private String payBankNick;	//결제은행
		private String payDate;		//결제일
		private String cardNick;	//카드 간편 이름
		
		//CardWithdraw테이블, AccountDisposit 테이블, AccountWithdraw테이블
		private String CADate;		//사용 일자
		private String category;	//사용유형(식대, 쇼핑 등)
		private int CAAmount;		//입/출금 금액
		private String CANick;		//카드/계좌 간편 이름
	
		
		//getter, setter
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPw() {
			return pw;
		}

		public void setPw(String pw) {
			this.pw = pw;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getInputDate() {
			return inputDate;
		}

		public void setInputDate(String inputDate) {
			this.inputDate = inputDate;
		}

		public String getAccDiv() {
			return accDiv;
		}

		public void setAccDiv(String accDiv) {
			this.accDiv = accDiv;
		}

		public String getBank() {
			return bank;
		}

		public void setBank(String bank) {
			this.bank = bank;
		}

		public String getAccName() {
			return accName;
		}

		public void setAccName(String accName) {
			this.accName = accName;
		}

		public String getAccNo() {
			return accNo;
		}

		public void setAccNo(String accNo) {
			this.accNo = accNo;
		}

		public String getBankNick() {
			return bankNick;
		}

		public void setBankNick(String bankNick) {
			this.bankNick = bankNick;
		}

		public int getAccAmount() {
			return accAmount;
		}

		public void setAccAmount(int accAmount) {
			this.accAmount = accAmount;
		}

		public String getCard() {
			return card;
		}

		public void setCard(String card) {
			this.card = card;
		}

		public String getCardName() {
			return cardName;
		}

		public void setCardName(String cardName) {
			this.cardName = cardName;
		}

		public String getCardNo() {
			return cardNo;
		}

		public void setCardNo(String cardNo) {
			this.cardNo = cardNo;
		}

		public int getCardAmount() {
			return cardAmount;
		}

		public void setCardAmount(int cardAmount) {
			this.cardAmount = cardAmount;
		}

		public String getPayDate() {
			return payDate;
		}

		public void setPayDate(String payDate) {
			this.payDate = payDate;
		}

		public String getCardNick() {
			return cardNick;
		}

		public void setCardNick(String cardNick) {
			this.cardNick = cardNick;
		}

		public String getCADate() {
			return CADate;
		}

		public void setCADate(String cADate) {
			CADate = cADate;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public int getCAAmount() {
			return CAAmount;
		}

		public void setCAAmount(int cAAmount) {
			CAAmount = cAAmount;
		}

		public String getCANick() {
			return CANick;
		}

		public void setCANick(String cANick) {
			CANick = cANick;
		}
		
		
		//print메소드
		public void printAcc() {
			System.out.printf("%6s통장\t%10s\t%10s\t%20s\t%10d\t%8s\n",accDiv,bank,accName,accNo,accAmount,bankNick);
					
		}
		
		public void printCard() {
			String str = "카드";
			System.out.printf("     %4s\t%10s\t%10s\t%20s\t%10d\t%8s\t%6s\t%13s\n",str,card,cardName,cardNo,cardAmount,cardNick,payBankNick,payDate);
			
		}
		
		public void print() {
			System.out.printf("%10s %10s %5s %10d %6s \n", id, CADate, CANick, CAAmount, category);	
		}

		public String getPayBankNick() {
			return payBankNick;
		}

		public void setPayBankNick(String payBankNick) {
			this.payBankNick = payBankNick;
		}

		
		
}	
