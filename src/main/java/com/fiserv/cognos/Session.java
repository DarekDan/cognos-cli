package com.fiserv.cognos;

import com.cognos.developer.schemas.bibus._3.BiBusHeader;
import com.cognos.developer.schemas.bibus._3.CAM;
import com.cognos.developer.schemas.bibus._3.ContentManagerService_PortType;
import com.cognos.developer.schemas.bibus._3.ContentManagerService_ServiceLocator;
import com.cognos.developer.schemas.bibus._3.EventManagementService_PortType;
import com.cognos.developer.schemas.bibus._3.EventManagementService_ServiceLocator;
import com.cognos.developer.schemas.bibus._3.HdrSession;
import com.cognos.developer.schemas.bibus._3.PropEnum;
import com.cognos.developer.schemas.bibus._3.QueryOptions;
import com.cognos.developer.schemas.bibus._3.SearchPathMultipleObject;
import com.cognos.developer.schemas.bibus._3.Sort;
import com.cognos.developer.schemas.bibus._3.XmlEncodedXML;
import com.cognos.org.apache.axis.client.Stub;
import com.cognos.org.apache.axis.message.SOAPHeaderElement;

import com.fiserv.exceptions.SessionException;
import java.rmi.RemoteException;
import javax.xml.namespace.QName;

final public class Session {
    final String biBusHeader = "biBusHeader";
    final String biBusNamespace = "http://developer.cognos.com/schemas/bibus/3/";

    private ContentManagerService_PortType cmService;
    private ContentManagerService_ServiceLocator cmServiceLocator;
    private EventManagementService_PortType eventService;
    private EventManagementService_ServiceLocator eventServiceLocator;

    private String endpoint;

    public Session(String url){
        if(url ==null || url.trim().isEmpty()){
            throw new SessionException("A url must be provided");
        }
        try
        {
            endpoint = url;
            //initialize the service locators
            eventServiceLocator = new EventManagementService_ServiceLocator();
            cmServiceLocator = new ContentManagerService_ServiceLocator();

            //get the service objects from the locators
            eventService = eventServiceLocator.geteventManagementService(new java.net.URL(endpoint));

            cmService = cmServiceLocator.getcontentManagerService(new java.net.URL(endpoint));
            //Set the axis timeout to 0 (infinite)
            //There may be many, many actions due to this trigger
            ((Stub)eventService).setTimeout(0);
        }
        catch(Exception e)
        {
            throw new SessionException(e.getMessage());
        }
    }

    public boolean loginAnonymousEnabled()
    {
        SearchPathMultipleObject cmSearch = new SearchPathMultipleObject("~");
        try
        {
            cmService.query(
                cmSearch,
                new PropEnum[] {},
                new Sort[] {},
                new QueryOptions());
        }
        catch (java.rmi.RemoteException remoteEx)
        {
            return false;
        }

        try {

            SOAPHeaderElement temp = ((Stub)cmService).getResponseHeader(biBusNamespace, biBusHeader);
            BiBusHeader bibus = (BiBusHeader)temp.getValueAsType(new QName(biBusNamespace, biBusHeader));

            if (bibus != null)
            {
                ((Stub)eventService).setHeader(biBusNamespace, biBusHeader, bibus);
                return true;
            }
        } catch (Exception e) {
            // IGNORE
        }

        return false;
    }

    public boolean loginAnonymous()
    {
        if (! loginAnonymousEnabled() )
        {
            return false;
        }

        CAM cam = new CAM();
        cam.setAction("logon");

        HdrSession header = new HdrSession();

        BiBusHeader biBus = new BiBusHeader();
        biBus.setCAM(cam);
        biBus.setHdrSession(header);

        ((Stub)cmService).setHeader(biBusNamespace, biBusHeader, biBus);

        return true;
    }

    public boolean login(String namespace, String uid, String passwd)
    {
        try
        {
            final XmlEncodedXML credentialXEX = new XmlEncodedXML();
            final StringBuffer credentialXML = new StringBuffer();
            credentialXML.append("<credential>");
            credentialXML.append("<namespace>" + namespace + "</namespace>");
            credentialXML.append("<password>" + passwd + "</password>");
            credentialXML.append("<username>" + uid + "</username>");
            credentialXML.append("</credential>");

            credentialXEX.set_value(credentialXML.toString());
            cmService.logon(credentialXEX, null);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            return false;
        }

        try {

            SOAPHeaderElement temp = ((Stub)cmService).getResponseHeader(biBusNamespace, biBusHeader);
            BiBusHeader biBus = (BiBusHeader)temp.getValueAsType(new QName(biBusNamespace, biBusHeader));

            if (biBus != null)
            {
                ((Stub)eventService).setHeader(biBusNamespace, biBusHeader, biBus);
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

}
