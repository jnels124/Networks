(ns client.core
  (import java.io.BufferedReader
          java.net.InetAddress
          java.net.DatagramPacket
          java.net.DatagramSocket))

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



(defn -main
  [& args]
  (let [server-addr (message-and-response "Enter the DNS or IP address to the server.")
        server-port 5000
        item-id (message-and-response (str table "Enter an item id."))]
    (while (complement (some #{@item-id} ids))
      (do (swap! item-id (message-and-response (str invalid-id-message "\n\n" table)))))))

(defn process-request
  [item-id]
  (let [id-bytes (byte-array (.getBytes item-id))
        request-packet (DatagramPacket. id-bytes)
        server-socket (DatagramSocket. )]
    (.send request-packet )))


](defn process-response)
(defn -main
  [& args]
  (let [server-addr (message-and-response "Enter the DNS or IP address to the server.")
        server-port 5000
        item-id (message-and-response (str table "Enter an item id."))]
    (while (complement (some #{@item-id} ids))
      (do (swap! item-id (message-and-response (str invalid-id-message "\n\n" table)))))))
