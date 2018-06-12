package ca.judacribz.mosquitomanager.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CB {


    // Global Vars
    // --------------------------------------------------------------------------------------------
    private String samplingDate, community, cbAddress, stageOfDevelopment;
    private int numberOfLarvae;
    private long id = -1;
    // --------------------------------------------------------------------------------------------

    // ######################################################################################### //
    // Exercise Constructors                                                                     //
    // ######################################################################################### //
    // ######################################################################################### //
    /* Required empty constructor for firebase */
    public CB() {
    }

    public CB(
            String samplingDate,
            String community,
            String cbAddress,
            int numberOfLarvae,
            String stageOfDevelopment
    ) {
        this.samplingDate = samplingDate;
        this.community = community;
        this.cbAddress = cbAddress;
        this.numberOfLarvae = numberOfLarvae;
        this.stageOfDevelopment = stageOfDevelopment;
    }

// ============================================================================================

    // Getters and setters
    // ============================================================================================

    public String getSamplingDate() {
        return samplingDate;
    }

    public void setSamplingDate(String samplingDate) {
        this.samplingDate = samplingDate;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getCbAddress() {
        return cbAddress;
    }

    public void setCbAddress(String cbAddress) {
        this.cbAddress = cbAddress;
    }

    public String getStageOfDevelopment() {
        return stageOfDevelopment;
    }

    public void setStageOfDevelopment(String stageOfDevelopment) {
        this.stageOfDevelopment = stageOfDevelopment;
    }

    public int getNumberOfLarvae() {
        return numberOfLarvae;
    }

    public void setNumberOfLarvae(int numberOfLarvae) {
        this.numberOfLarvae = numberOfLarvae;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }
    // ============================================================================================


    /* Helper function used to store Exercise information in the firebase db */
    public Map<String, Object> toMap() {
        Map<String, Object> cbMap = new HashMap<>();

        cbMap.put("samplingDate", samplingDate);
        cbMap.put("community", community);
        cbMap.put("cbAddress", cbAddress);
        cbMap.put("numberOfLarvae", numberOfLarvae);
        cbMap.put("stageOfDevelopment", stageOfDevelopment);

        return cbMap;
    }
}
