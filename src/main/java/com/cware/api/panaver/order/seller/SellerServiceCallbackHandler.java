
/**
 * SellerServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1  Built on : Aug 31, 2011 (12:22:40 CEST)
 */

    package com.cware.api.panaver.order.seller;

    /**
     *  SellerServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class SellerServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public SellerServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public SellerServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for approveReturnApplication method
            * override this method for handling normal response from approveReturnApplication operation
            */
           public void receiveResultapproveReturnApplication(
                    com.cware.api.panaver.order.seller.SellerServiceStub.ApproveReturnApplicationResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from approveReturnApplication operation
           */
            public void receiveErrorapproveReturnApplication(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getProductOrderInfoList method
            * override this method for handling normal response from getProductOrderInfoList operation
            */
           public void receiveResultgetProductOrderInfoList(
                    com.cware.api.panaver.order.seller.SellerServiceStub.GetProductOrderInfoListResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getProductOrderInfoList operation
           */
            public void receiveErrorgetProductOrderInfoList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for cancelSale method
            * override this method for handling normal response from cancelSale operation
            */
           public void receiveResultcancelSale(
                    com.cware.api.panaver.order.seller.SellerServiceStub.CancelSaleResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from cancelSale operation
           */
            public void receiveErrorcancelSale(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getChangedProductOrderList method
            * override this method for handling normal response from getChangedProductOrderList operation
            */
           public void receiveResultgetChangedProductOrderList(
                    com.cware.api.panaver.order.seller.SellerServiceStub.GetChangedProductOrderListResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getChangedProductOrderList operation
           */
            public void receiveErrorgetChangedProductOrderList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for requestReturn method
            * override this method for handling normal response from requestReturn operation
            */
           public void receiveResultrequestReturn(
                    com.cware.api.panaver.order.seller.SellerServiceStub.RequestReturnResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from requestReturn operation
           */
            public void receiveErrorrequestReturn(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for approveCollectedExchange method
            * override this method for handling normal response from approveCollectedExchange operation
            */
           public void receiveResultapproveCollectedExchange(
                    com.cware.api.panaver.order.seller.SellerServiceStub.ApproveCollectedExchangeResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from approveCollectedExchange operation
           */
            public void receiveErrorapproveCollectedExchange(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for approveCancelApplication method
            * override this method for handling normal response from approveCancelApplication operation
            */
           public void receiveResultapproveCancelApplication(
                    com.cware.api.panaver.order.seller.SellerServiceStub.ApproveCancelApplicationResponseE result
                        ) {
           }
           
           /**
            * auto generated Axis2 call back method for rejectReturn method
            * override this method for handling normal response from rejectReturn operation
            */
           public void receiveResultrejectReturn(
                    com.cware.api.panaver.order.seller.SellerServiceStub.RejectReturnResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from approveCancelApplication operation
           */
            public void receiveErrorapproveCancelApplication(java.lang.Exception e) {
            }
            
            /**
             * auto generated Axis2 Error handler
             * override this method for handling error response from rejectReturn operation
             */
              public void receiveErrorrejectReturn(java.lang.Exception e) {
              }
                
           /**
            * auto generated Axis2 call back method for placeProductOrder method
            * override this method for handling normal response from placeProductOrder operation
            */
           public void receiveResultplaceProductOrder(
                    com.cware.api.panaver.order.seller.SellerServiceStub.PlaceProductOrderResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from placeProductOrder operation
           */
            public void receiveErrorplaceProductOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for shipProductOrder method
            * override this method for handling normal response from shipProductOrder operation
            */
           public void receiveResultshipProductOrder(
                    com.cware.api.panaver.order.seller.SellerServiceStub.ShipProductOrderResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from shipProductOrder operation
           */
            public void receiveErrorshipProductOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for reDeliveryExchange method
            * override this method for handling normal response from reDeliveryExchange operation
            */
           public void receiveResultreDeliveryExchange(
                    com.cware.api.panaver.order.seller.SellerServiceStub.ReDeliveryExchangeResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from reDeliveryExchange operation
           */
            public void receiveErrorreDeliveryExchange(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delayProductOrder method
            * override this method for handling normal response from delayProductOrder operation
            */
           public void receiveResultdelayProductOrder(
                    com.cware.api.panaver.order.seller.SellerServiceStub.DelayProductOrderResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delayProductOrder operation
           */
            public void receiveErrordelayProductOrder(java.lang.Exception e) {
            }
                


    }
    