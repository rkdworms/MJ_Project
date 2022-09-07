package controller;

import java.util.Scanner;

import db.DBconn;
import resource.Ending;
import service.CAD;

public class CADMain {
	public static void main(String[] args) {
		
		DBconn.connect();
		
		Scanner sc = new Scanner(System.in);
		int ch;
		CAD cad = new CAD();
		
		try {
			
			while (true) {
				
				do {
					System.out.println("-------------------------------------------------------------------------------------");
					System.out.println("      		                          가계부  ");
					System.out.println("	1.가계부 소개        2.회원가입        3.로그인         4.회원탈퇴         5.종료  ");
					System.out.println("-------------------------------------------------------------------------------------");
					System.out.print("▶");
					ch = sc.nextInt();
					
				} while (ch < 1 || ch > 5);
				
				if (ch == 1) {
					cad.CAInformation();
					continue;
					
				} else if (ch == 2) {
					cad.insertMember();
					continue;
					
				} else if (ch == 3) {
					CADLogin.login();
					continue;
					
				} else if (ch == 4) {
					cad.deleteMember();
					continue;
					
				} else if (ch == 5) {
					DBconn.close();
					Ending.main(args);
					System.exit(0);
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
