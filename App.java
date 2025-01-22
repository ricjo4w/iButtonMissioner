import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
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

        // Get the DS1490F USB adapter
        adapter = OneWireAccessProvider.getAdapter("DS9097U", "USB1");

        // attempt to get a default adapter
        try
        {
            adapter = OneWireAccessProvider.getDefaultAdapter();

            // List the ports using adapter.getPortNames()
            
            //adapter.setPortName("/dev/ttyUSB0");

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
        //getButtonAddresses();

        DSPortAdapter adapter;
        String        port;

        // get the adapters
        for (Enumeration adapter_enum = OneWireAccessProvider.enumerateAllAdapters();
                                  adapter_enum.hasMoreElements(); )
        {
            // cast the enum as a DSPortAdapter
            adapter = ( DSPortAdapter ) adapter_enum.nextElement();

            System.out.print("Adapter: " + adapter.getAdapterName() + " with ports: ");

            // get the ports
            for (Enumeration port_enum = adapter.getPortNames();
                                port_enum.hasMoreElements(); )
            {
                // cast the enum as a String
                port = ( String ) port_enum.nextElement();

                System.out.print(port + " ");
            }

            System.out.println();
        }

    }
}
