;; Replace all "sk" for your database name ex. contacts
{:db-protocol  "mysql"
 :db-name      "//localhost:3306/sk?characterEncoding=UTF-8&serverTimezone=America/Los_Angeles" ; Change me
 :database-url "mysql://localhost:3306/sk?user=root&password=xxxxx&serverTimezone=America/Los_Angeles" ; Change me
 :db-user      "root"
 :db-pwd       "xxxxx" ; Change me
 :db-class     "com.mysql.cj.jdbc.Driver"
 :email-host   "xxxxx" ; Optional
 :email-user   "xxxxx" ; Optional
 :email-pwd    "xxxxx" ; Optional
 :port         3000
 :tz           "US/Pacific"
 :site-name    "Sitio" ; Change me
 :uploads      "./uploads/sk/" ; Change me
 :base-url     "http://0.0.0.0:3000/"
 :img-url      "https://0.0.0.0/uploads/"
 :path         "/uploads/"}
