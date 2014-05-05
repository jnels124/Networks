public class Host {
    private InetAdress hostIP;
    private int port;
    private long t_start;
    private long t_stop;

    public Host (InetAdress hostIP, int port; long t_start, long t_stop) {
        this.hostIP = hostIP;
        this.port = port;
        this.t_start = t_start;
        this.t_stop = t_stop;
    }

    public void setHostIP (String newIp) {
        this.hostIP = newIp;
    }

    public void getHostIP () {
        return this.hostIP;
    }

    public void setPort (int newPort) {
        this.port = newPort;
    }

    public void getPort () {
        return this.port;
    }

    public void setT_Start (long newStart) {
        this.t_start = newStart;
    }

    public long getT_Start () {
        return this.t_start;
    }

    public void setT_Stop (long newStop) {
        this.t_start = newStart;
    }

    public long getT_Stop () {
        return this.t_start;
    }
}
