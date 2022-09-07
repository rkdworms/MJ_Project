package mjproject;

import java.sql.Date;
import java.util.List;
import java.util.regex.Pattern;

import repository.CADAO;

public class CADException {
	
	CADAO dao = new CADAO();
	
	//아이디 형식 확인
	public void idFormat(String id) throws AuthenException {
		
		if (id.length() < 3 || id.length() > 10) {
			throw new AuthenException("3~10자 이내의 아이디만 입력 가능합니다.\n");
		}
		
		int cntAlpha = 0;
		int cntNum = 0;
		
		for (int i=0; i<id.length(); i++) {
			
			char alpha = id.charAt(i);
			
			if (('a' <= alpha && alpha <= 'z') || ('A' <= alpha && alpha <= 'Z')) {
				++cntAlpha;
			} else if ('0' <= alpha && alpha <= '9') {
				++cntNum;
			}
		}
		
		if (cntAlpha == 0 || cntNum == 0) {
			throw new AuthenException("영문 혼합 아이디만 입력 가능합니다.\n");
		}
	}

	// 아이디 중복 확인
	public void idDuplicateCheck(String id) throws AuthenException {

		List<CADTO> lists = dao.getId();

		for (int i = 0; i < lists.size(); i++) {
			
			if (id.equals(lists.get(i).getId())) {
				throw new AuthenException("중복된 아이디가 있습니다. 아이디를 다시 입력해주세요.\n");
			}
		}
	}

	//비밀번호 형식 확인
	public void pwCheck(String pw1, String pw2) throws AuthenException {
		
		if (pw1.length() < 5 || pw1.length() > 20) {
			throw new AuthenException("5~20자 이내의 비밀번호만 입력 가능합니다.\n");
		}
		
		int cntEng = 0;
		int cntNum = 0;
		
		for (int i=0; i<pw1.length(); i++) {
			char pw = pw1.charAt(i);
			
			if (('a' <= pw && pw <= 'z')||('A' <= pw && pw <= 'Z')) {
				++cntEng;
			} else if ('0' <= pw && pw <= '9') {
				++cntNum;
			}
		}
		
		if (cntEng == 0 || cntNum == 0) {
			throw new AuthenException("영문 혼합 비밀번호만 입력 가능합니다.\n");
		}
		
		if (!pw1.equals(pw2)) {
			throw new AuthenException("비밀번호가 다릅니다.\n");
		}
	}
	
	//이름 형식 확인
	public void nameCheck(String name) throws AuthenException {
		
		boolean check = Pattern.matches("^[가-힣]*$", name);
		
		if (!check) {
			throw new AuthenException("이름은 한글로만 입력 가능합니다.\n");
		}
	}
	
	//입출금, 예/적금만 입력 가능
	public void accdivCheck(String accdiv) throws AuthenException {
		
		if (!accdiv.equals("입출금") && !accdiv.equals("예/적금")) {
			throw new AuthenException("입력은 [입출금], [예/적금]으로만 입력 가능합니다.\n");
		}
	}
	
	//계좌/카드 간편이름 형식 확인
	public void accNickCheck(String accNick) throws AuthenException {

		
		if (1 > accNick.length() || accNick.length() > 10) {
			throw new AuthenException("간편 이름은 10자 이내로만 입력 가능합니다.\n");
		}
		
		boolean check = Pattern.matches("^[가-힣]*$", accNick);
		
		if (!check) {
			throw new AuthenException("간편 이름은 한글로만 입력 가능합니다.\n");
		}
	}

	//금액은 숫자만 입력 가능
	public int numberCheck(String number) throws AuthenException {
		
		boolean check = number.matches("^[0-9]*$");
		
		if (!check) {
			throw new AuthenException("금액은 숫자로만 입력 가능합니다.\n");
		} else {
			return Integer.parseInt(number);
		}
	}

	//계좌/카드 번호는 숫자만 입력 가능
	public String numberCheck2(String number) throws AuthenException {
		
		boolean check = number.matches("^[0-9]*$");
		
		if (!check) {
			throw new AuthenException("계좌/카드번호는 숫자로만 입력 가능합니다.\n");
		} else {
			return number;
		}
	}
	
	//카드 결제통장 간편이름 형식 확인
	public String payBankCheck(String nick) throws AuthenException {
		
		List<CADTO> lists = dao.getAccNick();
		
		boolean check = true;
		
		for (int i = 0; i<lists.size(); i++) {
			
			if (nick.equals(lists.get(i).getBankNick())) {
				check = false;
				break;
			} 
		}
		
		if(check) {
			throw new AuthenException("등록된 계좌 간편이름이 없습니다.\n");
		}
		
		return nick;
	}
	
	//카드 결제일 형식 확인
	public int payDateCheck(int day) throws AuthenException {

		if ( day != 5 && day != 10 && day != 15 && day != 20 && day != 25 ) {
			throw new AuthenException("결제일은 [5, 10, 15, 20, 25] 일만 선택 가능합니다.");
		} 
		
		return day;
	}
	
	//카테고리 입력 확인
	public void categoryCheck(String category) throws AuthenException {
		
		String check[] = {"자기계발", "생활비", "식대", "교육", "문화", "미용", "교통", "쇼핑", "월급"};
		
		boolean ch = false;
		
		for (int i=0; i<check.length; i++) {
			
			if (category.equals(check[i])) {
				ch = true;	
			}
		}
		
		if (!ch) {
			throw new AuthenException("카테고리 : [자기계발] [생활비] [식대] [교육] [문화] [미용] [교통] [쇼핑] [월급]\n");
		}
		
	}
	
	//날짜 확인
	public void dateCheck(String date) throws AuthenException {
		
		boolean check = Pattern.matches("(\\d{4})-(\\d{2})-(\\d{2})", date);
		
		if (!check) {
			throw new AuthenException("날짜 입력 형식은 [YYYY-MM-DD]입니다.\n");
		}
	}

	//연도 확인
	public void yearCheck(String year) throws AuthenException {
		
		boolean check = Pattern.matches("(\\d{4})", year);
		
		if (!check) {
			throw new AuthenException("연도를 정확히 입력해주세요.\n");
		}
	}
	
	//월 확인 
	public void monthCheck(String month) throws AuthenException {
		
		boolean check = Pattern.matches("01|02|03|04|05|06|07|08|09|10|11|12", month);
		
		if (!check) {
			throw new AuthenException("월을 정확히 입력해주세요. 입력 형식은 1월의 경우 [01]입니다.\n");
		}
	}

	
}
