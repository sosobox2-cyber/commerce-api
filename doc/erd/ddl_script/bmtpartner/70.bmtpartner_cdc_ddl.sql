

create table TPACDCREASON
(
  cdc_reason_code VARCHAR2(3) not null,
  cdc_reason_name VARCHAR2(100) not null,
  cdc_reason_note VARCHAR2(4000),
  cdc_event      VARCHAR2(30),
  cdc_boosting    NUMBER(3) default 0 not null,
  last_cdc_date   DATE,
  use_yn VARCHAR2(3) default '1' not null,
  insert_date     DATE default sysdate not null,
  modify_date     DATE default sysdate not null
)
tablespace TS_BMTPARTNER_DAT;

comment on table TPACDCREASON is '제휴변경데이터캡처사유';
comment on column TPACDCREASON.cdc_reason_code  is '변경사유코드';
comment on column TPACDCREASON.cdc_reason_name  is '변경사유명';
comment on column TPACDCREASON.cdc_reason_note  is '변경사유설명';
comment on column TPACDCREASON.cdc_event  is '변경이벤트';
comment on column TPACDCREASON.cdc_boosting  is '변경가중치';
comment on column TPACDCREASON.last_cdc_date  is '마지막캡처일시';
comment on column TPACDCREASON.use_yn  is '사용여부';
comment on column TPACDCREASON.insert_date  is '등록일시';
comment on column TPACDCREASON.modify_date  is '수정일시';

alter table TPACDCREASON
  add constraint PK_TPACDCRESON primary key (CDC_REASON_CODE)
  using index
  tablespace TS_BMTPARTNER_IDX;

create table TPACDCSNAPSHOT
(
  cdc_snapshot_no NUMBER(18) not null,
  cdc_reason_code VARCHAR2(3) not null,
  target_cnt NUMBER(9) default 0 not null,
  cdc_cnt    NUMBER(9) default 0 not null,
  start_date     DATE default sysdate not null,
  end_date     DATE
)
tablespace TS_BMTPARTNER_DAT
partition by range (start_date) interval (numtoyminterval(1,'month') )
( partition p0 values less than (to_date('2022/01/01','yyyy/mm/dd')));

comment on table TPACDCSNAPSHOT is '제휴CDC스냅샷';
comment on column TPACDCSNAPSHOT.cdc_snapshot_no  is '변경스냅샷번호';
comment on column TPACDCSNAPSHOT.cdc_reason_code  is '변경사유코드';
comment on column TPACDCSNAPSHOT.target_cnt  is '대상건수';
comment on column TPACDCSNAPSHOT.cdc_cnt  is '캡처건수';
comment on column TPACDCSNAPSHOT.start_date  is '캡처시작일시';
comment on column TPACDCSNAPSHOT.end_date  is '캡처종료일시';

alter table TPACDCSNAPSHOT
  add constraint PK_TPACDCSNAPSHOT primary key (CDC_SNAPSHOT_NO)
  using index
  tablespace TS_BMTPARTNER_IDX;

create sequence SEQ_CDC_SNAPSHOT_NO
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
nocache
nocycle;


-------------- 시퀀스는 number 18자리로 지정, long기준으로 작성
create table TPACDCGOODS
(
  goods_code VARCHAR2(10) not null,
  cdc_snapshot_no NUMBER(18) not null,
  ranking NUMBER(9)  default 0 not null,
  insert_date DATE default sysdate not null,
  modify_date DATE default sysdate not null
)
tablespace TS_BMTPARTNER_DAT;

comment on table TPACDCGOODS is '제휴상품변경데이터캡처';
comment on column TPACDCGOODS.goods_code  is '상품코드';
comment on column TPACDCGOODS.cdc_snapshot_no  is '변경스냅샷순번';
comment on column TPACDCGOODS.insert_date  is '등록일시';
comment on column TPACDCGOODS.modify_date  is '수정일시';

alter table TPACDCGOODS
  add constraint PK_TPACDCGOODS primary key (GOODS_CODE)
  using index
  tablespace TS_BMTPARTNER_IDX;


create table TPACDCGOODSSNAPSHOT
(
  goods_code VARCHAR2(10) not null,
  cdc_snapshot_no NUMBER(18) not null,
  insert_date DATE default sysdate not null,
  process_id varchar2(10)
)
tablespace TS_BMTPARTNER_DAT
partition by range (insert_date) interval (numtoyminterval(1,'month') )
( partition p0 values less than (to_date('2022/01/01','yyyy/mm/dd')));

comment on table TPACDCGOODSSNAPSHOT is '제휴상품CDC스냅샷';
comment on column TPACDCGOODSSNAPSHOT.goods_code is '상품코드';
comment on column TPACDCGOODSSNAPSHOT.cdc_snapshot_no  is '변경스냅샷순번';
comment on column TPACDCGOODSSNAPSHOT.insert_date  is '등록일시';
comment on column TPACDCGOODSSNAPSHOT.process_id  is '프로세스ID';

alter table TPACDCGOODSSNAPSHOT
  add constraint PK_TPACDCGOODSSNAPSHOT primary key (GOODS_CODE, CDC_SNAPSHOT_NO)
  using index
  tablespace TS_BMTPARTNER_IDX;


create table TPAGOODSSYNC
(
  goods_sync_no NUMBER(18) not null,
  target_cnt NUMBER(9) default 0 not null,
  proc_cnt NUMBER(9) default 0 not null,
  sync_cnt   NUMBER(9) default 0 not null,
  filter_cnt NUMBER(9) default 0 not null,
  stop_cnt NUMBER(9) default 0 not null,
  start_date DATE default sysdate not null,
  end_date DATE
)
tablespace TS_BMTPARTNER_DAT
partition by range (start_date) interval (numtoyminterval(1,'month') )
( partition p0 values less than (to_date('2022/01/01','yyyy/mm/dd')));

