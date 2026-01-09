package com.cware.netshopping.pawemp.common.model;


public class GetCategoryResponse {

		private String lcateName;
		private String mcateName;
		private String scateName;
		private String dcateName;
		private long dcateCode;
		public String getLcateName() {
			return lcateName;
		}
		public void setLcateName(String lcateName) {
			this.lcateName = lcateName;
		}
		public String getMcateName() {
			return mcateName;
		}
		public void setMcateName(String mcateName) {
			this.mcateName = mcateName;
		}
		public String getScateName() {
			return scateName;
		}
		public void setScateName(String scateName) {
			this.scateName = scateName;
		}
		public String getDcateName() {
			return dcateName;
		}
		public void setDcateName(String dcateName) {
			this.dcateName = dcateName;
		}
		public long getDcateCode() {
			return dcateCode;
		}
		public void setDcateCode(long dcateCode) {
			this.dcateCode = dcateCode;
		}
		@Override
		public String toString() {
			return "GetCategoryResponse [lcateName=" + lcateName
					+ ", mcateName=" + mcateName + ", scateName=" + scateName
					+ ", dcateName=" + dcateName + ", dcateCode=" + dcateCode
					+ "]";
		}
		
	
}
