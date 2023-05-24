package proto;
import java.util.Scanner;
//import java.util.HashMap;
import java.util.InputMismatchException;

class M_Kiosk extends ManageUser implements KMenu	
{	
	int sel;



	@Override
	public void systemOn()
	{
		menuDisp();
		menuSelect();
		menuRun();
	}
	
	@Override
	public void menuDisp()	
	{
		System.out.println();
		System.out.println("===========================");
		System.out.println("       무인 편의점");
		System.out.println("===========================");
		System.out.println("    1. 고객 로그인");
		System.out.println("    2. 비회원 구매");
		System.out.println("    3. 회원 가입");
		System.out.println("    4. 관리자 모드");
		System.out.println("===========================");
	}

	@Override
	public void menuSelect()
	{
		Scanner sc = new Scanner(System.in);
		try
		{
			do
			{		
				System.out.print("▶ ");
				sel = sc.nextInt();
				if (sel < 1 || sel > 4) {
					System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
				}
			}
			while(sel < 1 || sel > 4);	
		}
		catch (InputMismatchException e)
		{
			System.out.println("숫자 입력해 주세요~~");
			menuSelect();
		}
				
	}

	// 선택된 메뉴 실행에 따른 기능 호출 메소드
	@Override
	public void menuRun()
	{
			if (sel == 1)
			{
				M_UserSystem us = new M_UserSystem();
				us.systemOn();
			}
			else if (sel == 2)
			{
				M_NotUserSystem nu = new M_NotUserSystem();
				nu.systemOn();
			}
			else if (sel == 3)
			{
				M_Users u = new M_Users();
				u.joinning();
			}
			else if (sel == 4)
			{
				M_AdSystem ad = new M_AdSystem();
				ad.systemOn();
			}
	}
}

