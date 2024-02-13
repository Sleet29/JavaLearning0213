package boardtest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class BoardDAO_seq {
	private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;
	private ArrayList<Board> boardList;

    public BoardDAO_seq() {
    	try {
            String dbURL = "jdbc:oracle:thin:@localhost:1521:xe"; // 데이터베이스 URL
            String dbID = "board"; // 데이터베이스 계정
            String dbPassword = "1234"; // 데이터베이스 비밀번호
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int boardInsert(Board board) {
    	String SQL = "INSERT INTO BOARD VALUES (board_seq.NEXTVAL, ?, ?, ?, ?, NULL, NULL, 0, 0, 0, 0, SYSDATE)";
        try {
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, board.getBoard_name());
            pstmt.setString(2, board.getBoard_pass());
            pstmt.setString(3, board.getBoard_subject());
            pstmt.setString(4, board.getBoard_content());
            return pstmt.executeUpdate(); // 삽입된 행의 수 반환
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // 오류 발생 시 -1 반환
    }

    public int boardModify(Board modifyboard) {
    	String SQL = "UPDATE BOARD SET BOARD_SUBJECT=?, BOARD_CONTENT=? WHERE BOARD_NUM=?";
        try {
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, modifyboard.getBoard_subject());
            pstmt.setString(2, modifyboard.getBoard_content());
            pstmt.setInt(3, modifyboard.getBoard_num());
            return pstmt.executeUpdate(); // 수정된 행의 수 반환
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // 오류 발생 시 -1 반환
    }

    public int boardReply(Board board) {
    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	
        int result = 0; 
        /*
         * 답변을 할 원문 글 그룹 번호입니다.
         * 답변을 달게 되면 답변 글은 이 번호와 같은 관련글 번호를 갖게 처리되면서
         * 같은 그룹에 속하게 됩니다. 글 목록에서 보여줄 때 하나의 그룹으로 묶여서 출력됩니다.
         * 
         */
        int re_ref = board.getBoard_re_ref();
        
        /*
         * 답글의 깊이를 의미합니다.
         * 원문에 대한 답글이 출력될 때 한 번 들여쓰기 처리가 되고 답글에 대한 답글은 들여쓰기가 두 번 처리되게
         * 합니다. 원문인 경우에는 이 값이 0이고 원문의 답글은 1, 답글의 답글은 2가 됩니다.
         * 
         */
        int re_lev = board.getBoard_re_lev();
        
        // 같은 관련 글 중에서 해당 글이 출력되는 순서.
        int re_seq = board.getBoard_re_seq();
        
        try {
        	Class.forName("oracle.jdbc.driver.OracleDriver");
        	String url = "jdbc:oracle:thin:@localhost:1521:xe";
        	conn = DriverManager.getConnection(url,"board","1234");
        	
        	// 트랜잭션을 이용하기 위해서 setAutoCommit을 false로 설정합니다.
        	conn.setAutoCommit(false);
        	
        	// BOARD_RE_REF, BOARD_RE_SEQ 값을 확인하여 원문 글에 다른 답글이 있으면
        	// 다른 답글들의 BOARD_RE_SEQ값을 1씩 증가시킵니다.
        	// 현재 글을 다른 답글보다 앞에 출력되게 하기 위해서 입니다.
        	String sql = "update board "
        			+ "set 		BOARD_RE_SEQ = BOARD_RE_SEQ+1 "
        			+ "where 	BOARD_RE_REF = ? "
        			+ "and 		BOARD_RE_SEQ > ? ";
        	
        }
    
    }

    public List<Board> getBoardList(int page, int limit) {
    	List<Board> list = new ArrayList<>();
        int startrow = (page - 1) * limit + 1; // 읽기 시작할 row 번호
        int endrow = startrow + limit - 1; // 읽을 마지막 row 번호
        String SQL = "SELECT * FROM (SELECT ROWNUM AS rnum, B.* FROM (SELECT * FROM BOARD ORDER BY BOARD_NUM DESC) B) WHERE rnum >= ? AND rnum <= ?";
        try {
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, startrow);
            pstmt.setInt(2, endrow);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Board board = new Board();
                board.setBoard_num(rs.getInt("board_num"));
                board.setBoard_name(rs.getString("board_name"));
                board.setBoard_subject(rs.getString("board_subject"));
                board.setBoard_content(rs.getString("board_content"));
                board.setBoard_date(rs.getString("board_date"));
                list.add(board);
            }
            if (list.isEmpty()) {
                System.out.println("테이블에 데이터가 없습니다.");
            } else {
                System.out.println("글번호\t작성자\t\t제목\t\t\t\t\t내용\t\t\t\t\tdate");
                for (Board board : list) {
                    System.out.println(board.getBoard_num() + "\t" + board.getBoard_name() + "\t\t" + board.getBoard_subject() + "\t\t\t\t" + board.getBoard_content() + "\t\t\t\t" + board.getBoard_date());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Board getDetail(int boardNum) {
    	String SQL = "SELECT * FROM BOARD WHERE BOARD_NUM=?";
        try {
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, boardNum);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Board board = new Board();
                board.setBoard_num(rs.getInt("board_num"));
                board.setBoard_name(rs.getString("board_name"));
                board.setBoard_subject(rs.getString("board_subject"));
                board.setBoard_content(rs.getString("board_content"));
                board.setBoard_date(rs.getString("board_date"));
                return board;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 해당하는 글이 없는 경우 null 반환
    }
}