comment on table TPAGOODSSYNC is '제휴상품동기화';
comment on column TPAGOODSSYNC.goods_sync_no  is '상품동기화번호';
comment on column TPAGOODSSYNC.target_cnt  is '대상건수';
comment on column TPAGOODSSYNC.proc_cnt  is '처리건수';
comment on column TPAGOODSSYNC.sync_cnt  is '동기화건수';
comment on column TPAGOODSSYNC.filter_cnt  is '필터건수';
comment on column TPAGOODSSYNC.stop_cnt  is '판매중지건수';
comment on column TPAGOODSSYNC.start_date  is '동기시작일시';
comment on column TPAGOODSSYNC.end_date  is '동기종료일시';

alter table TPAGOODSSYNC
  add constraint PK_TPAGOODSSYNC primary key (GOODS_SYNC_NO)
  using index
  tablespace TS_BMTPARTNER_IDX;

create sequence SEQ_GOODS_SYNC_NO
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
nocache
nocycle;


create table TPAGOODSSYNCLOG
(
  goods_code VARCHAR2(10) not null,
  sync_log_no NUMBER(18) not null,
  cdc_reason_code VARCHAR2(3) not null,
  sync_note VARCHAR2(1000),
  goods_sync_no NUMBER(18),
  pa_group_code VARCHAR2(2),
  insert_date DATE default sysdate not null
)
tablespace TS_BMTPARTNER_DAT
partition by range (insert_date) interval (numtoyminterval(1,'month') )
( partition p0 values less than (to_date('2022/01/01','yyyy/mm/dd')));


comment on table TPAGOODSSYNCLOG is '제휴상품동기화로그';
comment on column TPAGOODSSYNCLOG.goods_code  is '상품코드';
comment on column TPAGOODSSYNCLOG.sync_log_no  is '동기화로그번호';
comment on column TPAGOODSSYNCLOG.cdc_reason_code  is '변경사유코드';
comment on column TPAGOODSSYNCLOG.sync_note  is '동기화내역';
comment on column TPAGOODSSYNCLOG.goods_sync_no  is '상품동기화번호';
comment on column TPAGOODSSYNCLOG.pa_group_code  is '대표제휴사코드';
comment on column TPAGOODSSYNCLOG.insert_date  is '등록일시';


alter table TPAGOODSSYNCLOG
  add constraint PK_TPAGOODSSYNCLOG primary key (GOODS_CODE, SYNC_LOG_NO)
  using index
  tablespace TS_BMTPARTNER_IDX;


create sequence SEQ_SYNC_LOG_NO
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20
cycle;

create table TPAGOODSFILTERLOG
(
  goods_code VARCHAR2(10) not null,
  filter_log_no NUMBER(18) not null,
  filter_type VARCHAR2(100) not null,
  filter_note VARCHAR2(1000),
  goods_sync_no NUMBER(18),
  pa_group_code VARCHAR2(2),
  insert_date DATE default sysdate not null
)
tablespace TS_BMTPARTNER_DAT
partition by range (insert_date) interval (numtoyminterval(1,'month') )
( partition p0 values less than (to_date('2022/01/01','yyyy/mm/dd')));

comment on table TPAGOODSFILTERLOG is '제휴상품필터내역';
comment on column TPAGOODSFILTERLOG.goods_code  is '상품코드';
comment on column TPAGOODSFILTERLOG.filter_log_no  is '상품필터순번';
comment on column TPAGOODSFILTERLOG.filter_type  is '필터유형';
comment on column TPAGOODSFILTERLOG.filter_note  is '필터내역';
comment on column TPAGOODSFILTERLOG.goods_sync_no  is '상품동기화번호';
comment on column TPAGOODSFILTERLOG.pa_group_code  is '대표제휴사코드';
comment on column TPAGOODSFILTERLOG.insert_date  is '등록일시';


alter table TPAGOODSFILTERLOG
  add constraint PK_TPAGOODSFILTERLOG primary key (GOODS_CODE, FILTER_LOG_NO)
  using index
  tablespace TS_BMTPARTNER_IDX;


create sequence SEQ_FILTER_LOG_NO
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20
cycle;


--------------- 프로모션개선
create table TPAGOODSPRICEAPPLY
(
  goods_code VARCHAR2(10) not null,
  pa_group_code VARCHAR2(2) not null,
  pa_code VARCHAR2(2) not null,
  apply_date DATE not null,
  price_apply_seq NUMBER(9) not null,
  price_seq  VARCHAR2(6),
  sale_price NUMBER(15,2) default 0 not null,
  ars_dc_amt NUMBER(15,2) default 0 not null,
  ars_own_dc_amt NUMBER(15,2) default 0 not null,
  ars_entp_dc_amt NUMBER(15,2) default 0 not null,
  lump_sum_dc_amt NUMBER(15,2) default 0 not null,
  lump_sum_own_dc_amt NUMBER(15,2) default 0 not null,
  lump_sum_entp_dc_amt NUMBER(15,2) default 0 not null,
  coupon_promo_no VARCHAR2(12),
  coupon_dc_amt NUMBER(15,2) default 0 not null,
  coupon_own_cost NUMBER(15,2) default 0 not null,
  coupon_entp_cost NUMBER(15,2) default 0 not null,
  best_price NUMBER(15,2) default 0 not null,
  supply_price NUMBER(15,2) default 0 not null,
  commission NUMBER(15,2) default 0 not null,
  insert_id VARCHAR2(10) not null,
  insert_date DATE default sysdate not null,
  trans_id VARCHAR2(10),
  trans_date DATE
)
tablespace TS_BMTPARTNER_DAT;

