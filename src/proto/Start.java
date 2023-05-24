package proto;
public class Start {
	public static void main(String[] args)
	{
		ManageUser pj = new ManageUser();
		pj.TestMemberData();
		pj.TestProdData();

		M_Kiosk ks = new M_Kiosk();
		ks.systemOn();
	}
}
