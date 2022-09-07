package service;

import java.util.Scanner;

import mjproject.AuthenException;
import mjproject.CADException;
import mjproject.CADTO;
import repository.CADAO;

public class Money {
	
	public int inputMoney (String id, int i) {
		
		Scanner sc = new Scanner(System.in);
		CADException cad = new CADException();
		
		boolean date = true;
		boolean won = true;
		boolean category = true;
		int x;
		int result = 0;
		
		try {
			
			CADAO dao = new CADAO();
			CADTO dto = new CADTO();
			
			dto.setId(id);
			
			do {
				try {
					
					System.out.print("날짜: ");
					dto.setCADate(sc.next());
					cad.dateCheck(dto.getCADate());
					
					date = false;
					
				} catch (AuthenException e) {
					System.out.println(e.toString());
				}
			} while (date);
			
			
			do {
				try {
					
					System.out.print("금액: ");
					x = cad.numberCheck(sc.next());
					dto.setCAAmount(x);
					
					won = false;
					
				} catch (AuthenException e) {
					System.out.println(e.toString());
				}
			} while (won);
			
			if (i==1 || i==4) {
				System.out.println("카테고리 : [생활비] [식대] [교통] [쇼핑] [문화] [미용]");
			} else {
				System.out.println("카테고리 : [월급] [생활비] [식대] [교통] [쇼핑] [문화] [미용]");
			}
			
			
			do {
				try {
					
					System.out.print("카테고리: ");
					dto.setCategory(sc.next());
					cad.categoryCheck(dto.getCategory());
					
					category = false;
					
				} catch (AuthenException e) {
					System.out.println(e.toString());
				}
			} while (category);
			
			
			System.out.print("계좌/카드 별칭: ");
			dto.setCANick(sc.next());
			
			if(i==1)
				result = dao.deleteOutCardCA(dto);
			else if(i==2)
				result = dao.deleteInAccountCA(dto);
			else if(i==3)
				result = dao.deleteOutAccountCA(dto);
			else if(i==4)
				result = dao.outputCardCA(dto);
			else if(i==5)
				result = dao.outputAccountCA(dto);
			else
				result = dao.inputAccountCA(dto);
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}
}
