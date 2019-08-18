(ns sk.models.email
  (:require [postal.core :refer [send-message]]
            [sk.models.crud :refer [config]]))

;;(send-message {:host "email-host"
;;               :user "email-user"
;;               :pass "email-password"
;;               :ssl  true}
;;              {:from    "me@draines.com"
;;               :to      "foo@example.com"
;;               :subject "Hi!"
;;               :body    [{:type    "text/html"
;;                          :content "<b>Test!</b>"}
;;                           ;;;; supports both dispositions:
;;                         {:type    :attachment
;;                          :content (java.io.File. "/tmp/foo.txt")}
;;                         {:type         :inline
;;                          :content      (java.io.File. "/tmp/a.pdf")
;;                          :content-type "application/pdf"}]})
;;{:code 0, :error :SUCCESS, :message "message sent"}      ;Returned error messages
;;
;;{:host "mail.gmx.com"
;; :user "xxxxxxx@gmx.com"
;; :pass "xxxxxxx"
;; :tls  true}
;;
;;{:host "smtp.gmail.com"
;; :user "xxxxxxx@gmx.com"
;; :pass "xxxxxxxx"
;; :ssl  true}

(def host
  {:host (:email-host config)
   :user (:email-user config)
   :pass (:email-pwd config)
   :ssl  true})

(def body
  {:from    "hectorqlucero@gmail.com"
   :to      "hectorqlucero@gmail.com"
   :subject "Hi!"
   :body    [{:type    "text/html;charset=utf-8"
              :content "<b>Testing</b>"}]})
(defn send-email [host body]
  (send-message host body))

;;(send-email host body)
