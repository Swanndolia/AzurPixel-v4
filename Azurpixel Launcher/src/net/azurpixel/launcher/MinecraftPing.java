package net.azurpixel.launcher;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.google.gson.Gson;

public class MinecraftPing {
    
	public static long latency;
    	
    public MinecraftPingReply getPing(String host, int port) throws IOException {

        final Socket socket = new Socket();
        long startTime = System.currentTimeMillis();
        socket.connect(new InetSocketAddress(host, port));
        latency = System.currentTimeMillis() - startTime;
        final DataInputStream in = new DataInputStream(socket.getInputStream());
        final DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        ByteArrayOutputStream handshake_bytes = new ByteArrayOutputStream();
        DataOutputStream handshake = new DataOutputStream(handshake_bytes);

        handshake.writeByte(0x00);
        writeVarInt(handshake, 4);
        writeVarInt(handshake, host.length());
        handshake.writeBytes(host);
        handshake.writeShort(port);
        writeVarInt(handshake, 1);

        writeVarInt(out, handshake_bytes.size());
        out.write(handshake_bytes.toByteArray());

        out.writeByte(0x01);
        out.writeByte(0x00);

        readVarInt(in);
        int id = readVarInt(in);

        io(id == -1, "Server prematurely ended stream.");
        io(id != 0x00, "Server returned invalid packet.");

        int length = readVarInt(in);
        io(length == -1, "Server prematurely ended stream.");
        io(length == 0, "Server returned unexpected value.");

        byte[] data = new byte[length];
        in.readFully(data);
        String json = new String(data, "UTF-8");

        out.writeByte(0x09);
        out.writeByte(0x01);
        out.writeLong(System.currentTimeMillis());

        readVarInt(in);
        id = readVarInt(in);
        io(id == -1, "Server prematurely ended stream.");
        io(id != 0x01, "Server returned invalid packet.");


        handshake.close();
        handshake_bytes.close();
        out.close();
        in.close();
        socket.close();

        return new Gson().fromJson(json, MinecraftPingReply.class);
        
    }

    
    public static void io(final boolean b, final String m) throws IOException {
        if (b) {
            throw new IOException(m);
        }
    }

    public static int readVarInt(DataInputStream in) throws IOException {
        int i = 0;
        int j = 0;
        while (true) {
            int k = in.readByte();

            i |= (k & 0x7F) << j++ * 7;

            if (j > 5)
                throw new RuntimeException("VarInt too big");

            if ((k & 0x80) != 128)
                break;
        }

        return i;
    }

    public static void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
        while (true) {
            if ((paramInt & 0xFFFFFF80) == 0) {
                out.writeByte(paramInt);
                return;
            }

            out.writeByte(paramInt & 0x7F | 0x80);
            paramInt >>>= 7;
        }
    }
    
	public class MinecraftPingReply {
	    
	    private Players players;
	    
	    public Players getPlayers() {
	        return players;
	    }
	    
	    public class Players {
	        private int max;
	        private int online;

	        public int getMax() {
	            return this.max;
	        }

	        public int getOnline() {
	            return this.online;
	        }
	    }
		public long getLatency() {
			return latency;
		}
	}
}