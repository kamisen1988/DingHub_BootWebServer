# BootWebServer
· BootWebServer是ddtc Hub的WebServer功能。
http://localhost:8080/hub/login


url先传入aop切面，也即AuthAspect.java文件，
如果是loginin请求，要校验appId和appSecret获取toBToken，
然后发回给HelloControler.java这类proceedingJoinPoint