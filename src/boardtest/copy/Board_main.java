package boardtest.copy;

import java.util.Scanner;

public class Board_main {
    static String menus[] = {
        		"글쓰기",
        		"수정",
        		"답변달기",
        		"글삭제",
        		"조회",
        		"페이지 선정",
        		"종료"
        };
    
    public static void main(String[] args) {
        
    	Scanner sc = new Scanner(System.in);
        BoardDAO_seq dao = new BoardDAO_seq();

        do {
            int menu =	 menuselect(sc);
            switch (menu) {
                case 1:
                    insert(sc, dao);
                    break;
                case 2:
                    update(sc, dao);
                    break;
                case 5:
                    selectAll(dao);
                    break;
                case 7:
                    sc.close();
                    return;
            }
        } while (true);
    }

    public static int menuselect(Scanner sc) {
        int choice;
        do {
        	displayMenu();
        	choice = inputNumber(sc,1,menus.length);
        } while (choice == -1);
        return choice;
    }
    
    private static void displayMenu() {
        System.out.println("================================================");
        for (int i = 0; i < menus.length; i++) {
            System.out.println((i + 1) + "." + menus[i]);
        }
        System.out.println("================================================");
        System.out.print("메뉴를 선택하세요> ");
    }

    public static int inputNumber(Scanner sc, int start, int end) {
        int input = 0;
        while (true) {
        	try {
        		input = Integer.parseInt(sc.nextLine());
        		if(input<=end && input>= start) {
        			break;
        		} else {
        			System.out.println(start + "~" + end + "사이의 숫자를 입력하세요>");
        		}
        	} catch(NumberFormatException e ) {
        		System.out.println("숫자로 입력하세요> ");
        	}
        }
		return input;
    }
    
    private static void reply(Scanner sc, BoardDAO_seq dao) {
    	System.out.println("답변을 달 글 번호를 입력하세요>");
    	int num = inputNumber(sc);
    	Board board = select(dao,num);
    	if (board != null) {
    		System.out.println("글쓴이>");
    		board.setBoard_name(sc.nextLine());
    		
    		System.out.println("제목>");
    		board.setBoard_subject(sc.nextLine());
    		
    		System.out.println("글 내용>");
    		board.setBoard_content(sc.nextLine());
    		
    		System.out.println("비밀번호>");
    		board.setBoard_pass(sc.nextLine());
    		
    		int result = dao.boardReply(board);
    		if (result != 0) {
    			System.out.println("답변 달기 성공");
    		} else {
    			
    		}
    	}
    }
    
    public static void insert(Scanner sc, BoardDAO_seq dao) {
    	System.out.println("작성자를 입력하세요> ");
    	String 
    	
    }
        
    

}
