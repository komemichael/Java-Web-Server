/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httprequest;

import java.io.* ; 
import java.net.* ;
import java.util.* ;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Owner Komemichael
 */
public final class HttpRequest implements Runnable{
    
    
    Socket socket;
    final static String CRLF = "\r\n";
    InputStream is = null;
    OutputStream os = null;
    DataOutputStream dos = null;

    
    public HttpRequest(Socket socket)
    {
        this.socket = socket;
    }
    
    @Override
    public void run()
    {
        try {
            processRequest();
        } 
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    
    public void processRequest() 
    {
        try
        {
            is = socket.getInputStream(); 
            os = socket.getOutputStream();
            dos = new DataOutputStream(os);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            String requestLine = br.readLine();
            System.out.println(requestLine);
            
            StringTokenizer tokens = new StringTokenizer(requestLine);
            tokens.nextToken(); // skip over the method, which should be "GET"
            String fileName = "./web/" + tokens.nextToken();
            FileInputStream fis = null;
            boolean fileExists = true;
            
            try
            {
                fis = new FileInputStream(fileName);
            }
            catch (FileNotFoundException e)
            {
                fileExists = false;
            }
            
            
            String statusLine = null;
            String contentTypeLine = null;
            String entityBody = null;
            
            
            //SENDING STATUS LINE AND RESPONSE HEADERS
            if (fileExists)
            {
                statusLine = "HTTP/1.1 200 OK" + CRLF;
                contentTypeLine = "Content-type: " + contentType( fileName ) + "; charset=UTF-8" + CRLF;
            }
            else
            {
                statusLine = "404 NOT FOUND";
                contentTypeLine = "text/html";
                entityBody = "<HTML>" +
                        "<HEAD><TITLE>Not Found</TITLE></HEAD>" + "<BODY>Not Found</BODY></HTML>";
            }
            dos.writeBytes(statusLine);
            dos.writeBytes(contentTypeLine);
            dos.writeBytes(CRLF);
            
            //SENDING THE ENTITY BODY
            if (fileExists)
            {
                sendBytes(fis, os);
                System.out.println("cont type: " + contentTypeLine);
                fis.close();
            }
            else
            {
                dos.writeBytes (entityBody);
            }
            
            os.close();
            br.close();
            socket.close();
        }
        catch (IOException ex) 
        {
            //Logger.getLogger(HttpRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    private static void sendBytes(FileInputStream fis, OutputStream os)
    {
        try {
            byte[] buffer = new byte[8000];
            int bytes = 0;
            while((bytes = fis.read(buffer)) != -1 )
            {
                os.write(buffer, 0, bytes);
            }
        } catch (IOException ex) {
            Logger.getLogger(HttpRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static String contentType(String fileName)
    {
        if(fileName.endsWith(".htm") || fileName.endsWith(".html")) 
        {
            return "text/html";
        }
        if (fileName.endsWith(".xml"))
        {
            return "application/xml";
        }
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))
        {
            return "image/jpg";
        }
        if (fileName.endsWith(".gif"))
        {
            return "image/gif";
        }
        return "application/octet-stream";
    }
}
























//        String headerLine;
//        while ((headerLine = br.readLine()).length() != 0) 
//        {
//            System.out.println(headerLine);
//        }
