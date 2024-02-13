package boardtest.copy;

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

    public Board getDetail(int num) {
        String SQL = "SELECT * FROM BOARD WHERE BOARD_NUM=?";
        try {
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, num);
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
