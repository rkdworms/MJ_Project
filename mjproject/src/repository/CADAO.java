package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import db.DBconn;
import mjproject.CADTO;

public class CADAO implements ICADAO {
	
	//초기화
	@Override
	public void init(String id) {
		
		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql;
		
		try {
			
			sql = "update account as a, card as c, (select sum(cardamount) as sumaccount, cdata.paybanknick as paybanknick from"
					+ "												(select c.cardamount as cardamount , c.paybanknick as paybanknick"
					+ "												  from account as a, card as c"
					+ "												  where c.id= ?"
					+ "												  and c.paybanknick = a.nickname"
					+ "											      and ( c.paydate <= now()) ) as cdata"
					+ "													group by cdata.paybanknick) as cagroup"
					+ "   set a.accamount = a.accamount - cagroup.sumaccount,"
					+ "	   c.paydate = date_add(paydate, INTERVAL 1 MONTH),"
					+ "       c.cardamount = 0"
					+ "         where c.id= ?"
					+ "				and cagroup.paybanknick = a.nickname"
					+ "				and ( c.paydate <= now() )";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, id);
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		DBconn.close(pstmt, rs);
	}
	
	//회원가입
	@Override
	public int insertMember(CADTO dto) {

		int result = 0;
		
		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql;
		
		try {
			sql = "insert into member (id, pw, name, inputdate) values (?, ?, ?, now())";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getPw());
			pstmt.setString(3, dto.getName());
			
			result = pstmt.executeUpdate();
			
			DBconn.close(pstmt, rs);
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}
	
