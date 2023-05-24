package proto;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ManageUser
{	
	// 이거 할 때는 DB 안 배워서 HashMap 을 상품정보와, 고객정보 테이블 정도로 사용한 듯 하다.
	public static Map<String, Members> memList = new HashMap<String, Members>(); 
	public static Map<String, Products> pdList = new HashMap<String, Products>();

	int sel;
	static String id, pw;
	static boolean loginflag;
	
	
	public void TestMemberData()
	{
		memList.put("010-5154-6322", new Members("김민성"));
		memList.put("010-9768-3110", new Members("김태형"));
		memList.get("010-5154-6322").setPoint(0);
	}

	public void TestProdData()
	{
		pdList.put("코카콜라", new Products(1000,10,1));
		pdList.put("펩시", new Products(900,10,1));
		pdList.put("사이다",new Products(1000,10,1));
		pdList.put("양말", new Products(5000,10,2));
		pdList.put("면도기", new Products(8000,10,2));
		pdList.put("빨래세제", new Products(10000,10,2));
		pdList.put("샴푸", new Products(12000,10,2));
	    pdList.put("말보로레드",new Products(4500,10,3));
	    pdList.put("참이슬",new Products(1700,10,3));
	}
	
	
	// 회원 로그인
	public void login() 
	{
		Scanner sc = new Scanner(System.in);

		M_Users u = new M_Users();				
		int loginCount = 1;		//-- 로그인 제한 횟수용
		boolean flag;	//-- 로그인 횟수제한 조건용

		System.out.println();
		System.out.println("=============== 로그인 ==============");

		flag = false;
		
		do
		{
			System.out.println("[ID : 이름 / PW : 전화번호('-' 포함)]");
			System.out.print("ID ▶ ");
			id = sc.next();
			System.out.print("PW ▶ ");
			pw = sc.next();

			// PW 잘못 입력
			if (!memList.containsKey(pw))
			{
				System.out.println();
				System.out.println("PW 입력 오류");
				System.out.printf("[남은기회 : %d번]\n", 5 - loginCount);
				System.out.println();
			}
			// 비번은 올바로 입력했으나, ID가 잘못된 경우
			else if(!(memList).get(pw).getName().equals(id) && memList.containsKey(pw))	
			{
				System.out.println();
				System.out.println("ID 입력 오류");
				System.out.printf("[남은기회 : %d번]\n", 5 - loginCount);
				System.out.println();
			}
			// 성공한 경우
			else if ((memList).get(pw).getName().equals(id) && memList.containsKey(pw))
			{
				flag = true;
			}
			// 횟수를 5회 를 초과한 경우
			if (loginCount > 4)
			{
				M_Kiosk ks = new M_Kiosk();
				System.out.println("로그인 실패...");
				System.out.println("메인으로 돌아갑니다.");
				ks.systemOn();
			}
			loginCount++;
		}
		while (!flag);
		
		loginflag = true;
		
		M_UserSystem us = new M_UserSystem();
		us.systemOn();

	}// end of login
	
}