package boardtest;
import java.util.Scanner;

public class Board_main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BoardDAO_seq dao = new BoardDAO_seq();

        do {
            int menu = menuselect(sc);
            switch (menu) {
                case 1:
                    insert(sc, dao);
                    break;

                case 2:
                    update(sc, dao);
                    break;

                case 3:
                    reply(sc, dao);
                    break;

                case 5:
                    selectAll(dao);
                    break;

                case 7:
                    sc.close();
                    System.exit(0); // Properly exit the program
            }
        } while (true);
    }

    public static int menuselect(Scanner sc) {
        System.out.println("================ 출력 결과 ================================");
        System.out.println("1.글쓰기\n2.수정\n3.답변달기\n4.글삭제\n5.조회\n6.페이지 선정\n7.종료");
        System.out.println("===========================================================");

        return inputNumber(sc, 1, 7);
    }

    public static int inputNumber(Scanner sc, int start, int end) {
        int number;
        do {
            System.out.print("메뉴를 선택하세요>");
            String input = sc.next();
            try {
                number = Integer.parseInt(input);
                if (number >= start && number <= end) {
                    break;
                } else {
                    System.out.println(start + "~" + end + "사이의 숫자를 입력하세요>");
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자로 입력하세요>");
            }
        } while (true);
        return number;
    }

    private static void reply(Scanner sc, BoardDAO_seq dao) {
        System.out.println("답변을 달 글 번호를 입력하세요>");
        int num = inputNumber(sc, 1, 200);
        Board board = dao.getDetail(num);
        if (board != null) {
            System.out.println("글쓴이>");
            String board_name = sc.next();
            System.out.println("제목>");
            String board_subject = sc.next();
            System.out.println("글 내용>");
            String board_content = sc.nextLine(); // Read entire line
            System.out.println("비밀번호>");
            String board_pass = sc.next();

            board.setBoard_name(board_name);
            board.setBoard_subject(board_subject);
            board.setBoard_content(board_content);
            board.setBoard_pass(board_pass);

            int result = dao.boardReply(board);
            if (result == 1) {
                System.out.println("답변 달기 성공");
            } else {
                System.out.println("답변 달기 실패");
            }
        } else {
            System.out.println("해당 글이 존재하지 않습니다.");
        }
    }

    private static void insert(Scanner sc, BoardDAO_seq dao) {
        System.out.print("작성자>");
        String board_name = sc.next();
        System.out.print("비밀번호>");
        String board_pass = sc.next();
        System.out.print("제목>");
        String board_subject = sc.next();
        System.out.print("글 내용>");
        String board_content = sc.nextLine(); // Read entire line

        Board board = new Board();
        board.setBoard_name(board_name);
        board.setBoard_pass(board_pass);
        board.setBoard_subject(board_subject);
        board.setBoard_content(board_content);

        int result = dao.boardInsert(board);
        if (result == 1) {
            System.out.println("글이 작성되었습니다.");
        } else {
            System.out.println("오류가 발생되었습니다.");
        }
    }

    private static void selectAll(BoardDAO_seq dao) {
        dao.getBoardList(1, 10);
    }

    private static void update(Scanner sc, BoardDAO_seq dao) {
        System.out.print("수정할 글 번호를 입력하세요>");
        int num = inputNumber(sc, 1, 7);

        Board board = dao.getDetail(num);
        if (board != null) {
            System.out.print("제목>");
            String board_subject = sc.next();
            System.out.print("글 내용>");
            String board_content = sc.nextLine(); // Read entire line
            System.out.print("비밀번호>");
            String board_pass = sc.next();

            board.setBoard_subject(board_subject);
            board.setBoard_content(board_content);

            if (board_pass.equals(board.getBoard_pass())) {
                int result = dao.boardModify(board);
                if (result == 1) {
                    System.out.println("수정에 성공했습니다.");
                } else {
                    System.out.println("수정에 실패했습니다.");
                }
            } else {
                System.out.println("비밀번호가 다릅니다.");
            }
        }
    }
}
