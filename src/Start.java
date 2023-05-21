public class Start {
	public static void main(String[] args)
	{
		Project pj = new Project();
		pj.TestMemberData();
		pj.TestProdData();

		Kiosk ks = new Kiosk();
		ks.systemOn();
	}
}
