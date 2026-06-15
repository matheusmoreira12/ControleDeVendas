package Data;

import TextDatabases.DBRecord;

public class Client extends DBRecord {
    public Client(int id, String name, String address, String phone) {
        super(id);

        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    private String name;
    private String address;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
