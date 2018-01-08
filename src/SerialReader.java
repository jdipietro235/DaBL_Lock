import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class SerialReader implements SerialPortEventListener {
    SerialPort serialPort;

    private static final String PORT_NAMES[] = {
            "COM3"
    };

    private BufferedReader input;

    private OutputStream output;

    private static final int TIME_OUT = 2000;

    private static final int DATA_RATE = 9600;

    ServerSocket servSock = new ServerSocket(6000);
    Socket sock = servSock.accept();

    ObjectOutputStream outStream = new ObjectOutputStream(sock.getOutputStream());
    ObjectInputStream inStream = new ObjectInputStream(sock.getInputStream());

    public SerialReader() throws IOException {
    }

    public void initialize() {
        CommPortIdentifier portID = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        while (portEnum.hasMoreElements()){
            CommPortIdentifier currPortID = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortID.getName().equals(portName)) {
                    portID = currPortID;
                    break;
                }
            }
        }
        if (portID == null) {
            System.out.println("COM port not found");
            return;
        }

        try {
            serialPort = (SerialPort) portID.open(this.getClass().getName(),TIME_OUT);

            serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public synchronized void close() {
        if(serialPort != null){
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    public synchronized void serialEvent(SerialPortEvent oEvent){
        if(oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE){
            try {
                String inputLine = input.readLine();
                System.out.println(inputLine);
            }
            catch (Exception e){
                System.out.println(e.toString());
            }
        }
    }

    private List<CardReadListener> listeners = new ArrayList<CardReadListener>();

    public void addListener(CardReadListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(){
        for (CardReadListener listener : listeners){
            try {
                listener.onRead(Integer.parseInt(input.readLine()));
            }
            catch (Exception e){
                System.out.println(e.toString());
            }

        }
    }

    /*
    public static void serialReader(String args) throws Exception {
        SerialReader serialReader = new SerialReader();
        serialReader.initialize();
        Thread t = new Thread(){
            public void run() {
                try {
                    Thread.sleep(1000000);
                }
                catch (InterruptedException ie) {}
            }
        };
    }
    */


}
