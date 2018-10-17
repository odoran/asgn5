(ns asgnx.core
  (:require [clojure.string :as string]
            [clojure.core.async :as async :refer [go chan <! >!]]
            [asgnx.kvstore :as kvstore
             :refer [put! get! list! remove!]]))


(defn safe-key-str [s]
    (string/replace s #"[^a-zA-Z0-9]" ""))


(def times {"rand-bowls" {:Mon  "11AM - 3PM,4:30 PM - 8PM"
                                  :Tue "11AM - 3PM"
                                  :Wed "11AM - 3PM, 4:30 PM - 8PM"
                                  :Thurs "11AM - 3PM, 4:30 PM - 8PM"
                                  :Fri "11AM - 3PM"
                                  :Sat "11AM - 3PM"
                                  :Sun "11AM - 3PM, 4:30 PM - 8PM"}


                    "rand-grill" {:Mon  "11AM - 3PM, 4:30 PM - 8PM"
                                                      :Tue "11AM - 3PM, 4:30 PM - 8PM"
                                                      :Wed "11AM - 3PM, 4:30 PM - 8PM"
                                                      :Thurs "11AM - 3PM, 4:30 PM - 8PM"
                                                      :Fri "11AM - 3PM, 4:30 PM - 8PM"
                                                      :Sat "4:30 PM - 8PM"
                                                      :Sun "4:30 PM - 8PM"}

                    "rand-sandwich" {:Mon  "11AM - 3PM"
                                                      :Tue "11AM - 3PM"
                                                      :Wed "11AM - 3PM"
                                                      :Thurs "11AM - 3PM"
                                                      :Fri "11AM - 3PM"
                                                      :Sat "Closed"
                                                      :Sun "Closed"}

                    "rand-tortelini" {:Mon  "Closed"
                                                      :Tue "4:30PM - 8PM"
                                                      :Wed "Closed"
                                                      :Thurs "Closed"
                                                      :Fri "Closed"
                                                      :Sat "Closed"
                                                      :Sun "Closed"}

                    "grins" {:Mon  "7:30 AM - 9 PM"
                                                      :Tue "7:30AM - 9PM"
                                                      :Wed "7:30AM - 9PM"
                                                      :Thurs "7:30AM - 9PM"
                                                      :Fri "7:30AM - 3PM"
                                                      :Sat "Closed"
                                                      :Sun "Closed"}
                    "commons" {:Mon  "7:30 AM - 9 PM"
                                                      :Tue "7AM - 8PM"
                                                      :Wed "7AM - 8PM"
                                                      :Thurs "7AM - 8PM"
                                                      :Fri "7 AM - 8 PM"
                                                      :Sat "10AM - 2PM, 4:30PM - 8PM"
                                                      :Sun "10AM - 2PM, 4:30PM - 8PM"}
                    "bronson" {:Mon  "7:30AM - 10AM, 11AM - 7:30PM"
                                                      :Tue "7:30AM - 10AM, 11AM - 7:30PM"
                                                      :Wed "7:30AM - 10AM, 11AM - 7:30PM"
                                                      :Thurs "7:30AM - 10AM, 11AM - 7:30PM"
                                                      :Fri "7:30AM - 10AM"
                                                      :Sat "5:30 PM - 7:30PM"
                                                      :Sun "5:30 PM - 7:30PM"}})




;; words
;; This is a helper function to implement 'cmd' and 'args'
;;
(defn words [msg]
  (if msg
      (string/split msg #" ")
      []))


;; cmd
;; This function returns the first word in a text message.
;;
;; Example: (cmd "foo bar") => "foo"
;;
;; See the cmd-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn cmd [msg]
  ( let [vec (words msg)] (first vec)))


;; args
;; This function returns the list of words following
;; the command in a text message.
;;
;; Example: (args "foo bar baz") => ("bar" "baz")
;;
;; See the args-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn args [msg]
  (let [vec (words msg)] (rest vec)))


;; parsed-msg
;; This function returns a map with keys for the
;; :cmd and :args parsed from the msg.
;;
;; Example:
;;
;; (parsed-msg "foo bar baz") => {:cmd "foo" :args ["bar" "baz"]}
;;
;; See the parsed-msg-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn parsed-msg [msg] (let [first (cmd msg) last (args msg)]
                         {:cmd first
                          :args last}))


;; action-send-msg
;; This funciton takes a destination for the msg in a parameter called `to`
;; and the message in a parameter called `msg` and returns
;; a map with the keys :to and :msg bound to each parameter.
;; The map should also have the key :action bound to the value
;; :send.
;;
(defn action-send-msg [to msg]
  (let
    [resp (hash-map :to to :msg msg :action :send)]
    resp))


