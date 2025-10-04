/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ph.com.guanzongroup.cas.sysmonitor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class UnPaidDeliveryAcceptance implements iSystemMonitor {
    private String psMonitorName = "UnPaid Delivery Acceptance";
    private GRiderCAS poDriver;
    private String[] pasBranchCD;
    private String[] pasCompnyID;
    private String[] pasIndstCdx;
    private String[] pasCategrCd;
    
    JSONArray poJAData = null;
    
    @Override
    public void setDriver(GRiderCAS driver) {
        poDriver = driver;
    }

    @Override
    public String getName() {
        return psMonitorName;
    }

    @Override
    public void setBranchFilter(String[] branchcd) {
        pasBranchCD = branchcd;
    }

    @Override
    public void setCompanyFilter(String[] companycd) {
        pasCompnyID = companycd;
    }

    @Override
    public void setIndustryFilter(String[] indstcd) {
        pasIndstCdx = indstcd;
    }

    @Override
    public void setCategoryFilter(String[] categcd) {
        pasCategrCd = categcd;
    }

    @Override
    public JSONObject processMonitor() {
        String lsSQL;
        JSONObject oRes = new JSONObject();
        
        lsSQL = "SELECT" + 
                       "  a.sTransNox" + 
                       ", a.dTransact" + 
                       ", c.sCompnyNm" + 
                       ", b.sBranchNm" + 
                       ", d.sCompnyNm" + 
                       ", a.sIndstCdx" + 
                       ", a.sCategrCd" + 
                       ", a.dDueDatex" +
                       ", Case a.cPurposex" +  
                       " WHEN '0' THEN 'Regular'" +
                       " WHEN '1' THEN 'Consignment'" +
                       " WHEN '2' THEN 'Replacement'" +
                       " WHEN '3' THEN 'Warranty'" +
                       " WHEN '4' THEN 'Supplier Giveaways'" +
                       " END `DA Type`" +
                       ", a.cProcessd" +
                       ", a.cTranStat" +
               " FROM PO_Receiving_Master a" +
                    " LEFT JOIN Branch b ON a.sBranchCd = b.sBranchCD" +
                    " LEFT JOIN Client_Master c ON a.sSupplier = c.sClientID" +
                    " LEFT JOIN sCompnyID d ON a.sCompnyID = d.sCompnyID" +
               " WHERE a.cTranStat IN ('2')" +
               " AND a.cProcessd IN ('0', '1')";

        String lsFilterAll = "";
        String lsFilter;
        
        //set filter by industry
        lsFilter = "";
        for (String lsValue : pasIndstCdx) {
            lsFilter += ", " + SQLUtil.toSQL(lsValue);
        }
        if(!lsFilter.isEmpty()){
            lsFilterAll += " AND a.sIndstCdx IN(" + lsFilter.substring(2) + ")";
        }

        //set filter by category
        lsFilter = "";
        for (String lsValue : pasCategrCd) {
            lsFilter += ", " + SQLUtil.toSQL(lsValue);
        }
        if(!lsFilter.isEmpty()){
            lsFilterAll += " AND a.sCategrCd IN(" + lsFilter.substring(2) + ")";
        }

        //set filter by company
        lsFilter = "";
        for (String lsValue : pasCompnyID) {
            lsFilter += ", " + SQLUtil.toSQL(lsValue);
        }
        if(!lsFilter.isEmpty()){
            lsFilterAll += " AND a.sCompnyID IN(" + lsFilter.substring(2) + ")";
        }
        
        //set filter by branch
        lsFilter = "";
        for (String lsValue : pasBranchCD) {
            lsFilter += ", " + SQLUtil.toSQL(lsValue);
        }
        if(!lsFilter.isEmpty()){
            lsFilterAll += " AND a.sBranchCD IN(" + lsFilter.substring(2) + ")";
        }

        if(!lsFilterAll.isEmpty()){
            lsSQL += lsFilterAll;
        }
        
        try {
            ResultSet loRS = poDriver.executeQuery(lsSQL);
            
            poJAData = MiscUtil.RS2JSON(loRS);
            
        } catch (SQLException ex) {
            oRes.put("result", "Failed");
            oRes.put("message", MiscUtil.getException(ex));
            return oRes;
        }
        
        oRes.put("result", "Success");
        return oRes;
    }

    @Override
    public JSONArray getRecords() {
        return poJAData;
    }
    
}
