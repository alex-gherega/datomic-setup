(set-env!
 :resource-paths #{"src"}
 :dependencies '[[com.datomic/datomic-pro "0.9.5561" :exclusions [org.slf4j/slf4j-nop org.slf4j/slf4j-log4j12]]
                 [ch.qos.logback/logback-classic "1.1.7"]
                 [com.datomic/clj-client "0.8.606"]
                 [me.raynes/conch "0.8.0"]]
 :repositories #(conj %
                      ["my.datomic.com" {:url "https://my.datomic.com/repo"
                                         :username "alex.gherega@gmail.com"
                                         :password "5e0ae682-5b3b-405d-b524-4b894542d65a"}]))

;; (task-options!
;;   pom {:project 'my-project
;;        :version "0.1.0"}
;;   jar {:manifest {"Foo" "bar"}})

(require '[datomic.api :refer [create-database]]
         '[me.raynes.conch :refer [programs let-programs]]
         '[me.raynes.conch.low-level :refer [proc stream-to-out]])

(deftask start-transactor
  "Start a Datomic transactor"
  [d datomic-path VALUE str "Path to datomic installation dir"
   t transactor-props VALUE str "Path to transactor properties file"]
  ;; (let [process-builder (ProcessBuilder. (list (str datomic-path "/bin/transactor")
  ;;                                              (str (System/getProperty "user.dir") "/" transactor-props)))
  ;;       process (.start process-builder)]
  ((sh (str datomic-path "/bin/transactor")
       (str (System/getProperty "user.dir") "/" transactor-props))))

(deftask make-db
  "Create a Datomic database"
  [u uri VALUE str "Datomic database uri"]
  (try
    (let [create (datomic.api/create-database uri)]
      (prn uri " database created: " create))
    (catch Exception e (do (Thread/sleep 1000) (make-db "-u" uri)))))

(deftask start-peerserver
  "Start the Datormic  Peer Server"
  [d datomic-path VALUE str "Path to datomic installation dir"
   n host VALUE str "Host for the peer server"
   p port VALUE str "Peer server listening port"
   a auth VALUE str "Acces key,secret"
   b db VALUE str "Database name,uri"]
  ((sh (str datomic-path "/bin/run")
       "-m" "datomic.peer-server"
       "-h" host
       "-p" port
       "-a" auth
       "-d" db)))

(deftask kill-process
  [p process-name VALUE str "Process name"]
  (programs grep ps)
  (try
    (let [processes (clojure.string/split (grep process-name (ps "-ax" {:seq true}))
                                          #"\n")
          _ (prn "SPLIT  " processes)
          pids (map #(second (clojure.string/split % #" ")) processes)]
      
      (prn pids)
      (doall (map #((sh "kill" "-9" %)) pids)))
    (catch Exception e)))

(deftask stop-transactor []
  (kill-process "-p" "start-transactor")
  (kill-process "-p" "datomic-transactor"))

(deftask stop-peerserver []
  (kill-process "-p" "peer-server")
  (kill-process "-p" "peerserver"))

(deftask datomic-setup
  [d datomic-path VALUE str "Path to datomic directory"
   b db VALUE str "Datomic database name"]
  (future (start-transactor "-d" datomic-path "-t" "./transactor.properties"))
  (make-db "-u" (str "datomic:dev://localhost:4334/" db))
  (start-peerserver "-d" datomic-path "-n" "localhost" "-p" "3334" "-a" "myaccesskey,secret" "-b" (str db ",datomic:dev://localhost:4334/" db)))
