package project.addressbook;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by deepakgavkar on 18/12/16.
 */
public class AddressStruct implements Parcelable {
    private String id,name,mobileno,address,email,profile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.mobileno);
        dest.writeString(this.address);
        dest.writeString(this.email);
        dest.writeString(this.profile);
    }

    public AddressStruct() {
    }

    protected AddressStruct(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.mobileno = in.readString();
        this.address = in.readString();
        this.email = in.readString();
        this.profile = in.readString();
    }

    public static final Parcelable.Creator<AddressStruct> CREATOR = new Parcelable.Creator<AddressStruct>() {
        @Override
        public AddressStruct createFromParcel(Parcel source) {
            return new AddressStruct(source);
        }

        @Override
        public AddressStruct[] newArray(int size) {
            return new AddressStruct[size];
        }
    };
}
