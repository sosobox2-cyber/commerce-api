--------- 제휴상품API연동 개선 --------------

create table TPATRANSBATCH
(
  trans_batch_no NUMBER(18) not null,
  pa_group_code VARCHAR2(2) not null,
  batch_name VARCHAR2(100) not null,
  batch_note VARCHAR2(1000),
  target_cnt NUMBER(9) default 0 not null,
  proc_cnt NUMBER(9) default 0 not null,
  filter_cnt NUMBER(9) default 0 not null,
  success_cnt  NUMBER(9) default 0 not null,
  fail_cnt NUMBER(9) default 0 not null,
  start_date TIMESTAMP default systimestamp not null,
  end_date TIMESTAMP,
  process_id VARCHAR2(10)
)
tablespace TS_BMTPARTNER_DAT
partition by range (start_date) interval (numtoyminterval(1,'month') )
( partition p0 values less than (to_date('2022/06/01','yyyy/mm/dd')));

comment on table TPATRANSBATCH is '제휴연동배치';
comment on column TPATRANSBATCH.trans_batch_no  is '연동배치번호';
comment on column TPATRANSBATCH.pa_group_code  is '대표제휴사코드';
comment on column TPATRANSBATCH.batch_name  is '실행배치명';
comment on column TPATRANSBATCH.batch_note  is '실행배치설명';
comment on column TPATRANSBATCH.target_cnt  is '대상건수';
comment on column TPATRANSBATCH.proc_cnt  is '처리건수';
comment on column TPATRANSBATCH.filter_cnt  is '필터건수';
comment on column TPATRANSBATCH.success_cnt  is '성공건수';
comment on column TPATRANSBATCH.fail_cnt  is '실패건수';
comment on column TPATRANSBATCH.start_date  is '배치시작일시';
comment on column TPATRANSBATCH.end_date  is '배치종료일시';
comment on column TPATRANSBATCH.process_id  is '배치실행프로세스ID';

alter table TPATRANSBATCH
  add constraint PK_TPATRANSBATCH primary key (TRANS_BATCH_NO, START_DATE)
  using index local
  tablespace TS_BMTPARTNER_IDX;

create sequence SEQ_TRANS_BATCH_NO
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
nocache
nocycle;


create table TPATRANSSERVICE
(

  trans_code VARCHAR2(30) not null,
  trans_type VARCHAR2(10) not null,
  trans_service_no NUMBER(18) not null,
  service_name VARCHAR2(100) not null,
  service_note VARCHAR2(1000),
  success_yn VARCHAR2(1) default '0' not null,
  result_code VARCHAR2(100),
  result_msg VARCHAR2(4000),
  trans_batch_no NUMBER(18),
  pa_group_code VARCHAR2(2),
  start_date TIMESTAMP default systimestamp not null,
  end_date TIMESTAMP,
  process_id VARCHAR2(10)
)
tablespace TS_BMTPARTNER_DAT
partition by range (start_date) interval (numtoyminterval(1,'month') )
( partition p0 values less than (to_date('2022/05/01','yyyy/mm/dd')));

comment on table TPATRANSSERVICE is '제휴연동서비스내역';
comment on column TPATRANSSERVICE.trans_code  is '연동대상코드 (상품코드, 업체코드+담당자, 업체코드+담당자+배송비)';
comment on column TPATRANSSERVICE.trans_type  is 'PRODUCT:상품, SELLER:출고/회수지, SHIPCOST: 배송비정책';
comment on column TPATRANSSERVICE.trans_service_no  is '상품연동번호';
comment on column TPATRANSSERVICE.service_name  is '실행서비스명';
comment on column TPATRANSSERVICE.service_note  is '실행서비스설명';
comment on column TPATRANSSERVICE.success_yn  is '성공여부';
comment on column TPATRANSSERVICE.result_code  is '결과코드';
comment on column TPATRANSSERVICE.result_msg  is '결과메시지';
comment on column TPATRANSSERVICE.trans_batch_no  is '연동배치번호';
comment on column TPATRANSSERVICE.pa_group_code  is '제휴사그룹코드';
comment on column TPATRANSSERVICE.start_date  is '연동시작일시';
comment on column TPATRANSSERVICE.end_date  is '연동종료일시';
comment on column TPATRANSSERVICE.process_id  is '서비스프로세스ID';

alter table TPATRANSSERVICE
  add constraint PK_TPATRANSSERVICE primary key (TRANS_CODE, TRANS_TYPE, TRANS_SERVICE_NO, START_DATE)
  using index local
  tablespace TS_BMTPARTNER_IDX;

create sequence SEQ_TRANS_SERVICE_NO
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20
cycle;

create table TPATRANSAPI
(
  trans_code VARCHAR2(30) not null,
  trans_type VARCHAR2(10) not null,
  trans_api_no NUMBER(18) not null,
  api_name VARCHAR2(100) not null,
  api_url VARCHAR2(2000),
  api_note VARCHAR2(1000),
  request_header VARCHAR2(4000),
  request_payload CLOB,
  request_date TIMESTAMP default systimestamp not null,
  response_payload CLOB,
  response_date TIMESTAMP,
  success_yn VARCHAR2(1) default '0' not null,
  result_code VARCHAR2(100),
  result_msg VARCHAR2(4000),
  trans_service_no NUMBER(18),
  pa_group_code VARCHAR2(2),
  process_id VARCHAR2(10)
)
tablespace TS_BMTPARTNER_DAT
partition by range (request_date) interval (numtoyminterval(1,'month') )
( partition p0 values less than (to_date('2022/05/01','yyyy/mm/dd')));

