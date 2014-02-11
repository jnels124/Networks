(ns client.core
  (import java.io.BufferedReader
          java.net.InetAddress
          java.net.DatagramPacket
          java.net.DatagramSocket))

(def port 4567)

(def continue-loop (atom true))
(def server-address (atom ""))

(def invalid-id-message
  (str "The id you have entered isn't valid."
        "Please choose a valid id from the table."
        "\n\n"))

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
    (println "The response is:\n"
             (String. (.getData packet) 0 (.getLength packet)))))

(defn generate-packet
  [the-bytes]
  (DatagramPacket. the-bytes (count the-bytes) @server-address port))

(defn send-request
  [packet]
  (let [socket (DatagramSocket.)]
    (.send socket packet)
    socket))

(defn process-request
  [item-id]
  (-> (byte-array (.getBytes item-id)) generate-packet send-request process-response))

(defn -main
  [& args]
  (swap! server-address
         #(InetAddress/getByName
           (message-and-response (str "Enter the DNS or IP address to the server." %))))
  (while @continue-loop
    (do
      (process-request
       ((fn valid-id [item-id]
          (if (some #{item-id} #{"done" "Done" "bye" "Bye" "quit" "Quit" "Exit" "exit"})
            (swap! continue-loop #(false))
            (if (not (some #{item-id} ids))
              (valid-id (message-and-response (str invalid-id-message "\n\n" table)))
              item-id)))
        (message-and-response (str table "Enter an item id.")))))))

;(defn take-requests
;  [request]
;(if (not (some #{item-id} #{"done" "Done" "bye" "Bye" "quit" "Quit" "Exit" "exit"}))
;(process-request
;((fn valid-id
;   [item-id]
;(if (not (some #{item-id} ids))
;  (take-requests (message-and-response (str invalid-id-message "\n\n" table)))
;  item-id))))))