comment on table TPAGOODSPRICEAPPLY is '제휴상품가격적용';
comment on column TPAGOODSPRICEAPPLY.goods_code  is '상품코드';
comment on column TPAGOODSPRICEAPPLY.pa_group_code  is '대표제휴사코드';
comment on column TPAGOODSPRICEAPPLY.pa_code  is '제휴사코드';
comment on column TPAGOODSPRICEAPPLY.apply_date  is '가격적용일시';
comment on column TPAGOODSPRICEAPPLY.price_apply_seq  is '가격적용순번';
comment on column TPAGOODSPRICEAPPLY.price_seq  is '가격순번';
comment on column TPAGOODSPRICEAPPLY.sale_price  is '판매가';
comment on column TPAGOODSPRICEAPPLY.ars_dc_amt  is 'ARS할인금액';
comment on column TPAGOODSPRICEAPPLY.ars_own_dc_amt  is 'ARS당사부담금액';
comment on column TPAGOODSPRICEAPPLY.ars_entp_dc_amt  is 'ARS업체부담금액';
comment on column TPAGOODSPRICEAPPLY.lump_sum_dc_amt  is '일시불할인금액';
comment on column TPAGOODSPRICEAPPLY.lump_sum_own_dc_amt  is '일시불당사부담금액';
comment on column TPAGOODSPRICEAPPLY.lump_sum_entp_dc_amt  is '일시불업체부담금액';
comment on column TPAGOODSPRICEAPPLY.coupon_promo_no  is '쿠폰프로모션번호';
comment on column TPAGOODSPRICEAPPLY.coupon_dc_amt  is '쿠폰할인금액';
comment on column TPAGOODSPRICEAPPLY.coupon_own_cost  is '쿠폰당사부담금액';
comment on column TPAGOODSPRICEAPPLY.coupon_entp_cost  is '쿠폰업체부담금액';
comment on column TPAGOODSPRICEAPPLY.best_price  is '최종혜택가';
comment on column TPAGOODSPRICEAPPLY.supply_price  is '공급가';
comment on column TPAGOODSPRICEAPPLY.commission  is '수수료율';
comment on column TPAGOODSPRICEAPPLY.insert_id  is '등록자ID';
comment on column TPAGOODSPRICEAPPLY.insert_date  is '등록일시';
comment on column TPAGOODSPRICEAPPLY.trans_id  is '전송자ID';
comment on column TPAGOODSPRICEAPPLY.trans_date  is '전송일시';


alter table TPAGOODSPRICEAPPLY
  add constraint PK_TPAGOODSPRICEAPPLY primary key (GOODS_CODE, PA_GROUP_CODE, PA_CODE, APPLY_DATE, PRICE_APPLY_SEQ)
  using index
  tablespace TS_BMTPARTNER_IDX;


=================================================================================

-- TPROMOTARGETLOG
grant select on TPROMOTARGETLOG to BMTPARTNER;
create or replace synonym TPROMOTARGETLOG
  for BMTCOM.TPROMOTARGETLOG ;


-- TPAGOODSTARGET (개발서버 생성 5.86초 소요: 약 4백만건, 운영서버가 약 4배 많음)
create index IDX_TPAGOODSTARGET_02 on TPAGOODSTARGET (GOODS_CODE)
  tablespace TS_BMTCOM_IDX;


/////////////////// 운영서버 데이터가 많아서 운영중 생성하기 부담스러움.
/////////////////// torderstock의 경우 자주 업뎃이 되므로 정말 필요한 경우만 해야할듯
/////////////////// 4개 테이블의 인덱스는 일단 생성하지 않기로 하고, 운영 적용 후 결정
-- TOFFER (개발서버기준으로 moidfy_date인덱스유무에 따라 성능차이가 크지 않음. 굳이 생성안해도 될듯)
-- create index IDX_TOFFER_01 on TOFFER (MODIFY_DATE)
--  tablespace TS_BMTCOM_IDX;

-- TDESCRIBE (개발서버기준으로 moidfy_date인덱스유무에 따라 성능차이가 크지 않음. 굳이 생성안해도 될듯)
-- create index IDX_TDESCRIBE_01 on TDESCRIBE (MODIFY_DATE)
--  tablespace TS_BMTCOM_IDX;

-- TINPLANQTY
--create index IDX_TINPLANQTY_02 on TINPLANQTY (START_DATE)
--  tablespace TS_BMTCOM_IDX;
--create index IDX_TINPLANQTY_03 on TINPLANQTY (MODIFY_DATE)
--  tablespace TS_BMTCOM_IDX;

-- TORDERSTOCK
--create index IDX_TORDERSTOCK_01 on TORDERSTOCK (MODIFY_DATE)
--  tablespace TS_BMTCOM_IDX;
//////////////////////////////////////////////////


------------------------------------------
--- 상품동기화 이원화 정책에 따른 공통 테이블 분리
------------------------------------------
-- TPAGOODS -> TPAPRODUCT
create table TPAPRODUCT
(
  goods_code              VARCHAR2(10) not null,
  goods_name              VARCHAR2(300) not null,
  sale_gb                 VARCHAR2(2) not null,
  lmsd_code               VARCHAR2(8) not null,
  makeco_code             VARCHAR2(4) not null,
  brand_name              VARCHAR2(100) not null,
  origin_name             VARCHAR2(100) not null,
  tax_yn                  VARCHAR2(1) not null,
  tax_small_yn            VARCHAR2(1) not null,
  adult_yn                VARCHAR2(1) not null,
  order_min_qty           NUMBER(7) not null,
  order_max_qty           NUMBER(7) not null,
  cust_ord_qty_check_term NUMBER(5) default 0,
  do_not_island_dely_yn   VARCHAR2(1) not null,
  entp_code               VARCHAR2(6) not null,
  ship_man_seq            VARCHAR2(3) not null,
  return_man_seq          VARCHAR2(3) not null,
  ship_cost_code          VARCHAR2(5) not null,
  sale_pa_code            VARCHAR2(100) not null,
  avg_dely_leadtime       NUMBER(3) not null,
  origin_code             VARCHAR2(4) not null,
  sale_start_date         DATE,
  sale_end_date           DATE,
  order_create_yn         VARCHAR2(1),
  keyword                 NVARCHAR2(600),
  collect_yn              VARCHAR2(1) default '0' not null,
  last_sync_date          DATE,
  last_describe_sync_date DATE not null,
  ranking                 NUMBER(9) default 0 not null, -- 추가
  insert_id               VARCHAR2(10) not null,
  insert_date             DATE not null,
  modify_id               VARCHAR2(10) not null,
  modify_date             DATE not null
)
tablespace TS_BMTPARTNER_DAT;