comment on table TPATRANSAPI is '제휴연동API내역';
comment on column TPATRANSAPI.trans_code  is '연동대상코드 (상품코드, 업체코드+담당자, 업체코드+담당자+배송비)';
comment on column TPATRANSAPI.trans_type  is 'PRODUCT:상품, SELLER:출고/회수지, SHIPCOST: 배송비정책';
comment on column TPATRANSAPI.trans_api_no  is '연동API번호';
comment on column TPATRANSAPI.api_name  is '호출API명';
comment on column TPATRANSAPI.api_url  is '호출API URL';
comment on column TPATRANSAPI.api_note  is '호출API설명';
comment on column TPATRANSAPI.request_header  is 'API요청헤더';
comment on column TPATRANSAPI.request_payload  is 'API요청전문';
comment on column TPATRANSAPI.request_date  is 'API요청일시';
comment on column TPATRANSAPI.response_payload  is 'API응답전문';
comment on column TPATRANSAPI.response_date  is 'API응답일시';
comment on column TPATRANSAPI.success_yn  is '성공여부';
comment on column TPATRANSAPI.result_code  is '결과코드';
comment on column TPATRANSAPI.result_msg  is '결과메시지';
comment on column TPATRANSAPI.trans_service_no  is '상품연동번호';
comment on column TPATRANSAPI.pa_group_code  is '제휴사그룹코드';
comment on column TPATRANSAPI.process_id  is '서비스프로세스ID';

alter table TPATRANSAPI
  add constraint PK_TPATRANSAPI primary key (TRANS_CODE, TRANS_TYPE, TRANS_API_NO, REQUEST_DATE)
  using index local
  tablespace TS_BMTPARTNER_IDX;

create sequence SEQ_TRANS_API_NO
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20
cycle;

------------------------- 쿠팡 연동 개선
-- 재고전송여부 추가  (운영에서 생성시 널로 생성하여 이후 데이터 '0'업데이트 하도록 함)
alter table BMTPARTNER.TPAGOODSDTMAPPING add trans_stock_yn VARCHAR2(1) default '0' not null;
comment on column BMTPARTNER.TPAGOODSDTMAPPING.trans_stock_yn
  is '재고전송여부';


------------------------ 11번가 연동 개선
-- 배송비정책 연동 개선

create table TPA11STCNSHIPCOST
(
  entp_code VARCHAR2(6) not null,
  entp_man_seq VARCHAR2(3) not null,
  pa_code VARCHAR2(2) not null,
  ship_cost_code VARCHAR2(5) not null,
  ship_cost_base_amt NUMBER(10,2) default 0 not null,
  ord_cost_amt NUMBER(10,2) default 0 not null,
  pa_addr_seq VARCHAR2(10),
  pa_addr_nm VARCHAR2(30),
  trans_target_yn VARCHAR2(1) default '0' not null,
  trans_date date,
  insert_id VARCHAR2(10) not null,
  insert_date date default sysdate not null,
  modify_id VARCHAR2(10) not null,
  modify_date date default sysdate not null
)
tablespace TS_BMTPARTNER_DAT;

comment on table TPA11STCNSHIPCOST is '11번가출고지조건부배송비';
comment on column TPA11STCNSHIPCOST.entp_code  is '업체코드';
comment on column TPA11STCNSHIPCOST.entp_man_seq  is '업체담당자순번';
comment on column TPA11STCNSHIPCOST.pa_code  is '제휴사코드';
comment on column TPA11STCNSHIPCOST.ship_cost_code  is '배송비정책코드';
comment on column TPA11STCNSHIPCOST.ship_cost_base_amt  is '배송비기준금액';
comment on column TPA11STCNSHIPCOST.ord_cost_amt  is '주문배송비';
comment on column TPA11STCNSHIPCOST.pa_addr_seq  is '제휴사주소순번';
comment on column TPA11STCNSHIPCOST.pa_addr_nm  is '제휴사주소명';
comment on column TPA11STCNSHIPCOST.trans_target_yn  is '전송대상여부';
comment on column TPA11STCNSHIPCOST.trans_date  is '전송일시';
comment on column TPA11STCNSHIPCOST.insert_id  is '등록자ID';
comment on column TPA11STCNSHIPCOST.insert_date  is '등록일시';
comment on column TPA11STCNSHIPCOST.modify_id  is '수정자ID';
comment on column TPA11STCNSHIPCOST.modify_date  is '수정일시';

alter table TPA11STCNSHIPCOST
  add constraint PK_TPA11STCNSHIPCOST primary key (ENTP_CODE, ENTP_MAN_SEQ, PA_CODE, SHIP_COST_CODE)
  using index
  tablespace TS_BMTPARTNER_IDX;


------------------------- SSG 연동 개선
-- 재고전송여부 추가  (운영에서 생성시 널로 생성하여 이후 데이터 '0'업데이트 하도록 함)
alter table BMTPARTNER.TPASSGGOODSDTMAPPING add trans_stock_yn VARCHAR2(1) default '0' not null;
comment on column BMTPARTNER.TPASSGGOODSDTMAPPING.trans_stock_yn
  is '재고전송여부';

