package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CertInfoIac {

	// 의료기기 인증 사용 여부
	public CertInfoDetail medicalInstrument;

	// 방송통신기기 인증 사용 여부
	public CertInfoDetail broadcastEquipment;

	// 식품제조가공업 입력사항 여부
	public CertInfoDetail food;

	// 건강기능식품 인증 여부
	public CertInfoDetail healthFood;

	// 친환경 인증 여부
	public CertInfoDetail environmentFriendly;

	public CertInfoDetail getMedicalInstrument() {
		return medicalInstrument;
	}

	public void setMedicalInstrument(CertInfoDetail medicalInstrument) {
		this.medicalInstrument = medicalInstrument;
	}

	public CertInfoDetail getBroadcastEquipment() {
		return broadcastEquipment;
	}

	public void setBroadcastEquipment(CertInfoDetail broadcastEquipment) {
		this.broadcastEquipment = broadcastEquipment;
	}

	public CertInfoDetail getFood() {
		return food;
	}

	public void setFood(CertInfoDetail food) {
		this.food = food;
	}

	public CertInfoDetail getHealthFood() {
		return healthFood;
	}

	public void setHealthFood(CertInfoDetail healthFood) {
		this.healthFood = healthFood;
	}

	public CertInfoDetail getEnvironmentFriendly() {
		return environmentFriendly;
	}

	public void setEnvironmentFriendly(CertInfoDetail environmentFriendly) {
		this.environmentFriendly = environmentFriendly;
	}

	@Override
	public String toString() {
		return "CertInfoIac [medicalInstrument=" + medicalInstrument + ", broadcastEquipment=" + broadcastEquipment
				+ ", food=" + food + ", healthFood=" + healthFood + ", environmentFriendly=" + environmentFriendly
				+ "]";
	}

}