comment on table TPAPRODUCT is '제휴상품(공통)';
comment on column TPAPRODUCT.goods_code  is '상품코드';
comment on column TPAPRODUCT.goods_name  is '제휴사 상품명';
comment on column TPAPRODUCT.sale_gb  is '판매상태[B032]';
comment on column TPAPRODUCT.lmsd_code  is '상품분류코드';
comment on column TPAPRODUCT.makeco_code  is '제조사코드';
comment on column TPAPRODUCT.brand_name  is '브랜드명';
comment on column TPAPRODUCT.origin_name  is '원산지명';
comment on column TPAPRODUCT.tax_yn  is '과세여부';
comment on column TPAPRODUCT.tax_small_yn  is '영세여부';
comment on column TPAPRODUCT.adult_yn  is '성인상품여부';
comment on column TPAPRODUCT.order_min_qty  is '주문최소수량';
comment on column TPAPRODUCT.order_max_qty  is '주문최대가능수량';
comment on column TPAPRODUCT.cust_ord_qty_check_term  is '고객주문수량검사기간';
comment on column TPAPRODUCT.do_not_island_dely_yn  is '도서/산간불가여부';
comment on column TPAPRODUCT.entp_code  is '업체코드';
comment on column TPAPRODUCT.ship_man_seq  is '출고담당자순번';
comment on column TPAPRODUCT.return_man_seq  is '회수담당자순번';
comment on column TPAPRODUCT.ship_cost_code  is '배송비정책코드';
comment on column TPAPRODUCT.sale_pa_code  is '판매제휴사[O501]';
comment on column TPAPRODUCT.avg_dely_leadtime  is '평균배송소요일';
comment on column TPAPRODUCT.origin_code  is '원산지코드[B023]';
comment on column TPAPRODUCT.sale_start_date  is '판매시작일';
comment on column TPAPRODUCT.sale_end_date  is '판매종료일시';
comment on column TPAPRODUCT.order_create_yn  is '주문제작여부';
comment on column TPAPRODUCT.keyword  is '검색어';
comment on column TPAPRODUCT.collect_yn  is '착불여부';
comment on column TPAPRODUCT.last_sync_date  is '마지막 동기화 일시';
comment on column TPAPRODUCT.last_describe_sync_date  is '상품상세정보 최종변경일자[TDESCRIBE-모바일]';
comment on column TPAPRODUCT.ranking  is '우선순위';
comment on column TPAPRODUCT.insert_id  is '등록자ID';
comment on column TPAPRODUCT.insert_date  is '등록일시';
comment on column TPAPRODUCT.modify_id  is '수정자ID';
comment on column TPAPRODUCT.modify_date  is '수정일시';

alter table TPAPRODUCT
  add constraint PK_TPAPRODUCT primary key (GOODS_CODE)
  using index
  tablespace TS_BMTPARTNER_IDX;

grant select, insert, update, delete on TPAPRODUCT to BMTCOM;

create or replace synonym BMTCOM.TPAPRODUCT
  for BMTPARTNER.TPAPRODUCT;


-- TPAGOODSDT -> TPAPRODUCTOPTION
create table TPAPRODUCTOPTION
(
  goods_code        VARCHAR2(10) not null,
  goodsdt_code      VARCHAR2(3) not null,
  goodsdt_info      VARCHAR2(300) not null,
  goodsdt_info_kind VARCHAR2(300) not null,
  sort_type         VARCHAR2(10) not null,
  sale_gb           VARCHAR2(2) default '00' not null,
  insert_id         VARCHAR2(10) not null,
  insert_date       DATE not null,
  modify_id         VARCHAR2(10) not null,
  modify_date       DATE not null
)
tablespace TS_BMTPARTNER_DAT;

comment on table TPAPRODUCTOPTION  is '제휴단품(공통)';
comment on column TPAPRODUCTOPTION.goods_code  is '상품코드';
comment on column TPAPRODUCTOPTION.goodsdt_code  is '단품코드';
comment on column TPAPRODUCTOPTION.goodsdt_info  is '단품상세';
comment on column TPAPRODUCTOPTION.goodsdt_info_kind  is '단품상세명(색상/사이즈/무늬/형태)';
comment on column TPAPRODUCTOPTION.sort_type  is '정렬순서(00:등록순, 01:가나다순, 02:가격낮은순)';
comment on column TPAPRODUCTOPTION.insert_id  is '등록자ID';
comment on column TPAPRODUCTOPTION.insert_date  is '등록일시';
comment on column TPAPRODUCTOPTION.modify_id  is '수정자ID';
comment on column TPAPRODUCTOPTION.modify_date  is '수정일시';
comment on column TPAPRODUCTOPTION.sale_gb  is '판매상태';

alter table TPAPRODUCTOPTION
  add constraint PK_TPAPRODUCTOPTION primary key (GOODS_CODE, GOODSDT_CODE)
  using index
  tablespace TS_BMTPARTNER_IDX;

