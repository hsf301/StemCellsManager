package com.huashengfu.StemCellsManager.entity.settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/*

{"id":[300000002,500000020,700000001,700000002,700000003,700000004,300000003,300000004,
300000005,300000006,800000001,800000002,300000014,900000001,900000002,900000004,900000003,
900000005,900000012,900000013,900000014,900000015,900000016,900000017,900000018,900000019,
1100000001,1100000002,1100000003,1000000002,1100000004,1100000005,1100000006,1100000007,
1000000004,1100000008,1000000006,1100000009,1000000008,1100000010,110000113],"phone":"13520485302"}

 */
public class TelePhone implements Serializable {

    private List<String> id = new ArrayList<>();
    private String phone = "";

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
