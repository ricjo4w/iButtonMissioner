import java.io.BufferedReader;
import java.io.IOException;
import java.util.Vector;

import com.dalsemi.onewire.OneWireAccessProvider;
import com.dalsemi.onewire.OneWireException;
import com.dalsemi.onewire.adapter.DSPortAdapter;
import com.dalsemi.onewire.adapter.OneWireIOException;
import com.dalsemi.onewire.container.OneWireContainer;
import com.dalsemi.onewire.container.OneWireContainer41;
import com.dalsemi.onewire.container.TemperatureContainer;

public class App {

    static int parseInt (BufferedReader in, int def)
   {
      try
      {
         return Integer.parseInt(in.readLine());
      }
      catch (Exception e)
      {
         return def;
      }
   }

   static String[][] getButtonInfo() {
        String[][] buttonInfo = new String[3][10];

        return buttonInfo;
   }

   static Vector<Long> getButtonAddresses()
   throws OneWireIOException, OneWireException, IOException, Exception{
        // initialize the array of addresses

        Vector<Long> addresses = new Vector<Long>();

        // initialize the adapter
        DSPortAdapter adapter = null;
        boolean default_adapter = true;

        // attempt to get a default adapter
        try
        {
            adapter = OneWireAccessProvider.getDefaultAdapter();
            default_adapter = true;
        }
        catch (OneWireException e)
        {
            System.out.println("Default adapter not found: " + e);
            default_adapter = false;
        }

        if (!default_adapter)
        {
            return addresses;
        }

        adapter.adapterDetected();
        adapter.targetFamily(0x41); // DS1922
        adapter.beginExclusive(true);
        adapter.reset();
        adapter.setSearchAllDevices();

        boolean hasButtons = adapter.findFirstDevice();

        if (!hasButtons)
        {
            return addresses;
        } else {
            addresses.add(adapter.getAddressAsLong());
            System.out.println("Found a device: " + adapter.getAddressAsLong());
        }

        while (adapter.findNextDevice())
        {
            addresses.add(adapter.getAddressAsLong());
            System.out.println("Found another device: " + adapter.getAddressAsLong());
        }

        return addresses;
   }

   static int getNumberOfButtonsConnected() 
   throws OneWireIOException, OneWireException, IOException, Exception
   {

        // initialize the adapter
        DSPortAdapter adapter = null;
        boolean default_adapter = true;

        // attempt to get a default adapter
        try
        {
            adapter = OneWireAccessProvider.getDefaultAdapter();
            default_adapter = true;
        }
        catch (OneWireException e)
        {
            System.out.println("Default adapter not found: " + e);
            default_adapter = false;
        }

        if (!default_adapter)
        {
            return 0;
        }

        adapter.adapterDetected();
        adapter.targetFamily(0x41); // DS1922
        adapter.beginExclusive(true);
        adapter.reset();
        adapter.setSearchAllDevices();

        boolean hasButtons = adapter.findFirstDevice();

        if (!hasButtons)
        {
            return 0;
        }

        int count = 1;
        while (adapter.findNextDevice())
        {
            count++;
        }

        return count;
   }

    public static void main(String[] args) 
        throws OneWireIOException, OneWireException, IOException, Exception
    {
        System.out.println("Hello, World!");
        getButtonAddresses();
    }
}
