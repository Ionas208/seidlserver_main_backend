package com.seidlserver.wol;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


/*
    Created by: Jonas Seidl
    Date: 19.03.2021
    Time: 17:45
*/

/***
 * Class to craft WOL packets and send them to server
 */
public class WakeOnLan {

    public static final int PORT = 9;
    public static final String ipStr = "10.0.0.255";
    public static final String macStr = "FC-AA-14-1F-9F-AB";


    /***
     * Builds packet and sends it to server
     * @throws Exception
     */
    public static void wake() throws Exception{
        byte[] macBytes = getMacBytes(macStr);
        byte[] bytes = new byte[6 + 16 * macBytes.length];
        for (int i = 0; i < 6; i++) {
            bytes[i] = (byte) 0xff;
        }
        for (int i = 6; i < bytes.length; i += macBytes.length) {
            System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
        }

        InetAddress address = InetAddress.getByName(ipStr);
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);
        DatagramSocket socket = new DatagramSocket();
        socket.send(packet);
        socket.close();
    }

    /***
     * Checks MAC for errors and returns bytes
     * @param macStr MAC Address
     * @return Bytes of MAC Address
     * @throws IllegalArgumentException
     */
    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }

}
