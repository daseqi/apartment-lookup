// dph38
public class Apartment
{
    String address;
    String aptNumber;
    String city;
    String zip;
    int price;
    int sqFt;
    
    public Apartment(String address, String aptNumber, String city, String zip, int price, int sqFt)
    {
        this.address = address;
        this.aptNumber = aptNumber;
        this.city = city;
        this.zip = zip;
        this.price = price;
        this.sqFt = sqFt;
    }

    // SETTERS
    public void setAddress(String a)
    {
        address = a;
    }
    public void setAptNumber(String n)
    {
        aptNumber = n;
    }
    public void setCity(String c)
    {
        city = c;
    }
    public void setZip(String z)
    {
        zip = z;
    }
    public void setPrice(int p)
    {
        price = p;
    }
    public void setSqFt(int ft)
    {
        sqFt = ft;
    }
    
    // -----------------------------
    // GETTERS
    public String getAddress()
    {
        return address;
    }
    public String getAptNumber()
    {
        return aptNumber;
    }
    public String getCity()
    {
        return city;
    }
    public String getZip()
    {
        return zip;
    }
    public int getPrice()
    {
        return price;
    }
    public int getSqFt()
    {
        return sqFt;
    }

    /**
     * Returns a string of the Apartment attributes.
     */
    public String toString()
    {
        return address+"\n"+aptNumber+"\n"+city+"\n"+zip+"\n$: "+price+"\nSqFt: "+sqFt; 
    }
}