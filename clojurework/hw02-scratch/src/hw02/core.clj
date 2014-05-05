(ns hw02.core
  (import java.net.DatagramPacket
          java.net.InetAddress
          java.net.DatagramSocket
          java.io.IOException
          java.io.BufferedReader))

(defrecord Item [id descrptn price inventory])

(def item-map
  {:00001 (Item. "00001" "New Inspiron" "$379.99" 157)
   :00002 (Item. "00002" "New Inspiron 17" "$449.99" 128)
   :00003 (Item. "00003" "New Inspiron 15R" "$549.99" 202)
   :00004 (Item. "00004" "New Inspiron 15z Ultrabook" "$749.99" 315)
   :00005 (Item. "00005" "XPS 14 Ultrabook" "$999.99" 261)
   :00006 (Item. "00006" "New XPS 12 UltrabookXPS" "$1199.99" 178)})

(def recieve-valid true)
(def socket (DatagramSocket. 5000))
(def in-buffer (byte-array 256))

(defn process-request
  [req]
  (if (item-map (keyword req))
    (str (into [] (vals ((keyword req) item-map))))
    "The lookup code provided is invalid"))

(defn -main
  [& args]
  (try
    (while recieve-valid
      (let [in-packet (DatagramPacket. in-buffer (count in-buffer))
            mutated (.receive socket in-packet)
            client-req (String. (.getData in-packet) 0 (.getLength in-packet))
            client-address (.getAddress in-packet)
            client-port (.getPort in-packet)]

        ;; Return response to client
        (.send socket (DatagramPacket.
                       (byte-array (.getBytes (process-request client-req)))
                       256
                       client-address
                       client-port))))
     (catch IOException e (str "caught exception: " (.getMessage e)))))
