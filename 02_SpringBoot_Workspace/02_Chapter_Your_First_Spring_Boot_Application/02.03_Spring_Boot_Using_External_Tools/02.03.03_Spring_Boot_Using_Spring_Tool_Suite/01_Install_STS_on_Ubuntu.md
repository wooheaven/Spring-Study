```{bash}
$ pwd
/home/rwoo/03_Programs/spring-tool-suite

$ tar -zxf spring-tool-suite-3.9.0.RELEASE-e4.7.0-linux-gtk-x86_64.tar.gz

$ sudo vi /usr/share/applications/STS.desktop

$ cat /usr/share/applications/STS.desktop
[Desktop Entry]
Name=SpringSource Tool Suite
Comment=SpringSource Tool Suite
Exec=/home/rwoo/03_Programs/spring-tool-suite/sts-bundle/sts-3.9.0.RELEASE/STS
Icon=/home/rwoo/03_Programs/spring-tool-suite/sts-bundle/sts-3.9.0.RELEASE/icon.xpm
StartupNotify=true
Terminal=false
Type=Application
Categories=Development;IDE;Java;
```
