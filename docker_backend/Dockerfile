FROM debian:latest

RUN apt-get update && \
  apt-get install -yq --no-install-recommends \
    curl \
    bzip2 \
    ca-certificates \
    libgtk2.0-0

ENV MINICONDA_VERSION 4.5.4
ENV PATH=/opt/conda/bin:$PATH
RUN cd /tmp && \
    curl -sSL https://repo.continuum.io/miniconda/Miniconda3-${MINICONDA_VERSION}-Linux-x86_64.sh -o /tmp/miniconda.sh && \
    echo "a946ea1d0c4a642ddf0c3a26a18bb16d *miniconda.sh" | md5sum -c - && \
    /bin/bash miniconda.sh -f -b -p /opt/conda && \
    rm miniconda.sh && \
    /opt/conda/bin/conda config --system --prepend channels conda-forge && \
    /opt/conda/bin/conda config --system --set auto_update_conda false && \
    /opt/conda/bin/conda config --system --set show_channel_urls true && \
    /opt/conda/bin/conda install --quiet --yes conda="${MINICONDA_VERSION%.*}.*" && \
    /opt/conda/bin/conda update --all --quiet --yes && \
    conda install -c conda-forge earthengine-api && \
    conda install nodejs git &&\
    apt-get -qq -y remove curl bzip2 && \
    apt-get -qq -y autoremove && \
    apt-get autoclean && \
    rm -rf /var/lib/apt/lists/* /var/log/dpkg.log && \
    conda clean --all --yes


ADD python_envs/environment_p2.yml /tmp/environment_p2.yml
ADD python_envs/environment_p3.yml /tmp/environment_p3.yml

RUN conda env create -f /tmp/environment_p2.yml && \
    conda env create -f /tmp/environment_p3.yml && \
    conda clean --all --yes
    
WORKDIR /farmero_nodejs_pca

# clean
RUN apt-get autoremove -y && apt-get clean && \
    rm -rf /usr/local/src/*

COPY package-lock.json /farmero_nodejs_pca/
COPY package.json /farmero_nodejs_pca/
COPY scripts /farmero_nodejs_pca/scripts
COPY server.js /farmero_nodejs_pca/

RUN npm install .


EXPOSE 3000

CMD [ "npm", "start"]