	//아이디 불러오기
	@Override
	public List<CADTO> getId() {

		List<CADTO> result = null;
		CADTO dto = null;

		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql;

		try {

			sql = "select id from member";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs != null) {

				result = new ArrayList<>();

				while (rs.next()) {

					dto = new CADTO();

					dto.setId(rs.getString("id"));

					result.add(dto);
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		DBconn.close(pstmt, rs);
		return result;
	}

	//로그인
	@Override
	public String login(String id, String pw) {

		String login = null;
		
		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql;
		
		try {
			
			sql = "select id, pw from member where id=? and pw=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {	
				login = id;
			} else {
				login = "";
			}
			
			DBconn.close(pstmt, rs);
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return login;
		}
		return login;
	}

	//회원탈퇴
	@Override
	public int deleteMember(String id, String pw) {

		int result = 0;

		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql;

		try {

			sql = "delete from member where id=? and pw=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);

			result = pstmt.executeUpdate();

			DBconn.close(pstmt, rs);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}

	//계좌/카드 등록
	@Override
	public int insertAsset(CADTO dto, String id, int BC) {

		int resultNick = 0;
		int result = 0;

		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql;

		try {

			//계좌등록
			if (BC == 1) {

				sql = "select * from account where id=? and nickname=?";

				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				pstmt.setString(2, dto.getBankNick());

				rs = pstmt.executeQuery();

				if (rs.next()) {
					
					System.out.println();
					System.out.println("이미 존재하는 간편이름입니다. 다시 입력해주세요.");
					System.out.println();

				} else {
					
					sql = "insert into account (id, accdiv, bank, accname, accno, accamount, nickname)"
							+ " values (?,?,?,?,?,?,?)";

					pstmt = conn.prepareStatement(sql);

					pstmt.setString(1, id);
					pstmt.setString(2, dto.getAccDiv());
					pstmt.setString(3, dto.getBank());
					pstmt.setString(4, dto.getAccName());
					pstmt.setString(5, dto.getAccNo());
					pstmt.setInt(6, dto.getAccAmount());
					pstmt.setString(7, dto.getBankNick());

					result = pstmt.executeUpdate();

				}

			//카드등록
			} else if (BC == 2) {

				sql = "select * from card where id=? and nickname=?";

				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				pstmt.setString(2, dto.getBankNick());

				rs = pstmt.executeQuery();

				if (rs.next()) {
					
					System.out.println();
					System.out.println("이미 존재하는 간편이름입니다. 다시 입력해주세요.");
					System.out.println();

				} else {
					
					sql = "insert into card (id, card, cardname, cardno, cardamount, paydate, paybanknick, nickname)"
							+ " values (?, ?, ?, ?, 0,"
							+ " (select concat ((date_format(now(), '%Y-%m-')), ? )),"
							+ " ?, ?)";

					pstmt = conn.prepareStatement(sql);

					pstmt.setString(1, id);
					pstmt.setString(2, dto.getCard());
					pstmt.setString(3, dto.getCardName());
					pstmt.setString(4, dto.getCardNo());
					pstmt.setString(5, dto.getPayDate());
					pstmt.setString(6, dto.getPayBankNick());
					pstmt.setString(7, dto.getCardNick());
					
					result = pstmt.executeUpdate();
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		DBconn.close(pstmt, rs);
		return result;
	}

	//계좌/카드 삭제
	@Override
	public int deleteAsset(String id, String CAname, String CAno, int BC) {
		
		int result = 0;
		
		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql ="";
		
		try {
			
			//계좌 삭제
			if (BC == 1) {
				sql = "delete from account where id=? and bank=? and accno=?";
				
			//카드 삭제	
			} else if (BC == 2) {
				sql = "delete from card where id=? and card=? and cardno=?";
			}	
			
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, id);
				pstmt.setString(2, CAname);
				pstmt.setString(3, CAno);
				
				result = pstmt.executeUpdate();
				
				DBconn.close(pstmt, rs);
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}

	//계좌/카드 조회
	@Override
	public List<CADTO> selectAsset(String id, int BC) {
		
		List<CADTO> lists = new ArrayList<CADTO>();
		
		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql;
		CADTO dto;
		
		try {
			
			//계좌 조회
			if (BC == 1) {
			
				sql = "select * from account where id=?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					
					dto = new CADTO();
					
					dto.setAccDiv(rs.getString("accdiv"));
					dto.setBank(rs.getString("bank"));
					dto.setAccName(rs.getString("accname"));
					dto.setAccNo(rs.getString("accno"));
					dto.setAccAmount(rs.getInt("accamount"));
					dto.setBankNick(rs.getString("nickname"));
					
					lists.add(dto);
				}
				
			//카드 조회	
			} else if (BC == 2) {
				
				sql = "select * from card where id=?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					
					dto = new CADTO();
					
					dto.setCard(rs.getString("card"));
					dto.setCardName(rs.getString("cardname"));
					dto.setCardNo(rs.getString("cardno"));
					dto.setCardAmount(rs.getInt("cardamount"));
					dto.setCardNick(rs.getString("nickname"));
					dto.setPayBankNick(rs.getString("paybanknick"));
					dto.setPayDate(rs.getString("paydate"));
					
					lists.add(dto);
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		DBconn.close(pstmt, rs);
		return lists;	
	}
	
	//계좌 닉네임 불러오기
	public List<CADTO> getAccNick() {
		
		List<CADTO> result = null;
		CADTO dto;
		
		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql;
		
		try {
			
			sql = "select nickname from account";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				
				result = new ArrayList<>();

				while(rs.next()) {
					
					dto = new CADTO();
					dto.setBankNick(rs.getString("nickname"));
					result.add(dto);
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		DBconn.close(pstmt, rs);
		return result;
	}
	 
	//가계부조회 - 일별
	@Override
	public void selectDay(String id, String date) {
		
		List<CADTO> lists= null;
		
		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql;
		CADTO dto;
		
		int deposit = 0;
		int withdraw = 0;
		
		try {
			
			//입출금 내역 전체 조회
			
			//카드 이용 내역
			sql = "(select id, date_format(cadate, '%Y-%m-%d') as cadate, -(amount) as amount, category, nickname"
					+ " from cardwithdraw where id = ? and date_format(cadate, '%Y-%m-%d') = ?";
			sql += " union ";
			//계좌 입금 내역
			sql += "select id, date_format(cadate, '%Y-%m-%d') as cadate, amount, category, nickname" 
					+ " from accountdeposit where id = ? and date_format(cadate, '%Y-%m-%d') = ?";
			sql += " union ";
			//계좌 출금 내역 
			sql += "select id, date_format(cadate, '%Y-%m-%d') as cadate, -(amount) as amount, category, nickname"
					+ " from accountwithdraw where id = ? and date_format(cadate, '%Y-%m-%d') = ?) order by cadate desc";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, date);
			pstmt.setString(3, id);
			pstmt.setString(4, date);
			pstmt.setString(5, id);
			pstmt.setString(6, date);
			
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				
				lists = new ArrayList<CADTO>();
				
				while (rs.next()) {
					
					dto = new CADTO();
					
					dto.setId(rs.getString("id"));
					dto.setCADate(rs.getString("cadate"));
					dto.setCANick(rs.getString("nickname"));
					dto.setCAAmount(rs.getInt("amount"));
					dto.setCategory(rs.getString("category"));
					
					lists.add(dto);
				}
				
			} else {
				
				System.out.println("입력한 입출금 내역이 존재하지 않습니다.");
				DBconn.close(pstmt, rs);
				return;
			}
			
			rs.close();
			pstmt.close();
			
			
			//출금 총액 구하기
			sql = "select -sum(amount) as amount from ("
					+ "	select sum(amount) as amount from cardwithdraw where id = ? and cadate = ?"
					+ "	union all"
					+ "	select sum(amount) as amount from accountwithdraw where id = ? and cadate = ?"
					+ "	) as MJ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, date);
			pstmt.setString(3, id);
			pstmt.setString(4, date);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				withdraw = rs.getInt("amount");
			}
			
			rs.close();
			pstmt.close();
			
			
			//입금 총액 구하기
			sql = "select sum(amount) as amount from accountdeposit where id = ? and cadate = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, date);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				withdraw = rs.getInt("amount");
			}
			
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		if (lists != null) {
			System.out.println("--------------------------------------------------");
			System.out.println("     아이디     결제일     계좌별칭     금  액     카테고리");
			System.out.println("--------------------------------------------------");
			
			Iterator<CADTO> it = lists.iterator();
			while (it.hasNext()) {
				dto = it.next();
				dto.print();
			}
			
			System.out.println("\n------------------------------------------------");
			System.out.println("\t"+date +"의 가계부 총계");
			System.out.println("지출 :" + withdraw);
			System.out.println("수입 :" + deposit);
			System.out.println("증가 자산 :" + (deposit+withdraw));
			System.out.println("------------------------------------------------\n");
		}
		DBconn.close(pstmt, rs);
	}
	
	//가계부조회 - 기간별
	@Override
	public void selectDate(String id, String date1, String date2) {
		
		List<CADTO> lists = null;
		
		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql;
		CADTO dto;
		
		int deposit = 0;
		int withdraw = 0;
		
		try {
			
			//입출금내역 전체조회
			
			//카드 이용 내역
			sql = "(select id, date_format(cadate, '%Y-%m-%d') as cadate, -(amount) as amount, category, nickname from cardwithdraw"
					+ " where id = ? and ? <= date_format(cadate, '%Y-%m-%d') <= ?";
			sql += " union ";
			//계좌 입금 내역
			sql += "select id, date_format(cadate, '%Y-%m-%d') as cadate, amount, category, nickname from accountdeposit"
					+ " where id = ? and ? <= date_format(cadate, '%Y-%m-%d') <= ?";
			sql += " union ";
			//계좌 출금 내역 
			sql += "select id, date_format(cadate, '%Y-%m-%d') as cadate, -(amount) as amount, category, nickname from accountwithdraw"
					+ " where id = ? and ? <= date_format(cadate, '%Y-%m-%d') <= ?) order by cadate desc";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, date1);
			pstmt.setString(3, date2);
			pstmt.setString(4, id);
			pstmt.setString(5, date1);
			pstmt.setString(6, date2);
			pstmt.setString(7, id);
			pstmt.setString(8, date1);
			pstmt.setString(9, date2);
			
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				
				lists = new ArrayList<CADTO>();
				
				while(rs.next()) {
					
					dto = new CADTO();
					
					dto.setId(id);
					dto.setCADate(rs.getString("cadate"));
					dto.setCAAmount(rs.getInt("amount"));
					dto.setCategory(rs.getString("category"));
					dto.setCANick(rs.getString("nickname"));
					
					lists.add(dto);
				}
				
			} else {
				System.out.println("입력된 입출금 내역이 존재하지 않습니다.");
				DBconn.close(pstmt, rs);
				return;
			}
			
			rs.close();
			pstmt.close();
			
			//출금 총액 구하기
			sql = "select -sum(amount) as amount from ("
					+ " select sum(amount) as amount from cardwithdraw"
					+ " where id = ? and ? <= DATE_FORMAT(cadate, '%Y-%m-%d') <= ?"
					+ " union all "
					+ " select sum(amount) as amount from accountwithdraw"
					+ " where id = ? and ? <= DATE_FORMAT(cadate, '%Y-%m-%d') <= ?"
					+ " ) as MJ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, date1);
			pstmt.setString(3, date2);
			pstmt.setString(4, id);
			pstmt.setString(5, date1);
			pstmt.setString(6, date2);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				withdraw = rs.getInt("amount");
			}
			
			rs.close();
			pstmt.close();
					
			//입금 총액 구하기		
			sql = "select sum(amount) as amount from accountdeposit where id = ? and ? <= DATE_FORMAT(cadate, '%Y-%m-%d') <= ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, date1);
			pstmt.setString(3, date2);
			
			rs = pstmt.executeQuery();//(amount,100000)
			
			if (rs.next()) {
				withdraw = rs.getInt("amount");
			}
			
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		if (lists != null) {
			
			System.out.println("---------------------------------------------------");
			System.out.println("  아이디   결제일   계좌별칭    금  액    카테고리");
			System.out.println("---------------------------------------------------");
			
			Iterator<CADTO> it = lists.iterator();
			while (it.hasNext()) {
				dto = it.next();
				dto.print();
			}
			
			System.out.println("\n----------------------------------------");
			System.out.println(date1+" ~ "+date2+" 의 가계부 총계");
			System.out.println("지출 :" + withdraw);
			System.out.println("수입 :" + deposit);
			System.out.println("증가 자산 :" + (deposit+withdraw));
			System.out.println("----------------------------------------\n");
		
		}
		DBconn.close(pstmt, rs);
	}
	
	//가계부조회 - 월별
	@Override
	public void selectMonth(String id, String year, String month) {

		List<CADTO> lists = null;
		
		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql;
		CADTO dto;
		
		int deposit = 0;
		int withdraw = 0;
		
		try {
			
			//입출금내역 전체조회
			//카드 이용 내역 
			sql = "(select id, date_format(cadate, '%Y-%m-%d') as cadate, -(amount) as amount, category, nickname"
					+ " from cardwithdraw where id = ? and year(date_format(cadate, '%Y-%m-%d')) = ? and month(date_format(cadate, '%Y-%m-%d')) = ?";
			sql += " union ";
			//계좌 입금 내역 
			sql += "select id, date_format(cadate, '%Y-%m-%d') as cadate, amount, category, nickname"
					+ " from accountdeposit where id = ? and year(date_format(cadate, '%Y-%m-%d')) = ? and month(date_format(cadate, '%Y-%m-%d')) = ?";
			sql += " union ";		
			//계좌 출금 내역
			sql += "select id, date_format(cadate, '%Y-%m-%d') as cadate, -(amount) as amount, category, nickname"
					+ " from accountwithdraw where id = ? and year(date_format(cadate, '%Y-%m-%d')) = ? and month(date_format(cadate, '%Y-%m-%d')) = ?) order by cadate desc";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, year);
			pstmt.setString(3, month);
			pstmt.setString(4, id);
			pstmt.setString(5, year);
			pstmt.setString(6, month);
			pstmt.setString(7, id);
			pstmt.setString(8, year);
			pstmt.setString(9, month);
			
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				
				lists = new ArrayList<CADTO>();
				
				while (rs.next()) {
					
					dto = new CADTO();
					
					dto.setId(rs.getString("id"));
					dto.setCADate(rs.getString("cadate"));
					dto.setCAAmount(rs.getInt("amount"));
					dto.setCategory(rs.getString("category"));
					dto.setCANick(rs.getString("nickname"));
					
					lists.add(dto);
				}
				
			} else {
				
				System.out.println("입력된 입출금 내역이 존재하지 않습니다.");
				DBconn.close(pstmt, rs);
				return;
			}
		 
			rs.close();
			pstmt.close();
			
			//출금총액 구하기
			sql = "select -sum(amount) as amount from ("
					+ "	select sum(amount) as amount from cardwithdraw where id = ? and year(date_format(cadate, '%Y-%m-%d')) = ? and month(date_format(cadate, '%Y-%m-%d')) = ?"
					+ "	union all"
					+ "	select sum(amount) as amount from accountwithdraw where id = ? and year(date_format(cadate, '%Y-%m-%d')) = ? and month(date_format(cadate, '%Y-%m-%d')) = ?"
					+ "	) as MJ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, year);
			pstmt.setString(3, month);
			pstmt.setString(4, id);
			pstmt.setString(5, year);
			pstmt.setString(6, month);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				withdraw = rs.getInt("amount");
			}
			
			rs.close();
			pstmt.close();

			//입금총액 구하기
			sql = "select sum(amount) as amount from accountdeposit where id = ? and year(date_format(cadate, '%Y-%m-%d')) = ? and month(date_format(cadate, '%Y-%m-%d')) = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, year);
			pstmt.setString(3, month);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				deposit = rs.getInt("amount");
			}
			
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		if (lists != null) {
			
			System.out.println("---------------------------------------------------");
			System.out.println("  아이디   결제일   계좌별칭    금  액    카테고리");
			System.out.println("---------------------------------------------------");
			
			Iterator<CADTO> it = lists.iterator();
			while (it.hasNext()) {
				dto = it.next();
				dto.print();
			}
			
			System.out.println("\n----------------------------------------");
			System.out.println("\t"+year +"년 "+ month +"월의 가계부 총계");
			System.out.println("지출 :" + withdraw);
			System.out.println("수입 :" + deposit);
			System.out.println("증가 자산 :" + (deposit+withdraw));
			System.out.println("----------------------------------------\n");
		}
		DBconn.close(pstmt, rs);
	}
	
