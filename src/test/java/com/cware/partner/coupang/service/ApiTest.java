package com.cware.partner.coupang.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.cware.partner.common.domain.ResponseMsg;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class ApiTest {

	@Autowired
	private RestTemplate restTemplate;

	@Test
	void temp() {
		System.out.println(Math.floorMod(0, 100));
	}

	@PersistenceContext
	EntityManager entityManager;

	@Test
	void test() {
		String URI = "https://pa-api.skstoa.com:8443/pacopn/goods/goods-insert?goodsCode={goodsCode}&paCode={paCode}";

		Map<String, String> params = new HashMap<String, String>();
		params.put("goodsCode", "24846235");
		params.put("paCode", "52");

		ResponseMsg result = restTemplate.getForObject(URI, ResponseMsg.class, params);

		System.out.println(result);

	}

	@Test
	void testTemp() {

		Timestamp testDate = (Timestamp) entityManager.createNativeQuery("select current_date from dual")
				.getSingleResult();

		System.out.println(testDate);
		System.out.println(Timestamp.from(testDate.toInstant().minusSeconds(5)));

	}

	@Test
	void testGoodsStop() {

		String URI = "https://pa-api.skstoa.com:8443/pacopn/goods/goods-sell-stop?goodsCode={goodsCode}&paCode={paCode}";
		String[] goodsList = { "21979600", "22135403", "22981944", "22981945", "23148166", "23617049", "24081349",
				"24260375", "24269946", "24295196", "24303799", "24412961", "24490897", "24652009", "24753399",
				"24815208", "24817300", "24838891", "24882684", "24983400", "25175544", "25250839", "25257051",
				"25260079", "25288635", "25290355", "25294151", "25295904", "25298072", "25419372", "25419379",
				"25419393", "25470064", "25518179", "25640119", "25684616", "25689187", "25689188", "25715185",
				"25715196", "25715315", "25715435", "25722811", "25769556", "25844348", "25880155", "25884559",
				"25886357", "25966506", "25976262", "26052992", "26068025", "26114406", "26183594", "26183602",
				"26199039", "26239570", "26239601", "26239602", "26261280", "26298198", "26298365", "26476080",
				"26476081", "26476082", "26476083", "26501732", "26501733", "26501734", "26501735", "26531679",
				"26558428", "26559299", "26603002", "26671325", "26675118", "26676201", "26713829" };

		Map<String, String> params = new HashMap<String, String>();
		params.put("paCode", "52");

		ResponseMsg result;

		int cnt = 0;
		for (String goodsCode : goodsList) {
			params.put("goodsCode", goodsCode);
			result = restTemplate.getForObject(URI, ResponseMsg.class, params);
			log.info("No.{} {} Result:{}", ++cnt, goodsCode, result);
		}

	}

	@Test
	void testGoodsModify() {

		String URI = "https://pa-api.skstoa.com:8443/pacopn/goods/goods-modify?goodsCode={goodsCode}&paCode={paCode}&searchTermGb=1";
		String[] goodsList = { "20069234", "20069235", "21514389", "21877777", "21918886", "21918891", "21920521",
				"22098142", "22373859", "22379504", "22379505", "22617611", "22665931", "23028607", "23147106",
				"23319361", "23320288", "23405769", "23405772", "23405773", "23477051", "23493085", "23535250",
				"23650638", "23778068", "24017501", "24047011", "24153386", "24155696", "24155885", "24155950",
				"24156001", "24157224", "24190267", "24193636", "24258618", "24275265", "24349505", "24360778",
				"24728601", "24798719", "24867046", "24952991", "25166552", "25168869", "25168870", "25168871",
				"25246286", "25362828", "25415323", "25450621", "25450658", "25454715", "25504952", "25504957",
				"25654026", "25714348", "25715890", "25718018", "25776648", "25795556", "25827960", "25861608",
				"25906806", "25929022", "25929259", "25982882", "26049925", "26195195", "26196231", "26201746",
				"26201929", "26209243", "26243095", "26253615", "26264351", "26286373", "26307863", "26313366",
				"26314215", "26321263", "26355846", "26355853", "26368973", "26368974", "26369002", "26369004",
				"26369024", "26369026", "26369028", "26369040", "26369072", "26369109", "26369110", "26369150",
				"26369151", "26369162", "26415661", "26460228", "26498266", "26558211", "26558227", "26558254",
				"26558280", "26558287", "26558298", "26558381", "26558476", "26558924", "26558925", "26558931",
				"26558933", "26558981", "26559192", "26559194", "26559304", "26559588", "26560379", "26560533",
				"26561319", "26561953", "26563269", "26565036", "26566441", "26585150", "26598435", "26676210",
				"26698532", "26698618", "26699720", "26721704" };

		Map<String, String> params = new HashMap<String, String>();
		params.put("paCode", "52");

		ResponseMsg result;

		int cnt = 0;
		for (String goodsCode : goodsList) {
			params.put("goodsCode", goodsCode);
			result = restTemplate.getForObject(URI, ResponseMsg.class, params);
			log.info("No.{} {} Result:{}", ++cnt, goodsCode, result);
		}

	}

	@Test
	void testGoodsModifyTv() {

		String URI = "https://pa-api.skstoa.com:8443/pacopn/goods/goods-modify?goodsCode={goodsCode}&paCode={paCode}&searchTermGb=1";
		String[] goodsList = { "23686379", "25243874", "26043935", "26155454" };

		Map<String, String> params = new HashMap<String, String>();
		params.put("paCode", "51");

		ResponseMsg result;

		int cnt = 0;
		for (String goodsCode : goodsList) {
			params.put("goodsCode", goodsCode);
			result = restTemplate.getForObject(URI, ResponseMsg.class, params);
			log.info("No.{} {} Result:{}", ++cnt, goodsCode, result);
		}

	}
}
