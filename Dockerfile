FROM maven:3.9.9-eclipse-temurin-21

ARG DEBIAN_FRONTEND=noninteractive

RUN apt-get update \
 && apt-get -y install build-essential procps curl file git unzip zip curl sed tree jq sudo

RUN localedef -i en_US -f UTF-8 en_US.UTF-8

RUN useradd -m -s /bin/bash linuxbrew \
 && usermod -aG sudo linuxbrew # echo 'linuxbrew ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers \
 && mkdir -p /home/linuxbrew/.linuxbrew \
 && chown -R linuxbrew: /home/linuxbrew/.linuxbrew
 
USER linuxbrew
RUN /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

ENV PATH="/home/linuxbrew/.linuxbrew/bin:${PATH}"
RUN git config --global --add safe.directory /home/linuxbrew/.linuxbrew/Homebrew

RUN brew doctor \
 && brew update \
 && brew install hello \
 && brew list

USER root
RUN rm /bin/sh \
 && ln -s /bin/bash /bin/sh

RUN curl -s https://get.sdkman.io | bash \
 && (echo; echo 'source $HOME/.sdkman/bin/sdkman-init.sh') >> /root/.profile \
 && cat /root/.profile \
 && source /root/.profile \
 && sdk version
