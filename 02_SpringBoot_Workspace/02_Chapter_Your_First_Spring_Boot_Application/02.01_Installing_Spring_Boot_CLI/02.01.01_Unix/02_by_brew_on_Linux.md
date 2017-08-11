# Install Linux Brew on Linux
```{bash}
$ # Dependencies of brew on ubuntu 16
$ sudo apt-get install build-essential curl file python-setuptools ruby

$ # Install brew on ubuntu 16
$ ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Linuxbrew/install/master/install)"

$ # Check install location
$ ls -als /home/linuxbrew/.linuxbrew/

$ # LINUX BREW ENv
$ echo '# LINUX BREW Env' >> ~/.profile
$ echo 'export LINUXBREW_HOME="/home/linuxbrew/.linuxbrew"' >> ~/.profile
$ echo 'export PATH="$PATH:$LINUXBREW_HOME/bin"' >> ~/.profile
$ echo 'export MANPATH="$MANPATH:$LINUXBREW_HOME/share/man"' >> ~/.profile
$ echo 'export INFOPATH="$INFOPATH:$LINUXBREW_HOME/share/info"' >> ~/.profile

$ tail -5 ~/.profile
# LINUX BREW Env
export LINUXBREW_HOME="/home/linuxbrew/.linuxbrew"
export PATH="$PATH:$LINUXBREW_HOME/bin"
export MANPATH="$MANPATH:$LINUXBREW_HOME/share/man"
export INFOPATH="$INFOPATH:$LINUXBREW_HOME/share/info"

$ source ~/.profile
$ echo $LINUXBREW_HOME 
/home/linuxbrew/.linuxbrew
$ which brew
/home/linuxbrew/.linuxbrew/bin/brew
```

# Install SpringBoot by brew on Linux
```{bash}
$ brew install gcc

$ brew update
$ brew tap pivotal/tap
$ brew install springboot

$ echo '# SPRINT BOOT Enb' >> ~/.profile
$ echo 'export SPRINGBOOT_HOME="/home/linuxbrew/.linuxbrew/Cellar/springboot/1.5.4.RELEASE"' >> ~/.profile
$ echo 'export PATH="$PATH:$SPRINGBOOT_HOME/bin"' >> ~/.profile

$ tail -3 ~/.profile 
# SPRINT BOOT Env
export SPRINGBOOT_HOME="/home/linuxbrew/.linuxbrew/Cellar/springboot/1.5.4.RELEASE"
export PATH="$PATH:$SPRINGBOOT_HOME/bin"

$ source ~/.profile
$ echo $SPRINGBOOT_HOME
/home/linuxbrew/.linuxbrew/Cellar/springboot/1.5.4.RELEASE
$ which spring
/home/linuxbrew/.linuxbrew/bin/spring
$ spring --version
Spring CLI v1.5.4.RELEASE
```
