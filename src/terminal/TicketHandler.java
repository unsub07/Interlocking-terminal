//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
//https://github.com/hazelcast/hazelcast/blob/v3.7.1/hazelcast/src/main/java/com/hazelcast/internal/cluster/impl/MulticastService.java#L94-L98
package terminal;

class TicketHandler {

    private Thread t;
    private boolean stop_bit;
    static final java.util.Queue<byte[]> QUEUE = new java.util.concurrent.ConcurrentLinkedQueue<>();

    private class Packet_receiver implements Runnable {

        @Override
        public void run() {
            int c;
            java.net.DatagramSocket s = Net.getDatagramSocet();
            while (!Thread.currentThread().isInterrupted()) {
                if (Exit.isEXIT()){
                    Log.log("TicketHandler return");
                    return;
                }
                if (stop_bit){
                    break;
                }
                try {
                    s.receive(Net.PACKET);
                    c = Net.PACKET.getLength();
                    byte[] b = new byte[c];
                    System.arraycopy(Net.PACKET.getData(), Net.PACKET.getOffset(), b, 0, c);
                    QUEUE.add(b);
                    Net.setPacket_count(Net.getPacket_count() + 1);
                    Net.setByte_count(Net.getByte_count() + c);
                } catch (java.net.SocketTimeoutException ignored) {
                    //
                } catch (java.io.IOException e) {
                    Err.err(e);
                }
            }
            Log.log("TicketHandler end run");
        }
    }

    void start() {
        Log.log("try start Ticet handler");
        stop_bit = false;
        (t = new Thread(new Packet_receiver())).start();
    }

    void stop() {
        t.interrupt();
        stop_bit = true;
        while (t.isAlive()) {
//            Log.log("+++ tiket handler stop");
        }
    }
}