grant select, insert, update, delete on TPAPRODUCTOPTION to BMTCOM;

create or replace synonym BMTCOM.TPAPRODUCTOPTION
  for BMTPARTNER.TPAPRODUCTOPTION;


create table TPAPRODUCTHISTORY
(
  goods_code              VARCHAR2(10) not null,
  history_seq             VARCHAR2(5) not null,
  iud_flag                VARCHAR2(1),
  iud_date                DATE,
  terminal                VARCHAR2(100),
  client_info             VARCHAR2(100),
  goods_name              VARCHAR2(300) not null,
  sale_gb                 VARCHAR2(2) not null,
  lmsd_code               VARCHAR2(8) not null,
  makeco_code             VARCHAR2(4) not null,
  brand_name              VARCHAR2(100) not null,
  origin_name             VARCHAR2(100) not null,
  tax_yn                  VARCHAR2(1) not null,
  tax_small_yn            VARCHAR2(1) not null,
  adult_yn                VARCHAR2(1) not null,
  order_min_qty           NUMBER(7) not null,
  order_max_qty           NUMBER(7) not null,
  cust_ord_qty_check_term NUMBER(5) default 0,
  do_not_island_dely_yn   VARCHAR2(1) not null,
  entp_code               VARCHAR2(6) not null,
  ship_man_seq            VARCHAR2(3) not null,
  return_man_seq          VARCHAR2(3) not null,
  ship_cost_code          VARCHAR2(5) not null,
  sale_pa_code            VARCHAR2(100) not null,
  avg_dely_leadtime       NUMBER(3) not null,
  origin_code             VARCHAR2(4) not null,
  sale_start_date         DATE,
  sale_end_date           DATE,
  order_create_yn         VARCHAR2(1),
  keyword                 NVARCHAR2(600),
  collect_yn              VARCHAR2(1) default '0' not null,
  last_sync_date          DATE,
  last_describe_sync_date DATE not null,
  ranking                 NUMBER(9) default 0 not null,
  insert_id               VARCHAR2(10) not null,
  insert_date             DATE not null,
  modify_id               VARCHAR2(10) not null,
  modify_date             DATE not null
)
tablespace TS_BMTPARTNER_DAT
partition by range (iud_date) interval (numtoyminterval(1,'year') )
( partition p0 values less than (to_date('2022/01/01','yyyy/mm/dd')));

comment on table TPAPRODUCTHISTORY is '제휴상품(공통)이력';
comment on column TPAPRODUCTHISTORY.goods_code  is '상품코드';
comment on column TPAPRODUCTHISTORY.history_seq  is '변경순번';
comment on column TPAPRODUCTHISTORY.iud_flag  is '변경구분 [I/U/D]';
comment on column TPAPRODUCTHISTORY.iud_date  is '변경날짜';
comment on column TPAPRODUCTHISTORY.terminal  is '컴퓨터명';
comment on column TPAPRODUCTHISTORY.client_info  is 'CLIENT IP정보';
comment on column TPAPRODUCTHISTORY.goods_name  is '제휴사 상품명';
comment on column TPAPRODUCTHISTORY.sale_gb  is '판매상태[B032]';
comment on column TPAPRODUCTHISTORY.lmsd_code  is '상품분류코드';
comment on column TPAPRODUCTHISTORY.makeco_code  is '제조사코드';
comment on column TPAPRODUCTHISTORY.brand_name  is '브랜드명';
comment on column TPAPRODUCTHISTORY.origin_name  is '원산지명';
comment on column TPAPRODUCTHISTORY.tax_yn  is '과세여부';
comment on column TPAPRODUCTHISTORY.tax_small_yn  is '영세여부';
comment on column TPAPRODUCTHISTORY.adult_yn  is '성인상품여부';
comment on column TPAPRODUCTHISTORY.order_min_qty  is '주문최소수량';
comment on column TPAPRODUCTHISTORY.order_max_qty  is '주문최대가능수량';
comment on column TPAPRODUCTHISTORY.cust_ord_qty_check_term  is '고객주문수량검사기간';
comment on column TPAPRODUCTHISTORY.do_not_island_dely_yn  is '도서/산간불가여부';
comment on column TPAPRODUCTHISTORY.entp_code  is '업체코드';
comment on column TPAPRODUCTHISTORY.ship_man_seq  is '출고담당자순번';
comment on column TPAPRODUCTHISTORY.return_man_seq  is '회수담당자순번';
comment on column TPAPRODUCTHISTORY.ship_cost_code  is '배송비정책코드';
comment on column TPAPRODUCTHISTORY.sale_pa_code  is '판매제휴사[O501]';
comment on column TPAPRODUCTHISTORY.avg_dely_leadtime  is '평균배송소요일';
comment on column TPAPRODUCTHISTORY.origin_code  is '원산지코드[B023]';
comment on column TPAPRODUCTHISTORY.sale_start_date  is '판매시작일';
comment on column TPAPRODUCTHISTORY.sale_end_date  is '판매종료일시';
comment on column TPAPRODUCTHISTORY.order_create_yn  is '주문제작여부';
comment on column TPAPRODUCTHISTORY.keyword  is '검색어';
comment on column TPAPRODUCTHISTORY.collect_yn  is '착불여부';
comment on column TPAPRODUCTHISTORY.last_sync_date  is '마지막 동기화 일시';
comment on column TPAPRODUCTHISTORY.last_describe_sync_date  is '상품상세정보 최종변경일자[TDESCRIBE-모바일]';
comment on column TPAPRODUCTHISTORY.ranking  is '우선순위';
comment on column TPAPRODUCTHISTORY.insert_id  is '등록자ID';
comment on column TPAPRODUCTHISTORY.insert_date  is '등록일시';
comment on column TPAPRODUCTHISTORY.modify_id  is '수정자ID';
comment on column TPAPRODUCTHISTORY.modify_date  is '수정일시';

