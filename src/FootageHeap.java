import java.io.*;
// dph38
public class FootageHeap 
{
    private int maxNum;
    private int n;
    private int[] pq;
    private int[] qp;
    private Apartment[] keys;

    public FootageHeap()
    {
        n = 0;
        pq = new int[1000];
        qp = new int[1000];
        keys = new Apartment[1000];
        maxNum = 999;               // HARD CODED SIZE
        for (int i = 0; i <= maxNum; i++)
            qp[i] = -1;
    }

    public void insert(int i, Apartment apartment)
    {
        n++;
        qp[i] = n;
        pq[n] = i;
        keys[i] = apartment;
        swim(n);
    }

    public void insert(Apartment apartment)
    {
        int i = n; 
        n++;
        qp[i] = n;
        pq[n] = i;
        keys[i] = apartment;
        swim(n);
    }

    public Apartment keyOf(int i) throws Exception{
        validateIndex(i);
        if (!contains(i)) throw new Exception("index is not in the priority queue");
        else return keys[i];
    }

    
    public void delete(int i) throws Exception
    {
        validateIndex(i);
        if (!contains(i)) throw new Exception("index is not in the priority queue");
        int index = qp[i];
        exch(index, n--);
        swim(index);
        sink(index);
        keys[i] = null;
        qp[i] = -1;
    }

    // HELPERS
    // ----------------------
    private void swim(int k) 
    {
        while (k > 1 && greater(k/2, k)) 
        {
            exch(k, k/2);
            k = k/2;
        }
    }
    private void sink(int k) 
    {
        while (2*k <= n) 
        {
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }


    private boolean greater(int i, int j) 
    {
        return keys[pq[i]].getSqFt() < keys[pq[j]].getSqFt();
    }

    private void exch(int i, int j) 
    {
        int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }
    public Apartment minKey() throws Exception 
    {
        if (n == 0) throw new Exception("Priority queue underflow");
        return keys[pq[1]];
    }

    private void validateIndex(int i) 
    {
        if (i < 0) throw new IllegalArgumentException("index is negative: " + i);
        if (i >= maxNum) throw new IllegalArgumentException("index >= capacity: " + i);
    }

    public boolean contains(int i) 
    {
        validateIndex(i);
        return qp[i] != -1;
    }

    public int size() 
    {
        return n;
    }

    public boolean isEmpty() 
    {
        return n == 0;
    }



    /*
    public static void main(String[] args) throws Exception {
        BufferedReader br= new BufferedReader(new FileReader("apartments.txt"));
        String skipHeadLine = br.readLine();  // NOTE : skip header line of file
        FootageHeap pq = new FootageHeap();
        // int i = 1;

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
            pq.insert(apartment);
            //i++;
            
            
        }
        System.out.println(pq.size());
        System.out.println("ALL");
        for(int j = 0; j < pq.n; j++)
            System.out.println(j + " keys[j]: " + pq.keys[j] + " qp[j]: " + pq.qp[j] +" pq[j]: " + pq.pq[j] + " keys[pq[j]]: " + pq.keys[pq.pq[j]]);
        
        
        pq.insert(new Apartment("dev", "ha", "ral", "3332", 5, 5));
        System.out.println("NEW");
        for(int j = 0; j < pq.n; j++)
            System.out.println(j + " keys[j]: " + pq.keys[j] + " qp[j]: " + pq.qp[j] +" pq[j]: " + pq.pq[j] + " keys[pq[j]]: " + pq.keys[pq.pq[j]]);
        System.out.println(pq.minKey().toString());

    }
    */
       
}
