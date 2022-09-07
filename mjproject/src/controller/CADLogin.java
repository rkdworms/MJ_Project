package controller;

import java.util.Scanner;

import mjproject.AuthenException;
import service.CAD;

public class CADLogin {
	public static void login() throws AuthenException {
		
		Scanner sc = new Scanner(System.in);
		
		CAD cad = new CAD();
		
		int ch;
		
		while(true) {
			
			String id = cad.login();
			cad.init(id);
			
			while(!id.equals("")) {
				
				do {
					System.out.println("------------------------------------------------------------------------------------------");
					System.out.println("	1.계좌/카드 등록         2.계좌/카드 삭제            3.계좌/카드 조회          　4.계좌이체             ");
					System.out.println("	5.가계부 작성           6.가계부 삭제　             7.가계부 조회      　　   　 8.로그아웃   ");
					System.out.println("------------------------------------------------------------------------------------------");
					System.out.print("▶");
					ch = sc.nextInt();
					
				} while (ch<1 || ch>8);
				
				System.out.println();
				
				if(ch == 1) {
					cad.insertAsset(id);
					
				} else if (ch == 2) {
					cad.deleteAsset(id);
					
				} else if (ch == 3) {
					cad.selectAsset(id);
					
				} else if (ch == 4) {
					cad.changeAsset(id);
					
				} else if (ch == 5) {
					cad.inputCAData(id);
					
				} else if (ch == 6) {
					cad.deleteCAData(id);
					
				} else if (ch == 7) {
					cad.selectaccount(id);
					
				} else if (ch == 8) {
					return;
				}	
			}
			System.out.println("로그인에 실패하였습니다.");
			return;
		}
		
	}
}
