/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServer;

import java.net.* ;

/**
 *
 * @author Owner
 */
public final class WebServer {
    public static void main(String args[]) throws Exception
    {
        final int port = 5555; //Set port number
        
        ServerSocket server_socket = new ServerSocket(port, 1000);
        while (true)
        {
            Socket clientSocket = server_socket.accept();
            httprequest.HttpRequest request = new httprequest.HttpRequest(clientSocket);
            Thread thread = new Thread(request);
            thread.start();
        }
    }
}


