package service;

import mjproject.AuthenException;
import mjproject.CADTO;

public interface ICAD {
	
	//0. 초기화
	public void init(String id);
	
	//1. 가계부 소개
	public void CAInformation();
	
	//2. 회원가입
	public void insertMember() throws AuthenException;
	
	//3. 로그인
	public String login();
	
	//4. 회원탈퇴
	public void deleteMember();
	
	//5. 계좌/카드 등록
	public void insertAsset(String id) throws AuthenException;
	
	//6. 계좌/카드 삭제
	public void deleteAsset(String id);
	
	//7. 계좌/카드 조회
	public void selectAsset(String id);
	
	//8. 가계부 조회
	public void selectaccount(String id);
	
	//9. 가계부 삭제
	public void deleteCAData (String id);
	
	//10. 가계부 입력
	public void inputCAData (String id);
	
	//11. 계좌이체
	public void changeAsset (String id);

}
