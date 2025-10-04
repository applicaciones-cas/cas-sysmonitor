/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package ph.com.guanzongroup.cas.sysmonitor;

import org.guanzon.appdriver.base.GRiderCAS;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public interface iSystemMonitor {
    /**
     * Set driver to use (e.g., [gRiderCAS]).
     * @param driver
     */
    void setDriver(GRiderCAS driver);
    
    /**
     * Returns the name of the monitoring object (e.g., "Undelivered Purchase Orders").
     * @return 
     */
    String getName();    

    /**
     * Set filter by branch code(e.g., ['M001', 'M002']).
     * @param branchcd
     * @return 
     */
    void setBranchFilter(String[] branchcd);

    /**
     * Set filter by company code (e.g., ['M001', 'M002']).
     * @param companycd
     * @return 
     */
    void setCompanyFilter(String[] companycd);

    /**
     * Set filter by branch code(e.g., ['01']).
     * @param indstcd
     * @return 
     */
    void setIndustryFilter(String[] indstcd);

    /**
     * Set filter by category code (e.g., ['01']).
     * @param categcd
     * @return 
     */
    void setCategoryFilter(String[] categcd);
    
    /**
     * Process monitor and create the list of records.
     * @return JSONObject
     */
    JSONObject processMonitor();
    
    /**
     * Returns matching records based on filters.
     * @return JSONArray
     */
    JSONArray getRecords();
}