	//가계부조회 - 카테고리별
	@Override
	public void selectCategory(String id, String category) {

		List<CADTO> lists = null;
		
		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql;
		CADTO dto;
		
		int deposit = 0;
		int withdraw = 0;
		
		try {
			
			//입출금내역 전체조회
			//카드 이용 내역
			sql = "(select id, date_format(cadate, '%Y-%m-%d') as cadate, -(amount) as amount, category, nickname"
					+ " from cardwithdraw where id = ? and category = ?";
			sql += " union ";
			//계좌 입금 내역
			sql += "select id, date_format(cadate, '%Y-%m-%d') as cadate, amount, category, nickname"
					+ " from accountdeposit where id = ? and category = ?";
			sql += " union ";
			//계좌 출금 내역
			sql += "select id, date_format(cadate, '%Y-%m-%d') as cadate, -(amount) as amount, category, nickname"
					+ " from accountwithdraw where id = ? and category = ?) order by cadate desc";		
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, category);
			pstmt.setString(3, id);
			pstmt.setString(4, category);
			pstmt.setString(5, id);
			pstmt.setString(6, category);
			
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				
				lists = new ArrayList<CADTO>();
				
				while (rs.next()) {
					
					dto = new CADTO();
					
					dto.setId(rs.getString("id"));
					dto.setCADate(rs.getString("cadate"));
					dto.setCAAmount(rs.getInt("amount"));
					dto.setCategory(rs.getString("category"));
					dto.setCANick(rs.getString("nickname"));
					
					lists.add(dto);
					
				}
				
			} else {
				
				System.out.println("입력된 입출금 내역이 존재하지 않습니다.");
				DBconn.close(pstmt, rs);
				return;
			}
			
