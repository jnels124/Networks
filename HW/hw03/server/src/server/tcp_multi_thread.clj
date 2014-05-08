(ns server.tcp-multi-thread
  (import (java.net ServerSocket Socket SocketException)
          (java.io InputStreamReader BufferedReader OutputStreamWriter PrintWriter)
          (java.util Date Calendar)
          (java.text DateFormat SimpleDateFormat)
          (clojure.lang LineNumberingPushbackReader))
  (use [clojure.string :only (join split)])
  (require [clojure.java.io :as io])
  (:gen-class))

(def port 4567)
(def hosted-files #{"/CS3700.html"})
(def supported-methods #{"GET"})
(def cal (Calendar/getInstance))
(def date-fmt (SimpleDateFormat. "yyyy/MM/dd HH:mm:ss"))

(defn run-thread
  "This method will take some function and start a thread with it.
   Note that all Clojure functions implement the Java runnable interface
   by default so fnctn is runnable."
  [fnctn]
  (println "Starting new thread")
  (.start (Thread. fnctn)))

(defn start-server
  "creates and returns a server socket on declared port and will pass the client
  socket to accept-client-socket on connection"
  [accept-client-socket]
  (let [ss (ServerSocket. port)] ;; Create instance of java.net.ServerSocket
    (run-thread #(when-not (.isClosed ss) ;; Run a new thread to listen for client requests for socket
                   (try (accept-client-socket (.accept ss)) ;;
                        (println "accept-client-socket called")
                        (catch SocketException e
                          (.close ss)
                          (println "There was an error with the socket." (.getMessage e))))
                   ;;repeat until server socket is closed ..
                   ;;This will not happen unless an exception
                   ;;or user event triggers termination (ie C-c)
                   (recur)))
    ss))

(defn process-header
  "Process header lines and determine appropriate status line"
  [[mthd-typ req-file vrsn]]
  (cond
   (= nil (some #{mthd-typ} supported-methods)) ["400 Bad Request" vrsn nil]
   (= nil (some #{req-file} hosted-files)) ["404 Not Found" vrsn nil]
   :else ["200 OK" vrsn req-file]))

(defn gen-response
  [[rspns-code vrsn req-file]]
  (str vrsn " " rspns-code "\r\n"
       "Date: " (.format date-fmt (.getTime cal)) "\r\n"
       "Server: IMPERATIVE=JAVA=SUCKS! FUNCTIONAL=CLOJURE=AWESOME!\r\n"
       "\r\n"
       ;; append the file to response for 200 OK case
       (if (= rspns-code "200 OK")
         (str (-> (.substring req-file 1) io/resource io/file slurp))
        "")
       "\r\n\r\n\r\n\r\n")) ;; Append ending signature

(defn process-request
  "This will take the client socket input stream and write the request to the output stream
   This function executes on a separate thread and continuously calls its self until client
   closes sckt."
  [instrm outstrm sckt]
  (when-not (.isClosed sckt)
    (try ;; Write response to clients input stream
      (let [req-line (.readLine instrm)]
        (println req-line)
        (loop [line (.readLine instrm)]
          (when-not (or (= "" line) (= nil line))
            (println line)
            (recur (.readLine instrm))))
        ;; Print all reamining lines sent by client.
         (.println outstrm (gen-response (process-header (split req-line #"\s")))))
      (catch Exception e
        (println "Stream disconnected by client")
        (println  (.getMessage e))
        (.close instrm)
        (.close outstrm)
        (.close sckt))))
  (recur instrm outstrm sckt)) ;;repeat function

(defn -main
  [& args]
  (try
    (start-server
     (fn socket-repl
       [sckt]
       (run-thread
        #(process-request
          (BufferedReader. (InputStreamReader. (.getInputStream sckt)))
          (PrintWriter. (.getOutputStream sckt) true)
          sckt))))
    (catch Exception e
      (println (.printStackTrace e)))))