alter table TPAPRODUCTHISTORY
  add constraint PK_TPAPRODUCTHISTORY primary key (GOODS_CODE, HISTORY_SEQ)
  using index
  tablespace TS_BMTPARTNER_IDX;

grant select, insert, update, delete on TPAPRODUCTHISTORY to BMTCOM;


----------- TPAPRODUCT 이력생성
CREATE OR REPLACE TRIGGER "TIUDA_TPAPRODUCTHISTORY_HIS"
AFTER  INSERT OR UPDATE OR DELETE ON "TPAPRODUCT"
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW

/*********************************************************************
   SYSTEM  : TIUDA_TPAPRODUCTHISTORY_HIS
   JOBS    : Make a history [TIUDA_TPAPRODUCTHISTORY_HIS]
             when it is modified [TPAPRODUCT]
   CREATOR : CommerceWare
   DATE    : 2022/02/17
   HISTORY :
**********************************************************************/
DECLARE
    v_history_seq VARCHAR2(5);
    v_iud_flag CHAR;
    arg_goods_code VARCHAR2(10);
    v_terminal VARCHAR2(100);
    v_client_info VARCHAR2(100);

    BEGIN

        -- 변경구분
        IF INSERTING THEN
            v_iud_flag := 'I';
            arg_goods_code :=  :new.GOODS_CODE;
        ELSIF UPDATING THEN
            v_iud_flag := 'U';
            arg_goods_code :=  :new.GOODS_CODE;
        ELSE
            v_iud_flag := 'D';
            arg_goods_code :=  :old.GOODS_CODE;
        END IF;

        -- 변경순번
        SELECT LTRIM(TO_CHAR(NVL(MAX(TO_NUMBER(HISTORY_SEQ)), 0) + 1, '00000'))
          INTO v_history_seq
          FROM TPAPRODUCTHISTORY
         WHERE GOODS_CODE = arg_goods_code;

        IF SQL%NOTFOUND THEN
	        v_history_seq := '00001';
        END IF;

        SELECT USERENV('TERMINAL')
             , SYS_CONTEXT('USERENV','IP_ADDRESS')
          INTO v_terminal
             , v_client_info
          FROM DUAL;

         -- delete
        IF DELETING THEN
            INSERT INTO TPAPRODUCTHISTORY (
                      GOODS_CODE
                    , HISTORY_SEQ
                    , IUD_FLAG
                    , IUD_DATE
                    , TERMINAL
                    , CLIENT_INFO
                    , GOODS_NAME
                    , SALE_GB
                    , LMSD_CODE
                    , MAKECO_CODE
                    , BRAND_NAME
                    , ORIGIN_NAME
                    , TAX_YN
                    , TAX_SMALL_YN
                    , ADULT_YN
                    , ORDER_MIN_QTY
                    , ORDER_MAX_QTY
                    , CUST_ORD_QTY_CHECK_TERM
                    , DO_NOT_ISLAND_DELY_YN
                    , ENTP_CODE
                    , SHIP_MAN_SEQ
                    , RETURN_MAN_SEQ
                    , SHIP_COST_CODE
                    , SALE_PA_CODE
                    , AVG_DELY_LEADTIME
                    , ORIGIN_CODE
                    , SALE_START_DATE
                    , SALE_END_DATE
                    , ORDER_CREATE_YN
                    , KEYWORD
                    , COLLECT_YN
                    , LAST_SYNC_DATE
                    , LAST_DESCRIBE_SYNC_DATE
                    , RANKING
                    , INSERT_ID
                    , INSERT_DATE
                    , MODIFY_ID
                    , MODIFY_DATE
            ) VALUES (
                      :old.GOODS_CODE
                    , v_history_seq
                    , v_iud_flag
                    , SYSDATE
                    , v_terminal
                    , v_client_info
                    , :old.GOODS_NAME
                    , :old.SALE_GB
                    , :old.LMSD_CODE
                    , :old.MAKECO_CODE
                    , :old.BRAND_NAME
                    , :old.ORIGIN_NAME
                    , :old.TAX_YN
                    , :old.TAX_SMALL_YN
                    , :old.ADULT_YN
                    , :old.ORDER_MIN_QTY
                    , :old.ORDER_MAX_QTY
                    , :old.CUST_ORD_QTY_CHECK_TERM
                    , :old.DO_NOT_ISLAND_DELY_YN
                    , :old.ENTP_CODE
                    , :old.SHIP_MAN_SEQ
                    , :old.RETURN_MAN_SEQ
                    , :old.SHIP_COST_CODE
                    , :old.SALE_PA_CODE
                    , :old.AVG_DELY_LEADTIME
                    , :old.ORIGIN_CODE
                    , :old.SALE_START_DATE
                    , :old.SALE_END_DATE
                    , :old.ORDER_CREATE_YN
                    , :old.KEYWORD
                    , :old.COLLECT_YN
                    , :old.LAST_SYNC_DATE
                    , :old.LAST_DESCRIBE_SYNC_DATE
                    , :old.RANKING
                    , :old.INSERT_ID
                    , :old.INSERT_DATE
                    , :old.MODIFY_ID
                    , :old.MODIFY_DATE
            );
         ELSE

            INSERT INTO TPAPRODUCTHISTORY (
                      GOODS_CODE
                    , HISTORY_SEQ
                    , IUD_FLAG
                    , IUD_DATE
                    , TERMINAL
                    , CLIENT_INFO
                    , GOODS_NAME
                    , SALE_GB
                    , LMSD_CODE
                    , MAKECO_CODE
                    , BRAND_NAME
                    , ORIGIN_NAME
                    , TAX_YN
                    , TAX_SMALL_YN
                    , ADULT_YN
                    , ORDER_MIN_QTY
                    , ORDER_MAX_QTY
                    , CUST_ORD_QTY_CHECK_TERM
                    , DO_NOT_ISLAND_DELY_YN
                    , ENTP_CODE
                    , SHIP_MAN_SEQ
                    , RETURN_MAN_SEQ
                    , SHIP_COST_CODE
                    , SALE_PA_CODE
                    , AVG_DELY_LEADTIME
                    , ORIGIN_CODE
                    , SALE_START_DATE
                    , SALE_END_DATE
                    , ORDER_CREATE_YN
                    , KEYWORD
                    , COLLECT_YN
                    , LAST_SYNC_DATE
                    , LAST_DESCRIBE_SYNC_DATE
                    , RANKING
                    , INSERT_ID
                    , INSERT_DATE
                    , MODIFY_ID
                    , MODIFY_DATE
            ) VALUES (
                      :new.GOODS_CODE
                    , v_history_seq
                    , v_iud_flag
                    , SYSDATE
                    , v_terminal
                    , v_client_info
                    , :new.GOODS_NAME
                    , :new.SALE_GB
                    , :new.LMSD_CODE
                    , :new.MAKECO_CODE
                    , :new.BRAND_NAME
                    , :new.ORIGIN_NAME
                    , :new.TAX_YN
                    , :new.TAX_SMALL_YN
                    , :new.ADULT_YN
                    , :new.ORDER_MIN_QTY
                    , :new.ORDER_MAX_QTY
                    , :new.CUST_ORD_QTY_CHECK_TERM
                    , :new.DO_NOT_ISLAND_DELY_YN
                    , :new.ENTP_CODE
                    , :new.SHIP_MAN_SEQ
                    , :new.RETURN_MAN_SEQ
                    , :new.SHIP_COST_CODE
                    , :new.SALE_PA_CODE
                    , :new.AVG_DELY_LEADTIME
                    , :new.ORIGIN_CODE
                    , :new.SALE_START_DATE
                    , :new.SALE_END_DATE
                    , :new.ORDER_CREATE_YN
                    , :new.KEYWORD
                    , :new.COLLECT_YN
                    , :new.LAST_SYNC_DATE
                    , :new.LAST_DESCRIBE_SYNC_DATE
                    , :new.RANKING
                    , :new.INSERT_ID
                    , :new.INSERT_DATE
                    , :new.MODIFY_ID
                    , :new.MODIFY_DATE
            );

         END IF;

   END;

