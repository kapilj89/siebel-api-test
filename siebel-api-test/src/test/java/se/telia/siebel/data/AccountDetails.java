package se.telia.siebel.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AccountDetails {

    private String billingAccount=null;
    private String billingProfile=null;
    private String serviceAccount=null;
    private String primaryContact=null;
    private String customerAccount=null;

    public String dump() {
        return "billingAccount="+billingAccount+"\n"+
        "billingProfile="+billingProfile+"\n"+
        "serviceAccount="+serviceAccount+"\n"+
        "primaryContact="+primaryContact+"\n"+
        "customerAccount="+customerAccount+"\n";
    }

}
