echo "1、生成服务器证书库"
keytool -validity 365 -genkeypair -v -alias server -keyalg RSA -keystore %~dp0\server.keystore -dname "CN=tom,OU=grgbanking,O=grgbanking,L=guangzhou,ST=guangdong,c=cn" -storepass 123456 -keypass 123456

echo "2、生成客户端证书库"
keytool -validity 365 -genkeypair -v -alias client -keyalg RSA -storetype PKCS12 -keystore %~dp0\client.p12 -dname "CN=tom,OU=grgbanking,O=grgbanking,L=guangzhou,ST=guangdong,c=cn" -storepass 123456 -keypass 123456

echo "3、从客户端证书库中导出客户端证书"
keytool -export -alias client -keystore %~dp0\client.p12 -storetype PKCS12 -storepass 123456 -file %~dp0\client.cer

echo "4、从服务器证书库中导出服务器证书"
keytool -export -alias server -keystore %~dp0\server.keystore -storepass 123456 -file %~dp0\server.cer

echo "5、生成客户端信任证书库(由服务端证书生成的证书库)"
keytool -import -v -alias server -file %~dp0\server.cer -keystore %~dp0\server.truststore -storepass 123456

echo "6、将客户端证书导入到服务器证书库(使得服务器信任客户端证书)"
keytool -import -v -alias client -file %~dp0\client.cer -keystore %~dp0\client.truststore -storepass 123456

echo "7、查看证书库中的全部证书"
keytool -list -keystore %~dp0\client.truststore -storepass 123456