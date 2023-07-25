FROM maven:3.9.3-eclipse-temurin-17

ARG DEBIAN_FRONTEND=noninteractive

RUN apt-get update \
 && apt-get -y install build-essential procps curl file git unzip zip curl sed

RUN localedef -i en_US -f UTF-8 en_US.UTF-8

RUN useradd -m -s /bin/bash linuxbrew \
 && echo 'linuxbrew ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers \
 && su - linuxbrew -c 'mkdir ~/.linuxbrew'

USER linuxbrew
RUN bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

USER root
ENV PATH="/home/linuxbrew/.linuxbrew/bin:${PATH}"
RUN git config --global --add safe.directory /home/linuxbrew/.linuxbrew/Homebrew \
 && brew install hello \
 && brew list

RUN rm /bin/sh \
 && ln -s /bin/bash /bin/sh
RUN curl -s https://get.sdkman.io | bash \
 && (echo; echo 'source $HOME/.sdkman/bin/sdkman-init.sh') >> /root/.profile \
 && cat /root/.profile \
 && source /root/.profile \
 && sdk version
