package repository;

import java.util.List;

import mjproject.CADTO;

public interface ICADAO {
		
	//초기화
	public void init(String id);
	
	//회원가입
	public int insertMember(CADTO dto);
	
	//아이디 불러오기
	public List<CADTO> getId();
	
	//로그인
	public String login(String id, String pw);
	
	//회원탈퇴
	public int deleteMember(String id, String pw);
	
	//계좌/카드 등록
	public int insertAsset(CADTO dto, String id, int BC);
	
	//계좌/카드 삭제
	public int deleteAsset(String id, String CAname, String CAno, int BC);
	
	//계좌/카드 조회
	public List<CADTO> selectAsset(String id, int BC);
	
	//계좌 닉네임 불러오기
	public List<CADTO> getAccNick();
	
	//가계부 조회 - 일별
	public void selectDay(String id, String date);
	
	//가계부 조회 - 기간별
	public void selectDate(String id, String date1, String date2);
	
	//가계부 조회 - 월별
	public void selectMonth(String id, String year, String month);
	
	//가계부 조회 - 카테고리별
	public void selectCategory(String id, String category);
	
	//가계부 조회 - 입금달력
	public void selectInputCalendar(String id, String year, String month);
	
	//가계부 조회 - 출금달력
	public void selectOutputCalendar(String id, String year, String month);
	
	//지출 입력 - 카드
	public int outputCardCA(CADTO dto);
	
	//지출 입력 - 계좌
	public int outputAccountCA(CADTO dto);
	
	//입금 입력 - 계좌
	public int inputAccountCA(CADTO dto);
	
	//지출 내역 삭제 - 계좌
	public int deleteOutAccountCA(CADTO dto);
	
	//입금 내역 삭제 - 계좌
	public int deleteInAccountCA(CADTO dto);
	
	//지출 내역 삭제 - 카드
	public int deleteOutCardCA(CADTO dto);
	
	//계좌이체
	public int changeAsset(String id, String str, String str2, int money);
}
