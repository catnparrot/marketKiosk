package proto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;

class M_Buy extends ManageUser
{ 
	ArrayList<String> nameLists = new ArrayList<String>();		//-- 상품 이름
	ArrayList<Integer> amountLists = new ArrayList<Integer>();	//-- 상품 수량
	ArrayList<Integer> priceLists = new ArrayList<Integer>();	//-- 상품 가격

	int returnCash = 0;		//-- 거스름돈
	int sumCash = 0;		//-- 누적 투입 현금

	static int salesCate1, salesCate2, salesCate3;	//-- 카테고리별 매출 계산

	static int totPrice;
	
	public static String name;		//-- 구매할 상품명
	static int buyAmount = 0;		//-- 구매 수량
	static String name1;
	static boolean flag = false;
	static int j = 0;

	
	
	
	M_Kiosk ks = new M_Kiosk();

	// (회원) 결제수단 선택
	public void selPay()
	{
		Scanner sc = new Scanner(System.in);
		
		
		int sel;
		
		System.out.println();
		System.out.println("====== 결제수단 선택 ======");
		
		try
		{
			if (M_UserSystem.loginflag == true)
			{
				do
				{
					System.out.println("    1. 현금 결제");
					System.out.println("    2. 잔액 결제");
					System.out.println("    3. 장바구니 비우기");
					System.out.println("===========================");
					System.out.println("* 참고 : 비회원은 현금결제만 가능합니다.");
					System.out.print("▶ ");
					sel = sc.nextInt();

					if (sel == 1)
						payCash();
					else if (sel == 2)
						payPoint();
					else if (sel == 3)
						clearCart();	
				}
				while (sel < 1 || sel > 3);
			}
			else 
			{
				do
				{
					System.out.println("    1. 현금 결제");
					System.out.println("    2. 장바구니 비우기");
					System.out.println("===========================");
					System.out.println("* 참고 : 비회원은 현금결제만 가능합니다.");
					System.out.print("▶ ");
					sel = sc.nextInt();

					if (sel == 1)
						payCash();
					else if (sel == 2)
						clearCart();	
				}
				while (sel < 1 || sel > 2);
			}
		}
		catch (InputMismatchException e)
		{
			System.out.println("숫자 입력해 주세요~~");	
			totPrice = 0;
			selPay();
		}
		
	}

	// 현금결제 메소드 (이용자, 비회원 둘 다 이용)
	public void payCash()
	{
		Scanner sc = new Scanner(System.in);

		int inputCash;		//-- 넣은 현금

		for (int i = 0; i < nameLists.size(); i++)
			totPrice += ( amountLists.get(i) * priceLists.get(i) );
		
		
		System.out.println();
		System.out.printf("총 금액          ▶ %d\n", totPrice);
		System.out.print("현금 넣어주세요  ▶ ");
		inputCash = sc.nextInt();

		sumCash = inputCash;

		if (inputCash < totPrice)
		{
			System.out.println();
			System.out.printf("%d원 부족\n", totPrice - sumCash);
			System.out.print("현금 넣어주세요  ▶ ");
			inputCash = sc.nextInt();

			sumCash += inputCash;

			if (sumCash < totPrice)
			{
				System.out.println();
				System.out.printf("%d원 부족\n", totPrice - sumCash);
				System.out.println("결제가 취소되었습니다.");
				System.out.println("카테고리로 돌아갑니다.");
				totPrice = 0; // 고른 총액 다 초기화.
				clearCart();
				//ks.systemOn();

			}
		}
		if (sumCash == totPrice)
		{
			System.out.println("\n★ 결제가 완료되었습니다 ★\n");
		}
		else if (sumCash > totPrice)
		{
			System.out.println("\n★ 결제가 완료되었습니다 ★\n");

			returnCash = returnChange(sumCash);		//-- 거스름돈 메소드 호출

			System.out.printf("넣은  돈 ▶ %8d 원\n", sumCash);
			System.out.printf("거스름돈 ▶ %8d 원\n", returnCash);
			System.out.println();
		}

		// 영수증출력 메소드 호출
		receipt();
	}
//==================================================================================================//
	
	// 잔액결제 메소드 (이용자만)
	public void payPoint()
	{
		Scanner sc = new Scanner(System.in);

		M_UserSystem us = new M_UserSystem();
		

		String name = memList.get(us.pw).getName();
		int point = memList.get(us.pw).getPoint();
		
		// 총 금액 = 장바구니에 담긴 상품수량*상품가격 
		for (int i = 0; i < nameLists.size(); i++)
			totPrice += ( amountLists.get(i) * priceLists.get(i) );  // 수량 * 상품가격

		System.out.println();
		System.out.printf("총 금액                 ▶ %10d 원\n", totPrice);
		System.out.printf("%s 님의 현재 잔액   ▶ %10d 원\n", name, point);
		System.out.printf("결제 후 %s님의 잔액 ▶ %10d 원\n", name, point-totPrice);
		
		if (point < totPrice)	// → 충전 후 진행
		{
			//payflag = false;
			System.out.println();
			System.out.printf("부족금액 ▶ %d 원\n", totPrice - point); 
			System.out.println();
			System.out.println("충전 후 이용해주세요.");
			System.out.println("이용자 페이지로 이동합니다.");
			totPrice = 0; // 고른 총액 다 초기화.
			clearCart2(); // 장바구니 비운 후 유저메뉴 출력
			
		}
		
		memList.get(us.pw).setPoint(point - totPrice);

		// 영수증출력 메소드 출력
		receipt();

	}