/* TIUDA_TPAPRODUCTHISTORY_HIS */

-------------------------------------------------

--- last_sync_date 추가

alter table TPACOPNGOODS add last_sync_date date;
comment on column TPACOPNGOODS.last_sync_date  is '마지막동기화일시';


alter table TPAKAKAOGOODS add last_sync_date date;
comment on column TPAKAKAOGOODS.last_sync_date  is '마지막동기화일시';

alter table TPAGMKTGOODS add last_sync_date date;
comment on column TPAGMKTGOODS.last_sync_date  is '마지막동기화일시';

alter table TPA11STGOODS add last_sync_date date;
comment on column TPA11STGOODS.last_sync_date  is '마지막동기화일시';

alter table TPANAVERGOODS add last_sync_date date;
comment on column TPANAVERGOODS.last_sync_date  is '마지막동기화일시';

alter table TPAWEMPGOODS add last_sync_date date;
comment on column TPAWEMPGOODS.last_sync_date  is '마지막동기화일시';

alter table TPAINTPGOODS add last_sync_date date;
comment on column TPAINTPGOODS.last_sync_date  is '마지막동기화일시';

alter table TPALTONGOODS add last_sync_date date;
comment on column TPALTONGOODS.last_sync_date  is '마지막동기화일시';

alter table TPATMONGOODS add last_sync_date date;
comment on column TPATMONGOODS.last_sync_date  is '마지막동기화일시';

alter table TPASSGGOODS add last_sync_date date;
comment on column TPASSGGOODS.last_sync_date  is '마지막동기화일시';

update TPACOPNGOODS set last_sync_date = modify_date where last_sync_date is null;
update TPAKAKAOGOODS set last_sync_date = modify_date where last_sync_date is null;
update TPAGMKTGOODS set last_sync_date = modify_date where last_sync_date is null;
update TPA11STGOODS set last_sync_date = modify_date where last_sync_date is null;
update TPANAVERGOODS set last_sync_date = modify_date where last_sync_date is null;
update TPAWEMPGOODS set last_sync_date = modify_date where last_sync_date is null;
update TPAINTPGOODS set last_sync_date = modify_date where last_sync_date is null;
update TPALTONGOODS set last_sync_date = modify_date where last_sync_date is null;
update TPATMONGOODS set last_sync_date = modify_date where last_sync_date is null;
update TPASSGGOODS set last_sync_date = modify_date where last_sync_date is null;


alter table TPACOPNGOODS modify last_sync_date default sysdate not null;
alter table TPAKAKAOGOODS modify last_sync_date default sysdate not null;
alter table TPAGMKTGOODS modify last_sync_date default sysdate not null;
alter table TPA11STGOODS modify last_sync_date default sysdate not null;
alter table TPANAVERGOODS modify last_sync_date default sysdate not null;
alter table TPAWEMPGOODS modify last_sync_date default sysdate not null;
alter table TPAINTPGOODS modify last_sync_date default sysdate not null;
alter table TPALTONGOODS modify last_sync_date default sysdate not null;
alter table TPATMONGOODS modify last_sync_date default sysdate not null;
alter table TPASSGGOODS modify last_sync_date default sysdate not null;


