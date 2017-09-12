# Install Linux Brew on Linux
```{bash}
$ # Dependencies of brew on ubuntu 16
$ sudo apt-get install build-essential curl file python-setuptools ruby

$ # Install brew on ubuntu 16
$ ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Linuxbrew/install/master/install)"

$ # Check install location
$ ls -als /home/linuxbrew/.linuxbrew/

$ # LINUX BREW ENv
$ echo '# LINUX BREW Env' >> ~/.bashrc
$ echo 'export LINUXBREW_HOME="/home/linuxbrew/.linuxbrew"' >> ~/.bashrc
$ echo 'export PATH="$PATH:$LINUXBREW_HOME/bin"' >> ~/.bashrc
$ echo 'export MANPATH="$MANPATH:$LINUXBREW_HOME/share/man"' >> ~/.bashrc
$ echo 'export INFOPATH="$INFOPATH:$LINUXBREW_HOME/share/info"' >> ~/.bashrc

$ tail -5 ~/.bashrc
# LINUX BREW Env
export LINUXBREW_HOME="/home/linuxbrew/.linuxbrew"
export PATH="$PATH:$LINUXBREW_HOME/bin"
export MANPATH="$MANPATH:$LINUXBREW_HOME/share/man"
export INFOPATH="$INFOPATH:$LINUXBREW_HOME/share/info"

$ source ~/.bashrc

$ echo $LINUXBREW_HOME 
/home/linuxbrew/.linuxbrew

$ which brew
/home/linuxbrew/.linuxbrew/bin/brew

$ brew --version
Homebrew 1.3.1
Homebrew/homebrew-core (git revision 64d8; last commit 2017-09-09)
```

# Install SpringBoot by brew on Linux
```{bash}
$ brew install gcc

$ brew update
$ brew tap pivotal/tap
$ brew install springboot

$ echo '# SPRING BOOT Env' >> ~/.bashrc
$ echo 'export SPRINGBOOT_HOME="/home/linuxbrew/.linuxbrew/Cellar/springboot/1.5.4.RELEASE"' >> ~/.bashrc
$ echo 'export PATH="$PATH:$SPRINGBOOT_HOME/bin"' >> ~/.bashrc

$ tail -3 ~/.bashrc 
# SPRING BOOT Env
export SPRINGBOOT_HOME="/home/linuxbrew/.linuxbrew/Cellar/springboot/1.5.4.RELEASE"
export PATH="$PATH:$SPRINGBOOT_HOME/bin"

$ source ~/.bashrc

$ echo $SPRINGBOOT_HOME
/home/linuxbrew/.linuxbrew/Cellar/springboot/1.5.4.RELEASE

$ which spring
/home/linuxbrew/.linuxbrew/bin/spring

$ spring --version
Spring CLI v1.5.4.RELEASE
```
