# PChouse PDF API

A Rest API to generate, print and sign pdf from jasper report files.

The JasperReports library is used to generate the PDF files.

This version is using JasperReports 7, to use a different version change jar version in a maven pom file.

In the maven pom file put the SQL server jdbc driver in the dependencies section as your needs.

## To execute
Listen on default port 4999 and allow only request from localhost

```
java -jar api-x.x.x.jar
```

Listen on different port
```
java -jar -Dserver.port=9999 api-x.x.x.jar
```

Authorize request from listed ip

```
java -jar -Dclient.allowIps="127.0.0.1,10.0.0.1" api-x.x.x.jar
```
Set a printer properties file, create a file named printer.properties
```
java -jar -Dapphome="/path/to/file/directory" api-x.x.x.jar
```
For SSL HTTPS setup, create a keystore file and set the path to it in application.properties

```bash

keytool -genkeypair -alias keystore-name -keyalg RSA -keysize 2048 -keystore /your/path/keystore-exmaple.p12 -storetype PKCS12 -validity 9999 -storepass password -keypass password -dname "CN=CHANGE, OU=CHANGE, O=CHANGE, L=CHANGE, S=CHANGE, C=CHANGE"
```


## Features
### Endpoints to:
- PDF
- PDF and Digital Sign (Multicert signer only for now)
- Digital Sign a PDF (Multicert signer only for now)
- PDF to images, one per page
- To printer
- Cut paper on ticket printer
- Open cash drawer


## License

MIT License

Copyright (c) 2025 PCHouse, Reflexão, Estudos e Sistemas Informáticos, LDA

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

The PCHouse PDF API has dynamic link to jasper report library via Maven repository,
the terms of license LGPL v3 is in file jasper_reports_lib_license.pdf
