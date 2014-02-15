(ns server.tcp-multi-thread
  (import (java.net ServerSocket Socket SocketException)
          (java.io InputStreamReader OutputStreamWriter)))

(defn run-thread
  "This method will take some function and start a thread with it.
   Note that all Clojure functions implement the Java runnable interface
   by default so fnctn is runnable."
  [fnctn]
  (.start (Thread. f)))

(defn start-server
  "creates and returns a server socket on declared port and will pass the client
  socket to accept-client-socket on connection"
  [accept-client-socket port]
    (let [ss (new ServerSocket port)]
      (run-thread #(when-not (.isClosed ss)
                    (try (accept-client-socket (.accept ss ))
                         (catch SocketException e
                           (println "There was an error with the socket." (.getMessage e))))
                    (recur)))
      ss))

(defn process-request
  "This will take the client socket input stream and write the request to the output stream"
  [instrm outstrm]
  (binding [*ns* (create-ns 'user) ;; For good measure ... Not sure how various threads react to ns
            *warn-on-reflection* false ;; Avoid java type casiting warnings
            *out* (OutputStreamWriter. outstrm)] ;; bind stndout to socket outstrm
    (let [eof "\n"
          rdr (InputStreamReader. instrm)]
      (loop [nwln (read rdr false eof)]
        (when-not (= nlwn eof)
          (println "A line " nwln))))
    ))

(defn -main
  [& args]
  (try
    (start-server
     (fn socket-repl
       [sckt]
       (run-thread
        #(process-request (.getInputStream sckt) (.getOutputStream sckt)))))
    (catch Exception e
      (println (.printStackTrace e)))))
