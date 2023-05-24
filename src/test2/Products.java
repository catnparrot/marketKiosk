package test2;

import lombok.Data;

@Data
class Products
{
	// HashMap<K, V> → <상품이름, Products 객체> 로 사용하기 때문에,
	// Products DTO 에 상품 이름 정보 없음
	private int price;
	private int product_Num;	// 상품개수
	private int category;
	private int count;			   //-- 계산 카운터 결제시 0 초기화 (쓰는 곳 없는듯..?)
	private int	addCount = 0;	  //-- 누적 카운터 결제시 카운트만큼 누적 (쓰는 곳 없는듯,,?)

	//생성자
	Products(int price, int product_Num, int category) {
		this.price = price;
		this.product_Num = product_Num;
		this.category = category;
	}	
}