	// 거스름돈 메소드
	public int returnChange(int sumCash)
	{
		// 거스름돈 = 이용자 낸 금액 - 총 금액 
		returnCash = sumCash - totPrice;

		return returnCash;
	}


	// 영수증출력 메소드
	private void receipt()
	{
		LocalDate nowDate = LocalDate.now();	//-- 현재 날짜
		LocalTime nowTime = LocalTime.now();	//-- 현재 시간
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시 mm분 ss초");	//-- 포맷 정의
		String formatTime = nowTime.format(formatter);	//-- 포맷 적용
		
		System.out.println();
		System.out.println("★ 영수증을 출력합니다 ★");
		System.out.println();
		System.out.println("===========================================");
		System.out.println("         2조 무인편의점 (영수증)");
		System.out.println("-------------------------------------------");
		System.out.println("상품명                수량            가격");
		System.out.println("-------------------------------------------");

		int a = 0;
		for (int i = 0; i < nameLists.size(); i++)
			System.out.printf("%-10s\t%10d\t%10d\n", nameLists.get(i), amountLists.get(i), priceLists.get(i));

		System.out.println("-------------------------------------------"); 
		System.out.printf("판매금액%34d\n", totPrice);
		System.out.println("-------------------------------------------");
		System.out.println(nowDate + " " + formatTime);
		System.out.println("\n이용해주셔서 감사합니다 *^^*");
		System.out.println("===========================================");
		System.out.println();
		System.out.println();
		

		M_AdSystem.totalSales += totPrice;
		calCateTot();

		M_Kiosk ks = new M_Kiosk();
		totPrice = 0;
		M_UserSystem.loginflag = false;
		ks.systemOn();

	}	

	// 카테고리 합 계산
	public void calCateTot()
	{
		salesCate1 = 0; 
		salesCate2 = 0; 
		salesCate3 = 0;
		
		int category = 1;

		for (int i = 0; i < nameLists.size(); i++)
		{	
			category = pdList.get(nameLists.get(i)).getCategory();

			if (category == 1)
				salesCate1 += amountLists.get(i) * priceLists.get(i);
			else if (category == 2)
				salesCate2 += amountLists.get(i) * priceLists.get(i);
			else	// category == 3
				salesCate3 += amountLists.get(i) * priceLists.get(i);
		}	

		M_AdSystem.totalCate1 += salesCate1;
		M_AdSystem.totalCate2 += salesCate2;
		M_AdSystem.totalCate3 += salesCate3;
	}
	
	
	public void menuChoice()
	{
		int sel;
		
		System.out.println();
		System.out.println("=========== 카테고리 선택 ===========");
		System.out.println("    1. 식료품");
		System.out.println("    2. 생활용품");
		System.out.println("    3. 연령제한물품(술/담배)");
		System.out.println("    4. 장바구니 확인(결제 및 종료)");
		System.out.println("=====================================");

		Scanner sc = new Scanner(System.in);
		try
		{
			do
			{
				System.out.print("▶ ");
				sel = sc.nextInt();
			}
			while(sel < 1 || sel > 4);

			switch (sel)
			{
			case 1 -> {printList1(); menuBuy();}
			case 2 -> {printList2(); menuBuy();}
			case 3 -> testAge();	
			case 4 -> {cartList(); selPay();}
			}

		}
		catch (InputMismatchException e)
		{
			System.out.println("숫자 입력해 주세요~~");
			menuChoice();
		}	
	}

	
	
	// 카테고리 ① 식료품
	public void printList1()
	{
		//-------------------------------------------------------------------------------//
		//							[카테고리별로 정렬 하기(출력하기)]
			
		//------------------------------[식료품 출력]---------------------	
		System.out.println();
		System.out.println("---------------------- [식료품] --------------------------");
		for (String key :pdList.keySet())
		{
			Products value = pdList.get(key);

			if (value.getCategory() == 1)
				System.out.printf("상품명 : %-8s\t가격 : %-5d   남은수량 : %3d\n"
				, key,value.getPrice(), value.getProduct_Num());
		}
		System.out.println("----------------------------------------------------------");
	}

	// 카테고리 ② 생활용품
	public void printList2()
	{
		//------------------------------[생활용품 출력]---------------------
		System.out.println();
		System.out.println("----------------------- [생활용품] -----------------------");
		for (String key : pdList.keySet())
		{
			Products value = pdList.get(key);
			if (value.getCategory() == 2)
				System.out.printf("상품명 : %-8s\t가격 : %-5d   남은수량 : %3d\n"
				, key, value.getPrice(), value.getProduct_Num());
		}
		System.out.println("----------------------------------------------------------");
	}

