(ns asgnx.core-test
  (:require [clojure.test :refer :all]
            [clojure.core.async :refer [<!!]]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure.test.check.generators :as gen]
            [asgnx.core :refer :all]
            [asgnx.kvstore :as kvstore :refer [put! get!]]))



(deftest words-test
  (testing "that sentences can be split into their constituent words"
    (is (= ["a" "b" "c"] (words "a b c")))
    (is (= [] (words "   ")))
    (is (= [] (words nil)))
    (is (= ["a"] (words "a")))
    (is (= ["a"] (words "a ")))
    (is (= ["a" "b"] (words "a b")))))


(deftest cmd-test
  (testing "that commands can be parsed from text messages"
    (is (= "foo" (cmd "foo")))
    (is (= "foo" (cmd "foo x y")))
    (is (= nil   (cmd nil)))
    (is (= ""    (cmd "")))))


(deftest args-test
  (testing "that arguments can be parsed from text messages"
    (is (= ["x" "y"] (args "foo x y")))
    (is (= ["x"] (args "foo x")))
    (is (= [] (args "foo")))
    (is (= [] (args nil)))))


(deftest parsed-msg-test
  (testing "that text messages can be parsed into cmd/args data structures"
    (is (= {:cmd "foo"
            :args ["x" "y"]}
           (parsed-msg "foo x y")))
    (is (= {:cmd "foo"
            :args ["x"]}
           (parsed-msg "foo x")))
    (is (= {:cmd "foo"
            :args []}
           (parsed-msg "foo")))
    (is (= {:cmd "foo"
            :args ["x" "y" "z" "somereallylongthing"]}
           (parsed-msg "foo x y z somereallylongthing")))))


(deftest create-router-test
  (testing "correct creation of a function to lookup a handler for a parsed message"
    (let [router (create-router {"hello" #(str (:cmd %) " " "test")
                                 "argc"  #(count (:args %))
                                 "echo"  identity
                                 "default" (fn [& a] "No!")})
          msg1   {:cmd "hello"}
          msg2   {:cmd "argc" :args [1 2 3]}
          msg3   {:cmd "echo" :args ["a" "z"]}
          msg4   {:cmd "echo2" :args ["a" "z"]}]
      (is (= "hello test" ((router msg1) msg1)))
      (is (= "No!" ((router msg4) msg4)))
      (is (= 3 ((router msg2) msg2)))
      (is (= msg3 ((router msg3) msg3))))))


(deftest action-send-msg-test
  (testing "That action send msg returns a correctly formatted map"
    (is (= :send
           (:action (action-send-msg :bob "foo"))))
    (is (= :bob
           (:to (action-send-msg :bob "foo"))))
    (is (= "foo"
           (:msg (action-send-msg [:a :b] "foo"))))))


(deftest action-send-msgs-test
  (testing "That action send msgs generates a list of sends"
    (let [a (action-send-msg [:a :f :b] 1)
          b (action-send-msg [:a :f :d] 1)
          c (action-send-msg [:a :f :e] 1)
          d (action-send-msg [:a :f :c] 1)]
      (is (= [a b c d]
             (action-send-msgs [[:a :f :b]
                                [:a :f :d]
                                [:a :f :e]
                                [:a :f :c]]
                              1))))))

;(deftest action-insert-test
  ;(testing "That action insert returns a correctly formatted map"
    ;(is (= #{:action :ks :v}
           ;(into #{}(keys (action-insert [:a :b] {:foo 1}))))))
    ;(is (= #{:assoc-in [:a :b] {:foo 1}}
           ;(into #{}(vals (action-insert [:a :b] {:foo 1}))))))
    ;(is (= :assoc-in
           ;(:action (action-insert [:a :b] {:foo 1})))))
    ;(is (= {:foo 1}
           ;(:v (action-insert [:a :b] {:foo 1})))))
    ;(is (= [:a :b]
           ;(:ks (action-insert [:a :b] {:foo 1})))))))


(deftest action-remove-test
  (testing "That action remove returns a correctly formatted map"
    (is (= #{:action :ks}
         (into #{} (keys (action-remove [:a :b])))))
    (is (= #{:dissoc-in [:a :b]}
          (into #{}(vals (action-remove [:a :b])))))
    (is (= :dissoc-in
           (:action (action-remove [:a :b]))))
    (is (= [:a :b]
           (:ks (action-remove [:a :b]))))))


(deftest action-inserts-test
  (testing "That action inserts generates a list of inserts"
    (let [a (action-insert [:a :f :b] 1)
          b (action-insert [:a :f :d] 1)
          c (action-insert [:a :f :e] 1)
          d (action-insert [:a :f :c] 1)]
      (is (= [a b c d]
             (action-inserts [:a :f] [:b :d :e :c] 1))))))


(defn action-send [system {:keys [to msg]}]
  (put! (:state-mgr system) [:msgs to] msg))

(defn pending-send-msgs [system to]
  (get! (:state-mgr system) [:msgs to]))

(def send-action-handlers
  {:send action-send})

(deftest handle-message-test
  (testing "the integration and handling of messages"
    (let [ehdlrs (merge
                   send-action-handlers
                   kvstore/action-handlers)
          state  (atom {})
          smgr   (kvstore/create state)
          system {:state-mgr smgr
                  :effect-handlers ehdlrs}]
      (is (= "There are no wait times reported for that line."
             (<!! (handle-message
                    system
                    "test-user"
                    "ask rand-bowls"))))
      (is (= "The lines you can report and ask about are:\nrand-bowls\nrand-grill\nrand-sandwich\nrand-tortelini\ngrins\ncommons\nbronsonf"
             (<!! (handle-message
                   system
                   "test-user"
                   "lines-info"))))
      (is (= "https://campusdining.vanderbilt.edu"
             (<!! (handle-message
                   system
                   "test-user"
                    "homepage"))))
      (is (= "test-user successfully reported a wait time for rand-bowls as 3 minutes."
             (<!! (handle-message
                    system
                    "test-user"
                    "report rand-bowls 3"))))
      (is (= "test-user successfully reported a wait time for rand-bowls as 10 minutes."
             (<!! (handle-message
                   system
                   "test-user"
                   "report rand-bowls 10"))))
      (is (= "test-user successfully reported a wait time for rand-sandwich as 15 minutes."
             (<!! (handle-message
                   system
                   "test-user"
                   "report rand-sandwich 15"))))
      (is (= "Please report for a valid campus dining line."
             (<!! (handle-message
                    system
                    "test-user2"
                    "report rand-bo"))))
      (is (= "Please report a wait time for the line."
             (<!! (handle-message
                    system
                    "test-user"
                    "report rand-bowls"))))
      (is (= "Please ask for a valid campus dining line."
              (<!! (handle-message
                    system
                    "test-user2"
                    "ask rand-bo")))))))