----- 카카오쇼핑 파일럿 테스트용 테이블 추가

create table TPAKAKAOPILOT
(
  goods_code VARCHAR2(10) not null,
  insert_date DATE default sysdate not null
)
tablespace TS_BMTPARTNER_DAT;

comment on table TPAKAKAOPILOT is '카카오입점파일럿';
comment on column TPAKAKAOPILOT.goods_code  is '상품코드';
comment on column TPAKAKAOPILOT.insert_date  is '등록일시';

alter table TPAKAKAOPILOT
  add constraint PK_TPAKAKAOPILOT primary key (GOODS_CODE)
  using index
  tablespace TS_BMTPARTNER_IDX;

------- 제휴사별 상품동기화 파일럿 테스트용 테이블 추가

create table TPACDCREASON_PILOT
(
  cdc_reason_code VARCHAR2(3) not null,
  cdc_reason_name VARCHAR2(100) not null,
  cdc_reason_note VARCHAR2(4000),
  cdc_event      VARCHAR2(30),
  cdc_boosting    NUMBER(3) default 0 not null,
  last_cdc_date   DATE,
  use_yn VARCHAR2(3) default '1' not null,
  insert_date     DATE default sysdate not null,
  modify_date     DATE default sysdate not null
)
tablespace TS_BMTPARTNER_DAT;

comment on table TPACDCREASON_PILOT is '제휴변경데이터캡처사유(파일럿테스트)';
comment on column TPACDCREASON_PILOT.cdc_reason_code  is '변경사유코드';
comment on column TPACDCREASON_PILOT.cdc_reason_name  is '변경사유명';
comment on column TPACDCREASON_PILOT.cdc_reason_note  is '변경사유설명';
comment on column TPACDCREASON_PILOT.cdc_event  is '변경이벤트';
comment on column TPACDCREASON_PILOT.cdc_boosting  is '변경가중치';
comment on column TPACDCREASON_PILOT.last_cdc_date  is '마지막캡처일시';
comment on column TPACDCREASON_PILOT.use_yn  is '사용여부';
comment on column TPACDCREASON_PILOT.insert_date  is '등록일시';
comment on column TPACDCREASON_PILOT.modify_date  is '수정일시';

alter table TPACDCREASON_PILOT
  add constraint PK_TPACDCRESON_PILOT primary key (CDC_REASON_CODE)
  using index
  tablespace TS_BMTPARTNER_IDX;


create table TPACDCGOODS_PILOT
(
  goods_code VARCHAR2(10) not null,
  cdc_snapshot_no NUMBER(18) not null,
  ranking NUMBER(9)  default 0 not null,
  insert_date DATE default sysdate not null,
  modify_date DATE default sysdate not null
)
tablespace TS_BMTPARTNER_DAT;

comment on table TPACDCGOODS_PILOT is '제휴상품변경데이터캡처(파일럿테스트)';
comment on column TPACDCGOODS_PILOT.goods_code  is '상품코드';
comment on column TPACDCGOODS_PILOT.cdc_snapshot_no  is '변경스냅샷순번';
comment on column TPACDCGOODS_PILOT.insert_date  is '등록일시';
comment on column TPACDCGOODS_PILOT.modify_date  is '수정일시';

alter table TPACDCGOODS_PILOT
  add constraint PK_TPACDCGOODS_PILOT primary key (GOODS_CODE)
  using index
  tablespace TS_BMTPARTNER_IDX;


 -------- 입점제외된 소싱상품에 대해서 예외적으로 입점시키기 위한 상품 테이블 추가

create table TPASOURCINGEXCEPTINPUT
(
  goods_code    VARCHAR2(10) not null,
  sourcing_code VARCHAR2(20) not null,
  pa_all_yn     VARCHAR2(1) default '1' not null,
  pa_group_code VARCHAR2(20),
  use_yn        VARCHAR2(1) default '1' not null,
  insert_id     VARCHAR2(10) not null,
  insert_date   DATE default sysdate not null,
  modify_id     VARCHAR2(10) not null,
  modify_date   DATE default sysdate not null
)
tablespace TS_BMTPARTNER_DAT;

comment on table TPASOURCINGEXCEPTINPUT  is '제외소싱상품예외입점';
comment on column TPASOURCINGEXCEPTINPUT.goods_code  is '상품코드';
comment on column TPASOURCINGEXCEPTINPUT.sourcing_code  is '소싱코드[B711]';
comment on column TPASOURCINGEXCEPTINPUT.pa_all_yn  is '전체제휴사여부';
comment on column TPASOURCINGEXCEPTINPUT.pa_group_code  is '제휴사그룹코드[O500]';
comment on column TPASOURCINGEXCEPTINPUT.use_yn  is '사용여부';
comment on column TPASOURCINGEXCEPTINPUT.insert_id  is '등록자ID';
comment on column TPASOURCINGEXCEPTINPUT.insert_date  is '등록일시';
comment on column TPASOURCINGEXCEPTINPUT.modify_id  is '수정자ID';
comment on column TPASOURCINGEXCEPTINPUT.modify_date  is '수정일시';

alter table TPASOURCINGEXCEPTINPUT
  add constraint PK_TPASOURCINGEXCEPTINPUT primary key (GOODS_CODE)
  using index
  tablespace TS_BMTPARTNER_IDX;

grant select, insert, update, delete on TPASOURCINGEXCEPTINPUT to BMTCOM;

------- 제외소싱상품예외입점 CDC사유 추가
insert into TPACDCREASON (CDC_REASON_CODE, CDC_REASON_NAME, CDC_REASON_NOTE, CDC_EVENT, CDC_BOOSTING, USE_YN)
values ('131', '제외소싱상품예외입점', '제외소싱상품예외입점/변경', 'SOURCING_EXCEPT_START', 20, '1');



