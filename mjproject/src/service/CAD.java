package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import mjproject.AuthenException;
import mjproject.CADException;
import mjproject.CADTO;
import repository.CADAO;
import resource.Picture;

public class CAD implements ICAD {
	
	Scanner sc = new Scanner(System.in);
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	CADAO dao = new CADAO();
	CADException cad = new CADException();
	Money money = new Money();
	Picture pic = new Picture();
	
	//0. 초기화
	@Override
	public void init(String id) {
		dao.init(id);
	}
	
	//1. 가계부 소개
	@Override
	public void CAInformation() {
		
		System.out.println("지출 내역을 기록하고 예산 대비 얼마나 사용했는지 확인해 보세요.\r"
				+ "돈을 어디에 얼마나 쓰는지 항상 확인하면 더 이상 금전 문제를 겪을 일이 없어요.\r"
				+ "예산 목표를 맞출 수 있도록 스스로 상기시켜 보세요.");
		System.out.println();
		pic.main2();
		
	}
	
	//2. 회원가입
	@Override
	public void insertMember() throws AuthenException {
		
		String spw = null;
		String str = null;
		boolean id = true;
		boolean pw = true;
		boolean name = true;
		
		System.out.println("-----------------------------------------------------------------------------------------------------");
		System.out.println("             				        회원가입");
		System.out.println("-----------------------------------------------------------------------------------------------------");
		
		try {
			
			CADTO dto = new CADTO();
			
			//아이디
			do {
				try {
					System.out.print("아이디 : ");
					str = (sc.next());
					cad.idFormat(str);
					cad.idDuplicateCheck(str);
					
					id = false;
					
					dto.setId(str);
					
				} catch (AuthenException e) {
					System.out.println(e.toString());
				}
 				
			} while (id);
			
			//비밀번호
			do {
				try {
					System.out.print("비밀번호 : ");
					dto.setPw(sc.next());
					
					System.out.print("비밀번호 확인 : ");
					spw = sc.next();
					cad.pwCheck(dto.getPw(), spw);
					
					pw = false;
					
				} catch (AuthenException e) {
					System.out.println(e.toString());
				}
				
			} while (pw);
			
			//이름
			do {
				try {
					System.out.print("이름 : ");
					str = sc.next();
					cad.nameCheck(str);
					
					name = false;
					dto.setName(str);
					
				} catch (AuthenException e) {
					System.out.println(e.toString());
				}
			} while (name);
			
			//체크 후 가입
			int result = dao.insertMember(dto);
			
			if (result == 1) {
				System.out.println();
				System.out.println("****성공적으로 가입이 되었습니다!****");
				System.out.println();
			} else {
				System.out.println("회원가입에 실패했습니다.");
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	//3. 로그인
	@Override
	public String login() {
		
		String id;
		String pw;
		String checkid = null;
		
		try {
			System.out.print("아이디 : ");
			id = sc.next();
			
			System.out.print("비밀번호 : ");
			pw = sc.next();
			
			checkid = dao.login(id, pw);
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return checkid;
		}
		return checkid;
	}

	//4. 회원탈퇴
	@Override
	public void deleteMember() {

		try {
			String id, pw;
			
			System.out.println("삭제할 정보를 입력하세요.");
			System.out.print("id: ");
			id = sc.next();
			System.out.print("pw: ");
			pw = sc.next();
			
			int result = dao.deleteMember(id, pw);
			
			if (result == 1) {
				System.out.println("회원탈퇴가 성공적으로 진행되었습니다!");
			} else {
				System.out.println("회원탈퇴가 실패했습니다! 회원정보를 확인해주세요.");
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}
	
	//5. 계좌/카드 등록
	@Override
	public void insertAsset(String id) throws AuthenException {
		
		System.out.println("-----------------------------------------------------------------------------------------------------");
		System.out.println("                                        ◈카드/계좌 등록◈");
		System.out.println("-----------------------------------------------------------------------------------------------------");
		
		try {
			
			int result = 0;
			int BC = 0;
			int y = 0;
			String str = null;
			
			boolean accDiv = true;
			boolean accno = true;
			boolean cardno = true;
			boolean accAmount = true;
			boolean nickname = true;
			boolean payBank = true;
			boolean payDate = true;
			
			CADTO dto = new CADTO();
			
			do {
				System.out
				.println("계좌 등록을 원하시면 [1]을, 카드 등록을 원하시면 [2]를 입력해주세요.");
				System.out.print("▶");
				BC = sc.nextInt();
				
			} while (BC < 1 || BC > 2);
				
			System.out.println();
			
			// 계좌 등록
			if (BC == 1) {
				System.out.println("등록하시는 계좌의 유형을 입력해주세요.");
				System.out.println("유형은 [입출금],[예/적금] 두가지가 가능합니다.");
				
				do {
					try {
						System.out.print("▶");
						str = sc.next();
						cad.accdivCheck(str);
						
						accDiv = false;
						dto.setAccDiv(str);
						
					} catch (AuthenException e) {
						System.out.println(e.toString());
					}
				} while (accDiv);
				
			System.out.println();
			
			System.out.println("은행을 입력해주세요");
			System.out.print("▶");
			dto.setBank(sc.next());
			
			System.out.println();
			
			System.out.println("예금주를 입력해주세요");
			System.out.print("▶");
			dto.setAccName(sc.next());
			
			System.out.println();
			
			System.out.println("계좌번호를 입력해주세요.");
			System.out.println("계좌번호는 - 없이 숫자로만 입력 가능합니다.");
			
			do {
				try {
					System.out.print("▶");
					str = cad.numberCheck2(sc.next());
					
					dto.setAccNo(str);
					accno = false;
					
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			} while (accno);
				
			System.out.println();
			
			System.out.println("현재 계좌에 있는 금액을 입력해주세요.");
			System.out.println("금액은 숫자로만 입력 가능합니다.");
			
			do {
				try {
					System.out.print("▶");
					y = cad.numberCheck(sc.next());
					
					dto.setAccAmount(y);
					accAmount = false;
					
				} catch (Exception e) {
					System.out.println(e.toString());
				}
				
			} while (accAmount);
			
			System.out.println();
			
			do {
				try {
					System.out.println("계좌 간편이름을 작성해주세요.");
					System.out.print("▶");
					
					str = sc.next();
					cad.accNickCheck(str);
					
					nickname = false;
					dto.setBankNick(str);
					
				} catch (AuthenException e) {
					System.out.println(e.toString());
				} 
				
			} while (nickname);
				
				result = dao.insertAsset(dto, id, BC);
				
				if (result == 1) {
					System.out.println();
					System.out.println("****성공적으로 계좌등록이 되었습니다!****");
					System.out.println();
				} else {
					System.out.println("계좌등록에 실패했습니다.");
				}
			
			// 카드 등록
			} else if (BC == 2) {
				System.out.println("카드번호를 입력해주세요.");
				System.out.println("카드번호는 - 없이 숫자로만 입력 가능합니다.");
				
				do {
					try {
						System.out.print("▶");
						str = cad.numberCheck2(sc.next());
						
						dto.setCardNo(str);
						cardno = false;
						
					} catch (Exception e) {
						System.out.println(e.toString());
					}
				} while (cardno);
				
				System.out.println();

				System.out.println("카드사를 입력해주세요");
				System.out.print("▶");
				dto.setCard(sc.next());
				
				System.out.println();
				
				System.out.println("명의주를 입력해주세요");
				System.out.print("▶");
				dto.setCardName(sc.next());
				
				System.out.println();
				
				do {
					try {
						System.out.println("결제통장 간편이름을 입력해주세요");
						System.out.print("▶");
						
						str = cad.payBankCheck(sc.next());
						System.out.println();
						
						dto.setPayBankNick(str);
						payBank = false;
						
					} catch (AuthenException e) {
						System.out.println(e.toString());
					}
				} while (payBank);
				
				do {
					try {
						System.out.println("결제일을 입력해주세요 [5, 10, 15, 20, 25]일만 입력 가능합니다.");
						System.out.print("▶");
						
						str = ""+cad.payDateCheck(sc.nextInt());
						System.out.println();
						
						dto.setPayDate(str);
						payDate = false;
						
					} catch (AuthenException e) {
						System.out.println(e.toString());
					}
				} while (payDate);
				
				do {
					try {
						System.out.println("카드 간편이름을 작성해주세요");
						System.out.print("▶");
						str = sc.next();
						cad.accNickCheck(str);
						
						nickname = false;
						dto.setCardNick(str);
						
					} catch (AuthenException e) {
						System.out.println(e.toString());
					}
				} while (nickname);
				
				result = dao.insertAsset(dto, id, BC);
				
				if (result == 1) {
					System.out.println();
					System.out.println("****성공적으로 카드등록이 되었습니다!****");
					System.out.println();
				} else {
					System.out.println("카드등록에 실패했습니다");
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	//6. 계좌/카드 삭제
	@Override
	public void deleteAsset(String id) {
		
		try {
			String CAname;
			String CAno;
			int BC = 0;
			int result = 0;
			
			do {
				System.out
				.println("계좌 삭제를 원하시면 [1]을, 카드 삭제를 원하시면 [2]를 입력해주세요.");
				System.out.print("▶");
				BC = sc.nextInt();
				
			} while (BC < 1 || BC > 2);
			
			System.out.println("삭제할 정보를 입력하세요.");
			System.out.print("사용하는 카드/계좌 은행: ");
			CAname = sc.next();
			
			System.out.print("사용하는 카드/계좌 번호: ");
			CAno = sc.next();
			
			result = dao.deleteAsset(id, CAname, CAno, BC);
			
			if (result == 1) {
				System.out.println("삭제가 완료되었습니다!");
			} else {
				System.out.println("삭제를 실패했습니다! 계좌/카드 정보를 확인해주세요.");
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	//7. 계좌/카드 조회
	@Override
	public void selectAsset(String id) {

		System.out.println("   카드/계좌	        은행            예금주                 계좌/카드번호           금액         간편이름       결제계좌닉네임          결제일");
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------");
		
		// 계좌정보 출력
		List<CADTO> lists = dao.selectAsset(id, 1);
		Iterator<CADTO> it = lists.iterator();
		
		while (it.hasNext()) {
			CADTO dto = it.next();
			dto.printAcc();
		}
		
		// 카드정보 출력
		lists = dao.selectAsset(id, 2);
		it = lists.iterator();
		
		while (it.hasNext()) {
			CADTO dto = it.next();
			dto.printCard();
		}
		System.out.println();
	}
	
	//8. 가계부 조회 
	@Override
	public void selectaccount(String id) {
		
		int ch = 0;
		int cal = 0;
		String date1 = "";
		String date2 = "";
		String category = "";
		boolean categoryCheck = true;
		boolean flag = true;
		
		try {
			do {
				System.out.println("가계부 조회 유형을 선택하세요.");
				System.out.println("1.일별 2.기간별 3.월별 4.유형별 5.달력 ");
				System.out.print("입력 ▶ ");
				ch = sc.nextInt();
				
				//일별
				if (ch == 1) {
					do {
						try {
							System.out.print("조회할 일자를 입력하세요: ");
							date1 = sc.next();
							cad.dateCheck(date1);
							dao.selectDay(id, date1);
							flag = false;
							
						} catch (Exception e) {
							System.out.println(e.toString());
						}
					} while (flag);
					
				//기간별	
				} else if (ch == 2) {
					do {
						try {
							System.out.print("조회할 기간을 입력하세요. ");
							
							System.out.print("시작일 : ");
							date1 = sc.next();
							cad.dateCheck(date1);
							
							System.out.print("종료일 : ");
							date2 = sc.next();
							cad.dateCheck(date2);
							
							dao.selectDate(id, date1, date2);
							flag = false;
							
						} catch (Exception e) {
							System.out.println(e.toString());
						}
					} while (flag);
					
				//월별	
				} else if (ch == 3) {
					
					do {
						try {
							System.out.print("조회할 년/월을 입력하세요.");
							
							System.out.print("연도 : ");
							date1 = sc.next();
							cad.yearCheck(date1);
							
							System.out.print("월 : ");
							date2 = sc.next();
							cad.monthCheck(date2);
							
							flag = false;
							dao.selectMonth(id, date1, date2);
							
						} catch (Exception e) {
							System.out.println(e.toString());
						}
					} while (flag);
					
				//카테고리별
				} else if (ch == 4) {
					
					System.out.print("조회할 카테고리를 입력하세요.");
					System.out.println("카테고리 : [자기계발] [생활비] [식대] [교육] [문화] [미용] [교통] [쇼핑] [월급]");
					
					do {
						try {
							System.out.print("카테고리 : ");
							category = sc.next();
							
							cad.categoryCheck(category);
							categoryCheck = false;
							
							dao.selectCategory(id, category);
							
						} catch (AuthenException e) {
							System.out.println(e.toString());
						}
						
					} while (categoryCheck);
					
					//달력	
				} else if (ch == 5) {
					
					do {
						try {
							System.out.print("달력을 조회할 년도와 월을 입력하세요.");
							
							System.out.print("연도 : ");
							date1 = sc.next();
							cad.yearCheck(date1);
							
							System.out.print("월 : ");
							date2 = sc.next();
							cad.monthCheck(date2);
							
							System.out.print("입출금 달력 중 무엇을 조회하시겠습니까? [1:입금], [2:출금], [3:메인화면으로 돌아가기] ");
							cal = sc.nextInt();
							
							if (cal == 1) {
								dao.selectInputCalendar(id, date1, date2); 
								break;
								
							} else if (cal == 2) {
								dao.selectOutputCalendar(id, date1, date2);
								break;
								
							} else if (cal == 3) {
								break;
								
							} else {
								System.out.println("다시 입력하여 주세요.");
							}
						
						} catch (Exception e) {
							System.out.println(e.toString());
						}
					} while (true);
				}
				
			} while (ch > 5 || ch < 1);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	//9. 가계부 삭제
	@Override
	public void deleteCAData(String id) {

		int se1, se2;
		int result;
		
		do {
			System.out
			.println("카드사용 가계부 삭제를 원하시면 [1]을, 계좌사용 가계부 삭제를 원하시면 [2]을,");
			System.out.println("가계부 삭제를 취소하시려면 [3]을 입력해주세요.");
			System.out.print("▶");
			se1 = sc.nextInt();
			
		} while (se1 < 1 || se1 > 3);
		
		System.out.println(); 
		
		//카드 사용 삭제 
		if (se1 == 1) {
			
			result = money.inputMoney(id, 1);
			
			if (result != 0) {
				System.out.println("카드 사용 가계부 삭제 완료!");
			} else {
				System.out.println("해당 내용으로 작성된 가계부가 존재하지 않습니다.");
			}
		
		//계좌 사용 삭제
		} else if (se1 == 2) {
			
			do {
				System.out.println("계좌입금 내역 가계부 삭제를 원하시면 [1]을, 계좌출금 가계부 삭제를 원하시면 [2]을,");
				System.out.println("가계부 삭제를 취소하시려면 [3]을 입력해주세요.");
				System.out.print("▶");
				se2 = sc.nextInt();
			} while (se2 < 1 || se2 > 3);
			
			//입금 삭제
			if (se2 == 1) {
				
				result = money.inputMoney(id, 2);
				if (result != 0) {
					System.out.println("삭제 완료!");
				} else {
					System.out.println("해당 내용으로 작성된 가계부가 존재하지 않습니다.");
				}
				
			//출금 삭제
			} else if (se2 == 2) {
				result = money.inputMoney(id, 3);
				if (result != 0) {
					System.out.println("삭제 완료!!");
				} else {
					System.out.println("해당 내용으로 작성된 가계부가 존재하지 않습니다.");
				}
				
			//가계부 삭제 취소	
			} else if (se2 == 3) {
				System.out.println("삭제가 취소되었습니다.");
			}
				
		//삭제 나가기
		} else if (se1 == 3) {
			System.out.println("삭제가 취소되었습니다.");
		}
	}
	
	//10. 가계부 작성
	@Override
	public void inputCAData(String id) {
		
		int se1, se2;
		int result;
		
		do {
			System.out.println("지출 가계부를 원하시면 [1]을, 수입 가계부를 원하시면 [2]을,");
			System.out.println("가계부 입력을 취소하시려면 [3]을 입력해주세요.");
			System.out.print("▶");
			se1 = sc.nextInt();
			
		} while (se1 < 1 || se1 > 3);
		
		System.out.println();
		
		//지출
		if (se1 == 1) {
			
			do {
				System.out.println("카드 지출을 원하시면 [1]을, 계좌 지출을 원하시면 [2]을,");
				System.out.println("가계부 입력을 취소하시려면 [3]을 입력해주세요.");
				System.out.print("▶");
				se2 = sc.nextInt();
			} while (se2 < 1 || se2 > 3);
			
			//카드지출
			if (se2 == 1) {
				
				result = money.inputMoney(id, 4);
				
				if (result == 2) {
					System.out.println("작성 완료!");
				} else {
					System.out.println("작성 실패!");
				}
				
			//계좌지출	
			} else if (se2 == 2) {
				
				result = money.inputMoney(id, 5);
				
				if (result == 2) {
					System.out.println("작성 완료!");
				} else {
					System.out.println("작성 실패!");
				}
			}
			
		//수입	
		} else if (se1 == 2) {
			
			result = money.inputMoney(id, 6);
			
			if (result == 2) {
				System.out.println("작성 완료!");
			} else {
				System.out.println("작성 실패!");
			}
		} 
	}

	//11. 계좌 이체
	@Override
	public void changeAsset(String id) {

		try {
			
			String out;
			String in;
			int money;
			
			System.out.print("출금통장 : ");
			out = sc.next();
			
			System.out.print("입금통장 : ");
			in = sc.next();
			
			System.out.println("금액 : ");
			money = sc.nextInt();
			
			int result = dao.changeAsset(id, out, in, money);
			
			if (result == 4) {
				System.out.println("계좌이체에 성공하셨습니다.");
			} else {
				System.out.println("계좌이체에 실패하셨습니다. 통장정보를 확인해주세요.");
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	
}
