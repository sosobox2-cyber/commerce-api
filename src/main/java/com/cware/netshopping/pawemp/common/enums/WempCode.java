package com.cware.netshopping.pawemp.common.enums;

public interface WempCode {
	
	enum WHO_REASON{
		CUSTOMER("01","구매자책임"),
		SELLER("02","판매자책임"),
		WEMP("03","위메프책임");
		
		private String code;
		private String text;
		
		private WHO_REASON(String code,String text){
			this.code = code;
			this.text = text;
		}
		
		public String getCode () {

            return code;
        }

        public String getText () {

            return text;
        }
	};
	
	/**
	 * O723 TPAWEMPORDERLIST.DELIVERY_STATUS
	 */
	enum DELIVERY_STATUS{
		NEW("01","신규주문"),
		READY("02","상품준비중"),
		DELIVERY_DOING("03","배송중"),
		DELIVERY_COMPLETE("04","배송완료"),;
		
		private String code;
		private String text;
		
		private DELIVERY_STATUS(String code,String text){
			this.code = code;
			this.text = text;
		}
		
		public String getCode () {

            return code;
        }

        public String getText () {

            return text;
        }
	};
	
	enum DO_FLAG{
		ENTERED("10","접수"),
		APPROVED("20","승인"),
		COMPANY_ORDER("25","업체지시"),
		SHIPMENT_ORDER("30","출하지시"),
		SHIP("40","출고"),
		COLLECTION_ORDER("50","회수지시"),
		COLLECTION_CUSTOMER_DIRECT_SEND("52","회수고객직접발송"),
		TRANSMISSION_COLLECTION_ORDER("55","회수지시전송"),
		GATHERING_COLLECTION_GOODS("58","회수집하완료"),
		COLLECTION_APPROVAL("60","회수확정"),
		SHIP_COMPLETE("80","배송완료");
		
		private String code;
		private String text;
		
		private DO_FLAG(String code,String text){
			this.code = code;
			this.text = text;
		}
		
		public String getCode () {

            return code;
        }

        public String getText () {

            return text;
        }
	}
	
	enum ORDER_GB{
		ORDER("10","주문"),
		CANCEL("20","취소"),
		RETURN("30","반품"),
		CANCEL_RETURN("31","반품취소"),
		EXCHANGE_DELIVERY("40","교환배송"),
		EXCHANGE_DELIVERY_CANCELLATION("41","교환배취"),
		EXCHANGE_COLLECTION("45","교환회수"),
		EXCHANGE_COLLECTION_CANCELLATION("46","교환회취");
		
		private String code;
		private String text;
		
		private ORDER_GB(String code,String text){
			this.code = code;
			this.text = text;
		}
		
		public String getCode () {

            return code;
        }

        public String getText () {

            return text;
        }
	}
}
