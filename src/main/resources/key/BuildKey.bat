echo "1�����ɷ�����֤���"
keytool -validity 365 -genkeypair -v -alias server -keyalg RSA -keystore %~dp0\server.keystore -dname "CN=tom,OU=grgbanking,O=grgbanking,L=guangzhou,ST=guangdong,c=cn" -storepass 123456 -keypass 123456

echo "2�����ɿͻ���֤���"
keytool -validity 365 -genkeypair -v -alias client -keyalg RSA -storetype PKCS12 -keystore %~dp0\client.p12 -dname "CN=tom,OU=grgbanking,O=grgbanking,L=guangzhou,ST=guangdong,c=cn" -storepass 123456 -keypass 123456

echo "3���ӿͻ���֤����е����ͻ���֤��"
keytool -export -alias client -keystore %~dp0\client.p12 -storetype PKCS12 -storepass 123456 -file %~dp0\client.cer

echo "4���ӷ�����֤����е���������֤��"
keytool -export -alias server -keystore %~dp0\server.keystore -storepass 123456 -file %~dp0\server.cer

echo "5�����ɿͻ�������֤���(�ɷ����֤�����ɵ�֤���)"
keytool -import -v -alias server -file %~dp0\server.cer -keystore %~dp0\server.truststore -storepass 123456

echo "6�����ͻ���֤�鵼�뵽������֤���(ʹ�÷��������οͻ���֤��)"
keytool -import -v -alias client -file %~dp0\client.cer -keystore %~dp0\client.truststore -storepass 123456

echo "7���鿴֤����е�ȫ��֤��"
keytool -list -keystore %~dp0\client.truststore -storepass 123456