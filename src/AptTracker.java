import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;

public class AptTracker {

    public static PriceHeap prices;     // main min heap for prices
    public static FootageHeap footage;  // main max heap for footage
    public static PricesDLB dlb;        // index lookup for main heaps

    public static PricesDLB cityDLB;    // index lookup for city heaps
    public static cityPriceDLB cityPrice;   // DLB for cities, with PriceHeaps as values
    public static cityFootageDLB cityFootage;   // DLB for cities, with FootageHeaps as values
    
    static void createPQs() throws Exception
    {
        prices = new PriceHeap();   // stores main price heap
        footage = new FootageHeap();    // stores main footage heap
        dlb = new PricesDLB();      // stores index of the apartment in the main price heap for price updating
        
        cityPrice = new cityPriceDLB(); // stores (city : city price heap) as (value : key) pair
        cityDLB = new PricesDLB();  // stores index of the apartment in the city price heap for price updating

        cityFootage = new cityFootageDLB();

        String id = "";

        BufferedReader br= new BufferedReader(new FileReader("apartments.txt"));
        String skipHeadLine = br.readLine();  // NOTE : skip header line of file
        String line = null;
        while((line = br.readLine()) != null)
        { 
            // line = line.replaceAll("\\s", ""); replaces all spaces, if needed
            String attributes[] = line.split(":");
            String address = attributes[0];
            String aptNumber = attributes[1];
            String city = attributes[2];
            String zip = attributes[3];
            int price = Integer.parseInt(attributes[4]);
            int sqFt = Integer.parseInt(attributes[5]);
            Apartment apartment = new Apartment(address, aptNumber, city, zip, price, sqFt);

            id = (address+aptNumber+zip).replaceAll("\\s", "");
            dlb.add(id, prices.size());
            prices.insert(apartment);
            footage.insert(apartment);
            
            // ADD TO SPECIFIC CITY HEAP AND DLB
            if(!cityPrice.exists(city))
            {
                cityPrice.add(city);
                cityFootage.add(city);
            }
            PriceHeap specificCity = cityPrice.get(city);
            FootageHeap specificCityFootage = cityFootage.get(city);
            cityDLB.add(id, specificCity.size());   // the city price and footage heaps will have same index 
            specificCity.insert(apartment);
            specificCityFootage.insert(apartment);



            
            
        }
        br.close();
    }

    public static void addApartment()
    {
        String address, aptNumber, city, zip;
        int price, sqFt;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Street address: ");
        address = scanner.nextLine();
        System.out.print("Apt number: ");
        aptNumber = scanner.nextLine();
        System.out.print("City: ");
        city = scanner.nextLine();
        System.out.print("ZIP: ");
        zip = scanner.nextLine();
        System.out.print("Price: ");
        price = scanner.nextInt();
        System.out.print("Square feet: ");
        sqFt = scanner.nextInt();
        Apartment toAdd = new Apartment(address, aptNumber, city, zip, price, sqFt);
        String id = (address+aptNumber+zip).replaceAll("\\s", "");
        dlb.add(id, prices.size()); // size() will be used for DLB key/index
        prices.insert(toAdd);
        footage.insert(toAdd);
        
        // ADD TO SPECIFIC CITY PRICE HEAP AND DLB, FOOTAGE HEAP AND DLB
        if(!cityPrice.exists(city))
            {
                cityPrice.add(city);
                cityFootage.add(city);
            }
        PriceHeap specificCity = cityPrice.get(city);
        FootageHeap specificCityFootage = cityFootage.get(city);
        cityDLB.add(id, specificCity.size()); // size() will be used for DLB key/index
        specificCity.insert(toAdd);
        specificCityFootage.insert(toAdd);

        System.out.println("\nADDITION SUCCESSFUL\n");
    }

    public static void updateApartment() throws Exception
    {
        Scanner scanner = new Scanner(System.in);
        String address, aptNumber, zip;
        int price;
        System.out.print("Street address: ");
        address = scanner.nextLine();
        System.out.print("Apt number: ");
        aptNumber = scanner.nextLine();
        System.out.print("ZIP: ");
        zip = scanner.nextLine();
        String id = (address+aptNumber+zip).replaceAll("\\s", "");
        int i = dlb.get(id);
        if(i==-1) 
        {
            System.out.println("\nApartment does not exist. Please check input.\n");
            return;
        }
        System.out.print("Update the rent price(y/n)? ");
        String answer = scanner.nextLine();
        if(answer.startsWith("n")) return;
        else
        {
            System.out.print("New integer price: ");
            price = scanner.nextInt();
            if(!prices.contains(i))
            {
                System.out.println("\nApartment does not exist. Please check input.\n");
                return;
            }
            prices.updatePrice(i, price);
        }

        // UPDATE TO SPECIFIC CITY HEAP
        Apartment toUpdate = prices.keyOf(i);      
        int j = cityDLB.get(id);
        if(j==-1) 
        {
            System.out.println("\nApartment does not exist. Please check input.\n");
            return;
        }
        PriceHeap specificCity = cityPrice.get(toUpdate.getCity());
        specificCity.updatePrice(j, price);

        System.out.println("\nUPDATE SUCCESSFUL\n");
        
    }

