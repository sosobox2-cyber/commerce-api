package com.cware.partner.sync.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.entity.PaFapleShipCost;
import com.cware.partner.sync.domain.id.PaFapleShipCostId;

@Repository
public interface PaFapleShipCostRepository extends JpaRepository<PaFapleShipCost, PaFapleShipCostId> {

	// 업체주소변경리스트
	@Query(value = "select p "
			+ "  from PaFapleShipCost p "
			+ " inner join EntpUser e on p.entpCode = e.entpCode and p.shipManSeq   = e.entpManSeq "
			+ " inner join EntpUser r on p.entpCode = r.entpCode and p.returnManSeq = r.entpManSeq "
			+ " where (p.lastEntpSyncDate  <= e.modifyDate) OR  (p.lastEntpSyncDate  <= r.modifyDate)"
			)
	Slice<PaFapleShipCost> findModifyTargetList(Pageable pageRequest);
	
	
	@Query(value = "select GREATEST( "
			+ " GREATEST(cd2.ret_cost_amt, cd3.ret_cost_amt), " 
		    + " GREATEST(cd2.exch_cost_amt, cd3.exch_cost_amt) ) " 
			+ " from tshipcostm cm "
			+ " inner join tshipcostdt cd1 on cm.entp_code = cd1.entp_code and cm.ship_cost_code = cd1.ship_cost_code and cd1.cust_area_gb = '10' "
			+ " inner join tshipcostdt cd2 on cm.entp_code = cd2.entp_code and cm.ship_cost_code = cd2.ship_cost_code and cd2.cust_area_gb = '20' "
			+ " inner join tshipcostdt cd3 on cm.entp_code = cd3.entp_code and cm.ship_cost_code = cd3.ship_cost_code and cd3.cust_area_gb = '30' "
			+ " where cm.entp_code = :entpCode "
			+ " and cm.ship_cost_code = :shipCostCode "
			+ " and cd1.apply_date = cd2.apply_date "
			+ " and cd1.apply_date = cd3.apply_date "
			+ " and cd1.apply_date = ( select max(cds.apply_date) from tshipcostdt cds where cds.entp_code = cm.entp_code and cds.ship_cost_code = cm.ship_cost_code and cds.apply_date < sysdate ) "
			, nativeQuery = true)
	Double getIslandReturnShipCost(String entpCode, String shipCostCode);
	
	
	
}
