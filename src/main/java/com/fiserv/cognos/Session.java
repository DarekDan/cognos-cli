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

public final class Session {
    static final String BI_BUS_HEADER = "biBusHeader";
    static final String BI_BUS_NAMESPACE = "http://developer.cognos.com/schemas/bibus/3/";

    public ContentManagerService_PortType getCmService() {
        return cmService;
    }

    public EventManagementService_PortType getEventService() {
        return eventService;
    }

    private final ContentManagerService_PortType cmService;
    private final EventManagementService_PortType eventService;


    private String endpoint;

    public Session(String url){
        if(url ==null || url.trim().isEmpty()){
            throw new SessionException("A url must be provided");
        }
        try
        {
            endpoint = url;
            //initialize the service locators
            EventManagementService_ServiceLocator eventServiceLocator = new EventManagementService_ServiceLocator();
            ContentManagerService_ServiceLocator cmServiceLocator = new ContentManagerService_ServiceLocator();

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

            SOAPHeaderElement temp = ((Stub)cmService).getResponseHeader(BI_BUS_NAMESPACE, BI_BUS_HEADER);
            BiBusHeader bibus = (BiBusHeader)temp.getValueAsType(new QName(BI_BUS_NAMESPACE, BI_BUS_HEADER));

            if (bibus != null)
            {
                ((Stub)eventService).setHeader(BI_BUS_NAMESPACE, BI_BUS_HEADER, bibus);
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

        ((Stub)cmService).setHeader(BI_BUS_NAMESPACE, BI_BUS_HEADER, biBus);

        return true;
    }

    public boolean login(String namespace, String uid, String passwd)
    {
        try
        {
            final XmlEncodedXML credentialXEX = new XmlEncodedXML();
            final StringBuilder credentialXML = new StringBuilder();
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

            SOAPHeaderElement temp = ((Stub)cmService).getResponseHeader(BI_BUS_NAMESPACE, BI_BUS_HEADER);
            BiBusHeader biBus = (BiBusHeader)temp.getValueAsType(new QName(BI_BUS_NAMESPACE, BI_BUS_HEADER));

            if (biBus != null)
            {
                ((Stub)eventService).setHeader(BI_BUS_NAMESPACE, BI_BUS_HEADER, biBus);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Failed to login");
        }
        return false;
    }

}
