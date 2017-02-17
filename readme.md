Send Email with java
===

Recently, 163 changed the way to allow client to send email 
which people should send email with username and authorization code(授权码) 
but not just address and password.  Here is one way to send email 
with authorization code.   

---
### 1. Prepare
Add dependency:
```xml
<dependency>
      <groupId>javax.mail</groupId>
      <artifactId>mail</artifactId>
      <version>1.4.7</version>
</dependency>
```
### 2. Send Email
Find the main method: `com.test.mail.SendMailWithAuthCode.main`,
replace the `from_address` and `to_address`. Note that `from_address`
should belong to `163`, if not then you should change `SMTP_HOST` accordingly.

### 3.Authenticator
`javax.mail.Authenticator` is an abstract class, Session use it to
create `PasswordAuthentication`. We need username and password(code).



