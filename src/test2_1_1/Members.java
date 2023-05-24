package test2_1_1;

import lombok.Data;

@Data
class Members
{
	// HashMap<K, V> → <tel, Members 객체> 로 사용하기 때문에,
	// Members DTO 에 회원 핸드폰 정보 없음
	private String name;	//-- 회원 이름
	private String pwd;
	private String ssn;
	private int point;		//-- 잔액


	// 생성자
	Members(String name) {
		this.name = name;
	}
	
	Members() {
	}
}






	