;; action-send-msgs
;; This function takes a list of people to receive a message in a `people`
;; parameter and a message to send them in a `msg` parmaeter
;; and returns a list produced by invoking the above `action-send-msg`
;; function on each person in the people list.
;;
;; java-like pseudo code:
;;
;; output = new list
;; for person in people:
;;   output.add( action-send-msg(person, msg) )
;; return output
;;
(defn action-send-msgs [people msg]
  (for [p people]
    (action-send-msg p msg)))


;; action-insert
;; This function takes a list of keys in a `ks` parameter, a value to
;; bind to that key path to in a `v` parameter, and returns a map with
;; the key :ks bound to the `ks` parameter value and the key :v
;; vound to the `v` parameter value.)
;; The map should also have the key :action bound to the value
;; :assoc-in.
;;
(defn action-insert [ks v]
  (let [resp (sorted-map :ks ks :v v :action :assoc-in)]
    resp))


;; action-inserts
;;
;; This function called action-inserts takes:
;; 1. a key prefix (e.g., [:a :b])
;; 2. a list of suffixes for the key (e.g., [:c :d])
;; 3. a value to bind
;;
;; and calls (action-insert combined-key value) for each possible
;; combined-key that can be produced by appending one of the suffixes
;; to the prefix.
;;
;; In other words, this invocation:
;;
;; (action-inserts [:foo :bar] [:a :b :c] 32)
;;
;; would be equivalent to this:
;;
;; [(action-insert [:foo :bar :a] 32)
;;  (action-insert [:foo :bar :b] 32)
;;  (action-insert [:foo :bar :c] 32)]
;;
(defn action-inserts [prefix ks v]
  (for [k ks]
    (action-insert (conj prefix k) v)))

;; action-remove
;; This function takes a list of keys in a `ks` parameter and
;; returns a map with the key :ks bound to the `ks` parameter value.
;; The map should also have the key :action bound to the value
;; :dissoc-in.
;;
(defn action-remove [ks]
  (let
    [resp (sorted-map :ks ks :action :dissoc-in)]
    resp))


;; date-time-now-str
;; This function creates a date object that includes
;; the format "E MM/dd/yyyy HH:mm"

(defn date-time-now-str []
            #?(:clj
               (.format
                 (java.time.format.DateTimeFormatter/ofPattern
                  "E MM/dd/yyyy HH:mm"
                  java.util.Locale/ENGLISH)
                 (java.time.LocalDateTime/now))
               :cljs
               (let [d (js/Date.)
                     hour (- (.getHours d) 5)]
                 (str
                   (get ["Sun" "Mon" "Tues" "Wed" "Thurs" "Fri" "Sat"] (.getDay d)) " "
                   (+ (.getMonth d) 1) "/"
                   (.getDate d) "/"
                   (.getFullYear d) " "
                   (if (neg? hour)
                       (+ hour 12)
                       hour) ":"
                   (.getMinutes d)))))




;; experts-register
;; This function takes the current application `topic`, a `date`
;; 'info', and information to register a certain dining hall waiting
;; time under a specific dining hall line
;;
;; See the integration test in See handle-message-test for the
;; expectations on how your code operates
;;
(defn experts-register [experts topic date info]
  [(action-insert [:report] (assoc experts topic {date info}))])



(defn experts-question-msg [experts question-words]
  (str "Asking " (count experts) " expert(s) for an answer to: \""
       (string/join " " question-words) "\""))


