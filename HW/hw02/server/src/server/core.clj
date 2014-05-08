(ns server.core
  (import java.net.DatagramPacket
          java.net.InetAddress
          java.net.DatagramSocket
          java.io.IOException
          java.io.BufferedReader)
  (:gen-class))

(defrecord Item
    ;This defines a non-mutable Java object.
    [id descrptn price inventory])

;hashmap with :xxxxx as key
(def item-map
  {:00001 (Item. "00001" "New Inspiron 15" "$379.99" 157)
   :00002 (Item. "00002" "New Inspiron 17" "$449.99" 128)
   :00003 (Item. "00003" "New Inspiron 15R" "$549.99" 202)
   :00004 (Item. "00004" "New Inspiron 15z Ultrabook" "$749.99" 315)
   :00005 (Item. "00005" "XPS 14 Ultrabook" "$999.99" 261)
   :00006 (Item. "00006" "New XPS 12 UltrabookXPS" "$1199.99" 178)})

(def port 4567)
(def recieve-valid (atom true))
(def socket (DatagramSocket. port))
(def in-buffer (byte-array 4096))

(defn process-request
  "This will take the users input and return a vector
   containing the contents of requested item."
  [req]
  (if (item-map (keyword req))
    (str (into [] (vals ((keyword req) item-map))))
    "The lookup code provided is invalid"))

(defn -main
  [& args]
  (try
    (while @recieve-valid ;;@ dereferences the atomic reference
      (let [in-packet (DatagramPacket. in-buffer (count in-buffer))
            mutated (.receive socket in-packet) ;Var declared only to make loop loook more Java like
            client-req (String. (.getData in-packet) 0 (.getLength in-packet))
            client-address (.getAddress in-packet)
            client-port (.getPort in-packet)]

        (let [new-buffer (.getBytes (process-request client-req))]
          (.send socket (DatagramPacket.
                         new-buffer
                         (count new-buffer)
                         client-address
                         client-port)))))

    (catch IOException e
      (str "caught exception: " (.getMessage e))
      (swap! recieve-valid #(not %)))))
