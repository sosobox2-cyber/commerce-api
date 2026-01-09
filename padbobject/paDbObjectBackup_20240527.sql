------------------------------------------------
-- Export file for user BMTCOM@SKB_IDC_REAL   --
-- Created by hham3 on 2024-05-27, 오후 4:26:20 --
------------------------------------------------

set define off
spool paDbObjectBackup_20240527.log

prompt
prompt Creating function FUN_GET_PAGOODS_INFO_CHECK
prompt ============================================
prompt
CREATE OR REPLACE FUNCTION BMTCOM.FUN_GET_PAGOODS_INFO_CHECK (
    arg_pa_group_code   IN VARCHAR2,
    arg_goods_code      IN TPAGOODS.GOODS_CODE%TYPE,
    margin_chk_type     IN VARCHAR2 DEFAULT '0'
)
    RETURN VARCHAR2
IS
    /**********************************************
     create date : 2019.11.14
     create by   : cware

     return      : check msg
    ***********************************************/
    v_check_msg       VARCHAR2(200) :='';
    v_shipcode_check  VARCHAR2(30);
    v_sign_gb       VARCHAR2(2);
    i_broad_margin  INTEGER := 10;
    i_online_margin INTEGER := 10;
    i_broad_min_price  INTEGER := 100;
    i_online_min_price INTEGER := 100;
    l_stock_broad_rate LONG := 0.7;
    l_stock_online_rate LONG := 0.7;
    v_code_check        VARCHAR2(1) := '1';
    v_margin_rate_check VARCHAR2(30) := '';
    v_sale_price_check  VARCHAR2(30) := '';
    v_stock_check       VARCHAR2(30) := '';
    v_ombudsman_check   VARCHAR2(30) := '';
    v_broad_sale_check  VARCHAR2(30) := '';
    v_invi_goods_type_check VARCHAR2(30) := '';
    v_order_media_check VARCHAR2(30) := '';
    v_sale_end_check    VARCHAR2(30) := '';

BEGIN

    IF arg_pa_group_code IS NULL OR arg_goods_code IS NULL THEN
        v_check_msg := '점검 입력데이터 확인';
        RETURN v_check_msg;
    END IF;

    BEGIN
        SELECT GD.SIGN_GB
          into v_sign_gb
          FROM TGOODS  GD
         WHERE GD.GOODS_CODE = arg_goods_code
       ;
    EXCEPTION
    WHEN OTHERS THEN
         v_check_msg := '결재단계 점검';
    END;

    BEGIN
        SELECT
              CASE
                  WHEN arg_pa_group_code ='04' OR SUBSTR(CM.SHIP_COST_CODE,0,2) != 'QN' THEN
                       ''
                  ELSE
                      '배송비'
                   END
              INTO v_shipcode_check
         FROM TSHIPCOSTM  CM
            , TGOODS      TG
       WHERE DECODE(TG.DELY_TYPE, '20', TG.ENTP_CODE, '100001') = CM.ENTP_CODE
         AND TG.SHIP_COST_CODE = CM.SHIP_COST_CODE
         AND TG.GOODS_CODE     = arg_goods_code
        ;

        v_check_msg := v_shipcode_check;

    EXCEPTION
    WHEN OTHERS THEN
         v_check_msg := '배송비 점검 오류';
    END;

    BEGIN
        -- 마진 조회
        SELECT TC.REMARK
             , TC.REMARK1
          INTO i_broad_margin
             , i_online_margin
          FROM TCODE TC
         WHERE TC.CODE_LGROUP = 'O582'
           AND TC.CODE_MGROUP = CASE
                                    WHEN arg_pa_group_code = '01' THEN
                                         '10'
                                      WHEN arg_pa_group_code = '02' THEN
                                         '30'    
                                     WHEN arg_pa_group_code = '03' THEN
                                         '30'  
                                    WHEN arg_pa_group_code = '04' THEN
                                         '50'
                                    WHEN arg_pa_group_code = '05' THEN
                                         '60'
                                    WHEN arg_pa_group_code = '10' THEN
                                         '86'     
                                    WHEN arg_pa_group_code = '12' THEN
                                         '90'
                                    ELSE
                                         '70'
                                 END
          ;
        -- 최소 판매가 조회
        SELECT TC.REMARK
             , TC.REMARK1
          INTO i_broad_min_price
             , i_online_min_price
          FROM TCODE TC
         WHERE TC.CODE_LGROUP = 'O582'
           AND TC.CODE_MGROUP = CASE
                                    WHEN arg_pa_group_code = '01' THEN
                                         '20'
                                    WHEN arg_pa_group_code = '04' THEN
                                         '51'
                                    WHEN arg_pa_group_code = '05' THEN
                                         '61'
                                    ELSE
                                         '40'
                                 END
          ;

        -- 방송 재고비율 조회
        SELECT TC.CODE_GROUP
          INTO l_stock_online_rate
          FROM TCODE TC
         WHERE TC.CODE_LGROUP = 'O505'
           AND TC.CODE_MGROUP = CASE
                                    WHEN arg_pa_group_code = '01' THEN
                                         '11'
                                    WHEN arg_pa_group_code = '04' THEN
                                         '41'
                                    WHEN arg_pa_group_code = '05' THEN
                                         '51'
                                    ELSE
                                         '21'
                                 END
          ;

        -- 온라인  재고비율 조회
        SELECT TC.CODE_GROUP
          INTO l_stock_online_rate
          FROM TCODE TC
         WHERE TC.CODE_LGROUP = 'O505'
           AND TC.CODE_MGROUP = CASE
                                    WHEN arg_pa_group_code = '01' THEN
                                         '12'
                                    WHEN arg_pa_group_code = '04' THEN
                                         '41'
                                    WHEN arg_pa_group_code = '05' THEN
                                         '51'
                                    ELSE
                                         '22'
                                 END
          ;


    EXCEPTION
    WHEN OTHERS THEN
         v_check_msg  := v_check_msg||' , 기초코드 점검 오류';
         v_code_check := '0';
    END;


    BEGIN
        IF v_code_check ='1' THEN
            IF v_sign_gb = '80' THEN
                SELECT CASE
                           WHEN DECODE(GP.SALE_PRICE, 0, 0, TRUNC((GP.SALE_PRICE - GP.BUY_PRICE) / GP.SALE_PRICE * 100))
                                >= DECODE(G.SOURCING_MEDIA, '01', i_broad_margin, i_online_margin) THEN
                                ''
                           WHEN ( SELECT COUNT(1)
                                    FROM TPAGOODSEVENT SD
                                   WHERE SYSDATE BETWEEN SD.START_DATE
                                                     AND SD.END_DATE
                                     AND SD.PA_GROUP_CODE = arg_pa_group_code
                                     AND SD.USE_YN = '1'
                                     AND DECODE(GP.SALE_PRICE, 0, 0, TRUNC((GP.SALE_PRICE - GP.BUY_PRICE) / GP.SALE_PRICE * 100, 2)) >= TO_NUMBER(FUN_GETCONFIG('PA_EVENT_LIMIT_MARGIN'))                                     
                                     AND SD.GOODS_CODE = G.GOODS_CODE ) > 0 THEN
                                ''
                           WHEN margin_chk_type = '1' THEN
                                ''
                           ELSE
                                ' ,마진율'
                        END MARGIN_CHECK
                     , CASE
                           WHEN GP.SALE_PRICE > DECODE(SOURCING_MEDIA, '01', i_broad_min_price, i_online_min_price) THEN
                                ''
                           ELSE
                                ' ,최소 판매가'
                        END SALE_PRICE_CHECK
                     , CASE
                           WHEN G.SOURCING_MEDIA = '01' THEN
                                CASE
                                    WHEN CEIL(fun_get_order_able_qty(G.GOODS_CODE, '', G.WH_CODE) * l_stock_broad_rate) >= 3 OR G.BUY_MED ='13' THEN
                                        ''
                                    ELSE
                                        ' ,재고'
                                END
                           ELSE
                                CASE
                                    WHEN CEIL(fun_get_order_able_qty(G.GOODS_CODE, '', G.WH_CODE) * l_stock_online_rate) >= 3 THEN
                                        ''
                                    ELSE
                                        ' ,재고'
                                 END
                        END AS STOCK_CHECK
                     , CASE
                           WHEN G.OMBUDSMAN_YN = '0' THEN
                               ''
                           ELSE
                               ' ,옴부즈맨 '
                        END AS OMBUDSMAN_CHECK
                     , CASE
                           WHEN G.BROAD_SALE_YN = '0' THEN
                               ''
                           ELSE
                               ' ,방송중 판매 '
                       END AS BROAD_SALE_CHECK
                     , CASE
                           WHEN G.INVI_GOODS_TYPE = '00' THEN
                                ''
                           ELSE
                               ' ,무형상품'
                       END AS INVI_GOODS_TYPE_CHECK
                     , CASE
                           WHEN G.ORDER_MEDIA_ALL_YN = '1' OR INSTR(G.ORDER_MEDIA, '62') > 0  OR INSTR(G.ORDER_MEDIA, '61') > 0 THEN
                               ''
                           ELSE
                               ' ,주문매체'
                       END AS ORDER_MEDIA_CHECK
                     , CASE
                       WHEN NVL(G.SALE_END_DATE, SYSDATE + 1) > SYSDATE THEN
                               ''
                           ELSE
                               ' ,판매종료일'
                       END AS SALE_END_CHECK
                 INTO  v_margin_rate_check
                     , v_sale_price_check
                     , v_stock_check
                     , v_ombudsman_check
                     , v_broad_sale_check
                     , v_invi_goods_type_check
                     , v_order_media_check
                     , v_sale_end_check
                 FROM TGOODS      G
                    , TGOODSPRICE GP
                WHERE GP.GOODS_CODE = G.GOODS_CODE
                  AND GP.APPLY_DATE = ( SELECT /*+ INDEX_DESC (GPS PK_TGOODSPRICE)*/
                                               GPS.APPLY_DATE
                                          FROM TGOODSPRICE GPS
                                         WHERE GPS.GOODS_CODE = G.GOODS_CODE
                                           AND GPS.APPLY_DATE <= SYSDATE
                                           AND ROWNUM = 1
                                       )
                   AND G.GOODS_CODE = arg_goods_code
           ;
           ELSE
               SELECT CASE
                           WHEN DECODE(GP.SALE_PRICE, 0, 0, TRUNC((GP.SALE_PRICE - GP.BUY_PRICE) / GP.SALE_PRICE * 100))
                                >= DECODE(G.SOURCING_MEDIA, '01', i_broad_margin, i_online_margin) THEN
                                ''
                           WHEN ( SELECT COUNT(1)
                                    FROM TPAGOODSEVENT SD
                                   WHERE SYSDATE BETWEEN SD.START_DATE
                                                     AND SD.END_DATE
                                     AND SD.PA_GROUP_CODE = arg_pa_group_code
                                     AND SD.USE_YN = '1'
                                     AND DECODE(GP.SALE_PRICE, 0, 0, TRUNC((GP.SALE_PRICE - GP.BUY_PRICE) / GP.SALE_PRICE * 100, 2)) >= TO_NUMBER(FUN_GETCONFIG('PA_EVENT_LIMIT_MARGIN'))                                     
                                     AND SD.GOODS_CODE = G.GOODS_CODE ) > 0 THEN
                                ''
                           WHEN margin_chk_type = '1' THEN
                                ''
                           ELSE
                                ' ,마진율'
                        END MARGIN_CHECK
                     , CASE
                           WHEN GP.SALE_PRICE > DECODE(SOURCING_MEDIA, '01', i_broad_min_price, i_online_min_price) THEN
                                ''
                           ELSE
                                ' ,최소 판매가'
                        END SALE_PRICE_CHECK
                     , CASE
                           WHEN G.SOURCING_MEDIA = '01' THEN
                                CASE
                                    WHEN CEIL(fun_get_order_able_qty(G.GOODS_CODE, '', G.WH_CODE) * l_stock_broad_rate) >= 3 OR G.BUY_MED ='13' THEN
                                        ''
                                    ELSE
                                        ' ,재고'
                                END
                           ELSE
                                CASE
                                    WHEN CEIL(fun_get_order_able_qty(G.GOODS_CODE, '', G.WH_CODE) * l_stock_online_rate) >= 3 THEN
                                        ''
                                    ELSE
                                        ' ,재고'
                                 END
                        END AS STOCK_CHECK
                     , CASE
                           WHEN G.OMBUDSMAN_YN = '0' THEN
                               ''
                           ELSE
                               ' ,옴부즈맨 '
                        END AS OMBUDSMAN_CHECK
                     , CASE
                           WHEN G.BROAD_SALE_YN = '0' THEN
                               ''
                           ELSE
                               ' ,방송중 판매 '
                       END AS BROAD_SALE_CHECK
                     , CASE
                           WHEN G.INVI_GOODS_TYPE = '00' THEN
                                ''
                           ELSE
                               ' ,무형상품'
                       END AS INVI_GOODS_TYPE_CHECK
                     , CASE
                           WHEN G.ORDER_MEDIA_ALL_YN = '1' OR INSTR(G.ORDER_MEDIA, '62') > 0  OR INSTR(G.ORDER_MEDIA, '61') > 0 THEN
                               ''
                           ELSE
                               ' ,주문매체'
                       END AS ORDER_MEDIA_CHECK
                     , CASE
                       WHEN NVL(G.SALE_END_DATE, SYSDATE + 1) > SYSDATE THEN
                               ''
                           ELSE
                               ' ,판매종료일'
                       END AS SALE_END_CHECK
                 INTO  v_margin_rate_check
                     , v_sale_price_check
                     , v_stock_check
                     , v_ombudsman_check
                     , v_broad_sale_check
                     , v_invi_goods_type_check
                     , v_order_media_check
                     , v_sale_end_check
                 FROM TGOODS      G
                    , TGOODSSIGN  GP
                WHERE GP.GOODS_CODE = G.GOODS_CODE
                  AND GP.SIGN_SEQ = ( SELECT /*+ INDEX_DESC (GPS PK_TGOODSSIGN)*/
                                             GPS.SIGN_SEQ
                                        FROM TGOODSSIGN GPS
                                       WHERE GPS.GOODS_CODE = G.GOODS_CODE
                                         AND GPS.APPLY_DATE <= SYSDATE
                                         AND ROWNUM = 1
                                      )
                   AND G.GOODS_CODE = arg_goods_code
                   ;
           END IF
           ;

           v_check_msg := v_check_msg||v_margin_rate_check||v_sale_price_check||v_stock_check||v_ombudsman_check||v_broad_sale_check||v_invi_goods_type_check||v_order_media_check||v_sale_end_check;

       END IF;

    IF v_check_msg IS NULL THEN
        v_check_msg := '000000';
    ELSE
        v_check_msg := '연동제약사항 체크 : '||v_check_msg;
    END IF
    ;

    EXCEPTION
    WHEN OTHERS THEN
         v_check_msg := v_check_msg||', 상품 점검 오류';
    END;

    RETURN v_check_msg;

END FUN_GET_PAGOODS_INFO_CHECK;
/

prompt
prompt Creating function FUN_GET_PAGOODS_LNKG_CHECK
prompt ============================================
prompt
CREATE OR REPLACE FUNCTION BMTCOM.FUN_GET_PAGOODS_LNKG_CHECK (
    arg_pa_group_code   IN VARCHAR2 DEFAULT NULL
  , arg_goods_code      IN TPAGOODS.GOODS_CODE%TYPE
)
    RETURN VARCHAR2
IS
/**********************************************
    Job     : 제휴상품 연동 체크
    Date    : 2021.03.29
    Creator : 정명훈
    return  : check msg
***********************************************/
    v_check_msg             VARCHAR2(300) :='';
    v_sale_price_chk    NUMBER := 0;    /* 판매가 100원미만 연동불가 */
    v_ombudsman_chk     NUMBER := 0;  /* 옴부즈맨 상품 연동불가 */
    v_order_media_chk   NUMBER := 0;  /* 쇼핑몰/모바일 주문 불가 상품 연동불가 */
    v_broad_sale_chk    NUMBER := 0;  /* 방송 중 구매조건 상품 연동불가 */
    v_invi_goods_type_chk   NUMBER := 0;  /* 무형상품 연동 불가 */
    v_goods_except_chk    NUMBER := 0;  /* 상품제외대상 */
    v_entp_except_chk   NUMBER := 0;  /* 업체제외대상 */
    v_brand_except_chk    NUMBER := 0;  /* 브랜드제외대상 */
    v_goods_kind_except_chk NUMBER := 0;  /* 상품분류제외대상 */
    v_keyword_except_chk  NUMBER := 0;  /* 키워드제외대상 */
    v_sale_start_end_chk  NUMBER := 0;  /* 상품 판매기간 오류 */
    v_pagoods_kind_chk    NUMBER := 0;  /* 제휴사 카테고리 미존재 */
    v_qa_chk            NUMBER := 0;  /* QA승인 체크 */
    v_lnkg_cncl_chk         VARCHAR2(10)  :=''; /* 연동해제 체크 */
    v_lnkg_cncl_note        VARCHAR2(300) :=''; /* 연동해제사유 */

    v_goods_target_cnt  NUMBER := 0;
    v_goods_filter_log_note VARCHAR2(300)  :='';


/* 판매가 100원미만 연동불가 */
PROCEDURE SP_SALE_PRICE_CHECK(in_goods_code IN VARCHAR2) IS
  BEGIN
    SELECT COUNT(*)
      INTO v_sale_price_chk
      FROM TGOODSPRICE GP
     WHERE GP.SALE_PRICE < 100
       AND GP.GOODS_CODE = in_goods_code
       AND GP.APPLY_DATE = (SELECT MAX(GPS.APPLY_DATE)
                              FROM TGOODSPRICE GPS
                             WHERE GPS.GOODS_CODE = GP.GOODS_CODE
                               AND GPS.APPLY_DATE <= SYSDATE )
    ;
  EXCEPTION
  WHEN OTHERS THEN
       v_sale_price_chk := 0;
  END;

/* 옴부즈맨 상품 연동불가 */
PROCEDURE SP_OMBUDSMAN_CHECK(in_goods_code IN VARCHAR2) IS
  BEGIN
    SELECT COUNT(*)
      INTO v_ombudsman_chk
      FROM TGOODS GD
     WHERE GD.OMBUDSMAN_YN = '1'
       AND GD.GOODS_CODE = in_goods_code
    ;
  EXCEPTION
  WHEN OTHERS THEN
       v_ombudsman_chk := 0;
  END;

/* 쇼핑몰/모바일 주문 불가 상품 연동불가 */
PROCEDURE SP_ORDER_MEDIA_CHECK(in_goods_code IN VARCHAR2) IS
  BEGIN
    SELECT COUNT(*)
      INTO v_order_media_chk
      FROM TGOODS GD
     WHERE (GD.ORDER_MEDIA_ALL_YN <> '1' AND INSTR(GD.ORDER_MEDIA,'62') < 1 AND INSTR(GD.ORDER_MEDIA,'61') < 1 )
       AND GD.GOODS_CODE = in_goods_code
    ;
  EXCEPTION
  WHEN OTHERS THEN
       v_order_media_chk := 0;
  END;

/* 방송 중 구매조건 상품 연동불가 */
PROCEDURE SP_BROAD_SALE_CHECK(in_goods_code IN VARCHAR2) IS
  BEGIN
    SELECT COUNT(*)
      INTO v_broad_sale_chk
      FROM TGOODS GD
     WHERE GD.BROAD_SALE_YN   = '1'
       AND GD.GOODS_CODE = in_goods_code
    ;
  EXCEPTION
  WHEN OTHERS THEN
       v_broad_sale_chk := 0;
  END;

/* 무형상품 연동 불가 */
PROCEDURE SP_INVI_GOODS_TYPE_CHECK(in_goods_code IN VARCHAR2) IS
  BEGIN
    SELECT COUNT(*)
      INTO v_invi_goods_type_chk
      FROM TGOODS GD
     WHERE GD.INVI_GOODS_TYPE <> '00'
       AND GD.GOODS_CODE=  in_goods_code
    ;
  EXCEPTION
  WHEN OTHERS THEN
       v_invi_goods_type_chk := 0;
  END;

/* 상품제외대상 */
PROCEDURE SP_GOODS_EXCEPT_CHECK(in_goods_code IN VARCHAR2) IS
  BEGIN
    SELECT COUNT(*)
      INTO v_goods_except_chk
      FROM TPATARGETEXCEPT PT
     WHERE PT.TARGET_GB   = '00'
       AND PT.TARGET_CODE = in_goods_code
       AND PT.USE_YN = '1'
       AND ROWNUM = 1
    ;
  EXCEPTION
  WHEN OTHERS THEN
       v_goods_except_chk := 0;
  END;

/* 업체제외대상 */
PROCEDURE SP_ENTP_EXCEPT_CHECK(in_goods_code IN VARCHAR2) IS
  BEGIN
    SELECT COUNT(*)
      INTO v_entp_except_chk
      FROM TPAEXCEPTENTP PEE
      LEFT OUTER JOIN TPAEXCEPTBRAND PEB ON PEE.ENTP_CODE = PEB.ENTP_CODE AND PEB.USE_YN = '1'
         , TGOODS GD
     WHERE PEE.USE_YN    = '1'
       AND PEE.ENTP_CODE = GD.ENTP_CODE
       AND (PEE.PA_GROUP_CODE_ALL_YN = '1')
       AND (PEE.ALL_BRAND_YN = '1' OR PEB.BRAND_CODE = GD.BRAND_CODE)
       AND (PEE.SOURCING_MEDIA = '00' OR PEE.SOURCING_MEDIA LIKE '%' || GD.SOURCING_MEDIA || '%')
       AND GD.GOODS_CODE = in_goods_code
       AND ROWNUM = 1
    ;
  EXCEPTION
  WHEN OTHERS THEN
       v_entp_except_chk := 0;
  END;

/* 브랜드제외대상 */
PROCEDURE SP_BRAND_EXCEPT_CHECK(in_goods_code IN VARCHAR2) IS
  BEGIN
    SELECT COUNT(*)
      INTO v_brand_except_chk
      FROM TPATARGETEXCEPT PT
         , TGOODS GD
     WHERE PT.TARGET_GB   = '30'
       AND PT.TARGET_CODE = GD.BRAND_CODE
       AND GD.GOODS_CODE  = in_goods_code
       AND PT.USE_YN = '1'
       AND ROWNUM = 1
    ;
  EXCEPTION
  WHEN OTHERS THEN
       v_brand_except_chk := 0;
  END;

/* 상품분류제외대상 */
PROCEDURE SP_GOODS_KIND_EXCEPT_CHECK(in_goods_code IN VARCHAR2) IS
  BEGIN
    SELECT COUNT(*)
      INTO v_goods_kind_except_chk
      FROM TPATARGETEXCEPT PT
         , TGOODS GD
     WHERE PT.TARGET_GB   = '40'
       AND PT.TARGET_CODE = GD.LMSD_CODE
       AND GD.GOODS_CODE  = in_goods_code
       AND PT.USE_YN = '1'
       AND ROWNUM = 1
    ;
  EXCEPTION
  WHEN OTHERS THEN
       v_goods_kind_except_chk := 0;
  END;

/* 키워드제외대상 */
PROCEDURE SP_KEYWORD_EXCEPT_CHECK(in_goods_code IN VARCHAR2) IS
  BEGIN
    SELECT COUNT(*)
      INTO v_keyword_except_chk
      FROM TPATARGETEXCEPT PT
         , TGOODS GD
     WHERE PT.TARGET_GB   = '50'
       AND INSTR(GD.GOODS_NAME,PT.TARGET_CODE) > 0
       AND GD.GOODS_CODE = in_goods_code
       AND PT.USE_YN = '1'
       AND ROWNUM = 1
    ;
  EXCEPTION
  WHEN OTHERS THEN
       v_keyword_except_chk := 0;
  END;

/* 상품 판매기간 오류 */
PROCEDURE SP_SALE_START_END_CHECK(in_goods_code IN VARCHAR2) IS
  BEGIN
    SELECT COUNT(*)
      INTO v_sale_start_end_chk
      FROM TGOODS G
     WHERE G.GOODS_CODE = in_goods_code
       AND (G.SALE_START_DATE > SYSDATE
           OR NVL(G.SALE_END_DATE, SYSDATE+1) < SYSDATE)
    ;
  EXCEPTION
  WHEN OTHERS THEN
       v_sale_start_end_chk := 0;
  END;

/* 제휴사 카테고리 미존재 */
PROCEDURE SP_PAGOODS_KIND_CHECK(in_goods_code IN VARCHAR2, in_pa_group_code IN VARCHAR2) IS
  BEGIN
    SELECT COUNT(*)
      INTO v_pagoods_kind_chk
      FROM TPAGOODSKINDSMAPPING KM
         , TGOODS GD
     WHERE KM.LMSD_CODE = GD.LMSD_CODE
       AND KM.PA_GROUP_CODE = in_pa_group_code
       AND GD.GOODS_CODE = in_goods_code
       AND ROWNUM = 1
    ;
  EXCEPTION
  WHEN OTHERS THEN
       v_pagoods_kind_chk := 0;
  END;

/* 이베이  카테고리 미존재 */
PROCEDURE SP_PAGOODS_KIND_CHECK2(in_goods_code IN VARCHAR2, in_pa_group_code IN VARCHAR2) IS
  BEGIN
    SELECT COUNT(*)
      INTO v_pagoods_kind_chk
      FROM TGOODS TG,TGOODSKINDS GK, TPAESMGOODSKINDSMAPPING PEM, TPAESMGOODSKINDS PEG, TPASITEGOODSKINDS PSG
     WHERE GK.LMSD_CODE = PEM.LMSD_CODE(+)
       AND GK.LMSD_CODE = TG.LMSD_CODE
       AND PEM.PA_LMSDN_KEY = PEG.LMSDN_CODE(+)
       AND PEM.PA_LMSD_KEY = PSG.LMSD_CODE(+)
       AND PSG.LMSD_CODE(+) = CASE WHEN in_pa_group_code = '02' THEN PEM.PA_LMSD_KEY
                                   WHEN in_pa_group_code = '03' THEN PEM.PA_IAC_LMSD_KEY
                              END
       AND TG.GOODS_CODE = in_goods_code
       AND ROWNUM = 1
    ;
  EXCEPTION
  WHEN OTHERS THEN
       v_pagoods_kind_chk := 0;
  END;

/* QA 체크  */
PROCEDURE SP_QA_CHECK(in_goods_code IN VARCHAR2) IS
  BEGIN
    SELECT COUNT(*)
      INTO v_qa_chk
      FROM TGOODS GD
     WHERE GD.SQC_GB IN ('16', '18', '19')
       AND GD.DESCRIBE_SQC_GB IN ('16', '18', '19')
       AND GD.GOODS_CODE = in_goods_code
    ;
  EXCEPTION
  WHEN OTHERS THEN
       v_qa_chk := 1;
  END;

PROCEDURE SP_TARGET_CHECK(in_goods_code IN VARCHAR2, in_pa_group_code IN VARCHAR2) IS
  BEGIN
     SELECT COUNT(1)
       INTO v_goods_target_cnt
       FROM TGOODS G
       inner join TGOODSADDINFO ga on g.GOODS_CODE = ga.GOODS_CODE
       inner join TGOODSPRICE gp on g.GOODS_CODE = gp.GOODS_CODE
      WHERE gp.apply_Date = (select max(gps.Apply_Date) from tgoodsprice gps where gps.goods_code = g.goods_code and gps.apply_Date <= SYSDATE)
        AND sourcing_media IN ('01','61')
        AND ga.em_goods_yn  = '0'
        AND gp.sale_Price > 0
        AND g.goods_code = in_goods_code
        and not exists (select 1 from TPATARGETEXCEPT ge where ge.Target_Gb = '00' and ge.Target_Code = g.GOODS_CODE and ge.Use_Yn = '1' and ge.Pa_Code_All_Yn ='1'
                                                           AND (ge.pa_code_all_yn ='1' or instr(ge.pa_code, in_pa_group_code ) >0  ))
        and not exists (select 1 from TPAEXCEPTENTP ee
                                 left outer join TPAEXCEPTBRAND be on be.ENTP_CODE = ee.ENTP_CODE and be.USE_YN = '1'
                                  where ee.ENTP_CODE = g.ENTP_CODE and ee.USE_YN = '1'
                                    and (ee.Pa_Group_Code_All_Yn = '1' OR INSTR(EE.PA_GROUP_CODE , in_pa_group_code) > 0)
                                    and (ee.All_Brand_Yn = '1' or be.Brand_Code = g.Brand_Code)
                                    and (ee.Sourcing_Media = '00' or instr(ee.Sourcing_Media, g.Sourcing_Media) > 0));
  EXCEPTION
  WHEN OTHERS THEN
       v_goods_target_cnt := 0;
  END;

PROCEDURE SP_TPAGOODSFILTERLOG_CHECK(in_goods_code IN VARCHAR2, in_pa_group_code IN VARCHAR2) IS
  BEGIN
     SELECT FL.FILTER_NOTE
       INTO v_goods_filter_log_note
       FROM TPAGOODSFILTERLOG FL
      WHERE GOODS_CODE = in_goods_code
        AND FILTER_LOG_NO = ( SELECT MAX(FILTER_LOG_NO)
                                FROM TPAGOODSFILTERLOG FLL
                                WHERE FLL.GOODS_CODE = FL.GOODS_CODE
                                  AND (FLL.PA_GROUP_CODE IS NULL OR FLL.PA_GROUP_CODE = in_pa_group_code )
                                );

  EXCEPTION
  WHEN OTHERS THEN
       v_goods_filter_log_note := '';
  END;

/* 연동해제 체크  */
PROCEDURE SP_LNKG_CNCL_CHK(in_goods_code IN VARCHAR2, in_pa_group_code IN VARCHAR2) IS
  BEGIN
    SELECT /*+ INDEX_DESC (PA PK_TPASALENOGOODS)*/
           PA.GOODS_CODE
         , PA.NOTE
      INTO v_lnkg_cncl_chk
         , v_lnkg_cncl_note
      FROM TPASALENOGOODS PA
     WHERE PA.GOODS_CODE = in_goods_code
       AND PA.PA_GROUP_CODE = in_pa_group_code
       AND PA.PA_SALE_GB IN ('30', '40')
       AND PA.PA_SALE_GB  = CASE WHEN PA.PA_GROUP_CODE = '01' THEN (SELECT PA_SALE_GB
                                                                      FROM TPA11STGOODS PG
                                                                     WHERE PG.GOODS_CODE    = PA.GOODS_CODE
                                                                       AND PG.PA_GROUP_CODE = PA.PA_GROUP_CODE
                                                                       AND PG.PA_CODE       = PA.PA_CODE)
                                 WHEN PA.PA_GROUP_CODE = '02' THEN (SELECT PA_SALE_GB
                                                                      FROM TPAGMKTGOODS PG
                                                                     WHERE PG.GOODS_CODE    = PA.GOODS_CODE
                                                                       AND PG.PA_GROUP_CODE = PA.PA_GROUP_CODE
                                                                       AND PG.PA_CODE       = PA.PA_CODE)
                                 WHEN PA.PA_GROUP_CODE = '03' THEN (SELECT PA_SALE_GB
                                                                      FROM TPAGMKTGOODS PG
                                                                     WHERE PG.GOODS_CODE    = PA.GOODS_CODE
                                                                       AND PG.PA_GROUP_CODE = PA.PA_GROUP_CODE
                                                                       AND PG.PA_CODE       = PA.PA_CODE)
                                 WHEN PA.PA_GROUP_CODE = '04' THEN (SELECT PA_SALE_GB
                                                                      FROM TPANAVERGOODS PG
                                                                     WHERE PG.GOODS_CODE    = PA.GOODS_CODE
                                                                       AND PG.PA_GROUP_CODE = PA.PA_GROUP_CODE
                                                                       AND PG.PA_CODE       = PA.PA_CODE)
                                 WHEN PA.PA_GROUP_CODE = '05' THEN (SELECT PA_SALE_GB
                                                                      FROM TPACOPNGOODS PG
                                                                     WHERE PG.GOODS_CODE    = PA.GOODS_CODE
                                                                       AND PG.PA_GROUP_CODE = PA.PA_GROUP_CODE
                                                                       AND PG.PA_CODE       = PA.PA_CODE)
                                 WHEN PA.PA_GROUP_CODE = '06' THEN (SELECT PA_SALE_GB
                                                                      FROM TPAWEMPGOODS PG
                                                                     WHERE PG.GOODS_CODE    = PA.GOODS_CODE
                                                                       AND PG.PA_GROUP_CODE = PA.PA_GROUP_CODE
                                                                       AND PG.PA_CODE       = PA.PA_CODE)
                                 END
       AND ROWNUM = '1'
    ;
  EXCEPTION
  WHEN OTHERS THEN
       v_lnkg_cncl_chk := '';
  END;


BEGIN

    BEGIN
       --신규동기화 사용
       IF arg_pa_group_code IN ('11','12') THEN

         SP_TARGET_CHECK(arg_goods_code , arg_pa_group_code); --동기화 타겟 추출 쿼리랑 기준 동일하게 갈것, 나머지는 동기화에서 걸러주는 TPAGOODSFILTER에서 추출

         IF v_goods_target_cnt = 0 THEN
            v_check_msg  := '연동 대상 상품이 아닙니다(0원가격,구성원 상품 ,브랜드 예외 등)';
         ELSE
            SP_TPAGOODSFILTERLOG_CHECK(arg_goods_code , arg_pa_group_code);
            v_check_msg :=  v_goods_filter_log_note;
         END IF;


       --레거시 사용
       ELSE
            /* 판매가 100원미만 연동불가 체크 */
            SP_SALE_PRICE_CHECK(arg_goods_code);

            /* 옴부즈맨 상품 연동불가 체크 */
            SP_OMBUDSMAN_CHECK(arg_goods_code);

            /* 쇼핑몰/모바일 주문 불가 상품 연동불가 체크 */
            SP_ORDER_MEDIA_CHECK(arg_goods_code);

            /* 방송 중 구매조건 상품 연동불가 체크 */
            SP_BROAD_SALE_CHECK(arg_goods_code);

            /* 무형상품 연동 불가 체크 */
            SP_INVI_GOODS_TYPE_CHECK(arg_goods_code);

            /* 상품제외대상 체크 */
            SP_GOODS_EXCEPT_CHECK(arg_goods_code);

            /* 업체제외대상 체크 */
            SP_ENTP_EXCEPT_CHECK(arg_goods_code);

            /* 브랜드제외대상 체크 */
            SP_BRAND_EXCEPT_CHECK(arg_goods_code);

            /* 상품분류제외대상 체크 */
            SP_GOODS_KIND_EXCEPT_CHECK(arg_goods_code);

            /* 키워드제외대상 체크 */
            SP_KEYWORD_EXCEPT_CHECK(arg_goods_code);

            /* 상품 판매기간 오류 체크 */
            SP_SALE_START_END_CHECK(arg_goods_code);

            /* 제휴사 카테고리 미존재 체크 */
            IF arg_pa_group_code IN ( '02', '03' ) THEN
               SP_PAGOODS_KIND_CHECK2(arg_goods_code, arg_pa_group_code);
            ELSIF arg_pa_group_code IN ( '01', '04', '05', '06', '07', '08', '09' ) THEN
               SP_PAGOODS_KIND_CHECK(arg_goods_code, arg_pa_group_code);
            ELSE v_pagoods_kind_chk := 1; /*PA_GROUP CODE = NULL 경우 반려 메세지 없도록함*/
            END IF;

            /* QA 체크 */
            SP_QA_CHECK(arg_goods_code);

            /* 연동해제 체크 */
            SP_LNKG_CNCL_CHK(arg_goods_code, arg_pa_group_code);

            BEGIN
                SELECT LISTAGG(CODE_NAME||' : '||REPLACE(NOTE, ', ') || CHR(13)||CHR(10)) WITHIN GROUP (ORDER BY GOODS_CODE)
                  INTO v_check_msg
                  FROM (
                        SELECT DISTINCT QA.GOODS_CODE, QA.NOTE, TC.CODE_NAME
                          FROM TPAGOODSQALOG QA
                             , TCODE TC
                         WHERE QA.GOODS_CODE    = arg_goods_code
                           AND QA.PA_GROUP_CODE = DECODE(arg_pa_group_code, NULL, QA.PA_GROUP_CODE, arg_pa_group_code)
                           AND TC.CODE_LGROUP   = 'O500'
                           AND TC.CODE_MGROUP   = QA.PA_GROUP_CODE
                           AND QA.INSERT_DATE   >= TRUNC(SYSDATE - 30)
                           AND  1  = CASE WHEN arg_pa_group_code = '01' AND NOT EXISTS (SELECT '1'
                                                                                          FROM TPA11STGOODS PG
                                                                                         WHERE PG.GOODS_CODE = arg_goods_code) THEN 1
                                          WHEN(arg_pa_group_code = '02' OR arg_pa_group_code = '03') AND NOT EXISTS (SELECT '1'
                                                                                                                      FROM TPAGMKTGOODS PG
                                                                                                                     WHERE PG.GOODS_CODE = arg_goods_code
                                                                                                                       AND PG.PA_GROUP_CODE = arg_pa_group_code) THEN 1
                                          WHEN arg_pa_group_code = '04' AND NOT EXISTS (SELECT '1'
                                                                                          FROM TPANAVERGOODS PG
                                                                                         WHERE PG.GOODS_CODE = arg_goods_code) THEN 1
                                          WHEN arg_pa_group_code = '05' AND NOT EXISTS (SELECT '1'
                                                                                          FROM TPACOPNGOODS PG
                                                                                         WHERE PG.GOODS_CODE = arg_goods_code) THEN 1
                                          WHEN arg_pa_group_code = '06' AND NOT EXISTS (SELECT '1'
                                                                                          FROM TPAWEMPGOODS PG
                                                                                         WHERE PG.GOODS_CODE = arg_goods_code) THEN 1
                                          WHEN arg_pa_group_code = '07' AND NOT EXISTS (SELECT '1'
                                                                                          FROM TPAINTPGOODS PG
                                                                                         WHERE PG.GOODS_CODE = arg_goods_code) THEN 1
                                          WHEN arg_pa_group_code = '08' AND NOT EXISTS (SELECT '1'
                                                                                          FROM TPALTONGOODS PG
                                                                                         WHERE PG.GOODS_CODE = arg_goods_code) THEN 1
                                          WHEN arg_pa_group_code = '09' AND NOT EXISTS (SELECT '1'
                                                                                          FROM TPATMONGOODS PG
                                                                                         WHERE PG.GOODS_CODE = arg_goods_code) THEN 1
                                          WHEN arg_pa_group_code = '10' AND NOT EXISTS (SELECT '1'
                                                                                          FROM TPASSGGOODS PG
                                                                                         WHERE PG.GOODS_CODE = arg_goods_code) THEN 1
                           ELSE 0
                           END

                       )
                ;
            EXCEPTION
            WHEN NO_DATA_FOUND THEN
                 v_check_msg  := '';
            END;

            /* 판매가 100원미만 연동불가 */
            IF v_sale_price_chk > 0 THEN
                v_check_msg := v_check_msg || '판매가 100원미만 연동불가' || CHR(13)||CHR(10);
            END IF;

            /* 옴부즈맨 상품 연동불가 */
            IF v_ombudsman_chk > 0 THEN
                v_check_msg := v_check_msg || '옴부즈맨 상품 연동불가' || CHR(13)||CHR(10);
            END IF;

            /* 쇼핑몰/모바일 주문 불가 상품 연동불가 */
            IF v_order_media_chk > 0 THEN
                v_check_msg := v_check_msg || '쇼핑몰/모바일 주문 불가 상품 연동불가' || CHR(13)||CHR(10);
            END IF;

            /* 방송 중 구매조건 상품 연동불가 */
            IF v_broad_sale_chk > 0 THEN
                v_check_msg := v_check_msg || '방송 중 구매조건 상품 연동불가' || CHR(13)||CHR(10);
            END IF;

            /* 무형상품 연동 불가 */
            IF v_invi_goods_type_chk > 0 THEN
                v_check_msg := v_check_msg || '무형상품 연동 불가' || CHR(13)||CHR(10);
            END IF;

            /* 상품제외대상 */
            IF v_goods_except_chk > 0 THEN
                v_check_msg := v_check_msg || '상품제외대상' || CHR(13)||CHR(10);
            END IF;

            /* 업체제외대상 */
            IF v_entp_except_chk > 0 THEN
                v_check_msg := v_check_msg || '업체제외대상' || CHR(13)||CHR(10);
            END IF;

            /* 브랜드제외대상 */
            IF v_brand_except_chk > 0 THEN
                v_check_msg := v_check_msg || '브랜드제외대상' || CHR(13)||CHR(10);
            END IF;

            /* 상품분류제외대상 */
            IF v_goods_kind_except_chk > 0 THEN
                v_check_msg := v_check_msg || '상품분류제외대상' || CHR(13)||CHR(10);
            END IF;

            /* 키워드제외대상 */
            IF v_keyword_except_chk > 0 THEN
                v_check_msg := v_check_msg || '키워드제외대상' || CHR(13)||CHR(10);
            END IF;

            /* 상품 판매기간 오류 */
            IF v_sale_start_end_chk > 0 THEN
                v_check_msg := v_check_msg || '상품 판매기간 오류' || CHR(13)||CHR(10);
            END IF;

            /* 제휴사 카테고리 미존재 */
            IF v_pagoods_kind_chk = 0 THEN
                v_check_msg := v_check_msg || '제휴사 카테고리 미존재' || CHR(13)||CHR(10);
            END IF;

            /* QA 미승인 */
            IF v_qa_chk = 0 THEN
                v_check_msg := v_check_msg || 'QA 미승인' || CHR(13)||CHR(10);
            END IF;

            /* 연동해제*/
            IF v_lnkg_cncl_chk IS NOT NULL THEN
                v_check_msg := v_check_msg || NVL(v_lnkg_cncl_note, '연동해제');
            END IF;


       END IF;

    EXCEPTION
    WHEN OTHERS THEN
         v_check_msg  := v_check_msg||' , 연동체크 점검 오류';
    END;

    RETURN v_check_msg;

END FUN_GET_PAGOODS_LNKG_CHECK;
/

prompt
prompt Creating function FUN_GET_PAPROMO_MARGINS
prompt =========================================
prompt
CREATE OR REPLACE FUNCTION BMTCOM.FUN_GET_PAPROMO_MARGINS (
    arg_goods_code         VARCHAR2,
    arg_mode               VARCHAR2,
    arg_out_own_price      NUMBER,
    arg_pa_code            VARCHAR2,
    arg_pa_group_code      VARCHAR2
)
    RETURN NUMBER
IS
    /***************************************************
    ****************************************************/
    v_sale_price    NUMBER(10,2);
    v_dc_amt        NUMBER(10,2);
    v_buy_price     NUMBER(10,2);
    v_out_promo_amt NUMBER(10,2);
    rtn_margin      NUMBER(12,4);
    v_pa_co         VARCHAR2(1);
   -- v_margin_except_cnt NUMBER(2);

  CURSOR DC_AMT_CURSOR IS
   SELECT DC_AMT
        , SALE_PRICE
        , BUY_PRICE
     FROM(
          SELECT NB
               , DC_AMT
               , SALE_PRICE
               , BUY_PRICE
            FROM(
                  SELECT /*+ INDEX_DESC(PI PK_TPAGOODSPRICE) */
                         1 AS NB
                       , GP.ARS_OWN_DC_AMT + GP.LUMP_SUM_OWN_DC_AMT AS DC_AMT
                       , GP.SALE_PRICE
                       , GP.BUY_PRICE
                    FROM TPAGOODSPRICE PI
                       , TGOODSPRICE GP
                   WHERE PI.APPLY_DATE   = GP.APPLY_DATE
                     AND PI.GOODS_CODE   = GP.GOODS_CODE
                     AND PI.SALE_PRICE   = GP.SALE_PRICE
                     AND PI.PA_CODE      = arg_pa_code
                     AND PI.GOODS_CODE   = arg_goods_code
                     AND PI.APPLY_DATE  < SYSDATE
                     AND ROWNUM = 1

                  UNION ALL

                  SELECT /*+ INDEX_DESC(GP PK_TGOODSPRICE) */
                         2 AS NB --입점이 되지 않은 상품
                       , GP.ARS_OWN_DC_AMT + GP.LUMP_SUM_OWN_DC_AMT AS DC_AMT
                       , GP.SALE_PRICE
                       , GP.BUY_PRICE
                    FROM TGOODSPRICE GP
                   WHERE GP.GOODS_CODE = arg_goods_code
                     AND APPLY_DATE    < SYSDATE
                     AND ROWNUM = 1 )
            ORDER BY NB ASC)
    WHERE ROWNUM =1;

  CURSOR OUT_PROMO_CURSOR IS
    SELECT /*+INDEX_DESC (PPT IDX_TPAPROMOTARGET_04)*/
         CASE WHEN PPT.PROC_GB  = 'D'  THEN 0
              WHEN PPT.USE_CODE <> '00' THEN 0
              ELSE PPT.OWN_COST
          END PROMO_AMT
    FROM TPAPROMOTARGET PPT
       , TPROMOM PM
   WHERE PM.PROMO_NO            = PPT.PROMO_NO
     AND PPT.PA_CODE            = arg_pa_code
     AND PPT.GOODS_CODE         = arg_goods_code
     AND PPT.PA_GROUP_CODE LIKE '%'||arg_pa_group_code||'%'
     AND NVL(PM.ALCOUT_PROMO_YN, '0') = '1'
     AND PPT.TRANS_DATE IS NOT NULL
     AND SYSDATE BETWEEN PPT.TRANS_DATE AND PPT.PROMO_EDATE
     AND PM.COUPON_YN           = '1'
     AND PM.APP_TYPE            = '10'
     AND PM.IMMEDIATELY_YN      = '1'
     AND PM.GOODS_ALL_YN        = '0'
     AND ROWNUM = 1
     ;

  --SSG이후 프로모션
/*  CURSOR OUT_PROMO_CURSOR2 IS
    SELECT PM.OWN_COST AS PROMO_AMT
      FROM TPASSGGOODS PG
         , TPAGOODSPRICE PP
         , TPAPROMOGOODSPRICE PM
         , TPROMOM TA
     WHERE PG.PA_CODE    = PP.PA_CODE
       AND PG.GOODS_CODE = PP.GOODS_CODE
       AND PP.ROWID      = (SELECT \*+ INDEX_DESC (GP PK_TPAGOODSPRICE)*\
                                  GP.ROWID
                              FROM TPAGOODSPRICE GP
                             WHERE GP.GOODS_CODE = PG.GOODS_CODE
                               AND GP.APPLY_DATE < SYSDATE
                               AND GP.PA_CODE = PG.PA_CODE
                               AND ROWNUM = 1)
       AND PP.GOODS_CODE = PM.GOODS_CODE
       AND PP.PA_CODE    = PM.PA_CODE
       AND PP.APPLY_DATE = PM.APPLY_DATE
       AND PM.ALCOUT_PROMO_YN = '1'
       AND PM.PROMO_SEQ = (SELECT \*+ INDEX_DESC (PM2 PK_TPAPROMOGOODSPRICE)*\
                                  PM2.PROMO_SEQ
                             FROM TPAPROMOGOODSPRICE PM2
                            WHERE PM2.GOODS_CODE = PM.GOODS_CODE
                              AND PM2.PA_CODE = PM.PA_CODE
                              AND PM2.APPLY_DATE = PM.APPLY_DATE
                              AND PM2.TRANS_DATE < SYSDATE
                              AND ROWNUM = 1)
       AND PM.PROMO_NO = TA.PROMO_NO
       --AND PG.PA_GROUP_CODE LIKE '%'||arg_pa_group_code||'%'
       AND PM.PA_CODE     = arg_pa_code
       AND PM.GOODS_CODE  = arg_goods_code
       AND ROWNUM = 1;
*/

/*  CURSOR SELECT_EXCEPT_CURSOR IS
     SELECT COUNT(1)
       FROM TCODE TC
      WHERE TC.CODE_LGROUP ='O999'
        AND TC.CODE_MGROUP = arg_goods_code
        AND TC.CODE_GROUP  = arg_pa_code;*/

BEGIN
     /*************************************************************************************************
     *  함수 설명 -  제휴 프로모션 마진률 계산 함수 , 할인금액을 인자로 받고 arg_mode에 따라
     *             인자로 들어온 할인금액 + 기존 할인금액 계산을 통해 마진률 계산
     *
     *  arg_mode    - 1  - 제휴 아웃 프로모션이 신규로 들어올때 마진률 계산
     *              - 4  - 현재 제휴사와 연동된 OUT, ARS, 일시불 프로모션을 가지고 마진률을 계산
     *
     *  arg_out_own_price : 새로 적용할 OUT 쿠폰의 OWN_PRICE (MODE 1에서 사용)
     *
     *  마진률  = ((판매가 - (ARS + 일시불 + OUT프로모션 ) - 공급가 ) / 판매가  ) * 100
     *  ※ 프로모션은 모두 당사 부담금액으로 책정
     **************************************************************************************************/
    BEGIN

        v_sale_price        := 0;
        v_buy_price         := 0;
        v_dc_amt            := 0;
        rtn_margin          := 0;
        v_out_promo_amt     := 0;
      --  v_margin_except_cnt := 0;

        --마진체크 예외 추가 TCODE O999 포함된 상품은 마진률 체크를 하지 않는다.
       /* OPEN SELECT_EXCEPT_CURSOR;
          LOOP
              FETCH SELECT_EXCEPT_CURSOR
               INTO v_margin_except_cnt;
              EXIT WHEN SELECT_EXCEPT_CURSOR%NOTFOUND ;
          END LOOP;
        CLOSE SELECT_EXCEPT_CURSOR;

        IF v_margin_except_cnt != '0' then
          rtn_margin := 99;
          RETURN rtn_margin;
        END IF;*/

        IF arg_mode = '1' THEN --인자로 넘어온 OUP프로모션에 대한 마진률 계산 MODE
          OPEN DC_AMT_CURSOR;
          LOOP
              FETCH DC_AMT_CURSOR
               INTO v_dc_amt, v_sale_price, v_buy_price;
              EXIT WHEN DC_AMT_CURSOR%NOTFOUND ;
          END LOOP;
          CLOSE DC_AMT_CURSOR;

          rtn_margin :=  (v_sale_price - (v_dc_amt + arg_out_own_price) - v_buy_price) / v_sale_price;

        ELSIF arg_mode = '4' THEN --현재 적용된 OUP프로모션에 대한 마진률 계산 MODE

          OPEN DC_AMT_CURSOR;
          LOOP
              FETCH DC_AMT_CURSOR
               INTO v_dc_amt, v_sale_price, v_buy_price;
              EXIT WHEN DC_AMT_CURSOR%NOTFOUND ;
          END LOOP;
          CLOSE DC_AMT_CURSOR;


        /*  IF  arg_pa_code = 'A1' OR arg_pa_code = 'A2' OR arg_pa_code = 'B1' OR arg_pa_code = 'B2'  THEN
             OPEN OUT_PROMO_CURSOR2;
             LOOP
                 FETCH OUT_PROMO_CURSOR2
                  INTO v_out_promo_amt;
                 EXIT WHEN OUT_PROMO_CURSOR2%NOTFOUND ;
             END LOOP;
             CLOSE OUT_PROMO_CURSOR2;
          ELSE*/

             OPEN OUT_PROMO_CURSOR;
             LOOP
                 FETCH OUT_PROMO_CURSOR
                  INTO v_out_promo_amt;
                 EXIT WHEN OUT_PROMO_CURSOR%NOTFOUND ;
             END LOOP;
             CLOSE OUT_PROMO_CURSOR;
          --END IF;

        rtn_margin :=  (v_sale_price - (v_dc_amt +v_out_promo_amt) - v_buy_price) / v_sale_price;

        END IF;

       rtn_margin := TRUNC(rtn_margin * 100 , 1);

    EXCEPTION
    WHEN OTHERS THEN
     --    IF SELECT_EXCEPT_CURSOR%ISOPEN    THEN CLOSE SELECT_EXCEPT_CURSOR; END IF;
         IF DC_AMT_CURSOR%ISOPEN           THEN CLOSE DC_AMT_CURSOR; END IF;
         IF OUT_PROMO_CURSOR%ISOPEN        THEN CLOSE OUT_PROMO_CURSOR; END IF;
         rtn_margin := 0;
    END;

    RETURN rtn_margin;

END FUN_GET_PAPROMO_MARGINS;
/

prompt
prompt Creating function FUN_GET_PA_GOODS_PRICE
prompt ========================================
prompt
CREATE OR REPLACE FUNCTION BMTCOM.FUN_GET_PA_GOODS_PRICE (
    arg_goods_code  IN TPAGOODSPRICE.GOODS_CODE%TYPE,
    arg_std_date    IN TPAGOODSPRICE.APPLY_DATE%TYPE,
    arg_flag        IN VARCHAR2
)
    RETURN NUMBER
IS
    /**********************************************
     Descript    : gooods get price
     create date : 2018.8.7
     create by   : sjPark
     argument    : arg_flag
                   1 : TGOODSPRICE   확인
                   2 : TPAGOODSPRICE 확인
                   3 : TPAGOODSPRICE 중 11번가만 확인( 80% 가격 인하건 BO처리 )
                   4 : TPAGOODSPRICE 중 쿠팡확인(50% 가격 인하 또는 100% 가격인상 불가)
    **********************************************/
    rtn_amt    TPAGOODSPRICE.SALE_PRICE%TYPE;

BEGIN

    BEGIN
        IF arg_flag = '1' THEN
            SELECT GP.SALE_PRICE
              INTO rtn_amt
              FROM TGOODSPRICE GP
             WHERE GP.GOODS_CODE = arg_goods_code
               AND GP.ROWID      = (SELECT /*+ INDEX_DESC (I PK_TGOODSPRICE)*/
                                          ROWID
                                     FROM TGOODSPRICE GPS
                                    WHERE GPS.GOODS_CODE = GP.GOODS_CODE
                                      AND GPS.APPLY_DATE <= arg_std_date
                                      AND ROWNUM = 1);
        ELSIF arg_flag = '2' THEN
            SELECT GP.SALE_PRICE
              INTO rtn_amt
              FROM TPAGOODSPRICE GP
             WHERE GP.GOODS_CODE = arg_goods_code
               AND GP.ROWID      = (SELECT /*+ INDEX_DESC (GPS PK_TPAGOODSPRICE)*/
                                          ROWID
                                     FROM TPAGOODSPRICE GPS
                                    WHERE GPS.GOODS_CODE = GP.GOODS_CODE
                                      AND GPS.PA_CODE IN ('11','12')
                                      AND GPS.APPLY_DATE <= arg_std_date
                                      AND ROWNUM = 1);
        ELSIF arg_flag = '4' THEN
            SELECT GP.SALE_PRICE
              INTO rtn_amt
              FROM TPAGOODSPRICE GP
             WHERE GP.GOODS_CODE = arg_goods_code
               AND GP.ROWID      = (SELECT /*+ INDEX_DESC (GPS PK_TPAGOODSPRICE)*/
                                          ROWID
                                     FROM TPAGOODSPRICE GPS
                                    WHERE GPS.GOODS_CODE = GP.GOODS_CODE
                                      AND GPS.PA_CODE IN ('51','52')
                                      AND GPS.APPLY_DATE <= arg_std_date
                                      AND ROWNUM = 1);
        ELSE
            SELECT GP.SALE_PRICE
              INTO rtn_amt
              FROM TPAGOODSPRICE GP
             WHERE GP.GOODS_CODE = arg_goods_code
               AND GP.ROWID      = (SELECT /*+ INDEX_DESC (GPS PK_TPAGOODSPRICE)*/
                                          ROWID
                                     FROM TPAGOODSPRICE GPS
                                    WHERE GPS.GOODS_CODE = GP.GOODS_CODE
                                      AND GPS.APPLY_DATE <= arg_std_date
                                      AND ROWNUM = 1);
        END IF;

    EXCEPTION
    WHEN OTHERS THEN
         rtn_amt := 0;
    END;

    RETURN rtn_amt;

END FUN_GET_PA_GOODS_PRICE;
/

prompt
prompt Creating function FUN_GET_PA_SALE_GB
prompt ====================================
prompt
CREATE OR REPLACE FUNCTION BMTCOM.FUN_GET_PA_SALE_GB (
     arg_sale_gb           IN TGOODS.SALE_GB%TYPE,
     arg_sale_start_date   IN TGOODS.SALE_START_DATE%TYPE,
     arg_sale_end_date     IN TGOODS.SALE_END_DATE%TYPE
)
    RETURN VARCHAR2
IS
    /**********************************************
     create date : 2018.04.23 ~
     create by   : CommerceWare
     remark      : 제휴사-판매진행 상태 ( 판매구분 , 판매기간 )
     return      : 판매상태
    ****************************************************/
    v_sale_start_date TGOODS.SALE_START_DATE%TYPE;
    v_sale_end_date TGOODS.SALE_END_DATE%TYPE;
    result_sale_gb TGOODS.SALE_GB%TYPE := '';


BEGIN

    BEGIN
        v_sale_start_date := NVL(arg_sale_start_date,SYSDATE);
        v_sale_end_date := NVL(arg_sale_end_date,SYSDATE);

        /* 판매중(00) 이면서 판매기간이 현재시간 기준으로 판매할 수 없다면 판매중단(11) 처리  */
        IF arg_sale_gb = '00' AND ( v_sale_start_date > SYSDATE OR v_sale_end_date < SYSDATE  ) THEN
            result_sale_gb := '11';
        ELSE
            result_sale_gb := arg_sale_gb;
        END IF
        ;
    EXCEPTION
    WHEN OTHERS THEN
        result_sale_gb := arg_sale_gb;
    END
    ;

    RETURN result_sale_gb;

END FUN_GET_PA_SALE_GB;
/

prompt
prompt Creating procedure SP_TSDCHECK_PA
prompt =================================
prompt
CREATE OR REPLACE PROCEDURE BMTCOM.sp_tsdcheck_pa(out_code out integer,
                                                  out_msg  out varchar2) is

  /***********************************************************************
    Job     : daily data check  --
    Date    : 2021/07/05  --

    Creator : CommerceWare  --

    제휴(11번가) 데이터 검증용 [ 기초정보, 상품정보 검증 ]
    [알림] : 정보성데이터
    [확인] : 출근 후 확인
    [즉시] : 즉시 확인 필요
    P101 : 기초 정보   --
    P201 : 상담 정보   --
    p301 : 상품 정보   --
  
  ***********************************************************************/

  v_prog_id      varchar2(1000);
  v_msg          varchar2(1000);
  v_error_msg    varchar2(1000);
  v_db_error_msg varchar2(1000);
  v_sysdate      varchar2(8);
  v_sysdatetime  varchar2(14);
  d_sysdate      DATE;

  v_user_id  varchar2(10);
  v_fromdate varchar2(14);
  v_todate   varchar2(14);
  is_existed integer := 0;
  v_flag     varchar2(4);

  v_phone_no varchar2(20);
  v_name     varchar2(30);

  v_send_sms_yn   varchar2(1);
  v_sms_send_date varchar2(14);
  v_reserved_fg   varchar2(1) := 'I';
  v_time_check_yn varchar2(1);

  type v_arry is varray(30) of varchar2(30);

  name_varry_P100 v_arry := v_arry('조민하',
                                   '이지영',
                                   '정기복',
                                   '박은진',
                                   '정연진');
  tel_varry_P100  v_arry := v_arry('01046355217',
                                   '01029352811',
                                   '01092665720',
                                   '01020840414',
                                   '01056053913');

  name_varry_P200 v_arry := v_arry('조민하',
                                   '이지영',
                                   '정기복',
                                   '박은진',
                                   '정연진');
  tel_varry_P200  v_arry := v_arry('01046355217',
                                   '01029352811',
                                   '01092665720',
                                   '01020840414',
                                   '01056053913');

  name_varry_P300 v_arry := v_arry('조민하',
                                   '이지영',
                                   '정기복',
                                   '박은진',
                                   '정연진');
  tel_varry_P300  v_arry := v_arry('01046355217',
                                   '01029352811',
                                   '01092665720',
                                   '01020840414',
                                   '01056053913');

  -- SMS 전송 --
  PROCEDURE insert_tsms IS
  BEGIN
    INSERT INTO TBL_SUBMIT_QUEUE_1
      (CMP_MSG_ID,
       CMP_MSG_GROUP_ID,
       USR_ID,
       SMS_GB,
       USED_CD,
       RESERVED_FG,
       RESERVED_DTTM,
       SAVED_FG,
       RCV_PHN_ID,
       SND_PHN_ID,
       NAT_CD,
       ASSIGN_CD,
       SND_MSG,
       CALLBACK_URL,
       CONTENT_CNT,
       CONTENT_MIME_TYPE,
       CONTENT_PATH,
       CMP_SND_DTTM,
       CMP_RCV_DTTM,
       REG_SND_DTTM,
       REG_RCV_DTTM,
       MACHINE_ID,
       SMS_STATUS,
       RSLT_VAL,
       MSG_TITLE,
       TELCO_ID,
       ETC_CHAR_1,
       ETC_CHAR_2,
       ETC_CHAR_3,
       ETC_CHAR_4)
      SELECT TO_CHAR(SYSDATE, 'YYYYMMDD') ||
             LTRIM(TO_CHAR(TBL_SUBMIT_QUEUE_1_SEQ.NEXTVAL, '000000')),
             '',
             (SELECT MAX(C.VAL) FROM TCONFIG C WHERE C.ITEM = 'DB_AGENT1_ID'),
             '1',
             '10',
             v_reserved_fg,
             v_sms_send_date,
             '0',
             v_phone_no,
             (SELECT MAX(C.VAL) FROM TCONFIG C WHERE C.ITEM = 'SMS_SEND_NO'),
             '',
             '00000',
             v_error_msg,
             '',
             1,
             'text/plain',
             '',
             '',
             '',
             '',
             '',
             '',
             '0',
             '',
             REMARK1,
             '',
             '',
             '999',
             '',
             '999999'
        FROM TCODE
       WHERE CODE_LGROUP = 'C302'
         AND CODE_MGROUP = '999';
  END;

  -- SMS 전송대상 --
  PROCEDURE send_sms IS
  BEGIN
  
    CASE
      WHEN v_send_sms_yn = '1' AND v_flag >= 'P100' AND v_flag < 'P200' THEN
        FOR i IN tel_varry_P100.FIRST .. tel_varry_P100.LAST LOOP
          v_name     := name_varry_P100(i);
          v_phone_no := tel_varry_P100(i);
          insert_tsms();
        END LOOP;
      
      WHEN v_send_sms_yn = '1' AND v_flag >= 'P200' AND v_flag < 'P300' THEN
        FOR i IN tel_varry_P200.FIRST .. tel_varry_P200.LAST LOOP
          v_name     := name_varry_P200(i);
          v_phone_no := tel_varry_P200(i);
          insert_tsms();
        END LOOP;
      
      WHEN v_send_sms_yn = '1' AND v_flag >= 'P300' AND v_flag < 'P400' THEN
        FOR i IN tel_varry_P300.FIRST .. tel_varry_P300.LAST LOOP
          v_name     := name_varry_P300(i);
          v_phone_no := tel_varry_P300(i);
          insert_tsms();
        END LOOP;
      
    END CASE;
  END;

  -- 자료오류 처리 : 로그, 전송  --
  PROCEDURE insert_error_log is
  BEGIN
    DBMS_OUTPUT.PUT_LINE('--  ERROR ' || v_flag || ':' || v_error_msg);
    PKG_COMMON.EXP_PRNS(v_prog_id,
                        '  --  ERROR ' || v_flag || ':' || v_error_msg);
  
    INSERT INTO TSDCHECKLOG_PA
      (CLOSE_DATE, SEQ, FLAG, ERR_TEXT, INSERT_DATE)
    VALUES
      (TO_DATE(v_sysdate, 'YYYYMMDD'),
       (SELECT LPAD(NVL(MAX(A.SEQ) + 1, 1), 3, '000')
          FROM TSDCHECKLOG_PA A
         WHERE A.CLOSE_DATE = TO_DATE(v_sysdate, 'YYYYMMDD')),
       v_flag,
       v_flag || ':' || v_error_msg,
       SYSDATE);
  
    send_sms();
  END;

  -- 프로시저 실행여부 --
  PROCEDURE insert_proc_s_log is
  BEGIN
    INSERT INTO TPASDCHECKLOG
      (PRG_ID, FLAG, ERR_TEXT, PROC_DATE)
    VALUES
      (v_prog_id, 'S', '', sysdate);
  END;

  -- 프로시저 종료여부 --
  PROCEDURE insert_proc_e_log is
  BEGIN
    INSERT INTO TPASDCHECKLOG
      (PRG_ID, FLAG, ERR_TEXT, PROC_DATE)
    VALUES
      (v_prog_id, 'E', '', sysdate);
  END;

  -- 프로시저 에러여부 --
  PROCEDURE insert_proc_f_log is
  BEGIN
    INSERT INTO TPASDCHECKLOG
      (PRG_ID, FLAG, ERR_TEXT, PROC_DATE)
    VALUES
      (v_prog_id, 'F', v_db_error_msg, sysdate);
  END;

  ----------------------------------------------------------

BEGIN

  -- setting program id
  v_prog_id := 'sp_tsdcheck_pa';

  -- setting sysdate
  out_code      := 0;
  v_sysdate     := PKG_COMMON.F_GET_SYSDATE;
  v_sysdatetime := PKG_COMMON.F_GET_SYSDATETIME;
  d_sysdate     := TRUNC(SYSDATE);
  insert_proc_s_log();
  -- create start log
  PKG_COMMON.STARTLOG(v_prog_id);

  -- setting input argument
  v_error_msg := 'set input arguments';

  v_user_id  := PKG_COMMON.B_USER_ID;
  v_fromdate := to_char(TRUNC(SYSDATE) - 5, 'yyyymmddhh24miss');
  v_todate   := to_char(TRUNC(SYSDATE) + 1, 'yyyymmddhh24miss');
  PKG_COMMON.EXP_PRNS(v_prog_id, 'sysdate     : ' || v_sysdate);
  PKG_COMMON.EXP_PRNS(v_prog_id, 'sysdatetime : ' || v_sysdatetime);
  PKG_COMMON.EXP_PRNS(v_prog_id, 'v_fromdate  : ' || v_fromdate);
  PKG_COMMON.EXP_PRNS(v_prog_id, 'v_todate    : ' || v_todate);

  SELECT CASE
           WHEN XA.HOLIDAY_DAY = '0' AND XA.WORK_YN = '1' THEN
            '1'
           ELSE
            '0'
         END
    INTO v_send_sms_yn
    FROM (SELECT CASE
                   WHEN TO_CHAR(d_sysdate, 'D') IN ('2', '3', '4', '5', '6') THEN
                    '0'
                   ELSE
                    '1'
                 END HOLIDAY_DAY,
                 '1' AS WORK_YN
          /*, NVL(( SELECT DA.WORK_YN
              FROM TDELYDAY DA
             WHERE DA.DELY_GB ='10'
               AND DA.YYMMDD = d_sysdate
          ),'1') AS WORK_YN*/
            FROM DUAL) XA;

  SELECT NVL((SELECT '1'
               FROM DUAL
              WHERE v_sysdatetime BETWEEN v_sysdate || '081000' AND
                    v_sysdate || '200000'),
             '0')
    INTO v_time_check_yn
    FROM DUAL;

  IF v_time_check_yn = '1' THEN
    v_reserved_fg   := 'I';
    v_sms_send_date := TO_CHAR(sysdate, 'YYYYMMDDHH24MISS');
  ELSE
    v_reserved_fg := 'R';
    IF v_sysdatetime >= (v_sysdate || '200000') THEN
      v_sms_send_date := TO_CHAR(sysdate + 1, 'YYYYMMDD') || '083000';
    ELSE
      v_sms_send_date := TO_CHAR(sysdate, 'YYYYMMDD') || '083000';
    END IF;
  END IF;

  --------------------------------------------------------------------------------

  /*
  v_flag      := 'P102';    v_error_msg := '[즉시_P102][제휴_공통] 카테고리분류(대중소세)적재 확인';
  is_existed  := 0;
  --제휴카테고리(대중소세) 분류 적재 여부 확인 없을 경우 입점실패 오픈 시 확인
  SELECT CASE WHEN MIN(CNT) > 0 THEN 0 ELSE 1 END DATA_CHECK
    into is_existed
    FROM (
          SELECT COUNT(1) AS CNT
            FROM TPAGOODSKINDS GK
            WHERE GK.PA_GROUP_CODE ='07'
  
          UNION ALL
  
          SELECT COUNT(1) AS CNT
            FROM TPAGOODSKINDS GK
           WHERE GK.PA_GROUP_CODE ='02'
          )
         ;
  
  IF( is_existed > 0 ) THEN insert_error_log(); END IF;
  */
  -- END --

  /*
  v_flag      := 'P104';    v_error_msg := '[즉시_P104][제휴_11번가] 원산지 데이터 적재 확인';
  is_existed  := 0;
  
  11번가 원산지 데이터 적재 확인 미등록시 입점실패
  SELECT CASE WHEN COUNT(1) > 0 THEN 0 ELSE 1 END DATA_CHECK
    into is_existed
    FROM TPA11STORIGIN PO
  ;
  
  IF( is_existed > 0 ) THEN insert_error_log(); END IF;
  -- END --
  */

  /*
  v_flag      := 'P105';    v_error_msg := '[알림_P105][제휴_11번가] 원산지 데이터 매핑 확인';
  is_existed  := 0;
  
  --11번가 원산지 데이터 매핑 확인
  SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END DATA_CHECK
    into is_existed
    FROM (
           SELECT PO.ORGN_TYP_DTLS_CD
                , TB.CODE_MGROUP
             FROM TPA11STORIGIN PO
                , TCODE TB
            WHERE TB.CODE_LGROUP ='B023'
              AND TB.USE_YN ='1'
              AND REPLACE(TRIM(UPPER(TB.CODE_NAME)),' ','') = REPLACE(TRIM(UPPER(PO.DETAIL_AREA_NAME)),' ','')
              AND PO.INSERT_DATE >= TO_DATE(v_fromdate,'YYYYMMDDHH24MISS')
  
           MINUS
  
           SELECT POM.ORGN_TYP_DTLS_CD
                , POM.ORIGIN_CODE
             FROM TPA11STORIGINMAPPING POM
            WHERE POM.INSERT_DATE >= TO_DATE(v_fromdate,'YYYYMMDDHH24MISS')
         )
         ;
  
  IF( is_existed > 0 ) THEN insert_error_log(); END IF;
  -- END --
  */

  v_flag      := 'P106';
  v_error_msg :='[확인_P106][제휴_공통] 업체배송지에는 없는 업체담당자 정보가 있는지 확인';
  is_existed  := 0;

  /* 제휴공통_업체배송지에는 없는 업체담당자 정보가 있는지 확인 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM TPAENTPSLIP PE
   WHERE NOT EXISTS (SELECT '1'
            FROM TENTPUSER EU
           WHERE PE.ENTP_CODE = EU.ENTP_CODE
             AND PE.ENTP_MAN_SEQ = EU.ENTP_MAN_SEQ);
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;
  -- END --

  /*
  v_flag      := 'P107';    v_error_msg := [즉시_P107][제휴_공통] 제휴_고시정보 데이터 적재 확인';
  is_existed  := 0;
  
  --제휴_고시정보 데이터 적재 확인 적재 안될 경우 입점 실패 오픈 시 확인
  SELECT CASE WHEN MIN(CNT) > 0 THEN 0 ELSE 1 END DATA_CHECK
    into is_existed
    FROM (
          SELECT COUNT(1) AS CNT
            FROM TPAOFFERCODE GK
           WHERE GK.PA_GROUP_CODE = '01'
  
          UNION ALL
  
          SELECT COUNT(1) AS CNT
            FROM TPAOFFERCODE GK
           WHERE GK.PA_GROUP_CODE = '02'
  
          UNION ALL
  
          SELECT COUNT(1) AS CNT
            FROM TPAOFFERCODE GK
           WHERE GK.PA_GROUP_CODE = '05'
  
          UNION ALL
  
          SELECT COUNT(1) AS CNT
            FROM TPAOFFERCODE GK
           WHERE GK.PA_GROUP_CODE = '06'
  
          UNION ALL
  
          SELECT COUNT(1) AS CNT
            FROM TPAOFFERCODE GK
           WHERE GK.PA_GROUP_CODE = '07'
  
          UNION ALL
  
          SELECT COUNT(1) AS CNT
            FROM TPAOFFERCODE GK
           WHERE GK.PA_GROUP_CODE = '08'
          )
         ;
  
  IF( is_existed > 0 ) THEN insert_error_log(); END IF;
  -- END --
  */

  /*
  v_flag      := 'P108';    v_error_msg := [확인_P108][제휴_공통] 제휴_고시항목 매핑 데이터 적재 확인';
  is_existed  := 0;
  
  --제휴_고시항목 매핑 데이터 적재 확인 미등록시 입점 실패
  SELECT CASE WHEN MIN(CNT) > 0 THEN 0 ELSE 1 END DATA_CHECK
    into is_existed
    FROM (
          SELECT COUNT(1) AS CNT
            FROM TPAOFFERCODEMAPPING GK
            WHERE GK.PA_GROUP_CODE = '01'
  
          UNION ALL
  
          SELECT COUNT(1) AS CNT
            FROM TPAOFFERCODEMAPPING GK
           WHERE GK.PA_GROUP_CODE = '02'
  
          UNION ALL
  
          SELECT COUNT(1) AS CNT
            FROM TPAOFFERCODEMAPPING GK
           WHERE GK.PA_GROUP_CODE = '05'
  
          UNION ALL
  
          SELECT COUNT(1) AS CNT
            FROM TPAOFFERCODEMAPPING GK
           WHERE GK.PA_GROUP_CODE = '06'
  
          UNION ALL
  
          SELECT COUNT(1) AS CNT
            FROM TPAOFFERCODEMAPPING GK
           WHERE GK.PA_GROUP_CODE = '07'
  
          UNION ALL
  
          SELECT COUNT(1) AS CNT
            FROM TPAOFFERCODEMAPPING GK
           WHERE GK.PA_GROUP_CODE = '08'
          )
         ;
  
  IF( is_existed > 0 ) THEN insert_error_log(); END IF;
  -- END --
  */

  -----------------------------------------------------------------------------------

  v_flag      := 'P301';
  v_error_msg := '[즉시_P301][제휴_공통] 판매단계 동기화 점검';
  is_existed  := 0;

  /*판매단계 동기화 점검 SK스토아에서 판매중단 된 상품이 제휴사에서 판대되는지 확인 해당 데이터 발생시 바로 수정 필요 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT TG.GOODS_CODE, TG.PA_CODE
            FROM TGOODS GD, TPAGOODSTARGET TG
           WHERE GD.SALE_GB <> '00'
             AND GD.GOODS_CODE = TG.GOODS_CODE
             AND TG.PA_GOODS_CODE IS NOT NULL
             AND GD.MODIFY_DATE >= d_sysdate - 4
             AND GD.MODIFY_DATE < SYSDATE - 1 / 24
                --TPAGMKTGOODS 테이블은 PA_SALE_GB 20 , PA_STATUS 30 , TRANS_SALE_YN 0 이면 판매중단대상임
             AND 1 = CASE
                   WHEN TG.PA_GROUP_CODE = '01' THEN
                    (SELECT '1'
                       FROM TPA11STGOODS PG
                      WHERE PG.GOODS_CODE = GD.GOODS_CODE
                        AND PG.PA_SALE_GB = '20'
                        AND PG.PA_STATUS = '30'
                        AND PG.TRANS_SALE_YN = '0')
                   WHEN (TG.PA_GROUP_CODE = '02' OR TG.PA_GROUP_CODE = '03') THEN
                    (SELECT '1'
                       FROM TPAGMKTGOODS PG
                      WHERE PG.GOODS_CODE = GD.GOODS_CODE
                        AND PG.PA_SALE_GB = '20'
                        AND PG.PA_STATUS = '30'
                        AND PG.TRANS_SALE_YN = '1'
                        AND ROWNUM = 1)
                   WHEN TG.PA_GROUP_CODE = '04' THEN
                    (SELECT '1'
                       FROM TPANAVERGOODS PG
                      WHERE PG.GOODS_CODE = GD.GOODS_CODE
                        AND PG.PA_SALE_GB = '20'
                        AND PG.PA_STATUS = '30'
                        AND PG.TRANS_SALE_YN = '0')
                   WHEN TG.PA_GROUP_CODE = '05' THEN
                    (SELECT '1'
                       FROM TPACOPNGOODS PG
                      WHERE PG.GOODS_CODE = GD.GOODS_CODE
                        AND PG.PA_SALE_GB = '20'
                        AND PG.PA_STATUS = '30'
                        AND PG.TRANS_SALE_YN = '0')
                   WHEN TG.PA_GROUP_CODE = '06' THEN
                    (SELECT '1'
                       FROM TPAWEMPGOODS PG
                      WHERE PG.GOODS_CODE = GD.GOODS_CODE
                        AND PG.PA_SALE_GB = '20'
                        AND PG.PA_STATUS = '30'
                        AND PG.TRANS_SALE_YN = '0')
                   WHEN TG.PA_GROUP_CODE = '07' THEN
                    (SELECT '1'
                       FROM TPAINTPGOODS PG
                      WHERE PG.GOODS_CODE = GD.GOODS_CODE
                        AND PG.PA_SALE_GB = '20'
                        AND PG.PA_STATUS = '30'
                        AND PG.TRANS_SALE_YN = '0')
                   WHEN TG.PA_GROUP_CODE = '08' THEN
                    (SELECT '1'
                       FROM TPALTONGOODS PG
                      WHERE PG.GOODS_CODE = GD.GOODS_CODE
                        AND PG.PA_SALE_GB = '20'
                        AND PG.PA_STATUS = '30'
                        AND PG.TRANS_SALE_YN = '0')
                   WHEN TG.PA_GROUP_CODE = '09' THEN
                    (SELECT '1'
                       FROM TPATMONGOODS PG
                      WHERE PG.GOODS_CODE = GD.GOODS_CODE
                        AND PG.PA_SALE_GB = '20'
                        AND PG.PA_STATUS = '30'
                        AND PG.TRANS_SALE_YN = '0')
                   WHEN TG.PA_GROUP_CODE = '10' THEN
                    (SELECT '1'
                       FROM TPASSGGOODS PG
                      WHERE PG.GOODS_CODE = GD.GOODS_CODE
                        AND PG.PA_SALE_GB = '20'
                        AND PG.PA_STATUS = '30'
                        AND PG.TRANS_SALE_YN = '0')
                   WHEN TG.PA_GROUP_CODE = '11' THEN
                    (SELECT '1'
                       FROM TPAKAKAOGOODS PG
                      WHERE PG.GOODS_CODE = GD.GOODS_CODE
                        AND PG.PA_SALE_GB = '20'
                        AND PG.PA_STATUS = '30'
                        AND PG.TRANS_SALE_YN = '0')
                   WHEN TG.PA_GROUP_CODE = '12' THEN
                    (SELECT '1'
                       FROM TPAHALFGOODS PG
                      WHERE PG.GOODS_CODE = GD.GOODS_CODE
                        AND PG.PA_SALE_GB = '20'
                        AND PG.PA_STATUS = '30'
                        AND PG.TRANS_SALE_YN = '0')
                   ELSE
                    '0'
                 END);
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;
  -- END --

  /*   v_flag      := 'P304';    v_error_msg := '[확인_P304][제휴_공통] 브랜드명 동기화 점검';
      is_existed  := 0;
  
      \* 브랜드명 동기화 점검 *\
      SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END DATA_CHECK
        into is_existed
        FROM (
              SELECT GD.GOODS_CODE
                   , GD.BRAND_CODE
                   , BD.BRAND_NAME
                   , PG.BRAND_NAME AS PA_BRAND_NAME
                FROM TPAGOODS PG
                   , TGOODS   GD
                   , TBRAND   BD
               WHERE PG.GOODS_CODE = GD.GOODS_CODE
                 AND GD.BRAND_CODE = BD.BRAND_CODE
                 AND PG.BRAND_NAME <> BD.BRAND_NAME
                 AND GD.MODIFY_DATE >= d_sysdate-30
                 AND GD.SALE_GB <> '19'
                 AND GD.BRAND_CODE NOT IN ('000167', '001619', '001613','001681','005499','001381','011037','005569','014047','999999','009645','010583','000008','000374','001037'
  ,'011053','011038','007527','005423','006679','000284','007509')
                 --AND GD.GOODS_CODE NOT IN ('20087466') -> 2019.02.08 THJEON THJEON 상품코드에서 브랜드코드로 변경
  
              UNION
  
              SELECT GD.GOODS_CODE
                   , GD.BRAND_CODE
                   , BD.BRAND_NAME
                   , PG.BRAND_NAME AS PA_BRAND_NAME
                FROM TPAPRODUCT PG
                   , TGOODS   GD
                   , TBRAND   BD
               WHERE PG.GOODS_CODE = GD.GOODS_CODE
                 AND GD.BRAND_CODE = BD.BRAND_CODE
                 AND PG.BRAND_NAME <> BD.BRAND_NAME
                 AND GD.MODIFY_DATE >= d_sysdate-30
                 AND GD.SALE_GB <> '19'
                 AND GD.BRAND_CODE NOT IN ('000167', '001619', '001613','001681','005499','001381','011037','005569','014047','999999','009645','010583','000008','000374','001037'
  ,'011053','011038','007527','005423','006679','000284','007509')
             )
             ;
  
      IF( is_existed > 0 ) THEN insert_error_log(); END IF;*/
  -- END --

  /* 
      v_flag      := 'P305';    v_error_msg := '[확인_P305][제휴_공통] 원산지코드/명 동기화 점검';
      is_existed  := 0;
  
     /* 원산지코드/명 동기화 점검
      SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END DATA_CHECK
        into is_existed
        FROM (
              SELECT GD.GOODS_CODE
                   , GD.ORIGIN_CODE
                   , PG.ORIGIN_CODE AS PA_ORIGIN_CODE
                   , TC.CODE_NAME   AS ORIGIN_NAME
                   , PG.ORIGIN_NAME AS PA_ORIGIN_NAME
                FROM TPAGOODS PG
                   , TGOODS   GD
                   , TCODE    TC
               WHERE PG.GOODS_CODE  = GD.GOODS_CODE
                 AND GD.ORIGIN_CODE = TC.CODE_MGROUP
                 AND TC.CODE_LGROUP = 'B023'
                 AND ( PG.ORIGIN_NAME <> TC.CODE_NAME OR PG.ORIGIN_CODE <> GD.ORIGIN_CODE )
                 AND GD.MODIFY_DATE >= d_sysdate-30
                 AND GD.SALE_GB <> '19'
  
                 UNION
  
              SELECT GD.GOODS_CODE
                   , GD.ORIGIN_CODE
                   , PG.ORIGIN_CODE AS PA_ORIGIN_CODE
                   , TC.CODE_NAME   AS ORIGIN_NAME
                   , PG.ORIGIN_NAME AS PA_ORIGIN_NAME
                FROM TPAPRODUCT PG
                   , TGOODS   GD
                   , TCODE    TC
               WHERE PG.GOODS_CODE  = GD.GOODS_CODE
                 AND GD.ORIGIN_CODE = TC.CODE_MGROUP
                 AND TC.CODE_LGROUP = 'B023'
                 AND ( PG.ORIGIN_NAME <> TC.CODE_NAME OR PG.ORIGIN_CODE <> GD.ORIGIN_CODE )
                 AND GD.MODIFY_DATE >= d_sysdate-30
                 AND GD.SALE_GB <> '19'
             )
             ;
  
      IF( is_existed > 0 ) THEN insert_error_log(); END IF;
      -- END --
  */

  v_flag      := 'P306';
  v_error_msg := '[확인_P306][제휴_공통] 상품기초정보 동기화 점검';
  is_existed  := 0;

  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (
           /*신규 동기화용*/
          SELECT GD.GOODS_CODE,
                  GD.MAKECO_CODE,
                  PG.MAKECO_CODE             AS PA_MAKECO_CODE,
                  GD.TAX_YN,
                  PG.TAX_YN                  AS PA_TAX_YN,
                  GD.TAX_SMALL_YN,
                  PG.TAX_SMALL_YN            AS PA_TAX_SMALL_YN,
                  GD.ADULT_YN,
                  PG.ADULT_YN                AS PA_ADULT_YN,
                  GD.ORDER_MIN_QTY,
                  PG.ORDER_MIN_QTY           AS PA_ORDER_MIN_QTY,
                  GD.ORDER_MAX_QTY,
                  PG.ORDER_MAX_QTY           AS PA_ORDER_MAX_QTY,
                  GD.CUST_ORD_QTY_CHECK_TERM,
                  PG.CUST_ORD_QTY_CHECK_TERM AS PA_CUST_ORD_QTY_CHECK_TERM,
                  GD.ENTP_CODE,
                  PG.ENTP_CODE               AS PA_ENTP_CODE,
                  GD.SHIP_MAN_SEQ,
                  PG.SHIP_MAN_SEQ            AS PA_SHIP_MAN_SEQ,
                  GD.RETURN_MAN_SEQ,
                  PG.RETURN_MAN_SEQ          AS PA_RETURN_MAN_SEQ,
                  GD.SHIP_COST_CODE,
                  PG.SHIP_COST_CODE          AS PA_SHIP_COST_CODE,
                  GD.AVG_DELY_LEADTIME,
                  PG.AVG_DELY_LEADTIME       AS PA_AVG_DELY_LEADTIME
            FROM TPAPRODUCT PG, TGOODS GD
           WHERE PG.GOODS_CODE = GD.GOODS_CODE
             AND (PG.MAKECO_CODE <> GD.MAKECO_CODE OR PG.TAX_YN <> GD.TAX_YN OR
                 PG.TAX_SMALL_YN <> GD.TAX_SMALL_YN OR
                 PG.ADULT_YN <> GD.ADULT_YN OR
                 PG.ORDER_MIN_QTY <> GD.ORDER_MIN_QTY OR
                 PG.ORDER_MAX_QTY <> GD.ORDER_MAX_QTY OR
                 PG.CUST_ORD_QTY_CHECK_TERM <> GD.CUST_ORD_QTY_CHECK_TERM OR
                 PG.ENTP_CODE <> GD.ENTP_CODE OR
                 PG.SHIP_MAN_SEQ <> GD.SHIP_MAN_SEQ OR
                 PG.RETURN_MAN_SEQ <> GD.RETURN_MAN_SEQ OR
                 PG.SHIP_COST_CODE <> GD.SHIP_COST_CODE OR
                 PG.AVG_DELY_LEADTIME <> GD.AVG_DELY_LEADTIME OR
                 PG.COLLECT_YN <> GD.COLLECT_YN)
             AND GD.MODIFY_DATE >= d_sysdate - 4
             AND GD.MODIFY_DATE < SYSDATE - INTERVAL '60'
           MINUTE
             AND GD.SALE_GB <> '19'
             AND (PG.SALE_PA_CODE LIKE '5%' OR PG.SALE_PA_CODE LIKE 'B%' OR
                 PG.SALE_PA_CODE LIKE 'C%' OR PG.SALE_PA_CODE LIKE '1%' OR
                 PG.SALE_PA_CODE LIKE '2%' OR PG.SALE_PA_CODE LIKE '4%' OR
                 PG.SALE_PA_CODE LIKE '6%' OR PG.SALE_PA_CODE LIKE '7%' OR
                 PG.SALE_PA_CODE LIKE '8%' OR PG.SALE_PA_CODE LIKE '9%' OR
                 PG.SALE_PA_CODE LIKE 'A%')
                -- 조건에 맞지 않을 경우 TPAPRODUCT업데이트 하지 않고 판매중단 시키기 때문에 제휴사 판매중인 상품만 검증 처리 2022.04.25 by jchoi
             AND 1 = CASE
                   WHEN PG.SALE_PA_CODE LIKE 'B%' THEN
                    NVL((SELECT 1
                          FROM TPAKAKAOGOODS TPG
                         WHERE TPG.GOODS_CODE = PG.GOODS_CODE
                           AND TPG.PA_SALE_GB = '20'
                           AND TPG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN PG.SALE_PA_CODE LIKE 'C%' THEN
                    NVL((SELECT 1
                          FROM TPAHALFGOODS TPG
                         WHERE TPG.GOODS_CODE = PG.GOODS_CODE
                           AND TPG.PA_SALE_GB = '20'
                           AND TPG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN PG.SALE_PA_CODE LIKE 'A%' THEN
                    NVL((SELECT 1
                          FROM TPASSGGOODS TPG
                         WHERE TPG.GOODS_CODE = PG.GOODS_CODE
                           AND TPG.PA_SALE_GB = '20'
                           AND TPG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN PG.SALE_PA_CODE LIKE '9%' THEN
                    NVL((SELECT 1
                          FROM TPATMONGOODS TPG
                         WHERE TPG.GOODS_CODE = PG.GOODS_CODE
                           AND TPG.PA_SALE_GB = '20'
                           AND TPG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN PG.SALE_PA_CODE LIKE '8%' THEN
                    NVL((SELECT 1
                          FROM TPALTONGOODS TPG
                         WHERE TPG.GOODS_CODE = PG.GOODS_CODE
                           AND TPG.PA_SALE_GB = '20'
                           AND TPG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN PG.SALE_PA_CODE LIKE '7%' THEN
                    NVL((SELECT 1
                          FROM TPAINTPGOODS TPG
                         WHERE TPG.GOODS_CODE = PG.GOODS_CODE
                           AND TPG.PA_SALE_GB = '20'
                           AND TPG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN PG.SALE_PA_CODE LIKE '6%' THEN
                    NVL((SELECT 1
                          FROM TPAWEMPGOODS TPG
                         WHERE TPG.GOODS_CODE = PG.GOODS_CODE
                           AND TPG.PA_SALE_GB = '20'
                           AND TPG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN PG.SALE_PA_CODE LIKE '5%' THEN
                    NVL((SELECT 1
                          FROM TPACOPNGOODS TPG
                         WHERE TPG.GOODS_CODE = PG.GOODS_CODE
                           AND TPG.PA_SALE_GB = '20'
                           AND TPG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN PG.SALE_PA_CODE LIKE '4%' THEN
                    NVL((SELECT 1
                          FROM TPANAVERGOODS TPG
                         WHERE TPG.GOODS_CODE = PG.GOODS_CODE
                           AND TPG.PA_SALE_GB = '20'
                           AND TPG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN PG.SALE_PA_CODE LIKE '2%' THEN
                    NVL((SELECT 1
                          FROM TPAGMKTGOODS TPG
                         WHERE TPG.GOODS_CODE = PG.GOODS_CODE
                           AND TPG.PA_SALE_GB = '20'
                           AND TPG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN PG.SALE_PA_CODE LIKE '1%' THEN
                    NVL((SELECT 1
                          FROM TPA11STGOODS TPG
                         WHERE TPG.GOODS_CODE = PG.GOODS_CODE
                           AND TPG.PA_SALE_GB = '20'
                           AND TPG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
          
          );
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;
  -- END --

  v_flag      := 'P313';
  v_error_msg := '[즉시_P313][제휴_공통] 판매제휴사 데이터 점검';
  is_existed  := 0;

  /* P313. [제휴_공통] 판매제휴사 데이터 점검 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT G.GOODS_CODE
            FROM TGOODS G, TGOODSADDINFO GA
           WHERE G.MODIFY_DATE > SYSDATE - 1
             AND G.GOODS_CODE = GA.GOODS_CODE
             AND GA.MOBILE_ETV_YN = '0'
             AND G.SALE_GB = '00'
             AND (EXISTS (SELECT 1
                            FROM TPA11STGOODS PG
                           WHERE PG.GOODS_CODE = G.GOODS_CODE
                             AND PG.PA_CODE <> DECODE(G.SOURCING_MEDIA,
                                                      '01',
                                                      '11',
                                                      '61',
                                                      '12',
                                                      '99')) OR EXISTS
                  (SELECT 1
                     FROM TPAGMKTGOODS PG
                    WHERE PG.GOODS_CODE = G.GOODS_CODE
                      AND PG.PA_CODE <> DECODE(G.SOURCING_MEDIA,
                                               '01',
                                               '21',
                                               '61',
                                               '22',
                                               '99'))
                 --NAVER는 무조건 41
                  OR EXISTS
                  (SELECT 1
                     FROM TPACOPNGOODS PG
                    WHERE PG.GOODS_CODE = G.GOODS_CODE
                      AND PG.PA_CODE <> DECODE(G.SOURCING_MEDIA,
                                               '01',
                                               '51',
                                               '61',
                                               '52',
                                               '99')) OR EXISTS
                  (SELECT 1
                     FROM TPAWEMPGOODS PG
                    WHERE PG.GOODS_CODE = G.GOODS_CODE
                      AND PG.PA_CODE <> DECODE(G.SOURCING_MEDIA,
                                               '01',
                                               '61',
                                               '61',
                                               '62',
                                               '99')) OR EXISTS
                  (SELECT 1
                     FROM TPAINTPGOODS PG
                    WHERE PG.GOODS_CODE = G.GOODS_CODE
                      AND PG.PA_CODE <> DECODE(G.SOURCING_MEDIA,
                                               '01',
                                               '71',
                                               '61',
                                               '72',
                                               '99')) OR EXISTS
                  (SELECT 1
                     FROM TPALTONGOODS PG
                    WHERE PG.GOODS_CODE = G.GOODS_CODE
                      AND PG.PA_CODE <> DECODE(G.SOURCING_MEDIA,
                                               '01',
                                               '81',
                                               '61',
                                               '82',
                                               '99'))
                 -- TMON 모바일계정으로 통합 됨, 방송상품도 앞으로 92만 생성
                 -- OR EXISTS ( SELECT 1 FROM TPATMONGOODS PG WHERE PG.GOODS_CODE = G.GOODS_CODE AND PG.PA_CODE <> DECODE(G.SOURCING_MEDIA,'01','91','61','92','99') )
                  OR EXISTS
                  (SELECT 1
                     FROM TPASSGGOODS PG
                    WHERE PG.GOODS_CODE = G.GOODS_CODE
                      AND PG.PA_CODE <> DECODE(G.SOURCING_MEDIA,
                                               '01',
                                               'A1',
                                               '61',
                                               'A2',
                                               '99')) OR EXISTS
                  (SELECT 1
                     FROM TPAKAKAOGOODS PG
                    WHERE PG.GOODS_CODE = G.GOODS_CODE
                      AND PG.PA_CODE <> DECODE(G.SOURCING_MEDIA,
                                               '01',
                                               'B1',
                                               '61',
                                               'B2',
                                               '99')) OR EXISTS
                  (SELECT 1
                     FROM TPAHALFGOODS PG
                    WHERE PG.GOODS_CODE = G.GOODS_CODE
                      AND PG.PA_CODE <> DECODE(G.SOURCING_MEDIA,
                                               '01',
                                               'C1',
                                               '61',
                                               'C2',
                                               '99'))
                 
                 )
             AND G.GOODS_CODE NOT IN ('25677110',
                                      '25677126',
                                      '25677213',
                                      '25679058',
                                      '25679101',
                                      '25679139',
                                      '25679178',
                                      '25679186',
                                      '25697949',
                                      '25697958',
                                      '25697959',
                                      '25697966',
                                      '25697975',
                                      '25738045',
                                      '25738063',
                                      '25738085',
                                      '25738127',
                                      '25797745',
                                      '25679068',
                                      '25738051',
                                      '25679112',
                                      '20007357',
                                      '25679176',
                                      '20001439',
                                      '20001552',
                                      '20007446',
                                      '20007447',
                                      '20007448',
                                      '20007449',
                                      '25677133',
                                      '25738001',
                                      '20000392',
                                      '20000985',
                                      '20001245',
                                      '20001268',
                                      '20001279',
                                      '20089304',
                                      '20174729',
                                      '25839729',
                                      '20021480',
                                      '25839728',
                                      '20007440',
                                      '20007442',
                                      '24310357',
                                      '22157594',
                                      '28790739',
                                      '31574394',
                                      '31574395',
                                      '31574397',
                                      '31574400',
                                      '31574402',
                                      '31574405',
                                      '31600572',
                                      '31063342',
                                      '31068348',
                                      '31464324',
                                      '29441229',
                                      '29929539',
                                      '31163941',
                                      '31423333',
                                      '31254681',
                                      '30355824',
                                      '35123034',
                                      '29934813',
                                      '34507159',
                                      '36837118',
                                      '36837121'   -- wemp 수기연동
                                      ) -- 수기연동으로 인한 11번가 PACODE 차이
          MINUS
          
           /*모바일 eTV 상품 제외*/
          SELECT G.GOODS_CODE
            FROM TGOODS G
           WHERE G.MODIFY_DATE > SYSDATE - 1
             AND G.MD_KIND = '0032'
             AND G.SOURCING_MEDIA = '61'
             AND (EXISTS (SELECT 1
                            FROM TPA11STGOODS PG
                           WHERE PG.GOODS_CODE = G.GOODS_CODE
                             AND PG.PA_CODE = '11') OR EXISTS
                  (SELECT 1
                     FROM TPAGMKTGOODS PG
                    WHERE PG.GOODS_CODE = G.GOODS_CODE
                      AND PG.PA_CODE = '21') OR EXISTS
                  (SELECT 1
                     FROM TPACOPNGOODS PG
                    WHERE PG.GOODS_CODE = G.GOODS_CODE
                      AND PG.PA_CODE = '51') OR EXISTS
                  (SELECT 1
                     FROM TPAWEMPGOODS PG
                    WHERE PG.GOODS_CODE = G.GOODS_CODE
                      AND PG.PA_CODE = '61') OR EXISTS
                  (SELECT 1
                     FROM TPAINTPGOODS PG
                    WHERE PG.GOODS_CODE = G.GOODS_CODE
                      AND PG.PA_CODE = '71') OR EXISTS
                  (SELECT 1
                     FROM TPALTONGOODS PG
                    WHERE PG.GOODS_CODE = G.GOODS_CODE
                      AND PG.PA_CODE = '81') OR EXISTS
                  (SELECT 1
                     FROM TPATMONGOODS PG
                    WHERE PG.GOODS_CODE = G.GOODS_CODE
                      AND PG.PA_CODE = '91') OR EXISTS
                  (SELECT 1
                     FROM TPASSGGOODS PG
                    WHERE PG.GOODS_CODE = G.GOODS_CODE
                      AND PG.PA_CODE = 'A1') OR EXISTS
                  (SELECT 1
                     FROM TPAKAKAOGOODS PG
                    WHERE PG.GOODS_CODE = G.GOODS_CODE
                      AND PG.PA_CODE = 'B1') OR EXISTS
                  (SELECT 1
                     FROM TPAHALFGOODS PG
                    WHERE PG.GOODS_CODE = G.GOODS_CODE
                      AND PG.PA_CODE = 'C1')));
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;
  -- END --

  /*    v_flag      := 'P314';    v_error_msg := '[즉시_P314][제휴_공통] 단품정보 동기화 점검';
  is_existed  := 0;
  
  \* P314. [제휴_공통] 단품정보 동기화 점검 (의도를 모르겠다.)*\
  SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END DATA_CHECK
    into is_existed
    FROM (  
             \*신규 동기화용*\
            SELECT DT.GOODS_CODE, DT.GOODSDT_CODE
              FROM TGOODSDT DT, TGOODS TG, TPAPRODUCT PP
             WHERE DT.MODIFY_DATE > d_sysdate - 4
               AND DT.MODIFY_DATE < SYSDATE - 1/24
               AND TG.SALE_GB = '00'
               AND DT.SALE_GB = '00'
               AND DT.GOODS_CODE = TG.GOODS_CODE
               AND TG.GOODS_CODE = PP.GOODS_CODE
               AND ( PP.SALE_PA_CODE LIKE '1%' OR PP.SALE_PA_CODE LIKE '2%' OR PP.SALE_PA_CODE LIKE '4%' OR PP.SALE_PA_CODE LIKE '5%'  OR PP.SALE_PA_CODE LIKE '6%'  OR PP.SALE_PA_CODE LIKE '7%'                  
                OR PP.SALE_PA_CODE LIKE '8%'  OR PP.SALE_PA_CODE LIKE '9%'  OR PP.SALE_PA_CODE LIKE 'A%' OR PP.SALE_PA_CODE LIKE 'B%' OR PP.SALE_PA_CODE LIKE 'C%')
               AND 1 = CASE WHEN PP.SALE_PA_CODE LIKE '1%' THEN NVL(( SELECT 1 FROM TPA11STGOODS TPG WHERE TPG.GOODS_CODE = PP.GOODS_CODE AND TPG.PA_SALE_GB = '20' AND TPG.PA_STATUS = '30'),0)
                          ELSE 1 END
               AND 1 = CASE WHEN PP.SALE_PA_CODE LIKE '2%' THEN NVL(( SELECT 1 FROM TPAGMKTGOODS TPG WHERE TPG.GOODS_CODE = PP.GOODS_CODE AND TPG.PA_SALE_GB = '20' AND TPG.PA_STATUS = '30'),0)
                          ELSE 1 END
               AND 1 = CASE WHEN PP.SALE_PA_CODE LIKE '4%' THEN NVL(( SELECT 1 FROM TPANAVERGOODS TPG WHERE TPG.GOODS_CODE = PP.GOODS_CODE AND TPG.PA_SALE_GB = '20' AND TPG.PA_STATUS = '30'),0)
                          ELSE 1 END
               AND 1 = CASE WHEN PP.SALE_PA_CODE LIKE '5%' THEN NVL(( SELECT 1 FROM TPACOPNGOODS TPG WHERE TPG.GOODS_CODE = PP.GOODS_CODE AND TPG.PA_SALE_GB = '20' AND TPG.PA_STATUS = '30'),0)
                          ELSE 1 END
               AND 1 = CASE WHEN PP.SALE_PA_CODE LIKE '6%' THEN NVL(( SELECT 1 FROM TPAWEMPGOODS TPG WHERE TPG.GOODS_CODE = PP.GOODS_CODE AND TPG.PA_SALE_GB = '20' AND TPG.PA_STATUS = '30'),0)
                          ELSE 1 END
               AND 1 = CASE WHEN PP.SALE_PA_CODE LIKE '7%' THEN NVL(( SELECT 1 FROM TPAINTPGOODS TPG WHERE TPG.GOODS_CODE = PP.GOODS_CODE AND TPG.PA_SALE_GB = '20' AND TPG.PA_STATUS = '30'),0)
                          ELSE 1 END
               AND 1 = CASE WHEN PP.SALE_PA_CODE LIKE '8%' THEN NVL(( SELECT 1 FROM TPALTONGOODS TPG WHERE TPG.GOODS_CODE = PP.GOODS_CODE AND TPG.PA_SALE_GB = '20' AND TPG.PA_STATUS = '30'),0)
                          ELSE 1 END
               AND 1 = CASE WHEN PP.SALE_PA_CODE LIKE '9%' THEN NVL(( SELECT 1 FROM TPATMONGOODS TPG WHERE TPG.GOODS_CODE = PP.GOODS_CODE AND TPG.PA_SALE_GB = '20' AND TPG.PA_STATUS = '30'),0)
                          ELSE 1 END
              AND 1 = CASE WHEN PP.SALE_PA_CODE LIKE 'A%' THEN NVL(( SELECT 1 FROM TPASSGGOODS TPG WHERE TPG.GOODS_CODE = PP.GOODS_CODE AND TPG.PA_SALE_GB = '20' AND TPG.PA_STATUS = '30'),0)
                          ELSE 1 END
               AND 1 = CASE WHEN PP.SALE_PA_CODE LIKE 'B%' THEN NVL(( SELECT 1 FROM TPAKAKAOGOODS TPG WHERE TPG.GOODS_CODE = PP.GOODS_CODE AND TPG.PA_SALE_GB = '20' AND TPG.PA_STATUS = '30'),0)
                          ELSE 1 END
               AND 1 = CASE WHEN PP.SALE_PA_CODE LIKE 'C%' THEN NVL(( SELECT 1 FROM TPAHALFGOODS TPG WHERE TPG.GOODS_CODE = PP.GOODS_CODE AND TPG.PA_SALE_GB = '20' AND TPG.PA_STATUS = '30'),0)
                          ELSE 1 END
               AND NOT EXISTS ( SELECT 'X'
                                  FROM TPAPRODUCTOPTION T
                                 WHERE T.GOODS_CODE = DT.GOODS_CODE
                                   AND T.GOODSDT_CODE = DT.GOODSDT_CODE )
         )
         ;
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF( is_existed > 0 ) THEN insert_error_log(); END IF;*/
  -- END --

  v_flag      := 'P315';
  v_error_msg := '[즉시_P315][제휴_공통] 단품코드에는 존재하나 공통 상품-단품 매핑 테이블에 존재하지 않는건';
  is_existed  := 0;

  /* P315. [제휴_공통] 단품코드에는 존재하나 공통 상품-단품 매핑 테이블에 존재하지 않는건 */
  -- 신규동기화에서 검출될 경우 DB이용해 수기로 살린상품인지 확인
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (
          /*신규 동기화용*/
          SELECT DISTINCT GD.GOODS_CODE, GD.GOODSDT_CODE, TG.PA_CODE
            FROM TGOODSDT GD, TPAGOODSTARGET TG
           WHERE GD.MODIFY_DATE >= d_sysdate - 10
             AND GD.GOODS_CODE = TG.GOODS_CODE
             AND TG.PA_GROUP_CODE IN
                 ('01', '02', '04', '05', '06', '07', '09', '11')
             AND GD.SALE_GB != '19'
             AND EXISTS (SELECT 'X'
                    FROM TPAPRODUCTOPTION PD
                   WHERE PD.GOODS_CODE = GD.GOODS_CODE
                     AND PD.GOODSDT_CODE = GD.GOODSDT_CODE)
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAGOODSDTMAPPING MP
                    WHERE MP.GOODS_CODE = GD.GOODS_CODE
                      AND MP.GOODSDT_CODE = GD.GOODSDT_CODE
                      AND MP.PA_CODE = TG.PA_CODE) AND 1 = (CASE
                    WHEN TG.PA_GROUP_CODE = '01' THEN
                     (SELECT 1
                        FROM TPA11STGOODS PG
                       WHERE PG.GOODS_CODE = TG.GOODS_CODE
                         AND PG.PA_SALE_GB = '20'
                         AND PG.PA_STATUS = '30')
                    WHEN TG.PA_GROUP_CODE = '02' THEN
                     (SELECT 1
                        FROM TPAGMKTGOODS PG
                       WHERE PG.GOODS_CODE = TG.GOODS_CODE
                         AND PG.PA_SALE_GB = '20'
                         AND PG.PA_STATUS = '30'
                         AND PG.PA_GROUP_CODE = '02')
                    WHEN TG.PA_GROUP_CODE = '04' THEN
                     (SELECT 1
                        FROM TPANAVERGOODS PG
                       WHERE PG.GOODS_CODE = TG.GOODS_CODE
                         AND PG.PA_SALE_GB = '20'
                         AND PG.PA_STATUS = '30')
                    WHEN TG.PA_GROUP_CODE = '05' THEN
                     (SELECT 1
                        FROM TPACOPNGOODS PG
                       WHERE PG.GOODS_CODE = TG.GOODS_CODE
                         AND PG.PA_SALE_GB = '20'
                         AND PG.PA_STATUS = '30')
                    WHEN TG.PA_GROUP_CODE = '06' THEN
                     (SELECT 1
                        FROM TPAWEMPGOODS PG
                       WHERE PG.GOODS_CODE = TG.GOODS_CODE
                         AND PG.PA_SALE_GB = '20'
                         AND PG.PA_STATUS = '30')
                    WHEN TG.PA_GROUP_CODE = '07' THEN
                     (SELECT 1
                        FROM TPAINTPGOODS PG
                       WHERE PG.GOODS_CODE = TG.GOODS_CODE
                         AND PG.PA_SALE_GB = '20'
                         AND PG.PA_STATUS = '30')
                    WHEN TG.PA_GROUP_CODE = '09' THEN
                     (SELECT 1
                        FROM TPATMONGOODS PG
                       WHERE PG.GOODS_CODE = TG.GOODS_CODE
                         AND PG.PA_SALE_GB = '20'
                         AND PG.PA_STATUS = '30')
                    WHEN TG.PA_GROUP_CODE = '11' THEN
                     (SELECT 1
                        FROM TPAKAKAOGOODS PG
                       WHERE PG.GOODS_CODE = TG.GOODS_CODE
                         AND PG.PA_SALE_GB = '20'
                         AND PG.PA_STATUS = '30')
                    WHEN TG.PA_GROUP_CODE = '12' THEN
                     (SELECT 1
                        FROM TPAHALFGOODS PG
                       WHERE PG.GOODS_CODE = TG.GOODS_CODE
                         AND PG.PA_SALE_GB = '20'
                         AND PG.PA_STATUS = '30')
                  END)));
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;
  -- END --

  -- v_flag      := 'P316';    v_error_msg := '[확인_P316][제휴_공통] 상품이미지 동기화 점검';
  -- is_existed  := 0;

  /* P316. [제휴_공통] 상품이미지 동기화 점검
      --서버 이전 이후 at로 이미지 정상적으로 생성되지 않던 이슈가 있어 OR로직 추가 by jchoi 2020.12.15
      --안정화되면 걷어내야됨.
      SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END DATA_CHECK
        into is_existed
        FROM (
              SELECT GI.GOODS_CODE
                   , NVL(GI.IMAGE_C, GI.IMAGE_G) AS IMAGE_FILE
                FROM TGOODSIMAGE GI
               WHERE GI.IMAGE_G IS NOT NULL
                 AND NOT EXISTS ( SELECT '1'
                                    FROM TPAGOODSIMAGE PGI
                                   WHERE GI.GOODS_CODE = PGI.GOODS_CODE
                                     AND NVL(GI.IMAGE_C, GI.IMAGE_G) = PGI.IMAGE_P
                                 )
                 AND EXISTS ( SELECT '1'
                                FROM TPAGOODS PGI
                                   , TGOODS   GD
                               WHERE PGI.GOODS_CODE = GI.GOODS_CODE
                                 AND PGI.GOODS_CODE = GD.GOODS_CODE
                                 AND GD.SALE_GB <> '19'
                             )
                 AND GI.MODIFY_DATE >= d_sysdate-30
                 AND GI.MODIFY_DATE < SYSDATE - 1/24
  
               UNION ALL
  
              SELECT GI.GOODS_CODE
                   , NVL(GI.IMAGE_AT, GI.IMAGE_AG) AS IMAGE_FILE
                FROM TGOODSIMAGE GI
               WHERE GI.IMAGE_AG IS NOT NULL
                 AND NOT EXISTS ( SELECT '1'
                                    FROM TPAGOODSIMAGE PGI
                                   WHERE GI.GOODS_CODE = PGI.GOODS_CODE
                                     AND ( NVL(GI.IMAGE_AT, GI.IMAGE_AG) = PGI.IMAGE_AP
                                        OR GI.IMAGE_AG = PGI.IMAGE_AP )
                                 )
                 AND EXISTS ( SELECT '1'
                                FROM TPAGOODS PGI
                                   , TGOODS   GD
                               WHERE PGI.GOODS_CODE = GI.GOODS_CODE
                                 AND PGI.GOODS_CODE = GD.GOODS_CODE
                                 AND GD.SALE_GB <> '19'
                             )
                 AND GI.MODIFY_DATE >= d_sysdate-30
                 AND GI.MODIFY_DATE < SYSDATE - 1/24
  
               UNION ALL
  
              SELECT GI.GOODS_CODE
                   , NVL(GI.IMAGE_BT, GI.IMAGE_BG) AS IMAGE_FILE
                FROM TGOODSIMAGE GI
               WHERE GI.IMAGE_BG IS NOT NULL
                 AND NOT EXISTS ( SELECT '1'
                                    FROM TPAGOODSIMAGE PGI
                                   WHERE GI.GOODS_CODE = PGI.GOODS_CODE
                                     AND ( NVL(GI.IMAGE_BT, GI.IMAGE_BG) = PGI.IMAGE_BP
                                        OR GI.IMAGE_BG = PGI.IMAGE_BP )
                                 )
                 AND EXISTS ( SELECT '1'
                                FROM TPAGOODS PGI
                                   , TGOODS   GD
                               WHERE PGI.GOODS_CODE = GI.GOODS_CODE
                                 AND PGI.GOODS_CODE = GD.GOODS_CODE
                                 AND GD.SALE_GB <> '19'
                             )
                 AND GI.MODIFY_DATE >= d_sysdate-30
                 AND GI.MODIFY_DATE < SYSDATE - 1/24
  
               UNION ALL
  
              SELECT GI.GOODS_CODE
                   , NVL(GI.IMAGE_CT, GI.IMAGE_CG) AS IMAGE_FILE
                FROM TGOODSIMAGE GI
               WHERE GI.IMAGE_CG IS NOT NULL
                 AND NOT EXISTS ( SELECT '1'
                                    FROM TPAGOODSIMAGE PGI
                                   WHERE GI.GOODS_CODE = PGI.GOODS_CODE
                                     AND ( NVL(GI.IMAGE_CT, GI.IMAGE_CG) = PGI.IMAGE_CP
                                        OR GI.IMAGE_CG = PGI.IMAGE_CP )
                                 )
                 AND EXISTS ( SELECT '1'
                                FROM TPAGOODS PGI
                                   , TGOODS   GD
                               WHERE PGI.GOODS_CODE = GI.GOODS_CODE
                                 AND PGI.GOODS_CODE = GD.GOODS_CODE
                                 AND GD.SALE_GB <> '19'
                             )
                 AND EXISTS ( SELECT '1'
                                FROM TPAGOODSIMAGE PGI
                               WHERE GI.GOODS_CODE  = PGI.GOODS_CODE
                                 AND PGI.PA_GROUP_CODE = '01'
                                 ) -- 20190927 YEKIM, 11번가만 체크(이베이는 이미지 3개까지만 연동하여, 4번째 상품이미지 동기화하지 않음.)
                 AND GI.MODIFY_DATE >= d_sysdate-30
                 AND GI.MODIFY_DATE < SYSDATE - 1/24
  
  
               UNION ALL
  
              SELECT GI.GOODS_CODE
                   , NVL(GI.IMAGE_DT, GI.IMAGE_DG) AS IMAGE_FILE
                FROM TGOODSIMAGE GI
               WHERE GI.IMAGE_DG IS NOT NULL
                 AND NOT EXISTS ( SELECT '1'
                                    FROM TPAGOODSIMAGE PGI
                                   WHERE GI.GOODS_CODE = PGI.GOODS_CODE
                                     AND ( NVL(GI.IMAGE_DT, GI.IMAGE_DG) = PGI.IMAGE_DP
                                        OR GI.IMAGE_DG = PGI.IMAGE_DP )
                                 )
                 AND EXISTS ( SELECT '1'
                                FROM TPAGOODS PGI
                                   , TGOODS   GD
                               WHERE PGI.GOODS_CODE = GI.GOODS_CODE
                                 AND PGI.GOODS_CODE = GD.GOODS_CODE
                                 AND GD.SALE_GB <> '19'
                             )
                 AND EXISTS ( SELECT '1'
                                FROM TPAGOODSIMAGE PGI
                               WHERE GI.GOODS_CODE  = PGI.GOODS_CODE
                                 AND PGI.PA_GROUP_CODE = '01'
                                 ) -- 20190927 YEKIM, 11번가만 체크(이베이는 이미지 3개까지만 연동하여, 5번째 상품이미지 동기화하지 않음.)
                 AND GI.MODIFY_DATE >= d_sysdate-30
                 AND GI.MODIFY_DATE < SYSDATE - 1/24
  
          ) XA
       ;
  
      IF( is_existed > 0 ) THEN insert_error_log(); END IF;
      -- END --
  */
  v_flag      := 'P317';
  v_error_msg := '[확인_P317][제휴_공통] 정보고시가 없는건 점검 ( TGOODS <-> TPAGOODSOFFER )';
  is_existed  := 0;

  /* P317. [제휴_공통] 정보고시가 없는건 점검 ( TGOODS <-> TPAGOODSOFFER ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT /*+INDEX(GD PK_TGOODS)*/
           GD.GOODS_CODE, GD.OFFER_TYPE
            FROM TGOODS GD, TPAPRODUCT PG
          --  , TOFFER OFR
           WHERE GD.GOODS_CODE = PG.GOODS_CODE
             AND PG.MODIFY_DATE >= d_sysdate - 5
                --   AND GD.GOODS_CODE = OFR.GOODS_CODE
                --   AND GD.OFFER_TYPE = OFR.OFFER_TYPE
             AND GD.SALE_GB <> '19'
             AND NOT EXISTS (SELECT '1'
                    FROM TPAGOODSOFFER POF
                   WHERE GD.GOODS_CODE = POF.GOODS_CODE)
             AND EXISTS
           (SELECT '1'
                    FROM TPAGOODSOFFER PGP, TPAOFFERCODEMAPPING POMP
                   WHERE PGP.PA_OFFER_TYPE = POMP.PA_OFFER_TYPE
                     AND PGP.PA_GROUP_CODE = POMP.PA_GROUP_CODE
                     AND GD.OFFER_TYPE = POMP.OFFER_TYPE)
             AND (SELECT COUNT(1)
                    FROM TPAGOODSTARGET GT
                   WHERE GT.GOODS_CODE = GD.GOODS_CODE
                     AND PA_GOODS_CODE IS NOT NULL) > 0);
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;
  -- END --

  /*  P335 에서 검증 가능할듯한데 ..OFFER 변경되도 TPAGOODS MODIFY_DATE 동기화 하지않음
      v_flag      := 'P318';    v_error_msg := '[확인_P318][제휴_공통] 제휴상품 정보는 존재하나 정보고시가 없는건 점검 ( TPAGOODS <-> TPAGOODSOFFER )';
      is_existed  := 0;
  
  
       P318. [제휴_공통] 제휴상품 정보는 존재하나 정보고시가 없는건 점검 ( TPAGOODS <-> TPAGOODSOFFER )
      SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END DATA_CHECK
        into is_existed
        FROM (
              SELECT PG.GOODS_CODE
                FROM TPAGOODS PG
               WHERE NOT EXISTS ( SELECT '1'
                                    FROM TPAGOODSOFFER PAOF
                                       , TCODE TC
                                   WHERE PG.GOODS_CODE  = PAOF.GOODS_CODE
                                     AND TC.CODE_GROUP  = PAOF.PA_GROUP_CODE
                                     AND TC.CODE_LGROUP ='O501'
                                     AND PG.SALE_PA_CODE LIKE '%'||TC.CODE_MGROUP||'%'
                                )
                 AND PG.MODIFY_DATE >= d_sysdate-30
             )
             ;
         IF( is_existed > 0 ) THEN insert_error_log(); END IF;
      -- END --
  */

  /* 신규 가격 테이블로(TPAGOODSPRICEAPPLY) 변경되어 주석처리  P329로 대체
   v_flag      := 'P320';    v_error_msg := '[즉시_P320][제휴_공통] 상품가격정보 동기화 점검';
   is_existed  := 0;
  
   /* P320. [제휴_공통] 상품가격정보 동기화 점검
   일시불금액이 예전엔 들어가 있지 않아 몇일간 에러가 나올 수 있다.
  
   SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END DATA_CHECK
     into is_existed
     FROM (
           SELECT TPG.PA_CODE, TPG.GOODS_CODE, GP.APPLY_DATE
             FROM TGOODS GD
                , TGOODSPRICE GP
                , TPAGOODSTARGET TPG
            WHERE GD.SALE_GB = '00'
              AND SYSDATE BETWEEN GD.SALE_START_DATE AND GD.SALE_END_DATE
              AND GD.GOODS_CODE = GP.GOODS_CODE
              AND GP.APPLY_DATE = ( SELECT /*+ INDEX_DESC (PR PK_TGOODSPRICE)
                                           PR.APPLY_DATE
                                      FROM TGOODSPRICE  PR
                                     WHERE GP.GOODS_CODE = PR.GOODS_CODE
                                       AND PR.APPLY_DATE < SYSDATE
                                       AND ROWNUM = 1
                                  )
              AND GP.APPLY_DATE <= SYSDATE - 1/24
              AND GD.GOODS_CODE = TPG.GOODS_CODE
              AND NOT EXISTS ( SELECT '1'
                                 FROM TPAGOODSPRICE PGP
                                WHERE PGP.GOODS_CODE = TPG.GOODS_CODE
                                  AND PGP.PA_CODE = TPG.PA_CODE
                                  AND PGP.APPLY_DATE = GP.APPLY_DATE
                                  AND PGP.PRICE_SEQ = GP.PRICE_SEQ
                                  AND PGP.SALE_PRICE = GP.SALE_PRICE
                                  AND PGP.DC_AMT = GP.ARS_DC_AMT
                                  AND PGP.LUMP_SUM_DC_AMT = GP.LUMP_SUM_DC_AMT
                             )
              AND TPG.PA_GROUP_CODE NOT IN ('11','12')
              AND 1 = CASE WHEN TPG.PA_GROUP_CODE = '01' THEN NVL((SELECT 1 FROM TPA11STGOODS PG WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE AND PG.PA_CODE = TPG.PA_CODE AND PG.GOODS_CODE = TPG.GOODS_CODE AND PG.PA_SALE_GB = '20' AND PG.PA_STATUS = '30'),0)
                           ELSE 1 END
              AND 1 = CASE WHEN TPG.PA_GROUP_CODE IN ('02','03') THEN NVL((SELECT 1 FROM TPAGMKTGOODS PG WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE AND PG.PA_CODE = TPG.PA_CODE AND PG.GOODS_CODE = TPG.GOODS_CODE AND PG.PA_SALE_GB = '20' AND PG.PA_STATUS = '30'),0)
                           ELSE 1 END
              AND 1 = CASE WHEN TPG.PA_GROUP_CODE = '04' THEN NVL((SELECT 1 FROM TPANAVERGOODS PG WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE AND PG.PA_CODE = TPG.PA_CODE AND PG.GOODS_CODE = TPG.GOODS_CODE AND PG.PA_SALE_GB = '20' AND PG.PA_STATUS = '30'),0)
                           ELSE 1 END
              AND 1 = CASE WHEN TPG.PA_GROUP_CODE = '05' THEN NVL((SELECT 1 FROM TPACOPNGOODS PG WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE AND PG.PA_CODE = TPG.PA_CODE AND PG.GOODS_CODE = TPG.GOODS_CODE AND PG.PA_SALE_GB = '20' AND PG.PA_STATUS = '30'),0)
                           ELSE 1 END
              AND 1 = CASE WHEN TPG.PA_GROUP_CODE = '06' THEN NVL((SELECT 1 FROM TPAWEMPGOODS PG WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE AND PG.PA_CODE = TPG.PA_CODE AND PG.GOODS_CODE = TPG.GOODS_CODE AND PG.PA_SALE_GB = '20' AND PG.PA_STATUS = '30'),0)
                           ELSE 1 END
              AND 1 = CASE WHEN TPG.PA_GROUP_CODE = '07' THEN NVL((SELECT 1 FROM TPAINTPGOODS PG WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE AND PG.PA_CODE = TPG.PA_CODE AND PG.GOODS_CODE = TPG.GOODS_CODE AND PG.PA_SALE_GB = '20' AND PG.PA_STATUS = '30'),0)
                           ELSE 1 END
              AND 1 = CASE WHEN TPG.PA_GROUP_CODE = '08' THEN NVL((SELECT 1 FROM TPALTONGOODS PG WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE AND PG.PA_CODE = TPG.PA_CODE AND PG.GOODS_CODE = TPG.GOODS_CODE AND PG.PA_SALE_GB = '20' AND PG.PA_STATUS = '30'),0)
                           ELSE 1 END
              AND 1 = CASE WHEN TPG.PA_GROUP_CODE = '09' THEN NVL((SELECT 1 FROM TPATMONGOODS PG WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE AND PG.PA_CODE = TPG.PA_CODE AND PG.GOODS_CODE = TPG.GOODS_CODE AND PG.PA_SALE_GB = '20' AND PG.PA_STATUS = '30'),0)
                           ELSE 1 END
              AND 1 = CASE WHEN TPG.PA_GROUP_CODE = '10' THEN NVL((SELECT 1 FROM TPASSGGOODS PG WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE AND PG.PA_CODE = TPG.PA_CODE AND PG.GOODS_CODE = TPG.GOODS_CODE AND PG.PA_SALE_GB = '20' AND PG.PA_STATUS = '30'),0)
                           ELSE 1 END
  
        UNION ALL
  
           SELECT TPG.PA_CODE, TPG.GOODS_CODE, GP.APPLY_DATE
             FROM TGOODS GD
                , TGOODSPRICE GP
                , TPAGOODSTARGET TPG
            WHERE GD.SALE_GB = '00'
              AND SYSDATE BETWEEN GD.SALE_START_DATE AND GD.SALE_END_DATE
              AND GD.GOODS_CODE = GP.GOODS_CODE
              AND GP.APPLY_DATE = ( SELECT /*+ INDEX_DESC (PR PK_TGOODSPRICE)
                                           PR.APPLY_DATE
                                      FROM TGOODSPRICE  PR
                                     WHERE GP.GOODS_CODE = PR.GOODS_CODE
                                       AND PR.APPLY_DATE < SYSDATE
                                       AND ROWNUM = 1
                                  )
              AND GP.APPLY_DATE <= SYSDATE - 1/24
              AND GD.GOODS_CODE = TPG.GOODS_CODE
              AND NOT EXISTS ( SELECT '1'
                                 FROM TPAGOODSPRICEAPPLY PGP
                                WHERE PGP.GOODS_CODE = TPG.GOODS_CODE
                                  AND PGP.PA_CODE = TPG.PA_CODE
                                  AND PGP.APPLY_DATE = GP.APPLY_DATE
                                  AND PGP.PRICE_SEQ = GP.PRICE_SEQ
                                  AND PGP.SALE_PRICE = GP.SALE_PRICE
                                  AND PGP.ARS_DC_AMT = GP.ARS_DC_AMT
                                  AND PGP.LUMP_SUM_DC_AMT = GP.LUMP_SUM_DC_AMT
                             )
              AND TPG.PA_GROUP_CODE IN ('11','12')
              AND 1 = CASE WHEN TPG.PA_GROUP_CODE = '11' THEN NVL((SELECT 1 FROM TPAKAKAOGOODS PG WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE AND PG.PA_CODE = TPG.PA_CODE AND PG.GOODS_CODE = TPG.GOODS_CODE AND PG.PA_SALE_GB = '20' AND PG.PA_STATUS = '30'),0)
                           WHEN TPG.PA_GROUP_CODE = '12' THEN NVL((SELECT 1 FROM TPAHALFGOODS PG WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE AND PG.PA_CODE = TPG.PA_CODE AND PG.GOODS_CODE = TPG.GOODS_CODE AND PG.PA_SALE_GB = '20' AND PG.PA_STATUS = '30'),0)
                           ELSE 1 END
          ) XA
          ;
  
   IF( is_existed > 0 ) THEN insert_error_log(); END IF;
   
  */
  -- END --

  /* 필요없는 체크...
  v_flag      := 'P322';    v_error_msg := '[즉시_P322][제휴_공통] 상품이관 처리되었으나 적재된 데이터가 1건도 없는건';
  is_existed  := 0;
  
  --P322. [제휴_공통] 상품이관 처리되었으나 적재된 데이터가 1건도 없는건
  SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END DATA_CHECK
    into is_existed
    FROM (
          SELECT PA.GOODS_CODE, PA.PA_CODE
            FROM ( SELECT PA11.GOODS_CODE
                        , PA11.PA_CODE
                     FROM TPA11STGOODS PA11
                    WHERE PA11.PRODUCT_NO IS NOT NULL
                      AND PA11.MODIFY_DATE >= d_sysdate-5
  
                    UNION ALL
  
                   SELECT PAGM.GOODS_CODE
                        , PAGM.PA_CODE
                     FROM TPAGMKTGOODS PAGM
                    WHERE PAGM.ITEM_NO IS NOT NULL
                      AND PAGM.MODIFY_DATE >= d_sysdate-5
  
                    UNION ALL
  
                   SELECT PANG.GOODS_CODE
                        , PANG.PA_CODE
                     FROM TPANAVERGOODS PANG
                    WHERE PANG.PRODUCT_ID IS NOT NULL
                      AND PANG.MODIFY_DATE >= d_sysdate-5
  
                    UNION ALL
  
                   SELECT PACP.GOODS_CODE
                        , PACP.PA_CODE
                     FROM TPACOPNGOODS PACP
                    WHERE PACP.SELLER_PRODUCT_ID IS NOT NULL
                      AND PACP.MODIFY_DATE >= d_sysdate-5
  
                    UNION ALL
  
                   SELECT WEMP.GOODS_CODE
                        , WEMP.PA_CODE
                     FROM TPAWEMPGOODS WEMP
                    WHERE WEMP.PRODUCT_NO IS NOT NULL
                      AND WEMP.MODIFY_DATE >= d_sysdate-5
  
                    UNION ALL
  
                   SELECT INTP.GOODS_CODE
                        , INTP.PA_CODE
                     FROM TPAINTPGOODS INTP
                    WHERE INTP.PRD_NO IS NOT NULL
                      AND INTP.MODIFY_DATE >= d_sysdate-5
  
                    UNION ALL
  
                   SELECT LTON.GOODS_CODE
                        , LTON.PA_CODE
                     FROM TPALTONGOODS LTON
                    WHERE LTON.SPD_NO IS NOT NULL
                      AND LTON.MODIFY_DATE >= d_sysdate-5
  
                    UNION ALL
  
                   SELECT TMON.GOODS_CODE
                        , TMON.PA_CODE
                     FROM TPATMONGOODS TMON
                    WHERE TMON.DEAL_NO IS NOT NULL
                      AND TMON.MODIFY_DATE >= d_sysdate-5
  
                    UNION ALL
  
                   SELECT SSG.GOODS_CODE
                        , SSG.PA_CODE
                     FROM TPASSGGOODS SSG
                    WHERE SSG.ITEM_ID IS NOT NULL
                      AND SSG.MODIFY_DATE >= d_sysdate-5
                 ) PA
             WHERE NOT EXISTS ( SELECT '1'
                                  FROM TPAGOODSTRANSLOG PAT
                                 WHERE PA.GOODS_CODE = PAT.GOODS_CODE
                                   AND PA.PA_CODE    = PAT.PA_CODE
                              )
         )
         ;
  
  IF( is_existed > 0 ) THEN insert_error_log(); END IF;
  -- END --
  */

  /*    v_flag      := 'P331';    v_error_msg := '[확인_P331][제휴_11번가] 상품정보 전송 2회 이상 실패건';
  is_existed  := 0;
  
  \* P331. [제휴_11번가] 상품정보 전송 실패건 *\
  SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END DATA_CHECK
    into is_existed
    FROM (
          SELECT PA.GOODS_CODE
               , PA.PA_CODE
               , PA.ITEM_NO
               , PA.RTN_CODE
               , PA.RTN_MSG
            FROM TPAGOODSTRANSLOG PA
           WHERE PA.SUCCESS_YN ='0'
             AND PA.PROC_DATE >= sysdate-1
             AND 1=2 --나중에 여유될때 주석치고 확인해보기 현재 안보고 있음.
             AND PA.PA_CODE IN ('11','12')
             AND PA.RTN_MSG NOT LIKE '%Internal Server Error%'
             AND PA.RTN_MSG NOT LIKE '%ERROR%'
             AND PA.RTN_MSG NOT LIKE '%Bad Gateway%'
             AND PA.RTN_MSG NOT LIKE '%상품에 해당하는 재고번호가 아닙니다%'--TPAGOODSDTMAPPING 다시 연동처리함.
             AND PA.RTN_MSG NOT LIKE '%해당 상품의 정보를 찾을 수 없습니다%'--PA_STATUS 90으로 만들어 연동 제외처리함
             AND (PA.GOODS_CODE, ITEM_NO) NOT IN (('100614','160'),('101192','007'),('104843','003'),('100071','089')
                                                 ,('100614','161'),('100071','088'),('100558','003'),('100558','002'),('104843','006'))
           --AND PA.GOODS_CODE NOT IN (20036282, 20033699, 20029749, 20035543, 20017046)
        GROUP BY PA.GOODS_CODE, PA.PA_CODE, PA.ITEM_NO, PA.RTN_CODE, PA.RTN_MSG
    HAVING COUNT(PA.GOODS_CODE) > 1
         )
         ;
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF( is_existed > 0 ) THEN insert_error_log(); END IF;*/
  -- END --

  /*    v_flag      := 'P334';    v_error_msg := '[확인_P334][제휴_EBAY] 상품정보 전송 2회 이상 실패건';
  is_existed  := 0;
  
  \* P334. [제휴_EBAY] 상품정보 전송 실패건 *\
  SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END DATA_CHECK
    into is_existed
    FROM (
          SELECT PA.GOODS_CODE
               , PA.PA_CODE
               , PA.ITEM_NO
               , PA.RTN_CODE
               , PA.RTN_MSG
            FROM TPAGOODSTRANSLOG PA
           WHERE PA.SUCCESS_YN ='0'
             AND PA.PROC_DATE >= sysdate-1
             AND 1=2 --나중에 여유될때 주석치고 확인해보기 현재 안보고 있음.
             AND PA.PA_CODE IN ('21','22')
             AND PA.RTN_MSG NOT LIKE '%동일한 배치가%'
             AND PA.RTN_MSG NOT LIKE '%Connection reset%'
             AND PA.RTN_MSG NOT LIKE '%string buffer too small%'
             AND PA.RTN_MSG NOT LIKE '%IF_PAGMKTAPI_V2_01_003500%' --몬지 모르겠지만 TPAGOODSDTMAPPING 테이블 trans_target_yn = 1로 바꿈으로서 해결됨.
             AND PA.RTN_MSG NOT LIKE '%IF_PAGMKTAPI_V2_01_003502%' --몬지 모르겠지만 TPAGOODSDTMAPPING 테이블 trans_target_yn = 1로 바꿈으로서 해결됨.
             AND PA.RTN_MSG NOT LIKE '%IF_PAGMKTAPI_V2_01_014_O || '
             AND PA.RTN_MSG NOT LIKE '%IF_PAGMKTAPI_V2_01_014_B || '
             --아래는 무슨 에런지 모르겠지만 따로 처리할 수 있는게 없음. 오래 수정되지 않으면 연동 제외 처리 필요.
             AND PA.RTN_MSG NOT LIKE '%IF_PAGMKTAPI_V2_01_014_B500 '
             AND PA.RTN_MSG NOT LIKE '%IF_PAGMKTAPI_V2_01_014_B502 '
             AND PA.RTN_MSG NOT LIKE '%IF_PAGMKTAPI_V2_01_014_O500 '
             AND PA.RTN_MSG NOT LIKE '%IF_PAGMKTAPI_V2_01_014_O502 '
             AND PA.RTN_MSG NOT LIKE '%IF_PAGMKTAPI_V2_01_004 || Index: 0, Size: 0' --일시적으로 나는 에러로 보
        GROUP BY PA.GOODS_CODE, PA.PA_CODE, PA.ITEM_NO, PA.RTN_CODE, PA.RTN_MSG
          HAVING COUNT(GOODS_CODE) > 1
         )
         ;
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF( is_existed > 0 ) THEN insert_error_log(); END IF;*/
  -- END --

  /*    v_flag      := 'P337';    v_error_msg := '[확인_P337][제휴_네이버] 상품정보 전송 2회 이상 실패건';
  is_existed  := 0;

  \* P337. [제휴_네이버] 상품정보 전송 실패건 *\
  SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END DATA_CHECK
    into is_existed
    FROM (
          SELECT PA.GOODS_CODE
               , PA.PA_CODE
               , PA.ITEM_NO
               , PA.RTN_CODE
               , PA.RTN_MSG
            FROM TPAGOODSTRANSLOG PA
           WHERE PA.SUCCESS_YN ='0'
             AND PA.PROC_DATE >= sysdate-1
             AND 1=2 --나중에 여유될때 주석치고 확인해보기 현재 안보고 있음.
             AND PA.PA_CODE = '41'
             AND PA.RTN_MSG NOT LIKE '%주소 입니다.%'
             AND PA.RTN_MSG NOT LIKE '%IF_PANAVERAPI_01_001 || 오류메세지 : 해당 카테고리는 사용하실 수 없습니다%'
        GROUP BY PA.GOODS_CODE, PA.PA_CODE, PA.ITEM_NO, PA.RTN_CODE, PA.RTN_MSG
           HAVING COUNT(PA.GOODS_CODE) > 1
         )
         ;
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF( is_existed > 0 ) THEN insert_error_log(); END IF;*/
  -- END --

  v_flag      := 'P324';
  v_error_msg := '[확인_P324][제휴_11번가] 상품정보 기초코드 데이터 유효성 검사';
  is_existed  := 0;

  /* P324. [제휴_11번가] 상품정보 기초코드 데이터 유효성 검사 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT PA11.GOODS_CODE
            FROM TPA11STGOODS PA11
           WHERE NOT EXISTS
           (SELECT '1'
                    FROM TPAGOODSKINDS PGK
                   WHERE PGK.PA_GROUP_CODE = '01'
                     AND PA11.PA_LMSD_KEY = NVL(PGK.PA_DGROUP, PGK.PA_SGROUP))
             AND PA11.MODIFY_DATE >= d_sysdate - 5
             AND PA11.PA_LMSD_KEY != '1'
          
          UNION ALL
          
          SELECT PA11.GOODS_CODE
            FROM TPA11STGOODS PA11
           WHERE NOT EXISTS (SELECT '1'
                    FROM TCODE TC
                   WHERE TC.CODE_LGROUP = 'O502'
                     AND TC.CODE_MGROUP = PA11.PA_STATUS)
             AND PA11.MODIFY_DATE >= d_sysdate - 5
          
          UNION ALL
          
          SELECT PA11.GOODS_CODE
            FROM TPA11STGOODS PA11
           WHERE PA11.ORGN_TYP_CD NOT IN ('01', '02', '03')
             AND PA11.MODIFY_DATE >= d_sysdate - 5);
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;
  -- END --

  v_flag      := 'P325';
  v_error_msg := '[확인_P325][제휴_공통] 제휴상품 테이블에는 존재하는 상품코드이나 제휴공통 상품코드에 존재하지 않은 건';
  is_existed  := 0;

  /* P325. [제휴_공통] 제휴상품 테이블에는 존재하는 상품코드이나 제휴공통 상품코드에 존재하지 않은 건 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT PA.GOODS_CODE, PA.PA_CODE
            FROM (SELECT PA11.GOODS_CODE, PA11.PA_CODE
                    FROM TPA11STGOODS PA11
                   WHERE PA11.INSERT_DATE >= d_sysdate - 5
                  
                  UNION ALL
                  
                  SELECT PAGM.GOODS_CODE, PAGM.PA_CODE
                    FROM TPAGMKTGOODS PAGM
                   WHERE PAGM.INSERT_DATE >= d_sysdate - 5
                  
                  UNION ALL
                  
                  SELECT PANG.GOODS_CODE, PANG.PA_CODE
                    FROM TPANAVERGOODS PANG
                   WHERE PANG.INSERT_DATE >= d_sysdate - 5
                  
                  UNION ALL
                  /*
                  SELECT PACP.GOODS_CODE
                       , PACP.PA_CODE
                    FROM TPACOPNGOODS PACP
                   WHERE PACP.INSERT_DATE >= d_sysdate-5
                  
                   UNION ALL
                   */
                  SELECT WEMP.GOODS_CODE, WEMP.PA_CODE
                    FROM TPAWEMPGOODS WEMP
                   WHERE WEMP.INSERT_DATE >= d_sysdate - 5
                  
                  UNION ALL
                  
                  SELECT GG.GOODS_CODE, GG.PA_CODE
                    FROM TPAINTPGOODS GG
                   WHERE GG.INSERT_DATE >= d_sysdate - 5
                  
                  UNION ALL
                  
                  SELECT LTON.GOODS_CODE, LTON.PA_CODE
                    FROM TPALTONGOODS LTON
                   WHERE LTON.INSERT_DATE >= d_sysdate - 5
                  
                  UNION ALL
                  
                  SELECT TMON.GOODS_CODE, TMON.PA_CODE
                    FROM TPATMONGOODS TMON
                   WHERE TMON.INSERT_DATE >= d_sysdate - 5
                  
                  UNION ALL
                  
                  SELECT SSG.GOODS_CODE, SSG.PA_CODE
                    FROM TPASSGGOODS SSG
                   WHERE SSG.INSERT_DATE >= d_sysdate - 5
                  
                  UNION ALL
                  
                  SELECT HAF.GOODS_CODE, HAF.PA_CODE
                    FROM TPAHALFGOODS HAF
                   WHERE HAF.INSERT_DATE >= d_sysdate - 5
                  
                  ) PA
           WHERE NOT EXISTS (SELECT '1'
                    FROM TPAGOODS PG
                   WHERE PA.GOODS_CODE = PG.GOODS_CODE
                     AND INSTR(PG.SALE_PA_CODE, PA.PA_CODE) > 0
                  
                  )
             AND NOT EXISTS
           (SELECT '1'
                    FROM TPAPRODUCT PD
                   WHERE PA.GOODS_CODE = PD.GOODS_CODE
                     AND INSTR(PD.SALE_PA_CODE, PA.PA_CODE) > 0));
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;
  -- END --

  v_flag      := 'P326';
  v_error_msg := '[확인_P326][제휴_공통] 수정일시가 동기화 배치 시간을 지났는데 전송대상여부가 1 인건';
  is_existed  := 0;

  /* P326. [제휴_공통] 수정일시가 동기화 배치 시간을 지났는데 전송대상여부가 '1' 인건 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT PA11.GOODS_CODE, PA11.PA_CODE
            FROM TPA11STGOODS PA11
           WHERE PA11.TRANS_TARGET_YN = '1'
             AND PA11.PA_STATUS = '30'
             AND PA11.PA_SALE_GB = '20'
             AND PA11.MODIFY_DATE >= d_sysdate - 3
             AND PA11.MODIFY_DATE < SYSDATE - INTERVAL '60'
           MINUTE
             AND NOT EXISTS (SELECT '1'
                    FROM TGOODS GD
                   WHERE PA11.GOODS_CODE = GD.GOODS_CODE
                     AND GD.SALE_GB = '19')
          
          UNION ALL
          
          SELECT PAGM.GOODS_CODE, PAGM.PA_CODE
            FROM TPAGMKTGOODS PAGM, TGOODS TG
           WHERE PAGM.TRANS_TARGET_YN = '1'
             AND PAGM.PA_STATUS = '30'
             AND PAGM.MODIFY_DATE >= d_sysdate - 3
                --아래부분 추가 THJEON
             AND PAGM.PA_SALE_GB = '20'
             AND PAGM.MODIFY_DATE < SYSDATE - INTERVAL '60'
           MINUTE
                --위부분 추가 , 지마켓 전송시간 3분,33분이기에 최근 변경60분 이내데이터는 검색하지않음
             AND NOT EXISTS
           (SELECT '1'
                    FROM TGOODS GD
                   WHERE PAGM.GOODS_CODE = GD.GOODS_CODE
                     AND GD.SALE_GB = '19')
             AND PAGM.GOODS_CODE = TG.GOODS_CODE
             AND FUN_GET_ORDER_ABLE_QTY(TG.GOODS_CODE, '%', TG.WH_CODE) > 0
          
          UNION ALL
          
          SELECT COPN.GOODS_CODE, COPN.PA_CODE
            FROM TPACOPNGOODS COPN
           WHERE COPN.PA_CODE IN ('51', '52')
             AND COPN.TRANS_TARGET_YN = '1'
             AND COPN.PA_STATUS = '30'
             AND COPN.MODIFY_DATE >= d_sysdate - 3
             AND COPN.PA_SALE_GB = '20'
             AND COPN.APPROVAL_STATUS NOT IN ('05', '15') -- 임시저장, 승인대기중 상태는 제외
             AND COPN.MODIFY_DATE < SYSDATE - INTERVAL '60'
           MINUTE
             AND NOT EXISTS (SELECT '1'
                    FROM TGOODS GD
                   WHERE COPN.GOODS_CODE = GD.GOODS_CODE
                     AND GD.SALE_GB = '19')
          
          UNION ALL
          
          SELECT WEMP.GOODS_CODE, WEMP.PA_CODE
            FROM TPAWEMPGOODS WEMP
           WHERE WEMP.PA_CODE IN ('61', '62')
             AND WEMP.TRANS_TARGET_YN = '1'
             AND WEMP.PA_STATUS = '30'
             AND WEMP.MODIFY_DATE >= d_sysdate - 3
             AND WEMP.PA_SALE_GB = '20'
             AND WEMP.MODIFY_DATE < SYSDATE - INTERVAL '60'
           MINUTE
             AND NOT EXISTS (SELECT '1'
                    FROM TGOODS GD
                   WHERE WEMP.GOODS_CODE = GD.GOODS_CODE
                     AND GD.SALE_GB = '19')
          
          UNION ALL
          
          SELECT PG.GOODS_CODE, PG.PA_CODE
            FROM TPAINTPGOODS PG
           WHERE PG.TRANS_TARGET_YN = '1'
             AND PG.PA_SALE_GB = '20'
             AND PG.PA_STATUS = '30'
             AND PG.MODIFY_DATE >= d_sysdate - 3
             AND PG.PA_SALE_GB = '20'
             AND PG.MODIFY_DATE < SYSDATE - INTERVAL '60'
           MINUTE
             AND NOT EXISTS (SELECT '1'
                    FROM TGOODS GD
                   WHERE PG.GOODS_CODE = GD.GOODS_CODE
                     AND GD.SALE_GB = '19')
          
          UNION ALL
          
          SELECT LTON.GOODS_CODE, LTON.PA_CODE
            FROM TPALTONGOODS LTON
           WHERE LTON.TRANS_TARGET_YN = '1'
             AND LTON.PA_SALE_GB = '20'
             AND LTON.PA_STATUS = '30'
             AND LTON.MODIFY_DATE >= d_sysdate - 3
             AND LTON.MODIFY_DATE < SYSDATE - INTERVAL '60'
           MINUTE
             AND NOT EXISTS (SELECT '1'
                    FROM TGOODS GD
                   WHERE LTON.GOODS_CODE = GD.GOODS_CODE
                     AND GD.SALE_GB = '19')
          
          UNION ALL
          
          SELECT TMON.GOODS_CODE, TMON.PA_CODE
            FROM TPATMONGOODS TMON
           WHERE TMON.TRANS_TARGET_YN = '1'
             AND TMON.PA_SALE_GB = '20'
             AND TMON.PA_STATUS = '30'
             AND TMON.MODIFY_DATE >= d_sysdate - 3
             AND TMON.MODIFY_DATE < SYSDATE - INTERVAL '60'
           MINUTE
             AND NOT EXISTS (SELECT '1'
                    FROM TGOODS GD
                   WHERE TMON.GOODS_CODE = GD.GOODS_CODE
                     AND GD.SALE_GB = '19')
          
          UNION ALL
          
          SELECT SSG.GOODS_CODE, SSG.PA_CODE
            FROM TPASSGGOODS SSG
           WHERE SSG.TRANS_TARGET_YN = '1'
             AND SSG.PA_SALE_GB = '20'
             AND SSG.PA_STATUS = '30'
             AND SSG.MODIFY_DATE >= d_sysdate - 3
             AND SSG.MODIFY_DATE < SYSDATE - INTERVAL '60'
           MINUTE
             AND NOT EXISTS (SELECT '1'
                    FROM TGOODS GD
                   WHERE SSG.GOODS_CODE = GD.GOODS_CODE
                     AND GD.SALE_GB = '19')
          
          UNION ALL
          
          SELECT KG.GOODS_CODE, KG.PA_CODE
            FROM TPAKAKAOGOODS KG
           WHERE KG.TRANS_TARGET_YN = '1'
             AND KG.PA_SALE_GB = '20'
             AND KG.PA_STATUS = '30'
             AND KG.MODIFY_DATE >= d_sysdate - 3
             AND KG.MODIFY_DATE < SYSDATE - INTERVAL '60'
           MINUTE
             AND NOT EXISTS (SELECT '1'
                    FROM TGOODS GD
                   WHERE KG.GOODS_CODE = GD.GOODS_CODE
                     AND GD.SALE_GB = '19')
          
          UNION ALL
          
          SELECT HG.GOODS_CODE, HG.PA_CODE
            FROM TPAHALFGOODS HG
           WHERE HG.TRANS_TARGET_YN = '1'
             AND HG.PA_SALE_GB = '20'
             AND HG.PA_STATUS = '30'
             AND HG.MODIFY_DATE >= d_sysdate - 3
             AND HG.MODIFY_DATE < SYSDATE - INTERVAL '60'
           MINUTE
             AND NOT EXISTS (SELECT '1'
                    FROM TGOODS GD
                   WHERE HG.GOODS_CODE = GD.GOODS_CODE
                     AND GD.SALE_GB = '19')
          
          );
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;
  -- END --

  v_flag      := 'P332';
  v_error_msg := '[즉시_P332][제휴_공통]출고지별 배송비 미전송건';
  is_existed  := 0;

  /* P332. [제휴_공통]출고지별 배송비 미전송건*/
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT PCC.ENTP_CODE
            FROM TPACUSTCNSHIPCOST PCC
           WHERE PCC.TRANS_CN_COST_YN = '1'
             AND EXISTS (SELECT 1
                    FROM TPAENTPSLIP PES
                   WHERE PES.PA_CODE = PCC.PA_CODE
                     AND PES.ENTP_CODE = PCC.ENTP_CODE
                     AND PES.ENTP_MAN_SEQ = PCC.ENTP_MAN_SEQ
                     AND PES.PA_ADDR_GB = '30')
             AND EXISTS
           (SELECT 1
                    FROM TPACUSTSHIPCOST PCS
                   WHERE PCS.ENTP_CODE = PCC.ENTP_CODE
                     AND PCS.SHIP_COST_CODE = PCC.SHIP_COST_CODE
                     AND PCS.TRANS_TARGET_YN = '0')
             AND ENTP_CODE NOT IN
                 ('104529', '105032', '108140', '108849', '112182'));
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;
  -- END --

  v_flag      := 'P333';
  v_error_msg := '[즉시_P333][제휴_공통]출고지별 배송비 동기화미처리건';
  is_existed  := 0;

  /* P333. [제휴_공통]출고지별 배송비 동기화미처리건 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT 1
            FROM TPACUSTSHIPCOST PCS
           WHERE PCS.SHIP_COST_CODE LIKE 'CN%'
             AND PCS.TRANS_CN_COST_YN = '1'
             AND EXISTS
           ( --- YEKIM 19/10/18, TPACUSTSHIPCOST TB는 11번가만 사용. 11번가 입점 된 상품들 한해 SD체크
                  SELECT 1
                    FROM TPAGOODS PG, TPA11STGOODS PGG
                   WHERE PG.GOODS_CODE = PGG.GOODS_CODE
                     AND PG.ENTP_CODE = PCS.ENTP_CODE
                     AND PG.SHIP_COST_CODE = PCS.SHIP_COST_CODE)
          UNION
          SELECT 1
            FROM TPACUSTCNSHIPCOST PCC
           WHERE PCC.TRANS_TARGET_YN = '1');
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;
  -- END --

  v_flag      := 'P327';
  v_error_msg := '[확인_P327][제휴_공통] 제휴상품코드가 존재하는데 입점상태가 입점완료가 아닌건';
  is_existed  := 0;

  /* P327. [제휴_공통] 제휴상품코드가 존재하는데 입점상태가 입점완료가 아닌건 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT PA11.GOODS_CODE, PA11.PA_CODE
            FROM TPA11STGOODS PA11
           WHERE PA11.PRODUCT_NO IS NOT NULL
             AND PA11.PA_STATUS NOT IN ('30', '40', '90')
             AND PA11.PA_SALE_GB = '20'
             AND PA11.MODIFY_DATE >= d_sysdate - 4
             AND PA11.GOODS_CODE NOT IN
                 ('20033069', '20033070', '20033072', '20033680', '20033830')
          
          UNION ALL
          
          SELECT PAGM.GOODS_CODE, PAGM.PA_CODE
            FROM TPAGMKTGOODS PAGM
           WHERE PAGM.ITEM_NO IS NOT NULL
             AND PAGM.PA_STATUS NOT IN ('30', '40', '90')
             AND PAGM.PA_SALE_GB = '20'
             AND PAGM.MODIFY_DATE >= d_sysdate - 4
             AND PAGM.GOODS_CODE NOT IN ('20094984', '22539087')
          
          UNION ALL
          
          SELECT PACP.GOODS_CODE, PACP.PA_CODE
            FROM TPACOPNGOODS PACP
           WHERE PACP.SELLER_PRODUCT_ID IS NOT NULL
             AND PACP.PA_STATUS NOT IN ('30', '40', '80', '90') --90은 수기로 상품 연동끊은 건. 80은 분리요청
             AND PACP.PA_SALE_GB = '20'
             AND PACP.MODIFY_DATE >= d_sysdate - 4
          
          UNION ALL
          
          SELECT WEMP.GOODS_CODE, WEMP.PA_CODE
            FROM TPAWEMPGOODS WEMP
           WHERE WEMP.PRODUCT_NO IS NOT NULL
             AND WEMP.PA_STATUS NOT IN ('30', '40', '90')
             AND WEMP.PA_SALE_GB = '20'
             AND WEMP.MODIFY_DATE >= d_sysdate - 4
          
          UNION ALL
          
          SELECT PG.GOODS_CODE, PG.PA_CODE
            FROM TPAINTPGOODS PG
           WHERE PG.PRD_NO IS NOT NULL
             AND PG.PA_STATUS NOT IN ('30', '40', '90')
             AND PG.PA_SALE_GB = '20'
             AND PG.MODIFY_DATE >= d_sysdate - 4
          
          UNION ALL
          
          SELECT LTON.GOODS_CODE, LTON.PA_CODE
            FROM TPALTONGOODS LTON
           WHERE LTON.SPD_NO IS NOT NULL
             AND LTON.PA_STATUS NOT IN ('30', '40', '90')
             AND LTON.PA_SALE_GB = '20'
             AND LTON.MODIFY_DATE >= d_sysdate - 4
          
          UNION ALL
          
          SELECT TMON.GOODS_CODE, TMON.PA_CODE
            FROM TPATMONGOODS TMON
           WHERE TMON.DEAL_NO IS NOT NULL
             AND TMON.PA_STATUS NOT IN ('30', '40', '90')
             AND TMON.PA_SALE_GB = '20'
             AND TMON.MODIFY_DATE >= d_sysdate - 4
          
          UNION ALL
          
          SELECT SSG.GOODS_CODE, SSG.PA_CODE
            FROM TPASSGGOODS SSG
           WHERE SSG.ITEM_ID IS NOT NULL
             AND SSG.PA_STATUS NOT IN ('30', '40', '90')
             AND SSG.PA_SALE_GB = '20'
             AND SSG.MODIFY_DATE >= d_sysdate - 4
          
          UNION ALL
          
          SELECT KG.GOODS_CODE, KG.PA_CODE
            FROM TPAKAKAOGOODS KG
           WHERE KG.PRODUCT_ID IS NOT NULL
             AND KG.PA_STATUS NOT IN ('30', '40', '90')
             AND KG.PA_SALE_GB = '20'
             AND KG.MODIFY_DATE >= d_sysdate - 4
          
          UNION ALL
          
          SELECT HG.GOODS_CODE, HG.PA_CODE
            FROM TPAHALFGOODS HG
           WHERE HG.PRODUCT_NO IS NOT NULL
             AND HG.PA_STATUS NOT IN ('30', '40', '90')
             AND HG.PA_SALE_GB = '20'
             AND HG.MODIFY_DATE >= d_sysdate - 4
          
          );
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;
  insert_proc_e_log();
  -- END --

  v_flag      := 'P328';
  v_error_msg := '[확인_P328][제휴_G마켓] 상품정보 기초코드 데이터 유효성 검사';
  is_existed  := 0;

  /* P328. [제휴_G마켓] 상품정보 기초코드 데이터 유효성 검사 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (
          /*SELECT PAGK.GOODS_CODE
           FROM TPAGMKTGOODS PAGK
          WHERE NOT EXISTS ( SELECT '1'
                               FROM TPAGOODSKINDS PGK
                              WHERE PGK.PA_GROUP_CODE ='02'
                                AND PAGK.PA_SGROUP = PGK.PA_SGROUP
                            )
            AND PAGK.MODIFY_DATE >= d_sysdate-5  제외
          
          UNION ALL*/
          
          SELECT PAGK.GOODS_CODE
            FROM TPAGMKTGOODS PAGK
           WHERE NOT EXISTS (SELECT '1'
                    FROM TCODE TC
                   WHERE TC.CODE_LGROUP = 'O502'
                     AND TC.CODE_MGROUP = PAGK.PA_STATUS)
             AND PAGK.MODIFY_DATE >= d_sysdate - 5
          
          UNION ALL
          
          SELECT PAGK.GOODS_CODE
            FROM TPAGMKTGOODS PAGK
           WHERE PAGK.ORIGIN_ENUM NOT IN
                 (SELECT TC.CODE_MGROUP
                    FROM TCODE TC
                   WHERE TC.CODE_LGROUP = 'O564'
                     AND TC.USE_YN = 1)
             AND PAGK.MODIFY_DATE >= d_sysdate - 5
          
          UNION ALL
          
          SELECT PAGK.GOODS_CODE
            FROM TPAGMKTGOODS PAGK
           WHERE PAGK.ORIGIN_ENUM IS NULL
             AND PAGK.MODIFY_DATE >= d_sysdate - 5
          
          UNION ALL
          
          SELECT PAGK.GOODS_CODE
            FROM TPAGMKTGOODS PAGK
           WHERE NOT EXISTS (SELECT '1'
                    FROM TPAGMKTBRAND PB
                   WHERE PB.BRAND_NO = PAGK.BRAND_NO)
             AND PAGK.BRAND_NO IS NOT NULL
             AND PAGK.BRAND_NO != '37445'
             AND PAGK.MODIFY_DATE >= d_sysdate - 5
          
          );
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;
  -- END --

  v_flag      := 'P329';
  v_error_msg := '[즉시_P329][제휴_공통] 가격/옵션 정보 전송 안된건';
  is_existed  := 0;

  /* P329. [제휴_공통] 가격/옵션 정보 전송 검사 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT GPA.GOODS_CODE, GPA.PA_CODE, '1'
            FROM TPAGOODSPRICEAPPLY GPA
           WHERE GPA.TRANS_DATE IS NULL
             AND GPA.INSERT_DATE >= d_sysdate - 3
             AND GPA.INSERT_DATE < SYSDATE - INTERVAL '60'
           MINUTE
             AND (GPA.APPLY_DATE, GPA.PRICE_APPLY_SEQ) IN
                 (SELECT /*+ INDEX_DESC (PP PK_TPAGOODSPRICEAPPLY)*/
                   PP.APPLY_DATE, PP.PRICE_APPLY_SEQ
                    FROM TPAGOODSPRICEAPPLY PP
                   WHERE PP.GOODS_CODE = GPA.GOODS_CODE
                     AND PP.PA_GROUP_CODE = GPA.PA_GROUP_CODE
                     AND PP.PA_CODE = GPA.PA_CODE
                     AND PP.APPLY_DATE < SYSDATE
                     AND ROWNUM = 1)
             AND 1 = CASE
                   WHEN GPA.PA_CODE IN ('B1', 'B2') THEN
                    (SELECT 1
                       FROM TPAKAKAOGOODS PG
                      WHERE PG.GOODS_CODE = GPA.GOODS_CODE
                        AND PG.PA_CODE = GPA.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20'
                        AND PG.PA_GROUP_CODE = GPA.PA_GROUP_CODE)
                   WHEN GPA.PA_CODE IN ('C1', 'C2') THEN
                    (SELECT 1
                       FROM TPAHALFGOODS PG
                      WHERE PG.GOODS_CODE = GPA.GOODS_CODE
                        AND PG.PA_CODE = GPA.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20'
                        AND PG.PA_GROUP_CODE = GPA.PA_GROUP_CODE)
                   WHEN GPA.PA_CODE IN ('21', '22') THEN
                    (SELECT 1
                       FROM TPAGMKTGOODS PG
                      WHERE PG.GOODS_CODE = GPA.GOODS_CODE
                        AND PG.PA_CODE = GPA.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20'
                        AND PG.PA_GROUP_CODE = GPA.PA_GROUP_CODE)
                   WHEN GPA.PA_CODE IN ('11', '12') THEN
                    (SELECT 1
                       FROM TPA11STGOODS PG
                      WHERE PG.GOODS_CODE = GPA.GOODS_CODE
                        AND PG.PA_CODE = GPA.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20')
                   WHEN GPA.PA_CODE IN ('41') THEN
                    (SELECT 1
                       FROM TPANAVERGOODS PG
                      WHERE PG.GOODS_CODE = GPA.GOODS_CODE
                        AND PG.PA_CODE = GPA.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20')
                   WHEN GPA.PA_CODE IN ('51', '52') THEN
                    (SELECT 1
                       FROM TPACOPNGOODS PG
                      WHERE PG.GOODS_CODE = GPA.GOODS_CODE
                        AND PG.PA_CODE = GPA.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20'
                        AND PG.APPROVAL_STATUS = '30')
                   WHEN GPA.PA_CODE IN ('61', '62') THEN
                    (SELECT 1
                       FROM TPAWEMPGOODS PG
                      WHERE PG.GOODS_CODE = GPA.GOODS_CODE
                        AND PG.PA_CODE = GPA.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20')
                   WHEN GPA.PA_CODE IN ('71', '72') THEN
                    (SELECT 1
                       FROM TPAINTPGOODS PG
                      WHERE PG.GOODS_CODE = GPA.GOODS_CODE
                        AND PG.PA_CODE = GPA.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20')
                   WHEN GPA.PA_CODE IN ('81', '82') THEN
                    (SELECT 1
                       FROM TPALTONGOODS PG
                      WHERE PG.GOODS_CODE = GPA.GOODS_CODE
                        AND PG.PA_CODE = GPA.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20')
                   WHEN GPA.PA_CODE IN ('91', '92') THEN
                    (SELECT 1
                       FROM TPATMONGOODS PG
                      WHERE PG.GOODS_CODE = GPA.GOODS_CODE
                        AND PG.PA_CODE = GPA.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20')
                   WHEN GPA.PA_CODE IN ('A1', 'A2') THEN
                    (SELECT 1
                       FROM TPASSGGOODS PG
                      WHERE PG.GOODS_CODE = GPA.GOODS_CODE
                        AND PG.PA_CODE = GPA.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20'
                        AND PG.PROC_STAT_CD = '20')
                 
                   ELSE
                    0
                 END
          
          UNION ALL
          
          SELECT distinct M.GOODS_CODE, M.PA_CODE, '2'
            FROM TPAGOODSDTMAPPING M
           WHERE M.TRANS_TARGET_YN = '1'
             AND M.MODIFY_DATE >= d_sysdate - 3
             AND M.MODIFY_DATE < SYSDATE - INTERVAL '60'
           MINUTE
             AND 1 = CASE
                   WHEN M.PA_CODE IN ('11', '12') THEN
                    (SELECT 1
                       FROM TPA11STGOODS PG
                      WHERE PG.GOODS_CODE = M.GOODS_CODE
                        AND PG.PA_CODE = M.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20'
                        AND PG.MODIFY_DATE > SYSDATE - INTERVAL '60'
                      MINUTE)
                   WHEN M.PA_CODE IN ('21', '22') THEN
                    (SELECT 1
                       FROM TPAGMKTGOODS PG
                      WHERE PG.GOODS_CODE = M.GOODS_CODE
                        AND PG.PA_CODE = M.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20'
                        AND rownum = 1)
                   WHEN M.PA_CODE IN ('51', '52') THEN
                    (SELECT 1
                       FROM TPACOPNGOODS PG
                      WHERE PG.GOODS_CODE = M.GOODS_CODE
                        AND PG.PA_CODE = M.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20'
                        AND PG.MODIFY_DATE > SYSDATE - INTERVAL '60'
                      MINUTE
                        AND PG.APPROVAL_STATUS = '30')
                   WHEN M.PA_CODE IN ('61', '62') THEN
                    (SELECT 1
                       FROM TPAWEMPGOODS PG
                      WHERE PG.GOODS_CODE = M.GOODS_CODE
                        AND PG.PA_CODE = M.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20'
                        AND PG.MODIFY_DATE > SYSDATE - INTERVAL '60'
                      MINUTE)
                   WHEN M.PA_CODE IN ('71', '72') THEN
                    (SELECT 1
                       FROM TPAINTPGOODS PG
                      WHERE PG.GOODS_CODE = M.GOODS_CODE
                        AND PG.PA_CODE = M.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20'
                        AND PG.MODIFY_DATE > SYSDATE - INTERVAL '60'
                      MINUTE)
                   WHEN M.PA_CODE IN ('91', '92') THEN
                    (SELECT 1
                       FROM TPATMONGOODS PG
                      WHERE PG.GOODS_CODE = M.GOODS_CODE
                        AND PG.PA_CODE = M.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20'
                        AND PG.MODIFY_DATE > SYSDATE - INTERVAL '60'
                      MINUTE)
                   WHEN M.PA_CODE IN ('B1', 'B2') THEN
                    (SELECT 1
                       FROM TPAKAKAOGOODS PG
                      WHERE PG.GOODS_CODE = M.GOODS_CODE
                        AND PG.PA_CODE = M.PA_CODE
                        AND PG.PA_STATUS = '30'
                        AND PG.PA_SALE_GB = '20'
                        AND PG.MODIFY_DATE > SYSDATE - INTERVAL '60'
                      MINUTE)
                   ELSE
                    0
                 END
             AND M.TRANS_ORDER_ABLE_QTY != 0
          
          UNION ALL
          
          SELECT DM.GOODS_CODE, DM.PA_CODE, '2'
            FROM TPALTONGOODS PG, TPALTONGOODSDTMAPPING DM
           WHERE PG.PA_STATUS = '30'
             AND PG.PA_SALE_GB = '20'
             AND PG.MODIFY_DATE < SYSDATE - INTERVAL '60'
           MINUTE
             AND PG.GOODS_CODE = DM.GOODS_CODE
             AND PG.PA_CODE = DM.PA_CODE
             AND DM.MODIFY_DATE >= d_sysdate - 3
             AND DM.USE_YN = '1'
             AND DM.PA_OPTION_CODE IS NULL
             AND DM.TRANS_ORDER_ABLE_QTY != 0
          
          UNION ALL
          
          SELECT DM.GOODS_CODE, DM.PA_CODE, '2'
            FROM TPASSGGOODS PG, TPASSGGOODSDTMAPPING DM
           WHERE PG.PA_STATUS = '30'
             AND PG.PA_SALE_GB = '20'
             AND PG.MODIFY_DATE < SYSDATE - INTERVAL '60'
           MINUTE
             AND PG.GOODS_CODE = DM.GOODS_CODE
             AND PG.PA_CODE = DM.PA_CODE
             AND DM.MODIFY_DATE >= d_sysdate - 3
             AND DM.USE_YN = '1'
             AND DM.PA_OPTION_CODE IS NULL
             AND DM.TRANS_ORDER_ABLE_QTY != 0
          
          UNION ALL
          
          SELECT DM.GOODS_CODE, DM.PA_CODE, '2'
            FROM TPAHALFGOODS PG, TPAHALFGOODSDTMAPPING DM
           WHERE PG.PA_STATUS = '30'
             AND PG.PA_SALE_GB = '20'
             AND PG.MODIFY_DATE < SYSDATE - INTERVAL '60'
           MINUTE
             AND PG.GOODS_CODE = DM.GOODS_CODE
             AND PG.PA_CODE = DM.PA_CODE
             AND DM.MODIFY_DATE >= d_sysdate - 3
             AND DM.USE_YN = '1'
             AND DM.PA_OPTION_CODE IS NULL
             AND DM.TRANS_ORDER_ABLE_QTY != 0
          
          );
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;

  v_flag      := 'P336';
  v_error_msg := '[즉시_P336] 11번가 제휴사단품KEY(옵션코드) 중복 검증';
  is_existed  := 0;

  /*[즉시_P336] 11번가 제휴사단품KEY 중복 검증 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT /*+INDEX(PGM PK_TPAGOODSDTMAPPING)*/
           PA_OPTION_CODE, COUNT(1)
            FROM TPAGOODSDTMAPPING PGM, TPA11STGOODS PG, TGOODSDT TD
           WHERE PGM.PA_CODE IN ('11', '12')
             AND PGM.PA_OPTION_CODE IS NOT NULL
             AND PGM.PA_CODE = PG.PA_CODE
             AND PGM.GOODS_CODE = PG.GOODS_CODE
             AND PGM.GOODS_CODE = TD.GOODS_CODE
             AND PGM.GOODSDT_CODE = TD.GOODSDT_CODE
             AND TD.SALE_GB = '00'
             AND PG.PA_STATUS = '30'
             AND PG.MODIFY_DATE >= d_sysdate - 3
             AND PA_OPTION_CODE NOT IN ('8555779994')
           GROUP BY PA_OPTION_CODE
          HAVING COUNT(1) > 1);
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;

  -- END --

  /*    v_flag      := 'P338';  v_error_msg := '[즉시_P338] 네이버 제휴사단품KEY 중복 검증';
  is_existed  := 0;
  
  \*[즉시_P338] 네이버 제휴사단품KEY 중복 검증 *\
  SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END DATA_CHECK
        into is_existed
    FROM (SELECT PA_OPTION_CODE, COUNT(1)
            FROM TPAGOODSDTMAPPING PGM
               , TPANAVERGOODS PG
           WHERE PGM.PA_CODE ='41'
             AND PGM.PA_OPTION_CODE IS NOT NULL
             AND PGM.PA_CODE    = PG.PA_CODE
             AND PGM.GOODS_CODE = PG.GOODS_CODE
             AND PG.PA_STATUS   = '30'
           GROUP BY PA_OPTION_CODE
          HAVING COUNT(1) > 1
         )
  ;
  
  IF( is_existed > 0 ) THEN insert_error_log(); END IF;*/
  -- END --

  /*    v_flag      := 'P398';    v_error_msg := '[확인_P398][제휴_EBAY] EBAY 500에러 발생 COUNT > 3 건';
  is_existed  := 0; */

  /* P398. [제휴_EBAY] EBAY 500에러 발생 COUNT > 3 */
  /*   SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END DATA_CHECK
   into is_existed
   FROM (
           SELECT PG.GOODS_CODE,
                  PG.PA_GROUP_CODE
             FROM TPAGMKTGOODS PG,
                 (SELECT TL.GOODS_CODE, COUNT(1)
                     FROM TPAGOODSTRANSLOG TL, TPAGOODSTARGET TPG
                    WHERE TL.PA_CODE IN ( '21', '22' )
                      AND TL.PA_CODE = TPG.PA_CODE
                      AND TL.GOODS_CODE = TPG.GOODS_CODE
                      AND TPG.AUTO_YN = '1'
                      AND TL.SUCCESS_YN = '0'
                      AND (TL.RTN_MSG LIKE '%IF_PAGMKTAPI_V2_01_003500%' OR TL.RTN_MSG LIKE '%IF_PAGMKTAPI_V2_01_003502%')
                      AND (TL.TRANS_SEQ LIKE  TO_CHAR(TRUNC(SYSDATE)-1, 'YYMMDD')||'%' OR TL.TRANS_SEQ LIKE  TO_CHAR(TRUNC(SYSDATE), 'YYMMDD')||'%')
                      GROUP BY TL.GOODS_CODE
                      HAVING COUNT(1) > 3) TARGET
            WHERE PG.RETURN_NOTE IS NOT NULL
              AND PG.GOODS_CODE = TARGET.GOODS_CODE
  
   /*      SELECT PGG.GOODS_CODE
              , PGG.PA_GROUP_CODE
           FROM TPAGMKTGOODS PGG
              , (
  
                      SELECT
                         PTL.GOODS_CODE
                       , COUNT(1) AS CNT
                      FROM TPAGOODSTARGET T
                         , TPAGOODSTRANSLOG PTL
                     WHERE T.GOODS_CODE = PTL.GOODS_CODE
                       AND T.PA_CODE = PTL.PA_CODE
                       AND T.AUTO_YN = '1'
                       AND T.PA_GROUP_CODE IN ('02', '03')
                       AND PTL.SUCCESS_YN = '0'
                       AND PTL.PROC_DATE >= SYSDATE - 1
                       AND (PTL.RTN_MSG LIKE '%IF_PAGMKTAPI_V2_01_003500%' OR PTL.RTN_MSG LIKE '%IF_PAGMKTAPI_V2_01_003502%')
                     GROUP BY PTL.GOODS_CODE
                   HAVING COUNT(1) > 3
  
  
  
                 ) TARGET
           WHERE PGG.GOODS_CODE = TARGET.GOODS_CODE
             AND (PGG.RETURN_NOTE LIKE '%IF_PAGMKTAPI_V2_01_003500%' OR PGG.RETURN_NOTE LIKE '%IF_PAGMKTAPI_V2_01_003502%')*\
        )
  ;*/

  /*    IF( is_existed > 0 ) THEN insert_error_log(); END IF; */
  -- END --

  v_flag      := 'P335';
  v_error_msg := '[확인_P335]상품고시정보 필수 데이터 없는 경우';
  is_existed  := 0;

  /* P335. 상품고시정보 필수 데이터 없는 경우  여기다가 STATUS 빼면 ..*/
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT TPG.GOODS_CODE, TPG.PA_CODE, TPG.PA_GROUP_CODE
            FROM TGOODS GD, TPAGOODSTARGET TPG
           WHERE GD.SALE_GB = '00'
             AND SYSDATE BETWEEN GD.SALE_START_DATE AND GD.SALE_END_DATE
             AND GD.GOODS_CODE = TPG.GOODS_CODE
             AND NOT EXISTS (SELECT 1
                    FROM TPAGOODSOFFER PO
                   WHERE PO.GOODS_CODE = TPG.GOODS_CODE
                     AND PO.PA_GROUP_CODE =
                         DECODE(TPG.PA_GROUP_CODE,
                                '03',
                                '02',
                                TPG.PA_GROUP_CODE)
                     AND PO.USE_YN = '1')
             AND 1 = CASE
                   WHEN TPG.PA_GROUP_CODE = '01' THEN
                    NVL((SELECT 1
                          FROM TPA11STGOODS PG
                         WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE
                           AND PG.PA_CODE = TPG.PA_CODE
                           AND PG.GOODS_CODE = TPG.GOODS_CODE
                           AND PG.PA_SALE_GB = '20'
                           AND PG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN TPG.PA_GROUP_CODE IN ('02', '03') THEN
                    NVL((SELECT 1
                          FROM TPAGMKTGOODS PG
                         WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE
                           AND PG.PA_CODE = TPG.PA_CODE
                           AND PG.GOODS_CODE = TPG.GOODS_CODE
                           AND PG.PA_SALE_GB = '20'
                           AND PG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN TPG.PA_GROUP_CODE = '04' THEN
                    NVL((SELECT 1
                          FROM TPANAVERGOODS PG
                         WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE
                           AND PG.PA_CODE = TPG.PA_CODE
                           AND PG.GOODS_CODE = TPG.GOODS_CODE
                           AND PG.PA_SALE_GB = '20'
                           AND PG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN TPG.PA_GROUP_CODE = '05' THEN
                    NVL((SELECT 1
                          FROM TPACOPNGOODS PG
                         WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE
                           AND PG.PA_CODE = TPG.PA_CODE
                           AND PG.GOODS_CODE = TPG.GOODS_CODE
                           AND PG.PA_SALE_GB = '20'
                           AND PG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN TPG.PA_GROUP_CODE = '06' THEN
                    NVL((SELECT 1
                          FROM TPAWEMPGOODS PG
                         WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE
                           AND PG.PA_CODE = TPG.PA_CODE
                           AND PG.GOODS_CODE = TPG.GOODS_CODE
                           AND PG.PA_SALE_GB = '20'
                           AND PG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN TPG.PA_GROUP_CODE = '07' THEN
                    NVL((SELECT 1
                          FROM TPAINTPGOODS PG
                         WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE
                           AND PG.PA_CODE = TPG.PA_CODE
                           AND PG.GOODS_CODE = TPG.GOODS_CODE
                           AND PG.PA_SALE_GB = '20'
                           AND PG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN TPG.PA_GROUP_CODE = '08' THEN
                    NVL((SELECT 1
                          FROM TPALTONGOODS PG
                         WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE
                           AND PG.PA_CODE = TPG.PA_CODE
                           AND PG.GOODS_CODE = TPG.GOODS_CODE
                           AND PG.PA_SALE_GB = '20'
                           AND PG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN TPG.PA_GROUP_CODE = '09' THEN
                    NVL((SELECT 1
                          FROM TPATMONGOODS PG
                         WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE
                           AND PG.PA_CODE = TPG.PA_CODE
                           AND PG.GOODS_CODE = TPG.GOODS_CODE
                           AND PG.PA_SALE_GB = '20'
                           AND PG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN TPG.PA_GROUP_CODE = '10' THEN
                    NVL((SELECT 1
                          FROM TPASSGGOODS PG
                         WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE
                           AND PG.PA_CODE = TPG.PA_CODE
                           AND PG.GOODS_CODE = TPG.GOODS_CODE
                           AND PG.PA_SALE_GB = '20'
                           AND PG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN TPG.PA_GROUP_CODE = '11' THEN
                    NVL((SELECT 1
                          FROM TPAKAKAOGOODS PG
                         WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE
                           AND PG.PA_CODE = TPG.PA_CODE
                           AND PG.GOODS_CODE = TPG.GOODS_CODE
                           AND PG.PA_SALE_GB = '20'
                           AND PG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END
             AND 1 = CASE
                   WHEN TPG.PA_GROUP_CODE = '12' THEN
                    NVL((SELECT 1
                          FROM TPAHALFGOODS PG
                         WHERE PG.PA_GROUP_CODE = TPG.PA_GROUP_CODE
                           AND PG.PA_CODE = TPG.PA_CODE
                           AND PG.GOODS_CODE = TPG.GOODS_CODE
                           AND PG.PA_SALE_GB = '20'
                           AND PG.PA_STATUS = '30'),
                        0)
                   ELSE
                    1
                 END);
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;

  v_flag      := 'P339';
  v_error_msg := '[확인_P339][제휴_네이버] 전일자 기준으로 정산데이터가 적재 되었는지 확인';
  is_existed  := 0;

  /* P339. [제휴_네이버] 전일자 기준으로 정산데이터가 적재 되었는지 확인 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            0
           ELSE
            1
         END DATA_CHECK
    into is_existed
    FROM (SELECT '1'
            FROM TPANAVERSETTLEMENT PNS
           WHERE PNS.SETTLE_BASIC_DATE = TRUNC(d_sysdate) - 2
             AND ROWNUM = 1
          
          );
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;
  /*
      v_flag      := 'P340';    v_error_msg := '[확인_P340][제휴_네이버] 업체 출고/회수지가 매핑 안된건';
      is_existed  := 0;

      P340. [제휴_네이버] 업체 출고/회수지가 매핑 안된건
      SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END DATA_CHECK
        into is_existed
        FROM ( SELECT '1'
                 FROM TPANAVERENTPADDRESS PN
                WHERE NOT EXISTS ( SELECT '1'
                                     FROM TPAENTPSLIP PE
                                    WHERE PN.NAME = PE.ENTP_CODE||PE.ENTP_MAN_SEQ
                                  )
                  AND REGEXP_LIKE(PN.NAME , '[[:digit:]]')
                  AND PN.ADDRESS_ID NOT IN ('101556371','101474048','101542936','101852171' , '102134875')
                  AND ROWNUM = 1
             )
      ;
  
      IF( is_existed > 0 ) THEN insert_error_log(); END IF;
  */

  v_flag      := 'P341';
  v_error_msg := '[확인_P341][제휴_네이버] 네이버 주소지 정보에서 주소명이 중복된 건';
  is_existed  := 0;

  /* P341. [제휴_네이버] 네이버 주소지 정보에서 주소명이 중복된 건 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT PN.NAME
            FROM TPANAVERENTPADDRESS PN
           GROUP BY PN.NAME
          HAVING COUNT(1) > 1)
   WHERE NAME NOT IN ('107820004',
                      '101021026',
                      '101021023',
                      '107820005',
                      '208818002',
                      '108365005',
                      '109214014',
                      '109214015',
                      '109214003',
                      '109214004');
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;

  v_flag      := 'P343';
  v_error_msg := '[확인_P343][제휴_쿠팡] 구매옵션 미 생성건';
  is_existed  := 0;

  /* P343. [제휴_쿠팡] 구매옵션 미 생성건 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT A.GOODS_CODE
            FROM TPACOPNGOODS A
           WHERE NOT EXISTS (SELECT 1
                    FROM TPACOPNGOODSATTRI B
                   WHERE A.GOODS_CODE = B.GOODS_CODE));
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;

  v_flag      := 'P344';
  v_error_msg := '[즉시_P344][제휴_카카오] 상품가격정보 동기화 점검';
  is_existed  := 0;

  /* P344. [제휴_카카오] 상품가격정보 동기화 점검 */
  -- 11번가, 이베이, 쿠팡 추가 함.  1시간 이내에 금액 변경 안될경우 감지
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT AA.GOODS_CODE
            FROM (SELECT DISTINCT BP.PA_CODE, BP.GOODS_CODE, GP.APPLY_DATE
                    FROM (
                          
                          SELECT CG.PA_CODE, CG.GOODS_CODE
                            FROM TPA11STGOODS CG
                           WHERE CG.MODIFY_DATE > sysdate - 7
                             AND CG.MODIFY_DATE < sysdate - 30 / 24 / 60
                             AND CG.PA_SALE_GB = '20'
                             AND CG.PA_STATUS = '30'
                          
                          UNION
                          
                          SELECT CG.PA_CODE, CG.GOODS_CODE
                            FROM TPAGMKTGOODS CG
                           WHERE CG.MODIFY_DATE > sysdate - 7
                             AND CG.MODIFY_DATE < sysdate - 30 / 24 / 60
                             AND CG.PA_SALE_GB = '20'
                             AND CG.PA_STATUS = '30'
                          
                          UNION
                          
                          SELECT CG.PA_CODE, CG.GOODS_CODE
                            FROM TPACOPNGOODS CG
                           WHERE CG.MODIFY_DATE > sysdate - 7
                             AND CG.MODIFY_DATE < sysdate - 30 / 24 / 60
                             AND CG.PA_SALE_GB = '20'
                             AND CG.PA_STATUS = '30'
                          
                          UNION
                          
                          SELECT KG.PA_CODE, KG.GOODS_CODE
                            FROM TPAKAKAOGOODS KG
                           WHERE KG.MODIFY_DATE > d_sysdate - 7
                             AND KG.MODIFY_DATE < d_sysdate - 30 / 24 / 60
                             AND KG.PA_SALE_GB = '20'
                             AND KG.PA_STATUS = '30'
                          
                          UNION
                          
                          SELECT HG.PA_CODE, HG.GOODS_CODE
                            FROM TPAHALFGOODS HG
                           WHERE HG.MODIFY_DATE > d_sysdate - 7
                             AND HG.MODIFY_DATE < d_sysdate - 30 / 24 / 60
                             AND HG.PA_SALE_GB = '20'
                             AND HG.PA_STATUS = '30'
                          
                          ) BP,
                         TGOODSPRICE GP
                   WHERE GP.GOODS_CODE = BP.GOODS_CODE
                     AND GP.APPLY_DATE = (SELECT /*+ INDEX_DESC (PR PK_TGOODSPRICE)*/
                                           PR.APPLY_DATE
                                            FROM TGOODSPRICE PR
                                           WHERE PR.GOODS_CODE = BP.GOODS_CODE
                                             AND PR.APPLY_DATE < SYSDATE
                                             AND ROWNUM = 1)
                     AND NOT EXISTS
                   (SELECT 'X'
                            FROM TPAGOODSPRICEAPPLY PPA
                           WHERE PPA.GOODS_CODE = BP.GOODS_CODE
                             AND PPA.APPLY_DATE = GP.APPLY_DATE
                             AND PPA.PA_CODE = BP.PA_CODE
                             AND ROWNUM = 1)
                     AND GP.GOODS_CODE NOT IN ('26922336', '27948024')) AA
           WHERE AA.APPLY_DATE < SYSDATE - 1 / 24
          
          );
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;

  v_flag      := 'P345';
  v_error_msg := '[확인_P345][제휴_하프클럽] 브랜드 교환환불불가 속성 불일치건 검출';
  is_existed  := 0;

  /*P345. [제휴_하프클럽] 브랜드 교환환불불가 속성 불일치건 검출 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT GD.GOODS_CODE,
                 GD.GOODS_NAME,
                 GD.ENTP_CODE,
                 GD.SALE_GB,
                 GD.BRAND_CODE,
                 GD.RETURN_NO_YN,
                 HG.PRODUCT_NO,
                 HG.PA_BRAND_NO,
                 HB.RETURN_NO_YN AS PA_RETURN_NO_YN
            FROM TGOODS              GD,
                 TPAHALFGOODS        HG,
                 TPAHALFBRANDMAPPING HB,
                 TPAHALFBRAND        BR
           WHERE GD.GOODS_CODE = HG.GOODS_CODE
             AND HG.PA_BRAND_NO = HB.PA_BRAND_NO
             AND GD.BRAND_CODE = HB.BRAND_CODE
             AND GD.RETURN_NO_YN != HB.RETURN_NO_YN
             AND BR.PA_BRAND_NO = HB.PA_BRAND_NO
             AND BR.USE_YN = '1'
             AND HG.PA_SALE_GB = '20'
             AND HG.PA_STATUS = '30'
             AND HB.RETURN_NO_YN = '0'
             AND GD.RETURN_NO_YN = '1'
             AND GD.GOODS_NAME NOT LIKE '%제휴%테스트%');
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;

  v_flag      := 'P346';
  v_error_msg := '[확인_P346][제휴_하프클럽] 배송템플릿 점검';
  is_existed  := 0;

  /*P346. [제휴_하프클럽] 배송템플릿 점검, 검출시 TPAHALFGOODS.TRANS_TARGET_YN='1'로 UPDATE*/
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT HG.PA_CODE,
                 HG.GOODS_CODE,
                 HG.PRODUCT_NO,
                 GD.ENTP_CODE,
                 GD.SHIP_MAN_SEQ,
                 GD.RETURN_MAN_SEQ,
                 GD.SHIP_COST_CODE,
                 HS.PA_SHIP_COST_CODE AS HS_PA_SHIP_COST_CODE,
                 HG.PA_SHIP_COST_CODE AS HG_PA_SHIP_COST_CODE
            FROM (SELECT NVL((SELECT '1'
                               FROM TDELYNOAREA D
                              WHERE D.GOODS_CODE = G.GOODS_CODE
                                AND ROWNUM = 1),
                             '0') AS DELY_NO_YN,
                         G.*
                    FROM TGOODS G) GD,
                 TENTPUSER OE,
                 TENTPUSER IE,
                 TPAHALFGOODS HG,
                 TPAHALFSHIPINFO HS
           WHERE GD.GOODS_CODE = HG.GOODS_CODE
             AND OE.ENTP_CODE = GD.ENTP_CODE
             AND GD.ENTP_CODE = HS.ENTP_CODE
             AND GD.ENTP_CODE = IE.ENTP_CODE
             AND GD.DELY_TYPE != '10'
             AND GD.COLLECT_YN != 1
             AND GD.SHIP_MAN_SEQ = OE.ENTP_MAN_SEQ
             AND HS.SHIP_MAN_SEQ = OE.ENTP_MAN_SEQ
             AND OE.ENTP_MAN_GB = '30'
             AND GD.RETURN_MAN_SEQ = IE.ENTP_MAN_SEQ
             AND HS.RETURN_MAN_SEQ = IE.ENTP_MAN_SEQ
             AND IE.ENTP_MAN_GB = '20'
             AND HS.SHIP_COST_CODE = GD.SHIP_COST_CODE
             AND HG.PA_CODE IN ('C1', 'C2')
             AND HG.PA_CODE = HS.PA_CODE
             AND HG.PA_STATUS = '30'
             AND HG.PA_SALE_GB = '20'
             AND HG.TRANS_TARGET_YN = '0'
             AND HS.TRANS_TARGET_YN = '0'
             AND HG.MODIFY_DATE > SYSDATE - 3
             AND HS.NO_SHIP_JEJU_ISLAND = GD.DELY_NO_YN
             AND HS.PA_SHIP_COST_CODE != HG.PA_SHIP_COST_CODE);
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;

 /* v_flag      := 'P347';
  v_error_msg := '[확인_P347][제휴_하프클럽] 당사배송상품 검출';
  is_existed  := 0;*/

  /*P347. [제휴_하프클럽] 당사배송상품 검출 */
  /*SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (select GD.GOODS_CODE,
                 (SELECT COUNT(1)
                    FROM TPAHALFGOODS HG
                   WHERE HG.GOODS_CODE = GD.GOODS_CODE) AS CNT
            from TGOODS GD
           WHERE GD.DELY_TYPE = '10')
   WHERE CNT > 0;
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;*/

  v_flag      := 'P348';
  v_error_msg := '[확인_P348][제휴_하프클럽] 배송템플릿 출고지구분값30 또는 회수지구분값20이 아닌 건';
  is_existed  := 0;

  /* P348. [제휴_하프클럽] 배송템플릿 출고지구분값30 또는 회수지구분값20이 아닌 건 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT GD.GOODS_CODE,
                 GD.ENTP_CODE,
                 GD.SHIP_MAN_SEQ,
                 GD.RETURN_MAN_SEQ,
                 OE.ENTP_MAN_SEQ   AS OUT_GB,
                 IE.ENTP_MAN_SEQ   AS IN_GB
            FROM TPAHALFGOODS HG, TGOODS GD, TENTPUSER IE, TENTPUSER OE
           WHERE HG.GOODS_CODE = GD.GOODS_CODE
             AND GD.ENTP_CODE = IE.ENTP_CODE
             AND GD.ENTP_CODE = OE.ENTP_CODE
             AND GD.SHIP_MAN_SEQ = OE.ENTP_MAN_SEQ
             AND GD.RETURN_MAN_SEQ = IE.ENTP_MAN_SEQ
             AND (OE.ENTP_MAN_GB != '30' OR IE.ENTP_MAN_GB != '20')
             AND HG.PA_STATUS != '20'
             AND HG.PA_SALE_GB = '20'
             AND GD.DELY_TYPE !=10);
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;

  v_flag      := 'P349';
  v_error_msg := '[즉시_P349][제휴_하프클럽] 하프클럽 배치 3시간 이상 멈춤 점검';
  is_existed  := 0;

  /* P349. [제휴_하프클럽] 하프클럽 배치 3시간 이상 멈춤 점검 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT A.PRG_ID
            FROM TCLOSEHISTORY A
           WHERE A.PRG_ID LIKE '%IF_PAHALF%'
             AND A.PROC_YN = '1'
             AND A.MODIFY_DATE < SYSDATE - 1 / 8);
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;

  v_flag      := 'P350';
  v_error_msg := '[확인_P350][제휴_하프클럽] 기준정보등록 실패건';
  is_existed  := 0;

  /* P350. [제휴_하프클럽] 기준정보등록 실패건 상품등록리셋 수기처리 필요*/
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT HG.GOODS_CODE, HG.PA_CODE
            FROM TPAHALFGOODS HG
           WHERE HG.RETURN_NOTE LIKE '%IF_PAHALFAPI_01_002%'
             AND HG.RETURN_NOTE NOT LIKE
                 '%표준아이템을 3depth로 정확하게 설정해주시기 바랍니다.%'
             AND HG.RETURN_NOTE NOT LIKE '%상세브랜드는 필수항목입니다.%'
             AND HG.PA_SALE_GB = '20'
             AND HG.PA_STATUS = '10');
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;

  v_flag      := 'P351';
  v_error_msg := '[즉시_P351][제휴_하프클럽] 업체의 출고지/회수지 주소와 배송템플릿의 출고지/회수지 주소가 다른건';
  is_existed  := 0;

  /* P351. [제휴_하프클럽] 업체의 출고지/회수지 주소와 배송템플릿의 출고지/회수지 주소가 다른건 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT DECODE(IE.ROAD_ADDR_YN, '1', IE.ROAD_ADDR, IE.ADDR)
            FROM TPAHALFSHIPINFOHISTORY A,
                 TPAHALFSHIPINFO        B,
                 TENTPUSER              OE,
                 TENTPUSER              IE
           WHERE A.PA_SHIP_COST_CODE = B.PA_SHIP_COST_CODE
             AND A.PA_CODE = B.PA_CODE
             AND A.ENTP_CODE = B.ENTP_CODE
             AND A.SHIP_MAN_SEQ = B.SHIP_MAN_SEQ
             AND A.RETURN_MAN_SEQ = B.RETURN_MAN_SEQ
             AND A.SHIP_COST_CODE = B.SHIP_COST_CODE
             AND A.NO_SHIP_JEJU_ISLAND = B.NO_SHIP_JEJU_ISLAND
             AND (A.PA_SHIP_COST_CODE, A.SEQ) =
                 (SELECT B.PA_SHIP_COST_CODE, MAX(B.SEQ)
                    FROM TPAHALFSHIPINFOHISTORY B
                   WHERE B.PA_CODE = A.PA_CODE
                     AND B.PA_SHIP_COST_CODE = A.PA_SHIP_COST_CODE
                   GROUP BY B.PA_SHIP_COST_CODE)
                --출고
             AND A.ENTP_CODE = OE.ENTP_CODE
             AND A.SHIP_MAN_SEQ = OE.ENTP_MAN_SEQ
             AND OE.ENTP_MAN_GB = '30'
                --회수
             AND A.ENTP_CODE = IE.ENTP_CODE
             AND A.RETURN_MAN_SEQ = IE.ENTP_MAN_SEQ
             AND IE.ENTP_MAN_GB = '20'
                
             AND (OE.POST_NO <> A.POST_NO OR IE.POST_NO <> A.RET_POST_NO OR
                 NVL(OE.ROAD_POST_ADDR, OE.POST_ADDR) <> A.ADDR OR
                 NVL(IE.ROAD_POST_ADDR, IE.POST_ADDR) <> A.RET_ADDR OR
                 DECODE(OE.ROAD_ADDR_YN, '1', OE.ROAD_ADDR, OE.ADDR) <>
                 A.ADDR_DT OR
                 DECODE(IE.ROAD_ADDR_YN, '1', IE.ROAD_ADDR, IE.ADDR) <>
                 A.RET_ADDR_DT)
          
          );
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;

  v_flag      := 'P352';
  v_error_msg := '[참고_P352][제휴_하프클럽] 하프클럽 브랜드 매핑 변경된 상품';
  is_existed  := 0;

  /* P352. [제휴_하프클럽] 하프클럽 브랜드 매핑 변경된 상품 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT HG.GOODS_CODE, HG.GOODS_NAME, HG.PRODUCT_NO, HG.PA_BRAND_NO
            FROM TPAHALFGOODS        HG,
                 TGOODS              GD,
                 TPAHALFBRANDMAPPING HB,
                 TPAHALFBRAND        BR
           WHERE HG.GOODS_CODE = GD.GOODS_CODE
             AND GD.RETURN_NO_YN = HB.RETURN_NO_YN
             AND HG.PA_CODE = HB.PA_CODE
             AND GD.BRAND_CODE = HB.BRAND_CODE
             AND BR.PA_BRAND_NO = HB.PA_BRAND_NO
             AND BR.USE_YN = '1'
             AND HG.PA_SALE_GB = '20'
             AND HG.PA_STATUS = '30'
             AND GD.GOODS_NAME NOT LIKE '%제휴%테스트%'
             AND HG.PA_BRAND_NO != HB.PA_BRAND_NO
             AND GD.GOODS_CODE NOT IN
                 (SELECT GD.GOODS_CODE
                    FROM TGOODS GD, TPAHALFGOODS HG, TPAHALFBRANDMAPPING HB
                   WHERE GD.GOODS_CODE = HG.GOODS_CODE
                     AND HG.PA_BRAND_NO = HB.PA_BRAND_NO
                     AND GD.BRAND_CODE = HB.BRAND_CODE
                     AND GD.RETURN_NO_YN != HB.RETURN_NO_YN
                     AND HG.PA_SALE_GB = '20'
                     AND HG.PA_STATUS = '30'));
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;

  v_flag      := 'P353';
  v_error_msg := '[즉시_P353][제휴_하프클럽] 브랜드가 중복으로 들어간 건';
  is_existed  := 0;
   /* P353. [제휴_하프클럽] 브랜드가 중복으로 들어간 건 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT PB.PA_BRAND_NAME, PB.PA_CODE
            FROM TPAHALFBRAND PB
           WHERE PB.USE_YN = '1' HAVING COUNT(1) > 1
           GROUP BY PB.PA_BRAND_NAME, PB.PA_CODE);
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;

  v_flag      := 'P354';
  v_error_msg := '[확인_P354][제휴_하프클럽] 제주/도서산간배송불가 배송템플릿 점검';
  is_existed  := 0;

  /*P354. [제휴_하프클럽] 제주/도서산간배송불가 배송템플릿 점검, 검출시 TPAHALFGOODS.TRANS_TARGET_YN='1'로 UPDATE*/
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT HG.PA_CODE,
                 HG.GOODS_CODE,
                 HG.PRODUCT_NO,
                 GD.ENTP_CODE,
                 GD.SHIP_MAN_SEQ,
                 GD.RETURN_MAN_SEQ,
                 GD.SHIP_COST_CODE,
                 HS.PA_SHIP_COST_CODE AS HS_PA_SHIP_COST_CODE,
                 HG.PA_SHIP_COST_CODE AS HG_PA_SHIP_COST_CODE
            FROM (SELECT NVL((SELECT '1'
                               FROM TDELYNOAREA D
                              WHERE D.GOODS_CODE = G.GOODS_CODE
                                AND ROWNUM = 1),
                             '0') AS DELY_NO_YN,
                         G.*
                    FROM TGOODS G) GD,
                 TENTPUSER OE,
                 TENTPUSER IE,
                 TPAHALFGOODS HG,
                 TPAHALFSHIPINFO HS
           WHERE GD.GOODS_CODE = HG.GOODS_CODE
             AND OE.ENTP_CODE = GD.ENTP_CODE
             AND GD.ENTP_CODE = HS.ENTP_CODE
             AND GD.ENTP_CODE = IE.ENTP_CODE
             AND GD.DELY_TYPE != '10'
             AND GD.SHIP_MAN_SEQ = OE.ENTP_MAN_SEQ
             AND HS.SHIP_MAN_SEQ = OE.ENTP_MAN_SEQ
             AND OE.ENTP_MAN_GB = '30'
             AND GD.RETURN_MAN_SEQ = IE.ENTP_MAN_SEQ
             AND HS.RETURN_MAN_SEQ = IE.ENTP_MAN_SEQ
             AND IE.ENTP_MAN_GB = '20'
             AND HS.SHIP_COST_CODE = GD.SHIP_COST_CODE
             AND HG.PA_CODE IN ('C1', 'C2')
             AND HG.PA_CODE = HS.PA_CODE
             AND HG.PA_STATUS = '30'
             AND HG.PA_SALE_GB = '20'
             AND HG.TRANS_TARGET_YN = '0'
             AND HS.TRANS_TARGET_YN = '0'
             AND HG.MODIFY_DATE > SYSDATE - 3
             AND HS.NO_SHIP_JEJU_ISLAND != GD.DELY_NO_YN
             AND HS.PA_SHIP_COST_CODE = HG.PA_SHIP_COST_CODE);
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;

  v_flag      := 'P355';
  v_error_msg := '[확인_P355][제휴_티몬] TPAGOODSTARGET PA_CODE=91 건';
  is_existed  := 0;
  /* P355. [제휴_티몬] TPAGOODSTARGET PA_CODE=91 건 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    into is_existed
    FROM (SELECT T.*
            FROM TPAGOODSTARGET T
           WHERE T.PA_GROUP_CODE = '09'
             AND PA_CODE = '91');
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' ||
                       TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
  IF (is_existed > 0) THEN
    insert_error_log();
  END IF;
  
  v_flag      := 'P356';    v_error_msg := '[확인_P356][제휴_티몬] 하이마트 판매상태 동기화 확인';
  is_existed  := 0;
  /* P356. [제휴_티몬] 하이마트상품 판매상태 동기화 되지 않아 수기처리 한다.
    SK스토아 판매중단,티몬 판매중단 검출 */
  SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END DATA_CHECK
    into is_existed
    FROM (SELECT TM.*
              FROM TPATMONGOODS TM , TGOODS TG
             WHERE TM.GOODS_CODE = TG.GOODS_CODE
                AND TM.PA_SALE_GB = '30'
                AND TM.PA_STATUS = '30'
                AND TG.SALE_GB <> '00'
                AND TG.ENTP_CODE = '101291'
                AND TM.MODIFY_DATE > SYSDATE -1
             );
    DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || v_flag || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
    IF( is_existed > 0 ) THEN insert_error_log(); END IF;

  /*
       2023-06-08 중복 저장 허용하게끔 수정 완료
      v_flag      := 'P355';    v_error_msg := '[즉시_P355] 프로모션 제외 중복 저장건';
      is_existed  := 0;
      SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END DATA_CHECK
        into is_existed
        FROM (
                SELECT A.TARGET_GB,A.TARGET_CODE, COUNT(1)
                  FROM TPAPROMOTARGETEXCEPT A
                 WHERE A.USE_YN  = '1'
                   AND A.PA_EXCEPT_EDATE > SYSDATE 
                   AND A.TARGET_GB = '00'
                HAVING COUNT(1) > 1
                 GROUP BY A.TARGET_GB,A.TARGET_CODE 
       );
      IF( is_existed > 0 ) THEN insert_error_log(); END IF;
  */

  v_flag      := 'P399';
  v_error_msg := '[알림_P399]제휴 데이터 정합성 실행 완료';
  SELECT v_error_msg into v_error_msg FROM DUAL;
  insert_error_log();

  -- execution time  ------------------------

  PKG_COMMON.EXP_PRNS(v_prog_id, '');

  SELECT 'Execution Time = ' ||
         FLOOR((SYSDATE - TO_DATE(v_sysdatetime, 'YYYYMMDDHH24MISS')) * 24 * 60) ||
         ' min  ' ||
         FLOOR(MOD(((SYSDATE - TO_DATE(v_sysdatetime, 'YYYYMMDDHH24MISS')) * 24 * 60 * 60),
                   60)) || ' second'
    INTO v_msg
    FROM dual;

  PKG_COMMON.EXP_PRNS(v_prog_id, v_msg);
  PKG_COMMON.EXP_PRNS(v_prog_id, '');

  PKG_COMMON.HISTORY_SUCCESS(v_prog_id,
                             v_user_id,
                             TO_CHAR((SYSDATE - 1), 'YYYYMMDD'),
                             '',
                             v_msg);

  COMMIT;

  -- return result
  out_msg := v_msg;

  -- close file
  UTL_FILE.FCLOSE_ALL;

  -- ERROR
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE(v_flag || SQLERRM);
    v_db_error_msg := substr(SQLERRM, 1, 200);
    out_code       := sqlcode;
    ROLLBACK;
    insert_proc_f_log();
    PKG_COMMON.EXP_PRNS(v_prog_id, v_error_msg);
    PKG_COMMON.EXP_PRNS(v_prog_id, v_db_error_msg);
    out_msg := v_db_error_msg;
    UTL_FILE.FCLOSE_ALL;
    PKG_COMMON.HISTORY_FAIL(v_prog_id,
                            v_user_id,
                            TO_CHAR((SYSDATE - 1), 'YYYYMMDD'),
                            '',
                            trim(v_error_msg) || trim(v_db_error_msg));
    --raise;

END;
/

prompt
prompt Creating procedure SP_TSDCHECK_PAORDER
prompt ======================================
prompt
CREATE OR REPLACE PROCEDURE BMTCOM.SP_TSDCHECK_PAORDER(OUT_CODE OUT INTEGER,
                                                OUT_MSG  OUT VARCHAR2) IS

  /***********************************************************************
      Job     : daily data check  -- 오전 9시 ~ 오후 6시
      Date    : 2018/02/28  --
      Creator : CommerceWare  --
  
      제휴 데이터 검증용 [ 주문배송 , I/F 검증 ]
      [알림] : 정보성데이터
      [확인] : 출근 후 확인
      [즉시] : 즉시 확인 필요
  ***********************************************************************/

  V_PROG_ID      VARCHAR2(1000);
  V_MSG          VARCHAR2(1000);
  V_ERROR_MSG    VARCHAR2(2000);
  V_DB_ERROR_MSG VARCHAR2(1000);
  V_SYSDATE      VARCHAR2(8);
  V_SYSDATETIME  VARCHAR2(14);

  V_USER_ID  VARCHAR2(10);
  V_FROMDATE VARCHAR2(8);
  V_TODATE   VARCHAR2(8);
  IS_EXISTED INTEGER := 0;
  V_FLAG     VARCHAR2(8);

  V_PHONE_NO VARCHAR2(20);
  V_NAME     VARCHAR2(30);

  D_SYSDATE       DATE;
  V_SEND_SMS_YN   VARCHAR2(1);
  V_TIME_CHECK_YN VARCHAR2(1);

  TYPE V_ARRY IS VARRAY(30) OF VARCHAR2(30);

  NAME_VARRY_P700 V_ARRY := V_ARRY('조민하',
                                   '이지영',
                                   '정기복',
                                   '박은진',
                                   '정연진');
  TEL_VARRY_P700  V_ARRY := V_ARRY('01046355217',
                                   '01029352811',
                                   '01092665720',
                                   '01020840414',
                                   '01056053913');
  NAME_VARRY_P799 V_ARRY := V_ARRY('조민하',
                                   '이지영',
                                   '조미화',
                                   '김유진',
                                   '정기복',
                                   '박은진',
                                   '정연진');
  TEL_VARRY_P799  V_ARRY := V_ARRY('01046355217',
                                   '01029352811',
                                   '01074772806',
                                   '01032547885',
                                   '01092665720',
                                   '01020840414',
                                   '01056053913');
  NAME_VARRY_P900 V_ARRY := V_ARRY('조민하',
                                   '이지영',
                                   '정기복',
                                   '박은진',
                                   '정연진');
  TEL_VARRY_P900  V_ARRY := V_ARRY('01046355217',
                                   '01029352811',
                                   '01092665720',
                                   '01020840414',
                                   '01056053913');
  NAME_VARRY_PC01 V_ARRY := V_ARRY('이지영', '정기복', '박은진', '정연진');
  TEL_VARRY_PC01  V_ARRY := V_ARRY('01029352811',
                                   '01092665720',
                                   '01020840414',
                                   '01056053913');
  -- SMS 전송 --
  PROCEDURE INSERT_TSMS IS
  BEGIN
    INSERT INTO TBL_SUBMIT_QUEUE_1
      ( /* ums.xml : core.exchange.insertSmsDBAgentSend */CMP_MSG_ID,
       CMP_MSG_GROUP_ID,
       USR_ID,
       SMS_GB,
       USED_CD,
       RESERVED_FG,
       RESERVED_DTTM,
       SAVED_FG,
       RCV_PHN_ID,
       SND_PHN_ID,
       NAT_CD,
       ASSIGN_CD,
       SND_MSG,
       CALLBACK_URL,
       CONTENT_CNT,
       CONTENT_MIME_TYPE,
       CONTENT_PATH,
       CMP_SND_DTTM,
       CMP_RCV_DTTM,
       REG_SND_DTTM,
       REG_RCV_DTTM,
       MACHINE_ID,
       SMS_STATUS,
       RSLT_VAL,
       MSG_TITLE,
       TELCO_ID,
       ETC_CHAR_1,
       ETC_CHAR_2,
       ETC_CHAR_3,
       ETC_CHAR_4)
      SELECT TO_CHAR(SYSDATE, 'YYYYMMDD') ||
             LTRIM(TO_CHAR(TBL_SUBMIT_QUEUE_1_SEQ.NEXTVAL, '000000')),
             '',
             (SELECT MAX(C.VAL) FROM TCONFIG C WHERE C.ITEM = 'DB_AGENT1_ID'),
             '1',
             '10',
             'I',
             TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'),
             '0',
             V_PHONE_NO,
             (SELECT MAX(C.VAL) FROM TCONFIG C WHERE C.ITEM = 'SMS_SEND_NO'),
             '',
             '00000',
             V_ERROR_MSG,
             '',
             1,
             'text/plain',
             '',
             '',
             '',
             '',
             '',
             '',
             '0',
             '',
             REMARK1,
             '',
             '',
             '999',
             '',
             '999999'
        FROM TCODE
       WHERE CODE_LGROUP = 'C302'
         AND CODE_MGROUP = '999';
  END;

  -- SMS 전송대상  --
  PROCEDURE SEND_SMS IS
  BEGIN
    CASE
      WHEN V_SEND_SMS_YN = '1' AND V_FLAG >= 'P700' AND V_FLAG < 'P799' THEN
        FOR I IN TEL_VARRY_P700.FIRST .. TEL_VARRY_P700.LAST LOOP
          V_NAME     := NAME_VARRY_P700(I);
          V_PHONE_NO := TEL_VARRY_P700(I);
          INSERT_TSMS();
        END LOOP;
      WHEN V_FLAG >= 'P799' AND V_FLAG < 'P800' THEN
        FOR I IN TEL_VARRY_P799.FIRST .. TEL_VARRY_P799.LAST LOOP
          V_NAME     := NAME_VARRY_P799(I);
          V_PHONE_NO := TEL_VARRY_P799(I);
          INSERT_TSMS();
        END LOOP;
      WHEN V_SEND_SMS_YN = '1' AND V_FLAG >= 'P900' AND V_FLAG < 'P999' THEN
        FOR I IN TEL_VARRY_P900.FIRST .. TEL_VARRY_P900.LAST LOOP
          V_NAME     := NAME_VARRY_P900(I);
          V_PHONE_NO := TEL_VARRY_P900(I);
          INSERT_TSMS();
        END LOOP;
      WHEN V_SEND_SMS_YN = '1' THEN
        --케이스 없을 경우 쿠팡 팀에게 메세지 가게 처리
        FOR I IN TEL_VARRY_PC01.FIRST .. TEL_VARRY_PC01.LAST LOOP
          V_NAME     := NAME_VARRY_PC01(I);
          V_PHONE_NO := TEL_VARRY_PC01(I);
          INSERT_TSMS();
        END LOOP;
      ELSE
          V_NAME := '발송X';
          
    END CASE;
  END;

  -- 자료오류 처리 : 로그, 전송  --
  PROCEDURE INSERT_ERROR_LOG IS
  BEGIN
  	DBMS_OUTPUT.PUT_LINE('--  ERROR ' || V_FLAG || ':' || V_ERROR_MSG);
    PKG_COMMON.EXP_PRNS(V_PROG_ID,
                        '  --  ERROR ' || V_FLAG || ':' || V_ERROR_MSG);
  
    INSERT INTO TSDCHECKLOG_PA
      (CLOSE_DATE, SEQ, FLAG, ERR_TEXT, INSERT_DATE)
    VALUES
      (TO_DATE(V_SYSDATE, 'YYYYMMDD'),
       (SELECT LPAD(NVL(MAX(A.SEQ) + 1, 1), 3, '000')
          FROM TSDCHECKLOG_PA A
         WHERE A.CLOSE_DATE = TO_DATE(V_SYSDATE, 'YYYYMMDD')),
       V_FLAG,
       V_FLAG || ':' || V_ERROR_MSG,
       SYSDATE);
    SEND_SMS();
  END;

  -- 프로시저 실행여부  --
  PROCEDURE INSERT_PROC_S_LOG IS
  BEGIN
    INSERT INTO TPASDCHECKLOG
      (PRG_ID, FLAG, ERR_TEXT, PROC_DATE)
    VALUES
      (V_PROG_ID, 'S', '', TO_DATE(V_SYSDATETIME, 'YYYYMMDDHH24MISS'));
  END;

  -- 프로시저 종료여부  --
  PROCEDURE INSERT_PROC_E_LOG IS
  BEGIN
    INSERT INTO TPASDCHECKLOG
      (PRG_ID, FLAG, ERR_TEXT, PROC_DATE)
    VALUES
      (V_PROG_ID, 'E', '', SYSDATE);
  END;

  -- 프로시저 에러여부-
  PROCEDURE INSERT_PROC_F_LOG IS
  BEGIN
    INSERT INTO TPASDCHECKLOG
      (PRG_ID, FLAG, ERR_TEXT, PROC_DATE)
    VALUES
      (V_PROG_ID, 'F', V_DB_ERROR_MSG, SYSDATE);
  END;

  ----------------------------------------------------------

BEGIN

  -- setting program id
  V_PROG_ID := 'sp_tsdcheck_paorder';

  -- setting sysdate
  OUT_CODE      := 0;
  V_SYSDATE     := PKG_COMMON.F_GET_SYSDATE;
  V_SYSDATETIME := PKG_COMMON.F_GET_SYSDATETIME;

  INSERT_PROC_S_LOG();
  commit;
  
  -- create start log
  PKG_COMMON.STARTLOG(V_PROG_ID);

  -- setting input argument
  V_ERROR_MSG := 'set input arguments';
  OUT_MSG     := '';
  V_USER_ID   := PKG_COMMON.B_USER_ID;
  V_FROMDATE  := TO_CHAR(TRUNC(SYSDATE) - 5, 'yyyymmdd');
  V_TODATE    := TO_CHAR(TRUNC(SYSDATE) + 1, 'yyyymmdd');
  D_SYSDATE   := SYSDATE;
  PKG_COMMON.EXP_PRNS(V_PROG_ID, 'sysdate     : ' || V_SYSDATE);
  PKG_COMMON.EXP_PRNS(V_PROG_ID, 'sysdatetime : ' || V_SYSDATETIME);
  PKG_COMMON.EXP_PRNS(V_PROG_ID, 'v_fromdate  : ' || V_FROMDATE);
  PKG_COMMON.EXP_PRNS(V_PROG_ID, 'v_todate    : ' || V_TODATE);

  SELECT CASE
           WHEN XA.HOLIDAY_DAY = '0' AND XA.WORK_YN = '1' THEN
            '1'
           ELSE
            '0'
         END
    INTO V_SEND_SMS_YN
    FROM (SELECT CASE
                   WHEN TO_CHAR(D_SYSDATE, 'D') IN ('2', '3', '4', '5', '6') THEN
                    '0'
                   ELSE
                    '1'
                 END HOLIDAY_DAY,
                 '1' AS WORK_YN
          /* , NVL(( SELECT DA.WORK_YN
              FROM TDELYDAY DA
             WHERE DA.DELY_GB ='10'
               AND DA.YYMMDD = TRUNC(d_sysdate)
          ),'1') AS WORK_YN*/
            FROM DUAL) XA;

  SELECT NVL((SELECT '1'
               FROM DUAL
              WHERE V_SYSDATETIME BETWEEN V_SYSDATE || '071000' AND
                    V_SYSDATE || '200000'),
             '0')
    INTO V_TIME_CHECK_YN
    FROM DUAL;

  IF V_TIME_CHECK_YN = '0' THEN
    RETURN;
  END IF;

  --------------------------------------------------------------------------------
  /* 주문/배송 데이터 생성 관련 */

  V_FLAG      := 'P701';
  V_ERROR_MSG := '[확인_P701]공통 MEDIA_CODE 점검 (TPAORDERM.PA_CODE <-> TORDERDT.MEDIA_CODE 비교)';
  IS_EXISTED  := 0;

  /* P701: 11번가인경우 매체코드 EX01 G마켓인경우 매체코드 EX02 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM TPAORDERM PO, TCODE TC
  /*   WHERE PO.PA_CODE = TC.CODE_MGROUP*/ -- CODE_MGROUP은 GROUP_CODE인데 왜 PA_CODE랑 쪼인 .. ?
   WHERE PO.PA_GROUP_CODE = TC.CODE_MGROUP
     AND PO.CREATE_YN = '1'
     AND TC.CODE_LGROUP = 'O500'
     AND PO.CREATE_DATE  >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')     
     AND EXISTS (SELECT 1
            FROM TORDERDT OD
           WHERE OD.ORDER_NO = PO.ORDER_NO
             AND OD.ORDER_G_SEQ = PO.ORDER_G_SEQ
             AND OD.ORDER_D_SEQ = PO.ORDER_D_SEQ
             AND OD.ORDER_W_SEQ = PO.ORDER_W_SEQ
             AND OD.MEDIA_CODE <> TC.REMARK1)
     AND EXISTS (SELECT 1
            FROM TCANCELDT CD1
           WHERE CD1.ORDER_NO = PO.ORDER_NO
             AND CD1.ORDER_G_SEQ = PO.ORDER_G_SEQ
             AND CD1.ORDER_D_SEQ = PO.ORDER_D_SEQ
             AND CD1.ORDER_W_SEQ = PO.ORDER_W_SEQ
             AND CD1.MEDIA_CODE <> TC.REMARK1)
     AND EXISTS (SELECT 1
            FROM TCLAIMDT CD2
           WHERE CD2.ORDER_NO = PO.ORDER_NO
             AND CD2.ORDER_G_SEQ = PO.ORDER_G_SEQ
             AND CD2.ORDER_D_SEQ = PO.ORDER_D_SEQ
             AND CD2.ORDER_W_SEQ = PO.ORDER_W_SEQ
             AND CD2.MEDIA_CODE <> TC.REMARK1);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  /*  V_FLAG      := 'P702';
  V_ERROR_MSG := '[확인_P702]공통_주문수량 대비 반품+교환 수량이 많은건';
  IS_EXISTED  := 0;*/

  /* P702 : 공통_주문수량 대비 반품+교환 수량이 많은건 */
  /*SELECT CASE WHEN COUNT(1) > 0 THEN 1
           ELSE 0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT OD.ORDER_NO,
                 OD.ORDER_G_SEQ,
                 OD.ORDER_DATE,
                 OD.ORDER_QTY,
                 OD.CANCEL_QTY,
                 (OD.CLAIM_QTY - OD.CLAIM_CAN_QTY) AS CLAIM_QTY,
                 NVL((SELECT SUM(CD.SYSLAST)
                       FROM TCLAIMDT CD
                      WHERE OD.ORDER_NO = CD.ORDER_NO
                        AND OD.ORDER_G_SEQ = CD.ORDER_G_SEQ
                        AND CD.CLAIM_GB = '40' \* 40-교환배송 *\
                        AND CD.SYSLAST > 0
                        AND CD.DO_FLAG < 40),
                     0) AS EXCH_QTY
            FROM TORDERGOODS OD
           WHERE OD.ORDER_DATE >= TO_DATE(V_TODATE, 'YYYYMMDDHH24MISS') - 30
             AND OD.ORDER_GB = '10'
             AND EXISTS (SELECT '1'
                    FROM TORDERDT ODT
                   WHERE OD.ORDER_NO = ODT.ORDER_NO
                     AND OD.ORDER_G_SEQ = ODT.ORDER_G_SEQ
                     AND ODT.MEDIA_CODE IN
                         (SELECT REMARK1
                            FROM TCODE
                           WHERE CODE_LGROUP = 'O501'
                             AND USE_YN = '1'))
                             AND OD.ORDER_NO NOT IN (
                                                      '20210905528098'
                                                    , '20210913205644'
                                                    , '20210921813909'
                                                    , '20210911430752' -- 10/6
                                                    , '20210913242718' -- 10/6
                                                    , '20210922496136' -- 10/6
                                                    , '20210913189810' -- 10/12
                                                    , '20210922536035' -- 10/12
                                                    , '20211006649705' -- 10/12
                                                    , '20210930068286' -- 10/13
                                                    , '20211005947965' -- 10/13
                                                    , '20211002641529' -- 10/14
                                                    , '20210927409094' -- 10/18
                                                    , '20210927409094' -- 10/18
                                                    , '20210921786143' -- 10/18
                                                    , '20211001332801' -- 10/18
                                                    , '20210921822247' -- 10/19
                                                    , '20210924253504' -- 10/19
                                                    , '20211012987085' -- 10/20
                                                    , '20211016773521' -- 10/20
                                                    , '20210930058551' -- 10/21
                                                    , '20210928870827' -- 10/21
                                                    , '20211011599339' -- 10/21
                                                    , '20210930056642' -- 10/22
                                                    , '20211016784884' -- 10/25
                                                    , '20211017019778' -- 10/26
                                                    , '20211017949096' -- 10/27
                                                    , '20211020766492' -- 10/28
                                                    , '20211009678880' -- 10/29
                                                    , '20211017977650' -- 10/29
                                                    , '20211019077099' -- 10/29
                                                    , '20211023861604' -- 10/29
                                                    , '20211014168098' -- 10/29
                                                    , '20211024095206' -- 11/2
                                                    , '20211019032888' -- 11/3
                                                    , '20211028285079' -- 11/3
                                                    , '20211008390571' -- 11/3
                                                    , '20211018663956' -- 11/5
                                                    , '20211022540559' -- 11/5
                                                    , '20211027860036' -- 11/5
                                                     )) XA
   WHERE XA.ORDER_QTY < (XA.CANCEL_QTY + XA.CLAIM_QTY + XA.EXCH_QTY)
  
  ;
  
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;*/
  -- END --

  V_FLAG      := 'P703';
  V_ERROR_MSG := '[확인_P703]공통_제휴 주문건 출고되었으나 제휴사 출고처리 안된건';
  IS_EXISTED  := 0;

  /* P703 : SK스토아는 출고됨 하지만 제휴사는 출고안됨*/
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PO1.*
            FROM TPAORDERM PO1
           WHERE PO1.PA_ORDER_GB IN ('10', '40')
             AND PO1.PA_DO_FLAG < 40
             AND PO1.CREATE_DATE > TRUNC(SYSDATE) - 60
             AND PO1.PA_ORDER_NO NOT IN ('20200820454116' -- 9/7
                                         )
             AND EXISTS
           (SELECT 1
                    FROM TPAORDERM   PO2,
                         TSLIPM      SM,
                         TSLIPDT     SD,
                         TORDERGOODS OG,
                         TSLIPPROC   SP
                   WHERE PO2.ORDER_NO = PO1.ORDER_NO
                     AND PO2.ORDER_G_SEQ = PO1.ORDER_G_SEQ
                     AND PO2.ORDER_D_SEQ = PO1.ORDER_D_SEQ
                     AND PO2.ORDER_W_SEQ = PO1.ORDER_W_SEQ
                     AND SM.SLIP_I_NO = SD.SLIP_I_NO
                     AND PO2.ORDER_NO = SD.ORDER_NO
                     AND PO2.ORDER_G_SEQ = SD.ORDER_G_SEQ
                     AND PO2.ORDER_D_SEQ = SD.ORDER_D_SEQ
                     AND PO2.ORDER_W_SEQ = SD.ORDER_W_SEQ
                     AND PO2.ORDER_NO = OG.ORDER_NO
                     AND PO2.ORDER_G_SEQ = OG.ORDER_G_SEQ
                     AND SM.SLIP_I_NO = SP.SLIP_I_NO
                     AND SP.SLIP_PROC = '40'
                     AND SM.SLIP_NO IS NOT NULL
                     AND SP.SLIP_PROC_DATE < SYSDATE - INTERVAL '2'
                   HOUR
                     AND DECODE(SM.SLIP_GB,
                                '101',
                                OG.ORDER_QTY - OG.CANCEL_QTY - OG.CLAIM_QTY +
                                OG.CLAIM_CAN_QTY,
                                SD.DELY_QTY) > 0)
             AND PO1.PA_ORDER_NO NOT IN ('21000137828018' -- 5/24 수동회수접수 철회 상태(문의중)
                                         ));
                                         
	DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'P704';
  V_ERROR_MSG := '[확인_P704]공통_제휴 회수확정 완료되었으나 제휴사 회수확정 안된건';
  IS_EXISTED  := 0;

  /* P704 : SK스토아는 회수확정, 제휴사는 회수확정 안됨 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT /*+USE_HASH(PO)*/
           PO.PA_ORDER_NO, PO.ORDER_NO, PO.PA_ORDER_SEQ, PO.PA_CLAIM_NO
            FROM TPAORDERM PO, TCLAIMDT CD, TSLIPDT SD, TSLIPM SM
           WHERE PO.ORDER_NO = CD.ORDER_NO
             AND PO.ORDER_G_SEQ = CD.ORDER_G_SEQ
             AND PO.ORDER_D_SEQ = CD.ORDER_D_SEQ
             AND PO.ORDER_W_SEQ = CD.ORDER_W_SEQ
             AND SD.ORDER_NO = CD.ORDER_NO
             AND SD.ORDER_G_SEQ = CD.ORDER_G_SEQ
             AND SD.ORDER_D_SEQ = CD.ORDER_D_SEQ
             AND SD.ORDER_W_SEQ = CD.ORDER_W_SEQ
             AND SD.SLIP_I_NO = SM.SLIP_I_NO
             AND SM.SLIP_NO IS NOT NULL
             AND PO.PA_ORDER_GB = '30'
             AND PO.CREATE_YN = '1'
             AND PO.PA_DO_FLAG NOT IN ('55', '58', '59', '60') --55: 위메프 수거요청, 58: 위메프 수거완료
             AND PO.OUT_BEF_CLAIM_GB = '0' --쿠팡 출고전 반품처리 예외처리 pa_order_gb = 30, out_bef_claim_gb = '1'
             AND CD.DO_FLAG = '60'
             AND CD.SYSLAST > 0
             AND CD.LAST_PROC_DATE >= TRUNC(SYSDATE) - 5
             AND CD.LAST_PROC_DATE < SYSDATE - INTERVAL '2'
           HOUR
            -- AND PO.PA_ORDER_NO NOT IN ('' -- 수기 처리 건 추가하시면 됩니다.(23/03/15 영향 없는 주문건 제거했음)
            --                            )
          
          UNION ALL
          
          SELECT PO.PA_ORDER_NO,
                 PO.ORDER_NO,
                 PO.PA_ORDER_SEQ,
                 PO.PA_CLAIM_NO
            FROM TPAORDERM  PO,
                 TCLAIMDT   CD /* 교환회수 */,
                 TORDERPROC OP /* 교환배송 상태 조회 */,
                 TSLIPDT    SD,
                 TSLIPM     SM
           WHERE PO.ORDER_NO = CD.ORDER_NO
             AND PO.ORDER_G_SEQ = CD.ORDER_G_SEQ
             AND PO.ORDER_D_SEQ = CD.ORDER_D_SEQ
             AND PO.ORDER_W_SEQ = CD.ORDER_W_SEQ
             AND OP.ORDER_NO = CD.ORDER_NO
             AND OP.ORDER_G_SEQ = CD.ORDER_G_SEQ
             AND OP.ORDER_D_SEQ = CD.ORDER_D_SEQ
             AND OP.ORDER_W_SEQ = CD.EXCH_PAIR
             AND OP.DO_FLAG = '40'
             AND PO.PA_ORDER_GB = '45'
             AND SD.ORDER_NO = CD.ORDER_NO
             AND SD.ORDER_G_SEQ = CD.ORDER_G_SEQ
             AND SD.ORDER_D_SEQ = CD.ORDER_D_SEQ
             AND SD.ORDER_W_SEQ = CD.ORDER_W_SEQ
             AND SD.SLIP_I_NO = SM.SLIP_I_NO
             AND SM.SLIP_NO IS NOT NULL
             AND PO.CREATE_YN = '1'
                --AND PO.PA_DO_FLAG <> '60'
                --AND PO.PA_DO_FLAG <> '90'
             AND PO.PA_DO_FLAG NOT IN ('55', '58', '60', '90')
             AND CD.DO_FLAG = '60'
             AND CD.SYSLAST > 0
             AND (CASE
                   WHEN PO.PA_CODE IN ('11', '12') THEN
                    (SELECT COUNT(1)
                       FROM TSLIPM SM, TSLIPDT SD
                      WHERE SM.SLIP_I_NO = SD.SLIP_I_NO
                        AND SD.ORDER_NO = PO.ORDER_NO
                        AND PO.ORDER_G_SEQ = SD.ORDER_G_SEQ
                        AND PO.ORDER_D_SEQ = SD.ORDER_D_SEQ
                        AND PO.ORDER_W_SEQ = SD.ORDER_W_SEQ
                        AND SM.SLIP_GB = '104'
                        AND SM.SLIP_NO IS NOT NULL)
                   ELSE
                    1
                 END) > 0
             AND OP.PROC_DATE >= TRUNC(SYSDATE) - 5
             AND OP.PROC_DATE < SYSDATE - INTERVAL '2' HOUR);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'P705';
  V_ERROR_MSG := '[즉시_P705]공통_주문 정산배송비 금액검증 (TODERSHIPCOST <-> TORDERRECEIPTS)';
  IS_EXISTED  := 0;

  /* P705 : 공통_주문 정산배송비 금액검증 (TODERSHIPCOST <-> TORDERRECEIPTS) TYPE 발생구분[J081] */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT (SELECT SUM(POC.SHPFEE_COST)
                    FROM TORDERSHIPCOST POC
                   WHERE POC.ORDER_NO = PO.ORDER_NO
                     AND POC.TYPE IN ('10', '30', '40')) AS P_SHPFEE_COST,
                 (SELECT SUM(ORC.QUEST_AMT)
                    FROM TORDERRECEIPTS ORC
                   WHERE ORC.ORDER_NO = PO.ORDER_NO
                     AND ORC.SETTLE_GB = '15') AS P_QUEST_AMT,
                 (SELECT SUM(POC.SHPFEE_COST)
                    FROM TORDERSHIPCOST POC
                   WHERE POC.ORDER_NO = PO.ORDER_NO
                     AND POC.TYPE IN ('11', '20', '31', '41')) AS M_SHPFEE_COST,
                 (SELECT SUM(ORC.QUEST_AMT)
                    FROM TORDERRECEIPTS ORC
                   WHERE ORC.ORDER_NO = PO.ORDER_NO
                     AND (ORC.SETTLE_GB = '65' OR
                         (ORC.SETTLE_GB = '15' AND ORC.CANCEL_YN = '1'))) AS M_QUEST_AMT,
                 PO.ORDER_NO
            FROM TPAORDERM PO
           WHERE PO.CREATE_YN = '1'
             AND PO.CREATE_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND PO.ORDER_NO NOT IN ('20211110010095' -- 11/17
                                     )
           GROUP BY PO.ORDER_NO)
   WHERE P_SHPFEE_COST != P_QUEST_AMT
      OR M_SHPFEE_COST != M_QUEST_AMT;

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'P706';
  V_ERROR_MSG := '[즉시_P706]11ST 결제가 금액검증 (TPA11STORDERLIST  <-> TORDERDT <-> TGOODSPRICE)';
  IS_EXISTED  := 0;

  /* P706 : 11번가 결제가 금액검증 (TPA11STORDERLIST <-> TORDERDT <-> TGOODSPRICE ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT OL.ORD_NO,
                 OL.PRD_NO,
                 OL.ORD_PAY_AMT,
                 OL.LST_TMALL_DSC_PRC,
                 OL.ORD_QTY,
                 OL.ORD_AMT AS PA11ST_SALE_PRICE,
                 OD.SALE_PRICE * ORDER_QTY AS TORDERDT_SALE_PRICE -- 완료
                 -- , FUN_GET_GOODS_PRICE(OD.GOODS_CODE, OD.ORDER_DATE-60/24/60, '6') * OD.ORDER_QTY AS CALC_FUN_GET_GOODS_PRICE
                ,
                 FUN_GET_GOODS_PRICE(OD.GOODS_CODE, PGP.TRANS_DATE, '6') *
                 OD.ORDER_QTY AS CALC_FUN_GET_GOODS_PRICE,
                 PGP.SALE_PRICE * ORDER_QTY AS TPAGOODSPRICE_SALE_PRICE
            FROM TPAORDERM        PM,
                 TPA11STORDERLIST OL,
                 TORDERDT         OD,
                 TPAGOODSPRICE    PGP
           WHERE PM.PA_ORDER_GB = OL.PA_ORDER_GB
             AND PM.PA_ORDER_NO = OL.ORD_NO
             AND PM.PA_ORDER_SEQ = OL.ORD_PRD_SEQ
             AND PM.PA_SHIP_NO = OL.DLV_NO
             AND PM.ORDER_NO = OD.ORDER_NO
             AND PM.ORDER_G_SEQ = OD.ORDER_G_SEQ
             AND PM.ORDER_D_SEQ = OD.ORDER_D_SEQ
             AND PM.ORDER_W_SEQ = OD.ORDER_W_SEQ
             AND OD.GOODS_CODE = PGP.GOODS_CODE
             AND PM.PA_CODE IN ('11','12')
             AND PGP.PA_CODE = PM.PA_CODE
             AND PM.PA_ORDER_GB = '10'
             AND PM.CREATE_YN = '1'
             AND PGP.ROWID = (SELECT /*+ INDEX_DESC(I PK_TPAGOODSPRICE) */
                               ROWID
                                FROM TPAGOODSPRICE I
                               WHERE I.GOODS_CODE = PGP.GOODS_CODE
                                 AND I.PA_CODE = PM.PA_CODE
                                 AND I.TRANS_DATE <= OL.ORD_DT
                                 AND I.TRANS_DATE IS NOT NULL
                                 AND I.SALE_PRICE = OL.SEL_PRC
                                 AND ROWNUM = 1)
             AND PM.CREATE_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND ORD_NO NOT IN ('20220731490563000' -- 8/1
                                )
          --and ord_no not in ('201802282543973', '201803052544759', '201803052544751', '201803052544766') -- 임시주석
          
          )
   WHERE (PA11ST_SALE_PRICE <> TORDERDT_SALE_PRICE OR
         TORDERDT_SALE_PRICE <> CALC_FUN_GET_GOODS_PRICE OR
         CALC_FUN_GET_GOODS_PRICE <> TPAGOODSPRICE_SALE_PRICE);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'P709';
  V_ERROR_MSG := '[즉시_P709]공통_제휴결제가 검증 (전체취소/반품인 경우 주문 제휴결제가와 취+반품의 제휴결제가가 일치하는지)';
  IS_EXISTED  := 0;

  /* P709 : 공통_제휴결제가 검증 (전체취소/반품인 경우 주문 제휴결제가와 취+반품의 제휴결제가가 일치하는지) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT OD.ORDER_NO, OD.ORDER_G_SEQ, OD.ORDER_D_SEQ, OD.RSALE_AMT
            FROM TORDERDT OD
           WHERE OD.MEDIA_CODE IN (SELECT C.REMARK1
                                     FROM TCODE C
                                    WHERE C.CODE_LGROUP = 'O500'
                                      AND C.USE_YN = '1')
             AND OD.RSALE_AMT > 0
             AND OD.SYSLAST > 0
             AND OD.ORDER_DATE >= TRUNC(SYSDATE) - 30
             AND EXISTS
           (SELECT '1'
                    FROM TORDERGOODS OG
                   WHERE OD.ORDER_NO = OG.ORDER_NO
                     AND OD.ORDER_G_SEQ = OG.ORDER_G_SEQ
                     AND (OG.ORDER_QTY - OG.CANCEL_QTY - OG.CLAIM_QTY +
                         OG.CLAIM_CAN_QTY) = 0)
          MINUS
          
          SELECT XA.ORDER_NO,
                 XA.ORDER_G_SEQ,
                 XA.ORDER_D_SEQ,
                 SUM(XA.RSALE_AMT) AS RSALE_PA_AMT
            FROM (SELECT CD.ORDER_NO,
                         CD.ORDER_G_SEQ,
                         CD.ORDER_D_SEQ,
                         CD.RSALE_AMT
                    FROM TCANCELDT CD
                   WHERE CD.MEDIA_CODE IN
                         (SELECT C.REMARK1
                            FROM TCODE C
                           WHERE C.CODE_LGROUP = 'O500'
                             AND C.USE_YN = '1')
                     AND CD.RSALE_AMT > 0
                     AND CD.CANCEL_DATE >= TRUNC(SYSDATE) - 30
                     AND EXISTS
                   (SELECT '1'
                            FROM TORDERGOODS OG
                           WHERE CD.ORDER_NO = OG.ORDER_NO
                             AND CD.ORDER_G_SEQ = OG.ORDER_G_SEQ
                             AND (OG.ORDER_QTY - OG.CANCEL_QTY - OG.CLAIM_QTY +
                                 OG.CLAIM_CAN_QTY) = 0)
                  
                  UNION ALL
                  
                  SELECT CD.ORDER_NO,
                         CD.ORDER_G_SEQ,
                         CD.ORDER_D_SEQ,
                         CD.RSALE_AMT
                    FROM TCLAIMDT CD
                   WHERE CD.MEDIA_CODE IN
                         (SELECT C.REMARK1
                            FROM TCODE C
                           WHERE C.CODE_LGROUP = 'O500'
                             AND C.USE_YN = '1')
                     AND CD.RSALE_AMT > 0
                     AND CD.CLAIM_GB = '30'
                     AND CD.SYSLAST > 0
                     AND CD.CLAIM_DATE >= TRUNC(SYSDATE) - 30
                     AND EXISTS
                   (SELECT '1'
                            FROM TORDERGOODS OG
                           WHERE CD.ORDER_NO = OG.ORDER_NO
                             AND CD.ORDER_G_SEQ = OG.ORDER_G_SEQ
                             AND (OG.ORDER_QTY - OG.CANCEL_QTY - OG.CLAIM_QTY +
                                 OG.CLAIM_CAN_QTY) = 0)) XA
           GROUP BY XA.ORDER_NO, XA.ORDER_G_SEQ, XA.ORDER_D_SEQ);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'P710';
  V_ERROR_MSG := '[확인_P710]공통_분리포장 상품 합포장 되었는지 점검';
  IS_EXISTED  := 0;
  /* P710 : 공통_분리포장 상품 합포장 되었는지 점검 */
  SELECT /*+LEADING(SM) INDEX(SM IDX_TSLIPM_03) USE_NL(OD SD GD)*/
         CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM TORDERDT OD, TSLIPDT SD, TSLIPM SM, TGOODS GD
   WHERE OD.ORDER_NO = SD.ORDER_NO
     AND OD.ORDER_G_SEQ = SD.ORDER_G_SEQ
     AND OD.ORDER_D_SEQ = SD.ORDER_D_SEQ
     AND OD.ORDER_W_SEQ = SD.ORDER_W_SEQ
     AND SD.SLIP_I_NO = SM.SLIP_I_NO
     AND OD.GOODS_CODE = GD.GOODS_CODE
     AND SM.CREATE_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
     AND OD.MEDIA_CODE IN (SELECT C.REMARK1
                             FROM TCODE C
                            WHERE C.CODE_LGROUP = 'O500'
                              AND C.USE_YN = '1' /* 합포장여부가 1인 제휴사의 MEDIA_CODE 조회 */
                           )
     AND GD.MIXPACK_YN = '0'
     AND OD.SYSLAST > 2
     AND OD.SYSLAST <> SD.DELY_QTY;

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'P711';
  V_ERROR_MSG := '[확인_P711]공통_맞교환 불가 제휴사인데 맞교환 처리된건 점검';
  IS_EXISTED  := 0;
  /* P711 : 공통_맞교환 불가 제휴사인데 맞교환 처리된건 점검 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM TCLAIMDT CD1 /*교환배송건*/, TCLAIMDT CD2 /*교환회수건*/
   WHERE CD1.ORDER_NO = CD2.ORDER_NO
     AND CD1.ORDER_G_SEQ = CD2.ORDER_G_SEQ
     AND CD1.ORDER_D_SEQ = CD2.ORDER_D_SEQ
     AND CD1.ORDER_W_SEQ = CD2.EXCH_PAIR
     AND CD1.CLAIM_GB = '40'
     AND CD2.CLAIM_GB = '45'
     AND CD1.SYSLAST > 0
     AND CD1.DO_FLAG <> '10'
     AND CD2.DO_FLAG < '60'
     AND CD1.CLAIM_DATE > SYSDATE - 90
     AND CD1.MEDIA_CODE IN (SELECT C.REMARK1
                              FROM TCODE C
                             WHERE C.CODE_LGROUP = 'O500'
                               AND C.USE_YN = '1')
     AND CD1.ORDER_NO NOT IN ('20210908400887',
                              '20210910142159',
                              '20211127179440',
                              '20211203887795');

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'P712';
  V_ERROR_MSG := '[확인_P712]공통_교환회수확정이 되었는데 교환출고 처리가 안되는건 점검';
  IS_EXISTED  := 0;
  /* P712 : 공통_교환회수확정이 되었는데 교환출고 처리가 안되는건 점검 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT CD.ORDER_NO,
                 CD.ORDER_G_SEQ,
                 CD.ORDER_D_SEQ,
                 CD.ORDER_W_SEQ,
                 (SELECT CD2.LAST_PROC_DATE
                    FROM TCLAIMDT CD2
                   WHERE CD.ORDER_NO = CD2.ORDER_NO
                     AND CD.ORDER_G_SEQ = CD2.ORDER_G_SEQ
                     AND CD.ORDER_D_SEQ = CD2.ORDER_D_SEQ
                     AND CD.ORDER_W_SEQ = CD2.EXCH_PAIR
                     AND CD2.DO_FLAG = '60'
                     AND CD2.CLAIM_GB = '45') AS EXCH_RETURN_DATE
            FROM TCLAIMDT CD, TSLIPDT SD, TSLIPM SM
           WHERE CD.MEDIA_CODE IN (SELECT C.REMARK1
                                     FROM TCODE C
                                    WHERE C.CODE_LGROUP = 'O500'
                                      AND C.USE_YN = '1')
             AND CD.CLAIM_DATE >=
                 TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS') - 90
             AND CD.CLAIM_GB = '40'
             AND CD.DO_FLAG = '10'
             AND CD.DELY_TYPE = '20'
             AND CD.ORDER_NO = SD.ORDER_NO
             AND CD.ORDER_G_SEQ = SD.ORDER_G_SEQ
             AND CD.ORDER_D_SEQ = SD.ORDER_D_SEQ
             AND CD.ORDER_W_SEQ = SD.ORDER_W_SEQ
             AND SD.SLIP_I_NO = SM.SLIP_I_NO
             AND SM.SLIP_NO IS NOT NULL
             AND EXISTS (SELECT '1'
                    FROM TCLAIMDT CD2
                   WHERE CD.ORDER_NO = CD2.ORDER_NO
                     AND CD.ORDER_G_SEQ = CD2.ORDER_G_SEQ
                     AND CD.ORDER_D_SEQ = CD2.ORDER_D_SEQ
                     AND CD.ORDER_W_SEQ = CD2.EXCH_PAIR
                     AND CD2.DO_FLAG = '60'
                     AND CD2.CLAIM_GB = '45'
                     AND CD2.LAST_PROC_DATE >=
                         TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
                     AND CD2.LAST_PROC_DATE < TRUNC(SYSDATE))
             AND CD.ORDER_NO NOT IN ('20191109901219',
                                     '20200705208328',
                                     '20200918689244' -- 9/29
                                    ,
                                     '20200925759177' -- 11/25
                                    ,
                                     '20201216851696' -- 02/03
                                    ,
                                     '20211020765430' -- 10/28 교취상태에서 회수확정??
                                     ))
   WHERE 1 = 1;

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

/* 굳이 필요 없는거 같은데 2023-05-16 gbjeong
  V_FLAG      := 'P714';
  V_ERROR_MSG := '[확인_P714]공통_주문매핑 테이블에 정의되지 않은 코드값이 들어 왔는지 점검';
  IS_EXISTED  := 0;
  \* P714 : 공통_주문매핑 테이블에 정의되지 않은 코드값이 들어 왔는지 점검 *\
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM TPAORDERM PO
   WHERE PO.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
     AND (NOT EXISTS
          (SELECT '1'
             FROM TCODE TC
            WHERE TC.CODE_LGROUP = 'O501'
              AND TC.CODE_MGROUP = PO.PA_CODE) OR NOT EXISTS
          (SELECT '1'
             FROM TCODE TC
            WHERE TC.CODE_LGROUP = 'O500'
              AND TC.CODE_MGROUP = PO.PA_GROUP_CODE) OR NOT EXISTS
          (SELECT '1'
             FROM TCODE TC
            WHERE TC.CODE_LGROUP = 'J007'
              AND TC.CODE_MGROUP = PO.PA_ORDER_GB) OR
          PO.PA_DO_FLAG NOT IN ('10',
                                '20',
                                '30',
                                '35',
                                '40',
                                '50',
                                '55',
                                '56',
                                '58',
                                '59',
                                '60',
                                '80',
                                '90') OR PO.PRE_CANCEL_YN NOT IN ('0', '1') OR
          PO.OUT_BEF_CLAIM_GB NOT IN ('0', '1', '2') OR
          PO.PA_HOLD_YN NOT IN ('0', '1'));

  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF; */ 

  /* V_FLAG      := 'P716';
    V_ERROR_MSG := '[확인_P716]공통_전일자 데이터 중 배송번호 기준 : API 통신 실패 횟수가 5회 이상인 건 조회';
    IS_EXISTED  := 0;
    \* P716 : 공통_전일자 데이터 중 배송번호 기준 : API 통신 실패 횟수가 5회 이상인 건 조회 *\
    SELECT CASE
             WHEN COUNT(1) > 0 THEN
              1
             ELSE
              0
           END DATA_CHECK
      INTO IS_EXISTED
      FROM (SELECT POL.PA_ORDER_NO,
                   POL.PA_ORDER_SEQ,
                   POL.PA_CODE,
                   COUNT(1),
                   POL.API_RESULT_MESSAGE
              FROM TPAORDERAPILOG POL
             WHERE POL.API_RESULT_CODE NOT IN ('0', '000000', '100001')
               AND PROC_DATE BETWEEN TO_DATE(V_TODATE, 'yyyymmdd') - 2 AND
                   TO_DATE(V_TODATE, 'yyyymmdd')
               AND POL.PA_ORDER_NO NOT IN ('240635344' -- 12/10
                                          ,
                                           '241436959' -- 12/10
                                          ,
                                           '20211207323901661' -- 12/10
                                          ,
                                           '4923936305' -- 12/10
                                          ,
                                           '20211203083231714592' -- 12/10
                                          ,
                                           '3071050270' -- 12/10
                                          ,
                                           '4926059123' -- 12/10
                                          ,
                                           '31000118942354' -- 12/10
                                          ,
                                           '3073788046' -- 12/10
                                          ,
                                           '3072895714' -- 12/10
                                          ,
                                           '3067295562' -- 12/10
                                          ,
                                           '3071812574' -- 12/10
                                          ,
                                           '3071050270' -- 12/10
                                          ,
                                           '3072895714' -- 12/10
                                          ,
                                           '3073788046' -- 12/10
                                           )
               AND POL.API_RESULT_MESSAGE NOT LIKE '%DELIVERING%'
               AND POL.API_RESULT_MESSAGE NOT LIKE '%FINAL_DELIVERY%'
               AND POL.API_RESULT_MESSAGE NOT LIKE '%NONE_TRACKING%'
               AND POL.API_RESULT_MESSAGE NOT LIKE '%배송완료 처리 실패%'
               AND POL.API_RESULT_MESSAGE NOT LIKE '%배송완료 처리실패%'
               AND POL.API_RESULT_MESSAGE NOT LIKE '%이미%'
               AND POL.API_RESULT_MESSAGE NOT LIKE '%DataBase에서 오류가%'
               AND POL.API_RESULT_MESSAGE NOT LIKE '재고부족으로 인한 결품처리'
               AND POL.API_RESULT_MESSAGE NOT LIKE '%Connection reset'
               AND POL.API_RESULT_MESSAGE NOT LIKE
                   '%code: 500 for URL: https://api.11st.co.kr/rest/ordservices/reqdelivery%'
               AND POL.API_RESULT_MESSAGE NOT LIKE
                   '%Ip address of the request is not allowed%'
               AND POL.API_RESULT_MESSAGE NOT LIKE
                   '%Could not open JDBC Connection%'
               AND POL.API_RESULT_MESSAGE NOT LIKE '%취소처리중 에러발생(내부오류)%'
               AND POL.API_RESULT_MESSAGE NOT LIKE '%배송진행상태가 유효하지 않습니다.%'
               AND POL.API_RESULT_MESSAGE NOT LIKE '%취소요청건 발송처리를 위한 취소철회가 실패%'
               AND POL.API_RESULT_MESSAGE NOT LIKE
                   '%White spaces are required between publicId and systemId.%'
               AND POL.API_RESULT_MESSAGE NOT LIKE '%Dismatch_Price%'
               AND POL.API_RESULT_MESSAGE NOT LIKE 'selectPaOrderPromo%'
             GROUP BY POL.PA_ORDER_NO,
                      POL.PA_ORDER_SEQ,
                      POL.PA_CODE,
                      POL.API_RESULT_MESSAGE
            HAVING COUNT(1) > 5);
  */
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'P719';
  V_ERROR_MSG := '[즉시_P719]I/F 공통 주문검증 ( TPAORDERM <-> TORDERDT )';
  IS_EXISTED  := 0;
  /* P719 : I/F 공통 주문검증 ( TPAORDERM <-> TORDERDT ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT /*+ INDEX(PM IDX_TPAORDERM_06)*/
                 PM.ORDER_NO,
                 PM.ORDER_G_SEQ,
                 PM.ORDER_D_SEQ,
                 PM.ORDER_W_SEQ,
                 TO_NUMBER(PM.PA_PROC_QTY)
            FROM TPAORDERM PM
           WHERE PM.CREATE_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND PM.CREATE_YN = '1'
             AND PM.PA_ORDER_GB = '10'
             /*AND PM.ORDER_NO NOT IN ('20201121473925'  11/27
                                     )*/
             AND NOT EXISTS
           (SELECT /*+LEADING(OD OM TM) INDEX(OD PK_TORDERDT)*/1
                    FROM TORDERDT OD, TORDERM OM, TMEDIA TM
                   WHERE OM.ORDER_NO = OD.ORDER_NO
                     AND OD.MEDIA_GB = '03'
                     AND OD.GOODS_GB = '10'
                     AND OD.MEDIA_CODE = TM.MEDIA_CODE
                     AND TM.MOB_POC_GB = '20'
                     AND OD.ORDER_NO = PM.ORDER_NO
                     AND OD.ORDER_G_SEQ = PM.ORDER_G_SEQ
                     AND OD.ORDER_D_SEQ = PM.ORDER_D_SEQ
                     AND OD.ORDER_W_SEQ = PM.ORDER_W_SEQ
                     AND OD.ORDER_QTY = TO_NUMBER(PM.PA_PROC_QTY)));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  -- END --

  V_FLAG      := 'P720';
  V_ERROR_MSG := '[즉시_P720]I/F 공통 주문검증 ( TORDERDT <-> TPAORDERM )';
  IS_EXISTED  := 0;
  /* P720 : I/F 공통 주문검증 ( TORDERDT <-> TPAORDERM ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT OD.ORDER_NO,
                 OD.ORDER_G_SEQ,
                 OD.ORDER_D_SEQ,
                 OD.ORDER_W_SEQ,
                 OD.ORDER_QTY
            FROM TORDERDT OD, TORDERM OM, TCUSTOMER CU, TMEDIA TM
           WHERE OD.ORDER_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND OD.ORDER_NO = OM.ORDER_NO
             AND OD.CUST_NO = CU.CUST_NO
             AND CU.RECEIVE_METHOD IN
                 ('50', '55', '60', '61', '62', '63', '64', '65', '66', '67')
             AND OD.MEDIA_GB = '03'
             AND OD.MEDIA_CODE IN (SELECT TC.REMARK1
                                     FROM TCODE TC
                                    WHERE TC.CODE_LGROUP = 'O500'
                                      AND TC.USE_YN = '1')
             AND OD.GOODS_GB = '10'
             AND OD.MEDIA_CODE = TM.MEDIA_CODE
             AND TM.MOB_POC_GB = '20'
             AND OD.PREOUT_GB = '00' 
             AND OD.ORDER_NO NOT IN ('20220510984934' -- 5/11
                                    ,
                                     '20220522472419' -- 5/26 (SSG)
                                    ,'20230814824361' -- 8/14
                                    ,'20230818237586' -- 8/18 SSG 기출하
                                     )
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.CREATE_YN = '1'
                     AND PM.PA_ORDER_GB = '10'
                     AND PM.ORDER_NO = OD.ORDER_NO
                     AND PM.ORDER_G_SEQ = OD.ORDER_G_SEQ
                     AND PM.ORDER_D_SEQ = OD.ORDER_D_SEQ
                     AND PM.ORDER_W_SEQ = OD.ORDER_W_SEQ
                     AND TO_NUMBER(PM.PA_PROC_QTY) = OD.ORDER_QTY));

  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  -- END --

  V_FLAG      := 'P723';
  V_ERROR_MSG := '[즉시_P723]I/F 공통 취소검증 ( TPAORDERM <-> TCANCELDT )';
  IS_EXISTED  := 0;
  /* P723 : I/F 공통 취소검증 ( TPAORDERM <-> TCANCELDT ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.ORDER_NO,
                 PM.ORDER_G_SEQ,
                 PM.ORDER_D_SEQ,
                 PM.ORDER_W_SEQ,
                 TO_NUMBER(PM.PA_PROC_QTY)
            FROM TPAORDERM PM
           WHERE PM.CREATE_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND PM.CREATE_YN = '1'
             AND (PM.PA_ORDER_GB = '20' OR
                 (PM.PA_CODE IN ('51', '52') AND PM.PA_ORDER_GB = '30' AND
                 INSTR(PM.API_RESULT_MESSAGE, '주문취소') > 0))
             AND PM.OUT_BEF_CLAIM_GB = '0'
             AND PM.ORDER_NO NOT IN ('20200922759933' -- 9/28
                                     )
             AND NOT EXISTS
           (SELECT 1
                    FROM TCANCELDT CD, TMEDIA TM
                   WHERE CD.MEDIA_GB = '03'
                     AND CD.GOODS_GB = '10'
                     AND CD.MEDIA_CODE = TM.MEDIA_CODE
                     AND TM.MOB_POC_GB = '20'
                     AND CD.ORDER_NO = PM.ORDER_NO
                     AND CD.ORDER_G_SEQ = PM.ORDER_G_SEQ
                     AND CD.ORDER_D_SEQ = PM.ORDER_D_SEQ
                     AND CD.ORDER_W_SEQ = PM.ORDER_W_SEQ
                     AND CD.CANCEL_QTY = TO_NUMBER(PM.PA_PROC_QTY)));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  -- END --

  V_FLAG      := 'P724';
  V_ERROR_MSG := '[즉시_P724] I/F 공통 취소검증 ( TCANCELDT <-> TPAORDERM )';
  IS_EXISTED  := 0;
  /* P724 : I/F 공통 취소검증 ( TCANCELDT <-> TPAORDERM ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT CD.ORDER_NO,
                 CD.ORDER_G_SEQ,
                 CD.ORDER_D_SEQ,
                 CD.ORDER_W_SEQ,
                 CD.CANCEL_QTY
            FROM TCANCELDT CD, TORDERM OM, TCUSTOMER CU, TMEDIA TM
           WHERE CD.CANCEL_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CD.MEDIA_GB = '03'
             AND CD.ORDER_NO = OM.ORDER_NO
             AND CD.CUST_NO = CU.CUST_NO
             AND CU.RECEIVE_METHOD IN
                 ('50', '55', '60', '61', '62', '63', '64', '65', '66', '67')
             AND CD.MEDIA_CODE = TM.MEDIA_CODE
             AND TM.MOB_POC_GB = '20'
             AND TM.MEDIA_CODE != 'EX14' -- 10/24 퀸잇제휴(DR-2310-0255)
             AND CD.GOODS_GB = '10'
             AND CD.ORDER_NO NOT IN ('20240229803896' -- 수기처리
                                     )
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.CREATE_YN = '1'
                     AND (PM.PA_ORDER_GB = '20' OR
                         (PM.PA_CODE IN ('51', '52') AND
                         PM.PA_ORDER_GB = '30' AND
                         INSTR(PM.API_RESULT_MESSAGE, '주문취소') > 0))
                     AND PM.ORDER_NO = CD.ORDER_NO
                     AND PM.ORDER_G_SEQ = CD.ORDER_G_SEQ
                     AND PM.ORDER_D_SEQ = CD.ORDER_D_SEQ
                     AND PM.ORDER_W_SEQ = CD.ORDER_W_SEQ
                     AND TO_NUMBER(PM.PA_PROC_QTY) = CD.CANCEL_QTY));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  -- END --

  V_FLAG      := 'P729';
  V_ERROR_MSG := '[즉시_P729] I/F 공통 반품 검증 ( TPAORDERM <-> TCLAIMDT )';
  IS_EXISTED  := 0;
  /* P729 : I/F 공통 반품 검증 ( TPAORDERM <-> TCLAIMDT ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.ORDER_NO,
                 PM.ORDER_G_SEQ,
                 PM.ORDER_D_SEQ,
                 PM.ORDER_W_SEQ,
                 TO_NUMBER(PM.PA_PROC_QTY)
            FROM TPAORDERM PM
           WHERE PM.CREATE_YN = '1'
             AND PM.CREATE_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND PM.PA_ORDER_GB = '30'
             AND (CASE
                   WHEN PM.PA_CODE IN ('51', '52') THEN
                    INSTR(PM.API_RESULT_MESSAGE, '주문취소')
                   ELSE
                    0
                 END) = 0
             AND NOT EXISTS
           (SELECT 1
                    FROM TCLAIMDT CL, TMEDIA TM
                   WHERE CL.CLAIM_GB = '30'
                     AND CL.MEDIA_GB = '03'
                     AND CL.MEDIA_CODE = TM.MEDIA_CODE
                     AND TM.MOB_POC_GB = '20'
                     AND CL.ORDER_NO = PM.ORDER_NO
                     AND CL.ORDER_G_SEQ = PM.ORDER_G_SEQ
                     AND CL.ORDER_D_SEQ = PM.ORDER_D_SEQ
                     AND CL.ORDER_W_SEQ = PM.ORDER_W_SEQ
                     AND CL.CLAIM_QTY = TO_NUMBER(PM.PA_PROC_QTY))
          
          )
   WHERE ORDER_NO NOT IN ('20200824603819',
                          '20200921839097',
                          '20201101376274' -- 11/5
                         ,
                          '20210305400459' -- 3/8
                          );

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  -- END --

  V_FLAG      := 'P730';
  V_ERROR_MSG := '[즉시_P730] I/F 공통 반품 검증 ( TCLAIMDT <-> TPAORDERM )';
  IS_EXISTED  := 0;
  /* P730 : I/F 공통 반품 검증 ( TCLAIMDT <-> TPAORDERM ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT CL.ORDER_NO,
                 CL.ORDER_G_SEQ,
                 CL.ORDER_D_SEQ,
                 CL.ORDER_W_SEQ,
                 CL.CLAIM_QTY
            FROM TCLAIMDT CL, TMEDIA TM
           WHERE CL.CLAIM_GB = '30'
             AND CL.MEDIA_GB = '03'
             AND CL.MEDIA_CODE = TM.MEDIA_CODE
             AND TM.MOB_POC_GB = '20'
             AND TM.MEDIA_CODE != 'EX14' -- 10/24 퀸잇제휴(DR-2310-0255)
             AND CL.GOODS_GB = '10'
             AND CL.ORDER_MEDIA = '62'
             AND CL.CLAIM_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CL.ORDER_NO NOT IN ('20220420919436' -- 4/25
                                    ,
                                     '20220627183638',
                                     '20220623687921',
                                     '20220625270718',
                                     '20220626811637',
                                     '20220626819450' -- 7/6 5개
                                    ,
                                     '20220917939642' -- 9/19
                                    ,
                                     '20220921421746' -- 9/28
                                    ,'20220921411969' -- 1/11
                                    ,'20230110096175' -- 1/27 하프클럽 교환배송취소
                                     )
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.CREATE_YN = '1'
                     AND (PM.PA_ORDER_GB = '30' OR
                         (PM.PA_ORDER_GB = '20' AND
                         PM.OUT_BEF_CLAIM_GB IN ('1', '2')))
                     AND PM.ORDER_NO = CL.ORDER_NO
                     AND PM.ORDER_G_SEQ = CL.ORDER_G_SEQ
                     AND PM.ORDER_D_SEQ = CL.ORDER_D_SEQ
                     AND PM.ORDER_W_SEQ = CL.ORDER_W_SEQ
                     AND TO_NUMBER(PM.PA_PROC_QTY) = CL.CLAIM_QTY));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  -- END --

  V_FLAG      := 'P731';
  V_ERROR_MSG := '[즉시_P731]I/F 공통 반품취소 검증 ( TPAORDERM <-> TCLAIMDT )';
  IS_EXISTED  := 0;
  /* P731 : I/F 공통 반품취소 검증 ( TPAORDERM <-> TCLAIMDT ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.ORDER_NO,
                 PM.ORDER_G_SEQ,
                 PM.ORDER_D_SEQ,
                 PM.ORDER_W_SEQ,
                 TO_NUMBER(PM.PA_PROC_QTY)
            FROM TPAORDERM PM
           WHERE PM.CREATE_YN = '1'
             AND PM.CREATE_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND PM.PA_ORDER_GB = '31'
             AND NOT EXISTS
           (SELECT 1
                    FROM TCLAIMDT CL, TMEDIA TM
                   WHERE CL.CLAIM_GB = '31'
                     AND CL.MEDIA_GB = '03'
                     AND CL.MEDIA_CODE = TM.MEDIA_CODE
                     AND TM.MOB_POC_GB = '20'
                     AND CL.ORDER_NO = PM.ORDER_NO
                     AND CL.ORDER_G_SEQ = PM.ORDER_G_SEQ
                     AND CL.ORDER_D_SEQ = PM.ORDER_D_SEQ
                     AND CL.ORDER_W_SEQ = PM.ORDER_W_SEQ
                     AND CL.CLAIM_QTY = TO_NUMBER(PM.PA_PROC_QTY))
             AND PM.ORDER_NO NOT IN ('20221111116954'));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  -- END --

  V_FLAG      := 'P732';
  V_ERROR_MSG := '[즉시_P732]I/F 공통 반품취소 검증 ( TCLAIMDT <-> TPAORDERM )';
  IS_EXISTED  := 0;

  /* P732 : I/F 공통 반품취소 검증 ( TCLAIMDT <-> TPAORDERM ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT CL.ORDER_NO,
                 CL.ORDER_G_SEQ,
                 CL.ORDER_D_SEQ,
                 CL.ORDER_W_SEQ,
                 CL.CLAIM_QTY
            FROM TCLAIMDT CL, TMEDIA TM
           WHERE CL.CLAIM_GB = '31'
             AND CL.MEDIA_GB = '03'
             AND CL.MEDIA_CODE = TM.MEDIA_CODE
             AND TM.MOB_POC_GB = '20'
             AND TM.MEDIA_CODE != 'EX14' -- 10/24 퀸잇제휴(DR-2310-0255)
             AND CL.ORDER_D_SEQ = '001' -- 본품인 경우만
             AND CL.CLAIM_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.CREATE_YN = '1'
                     AND PM.PA_ORDER_GB = '31'
                     AND PM.ORDER_NO = CL.ORDER_NO
                     AND PM.ORDER_G_SEQ = CL.ORDER_G_SEQ
                     AND PM.ORDER_D_SEQ = CL.ORDER_D_SEQ
                     AND PM.ORDER_W_SEQ = CL.ORDER_W_SEQ
                     AND TO_NUMBER(PM.PA_PROC_QTY) = CL.CLAIM_QTY)
             AND CL.ORDER_NO NOT IN ('20220921411969' -- 1/11
                                    ,'20220628387088' -- 1/31
                                    ,'20220302402863' -- 2/14
                                    ,'20220605592122' -- 2/14
                                    ,'20211106989977' -- 3/30
                                     ));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  -- END --
  ------------------------------------------------------------------------------------

  V_FLAG      := 'P741';
  V_ERROR_MSG := '[즉시_P741]I/F 공통 교환출고 검증 ( TPAORDERM <-> TCLAIMDT )';
  IS_EXISTED  := 0;
  /* P741 : I/F 공통 교환출고 검증 ( TPAORDERM <-> TCLAIMDT ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.ORDER_NO,
                 PM.ORDER_G_SEQ,
                 PM.ORDER_D_SEQ,
                 PM.ORDER_W_SEQ,
                 TO_NUMBER(PM.PA_PROC_QTY)
            FROM TPAORDERM PM
           WHERE PM.CREATE_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND PM.PA_ORDER_GB = '40'
             AND PM.CREATE_YN = '1'
             AND NOT EXISTS
           (SELECT 1
                    FROM TCLAIMDT CL, TMEDIA TM
                   WHERE CL.MEDIA_GB = '03'
                     AND CL.MEDIA_CODE = TM.MEDIA_CODE
                     AND TM.MOB_POC_GB = '20'
                     AND CL.CLAIM_GB = '40'
                     AND CL.ORDER_NO = PM.ORDER_NO
                     AND CL.ORDER_G_SEQ = PM.ORDER_G_SEQ
                     AND CL.ORDER_D_SEQ = PM.ORDER_D_SEQ
                     AND CL.ORDER_W_SEQ = PM.ORDER_W_SEQ
                     AND CL.CLAIM_QTY = TO_NUMBER(PM.PA_PROC_QTY)));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  -- END --

  V_FLAG      := 'P742';
  V_ERROR_MSG := '[즉시_P742]I/F 공통 교환배송 검증 ( TCLAIMDT <-> TPAORDERM )';
  IS_EXISTED  := 0;
  /* P742 : I/F 공통 교환배송 검증 ( TCLAIMDT <-> TPAORDERM ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT CL.ORDER_NO,
                 CL.ORDER_G_SEQ,
                 CL.ORDER_D_SEQ,
                 CL.ORDER_W_SEQ,
                 CL.CLAIM_QTY
            FROM TCLAIMDT CL, TMEDIA TM
           WHERE CL.MEDIA_GB = '03'
             AND CL.MEDIA_CODE = TM.MEDIA_CODE
             AND TM.MOB_POC_GB = '20'
             AND TM.MEDIA_CODE != 'EX14' -- 10/24 퀸잇제휴(DR-2310-0255)
             AND CL.CLAIM_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CL.CLAIM_GB = '40'
             AND CL.ORDER_NO NOT IN (  '20240408720316' --5/3
                                      ,'20240501703708' --5/3
                                      ,'20240319397630' -- 5/7
                                      ,'20240417579610' -- 5/9 
                                      ,'20240413345551' --5/13
                                      ,'20240518642580' --5/23
                                      ,'20240412990896' --5/24
                                     )
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '40'
                     AND PM.CREATE_YN = '1'
                     AND PM.ORDER_NO = CL.ORDER_NO
                     AND PM.ORDER_G_SEQ = CL.ORDER_G_SEQ
                     AND PM.ORDER_D_SEQ = CL.ORDER_D_SEQ
                     AND PM.ORDER_W_SEQ = CL.ORDER_W_SEQ
                     AND TO_NUMBER(PM.PA_PROC_QTY) = CL.CLAIM_QTY));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  -- END --

  V_FLAG      := 'P743';
  V_ERROR_MSG := '[즉시_P743]I/F 공통 교환회수 검증 ( TPAORDERM <-> TCLAIMDT )';
  IS_EXISTED  := 0;
  /* P743 : I/F 공통 교환회수 검증 ( TPAORDERM <-> TCLAIMDT ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.ORDER_NO,
                 PM.ORDER_G_SEQ,
                 PM.ORDER_D_SEQ,
                 PM.ORDER_W_SEQ,
                 TO_NUMBER(PM.PA_PROC_QTY)
            FROM TPAORDERM PM
           WHERE PM.CREATE_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND PM.PA_ORDER_GB = '45'
             AND PM.CREATE_YN = '1'
             AND NOT EXISTS
           (SELECT 1
                    FROM TCLAIMDT CL, TMEDIA TM
                   WHERE CL.MEDIA_GB = '03'
                     AND CL.MEDIA_CODE = TM.MEDIA_CODE
                     AND TM.MOB_POC_GB = '20'
                     AND CL.CLAIM_GB = '45'
                     AND CL.ORDER_NO = PM.ORDER_NO
                     AND CL.ORDER_G_SEQ = PM.ORDER_G_SEQ
                     AND CL.ORDER_D_SEQ = PM.ORDER_D_SEQ
                     AND CL.ORDER_W_SEQ = PM.ORDER_W_SEQ
                     AND CL.CLAIM_QTY = TO_NUMBER(PM.PA_PROC_QTY)));

  V_FLAG      := 'P744';
  V_ERROR_MSG := '[즉시_P744] I/F 공통 교환회수 검증 ( TCLAIMDT <-> TPAORDERM )';
  IS_EXISTED  := 0;
  /* [즉시_P744]I/F 공통 교환회수 검증 ( TCLAIMDT <-> TPAORDERM )*/
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT CL.ORDER_NO,
                 CL.ORDER_G_SEQ,
                 CL.ORDER_D_SEQ,
                 CL.ORDER_W_SEQ,
                 CL.CLAIM_QTY
            FROM TCLAIMDT CL, TMEDIA TM
           WHERE CL.MEDIA_GB = '03'
             AND CL.MEDIA_CODE = TM.MEDIA_CODE
             AND TM.MOB_POC_GB = '20'
             AND TM.MEDIA_CODE != 'EX14' -- 10/24 퀸잇제휴(DR-2310-0255)
             AND CL.CLAIM_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CL.CLAIM_GB = '45'
             AND CL.ORDER_NO NOT IN (  '20240408720316' --5/3
                                      ,'20240501703708' --5/3
                                      ,'20240319397630' -- 5/7
                                      ,'20240417579610' -- 5/9 
                                      ,'20240413345551' --5/13
                                      ,'20240518642580' --5/23
                                      ,'20240412990896' --5/24
                                     )
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '45'
                     AND PM.CREATE_YN = '1'
                     AND PM.ORDER_NO = CL.ORDER_NO
                     AND PM.ORDER_G_SEQ = CL.ORDER_G_SEQ
                     AND PM.ORDER_D_SEQ = CL.ORDER_D_SEQ
                     AND PM.ORDER_W_SEQ = CL.ORDER_W_SEQ
                     AND TO_NUMBER(PM.PA_PROC_QTY) = CL.CLAIM_QTY));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  -- END --

  V_FLAG      := 'P745';
  V_ERROR_MSG := '[즉시_P745] I/F 공통 교환출고 취소 검증 ( TPAORDERM <-> TCLAIMDT )';
  IS_EXISTED  := 0;
  /* [즉시_P745] I/F 공통 교환출고 취소 검증 ( TPAORDERM <-> TCLAIMDT )*/
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.ORDER_NO,
                 PM.ORDER_G_SEQ,
                 PM.ORDER_D_SEQ,
                 PM.ORDER_W_SEQ,
                 TO_NUMBER(PM.PA_PROC_QTY)
            FROM TPAORDERM PM
           WHERE PM.CREATE_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND PM.PA_ORDER_GB = '41'
             AND PM.CREATE_YN = '1'
             AND PM.ORDER_NO NOT IN ('20201127433155' -- 12/28
                                     )
             AND NOT EXISTS
           (SELECT 1
                    FROM TCLAIMDT CL, TMEDIA TM
                   WHERE CL.MEDIA_GB = '03'
                     AND CL.MEDIA_CODE = TM.MEDIA_CODE
                     AND TM.MOB_POC_GB = '20'
                     AND CL.CLAIM_GB = '41'
                     AND CL.ORDER_NO = PM.ORDER_NO
                     AND CL.ORDER_G_SEQ = PM.ORDER_G_SEQ
                     AND CL.ORDER_D_SEQ = PM.ORDER_D_SEQ
                     AND CL.ORDER_W_SEQ = PM.ORDER_W_SEQ
                     AND CL.CLAIM_QTY = TO_NUMBER(PM.PA_PROC_QTY)));

 DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  -- END --

  V_FLAG      := 'P746';
  V_ERROR_MSG := '[즉시_P746] I/F 공통 교환배송 취소 검증 ( TCLAIMDT <-> TPAORDERM )';
  IS_EXISTED  := 0;
  /* [즉시_P746] I/F 공통 교환배송 취소 검증 ( TCLAIMDT <-> TPAORDERM ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT CL.ORDER_NO,
                 CL.ORDER_G_SEQ,
                 CL.ORDER_D_SEQ,
                 CL.ORDER_W_SEQ,
                 CL.CLAIM_QTY
            FROM TCLAIMDT CL, TMEDIA TM
           WHERE CL.MEDIA_GB = '03'
             AND CL.MEDIA_CODE = TM.MEDIA_CODE
             AND TM.MOB_POC_GB = '20'
             AND TM.MEDIA_CODE != 'EX14' -- 10/24 퀸잇제휴(DR-2310-0255)
             AND CL.CLAIM_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CL.CLAIM_GB = '41'
             AND CL.ORDER_NO NOT IN ('20220730523404' -- 8/30
                                    ,
                                     '20220821273562' -- 8/31
                                    ,'20231008791601' -- 10//27
                                    ,'20240302040441' --24.3.29
                                     )
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '41'
                     AND PM.CREATE_YN = '1'
                     AND PM.ORDER_NO = CL.ORDER_NO
                     AND PM.ORDER_G_SEQ = CL.ORDER_G_SEQ
                     AND PM.ORDER_D_SEQ = CL.ORDER_D_SEQ
                     AND PM.ORDER_W_SEQ = CL.ORDER_W_SEQ
                     AND TO_NUMBER(PM.PA_PROC_QTY) = CL.CLAIM_QTY));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  -- END --

  V_FLAG      := 'P747';
  V_ERROR_MSG := '[즉시_P747] I/F 공통 교환회수 취소 검증 ( TPAORDERM <-> TCLAIMDT )';
  IS_EXISTED  := 0;
  /* [즉시_P747] I/F 공통 교환회수 취소 검증 ( TPAORDERM <-> TCLAIMDT ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.ORDER_NO,
                 PM.ORDER_G_SEQ,
                 PM.ORDER_D_SEQ,
                 PM.ORDER_W_SEQ,
                 TO_NUMBER(PM.PA_PROC_QTY)
            FROM TPAORDERM PM
           WHERE PM.CREATE_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND PM.PA_ORDER_GB = '46'
             AND PM.CREATE_YN = '1'
             AND PM.ORDER_NO NOT IN ('20201127433155' -- 12/28
                                     )
             AND NOT EXISTS
           (SELECT 1
                    FROM TCLAIMDT CL, TMEDIA TM
                   WHERE CL.MEDIA_GB = '03'
                     AND CL.MEDIA_CODE = TM.MEDIA_CODE
                     AND TM.MOB_POC_GB = '20'
                     AND CL.CLAIM_GB = '46'
                     AND CL.ORDER_NO = PM.ORDER_NO
                     AND CL.ORDER_G_SEQ = PM.ORDER_G_SEQ
                     AND CL.ORDER_D_SEQ = PM.ORDER_D_SEQ
                     AND CL.ORDER_W_SEQ = PM.ORDER_W_SEQ
                     AND CL.CLAIM_QTY = TO_NUMBER(PM.PA_PROC_QTY)));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  -- END --

  V_FLAG      := 'P748';
  V_ERROR_MSG := '[즉시_P748] I/F 공통 교환회수 취소 검증 ( TCLAIMDT <-> TPAORDERM )';
  IS_EXISTED  := 0;
  /* [즉시_P748] I/F 공통 교환회수 취소 검증 ( TCLAIMDT <-> TPAORDERM ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT CL.ORDER_NO,
                 CL.ORDER_G_SEQ,
                 CL.ORDER_D_SEQ,
                 CL.ORDER_W_SEQ,
                 CL.CLAIM_QTY
            FROM TCLAIMDT CL, TMEDIA TM
           WHERE CL.MEDIA_GB = '03'
             AND CL.MEDIA_CODE = TM.MEDIA_CODE
             AND TM.MOB_POC_GB = '20'
             AND TM.MEDIA_CODE != 'EX14' -- 10/24 퀸잇제휴(DR-2310-0255)
             AND CL.CLAIM_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CL.CLAIM_GB = '46'
             AND CL.ORDER_NO NOT IN ('20220730523404' -- 8/30
                                    ,
                                     '20220821273562' -- 8/31
                                    ,'20231008791601' -- 10//27
                                    ,'20240302040441' --24.3.29
                                     )
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '46'
                     AND PM.CREATE_YN = '1'
                     AND PM.ORDER_NO = CL.ORDER_NO
                     AND PM.ORDER_G_SEQ = CL.ORDER_G_SEQ
                     AND PM.ORDER_D_SEQ = CL.ORDER_D_SEQ
                     AND PM.ORDER_W_SEQ = CL.ORDER_W_SEQ
                     AND TO_NUMBER(PM.PA_PROC_QTY) = CL.CLAIM_QTY));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  -- END --

  V_FLAG      := 'P749';
  V_ERROR_MSG := '[확인_P749] 11번가 취소 미처리건 CHECK';
  IS_EXISTED  := 0;
  /* [확인_P749] 11번가 취소 미처리건 CHECK */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.PA_ORDER_NO,
                 TRUNC(TO_NUMBER(SYSDATE - PT.CREATE_DT) * 24) -
                 (FUN_GET_DELYDAY_CNT(NVL(SM.DELY_GB, '90'),
                                      TRUNC(PT.CREATE_DT),
                                      SYSDATE,
                                      '0') * 24) AS BUSINESS_HOUR
            FROM TPAORDERM PM, TPA11STORDERLIST PT, TSLIPM SM, TSLIPDT SD
           WHERE PM.PA_ORDER_NO = PT.ORD_NO
             AND PM.PA_ORDER_SEQ = PT.ORD_PRD_SEQ
             AND PM.PA_SHIP_NO = PT.DLV_NO
             AND PM.ORDER_NO = SD.ORDER_NO(+)
             AND PM.ORDER_G_SEQ = SD.ORDER_G_SEQ(+)
             AND PM.ORDER_D_SEQ = SD.ORDER_D_SEQ(+)
             AND PM.ORDER_W_SEQ = SD.ORDER_W_SEQ(+)
             AND SD.SLIP_I_NO = SM.SLIP_I_NO(+)
             AND PT.PA_ORDER_GB = '20'
             AND PM.PA_ORDER_GB = '10'
             AND PT.WITHDRAW_YN = '0'
             AND PT.PROC_FLAG NOT IN ('07', '10', '20')
             AND PM.PA_CODE IN ('11', '12')
             AND PM.CREATE_YN = '1'
             AND PT.CREATE_DT > TRUNC(SYSDATE) - 30
--             AND PM.PA_ORDER_NO NOT IN ('20220305386753846' --3/25 ( 취소승인처리 미노출 옵션코드보정 ) /* 필요없어진 데이터 GBJEONG 2023.05.08 */ )
             AND (TRUNC(TO_NUMBER(SYSDATE - PT.CREATE_DT) * 24) -
                 (FUN_GET_DELYDAY_CNT(NVL(SM.DELY_GB, '90'),
                                       TRUNC(PT.CREATE_DT),
                                       SYSDATE,
                                       '0') * 24)) > 66);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'P750';
  V_ERROR_MSG := '[확인_P750]11번가_주문 생성 전 취소건';
  IS_EXISTED  := 0;
  /* P750 : 11번가_주문 생성 전 취소건 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT OL.ORD_NO,
                 OL.ORD_PRD_SEQ,
                 OL.DLV_NO,
                 OL.ORD_DT,
                 OL.INSERT_DATE
            FROM TPA11STORDERLIST OL
           WHERE OL.INSERT_DATE BETWEEN TO_DATE(V_TODATE, 'yyyymmdd') - 2 AND
                 TO_DATE(V_TODATE, 'yyyymmdd')
             AND OL.PA_ORDER_GB = '10'
             AND NOT EXISTS (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_GROUP_CODE = '01'
                     AND PM.PA_ORDER_GB = '10'
                     AND PM.PA_ORDER_NO = OL.ORD_NO
                     AND ROWNUM = 1)
             AND OL.ORD_NO NOT IN ('20230109594931551' -- 1/9
                                  ,'20230223621029873'
                                  ,'20230223621029917' -- 2/24
                                  ,'20230305625500086' -- 3/6
                                  ,'20230420651707785' -- 4/20
                                  ,'20230511663162880' -- 5/12
                                  ,'20230531672093638' -- 5/31
                                  ,'20230901716004792' -- 9/1
                                   )
          
          );

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  --이거 나오면 P782도 동일하게 나와야됨. 문제 없으면 B781지우기
  /*   v_flag     := 'P781';v_error_msg := '[즉시_P781] 공통 할인금액 상이(DC_AMT <> PROMO_AMT)';
  is_existed := 0;
  \*[즉시_P781] 공통 할인금액 상이(DC_AMT <> PROMO_AMT)*\
  SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT P.ORDER_NO, DC_AMT
               , SUM(PROC_AMT) AS PROMO_AMT
            FROM TORDERPROMO P
               , (SELECT SUM(DC_AMT_GOODS) AS DC_AMT, ORDER_NO
                    FROM TORDERDT
                   WHERE MEDIA_CODE IN (SELECT TC.REMARK1 FROM TCODE TC WHERE TC.CODE_LGROUP = 'O500' AND TC.USE_YN = '1')
                     AND ORDER_DATE BETWEEN SYSDATE -3 AND SYSDATE
                   GROUP BY ORDER_NO) X
           WHERE X.ORDER_NO = P.ORDER_NO
             AND DO_TYPE IN ('90', '30', '70', '92')
             AND INSERT_DATE BETWEEN SYSDATE -3 AND SYSDATE
             AND P.ORDER_NO NOT IN('20210422640342') -- 딜상품
           GROUP BY P.ORDER_NO, DC_AMT) G
   WHERE DC_AMT <> PROMO_AMT
     AND EXISTS (SELECT 'x'
                   FROM TPAORDERM T
                  WHERE PA_ORDER_GB = '10'
                    AND T.ORDER_NO  = G.ORDER_NO
                    AND INSERT_DATE BETWEEN SYSDATE -3 AND SYSDATE);
  
  IF( is_existed > 0 ) THEN insert_error_log(); END IF;*/

  V_FLAG      := 'P782';
  V_ERROR_MSG := '[즉시_P782] 공통 할인금액 상이(DC_AMT <> PROMO_AMT) 수정중';
  IS_EXISTED  := 0;
  /*[즉시_P782] 공통 할인금액 상이(DC_AMT <> PROMO_AMT)*/
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT OD.ORDER_NO,
                 SUM(OD.DC_AMT_GOODS) AS DC_AMT,
                 (SELECT SUM(PROC_AMT)
                    FROM TORDERPROMO P
                   WHERE P.ORDER_NO = OD.ORDER_NO
                     AND P.DO_TYPE IN ('90', '30', '70', '92')
                   GROUP BY P.ORDER_NO) AS PROMO_AMT
            FROM TORDERDT OD, TMEDIA TM
           WHERE OD.ORDER_DATE > SYSDATE - 3
             AND OD.MEDIA_CODE = TM.MEDIA_CODE
             AND TM.MOB_POC_GB = '20'
             AND TM.MEDIA_CODE != 'EX14' -- 10/24 퀸잇제휴(DR-2310-0255)
             AND OD.ORDER_D_SEQ = '001'
             AND OD.ORDER_W_SEQ = '001'
             AND OD.ORDER_NO NOT IN ('20210422640342') --딜상품
           GROUP BY OD.ORDER_NO
          HAVING SUM(OD.DC_AMT_GOODS) <> (SELECT SUM(PROC_AMT)
                                           FROM TORDERPROMO P
                                          WHERE P.ORDER_NO = OD.ORDER_NO
                                            AND P.DO_TYPE IN
                                                ('90', '30', '70', '92')
                                          GROUP BY P.ORDER_NO)
           ORDER BY OD.ORDER_NO);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

/*
  V_FLAG      := 'P768';
  V_ERROR_MSG := '[즉시_P768] 11번가 주문단품 상이건 (TGOODSDT <-> TPA11STORDERLIST) CHECK';
  IS_EXISTED  := 0;
  --[즉시_P768] 11번가 주문단품 상이건 (TGOODSDT <-> TPA11STORDERLIST) CHECK
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT OD.ORDER_NO,
                 OL.ORD_NO,
                 OL.PRD_NO,
                 OL.PRD_STCK_NO,
                 OL.SLCT_PRD_OPT_NM,
                 OD.GOODS_CODE,
                 OD.GOODSDT_CODE,
                 OD.GOODSDT_INFO,
                 GT.GOODSDT_INFO,
                 SUBSTR2(OL.SLCT_PRD_OPT_NM, 5),
                 TRIM(GT.GOODSDT_INFO) || '-' || OD.ORDER_QTY || '개'
            FROM TPAORDERM PM
           INNER JOIN TPA11STORDERLIST OL
                   ON PM.PA_ORDER_GB = OL.PA_ORDER_GB
                  AND PM.PA_ORDER_NO = OL.ORD_NO
                  AND PM.PA_ORDER_SEQ = OL.ORD_PRD_SEQ
                  AND PM.PA_SHIP_NO = OL.DLV_NO
                  AND PM.PA_ORDER_GB = '10'
           INNER JOIN TORDERDT OD
                   ON PM.ORDER_NO = OD.ORDER_NO
                  AND PM.ORDER_G_SEQ = OD.ORDER_G_SEQ
                  AND OD.GOODS_GB = '10'
                  AND OD.ORDER_DATE >= SYSDATE - 30
           INNER JOIN TGOODSDT GT
                   ON OD.GOODS_CODE = GT.GOODS_CODE
             AND OD.GOODSDT_CODE = GT.GOODSDT_CODE
             AND SUBSTR2(OL.SLCT_PRD_OPT_NM, 5) <>
                 TRIM(GT.GOODSDT_INFO) || '-' || OD.ORDER_QTY || '개'
           ORDER BY OD.ORDER_NO DESC);
           */

  V_FLAG      := 'P773';
  V_ERROR_MSG := '[즉시_P773] 11번가 주문결제가(RSALE_AMT) 검증';
  IS_EXISTED  := 0;
  /* [즉시_P773] 11번가 주문결제가(RSALE_AMT) 검증 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT /*+FULL(PM)*/
                 OD.ORDER_NO,
                 OD.ORDER_G_SEQ,
                 OD.RSALE_AMT,
                 OL.ORD_PAY_AMT,
                 OD.ORDER_QTY,
                 OD.ORDER_DATE
            FROM TORDERDT OD
         INNER JOIN TPAORDERM PM
                 ON OD.ORDER_NO = PM.ORDER_NO
                 AND OD.ORDER_G_SEQ = PM.ORDER_G_SEQ
                 AND OD.ORDER_D_SEQ = PM.ORDER_D_SEQ
                 AND OD.ORDER_W_SEQ = PM.ORDER_W_SEQ
                 AND PM.PA_GROUP_CODE = '01'
                 AND PM.PA_CODE IN ('11','12')
         INNER JOIN TPA11STORDERLIST OL
                 ON PM.PA_ORDER_NO = OL.ORD_NO
                 AND PM.PA_ORDER_SEQ = OL.ORD_PRD_SEQ
                 AND OL.PA_ORDER_GB = '10'
                 AND OD.RSALE_AMT != (OL.ORD_PAY_AMT - OL.DLV_CST)
                 AND PM.CREATE_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
       WHERE OD.ORDER_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND OD.ORDER_NO NOT IN ('20211108862168' -- 11/9
                                    ,
                                     '20211108862659' -- 11/9
                                    ,
                                     '20211108862171' -- 11/9
                                    ,
                                     '20211108862660' -- 11/9
                                    ,
                                     '20211108862170' -- 11/9
                                    ,
                                     '20211108862169' -- 11/9
                                    ,
                                     '20211108862167' -- 11/9
                                    ,
                                     '20211108862162' -- 11/9
                                    ,
                                     '20211108862166' -- 11/9
                                    ,
                                     '20211108862173' -- 11/9
                                    ,
                                     '20211108862174' -- 11/9
                                    ,
                                     '20211108862163' -- 11/9
                                    ,'20221211449483' -- 12/14
                                    ,'20230109067765' -- 1/9
                                    ,'20230327731405' -- 3/30
                                    ,'20230910605278' -- 9/11
                                     ));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'P774';
  V_ERROR_MSG := '[즉시_P774] 11번가 주문 SK스토아할인금액 검증';
  IS_EXISTED  := 0;
  /* [즉시_P774] 11번가 주문 SK스토아할인금액 검증(TORDERDT.REMARK3_N <-> TPA11STORDERLIST.LST_SELLER_DSC_PRC <-> TORDERPROMO.PROC_AMT)  */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT /*+FULL(PM)*/
                 OD.ORDER_NO
               , OD.ORDER_G_SEQ
               , OD.REMARK3_N
               , OL.LST_SELLER_DSC_PRC
            FROM TORDERDT OD
               , TPAORDERM PM
               , TPA11STORDERLIST OL
           WHERE OD.ORDER_NO     = PM.ORDER_NO
             AND OD.ORDER_G_SEQ  = PM.ORDER_G_SEQ
             AND OD.ORDER_D_SEQ  = PM.ORDER_D_SEQ
             AND OD.ORDER_W_SEQ  = PM.ORDER_W_SEQ
             AND PM.PA_ORDER_NO  = OL.ORD_NO
             AND PM.PA_ORDER_SEQ = OL.ORD_PRD_SEQ
             AND PM.PA_CODE IN ('11','12')
             AND PM.PA_GROUP_CODE = '01'
             AND OL.PA_ORDER_GB   = '10'
             AND OD.MEDIA_CODE    = 'EX01'
             AND NVL(OL.ORD_OPT_WON_STL, 0) = 0
             AND (OD.REMARK3_N <> OL.LST_SELLER_DSC_PRC /*OR OL.LST_SELLER_DSC_PRC <> OP.PROC_AMT*/)
             AND OD.ORDER_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND OD.ORDER_NO NOT IN ('20220102939778' --1/3
                                    ,
                                     '20220104791914' --1/4
                                    ,
                                     '20220104798097' -- 1/5
                                     ));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'P767';
  V_ERROR_MSG := '[확인_P767] 제휴 TRECEIVER 전화번호 000000000000 건';
  IS_EXISTED  := 0;
  /* P767 : 제휴 TRECEIVER 전화번호 000000000000 건  */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT TR.CUST_NO,
                 LPAD(TR.RECEIVER_DDD || TR.RECEIVER_TEL1 ||
                      TR.RECEIVER_TEL2 || TR.RECEIVER_TEL3,
                      12,
                      0) AS TEL,
                 LPAD(TR.RECEIVER_HP1 || TR.RECEIVER_HP2 || TR.RECEIVER_HP3,
                      12,
                      0) AS HP
            FROM TPAORDERM POM, TORDERDT OD, TRECEIVER TR
           WHERE POM.ORDER_NO = OD.ORDER_NO
             AND POM.ORDER_G_SEQ = OD.ORDER_G_SEQ
             AND POM.ORDER_D_SEQ = OD.ORDER_D_SEQ
             AND POM.ORDER_W_SEQ = OD.ORDER_W_SEQ
             AND OD.CUST_NO = TR.CUST_NO
             AND POM.CREATE_YN = '1'
             AND POM.CREATE_DATE > SYSDATE - 3
             
             UNION
             
             SELECT TR.CUST_NO,
                 LPAD(TR.RECEIVER_DDD || TR.RECEIVER_TEL1 ||
                      TR.RECEIVER_TEL2 || TR.RECEIVER_TEL3,
                      12,
                      0) AS TEL,
                 LPAD(TR.RECEIVER_HP1 || TR.RECEIVER_HP2 || TR.RECEIVER_HP3,
                      12,
                      0) AS HP                  
            FROM TPAORDERM POM, TCLAIMDT OD, TRECEIVER TR
           WHERE POM.ORDER_NO = OD.ORDER_NO
             AND POM.ORDER_G_SEQ = OD.ORDER_G_SEQ
             AND POM.ORDER_D_SEQ = OD.ORDER_D_SEQ
             AND POM.ORDER_W_SEQ = OD.ORDER_W_SEQ
             AND OD.CUST_NO = TR.CUST_NO
             AND POM.CREATE_YN = '1'
             AND POM.CREATE_DATE > SYSDATE - 3
             )
   WHERE ((HP IS NULL AND TEL = '000000000000') OR
         (HP = '000000000000' AND TEL IS NULL) OR
         (HP = '000000000000' AND TEL = '000000000000') OR
         (HP IS NULL AND TEL IS NULL))
     AND CUST_NO NOT IN ('202310732414', -- 23.10.27 주문취소
                         '202301308632',
                         '202311305929', -- 23.11.24 주문취소
                         '202311305922',  -- 23.11.24 주문취소
                         '202209854350',
                         '202303888160',
                         '202312924960',
                         '202304956926',
                         '202312924960',  -- 23.12.08 주문취소
                         '202301518943',
                         '202211505909',
                         '202305306683', -- 24.01.19 과거 주문
                         '202307917722', -- 24.02.01 과거 주문
                         '202106296439' -- 24.03.06 과거 주문
                        ,'202304622597' -- 24.03.20 과거 주문
                         );

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'P769';
  V_ERROR_MSG := '[즉시_P769] EBAY 반품수거정보등록API 미처리 건';
  IS_EXISTED  := 0;
  -- P769 : EBAY 반품 교환 수거 정보등록API 미처리 건
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PO.PA_ORDER_NO, PO.PA_CODE, PO.PA_ORDER_SEQ, PO.PA_CLAIM_NO
            FROM TPAORDERM PO, TCLAIMDT CD, TORDERPROC OP
           WHERE PO.ORDER_NO = CD.ORDER_NO
             AND PO.ORDER_G_SEQ = CD.ORDER_G_SEQ
             AND PO.ORDER_D_SEQ = CD.ORDER_D_SEQ
             AND PO.ORDER_W_SEQ = CD.ORDER_W_SEQ
             AND CD.ORDER_NO = OP.ORDER_NO
             AND CD.ORDER_G_SEQ = OP.ORDER_G_SEQ
             AND CD.ORDER_D_SEQ = OP.ORDER_D_SEQ
             AND CD.ORDER_W_SEQ = OP.ORDER_W_SEQ
             AND CD.MEDIA_CODE IN (SELECT TC.REMARK1
                                     FROM TCODE TC
                                    WHERE TC.CODE_LGROUP = 'O500'
                                      AND TC.USE_YN = '1')
             AND PO.PA_CODE IN ('21', '22')
             AND PO.PA_ORDER_GB IN ('30', '45')
             AND PO.CREATE_YN = '1'
             AND PO.PRE_CANCEL_YN = '0'
             AND PO.PA_DO_FLAG < '50'
             AND PO.OUT_BEF_CLAIM_GB = '0'
             AND CD.DO_FLAG = '60'
                --AND CD.DO_FLAG >= '50'
             AND CD.SYSLAST > 0
             AND OP.DO_FLAG = '50'
             AND OP.PROC_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND OP.PROC_DATE < SYSDATE - INTERVAL '2' HOUR);
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'P771';
  V_ERROR_MSG := '[확인_P771] EBAY 교환배송완료 미처리 건';
  IS_EXISTED  := 0;
  -- P771 : EBAY 교환배송완료 미처리 건 *\
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PO.*
            FROM TPAORDERM PO, TCLAIMDT CD, TORDERPROC OP
           WHERE PO.ORDER_NO = CD.ORDER_NO
             AND PO.ORDER_G_SEQ = CD.ORDER_G_SEQ
             AND PO.ORDER_D_SEQ = CD.ORDER_D_SEQ
             AND PO.ORDER_W_SEQ = CD.ORDER_W_SEQ
             AND CD.ORDER_NO = OP.ORDER_NO
             AND CD.ORDER_G_SEQ = OP.ORDER_G_SEQ
             AND CD.ORDER_D_SEQ = OP.ORDER_D_SEQ
             AND CD.ORDER_W_SEQ = OP.ORDER_W_SEQ
             AND CD.MEDIA_CODE IN (SELECT TC.REMARK1
                                     FROM TCODE TC
                                    WHERE TC.CODE_LGROUP = 'O500'
                                      AND TC.USE_YN = '1')
             AND PO.PA_CODE IN ('21', '22')
             AND PO.PA_ORDER_GB = '40'
             AND PO.CREATE_YN = '1'
             AND PO.PRE_CANCEL_YN = '0'
             AND PO.PA_DO_FLAG <> '80'
             AND CD.DO_FLAG = '80'
             AND CD.SYSLAST > 0
             AND OP.DO_FLAG = '80'
             AND OP.PROC_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND OP.PROC_DATE < SYSDATE - INTERVAL '5' HOUR);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'P777';
  V_ERROR_MSG := '[즉시_777] EBAY 주문 RSALE_AMT 검증';
  IS_EXISTED  := 0;
  -- P777 : G마켓 주문 RSALE_AMT 검증
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT /*+leading(pm) use_nl(od ol pgp)*/
           OD.ORDER_NO,
           OD.ORDER_G_SEQ,
           OD.RSALE_AMT,
           PGP.SUPPLY_PRICE * OL.CONTR_QTY,
           OD.ORDER_QTY,
           OD.ORDER_DATE,
           OL.PAYMENT_PRICE - OL.SHIPPING_FEE,
           OD.RSALE_AMT,
           OD.DC_AMT,
           OL.PAYMENT_PRICE
            FROM TORDERDT         OD,
                 TPAORDERM        PM,
                 TPAGMKTORDERLIST OL,
                 TPAGOODSPRICE    PGP
           WHERE PM.PA_CODE IN ('21', '22')
             AND OD.ORDER_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND PM.CREATE_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND PM.ORDER_NO = OD.ORDER_NO
             AND PM.ORDER_G_SEQ = OD.ORDER_G_SEQ
             AND PM.ORDER_D_SEQ = OD.ORDER_D_SEQ
             AND PM.ORDER_W_SEQ = OD.ORDER_W_SEQ
             AND PM.PA_ORDER_NO = OL.PAY_NO
             AND PM.PA_ORDER_SEQ = OL.CONTR_NO
             AND OD.GOODS_CODE = PGP.GOODS_CODE
             AND PM.PA_CODE = PGP.PA_CODE
             AND PGP.ROWID =
                 (SELECT /*+ INDEX_DESC (PP PK_TPAGOODSPRICE)*/
                   PP.ROWID
                    FROM TPAGOODSPRICE PP
                   WHERE PP.GOODS_CODE = OL.OUT_GOODS_NO
                     AND PP.TRANS_DATE < NVL(OL.ORDER_DATE, OL.PAY_DATE)
                     AND PP.APPLY_DATE < NVL(OL.ORDER_DATE, OL.PAY_DATE)
                     AND PP.PA_CODE = PM.PA_CODE
                     AND ROWNUM = 1)
             AND OD.MEDIA_CODE IN (SELECT TC.REMARK1
                                     FROM TCODE TC
                                    WHERE TC.CODE_LGROUP = 'O500'
                                      AND TC.USE_YN = '1')
             AND (OD.RSALE_AMT <> OL.ACNT_PRICE - OL.SHIPPING_FEE OR
                 OD.RSALE_AMT + OD.DC_AMT <> PGP.SALE_PRICE * OD.ORDER_QTY)
             AND PM.ORDER_NO NOT IN ('20211202669867' -- 12/2
                                    ,
                                     '20211202671271' -- 12/2
                                    ,
                                     '20221007761125' -- 10/12
                                    ,
                                     '20221025369634' -- 10/25
                                    ,
                                     '20221101410181' -- 11/1
                                    ,'20230113626890'
                                    ,'20230113626895'
                                    ,'20230113637005'
                                    ,'20230113658394'
                                    ,'20230114137709' -- 1/16
                                    ,'20230720077953'
                                    ,'20230724676604'
                                    ,'20230724690914' -- 7/25
                                     )
                        );

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'P778';
  V_ERROR_MSG := '[즉시_P778] EBAY 주문 TV쇼핑할인금액 검증';
  IS_EXISTED  := 0;
  --[즉시_P778] EBAY 주문 TV쇼핑할인금액 검증 REMARK3N
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT /*+FULL(PM)*/
           OD.ORDER_NO,
           OD.ORDER_G_SEQ,
           OD.REMARK3_N,
           OP.PROC_AMT,
           OD.GOODS_CODE,
           PM.PA_ORDER_NO
            FROM TORDERDT         OD,
                 TPAORDERM        PM,
                 TPAGMKTORDERLIST OL,
                 TORDERPROMO      OP
           WHERE OD.ORDER_NO = PM.ORDER_NO
             AND OD.ORDER_G_SEQ = PM.ORDER_G_SEQ
             AND OD.ORDER_D_SEQ = PM.ORDER_D_SEQ
             AND OD.ORDER_W_SEQ = PM.ORDER_W_SEQ
             AND PM.PA_ORDER_NO = OL.PAY_NO
             AND PM.PA_ORDER_SEQ = OL.CONTR_NO
             AND OD.ORDER_NO = OP.ORDER_NO
             AND OD.ORDER_G_SEQ = OP.ORDER_G_SEQ
             AND OP.DO_TYPE = '90'
             AND OD.MEDIA_CODE = 'EX02'
             AND (OD.REMARK3_N <> OP.PROC_AMT)
             AND PM.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND OD.ORDER_DATE  >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND OD.ORDER_NO NOT IN
                 ('20210226347533', '20210226362236', '20210226346542'));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'P799';
  V_ERROR_MSG := '[알림_P799]제휴 주문/cs내역 조회';
  /* 제휴 주문/cs내역 조회 */
  SELECT '[제휴 주문/CS 내역]' || CHR(13) || CHR(10) || '제휴 주문' || ' : ' ||
         AA.ORDER_CNT || CHR(13) || CHR(10) || '제휴 취소' || ' : ' ||
         AA.CANCEL_CNT || CHR(13) || CHR(10) || '제휴 반품접수' || ' : ' ||
         AA.CLAIM_CNT || CHR(13) || CHR(10) || '제휴 반품취소' || ' : ' ||
         AA.CLAIM_CANCEL_CNT || CHR(13) || CHR(10) || '제휴 교환접수' || ' : ' ||
         AA.EXCHANGE_CNT || CHR(13) || CHR(10) || '제휴 교환취소' || ' : ' ||
         AA.EXCHANGE_CANCEL_CNT || CHR(13) || CHR(10) || '월별 11번가 주문' ||
         ' : ' || AA.ST_ORDER_CNT || CHR(13) || CHR(10) || '월별 11번가 취소' ||
         ' : ' || AA.ST_CANCEL_CNT || CHR(13) || CHR(10) || '월별 11번가 반품' ||
         ' : ' || AA.ST_RETURN_CNT || CHR(13) || CHR(10) || '월별 11번가 교환' ||
         ' : ' || AA.ST_EXCHANGE_CNT || CHR(13) || CHR(10) || '월별 G마켓 주문' ||
         ' : ' || AA.GMKT_ORDER_CNT || CHR(13) || CHR(10) || '월별 G마켓 취소' ||
         ' : ' || AA.GMKT_CANCEL_CNT || CHR(13) || CHR(10) || '월별 G마켓 반품' ||
         ' : ' || AA.GMKT_RETURN_CNT || CHR(13) || CHR(10) || '월별 G마켓 교환' ||
         ' : ' || AA.GMKT_EXCHANGE_CNT || CHR(13) || CHR(10) || '월별 옥션 주문' ||
         ' : ' || AA.AUC_ORDER_CNT || CHR(13) || CHR(10) || '월별 옥션 취소' ||
         ' : ' || AA.AUC_CANCEL_CNT || CHR(13) || CHR(10) || '월별 옥션 반품' ||
         ' : ' || AA.AUC_RETURN_CNT || CHR(13) || CHR(10) || '월별 옥션 교환' ||
         ' : ' || AA.AUC_EXCHANGE_CNT || CHR(13) || CHR(10) || '월별 네이버 주문' ||
         ' : ' || AA.N_ORDER_CNT || CHR(13) || CHR(10) || '월별 네이버 취소' ||
         ' : ' || AA.N_CANCEL_CNT || CHR(13) || CHR(10) || '월별 네이버 반품' ||
         ' : ' || AA.N_RETURN_CNT || CHR(13) || CHR(10) || '월별 네이버 교환' ||
         ' : ' || AA.N_EXCHANGE_CNT || CHR(13) || CHR(10) || '월별 쿠팡 주문' ||
         ' : ' || AA.COPN_ORDER_CNT || CHR(13) || CHR(10) || '월별 쿠팡 취소' ||
         ' : ' || AA.COPN_CANCEL_CNT || CHR(13) || CHR(10) || '월별 쿠팡 반품' ||
         ' : ' || AA.COPN_RETURN_CNT || CHR(13) || CHR(10) || '월별 쿠팡 교환' ||
         ' : ' || AA.COPN_EXCHANGE_CNT || CHR(13) || CHR(10) || '월별 위메프 주문' ||
         ' : ' || AA.WEMP_ORDER_CNT || CHR(13) || CHR(10) || '월별 위메프 취소' ||
         ' : ' || AA.WEMP_CANCEL_CNT || CHR(13) || CHR(10) || '월별 위메프 반품' ||
         ' : ' || AA.WEMP_RETURN_CNT || CHR(13) || CHR(10) || '월별 위메프 교환' ||
         ' : ' || AA.WEMP_EXCHANGE_CNT || CHR(13) || CHR(10) ||
         '월별 인터파크 주문' || ' : ' || AA.INTP_ORDER_CNT || CHR(13) || CHR(10) ||
         '월별 인터파크 취소' || ' : ' || AA.INTP_CANCEL_CNT || CHR(13) || CHR(10) ||
         '월별 인터파크 반품' || ' : ' || AA.INTP_RETURN_CNT || CHR(13) || CHR(10) ||
         '월별 인터파크 교환' || ' : ' || AA.INTP_EXCHANGE_CNT || CHR(13) ||
         CHR(10) || '월별 롯데ON 주문' || ' : ' || AA.LTON_ORDER_CNT || CHR(13) ||
         CHR(10) || '월별 롯데ON 취소' || ' : ' || AA.LTON_CANCEL_CNT || CHR(13) ||
         CHR(10) || '월별 롯데ON 반품' || ' : ' || AA.LTON_RETURN_CNT || CHR(13) ||
         CHR(10) || '월별 롯데ON 교환' || ' : ' || AA.LTON_EXCHANGE_CNT ||
         CHR(10) || '월별 티몬 주문' || ' : ' || AA.TMON_ORDER_CNT || CHR(13) ||
         CHR(10) || '월별 티몬 취소' || ' : ' || AA.TMON_CANCEL_CNT || CHR(13) ||
         CHR(10) || '월별 티몬 반품' || ' : ' || AA.TMON_RETURN_CNT || CHR(13) ||
         CHR(10) || '월별 티몬 교환' || ' : ' || AA.TMON_EXCHANGE_CNT || CHR(10) ||
         '월별 SSG 주문' || ' : ' || AA.SSG_ORDER_CNT || CHR(13) || CHR(10) ||
         '월별 SSG 취소' || ' : ' || AA.SSG_CANCEL_CNT || CHR(13) || CHR(10) ||
         '월별 SSG 반품' || ' : ' || AA.SSG_RETURN_CNT || CHR(13) || CHR(10) ||
         '월별 SSG 교환' || ' : ' || AA.SSG_EXCHANGE_CNT || CHR(13) || CHR(10) ||
         '월별 카카오 주문' || ' : ' || AA.KAKAO_ORDER_CNT || CHR(13) || CHR(10) ||
         '월별 카카오 취소' || ' : ' || AA.KAKAO_CANCEL_CNT || CHR(13) || CHR(10) ||
         '월별 카카오 반품' || ' : ' || AA.KAKAO_RETURN_CNT || CHR(13) || CHR(10) ||
         '월별 카카오 교환' || ' : ' || AA.KAKAO_EXCHANGE_CNT || CHR(13) ||
         CHR(10) || '월별 하프클럽 주문' || ' : ' || AA.HALF_ORDER_CNT || CHR(13) ||
         CHR(10) || '월별 하프클럽 취소' || ' : ' || AA.HALF_CANCEL_CNT || CHR(13) ||
         CHR(10) || '월별 하프클럽 반품' || ' : ' || AA.HALF_RETURN_CNT || CHR(13) ||
         CHR(10) || '월별 하프클럽 교환' || ' : ' || AA.HALF_EXCHANGE_CNT
  
    INTO V_ERROR_MSG
    FROM (SELECT SUM(ORDER_CNT) AS ORDER_CNT,
                 SUM(CANCEL_CNT) AS CANCEL_CNT,
                 SUM(CLAIM_CNT) AS CLAIM_CNT,
                 SUM(CLAIM_CANCEL_CNT) AS CLAIM_CANCEL_CNT,
                 SUM(EXCHANGE_CNT) AS EXCHANGE_CNT,
                 SUM(EXCHANGE_CANCEL_CNT) AS EXCHANGE_CANCEL_CNT,
                 SUM(ST_ORDER_CNT) AS ST_ORDER_CNT,
                 SUM(GMKT_ORDER_CNT) AS GMKT_ORDER_CNT,
                 SUM(AUC_ORDER_CNT) AS AUC_ORDER_CNT,
                 SUM(N_ORDER_CNT) AS N_ORDER_CNT,
                 SUM(COPN_ORDER_CNT) AS COPN_ORDER_CNT,
                 SUM(WEMP_ORDER_CNT) AS WEMP_ORDER_CNT,
                 SUM(INTP_ORDER_CNT) AS INTP_ORDER_CNT,
                 SUM(LTON_ORDER_CNT) AS LTON_ORDER_CNT,
                 SUM(TMON_ORDER_CNT) AS TMON_ORDER_CNT,
                 SUM(SSG_ORDER_CNT) AS SSG_ORDER_CNT,
                 SUM(KAKAO_ORDER_CNT) AS KAKAO_ORDER_CNT,
                 SUM(HALF_ORDER_CNT) AS HALF_ORDER_CNT,
                 SUM(ST_CANCEL_CNT) AS ST_CANCEL_CNT,
                 SUM(GMKT_CANCEL_CNT) AS GMKT_CANCEL_CNT,
                 SUM(AUC_CANCEL_CNT) AS AUC_CANCEL_CNT,
                 SUM(N_CANCEL_CNT) AS N_CANCEL_CNT,
                 SUM(COPN_CANCEL_CNT) AS COPN_CANCEL_CNT,
                 SUM(WEMP_CANCEL_CNT) AS WEMP_CANCEL_CNT,
                 SUM(INTP_CANCEL_CNT) AS INTP_CANCEL_CNT,
                 SUM(LTON_CANCEL_CNT) AS LTON_CANCEL_CNT,
                 SUM(TMON_CANCEL_CNT) AS TMON_CANCEL_CNT,
                 SUM(SSG_CANCEL_CNT) AS SSG_CANCEL_CNT,
                 SUM(KAKAO_CANCEL_CNT) AS KAKAO_CANCEL_CNT,
                 SUM(HALF_CANCEL_CNT) AS HALF_CANCEL_CNT,
                 SUM(ST_RETURN_CNT) AS ST_RETURN_CNT,
                 SUM(GMKT_RETURN_CNT) AS GMKT_RETURN_CNT,
                 SUM(AUC_RETURN_CNT) AS AUC_RETURN_CNT,
                 SUM(N_RETURN_CNT) AS N_RETURN_CNT,
                 SUM(COPN_RETURN_CNT) AS COPN_RETURN_CNT,
                 SUM(WEMP_RETURN_CNT) AS WEMP_RETURN_CNT,
                 SUM(INTP_RETURN_CNT) AS INTP_RETURN_CNT,
                 SUM(LTON_RETURN_CNT) AS LTON_RETURN_CNT,
                 SUM(TMON_RETURN_CNT) AS TMON_RETURN_CNT,
                 SUM(SSG_RETURN_CNT) AS SSG_RETURN_CNT,
                 SUM(KAKAO_RETURN_CNT) AS KAKAO_RETURN_CNT,
                 SUM(HALF_RETURN_CNT) AS HALF_RETURN_CNT,
                 SUM(ST_EXCHANGE_CNT) AS ST_EXCHANGE_CNT,
                 SUM(GMKT_EXCHANGE_CNT) AS GMKT_EXCHANGE_CNT,
                 SUM(AUC_EXCHANGE_CNT) AS AUC_EXCHANGE_CNT,
                 SUM(N_EXCHANGE_CNT) AS N_EXCHANGE_CNT,
                 SUM(COPN_EXCHANGE_CNT) AS COPN_EXCHANGE_CNT,
                 SUM(WEMP_EXCHANGE_CNT) AS WEMP_EXCHANGE_CNT,
                 SUM(INTP_EXCHANGE_CNT) AS INTP_EXCHANGE_CNT,
                 SUM(LTON_EXCHANGE_CNT) AS LTON_EXCHANGE_CNT,
                 SUM(TMON_EXCHANGE_CNT) AS TMON_EXCHANGE_CNT,
                 SUM(SSG_EXCHANGE_CNT) AS SSG_EXCHANGE_CNT,
                 SUM(KAKAO_EXCHANGE_CNT) AS KAKAO_EXCHANGE_CNT,
                 SUM(HALF_EXCHANGE_CNT) AS HALF_EXCHANGE_CNT
          
            FROM (SELECT (SELECT COUNT(1)
                            FROM TORDERDT OD, TMEDIA TM
                           WHERE OD.ORDER_DATE > TRUNC(SYSDATE, 'MM') + 1 / 24
                             AND OD.MEDIA_CODE = TM.MEDIA_CODE
                             AND TM.MOB_POC_GB = '20') AS ORDER_CNT,
                         (SELECT COUNT(1)
                            FROM TCANCELDT CD, TMEDIA TM
                           WHERE CD.CANCEL_DATE >
                                 TRUNC(SYSDATE, 'MM') + 1 / 24
                             AND CD.MEDIA_CODE = TM.MEDIA_CODE
                             AND TM.MOB_POC_GB = '20') AS CANCEL_CNT,
                         0 AS CLAIM_CNT,
                         0 AS CLAIM_CANCEL_CNT,
                         0 AS EXCHANGE_CNT,
                         0 AS EXCHANGE_CANCEL_CNT,
                         0 AS ST_ORDER_CNT,
                         0 AS GMKT_ORDER_CNT,
                         0 AS AUC_ORDER_CNT,
                         0 AS N_ORDER_CNT,
                         0 AS COPN_ORDER_CNT,
                         0 AS WEMP_ORDER_CNT,
                         0 AS INTP_ORDER_CNT,
                         0 AS LTON_ORDER_CNT,
                         0 AS TMON_ORDER_CNT,
                         0 AS SSG_ORDER_CNT,
                         0 AS KAKAO_ORDER_CNT,
                         0 AS HALF_ORDER_CNT,
                         0 AS ST_CANCEL_CNT,
                         0 AS GMKT_CANCEL_CNT,
                         0 AS AUC_CANCEL_CNT,
                         0 AS N_CANCEL_CNT,
                         0 AS COPN_CANCEL_CNT,
                         0 AS WEMP_CANCEL_CNT,
                         0 AS INTP_CANCEL_CNT,
                         0 AS LTON_CANCEL_CNT,
                         0 AS TMON_CANCEL_CNT,
                         0 AS SSG_CANCEL_CNT,
                         0 AS KAKAO_CANCEL_CNT,
                         0 AS HALF_CANCEL_CNT,
                         0 AS ST_RETURN_CNT,
                         0 AS GMKT_RETURN_CNT,
                         0 AS AUC_RETURN_CNT,
                         0 AS N_RETURN_CNT,
                         0 AS COPN_RETURN_CNT,
                         0 AS WEMP_RETURN_CNT,
                         0 AS INTP_RETURN_CNT,
                         0 AS LTON_RETURN_CNT,
                         0 AS TMON_RETURN_CNT,
                         0 AS SSG_RETURN_CNT,
                         0 AS KAKAO_RETURN_CNT,
                         0 AS HALF_RETURN_CNT,
                         0 AS ST_EXCHANGE_CNT,
                         0 AS GMKT_EXCHANGE_CNT,
                         0 AS AUC_EXCHANGE_CNT,
                         0 AS N_EXCHANGE_CNT,
                         0 AS COPN_EXCHANGE_CNT,
                         0 AS WEMP_EXCHANGE_CNT,
                         0 AS INTP_EXCHANGE_CNT,
                         0 AS LTON_EXCHANGE_CNT,
                         0 AS TMON_EXCHANGE_CNT,
                         0 AS SSG_EXCHANGE_CNT,
                         0 AS KAKAO_EXCHANGE_CNT,
                         0 AS HALF_EXCHANGE_CNT
                    FROM DUAL
                  
                  UNION ALL
                  
                  SELECT 0 AS ORDER_CNT,
                         0 AS CANCEL_CNT,
                         SUM(DECODE(CD.CLAIM_GB, '30', 1, 0)) AS CLAIM_CNT,
                         SUM(DECODE(CD.CLAIM_GB, '31', 1, 0)) AS CLAIM_CANCEL_CNT,
                         SUM(DECODE(CD.CLAIM_GB, '40', 1, 0)) AS EXCHANGE_CNT,
                         SUM(DECODE(CD.CLAIM_GB, '41', 1, 0)) AS EXCHANGE_CANCEL_CNT,
                         0 AS ST_ORDER_CNT,
                         0 AS GMKT_ORDER_CNT,
                         0 AS AUC_ORDER_CNT,
                         0 AS N_ORDER_CNT,
                         0 AS COPN_ORDER_CNT,
                         0 AS WEMP_ORDER_CNT,
                         0 AS INTP_ORDER_CNT,
                         0 AS LTON_ORDER_CNT,
                         0 AS TMON_ORDER_CNT,
                         0 AS SSG_ORDER_CNT,
                         0 AS KAKAO_ORDER_CNT,
                         0 AS HALF_ORDER_CNT,
                         0 AS ST_CANCEL_CNT,
                         0 AS GMKT_CANCEL_CNT,
                         0 AS AUC_CANCEL_CNT,
                         0 AS N_CANCEL_CNT,
                         0 AS COPN_CANCEL_CNT,
                         0 AS WEMP_CANCEL_CNT,
                         0 AS INTP_CANCEL_CNT,
                         0 AS LTON_CANCEL_CNT,
                         0 AS TMON_CANCEL_CNT,
                         0 AS SSG_CANCEL_CNT,
                         0 AS KAKAO_CANCEL_CNT,
                         0 AS HALF_CANCEL_CNT,
                         0 AS ST_RETURN_CNT,
                         0 AS GMKT_RETURN_CNT,
                         0 AS AUC_RETURN_CNT,
                         0 AS N_RETURN_CNT,
                         0 AS COPN_RETURN_CNT,
                         0 AS WEMP_RETURN_CNT,
                         0 AS INTP_RETURN_CNT,
                         0 AS LTON_RETURN_CNT,
                         0 AS TMON_RETURN_CNT,
                         0 AS SSG_RETURN_CNT,
                         0 AS KAKAO_RETURN_CNT,
                         0 AS HALF_RETURN_CNT,
                         0 AS ST_EXCHANGE_CNT,
                         0 AS GMKT_EXCHANGE_CNT,
                         0 AS AUC_EXCHANGE_CNT,
                         0 AS N_EXCHANGE_CNT,
                         0 AS COPN_EXCHANGE_CNT,
                         0 AS WEMP_EXCHANGE_CNT,
                         0 AS INTP_EXCHANGE_CNT,
                         0 AS LTON_EXCHANGE_CNT,
                         0 AS TMON_EXCHANGE_CNT,
                         0 AS SSG_EXCHANGE_CNT,
                         0 AS KAKAO_EXCHANGE_CNT,
                         0 AS HALF_EXCHANGE_CNT
                    FROM TCLAIMDT CD, TMEDIA TM
                   WHERE CD.MEDIA_CODE = TM.MEDIA_CODE
                     AND TM.MOB_POC_GB = '20'
                     AND CD.CLAIM_DATE > TRUNC(SYSDATE, 'MM') + 1 / 24
                  
                  UNION ALL
                  
                  SELECT 0 AS ORDER_CNT,
                         0 AS CANCEL_CNT,
                         0 AS CLAIM_CNT,
                         0 AS CLAIM_CANCEL_CNT,
                         0 AS EXCHANGE_CNT,
                         0 AS EXCHANGE_CANCEL_CNT,
                         SUM(DECODE(PM.PA_CODE, '11', 1, '12', 1, 0)) AS ST_ORDER_CNT,
                         SUM(DECODE(PM.PA_GROUP_CODE,
                                    '02',
                                    DECODE(PM.PA_CODE, '21', 1, '22', 1, 0),
                                    0)) AS GMKT_ORDER_CNT,
                         SUM(DECODE(PM.PA_GROUP_CODE,
                                    '03',
                                    DECODE(PM.PA_CODE, '21', 1, '22', 1, 0),
                                    0)) AS AUC_ORDER_CNT,
                         SUM(DECODE(PM.PA_CODE, '41', 1, 0)) AS N_ORDER_CNT,
                         SUM(DECODE(PM.PA_CODE, '51', 1, '52', 1, 0)) AS COPN_ORDER_CNT,
                         SUM(DECODE(PM.PA_CODE, '61', 1, '62', 1, 0)) AS WEMP_ORDER_CNT,
                         SUM(DECODE(PM.PA_CODE, '71', 1, '72', 1, 0)) AS INTP_ORDER_CNT,
                         SUM(DECODE(PM.PA_CODE, '81', 1, '82', 1, 0)) AS LTON_ORDER_CNT,
                         SUM(DECODE(PM.PA_CODE, '91', 1, '92', 1, 0)) AS TMON_ORDER_CNT,
                         SUM(DECODE(PM.PA_CODE, 'A1', 1, 'A2', 1, 0)) AS SSG_ORDER_CNT,
                         SUM(DECODE(PM.PA_CODE, 'B1', 1, 'B2', 1, 0)) AS KAKAO_ORDER_CNT,
                         SUM(DECODE(PM.PA_CODE, 'C1', 1, 'C2', 1, 0)) AS HALF_ORDER_CNT,
                         0 AS ST_CANCEL_CNT,
                         0 AS GMKT_CANCEL_CNT,
                         0 AS AUC_CANCEL_CNT,
                         0 AS N_CANCEL_CNT,
                         0 AS COPN_CANCEL_CNT,
                         0 AS WEMP_CANCEL_CNT,
                         0 AS INTP_CANCEL_CNT,
                         0 AS LTON_CANCEL_CNT,
                         0 AS TMON_CANCEL_CNT,
                         0 AS SSG_CANCEL_CNT,
                         0 AS KAKAO_CANCEL_CNT,
                         0 AS HALF_CANCEL_CNT,
                         0 AS ST_RETURN_CNT,
                         0 AS GMKT_RETURN_CNT,
                         0 AS AUC_RETURN_CNT,
                         0 AS N_RETURN_CNT,
                         0 AS COPN_RETURN_CNT,
                         0 AS WEMP_RETURN_CNT,
                         0 AS INTP_RETURN_CNT,
                         0 AS LTON_RETURN_CNT,
                         0 AS TMON_RETURN_CNT,
                         0 AS SSG_RETURN_CNT,
                         0 AS KAKAO_RETURN_CNT,
                         0 AS HALF_RETURN_CNT,
                         0 AS ST_EXCHANGE_CNT,
                         0 AS GMKT_EXCHANGE_CNT,
                         0 AS AUC_EXCHANGE_CNT,
                         0 AS N_EXCHANGE_CNT,
                         0 AS COPN_EXCHANGE_CNT,
                         0 AS WEMP_EXCHANGE_CNT,
                         0 AS INTP_EXCHANGE_CNT,
                         0 AS LTON_EXCHANGE_CNT,
                         0 AS TMON_EXCHANGE_CNT,
                         0 AS SSG_EXCHANGE_CNT,
                         0 AS KAKAO_EXCHANGE_CNT,
                         0 AS HALF_EXCHANGE_CNT
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '10'
                     AND PM.CREATE_YN = '1'
                     AND PM.PRE_CANCEL_YN = '0'
                     AND PM.INSERT_DATE > TRUNC(SYSDATE, 'MM') + 1 / 24
                     AND NOT EXISTS
                   (SELECT 'x'
                            FROM TPAORDERM PM2
                           WHERE PM2.PA_CODE = PM.PA_CODE
                             AND PM2.PA_ORDER_GB = CASE
                                   WHEN PM.PA_GROUP_CODE = '05' THEN
                                    '30'
                                   ELSE
                                    '20'
                                 END
                             AND 0 < CASE
                                   WHEN PM.PA_GROUP_CODE = '05' THEN
                                    INSTR(PM2.API_RESULT_MESSAGE, '주문취소')
                                   ELSE
                                    1
                                 END
                             AND PM.PA_ORDER_NO = PM2.PA_ORDER_NO
                             AND PM.PA_ORDER_SEQ = PM2.PA_ORDER_SEQ
                             AND PM2.CREATE_YN = '1'
                             AND PM2.PRE_CANCEL_YN = '0')
                  
                  UNION ALL
                  
                  SELECT 0 AS ORDER_CNT,
                         0 AS CANCEL_CNT,
                         0 AS CLAIM_CNT,
                         0 AS CLAIM_CANCEL_CNT,
                         0 AS EXCHANGE_CNT,
                         0 AS EXCHANGE_CANCEL_CNT,
                         0 AS ST_ORDER_CNT,
                         0 AS GMKT_ORDER_CNT,
                         0 AS AUC_ORDER_CNT,
                         0 AS N_ORDER_CNT,
                         0 AS COPN_ORDER_CNT,
                         0 AS WEMP_ORDER_CNT,
                         0 AS INTP_ORDER_CNT,
                         0 AS LTON_ORDER_CNT,
                         0 AS TMON_ORDER_CNT,
                         0 AS SSG_ORDER_CNT,
                         0 AS KAKAO_ORDER_CNT,
                         0 AS HALF_ORDER_CNT,
                         SUM(DECODE(PM.PA_CODE, '11', 1, '12', 1, 0)) AS ST_CANCEL_CNT,
                         SUM(DECODE(PM.PA_GROUP_CODE,
                                    '02',
                                    DECODE(PM.PA_CODE, '21', 1, '22', 1, 0),
                                    0)) AS GMKT_CANCEL_CNT,
                         SUM(DECODE(PM.PA_GROUP_CODE,
                                    '03',
                                    DECODE(PM.PA_CODE, '21', 1, '22', 1, 0),
                                    0)) AS AUC_CANCEL_CNT,
                         SUM(DECODE(PM.PA_CODE, '41', 1, 0)) AS N_CANCEL_CNT,
                         SUM(DECODE(PM.PA_CODE, '51', 1, '52', 1, 0)) AS COPN_CANCEL_CNT,
                         SUM(DECODE(PM.PA_CODE, '61', 1, '62', 1, 0)) AS WEMP_CANCEL_CNT,
                         SUM(DECODE(PM.PA_CODE, '71', 1, '72', 1, 0)) AS INTP_CANCEL_CNT,
                         SUM(DECODE(PM.PA_CODE, '81', 1, '82', 1, 0)) AS LTON_CANCEL_CNT,
                         SUM(DECODE(PM.PA_CODE, '91', 1, '92', 1, 0)) AS TMON_CANCEL_CNT,
                         SUM(DECODE(PM.PA_CODE, 'A1', 1, 'A2', 1, 0)) AS SSG_CANCEL_CNT,
                         SUM(DECODE(PM.PA_CODE, 'B1', 1, 'B2', 1, 0)) AS KAKAO_CANCEL_CNT,
                         SUM(DECODE(PM.PA_CODE, 'C1', 1, 'C2', 1, 0)) AS HALF_CANCEL_CNT,
                         0 AS ST_RETURN_CNT,
                         0 AS GMKT_RETURN_CNT,
                         0 AS AUC_RETURN_CNT,
                         0 AS N_RETURN_CNT,
                         0 AS COPN_RETURN_CNT,
                         0 AS WEMP_RETURN_CNT,
                         0 AS INTP_RETURN_CNT,
                         0 AS LTON_RETURN_CNT,
                         0 AS TMON_RETURN_CNT,
                         0 AS SSG_RETURN_CNT,
                         0 AS KAKAO_RETURN_CNT,
                         0 AS HALF_RETURN_CNT,
                         0 AS ST_EXCHANGE_CNT,
                         0 AS GMKT_EXCHANGE_CNT,
                         0 AS AUC_EXCHANGE_CNT,
                         0 AS N_EXCHANGE_CNT,
                         0 AS COPN_EXCHANGE_CNT,
                         0 AS WEMP_EXCHANGE_CNT,
                         0 AS INTP_EXCHANGE_CNT,
                         0 AS LTON_EXCHANGE_CNT,
                         0 AS TMON_EXCHANGE_CNT,
                         0 AS SSG_EXCHANGE_CNT,
                         0 AS KAKAO_EXCHANGE_CNT,
                         0 AS HALF_EXCHANGE_CNT
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = CASE
                           WHEN PM.PA_GROUP_CODE = '05' THEN
                            '30'
                           ELSE
                            '20'
                         END
                     AND 0 < CASE
                           WHEN PM.PA_GROUP_CODE = '05' THEN
                            INSTR(PM.API_RESULT_MESSAGE, '주문취소')
                           ELSE
                            1
                         END
                     AND PM.INSERT_DATE > TRUNC(SYSDATE, 'MM') + 1 / 24
                     AND PM.CREATE_YN = '1'
                     AND PM.PRE_CANCEL_YN = '0'
                  
                  UNION ALL
                  
                  SELECT 0 AS ORDER_CNT,
                         0 AS CANCEL_CNT,
                         0 AS CLAIM_CNT,
                         0 AS CLAIM_CANCEL_CNT,
                         0 AS EXCHANGE_CNT,
                         0 AS EXCHANGE_CANCEL_CNT,
                         0 AS ST_ORDER_CNT,
                         0 AS GMKT_ORDER_CNT,
                         0 AS AUC_ORDER_CNT,
                         0 AS N_ORDER_CNT,
                         0 AS COPN_ORDER_CNT,
                         0 AS WEMP_ORDER_CNT,
                         0 AS INTP_ORDER_CNT,
                         0 AS LTON_ORDER_CNT,
                         0 AS TMON_ORDER_CNT,
                         0 AS SSG_ORDER_CNT,
                         0 AS KAKAO_ORDER_CNT,
                         0 AS HALF_ORDER_CNT,
                         0 AS ST_CANCEL_CNT,
                         0 AS GMKT_CANCEL_CNT,
                         0 AS AUC_CANCEL_CNT,
                         0 AS N_CANCEL_CNT,
                         0 AS COPN_CANCEL_CNT,
                         0 AS WEMP_CANCEL_CNT,
                         0 AS INTP_CANCEL_CNT,
                         0 AS LTON_CANCEL_CNT,
                         0 AS TMON_CANCEL_CNT,
                         0 AS SSG_CANCEL_CNT,
                         0 AS KAKAO_CANCEL_CNT,
                         0 AS HALF_CANCEL_CNT,
                         SUM(DECODE(PM.PA_CODE, '11', 1, '12', 1, 0)) AS ST_RETURN_CNT,
                         SUM(DECODE(PM.PA_GROUP_CODE,
                                    '02',
                                    DECODE(PM.PA_CODE, '21', 1, '22', 1, 0),
                                    0)) AS GMKT_RETURN_CNT,
                         SUM(DECODE(PM.PA_GROUP_CODE,
                                    '03',
                                    DECODE(PM.PA_CODE, '21', 1, '22', 1, 0),
                                    0)) AS AUC_RETURN_CNT,
                         SUM(DECODE(PM.PA_CODE, '41', 1, 0)) AS N_RETURN_CNT,
                         SUM(DECODE(PM.PA_CODE, '51', 1, '52', 1, 0)) AS COPN_RETURN_CNT,
                         SUM(DECODE(PM.PA_CODE, '61', 1, '62', 1, 0)) AS WEMP_RETURN_CNT,
                         SUM(DECODE(PM.PA_CODE, '71', 1, '72', 1, 0)) AS INTP_RETURN_CNT,
                         SUM(DECODE(PM.PA_CODE, '81', 1, '82', 1, 0)) AS LTON_RETURN_CNT,
                         SUM(DECODE(PM.PA_CODE, '91', 1, '92', 1, 0)) AS TMON_RETURN_CNT,
                         SUM(DECODE(PM.PA_CODE, 'A1', 1, 'A2', 1, 0)) AS SSG_RETURN_CNT,
                         SUM(DECODE(PM.PA_CODE, 'B1', 1, 'B2', 1, 0)) AS KAKAO_RETURN_CNT,
                         SUM(DECODE(PM.PA_CODE, 'C1', 1, 'C2', 1, 0)) AS HALF_RETURN_CNT,
                         0 AS ST_EXCHANGE_CNT,
                         0 AS GMKT_EXCHANGE_CNT,
                         0 AS AUC_EXCHANGE_CNT,
                         0 AS N_EXCHANGE_CNT,
                         0 AS COPN_EXCHANGE_CNT,
                         0 AS WEMP_EXCHANGE_CNT,
                         0 AS INTP_EXCHANGE_CNT,
                         0 AS LTON_EXCHANGE_CNT,
                         0 AS TMON_EXCHANGE_CNT,
                         0 AS SSG_EXCHANGE_CNT,
                         0 AS KAKAO_EXCHANGE_CNT,
                         0 AS HALF_EXCHANGE_CNT
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '30'
                     AND PM.CREATE_YN = '1'
                     AND PM.PRE_CANCEL_YN = '0'
                     AND PM.INSERT_DATE > TRUNC(SYSDATE, 'MM') + 1 / 24
                     AND 0 = CASE
                           WHEN PM.PA_GROUP_CODE = '05' THEN
                            INSTR(PM.API_RESULT_MESSAGE, '주문취소')
                           ELSE
                            0
                         END
                     AND NOT EXISTS
                   (SELECT 'X'
                            FROM TPAORDERM PM2
                           WHERE PM2.PA_ORDER_GB = '31'
                             AND PM2.PA_CODE = PM.PA_CODE
                             AND PM.PA_ORDER_NO = PM2.PA_ORDER_NO
                             AND PM.PA_ORDER_SEQ = PM2.PA_ORDER_SEQ
                             AND PM.PA_CLAIM_NO = PM2.PA_CLAIM_NO
                             AND PM2.CREATE_YN = '1'
                             AND PM2.PRE_CANCEL_YN = '0')
                  
                  UNION ALL
                  
                  SELECT 0 AS ORDER_CNT,
                         0 AS CANCEL_CNT,
                         0 AS CLAIM_CNT,
                         0 AS CLAIM_CANCEL_CNT,
                         0 AS EXCHANGE_CNT,
                         0 AS EXCHANGE_CANCEL_CNT,
                         0 AS ST_ORDER_CNT,
                         0 AS GMKT_ORDER_CNT,
                         0 AS AUC_ORDER_CNT,
                         0 AS N_ORDER_CNT,
                         0 AS COPN_ORDER_CNT,
                         0 AS WEMP_ORDER_CNT,
                         0 AS INTP_ORDER_CNT,
                         0 AS LTON_ORDER_CNT,
                         0 AS TMON_ORDER_CNT,
                         0 AS SSG_ORDER_CNT,
                         0 AS KAKAO_ORDER_CNT,
                         0 AS HALF_ORDER_CNT,
                         0 AS ST_CANCEL_CNT,
                         0 AS GMKT_CANCEL_CNT,
                         0 AS AUC_CANCEL_CNT,
                         0 AS N_CANCEL_CNT,
                         0 AS COPN_CANCEL_CNT,
                         0 AS WEMP_CANCEL_CNT,
                         0 AS INTP_CANCEL_CNT,
                         0 AS LTON_CANCEL_CNT,
                         0 AS TMON_CANCEL_CNT,
                         0 AS SSG_CANCEL_CNT,
                         0 AS KAKAO_CANCEL_CNT,
                         0 AS HALF_CANCEL_CNT,
                         0 AS ST_RETURN_CNT,
                         0 AS GMKT_RETURN_CNT,
                         0 AS AUC_RETURN_CNT,
                         0 AS N_RETURN_CNT,
                         0 AS COPN_RETURN_CNT,
                         0 AS WEMP_RETURN_CNT,
                         0 AS INTP_RETURN_CNT,
                         0 AS LTON_RETURN_CNT,
                         0 AS TMON_RETURN_CNT,
                         0 AS SSG_RETURN_CNT,
                         0 AS KAKAO_RETURN_CNT,
                         0 AS HALF_RETURN_CNT,
                         SUM(DECODE(PM.PA_CODE, '11', 1, '12', 1, 0)) AS ST_EXCHANGE_CNT,
                         SUM(DECODE(PM.PA_GROUP_CODE,
                                    '02',
                                    DECODE(PM.PA_CODE, '21', 1, '22', 1, 0),
                                    0)) AS GMKT_EXCHANGE_CNT,
                         SUM(DECODE(PM.PA_GROUP_CODE,
                                    '03',
                                    DECODE(PM.PA_CODE, '21', 1, '22', 1, 0),
                                    0)) AS AUC_EXCHANGE_CNT,
                         SUM(DECODE(PM.PA_CODE, '41', 1, 0)) AS N_EXCHANGE_CNT,
                         SUM(DECODE(PM.PA_CODE, '51', 1, '52', 1, 0)) AS COPN_EXCHANGE_CNT,
                         SUM(DECODE(PM.PA_CODE, '61', 1, '62', 1, 0)) AS WEMP_EXCHANGE_CNT,
                         SUM(DECODE(PM.PA_CODE, '71', 1, '72', 1, 0)) AS INTP_EXCHANGE_CNT,
                         SUM(DECODE(PM.PA_CODE, '81', 1, '82', 1, 0)) AS LTON_EXCHANGE_CNT,
                         SUM(DECODE(PM.PA_CODE, '91', 1, '92', 1, 0)) AS TMON_EXCHANGE_CNT,
                         SUM(DECODE(PM.PA_CODE, 'A1', 1, 'A2', 1, 0)) AS SSG_EXCHANGE_CNT,
                         SUM(DECODE(PM.PA_CODE, 'B1', 1, 'B2', 1, 0)) AS KAKAO_EXCHANGE_CNT,
                         SUM(DECODE(PM.PA_CODE, 'C1', 1, 'C2', 1, 0)) AS HALF_EXCHANGE_CNT
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '40'
                     AND PM.INSERT_DATE > TRUNC(SYSDATE, 'MM') + 1 / 24
                     AND PM.CREATE_YN = '1'
                     AND PM.PRE_CANCEL_YN = '0'
                     AND NOT EXISTS
                   (SELECT 'X'
                            FROM TPAORDERM PM2
                           WHERE PM2.PA_ORDER_GB = '41'
                             AND PM2.PA_CODE = PM.PA_CODE
                             AND PM.PA_ORDER_NO = PM2.PA_ORDER_NO
                             AND PM.PA_ORDER_SEQ = PM2.PA_ORDER_SEQ
                             AND PM.PA_CLAIM_NO = PM2.PA_CLAIM_NO
                             AND PM2.CREATE_YN = '1'))) AA;
  INSERT_PROC_E_LOG();
  INSERT_ERROR_LOG();

  /* 네이버 스마트스토어 주문 검증 시작 */
  V_FLAG      := 'PN01';
  V_ERROR_MSG := '[PN01] 네이버 결제가 금액검증 (TPANAVERORDERLIST <-> TORDERDT <-> TGOODSPRICE )';
  IS_EXISTED  := 0;

  /* PN01 : 네이버 결제가 금액검증 (TPANAVERORDERLIST <-> TORDERDT <-> TGOODSPRICE ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT /*+FULL(PM)*/OL.TOTAL_PRODUCT_AMT AS PANAVER_SALE_PRICE,
                 OD.SALE_PRICE * ORDER_QTY AS TORDERDT_SALE_PRICE,
                 FUN_GET_GOODS_PRICE(OD.GOODS_CODE, PGP.TRANS_DATE, '6') *
                 OD.ORDER_QTY AS CALC_FUN_GET_GOODS_PRICE,
                 PGP.SALE_PRICE * ORDER_QTY AS TPAGOODSPRICE_SALE_PRICE
            FROM TPAORDERM         PM,
                 TPANAVERORDERLIST OL,
                 TPANAVERORDERM    OM,
                 TORDERDT          OD,
                 TPAGOODSPRICE     PGP
           WHERE PM.PA_ORDER_GB = OL.PA_ORDER_GB
             AND PM.PA_ORDER_NO = OL.ORDER_ID
             AND PM.PA_ORDER_SEQ = OL.PRODUCT_ORDER_ID
             AND OL.ORDER_ID = OM.ORDER_ID
             AND PM.ORDER_NO = OD.ORDER_NO
             AND PM.ORDER_G_SEQ = OD.ORDER_G_SEQ
             AND PM.ORDER_D_SEQ = OD.ORDER_D_SEQ
             AND PM.ORDER_W_SEQ = OD.ORDER_W_SEQ
             AND OD.GOODS_CODE = PGP.GOODS_CODE
             AND PGP.PA_CODE = PM.PA_CODE
             AND PM.PA_ORDER_GB = '10'
             AND PM.CREATE_YN = '1'
             AND PM.PA_ORDER_NO NOT IN
                 ('2021051114736211', '2021050189156931')
             AND PGP.ROWID = (SELECT /*+ INDEX_DESC(I PK_TPAGOODSPRICE) */
                               ROWID
                                FROM TPAGOODSPRICE I
                               WHERE I.GOODS_CODE = PGP.GOODS_CODE
                                 AND I.PA_CODE = PM.PA_CODE
                                 AND I.TRANS_DATE <= OM.ORDER_DATE
                                 AND I.TRANS_DATE IS NOT NULL
                                 AND I.SALE_PRICE = OL.UNIT_PRICE
                                 AND ROWNUM = 1)
             AND PM.CREATE_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS'))
   WHERE (PANAVER_SALE_PRICE <> TORDERDT_SALE_PRICE OR
         TORDERDT_SALE_PRICE <> CALC_FUN_GET_GOODS_PRICE OR
         CALC_FUN_GET_GOODS_PRICE <> TPAGOODSPRICE_SALE_PRICE);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'PN13';
  V_ERROR_MSG := '[PN13] 네이버 교환배송 검증 ( TPAORDERM <-> TPANAVERCLAIMLIST )';
  IS_EXISTED  := 0;
  /* PN13 : 네이버 교환배송 검증 ( TPAORDERM <-> TPANAVERCLAIMLIST ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.PA_ORDER_GB,
                 PM.PA_ORDER_NO,
                 PM.PA_ORDER_SEQ,
                 PM.PA_CLAIM_NO,
                 PM.PA_PROC_QTY
            FROM TPAORDERM PM
           WHERE PM.PA_ORDER_GB = '40'
             AND PM.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND PM.PA_CODE = '41'
             AND NOT EXISTS
           (SELECT 1
                    FROM TPANAVERCLAIMLIST CL
                   WHERE CL.CLAIM_STATUS LIKE '2%'
                     AND CL.CLAIM_STATUS != '25'
                     AND CL.PRODUCT_ORDER_ID = PM.PA_ORDER_SEQ
                     AND CL.INSERT_DATE = PM.INSERT_DATE));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'PN18';
  V_ERROR_MSG := '[PN18] 네이버 취소 미처리건 CHECK ';
  IS_EXISTED  := 0;
  /* PN18 네이버 취소 미처리건 CHECK */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.PA_ORDER_NO,
                 TRUNC(TO_NUMBER(SYSDATE - OM.ORDER_DATE) * 24) -
                 (FUN_GET_DELYDAY_CNT(NVL(SM.DELY_GB, '90'),
                                      TRUNC(OM.ORDER_DATE),
                                      SYSDATE,
                                      '0') * 24) AS BUSINESS_HOUR
            FROM TPAORDERM         PM,
                 TPANAVERORDERLIST PT,
                 TPANAVERORDERM    OM,
                 TPANAVERCLAIMLIST NC,
                 TSLIPM            SM,
                 TSLIPDT           SD
           WHERE PM.PA_ORDER_NO = PT.ORDER_ID
             AND PM.PA_ORDER_SEQ = PT.PRODUCT_ORDER_ID
             AND PT.ORDER_ID = OM.ORDER_ID
             AND PM.PA_ORDER_SEQ = NC.PRODUCT_ORDER_ID
             AND PM.PA_CLAIM_NO = PT.CLAIM_ID
             AND PM.ORDER_NO = SD.ORDER_NO(+)
             AND PM.ORDER_G_SEQ = SD.ORDER_G_SEQ(+)
             AND PM.ORDER_D_SEQ = SD.ORDER_D_SEQ(+)
             AND PM.ORDER_W_SEQ = SD.ORDER_W_SEQ(+)
             AND SD.SLIP_I_NO = SM.SLIP_I_NO(+)
             AND PM.PA_ORDER_GB = '10'
             AND NC.CLAIM_STATUS IN ('00', '01', '40')
             AND PM.PA_CODE = '41'
             AND PT.PROC_FLAG <> '07'
             AND PM.CREATE_YN = '1'
             AND PM.CREATE_DATE > TRUNC(SYSDATE) - 90
             AND (TRUNC(TO_NUMBER(SYSDATE - OM.ORDER_DATE) * 24) -
                 (FUN_GET_DELYDAY_CNT(NVL(SM.DELY_GB, '90'),
                                       TRUNC(OM.ORDER_DATE),
                                       SYSDATE,
                                       '0') * 24)) > 66
             AND NOT EXISTS
           (SELECT 1
                    FROM TPANAVERCLAIMLIST CL
                   WHERE CL.CLAIM_STATUS IN ('02', '03', '41')
                     AND CL.PRODUCT_ORDER_ID = PM.PA_ORDER_SEQ)
             AND PM.PA_ORDER_NO NOT IN ('2022042855325441' -- 5/9
                                       ,
                                        '2022050479816861' -- 5/9
                                       ,
                                        '2022051277939321' -- 5/17
                                       ,
                                        '2022051419916131' -- 5/19
                                        ));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'PN20';
  V_ERROR_MSG := '[PN20] 네이버 주문단품 상이건 (TGOODSDT <-> TPANAVERORDERLIST) CHECK';
  IS_EXISTED  := 0;
  /*[PN20] 네이버 주문단품 상이건 (TGOODSDT <-> TPANAVERORDERLIST) CHECK*/
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT /*+FULL(PM)*/
                 OD.ORDER_NO,
                 OL.ORDER_ID,
                 OL.PRODUCT_ORDER_ID,
                 OL.OPTION_CODE,
                 OL.PRODUCT_OPTION,
                 OD.GOODS_CODE,
                 OD.GOODSDT_CODE,
                 OD.GOODSDT_INFO,
                 GT.GOODSDT_INFO,
                 SUBSTR2(OL.PRODUCT_OPTION, 5) || '-' ||
                 TO_CHAR(OL.QUANTITY) || '개',
                 TRIM(GT.GOODSDT_INFO) || '-' || OD.ORDER_QTY || '개'
            FROM TPAORDERM         PM,
                 TPANAVERORDERLIST OL,
                 TORDERDT          OD,
                 TGOODSDT          GT
           WHERE PM.PA_ORDER_GB = OL.PA_ORDER_GB
             AND PM.PA_ORDER_NO = OL.ORDER_ID
             AND PM.PA_ORDER_SEQ = OL.PRODUCT_ORDER_ID
             AND PM.PA_ORDER_GB = '10'
             AND PM.ORDER_NO = OD.ORDER_NO
             AND PM.ORDER_G_SEQ = OD.ORDER_G_SEQ
             AND OD.GOODS_GB = '10'
             AND OD.MEDIA_CODE = 'EX04'
             AND OD.GOODS_CODE = GT.GOODS_CODE
             AND OD.GOODSDT_CODE = GT.GOODSDT_CODE
             AND REPLACE(SUBSTR2(OL.PRODUCT_OPTION, 5),'X','*') || '-' ||
                 TO_CHAR(OL.QUANTITY) || '개' <>
                 REPLACE(TRIM(GT.GOODSDT_INFO),'X','*') || '-' || OD.ORDER_QTY || '개' -- 23.08.29 'X' -> '*' 치환 후 비교
             AND OD.GOODSDT_CODE <> '001'
             AND GT.MODIFY_DATE < OD.ORDER_DATE
             AND OD.ORDER_DATE >= SYSDATE - 5
             AND PM.PA_ORDER_NO NOT IN ('2023082880818001' -- 8/29
                                       ,'2023082987192871' -- 8/29
                                        )
           ORDER BY OD.ORDER_NO DESC);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'PN21';
  V_ERROR_MSG := '[PN21] 네이버 주문결제가(RSALE_AMT) 검증 ';
  IS_EXISTED  := 0;
  /* [PN21] 네이버 주문결제가(RSALE_AMT) 검증 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT 
                 OD.ORDER_NO,
                 OD.ORDER_G_SEQ,
                 OD.RSALE_AMT,
                 OL.TOTAL_PAYMENT_AMT,
                 OD.ORDER_QTY,
                 OD.ORDER_DATE
            FROM TORDERDT          OD,
                 TPAORDERM         PM,
                 TPANAVERORDERLIST OL,
                 TPAGOODSPRICE     PGP
           WHERE OD.ORDER_NO = PM.ORDER_NO
             AND OD.ORDER_G_SEQ = PM.ORDER_G_SEQ
             AND OD.ORDER_D_SEQ = PM.ORDER_D_SEQ
             AND OD.ORDER_W_SEQ = PM.ORDER_W_SEQ
             AND PM.PA_ORDER_NO = OL.ORDER_ID
             AND PM.PA_ORDER_SEQ = OL.PRODUCT_ORDER_ID
             AND OD.GOODS_CODE = PGP.GOODS_CODE
             AND PM.PA_CODE = PGP.PA_CODE
             AND OL.PA_ORDER_GB = '10'
             AND PM.PA_ORDER_GB = '10'
             AND OD.RSALE_AMT != OL.TOTAL_PAYMENT_AMT
             AND OD.MEDIA_CODE = 'EX04'
             AND OD.ORDER_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS'));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'PN22';
  V_ERROR_MSG := '[PN22] 네이버 주문 SK스토아할인금액 검증(TORDERDT.REMARK3_N <-> TPANAVERORDERLIST.SELLER_DISCOUNT_AMT <-> TORDERPROMO.PROC_AMT)  ';
  IS_EXISTED  := 0;
  /* [PN22] 네이버 주문 SK스토아할인금액 검증(TORDERDT.REMARK3_N <-> TPANAVERORDERLIST.SELLER_DISCOUNT_AMT <-> TORDERPROMO.PROC_AMT)  */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT /*+FULL(PM)*/
           OD.ORDER_NO, OD.ORDER_G_SEQ, OD.REMARK3_N, OL.SELLER_DISCOUNT_AMT
            FROM TORDERDT          OD,
                 TPAORDERM         PM,
                 TPANAVERORDERLIST OL,
                 TPAGOODSPRICE     PGP
           WHERE OD.ORDER_NO = PM.ORDER_NO
             AND OD.ORDER_G_SEQ = PM.ORDER_G_SEQ
             AND OD.ORDER_D_SEQ = PM.ORDER_D_SEQ
             AND OD.ORDER_W_SEQ = PM.ORDER_W_SEQ
             AND PM.PA_ORDER_NO = OL.ORDER_ID
             AND PM.PA_ORDER_SEQ = OL.PRODUCT_ORDER_ID
             AND OD.GOODS_CODE = PGP.GOODS_CODE
             AND PM.PA_CODE = PGP.PA_CODE
             AND OL.PA_ORDER_GB = '10'
             AND OD.MEDIA_CODE = 'EX04'
             AND (OD.REMARK3_N <> OL.SELLER_DISCOUNT_AMT OR
                 OL.SELLER_DISCOUNT_AMT <>
                 (SELECT SUM(OP.PROC_AMT)
                     FROM TORDERPROMO OP
                    WHERE OD.ORDER_NO = OP.ORDER_NO
                      AND OD.ORDER_G_SEQ = OP.ORDER_G_SEQ
                      AND OP.DO_TYPE IN ('30', '70', '90')))
             AND OD.ORDER_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS'));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'PN24';
  V_ERROR_MSG := '[PN24] 네이버 발주 이후 주문 미생성 검증 ';
  IS_EXISTED  := 0;
  /* [PN24] 네이버 발주 이후 주문 미생성 검증 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT OL.PRODUCT_ORDER_ID
            FROM TORDERDT OD, TPAORDERM PM, TPANAVERORDERLIST OL
           WHERE OD.ORDER_NO = PM.ORDER_NO
             AND OD.ORDER_G_SEQ = PM.ORDER_G_SEQ
             AND OD.ORDER_D_SEQ = PM.ORDER_D_SEQ
             AND OD.ORDER_W_SEQ = PM.ORDER_W_SEQ
             AND PM.PA_ORDER_NO = OL.ORDER_ID
             AND PM.PA_ORDER_SEQ = OL.PRODUCT_ORDER_ID
             AND PM.PA_ORDER_GB = OL.PA_ORDER_GB
             AND OL.PA_ORDER_GB = '10'
             AND OD.MEDIA_CODE = 'EX04'
             AND PM.PA_GROUP_CODE = '04'
             AND OL.PLACE_ORDER_DATE IS NOT NULL
             AND OL.PLACE_ORDER_STATUS = '10'
             AND PM.CREATE_YN = '0'
             AND OD.ORDER_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS'));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  -- END --

  V_FLAG      := 'PN25';
  V_ERROR_MSG := '[PN25] 네이버 TPANAVERORDERLIST.PA_OPTION_CODE 점검 ';
  IS_EXISTED  := 0;

  /* [PN25] 네이버 TPANAVERORDERLIST.PA_OPTION_CODE 점검 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT A.PRODUCT_ORDER_ID, A.ORDER_ID, A.SELLER_PRODUCT_CODE
            FROM TPANAVERORDERLIST A, TPAORDERM C, TPAGOODSDTMAPPING D
           WHERE NOT EXISTS
           (SELECT 1
                    FROM TPAGOODSDTMAPPING B
                   WHERE A.SELLER_PRODUCT_CODE = B.GOODS_CODE
                     AND B.PA_CODE = '41'
                     AND A.ITEM_NO = B.PA_OPTION_CODE)
             AND A.INSERT_DATE > TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND A.PRODUCT_ORDER_ID = C.PA_ORDER_SEQ
             AND A.SELLER_PRODUCT_CODE = D.GOODS_CODE
             AND D.PA_CODE = '41'
             AND D.GOODSDT_CODE = A.OPTION_MANAGE_CODE
             AND A.INSERT_DATE > D.LAST_SYNC_DATE
             AND C.PA_ORDER_GB = '10'
             AND C.CREATE_YN = '1'
             AND C.PA_ORDER_NO NOT IN ('2022042288474221' -- 4/25
                                       )
          
          );

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  /*
    V_FLAG      := 'PN26';
    V_ERROR_MSG := '[PN26] 발송지연 미처리건 점검';
    IS_EXISTED  := 0;
    \* [PN26] 발송지연 미처리건 점검 *\
    SELECT CASE
             WHEN COUNT(1) > 0 THEN
              1
             ELSE
              0
           END DATA_CHECK
      INTO IS_EXISTED
      FROM (SELECT B.PA_DO_FLAG, A.ORDER_ID, A.PRODUCT_ORDER_ID
              FROM TPANAVERORDERLIST A, TPAORDERM B
             WHERE A.PRODUCT_ORDER_ID = B.PA_ORDER_SEQ
               AND A.ORDER_ID = B.PA_ORDER_NO
               AND A.SHIPPING_DUE_DATE <= SYSDATE
               AND A.CLAIM_TYPE IS NULL
               AND B.REMARK1_V IS NULL
               AND B.PA_CODE = '41'
               AND B.PA_DO_FLAG < '40'
               AND B.PRE_CANCEL_YN = '0'
               AND NOT EXISTS
             (SELECT 1
                      FROM TPAORDERM C
                     WHERE C.PA_ORDER_NO = A.ORDER_ID
                       AND C.PA_ORDER_SEQ = A.PRODUCT_ORDER_ID
                       AND C.PA_CODE = '41'
                       AND C.PA_ORDER_GB > '10')
               AND A.ORDER_ID NOT IN (
                                      '2022042729500381' -- 5/3 송장 있는 출하지시
                                    , '2022051266507031' -- 5/18 송장 있는 출하지시
                                     )
       );
    IF (IS_EXISTED > 0) THEN
      INSERT_ERROR_LOG();
    END IF;
  */
  V_FLAG      := 'PN27';
  V_ERROR_MSG := '[PN27] TPAORDERM CREATE_YN=0, PRE_CANCEL_YN=0, OUT_BEF_CLAIM=0, CHANGE_FLAG=00 건 조회';
  IS_EXISTED  := 0;
  /* [PN27] TPAORDERM CREATE_YN=0, PRE_CANCEL_YN=0, OUT_BEF_CLAIM=0, CHANGE_FLAG!=00 건 조회 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT T.PA_ORDER_NO, T.PA_ORDER_SEQ, T.ORDER_NO
            FROM TPAORDERM T
           WHERE T.CREATE_YN = '0'
             AND T.PRE_CANCEL_YN = '0'
             AND T.OUT_BEF_CLAIM_GB = '0'
             AND T.CHANGE_FLAG != '00'
             AND T.CREATE_DATE > TRUNC(SYSDATE) - 90
             AND PA_CODE = '41');

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'PN28';
  V_ERROR_MSG := '[PN28] 교환 보류로 인해 진행 불가한 건 조회';
  IS_EXISTED  := 0;
  /* [PN28] 교환 보류로 인해 진행 불가한 건 조회 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT O.ORDER_ID, O.PRODUCT_ORDER_ID
            FROM TPANAVERCLAIMLIST T
           INNER JOIN TPANAVERORDERLIST O
              ON O.PRODUCT_ORDER_ID = T.PRODUCT_ORDER_ID
           WHERE T.HOLDBACK_STATUS = '10'
             AND T.CLAIM_STATUS = '20'
             AND T.DELIVERY_FEE_PAY_METHOD != '미청구(반품안심케어 대상)'
             AND NOT EXISTS
           (SELECT 1
                    FROM TPANAVERCLAIMLIST C
                   WHERE C.HOLDBACK_STATUS = '20'
                     AND C.PRODUCT_ORDER_ID = T.PRODUCT_ORDER_ID
                     AND T.CLAIM_SEQ < C.CLAIM_SEQ)
             AND T.PRODUCT_ORDER_ID NOT IN ('2022072958228181' -- 8/3
                                           ,'2023083016907531' -- 9/5 교환철회/반품접수
                                            )
             AND T.INSERT_DATE > TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS'));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'PN29';
  V_ERROR_MSG := '[PN29] 네이버 미반영건 하루 이상 처리 되지 않은 주문';
  IS_EXISTED  := 0;
  /* [PN29] 네이버 미반영건 하루 이상 처리 되지 않은 주문 */
  SELECT CASE
             WHEN COUNT(1) > 0 THEN
               1
             ELSE
               0
          END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT  X.*
            FROM ( SELECT NC.ORDER_ID
                    , NC.PRODUCT_ORDER_ID
                    , TC1.CODE_NAME AS LAST_CHANGED_TYPE
                    , NC.PAYMENT_DATE
                    , NC.LAST_CHANGED_DATE
                    , NC.PRODUCT_ORDER_STATUS
                    , TC2.CODE_NAME AS CLAIM_TYPE
                    , TC3.CODE_NAME AS CLAIM_STATUS
                    , NC.IS_RECEIVER_ADDRESS_CHANGED AS RECEIVER_ADDRESS_CHANGED
                    , NC.GIFT_RECEIVING_STATUS
                    , NC.CHANGE_APPLIED_YN
                    , ROW_NUMBER() OVER (PARTITION BY NC.PRODUCT_ORDER_ID ORDER BY NC.LAST_CHANGED_DATE DESC) AS RN
                 FROM TPANAVERORDERCHANGE NC
                    , TCODE TC1
                    , TCODE TC2
                    , TCODE TC3
                WHERE TC1.CODE_LGROUP = 'O610'
                  AND TC2.CODE_LGROUP(+) = 'O612'
                  AND TC3.CODE_LGROUP(+) = 'O613'
                  AND NC.LAST_CHANGED_STATUS = TC1.CODE_MGROUP
                  AND NC.CLAIM_TYPE = TC2.CODE_MGROUP(+)
                  AND NC.CLAIM_STATUS = TC3.CODE_MGROUP(+)
                 ) X
          WHERE X.CHANGE_APPLIED_YN = '0'
            AND X.RN = 1
            AND X.LAST_CHANGED_DATE < SYSDATE -1
            );

  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  --쿠팡 START
  V_FLAG      := 'PC01';
  V_ERROR_MSG := '[PC01] SK스토아에서는 배송완료 되었지만 쿠팡은 처리되지 않은 건 조회';
  IS_EXISTED  := 0;
  /* [PC01] 송장없이 배송완료 되어 쿠팡과 SK스토아 주문상태가 상의한 케이스 체크여부 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT A.PA_CODE, A.PA_ORDER_NO, B.ORDER_NO
            FROM TPAORDERM A, TORDERDT B
           WHERE A.PA_CODE IN ('51', '52')
             AND A.ORDER_NO = B.ORDER_NO
             AND A.ORDER_G_SEQ = B.ORDER_G_SEQ
             AND A.ORDER_D_SEQ = B.ORDER_D_SEQ
             AND A.ORDER_W_SEQ = B.ORDER_W_SEQ
             AND B.DO_FLAG = 80
             AND A.PA_DO_FLAG < 40
             AND A.INSERT_DATE > SYSDATE - 10
             AND B.ORDER_NO NOT IN ('20200705202715' -- 7/9
                                   ,
                                    '20200716153151' -- 7/20
                                   ,
                                    '4000070064734' -- 11/25
                                   ,
                                    '20200317213986' -- 11/27
                                   ,
                                    '26000091822021' -- 02/01
                                    )
             AND NOT EXISTS (SELECT 1
                    FROM TPAORDERM AA
                   WHERE AA.PA_CODE = A.PA_CODE
                     AND AA.PA_ORDER_GB = '30'
                     AND AA.PA_ORDER_NO = A.PA_ORDER_NO
                     AND AA.PA_SHIP_NO = A.PA_SHIP_NO
                     AND AA.PA_ORDER_SEQ = A.PA_ORDER_SEQ
                     AND OUT_BEF_CLAIM_GB = '1'));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'PC02';
  V_ERROR_MSG := '[PC02] 장기미배송 주문건 수동처리 필요한 주문';
  IS_EXISTED  := 0;
  /* [PC02] 장기미배송 처리 안되고 있는 데이터
     쿠팡 배송관리에서 배송지시/배송중으로 조회 후 업체직송으로 변경해준 다음
     TSLIPM의 CREATE_DATE 가 60일이 지나지 않았을 경우에는 자동 처리 됨.
     지났을 경우 로컬에서 날짜 변경 후 수동으로 돌려주면됨.(장기미배송배송완료처리)
  */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.MAPPING_SEQ,
                 PM.PA_ORDER_NO,
                 PM.PA_SHIP_NO,
                 SM.SLIP_NO,
                 PM.PA_CODE
            FROM TPAORDERM PM, TSLIPDT SD, TSLIPM SM, TPADELYGB GB, TCODE CD
           WHERE PM.ORDER_NO = SD.ORDER_NO
             AND PM.ORDER_G_SEQ = SD.ORDER_G_SEQ
             AND SD.SLIP_I_NO = SM.SLIP_I_NO
             AND SM.DELY_GB = GB.DELY_GB
             AND GB.DELY_GB = CD.CODE_MGROUP
             AND PM.PA_CODE IN ('51', '52')
             AND PM.PA_ORDER_GB = '10'
             AND PM.PA_DO_FLAG = '40'
             AND PM.PA_PROC_QTY > 0
             AND NVL(PM.API_RESULT_MESSAGE, ' ') NOT LIKE
                 'ALREADY_CANCEL_RETURN%'
             AND NVL(PM.API_RESULT_MESSAGE, ' ') NOT LIKE '%NONE_TRACKING%'
             AND SM.SLIP_PROC = '80'
             AND GB.PA_DELY_GB != 'DIRECT'
             AND GB.PA_CODE = '05'
             AND CD.CODE_LGROUP = 'B005'
             AND TRUNC(SYSDATE) > SM.CREATE_DATE + 55
             AND PM.PA_ORDER_NO NOT IN ('14000081118231',
                                        '8000083273709' -- 12/21 배송중
                                       ,
                                        '14000085166251' -- 12/31 반품완료
                                       ,
                                        '14000094184245' -- 4/14 반품완료
                                       ,
                                        '28000129222206' -- 8/25 쿠팡 반품상태 (???)
                                        ));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  --위메프 START
  V_FLAG      := 'WE01';
  V_ERROR_MSG := '[WE01] 부분출고 불가능한 케이스';
  IS_EXISTED  := 0;
  /* [WE01] 위메프/티몬 상태는 전부 출고 / SK스토아는 일부만 출고상태 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PA_ORDER_GB, PA_SHIP_NO
            FROM (SELECT POM.PA_ORDER_GB, POM.PA_SHIP_NO, POM.PA_DO_FLAG
                    FROM TPAORDERM POM
                   WHERE POM.PA_CODE IN ('61', '62', '91', '92')
                     AND POM.PA_ORDER_GB = '10'
                     AND POM.CREATE_YN = '1'
                     AND POM.PA_DO_FLAG IN ('30', '40')
                     AND POM.INSERT_DATE > TRUNC(SYSDATE) - 10
                     AND NOT EXISTS
                   (SELECT 1
                            FROM TPAORDERM PCM
                           WHERE POM.PA_ORDER_NO = PCM.PA_ORDER_NO
                             AND POM.PA_SHIP_NO = PCM.PA_SHIP_NO
                             AND POM.PA_ORDER_SEQ = PCM.PA_ORDER_SEQ
                             AND POM.PA_CODE = PCM.PA_CODE
                             AND PCM.PA_ORDER_GB = '20')
                   GROUP BY POM.PA_ORDER_GB, POM.PA_SHIP_NO, POM.PA_DO_FLAG
                  UNION ALL
                  SELECT POM.PA_ORDER_GB, POM.PA_CLAIM_NO, POM.PA_DO_FLAG
                    FROM TPAORDERM POM
                   WHERE POM.PA_CODE IN ('61', '62', '91', '92')
                     AND POM.PA_ORDER_GB IN ('30', '45')
                     AND POM.INSERT_DATE > TRUNC(SYSDATE) - 10
                   GROUP BY POM.PA_ORDER_GB, POM.PA_CLAIM_NO, POM.PA_DO_FLAG)
          --반품처리완료.
           WHERE PA_SHIP_NO NOT IN ('367160366' -- 8/4 주문생성전 취소
                                   ,'G1332233878' -- 8/31 주문생성전 취소
                                   ,'G1333997038' -- 9/4 주문생성전 취소
                                   ,'G1334099150' -- 9/5 주문생성전 취소
                                   ,'G1359564950' -- 10/30 주문생성전 취소
                                   ,'G1361546230' -- 11/2 주문생성전 취소
                                    )
           GROUP BY PA_ORDER_GB, PA_SHIP_NO
          HAVING COUNT(1) > 1);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  --하프클럽 START
  V_FLAG      := 'PH01';
  V_ERROR_MSG := '[PH01] 하프클럽 제휴즉시할인쿠폰 검출';
  IS_EXISTED  := 0;

  /*[PH01]하프클럽 제휴즉시할인쿠폰 검출 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT OP.PROMO_NO,
                 OP.DO_TYPE,
                 OD.ORDER_NO,
                 OD.ORDER_G_SEQ,
                 OP.PROC_AMT,
                 OP.CANCEL_AMT,
                 OP.CALIM_AMT,
                 OP.PROC_COST
            FROM TORDERPROMO OP, TORDERDT OD
           WHERE OP.ORDER_NO = OD.ORDER_NO
             AND OP.ORDER_G_SEQ = OD.ORDER_G_SEQ
             AND OP.DO_TYPE = '30'
             AND OP.PROMO_NO = '299912369999'
             AND OD.MEDIA_CODE = 'EX13');

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  

  V_FLAG      := 'PH03';
  V_ERROR_MSG := '[즉시_PH03] SK스토아는 교환 출하지시됐지만 TPAHALFORDERLIST.EXCHANGE_ORD_NO가 없는 건';
  IS_EXISTED  := 0;

  --[즉시_PH03] SK스토아는 교환 출하지시됐지만 TPAHALFORDERLIST.EXCHANGE_ORD_NO가 없는 건
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.PA_CODE, PM.ORDER_NO, PM.PA_ORDER_NO
            FROM TPAORDERM PM, TCLAIMDT CL, TPAHALFORDERLIST PO
           WHERE PM.PA_CODE IN ('C1', 'C2')
             AND PM.ORDER_NO = CL.ORDER_NO
             AND PM.ORDER_D_SEQ = CL.ORDER_D_SEQ
             AND PM.ORDER_G_SEQ = CL.ORDER_G_SEQ
             AND PM.ORDER_W_SEQ = CL.ORDER_W_SEQ
             AND PO.ORD_NO = PM.PA_ORDER_NO
             AND PO.ORD_NO_NM = PM.PA_ORDER_SEQ
             AND PO.BASKET_NO = PA_SHIP_NO
             AND PO.CLAIM_NO = PM.PA_CLAIM_NO
             AND PM.PRE_CANCEL_YN != '1'
             AND PM.PA_ORDER_GB = '40'
             AND PM.PA_DO_FLAG < '40'
             AND CL.CLAIM_GB = '40'
             AND CL.DO_FLAG >= '30'
             AND PO.EXCHANGE_ORD_NO IS NULL);
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'PH04';
  V_ERROR_MSG := '[참고_PH04] 하프클럽 도서산간/제주배송불가 주문 인입';
  IS_EXISTED  := 0;

  --[참고_PH04] 하프클럽 도서산간/제주배송불가 주문 인입
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT /*+FULL(PM)*/DISTINCT PM.PA_ORDER_NO, PM.ORDER_NO
            FROM TPOST            P,
                 TPAHALFGOODS     HG,
                 TPAORDERM        PM,
                 TORDERDT         TD,
                 TRECEIVER        TR,
                 TPAHALFORDERLIST HO
            WHERE PM.PA_CODE IN ('C1', 'C2')
              AND PM.PA_ORDER_GB ='10'
              AND PM.PA_ORDER_GB = HO.PA_ORDER_GB
              AND HO.ORD_NO = PM.PA_ORDER_NO
              AND PM.PA_ORDER_GB = TD.ORDER_GB
              AND PM.ORDER_NO    = TD.ORDER_NO
              AND PM.ORDER_G_SEQ = TD.ORDER_G_SEQ
              AND PM.ORDER_D_SEQ = TD.ORDER_D_SEQ
              AND PM.ORDER_W_SEQ = TD.ORDER_W_SEQ
              AND TD.CUST_NO     = TR.CUST_NO
              AND TD.RECEIVER_SEQ = TR.RECEIVER_SEQ
              AND HO.TO_ZI_CD     = P.POST_NO
              AND TR.STD_POST_SEQ = P.POST_SEQ
              AND P.AREA_GB IN ('20', '30')
              AND HG.PRODUCT_NO = HO.PRD_NO
              AND NOT EXISTS (SELECT 'X'
                     FROM TPAHALFORDERLIST OO
                    WHERE OO.ORD_NO = HO.ORD_NO
                      AND OO.ORD_NO_NM = HO.ORD_NO_NM
                      AND OO.PA_ORDER_GB IN ('20', '30'))
             AND EXISTS (SELECT 'X'
                    FROM TDELYNOAREA GG
                   WHERE GG.GOODS_CODE = HG.GOODS_CODE)
             AND TD.ORDER_DATE >= SYSDATE - 30
             AND HO.ORD_NO NOT IN ('240211322161') -- 02/13 상담내역 확인
             );
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  
  V_FLAG      := 'PH05';
  V_ERROR_MSG := '[PH05] 하프클럽 교환주문취소 인입';
  IS_EXISTED  := 0;
    
  --[PH05] 하프클럽 교환주문취소 인입
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM ( SELECT HE.ORD_NO AS PA_ORDER_NO
             FROM TPAHALFEXCHANGECANCEL HE
            WHERE HE.INSERT_DATE > SYSDATE - 3
              AND HE.ORD_NO NOT IN ('240415274058','240415340441'));
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;  
  
  
  /*=========================================20210318==============================================================*/

  V_FLAG      := 'PI001';
  V_ERROR_MSG := '[PI001] I/F 주문검증 ( 제휴주문테이블 <-> TPAORDERM )';
  IS_EXISTED  := 0;
  /* PI001 : I/F 주문검증 ( 제휴주문테이블 <-> TPAORDERM ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT '01' AS PA_GROUP_CODE, OL.ORD_NO
            FROM TPA11STORDERLIST OL
           WHERE OL.PROC_FLAG = '10'
             AND OL.PA_ORDER_GB = '10'
             AND OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS (SELECT /*+INDEX(PM UK_TPAORDERM_01)*/ 
                     1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = OL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = OL.ORD_NO
                     AND PM.PA_ORDER_SEQ = OL.ORD_PRD_SEQ
                     AND PM.PA_SHIP_NO = OL.DLV_NO
                     AND PM.PA_CODE IN ('11', '12')
                     AND PM.PA_PROC_QTY = OL.ORD_QTY)
          UNION ALL
          
          SELECT '02' AS PA_GROUP_CODE, OL.PAY_NO
            FROM TPAGMKTORDERLIST OL
           WHERE OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT /*+INDEX(PM UK_TPAORDERM_01)*/ 
                   1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '10'
                     AND PM.PA_ORDER_NO = OL.PAY_NO
                     AND PM.PA_ORDER_SEQ = OL.CONTR_NO
                     AND PM.PA_CODE IN ('21', '22')
                     AND PM.PA_PROC_QTY = OL.CONTR_QTY)
          UNION ALL
          
          SELECT '04' AS PA_GROUP_CODE, OL.ORDER_ID
            FROM TPANAVERORDERLIST OL
           WHERE OL.PA_ORDER_GB = '10'
             AND OL.PRODUCT_ORDER_STATUS NOT IN('28', '29')
             AND OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT /*+INDEX(PM UK_TPAORDERM_01)*/ 
                               1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = OL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = OL.ORDER_ID
                     AND PM.PA_ORDER_SEQ = OL.PRODUCT_ORDER_ID
                     AND PM.PA_CODE = '41'
                     AND PM.PA_PROC_QTY = OL.QUANTITY)
             AND NOT EXISTS
           (SELECT 1
                    FROM TPANAVERORDERCHANGE CH
                   WHERE OL.ORDER_ID = CH.ORDER_ID
                     AND CH.LAST_CHANGED_STATUS = '29'
                     AND CH.PRODUCT_ORDER_STATUS = '28')
          
          UNION ALL
          
          SELECT '05' AS PA_GROUP_CODE, OL.ORDER_ID
            FROM TPACOPNORDERITEMLIST OL
           WHERE OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT /*+INDEX(PM UK_TPAORDERM_01)*/ 
                   1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '10'
                     AND PM.PA_ORDER_NO = OL.ORDER_ID
                     AND PM.PA_ORDER_SEQ = OL.ITEM_SEQ
                     AND PM.PA_SHIP_NO = OL.SHIPMENT_BOX_ID
                     AND PM.PA_CODE IN ('51', '52')
                     AND PM.PA_PROC_QTY = OL.SHIPPING_COUNT)
          
          UNION ALL
          
          SELECT '06' AS PA_GROUP_CODE, OL.PA_ORDER_NO
            FROM TPAWEMPORDERITEMLIST OL
           WHERE OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT /*+INDEX(PM UK_TPAORDERM_01)*/ 
                   1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '10'
                     AND PM.PA_ORDER_NO = OL.PA_ORDER_NO
                     AND PM.PA_ORDER_SEQ = OL.PA_ORDER_SEQ
                     AND PM.PA_SHIP_NO = OL.PA_SHIP_NO
                     AND PM.PA_CODE IN ('61', '62')
                     AND PM.PA_PROC_QTY = OL.OPTION_QTY)
          
          UNION ALL
          
          SELECT '07' AS PA_GROUP_CODE, OL.ORD_NO
            FROM TPAINTPORDERLIST OL
           WHERE OL.PA_ORDER_GB = '10'
             AND OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS (SELECT /*+INDEX(PM UK_TPAORDERM_01)*/ 
                 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = OL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = OL.ORD_NO
                     AND PM.PA_ORDER_SEQ = OL.ORD_SEQ
                     AND PM.PA_SHIP_NO = OL.DELVSETL_SEQ
                     AND PM.PA_CODE IN ('71', '72')
                     AND PM.PA_PROC_QTY = OL.ORD_QTY)
          
          UNION ALL
          
          SELECT '08' AS PA_GROUP_CODE, OL.OD_NO
            FROM TPALTONORDERLIST OL
           WHERE OL.PA_ORDER_GB = '10'
             AND OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS (SELECT /*+INDEX(PM UK_TPAORDERM_01)*/ 
                            1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = OL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = OL.OD_NO
                     AND PM.PA_ORDER_SEQ = OL.OD_SEQ
                     AND PM.PA_SHIP_NO = OL.PROC_SEQ
                     AND PM.PA_CODE IN ('81', '82')
                     AND PM.PA_PROC_QTY = OL.OD_QTY)
          
          UNION ALL
          
          SELECT '09' AS PA_GROUP_CODE, OL.TMON_ORDER_NO
            FROM TPATMONORDERLIST OL
           WHERE OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT /*+INDEX(PM UK_TPAORDERM_01)*/ 
                   1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '10'
                     AND PM.PA_ORDER_NO = OL.TMON_ORDER_NO
                     AND PM.PA_SHIP_NO = OL.DELIVERY_NO
                     AND PM.PA_ORDER_SEQ = OL.TMON_DEAL_OPTION_NO
                     AND PM.PA_CODE IN ('91', '92')
                     AND PM.PA_PROC_QTY = OL.QTY)
          
          UNION ALL
          
          SELECT '10' AS PA_GROUP_CODE, OL.ORORD_NO
            FROM TPASSGORDERLIST OL
           WHERE OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND OL.SHPP_DIV_DTL_CD = '11'
             AND NOT EXISTS
           (SELECT /*+INDEX(PM UK_TPAORDERM_01)*/ 
                   1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '10'
                     AND PM.PA_ORDER_NO = OL.ORORD_NO
                     AND PM.PA_ORDER_SEQ = OL.ORORD_ITEM_SEQ
                     AND PM.PA_SHIP_NO = OL.SHPP_NO
                     AND PM.PA_SHIP_SEQ = OL.SHPP_SEQ
                     AND PM.PA_CODE IN ('A1', 'A2')
                     AND PM.PA_PROC_QTY = OL.DIRC_ITEM_QTY)
              AND OL.ORD_NO NOT IN ('2023042784D9D2') -- 4/27 테스트주문 
          
          UNION ALL
          
          SELECT '11' AS PA_GROUP_CODE, OL.PAYMENT_ID
            FROM TPAKAKAOORDERLIST OL
           WHERE OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT /*+INDEX(PM UK_TPAORDERM_01)*/ 
                   1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '10'
                     AND PM.PA_ORDER_NO = OL.PAYMENT_ID
                     AND PM.PA_ORDER_SEQ = OL.ID
                     AND PM.PA_CODE IN ('B1', 'B2')
                     AND PM.PA_PROC_QTY = TO_CHAR(OL.QUANTITY))
          
          UNION ALL
          
          SELECT '12' AS PA_GROUP_CODE, OL.ORD_NO
            FROM TPAHALFORDERLIST OL
           WHERE OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND OL.PA_ORDER_GB = '10'
             AND NOT EXISTS (SELECT /*+INDEX(PM UK_TPAORDERM_01)*/ 
                     1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '10'
                     AND PM.PA_ORDER_NO = OL.ORD_NO
                     AND PM.PA_ORDER_SEQ = OL.ORD_NO_NM
                     AND PM.PA_CODE IN ('C1', 'C2')));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  /*=======================================================================================================*/

  V_FLAG      := 'PI002';
  V_ERROR_MSG := '[] I/F 주문검증 ( TPAORDERM <-> 제휴주문테이블 )';
  IS_EXISTED  := 0;
  /* PI002 : I/F 주문검증 ( TPAORDERM <-> 제휴주문테이블 ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.PA_ORDER_GB,
                 PM.PA_ORDER_NO,
                 PM.PA_ORDER_SEQ,
                 PM.PA_SHIP_NO,
                 PM.PA_CLAIM_NO,
                 PM.PA_CODE,
                 PM.PA_PROC_QTY
            FROM TPAORDERM PM
           WHERE PM.PA_ORDER_GB = '10'
             AND PM.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND PM.PA_ORDER_NO NOT IN ('2023042784D9D2') -- 4/27 테스트주문
             AND 0 = CASE
                   WHEN PM.PA_CODE IN ('11', '12') THEN
                    (SELECT COUNT(1)
                       FROM TPA11STORDERLIST OL
                      WHERE PM.PA_ORDER_GB = OL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = OL.ORD_NO
                        AND PM.PA_ORDER_SEQ = OL.ORD_PRD_SEQ
                        AND PM.PA_SHIP_NO = OL.DLV_NO
                        AND PM.PA_PROC_QTY = OL.ORD_QTY)
                   WHEN PM.PA_CODE IN ('21', '22') THEN
                    (SELECT COUNT(1)
                       FROM TPAGMKTORDERLIST OL
                      WHERE OL.PAY_NO = PM.PA_ORDER_NO
                        AND OL.CONTR_NO = PM.PA_ORDER_SEQ
                        AND OL.CONTR_QTY = PM.PA_PROC_QTY)
                   WHEN PM.PA_CODE = ('41') THEN
                    (SELECT COUNT(1)
                       FROM TPANAVERORDERLIST OL
                      WHERE PM.PA_ORDER_GB = OL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = OL.ORDER_ID
                        AND PM.PA_ORDER_SEQ = OL.PRODUCT_ORDER_ID
                        AND PM.PA_PROC_QTY = OL.QUANTITY)
                   WHEN PM.PA_CODE IN ('51', '52') THEN
                    (SELECT COUNT(1)
                       FROM TPACOPNORDERITEMLIST OL
                      WHERE OL.ORDER_ID = PM.PA_ORDER_NO
                        AND OL.ITEM_SEQ = PM.PA_ORDER_SEQ
                        AND OL.SHIPMENT_BOX_ID = PM.PA_SHIP_NO
                        AND OL.SHIPPING_COUNT = PM.PA_PROC_QTY)
                   WHEN PM.PA_CODE IN ('61', '62') THEN
                    (SELECT COUNT(1)
                       FROM TPAWEMPORDERITEMLIST OL
                      WHERE OL.PA_ORDER_NO = PM.PA_ORDER_NO
                        AND OL.PA_ORDER_SEQ = PM.PA_ORDER_SEQ
                        AND OL.PA_SHIP_NO = PM.PA_SHIP_NO
                        AND OL.OPTION_QTY = PM.PA_PROC_QTY)
                   WHEN PM.PA_CODE IN ('71', '72') THEN
                    (SELECT COUNT(1)
                       FROM TPAINTPORDERLIST OL
                      WHERE OL.PA_ORDER_GB = PM.PA_ORDER_GB
                        AND OL.ORD_NO = PM.PA_ORDER_NO
                        AND OL.ORD_SEQ = PM.PA_ORDER_SEQ
                        AND OL.DELVSETL_SEQ = PM.PA_SHIP_NO
                        AND OL.ORD_QTY = PM.PA_PROC_QTY)
                   WHEN PM.PA_CODE IN ('81', '82') THEN
                    (SELECT COUNT(1)
                       FROM TPALTONORDERLIST OL
                      WHERE OL.PA_ORDER_GB = PM.PA_ORDER_GB
                        AND OL.OD_NO = PM.PA_ORDER_NO
                        AND OL.OD_SEQ = PM.PA_ORDER_SEQ
                        AND OL.ORGL_PROC_SEQ = PM.PA_SHIP_SEQ
                        AND OL.PROC_SEQ = PM.PA_SHIP_NO
                        AND OL.OD_QTY = PM.PA_PROC_QTY)
                   WHEN PM.PA_CODE IN ('91', '92') THEN
                    (SELECT COUNT(1)
                       FROM TPATMONORDERLIST OL
                      WHERE OL.TMON_ORDER_NO = PM.PA_ORDER_NO
                        AND OL.TMON_DEAL_OPTION_NO = PM.PA_ORDER_SEQ
                        AND OL.DELIVERY_NO = PM.PA_SHIP_NO
                        AND OL.QTY = PM.PA_PROC_QTY)
                   WHEN PM.PA_CODE IN ('A1', 'A2') THEN
                    (SELECT COUNT(1)
                       FROM TPASSGORDERLIST OL
                      WHERE OL.ORORD_NO = PM.PA_ORDER_NO
                        AND OL.ORORD_ITEM_SEQ = PM.PA_ORDER_SEQ
                        AND OL.SHPP_NO = PM.PA_SHIP_NO
                        AND OL.SHPP_SEQ = PM.PA_SHIP_SEQ
                        AND OL.DIRC_ITEM_QTY = PM.PA_PROC_QTY)
                   WHEN PM.PA_CODE IN ('B1', 'B2') THEN
                    (SELECT COUNT(1)
                       FROM TPAKAKAOORDERLIST OL
                      WHERE OL.PAYMENT_ID = PM.PA_ORDER_NO
                        AND OL.ID = PM.PA_ORDER_SEQ
                        AND TO_CHAR(OL.QUANTITY) = PM.PA_PROC_QTY)
                   WHEN PM.PA_CODE IN ('C1', 'C2') THEN
                    (SELECT COUNT(1)
                       FROM TPAHALFORDERLIST OL
                      WHERE OL.ORD_NO = PM.PA_ORDER_NO
                        AND OL.ORD_NO_NM = PM.PA_ORDER_SEQ
                        AND OL.PA_ORDER_GB = '10')
                   ELSE
                    1
                 END);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  /*=======================================================================================================*/

  V_FLAG      := 'PI003';
  V_ERROR_MSG := '[PI003] I/F 취소검증 ( 제휴테이블 <-> TPAORDERM )';
  IS_EXISTED  := 0;
  /* PI003 : I/F 취소검증 ( 제휴테이블 <-> TPAORDERM ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT '01' AS PA_GROUP_CODE, OL.ORD_NO
            FROM TPA11STORDERLIST OL
           WHERE OL.PROC_FLAG = '10'
             AND OL.PA_ORDER_GB = '20'
             AND OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = OL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = OL.ORD_NO
                     AND PM.PA_ORDER_SEQ = OL.ORD_PRD_SEQ
                     AND PM.PA_SHIP_NO = OL.DLV_NO
                     AND PM.PA_CLAIM_NO = OL.ORD_PRD_CN_SEQ
                     AND PM.PA_CODE IN ('11', '12')
                     AND PM.PA_PROC_QTY = OL.ORD_CN_QTY)
          UNION ALL
          
          SELECT '02' AS PA_GROUP_CODE, CL.CONTR_NO
            FROM TPAGMKTCANCELLIST CL
           WHERE CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CL.WITHDRAW_YN <> '1'
             AND CL.PROC_FLAG = '10'
             AND NOT EXISTS (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '20'
                     AND PM.PA_ORDER_NO = CL.PAY_NO
                     AND PM.PA_ORDER_SEQ = CL.CONTR_NO
                     AND PM.PA_CLAIM_NO = CL.CONTR_NO_SEQ
                     AND PM.PA_CODE IN ('21', '22'))
             AND CL.CONTR_NO NOT IN ('3677367513' -- 2/3
                                     )
          
          UNION ALL
          
          SELECT '04' AS PA_GROUP_CODE, CL.PRODUCT_ORDER_ID
            FROM TPANAVERCLAIMLIST CL
           WHERE CL.CLAIM_STATUS IN ('00', '01', '02')
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '20'
                     AND PM.PA_ORDER_SEQ = CL.PRODUCT_ORDER_ID
                     AND PM.PA_CODE = '41')
             AND NOT EXISTS
           (SELECT 1
                    FROM TPANAVERORDERLIST OL
                   WHERE OL.PRODUCT_ORDER_ID = CL.PRODUCT_ORDER_ID
                     AND OL.CLAIM_STATUS = '02'
                     AND OL.PROC_FLAG = '10')
          
          /* '05' 쿠팡은 취소(PA_ORDER_GB = 20)가 없다 */
          
          UNION ALL
          
          SELECT '06' AS PA_GROUP_CODE, CL.PA_ORDER_NO
            FROM TPAWEMPCLAIMLIST CW, TPAWEMPCLAIMITEMLIST CL
           WHERE CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CW.PA_ORDER_GB = '20'
             AND CW.PA_ORDER_GB = CL.PA_ORDER_GB
             AND CL.PROC_FLAG = '10'
             AND CW.CANCEL_WITHDRAW_YN <> '1'
             AND CW.PA_CLAIM_CODE <> '26' -- 품절주문건
             AND CW.PA_CLAIM_NO = CL.PA_CLAIM_NO
             AND CW.PA_ORDER_NO = CL.PA_ORDER_NO
             AND CW.PA_SHIP_NO = CL.PA_SHIP_NO
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = CL.PA_ORDER_NO
                     AND PM.PA_ORDER_SEQ = CL.PA_ORDER_SEQ
                     AND PM.PA_CODE IN ('61', '62')
                     AND PM.PA_SHIP_NO = CL.PA_SHIP_NO
                     AND PM.PA_CLAIM_NO = CL.PA_CLAIM_NO)
             AND CW.PA_ORDER_NO NOT IN ('188796309' -- 수기처리건
                                       ,
                                        '188796149' -- 수기처리건
                                       ,
                                        '217871895' -- 7/5
                                       ,
                                        '283707240' -- 10/26
                                       ,'292434718' -- 12/23
                                        )
          
          UNION ALL
          
          SELECT '07' AS PA_GROUP_CODE, OL.ORD_NO
            FROM TPAINTPORDERLIST OL
           WHERE OL.PROC_FLAG = '10'
             AND OL.PA_ORDER_GB = '20'
             AND OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND OL.CLMREQ_TP IS NOT NULL
             AND OL.WITHDRAW_YN = 0
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = OL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = OL.ORD_NO
                     AND PM.PA_ORDER_SEQ = OL.ORD_SEQ
                     AND PM.PA_SHIP_NO = OL.DELVSETL_SEQ
                     AND PM.PA_CODE IN ('71', '72')
                     AND PM.PA_PROC_QTY = OL.CLMREQ_QTY)
          
          UNION ALL
          
          SELECT '08' AS PA_GROUP_CODE, OL.OD_NO
            FROM TPALTONCANCELLIST OL
           WHERE OL.PROC_FLAG = '10'
             AND OL.OD_TYP_CD = '20'
             AND OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND OL.WITHDRAW_YN = 0
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = OL.OD_TYP_CD
                     AND PM.PA_ORDER_NO = OL.OD_NO
                     AND PM.PA_ORDER_SEQ = OL.OD_SEQ
                     AND PM.PA_SHIP_NO = OL.PROC_SEQ
                     AND PM.PA_SHIP_SEQ = OL.ORGL_PROC_SEQ
                     AND PM.PA_CODE IN ('81', '82')
                     AND PM.PA_PROC_QTY = OL.CNCL_QTY)
          
          UNION ALL
          
          SELECT '09' AS PA_GROUP_CODE, OL.TMON_ORDER_NO
            FROM TPATMONCANCELLIST OL
           WHERE OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND OL.WITHDRAW_YN = '0'
             AND PROC_FLAG NOT IN ('00', '07', '20', '05') -- 23.12.06 '처리중' 상태 추가
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '20'
                     AND PM.PA_ORDER_NO = OL.TMON_ORDER_NO
                     AND PM.PA_ORDER_SEQ = OL.TMON_DEAL_OPTION_NO
                     AND PM.PA_SHIP_NO = OL.DELIVERY_NO
                     AND PM.PA_CLAIM_NO = OL.CLAIM_NO
                     AND PM.PA_CODE IN ('91', '92')
                     AND PM.PA_PROC_QTY = OL.QTY)
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '10'
                     AND PM.PRE_CANCEL_YN = '1'
                     AND PM.CREATE_YN = '0'
                     AND PM.PA_ORDER_NO = OL.TMON_ORDER_NO
                     AND PM.PA_ORDER_SEQ = OL.TMON_DEAL_OPTION_NO
                     AND PM.PA_SHIP_NO = OL.DELIVERY_NO
                     AND PM.PA_CODE IN ('91', '92')
                     AND PM.PA_PROC_QTY = OL.QTY)
             AND OL.TMON_ORDER_NO NOT IN ('3104870458' -- 1/10 출고대상 운송장 미기입 상태
                                         ,'3561013906' -- 8/4 주문인입전취소로 인한 수량 수기처리
                                          )
          
          UNION ALL
          
          SELECT '10' AS PA_GROUP_CODE, OL.ORORD_NO
            FROM TPASSGCANCELLIST OL
           WHERE OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND PROC_FLAG NOT IN ('00', '09', '20', '05','07') -- 24.03.14 '처리중' 상태 추가 / 24.03.19 '출고대상' 상태 추가
             AND OL.WITHDRAW_YN <> '1' -- 24.03.18 취소 철회건 미조회
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '20'
                     AND PM.PA_ORDER_NO = OL.ORORD_NO
                     AND PM.PA_ORDER_SEQ = OL.ORORD_ITEM_SEQ
                     AND PM.PA_CODE IN ('A1', 'A2')
                     AND (PM.PA_PROC_QTY = OL.PROC_ORD_QTY OR
                         PM.PA_PROC_QTY = OL.DIRC_ITEM_QTY))
             AND OL.ORORD_NO NOT IN ('202304257E2B68' -- 4/26
                                    ,'2023042784D9D2' -- 4/27 테스트주문
                                    ,'2023042785DB70' -- 4/27 테스트주문 
                                    )
          
          UNION ALL
          
          SELECT '11' AS PA_GROUP_CODE, OL.PAYMENT_ID
            FROM TPAKAKAOCANCELLIST OL
           WHERE OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND PROC_FLAG NOT IN ('00', '07', '20', '05', '06') -- 23.12.06 '처리중' 상태 추가
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '20'
                     AND PM.PA_ORDER_NO = OL.PAYMENT_ID
                     AND PM.PA_ORDER_SEQ = OL.ID
                     AND PM.PA_CODE IN ('B1', 'B2')
                     AND PM.PA_PROC_QTY = TO_CHAR(OL.QUANTITY))
          
          UNION ALL
          
          SELECT '12' AS PA_GROUP_CODE, OL.ORD_NO
            FROM TPAHALFORDERLIST OL
           WHERE OL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND OL.PA_ORDER_GB = '20'
             AND NOT EXISTS (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '20'
                     AND PM.PA_ORDER_NO = OL.ORD_NO
                     AND PM.PA_ORDER_SEQ = OL.ORD_NO_NM
                     AND PM.PA_CODE IN ('C1', 'C2'))
          
          );

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  /*=======================================================================================================*/

  V_FLAG      := 'PI004';
  V_ERROR_MSG := '[PI004] I/F 취소검증 ( TPAORDERM <-> 제휴테이블 )';
  IS_EXISTED  := 0;
  /* PI004 : I/F 취소검증 ( TPAORDERM <-> 제휴테이블 ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.PA_ORDER_GB,
                 PM.PA_ORDER_NO,
                 PM.PA_ORDER_SEQ,
                 PM.PA_SHIP_NO,
                 PM.PA_CLAIM_NO,
                 PM.PA_CODE,
                 PM.PA_PROC_QTY
            FROM TPAORDERM PM
           WHERE PM.PA_ORDER_GB = '20'
             AND PM.PRE_CANCEL_YN = '0'
             AND PM.PA_ORDER_SEQ NOT IN ('3758942580') -- 6/15 문의 중
             AND PM.PA_ORDER_NO NOT IN ('202301251635CE') -- 1/25 테스트 주문
             AND PM.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND 0 = CASE
                   WHEN PM.PA_CODE IN ('11', '12') THEN
                    (SELECT COUNT(1)
                       FROM TPA11STORDERLIST OL
                      WHERE PM.PA_ORDER_GB = OL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = OL.ORD_NO
                        AND PM.PA_ORDER_SEQ = OL.ORD_PRD_SEQ
                        AND PM.PA_SHIP_NO = OL.DLV_NO
                        AND PM.PA_CLAIM_NO = OL.ORD_PRD_CN_SEQ
                        AND PM.PA_PROC_QTY = OL.ORD_CN_QTY)
                   WHEN PM.PA_CODE IN ('21', '22') THEN
                    (SELECT COUNT(1)
                       FROM TPAGMKTCANCELLIST CL
                      WHERE PM.PA_ORDER_NO = CL.PAY_NO
                        AND PM.PA_ORDER_SEQ = CL.CONTR_NO
                        AND PM.PA_CLAIM_NO = CL.CONTR_NO_SEQ
                        AND CL.WITHDRAW_YN = '0'
                        AND CL.CONTR_NO NOT IN ('3677367513' -- 2/3 이베이 문의 중
                                                ))
                   WHEN PM.PA_CODE = ('41') THEN
                    (SELECT COUNT(1)
                       FROM TPANAVERCLAIMLIST CL
                      WHERE PM.PA_ORDER_SEQ = CL.PRODUCT_ORDER_ID
                        AND CL.CLAIM_STATUS IN
                            ('00', '01', '02', '03', '40', '41')
                        AND ROWNUM = 1)
                   WHEN PM.PA_CODE IN ('61', '62') THEN
                    (SELECT COUNT(1)
                       FROM TPAWEMPCLAIMLIST CW, TPAWEMPCLAIMITEMLIST CL
                      WHERE 1 = 1
                        AND CL.PROC_FLAG = '10'
                        AND CW.CANCEL_WITHDRAW_YN <> '1'
                           --AND CW.PA_CLAIM_CODE <> '26' -- 품절주문건
                        AND CW.PA_CLAIM_NO = CL.PA_CLAIM_NO
                        AND CW.PA_ORDER_NO = CL.PA_ORDER_NO
                        AND CW.PA_SHIP_NO = CL.PA_SHIP_NO
                        AND CW.PA_ORDER_GB = CL.PA_ORDER_GB
                        AND CW.PA_CLAIM_NO = PM.PA_CLAIM_NO
                        AND CW.PA_ORDER_NO = PM.PA_ORDER_NO
                        AND CW.PA_SHIP_NO = PM.PA_SHIP_NO
                        AND CL.PA_ORDER_SEQ = PM.PA_ORDER_SEQ
                        AND CW.PA_ORDER_GB = PM.PA_ORDER_GB
                        AND CW.PA_ORDER_NO NOT IN ('188796309', '188796149'))
                   WHEN PM.PA_CODE IN ('71', '72') THEN
                    (SELECT COUNT(1)
                       FROM TPAINTPORDERLIST OL
                      WHERE (OL.PROC_FLAG = '10' OR OL.CLMREQ_TP = '1')
                        AND OL.PA_ORDER_GB = '20'
                        AND OL.PA_ORDER_GB = PM.PA_ORDER_GB
                        AND OL.CLMREQ_SEQ = PM.PA_CLAIM_NO
                        AND OL.ORD_NO = PM.PA_ORDER_NO
                        AND OL.ORD_SEQ = PM.PA_ORDER_SEQ
                        AND OL.CLMREQ_TP IS NOT NULL)
                   WHEN PM.PA_CODE IN ('81', '82') THEN
                    (SELECT COUNT(1)
                       FROM TPALTONCANCELLIST OL
                      WHERE OL.PROC_FLAG = '10'
                        AND OL.OD_TYP_CD = '20'
                        AND OL.OD_TYP_CD = PM.PA_ORDER_GB
                        AND OL.CLM_NO = PM.PA_CLAIM_NO
                        AND OL.OD_NO = PM.PA_ORDER_NO
                        AND OL.OD_SEQ = PM.PA_ORDER_SEQ
                        AND OL.ORGL_PROC_SEQ = PM.PA_SHIP_SEQ
                        AND OL.PROC_SEQ = PM.PA_SHIP_NO)
                   WHEN PM.PA_CODE IN ('91', '92') THEN
                    (SELECT COUNT(1)
                       FROM TPATMONCANCELLIST OL
                      WHERE OL.CLAIM_NO = PM.PA_CLAIM_NO
                        AND OL.TMON_ORDER_NO = PM.PA_ORDER_NO
                        AND OL.TMON_DEAL_OPTION_NO = PM.PA_ORDER_SEQ
                        AND OL.DELIVERY_NO = PM.PA_SHIP_NO)
                   WHEN PM.PA_CODE IN ('A1', 'A2') THEN
                    (SELECT COUNT(1)
                       FROM TPASSGCANCELLIST OL
                      WHERE OL.ORORD_NO = PM.PA_ORDER_NO
                        AND OL.ORORD_ITEM_SEQ = PM.PA_ORDER_SEQ)
                   WHEN PM.PA_CODE IN ('B1', 'B2') THEN
                    (SELECT COUNT(1)
                       FROM TPAKAKAOCANCELLIST OL
                      WHERE OL.PAYMENT_ID = PM.PA_ORDER_NO
                        AND OL.ID = PM.PA_ORDER_SEQ
                        AND OL.CLAIM_ID = PM.PA_CLAIM_NO)
                   WHEN PM.PA_CODE IN ('C1', 'C2') THEN
                    (SELECT COUNT(1)
                       FROM TPAHALFORDERLIST OL
                      WHERE OL.ORD_NO = PM.PA_ORDER_NO
                        AND OL.ORD_NO_NM = PM.PA_ORDER_SEQ
                        AND OL.PA_ORDER_GB = '20')
                   ELSE
                    1
                 END);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  /*=======================================================================================================*/

  V_FLAG      := 'PI005';
  V_ERROR_MSG := '[PI005] I/F 반품 검증 ( 제휴테이블 <-> TPAORDERM )';
  IS_EXISTED  := 0;

  /* PI005 : I/F 반품 검증 ( 제휴테이블 <-> TPAORDERM ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT '01' AS PA_GROUP_CODE, CL.ORD_NO
            FROM TPA11STCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '30'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = CL.ORD_NO
                     AND PM.PA_ORDER_SEQ = CL.ORD_PRD_SEQ
                     AND PM.PA_CLAIM_NO = CL.CLM_REQ_SEQ
                     AND PM.PA_CODE IN ('11', '12')
                     AND PM.PA_PROC_QTY = CL.CLM_REQ_QTY)
          
          UNION ALL
          
          SELECT '02' AS PA_GROUP_CODE, CL.CONTR_NO
            FROM TPAGMKTCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '30'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = CL.PAY_NO
                     AND PM.PA_ORDER_SEQ = CL.CONTR_NO
                     AND PM.PA_CLAIM_NO = CL.CONTR_NO_SEQ
                     AND PM.PA_CODE IN ('21', '22'))
          
          UNION ALL
          
          SELECT '04' AS PA_GROUP_CODE, CL.PRODUCT_ORDER_ID
            FROM TPANAVERCLAIMLIST CL, TPANAVERORDERLIST OL
           WHERE CL.PRODUCT_ORDER_ID = OL.PRODUCT_ORDER_ID
             AND CL.CLAIM_STATUS LIKE '1%'
             AND OL.CLAIM_STATUS = '10'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '30'
                     AND PM.PA_ORDER_SEQ = CL.PRODUCT_ORDER_ID
                     AND PM.PA_CODE = '41')
          
          UNION ALL
          
          SELECT '05' AS PA_GROUP_CODE, CL.ORDER_ID
            FROM TPACOPNCLAIMLIST CL, TPACOPNCLAIMITEMLIST CIL
           WHERE CL.PA_ORDER_GB = '30'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CL.RECEIPT_ID = CIL.RECEIPT_ID
             AND CL.ORDER_ID = CIL.ORDER_ID
             AND CL.PAYMENT_ID = CIL.PAYMENT_ID
             AND CL.PROC_FLAG = '10'
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '30'
                     AND PM.PA_CODE IN ('51', '52')
                     AND PM.PA_ORDER_NO = CIL.ORDER_ID
                     AND PM.PA_ORDER_SEQ = CIL.ITEM_SEQ)
          
          UNION ALL
          
          SELECT '06' AS PA_GROUP_CODE, CL.PA_ORDER_NO
            FROM TPAWEMPCLAIMLIST CL, TPAWEMPCLAIMITEMLIST CIL
           WHERE CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CL.PA_CLAIM_NO = CIL.PA_CLAIM_NO
             AND CL.PA_ORDER_NO = CIL.PA_ORDER_NO
             AND CL.PA_SHIP_NO = CIL.PA_SHIP_NO
             AND CL.PA_ORDER_GB = CIL.PA_ORDER_GB
             AND CL.PA_ORDER_GB = '30'
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '30'
                     AND PM.PA_ORDER_NO = CL.PA_ORDER_NO
                     AND PM.PA_ORDER_SEQ = CIL.PA_ORDER_SEQ
                     AND PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_CLAIM_NO = CL.PA_CLAIM_NO)
          
          UNION ALL
          
          SELECT '07' AS PA_GROUP_CODE, CL.ORD_NO
            FROM TPAINTPCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '30'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = CL.ORD_NO
                     AND PM.PA_ORDER_SEQ = CL.ORD_SEQ
                     AND PM.PA_CLAIM_NO = CL.CLM_NO
                     AND PM.PA_CODE IN ('71', '72')
                     AND PM.PA_PROC_QTY = CL.CLM_QTY)
          
          UNION ALL
          
          SELECT '08' AS PA_GROUP_CODE, CL.OD_NO
            FROM TPALTONCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '30'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = CL.OD_NO
                     AND PM.PA_ORDER_SEQ = CL.OD_SEQ
                     AND PM.PA_CLAIM_NO = CL.CLM_NO
                     AND PM.PA_SHIP_NO = CL.PROC_SEQ
                     AND PM.PA_SHIP_SEQ = CL.ORGL_PROC_SEQ
                     AND PM.PA_CODE IN ('81', '82')
                     AND PM.PA_PROC_QTY = CL.RTNG_QTY)
          
          UNION ALL
          
          SELECT '09' AS PA_GROUP_CODE, CL.TMON_ORDER_NO
            FROM TPATMONCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '30'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = CL.TMON_ORDER_NO
                     AND PM.PA_ORDER_SEQ = CL.TMON_DEAL_OPTION_NO
                     AND PM.PA_CLAIM_NO = CL.CLAIM_NO
                     AND PM.PA_SHIP_NO = CL.DELIVERY_NO
                     AND PM.PA_CODE IN ('91', '92')
                     AND PM.PA_PROC_QTY = CL.QTY)
          
          UNION ALL
          
          SELECT '10' AS PA_GROUP_CODE, CL.ORORD_NO
            FROM TPASSGCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '30'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CL.ORORD_NO NOT IN ('202301251635CE') -- 1/25 테스트 주문
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = CL.ORORD_NO
                     AND PM.PA_ORDER_SEQ = CL.ORORD_ITEM_SEQ
                     AND PM.PA_CLAIM_NO = CL.ORD_NO
                     AND PM.PA_SHIP_NO = CL.SHPP_NO
                     AND PM.PA_SHIP_SEQ = CL.SHPP_SEQ
                     AND PM.PA_CODE IN ('A1', 'A2')
                     AND PM.PA_PROC_QTY = CL.DIRC_ITEM_QTY)
          
          UNION ALL
          
          SELECT '11' AS PA_GROUP_CODE, CL.PAYMENT_ID
            FROM TPAKAKAOCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '30'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = CL.PAYMENT_ID
                     AND PM.PA_ORDER_SEQ = CL.ID
                     AND PM.PA_CLAIM_NO = CL.CLAIM_ID
                     AND PM.PA_CODE IN ('B1', 'B2')
                     AND PM.PA_PROC_QTY = TO_CHAR(CL.QUANTITY))
          
          UNION ALL
          
          SELECT '12' AS PA_GROUP_CODE, HL.ORD_NO
            FROM TPAHALFORDERLIST HL
           WHERE HL.PA_ORDER_GB = '30'
             AND HL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = HL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = HL.ORD_NO
                     AND PM.PA_ORDER_SEQ = HL.ORD_NO_NM
                     AND PM.PA_CLAIM_NO = HL.CLAIM_NO
                     AND PM.PA_CODE IN ('C1', 'C2'))
          
          );

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  /*=======================================================================================================*/

  V_FLAG      := 'PI006';
  V_ERROR_MSG := '[PI006] I/F 반품 검증 ( TPAORDERM <-> 제휴테이블 )';
  IS_EXISTED  := 0;

  /* PI006 : I/F 반품 검증 ( TPAORDERM <-> 제휴테이블 ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.PA_ORDER_GB,
                 PM.PA_ORDER_NO,
                 PM.PA_ORDER_SEQ,
                 PM.PA_SHIP_NO,
                 PM.PA_CLAIM_NO,
                 PM.PA_CODE,
                 PM.PA_PROC_QTY
            FROM TPAORDERM PM
           WHERE PM.PA_ORDER_GB = '30'
             AND PM.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND PM.PA_ORDER_NO NOT IN ('202202010B61E5' -- 3/24 확인중 주문
                                       ,
                                        '2022062516559437',
                                        '2022062616706083',
                                        '2022062616725861',
                                        '2022062316317204',
                                        '2022062716758301' -- 7/6 5개
                                        )
             AND 0 = CASE
                   WHEN PM.PA_CODE IN ('11', '12') THEN
                    (SELECT COUNT(1)
                       FROM TPA11STCLAIMLIST CL
                      WHERE CL.ORD_NO = PM.PA_ORDER_NO
                        AND CL.PA_ORDER_GB = PM.PA_ORDER_GB
                        AND CL.ORD_PRD_SEQ = PM.PA_ORDER_SEQ
                        AND CL.CLM_REQ_SEQ = PM.PA_CLAIM_NO
                        AND CL.CLM_REQ_QTY = PM.PA_PROC_QTY)
                   WHEN PM.PA_CODE IN ('21', '22') THEN
                    (SELECT COUNT(1)
                       FROM TPAGMKTCLAIMLIST CL
                      WHERE CL.PA_ORDER_GB = PM.PA_ORDER_GB
                        AND CL.PAY_NO = PM.PA_ORDER_NO
                        AND CL.CONTR_NO = PM.PA_ORDER_SEQ
                        AND CL.CONTR_NO_SEQ = PM.PA_CLAIM_NO)
                   WHEN PM.PA_CODE = ('41') THEN
                    (SELECT COUNT(1)
                       FROM TPANAVERCLAIMLIST CL
                      WHERE (CL.CLAIM_STATUS LIKE '1%' OR CL.CLAIM_STATUS = '41')
                        AND CL.PRODUCT_ORDER_ID = PM.PA_ORDER_SEQ
                        AND ROWNUM = 1)
                   WHEN PM.PA_CODE IN ('51', '52') THEN
                    (SELECT COUNT(1)
                       FROM TPACOPNCLAIMLIST CL, TPACOPNCLAIMITEMLIST CIL
                      WHERE CL.RECEIPT_ID = CIL.RECEIPT_ID
                        AND CL.ORDER_ID = CIL.ORDER_ID
                        AND CL.PAYMENT_ID = CIL.PAYMENT_ID
                        AND PM.PA_ORDER_NO = CIL.ORDER_ID
                        AND PM.PA_ORDER_SEQ = CIL.ITEM_SEQ
                        AND PM.PA_CLAIM_NO = CL.RECEIPT_ID
                        AND PM.PA_ORDER_GB = CL.PA_ORDER_GB)
                   WHEN PM.PA_CODE IN ('61', '62') THEN
                    (SELECT COUNT(1)
                       FROM TPAWEMPCLAIMLIST CW, TPAWEMPCLAIMITEMLIST CL
                      WHERE CW.PA_CLAIM_NO = CL.PA_CLAIM_NO
                        AND CW.PA_ORDER_NO = CL.PA_ORDER_NO
                        AND CW.PA_SHIP_NO = CL.PA_SHIP_NO
                        AND CW.PA_ORDER_GB = CL.PA_ORDER_GB
                        AND CW.PA_CLAIM_NO = PM.PA_CLAIM_NO
                        AND CW.PA_ORDER_NO = PM.PA_ORDER_NO
                        AND CW.PA_SHIP_NO = PM.PA_SHIP_NO
                        AND CL.PA_ORDER_SEQ = PM.PA_ORDER_SEQ
                        AND CW.PA_ORDER_GB = PM.PA_ORDER_GB)
                   WHEN PM.PA_CODE IN ('71', '72') THEN
                    (SELECT COUNT(1)
                       FROM TPAINTPCLAIMLIST CL
                      WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.ORD_NO
                        AND PM.PA_ORDER_SEQ = CL.ORD_SEQ
                        AND PM.PA_CLAIM_NO = CL.CLM_NO
                        AND PM.PA_PROC_QTY = CL.CLM_QTY)
                   WHEN PM.PA_CODE IN ('81', '82') THEN
                    (SELECT COUNT(1)
                       FROM TPALTONCLAIMLIST CL
                      WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.OD_NO
                        AND PM.PA_ORDER_SEQ = CL.OD_SEQ
                        AND PM.PA_CLAIM_NO = CL.CLM_NO
                        AND PM.PA_SHIP_NO = CL.PROC_SEQ
                        AND PM.PA_SHIP_SEQ = CL.ORGL_PROC_SEQ
                        AND PM.PA_PROC_QTY = CL.RTNG_QTY)
                   WHEN PM.PA_CODE IN ('91', '92') THEN
                    (SELECT COUNT(1)
                       FROM TPATMONCLAIMLIST CL
                      WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.TMON_ORDER_NO
                        AND PM.PA_ORDER_SEQ = CL.TMON_DEAL_OPTION_NO
                        AND PM.PA_CLAIM_NO = CL.CLAIM_NO
                        AND PM.PA_SHIP_NO = CL.DELIVERY_NO
                        AND PM.PA_PROC_QTY = CL.QTY)
                   WHEN PM.PA_CODE IN ('A1', 'A2') THEN
                    (SELECT COUNT(1)
                       FROM TPASSGCLAIMLIST CL
                      WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.ORORD_NO
                        AND PM.PA_ORDER_SEQ = CL.ORORD_ITEM_SEQ
                        AND PM.PA_CLAIM_NO = CL.ORD_NO
                        AND PM.PA_SHIP_NO = CL.SHPP_NO
                        AND PM.PA_SHIP_SEQ = CL.SHPP_SEQ
                        AND PM.PA_PROC_QTY = CL.DIRC_ITEM_QTY)
                   WHEN PM.PA_CODE IN ('B1', 'B2') THEN
                    (SELECT COUNT(1)
                       FROM TPAKAKAOCLAIMLIST CL
                      WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.PAYMENT_ID
                        AND PM.PA_ORDER_SEQ = CL.ID
                        AND PM.PA_CLAIM_NO = CL.CLAIM_ID
                        AND PM.PA_PROC_QTY = TO_CHAR(CL.QUANTITY))
                   WHEN PM.PA_CODE IN ('C1', 'C2') THEN
                    (SELECT COUNT(1)
                       FROM TPAHALFORDERLIST HL
                      WHERE PM.PA_ORDER_GB = HL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = HL.ORD_NO
                        AND PM.PA_ORDER_SEQ = HL.ORD_NO_NM
                        AND PM.PA_CLAIM_NO = HL.CLAIM_NO
                        AND HL.PA_ORDER_GB = '30')
                   ELSE
                    1
                 END);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  /*=======================================================================================================*/

  V_FLAG      := 'PI007';
  V_ERROR_MSG := '[PI007] I/F 반품취소 검증 ( 제휴테이블 <-> TPAORDERM )';
  IS_EXISTED  := 0;

  /* PI007 : I/F 반품취소 검증 ( 제휴테이블 <-> TPAORDERM ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT '01' AS PA_GROUP_CODE, CL.ORD_NO
            FROM TPA11STCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '31'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = CL.ORD_NO
                     AND PM.PA_ORDER_SEQ = CL.ORD_PRD_SEQ
                     AND PM.PA_CLAIM_NO = CL.CLM_REQ_SEQ
                     AND PM.PA_CODE IN ('11', '12')
                     AND PM.PA_PROC_QTY = CL.CLM_REQ_QTY)
          
          UNION ALL
          
          SELECT '02' AS PA_GROUP_CODE, CL.CONTR_NO
            FROM TPAGMKTCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '31'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = CL.PAY_NO
                     AND PM.PA_ORDER_SEQ = CL.CONTR_NO
                     AND PM.PA_CLAIM_NO = CL.CONTR_NO_SEQ
                     AND PM.PA_CODE IN ('21', '22'))
          
          UNION ALL
          
          SELECT '04' AS PA_GROUP_CODE, CL.PRODUCT_ORDER_ID
            FROM TPANAVERCLAIMLIST CL
           WHERE CL.CLAIM_STATUS = '14'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '31'
                     AND PM.INSERT_DATE <= PM.INSERT_DATE
                     AND PM.PA_ORDER_SEQ = CL.PRODUCT_ORDER_ID
                     AND PM.PA_CODE = '41')
          
          UNION ALL
          
          SELECT '05' AS PA_GROUP_CODE, CL.ORDER_ID
            FROM TPACOPNCLAIMLIST CL, TPACOPNCLAIMITEMLIST CIL
           WHERE CL.PA_ORDER_GB = '31'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CL.RECEIPT_ID = CIL.RECEIPT_ID
             AND CL.ORDER_ID = CIL.ORDER_ID
             AND CL.PAYMENT_ID = CIL.PAYMENT_ID
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_CODE IN ('51', '52')
                     AND PM.PA_ORDER_NO = CIL.ORDER_ID
                     AND PM.PA_ORDER_SEQ = CIL.ITEM_SEQ)
          
          UNION ALL
          
          SELECT '06' AS PA_GROUP_CODE, CL.PA_ORDER_NO
            FROM TPAWEMPCLAIMLIST CL, TPAWEMPCLAIMITEMLIST CIL
           WHERE CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CL.PA_CLAIM_NO = CIL.PA_CLAIM_NO
             AND CL.PA_ORDER_NO = CIL.PA_ORDER_NO
             AND CL.PA_SHIP_NO = CIL.PA_SHIP_NO
             AND CL.PA_ORDER_GB = CIL.PA_ORDER_GB
             AND CL.PA_ORDER_GB = '31'
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = '31'
                     AND PM.PA_ORDER_NO = CL.PA_ORDER_NO
                     AND PM.PA_ORDER_SEQ = CIL.PA_ORDER_SEQ
                     AND PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_CLAIM_NO = CL.PA_CLAIM_NO)
          
          UNION ALL
          
          SELECT '07' AS PA_GROUP_CODE, CL.ORD_NO
            FROM TPAINTPCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '31'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = CL.ORD_NO
                     AND PM.PA_ORDER_SEQ = CL.ORD_SEQ
                     AND PM.PA_CLAIM_NO = CL.CLM_NO
                     AND PM.PA_CODE IN ('71', '72')
                     AND PM.PA_PROC_QTY = CL.CLM_QTY)
          
          UNION ALL
          
          SELECT '08' AS PA_GROUP_CODE, CL.OD_NO
            FROM TPALTONCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '31'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = CL.OD_NO
                     AND PM.PA_ORDER_SEQ = CL.OD_SEQ
                     AND PM.PA_CLAIM_NO = CL.CLM_NO
                     AND PM.PA_SHIP_NO = CL.PROC_SEQ
                     AND PM.PA_SHIP_SEQ = CL.ORGL_PROC_SEQ
                     AND PM.PA_CODE IN ('81', '82')
                     AND PM.PA_PROC_QTY = CL.RTNG_QTY)
          
          UNION ALL
          
          SELECT '09' AS PA_GROUP_CODE, CL.TMON_ORDER_NO
            FROM TPATMONCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '31'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = CL.TMON_ORDER_NO
                     AND PM.PA_ORDER_SEQ = CL.TMON_DEAL_OPTION_NO
                     AND PM.PA_CLAIM_NO = CL.CLAIM_NO
                     AND PM.PA_SHIP_NO = CL.DELIVERY_NO
                     AND PM.PA_CODE IN ('91', '92')
                     AND PM.PA_PROC_QTY = CL.QTY)
          
          UNION ALL
          
          SELECT '10' AS PA_GROUP_CODE, CL.ORORD_NO
            FROM TPASSGCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '31'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = CL.ORORD_NO
                     AND PM.PA_ORDER_SEQ = CL.ORORD_ITEM_SEQ
                     AND PM.PA_CLAIM_NO = CL.ORD_NO
                     AND PM.PA_SHIP_NO = CL.SHPP_NO
                     AND PM.PA_SHIP_SEQ = CL.SHPP_SEQ
                     AND PM.PA_CODE IN ('A1', 'A2')
                     AND PM.PA_PROC_QTY = CL.DIRC_ITEM_QTY)
          
          UNION ALL
          
          SELECT '11' AS PA_GROUP_CODE, CL.PAYMENT_ID
            FROM TPAKAKAOCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '31'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS
           (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = CL.PAYMENT_ID
                     AND PM.PA_ORDER_SEQ = CL.ID
                     AND PM.PA_CLAIM_NO = CL.CLAIM_ID
                     AND PM.PA_CODE IN ('B1', 'B2')
                     AND PM.PA_PROC_QTY = TO_CHAR(CL.QUANTITY))
          
          UNION ALL
          
          SELECT '12' AS PA_GROUP_CODE, HL.ORD_NO
            FROM TPAHALFORDERLIST HL
           WHERE HL.PA_ORDER_GB = '31'
             AND HL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND NOT EXISTS (SELECT 1
                    FROM TPAORDERM PM
                   WHERE PM.PA_ORDER_GB = HL.PA_ORDER_GB
                     AND PM.PA_ORDER_NO = HL.ORD_NO
                     AND PM.PA_ORDER_SEQ = HL.ORD_NO_NM
                     AND PM.PA_CLAIM_NO = HL.CLAIM_NO
                     AND PM.PA_CODE IN ('C1', 'C2')));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  /*=======================================================================================================*/

  V_FLAG      := 'PI008';
  V_ERROR_MSG := '[PI008] I/F 반품취소 검증 ( TPAORDERM <-> 제휴테이블 )';
  IS_EXISTED  := 0;

  /* PI008 : I/F 반품취소 검증 ( TPAORDERM <-> 제휴테이블 ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.PA_ORDER_GB,
                 PM.PA_ORDER_NO,
                 PM.PA_ORDER_SEQ,
                 PM.PA_SHIP_NO,
                 PM.PA_CLAIM_NO,
                 PM.PA_CODE,
                 PM.PA_PROC_QTY
            FROM TPAORDERM PM
           WHERE PM.PA_ORDER_GB = '31'
             AND PM.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND PM.PA_ORDER_NO NOT IN ('202202010B61E5' -- 3/24 수기 반품취소 생성
                                       ,
                                        '20220911DB1838' -- 10/17 수기 반품취소 생성
                                        )
             AND 0 = CASE
                   WHEN PM.PA_CODE IN ('11', '12') THEN
                    (SELECT COUNT(1)
                       FROM TPA11STCLAIMLIST CL
                      WHERE CL.PA_ORDER_GB = PM.PA_ORDER_GB
                        AND CL.ORD_NO = PM.PA_ORDER_NO
                        AND CL.ORD_PRD_SEQ = PM.PA_ORDER_SEQ
                        AND CL.CLM_REQ_SEQ = PM.PA_CLAIM_NO
                        AND CL.CLM_REQ_QTY = PM.PA_PROC_QTY)
                   WHEN PM.PA_CODE IN ('21', '22') THEN
                    (SELECT COUNT(1)
                       FROM TPAGMKTCLAIMLIST CL
                      WHERE CL.PA_ORDER_GB = PM.PA_ORDER_GB
                        AND CL.PAY_NO = PM.PA_ORDER_NO
                        AND CL.CONTR_NO = PM.PA_ORDER_SEQ
                        AND CL.CONTR_NO_SEQ = PM.PA_CLAIM_NO)
                   WHEN PM.PA_CODE = ('41') THEN
                    (SELECT COUNT(1)
                       FROM TPANAVERCLAIMLIST CL
                      WHERE CL.CLAIM_STATUS = '14'
                        AND CL.PRODUCT_ORDER_ID = PM.PA_ORDER_SEQ
                        AND ROWNUM = 1)
                   WHEN PM.PA_CODE IN ('51', '52') THEN
                    (SELECT COUNT(1)
                       FROM TPACOPNCLAIMLIST CL, TPACOPNCLAIMITEMLIST CIL
                      WHERE CL.RECEIPT_ID = CIL.RECEIPT_ID
                        AND CL.ORDER_ID = CIL.ORDER_ID
                        AND CL.PAYMENT_ID = CIL.PAYMENT_ID
                        AND PM.PA_ORDER_NO = CIL.ORDER_ID
                        AND PM.PA_ORDER_SEQ = CIL.ITEM_SEQ
                        AND PM.PA_CLAIM_NO = CL.RECEIPT_ID
                        AND PM.PA_ORDER_GB = CL.PA_ORDER_GB)
                   WHEN PM.PA_CODE IN ('61', '62') THEN
                    (SELECT COUNT(1)
                       FROM TPAWEMPCLAIMLIST CW, TPAWEMPCLAIMITEMLIST CL
                      WHERE CW.PA_CLAIM_NO = CL.PA_CLAIM_NO
                        AND CW.PA_ORDER_NO = CL.PA_ORDER_NO
                        AND CW.PA_SHIP_NO = CL.PA_SHIP_NO
                        AND CW.PA_ORDER_GB = CL.PA_ORDER_GB
                        AND CW.PA_CLAIM_NO = PM.PA_CLAIM_NO
                        AND CW.PA_ORDER_NO = PM.PA_ORDER_NO
                        AND CW.PA_SHIP_NO = PM.PA_SHIP_NO
                        AND CL.PA_ORDER_SEQ = PM.PA_ORDER_SEQ
                        AND CW.PA_ORDER_GB = PM.PA_ORDER_GB)
                   WHEN PM.PA_CODE IN ('71', '72') THEN
                    (SELECT COUNT(1)
                       FROM TPAINTPCLAIMLIST CL
                      WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.ORD_NO
                        AND PM.PA_ORDER_SEQ = CL.ORD_SEQ
                        AND PM.PA_CLAIM_NO = CL.CLM_NO
                        AND PM.PA_PROC_QTY = CL.CLM_QTY)
                   WHEN PM.PA_CODE IN ('81', '82') THEN
                    (SELECT COUNT(1)
                       FROM TPALTONCLAIMLIST CL
                      WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.OD_NO
                        AND PM.PA_ORDER_SEQ = CL.OD_SEQ
                        AND PM.PA_CLAIM_NO = CL.CLM_NO
                        AND PM.PA_SHIP_NO = CL.PROC_SEQ
                        AND PM.PA_SHIP_SEQ = CL.ORGL_PROC_SEQ
                        AND PM.PA_PROC_QTY = CL.RTNG_QTY)
                   WHEN PM.PA_CODE IN ('91', '92') THEN
                    (SELECT COUNT(1)
                       FROM TPATMONCLAIMLIST CL
                      WHERE (PM.PA_ORDER_GB = CL.PA_ORDER_GB OR
                            (CL.PA_ORDER_GB = '30' AND CL.REJECT_YN = '1'))
                        AND PM.PA_ORDER_NO = CL.TMON_ORDER_NO
                        AND PM.PA_ORDER_SEQ = CL.TMON_DEAL_OPTION_NO
                        AND PM.PA_CLAIM_NO = CL.CLAIM_NO
                        AND PM.PA_SHIP_NO = CL.DELIVERY_NO
                        AND PM.PA_PROC_QTY = CL.QTY)
                   WHEN PM.PA_CODE IN ('A1', 'A2') THEN
                    (SELECT COUNT(1)
                       FROM TPASSGCLAIMLIST CL
                      WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.ORORD_NO
                        AND PM.PA_ORDER_SEQ = CL.ORORD_ITEM_SEQ
                        AND PM.PA_CLAIM_NO = CL.ORD_NO
                        AND PM.PA_SHIP_NO = CL.SHPP_NO
                        AND PM.PA_SHIP_SEQ = CL.SHPP_SEQ
                        AND PM.PA_PROC_QTY = CL.DIRC_ITEM_QTY)
                   WHEN PM.PA_CODE IN ('B1', 'B2') THEN
                    (SELECT COUNT(1)
                       FROM TPAKAKAOCLAIMLIST CL
                      WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.PAYMENT_ID
                        AND PM.PA_ORDER_SEQ = CL.ID
                        AND PM.PA_CLAIM_NO = CL.CLAIM_ID
                        AND PM.PA_CODE IN ('B1', 'B2')
                        AND PM.PA_PROC_QTY = TO_CHAR(CL.QUANTITY))
                   WHEN PM.PA_CODE IN ('C1', 'C2') THEN
                    (SELECT COUNT(1)
                       FROM TPAHALFORDERLIST HL
                      WHERE PM.PA_ORDER_GB = HL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = HL.ORD_NO
                        AND PM.PA_ORDER_SEQ = HL.ORD_NO_NM
                        AND PM.PA_CLAIM_NO = HL.CLAIM_NO
                        AND PM.PA_CODE IN ('C1', 'C2'))
                   ELSE
                    1
                 END);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  /*=======================================================================================================*/

  V_FLAG      := 'PI009';
  V_ERROR_MSG := '[PI009] I/F 교환회수, 교환배송 검증 ( 제휴테이블 <-> TPAORDERM )';
  IS_EXISTED  := 0;

  /* PI009 : I/F 교환회수, 교환배송 검증 ( 제휴테이블 <-> TPAORDERM ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT '01' AS PA_GROUP_CODE, CL.ORD_NO
            FROM TPA11STCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '40'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '40'
                      AND PM.PA_ORDER_NO = CL.ORD_NO
                      AND PM.PA_ORDER_SEQ = CL.ORD_PRD_SEQ
                      AND PM.PA_CLAIM_NO = CL.CLM_REQ_SEQ
                      AND PM.PA_PROC_QTY = CL.CLM_REQ_QTY
                      AND PM.PA_CODE IN ('11', '12')) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '45'
                      AND PM.PA_ORDER_NO = CL.ORD_NO
                      AND PM.PA_ORDER_SEQ = CL.ORD_PRD_SEQ
                      AND PM.PA_CLAIM_NO = CL.CLM_REQ_SEQ
                      AND PM.PA_PROC_QTY = CL.CLM_REQ_QTY
                      AND PM.PA_CODE IN ('11', '12')))
          
          UNION ALL
          
          SELECT '02' AS PA_GROUP_CODE, CL.CONTR_NO
            FROM TPAGMKTCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '40'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '40'
                      AND PM.PA_ORDER_NO = CL.PAY_NO
                      AND PM.PA_ORDER_SEQ = CL.CONTR_NO
                      AND PM.PA_CLAIM_NO = CL.CONTR_NO_SEQ
                      AND PM.PA_CODE IN ('21', '22')) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '45'
                      AND PM.PA_ORDER_NO = CL.PAY_NO
                      AND PM.PA_ORDER_SEQ = CL.CONTR_NO
                      AND PM.PA_CLAIM_NO = CL.CONTR_NO_SEQ
                      AND PM.PA_CODE IN ('21', '22')))
          
          UNION ALL
          
          SELECT '04' AS PA_GROUP_CODE, CL.PRODUCT_ORDER_ID
            FROM TPANAVERCLAIMLIST CL
           WHERE CL.CLAIM_STATUS LIKE '2%'
             AND CL.CLAIM_STATUS != '25'
             AND CL.HOLDBACK_STATUS != '10'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM, TPANAVERORDERLIST OL
                    WHERE PM.PA_ORDER_GB = '40'
                      AND PM.PA_ORDER_NO = OL.ORDER_ID
                      AND PM.PA_ORDER_SEQ = OL.PRODUCT_ORDER_ID
                      AND OL.PRODUCT_ORDER_ID = CL.PRODUCT_ORDER_ID
                      AND PM.PA_CODE = '41') OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM, TPANAVERORDERLIST OL
                    WHERE PM.PA_ORDER_GB = '45'
                      AND PM.PA_ORDER_NO = OL.ORDER_ID
                      AND PM.PA_ORDER_SEQ = OL.PRODUCT_ORDER_ID
                      AND OL.PRODUCT_ORDER_ID = CL.PRODUCT_ORDER_ID
                      AND PM.PA_CODE = '41'))
             AND CL.PRODUCT_ORDER_ID NOT IN ('2022072958228181' -- 8/3
                                            ,'2023041914943971' 
                                            ,'2023041913518021' -- 4/25
                                             )
          
          UNION ALL
          
          SELECT '05' AS PA_GROUP_CODE, CL.ORDER_ID
            FROM TPACOPNEXCHANGELIST CL, TPACOPNEXCHANGEITEMLIST CIL
           WHERE CL.PA_ORDER_GB = '40'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CL.PA_ORDER_GB = CIL.PA_ORDER_GB
             AND CL.ORDER_ID = CIL.ORDER_ID
             AND CL.EXCHANGE_ID = CIL.EXCHANGE_ID
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '40'
                      AND PM.PA_ORDER_NO = CL.ORDER_ID
                      AND PM.PA_CLAIM_NO = CL.EXCHANGE_ID
                      AND PM.PA_CODE IN ('51', '52')) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '45'
                      AND PM.PA_ORDER_NO = CL.ORDER_ID
                      AND PM.PA_CLAIM_NO = CL.EXCHANGE_ID
                      AND PM.PA_CODE IN ('51', '52')))
          
          UNION ALL
          
          SELECT '06' AS PA_GROUP_CODE, CL.PA_ORDER_NO
            FROM TPAWEMPCLAIMLIST CL, TPAWEMPCLAIMITEMLIST CIL
           WHERE CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CL.PA_CLAIM_NO = CIL.PA_CLAIM_NO
             AND CL.PA_ORDER_NO = CIL.PA_ORDER_NO
             AND CL.PA_SHIP_NO = CIL.PA_SHIP_NO
             AND CL.PA_ORDER_GB = CIL.PA_ORDER_GB
             AND CL.PA_ORDER_GB = '40'
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '40'
                      AND PM.PA_ORDER_NO = CL.PA_ORDER_NO
                      AND PM.PA_ORDER_SEQ = CIL.PA_ORDER_SEQ
                      AND PM.PA_CLAIM_NO = CL.PA_CLAIM_NO
                      AND PM.PA_SHIP_NO = CL.PA_SHIP_NO
                      AND PM.PA_CODE IN ('61', '62')) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '45'
                      AND PM.PA_ORDER_NO = CL.PA_ORDER_NO
                      AND PM.PA_ORDER_SEQ = CIL.PA_ORDER_SEQ
                      AND PM.PA_CLAIM_NO = CL.PA_CLAIM_NO
                      AND PM.PA_SHIP_NO = CL.PA_SHIP_NO
                      AND PM.PA_CODE IN ('61', '62')))
          
          UNION ALL
          
          SELECT '07' AS PA_GROUP_CODE, CL.ORD_NO
            FROM TPAINTPCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '40'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '40'
                      AND PM.PA_ORDER_NO = CL.ORD_NO
                      AND PM.PA_ORDER_SEQ = CL.ORD_SEQ
                      AND PM.PA_CLAIM_NO = CL.CLM_NO
                      AND PM.PA_CODE IN ('71', '72')
                      AND PM.PA_PROC_QTY = CL.CLM_QTY) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '45'
                      AND PM.PA_ORDER_NO = CL.ORD_NO
                      AND PM.PA_ORDER_SEQ = CL.ORD_SEQ
                      AND PM.PA_CLAIM_NO = CL.CLM_NO
                      AND PM.PA_CODE IN ('71', '72')
                      AND PM.PA_PROC_QTY = CL.CLM_QTY))
          
          UNION ALL
          
          SELECT '08' AS PA_GROUP_CODE, CL.OD_NO
            FROM TPALTONCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '40'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '40'
                      AND PM.PA_ORDER_NO = CL.OD_NO
                      AND PM.PA_ORDER_SEQ = CL.OD_SEQ
                      AND PM.PA_CLAIM_NO = CL.CLM_NO
                      AND PM.PA_CODE IN ('81', '82')
                      AND PM.PA_PROC_QTY = CL.OD_QTY) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '45'
                      AND PM.PA_ORDER_NO = CL.OD_NO
                      AND PM.PA_ORDER_SEQ = CL.OD_SEQ
                      AND PM.PA_CLAIM_NO = CL.CLM_NO
                      AND PM.PA_CODE IN ('81', '82')
                      AND PM.PA_PROC_QTY = CL.OD_QTY))
          
          UNION ALL
          
          SELECT '09' AS PA_GROUP_CODE, CL.TMON_ORDER_NO
            FROM TPATMONCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '40'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '40'
                      AND PM.PA_ORDER_NO = CL.TMON_ORDER_NO
                      AND PM.PA_SHIP_NO = CL.DELIVERY_NO
                      AND PM.PA_ORDER_SEQ = CL.TMON_DEAL_OPTION_NO
                      AND PM.PA_CLAIM_NO = CL.CLAIM_NO
                      AND PM.PA_CODE IN ('91', '92')
                      AND PM.PA_PROC_QTY = CL.QTY) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '45'
                      AND PM.PA_ORDER_NO = CL.TMON_ORDER_NO
                      AND PM.PA_SHIP_NO = CL.DELIVERY_NO
                      AND PM.PA_ORDER_SEQ = CL.TMON_DEAL_OPTION_NO
                      AND PM.PA_CLAIM_NO = CL.CLAIM_NO
                      AND PM.PA_CODE IN ('91', '92')
                      AND PM.PA_PROC_QTY = CL.QTY))
          
          UNION ALL
          
          SELECT '10' AS PA_GROUP_CODE, CL.ORORD_NO
            FROM TPASSGORDERLIST CL
           WHERE CL.SHPP_DIV_DTL_CD = '15'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '40'
                      AND PM.PA_ORDER_NO = CL.ORORD_NO
                      AND PM.PA_ORDER_SEQ = CL.ORORD_ITEM_SEQ
                      AND PM.PA_SHIP_NO = CL.SHPP_NO
                      AND PM.PA_SHIP_SEQ = CL.SHPP_SEQ
                      AND PM.PA_CLAIM_NO = CL.ORD_NO
                      AND PM.PA_CODE IN ('A1', 'A2')
                      AND PM.PA_PROC_QTY = CL.DIRC_ITEM_QTY) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '45'
                      AND PM.PA_ORDER_NO = CL.ORORD_NO
                      AND PM.PA_ORDER_SEQ = CL.ORORD_ITEM_SEQ
                      AND PM.PA_CLAIM_NO = CL.ORD_NO
                      AND PM.PA_CODE IN ('A1', 'A2')
                      AND PM.PA_PROC_QTY = CL.DIRC_ITEM_QTY))
          
          UNION ALL
          
          SELECT '11' AS PA_GROUP_CODE, CL.PAYMENT_ID
            FROM TPAKAKAOCLAIMLIST CL
           WHERE CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CL.PA_ORDER_GB = '40'
             AND (NOT EXISTS (SELECT 1
                                FROM TPAORDERM PM
                               WHERE PM.PA_ORDER_GB = '40'
                                 AND PM.PA_ORDER_NO = CL.PAYMENT_ID
                                 AND PM.PA_ORDER_SEQ = CL.ID
                                 AND PM.PA_CLAIM_NO = CL.CLAIM_ID
                                 AND PM.PA_CODE IN ('B1', 'B2')
                                 AND PM.PA_PROC_QTY = TO_CHAR(CL.QUANTITY)) OR
                  NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '45'
                      AND PM.PA_ORDER_NO = CL.PAYMENT_ID
                      AND PM.PA_ORDER_SEQ = CL.ID
                      AND PM.PA_CLAIM_NO = CL.CLAIM_ID
                      AND PM.PA_CODE IN ('B1', 'B2')
                      AND PM.PA_PROC_QTY = TO_CHAR(CL.QUANTITY)))
          
          UNION ALL
          
          SELECT '12' AS PA_GROUP_CODE, HL.ORD_NO
            FROM TPAHALFORDERLIST HL
           WHERE HL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND HL.PA_ORDER_GB = '40'
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '40'
                      AND PM.PA_ORDER_NO = HL.ORD_NO
                      AND PM.PA_ORDER_SEQ = HL.ORD_NO_NM
                      AND PM.PA_CLAIM_NO = HL.CLAIM_NO
                      AND PM.PA_CODE IN ('C1', 'C2')) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '45'
                      AND PM.PA_ORDER_NO = HL.ORD_NO
                      AND PM.PA_ORDER_SEQ = HL.ORD_NO_NM
                      AND PM.PA_CLAIM_NO = HL.CLAIM_NO
                      AND PM.PA_CODE IN ('C1', 'C2')))
          
          );

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  /*=======================================================================================================*/

  V_FLAG      := 'PI010';
  V_ERROR_MSG := '[PI10] I/F 교환회수, 교환배송 검증 ( TPAORDERM <-> 제휴테이블 )';
  IS_EXISTED  := 0;

  /* PI010 : I/F 교환회수, 교환배송 검증 ( TPAORDERM <-> 제휴테이블 ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.PA_ORDER_GB,
                 PM.PA_ORDER_NO,
                 PM.PA_ORDER_SEQ,
                 PM.PA_SHIP_NO,
                 PM.PA_CLAIM_NO,
                 PM.PA_CODE,
                 PM.PA_PROC_QTY
            FROM TPAORDERM PM
           WHERE PM.PA_ORDER_GB IN ('40', '45')
             AND PM.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND 0 = CASE
                   WHEN PM.PA_CODE IN ('11', '12') THEN
                    (SELECT COUNT(1)
                       FROM TPA11STCLAIMLIST CL
                      WHERE CL.PA_ORDER_GB =
                            DECODE(PM.PA_ORDER_GB, '45', '40', PM.PA_ORDER_GB)
                        AND CL.ORD_NO = PM.PA_ORDER_NO
                        AND CL.ORD_PRD_SEQ = PM.PA_ORDER_SEQ
                        AND CL.CLM_REQ_SEQ = PM.PA_CLAIM_NO
                        AND CL.CLM_REQ_QTY = PM.PA_PROC_QTY)
                   WHEN PM.PA_CODE IN ('21', '22') THEN
                    (SELECT COUNT(1)
                       FROM TPAGMKTCLAIMLIST CL
                      WHERE CL.PA_ORDER_GB =
                            DECODE(PM.PA_ORDER_GB, '45', '40', PM.PA_ORDER_GB)
                        AND CL.PAY_NO = PM.PA_ORDER_NO
                        AND CL.CONTR_NO = PM.PA_ORDER_SEQ
                        AND CL.CONTR_NO_SEQ = PM.PA_CLAIM_NO)
                   WHEN PM.PA_CODE = ('41') THEN
                    (SELECT COUNT(1)
                       FROM TPANAVERCLAIMLIST CL, TPANAVERORDERLIST OL
                      WHERE PM.PA_SHIP_NO IS NULL
                        AND PM.PA_SHIP_SEQ IS NULL
                        AND OL.ORDER_ID = PM.PA_ORDER_NO
                        AND OL.PRODUCT_ORDER_ID = PM.PA_ORDER_SEQ
                        AND OL.PRODUCT_ORDER_ID = CL.PRODUCT_ORDER_ID
                        AND OL.PRODUCT_ORDER_STATUS = '10'
                        AND CL.CLAIM_SEQ = OL.CLAIM_SEQ)
                   WHEN PM.PA_CODE IN ('51', '52') THEN
                    (SELECT COUNT(1)
                       FROM TPACOPNEXCHANGELIST     CL,
                            TPACOPNEXCHANGEITEMLIST CIL
                      WHERE CL.PA_ORDER_GB = CIL.PA_ORDER_GB
                        AND CL.ORDER_ID = CIL.ORDER_ID
                        AND CL.EXCHANGE_ID = CIL.EXCHANGE_ID
                        AND CL.PA_ORDER_GB = CIL.PA_ORDER_GB
                        AND CL.ORDER_ID = PM.PA_ORDER_NO
                        AND CL.PA_ORDER_GB =
                            DECODE(PM.PA_ORDER_GB, '45', '40', PM.PA_ORDER_GB)
                        AND CL.EXCHANGE_ID = PM.PA_CLAIM_NO
                        AND CIL.ITEM_SEQ = PM.PA_ORDER_SEQ)
                   WHEN PM.PA_CODE IN ('61', '62') THEN
                    (SELECT COUNT(1)
                       FROM TPAWEMPCLAIMLIST CL, TPAWEMPCLAIMITEMLIST CIL
                      WHERE CL.PA_CLAIM_NO = CIL.PA_CLAIM_NO
                        AND CL.PA_ORDER_NO = CIL.PA_ORDER_NO
                        AND CL.PA_SHIP_NO = CIL.PA_SHIP_NO
                        AND CL.PA_ORDER_GB = CIL.PA_ORDER_GB
                        AND DECODE(PM.PA_ORDER_GB, '45', '40', PM.PA_ORDER_GB) =
                            CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.PA_ORDER_NO
                        AND PM.PA_ORDER_SEQ = CIL.PA_ORDER_SEQ
                        AND PM.PA_CLAIM_NO = CL.PA_CLAIM_NO
                        AND PM.PA_SHIP_NO = CL.PA_SHIP_NO)
                   WHEN PM.PA_CODE IN ('71', '72') THEN
                    (SELECT COUNT(1)
                       FROM TPAINTPCLAIMLIST CL
                      WHERE DECODE(PM.PA_ORDER_GB, '45', '40', PM.PA_ORDER_GB) =
                            CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.ORD_NO
                        AND PM.PA_ORDER_SEQ = CL.ORD_SEQ
                        AND PM.PA_CLAIM_NO = CL.CLM_NO
                        AND PM.PA_PROC_QTY = CL.CLM_QTY)
                   WHEN PM.PA_CODE IN ('81', '82') THEN
                    (SELECT COUNT(1)
                       FROM TPALTONCLAIMLIST CL
                      WHERE DECODE(PM.PA_ORDER_GB, '45', '40', PM.PA_ORDER_GB) =
                            CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.OD_NO
                        AND PM.PA_ORDER_SEQ = CL.OD_SEQ
                        AND PM.PA_CLAIM_NO = CL.CLM_NO
                        AND PM.PA_PROC_QTY = CL.OD_QTY)
                   WHEN PM.PA_CODE IN ('91', '92') THEN
                    (SELECT COUNT(1)
                       FROM TPATMONCLAIMLIST CL
                      WHERE DECODE(PM.PA_ORDER_GB, '45', '40', PM.PA_ORDER_GB) =
                            CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.TMON_ORDER_NO
                        AND PM.PA_SHIP_NO = CL.DELIVERY_NO
                        AND PM.PA_ORDER_SEQ = CL.TMON_DEAL_OPTION_NO
                        AND PM.PA_CLAIM_NO = CL.CLAIM_NO
                        AND PM.PA_PROC_QTY = CL.QTY)
                   WHEN PM.PA_CODE IN ('A1', 'A2') THEN
                    (SELECT COUNT(1)
                       FROM TPASSGORDERLIST CL
                      WHERE PM.PA_ORDER_NO = CL.ORORD_NO
                        AND PM.PA_ORDER_SEQ = CL.ORORD_ITEM_SEQ
                        AND PM.PA_CLAIM_NO = CL.ORD_NO
                        AND PM.PA_PROC_QTY = CL.DIRC_ITEM_QTY
                        AND CL.SHPP_DIV_DTL_CD = '15')
                   WHEN PM.PA_CODE IN ('B1', 'B2') THEN
                    (SELECT COUNT(1)
                       FROM TPAKAKAOCLAIMLIST CL
                      WHERE DECODE(PM.PA_ORDER_GB, '45', '40', PM.PA_ORDER_GB) =
                            CL.PA_ORDER_GB
                        AND CL.PA_ORDER_GB = '40'
                        AND PM.PA_ORDER_NO = CL.PAYMENT_ID
                        AND PM.PA_ORDER_SEQ = CL.ID
                        AND PM.PA_CLAIM_NO = CL.CLAIM_ID
                        AND PM.PA_PROC_QTY = TO_CHAR(CL.QUANTITY))
                 
                   WHEN PM.PA_CODE IN ('C1', 'C2') THEN
                    (SELECT COUNT(1)
                       FROM TPAHALFORDERLIST CL
                      WHERE DECODE(PM.PA_ORDER_GB, '45', '40', PM.PA_ORDER_GB) =
                            CL.PA_ORDER_GB
                        AND CL.PA_ORDER_GB = '40'
                        AND PM.PA_ORDER_NO = CL.ORD_NO
                        AND PM.PA_ORDER_SEQ = CL.ORD_NO_NM
                        AND PM.PA_CLAIM_NO = CL.CLAIM_NO)
                 
                   ELSE
                    1
                 END);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  /*=======================================================================================================*/

  V_FLAG      := 'P011';
  V_ERROR_MSG := '[P011] I/F 교환배송 검증 ( 제휴테이블 <-> TPAORDERM )';
  IS_EXISTED  := 0;

  /* P011 : I/F 교환배송 검증 ( 제휴테이블 <-> TPAORDERM ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT '01' AS PA_GROUP_CODE, CL.ORD_NO
            FROM TPA11STCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '41'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '41'
                      AND PM.PA_ORDER_NO = CL.ORD_NO
                      AND PM.PA_ORDER_SEQ = CL.ORD_PRD_SEQ
                      AND PM.PA_CLAIM_NO = CL.CLM_REQ_SEQ
                      AND PM.PA_PROC_QTY = CL.CLM_REQ_QTY
                      AND PM.PA_CODE IN ('11', '12')) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '46'
                      AND PM.PA_ORDER_NO = CL.ORD_NO
                      AND PM.PA_ORDER_SEQ = CL.ORD_PRD_SEQ
                      AND PM.PA_CLAIM_NO = CL.CLM_REQ_SEQ
                      AND PM.PA_PROC_QTY = CL.CLM_REQ_QTY
                      AND PM.PA_CODE IN ('11', '12')))
          
          UNION ALL
          
          SELECT '02' AS PA_GROUP_CODE, CL.CONTR_NO
            FROM TPAGMKTCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '41'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '41'
                      AND PM.PA_ORDER_NO = CL.PAY_NO
                      AND PM.PA_ORDER_SEQ = CL.CONTR_NO
                      AND PM.PA_CLAIM_NO = CL.CONTR_NO_SEQ
                      AND PM.PA_CODE IN ('21', '22')) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '46'
                      AND PM.PA_ORDER_NO = CL.PAY_NO
                      AND PM.PA_ORDER_SEQ = CL.CONTR_NO
                      AND PM.PA_CLAIM_NO = CL.CONTR_NO_SEQ
                      AND PM.PA_CODE IN ('21', '22')))
          
          UNION ALL
          
          SELECT '04' AS PA_GROUP_CODE, CL.PRODUCT_ORDER_ID
            FROM TPANAVERCLAIMLIST CL
           WHERE CL.CLAIM_STATUS = '25'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM, TPANAVERORDERLIST OL
                    WHERE PM.PA_ORDER_GB = '41'
                      AND PM.PA_ORDER_NO = OL.ORDER_ID
                      AND PM.PA_ORDER_SEQ = OL.PRODUCT_ORDER_ID
                      AND OL.PRODUCT_ORDER_ID = CL.PRODUCT_ORDER_ID
                      AND PM.PA_CODE = '41') OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM, TPANAVERORDERLIST OL
                    WHERE PM.PA_ORDER_GB = '46'
                      AND PM.PA_ORDER_NO = OL.ORDER_ID
                      AND PM.PA_ORDER_SEQ = OL.PRODUCT_ORDER_ID
                      AND OL.PRODUCT_ORDER_ID = CL.PRODUCT_ORDER_ID
                      AND PM.PA_CODE = '41'))
          
          UNION ALL
          
          SELECT '05' AS PA_GROUP_CODE, CL.ORDER_ID
            FROM TPACOPNEXCHANGELIST CL, TPACOPNEXCHANGEITEMLIST CIL
           WHERE CL.PA_ORDER_GB = '41'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CL.PA_ORDER_GB = CIL.PA_ORDER_GB
             AND CL.ORDER_ID = CIL.ORDER_ID
             AND CL.EXCHANGE_ID = CIL.EXCHANGE_ID
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '41'
                      AND PM.PA_ORDER_NO = CL.ORDER_ID
                      AND PM.PA_CLAIM_NO = CL.EXCHANGE_ID
                      AND PM.PA_CODE IN ('51', '52')) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '46'
                      AND PM.PA_ORDER_NO = CL.ORDER_ID
                      AND PM.PA_CLAIM_NO = CL.EXCHANGE_ID
                      AND PM.PA_CODE IN ('51', '52')))
          
          UNION ALL
          
          SELECT '06' AS PA_GROUP_CODE, CL.PA_ORDER_NO
            FROM TPAWEMPCLAIMLIST CL, TPAWEMPCLAIMITEMLIST CIL
           WHERE CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND CL.PA_CLAIM_NO = CIL.PA_CLAIM_NO
             AND CL.PA_ORDER_NO = CIL.PA_ORDER_NO
             AND CL.PA_SHIP_NO = CIL.PA_SHIP_NO
             AND CL.PA_ORDER_GB = CIL.PA_ORDER_GB
             AND CL.PA_ORDER_GB = '41'
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '41'
                      AND PM.PA_ORDER_NO = CL.PA_ORDER_NO
                      AND PM.PA_ORDER_SEQ = CIL.PA_ORDER_SEQ
                      AND PM.PA_CLAIM_NO = CL.PA_CLAIM_NO
                      AND PM.PA_SHIP_NO = CL.PA_SHIP_NO
                      AND PM.PA_CODE IN ('61', '62')) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '46'
                      AND PM.PA_ORDER_NO = CL.PA_ORDER_NO
                      AND PM.PA_ORDER_SEQ = CIL.PA_ORDER_SEQ
                      AND PM.PA_CLAIM_NO = CL.PA_CLAIM_NO
                      AND PM.PA_SHIP_NO = CL.PA_SHIP_NO
                      AND PM.PA_CODE IN ('61', '62')))
          
          UNION ALL
          
          SELECT '07' AS PA_GROUP_CODE, CL.ORD_NO
            FROM TPAINTPCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '41'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '41'
                      AND PM.PA_ORDER_NO = CL.ORD_NO
                      AND PM.PA_ORDER_SEQ = CL.ORD_SEQ
                      AND PM.PA_CLAIM_NO = CL.CLM_NO
                      AND PM.PA_CODE IN ('71', '72')
                      AND PM.PA_PROC_QTY = CL.CLM_QTY) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '46'
                      AND PM.PA_ORDER_NO = CL.ORD_NO
                      AND PM.PA_ORDER_SEQ = CL.ORD_SEQ
                      AND PM.PA_CLAIM_NO = CL.CLM_NO
                      AND PM.PA_CODE IN ('71', '72')
                      AND PM.PA_PROC_QTY = CL.CLM_QTY))
          
          UNION ALL
          
          SELECT '08' AS PA_GROUP_CODE, CL.OD_NO
            FROM TPALTONCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '41'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '41'
                      AND PM.PA_ORDER_NO = CL.OD_NO
                      AND PM.PA_ORDER_SEQ = CL.OD_SEQ
                      AND PM.PA_CLAIM_NO = CL.CLM_NO
                      AND PM.PA_CODE IN ('81', '82')
                      AND PM.PA_PROC_QTY = CL.OD_QTY) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '46'
                      AND PM.PA_ORDER_NO = CL.OD_NO
                      AND PM.PA_ORDER_SEQ = CL.OD_SEQ
                      AND PM.PA_CLAIM_NO = CL.CLM_NO
                      AND PM.PA_CODE IN ('81', '82')
                      AND PM.PA_PROC_QTY = CL.OD_QTY))
          
          UNION ALL
          
          SELECT '09' AS PA_GROUP_CODE, CL.TMON_ORDER_NO
            FROM TPATMONCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '41'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '41'
                      AND PM.PA_ORDER_NO = CL.TMON_ORDER_NO
                      AND PM.PA_SHIP_NO = CL.DELIVERY_NO
                      AND PM.PA_ORDER_SEQ = CL.TMON_DEAL_OPTION_NO
                      AND PM.PA_CLAIM_NO = CL.CLAIM_NO
                      AND PM.PA_CODE IN ('91', '92')
                      AND PM.PA_PROC_QTY = CL.QTY) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '46'
                      AND PM.PA_ORDER_NO = CL.TMON_ORDER_NO
                      AND PM.PA_SHIP_NO = CL.DELIVERY_NO
                      AND PM.PA_ORDER_SEQ = CL.TMON_DEAL_OPTION_NO
                      AND PM.PA_CLAIM_NO = CL.CLAIM_NO
                      AND PM.PA_CODE IN ('91', '92')
                      AND PM.PA_PROC_QTY = CL.QTY))
          
          UNION ALL
          
          SELECT '10' AS PA_GROUP_CODE, CL.ORORD_NO
            FROM TPASSGCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '41'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '41'
                      AND PM.PA_ORDER_NO = CL.ORORD_NO
                      AND PM.PA_SHIP_NO = CL.SHPP_NO
                      AND PM.PA_SHIP_SEQ = CL.SHPP_SEQ
                      AND PM.PA_ORDER_SEQ = CL.ORORD_ITEM_SEQ
                      AND PM.PA_CLAIM_NO = CL.ORD_NO
                      AND PM.PA_CODE IN ('A1', 'A2')
                      AND PM.PA_PROC_QTY = CL.DIRC_ITEM_QTY) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '46'
                      AND PM.PA_ORDER_NO = CL.ORORD_NO
                      AND PM.PA_ORDER_SEQ = CL.ORORD_ITEM_SEQ
                      AND PM.PA_CLAIM_NO = CL.ORD_NO
                      AND PM.PA_CODE IN ('A1', 'A2')
                      AND PM.PA_PROC_QTY = CL.DIRC_ITEM_QTY))
          
          UNION ALL
          
          SELECT '11' AS PA_GROUP_CODE, CL.PAYMENT_ID
            FROM TPAKAKAOCLAIMLIST CL
           WHERE CL.PA_ORDER_GB = '41'
             AND CL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND (NOT EXISTS (SELECT 1
                                FROM TPAORDERM PM
                               WHERE PM.PA_ORDER_GB = '41'
                                 AND PM.PA_ORDER_NO = CL.PAYMENT_ID
                                 AND PM.PA_ORDER_SEQ = CL.ID
                                 AND PM.PA_CLAIM_NO = CL.CLAIM_ID
                                 AND PM.PA_CODE IN ('B1', 'B2')
                                 AND PM.PA_PROC_QTY = TO_CHAR(CL.QUANTITY)) OR
                  NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '46'
                      AND PM.PA_ORDER_NO = CL.PAYMENT_ID
                      AND PM.PA_ORDER_SEQ = CL.ID
                      AND PM.PA_CLAIM_NO = CL.CLAIM_ID
                      AND PM.PA_CODE IN ('B1', 'B2')
                      AND PM.PA_PROC_QTY = TO_CHAR(CL.QUANTITY)))
          
          UNION ALL
          
          SELECT '12' AS PA_GROUP_CODE, HL.ORD_NO
            FROM TPAHALFORDERLIST HL
           WHERE HL.PA_ORDER_GB = '41'
             AND HL.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND (NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '41'
                      AND PM.PA_ORDER_NO = HL.ORD_NO
                      AND PM.PA_ORDER_SEQ = HL.ORD_NO_NM
                      AND PM.PA_CLAIM_NO = HL.CLAIM_NO
                      AND PM.PA_CODE IN ('C1', 'C2')) OR NOT EXISTS
                  (SELECT 1
                     FROM TPAORDERM PM
                    WHERE PM.PA_ORDER_GB = '46'
                      AND PM.PA_ORDER_NO = HL.ORD_NO
                      AND PM.PA_ORDER_SEQ = HL.ORD_NO_NM
                      AND PM.PA_CLAIM_NO = HL.CLAIM_NO
                      AND PM.PA_CODE IN ('C1', 'C2')))
          
          );

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  /*=======================================================================================================*/

  V_FLAG      := 'PI012';
  V_ERROR_MSG := '[PI12] I/F 교환회수 취소, 교환배송 취소 검증 ( TPAORDERM <-> 제휴테이블 )';
  IS_EXISTED  := 0;

  /*PI012 : I/F 교환회수 취소, 교환배송 취소 검증 ( TPAORDERM <-> 제휴테이블 ) */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.PA_ORDER_GB,
                 PM.PA_ORDER_NO,
                 PM.PA_ORDER_SEQ,
                 PM.PA_SHIP_NO,
                 PM.PA_CLAIM_NO,
                 PM.PA_CODE,
                 PM.PA_PROC_QTY
            FROM TPAORDERM PM
           WHERE PM.PA_ORDER_GB IN ('41', '46')
             AND PM.INSERT_DATE >= TO_DATE(V_FROMDATE, 'YYYYMMDDHH24MISS')
             AND 0 = CASE
                   WHEN PM.PA_CODE IN ('11', '12') THEN
                    (SELECT COUNT(1)
                       FROM TPA11STCLAIMLIST CL
                      WHERE CL.PA_ORDER_GB =
                            DECODE(PM.PA_ORDER_GB, '46', '41', PM.PA_ORDER_GB)
                        AND CL.ORD_NO = PM.PA_ORDER_NO
                        AND CL.ORD_PRD_SEQ = PM.PA_ORDER_SEQ
                        AND CL.CLM_REQ_SEQ = PM.PA_CLAIM_NO
                        AND CL.CLM_REQ_QTY = PM.PA_PROC_QTY)
                   WHEN PM.PA_CODE IN ('21', '22') THEN
                    (SELECT COUNT(1)
                       FROM TPAGMKTCLAIMLIST CL
                      WHERE CL.PA_ORDER_GB =
                            DECODE(PM.PA_ORDER_GB, '46', '41', PM.PA_ORDER_GB)
                        AND CL.PAY_NO = PM.PA_ORDER_NO
                        AND CL.CONTR_NO = PM.PA_ORDER_SEQ
                        AND CL.CONTR_NO_SEQ = PM.PA_CLAIM_NO)
                   WHEN PM.PA_CODE = ('41') THEN
                    (SELECT COUNT(1)
                       FROM TPANAVERCLAIMLIST CL, TPANAVERORDERLIST OL
                      WHERE PM.PA_SHIP_NO IS NULL
                        AND PM.PA_SHIP_SEQ IS NULL
                        AND OL.ORDER_ID = PM.PA_ORDER_NO
                        AND OL.PRODUCT_ORDER_ID = PM.PA_ORDER_SEQ
                        AND OL.PRODUCT_ORDER_ID = CL.PRODUCT_ORDER_ID
                        AND OL.PRODUCT_ORDER_STATUS = '10'
                        AND CL.CLAIM_SEQ = OL.CLAIM_SEQ)
                   WHEN PM.PA_CODE IN ('51', '52') THEN
                    (SELECT COUNT(1)
                       FROM TPACOPNEXCHANGELIST     CL,
                            TPACOPNEXCHANGEITEMLIST CIL
                      WHERE CL.PA_ORDER_GB = CIL.PA_ORDER_GB
                        AND CL.ORDER_ID = CIL.ORDER_ID
                        AND CL.EXCHANGE_ID = CIL.EXCHANGE_ID
                        AND CL.PA_ORDER_GB = CIL.PA_ORDER_GB
                        AND CL.ORDER_ID = PM.PA_ORDER_NO
                        AND CL.PA_ORDER_GB =
                            DECODE(PM.PA_ORDER_GB, '46', '41', PM.PA_ORDER_GB)
                        AND CL.EXCHANGE_ID = PM.PA_CLAIM_NO
                        AND CIL.ITEM_SEQ = PM.PA_ORDER_SEQ)
                   WHEN PM.PA_CODE IN ('61', '62') THEN
                    (SELECT COUNT(1)
                       FROM TPAWEMPCLAIMLIST CL, TPAWEMPCLAIMITEMLIST CIL
                      WHERE CL.PA_CLAIM_NO = CIL.PA_CLAIM_NO
                        AND CL.PA_ORDER_NO = CIL.PA_ORDER_NO
                        AND CL.PA_SHIP_NO = CIL.PA_SHIP_NO
                        AND CL.PA_ORDER_GB = CIL.PA_ORDER_GB
                        AND DECODE(PM.PA_ORDER_GB, '46', '41', PM.PA_ORDER_GB) =
                            CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.PA_ORDER_NO
                        AND PM.PA_ORDER_SEQ = CIL.PA_ORDER_SEQ
                        AND PM.PA_CLAIM_NO = CL.PA_CLAIM_NO
                        AND PM.PA_SHIP_NO = CL.PA_SHIP_NO)
                   WHEN PM.PA_CODE IN ('71', '72') THEN
                    (SELECT COUNT(1)
                       FROM TPAINTPCLAIMLIST CL
                      WHERE DECODE(PM.PA_ORDER_GB, '46', '41', PM.PA_ORDER_GB) =
                            CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.ORD_NO
                        AND PM.PA_ORDER_SEQ = CL.ORD_SEQ
                        AND PM.PA_CLAIM_NO = CL.CLM_NO
                        AND PM.PA_PROC_QTY = CL.CLM_QTY)
                   WHEN PM.PA_CODE IN ('81', '82') THEN
                    (SELECT COUNT(1)
                       FROM TPALTONCLAIMLIST CL
                      WHERE DECODE(PM.PA_ORDER_GB, '46', '41', PM.PA_ORDER_GB) =
                            CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.OD_NO
                        AND PM.PA_ORDER_SEQ = CL.OD_SEQ
                        AND PM.PA_CLAIM_NO = CL.CLM_NO
                        AND PM.PA_PROC_QTY = CL.OD_QTY)
                   WHEN PM.PA_CODE IN ('91', '92') THEN
                    (SELECT COUNT(1)
                       FROM TPATMONCLAIMLIST CL
                      WHERE DECODE(PM.PA_ORDER_GB, '46', '41', PM.PA_ORDER_GB) =
                            CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.TMON_ORDER_NO
                        AND PM.PA_SHIP_NO = CL.DELIVERY_NO
                        AND PM.PA_ORDER_SEQ = CL.TMON_DEAL_OPTION_NO
                        AND PM.PA_CLAIM_NO = CL.CLAIM_NO
                        AND PM.PA_PROC_QTY = CL.QTY)
                   WHEN PM.PA_CODE IN ('A1', 'A2') AND PM.PA_ORDER_GB = '46' THEN
                    (SELECT COUNT(1)
                       FROM TPASSGCLAIMLIST CL
                      WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.ORORD_NO
                        AND PM.PA_SHIP_NO = CL.SHPP_NO
                        AND PM.PA_SHIP_SEQ = CL.SHPP_SEQ
                        AND PM.PA_ORDER_SEQ = CL.ORORD_ITEM_SEQ
                        AND PM.PA_CLAIM_NO = CL.ORD_NO
                        AND PM.PA_PROC_QTY = CL.DIRC_ITEM_QTY)
                   WHEN PM.PA_CODE IN ('A1', 'A2') AND PM.PA_ORDER_GB = '41' THEN
                    (SELECT COUNT(1)
                       FROM TPASSGORDERLIST OL
                      WHERE 1 = 1
                        AND PM.PA_ORDER_NO = OL.ORORD_NO
                        AND PM.PA_SHIP_NO = OL.SHPP_NO
                        AND PM.PA_SHIP_SEQ = OL.SHPP_SEQ
                        AND PM.PA_ORDER_SEQ = OL.ORORD_ITEM_SEQ
                        AND PM.PA_PROC_QTY = OL.DIRC_ITEM_QTY
                        AND OL.SHPP_DIV_DTL_CD = '15')
                   WHEN PM.PA_CODE IN ('B1', 'B2') AND PM.PA_ORDER_GB = '41' THEN
                    (SELECT COUNT(1)
                       FROM TPAKAKAOCLAIMLIST CL
                      WHERE PM.PA_ORDER_GB = CL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = CL.PAYMENT_ID
                        AND PM.PA_ORDER_SEQ = CL.ID
                        AND PM.PA_CLAIM_NO = CL.CLAIM_ID
                        AND PM.PA_PROC_QTY = TO_CHAR(CL.QUANTITY))
                   WHEN PM.PA_CODE IN ('C1', 'C2') AND PM.PA_ORDER_GB = '41' THEN
                    (SELECT COUNT(1)
                       FROM TPAHALFORDERLIST HL
                      WHERE PM.PA_ORDER_GB = HL.PA_ORDER_GB
                        AND PM.PA_ORDER_NO = HL.ORD_NO
                        AND PM.PA_ORDER_SEQ = HL.ORD_NO_NM
                        AND PM.PA_CLAIM_NO = HL.CLAIM_NO)
                   ELSE
                    1
                 END);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  /*=======================================================================================================*/

  V_FLAG      := 'PI013';
  V_ERROR_MSG := '[PI013] 제휴 주문 생성 불가건';
  IS_EXISTED  := 0;
  /*PI013 : 제휴 주문 생성 불가건 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.PA_CODE, PM.PA_ORDER_NO
            FROM TPAORDERM PM
           WHERE PM.ORDER_NO IS NULL
             AND PM.PRE_CANCEL_YN = '0'
             AND PM.PA_ORDER_GB = '10'
             AND PM.INSERT_DATE < SYSDATE - INTERVAL '5'
           MINUTE
             AND NOT EXISTS (SELECT 1
                    FROM TPAORDERM PM2
                   WHERE PM2.PA_CODE = PM.PA_CODE
                     AND PM2.PA_ORDER_GB = '20'
                     AND PM2.PA_ORDER_NO = PM.PA_ORDER_NO
                     AND PM2.PA_ORDER_SEQ = PM.PA_ORDER_SEQ
                     AND PM2.PA_SHIP_NO = PM.PA_SHIP_NO
                     AND PM2.PRE_CANCEL_YN = '1')
           ORDER BY PM.PA_CODE);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'PI014';
  V_ERROR_MSG := '[PI014] 도서산간/제주 배송 불가 주문';
  IS_EXISTED  := 0;

  /*[PI014] 도서산간/제주 배송 불가 주문
  3일 지난 주문 중 상태가 그대로인경우 메일 전달하기로함.
  11번가, 인터파크 : jheawon@willvi.co.kr, mesun0420@willvi.co.kr
  이베이, 네이버, 쿠팡, 위메프 : stoacs01@skstoa.com, stoacs02@skstoa.com, BF483@hintsmtp.skbroadband.com
  CC : good2cu@sk.com
  */

  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT PM.PA_CODE, TD.ORDER_NO, TD.ORDER_DATE
            FROM TPAORDERM PM, TORDERDT TD, TRECEIVER TR, VPOST VP
           WHERE 1 = 1
             AND PM.PA_ORDER_GB = TD.ORDER_GB
             AND PM.ORDER_NO = TD.ORDER_NO
             AND PM.ORDER_G_SEQ = TD.ORDER_G_SEQ
             AND PM.ORDER_D_SEQ = TD.ORDER_D_SEQ
             AND PM.ORDER_W_SEQ = TD.ORDER_W_SEQ
             AND TD.CUST_NO = TR.CUST_NO
             AND TD.RECEIVER_SEQ = TR.RECEIVER_SEQ
             AND TR.ROAD_ADDR_YN = VP.ROAD_ADDR_YN
             AND CASE
                   WHEN TR.ROAD_ADDR_YN = '1' THEN
                    TR.ROAD_ADDR_NO
                   ELSE
                    TR.RECEIVER_POST
                 END = VP.ROAD_ADDR_NO
             AND CASE
                   WHEN TR.ROAD_ADDR_YN = '1' THEN
                    VP.POST_SEQ
                   ELSE
                    TR.RECEIVER_POST_SEQ
                 END = VP.POST_SEQ
             AND TD.DO_FLAG < 40
             AND TD.SYSLAST > 0
             AND PM.INSERT_DATE >= SYSDATE - 14
             AND TD.ORDER_DATE < TRUNC(SYSDATE) - 2
             AND EXISTS (SELECT 1
                    FROM TDELYNOAREA TN
                   WHERE TN.GOODS_CODE = TD.GOODS_CODE
                     AND TN.AREA_GB = VP.AREA_GB)
             AND PM.ORDER_NO NOT IN ('20220809735843' -- 8/12
                                    ,'20220811093926' -- 8/16
                                     ));

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'PI015';
  V_ERROR_MSG := '[PI015] 제휴 W_SEQ 확인';
  IS_EXISTED  := 0;
  /*PI015 : 제휴 W_SEQ 확인 */
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT T.PA_CODE, T.PA_ORDER_GB, T.PA_ORDER_NO, T.ORDER_NO
            FROM TPAORDERM T
           WHERE T.CREATE_DATE > TRUNC(SYSDATE) - 5
             AND T.PA_ORDER_GB != '10'
             AND T.ORDER_W_SEQ = '001');

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'PI016';
  V_ERROR_MSG := '[PI016] 제휴 동기화배치 1시간 이상 실행';
  IS_EXISTED  := 0;
  /*[PI016] 제휴 동기화배치 1시간 이상 실행*/
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT T.MODIFY_DATE
            FROM TCLOSEHISTORY T
           WHERE T.PRG_ID = 'IF_PACOMMON_00_002'
             AND T.PROC_YN = '1'
             AND T.MODIFY_DATE < SYSDATE - INTERVAL '1' HOUR);

  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;

  V_FLAG      := 'PI017';
  V_ERROR_MSG := '[PI017] 모바일고마진상품 중복 입점 확인';
  IS_EXISTED  := 0;
  /*[PI017] 모바일고마진상품 중복 입점 확인*/
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT t.pa_code, t.goods_Code
            FROM tpa11stgoods t
           WHERE t.pa_code = '11'
             AND EXISTS (SELECT 1
                    FROM tpa11stgoods t2, TGOODS TG
                   WHERE t.goods_Code = t2.goods_Code
                     AND T2.GOODS_CODE = TG.GOODS_CODE
                     AND TG.SALE_GB = '00'
                     AND TG.INSERT_DATE > TRUNC(SYSDATE) - 5
                     AND t2.pa_code = '12')
          UNION
          SELECT t.pa_code, t.goods_Code
            FROM tpagmktgoods t
           WHERE t.pa_code = '21'
             AND EXISTS (SELECT 1
                    FROM tpagmktgoods t2, TGOODS TG
                   WHERE t.goods_Code = t2.goods_Code
                     AND T2.GOODS_CODE = TG.GOODS_CODE
                     AND TG.SALE_GB = '00'
                     AND TG.INSERT_DATE > TRUNC(SYSDATE) - 5
                     AND t2.pa_code = '22')
          UNION
          SELECT t.pa_code, t.goods_Code
            FROM tpacopngoods t
           WHERE t.pa_code = '51'
             AND EXISTS (SELECT 1
                    FROM tpacopngoods t2, TGOODS TG
                   WHERE t.goods_Code = t2.goods_Code
                     AND T2.GOODS_CODE = TG.GOODS_CODE
                     AND TG.SALE_GB = '00'
                     AND TG.INSERT_DATE > TRUNC(SYSDATE) - 5
                     AND t2.pa_code = '52')
          UNION
          SELECT t.pa_code, t.goods_Code
            FROM tpawempgoods t
           WHERE t.pa_code = '61'
             AND EXISTS (SELECT 1
                    FROM tpawempgoods t2, TGOODS TG
                   WHERE t.goods_Code = t2.goods_Code
                     AND T2.GOODS_CODE = TG.GOODS_CODE
                     AND TG.SALE_GB = '00'
                     AND TG.INSERT_DATE > TRUNC(SYSDATE) - 5
                     AND t2.pa_code = '62')
          UNION
          SELECT t.pa_code, t.goods_Code
            FROM tpaintpgoods t
           WHERE t.pa_code = '71'
             AND EXISTS (SELECT 1
                    FROM tpaintpgoods t2, TGOODS TG
                   WHERE t.goods_Code = t2.goods_Code
                     AND T2.GOODS_CODE = TG.GOODS_CODE
                     AND TG.SALE_GB = '00'
                     AND TG.INSERT_DATE > TRUNC(SYSDATE) - 5
                     AND t2.pa_code = '72')
          UNION
          SELECT t.pa_code, t.goods_Code
            FROM tpaltongoods t
           WHERE t.pa_code = '81'
             AND EXISTS (SELECT 1
                    FROM tpaltongoods t2, TGOODS TG
                   WHERE t.goods_Code = t2.goods_Code
                     AND T2.GOODS_CODE = TG.GOODS_CODE
                     AND TG.SALE_GB = '00'
                     AND TG.INSERT_DATE > TRUNC(SYSDATE) - 5
                     AND t2.pa_code = '82')
          UNION
          SELECT t.pa_code, t.goods_Code
            FROM tpassggoods t
           WHERE t.pa_code = 'A1'
             AND EXISTS (SELECT 1
                    FROM tpassggoods t2, TGOODS TG
                   WHERE t.goods_Code = t2.goods_Code
                     AND T2.GOODS_CODE = TG.GOODS_CODE
                     AND TG.SALE_GB = '00'
                     AND TG.INSERT_DATE > TRUNC(SYSDATE) - 5
                     AND t2.pa_code = 'A2')
          UNION
          SELECT t.pa_code, t.goods_Code
            FROM tpakakaogoods t
           WHERE t.pa_code = 'B1'
             AND EXISTS (SELECT 1
                    FROM tpakakaogoods t2, TGOODS TG
                   WHERE t.goods_Code = t2.goods_Code
                     AND T2.GOODS_CODE = TG.GOODS_CODE
                     AND TG.SALE_GB = '00'
                     AND TG.INSERT_DATE > TRUNC(SYSDATE) - 5
                     AND t2.pa_code = 'B2')
          
          UNION
          SELECT t.pa_code, t.goods_Code
            FROM TPAHALFGOODS t
           WHERE t.pa_code = 'C1'
             AND EXISTS (SELECT 1
                    FROM TPAHALFGOODS t2, TGOODS TG
                   WHERE t.goods_Code = t2.goods_Code
                     AND T2.GOODS_CODE = TG.GOODS_CODE
                     AND TG.SALE_GB = '00'
                     AND TG.INSERT_DATE > TRUNC(SYSDATE) - 5
                     AND t2.pa_code = 'C2')
          
          );
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  
  V_FLAG      := 'PI018';
  V_ERROR_MSG := '[PI018] 제휴 SD,ARS,일시불 가격검증';
  IS_EXISTED  := 0;
  /*[PI018] 제휴 SD,ARS,일시불 가격검증*/
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT XX.*
            FROM(   --SD 쿠폰 가격 검증
                    SELECT PM.ORDER_NO, PM.PA_GROUP_CODE, PM.PA_CODE, PM.INSERT_DATE , PM.PA_ORDER_NO
                      FROM TPAORDERM PM
                     INNER JOIN TORDERDT OD ON PM.ORDER_NO =  OD.ORDER_NO AND PM.ORDER_G_SEQ = OD.ORDER_G_SEQ AND PM.ORDER_D_SEQ = OD.ORDER_D_SEQ AND PM.ORDER_W_SEQ = OD.ORDER_W_SEQ
                     INNER JOIN TPAGOODSPRICEAPPLY GP ON GP.PA_CODE = PM.PA_CODE AND GP.GOODS_CODE = OD.GOODS_CODE AND GP.ROWID = (SELECT /*+ INDEX_DESC (PP PK_TPAGOODSPRICEAPPLY)*/
                                                                                                                                       PP.ROWID
                                                                                                                                   FROM TPAGOODSPRICEAPPLY PP
                                                                                                                                  WHERE PP.GOODS_CODE = GP.GOODS_CODE
                                                                                                                                    AND PP.PA_CODE    = PM.PA_CODE
                                                                                                                                    AND PP.APPLY_DATE < OD.ORDER_DATE 
                                                                                                                                    AND PP.TRANS_DATE < OD.ORDER_DATE  --OD.ORDER_DATE
                                                                                                                                    AND PP.TRANS_DATE IS NOT NULL
                                                                                                                                    AND PM.PA_GROUP_CODE = PP.PA_GROUP_CODE
                                                                                                                                    AND ROWNUM = 1)
                                                                                                                                    AND GP.PA_GROUP_CODE = PM.PA_GROUP_CODE
                     WHERE PM.PA_ORDER_GB = '10'
                       AND NOT EXISTS (SELECT 'X' FROM  TPAGOODSPRICEAPPLY ZZZ WHERE ZZZ.GOODS_CODE = OD.GOODS_CODE AND ZZZ.PA_CODE = PM.PA_CODE AND ZZZ.PA_GROUP_CODE = PM.PA_GROUP_CODE 
                                                                                 AND ZZZ.APPLY_DATE < OD.ORDER_DATE 
                                                                                -- AND ZZZ.TRANS_DATE > OD.ORDER_DATE + 1/64  -- 연동 시점차 고려
                                                                                 AND ZZZ.TRANS_DATE IS NOT NULL
                                                                                 AND ZZZ.COUPON_DC_AMT = 0
                                                                                 )
                       AND NOT EXISTS (SELECT 'X' FROM TORDERPROMO OP WHERE OP.ORDER_NO = OD.ORDER_NO AND OP.ORDER_G_SEQ = OD.ORDER_G_SEQ AND OP.DO_TYPE = '30' ) 
                       AND GP.COUPON_PROMO_NO IS NOT NULL
                       AND OD.DC_AMT > 0 --이거 빼는 케이스도 고려해봐야함, 상품 가격 연동 실패
                       
                       UNION ALL 
                       
                     --ARS 가격 검증   
                     SELECT PM.ORDER_NO, PM.PA_GROUP_CODE, PM.PA_CODE,  PM.INSERT_DATE , PM.PA_ORDER_NO
                       FROM TPAORDERM PM
                      INNER JOIN TORDERDT OD ON PM.ORDER_NO =  OD.ORDER_NO AND PM.ORDER_G_SEQ = OD.ORDER_G_SEQ AND PM.ORDER_D_SEQ = OD.ORDER_D_SEQ AND PM.ORDER_W_SEQ = OD.ORDER_W_SEQ
                      INNER JOIN TPAGOODSPRICEAPPLY GP ON GP.PA_CODE = PM.PA_CODE AND GP.GOODS_CODE = OD.GOODS_CODE AND GP.ROWID = (SELECT /*+ INDEX_DESC (PP PK_TPAGOODSPRICEAPPLY)*/
                                                                                                                                       PP.ROWID
                                                                                                                                   FROM TPAGOODSPRICEAPPLY PP
                                                                                                                                  WHERE PP.GOODS_CODE = GP.GOODS_CODE
                                                                                                                                    AND PP.PA_CODE    = PM.PA_CODE
                                                                                                                                    AND PP.APPLY_DATE < OD.ORDER_DATE 
                                                                                                                                    AND PP.TRANS_DATE < OD.ORDER_DATE  --OD.ORDER_DATE
                                                                                                                                    AND PP.TRANS_DATE IS NOT NULL
                                                                                                                                    AND PM.PA_GROUP_CODE = PP.PA_GROUP_CODE
                                                                                                                                    AND ROWNUM = 1)
                                                                                                                                    AND GP.PA_GROUP_CODE = PM.PA_GROUP_CODE
                     WHERE PM.PA_ORDER_GB = '10'
                       AND NOT EXISTS (SELECT 'X' FROM  TPAGOODSPRICEAPPLY ZZZ WHERE ZZZ.GOODS_CODE = OD.GOODS_CODE AND ZZZ.PA_CODE = PM.PA_CODE AND ZZZ.PA_GROUP_CODE = PM.PA_GROUP_CODE 
                                                                                 AND ZZZ.APPLY_DATE < OD.ORDER_DATE 
                                                                                 --AND ZZZ.TRANS_DATE > OD.ORDER_DATE + 1/64  -- 연동 시점차 고려
                                                                                 AND ZZZ.TRANS_DATE IS NOT NULL
                                                                                 AND ZZZ.ARS_DC_AMT = 0
                                                                                 )
                       AND NOT EXISTS (SELECT 'X' FROM TORDERPROMO OP WHERE OP.ORDER_NO = OD.ORDER_NO AND OP.ORDER_G_SEQ = OD.ORDER_G_SEQ AND OP.DO_TYPE = '90' ) 
                       AND GP.ARS_DC_AMT > 0 
                       AND OD.DC_AMT > 0 --이거 빼는 케이스도 고려해봐야함, 상품 가격 연동 실패
                       
                       UNION ALL 
                       
                     -- 일시불 가격 검증 
                     SELECT PM.ORDER_NO, PM.PA_GROUP_CODE, PM.PA_CODE , PM.INSERT_DATE , PM.PA_ORDER_NO
                       FROM TPAORDERM PM
                      INNER JOIN TORDERDT OD ON PM.ORDER_NO =  OD.ORDER_NO AND PM.ORDER_G_SEQ = OD.ORDER_G_SEQ AND PM.ORDER_D_SEQ = OD.ORDER_D_SEQ AND PM.ORDER_W_SEQ = OD.ORDER_W_SEQ
                      INNER JOIN TPAGOODSPRICEAPPLY GP ON GP.PA_CODE = PM.PA_CODE AND GP.GOODS_CODE = OD.GOODS_CODE AND GP.ROWID = (SELECT /*+ INDEX_DESC (PP PK_TPAGOODSPRICEAPPLY)*/
                                                                                                                                       PP.ROWID
                                                                                                                                   FROM TPAGOODSPRICEAPPLY PP
                                                                                                                                  WHERE PP.GOODS_CODE = GP.GOODS_CODE
                                                                                                                                    AND PP.PA_CODE    = PM.PA_CODE
                                                                                                                                    AND PP.APPLY_DATE < OD.ORDER_DATE 
                                                                                                                                    AND PP.TRANS_DATE < OD.ORDER_DATE  --OD.ORDER_DATE
                                                                                                                                    AND PP.TRANS_DATE IS NOT NULL
                                                                                                                                    AND PM.PA_GROUP_CODE = PP.PA_GROUP_CODE
                                                                                                                                    AND ROWNUM = 1)
                                                                                                                                    AND GP.PA_GROUP_CODE = PM.PA_GROUP_CODE
                     WHERE PM.PA_ORDER_GB = '10'
                       AND PM.INSERT_DATE > SYSDATE - 10
                       AND NOT EXISTS (SELECT 'X' FROM  TPAGOODSPRICEAPPLY ZZZ WHERE ZZZ.GOODS_CODE = OD.GOODS_CODE AND ZZZ.PA_CODE = PM.PA_CODE AND ZZZ.PA_GROUP_CODE = PM.PA_GROUP_CODE 
                                                                                 AND ZZZ.APPLY_DATE < OD.ORDER_DATE 
                                                                                -- AND ZZZ.TRANS_DATE > OD.ORDER_DATE + 1/64  -- 연동 시점차 고려
                                                                                 AND ZZZ.TRANS_DATE IS NOT NULL
                                                                                 AND ZZZ.LUMP_SUM_DC_AMT = 0
                                                                                 )
                       AND NOT EXISTS (SELECT 'X' FROM TORDERPROMO OP WHERE OP.ORDER_NO = OD.ORDER_NO AND OP.ORDER_G_SEQ = OD.ORDER_G_SEQ AND OP.DO_TYPE = '70' ) 
                       AND GP.LUMP_SUM_DC_AMT > 0 
                       AND OD.DC_AMT > 0
                      ) XX
                 WHERE XX.INSERT_DATE > SYSDATE - 2);
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF;
  
  V_FLAG      := 'PI019';
  V_ERROR_MSG := '[PI019] 업체분담금/가격 QTY 비교, 잘못된 수기처리 감지';
  IS_EXISTED  := 0;
  /*[PI019] 업체분담금/가격 QTY 비교, 잘못된 수기처리 감지*/
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT * 
            FROM TORDERDT OD 
           LEFT OUTER JOIN TORDERPROMO OP ON OD.ORDER_NO = OP.ORDER_NO AND OD.ORDER_G_SEQ = OP.ORDER_G_SEQ 
           WHERE OD.ORDER_DATE > SYSDATE - 1
             AND OD.MEDIA_CODE LIKE 'EX%' 
             AND ((OP.PROC_COST > OP.ENTP_PROC_COST + OP.OWN_PROC_COST)  OR  (OP.PROC_AMT != ROUND(OP.PROC_COST * OD.ORDER_QTY)) )
             AND OD.ORDER_NO NOT IN ('20240429904922'));
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF; 

  /*=======================================================================================================*/
  V_FLAG      := 'PI020';
  V_ERROR_MSG := '[PI020] 사은품주문 잘못된 배송비정책 검출';
  IS_EXISTED  := 0;
    
  --[PI020] 사은품주문 잘못된 배송비정책 검출

  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT /*+LEADING(OS)*/DISTINCT OS.ORDER_NO, OS.SHIP_COST_NO as OS_SHIP_COST_NO
                                   , OA.SHIP_COST_NO as OA_SHIP_COST_NO
            FROM TORDERSHIPCOST OS, TORDERCOSTAPPLY OA, TORDERDT OD
           WHERE OS.ORDER_NO = OA.ORDER_NO
             AND OS.SEQ = OA.APPLY_COST_SEQ
             AND OS.ORDER_NO = OD.ORDER_NO
             AND OS.INSERT_DATE > SYSDATE - 3
             AND OS.SHIP_COST_NO <> '6604'
             AND OD.ORDER_GB = '10'
             AND OD.MEDIA_CODE LIKE 'EX%'
             AND OA.SHIP_COST_NO <> 0
             AND NOT EXISTS (SELECT 1
                               FROM TORDERCOSTAPPLY OA2
                              WHERE OS.ORDER_NO = OA2.ORDER_NO
                                AND OS.SEQ = OA2.APPLY_COST_SEQ
                                AND OS.SHIP_COST_NO = OA2.SHIP_COST_NO
                                AND OA2.SHIP_COST_NO <> 0)
             AND EXISTS (SELECT 1
                                FROM TORDERDT OD2
                               WHERE OD.ORDER_NO = OD2.ORDER_NO
                                 AND OD2.ORDER_D_SEQ > 1));
           
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF; 
  
  /*=======================================================================================================*/
  V_FLAG      := 'PI021';
  V_ERROR_MSG := '[PI021] 주문 출하지시 이전 상태이나 반품 접수된 건 검출';
  IS_EXISTED  := 0;
    
  --[PI021] 주문 출하지시 이전 상태이나 반품 접수된 건 검출
  SELECT CASE
           WHEN COUNT(1) > 0 THEN
            1
           ELSE
            0
         END DATA_CHECK
    INTO IS_EXISTED
    FROM (SELECT *
            FROM TORDERDT O
           WHERE O.DO_FLAG < '30'
             AND O.ORDER_DATE >= SYSDATE - 15
             AND O.ORDER_D_SEQ = '001'
             AND O.MEDIA_CODE LIKE 'EX%'
             AND EXISTS (SELECT '1'
                           FROM TCLAIMDT C
                          WHERE C.ORDER_NO = O.ORDER_NO
                            AND C.ORDER_G_SEQ = O.ORDER_G_SEQ
                            AND C.ORDER_D_SEQ = O.ORDER_D_SEQ));
                            
  DBMS_OUTPUT.PUT_LINE('V_FLAG : ' || V_FLAG || ', TIME : ' || TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS'));
  IF (IS_EXISTED > 0) THEN
    INSERT_ERROR_LOG();
  END IF; 
  
  /*=======================================================================================================*/


  -- execution time  ------------------------

  PKG_COMMON.EXP_PRNS(V_PROG_ID, '');

  SELECT V_FLAG || 'Execution Time = ' ||
         FLOOR((SYSDATE - TO_DATE(V_SYSDATETIME, 'YYYYMMDDHH24MISS')) * 24 * 60) ||
         ' min  ' ||
         FLOOR(MOD(((SYSDATE - TO_DATE(V_SYSDATETIME, 'YYYYMMDDHH24MISS')) * 24 * 60 * 60),
                   60)) || ' second'
    INTO V_MSG
    FROM DUAL;

  PKG_COMMON.EXP_PRNS(V_PROG_ID, V_MSG);
  PKG_COMMON.EXP_PRNS(V_PROG_ID, '');

  PKG_COMMON.HISTORY_SUCCESS(V_PROG_ID,
                             V_USER_ID,
                             TO_CHAR((SYSDATE - 1), 'YYYYMMDD'),
                             '',
                             V_MSG);

  -- Commit
  COMMIT;

  -- return result
  OUT_MSG := V_MSG;

  -- close file
  UTL_FILE.FCLOSE_ALL;

  -- ERROR
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE(V_FLAG || SQLERRM);
    V_DB_ERROR_MSG := SUBSTR('11st' || V_FLAG || SQLERRM, 1, 200);
    OUT_CODE       := SQLCODE;
    ROLLBACK;
    INSERT_PROC_F_LOG();
    PKG_COMMON.EXP_PRNS(V_PROG_ID, V_ERROR_MSG);
    PKG_COMMON.EXP_PRNS(V_PROG_ID, V_DB_ERROR_MSG);
    OUT_MSG := V_DB_ERROR_MSG;
    UTL_FILE.FCLOSE_ALL;
    PKG_COMMON.HISTORY_FAIL(V_PROG_ID,
                            V_USER_ID,
                            TO_CHAR((SYSDATE - 1), 'YYYYMMDD'),
                            '',
                            TRIM(V_ERROR_MSG) || TRIM(V_DB_ERROR_MSG));
  
END;
/


spool off