;; ask-experts
;; This function takes in experts, and the args and time keys from experts.
;; It uses these parameters to get the wait time, the time it was reported
;; at, and the hours of that line.
;;
;; See the integration test in See handle-message-test for the
;; expectations on how your code operates
;;
(defn ask-experts [experts {:keys [args time]}]
  (let [day (first (string/split (date-time-now-str) #" "))
        hours (get (get times (first args)) day)
        location (first args)
        wait-data (get experts location)
        last-report (first (sort (keys wait-data)))
        wait-time (get wait-data last-report)
        day-of-week (first (string/split (date-time-now-str) #" "))
        _ (println "day-of-week: " day-of-week)
        open-close (get-in times [location (keyword day-of-week)])
        _ (println "map test: " (get times location))]
       (if (not (contains? times (first args)))
         [[], "Please ask for a valid campus dining line."]
         (if (= wait-time nil)
           [[], "There are no wait times reported for that line."]
           [[],
            (str "The wait time is "
              wait-time " minutes, and was reported at "
              last-report ". The following hours are " open-close ".")]))))



;; add-expert
;; This function takes in experts, and the args and time keys from experts.
;; It uses these parameters to let the reporter know that they have reported
;; a wait time for the dining hall line they wanted.
;;
;; See the integration test in See handle-message-test for the
;; expectations on how your code operates
(defn add-expert [experts {:keys [args user-id]}]
  (if (not (contains? times (first args)))
      [[], "Please report for a valid campus dining line."]
      (if (= (first (rest args)) nil)
        [[], "Please report a wait time for the line."]
        [(experts-register experts (first args) (date-time-now-str) (first (rest args)))
         (str user-id " successfully reported a wait time for " (first args)
          " as " (first (rest args)) " minutes.")])))

(defn lines-info [_]
    (let [str  "The lines you can report and ask about are:
rand-bowls
rand-grill
rand-sandwich
rand-tortelini
grins
commons
bronson"]
     str))


;; Don't edit!
(defn stateless [f]
  (fn [_ & args]
    [[] (apply f args)]))


(def routes {"default"  (stateless (fn [& args] "Unknown command."))
             "report"   add-expert
             "ask"      ask-experts
             "lines-info"    (stateless lines-info)})

;use same query that you are using for ask, use the same format ([], print) and can print wiat times for all lines




(defn experts-on-topic-query [state-mgr pmsg]
  (let [[topic]  (:args pmsg)]
    (get! state-mgr [:report])))

(defn experts-on-topic-query-ask [state-mgr pmsg]
  (println "experts-on-topic-query :args " (:args pmsg))
  (let [[topic]  (:args pmsg)
        result   (get! state-mgr [:report])]
    (println "query result " result)
    result))


(defn conversations-for-user-query [state-mgr pmsg]
  (let [user-id (:user-id pmsg)]
    (get! state-mgr [:conversations user-id])))



(def queries
  {
   "report" experts-on-topic-query
   "ask"    experts-on-topic-query-ask})
   




(defn read-state [state-mgr pmsg]
  (go
    (if-let [qfn (get queries (:cmd pmsg))]
      (<! (qfn state-mgr pmsg))
      {})))



;; create-router
;; This function takes a parsed message as input and returns the
;; function in the `routes` map that is associated with a key matching
;; the `:cmd` in the parsed message.
;;
;; If there isn't a function in the routes map that is mapped to a
;; corresponding key for the command, you should return the function
;; mapped to the key "default".
;;
;; See the create-router-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn create-router [routes]
  (fn [pmsg]
    (let [answer (get routes (get pmsg :cmd))]
      (if (nil? answer)
        (get routes "default")
        answer))))


(defn output [o]
  (second o))


(defn actions [o]
  (first o))


(defn invoke [{:keys [effect-handlers] :as system} e]
  (go
    (println "    Invoke:" e)
    (if-let [action (get effect-handlers (:action e))]
      (do
        (println "    Invoking:" action "with" e)
        (<! (action system e))))))


(defn process-actions [system actions]
  (go
    (println "  Processing actions:" actions)
    (let [results (atom [])]
      (doseq [action actions]
        (let [result (<! (invoke system action))]
          (swap! results conj result)))
      @results)))


(defn handle-message
  "
    This function orchestrates the processing of incoming messages
    and glues all of the pieces of the processing pipeline together.

    The basic flow to handle a message is as follows:

    1. Create the router that will be used later to find the
       function to handle the message
    2. Parse the message
    3. Load any saved state that is going to be needed to process
       the message (e.g., querying the list of experts, etc.)
    4. Find the function that can handle the message
    5. Call the handler function with the state from #3 and
       the message
    6. Run the different actions that the handler returned...these actions
       will be bound to different implementations depending on the environemnt
       (e.g., in test, the actions aren't going to send real text messages)
    7. Return the string response to the message

  "
  [{:keys [state-mgr] :as system} src msg]
  (go
  ;  (println "=========================================")
  ;  (println "  Processing:\"" msg "\" from" src)
    (let [rtr    (create-router routes)
          ;_      (println "  Router:" rtr)
          pmsg   (assoc (parsed-msg msg) :user-id src)
          ;_      (println "  Parsed msg:" pmsg)
          state  (<! (read-state state-mgr pmsg))
          _      (println "  Read state:" state)
          hdlr   (rtr pmsg)
          ;_      (println "  Hdlr:" hdlr)
          [as o] (hdlr state pmsg)
          ;_      (println "  Hdlr result:" [as o])
          arslt  (<! (process-actions system as))]
          ;_      (println "  Action results:" arslt)]
    ;  (println "=========================================")
      o)))
