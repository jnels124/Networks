(ns udp-example.core)

(defn -main
  "This program will implement a UDP server"
  [& args]
  (if (not= 1 (count args))
    (println ("Usage: java UDPClient <hostname>"))
    (run-client (first args))))

(defn run-client
  "This method will connect with the server and wait for interactive input"
  [host-address]
  (let [udpsocket (new java.net.DatagramSocket)
        sysIn (new java.io.BufferedReader (new java.io.InputStreamReader System/in))
        from-user ""
        ]
    (while (from-user)
      (do
        (println (str "From client: " (.getBytes from-user)))))
    )
  )
