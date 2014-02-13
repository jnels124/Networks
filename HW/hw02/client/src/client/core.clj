(ns client.core
  (import java.io.BufferedReader
          java.net.InetAddress
          java.net.DatagramPacket
          java.net.DatagramSocket)
  (:gen-class));; create Java classes and entry thread for stand-alone application

(declare start-time)
(def port 4567)
(declare server-address)

(def invalid-id-message
  (str "The id you have entered isn't valid."
        "Please choose a valid id from the table."))

(def ids #{"00001" "00002" "00003" "00004" "00005" "00006"})

(def table
  (str "Item ID  Item Description\n"
       "00001    New Inspiron 15\n"
       "00002    New Inspiron 17\n"
       "00003    New Inspiron 15R\n"
       "00004    New Inspiron 15z Ultrabook\n"
       "00005    XPS 14 Ultrabook\n"
       "00006    New XPS 12 UltrabookXPS\n\n"))

(defn message-and-response
  "Takes a message to display on stdout and returns the response from stand in"
  [message]
  (println message)
  (read-line))

(defn process-response
  [socket]
  (let [packet (DatagramPacket. (byte-array 256) 256)]
    (.receive socket packet)
    (let [msg-vals (read-string (String. (.getData packet) 0 (.getLength packet)))]
      (println (format "%s %-27s %-10s %-9s %-15s \n%-7s %-27s %-10s %-9s %-6f\n"
                       "Item ID" "Item Description" "Unit Price" "Inventory" "RTT of Query"
                       (msg-vals 0) (msg-vals 1) (msg-vals 2 ) (msg-vals 3)
                       (/  (- (System/nanoTime) start-time) 1000000.0))))
    (.close socket)))
;; There is no reference to this socket one this method terminates so I am not sure it needs to be closed.

(defn generate-packet
  [the-bytes]
  (DatagramPacket. the-bytes (count the-bytes) server-address port))

(defn send-request
  "Sends the request to the server and returns the socket to be threaded into process-response"
  [packet]
  (let [socket (DatagramSocket.)]
    (.send socket packet)
    socket))

(defn process-request
  "Uses threading macro(->) to feed data through the methods to generate, send, and process response of the datagram. Also starts the RTT."
  [item-id]
  (def start-time (System/nanoTime))
  (-> (byte-array (.getBytes item-id)) generate-packet send-request process-response))

(defn -main
  [& args]
  (def server-address (InetAddress/getByName
                       (message-and-response "Enter the DNS or IP address to the server.")))
  (while true
    (do
      (process-request
       ((fn valid-id [item-id]
          (if (some #{item-id} #{"done" "Done" "bye" "Bye" "quit" "Quit" "Exit" "exit"})
            (System/exit 0)
            (if (not (some #{item-id} ids))
              (valid-id (message-and-response (str invalid-id-message "\n\n" table)))
              item-id)))
        ;;This will call the above lamba expression named valid-id with this as an initial argument
        (message-and-response (str table "Enter an item id. Or, type exit to quit.")))))))
