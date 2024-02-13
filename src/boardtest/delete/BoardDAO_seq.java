package boardtest.delete;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class BoardDAO_seq {

	public BoardDAO_seq() {

	}

	// 글 목록 보기
	public List<Board> getBoardList(int page, int limit) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		
		String board_list_sql = 
				  "select * "
				+ "from " 
		        + "     (select rownum rnum, b.* " 
		        + "      from (select * from board "
				+ "            order by BOARD_RE_REF desc, BOARD_RE_SEQ asc) b"
				+ "      where rownum<=?) " 
				+ "where rnum>=? and rnum<=?";
		
		
		List<Board> list = new ArrayList<Board>();
		// 한 페이지당 10개씩 목록인 경우                              1페이지, 2페이지, 3페이지, 4페이지 ...
		int startrow = (page - 1) * limit + 1; // 읽기 시작할 row 번호(1    11     21      31 ...
		int endrow = startrow + limit - 1;     // 읽을 마지막 row 번호(10    20     30      40 ...
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(url, "board", "1234");
			pstmt = conn.prepareStatement(board_list_sql);
			pstmt.setInt(1, endrow);
			pstmt.setInt(2, startrow);
			pstmt.setInt(3, endrow);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				Board board = new Board();
				board.setBoard_num(rs.getInt("BOARD_NUM"));
				board.setBoard_name(rs.getString("BOARD_NAME"));
				board.setBoard_subject(rs.getString("BOARD_SUBJECT"));
				board.setBoard_content(rs.getString("BOARD_CONTENT"));
				//board.setBoard_file(rs.getString("BOARD_FILE"));
				board.setBoard_re_ref(rs.getInt("BOARD_RE_REF"));
				board.setBoard_re_lev(rs.getInt("BOARD_RE_LEV"));
				board.setBoard_re_seq(rs.getInt("BOARD_RE_SEQ"));
				//board.setBOARD_READCOUNT(rs.getInt("BOARD_READCOUNT"));
				board.setBoard_date(rs.getString("BOARD_DATE"));
				list.add(board); // 값을 담은 객체를 리스트에 저장합니다.
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("getBoardList() 에러 : " + ex);
		} finally {
			if (rs != null)
				try {
						rs.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			if (pstmt != null)
				try {
						pstmt.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			if (conn != null)
				try {
					conn.close(); //4단계 : DB연결을 끊는다.
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
		}
		return list;
	}// getBoardList()메서드 end

	// 글 내용 보기
	public Board getDetail(int num) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		Board board = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(url, "board", "1234");
			
			pstmt = conn.prepareStatement("select * from board where BOARD_NUM = ?");
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				board = new Board();
				board.setBoard_num(rs.getInt("BOARD_NUM"));
				board.setBoard_pass(rs.getString("BOARD_PASS"));//비밀번호 확인시 사용됩니다.
				board.setBoard_name(rs.getString("BOARD_NAME"));
				board.setBoard_subject(rs.getString("BOARD_SUBJECT"));
				board.setBoard_content(rs.getString("BOARD_CONTENT"));
				//board.setBoard_file(rs.getString("BOARD_FILE"));
				board.setBoard_re_ref(rs.getInt("BOARD_RE_REF"));
				board.setBoard_re_lev(rs.getInt("BOARD_RE_LEV"));
				board.setBoard_re_seq(rs.getInt("BOARD_RE_SEQ"));
				board.setBoard_readcount(rs.getInt("BOARD_READCOUNT"));
				board.setBoard_date(rs.getString("BOARD_DATE"));
			}
			
		} catch (Exception ex) {
			System.out.println("getDetail() 에러: " + ex);
		} finally {
			if (rs != null)
				try {
						rs.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			if (pstmt != null)
				try {
						pstmt.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			if (conn != null)
				try {
					conn.close(); //4단계 : DB연결을 끊는다.
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
		}
		return board;
	}// getDetail()메서드 end
	
	
	// 글 수정
		public int boardModify(Board modifyboard) {
			PreparedStatement pstmt = null;
			Connection conn = null;
			String sql = "update board " 
			           + "set    BOARD_SUBJECT= ?, " 
					   + "       BOARD_CONTENT= ? " 
			           + "where  BOARD_NUM=? ";
			int result = 0;
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				String url = "jdbc:oracle:thin:@localhost:1521:xe";
				conn = DriverManager.getConnection(url, "board", "1234");
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, modifyboard.getBoard_subject());
				pstmt.setString(2, modifyboard.getBoard_content());
				pstmt.setInt(3, modifyboard.getBoard_num());
				result = pstmt.executeUpdate();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("boardModify() 에러: " + ex);
			} finally {
				if (pstmt != null)
					try {
							pstmt.close();
					} catch (SQLException e) {
						System.out.println(e.getMessage());
					}
				if (conn != null)
					try {
						conn.close(); //4단계 : DB연결을 끊는다.
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
			}
			return result;
		}// boardModify()메서드 end


	// 글 등록하기
	public int boardInsert(Board board) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		int result = 0;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(url, "board", "1234");

			String sql = "insert into board " 
			    + "(BOARD_NUM,BOARD_NAME,BOARD_PASS,BOARD_SUBJECT,"
				+ " BOARD_CONTENT, BOARD_RE_REF,"
			    + " BOARD_RE_LEV,BOARD_RE_SEQ,BOARD_READCOUNT,"
				+ " BOARD_DATE) " 
			    + " values(board_seq.nextval,?,?,?,"
			    + "        ?,  board_seq.nextval,"
			    + "        ?,?,?,"
			    + "        sysdate)";

			// 새로운 글을 등록하는 부분입니다.
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getBoard_name());
			pstmt.setString(2, board.getBoard_pass());
			pstmt.setString(3, board.getBoard_subject());
			pstmt.setString(4, board.getBoard_content());

			// 원문의 경우 BOARD_RE_LEV, BOARD_RE_SEQ 필드 값은 0 입니다.
			pstmt.setInt(5, 0); // BOARD_RE_LEV 필드
			pstmt.setInt(6, 0); // BOARD_RE_SEQ 필드
			pstmt.setInt(7, 0); // BOARD_READCOUNT 필드

			result = pstmt.executeUpdate();
			
		} catch (Exception ex) {
			System.out.println("boardInsert() 에러: " + ex);
			ex.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
						pstmt.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			if (conn != null)
				try {
					conn.close(); //4단계 : DB연결을 끊는다.
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
		}
		return result;
	}// boardInsert()메서드 end

	// 글 답변
	public int boardReply(Board board) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		int result = 0;

		int re_ref = board.getBoard_re_ref();

		
		int re_lev = board.getBoard_re_lev();

		// 같은 관련 글 중에서 해당 글이 출력되는 순서입니다.
		int re_seq = board.getBoard_re_seq();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(url, "board", "1234");
			
			// 트랜잭션을 이용하기 위해서 setAutoCommit을 false로 설정합니다.
			conn.setAutoCommit(false);

			
			String sql =   "update board " 
					     + "set    BOARD_RE_SEQ = BOARD_RE_SEQ + 1 " 
						 + "where  BOARD_RE_REF = ? "
					  	 + "and    BOARD_RE_SEQ > ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, re_ref);
			pstmt.setInt(2, re_seq);
			pstmt.executeUpdate();
			pstmt.close();
			
			// 등록할 답변 글의 BOARD_RE_LEV, BOARD_RE_SEQ 값을 원문 글보다 1씩 증가시킵니다.
			++re_seq;
			++re_lev;

			sql = "insert into board " 
			     + "(BOARD_NUM,BOARD_NAME,BOARD_PASS,BOARD_SUBJECT,"
				 + " BOARD_CONTENT, BOARD_FILE, BOARD_RE_REF," 
			     + " BOARD_RE_LEV, BOARD_RE_SEQ,"
				 + " BOARD_READCOUNT,BOARD_DATE) " 
			     + "values(board_seq.nextval,?,?,?,?,?,?,?,?,?,sysdate)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getBoard_name());
			pstmt.setString(2, board.getBoard_pass());
			pstmt.setString(3, board.getBoard_subject());
			pstmt.setString(4, board.getBoard_content());
			pstmt.setString(5, ""); // 답변에는 파일을 업로드하지 않습니다.
			pstmt.setInt(6, re_ref);
			pstmt.setInt(7, re_lev);
			pstmt.setInt(8, re_seq);
			pstmt.setInt(9, 0); // BOARD_READCOUNT(조회수)는 0
			result = pstmt.executeUpdate();
			conn.commit(); // commit합니다.

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("boardReply() 에러 : " + ex);
				try {
					if (conn != null)
					   conn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		} finally {
			
			if (pstmt != null)
				try {
						pstmt.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			if (conn != null)
				try {
					conn.close(); //4단계 : DB연결을 끊는다.
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
		}
		return result;
	}// boardReply()메서드 end

	/*
	
	public int boardDelete(Board board) {
		int count = 0;

		if (board.getBoard_re_seq() > 0) {
			int smallestSeq = Integer.MAX_VALUE;
			for (Board b : boardList) {
				if (b.getBoard_re_ref() == board.getBoard_re_ref() && b.getBoard_re_lev() >= board.getBoard_re_lev() &&
						b.getBoard_re_seq() > board.getBoard_re_seq()) {
					smallestSeq = Math.min(smallestSeq, b.getBoard_re_seq());
				}
			}

			for (int i = board.getBoard_re_seq(); i < smallestSeq; i++) {
				count += deleteBoardBySeq(board.getBoard_re_ref(), i);
			}
		} else {
			count += deleteBoard(board);
		}

		return count;
	}
*/
	

	
}// class end