			rs.close();
			pstmt.close();
			
			//출금총액 구하기
			sql = "select -sum(amount) as amount from ("
					+ "	select sum(amount) as amount from cardwithdraw where id = ? and category = ?"
					+ "	union all"
					+ "	select sum(amount) as amount from accountwithdraw where id = ? and category = ?"
					+ "	) as MJ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, category);
			pstmt.setString(3, id);
			pstmt.setString(4, category);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				withdraw = rs.getInt("amount");
			}
			
			rs.close();
			pstmt.close();
			
			//입금총액 구하기
			sql = "select sum(amount) as amount from accountdeposit where id = ? and category = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, category);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				deposit = rs.getInt("amount");
			}
			
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		if (lists != null) {
			
			System.out.println("---------------------------------------------------");
			System.out.println("  아이디   결제일   계좌별칭    금  액    카테고리");
			System.out.println("---------------------------------------------------");
			
			Iterator<CADTO> it = lists.iterator();
			while (it.hasNext()) {
				dto = it.next();
				dto.print();
			}
			
			System.out.println("\n----------------------------------------");
			System.out.println("\t"+category +"의 가계부 총계");
			System.out.println("지출 :" + withdraw);
			System.out.println("수입 :" + deposit);
			System.out.println("증가 자산 :" + (deposit+withdraw));
			System.out.println("----------------------------------------\n");
			
		} 
		DBconn.close(pstmt, rs);
	}
	
	//가계부조회 - 입금달력
	@Override
	public void selectInputCalendar(String id, String year, String month) {

		int i, y, m, w, lastday;

		y = Integer.parseInt(year);
		m = Integer.parseInt(month);

		Calendar now = Calendar.getInstance();

		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql;

		int deposit[] = new int[32];

		now.set(y, m - 1, 1);

		w = now.get(Calendar.DAY_OF_WEEK);
		lastday = now.getActualMaximum(Calendar.DATE);

		try {

			//입금 총액 구하기
			sql = "select dayofmonth(cadate) as date, sum(amount) as amount "
					+ " from accountdeposit where id = ? and year(cadate) = ? and month(cadate) = ?"
					+ " group by dayofmonth(cadate)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, id);
			pstmt.setString(2, year);
			pstmt.setString(3, month);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				int day = Integer.parseInt(rs.getString("date"));
				deposit[day] = rs.getInt("amount");
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		//출력
		System.out.println(" ◎ " + y + "년 " + m + "월 가계부 ◎ ");
		System.out.println(
				"\n     일                  월                  화                  수                     목                  금                  토");
		System.out.println(
				"------------------------------------------------------------------------------------------------------------------------------------------------");

		for (i = 1; i < w; i++) {
			System.out.println("                     ");
		}

		for (i = 1; i <= lastday; i++) {
			System.out.printf("%4d (입금%8d원)", i, deposit[i]);
			w++;
			if (w % 7 == 1) {
				System.out.println("\n");
			}
		}
		
		if (w % 7 != 1) {
			System.out.printf("\n");
		}
	
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");
	}
	
	//가계부조회 - 출금달력
	@Override
	public void selectOutputCalendar(String id, String year, String month) {

		int i, y, m, w, lastday;

		y = Integer.parseInt(year);
		m = Integer.parseInt(month);

		Calendar now = Calendar.getInstance();

		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql;

		int withdraw[] = new int[32];

		now.set(y, m - 1, 1);

		w = now.get(Calendar.DAY_OF_WEEK);
		lastday = now.getActualMaximum(Calendar.DATE);

		try {

			//출금 총액 구하기
			
			sql = "select date, sum(amount) as amount from ("
					+ " select day(cadate) as date, sum(amount) as amount from accountwithdraw"
					+ " where id = ? and year(cadate) = ? and month(cadate) = ?"
					+ " union all"
					+ " select day(cadate) as date, sum(amount) as amount from cardwithdraw"
					+ " where id = ? and year(cadate) = ? and month(cadate) = ?"
					+ " group by day(cadate)"
					+ " ) as MJ;";
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, id);
			pstmt.setString(2, year);
			pstmt.setString(3, month);
			pstmt.setString(4, id);
			pstmt.setString(5, year);
			pstmt.setString(6, month);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int day = Integer.parseInt(rs.getString("date"));
				withdraw[day] = rs.getInt("amount");
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		//출력
		System.out.println(" ◎ " + y + "년 " + m + "월 가계부 ◎ ");
		System.out.println(
				"\n     일                  월                  화                  수                     목                  금                  토");
		System.out.println(
				"------------------------------------------------------------------------------------------------------------------------------------------------");

		for (i = 1; i < w; i++) {
			System.out.println("                     ");
		}

		for (i = 1; i <= lastday; i++) {
			System.out.printf("%4d (출금%8d원)", i, withdraw[i]);
			w++;
			if (w % 7 == 1) {
				System.out.println("\n");
			}
		}
		
		if (w % 7 != 1) {
			System.out.printf("\n");
		}
	
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");
		
	}
	
	//지출 입력 - 카드 
	@Override
	public int outputCardCA(CADTO dto) {
		
		int result = 0;
		
		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql;
		
		try {
			
			sql = "insert into cardwithdraw (id, cadate, amount, category, nickname)"
					+ " values (?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getCADate());
			pstmt.setInt(3, dto.getCAAmount());
			pstmt.setString(4, dto.getCategory());
			pstmt.setString(5, dto.getCANick());
			
			result = pstmt.executeUpdate();
			DBconn.close(pstmt, rs);
			
			if (result == 1) {
				
				sql = "update card set cardamount = (cardamount + ?) where id = ? and nickname = ?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, dto.getCAAmount());
				pstmt.setString(2, dto.getId());
				pstmt.setString(3, dto.getCANick());
				
				result += pstmt.executeUpdate();
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}
	
	//지출 입력 - 계좌
	@Override
	public int outputAccountCA(CADTO dto) {
		
		int result = 0;
		
		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql;
		
		try {
			
			sql = "insert into accountwithdraw (id, cadate, amount, category, nickname)"
					+ " values (?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getCADate());
			pstmt.setInt(3, dto.getCAAmount());
			pstmt.setString(4, dto.getCategory());
			pstmt.setString(5, dto.getCANick());
			
			result += pstmt.executeUpdate();
			pstmt.close();
		
			if (result == 1) {
				
				sql = "update account set accamount = (accamount - ?) where id = ? and nickname = ?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, dto.getCAAmount());
				pstmt.setString(2, dto.getId());
				pstmt.setString(3, dto.getCANick());
				
				result += pstmt.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		DBconn.close(pstmt, rs);
		return result;
	}
	
	//입금 입력 - 계좌 
	@Override
	public int inputAccountCA(CADTO dto) {
		
		int result = 0;
		
		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql;
		
		try {
			
			sql = "insert into accountdeposit (id, cadate, amount, category, nickname)"
					+ " values (?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getCADate());
			pstmt.setInt(3, dto.getCAAmount());
			pstmt.setString(4, dto.getCategory());
			pstmt.setString(5, dto.getCANick());
			
			result += pstmt.executeUpdate();
			pstmt.close();
		
			if (result == 1) {
				
				sql = "update account set accamount = (accamount + ?) where id = ? and nickname = ?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, dto.getCAAmount());
				pstmt.setString(2, dto.getId());
				pstmt.setString(3, dto.getCANick());
				
				result += pstmt.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		DBconn.close(pstmt, rs);
		return result;
	}
	
	//지출 내역 삭제 - 계좌
	@Override
	public int deleteOutAccountCA(CADTO dto) {
		
		int result = 0;
		
		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql;
		
		try {
			
			sql = "select * from accountwithdraw"
					+ " where id = ? and cadate = ? and amount = ? and category = ? and nickname = ?"; 
				
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getCADate());
			pstmt.setInt(3, dto.getCAAmount());
			pstmt.setString(4, dto.getCategory());
			pstmt.setString(5, dto.getCANick());
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				
				sql = "delete from accountwithdraw"
						+ " where id = ? and cadate = ? and amount = ? and category = ? and nickname = ?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, dto.getId());
				pstmt.setString(2, dto.getCADate());
				pstmt.setInt(3, dto.getCAAmount());
				pstmt.setString(4, dto.getCategory());
				pstmt.setString(5, dto.getCANick());
				
				result = pstmt.executeUpdate();
				
			}
			DBconn.close(pstmt, rs);
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}
	
	//입금 내역 삭제 - 계좌
	@Override
	public int deleteInAccountCA(CADTO dto) {
		
		int result = 0;
		
		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql;
		
		try {
			
			sql = "select * from accountdeposit"
					+ " where id = ? and cadate = ? and amount = ? and category = ? and nickname = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getCADate());
			pstmt.setInt(3, dto.getCAAmount());
			pstmt.setString(4, dto.getCategory());
			pstmt.setString(5, dto.getCANick());
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				
				sql = "delete from accountdeposit"
						+ " where id = ? and cadate = ? and amount = ? and category = ? and nickname = ?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, dto.getId());
				pstmt.setString(2, dto.getCADate());
				pstmt.setInt(3, dto.getCAAmount());
				pstmt.setString(4, dto.getCategory());
				pstmt.setString(5, dto.getCANick());
				
				result = pstmt.executeUpdate();

			}
			DBconn.close(pstmt, rs);
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}
	
	//지출 내역 삭제 - 카드
	@Override
	public int deleteOutCardCA(CADTO dto) {

		int result = 0;
		
		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql;
		
		try {
			
			sql = "select * from cardwithdraw"
					+ " where id = ? and cadate = ? and amount = ? and category = ? and nickname = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getCADate());
			pstmt.setInt(3, dto.getCAAmount());
			pstmt.setString(4, dto.getCategory());
			pstmt.setString(5, dto.getCANick());
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				
				sql = "delete from cardwithdraw "
						+ " where id = ? and cadate = ? and amount = ? and category = ? and nickname = ?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, dto.getId());
				pstmt.setString(2, dto.getCADate());
				pstmt.setInt(3, dto.getCAAmount());
				pstmt.setString(4, dto.getCategory());
				pstmt.setString(5, dto.getCANick());
				
				result = pstmt.executeUpdate();
				
			}
			DBconn.close(pstmt, rs);
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}
	
	//계좌이체
	@Override
	public int changeAsset(String id, String str, String str2, int money) {

		int result = 0;
		String nick;
		
		Connection conn = DBconn.getDBConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql;
		
		try {
			
			sql = "select nickname from account where id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				nick = rs.getString("nickname");
				
				if (nick.equals(str) || nick.equals(str2)) {
					++result;
				}
			}
			
			rs.close();
			pstmt.close();
			
			if (result == 2) {
				
				sql = "update account set accamount=(accamount-?) where nickname = ? and id = ?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, money);
				pstmt.setString(2, str);
				pstmt.setString(3, id);
				
				result = result + pstmt.executeUpdate();
				
				pstmt.close();
				
				sql = "update account set accamount=(accamount+?) where nickname = ? and id = ?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, money);
				pstmt.setString(2, str2);
				pstmt.setString(3, id);
				
				result = result + pstmt.executeUpdate();
				
			}
				DBconn.close(pstmt, rs);
				
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}

	

	
}
