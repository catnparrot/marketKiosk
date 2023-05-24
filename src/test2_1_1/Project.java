package test2_1_1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Project
{	
	// 이거 할 때는 DB 안 배워서 HashMap 을 상품정보와, 고객정보 테이블 정도로 사용한 듯 하다.
	public static Map<String, Members> memList = new HashMap<String, Members>(); 
	public static Map<String, Products> pdList = new HashMap<String, Products>();
	
	Connection conn;
	
	public Project() {
		try {
			//JDBC Driver 등록
			Class.forName("oracle.jdbc.OracleDriver");
			
			//연결하기
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521/xe",
					"hr",
					"12345"
					);

		}catch(Exception e) {
			e.printStackTrace();
			exit();
		}
	}
	
	public void exit() {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
		System.out.println("** DB 연결에 문제가 생겨 프로그램을 종료합니다. **");
		System.exit(0);
	}
	
	
	/*
	public void Memlist() {   
        try {
			String sql = ""+
					"SELECT name, pwd, ssn " +
					"FROM member";
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Members member = new Members();
				member.setName(rs.getString("name"));
				member.setPwd(rs.getString("pwd"));
				member.setSsn(rs.getString("ssn"));

				memList.put(member.getPwd(), member.getName());
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			exit();
		}
	}
	
	
	public void Plist() {
		try {
			String sql = ""+
					"SELECT kname, kprice, kstock, kcode " +
					"FROM kproduct";
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Products pro = new Products();
				pro.setName(rs.getString("kname"));
				pro.setPrice(rs.getInt("kprice"));
				pro.setProduct_Num(rs.getInt("kstock"));
				pro.setCategory(rs.getInt("kcode"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			exit();
		}
	}
	*/
	
	
	
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
	
	
	/*
	public void money() {
		
	}
	*/
	
	
	
	
	
	public static void main(String[] args)
	{
		Kiosk ks = new Kiosk();
		ks.systemOn();
	}

}

// 테스트 주민번호 : 100822-1371454