	// 카테고리 ③ 연령제한물품(술/담배)
	public void printList3()
	{
		//------------------------------[연령제한 출력]---------------------
		System.out.println();
		System.out.println("--------------------- [연령제한 물품] --------------------");
		for (String key : pdList.keySet())
		{
			Products value = pdList.get(key);
			if (value.getCategory() == 3)
			{
				System.out.printf("상품명 : %-8s\t가격 : %-5d   남은수량 : %3d\n"
				, key, value.getPrice(), value.getProduct_Num());
	
			}
				
		}
		System.out.println("----------------------------------------------------------");
	}
	
	
	
	public void menuBuy()
	{
		Scanner sc = new Scanner(System.in);
		
		System.out.println("[-1 : 뒤로가기]");
		System.out.print("구매할 상품명 입력 ▶ ");
		name = sc.next();
		
	
		// -1 : 뒤로가기
		if (name.equals("-1"))	
		{
			name = name1;
			menuChoice();		
		}
		
		try
		{
			if (pdList.containsKey(name))
			{
				int productNum = pdList.get(name).getProduct_Num();		//-- 상품 현재 수량
				int setProjectNum = productNum;							//-- 재설정할 상품 수량

				do
				{
					System.out.print("구매 수량 입력     ▶ ");
					buyAmount = sc.nextInt();
				}
				while (buyAmount <= 0);
					
				if (buyAmount <= productNum)	// 구매희망수량 <= 상품현재수량
				{				
					j = pdList.get(name).getProduct_Num();
					j -= buyAmount;
					pdList.get(name).setProduct_Num(j);
				
					name1 = name;
					nameLists.add(name);
					amountLists.add(buyAmount);
					flag = nameLists.contains(name);
					
					System.out.println();
					System.out.println("장바구니에 담겼습니다.");
					cartList(); 
					menuChoice();
				}
				else 
				{
					System.out.println();
					System.out.println("수량이 부족합니다!!");
					menuBuy();
				}
			}
			else
			{
				System.out.println("잘못된 물품 이름입니다.");
				System.out.println("다시 선택하세요.");
				menuBuy();
			}
		}
		catch (InputMismatchException e)
		{
			System.out.println("숫자 입력해 주세요~~");
			menuBuy();
		}
		
		
	
	}

	public void testAge()
	{
		M_Users user = new M_Users();
		
		Scanner sc = new Scanner(System.in);
		String str = "";

		System.out.println();
		System.out.println("성인인증 후, 구매 가능합니다.");
		
		do
		{
			System.out.print("주민번호 입력('-'포함) ▶ ");
			str = sc.next();

			if (!user.ssnAva(str))
			{
				System.out.println("주민번호 입력 오류");
				System.out.println();
			}
		}
		while (!user.ssnAva(str));

		if (user.ssnAdult(str) == true)
		{
			System.out.println("성인인증 완료");
			System.out.println();

			printList3();
			menuBuy();
		}
		else 
		{	
			System.out.println();	
			System.out.println("[미성년자 구매 불가 상품입니다.]");

			menuChoice();
		}
	}
	
	
	

	
	
	public void cartList()
	{
		int pdPrice;	//-- 상품 가격

		if (nameLists.isEmpty())
		{
			System.out.println();
			System.out.println("장바구니에 담긴 상품이 없습니다.");
			System.out.println("메인으로 돌아갑니다.");
			M_UserSystem.loginflag = false;
			
			ks.systemOn();
		}
		else
		{
			System.out.println();
			System.out.println("================ [장바구니] ===============");
			pdPrice = pdList.get(name).getPrice();
			priceLists.add(pdPrice);
			System.out.println("상품명                수량            가격");
			System.out.println("-------------------------------------------");
	
			for (int i = 0; i < nameLists.size(); i++)
			{
				totPrice += priceLists.get(i);
				System.out.printf("%-10s\t%10d\t%10d\n", nameLists.get(i), amountLists.get(i), priceLists.get(i));
			}
			System.out.println("===========================================");
		}
	}

	// 장바구니 비우기
	public void clearCart()
	{
		for (int i = 0; i < nameLists.size(); i++)
			pdList.get(nameLists.get(i)).setProduct_Num(pdList.get(nameLists.get(i)).getProduct_Num()+amountLists.get(i));
		
		nameLists.clear();
		amountLists.clear();
		priceLists.clear();

		name = null;
		menuChoice();
	}
	
	// 회원 충전 실패시 장바구니 비우기
	public void clearCart2() 
	{
		for (int i = 0; i < nameLists.size(); i++)
			pdList.get(nameLists.get(i)).setProduct_Num(pdList.get(nameLists.get(i)).getProduct_Num()+amountLists.get(i));

		nameLists.clear();
		amountLists.clear();
		priceLists.clear();
		
		name = null;

		M_UserSystem us = new M_UserSystem(); // 윗 메소드와 달리
		us.menuDisp();					  // 비우고 띄워야할 메뉴가 다름
		us.menuSelect();
		us.menuRun();
	}


}