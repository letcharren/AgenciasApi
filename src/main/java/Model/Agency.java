package Model;

import static Model.SortCriteria.*;

/**
 * Clase que modela la informacion de una direccion de Mercado Libre con sus correspondientes getters and Setters
 */
public class Agency implements Comparable<Agency> {
    private Integer agency_code;
    private String correspondent_id;
    private String description;
    private boolean disabled;
    private Double distance;
    private String id;
    private String payent_method_id;
    private String phone;
    private String site_id;
    private String terminal;
    private Address address;
    private static SortCriteria sortCriteria = AGENCY_CODE;

    public Agency() {
    }

    public Integer getAgency_code() {
        return agency_code;
    }

    public void setAgency_code(Integer agency_code) {
        this.agency_code = agency_code;
    }

    public String getCorrespondent_id() {
        return correspondent_id;
    }

    public void setCorrespondent_id(String correspondent_id) {
        this.correspondent_id = correspondent_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayent_method_id() {
        return payent_method_id;
    }

    public void setPayent_method_id(String payent_method_id) {
        this.payent_method_id = payent_method_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public Address getAddress() { return address; }

    public void setAddress(Address address) { this.address = address; }

    public static SortCriteria getSortCriteria() {
        return sortCriteria;
    }

    public static void setSortCriteria(SortCriteria sortCriteria) {
        Agency.sortCriteria = sortCriteria;
    }


    @Override
    public int compareTo(Agency o) {
        int result;
        switch(Agency.sortCriteria) {
            case ADDRESS_LINE: {
                result = this.address.getAddress_line().compareTo(o.address.getAddress_line());
                break;
            }
            case DISTANCE:
                result = this.distance.compareTo(o.distance);
                break;
            default:
               result = this.agency_code.compareTo(o.agency_code);
        }
        return result;
    }

}