    public static void deleteApartment() throws Exception
    {
        Scanner scanner = new Scanner(System.in);
        String address, aptNumber, zip;
        System.out.print("Street address: ");
        address = scanner.nextLine();
        System.out.print("Apt number: ");
        aptNumber = scanner.nextLine();
        System.out.print("ZIP: ");
        zip = scanner.nextLine();
        String id = (address+aptNumber+zip).replaceAll("\\s", "");
        int i = dlb.get(id);
        if(i==-1) 
        {
            System.out.println("\nApartment does not exist. Please check input.\n");
            return;
        }
        Apartment toDelete = prices.keyOf(i);
        prices.delete(i);
        footage.delete(i);


        // DELETE FROM SPECIFIC CITY HEAP
        int j = cityDLB.get(id);
        if(j==-1) 
        {
            System.out.println("\nApartment does not exist. Please check input.\n");
            return;
        }
        PriceHeap specificCity = cityPrice.get(toDelete.getCity());
        FootageHeap specificCityFootage = cityFootage.get(toDelete.getCity());
        specificCity.delete(j);
        specificCityFootage.delete(j);

        System.out.println("\nDELETION SUCCESSFUL\n");

    }


    public static void getCityPrice() throws Exception
    {
        Scanner scanner = new Scanner(System.in);
        String city;
        System.out.print("City: ");
        city = scanner.nextLine();
        if(cityPrice.exists(city) && cityPrice.get(city).size() > 0)
        {
            PriceHeap specificCity = cityPrice.get(city);
            System.out.println("\n"+specificCity.minKey().toString()+"\n");
        }
        else
        {
            System.out.println("\nCity does not have any Apartments\n");
        }
    }

    public static void getCityFootage() throws Exception
    {
        Scanner scanner = new Scanner(System.in);
        String city;
        System.out.print("City: ");
        city = scanner.nextLine();
        if(cityFootage.exists(city) && cityPrice.get(city).size() > 0)
        {
            FootageHeap specificCity = cityFootage.get(city);
            System.out.println("\n"+specificCity.minKey().toString()+"\n");
        }
        else
        {
            System.out.println("\nCity does not have any Apartments\n");
        }
    }

    
    public static void main(String[] args) throws Exception 
    {
        createPQs();
        Scanner scanner = new Scanner(System.in);
        boolean run = true;
        int input = 0;
        
        while(run)
        {
            System.out.println("----------------------------");
            System.out.println("(1) Add an apartment");
            System.out.println("(2) Update an apartment");
            System.out.println("(3) Remove a specific apartment from consideration");
            System.out.println("(4) Retrieve the lowest rent apartment");
            System.out.println("(5) Retrieve the highest square footage apartment");
            System.out.println("(6) Retrieve the lowest rent apartment by city");
            System.out.println("(7) Retrieve the highest square footage apartment by city");
            System.out.println("(8) Exit Program");
            System.out.println("----------------------------");

            System.out.print("Please enter integer choice: ");
            try 
            {
                input = scanner.nextInt();
            }
            catch(Exception e)
            {
                System.out.println("\nInput is Invalid");
                run = false;
                break;
            }
            if(input < 1 || input > 7)  // end program
            {
                run = false;
                break;
            }
            else if(input==1)
            {
                addApartment();
            }
            else if(input==2)
            {
                updateApartment();
            }
            else if(input==3)
            {
                deleteApartment();             
            }
            else if(input==4)
            {
                System.out.println("\n"+prices.minKey().toString()+"\n");  // return lowest price           
            }
            else if(input==5)
            {
                System.out.println("\n"+footage.minKey().toString()+"\n"); // return highest sq ft
            }
            else if(input==6)
            {
                getCityPrice();
            }
            else if(input==7)
            {
                getCityFootage();              
            }
        }
        scanner.close();
        System.out.println("\nHave a nice day!");   // end of main
    }
}
