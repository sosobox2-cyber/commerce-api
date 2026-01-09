package com.cware.api.paintp.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cware.netshopping.common.log.repository.TransLogMapper;

import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@RestController
@RequestMapping(path = "/paintp/v2/payload")
public class PaIntpPayloadApi {

	@Autowired
	TransLogMapper transLogMapper;

	/**
	 * 요청 전문 조회
	 * 
	 * @param transCode
	 * @param transApiNo
	 * @return
	 */
	@GetMapping(value="/{transCode}/{transApiNo}", produces="text/xml;charset=EUC-KR")
	public ResponseEntity<?> payload(@PathVariable String transCode, @PathVariable Long transApiNo) {
		
		String result = transLogMapper.selectRequestPayload(transCode, transApiNo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	

}
