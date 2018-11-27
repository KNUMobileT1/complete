package com.bmh.trackchild.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.bmh.trackchild.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.widget.ArrayAdapter;


public class BluetoothHelper 
{	
	
	public final static int REQUEST_ENABLE_BT=1;
	BluetoothAdapter mBluetoothAdapter;	
	ArrayAdapter<String> mArrayAdapter;
	private ArrayList<String> devicesList,pairedList;
	HashSet<BluetoothDevice> BTDevices;
	Context context;
	ProgressDialog progressDialog;
	
	
	public BluetoothHelper(Context context)
	{
		this.context=context;
		//create BluetoothAdapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
	    mArrayAdapter=new ArrayAdapter<String>(context.getApplicationContext(),R.layout.list_item,R.id.row);
	    //instantiate arraylist that will contain discovered devices 
		devicesList = new ArrayList<String>();
	    //instantiate arraylist that will contain paired devices 
		pairedList=new ArrayList<String>();
	    //instantiate hashSet that will contain all devices weather discovered or paired (without duplication)
		BTDevices=new HashSet<BluetoothDevice>();
	}
	
	public boolean checkBTstatus()
	{
		//Check local device adapter is turned on or off
		if (mBluetoothAdapter.isEnabled()) 
			return true;
		return false;	
	}

	public void turnOnBluetooth(Activity context) 
	{
		    //check if bluetooth is enabled or not, if not turn bluetooth on		
			if (!checkBTstatus()) 
			{
			    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			    context.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
	}

	
	public void scanPairedDevices() 
	{
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		if (pairedDevices.size() > 0) 
		{
	    // Loop through paired devices
			for (BluetoothDevice pDevice : pairedDevices) 
			{
				
				setAllDevices(pDevice);
				pairedList.add(pDevice.getName() + "\nPaired");
				
				mArrayAdapter.add(pDevice.getName() + "\nPaired");
				mArrayAdapter.notifyDataSetChanged();	
			} 
		}
	}
	public void discoverDevices()
	{
		IntentFilter filter = new IntentFilter();	    
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); 
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); 
		this.context.registerReceiver(mReceiver, filter);
		
		if(mBluetoothAdapter.isDiscovering())
		{
			mBluetoothAdapter.cancelDiscovery();
		}
		
		mBluetoothAdapter.startDiscovery();
	}
	
	public void unregisterBTReceiver()
	{
		if(mReceiver != null)
		{
			this.context.unregisterReceiver(mReceiver);
		}
		mReceiver=null;

	}

	public ArrayAdapter<String> getDevicesArrayAdapter()
	{
		return mArrayAdapter;
	}
	
	public BluetoothAdapter getBluetoothAdapter()
	{
		return mBluetoothAdapter;
	}
	
	 BroadcastReceiver mReceiver = new BroadcastReceiver()  
	{  
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			String action = intent.getAction();  

			if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
			 {
				//May devices that had been discovered before becomes non discoverable at the new discovery, so  clear devices list 
				 devicesList.clear();
				 createScaningDialog().show();
			 }
			else if (BluetoothDevice.ACTION_FOUND.equals(action))  
	        { 
	        	BluetoothDevice device = intent.getParcelableExtra(	BluetoothDevice.EXTRA_DEVICE);			
	        	setAllDevices(device);
	        	//Adding found device to device list after new discovery
				devicesList.add(device.getName() + "\n"+getDeviceState(device));
	        }
			 else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
			 {
				 //call scanPairedDevices Method to get pairedList
				 scanPairedDevices();
				 //clear the the old elements of arrayAdapter
				 mArrayAdapter.clear();
				 //devicesList has the found devices discovered through ACTION_FOUND
				 //Adding to them the paired devices List (that we get them though calling scanPairedDevices() )
				 devicesList.addAll(pairedList);
				 //create new arrayList that will be assigned with non duplicated element by using setListWithoutDuplication() metod
					ArrayList<String> arr=new ArrayList<String>();
					arr=setListWithoutDuplication(devicesList);
					//Loop through arrayList to get their elements and adding them to ArrayAdapter
					if(arr.size()>0)
					{
						for(int i=0;i<arr.size();i++)
						{
							mArrayAdapter.add(arr.get(i));
						}
					}
					//No devices found and there is no paired devices
					else if(mArrayAdapter.getCount()==0)
					{
						mArrayAdapter.add("Not Devices Found");
					}
					mArrayAdapter.notifyDataSetChanged();
				 progressDialog.dismiss();
			 }
		}     
	};

	public ProgressDialog createScaningDialog() 
	{
		progressDialog = new ProgressDialog(this.context);
		progressDialog.setTitle("Scaning");
		progressDialog.setMessage("Please wait....");

		return progressDialog;
	}	
	
	public ArrayList<String> setListWithoutDuplication(ArrayList<String> devicesList)
	{
		//HashSet Collection doesn't support duplication,so
		//We add ArrayList"devicesList" to hashSetAdapter
		HashSet<String> hashSetAdapter = new HashSet<String>();
		hashSetAdapter.addAll(devicesList);
		devicesList.clear();
		devicesList.addAll(hashSetAdapter);
		
		return devicesList;
	}
	
	public void setAllDevices(BluetoothDevice  device)
	{
		BTDevices.add(device);

	}
	public HashSet<BluetoothDevice> getAllDevices()
	{
		return BTDevices;
	}
	public String getDeviceState(BluetoothDevice  device)
	{
		String deviceState;
		switch (device.getBondState()) 
		{
			case BluetoothDevice.BOND_NONE:
				deviceState= "Not paired";
			break;
			
			case BluetoothDevice.BOND_BONDED:
				deviceState= "Paired";
			break;
			default : deviceState="";
		}
		
		return deviceState;
		
	}
}
