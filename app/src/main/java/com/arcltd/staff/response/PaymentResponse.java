package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

public class PaymentResponse {

    @SerializedName("BANKNAME")
    private String bANKNAME;
    @SerializedName("BANKTXNID")
    private String bANKTXNID;
    @SerializedName("CHECKSUMHASH")
    private String cHECKSUMHASH;
    @SerializedName("CURRENCY")
    private String cURRENCY;
    @SerializedName("GATEWAYNAME")
    private String gATEWAYNAME;
    @SerializedName("MID")
    private String mID;
    @SerializedName("ORDERID")
    private String oRDERID;
    @SerializedName("PAYMENTMODE")
    private String pAYMENTMODE;
    @SerializedName("RESPCODE")
    private String rESPCODE;
    @SerializedName("RESPMSG")
    private String rESPMSG;
    @SerializedName("STATUS")
    private String sTATUS;
    @SerializedName("TXNAMOUNT")
    private String tXNAMOUNT;
    @SerializedName("TXNDATE")
    private String tXNDATE;
    @SerializedName("TXNID")
    private String tXNID;

    public String getBANKNAME() {
        return bANKNAME;
    }

    public void setBANKNAME(String bANKNAME) {
        this.bANKNAME = bANKNAME;
    }

    public String getBANKTXNID() {
        return bANKTXNID;
    }

    public void setBANKTXNID(String bANKTXNID) {
        this.bANKTXNID = bANKTXNID;
    }

    public String getCHECKSUMHASH() {
        return cHECKSUMHASH;
    }

    public void setCHECKSUMHASH(String cHECKSUMHASH) {
        this.cHECKSUMHASH = cHECKSUMHASH;
    }

    public String getCURRENCY() {
        return cURRENCY;
    }

    public void setCURRENCY(String cURRENCY) {
        this.cURRENCY = cURRENCY;
    }

    public String getGATEWAYNAME() {
        return gATEWAYNAME;
    }

    public void setGATEWAYNAME(String gATEWAYNAME) {
        this.gATEWAYNAME = gATEWAYNAME;
    }

    public String getMID() {
        return mID;
    }

    public void setMID(String mID) {
        this.mID = mID;
    }

    public String getORDERID() {
        return oRDERID;
    }

    public void setORDERID(String oRDERID) {
        this.oRDERID = oRDERID;
    }

    public String getPAYMENTMODE() {
        return pAYMENTMODE;
    }

    public void setPAYMENTMODE(String pAYMENTMODE) {
        this.pAYMENTMODE = pAYMENTMODE;
    }

    public String getRESPCODE() {
        return rESPCODE;
    }

    public void setRESPCODE(String rESPCODE) {
        this.rESPCODE = rESPCODE;
    }

    public String getRESPMSG() {
        return rESPMSG;
    }

    public void setRESPMSG(String rESPMSG) {
        this.rESPMSG = rESPMSG;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

    public String getTXNAMOUNT() {
        return tXNAMOUNT;
    }

    public void setTXNAMOUNT(String tXNAMOUNT) {
        this.tXNAMOUNT = tXNAMOUNT;
    }

    public String getTXNDATE() {
        return tXNDATE;
    }

    public void setTXNDATE(String tXNDATE) {
        this.tXNDATE = tXNDATE;
    }

    public String getTXNID() {
        return tXNID;
    }

    public void setTXNID(String tXNID) {
        this.tXNID = tXNID;
    }
}
