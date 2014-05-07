(ns client.core
  (import java.net.Socket
          (java.io BufferedReader PrintWriter InputStreamReader))
  (use clojure.java.io)
  (:gen-class))

(def port 4567)

(defn kill-resources
  [ins outs socket excode]
  (.close ins)
  (.close outs)
  (.close socket)
  (System/exit excode))


(defn process-header
  "Handles the header from HTTP response and will determine the RTT of request"
  [ln strm]
  (when-not (or (= ln "") (= ln nil))
    (println ln)
    (process-header (.readLine strm) strm))
  strm)

(defn process-body
  [strm out-file]
  ;;Now process the body
  ;;Have to write to path directly due to jar app
  (let [wrtr  (str  (System/getProperty "user.dir")  "/" (cond (= out-file "") "default.html"
                                              :else out-file))]
    ;Creates file (in current directory) to write request body to,
    ;or erases the contents of the file if already there
    (spit wrtr "")
    (loop [ctr 0
           ln (.readLine strm)]
      (spit wrtr (str ln "\n") :append true) ;;Write the line to outputfile
      (if (and (not= ctr 4)
               (not= ln nil)) ;; Prevent infinite loop in case server terminates.
        (if (= "" ln)
          (recur (inc ctr) (.readLine strm))
          (recur 0 (.readLine strm))))))) ;; Done processing body.

(defn message-and-response
  [message]
  (println message)
  (read-line))

(defn gen-http-request
  "This method formats user request as an http request"
  [[srvr-addr type req-file http-version user-agent]]
  (let [message
        (str type " /" req-file " HTTP/" http-version "\n"
             "Host: " srvr-addr "\n"
             "User Agent: " user-agent "\r\n")]

    [message req-file]))

(defn gen-user-request
  [srvr-addr]
  (let [mthd-type (message-and-response "Enter the http method you would like to perform.")
        requested-file (message-and-response "Please enter the name of the html file.")
        http-version (message-and-response "What http version do you want to use for the request?")
        user-agent (message-and-response "What user agent are you using?")]

    [srvr-addr mthd-type requested-file http-version user-agent]))

(defn handle-connection
  [[msg req-file] instrm outstrm srv-addr socket]
  (-> (time ((fn [] (.println outstrm msg) (try (.readLine instrm)
                                               (catch Exception e (println "Socket closed by server"))))));; Times the write to out and read to in
      (process-header instrm)
      (process-body req-file))
  (if (= "no" (message-and-response "Would you like to make another request? (yes/no)"))
    (kill-resources instrm outstrm socket 0) ;; The stray 0 Signifies normal exit from application
    (-> (gen-user-request srv-addr)
            gen-http-request
            (handle-connection instrm outstrm srv-addr socket))))

(defn -main
  [& args]
  (let [srv-addr (message-and-response "Enter the DNS or ip of the server.")
        ;;Create the socket inside a try/catch and also calculate the time which will print to *out*
        socket (try (time (Socket. srv-addr port))
                    (catch Exception e
                      (println "There was an error requesting socket from the server. Please restart."
                      (.getMessage e)
                      (System/exit -1))))]
    (-> srv-addr
        gen-user-request
        gen-http-request
        (handle-connection
         (BufferedReader. (InputStreamReader. (.getInputStream socket)))
         (PrintWriter. (.getOutputStream socket) true)
         srv-addr
         socket))))
